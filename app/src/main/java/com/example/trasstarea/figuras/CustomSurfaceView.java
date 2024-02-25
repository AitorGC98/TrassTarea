package com.example.trasstarea.figuras;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    private List<Shape> shapes;
    private int screenWidth;
    private int screenHeight;
    private static final int NUM_CIRCLES = 3;
    private static final int NUM_SQUARES = 3;
    private static final int NUM_STARS = 3;
    private static final int NUM_TRIANGLES = 3;

    private static final int SHAPE_SIZE = 100; // Tamaño fijo para todos los shapes

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        shapes = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();

        // Crear círculos
        for (int i = 0; i < NUM_CIRCLES; i++) {
            shapes.add(new Circle(screenWidth, screenHeight, SHAPE_SIZE));
        }

        // Crear cuadrados
        for (int i = 0; i < NUM_SQUARES; i++) {
            shapes.add(new Square(screenWidth, screenHeight, SHAPE_SIZE));
        }

        // Crear estrellas
        for (int i = 0; i < NUM_STARS; i++) {
            shapes.add(new Star(screenWidth, screenHeight, SHAPE_SIZE));
        }

        // Crear triángulos
        for (int i = 0; i < NUM_TRIANGLES; i++) {
            shapes.add(new Triangle(screenWidth, screenHeight, SHAPE_SIZE));
        }

        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // No se requiere implementación
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class DrawThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isRunning;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            isRunning = false;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (isRunning) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        updatePositions();
                        drawShapes(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updatePositions() {
            for (Shape shape : shapes) {
                shape.move();
            }
        }

        private void drawShapes(Canvas canvas) {
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                for (Shape shape : shapes) {
                    paint.setColor(shape.getColor());
                    if (shape instanceof Circle) {
                        canvas.drawCircle(shape.getX(), shape.getY(), ((Circle) shape).getRadius(), paint);
                    } else if (shape instanceof Square) {
                        canvas.drawRect(shape.getX(), shape.getY(), shape.getX() + SHAPE_SIZE, shape.getY() + SHAPE_SIZE, paint);
                    } else if (shape instanceof Star) {
                        drawStar(canvas, shape.getX(), shape.getY(), SHAPE_SIZE, paint);
                    } else if (shape instanceof Triangle) {
                        drawTriangle(canvas, shape.getX(), shape.getY(), SHAPE_SIZE, paint);
                    }
                }
            }
        }
        private void drawTriangle(Canvas canvas, float x, float y, float size, Paint paint) {
            Path path = creaTriangulo((int) x, (int) y, (int) size);
            canvas.drawPath(path, paint);
        }

        public Path creaTriangulo(int x, int y, int lado) {
            Point punto1 = new Point(x + lado / 2, y); // Punto superior
            Point punto2 = new Point(x, y + lado); // Punto inferior izquierdo
            Point punto3 = new Point(x + lado, y + lado); // Punto inferior derecho

            Path path = new Path();
            path.moveTo(punto1.x, punto1.y);
            path.lineTo(punto2.x, punto2.y);
            path.lineTo(punto3.x, punto3.y);
            path.lineTo(punto1.x, punto1.y); // Cerrar el triángulo

            return path;
        }


        private void drawStar(Canvas canvas, float x, float y, float size, Paint paint) {
            Path path = creaEstrella((int) x, (int) y, (int) size);
            canvas.drawPath(path, paint);
        }

        public Path creaEstrella(int x, int y, int radio) {
            Point centro = new Point(x, y);

            //Creamos 10 puntos para trazar la estrella
            Point[] starP = new Point[10];
            //Creamos los puntos utilizando coordenadas polares
            //En este bucle for tenemos dos variables incrementales, la del índice del punto 'i' y la del ángulo
            //que parte de un valor aleatorio entre 0~180 y se irá incrementando en cada iteración 360/(nº de puntos)
            for (int i = 0, angulo = -90; i < starP.length; i++, angulo += 360 / starP.length) {
                if (i % 2 == 0) //Los puntos pares tendrán un módulo 'radio' (puntas de la estrella)
                    starP[i] = polarToRect(radio, angulo);
                else //Los puntos impares tendrán un módulo 'radio/2' (puntos interiores de la estrella)
                    starP[i] = polarToRect(radio / 2, angulo);
            }
            //Creamos el Path: desde el punto 0 vamos creando líneas hasta cerrar la forma
            Path star = new Path();
            star.moveTo(starP[0].x + centro.x, starP[0].y + centro.y);
            for (int i = 1; i < starP.length; i++)
                star.lineTo(starP[i].x + centro.x, starP[i].y + centro.y);
            star.lineTo(starP[0].x + centro.x, starP[0].y + centro.y); //Última línea para cerrar la estrella
            return star;
        }

        private Point polarToRect(int radius, int angle) {
            // Convertimos coordenadas polares a coordenadas cartesianas
            double x = radius * Math.cos(Math.toRadians(angle));
            double y = radius * Math.sin(Math.toRadians(angle));
            return new Point((int) x, (int) y);
        }

    }

    abstract class Shape {
        protected float x;
        protected float y;
        protected float dx;
        protected float dy;
        protected int screenWidth;
        protected int screenHeight;
        protected Random random;

        public Shape(int screenWidth, int screenHeight, int shapeSize) {
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            random = new Random();
            x = random.nextInt(screenWidth - shapeSize);
            y = random.nextInt(screenHeight - shapeSize);
            dx = random.nextFloat() * 10 - 5; // Velocidad en dirección x
            dy = random.nextFloat() * 10 - 5; // Velocidad en dirección y
        }

        public abstract void move();

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public abstract int getColor();
    }

    class Circle extends Shape {
        private float radius;

        public Circle(int screenWidth, int screenHeight, int shapeSize) {
            super(screenWidth, screenHeight, shapeSize);
            radius = shapeSize / 2;
        }

        public float getRadius() {
            return radius;
        }

        @Override
        public void move() {
            x += dx;
            y += dy;
            if (x < radius) {
                x = radius;
                dx = -dx;
            } else if (x > screenWidth - radius) {
                x = screenWidth - radius;
                dx = -dx;
            }
            if (y < radius) {
                y = radius;
                dy = -dy;
            } else if (y > screenHeight - radius) {
                y = screenHeight - radius;
                dy = -dy;
            }
        }

        @Override
        public int getColor() {
            // Define los componentes de color RGB para un marrón más oscuro
            int red = 139; // Valor de rojo
            int green = 69; // Valor de verde (menos que el anterior)
            int blue = 19; // Valor de azul (menos que el anterior)

            // Combina los componentes de color para obtener el marrón más oscuro
            int color = Color.rgb(red, green, blue);

            return color;
        }
    }

    class Square extends Shape {
        private float size;

        public Square(int screenWidth, int screenHeight, int shapeSize) {
            super(screenWidth, screenHeight, shapeSize);
            size = shapeSize;
        }

        public float getSize() {
            return size;
        }

        @Override
        public void move() {
            x += dx;
            y += dy;
            if (x < 0) {
                x = 0;
                dx = -dx;
            } else if (x > screenWidth - size) {
                x = screenWidth - size;
                dx = -dx;
            }
            if (y < 0) {
                y = 0;
                dy = -dy;
            } else if (y > screenHeight - size) {
                y = screenHeight - size;
                dy = -dy;
            }
        }

        @Override
        public int getColor() {
            // Define los componentes de color RGB para un marrón más oscuro
            int red = 139; // Valor de rojo
            int green = 69; // Valor de verde (menos que el anterior)
            int blue = 19; // Valor de azul (menos que el anterior)

            // Combina los componentes de color para obtener el marrón más oscuro
            int color = Color.rgb(red, green, blue);

            return color;
        }
    }

    class Star extends Shape {
        private float size;

        public Star(int screenWidth, int screenHeight, int shapeSize) {
            super(screenWidth, screenHeight, shapeSize);
            size = shapeSize;
        }

        public float getSize() {
            return size;
        }

        @Override
        public void move() {
            x += dx;
            y += dy;
            if (x < 100) {
                x = 100;
                dx = -dx;
            } else if (x > screenWidth - size) {
                x = screenWidth - size;
                dx = -dx;
            }
            if (y < 100) {
                y = 100;
                dy = -dy;
            } else if (y > screenHeight - size) {
                y = screenHeight - size;
                dy = -dy;
            }
        }


        @Override
        public int getColor() {
            // Define los componentes de color RGB para un marrón más oscuro
            int red = 139; // Valor de rojo
            int green = 69; // Valor de verde (menos que el anterior)
            int blue = 19; // Valor de azul (menos que el anterior)

            // Combina los componentes de color para obtener el marrón más oscuro
            int color = Color.rgb(red, green, blue);

            return color;
        }
    }

    // Clase Point simple para representar puntos (x, y)
    class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Triangle extends Shape {
        private float sideLength;

        public Triangle(int screenWidth, int screenHeight, int shapeSize) {
            super(screenWidth, screenHeight, shapeSize);
            sideLength = shapeSize;
        }

        public float getSideLength() {
            return sideLength;
        }

        @Override
        public void move() {
            x += dx;
            y += dy;
            if (x < 0) {
                x = 0;
                dx = -dx;
            } else if (x > screenWidth - sideLength) {
                x = screenWidth - sideLength;
                dx = -dx;
            }
            if (y < 0) {
                y = 0;
                dy = -dy;
            } else if (y > screenHeight - sideLength) {
                y = screenHeight - sideLength;
                dy = -dy;
            }
        }

        @Override
        public int getColor() {
            // Define los componentes de color RGB para un marrón más oscuro
            int red = 139; // Valor de rojo
            int green = 69; // Valor de verde (menos que el anterior)
            int blue = 19; // Valor de azul (menos que el anterior)

            // Combina los componentes de color para obtener el marrón más oscuro
            int color = Color.rgb(red, green, blue);

            return color;
        }
    }
}




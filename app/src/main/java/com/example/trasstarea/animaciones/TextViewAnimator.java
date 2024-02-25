package com.example.trasstarea.animaciones;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import androidx.annotation.RequiresApi;

import android.widget.TextView;

import java.lang.ref.WeakReference;

public class TextViewAnimator {


    private TextValueAnimator textValueAnimator;


    public static TextViewAnimator perLetter(TextView textView){

        int steps = textView.getText().length();
        TextViewAnimator textViewAnimator =
                new TextViewAnimator(textView,
                        new TextEvaluatorPerLetter(),
                        new TextInterpolator(steps));
        return textViewAnimator;
    }

    public static TextViewAnimator perWord(TextView textView){

        int steps = textView.getText().toString().split(" ").length;

        TextViewAnimator textViewAnimator =
                new TextViewAnimator(textView, new TextEvaluatorPerWord(), new TextInterpolator(steps));
        return textViewAnimator;
    }

    public TextViewAnimator(TextView textView, TypeEvaluator typeEvaluator, TextInterpolator textInterpolator){

        this.textValueAnimator = new TextValueAnimator(textView, textView.getText().toString());
        textValueAnimator.setEvaluator(typeEvaluator);
        textValueAnimator.setInterpolator(textInterpolator);
    }

    private static class TextValueAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {

        private WeakReference<TextView> weakTextView;

        public TextValueAnimator(TextView textView, String text) {

            weakTextView = new WeakReference<>(textView);
            setObjectValues(text);
            addUpdateListener(this);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            String text = (String) animation.getAnimatedValue();
            TextView textView = weakTextView.get();
            if(textView != null) {
                textView.setText(text);
            }
        }
    }

    private static class TextEvaluatorPerLetter implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            int step = (int) fraction;
            return ((String) endValue).substring(0, step);
        }
    }

    private static class TextEvaluatorPerWord implements TypeEvaluator {

        private String[] words;
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {

            int step = (int) fraction;
            if(words == null){
                words = ((String) endValue).split(" ");
            }
            String textAtStep = "";
            for (int i = 1; i <= step; i++) {
                textAtStep += words[i-1] + " ";
            }

            return textAtStep;
        }
    }

    private static class TextInterpolator implements TimeInterpolator {

        private int steps;
        public TextInterpolator(int steps) {

            this.steps = steps;
        }
        @Override
        public float getInterpolation(float input) {
            return input * steps;
        }
    }

    public void start(){
        textValueAnimator.start();
    }
    public void cancel(){
        textValueAnimator.cancel();
    }
    public void end(){
        textValueAnimator.end();
    }

    @RequiresApi(19)
    public void pause(){
        textValueAnimator.pause();
    }
    @RequiresApi(19)
    public void resume(){
        textValueAnimator.resume();
    }
    @RequiresApi(19)
    public boolean isStarted(){
        return textValueAnimator.isStarted();
    }
    @RequiresApi(19)
    public float getAnimatedFraction(){
        return textValueAnimator.getAnimatedFraction();
    }
    public void setRepeatCount(int value){
        textValueAnimator.setRepeatCount(value);
    }
    public void setRepeatMode(int repeatMode){
        textValueAnimator.setRepeatMode(repeatMode);
    }
    public void setDuration(long duration){
        textValueAnimator.setDuration(duration);
    }
    public void setStartDelay(long startDelay){
        textValueAnimator.setStartDelay(startDelay);
    }
    public void addUpdateListener(ValueAnimator.AnimatorUpdateListener listener){
        textValueAnimator.addUpdateListener(listener);
    }
    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener listener){
        textValueAnimator.removeUpdateListener(listener);
    }
    public boolean isRunning(){
        return textValueAnimator.isRunning();
    }
    public void addListener(Animator.AnimatorListener listener){
        textValueAnimator.addListener(listener);
    }
    public void removeListener(Animator.AnimatorListener listener){
        textValueAnimator.removeListener(listener);
    }

}

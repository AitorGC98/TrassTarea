package com.example.trasstarea.Fragmentos;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trasstarea.R;

public class FragmentPDF extends Fragment {

    private static final String ARG_PDF_URL = "pdf_url";
    private String pdfUrl;
    private WebView webView;
    private ProgressBar progressBar;

    public FragmentPDF() {
        // Required empty public constructor
    }

    public static FragmentPDF newInstance(String pdfUrl) {
        FragmentPDF fragment = new FragmentPDF();
        Bundle args = new Bundle();
        args.putString(ARG_PDF_URL, pdfUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pdfUrl = getArguments().getString(ARG_PDF_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pdf_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        setupWebView();
        loadPdf();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadPdf() {
        if (!TextUtils.isEmpty(pdfUrl)) {
            String url = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;
            webView.loadUrl(url);
        } else {
            Toast.makeText(getContext(), "URL del PDF vacía", Toast.LENGTH_SHORT).show();
        }
    }
}
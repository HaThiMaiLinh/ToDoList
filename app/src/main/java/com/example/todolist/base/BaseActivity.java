package com.example.todolist.base;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    final int PERMISSION_REQUEST_CODE = 112;
    protected T binding;

    protected abstract T setViewBinding();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        binding = setViewBinding();
        setContentView(binding.getRoot());
        initData();
        initView();
        initListener();
    }

    private void initWindow() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        }else{
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

       /* Drawable background = new ColorDrawable(Color.parseColor("#FFFFFF"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWindow();
    }

    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                Log.d("TAG", "getNotificationPermission: request");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception ignored) {
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            fullScreenImmersive(getWindow());
        }
    }

    public void fullScreenImmersive(Window window) {
        if (window != null) {
            fullScreenImmersive(window.getDecorView());
        }
    }

    public void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        }else{
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

    }
}

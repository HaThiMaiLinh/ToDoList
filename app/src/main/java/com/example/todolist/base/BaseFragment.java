package com.example.todolist.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {

    protected T binding;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void addDispose(Disposable disposable) {
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = getBinding(inflater, container);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    requireActivity().finish();
                }
            }
        });
        initView();
    }

    protected abstract T getBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView();

    protected void addAndRemoveCurrentFragment(Fragment currentFragment, Fragment newFragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(currentFragment);
        transaction.add(android.R.id.content, newFragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected void setColorStatusBar(int idColor) {
        if (getActivity() != null) {
            ((BaseActivity<?>) getActivity()).getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), idColor));
        }
    }

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().getWindow().setDecorFitsSystemWindows(false);
            requireActivity().getWindow().getInsetsController().hide(
                    android.view.WindowInsets.Type.navigationBars());
            requireActivity().getWindow().getInsetsController().setSystemBarsBehavior(
                    android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        } else {
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    public static boolean isGoToSetting = false;

    public static <F extends Fragment> F newInstance(Class<F> fragment, Bundle args) {
        F f;
        try {
            f = fragment.newInstance();
            if (args != null) {
                f.setArguments(args);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            f = null;
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
        return f;
    }
}

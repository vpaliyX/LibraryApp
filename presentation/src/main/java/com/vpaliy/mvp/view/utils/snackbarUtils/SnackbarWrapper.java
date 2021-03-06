package com.vpaliy.mvp.view.utils.snackbarUtils;


import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.vpaliy.mvp.R;

public class SnackbarWrapper {

    @NonNull
    private final Snackbar snackbar;


    @Nullable
    private Dismiss dismissAction;

    @Nullable
    private Perform performAction;

    private int color;

    private SnackbarWrapper(@NonNull View root, int duration) {
        this(root,"",duration);
    }

    private SnackbarWrapper(@NonNull View root, @StringRes int resId, int duration) {
        this.snackbar=Snackbar.make(root,resId,duration);
        color=root.getResources().getColor(R.color.colorAccent);
    }

    private SnackbarWrapper(@NonNull View root, String text, int duration) {
        this.snackbar=Snackbar.make(root,text,duration);
        color=root.getResources().getColor(R.color.colorAccent);
    }

    public static SnackbarWrapper start(@NonNull View root, int duration) {
        return new SnackbarWrapper(root,duration);
    }

    public static SnackbarWrapper start(@NonNull View root, @NonNull String text, int duration) {
        return new SnackbarWrapper(root,text,duration);
    }

    public static SnackbarWrapper start(@NonNull View root, @StringRes int resId, int duration) {
        return new SnackbarWrapper(root,resId,duration);
    }

    public SnackbarWrapper duration(int duration) {
        this.snackbar.setDuration(duration);
        return this;
    }

    public SnackbarWrapper text(@NonNull String text) {
        this.snackbar.setText(text);
        return this;
    }

    public SnackbarWrapper text(@StringRes int resId) {
        this.snackbar.setText(resId);
        return this;
    }


    public SnackbarWrapper dismissCallback(@Nullable final Dismiss dismissAction){
        this.dismissAction=dismissAction;
        return this;
    }

    public SnackbarWrapper performCallback(@Nullable final Perform performAction){
        this.performAction=performAction;
        return this;
    }

    public SnackbarWrapper color(int color) {
        this.color=color;
        return this;
    }

    public void show() {
        if(dismissAction!=null||performAction!=null) {
         //   snackbar.setAction(actionCallback.message, v -> actionCallback.onCancel());
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                   if(dismissAction!=null){
                       dismissAction.execute();
                   }
                    switch (event) {
                        case DISMISS_EVENT_TIMEOUT:
                        case DISMISS_EVENT_SWIPE:
                            if(performAction!=null){
                                performAction.execute();
                            }
                            break;
                    }
                }
            });
        }
        snackbar.setActionTextColor(color);
        snackbar.show();
    }

    public Snackbar showAndGet() {
        show();
        return snackbar;
    }

}
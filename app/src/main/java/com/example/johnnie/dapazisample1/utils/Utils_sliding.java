package com.example.johnnie.dapazisample1.utils;

import android.graphics.Outline;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.animation.PathInterpolator;

import com.example.johnnie.dapazisample1.R;

public class Utils_sliding {

    public static void configureWindowEnterExitTransition (Window w) {

        Explode ex = new Explode();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }

    public static void configureFab (View fabButton) {

        int fabSize = fabButton.getContext().getResources()
            .getDimensionPixelSize(R.dimen.fab_size);

        Outline fabOutLine = new Outline();
        fabOutLine.setOval(0, 0, fabSize, fabSize);
    }
}

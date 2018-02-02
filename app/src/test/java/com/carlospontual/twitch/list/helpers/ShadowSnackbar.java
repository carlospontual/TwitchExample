package com.carlospontual.twitch.list.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ShadowSnackbar borrowed from
 * https://medium.com/@jotaemepereira/android-tests-with-roboelectric-snackbarshadow-update-71681bc59e0f
 */

@SuppressWarnings({"UnusedDeclaration", "Unchecked"})
@Implements(Snackbar.class)
public class ShadowSnackbar {

    private static List<ShadowSnackbar> shadowSnackbars = new ArrayList<>();

    @RealObject
    private Snackbar snackbar;

    String text;
    private int duration;
    private int gravity;
    private int xOffset;
    private int yOffset;
    private View view;

    @Implementation
    private static Snackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = null;

        try {
            Constructor<Snackbar> constructor = Snackbar.class.getDeclaredConstructor(ViewGroup.class, View.class, BaseTransientBottomBar.ContentViewCallback.class);

            if (null == constructor)
                throw new IllegalArgumentException("Seems like the constructor was not found!");


            if (Modifier.isPrivate(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }

            ViewGroup parent = findSuitableParent(view);
            final SnackbarContentLayout content =
                    (SnackbarContentLayout)
                            LayoutInflater.from(parent.getContext()).inflate(android.support.design.R.layout.design_layout_snackbar_include, parent, false);

            snackbar = constructor.newInstance(parent, content, content);
            snackbar.setText(text);
            snackbar.setDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        shadowOf(snackbar).text = text.toString();

        shadowSnackbars.add(shadowOf(snackbar));

        return snackbar;
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                final ViewParent parent = view.getParent();

                if (parent == null) {
                    fallback = new FrameLayout(view.getContext());
                }

                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }

    @Implementation
    public static Snackbar make(@NonNull View view, @StringRes int resId, int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }


    private static ShadowSnackbar shadowOf(Snackbar bar) {
        return Shadow.extract(bar);
    }

    public static void reset() {
        shadowSnackbars.clear();
    }

    public static int shownSnackbarCount() {
        return shadowSnackbars.isEmpty() ? 0 : shadowSnackbars.size();

    }

    public static String getTextOfLatestSnackbar() {
        if (!shadowSnackbars.isEmpty())
            return shadowSnackbars.get(shadowSnackbars.size() - 1).text;

        return null;
    }

    public static Snackbar getLatestSnackbar() {
        if (!shadowSnackbars.isEmpty())
            return shadowSnackbars.get(shadowSnackbars.size() - 1).snackbar;

        return null;
    }
}
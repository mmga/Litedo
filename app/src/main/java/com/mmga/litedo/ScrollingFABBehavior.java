package com.mmga.litedo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public boolean isAnimating;
    FloatingActionButton fab;

    public ScrollingFABBehavior() {
    }

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
        this.fab = child;
        if (dy > 20 && !isAnimating && child.getVisibility() == View.VISIBLE) {
            Log.d("mmga", "dy1 = " + dy);
            hide();
            isAnimating = true;
        } else if (dy < 0 && !isAnimating && child.getVisibility() == View.GONE) {
            Log.d("mmga", "dy2 = " + dy);
            show();
            isAnimating = true;
        }
    }



    private void hide() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        int distanceToScroll = fab.getHeight() + fabBottomMargin;
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab, "translationY", 0, distanceToScroll);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setVisibility(View.GONE);
                isAnimating = false;
            }
        });
        animator.start();
    }

    public void show() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        int distanceToScroll = fab.getHeight() + fabBottomMargin;
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab, "translationY", distanceToScroll, 0);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fab.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }
        });
        animator.start();

    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        return dependency instanceof PtrFrameLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        Log.d("mmga", "behavior");
//        int distanceToScroll = child.getHeight() + fabBottomMargin;
//        int offset = ((PtrFrameLayout) dependency).;
//        float ratio = offset / DensityUtil.dip2px(MyApplication.getContext(), 10);
//        Log.d("mmga", "ratio = " + ratio);
//        Log.d("mmga", "offset = " + offset);
//        child.setTranslationY(-distanceToScroll * ratio);
//
//        return true;
//
//    }
}

//package com.mmga.litedo.widget;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.support.design.widget.FloatingActionButton;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import com.mmga.litedo.MyApplication;
//import com.mmga.litedo.R;
//import com.mmga.litedo.Util.DensityUtil;
//
///**
// * Created by mmga on 2015/12/14.
// */
//public class FabHelper {
//    static ObjectAnimator animator;
//    static FloatingActionButton fab;
//    private static boolean isAnimating;
//    private static final int distanceToScroll =DensityUtil.dip2px(MyApplication.getContext(),72);
//
//    public FabHelper() {
//
//        fab = (FloatingActionButton) LayoutInflater
//                .from(MyApplication.getContext()).inflate(R.layout.activity_main, null, true)
//                .findViewById(R.id.fab_add);
//        animator = new ObjectAnimator();
//        animator.setDuration(200);
//
//    }
//
//    public static void hide() {
//        animator.ofFloat(fab, "translationY", 0, distanceToScroll);
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                fab.setVisibility(View.GONE);
//                isAnimating = false;
//            }
//        });
//        animator.start();
//    }
//
//    public static void show() {
//        animator.ofFloat(fab, "translationY", distanceToScroll, 0);
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                fab.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                isAnimating = false;
//            }
//        });
//        animator.start();
//    }
//}

package com.alejandro_castilla.heartratetest.floatingview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.alejandro_castilla.heartratetest.R;


/**
 * Created by yasar on 12/5/18.
 */

public class FloatingViewService extends Service implements View.OnClickListener {

    private static final String TAG = "FloatingViewService";
    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;

    private int clickCount = 0;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);

        //adding click listener to close button and expanded view
//        mFloatingView.findViewById(R.id.collapsed_iv).setOnClickListener(this);

//        flipView = (FlipView) mFloatingView.findViewById(R.id.collapsed_iv);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean isMove = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
//                        collapsedView.setVisibility(View.GONE);
//                        expandedView.setVisibility(View.VISIBLE);

                        if (!isMove) {
                            Log.e(TAG, "onTouch: ");

//                            flipView.flip(true);

                            clickCount++;

                            changeColor(mFloatingView, clickCount);

//                            reInitializeCount();

                        }

                        isMove = false;

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        isMove = true;
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    private void changeColor(final View mFlipView, final int clickCount1) {

        final View view = mFlipView.findViewById(R.id.collapsed_iv);
//        final View viewGreen = mFlipView.findViewById(R.id.collapsed_iv1);
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();


        if (clickCount1 == 1) {
            reInitializeCount();
            bgShape.setColor(ContextCompat.getColor(this, R.color.yellow));

        } else if (clickCount1 == 2) {

            bgShape.setColor(ContextCompat.getColor(this, R.color.orange));

        } else if (clickCount1 == 3) {

            if (handler != null) {
                handler.removeCallbacks(runnable);
            }

            bgShape.setColor(ContextCompat.getColor(this, R.color.green));
            clickCount = 0;
            view.setVisibility(View.GONE);
            mFlipView.findViewById(R.id.collapsed_iv1).setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    clickCount = 0;
                    view.setVisibility(View.VISIBLE);
                    mFlipView.findViewById(R.id.collapsed_iv1).setVisibility(View.GONE);
                    changeColor(mFlipView, clickCount);
                }
            }, 500);

        } else {

            bgShape.setColor(ContextCompat.getColor(this, R.color.colorAccent));

        }
    }

    private void reInitializeCount() {

        handler.postDelayed(runnable, 3000);


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            clickCount = 0;

            changeColor(mFloatingView, clickCount);
        }
    };

    private Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.collapsed_iv:

                Log.e(TAG, "onClick: ");

                break;

//            case R.id.buttonClose:
//                //closing the widget
//                stopSelf();
//                break;
        }
    }
}
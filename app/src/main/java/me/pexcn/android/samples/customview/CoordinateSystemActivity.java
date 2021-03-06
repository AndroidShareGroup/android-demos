package me.pexcn.android.samples.customview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.pexcn.android.samples.R;
import me.pexcn.android.samples.base.BaseActivity;

/**
 * Created by pexcn on 2017-03-17.
 */
public class CoordinateSystemActivity extends BaseActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayout mLinearLayout;
    private TextView mGetX;
    private TextView mGetY;
    private TextView mGetRawX;
    private TextView mGetRawY;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customview_coordinate_system;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mLinearLayout = (LinearLayout) findViewById(R.id.ll);
        mGetX = (TextView) findViewById(R.id.getX);
        mGetY = (TextView) findViewById(R.id.getY);
        mGetRawX = (TextView) findViewById(R.id.getRawX);
        mGetRawY = (TextView) findViewById(R.id.getRawY);

        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        mGetX.setText("getX() -> " + String.valueOf(event.getX()));
                        mGetY.setText("getY() -> " + String.valueOf(event.getY()));
                        mGetRawX.setText("getRawX() -> " + String.valueOf(event.getRawX()));
                        mGetRawY.setText("getRawY() -> " + String.valueOf(event.getRawY()));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected boolean isSubActivity() {
        return true;
    }
}

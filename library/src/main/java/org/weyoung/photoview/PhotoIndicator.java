package org.weyoung.photoview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class PhotoIndicator extends FrameLayout{

    private Indicator indicator;
    private static int SPACE = 12;
    private static int POINT_SIZE = 3;
    private int max = 0;
    PointF selectedPoint = new PointF();

    public PhotoIndicator(Context context) {
        this(context, null);
    }

    public PhotoIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        indicator = new Indicator(context);
        addView(indicator);
    }

    public void setMaxIndex(int index) {
        max = index;
        indicator.invalidate();
    }

    public void setCurrentIndex(int index) {
        selectedPoint.set(SPACE/2 + SPACE * index, SPACE/2);
        indicator.invalidate();
    }

    public void setPinPosition(int position, float offset, int direction) {
        int start = SPACE / 2 + SPACE * (direction == -1 ? position-- : position);
        float delta = Math.abs(direction) * offset * SPACE;
        selectedPoint.x = start + delta;
        indicator.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(max * SPACE + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(SPACE + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    class Indicator extends View {
        private Paint selected;
        private Paint normal;

        public Indicator(Context context) {
            super(context);
            selected = new Paint();
            selected.setColor(Color.WHITE);
            normal = new Paint();
            normal.setColor(Color.GRAY);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for(int i = 0; i < max ; i++) {
                canvas.drawCircle(SPACE/2 + SPACE * i, SPACE/2, POINT_SIZE, normal);
            }
            canvas.drawCircle(selectedPoint.x, selectedPoint.y, POINT_SIZE, selected);
        }
    }
}

package org.weyoung.photoview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class PhotoIndicator extends FrameLayout{

    private IndicatorView indicator;
    private static int SPACE = 12 * 2;
    private static int POINT_SIZE = 3 * 2;
    private int max = 0;

    public PhotoIndicator(Context context) {
        this(context, null);
    }

    public PhotoIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        indicator = new IndicatorView(context, Color.WHITE, Color.GRAY);
        addView(indicator);
    }

    public void setMaxIndex(int index) {
        max = index;
        indicator.invalidate();
    }

    public void setStyleIndex(int[] styleIndex) {
        indicator.setStyleIndex(styleIndex);
        indicator.invalidate();
    }

    public void setCurrentIndex(int index) {
        indicator.setSelectedPoint(SPACE / 2 + SPACE * index, SPACE / 2);
        indicator.setMovingPosition(index, 0, 0);
        indicator.invalidate();
    }

    public void setPinPosition(int position, float offset, int direction) {
        int start = SPACE / 2 + SPACE * position;
        float delta = Math.abs(direction) * offset * SPACE;
        indicator.setSelectedPointX(start + delta);
        indicator.setMovingPosition(position, offset, direction);
        indicator.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(max * SPACE + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(SPACE + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    class IndicatorView extends View {
        private final Paint selected;
        private final Paint normal;
        private int []styleIndex;

        private PointF selectedPoint = new PointF();
        private int position;
        private int destination;
        private float offset;



        public IndicatorView(Context context, int selected, int normal) {
            super(context);
            this.selected = new Paint();
            this.selected.setColor(selected);
            this.normal = new Paint();
            this.normal.setColor(normal);
        }

        void setSelectedPoint(float x, float y) {
            selectedPoint.set(x, y);
        }

        void setSelectedPointX(float x) {
            selectedPoint.x = x;
        }

        void setMovingPosition(int position, float offset, int direction) {
            this.position = position + (direction == -1 ? 1 : 0);
            this.destination = position + (direction == 1 ? 1 : 0);
            this.offset = direction == -1 ? 1 - offset : offset;
        }

        void setStyleIndex(int[] styleIndex) {
            this.styleIndex = styleIndex;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(max == 1)
                return;
            //static dots
            for (int i = 0; i < max; i++) {
                if (i == position || i == destination)
                    continue;
                if (styleIndex == null || styleIndex[i] == 0) {
                    drawCircle(canvas, i, 1);
                } else if (styleIndex[i] == 1) {
                    drawArrow(canvas, i, 1);
                } else if (styleIndex[i] == 2) {
                    drawRect(canvas, i, 1);
                }
            }
            //shaping dots
            if(destination != position) {
                if (styleIndex == null || styleIndex[destination] == 0) {
                    drawCircle(canvas, destination, 1 - offset);
                } else if (styleIndex[destination] == 1) {
                    drawArrow(canvas, destination, 1 - offset);
                } else if (styleIndex[destination] == 2) {
                    drawRect(canvas, destination, 1 - offset);
                }
                if (styleIndex == null || styleIndex[position] == 0) {
                    drawCircle(canvas, position, offset);
                } else if (styleIndex[position] == 1) {
                    drawArrow(canvas, position, offset);
                } else if (styleIndex[position] == 2) {
                    drawRect(canvas, position, offset);
                }
            }
            //selected dots
            canvas.drawCircle(selectedPoint.x, selectedPoint.y, POINT_SIZE, selected);
        }

        private void drawArrow(Canvas canvas, int position, float offset) {
            Path path = new Path();
            path.moveTo(SPACE / 2 + SPACE * position - POINT_SIZE * offset, SPACE / 2 - POINT_SIZE * offset);
            path.lineTo(SPACE / 2 + SPACE * position - POINT_SIZE * offset, SPACE / 2 + POINT_SIZE * offset);
            path.lineTo(SPACE / 2 + SPACE * position + POINT_SIZE * offset, SPACE / 2);
            path.close();
            canvas.drawPath(path, normal);
        }

        private void drawCircle(Canvas canvas, int position, float scale) {
            canvas.drawCircle(SPACE / 2 + SPACE * position, SPACE / 2, POINT_SIZE * scale, normal);
        }

        private void drawRect(Canvas canvas, int position, float scale) {
            canvas.drawRect(SPACE / 2 + SPACE * position - POINT_SIZE * scale,
                    SPACE / 2 - POINT_SIZE * scale,
                    SPACE / 2 + SPACE * position + POINT_SIZE * scale,
                    SPACE / 2 + POINT_SIZE * scale,
                    normal);
        }
    }
}

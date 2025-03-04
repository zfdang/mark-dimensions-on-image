package com.zfdang.mdoi.ui.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zfdang.mdoi.model.Measure;

import java.util.ArrayList;
import java.util.List;

public class DrawingOverlayView extends View {
    private Paint paint = new Paint();
    private List<Measure> lines = new ArrayList<>();
    private PointF startPoint = new PointF();

    public DrawingOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Measure line : lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
    }

    public interface OnDrawListener {
        void onLineDrawn();
    }

    private OnDrawListener drawListener;

    public void setOnDrawListener(OnDrawListener listener) {
        this.drawListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                lines.add(new Measure(startPoint.x, startPoint.y, event.getX(), event.getY(), 0));
                if(drawListener != null) {
                    drawListener.onLineDrawn();
                }
                invalidate();
                return true;
        }
        return false;
    }

    public void setLines(List<Measure> newLines) {
        lines = new ArrayList<>(newLines);
        invalidate();
    }

    public List<Measure> getLines() {
        return new ArrayList<>(lines);
    }
}
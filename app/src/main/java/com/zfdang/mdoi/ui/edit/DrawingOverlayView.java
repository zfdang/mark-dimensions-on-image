package com.zfdang.mdoi.ui.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zfdang.mdoi.model.Measure;

import java.util.ArrayList;
import java.util.List;

public class DrawingOverlayView extends View {
    private Paint linePaint = new Paint();
    private Paint tickPaint = new Paint();
    private Paint textPaint = new Paint();
    private List<Measure> lines = new ArrayList<>();
    private PointF startPoint = new PointF();
    private PointF currentMovePoint = new PointF(); // For live drawing feedback
    private boolean isDrawing = false; // To distinguish tap from draw
    private static final float TICK_LENGTH = 20f; // Length of the tick marks
    private static final float TICK_INTERVAL = 50f; // Draw a tick every 50 pixels
    private static final float TAP_THRESHOLD = 30f; // Max distance for a tap to be considered a tap, and for line selection
    private OnRulerInteractionListener rulerInteractionListener;

    private Paint labelPaint = new Paint(); // For drawing labels
    private GestureDetector gestureDetector;

    // Interface for ruler interaction
    public interface OnRulerInteractionListener {
        void onRulerClicked(Measure ruler); // For calibration
        void onRulerLongPressed(Measure ruler); // For adding/editing label
    }

    public void setOnRulerInteractionListener(OnRulerInteractionListener listener) {
        this.rulerInteractionListener = listener;
    }

    public DrawingOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaints();
        setupGestureDetector();
    }

    private void setupPaints() {
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(8);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);

        tickPaint.setColor(Color.BLUE);
        tickPaint.setStrokeWidth(4);
        tickPaint.setStyle(Paint.Style.STROKE);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setStyle(Paint.Style.FILL);

        labelPaint.setColor(Color.DKGRAY); // Dark Gray for labels
        labelPaint.setTextSize(28); // Slightly smaller than measurements
        labelPaint.setStyle(Paint.Style.FILL);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // Handle single tap for calibration
                Measure tappedRuler = findTappedRuler(e.getX(), e.getY());
                if (tappedRuler != null && rulerInteractionListener != null) {
                    rulerInteractionListener.onRulerClicked(tappedRuler);
                    invalidate(); // Redraw in case ruler appearance changes (e.g. selection highlight)
                    return true; // Event handled
                }
                return false; // Event not handled, no ruler tapped
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // Handle long press for adding/editing label
                Measure longPressedRuler = findTappedRuler(e.getX(), e.getY()); // findTappedRuler can be reused
                if (longPressedRuler != null && rulerInteractionListener != null) {
                    rulerInteractionListener.onRulerLongPressed(longPressedRuler);
                    invalidate(); // Redraw might be needed if label changes
                }
            }

            @Override
            public boolean onDown(MotionEvent e) {
                startPoint.set(e.getX(), e.getY());
                currentMovePoint.set(e.getX(), e.getY());
                isDrawing = true; // Start drawing
                return true; // Necessary for other gesture events to trigger
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // e1 is the first onDown, e2 is the current move event.
                if (isDrawing) { // Only scroll if we are in drawing mode
                    currentMovePoint.set(e2.getX(), e2.getY());
                    invalidate(); // Redraw to show live line
                    return true; // Event handled
                }
                return false; // Event not handled
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Measure line : lines) {
            // Draw the main line
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, linePaint);

            float dx = line.endX - line.startX;
            float dy = line.endY - line.startY;
            float length = line.pixelLength; // Already calculated

            if (length == 0) continue; // Avoid division by zero for zero-length lines

            // Calculate the unit vector for the line direction
            float ux = dx / length;
            float uy = dy / length;

            // Calculate the perpendicular vector for ticks
            float px = -uy;
            float py = ux;

            // Draw ticks
            for (float i = 0; i <= length; i += TICK_INTERVAL) {
                float currentX = line.startX + i * ux;
                float currentY = line.startY + i * uy;

                // Tick start and end points
                float tickStartX = currentX - px * TICK_LENGTH / 2;
                float tickStartY = currentY - py * TICK_LENGTH / 2;
                float tickEndX = currentX + px * TICK_LENGTH / 2;
                float tickEndY = currentY + py * TICK_LENGTH / 2;
                canvas.drawLine(tickStartX, tickStartY, tickEndX, tickEndY, tickPaint);
            }

            // Display the pixelLength
            // Position the text slightly above the midpoint of the ruler
            float midX = (line.startX + line.endX) / 2;
            float midY = (line.startY + line.endY) / 2;
            // Offset the text slightly perpendicular to the line for better visibility
            float textOffsetX = -py * 30; // 30 pixels offset
            float textOffsetY = px * 30;

            // Adjust text alignment for better readability based on line angle
            if (Math.abs(ux) > Math.abs(uy)) { // Line is more horizontal
                textPaint.setTextAlign(Paint.Align.CENTER);
            } else { // Line is more vertical
                if (uy > 0) { // Points downwards
                    textPaint.setTextAlign(Paint.Align.LEFT);
                } else { // Points upwards
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                }
            }

            String measurementText;
            if (line.actualLength > 0 && line.unit != null && !line.unit.isEmpty()) {
                measurementText = String.format("%.2f %s", line.actualLength, line.unit);
            } else {
                measurementText = String.format("%.0f px", length);
            }
            canvas.drawText(measurementText, midX + textOffsetX, midY + textOffsetY, textPaint);

            // Draw the label if it exists
            if (line.label != null && !line.label.isEmpty()) {
                // Position label below the measurement text
                // Adjust textOffsetX and textOffsetY for label positioning
                // For simplicity, let's put it directly below, centered with the measurement text
                float labelOffsetY = textOffsetY + textPaint.getTextSize() + 5; // 5px padding
                labelPaint.setTextAlign(textPaint.getTextAlign()); // Use same alignment as measurement
                canvas.drawText(line.label, midX + textOffsetX, midY + labelOffsetY, labelPaint);
            }
        }

        // Draw live line if currently drawing
        if (isDrawing) {
            canvas.drawLine(startPoint.x, startPoint.y, currentMovePoint.x, currentMovePoint.y, linePaint);
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
        // First, let the GestureDetector try to handle the event.
        boolean gestureConsumed = gestureDetector.onTouchEvent(event);

        // Handle ACTION_UP for finalizing drawing, as GestureDetector's onScroll/onFling might not cover all drawing end cases.
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isDrawing) { // If we were drawing
                isDrawing = false; // Drawing finished

                float endX = event.getX();
                float endY = event.getY();
                float dx = endX - startPoint.x;
                float dy = endY - startPoint.y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                // Only add line if it's not a tap (distance check)
                // GestureDetector's onSingleTapUp handles taps, so this check here is
                // to prevent drawing a tiny line when a tap occurs that wasn't on a ruler.
                // Or, if the gesture was a long press that did not result in a scroll.
                // The TAP_THRESHOLD helps distinguish between an intended tap and a very short drag.
                if (distance >= TAP_THRESHOLD) {
                    lines.add(new Measure(startPoint.x, startPoint.y, endX, endY, distance));
                    if (drawListener != null) {
                        drawListener.onLineDrawn();
                    }
                }
                invalidate(); // Redraw to finalize or clear drawing preview
                return true; // Event handled
            }
        }
        // If gestureDetector consumed the event, or if ACTION_UP was handled, return true.
        return gestureConsumed || event.getAction() == MotionEvent.ACTION_UP;
    }


    private Measure findTappedRuler(float x, float y) {
        for (Measure ruler : lines) {
            if (isPointNearLine(x, y, ruler, TAP_THRESHOLD)) {
                return ruler;
            }
        }
        return null;
    }

    // Helper method to check if a point (x, y) is near a line segment (ruler)
    // This is a common algorithm: calculate distance from point to line segment
    private boolean isPointNearLine(float px, float py, Measure line, float maxDistance) {
        float x1 = line.startX;
        float y1 = line.startY;
        float x2 = line.endX;
        float y2 = line.endY;

        float dx = x2 - x1;
        float dy = y2 - y1;

        if (dx == 0 && dy == 0) { // Line is a point
            return Math.sqrt(Math.pow(px - x1, 2) + Math.pow(py - y1, 2)) < maxDistance;
        }

        // Calculate projection of point P onto the line L defined by (x1,y1) and (x2,y2)
        // t = [(P-A) . (B-A)] / |B-A|^2
        float t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);

        float closestX, closestY;
        if (t < 0) { // Closest point is A
            closestX = x1;
            closestY = y1;
        } else if (t > 1) { // Closest point is B
            closestX = x2;
            closestY = y2;
        } else { // Closest point is projection P' onto segment AB
            closestX = x1 + t * dx;
            closestY = y1 + t * dy;
        }

        float distanceToLine = (float) Math.sqrt(Math.pow(px - closestX, 2) + Math.pow(py - closestY, 2));
        return distanceToLine < maxDistance;
    }


    public void setLines(List<Measure> newLines) {
        lines = new ArrayList<>(newLines);
        invalidate();
    }

    public List<Measure> getLines() {
        return new ArrayList<>(lines);
    }
}
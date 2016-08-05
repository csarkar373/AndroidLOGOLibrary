package com.westhillcs.androidlogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chandan on 7/29/2016.
 */
public class LogoView extends View {
    //static constants
    private static final int DEFAULT_PEN_COLOR = Color.RED;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final int ROBOT_WIDTH = 50;
    private static final int ROBOT_HEIGHT = 50;
    private static final int INITIAL_HEADING = 0;
    private static final int STEP_SIZE = 20;
    private static final float LINE_THICKNESS = (float)5.0;

    // member variables
    private int mPenColor;
    private boolean mPenDown;
    private boolean mRobotHidden;
    private ArrayList<Path> mPaths;
    private ArrayList<Paint> mPaints;
    private Paint mPaint;
    private int mHeading;
    private float mX;
    private float mY;
    private boolean mClearCanvas;
    private Bitmap mRobot;

    public LogoView(Context context) {
        super(context);
        this.initialize();
    }

    public LogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public LogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    public void resetParametersToDefaults() {

        this.setPenColor(DEFAULT_PEN_COLOR);
        this.setPenDown(true);
        this.setHeading(INITIAL_HEADING);
        this.setRobotHidden(false);
        this.mPaths.clear();

       // this.invalidate();
    }

    public void resetPaint() {
        this.mPaints = new ArrayList<Paint>();
        this.mPaint = new Paint();
        // setting antialias flag to true reduces jaggedness of diagonal lines
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mPenColor);
        // we want to draw lines not a filled in shape so set style to stroke (not fill)
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(LINE_THICKNESS);
    }

    public void initialize() {
        Log.v("initialize LogoView", "");
        // the order of the next three statements is important
        this.mPaths = new ArrayList<Path>();
        this.resetPaint();
        resetParametersToDefaults(); // paint and path objects need to be created before calling
        this.mX = this.mY = (float)0.0;
        this.mClearCanvas = true;

        // load the android robot instead of the traditional turtle as the cursor
        mRobot = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        this.invalidate(); // draw the screen
    }

    public double getHeading() {
        return mHeading;
    }

    public void setHeading(int mHeading) {
        this.mHeading = mHeading;
    }

    public int getPenColor() {
        return mPenColor;
    }

    public void setPenColor(int mPenColor) {
        this.mPenColor = mPenColor;
        this.mPaint.setColor(mPenColor);
    }

    public boolean isPenDown() {
        return mPenDown;
    }

    public void setPenDown(boolean mPenDown) {
        this.mPenDown = mPenDown;
    }

    public boolean isRobotHidden() {
        return mRobotHidden;
    }

    public void setRobotHidden(boolean mRobotHidden) {
        this.mRobotHidden = mRobotHidden;
    }

    public double getXcoord() {
        return mX;
    }

    public void setXcoord(float mX) {
        this.mX = mX;
    }

    public double getYcoord() {
        return mY;
    }

    public void setYcoord(float mY) {
        this.mY = mY;
    }

    private void drawRobot(double angleInDegrees, Canvas canvas) {
        if (!mRobotHidden) {
            Matrix matrix = new Matrix();
            // load the number of degrees to rotate into matrix
            matrix.postRotate((float) (angleInDegrees * -1.0) + 90);
            // matrix.postRotate(45);
            Bitmap scaledRobot = Bitmap.createScaledBitmap(mRobot, ROBOT_WIDTH, ROBOT_HEIGHT, true);
            Bitmap rotatedRobot = Bitmap.createBitmap(scaledRobot, 0, 0,
                    scaledRobot.getWidth(), scaledRobot.getHeight(), matrix, true);
            canvas.drawBitmap(rotatedRobot, mX-ROBOT_WIDTH/2, mY-ROBOT_HEIGHT/2, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // Toast.makeText(c, "OnDRaw()", Toast.LENGTH_SHORT).show();
        if (mClearCanvas) {
           // Toast.makeText(c, "Clearing the canvas", Toast.LENGTH_SHORT).show();

            // clear the canvas
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Paint _p = new Paint();
            _p.setColor(DEFAULT_BACKGROUND_COLOR);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _p);
            // move robot back to center of screen
            this.setXcoord(canvas.getWidth()/2);
            this.setYcoord(canvas.getHeight()/2);
            mClearCanvas = false; // just finished clearing it
        }

        drawRobot(mHeading, canvas);
        for (int i=0; i<mPaths.size(); ++i) {
            canvas.drawPath(mPaths.get(i), mPaints.get(i));
        }

    }

    // button commands
    public void fwd() {
        double _theta = (float)Math.toRadians(mHeading);
        float _deltaX = (float)(STEP_SIZE * Math.cos(_theta));
        float _deltaY = (float)(-1.0 * STEP_SIZE * Math.sin(_theta));

        Path _path = new Path();
        _path.moveTo(mX, mY);
        if (mPenDown){
            _path.lineTo(mX + _deltaX, mY + _deltaY);
        } else {
            _path.moveTo(mX + _deltaX, mY + _deltaY);
        }
        mPaths.add(_path);
        mPaints.add(new Paint(mPaint));

        mX = mX + _deltaX;
        mY = mY + _deltaY;

        // force a redraw
        this.invalidate();
    }

    public void right() {
        this.turn (90);

    }

    public void turn(int angleInDegrees) {
        // new heading
        mHeading = (mHeading - angleInDegrees)  % 360;
        //redraw the robot
        this.invalidate();
    }

    public void up() {
        this.setPenDown(false);
    }

    public void down() {
        this.setPenDown(true);
    }

    public void hide() {
        this.setRobotHidden(true);
        // redraw the canvas without the robot
        this.invalidate();
    }

    public void show() {
        this.setRobotHidden(false);
        // draw the robot
        this.invalidate();
    }



    public int getRandomColor() {
        Random _rg = new Random();
        int _red = _rg.nextInt(256);
        int _green = _rg.nextInt(256);
        int _blue = _rg.nextInt(256);
        int color = Color.rgb(_red, _green, _blue);
        Log.v("Color = ", color+"");
        return color;

    }

    public void drawFlower() {

        for (int i = 0; i < 6; ++i) {
            this.setPenColor(this.getRandomColor());
            this.drawSquare();
            this.turn(60);
        }
    }

    public void drawSquare() {
        //Toast.makeText(c, "Drawing a Square",  Toast.LENGTH_SHORT).show();
        for (int j=0; j<4; ++j){
            for (int i = 0; i < 5; ++i)
                this.fwd();
            this.turn(90);  // alternatively, we could have used this.right()
        }

    }

    public void drawPolygon(int sides, int sideLengthInPixels) {
        // you write this one for part 2
    }

    public void drawFace() {
        // you have to write this for part 1

    }

    public void draw(Context c) {
        // you write this one for parts 1 and 2

        //Toast.makeText(c, "You have not implemented draw()", Toast.LENGTH_SHORT).show();

        // comment out the toast message, above, and insert your code below.
        this.drawFlower();
      // this.drawSquare();
       // this.drawFace();
    }



}

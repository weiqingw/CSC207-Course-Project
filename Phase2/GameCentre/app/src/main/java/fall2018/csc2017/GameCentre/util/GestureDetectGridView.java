package fall2018.csc2017.GameCentre.util;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * The GestureDetectGridView class.
 */
public class GestureDetectGridView extends GridView {
    /**
     *the minimum swipe distance.
     */
    public static final int SWIPE_MIN_DISTANCE = 100;
    /**
     * the gesture detector.
     */
    private GestureDetector gDetector;
    /**
     * the movement controller.
     */
    private MovementController mController;
    /**
     * the fling confirmed.
     */
    private boolean mFlingConfirmed = false;
    /**
     * touchX
     */
    private float mTouchX;
    /**
     * touchY
     */
    private float mTouchY;


    /**
     * constructor of grid view.
     * @param context the context from the activity.
     */
    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    /**
     * the constructor for gesture grid view
     * @param context the context from the activity.
     * @param attrs the attributeSet
     */
    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * the constructor for gesture grid view
     * @param context the context from the activity.
     * @param attrs the attributeSet
     * @param defStyleAttr the defStyleAttr.
     */
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * init method for the gesture grid view.
     * @param context the context from activity
     */
    private void init(final Context context) {
        mController = new MovementController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    /**
     * set new boardManager to grid View
     * @param boardManager the new boardManager.
     */
    public void setBoardManager(BoardManagerForBoardGames boardManager) {
        mController.setBoardManager(boardManager);
    }
}

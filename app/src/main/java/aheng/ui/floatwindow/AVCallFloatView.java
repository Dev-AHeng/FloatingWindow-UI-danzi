package aheng.ui.floatwindow;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


public class AVCallFloatView extends FrameLayout {
    private static final String TAG = "AVCallFloatView";
    
    private LayoutInflater inflater;
    private View floatView;
    
    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;
    
    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;
    
    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;
    
    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;
    
    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    
    private View view;
    private TextView floatTitle;
    private TableLayout tableLayout;
    
    // 悬浮窗展开收起状态
    private boolean floatStatus = true;
    // 表格展开收起状态
    private boolean tableStatus = false;
    
    private int tbWidth = 0;
    // 表格高度
    private int tbHeight = 0;
    // 悬浮窗宽度
    private int floatWidth = 0;
    // 悬浮窗高度
    private int floatHeight = 0;
    
    private boolean isAnchoring = false;
    private boolean isShowing = false;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    
    private ViewGroup.LayoutParams tableParams;
    private ViewGroup.LayoutParams floatParams;
    
    
    private OnTouchListener buttonListener = new OnTouchListener() {
        boolean isMove = false;
        boolean viewState = true;
        
        @SuppressLint("WrongConstant")
        @Override
        public boolean onTouch(View arg0, MotionEvent event) {
            if (isAnchoring) {
                return true;
            }
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xInView = event.getX();
                    yInView = event.getY();
                    xDownInScreen = event.getRawX();
                    yDownInScreen = event.getRawY();
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isMove = true;
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    // 手指移动的时候更新小悬浮窗的位置
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isMove) {
                        if (tableStatus) {
                            floatTitle.setVisibility(0);
                            tableStatus = false;
                        } else {
                            floatTitle.setVisibility(8);
                            tableStatus = true;
                        }
                        floatStatus();
                    } else {
                        isMove = false;
                    }
                    break;
                default:
                    break;
                
            }
            Log.i(TAG, String.valueOf(isMove));
            return true;
        }
    };
    
    @SuppressLint("InflateParams")
    public AVCallFloatView(Context context) {
        super(context);
        
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        inflater = LayoutInflater.from(getContext());
        floatView = inflater.inflate(R.layout.myfloat, null);
        
        View floatIcon = floatView.findViewById(R.id.float_icon);
        floatTitle = floatView.findViewById(R.id.floatTitle);
        LinearLayout linearLayout = floatView.findViewById(R.id.show_item);
        view = floatView.findViewById(R.id.rotate);
        tableLayout = floatView.findViewById(R.id.flaot_tb);
        
        addView(floatView);
        
        floatWidth = getViewWH(floatView)[0];
        floatHeight = getViewWH(floatView)[1];
        
        tbWidth = getViewWH(tableLayout)[0];
        tbHeight = getViewWH(tableLayout)[1];
        
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tableStatus();
            }
        });
        
        // floatIcon.setOnClickListener(new OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         floatStatus();
        //     }
        // });
        
        floatIcon.setOnTouchListener(buttonListener);
        
        
        // btn1.setOnClickListener(new OnClickListener() {
        //     @Override
        //     public void onClick(View p1) {
        //         // changeImageHandler.sendEmptyMessageDelayed(0, 0);
        //
        //         // ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        //         // 线程池
        //         ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        //         // ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        //         exec.scheduleAtFixedRate(new Runnable() {
        //             @Override
        //             public void run() {
        //                 txv1.post(new Runnable() {
        //                     @Override
        //                     public void run() {
        //                         txv1.setText("随机数 -> " + Math.random() * 10);
        //                         txv2.setText("随机数 -> " + Math.random() * 10);
        //                     }
        //                 });
        //
        //             }
        //         }, 0, 1, TimeUnit.MILLISECONDS); // 毫秒
        //
        //     }
        //
        // });
        
        // 取消
        // btn2.setOnClickListener(new OnClickListener() {
        //     @Override
        //     public void onClick(View p1) {
        //         FloatWindowManager.getInstance().dismissWindow();
        //     }
        // });
    }
    
    /**
     * 展开收起
     */
    private void tableStatus() {
        floatParams = floatView.getLayoutParams();
        if (tableStatus) {
            rotateView(view, 0, 90, 300);
            // floatParams.width = tbWidth;
            floatParams.height = floatHeight;
            floatView.setLayoutParams(floatParams);
            tableStatus = false;
        } else {
            rotateView(view, 90, 0, 300);
            // floatParams.width = tbWidth;
            floatParams.height = floatHeight - tbHeight;
            floatView.setLayoutParams(floatParams);
            tableStatus = true;
        }
    }
    
    private void floatStatus() {
        floatParams = floatView.getLayoutParams();
        if (floatStatus) {
            floatParams.width = 150;
            floatParams.height = 150;
            floatView.setLayoutParams(floatParams);
            floatStatus = false;
        } else {
            floatParams.width = floatWidth;
            floatParams.height = floatHeight;
            floatView.setLayoutParams(floatParams);
            floatStatus = true;
        }
    }
    
    /**
     * 获取控件高度
     *
     * @param view view
     * @return view高度
     */
    private int[] getViewWH(View view) {
        int[] viewArray = new int[2];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        viewArray[0] = view.getMeasuredWidth();
        viewArray[1] = view.getMeasuredHeight();
        return viewArray;
    }
    
    /**
     * 控件旋转
     * <p>
     * 控件对象, 开始角度, 结束角度, 动画持续时间
     */
    private void rotateView(View view, int start, int end, int time) {
        RotateAnimation rotate = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        // 设置动画持续周期
        rotate.setDuration(time);
        // 设置重复次数
        rotate.setRepeatCount(0);
        // 动画执行完后是否停留在执行完的状态
        rotate.setFillAfter(true);
        // 执行前的等待时间
        rotate.setStartOffset(0);
        view.setAnimation(rotate);
    }
    
    private void changeViewWH(View holder, int width, int height) {
        ViewGroup.LayoutParams lp = holder.getLayoutParams();
        lp.width = width;
        lp.height = height;
        holder.setLayoutParams(lp);
    }
    
    /**
     * 改变View高度动画
     * <p>
     * 控件对象, 设置控件的高度, 动画持续时间
     */
    private void changeViewHeightAnimation(final View holder, int height, int time) {
        final ViewGroup.LayoutParams lp = holder.getLayoutParams();
        new Thread(new Runnable() {
            @Override
            public void run() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        double originalHeight = holder.getMeasuredHeight();
                        final ValueAnimator foldAnimator = ValueAnimator.ofInt((int) originalHeight, height);
                        // 动画时间
                        foldAnimator.setDuration(time);
                        foldAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                lp.height = (int) valueAnimator.getAnimatedValue();
                                holder.setLayoutParams(lp);
                            }
                        });
                        foldAnimator.start();
                    }
                });
            }
        }).start();
    }
    
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
    
    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnchoring) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 吸附效果
                anchorToSide();
				/*
                if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()
                    && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    // 点击效果
                    Toast.makeText(getContext(), "this float window is clicked", Toast.LENGTH_SHORT).show();
                } else {
                    // 吸附效果
                    anchorToSide();
                }*/
                break;
            default:
                break;
        }
        return true;
    }
    
    // 吸附效果
    private void anchorToSide() {
        isAnchoring = true;
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int middleX = mParams.x + getWidth() / 2;
        
        
        int animTime = 0;
        int xDistance = 0;
        int yDistance = 0;
        
        int dp_25 = dp2px(15);
        
        //1
        if (middleX <= dp_25 + getWidth() / 2) {
            xDistance = dp_25 - mParams.x;
        } else if (middleX <= screenWidth / 2) {
            xDistance = dp_25 - mParams.x;
        } else if (middleX >= screenWidth - getWidth() / 2 - dp_25) {
            xDistance = screenWidth - mParams.x - getWidth() - dp_25;
        } else {
            xDistance = screenWidth - mParams.x - getWidth() - dp_25;
        }
        
        //1
        if (mParams.y < dp_25) {
            yDistance = dp_25 - mParams.y;
        } else if (mParams.y + getHeight() + dp_25 >= screenHeight) {
            yDistance = screenHeight - dp_25 - mParams.y - getHeight();
        }
        // Log.e(TAG, "xDistance  " + xDistance + "   yDistance" + yDistance);
        
        animTime = Math.abs(xDistance) > Math.abs(yDistance) ? (int) (((float) xDistance / (float) screenWidth) * 600f)
                : (int) (((float) yDistance / (float) screenHeight) * 900f);
        this.post(new AnchorAnimRunnable(Math.abs(animTime), xDistance, yDistance, System.currentTimeMillis()));
    }
    
    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    
    private void updateViewPosition() {
        //增加移动误差
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        // Log.e(TAG, "x  " + mParams.x + "   y  " + mParams.y);
        windowManager.updateViewLayout(this, mParams);
    }
    
    private class AnchorAnimRunnable implements Runnable {
        private int animTime;
        private long currentStartTime;
        private Interpolator interpolator;
        private int xDistance;
        private int yDistance;
        private int startX;
        private int startY;
        
        public AnchorAnimRunnable(int animTime, int xDistance, int yDistance, long currentStartTime) {
            this.animTime = animTime;
            this.currentStartTime = currentStartTime;
            interpolator = new AccelerateDecelerateInterpolator();
            this.xDistance = xDistance;
            this.yDistance = yDistance;
            startX = mParams.x;
            startY = mParams.y;
        }
        
        @Override
        public void run() {
            if (System.currentTimeMillis() >= currentStartTime + animTime) {
                if (mParams.x != (startX + xDistance) || mParams.y != (startY + yDistance)) {
                    mParams.x = startX + xDistance;
                    mParams.y = startY + yDistance;
                    windowManager.updateViewLayout(AVCallFloatView.this, mParams);
                }
                isAnchoring = false;
                return;
            }
            float delta = interpolator.getInterpolation((System.currentTimeMillis() - currentStartTime) / (float) animTime);
            int xMoveDistance = (int) (xDistance * delta);
            int yMoveDistance = (int) (yDistance * delta);
            // Log.e(TAG, "delta:  " + delta + "  xMoveDistance  " + xMoveDistance + "   yMoveDistance  " + yMoveDistance);
            mParams.x = startX + xMoveDistance;
            mParams.y = startY + yMoveDistance;
            if (!isShowing) {
                return;
            }
            windowManager.updateViewLayout(AVCallFloatView.this, mParams);
            AVCallFloatView.this.postDelayed(this, 0);
        }
    }
}


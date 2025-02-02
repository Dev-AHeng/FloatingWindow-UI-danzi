package aheng.ui.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * @author Dev_Heng
 */
public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";
    
    private static volatile FloatWindowManager instance;
    
    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private AVCallFloatView floatView = null;
    
    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }
    
    // 开启悬浮
    public void showWindow(Context context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "视图已在此处添加");
            return;
        }
        
        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        
        // 位置
        // int screenWidth = size.x;
        // int screenHeight = size.y;
        
        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int mType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mType = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        mParams.type = mType;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        // mParams.x = screenWidth - dp2px(context, 100);
        // mParams.y = screenHeight - dp2px(context, 171);
        mParams.x = 100;
        mParams.y = 150;
        
        floatView = new AVCallFloatView(context);
        floatView.setParams(mParams);
        floatView.setIsShowing(true);
        windowManager.addView(floatView, mParams);
    }
    
    /**
     * 最小化
     */
    public void zuixiaohua() {
    
    }
    
    // 取消悬浮
    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "窗口不能被关闭，因为它没有被添加");
            return;
        }
        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }
    
    
    
    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    
    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
    
}


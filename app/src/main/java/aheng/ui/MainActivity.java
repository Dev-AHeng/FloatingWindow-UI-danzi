package aheng.ui;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import aheng.ui.floatwindow.FloatWindowManager;

/**
 * @author Dev_Heng
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "日志";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // 右边cardview
        CardView cardView2 = findViewById(R.id.cardView2);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        cardView2.measure(w, h);
        int width = cardView2.getMeasuredWidth();
        int height = cardView2.getMeasuredHeight();
        
        Log.i(TAG, height + "---" + width);
        
        // 左边cardview-->linearlayout
        LinearLayout ll = findViewById(R.id.left);
        
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        
            }
        });
        
        
        
        ViewGroup.LayoutParams lp = ll.getLayoutParams();
        // lp.width = 0;
        lp.height = height;
        ll.setLayoutParams(lp);
        
        TextView textView = findViewById(R.id.date);
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        textView.setText(simpleDateFormat.format(date));
        
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAppOps(MainActivity.this)) {
                    FloatWindowManager.getInstance().showWindow(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "悬浮窗权限申请失败", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent2);
                }
                
            }
        });
    }
    
    /**
     * 判断 悬浮窗口权限是否打开(只适配部分机型)
     *
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            @SuppressLint("WrongConstant")
            Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
        
        }
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

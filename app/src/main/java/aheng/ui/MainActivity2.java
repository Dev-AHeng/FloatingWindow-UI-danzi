package aheng.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import aheng.ui.floatwindow.FloatWindowManager;

/**
 * @author Dev_Heng
 */
public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "日志";
    private Button open;
    private Button renove;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main2);
        
        open = (Button) findViewById(R.id.open);
        renove = (Button) findViewById(R.id.renove);
        
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance().showWindow(MainActivity2.this);
            }
        });
        
        renove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance().dismissWindow();
            }
        });
        
        
    }
}
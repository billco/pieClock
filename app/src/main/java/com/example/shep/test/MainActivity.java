package com.example.shep.test;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.View.OnTouchListener;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity implements OnTouchListener {


  boolean showTime = false;
  boolean showSec = true;
  boolean showTimeAlways = true;

  Calendar today = Calendar.getInstance();
  SimpleDateFormat formatter = new SimpleDateFormat("k:mm:ss", Locale.US);
  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

  RectF rectf = new RectF(200, 200, 850, 850);
  RectF rectff = new RectF(300, 300, 750, 750);
  RectF rectffs = new RectF(400, 400, 650, 650);
  int hh, hw;
  int scrW, scrH;
  int textX, textY;
  Handler mHandler = new Handler();
  GraphicsView gview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setContentView(gview = new GraphicsView(this));
    gview.setOnTouchListener(this);

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        {
          gview.invalidate();
          mHandler.postDelayed(this, 1000);
        }
      }
    };

    mHandler.post(runnable);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      setdims(metrics.heightPixels, metrics.widthPixels, false);
      Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      setdims(metrics.widthPixels, metrics.heightPixels, true);
      Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
    }
  }

  public void setdims(int with, int hight, boolean port) {

    scrW = with;
    scrH = hight;

    int off = 200;
    int w = with; //1440;
    int h = hight; //  2392;
    hh = h / 2;
    hw = w / 2;
    int box = ((w > h) ? h : w) - ((port) ? 100 : 320);
    int bx2 = box / 2;
    int l = hw - bx2;
    // int t = hh - bx2;
    int t = 25;
    int b = box + 25;
    int r = hw + bx2;
    rectf = new RectF(l, t, r, b);
    rectff = new RectF(l + off, t + off, r - off, b - off);
    textX = l + (int) ((port) ? off * 3 : off * 2.5) - 15;
    textY = t + (int) ((port) ? off * 3 : off * 2.5);
    rectffs = new RectF(l + off + off, t + off + off, r - off - off, b - off - off);


  }


  public boolean onTouch(View v, MotionEvent event) {
    int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        showTime = true;
        break;

      case MotionEvent.ACTION_UP:
        showTime = false;
        break;
      case MotionEvent.ACTION_CANCEL:
        break;
      default:
        break;
    }
    return true;
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      showTimeAlways = !showTimeAlways;
      return true;
    } else if (id == R.id.action_settings1) {
      showSec = !showSec;
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public class GraphicsView extends View {

    //   private int cIx = 0;
    String cHours[] = {"#0000FF", "#F0FFFF", "#FFEBCD",
            "#B0E0E6", "#00FFFF", "#E9967A",
            "#228B22", "#6495ED", "#FFFF00",
            "#BA55D3", "#DAA520", "#FF6347", "#FF6347"
    };


    public GraphicsView(Context context) {
      super(context);

      //setBackgroundResource(R.drawable.icon);
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      setdims(metrics.widthPixels, metrics.heightPixels, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      today.setTime(new Date());
      int hour = today.get(Calendar.HOUR_OF_DAY) % 12;

      float temp = 225;
      canvas.drawColor(Color.BLACK);
      for (int i = 0; i < 12; i++) {
        temp += 30;
        paint.setColor(Color.parseColor(cHours[i]));
        canvas.drawArc(rectf, temp, 30, true, paint);
      }
      // do Hour BackGround
      temp = (today.get(Calendar.MINUTE) * 6) + 285;
      paint.setColor(Color.BLACK);
      canvas.drawArc(rectff, (float)(temp - 0.25), (float)330.50, true, paint);

      // do Hour ForeGround
      paint.setColor(Color.parseColor(cHours[hour]));
      canvas.drawArc(rectff, temp, 330, true, paint);

      if(showSec) {
        // do Seconds

        temp = (today.get(Calendar.SECOND) * 6) + 285;
        // do Seconds BackGround
        paint.setColor(Color.BLACK);
        canvas.drawArc(rectffs, (float) (temp - 0.25), (float) 330.5, true, paint);
        // do Seconds ForeGround
        paint.setColor(Color.parseColor("#F5F5F5"));
        canvas.drawArc(rectffs, temp, 330, true, paint);
      }
      // do string time
      if (showTime || showTimeAlways) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(46);
        //  String s = today.format("%k:%M:%S");
        String s = formatter.format(new Date());
        canvas.drawText(s, 0, s.length(), textX, textY, paint);
      }
    }
  }
}

package com.example.shep.test;

import android.app.Activity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends Activity implements OnTouchListener {
  boolean showTime = false;
  boolean showTimeAlways = false;

  public Point p = new Point();

  Calendar today = Calendar.getInstance();
  SimpleDateFormat formatter = new SimpleDateFormat("k:mm:ss", Locale.US);
  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
  RectF rectf = new RectF(200, 200, 850, 850);
  RectF rectff = new RectF(300, 300, 750, 750);
  RectF rectffs = new RectF(400, 400, 650, 650);

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
    }

    return super.onOptionsItemSelected(item);
  }

  public class GraphicsView extends View {

    private int cIx = 0;
    String cHours[] = {"#0000FF", "#F0FFFF", "#FFEBCD",
            "#B0E0E6", "#00FFFF", "#E9967A",
            "#228B22", "#6495ED", "#FFFF00",
            "#BA55D3", "#DAA520", "#FF6347", "#FF6347"
    };


    public GraphicsView(Context context) {
      super(context);

      //setBackgroundResource(R.drawable.icon);

    }

    @Override
    protected void onDraw(Canvas canvas) {
      //   today = new Time(Time.getCurrentTimezone());
      today.setTime(new Date());
      int hour = today.get(Calendar.HOUR_OF_DAY) % 12;


      float[] value_degree = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};


      // draw clock face
      int temp = 255;

      for (int i = 0; i < value_degree.length; i++) {
        if (i == 0) {
          paint.setColor(Color.parseColor(cHours[i]));
          canvas.drawArc(rectf, temp, value_degree[i], true, paint);
        } else {
          temp += (int) value_degree[i - 1];
          paint.setColor(Color.parseColor(cHours[i]));
          canvas.drawArc(rectf, temp, value_degree[i], true, paint);
        }
      }
      // do Hour BackGround
      temp = (today.get(Calendar.MINUTE) * 6) + 285;
      paint.setColor(Color.BLACK);
      canvas.drawArc(rectff, temp + 2, 328, true, paint);

      // do Hour ForeGround
      paint.setColor(Color.parseColor(cHours[hour]));
      canvas.drawArc(rectff, temp, 330, true, paint);

      // do Seconds
      temp = (today.get(Calendar.SECOND) * 6) + 285;
      // do Seconds BackGround
      paint.setColor(Color.BLACK);
      canvas.drawArc(rectffs, temp + 2, 328, true, paint);
      // do Seconds ForeGround
      paint.setColor(Color.parseColor("#F5F5F5"));
      canvas.drawArc(rectffs, temp, 330, true, paint);

      // do string time
      if (showTime || showTimeAlways) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        //  String s = today.format("%k:%M:%S");
        String s = formatter.format(new Date());
        canvas.drawText(s, 0, s.length(), 450, 450, paint);
      }
    }
  }
}

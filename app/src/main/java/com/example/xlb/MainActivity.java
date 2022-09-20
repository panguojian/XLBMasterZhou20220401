package com.example.xlb;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.media.RatingCompat;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;
import android.net.Network;
import android.widget.ViewFlipper;

import com.example.xlb.config.SetConfig;
import com.example.xlb.databinding.ActivityMainBinding;
import com.example.xlb.utils.ByteUtils;
import com.example.xlb.utils.FunctionDelayed;
import com.example.xlb.utils.FunctionUtils;
import com.example.xlb.utils.LogUtils;
import com.example.xlb.utils.RoomInfoUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.IDataDeal;
import android_serialport_api.ReceiveComUtils;
import android_serialport_api.SerialPort;
import sfListView.Room;

import com.example.xlb.coustomView.DragFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.xlb.socket.TcpServer;


//public class MainActivity extends AppCompatActivity {
    public class MainActivity extends Activity implements  ServiceConnection {

    private Handler handler_net;
    private boolean[] checkedItems;
    private String[] Mitems1,Mitems2,DDitems1,DDitems2,HLFitems1,HLFitems2;
    private String Netcmd="";
   public  int i=0;
   public int Off_time=0;
    private int mProgressStatus = 0;
    private Handler mHandler;
    private ActivityMainBinding activityMainBinding;
//   private TcpServer socketTCPServer;
//   private static TcpServer tcpServer = null;
   ExecutorService exec = Executors.newCachedThreadPool();
    private final MyHandler myHandler = new MyHandler(this);
    final int FLAG_MSG = 0x001;
    private Message message;
    public static Context context;
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    //定义图片数组
    private int[] images = new int[]{R.drawable.cadroom1, R.drawable.cadroom2, R.drawable.cadroom3, R.drawable.cadroom4};
    private Animation[] animation = new Animation[2];  //定义动画数组，为ViewFlipper指定切换动画


    private boolean bind = false;
    private boolean runStatus = false;
    private boolean light_status=false;
    private boolean ziwai_status1=false,ziwai_status2=false,ziwai_status3=false,ziwai_status4=false;
    private boolean xd_status=false;
    Calendar c1,c2,c3,c4;
    private int TransforData;
    private String LISTVIEW_DATA_PA="";
    private String LISTVIEW_DATA_PB="";
    private String LISTVIEW_DATA_TEMP="";
    private int INDEX=0;
    private String[] tempkt={"---","---","---","---"};
    private String infokt="";
   // private String LISTVIEW_DATA_CH="";
    SerialPortService.MyBinder binder = null;


    private boolean MODE1_FIRST=false;
    private boolean MODE2_FIRST=false;
    private boolean MODE3_FIRST=false;
    private boolean MODE4_FIRST=false;
    private boolean MODE5_FIRST=false;
    private boolean MODE6_FIRST=false;


    private  String s_m1="false";
    private  String s_m2="false";
    private  String s_m3="false";
    private  String s_m4="false";

    private int single_s_mode1=0;
    private int single_d_mode1=0;
    private int single_h_mode1=0;
    private int single_kz_mode1=0;
    private int single_gz_mode1=0;
    private int single_zm_mode1=0;
    private int single_sd_mode1=0;

    private int single_s_mode2=0;
    private int single_d_mode2=0;
    private int single_h_mode2=0;
    private int single_kz_mode2=0;
    private int single_gz_mode2=0;
    private int single_zm_mode2=0;
    private int single_sd_mode2=0;

    private String light_fj="0";

    //空调模块
    int TEMP_H,TEMP_L,HUM_H,HUM_L,TEMPFL_H,TEMPFL_L;
    String TEMP,HUM,KT_STATUS="空调状态：",TEMP_FL;

    SetConfig.SYSTEM_TYPE S_TYPE;

    private int RECEIVE_DATA=0;


    private String[] room=new String[15];
    private String room_string="";
    private String[] room_edv=new String[15];
    private String roomedv_string="";
    private String[] dataP=new String[15];
    private Boolean[] states=new Boolean[15];
    private String[] dataT=new String[15];
    private String[] dataH=new String[15];

    public String dates;  //日期时间
    public int ziwai_which=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏


        Window xlbwindow;
        xlbwindow=getWindow();
        WindowManager.LayoutParams params = xlbwindow.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE;
        xlbwindow.setAttributes(params);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(activityMainBinding.getRoot());
        bindReceiver();
        context = this;
        //this.getSupportActionBar().hide();
        //设置文本
        activityMainBinding.btLog.setImageResource(R.drawable.xlb_log);
        //activityMainBinding.btRun.setImageResource(R.drawable.btn_run_bg);
        activityMainBinding.btSD.setImageResource(R.drawable.wensd);
        activityMainBinding.btWD.setImageResource(R.drawable.wensd);
        activityMainBinding.btSD.setImageResource(R.drawable.shid);

        activityMainBinding.btYL.setImageResource(R.drawable.yl);
        activityMainBinding.btJD.setImageResource(R.drawable.fengl);
        activityMainBinding.btWFL.setImageResource(R.drawable.fengs);
        activityMainBinding.btNFL.setImageResource(R.drawable.fengs);


        activityMainBinding.btMsetting.setImageResource(R.drawable.settingm);
        activityMainBinding.btDDSetting.setImageResource(R.drawable.ddm);
        activityMainBinding.btclear.setImageResource(R.drawable.clear);
        activityMainBinding.btlock.setImageResource(R.drawable.lock);
        activityMainBinding.btLight.setImageResource(R.drawable.light_off);
        activityMainBinding.btLightG1.setImageResource(R.drawable.gui1_off);
        activityMainBinding.btLightG2.setImageResource(R.drawable.gui2_off);

        activityMainBinding.btHLFsetting.setImageResource(R.drawable.huilf);
        activityMainBinding.btUPSetting.setImageResource(R.drawable.up);
        activityMainBinding.btStopSetting.setImageResource(R.drawable.stop);
        activityMainBinding.btDownSetting.setImageResource(R.drawable.down);


        activityMainBinding.btMsetting2.setImageResource(R.drawable.settingm);
        activityMainBinding.btDDSetting2.setImageResource(R.drawable.ddm);
        activityMainBinding.btclear2.setImageResource(R.drawable.clear);
        activityMainBinding.btlock2.setImageResource(R.drawable.lock);

        activityMainBinding.btHLFsetting2.setImageResource(R.drawable.huilf);
        activityMainBinding.btUPSetting2.setImageResource(R.drawable.up);
        activityMainBinding.btStopSetting2.setImageResource(R.drawable.stop);
        activityMainBinding.btDownSetting2.setImageResource(R.drawable.down);
        activityMainBinding.btSetting.setImageResource(R.drawable.xlb_setting);
        //activityMainBinding.btSetting.setImageResource(R.drawable.xlb_setting);
        //activityMainBinding.btOut.setImageResource(R.drawable.xlb_logout);
        //activityMainBinding.btQrcode.setImageResource(R.drawable.xlb_qrcode);
        //activityMainBinding.btCad.setImageResource(R.drawable.xlb_cad);

        activityMainBinding.btHLFsetting2.setVisibility(View.INVISIBLE);
        activityMainBinding.btHLFsetting.setVisibility(View.GONE);
        //activityMainBinding.bottomRun0.setOnClickListener(ZIWAI_Click);
        final SharedPreferences spp=getSharedPreferences("xlb",MODE_PRIVATE);
        final SharedPreferences.Editor editor=spp.edit();
        int[] imageArry =new int[]{R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,
                R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1,R.drawable.img1};


        Init_Left_View();
        FunctionDelayed.handlerTimer.postDelayed(FunctionDelayed.runnable, 300000);


        activityMainBinding.btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);

            }
        });


        activityMainBinding.btMsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //记录各列表项的状态


                //各列表项要显示的内容
                Mitems1 = new String[]{"停  止", "开  启", "紧  急",
                        "节  能"};
                // 显示带单选列表项的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);                 //设置对话框的图标
                builder.setTitle("柜一设备模式控制：");           //设置对话框标题
                builder.setSingleChoiceItems(Mitems1, single_s_mode1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //显示选择结果
                        single_s_mode1=which;
                    }
                });
                //为对话框添加“确定”按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_s_mode1)
                        {
                            case 0:
                                binder.setData(9);
                                break;
                            case 1:
                                binder.setData(10);
                                break;
                            case 2:
                                binder.setData(11);
                                break;
                            case 3:
                                binder.setData(12);
                                break;
                        }

                        Toast.makeText(MainActivity.this,
                                 Mitems1[single_s_mode1]+"模式已开", Toast.LENGTH_SHORT).show();


                    }
                });
                builder.create().show();
            }
        });
//
//
//

        activityMainBinding.btDDSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDitems1 = new String[]{"手  动", "半  自  动", "自  动"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);
                builder.setTitle("柜一电动门模式控制：");
                builder.setSingleChoiceItems(DDitems1, single_d_mode1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        single_d_mode1=which;
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_d_mode1)
                        {
                            case 0:
                                binder.setData(13);
                                break;
                            case 1:
                                binder.setData(14);
                                break;
                            case 2:
                                binder.setData(15);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        activityMainBinding.btHLFsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HLFitems1 = new String[]{"联  动", "关  闭", "开  90   度"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);
                builder.setTitle("柜一回流阀模式控制：");
                builder.setSingleChoiceItems(HLFitems1, single_h_mode1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        single_h_mode1=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_h_mode1)
                        {
                            case 0:
                                binder.setData(24);
                                break;
                            case 1:
                                binder.setData(25);
                                break;
                            case 2:
                                binder.setData(26);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });






        activityMainBinding.btMsetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedItems = new boolean[]{false, false, false, false};
                Mitems2 = new String[]{"停  止", "开  启", "紧  急",
                        "节  能"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);
                builder.setTitle("柜二设备模式控制：");
                builder.setSingleChoiceItems(Mitems2, single_s_mode2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        single_s_mode2=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_s_mode2)
                        {
                            case 0:
                                binder.setData(29);
                                break;
                            case 1:
                                binder.setData(30);
                                break;
                            case 2:
                                binder.setData(31);
                                break;
                            case 3:
                                binder.setData(32);
                                break;
                        }

                    }
                });
                builder.create().show();                                // 创建对话框并显示
            }
        });
//
//
//

        activityMainBinding.btDDSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDitems2 = new String[]{"手  动", "半  自  动", "自  动"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);
                builder.setTitle("柜二电动门模式控制：");
                builder.setSingleChoiceItems(DDitems2, single_d_mode2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        single_d_mode2=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_d_mode2)
                        {
                            case 0:
                                binder.setData(33);
                                break;
                            case 1:
                                binder.setData(34);
                                break;
                            case 2:
                                binder.setData(35);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });


        activityMainBinding.btHLFsetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HLFitems2 = new String[]{"联  动", "关  闭", "开  90   度"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.xlb_log);
                builder.setTitle("柜二回流阀模式控制：");
                builder.setSingleChoiceItems(HLFitems2, single_h_mode2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        single_h_mode2=which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetConfig.ReadCount=0;
                        switch (single_h_mode2)
                        {
                            case 0:
                                binder.setData(44);
                                break;
                            case 1:
                                binder.setData(45);
                                break;
                            case 2:
                                binder.setData(46);
                                break;
                        }


                    }
                });
                builder.create().show();
            }
        });

        for (int i = 0; i < images.length; i++) {      //遍历图片数组中的图片
            ImageView imageView = new ImageView(this);  //创建ImageView对象
            imageView.setImageResource(images[i]);  //将遍历的图片保存在ImageView中
           // activityMainBinding.viewFlipper.addView(imageView);             //加载图片
        }

        //初始化动画数组
        animation[0] = AnimationUtils.loadAnimation(this, R.anim.slide_in_right); //右侧平移进入动画
        animation[1] = AnimationUtils.loadAnimation(this, R.anim.slide_out_left); //左侧平移退出动画
       // activityMainBinding.viewFlipper.setInAnimation(animation[0]);   //为flipper设置图片进入动画效果
       // activityMainBinding.viewFlipper.setOutAnimation(animation[1]);  //为flipper设置图片退出动画效果

        message=Message.obtain();
        message.what=FLAG_MSG;
        handlerAnim.sendMessage(message);


       // init();





        activityMainBinding.btLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title=light_fj.equals("0")?"开灯确认":"关灯确认";
                String Message=light_fj.equals("0")?"您是否要打开照明？":"您是否要关闭照明？";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SetConfig.ReadCount=0;
                                if (light_fj.equals("0")) {
                                    light_fj="1";
                                    binder.setData(-2);
                                    activityMainBinding.btLight.setImageResource(R.drawable.light_on);
                                } else {
                                   // activityMainBinding.bottomRun.setBackgroundResource(R.drawable.xlb_run);
                                    light_fj="0";
                                    binder.setData(-1);
                                    activityMainBinding.btLight.setImageResource(R.drawable.light_off);
                                }
                            }
                        });
                alertDialog.show();
            }
        });


        activityMainBinding.btLightG1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建对话框对象
                String Title=single_zm_mode1==0?"柜一开灯确认":"柜一关灯确认";
                String Message=single_zm_mode1==0?"您是否要打开照明？":"您是否要关闭照明？";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (single_zm_mode1==0) {
                                    single_zm_mode1=1;
                                    binder.setData(20);
                                    activityMainBinding.btLightG1.setImageResource(R.drawable.gui1_on);
                                } else {
                                    // activityMainBinding.bottomRun.setBackgroundResource(R.drawable.xlb_run);
                                    single_zm_mode1=0;
                                    binder.setData(21);
                                    activityMainBinding.btLightG1.setImageResource(R.drawable.gui1_off);
                                }
                            }
                        });
                alertDialog.show();
            }
        });

        activityMainBinding.btclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title="故障清除";
                String Message="故障清除确认";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binder.setData(19);
                            }
                        });
                alertDialog.show();
            }
        });


        activityMainBinding.btlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title=single_sd_mode1==1?"柜一解锁确认":"柜一锁定确认";
                String Message=single_sd_mode1==1?"您是否要解锁柜一？":"您是否要锁定柜一？";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (single_sd_mode1==1) {
                                    single_sd_mode1=0;
                                    binder.setData(22);
                                    activityMainBinding.btlock.setImageResource(R.drawable.lock);
                                } else {
                                   single_sd_mode1=1;
                                    binder.setData(23);
                                    activityMainBinding.btlock.setImageResource(R.drawable.lockon);
                                }
                            }
                        });
                alertDialog.show();
            }
        });

        activityMainBinding.btStopSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(16);
            }
        });

        activityMainBinding.btUPSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(17);
            }
        });


        activityMainBinding.btDownSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(18);

            }
        });
        ///柜二照明
        activityMainBinding.btLightG2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title=single_zm_mode2==0?"柜二开灯确认":"柜二关灯确认";
                String Message=single_zm_mode2==0?"您是否要打开照明？":"您是否要关闭照明？";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (single_zm_mode2==0) {
                                    single_zm_mode2=1;
                                    binder.setData(40);
                                    activityMainBinding.btLightG2.setImageResource(R.drawable.gui2_on);
                                } else {
                                    // activityMainBinding.bottomRun.setBackgroundResource(R.drawable.xlb_run);
                                    single_zm_mode2=0;
                                     binder.setData(41);
                                    activityMainBinding.btLightG2.setImageResource(R.drawable.gui2_off);
                                }
                            }
                        });
                alertDialog.show();
            }
        });


        activityMainBinding.btStopSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(36);
            }
        });

        activityMainBinding.btUPSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(37);
            }
        });


        activityMainBinding.btDownSetting2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.setData(38);

            }
        });

        activityMainBinding.btclear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title="故障清除";
                String Message="故障清除确认";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binder.setData(39);
                            }
                        });
                alertDialog.show();
            }
        });



        activityMainBinding.btlock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title=single_sd_mode2==1?"柜二解锁确认":"柜二锁定确认";
                String Message=single_sd_mode2==1?"您是否要解锁柜二？":"您是否要锁定柜二？";
                AlertDialog  alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setIcon(R.drawable.xlb_log);
                alertDialog.setTitle(Title);
                alertDialog.setMessage(Message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (single_sd_mode2==1) {
                                    single_sd_mode2=0;
                                    binder.setData(42);
                                    activityMainBinding.btlock2.setImageResource(R.drawable.lock);
                                } else {
                                    single_sd_mode2=1;
                                    binder.setData(43);
                                    activityMainBinding.btlock2.setImageResource(R.drawable.lockon);
                                }
                            }
                        });
                alertDialog.show();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                } else {
                    runStatus = false;
                    SetConfig.SYS_STATUS=false;
                    binder.setData(7);
                    showTextToast("系统已停止!");

                }
            }
        };

        handler_net = new Handler() {  //如果服务器返回值为“ok”证明用户名密码正确并跳转登录后界面否则给出相应的提示信息
            @Override
            public void handleMessage(Message msg) {
                if ("1".equals(Netcmd)) { //如果服务器返回值为“ok”，证明用户名、密码输入正确
                    Intent intent=new Intent(MainActivity.this,DBDataSetting.class);
                    startActivity(intent);
                }
                super.handleMessage(msg);
            }
        };

    }


    @Override
    protected void onStop() {
        super.onStop();
        stopService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }


    private class MyHandler extends android.os.Handler{
        private final WeakReference<MainActivity> mActivity;
        MyHandler(MainActivity activity){
            mActivity = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity!= null){
                switch (msg.what){
                    case 1:
                        Stop_Start(msg.obj.toString());
                        break;
                }
            }
        }
    }



    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction){
                case "tcpServerReceiver":
                    String msg = intent.getStringExtra("tcpServerReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }

    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpServerReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SerialPortService.MyBinder) service;
        binder.TransferData(TransforData);
        SerialPortService myService2 = binder.getService();
        myService2.setCallback(new SerialPortService.Callback() {
            @Override
            public void onDataChange(String data) {
                Message msg = new Message();
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                AnalysisMSG(msg.obj.toString());
                if(SetConfig.OFF_1)
                {
                    SetConfig.OFF_1=false;
                    ziwai_status1=false;
                    //activityMainBinding.bottomRun0.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                    binder.setData(5);
                }
                else if(SetConfig.OFF_2)
                {
                    SetConfig.OFF_2=false;
                    ziwai_status2=false;
                    //activityMainBinding.bottomRun1.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                    binder.setData(11);
                }
                else if(SetConfig.OFF_3)
                {
                    SetConfig.OFF_3=false;
                    ziwai_status3=false;
                    //activityMainBinding.bottomRun2.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                    binder.setData(13);
                }
                else if(SetConfig.OFF_4)
                {
                    SetConfig.OFF_4=false;
                    ziwai_status4=false;
                    //activityMainBinding.bottomRun3.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                    binder.setData(15);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    static Handler handlerTimer=new Handler();
     Runnable runnable =new Runnable() {
        @Override
        public void run() {
            switch (S_TYPE)
            {
                case SYSTEM_RUN:
                    showTextToast("链接系统超时!");
                break;
                case SYSTEM_START:
                    showTextToast("启动系统超时!");
                break;
                case SYSTEM_STOP:
                    showTextToast("停止系统超时!");
                    break;
                case SYSTEM_SUCCESS:
                    SetConfig.SYS_DISCONNECTION=true;
                    Recovery();
                break;
                default:
                    break;
            }
           handlerTimer.postDelayed(this, 6000);
        }
    };



    static Handler handlerTimer_delay=new Handler();

    Runnable runnable_delay =new Runnable() {
        @Override
        public void run() {
            SetConfig.LOAD_FIRST=true;
        }
    };

    private void init() {
        if (!bind) {
            bind = true;

            Intent bindIntent = new Intent(MainActivity.this, SerialPortService.class);
            bindService(bindIntent, MainActivity.this, BIND_AUTO_CREATE);
            S_TYPE= SetConfig.SYSTEM_TYPE.SYSTEM_RUN;
            handlerTimer.postDelayed(runnable, 6000);
           // beginSocketServerListen();
            //Toast.makeText(MainActivity.this, "Bind Service Success!", Toast.LENGTH_SHORT).show();
        }

    }

    private void stopService()
    {
        if (bind) {
            unbindService(MainActivity.this);
            bind = false;
        }
    }

    private void Stop_Start(String msg)
    {
        //showTextToast(msg);
        if(msg.length()<4)
            return;
        if(msg.substring(0,4).equals("stop"))
        {
            if(!runStatus)
            {
                return;
            }
            else
            {
                runStatus = false;
                binder.setData(7);
                showTextToast("系统已停止!");
            }
        }
        else  if(msg.substring(0,5).equals("start"))
        {
            if(runStatus)
            {
                return;
            }
            else
            {
                runStatus = true;
                binder.setData(6);
                showTextToast("系统运行中!");
            }
        }
    }
    private void AnalysisMSG(String info)
    {
        switch (S_TYPE)
        {
            case SYSTEM_RUN:
                if(info.length()>3)
                {
                  handlerTimer.removeCallbacks(runnable);
                  S_TYPE=SetConfig.SYSTEM_TYPE.SYSTEM_SUCCESS;
                 }
                break;
            case SYSTEM_START://06001E0001
                if(info.substring(2,12).equals("0600200001"))
                {
                    handlerTimer.removeCallbacks(runnable);
                    S_TYPE=SetConfig.SYSTEM_TYPE.SYSTEM_SUCCESS;
                }
                break;
            case SYSTEM_STOP://06001E0000
                if(info.substring(2,12).equals("0600200000")) {
                    handlerTimer.removeCallbacks(runnable);
                }
                break;
            case SYSTEM_SUCCESS:
                RECEIVE_DATA=binder.getData();
                SetConfig.SYN_COUNT++;
                if(SetConfig.SYN_COUNT>5) {
                    if (RECEIVE_DATA == SetConfig.SYN) {
                        handlerTimer.postDelayed(runnable, 3000);
                    } else {
                        handlerTimer.removeCallbacks(runnable);
                        SetConfig.SYS_DISCONNECTION=false;
                    }
                    SetConfig.SYN=RECEIVE_DATA;
                    SetConfig.SYN_COUNT=0;
                }
                break;
        }

        if(info.length()==18)
        {
            return;
        }
        if(SetConfig.SYS_DISCONNECTION)
        {
            return;
        }
        if(info.length()<17)
            return;
        try {

            int len=info.length();
            String indexs = info.substring(0, 2);
            String index_last=info.substring(20, 30);
            String Lenth=info.substring(4, 6);
            String iccidInfo = info.substring(6, info.length() - 4); //PLC、空调+VAV+EDV

            switch (indexs) {
                case "01":  //VAV
                        if(SetConfig.PreStr.equals("01"))
                        {
                            Check_KZStatus1(iccidInfo);
                        }
                        else {
                            LISTVIEW_DATA_PA = RoomInfoUtils.AnalysisData(iccidInfo, "G1");
                            SetAdpter(LISTVIEW_DATA_PA, "G1");
                        }
                    break;
                case "02":
                    if(SetConfig.PreStr.equals("01"))
                    {
                        Check_KZStatus2(iccidInfo);
                    }
                    else {
                        LISTVIEW_DATA_PA = RoomInfoUtils.AnalysisData(iccidInfo, "G2");
                        SetAdpter(LISTVIEW_DATA_PA, "G2");
                    }
                    break;
                case "03":  //PLC
                    //info
                   Check_plc(iccidInfo);
                    break;
                default:
                    break;
            }

            SetConfig.PreStr=info.substring(0,2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SetAdpter(String DATA,String Type)
    {
        try {

            if (DATA.length() < 1)
                return;
            String[] tempData = DATA.split("!");

            ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < room.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("indexmain",(i + 1) + ")");
                map.put("room", room[i]);
                map.put("dataJD", tempData[i]);
                listitem.add(map);
            }
            MySimpleAdapter adapter = new MySimpleAdapter(this, listitem, R.layout.left_list, new String[]{"indexmain", "room","dataJD"}, new int[]{R.id.indexmain, R.id.room,R.id.dataJD});
            if(Type.equals("G1"))
            {
                activityMainBinding.Llistview.setAdapter(adapter);
            }
            else
            {
                activityMainBinding.Rlistview.setAdapter(adapter);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void Recovery()
    {
        Init_Left_View();
    }

    Handler handlerAnim = new Handler() {  //创建android.os.Handler对象
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLAG_MSG) {
                message=handlerAnim.obtainMessage(FLAG_MSG);
                handlerAnim.sendMessageDelayed(message, 10000);
            }

        }
    };


    private Toast mToast = null;
    private void showTextToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }

    private void dateDialog(final String type) {

    }


    private  void Check_KZStatus1(String plc)
    {

       // plc=plc.substring(4);
        String data="",datas="";
        try {
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_s_mode1=0;
            }
            else if(data.equals("1"))
            {
                single_s_mode1=1;
            }
            else if(data.equals("2"))
            {
                single_s_mode1=2;
            }
            else
            {
                single_s_mode1=3;
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_d_mode1=0;
            }
            else if(data.equals("1"))
            {
                single_d_mode1=1;
            }
            else if(data.equals("2"))
            {
                single_d_mode1=2;
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_kz_mode1=0;
            }
            else if(data.equals("1"))
            {
                single_kz_mode1=1;
            }
            else
            {
                single_kz_mode1=2;
            }

            plc=plc.substring(2);
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_gz_mode1=0;
            }
            else if(data.equals("1"))
            {
                single_gz_mode1=1;
            }
            else
            {
                single_gz_mode1=2;
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_zm_mode1=0;
                activityMainBinding.btLightG1.setImageResource(R.drawable.gui1_off);
            }
            else
            {
                single_zm_mode1=1;
                activityMainBinding.btLightG1.setImageResource(R.drawable.gui1_on);
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_sd_mode1=0;
                activityMainBinding.btlock.setImageResource(R.drawable.lock);
            }
            else
            {
                single_sd_mode1=1;
                activityMainBinding.btlock.setImageResource(R.drawable.lockon);
            }

            plc=plc.substring(2);
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_h_mode1=0;
            }
            else if(data.equals("1"))
            {
                single_h_mode1=1;
            }
            else
            {
                single_h_mode1=2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  void Check_KZStatus2(String plc)
    {

       // plc=plc.substring(4);
        String data="",datas="";
        try {
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_s_mode2=0;
            }
            else if(data.equals("1"))
            {
                single_s_mode2=1;
            }
            else if(data.equals("2"))
            {
                single_s_mode2=2;
            }
            else if(data.equals("3"))
            {
                single_s_mode2=3;
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_d_mode2=0;
            }
            else if(data.equals("1"))
            {
                single_d_mode2=1;
            }
            else if(data.equals("2"))
            {
                single_d_mode2=2;
            }

            plc=plc.substring(2);

            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_kz_mode2=0;
            }
            else if(data.equals("1"))
            {
                single_kz_mode2=1;
            }
            else
            {
                single_kz_mode2=2;
            }

            plc=plc.substring(2);
            //电动门故障
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_gz_mode2=0;
            }
            else if(data.equals("1"))
            {
                single_gz_mode2=1;
            }
            else
            {
                single_gz_mode2=2;
            }

            plc=plc.substring(2);

            //照明
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_zm_mode2=0;
                activityMainBinding.btLightG2.setImageResource(R.drawable.gui2_off);
            }
            else if(data.equals("1"))
            {
                single_zm_mode2=1;
                activityMainBinding.btLightG2.setImageResource(R.drawable.gui2_on);
            }


            plc=plc.substring(2);
            //锁定
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_sd_mode2=0;
                activityMainBinding.btlock2.setImageResource(R.drawable.lock);
            }
            else
            {
                single_sd_mode2=1;
                activityMainBinding.btlock2.setImageResource(R.drawable.lockon);
            }

            plc=plc.substring(2);
            //回流模式
            datas=plc.substring(0,2);
            data=ByteUtils.hexToDec(datas);
            if(data.equals("0"))
            {
                single_h_mode2=0;
            }
            else if(data.equals("1"))
            {
                single_h_mode2=1;
            }
            else
            {
                single_h_mode2=2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  void Check_plc(String plc)
    {
        plc=plc.substring(4);
        String data="",datas="";
            try {
                datas = plc.substring(0, 2);
                data = ByteUtils.hexToDec(datas) + "度";
                activityMainBinding.txtJD.setText("外排角度：" + data);

                plc = plc.substring(2);

                datas = plc.substring(0, 2);
                data = ByteUtils.hexToDec(datas) + "0CMH";
                activityMainBinding.txtWFL.setText("外排风量：" + data);

                plc = plc.substring(4);

                datas = plc.substring(0, 2);
                data = ByteUtils.hexToDec(datas) + "Pa";
                activityMainBinding.txtYL.setText("房间压差：-" + data);

                plc = plc.substring(2);

                datas = plc.substring(0, 2);
                data = ByteUtils.hexToDec(datas) + "0CMH";
                activityMainBinding.txtNFL.setText("房间补风量：" + data);

                plc = plc.substring(2);

                //灯的状态
                datas = plc.substring(0, 2);
                data = ByteUtils.hexToDec(datas);
                light_fj = data;
                if(SetConfig.ReadCount==3) {
                    if (light_fj.equals("0")) {
                        activityMainBinding.btLight.setImageResource(R.drawable.light_off);
                    } else if (light_fj.equals("1")) {
                        activityMainBinding.btLight.setImageResource(R.drawable.light_on);
                    }
                }

                plc=plc.substring(2);

                datas=plc.substring(0,2);
                data=ByteUtils.hexToDec(datas)+"℃";
                activityMainBinding.txtWD.setText("房间温度："+data);

                plc=plc.substring(2);

                datas=plc.substring(0,2);
                data=ByteUtils.hexToDec(datas)+"RH%";
                activityMainBinding.txtSD.setText("房间湿度："+data);

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    private  void Init_Left_View()
    {
        room=getResources().getStringArray(R.array.vavroom);
        room_edv=getResources().getStringArray(R.array.edvroom1);
        for(int i=0;i<room.length;i++){
            room_string+=room[i].toString()+"!";
            dataP[i]="--";
            states[i]=true;
            dataH[i]="";
            dataT[i]="";
        }
        for(int i=0;i<room_edv.length;i++){
            roomedv_string+=room_edv[i].toString()+"!";
        }

        List<Map<String,Object>> listitem=new ArrayList<Map<String,Object>>();
        for(int i=0;i<room.length;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("indexmain",i+1+")");
            map.put("room",room[i]);
            map.put("dataP",dataP[i]);
            map.put("dataH",dataH[i]);
            map.put("dataT",dataH[i]);
            map.put("status",states[i]);
            listitem.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,listitem,R.layout.left_list,new String[]{"indexmain","room","dataJD"},new int[]{R.id.indexmain,R.id.room,R.id.dataJD});

        activityMainBinding.Llistview.setAdapter(adapter);
        activityMainBinding.Rlistview.setAdapter(adapter);
    }

    private  void Button_Delay()
    {
        SetConfig.LOAD_FIRST=false;
        handlerTimer_delay.removeCallbacks(runnable_delay);
        handlerTimer_delay.postDelayed(runnable_delay, 3000);
    }

    private  void Config_Button(String title, String content, final int button_index,final boolean On_Off)  //按钮开关弹出提示信息
    {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setIcon(R.drawable.xlb_log);
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "操作命令已取消",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Button_Delay();
                        switch (button_index)
                        {
                            case 1:  ///   紫外灯
                                if(!On_Off)
                                {
                                    binder.setData(4);
                                    dateDialog("1");
                                   // activityMainBinding.bottomRun0.setBackgroundResource(R.drawable.xlb_run_ziwai);
                                    ziwai_status1=true;
                                    Toast.makeText(MainActivity.this,"紫外灯开启中，请稍等！",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    binder.setData(5);
                                   // activityMainBinding.bottomRun0.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                                    ziwai_status1=false;
                                    Toast.makeText(MainActivity.this,"紫外灯关闭中，请稍等！",Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 2:  //紫外灯
                                if(!On_Off)
                                {
                                    binder.setData(10);
                                    dateDialog("2");
                                    //activityMainBinding.bottomRun1.setBackgroundResource(R.drawable.xlb_run_ziwai);
                                    ziwai_status2=true;
                                    Toast.makeText(MainActivity.this,"紫外灯开启中，请稍等！",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    binder.setData(11);
                                   // activityMainBinding.bottomRun1.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                                    ziwai_status2=false;
                                    Toast.makeText(MainActivity.this,"紫外灯关闭中，请稍等！",Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 3:  //紫外灯
                                if(!On_Off)
                                {
                                    binder.setData(12);
                                    dateDialog("3");
                                   // activityMainBinding.bottomRun2.setBackgroundResource(R.drawable.xlb_run_ziwai);
                                    ziwai_status3=true;
                                    Toast.makeText(MainActivity.this,"紫外灯开启中，请稍等！",Toast.LENGTH_LONG).show();

                                }
                                else {
                                    binder.setData(13);
                                   // activityMainBinding.bottomRun2.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                                    ziwai_status3=false;
                                    Toast.makeText(MainActivity.this,"紫外灯关闭中，请稍等！",Toast.LENGTH_LONG).show();

                                }
                                break;
                            case 4:  //紫外灯
                                if(!On_Off)
                                {
                                    binder.setData(14);
                                    dateDialog("4");
                                    //activityMainBinding.bottomRun3.setBackgroundResource(R.drawable.xlb_run_ziwai);
                                    ziwai_status4=true;
                                    Toast.makeText(MainActivity.this,"紫外灯开启中，请稍等！",Toast.LENGTH_LONG).show();

                                }
                                else {
                                    binder.setData(15);
                                   // activityMainBinding.bottomRun3.setBackgroundResource(R.drawable.xlb_stop_ziwai);
                                    ziwai_status4=false;
                                    Toast.makeText(MainActivity.this,"紫外灯关闭中，请稍等！",Toast.LENGTH_LONG).show();

                                }
                                break;
                            case 5:  //照明
                                if(!On_Off)
                                {
                                    binder.setData(2);
                                   // activityMainBinding.bottomRun4.setBackgroundResource(R.drawable.xlb_run_light);
                                    light_status=true;
                                    Toast.makeText(MainActivity.this,"照明开启中，请稍等！",Toast.LENGTH_LONG).show();

                                }
                                else {
                                    binder.setData(3);
                                   // activityMainBinding.bottomRun4.setBackgroundResource(R.drawable.xlb_stop_light);
                                    light_status=false;
                                    Toast.makeText(MainActivity.this,"照明关闭中，请稍等！！",Toast.LENGTH_LONG).show();

                                }
                                break;
                        }
                    }
                });
        alertDialog.show();
    }



}







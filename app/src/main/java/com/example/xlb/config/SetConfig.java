package com.example.xlb.config;

public class SetConfig {

    public static int SYSTEM_CONFIG_COUNT=0;
    public  enum SYSTEM_TYPE {
        SYSTEM_RUN, SYSTEM_START, SYSTEM_STOP, SYSTEM_FAIL,SYSTEM_SUCCESS
    }

    public static boolean isNetworkSetting=false;
    public static int isNetworkCMD=0;
    public static String Name="移动方舱";//实验室名称
    public static String FJ="4";//风机数量
    public static String KT="华昱";//空调信息
    public static String serial=""; //设备号
    public  enum BGColor{
      value, bl,wh
    }
    // edv设备、VAV设备、空调有多少个组
    public  static int EDV_COUNT=4;
    public  static int VAV_COUNT=1;
    public  static int SF_COUNT=1;
    public  static int PF_COUNT=4;
    public  static int KT_COUNT=1;
    public static int EDV_TOTAL=13;
    public static int EDV_START_INDEX=10;   //EDV伐开始的位置序号
    public static int EDV_END_INDEX=15;     //EDV伐结束的位置序号
    public static int VAV_END_INDEX=EDV_START_INDEX-1;   //VAV伐结束的位置序号
    public static int VAV_TOTAL=11;
    //读取模式
    //public static int[] MODE_READ_COUNT= {4,1,1,1,2,1};
    public static int[] MODE_READ_COUNT= {2,1};
    public static int MODE1_READ_COUNT=3;  //模块一读取次数
    public static int MODE2_READ_COUNT=2;  //模块二读取次数
    public static int MODE3_READ_COUNT=2;  //模块三读取次数
    public static int MODE4_READ_COUNT=1;  //模块四读取次数
    public static int MODE5_READ_COUNT=2;  //模块五读取次数


    //设置模式
    public static int[] MODE_SET_COUNT_ARR= {3,2,2,1,9,1};
    public static int MODE_SET_COUNT=1;
    public static int MODE1_SET_COUNT=11;  //模块一读取次数
    public static int MODE2_SET_COUNT=11;  //模块二读取次数
    public static int MODE3_SET_COUNT=2;  //模块三读取次数
    public static int MODE4_SET_COUNT=1;  //模块四读取次数
    public static int MODE5_SET_COUNT=9;  //模块五读取次数

   //  第一次加载数据

    public static boolean MODE1_FIRST=false;
    public static boolean MODE1_1_FIRST=false;
    public static boolean MODE2_FIRST=false;

    //Adapter加载
    public static boolean[] MODE_ADAPTER={false,false,false,false};
    public static boolean[] MODE_PL_ADAPTER={false,false,false,false};
    public static boolean ALL_MODE_ADAPTER=true;
    public static boolean MODE1_ADAPTER=true;
    public static boolean MODE2_ADAPTER=false;
    public static boolean MODE3_ADAPTER=false;
    public static boolean MODE4_ADAPTER=false;
    public static boolean MODE5_ADAPTER=false;
    public static boolean MODE6_ADAPTER=false;
    public static boolean MODE7_ADAPTER=false;
    public static boolean MODE8_ADAPTER=false;

    //变频器读取
    public static int[] FREQUENCY_READ_SETTING_COUNT={1,1,1,1,1};
    public static int FREQUENCY1_READ_SETTING_COUNT=1;
    public static int FREQUENCY2_READ_SETTING_COUNT=1;
    public static int FREQUENCY3_READ_SETTING_COUNT=1;
    public static int FREQUENCY4_READ_SETTING_COUNT=1;
    public static int FREQUENCY5_READ_SETTING_COUNT=1;

    //同步计数
    public static  int SYN=0;
    public static  int SYN_COUNT=0;

    // 排风设备读取序号
    public static int PF_INDEX=0;

    // 灯执行
    public static Boolean LIGHT_1=false;
    public static Boolean LIGHT_2=false;
    public static Boolean LIGHT_3=false;
    public static Boolean LIGHT_4=false;

    //
    private boolean is_Server=true;

    //服务器端口号和IP地址
    public static int port=8080;
    public static String ip_addres="192.168.0.88";

    public static final String INTENT_IP="192.168.0.88";//ip
    public static final int INTENT_PORT=8080;//port(端口号)

    // 定时
    public static Boolean OFF_1=false;
    public static Boolean OFF_2=false;
    public static Boolean OFF_3=false;
    public static Boolean OFF_4=false;

    // 是否第一次加载
    public  static  Boolean LOAD_FIRST=true;

    //系统断开
    public static boolean  SYS_DISCONNECTION=false;

    //  系统是否启动
    public static boolean SYS_STATUS=false;
    //  W_WAR  报警记录保存
    public static boolean W_WAR=true;
    public static boolean[] W_WAR_ARRY={true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};

    public static int ReadCount=0;
    public static boolean FirstRead=true;
    public static boolean ReadStatus=false;

    public static String PreStr="";

}

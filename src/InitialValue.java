/***************************************************
 ******************初期値の設定クラス******************
 ***************************************************/

public class InitialValue {
    //使用機器の設定
    public final static int LIGHT_NUM=12;				          //照明数
    public final static int DIVIDE=5; 							    //照明分割数
    public final static int SENSOR_NUM=12;							//センサ数

    //照明の設定
    public final static int INITIAL_SIGNAL=10;							//初期調光信号値
    public final static double  INITIAL_CD = 1000;						//初期点灯光度
    public final static double  MAX_CD = 1200;							//最大点灯光度
    public final static double  MIN_CD = 0;								//最小点灯光度

    //照度センサの設定
    public final static int MAX_TARGET_LX = 1000;						//目標照度の上限値
    public final static int MIN_TARGET_LX = 200;						//目標照度の下限値

    //部屋の座標
    public final static int KC_X=600/5+1;								//kc111x座標
    public final static int KC_Y=480/5+1;		    					//kc111y座標

    //シミュレーション条件
    public final static int DIRE_COUNT=1;								//方向を変える回数
    public final static int SIMULATION_COUNT=1;							//方向を変える回数

    //最急降下法
    public final static int MAX_STEP =250;								//最大ステップ数
    public final static double EPS = 0.00001;							//ε：十分に小さな値
    public final static double TAU = (1+Math.sqrt(5))/2;		       	//τ：黄金分割法に使用
    public final static double ALPHA = 0.4257;							//消費電力の計算に使うα，とりあえず1に設定
    public final static double BETA = 0.3716;					    	//消費電力の計算に使うβ，とりあえず1に設定
    public final static double WEIGHT = 1;								//目的関数の重み．とりあえず1に設定
    public final static double INITIAL_X1 = 0;
    public final static double INITIAL_STEP_SIZE = 3;

    //ファイルパス
    public static String LX_PATH="Lx_log.csv";
    public static String SDM_PATH="SDM_log.csv";
    public static String LX_DIFF_PATH="Lx_diff_log.csv";
    public static String SIGNAL_PATH="Signal_log.csv";
    public static String DIRECTION_PATH="Direction_log.csv";
    public static String POWER_PATH="Power_log.csv";
    public static String TARGET_ILL_PATH="target_ill.csv";
    public static String ERROR_RATE_PATH="error_rate.csv";
    public static String LAST_DIRECTION_PATH="last_target_ill.csv";
    public static String CHANGE_ILL_PATH="change_erro_rate.csv";
    public static String ALL_LX_PATH="all_lx.csv";
}
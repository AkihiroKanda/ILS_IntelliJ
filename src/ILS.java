import java.util.Random;

public class ILS {
    Light[][] light = new Light[InitialValue.LIGHT_NUM][InitialValue.DIVIDE];
    Sensor[] sensor = new Sensor[InitialValue.SENSOR_NUM];
    private int sensor_num = 0; 			//使用センサ数
    private static double Function[]={-1,0};	//0:最適解（最小値），1:現在
    private int lightch=0;	//乱数，配光方向を変化する照明
    private int dividech=0;	//乱数，配光方向を変化させる分割面
    private double power[]={0,0};		//消費電力を格納

    //lightオブジェクトとsensorオブジェの作成
    public ILS() {
        for (int i = 0; i < InitialValue.LIGHT_NUM; i++) {
            for(int j = 0; j < InitialValue.DIVIDE; j++)
                light[i][j] = new Light( i , j );		//引数：照明ID
        }

        for (int i = 0; i < sensor.length; i++) {
            sensor[i] = new Sensor(i);		//引数：センサID
        }
    }

    //照度センサの追加
    public void add_newSensor(int x, int y, double LX) {
        if (sensor_num < InitialValue.SENSOR_NUM) {
            sensor[sensor_num].set_Target_LX(LX);
            sensor[sensor_num].set_Sensor_X(x);
            sensor[sensor_num].set_Sensor_Y(y);
            sensor_num++;
        }else{
            System.err.println("cannot add a new sensor");
        }
    }

    //影響度係数の読み込み
    public void readInf(){
        for (int i = 0; i < InitialValue.LIGHT_NUM; i++) {
            for(int j = 0; j < InitialValue.DIVIDE; j++)
                light[i][j].read_inf(sensor);;		//引数：照明ID
        }
    }
    //影響度係数の合計（照明1灯分の影響度の算出）
    public void sum_Inf() {
        for(int i=0;i<InitialValue.LIGHT_NUM;i++){
            for(int j=0;j<InitialValue.SENSOR_NUM;j++){
                double sum=0;
                for(int k=0;k<InitialValue.DIVIDE;k++){
                    sum+=light[i][k].get_Inf(light[i][k].get_dire(), j);
                }
                light[i][0].set_Influence_deg(j, sum);
            }
        }
/*		for (int i = 0; i < InitialValue.LIGHT_NUM; i++) {
			for(int j = 0; j < InitialValue.SENSOR_NUM; j++){
				System.out.println("Light"+i+",Sensor"+j+":"+light[i][0].get_Influence_deg(j));
			}
		}*/
    }

    //点灯光度・色度の最適化
    public void Optimization(Logger log,int step) {
        //最急降下法を使い照度と色温度の最適化を行うメソッド
        SDM.mainSDM_CD(light, sensor,log,step);
    }

    //目的関数の評価
    public void evaluation_Func(Logger log,int step,int simu_num) {
        Function[1]=SDM.get_func();
        if(Function[0]==-1) {
            Function[0]=Function[1];
            for(int i=0;i<InitialValue.SENSOR_NUM;i++)	sensor[i].set_Optimal_LX();
            log.direction_log(step, light);
            //log.lx_diff_log(step, sensor);
            //log.lx_log(step, sensor);
            //log.signal_log(step, light);
        }
        else if(Function[1]<Function[0]) {
            Function[0]=Function[1];
            for(int i=0;i<InitialValue.SENSOR_NUM;i++)	sensor[i].set_Optimal_LX();
            log.direction_log(step, light);
            //log.lx_diff_log(step, sensor);
            //log.lx_log(step, sensor);
            //log.signal_log(step, light);
        }
        else light[lightch][dividech].reset_dire();		//配光方向のリセット
        System.out.println(simu_num+":"+Function[0]+":"+Function[1]);
    }

    //次配光方向の決定
    public void next_Dire() {
        Random rnd=new Random(); //乱数
        lightch=rnd.nextInt(InitialValue.LIGHT_NUM);//方向を変える照明番号
        dividech=rnd.nextInt(InitialValue.DIVIDE-1);//方向を変える分割番号
        light[lightch][dividech].set_dire(rnd.nextInt(2)+1);
    }

    //光度値と色温度値を信号値に変換
    public void Signal_conversion(){

    }
    //目標照度ログ
    public void Target_ill(Logger log) {
        log.target_ill(sensor);
    }

    //消費電力
    public void Power(Logger log,int count,int classify){
        if(classify == 0){
            power[0]=0;
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                power[0]+=InitialValue.ALPHA*(light[i][0].get_CD()/12)+InitialValue.BETA;
            }
        }
        else{
            power[1]=0;
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                power[1]+=InitialValue.ALPHA*(light[i][0].get_CD()/12)+InitialValue.BETA;
            }
            log.power_log(count,power);
        }
    }

    //平均照度誤差率ログ
    public void Error_rate(Logger simu_log,int count,int classify) {
        simu_log.lx_diff_log(count, classify, sensor);
        simu_log.error_rate_log(count, classify, sensor);
        if(classify==1)Function[0]=-1;
    }

    //配光方向ログ
    public void result_dire(Logger simu_log,int count,int classify) {
        simu_log.last_direction_log(count,  light);
        //if(classify==1)Function[0]=-1;
    }

    //表示(センサ)
    public void show_Sensor() {
        System.out.println("SHOW SENSOR VALUE=============");
        System.out.println("==============================");
        for (int i = 0; i < sensor.length; i++) {
            System.out.println("Sensor"+(sensor[i].get_ID()+1));
            System.out.println("Target LX	: "+Math.round(sensor[i].get_Target_LX())+" lx");
            System.out.println("Current LX	: "+Math.round(sensor[i].get_Optimal_LX())+" lx");
            System.out.println("============================");
        }
    }

    //ランダムな目標照度の変更
    public void change_Target_Ill(int simu_num,Logger log,int step) {
        int changeIllSensorNum=2;//目標照度を変更するセンサの数
        Random rnd=new Random(); //乱数
        int[] senRan=new int[changeIllSensorNum];
        int[] illRan=new int[changeIllSensorNum];
        for(int i=0;i<changeIllSensorNum;i++)senRan[i]=rnd.nextInt(12);
        log.change_Ill_log(simu_num, sensor,senRan);//変更前の照度誤差率
        System.out.println("目標照度");
        for(int i=0;i<changeIllSensorNum;i++){
            for(;;){
                illRan[i]=300+200*rnd.nextInt(3);
                if(illRan[i]!=sensor[senRan[i]].get_Target_LX()){
                    sensor[senRan[i]].set_Target_LX(illRan[i]);
                    break;
                }
            }
            System.out.println("change sensor:"+(senRan[i]+1)+"，目標照度:"+illRan[i]);
        }
        Target_ill(log);
        log.lx_log(simu_num, sensor);
        log.signal_log(simu_num, light);
        Optimization(log,-1);		//光度の最適化
        evaluation_Func(log,step,simu_num);		//目的関数の評価
        show_Sensor();	//提案手法照度表示
        log.lx_log(simu_num, sensor);
        log.signal_log(simu_num, light);
        log.change_Ill_log(simu_num, sensor,senRan);//変更前の照度誤差率

    }
    /*****************************初期配光方向の設定***********************************/
    public void setdire(int dir[]){
        for(int i=0;i<InitialValue.LIGHT_NUM;i++){
            for(int j=0;j<InitialValue.DIVIDE-1;j++){
                light[i][j].set_dire(dir[i*4+j]);
                //System.out.print(dir[i*4+j]);
            }
            //System.out.println();
        }
/*		light[0][0].set_dire(0);
		light[0][1].set_dire(2);
		light[0][2].set_dire(0);
		light[0][3].set_dire(0);
		light[1][0].set_dire(1);
		light[1][1].set_dire(1);
		light[1][2].set_dire(1);
		light[1][3].set_dire(2);
		light[2][0].set_dire(2);
		light[2][1].set_dire(1);
		light[2][2].set_dire(1);
		light[2][3].set_dire(1);
		light[3][0].set_dire(2);
		light[3][1].set_dire(0);
		light[3][2].set_dire(2);
		light[3][3].set_dire(1);
		light[4][0].set_dire(2);
		light[4][1].set_dire(2);
		light[4][2].set_dire(1);
		light[4][3].set_dire(2);
		light[5][0].set_dire(1);
		light[5][1].set_dire(0);
		light[5][2].set_dire(0);
		light[5][3].set_dire(0);
		light[6][0].set_dire(0);
		light[6][1].set_dire(0);
		light[6][2].set_dire(0);
		light[6][3].set_dire(0);
		light[7][0].set_dire(0);
		light[7][1].set_dire(0);
		light[7][2].set_dire(0);
		light[7][3].set_dire(0);
		light[8][0].set_dire(1);
		light[8][1].set_dire(2);
		light[8][2].set_dire(1);
		light[8][3].set_dire(1);
		light[9][0].set_dire(2);
		light[9][1].set_dire(2);
		light[9][2].set_dire(0);
		light[9][3].set_dire(2);
		light[10][0].set_dire(1);
		light[10][1].set_dire(1);
		light[10][2].set_dire(0);
		light[10][3].set_dire(1);
		light[11][0].set_dire(0);
		light[11][1].set_dire(0);
		light[11][2].set_dire(0);
		light[11][3].set_dire(0);*/
    }
    public void calcAveLx(int allDire[][], Logger log, int simu_num){
        calcAve aveLx = new calcAve(allDire,light,log,sensor,simu_num);
        aveLx.calcAveLx(sensor,log);
    }

}
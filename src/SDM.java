public class SDM {
    private static int step = 0;	//現在のステップ数
    private static double stepsize = 0.0;		//α：ステップ幅
    private static double[] Ans_CD = new double[InitialValue.LIGHT_NUM] ;
    //private static double[] Ans_M = new double[InitialValue.LIGHT_NUM];
    private static double[] Gradient_vector = new double[InitialValue.LIGHT_NUM];		//勾配ベクトル
    private static double[] Drop_direction = new double[InitialValue.LIGHT_NUM];		//降下方向
    private static double[] function = new double [2];

    //照度の最適化
    public static void mainSDM_CD(Light light[][], Sensor sensor[],Logger log,int num) {
        Initialization();			//初期化
        Calc_current_LX(light, sensor);		//初期光度における照度値計算
        if(num==0)log.SDM_log(step, sensor);

        while (true) {
            step++;
            Calc_gradient_vector(light, sensor);		//勾配ベクトルの計算
            Calc_drop_direction();							//降下方向の計算
            stepsize = Calc_stepsize(light, sensor);	//ステップ幅αの計算
            Ans_CD = Check_max_min(Calc_next_value(stepsize, light));
            for (int i = 0; i < light.length; i++) {
                light[i][0].set_CD(Ans_CD[i]);
                //System.out.println("CD:"+Ans_CD[i]);
            }
            Calc_current_LX(light, sensor);		//求めた光度値での各照度センサの現在照度の計算
            if(num==0)log.SDM_log(step, sensor);

            if (step == InitialValue.MAX_STEP) {
                break;
            }
        }
    }
    //初期化
    private static void Initialization(){
        step = 0;
        stepsize = 0.0;
    }

    //目的関数の取得
    public static double get_func() {
        return (function[0]+function[1])/2;
    }

    //照度センサの現在照度の計算
    public static void Calc_current_LX(Light light[][], Sensor sensor[]) {
        for(int sensor_num=0;sensor_num<InitialValue.SENSOR_NUM;sensor_num++){
            for(int light_num=0;light_num<InitialValue.LIGHT_NUM;light_num++){
                sensor[sensor_num].set_Each_Current_LX(light_num,
                        light[light_num][0].get_Influence_deg(sensor_num) * light[light_num][0].get_CD());
            }
        }
    }

    //勾配ベクトルの計算
    private static void Calc_gradient_vector(Light light[][], Sensor sensor[]){
        double[] RRL = new double[InitialValue.LIGHT_NUM];		//偏微分値を格納する配列
        //照度に関数る制約条件項の光度による偏微分値
        for(int light_num=0;light_num<InitialValue.LIGHT_NUM;light_num++){
            for(int sensor_num=0;sensor_num<InitialValue.SENSOR_NUM;sensor_num++){
                RRL[light_num] += light[light_num][0].get_Influence_deg(sensor_num)
                        * (sensor[sensor_num].get_Current_LX() - sensor[sensor_num].get_Target_LX());
            }
        }
        //目的関数の各照明の光度に関数偏導関数
        for(int light_num=0;light_num<InitialValue.LIGHT_NUM;light_num++){
            Gradient_vector[light_num] = InitialValue.ALPHA + 2 * InitialValue.WEIGHT * RRL[light_num];
        }
    }

    //降下方向の計算
    private static void Calc_drop_direction(){
        for(int light_num=0;light_num<InitialValue.LIGHT_NUM;light_num++){
            Drop_direction[light_num] = -1 * Gradient_vector[light_num];
        }
    }

    //ステップ幅αの計算
    private static double Calc_stepsize(Light light[][], Sensor sensor[]){
        double h = InitialValue.INITIAL_STEP_SIZE;
        double TAU = InitialValue.TAU;
        double x1 = InitialValue.INITIAL_X1;
        double x2 = x1 + h;
        double x3 = x1 + ((TAU-1)/TAU) * (x2 - x1);
        double x4 = x1 + (1/TAU) * (x2 - x1);

        while((x2-x1)>InitialValue.EPS){
            function[0]=0;
            function[1]=0;
            function[0] = Calc_function_CD(x3, light, sensor);
            function[1] = Calc_function_CD(x4, light, sensor);
            //System.out.println("Function1:"+func1+",Function2:"+func2);
            if(function[0]>function[1]){
                x1 = x3;
                //x2 = x2;
                x3 = x1 + ((TAU-1)/TAU) * (x2 - x1);
                x4 = x1 + (1/TAU) * (x2 - x1);
            }
            else{
                //x1 = x1;
                x2 = x4;
                x3 = x1 + ((TAU-1)/TAU) * (x2 - x1);
                x4 = x1 + (1/TAU) * (x2 - x1);
            }
        }
        //System.out.println(x3);
        return x3;
    }

    //目的関数の計算(照度についての)
    private static double Calc_function_CD(double stepsize,Light light[][],Sensor sensor[]){
        double temp_CD[];
        temp_CD =  Check_max_min(Calc_next_value(stepsize, light));
        double[] now_ill = new double[InitialValue.SENSOR_NUM];
        double func = 0.0;
        int power = 0;
        double penalty = 0.0;
        for(int i=0;i<InitialValue.LIGHT_NUM;i++){
            power += temp_CD[i];
        }
        for(int j=0;j<InitialValue.SENSOR_NUM;j++){
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                now_ill[j] += temp_CD[i]*light[i][0].get_Influence_deg(j);
            }
        }
        for(int j=0;j<InitialValue.SENSOR_NUM;j++){
            penalty += Math.pow(now_ill[j]-sensor[j].get_Target_LX(),2);
        }
        func = power + InitialValue.WEIGHT * penalty;

        return func;
    }

    //次光度の計算
    private static double[] Calc_next_value(double stepsize, Light light[][]){
        double Next_value[] = new double[InitialValue.LIGHT_NUM];
        for(int i=0;i<InitialValue.LIGHT_NUM;i++){
            Next_value[i] = light[i][0].get_CD() + stepsize * Drop_direction[i];
        }
        return Next_value;
    }

    //光度値の最小値最大値のチェック
    private static double[] Check_max_min(double nextValue[]){
        for(int i=0;i<InitialValue.LIGHT_NUM;i++){
            if (nextValue[i] > InitialValue.MAX_CD) nextValue[i] = InitialValue.MAX_CD;
            else if (nextValue[i] < InitialValue.MIN_CD) nextValue[i] = InitialValue.MIN_CD;
        }
        return nextValue;
    }
}

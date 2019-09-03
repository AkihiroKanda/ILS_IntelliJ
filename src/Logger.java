import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class Logger {
    //出力先を作成する
    private FileWriter fw;
    private PrintWriter pw;
    private double[][] data_error_rate=new double[InitialValue.SIMULATION_COUNT][2];//０：標準手法，１：提案手法


    public Logger(){
        //日時取得
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");

        //フォルダ名が日時のディレクトリ作成
        String path="./log/7MM_12_"+sdf.format(now.getTime());
        File newdir = new File(path);
        newdir.mkdir();

        //ファイル名
        InitialValue.LX_PATH=path+"/"+InitialValue.LX_PATH;
        InitialValue.SIGNAL_PATH=path+"/"+InitialValue.SIGNAL_PATH;
        InitialValue.SDM_PATH=path+"/"+InitialValue.SDM_PATH;
        InitialValue.LX_DIFF_PATH=path+"/"+InitialValue.LX_DIFF_PATH;
        InitialValue.DIRECTION_PATH=path+"/"+InitialValue.DIRECTION_PATH;
        InitialValue.POWER_PATH=path+"/"+InitialValue.POWER_PATH;
        InitialValue.TARGET_ILL_PATH=path+"/"+InitialValue.TARGET_ILL_PATH;
        InitialValue.ERROR_RATE_PATH=path+"/"+InitialValue.ERROR_RATE_PATH;
        InitialValue.LAST_DIRECTION_PATH=path+"/"+InitialValue.LAST_DIRECTION_PATH;
        InitialValue.CHANGE_ILL_PATH=path+"/"+InitialValue.CHANGE_ILL_PATH;
        InitialValue.ALL_LX_PATH=path+"/"+InitialValue.ALL_LX_PATH;
    }

    /*********** 照度履歴をCSVファイル出力  lx_log  **************************/
    public void lx_log(int step,Sensor sen[]){
        //出力先を作成する
        try {
            fw = new FileWriter(InitialValue.LX_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));
            if(step==0){
                pw.print(",");
                for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                    pw.print("sensor"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }

            pw.print(step+1);
            pw.print(",");
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                pw.print(sen[i].get_Current_LX());
                pw.print(",");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********** SDM照度履歴をCSVファイル出力  shc_log  **************************/
    public void SDM_log(int step,Sensor sen[]){
        //出力先を作成する
        try {
            fw = new FileWriter(InitialValue.SDM_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));
            if(step==0){
                pw.print(",");
                for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                    pw.print("sensor"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }

            pw.print(step+1);
            pw.print(",");
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                pw.print(sen[i].get_Current_LX());
                pw.print(",");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********** 現在照度と目標照度の照度差履歴をCSVファイル出力  lx_diff_log  **************************/
    public void lx_diff_log(int count,int classify,Sensor sen[]){
        try {
            fw = new FileWriter(InitialValue.LX_DIFF_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));
            if(count==0&&classify==0){
                pw.print(",");
                for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                    pw.print("sensor"+(i+1));
                    pw.print(",");
                }
                for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                    pw.print("sensor"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }
            if(classify==0)pw.print((count+1)+",");
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                double diff=Math.abs(sen[i].get_Optimal_LX()-sen[i].get_Target_LX());
                pw.print(diff);
                pw.print(",");
            }
            if(classify==1)pw.println();

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*********** 調光信号値履歴をCSVファイル出力  signal_log  **************************/
    public void signal_log(int step,Light light[][]){
        try {
            fw = new FileWriter(InitialValue.SIGNAL_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            if(step==0){
                pw.print(",");
                for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                    pw.print("light"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }

            pw.print(step+1);
            pw.print(",");
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                pw.print(light[i][0].get_CD());
                pw.print(",");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********** 配光方向履歴をCSVファイル出力  direction_log  **************************/
    public void direction_log(int step,Light light[][]){
        try {
            fw = new FileWriter(InitialValue.DIRECTION_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            if(step==0){
                pw.print(",");
                for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                    pw.print("light"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }

            pw.print(step+1);
            pw.print(",");
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                for(int j=0;j<InitialValue.DIVIDE-1;j++){
                    if(light[i][j].get_dire()==0)pw.print("Mid:");
                    else if(light[i][j].get_dire()==1)	pw.print("Up:");
                    else pw.print("Down:");
                }
                pw.print(",");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********** 消費電力をCSVファイル出力  power_log  **************************/
    public void power_log(int step,double power[]){
        try {
            fw = new FileWriter(InitialValue.POWER_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            if(step==0){
                pw.print(",");
                pw.print("power consumption[W]");
                pw.println();
                pw.print(",");
                pw.print("standard,proposed");
                pw.println();
            }
            pw.print(step+1);
            pw.print(",");
            pw.print(power[0]);
            pw.print(",");
            pw.print(power[1]);
            pw.print(",");
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*********** 目標照度をCSVファイル出力  power_log  **************************/
    public void target_ill(Sensor sen[]){
        try {
            fw = new FileWriter(InitialValue.TARGET_ILL_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                pw.print("Sensor"+(i+1));
                pw.print(",");
            }
            pw.println();
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                pw.print(sen[i].get_Target_LX());
                pw.print(",");
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*********** 現在照度と目標照度の照度誤差率をCSVファイル出力  lx_diff_log  **************************/
    public void error_rate_log(int count,int classify, Sensor sen[]){
        //出力先を作成する
        try {
            fw = new FileWriter(InitialValue.ERROR_RATE_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));
            if(count==0&&classify==0){
                pw.print(",error_rate");
                pw.println();
            }

            double error_rate_sum=0;
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                error_rate_sum+=(Math.abs(sen[i].get_Optimal_LX()-sen[i].get_Target_LX()))/sen[i].get_Target_LX();
            }
            data_error_rate[count][classify]=error_rate_sum/InitialValue.SENSOR_NUM;
            if(classify==1){
                pw.print((count+1)+","+data_error_rate[count][0]+","+data_error_rate[count][1]);
                pw.print(",");
                pw.println();
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*********** 最終配光方向履歴をCSVファイル出力  direction_log  **************************/
    public void last_direction_log(int simu_num,Light light[][]){
        try {
            fw = new FileWriter(InitialValue.LAST_DIRECTION_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            if(simu_num==0){
                pw.print(",");
                for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                    pw.print("light"+(i+1));
                    pw.print(",");
                }
                pw.println();
            }

            pw.print(simu_num+1);
            pw.print(",");
            for(int i=0;i<InitialValue.LIGHT_NUM;i++){
                for(int j=0;j<InitialValue.DIVIDE-1;j++){
                    if(light[i][j].get_dire()==0)pw.print("0,");
                    else if(light[i][j].get_dire()==1)pw.print("1,");
                    else pw.print("2,");
                    System.out.print(light[i][j].get_dire());
                }
                //pw.print(",");
                System.out.println();
            }
            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*********** 変更目標照度の平均照度誤差をCSVファイル出力  direction_log  **************************/
    public void change_Ill_log(int simu_num,Sensor sensor[],int senRan[]){
        try {
            fw = new FileWriter(InitialValue.CHANGE_ILL_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            double[] errorRate=new double[senRan.length];
            errorRate[0]=Math.abs(sensor[senRan[0]].get_Target_LX()-sensor[senRan[0]].get_Current_LX())/sensor[senRan[0]].get_Target_LX();
            errorRate[1]=Math.abs(sensor[senRan[1]].get_Target_LX()-sensor[senRan[1]].get_Current_LX())/sensor[senRan[1]].get_Target_LX();
            pw.print(simu_num+1);
            pw.print(",Sensor"+(senRan[0]+1)+","+sensor[senRan[0]].get_Target_LX()+","+sensor[senRan[0]].get_Current_LX()+","+errorRate[0]);
            pw.println();

            pw.print(simu_num+1);
            pw.print(",Sensor"+(senRan[1]+1)+","+sensor[senRan[1]].get_Target_LX()+","+sensor[senRan[1]].get_Current_LX()+","+errorRate[1]);

            pw.println();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*********** 部屋全体の照度をCSVファイル出力  ave_lx_log  **************************/
    public void all_lx_log(double allLx[][]){
        try {
            fw = new FileWriter(InitialValue.ALL_LX_PATH, true);  //※１true:追記．false:上書き
            pw = new PrintWriter(new BufferedWriter(fw));

            for (int j=0; j<InitialValue.KC_X; j++) {
                pw.print(","+j*5);
            }
            pw.println();
            for (int i = InitialValue.KC_Y-1; i>=0; i--) {
                pw.print((i*5)+",");
                for (int j=0; j<InitialValue.KC_X; j++) {
                    pw.print(allLx[i][j]+",");
                }
                pw.println();
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
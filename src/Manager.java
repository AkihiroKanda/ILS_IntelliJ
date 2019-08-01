import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Manager {
    static int[][] allTargetIll = new int[InitialValue.SIMULATION_COUNT][InitialValue.SENSOR_NUM]; //目標照度csvデータ格納配列
    static int[][] allDire = new int[InitialValue.SIMULATION_COUNT][InitialValue.LIGHT_NUM*4]; //配光方向csvデータ格納配列

    public static void main(String[] args){
        //ILS[] ils = new ILS[InitialValue.SIMULATION_COUNT]; //照明ログの出力するオブジェクト
        read_Target_Ill();	//目標照度の読み込み
        read_Dire();//配光方向の読み込み
        Logger log = new Logger();
        ILS[] ils = new ILS[InitialValue.SIMULATION_COUNT];

        for(int i=0;i<InitialValue.SIMULATION_COUNT;i++){
            ils[i]=new ILS();
            calc_ILS(ils[i],log,i);
        }

    }

    public static void calc_ILS(ILS ils,Logger log,int simu_num) {
        /************************ログ出力ジェクトの初期設定****************************/
        //log = new Logger(); //照明ログの出力するオブジェクト
        //ILS ils=new ILS();
        //センサの設定
        //set_Target_Ill(ils,simu_num);		//目標照度自動設定
        set_Target_Ill_self(ils);		//目標照度，配光方向入力
        ils.Target_ill(log);	//目標照度ログ
        //ils.setdire(allDire[simu_num]);//配光方向の決定

        //影響度係数の取得
        ils.readInf();
        ils.sum_Inf();		//影響度係数の合計
//		long start = System.currentTimeMillis();//タイム測定
        ils.Optimization(log,-1);		//光度の最適化  0:SDMのログをとる
        ils.evaluation_Func(log,0,simu_num);		//目的関数の評価
        ils.show_Sensor();	//照度表示
        ils.Error_rate(log, simu_num, 0);//標準手法平均照度誤差率
        ils.Power(log, simu_num, 0);//消費電力標準手法

        //ランダムな目標照度の変更
        //for (int i = 0; i < 1; i++) {
        //	ils.change_Target_Ill(simu_num,log,i);
        //}

/*		for (int i = 0; i < InitialValue.DIRE_COUNT; i++) {
			ils.next_Dire();//次配光方向の決定
			ils.sum_Inf();		//影響度係数の合計
			ils.Optimization(log,-1);		//光度の最適化
			ils.evaluation_Func(log,i,simu_num);		//目的関数の評価
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start)  + "ms");//time測定終了*/
        ils.Error_rate(log, simu_num, 1);//提案手法平均照度誤差率
        ils.Power(log, simu_num, 1);//消費電力提案手法
        ils.result_dire(log, simu_num, 1); //最終配光方向
        //ils.show_Sensor();	//提案手法照度表示
    }

    public static void set_Target_Ill(ILS ils,int simu_num) {
        Random rnd=new Random(); //乱数
        int[] ran=new int[InitialValue.SENSOR_NUM];
        System.out.println("目標照度");
        for(int i=0;i<InitialValue.SENSOR_NUM;i++){
            ran[i]=300+200*rnd.nextInt(3);
            System.out.print(ran[i]+":");
        }
        System.out.println();
        //各センサの目標照度・色温度の設定part1
/*		ils.add_newSensor(85/5, 150/5, ran[0]);
		ils.add_newSensor(85/5, 270/5, ran[1]);
		ils.add_newSensor(85/5, 390/5, ran[2]);
		ils.add_newSensor(155/5, 150/5, ran[3]);
		ils.add_newSensor(155/5, 270/5, ran[4]);
		ils.add_newSensor(155/5, 390/5, ran[5]);
		ils.add_newSensor(375/5, 150/5, ran[6]);
		ils.add_newSensor(375/5, 270/5, ran[7]);
		ils.add_newSensor(375/5, 390/5, ran[8]);
		ils.add_newSensor(445/5, 150/5, ran[9]);
		ils.add_newSensor(445/5, 270/5, ran[10]);
		ils.add_newSensor(445/5, 390/5, ran[11]);
*/
        //各センサの目標照度・色温度の設定part2
        ils.add_newSensor(85/5, 150/5, allTargetIll[simu_num][0]);
        ils.add_newSensor(85/5, 270/5, allTargetIll[simu_num][1]);
        ils.add_newSensor(85/5, 390/5, allTargetIll[simu_num][2]);
        ils.add_newSensor(155/5, 150/5, allTargetIll[simu_num][3]);
        ils.add_newSensor(155/5, 270/5, allTargetIll[simu_num][4]);
        ils.add_newSensor(155/5, 390/5, allTargetIll[simu_num][5]);

        ils.add_newSensor(375/5, 150/5, allTargetIll[simu_num][6]);
        ils.add_newSensor(375/5, 270/5, allTargetIll[simu_num][7]);
        ils.add_newSensor(375/5, 390/5, allTargetIll[simu_num][8]);
        ils.add_newSensor(445/5, 150/5, allTargetIll[simu_num][9]);
        ils.add_newSensor(445/5, 270/5, allTargetIll[simu_num][10]);
        ils.add_newSensor(445/5, 390/5, allTargetIll[simu_num][11]);
        //各センサの目標照度・色温度の設定縦part1
/*		ils.add_newSensor(175/5, 150/5, allTargetIll[simu_num][0]);
 		ils.add_newSensor(175/5, 270/5, allTargetIll[simu_num][2]);
 		ils.add_newSensor(175/5, 390/5, allTargetIll[simu_num][4]);
 		ils.add_newSensor(245/5, 150/5, allTargetIll[simu_num][1]);
 		ils.add_newSensor(245/5, 270/5, allTargetIll[simu_num][3]);
 		ils.add_newSensor(245/5, 390/5, allTargetIll[simu_num][5]);*/

        //各センサの目標照度・色温度の設定縦part2
/*		ils.add_newSensor(85/5, 150/5, allTargetIll[simu_num][0]);
 		ils.add_newSensor(85/5, 270/5, allTargetIll[simu_num][2]);
 		ils.add_newSensor(85/5, 390/5, allTargetIll[simu_num][4]);
 		ils.add_newSensor(155/5, 150/5, allTargetIll[simu_num][1]);
 		ils.add_newSensor(155/5, 270/5,allTargetIll[simu_num][3]);
 		ils.add_newSensor(155/5, 390/5, allTargetIll[simu_num][5]);
*/
        //各センサの目標照度・色温度の設定 横part1
/*		ils.add_newSensor(90/5, 235/5, allTargetIll[simu_num][0]);
	 	ils.add_newSensor(90/5, 305/5, allTargetIll[simu_num][1]);
 		ils.add_newSensor(210/5, 235/5, allTargetIll[simu_num][2]);
 		ils.add_newSensor(210/5, 305/5, allTargetIll[simu_num][3]);
 		ils.add_newSensor(330/5, 235/5, allTargetIll[simu_num][4]);
 		ils.add_newSensor(330/5, 305/5, allTargetIll[simu_num][5]);*/

        //各センサの目標照度・色温度の設定 横part2
/*	 	ils.add_newSensor(90/5, 145/5, allTargetIll[simu_num][0]);
	 	ils.add_newSensor(90/5, 215/5, allTargetIll[simu_num][1]);
 		ils.add_newSensor(210/5, 145/5, allTargetIll[simu_num][2]);
 		ils.add_newSensor(210/5, 215/5, allTargetIll[simu_num][3]);
 		ils.add_newSensor(330/5, 145/5, allTargetIll[simu_num][4]);
 		ils.add_newSensor(330/5, 215/5,allTargetIll[simu_num][5]);*/


    }


    //目標照度の読み込み
    public static void read_Target_Ill() {
        try {
            String[][] data = new String[InitialValue.SIMULATION_COUNT][InitialValue.SENSOR_NUM]; //csvデータ格納配列
            File f = new File("./csv/target_ill_desk12.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            for (int ro = 0;ro<InitialValue.SIMULATION_COUNT; ro++) {
                data[ro] = line.split(",", 0);
                line = br.readLine();
            }
            br.close();

            // CSVから読み込んだ配列の中身を表示
            for(int i=0;i<InitialValue.SIMULATION_COUNT;i++){
                for(int j=0;j<InitialValue.SENSOR_NUM;j++){
                    allTargetIll[i][j]=Integer.parseInt(data[i][j]);
                    System.out.print(allTargetIll[i][j]+",");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    //配光方向の読み込み
    public static void read_Dire() {
        try {
            String[][] data = new String[InitialValue.SIMULATION_COUNT][InitialValue.LIGHT_NUM*4]; //csvデータ格納配列
            File f = new File("./csv/last_dir.csv");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            for (int ro = 0;ro<InitialValue.SIMULATION_COUNT; ro++) {
                data[ro] = line.split(",", 0);
                line = br.readLine();
            }
            br.close();

            // CSVから読み込んだ配列の中身を表示
            for(int i=0;i<InitialValue.SIMULATION_COUNT;i++){
                for(int j=0;j<InitialValue.LIGHT_NUM*4;j++){
                    allDire[i][j]=Integer.parseInt(data[i][j]);
                    //System.out.print(allDire[i][j]+",");
                }
                //System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public static void set_Target_Ill_self(ILS ils) {
        //各センサの目標照度・色温度の設定
        ils.add_newSensor(85/5, 150/5, 500);
        ils.add_newSensor(85/5, 270/5, 500);
        ils.add_newSensor(85/5, 390/5, 700);
        ils.add_newSensor(155/5, 150/5, 500);
        ils.add_newSensor(155/5, 270/5, 500);
        ils.add_newSensor(155/5, 390/5, 700);

        ils.add_newSensor(375/5, 150/5, 300);
        ils.add_newSensor(375/5, 270/5, 700);
        ils.add_newSensor(375/5, 390/5, 300);
        ils.add_newSensor(445/5, 150/5, 700);
        ils.add_newSensor(445/5, 270/5, 700);
        ils.add_newSensor(445/5, 390/5, 300);
    }
}


/*
ILS ils1=new ILS();
calc_ILS(ils1,log,0);
ILS ils2=new ILS();
calc_ILS(ils2,log,1);
ILS ils3=new ILS();
calc_ILS(ils3,log,2);
ILS ils4=new ILS();
calc_ILS(ils4,log,3);
ILS ils5=new ILS();
calc_ILS(ils5,log,4);
ILS ils6=new ILS();
calc_ILS(ils6,log,5);
ILS ils7=new ILS();
calc_ILS(ils7,log,6);
ILS ils8=new ILS();
calc_ILS(ils8,log,7);
ILS ils9=new ILS();
calc_ILS(ils9,log,8);
ILS ils10=new ILS();
calc_ILS(ils10,log,9);
ILS ils11=new ILS();
calc_ILS(ils11,log,10);
ILS ils12=new ILS();
calc_ILS(ils12,log,11);
ILS ils13=new ILS();
calc_ILS(ils13,log,12);
ILS ils14=new ILS();
calc_ILS(ils14,log,13);
ILS ils15=new ILS();
calc_ILS(ils15,log,14);
ILS ils16=new ILS();
calc_ILS(ils16,log,15);
ILS ils17=new ILS();
calc_ILS(ils17,log,16);
ILS ils18=new ILS();
calc_ILS(ils18,log,17);
ILS ils19=new ILS();
calc_ILS(ils19,log,18);
ILS ils20=new ILS();
calc_ILS(ils20,log,19);
ILS ils21=new ILS();
calc_ILS(ils21,log,20);
ILS ils22=new ILS();
calc_ILS(ils22,log,21);
ILS ils23=new ILS();
calc_ILS(ils23,log,22);
ILS ils24=new ILS();
calc_ILS(ils24,log,23);
ILS ils25=new ILS();
calc_ILS(ils25,log,24);
ILS ils26=new ILS();
calc_ILS(ils26,log,25);
ILS ils27=new ILS();
calc_ILS(ils27,log,26);
ILS ils28=new ILS();
calc_ILS(ils28,log,27);
ILS ils29=new ILS();
calc_ILS(ils29,log,28);
ILS ils30=new ILS();
calc_ILS(ils30,log,29);
ILS ils31=new ILS();
calc_ILS(ils31,log,30);
ILS ils32=new ILS();
calc_ILS(ils32,log,31);
ILS ils33=new ILS();
calc_ILS(ils33,log,32);
ILS ils34=new ILS();
calc_ILS(ils34,log,33);
ILS ils35=new ILS();
calc_ILS(ils35,log,34);
ILS ils36=new ILS();
calc_ILS(ils36,log,35);
ILS ils37=new ILS();
calc_ILS(ils37,log,36);
ILS ils38=new ILS();
calc_ILS(ils38,log,37);
ILS ils39=new ILS();
calc_ILS(ils39,log,38);
ILS ils40=new ILS();
calc_ILS(ils40,log,39);
ILS ils41=new ILS();
calc_ILS(ils41,log,40);
ILS ils42=new ILS();
calc_ILS(ils42,log,41);
ILS ils43=new ILS();
calc_ILS(ils43,log,42);
ILS ils44=new ILS();
calc_ILS(ils44,log,43);
ILS ils45=new ILS();
calc_ILS(ils45,log,44);
ILS ils46=new ILS();
calc_ILS(ils46,log,45);
ILS ils47=new ILS();
calc_ILS(ils47,log,46);
ILS ils48=new ILS();
calc_ILS(ils48,log,47);
ILS ils49=new ILS();
calc_ILS(ils49,log,48);
ILS ils50=new ILS();
calc_ILS(ils50,log,49);
ILS ils51=new ILS();
calc_ILS(ils51,log,50);
ILS ils52=new ILS();
calc_ILS(ils52,log,51);
ILS ils53=new ILS();
calc_ILS(ils53,log,52);
ILS ils54=new ILS();
calc_ILS(ils54,log,53);
ILS ils55=new ILS();
calc_ILS(ils55,log,54);
ILS ils56=new ILS();
calc_ILS(ils56,log,55);
ILS ils57=new ILS();
calc_ILS(ils57,log,56);
ILS ils58=new ILS();
calc_ILS(ils58,log,57);
ILS ils59=new ILS();
calc_ILS(ils59,log,58);
ILS ils60=new ILS();
calc_ILS(ils60,log,59);
ILS ils61=new ILS();
calc_ILS(ils61,log,60);
ILS ils62=new ILS();
calc_ILS(ils62,log,61);
ILS ils63=new ILS();
calc_ILS(ils63,log,62);
ILS ils64=new ILS();
calc_ILS(ils64,log,63);
ILS ils65=new ILS();
calc_ILS(ils65,log,64);
ILS ils66=new ILS();
calc_ILS(ils66,log,65);
ILS ils67=new ILS();
calc_ILS(ils67,log,66);
ILS ils68=new ILS();
calc_ILS(ils68,log,67);
ILS ils69=new ILS();
calc_ILS(ils69,log,68);
ILS ils70=new ILS();
calc_ILS(ils70,log,69);
ILS ils71=new ILS();
calc_ILS(ils71,log,70);
ILS ils72=new ILS();
calc_ILS(ils72,log,71);
ILS ils73=new ILS();
calc_ILS(ils73,log,72);
ILS ils74=new ILS();
calc_ILS(ils74,log,73);
ILS ils75=new ILS();
calc_ILS(ils75,log,74);
ILS ils76=new ILS();
calc_ILS(ils76,log,75);
ILS ils77=new ILS();
calc_ILS(ils77,log,76);
ILS ils78=new ILS();
calc_ILS(ils78,log,77);
ILS ils79=new ILS();
calc_ILS(ils79,log,78);
ILS ils80=new ILS();
calc_ILS(ils80,log,79);
ILS ils81=new ILS();
calc_ILS(ils81,log,80);
ILS ils82=new ILS();
calc_ILS(ils82,log,81);
ILS ils83=new ILS();
calc_ILS(ils83,log,82);
ILS ils84=new ILS();
calc_ILS(ils84,log,83);
ILS ils85=new ILS();
calc_ILS(ils85,log,84);
ILS ils86=new ILS();
calc_ILS(ils86,log,85);
ILS ils87=new ILS();
calc_ILS(ils87,log,86);
ILS ils88=new ILS();
calc_ILS(ils88,log,87);
ILS ils89=new ILS();
calc_ILS(ils89,log,88);
ILS ils90=new ILS();
calc_ILS(ils90,log,89);
ILS ils91=new ILS();
calc_ILS(ils91,log,90);
ILS ils92=new ILS();
calc_ILS(ils92,log,91);
ILS ils93=new ILS();
calc_ILS(ils93,log,92);
ILS ils94=new ILS();
calc_ILS(ils94,log,93);
ILS ils95=new ILS();
calc_ILS(ils95,log,94);
ILS ils96=new ILS();
calc_ILS(ils96,log,95);
ILS ils97=new ILS();
calc_ILS(ils97,log,96);
ILS ils98=new ILS();
calc_ILS(ils98,log,97);
ILS ils99=new ILS();
calc_ILS(ils99,log,98);
ILS ils100=new ILS();
calc_ILS(ils100,log,99);*/
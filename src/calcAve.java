import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*****************************机上面均斉度の計算***********************************/
public class calcAve {
    static double[][][][] allInf = new double[InitialValue.LIGHT_NUM][InitialValue.DIVIDE][InitialValue.KC_Y][InitialValue.KC_X]; //目標照度csvデータ格納配列
    static double [][][] joinInf = new double[InitialValue.LIGHT_NUM][InitialValue.KC_Y][InitialValue.KC_X];
    static double [][] allLx = new double [InitialValue.KC_Y][InitialValue.KC_X];
    private static String inputpath_lxcd;

    public calcAve(int allDire[][], Light light[][], Logger log) {
        //影響度係数の読み込み
        for(int i=0; i<InitialValue.LIGHT_NUM; i++){
            for (int j=0; j<InitialValue.DIVIDE; j++){
                if(j==4) read_Target_Ill(j,i,0);
                else read_Target_Ill(j,i,allDire[0][i*4+j]);
            }
        }
        //影響度係数の結合
        for(int i=0; i<InitialValue.LIGHT_NUM; i++) {
            for(int y=0; y<InitialValue.KC_Y; y++) {
                for (int x = 0; x < InitialValue.KC_X; x++) {
                    double sum = 0;
                    for (int j = 0; j < InitialValue.DIVIDE; j++) {
                        sum += allInf[i][j][y][x];
                    }
                    joinInf[i][y][x] = sum;
                }
            }
        }
        //照度の計算
        for(int y=0; y<InitialValue.KC_Y; y++) {
            for (int x = 0; x < InitialValue.KC_X; x++) {
                double sum = 0;
                for(int i=0; i<InitialValue.LIGHT_NUM; i++) {
                    sum += light[i][0].get_CD() * joinInf[i][y][x];
                    //System.out.println(light[i][0].get_CD());
                    //System.out.println(joinInf[i][y][x]);
                }
                allLx[y][x] = sum;
                //System.out.println(joinInf[11][y][x]);
                //System.out.println(allLx[y][x]);
            }
        }
        //System.out.println(allLx[270/5][85/5]);
        log.all_lx_log(allLx);
    }

    //影響度係数の読み込み
    public static void read_Target_Ill(int divideID, int lightID, int dir) {
        try {
            if(dir == 0 || divideID ==4){
                inputpath_lxcd="./csv/"+(divideID+1)+"/light"+(lightID+1)+".csv";
            }
            else if(dir == 1){
                inputpath_lxcd="./csv/"+(divideID+1)+"/lightup"+(lightID+1)+".csv";
            }
            else{
                inputpath_lxcd="./csv/"+(divideID+1)+"/lightdown"+(lightID+1)+".csv";
            }
            //System.out.println(inputpath_lxcd);

            String[][] data = new String[InitialValue.KC_Y][InitialValue.KC_X]; //csvデータ格納配列
            File f = new File(inputpath_lxcd);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            for (int ro = 0;ro<InitialValue.KC_Y; ro++) {
                data[ro] = line.split(",", 0);
                line = br.readLine();
            }
            br.close();

            // CSVから読み込んだ配列の中身を表示
            for(int i=0;i<InitialValue.KC_Y;i++){
                for(int j=0;j<InitialValue.KC_X;j++){
                    if(divideID == 4){
                        allInf[lightID][divideID][i][j]=Double.parseDouble(data[i][j])*0.382955;
                    }else if(dir == 0) {
                        allInf[lightID][divideID][i][j]=Double.parseDouble(data[i][j])*0.238067;
                    }else {
                        allInf[lightID][divideID][i][j]=Double.parseDouble(data[i][j])*0.122873                                        ;
                    }

                    //System.out.print(allInf[lightID][divideID][i][j]+",");
                }
                //System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

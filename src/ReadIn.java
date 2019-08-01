import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadIn {
    public static void csvReadIn(int divideID, Sensor sen[],int num,double data_inf[], String path){
        try {
            String[][] data = new String[InitialValue.KC_Y][InitialValue.KC_X]; //csvデータ格納配列
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            for (int ro = 0; line != null; ro++) {
                data[ro] = line.split(",", 0);
                line = br.readLine();
            }
            br.close();

            // CSVから読み込んだ配列の中身を表示
            for(int i=0;i<InitialValue.SENSOR_NUM;i++){
                if(divideID==4)
                    data_inf[i]=Double.parseDouble(data[sen[i].get_Sensor_Y()][sen[i].get_Sensor_X()])*0.382955;
                else if (num == 0)
                    data_inf[i]=Double.parseDouble(data[sen[i].get_Sensor_Y()][sen[i].get_Sensor_X()])*0.238067;
                else
                    data_inf[i]=Double.parseDouble(data[sen[i].get_Sensor_Y()][sen[i].get_Sensor_X()])*0.122873;

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

public class Sensor {
    private int ID;						//センサID
    private double Target_LX;		//目標照度
    private double Current_LX;	//現在照度
    private double Optimal_LX;	//最適照度
    private int Sensor_X;						//センサのx座標
    private int Sensor_Y;						//センサのy座標
    private double[] Each_LX = new double[InitialValue.LIGHT_NUM];		//照明ごとの照度

    public Sensor(int ID) {
        this.ID = ID;
    }

    //センサIDを取得するメソッド
    public int get_ID() {
        return ID;
    }

    //目標照度・色温度の設定と取得
    public void set_Target_LX(double LX) {
        if (LX <= InitialValue.MAX_TARGET_LX && LX >= InitialValue.MIN_TARGET_LX) {
            Target_LX = LX;
        } else {
            System.err.println("Setting of the target illuminance is not right.");
            System.err.println("Please set the target illuminance from "
                    +InitialValue.MIN_TARGET_LX+" to "+InitialValue.MAX_TARGET_LX+".");
        }
    }
    public double get_Target_LX(){
        return Target_LX;
    }
    public void set_Target_LX(int LX){
        Target_LX=LX;
    }

    //現在照度値・色温度値の格納と取得
    public void set_Current_LX(double LX){
        Current_LX = LX;
    }
    public double get_Current_LX(){
        Current_LX = 0;
        for (int i = 0; i < Each_LX.length; i++) {
            Current_LX += Each_LX[i];
        }
        return Current_LX;
    }
    //最適解の保持
    public void set_Optimal_LX(){
        Optimal_LX = Current_LX;
    }
    public double get_Optimal_LX(){
        return Optimal_LX;
    }

    //各照明から提供される照度
    public void set_Each_Current_LX(int light_ID, double LX){
        Each_LX[light_ID] = LX;
    }
    public double get_Each_Current_LX(int light_ID){
        return Each_LX[light_ID];
    }
    public double[] get_Each_Current_LX(){
        return Each_LX;
    }

    //センサ座標の設定と取得
    public void set_Sensor_X(int X) {
        this.Sensor_X = X;
    }
    public int get_Sensor_X() {
        return Sensor_X;
    }
    public void set_Sensor_Y(int Y) {
        this.Sensor_Y = Y;
    }
    public int get_Sensor_Y() {
        return Sensor_Y;
    }
}


public class Light {
    private int lightID=0;//照明の分割番号
    private int divideID=0;//照明の分割番号
    private double CD = InitialValue.INITIAL_CD;		//光度
    private int direction[]={0,0};									//0:下，1:上，2:下,[0]:現在，[1]前
    private double[] Influence_deg = new double[InitialValue.SENSOR_NUM];		//各センサの影響度を格納する配列
    String inputpath_cd = "./csv/光度値.csv"; //光度値csv
    String inputpath_lxcd="";//影響度係数のファイルパス

    double[][] data_cd = new double[3][101]; //0:2, 1:3, 2:3lens 分割部分の光度値
    double[][] data_inf = new double[3][InitialValue.SENSOR_NUM];

    public Light(int lightID, int divideID) {
        this.lightID = lightID;
        this.divideID=divideID;
    }

    public void read_inf(Sensor sen[]){
        /****************************影響度係数の取得**************************/
        inputpath_lxcd="./csv/"+(divideID+1)+"/light"+(lightID+1)+".csv";
        ReadIn.csvReadIn(divideID, sen, 0, data_inf[0],inputpath_lxcd);

        if(this.divideID!=InitialValue.DIVIDE-1){
            inputpath_lxcd="./csv/"+(divideID+1)+"/lightup"+(lightID+1)+".csv";
            ReadIn.csvReadIn(divideID, sen, 1, data_inf[1],inputpath_lxcd);
            inputpath_lxcd="./csv/"+(divideID+1)+"/lightdown"+(lightID+1)+".csv";
            ReadIn.csvReadIn(divideID, sen, 2, data_inf[2],inputpath_lxcd);
        }
/*		for(int i=0;i<InitialValue.SENSOR_NUM;i++){
			System.out.println(lightID+":"+divideID+":"+i+":"+data_inf[0][i]);
		}*/
    }

    //照明IDの取得
    public int get_ID() {
        return lightID;
    }
    //光度の設定と取得
    public void set_CD(double CD) {		//設定
        this.CD = CD;
    }
    public double get_CD() {					//取得
        return CD;
    }
    //分割面ごとの影響度取得
    public double get_Inf(int dire,int sen_num){
        return data_inf[dire][sen_num];
    }

    //照明1灯分の影響度係数の設定と取得
    public void set_Influence_deg(int sensorID, double Inf){
        Influence_deg[sensorID] = Inf;
    }
    public double get_Influence_deg(int sensorID){
        return Influence_deg[sensorID];
    }

    //配光方向
    public void set_dire(int dire) {
        direction[1]=direction[0];
        direction[0] = dire;
    }
    public int get_dire() {
        return direction[0];
    }
    public void reset_dire() {
        direction[0]=direction[1];
    }

}

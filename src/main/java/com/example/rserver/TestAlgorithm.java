package com.example.rserver;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by puroc on 17/4/21.
 */
public class TestAlgorithm {

    public static final String ARIMA_DATA_FILE = "/Users/puroc/git/learning-r/src/main/resources/rserver/test7_old_1.csv";
    public static final String ARIMA_TEST_FILE = "/Users/puroc/git/learning-r/src/main/resources/rserver/test7_old_2.csv";

    public static final String GM11_DATA_FILE = "/Users/puroc/git/learning-r/src/main/resources/rserver/test4_old_1.csv";
    public static final String GM11_TEST_FILE = "/Users/puroc/git/learning-r/src/main/resources/rserver/test4_old_2.csv";

    public static final String RSERVER_HOST_NAME = "192.168.34.106";

    public static void main(String[] args) {
        Rsession s = null;
        try {
            s = getRsession();
            importAlgorithm(s);
//            gm11(s, 18291);
            arima(s, 42091);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.end();
            }
        }
    }

    private static void runR(Rsession s, long forecastNum) throws REXPMismatchException {
        s.eval("row_data<-read.table(\"/root/r/rserver/test4_old_1.csv\",header=T, sep=\",\")");
        s.eval("forecast_result<-gm11(row_data$value,length(row_data$value)+" + forecastNum + ")");
        s.eval("forecast_result<-gm11(row_data,length(row_data)+" + forecastNum + ")");
        s.eval("forecast_result<-tail(forecast_result," + forecastNum + ")");
        s.eval("result<-floor(forecast_result)");
        int[] results = s.eval("result").asIntegers();
        System.out.println("!!!!!!!!!!!  " + results.length);
        for (int result : results) {
            System.out.println(result);
        }
    }

    private static void gm11(Rsession s, long forecastNum) throws REXPMismatchException {
        double[] rowData = loadData(GM11_DATA_FILE);
        //  将数据传入到R脚本中
        s.set("data", rowData);
        s.eval("forecast_result<-gm11(data,length(data)+" + forecastNum + ")");
        s.eval("forecast_result<-tail(forecast_result," + forecastNum + ")");
        s.eval("forecast_result<-floor(forecast_result)");

        double[] testData = loadData(GM11_TEST_FILE);
        s.set("test_data", testData);

        // 调用acc方法时，第一个参数的长度一定要比第二个参数的长度小
        s.eval("result<-acc(forecast_result,test_data)");

        // 输出拟合精度
        double result = s.eval("result").asDouble();
        System.out.println("!!!!!!!!!!!  " + result);

        // 删除R中变量，避免被其他方法中误用
        s.rm(new String[]{"data", "forecast_result", "test_data", "result"});
        s.eval("ls()");
    }

    private static void arima(Rsession s, long forecastNum) throws REXPMismatchException {
        double[] data = loadData(ARIMA_DATA_FILE);
        //  将数据传入到R脚本中
        s.set("data", data);
        s.eval("forecast_result<-xarima(data," + forecastNum + ")");

        double[] forecast_results = s.eval("forecast_result").asDoubles();

        double[] testData = loadData(ARIMA_TEST_FILE);
        s.set("test_data", testData);

        // 调用acc方法时，第一个参数的长度一定要比第二个参数的长度小
        s.eval("result<-acc(forecast_result,test_data)");

        // 输出拟合精度
        double result = s.eval("result").asDouble();
        System.out.println("!!!!!!!!!!!  " + result);

        // 删除R中变量，避免被其他方法中误用
        s.rm(new String[]{"data", "forecast_result", "test_data", "result"});
        s.eval("ls()");
    }

    // 加载数据
    private static double[] loadData(String path) {
        BufferedReader br = null;
        List<Double> list = new ArrayList<Double>();
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                list.add(Double.valueOf(line.split(",")[1]));
            }
            br.close();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        int size = list.size();
        double[] rowData = new double[size];
        for (int i = 0; i < size; i++) {
            rowData[i] = list.get(i);
        }
        return rowData;
    }

    // 获取Rsession
    private static Rsession getRsession() throws RserveException {
        RserverConf rconf = new RserverConf(RSERVER_HOST_NAME, 6311, "conan", "conan", new Properties());
        Rsession s = Rsession.newInstanceTry(System.out, rconf);
        return s;
    }

    // 导入算法脚本
    private static void importAlgorithm(Rsession s) throws RserveException {
        s.connection.eval("source('/root/r/rserver/algorithm.R')");
    }
}

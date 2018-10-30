package com.example;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by puroc on 17/4/21.
 */
public class Test2 {

    public static void main(String[] args) {
        try {
            Rsession s = getRsession();
            importGm11(s);
            run2(s, 18291);
            s.end();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void run1(Rsession s, long forecastNum) throws REXPMismatchException {
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

    private static void run2(Rsession s, long forecastNum) throws REXPMismatchException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/puroc/git/learning-r/src/main/resources/test4_old_1.csv"));
        String line;
        List<Double> list = new ArrayList<Double>();
        while ((line = br.readLine()) != null) {
            list.add(Double.valueOf(line.split(",")[1]));
        }
        int size = list.size();
        double[] rowData = new double[size];
        for (int i = 0; i < size; i++) {
            rowData[i] = list.get(i);
        }
        //  将数据传入到R脚本中
        s.set("row_data", rowData);
        s.eval("forecast_result<-gm11(row_data,length(row_data)+" + forecastNum + ")");
        s.eval("forecast_result<-tail(forecast_result," + forecastNum + ")");
        s.eval("forecast_result<-floor(forecast_result)");
        s.eval("test_data<-read.table(\"/root/r/rserver/test4_old_2.csv\",header=T, sep=\",\")");
        // 调用acc方法时，第一个参数的长度一定要比第二个参数的长度小
        s.eval("result<-acc(forecast_result,test_data$value)");

        // 输出拟合精度
        double result = s.eval("result").asDouble();
        System.out.println("!!!!!!!!!!!  " + result);
        System.out.println(s.eval("test_data$value").asDoubles().length);
        System.out.println(s.eval("forecast_result").asDoubles().length);

//        s.eval("print(length(test_data$value))");
//        s.eval("print(length(forecast_result))");
    }

    private static Rsession getRsession() throws RserveException {
        RserverConf rconf = new RserverConf("192.168.167.201", 6311, "conan", "conan", new Properties());
        Rsession s = Rsession.newInstanceTry(System.out, rconf);
        return s;
    }

    private static void importGm11(Rsession s) throws RserveException {
        s.connection.eval("source('/root/r/rserver/test6.R')");
    }
}

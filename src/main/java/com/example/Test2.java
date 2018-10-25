package com.example;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.RList;

import java.io.File;
import java.util.Properties;

/**
 * Created by puroc on 17/4/21.
 */
public class Test2 {

    public static void main(String[] args) {
        try {
            RserverConf rconf = new RserverConf("192.168.167.201", 6311, "conan", "conan", new Properties());
            Rsession s = Rsession.newInstanceTry(System.out, rconf);
            s.connection.eval("source('/root/r/a/test6.R')");
//            s.eval("x<-c(408.40,479.00,574.60,758.00,1055.30)");
//            s.eval("x1<-x");
//            s.eval("x2<-gm11(x,1000)");
//            String result = s.eval("acc(x1,x2)").asString();
            s.eval("row_data<-read.table(\"/root/r/a/test4_old_1.csv\",header=T, sep=\",\")");
            s.eval("test_data<-read.table(\"/root/r/a/test4_old_2.csv\",header=T, sep=\",\")");
            s.eval("forecast_result<-gm11(row_data$value,length(row_data$value)+20000)");
            s.eval("forecast_result<-tail(forecast_result,20000)");
            s.eval("result<-floor(forecast_result)");
            int[] results = s.eval("result").asIntegers();
            System.out.println("!!!!!!!!!!!  "+results.length);
            s.end();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

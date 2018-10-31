package com.example.rserver;

import org.math.R.RserverConf;
import org.math.R.Rsession;

import java.io.File;
import java.util.Properties;

/**
 * Created by puroc on 17/4/21.
 */
public class Test {

    public static void main(String[] args) {
        try {
            RserverConf rconf = new RserverConf("192.168.167.201", 6311, "conan", "conan", new Properties());
            Rsession s = Rsession.newInstanceTry(System.out, rconf);

            // 执行R脚本
            double[] rand = s.eval("rnorm(5)").asDoubles();
            System.out.println(rand);

            // 创建一个R对象
            s.set("demo", Math.random());
            s.eval("ls()");

            // 保存R运行时状态到文件
            s.save(new File("/Users/puroc/git/learning-r/output/save.Rdata"), "demo");

            // 删除R对象demo
            s.rm("demo");
            s.eval("ls()");

            // 从文件加载R环境
            s.load(new File("/Users/puroc/git/learning-r/output/save.Rdata"));
            s.eval("ls()");
            s.eval("print(demo)");

            // 创建一个data.frame对象
            s.set("df", new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } }, "x1", "x2", "x3");
            double df$x1_3 = s.eval("df$x1[3]").asDouble();
            System.out.println(df$x1_3);
            s.rm("df");

            // 生成一个图形文件
            s.eval("getwd()");
            s.toJPEG(new File("/Users/puroc/git/learning-r/output/plot.png"), 400, 400, "plot(rnorm(10))");

            // 以HTML格式输出
            String html = s.asHTML("summary(rnorm(100))");
            System.out.println(html);

            // 以文本格式输出
            String txt = s.asString("summary(rnorm(100))");
            System.out.println(txt);

            // 安装新类库
            System.out.println(s.installPackage("sensitivity", true));

            s.end();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

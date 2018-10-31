#加载tseries包
library(tseries)
#将一行数据赋值给price
price<-c(101,82,66,35,31,7)
#指定price为时序变量，序列从2005年1月开始，读入的数据频率为每年12个（读入半年的数据，frequency=2，读入季度数据frequency=4）
#price是数值向量或矩阵，数据框将被强制转化为数值向量；start是收集数据的第一年集第一个间隔期，如2018年第1季度，start = c(2018 , 1)；end指终止时间点；frequency指定数据在一年中的频数，如月份为12，季度为4；deltat是frequency的倒数，两者设置其一；names为命名
price<-ts(price,start=c(2005,1),frequency = 12)
plot(price)

#读取csv数据，当header=T时，csv中的第一行数据为头
x<-read.table("/Users/puroc/Desktop/r/test1.csv",header=T, sep=",")

#subset函数取x的子集，条件为age<30
z<-subset(x,age<30,select = age)

#缺失值插值，需要加载zoo包
library(zoo)
#首先定义1到10的序列，赋值给a，再将第4，8个值赋值为NA
a<-c(1:10)
a[4]<-NA
a[8]<-NA
#使用下面两个方法，都可以进行缺失值插值
a2<-na.approx(a)
a3<-na.spline(a)

#数据导出
#对x数据框中的age列做对数转换
y<-log(x$age)
#将y转换为数据框，赋值给newdata
newdata<-data.frame(y)
#将新的数据框写入文件
write.table(newdata,"/Users/puroc/Desktop/r/test1_out.csv",sep=",",row.names = F)

#绘制图形
yield<-c(112,118,132,129,121,135,148,148,136,119)
yield<-ts(yield,start=1984)
#绘制yield的时序图
plot(yield)
#绘制yield的acf图
acf(yield)
Box.test(yield,lag=2)

yield<-log(yield)
yield<-diff(yield,differences = 1)
plot.ts(yield)

#生成白噪声序列
white_noise<-rnorm(1000)
white_noise<-ts(white_noise)
plot(white_noise)
acf(white_noise)

Box.test(white_noise,lag=6)
Box.test(white_noise,lag=12)

Box.test(yield,lag=6)

# Create a sequence of numbers between -10 and 10 incrementing by 0.1.
x <- seq(-10, 10, by = .1)

# Choose the mean as 2.5 and standard deviation as 0.5.
y <- dnorm(x, mean = 2.5, sd = 0.5)

Box.test(y,lag=6)

plot.ts(y)





# 加载包
library(tseries)
library(forecast)

# 读取水表用于建立预测模型的历史数据，当header=T时，csv中的第一行数据为头
x1<-read.table("/Users/puroc/Desktop/r/test3_old_1.csv",header=T, sep=",")

# 加载水表读数，读数时间间隔是一小时一条
y1<-ts(x1$value,start=c(2018,8),frequency = 8640)

plot(y1,main="row data")

# 绘制时序图
# plot.ts(y1)

# 检验序列是否平稳，p-value小于0.05为平稳序列
adf.test(y1)
pp.test(y1)


# 检验序列是否为白噪声序列，p-value小于0.05为非白噪声序列
for(i in 1:2) print(Box.test(y1,lag=6*i))

# 确定arma的模型阶数p，q值
auto.arima(y1,trace=T)

# 参数估计
y1.fit<-arima(y1,order = c(1,2,1))

#检验残差是否为白噪声序列，如不是白噪声序列，说明数据中还存在相关性，需要对模型进行优化
for(i in 1:2) print(Box.test(y1.fit$residuals,lag=6*i))

# 使用模型进行预测，因为要与test3_old_2.csv文件中的检验数据进行对比，这个文件中有700多条数据，所以向后预测800条数据
result<-forecast(y1.fit,h=800)

plot(result,main="forcast")

accuracy(result)

# 对预测结果向下取整
result<-floor(result$mean)

# 将预测结果转换为序列，以便与检验数据进行对比
result2<-ts(result,start=c(2018,9),frequency = 8640)

# 读取水表用于检验的历史数据，当header=T时，csv中的第一行数据为头
x2<-read.table("/Users/puroc/Desktop/r/test3_old_2.csv",header=T, sep=",")

# 加载水表读数，读数时间间隔是一小时一条
y2<-ts(x2$value,start=c(2018,9),frequency = 8640)

# 将两条曲线进行对比画图，col是控制颜色，
plot(y2, col = 3, lty = 2)
lines(result2, lty = 1, col = 6)
title("compare result", cex.main = 1.1)
# c("real", "forecast")和lty = c(2, 1)是用来控制左上角的图例说明的
legend("topleft", c("real", "forecast"), col = c(3,6), lty = c(2, 1))




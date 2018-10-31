# 加载包
library(tseries)
library(forecast)

# 读取csv数据，当header=T时，csv中的第一行数据为头
x<-read.table("/Users/puroc/Desktop/r/test2.csv",header=T, sep=",")

# 加载水表读数，读数时间间隔是一小时一条
y<-ts(x$value,start=c(2018,8),frequency = 8640)
# list<-diff(list)

# 绘制时序图
plot.ts(y)

# 检验序列是否平稳，p-value小于0.05为平稳序列
adf.test(y)
pp.test(y)


# 检验序列是否为白噪声序列，p-value小于0.05为非白噪声序列
for(i in 1:2) print(Box.test(y,lag=6*i))

# 确定arma的模型阶数p，q值
auto.arima(y)

# 将模型阶数导入到模型中
y.fit<-arima(y,order = c(2,2,2))

#检验残差是否为白噪声序列，如不是白噪声序列，说明数据中还存在相关性，需要对模型进行优化
for(i in 1:2) print(Box.test(y.fit$residuals,lag=6*i))

# 使用模型进行预测
result<-forecast(y.fit,h=1000)

# 绘制预测之后的整体的时序图（包括历史数据）
plot(result)

# 将预测的序列导出到csv文件中
write.table(floor(result$mean), file="/Users/puroc/Desktop/r/test2_out.csv", sep=",",row.names = F,col.names=F)

# 绘制预测数据的时序图（不包括历史数据）
plot(floor(result$mean))






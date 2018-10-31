
gm11<-function(x,k)
{
  n<-length(x)
  x1<-numeric(n);
  for(i in 1:n)   
  {
    x1[i]<-sum(x[1:i]);
  }
  b<-numeric(n)
  m<-n-1
  for(j in 1:m)
  {
    b[j+1]<-(0.5*x1[j+1]+0.5*x1[j])   
  }
  Yn=t(t(x[2:n]))                   
  B<-matrix(1,nrow=n-1,ncol=2)      
  B[,1]<-t(t(-b[2:n]))              
  A<-solve(t(B)%*%B)%*%t(B)%*%Yn;   
  a<-A[1];
  u<-A[2];
  x2<-numeric(k);
  x2[1]<-x[1];
  for(i in 1:k-1)
  {
    x2[1+i]=(x[1]-u/a)*exp(-a*i)+u/a;
  }
  x2=c(0,x2); 
  y=diff(x2);                     
  y
}


acc<-function(x1,x2)
{
  n<-length(x1);
  sum1=0;
  for(k in 2:n-1)
  {
    sum1<-sum1+(x1[k]-x1[1]);
  }
  s1<-sum1+0.5*(x1[n]-x1[1]);
  sum2=0;
  for(k in 2:n-1)
  {
    sum2<-sum2+(x2[k]-x2[1]);
  }
  s2<-sum2+0.5*(x2[n]-x2[1]);
  abs1<-abs(s1)
  abs2<-abs(s2)
  abs12<-abs(s1-s2)
  ee<-(1+abs1+abs2)/(1+abs1+abs2+abs12)
  ee
}


# x<-c(408.40,479.00,574.60,758.00,1055.30)   
# x1<-x
# x2<-gm11(x,1000)
# acc(x1,x2)


# row_data<-read.table("/Users/puroc/Desktop/r/test6_new_1.csv",header=T, sep=",")
# all_data<-read.table("/Users/puroc/Desktop/r/test6_new_3.csv",header=T, sep=",")
# result<-floor(gm11(row_data$value,length(row_data$value)+20000))
# acc(row_data$value,result)
# plot.ts(all_data$value, col = 3, lty = 2)
# lines(result, lty = 1, col = 6)
# title("compare forecast result", cex.main = 1.1)
# legend("topleft", c("real","forecast"), col = c(3,6), lty = c(2, 1))

# row_data<-read.table("/Users/puroc/Desktop/r/test6_new_1.csv",header=T, sep=",")
# test_data<-read.table("/Users/puroc/Desktop/r/test6_new_2.csv",header=T, sep=",")


row_data<-read.table("/Users/puroc/Desktop/r/test4_old_1.csv",header=T, sep=",")
test_data<-read.table("/Users/puroc/Desktop/r/test4_old_2.csv",header=T, sep=",")
forecast_result<-gm11(row_data$value,length(row_data$value)+20000)
# forecast_result<-forecast_result[length(row_data$value):length(forecast_result)]

forecast_result<-tail(forecast_result,20000)

result<-floor(forecast_result)
acc(test_data$value,result)
plot.ts(test_data$value, col = 3, lty = 2)
lines(result, lty = 1, col = 6)
title("compare forecast result", cex.main = 1.1)
legend("topleft", c("real","forecast"), col = c(3,6), lty = c(2, 1))
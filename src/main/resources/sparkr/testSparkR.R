source("/root/r/b/gm11.R")

if (nchar(Sys.getenv("SPARK_HOME")) < 1) {
  Sys.setenv(SPARK_HOME = "/root/spark/spark-2.3.2-bin-hadoop2.7")
}
library(SparkR, lib.loc = c(file.path(Sys.getenv("SPARK_HOME"), "R", "lib")))
sparkR.session(master = "local[*]", sparkConfig = list(spark.driver.memory = "2g"))


#schema <- structType(structField("a", "string"),structField("b", "string"),structField("c", "string"),structField("d", "string"))
#row_data <- loadDF("/root/r/b/test.csv","csv",schema, multiLine = TRUE)
#head(select(row_data,row_data$a))

#people <- read.df("/root/r/b/test.json", "json")
#printSchema(people)
#head(select(people,people$name))

schema <- structType(structField("time", "string"),structField("value", "int"))
row_data <- loadDF("/root/r/b/test4_old_1.csv","csv",schema, multiLine = TRUE)
value <- select(row_data,row_data$value)
localValue<-as.data.frame(value)
localValue<-as.vector(unlist(localValue))

forecast_result<-floor(gm11(localValue,length(localValue)+200))
forecast_result<-tail(forecast_result,200)
forecast_result


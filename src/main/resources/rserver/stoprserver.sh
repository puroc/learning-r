kill -9 `ps -aux|grep Rserve | grep -v grep | awk '{print $2}'`

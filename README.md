# QingDaoBeer
青岛啤酒挂机领流量系统?

*原网址：
https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex

**原理：每天0:00-1:00的时间段内抽奖会有很大的机率抽中100M流量。一个月1G流量封顶。
该系统就是使用此原理，自动在0:00-1:00这一时间段内帮您抽取免费流量！**

`注：只限联通手机号抽奖`

## *功能
* 添加手机号后，自动在0点进行抽奖；
* 验证码，防止恶意添加；
* 删除手机号。

## *未实现的功能
* 多线程；

## 一、修改、添加功能
* 使用git拉取该项目；
* 使用Eclipse或者IDEA打开该项目。

## 二、打包并运行
* 下载该项目源文件，并解压到某个目录下。
* 下载Tesseract.dll运行时包，可在https://sourceforge.net/projects/tess4j/ 下载。下载完成后，解压并进入dist/目录，然后解压tess4j-x.x.x.jar。把里面的win32-x86中的libtesseractNNN.dll提取出来放到源文件目录下。
* 安装mysql数据库。可在此处下载：https://dev.mysql.com/downloads/mysql/
* 配置mysql，并记下登录的用户名和密码。然后进行mysql登录，在mysql中创建一个名为`QingDaoBeer`的表。
* 更改src/resources目录下的application.properties文件中的用户名和密码。
* 安装JRE8/JDK8或以上版本的运行时；可在此处下载：https://www.oracle.com/technetwork/java/javase/downloads/index.html
* 安装maven打包工具，该工具可在此处下载：https://maven.apache.org/download.html
* 在windows下运行cmd，或者在linux/mac os下运行`maven package`命令；此时maven工具将会自动创建一个target目录，并在该目录下生成一个`QingDaoBeer-1.0.jar`的运行包；
* 接着输入以下命令：`java -jar QingDaoBeer-1.0.jar`运行。
* 在浏览器中输入网址`http://localhost:8089`，即可看到效果。若以上操作都在服务器上，则可以在外网使用网址`http://[服务器IP]:8089`在浏览器中进行访问。

And, so on maybe...

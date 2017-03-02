執行環境
========

* JDK 7+
* Tomcat 7+


設定檔
======

程式已經有內建一組設定值，
若要覆寫，在 classpath 下增加一個 `dtc-g24.properties` 的檔案
（註：通常慣例上是放在 `D:\Datacom\Server\TOMCAT_VERSION\dtc\config` 下），
設定檔的內容與說明如下：

```XML
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<!-- 錄影檔來源的 root path -->
	<entry key="shared.folder">d:\test\oriv\shared\</entry>
	
	<!-- 轉檔程式執行路徑 -->
	<entry key="converter.path">D:\Tools\ffmpeg\bin\</entry>
	
	<!-- 轉檔完成的 Callback URL -->
	<entry key="callback.url">http://localhost:8080/G24/finish</entry>
	
</properties>
```

`dtc-dqc.properties` 只需保留欲複寫的設定值即可，
不需要整份設定檔重新宣告一次。
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
	<!-- 錄影檔來源的 root path，必須 slash 結尾 -->
	<entry key="shared.folder">d:\test\oriv\shared\</entry>
	
	<!-- 轉檔程式執行路徑，必須 slash 結尾，路徑下要有 ffmpeg.exe 檔 -->
	<entry key="converter.path">D:\Tools\ffmpeg\bin\</entry>
	
	<!-- 轉檔完成的 Callback URL
		POST 參數 'fname' 為原本 call ConvertServlet 傳進來的 fname -->
	<entry key="callback.url">http://localhost:8080/G24/finish</entry>
</properties>
```

`dtc-g24.properties` 只需保留欲複寫的設定值即可，
不需要整份設定檔重新宣告一次。
執行環境
========

* JDK 8+
* Tomcat 7+
* ffmpeg 3.2.2 64-bit static (for windows) 


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
	
	<!-- 轉檔完成的 Callback URL
		POST 參數 'fname' 為原本 call ConvertServlet 傳進來的 fname -->
	<entry key="callback.url">http://localhost:8080/G24/finish</entry>
	
	<!-- ==== Database 設定區 ==== -->
	<!-- 資料庫實體檔案存放路徑 -->
	<entry key="db.path">d:\test\g24</entry>
	
	<!-- 資料庫登入帳號 -->
	<entry key="db.username"></entry>
	
	<!-- 資料庫登入密碼 -->
	<entry key="db.password"></entry>
	
	<!-- 設定 connection pool 的最大 connection 數
		 預設值 30 -->
	<entry key="db.pool.max.connections">5</entry>
	
	<!-- 當 connection 被用光時，等待可用 connection 的時間，單位：秒 
		 預設值 30 -->
	<entry key="db.pool.login.timeout">30</entry>
	<!-- ======== -->
</properties>
```

`dtc-g24.properties` 只需保留欲複寫的設定值即可，
不需要整份設定檔重新宣告一次。
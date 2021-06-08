
Local server setup:
•	Download Xampp and install it in the system
•	Place the give upload_Video.php file in xampp->htdocs folder 
•	start xampp_start.exe and then xampp_control.exe
•	Once Xampp controller starts start the apache sever

Checking local server connection:
•	To check if apache has started or not open any browser and type localhost it should show apache dashboard. It means sever started successfully
•	Then take system IP address in which local sever is started and replace the ip address in PractiveVideo.java activity file in code at line 49. 
•	The URL should look something like this "http://xxx.xxx.x.xx/upload_Video.php". Now try the same URL in web browser it should show the error message from php (shown in demo video)

If trying to execute the app in mobile:
•	once the local server is started in system try the same URL in "http://xxx.xxx.x.xx/upload_Video.php" in web browser in mobile. It also should show the error message from php
•	If it doesn't show any connection to URL make sure mobile and local sever system are in same network

upload_Video.php:
•	Update the path to the folder where you want to store video files in $path variable as below
		$path = "E:/Mobile_Computing/PracticeVideos/";

Demo Video:
•	Demo video explains all the above and the application demo and video has subtitles as well.

Practice 60 videos:
•	https://drive.google.com/drive/folders/1GH4Y9tKE7YWs2aCxAqyUHXIf_QsrjzV1?usp=sharing


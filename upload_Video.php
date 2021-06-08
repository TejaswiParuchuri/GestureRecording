
<?php
 
  if($_SERVER['REQUEST_METHOD']=='POST'){
    $file_name = $_FILES['myFile']['name'];
    $temp_name = $_FILES['myFile']['tmp_name'];
			
    $path = "E:/Mobile_Computing/PracticeVideos/";
	if(!file_exists($path)){
    mkdir($path, 0777, true);
   }
    chmod($path, 0777);
    move_uploaded_file($temp_name, $path.$file_name);

  }else{
    echo "Error";
  }
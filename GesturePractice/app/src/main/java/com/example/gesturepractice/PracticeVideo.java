package com.example.gesturepractice;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PracticeVideo extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Uri video_uri=null;
    private String realPath;
    static int VIDEO_REQUEST=101;
    private Button practice_button;
    Button upload_button;
    Button other_gesture;
    VideoView uservideo=null;
    Button play_video;
    private static String user_name;
    private static String gesture_name;
    int PracticeNo;
    private static String gesture_url;
    private static HashMap<String, HashMap<String,Integer>> video_list=null;
    private int server_response;
    public static final String upload_URL="http://192.168.0.81/upload_Video.php";


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_video);
        recordVideo();

        user_name=getIntent().getStringExtra("UserName");
        gesture_name=getIntent().getStringExtra("Gesture");
        gesture_url=getIntent().getStringExtra("Url");

        practice_button=(Button)findViewById(R.id.btn_wpractice);
        other_gesture=(Button)findViewById(R.id.btn_other);
        play_video=(Button)findViewById(R.id.btn_previous);
        uservideo=(VideoView)findViewById(R.id.video_recorded);
        upload_button=(Button)findViewById(R.id.btn_upload);
        uservideo.setEnabled(false);
        upload_button.setEnabled(false);

        if(video_list==null)
        {
            video_list=new HashMap<>();
        }

        if(!video_list.containsKey(user_name))
        {
            video_list.put(user_name,new HashMap<String, Integer>());
        }

        if(!video_list.get(user_name).containsKey(gesture_name))
        {
            PracticeNo=0;
            video_list.get(user_name).put(gesture_name,PracticeNo);
        }

        if(isSDCardpresent())
        {
            if(ContextCompat.checkSelfPermission(PracticeVideo.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                ActivityCompat.requestPermissions(PracticeVideo.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"SD Card not found",Toast.LENGTH_LONG).show();
        }

        practice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PracticeVideo.this,DisplayVideo.class);
                intent.putExtra("Gesture",gesture_name);
                intent.putExtra("UserName",user_name);
                intent.putExtra("Url",gesture_url.toString());
                startActivityForResult(intent,2);
            }
        });

        other_gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PracticeVideo.this,SelectDropDown.class);
                intent.putExtra("UserName",user_name);
                startActivityForResult(intent,2);
            }
        });

        play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video_uri!=null)
                {
                    uservideo.setEnabled(true);
                    uservideo.setVideoURI(video_uri);
                    uservideo.start();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Last recorded vide not saved",Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(video_uri!=null)
                {
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor cursor=getContentResolver().query(video_uri,filePathColumn,null,null,null);
                    if(cursor.moveToFirst())
                    {
                        int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                        realPath=cursor.getString(columnIndex);
                    }
                    cursor.close();
                    uploadVideo();
                }
            }
        });

    }

    private boolean isSDCardpresent()
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        return false;
    }

    public void recordVideo()
    {
        try
        {
            Intent video=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            video.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
            if(video.resolveActivity(getPackageManager())!=null)
            {
                startActivityForResult(video,VIDEO_REQUEST);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int RequestCode,int resultCode,Intent data)
    {
        super.onActivityResult(RequestCode, resultCode, data);
        video_uri=data.getData();
        if(RequestCode==VIDEO_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                if(data!=null)
                {
                    Toast.makeText(this,"Video saved to "+video_uri,Toast.LENGTH_LONG).show();
                    upload_button.setEnabled(true);
                }
                else
                {
                    Toast.makeText(this,"No Data Found",Toast.LENGTH_LONG).show();
                }
            }
            if(RequestCode==RESULT_CANCELED)
            {
                Toast.makeText(this,"Video Recording Cancelled",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadVideo(){
        class UploadVideo extends AsyncTask<Void,Void,String>
        {
            ProgressDialog uploading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                uploading=ProgressDialog.show(PracticeVideo.this,"Uploading File","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                if(s.equals("Upload Successful"))
                {
                    video_list.get(user_name).put(gesture_name,PracticeNo);
                    upload_button.setEnabled(false);
                }
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
                uploading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params){
                PracticeNo=video_list.get(user_name).get(gesture_name)+1;
                String file_store_name=gesture_name+"_PRACTICE_"+String.valueOf(PracticeNo)+"_"+user_name+".mp4";
                String msg=uploadToServer(file_store_name,realPath);
                return msg;
            }
        }
        UploadVideo uv=new UploadVideo();
        uv.execute();
    }

    public String uploadToServer(String file_store_name,String sourceFileUri)
    {
        String fileName=file_store_name;
        HttpURLConnection get_connection=null;
        DataOutputStream file_output_stream=null;
        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize=1048576;

        File sourceFile=new File(sourceFileUri);
        if(!sourceFile.isFile())
        {
            return "Could not find file";
        }
        try{
            FileInputStream file_input_stream=new FileInputStream(sourceFile);
            URL url=new URL(upload_URL);
            get_connection=(HttpURLConnection) url.openConnection();
            get_connection.setDoInput(true);
            get_connection.setDoOutput(true);
            get_connection.setUseCaches(false);
            get_connection.setRequestMethod("POST");
            get_connection.setRequestProperty("Connetcion","Keep-Alive");
            get_connection.setRequestProperty("ENCTYPE","multipart/form-data");
            get_connection.setRequestProperty("Content-Type","multipart/form-data;boundary=**");
            get_connection.setRequestProperty("myFile",fileName);

            file_output_stream=new DataOutputStream(get_connection.getOutputStream());
            file_output_stream.writeBytes("--**\r\n");
            file_output_stream.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" +fileName+"\"\r\n");
            file_output_stream.writeBytes("\r\n");

            bytesAvailable=file_input_stream.available();
            bufferSize=Math.min(bytesAvailable,maxBufferSize);
            buffer=new byte[bufferSize];
            bytesRead=file_input_stream.read(buffer,0,bufferSize);

            while(bytesRead>0)
            {
                file_output_stream.write(buffer,0,bufferSize);
                bytesAvailable=file_input_stream.available();
                bufferSize=Math.min(bytesAvailable,maxBufferSize);
                bytesRead=file_input_stream.read(buffer,0,bufferSize);
            }
            file_output_stream.writeBytes("\r\n");
            file_output_stream.writeBytes("--**--"+"\r\n");
            server_response=get_connection.getResponseCode();

            file_input_stream.close();
            file_output_stream.flush();
            file_output_stream.close();

        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(server_response==200)
        {
            return "Upload Successful";
        }
        else{
            return "Upload Failed";
        }
    }

    @Override
    public void onBackPressed()
    {
        user_name=getIntent().getStringExtra("UserName");
        gesture_name=getIntent().getStringExtra("Gesture");
        gesture_url=getIntent().getStringExtra("Url");

        Intent intent=new Intent(PracticeVideo.this,DisplayVideo.class);
        intent.putExtra("Gesture",gesture_name.toString());
        intent.putExtra("Url",gesture_url.toString());
        intent.putExtra("UserName",user_name.toString());
        startActivityForResult(intent,2);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}

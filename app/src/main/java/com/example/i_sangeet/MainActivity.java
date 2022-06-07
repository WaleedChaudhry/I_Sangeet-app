package com.example.i_sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
    ListView listView;
    static int x=0;
    static int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Dexter.withContext(context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> mysongs=fetchsongs(Environment.getExternalStorageDirectory());
                        String[] items=new String[mysongs.size()];
                        for(int i=0;i < mysongs.size();i++)
                        {
                            items[i]=mysongs.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> Adapter=new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(Adapter);
                        if(y==0)
                        {
                            Toast.makeText(context, "Runtime permission given", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Total songs="+x, Toast.LENGTH_SHORT).show();
                            y++;
                        }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Intent intent=new Intent(context,playsong.class);
                            String currsong=listView.getItemAtPosition(position).toString();
                            intent.putExtra("SongList",mysongs);
                            intent.putExtra("Current Song",currsong);
                            intent.putExtra("Position",position);
                            startActivity(intent);

                        }
                    });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(context, "Runtime permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }
//used to get songs from phone memory
    public ArrayList<File> fetchsongs(File file) {
        ArrayList arr = new ArrayList();//list where we add songs
        File[] songs = file.listFiles();//file where songs are already in phone
        if(file!=null) //for checking wether the memory contaon any song or not
        {
            for (File myfile : songs) //for each loop to check all songs an if else condition add them to list if suitable
            {
                if(!myfile.isHidden()&& myfile.isDirectory())
                {
                    arr.addAll(fetchsongs(myfile));
                }
                else
                    {
                    if(myfile.getName().endsWith(".mp3")&&!myfile.getName().startsWith("."))
                    {
                        arr.add(myfile);
                        x++;
                    }
                }

            }
        }
        return arr;
    }
}
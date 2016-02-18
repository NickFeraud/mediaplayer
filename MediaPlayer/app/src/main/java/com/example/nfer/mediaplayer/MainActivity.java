package com.example.nfer.mediaplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/*
   Referenced:http://www.tutorialspoint.com/android/android_notifications.htm when creating
   notifications.

 */
public class MainActivity extends AppCompatActivity {
    private String uri;

    private  Handler download_handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            TextView songName = (TextView)findViewById(R.id.song);
            TextView artistName = (TextView)findViewById(R.id.artist);
            ImageView albumArt = (ImageView)findViewById(R.id.imageView);
            Bundle results = msg.getData();
                if(msg.arg1 == Activity.RESULT_OK){

                    songName.setText("You");
                    artistName.setText("Nils Frahm");
                    albumArt.setImageResource(R.drawable.album);
                    uri = results.getString("uri");

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this);
                    mBuilder.setSmallIcon(R.drawable.download);
                    mBuilder.setContentTitle("Download Complete!");
                    mBuilder.setContentText("Successfully downloaded mp3 for MediaPlayer");
                    NotificationManager notMan =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notMan.notify(12, mBuilder.build());

                }
            Intent createIntent = new Intent (MainActivity.this, Mp3Player.class);
            createIntent.putExtra("request", "create");
            createIntent.putExtra("uri", uri);
            startService(createIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton pButton = (ToggleButton) findViewById(R.id.playPauseButton);
        final ImageButton sButton = (ImageButton) findViewById(R.id.stop);

        pButton.setOnClickListener(new View.OnClickListener() {
            NotificationManager nMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            @Override
            public void onClick(View v) {
                if (pButton.isChecked()){
                    Toast.makeText(MainActivity.this, "play",Toast.LENGTH_LONG).show();
                    Intent playIntent = new Intent(MainActivity.this, Mp3Player.class);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                    builder.setSmallIcon(R.drawable.play_icon);
                    builder.setContentTitle("Now Playing:");
                    builder.setContentText("You - Nels Frahm");

                    nMan.notify(11, builder.build());
                    playIntent.putExtra("request", "play");
                    startService(playIntent);
                }
                if(!pButton.isChecked()){
                    Toast.makeText(MainActivity.this, "pause",Toast.LENGTH_LONG).show();
                    Intent pauseIntent = new Intent(MainActivity.this, Mp3Player.class);
                    pauseIntent.putExtra("request", "pause");
                    startService(pauseIntent);
                    nMan.cancel(11);
                }
            }
        });
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sButton.isPressed()){
                    pButton.setChecked(false);
                    Intent stopIntent = new Intent(MainActivity.this, Mp3Player.class);
                    stopIntent.putExtra("request", "stop");
                    startService(stopIntent);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.the_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()){
            case R.string.download_id:
                Intent dwnldIntent = new Intent (this, DownloadService.class);
                dwnldIntent.putExtra(DownloadService.EXTRA_MESSENGER, new Messenger(download_handler));
                startService(dwnldIntent);
                break;
            case R.string.exit_id:
                Intent exitIntent = new Intent (this, Mp3Player.class);
                exitIntent.putExtra("request", "exit");
                startService(exitIntent);
                finish();
                System.exit(0);
                break;
        }
        return true;
    }
}

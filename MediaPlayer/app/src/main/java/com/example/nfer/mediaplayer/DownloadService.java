package com.example.nfer.mediaplayer;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class DownloadService extends Service {
    public final static String url = "https://goo.gl/4XSD8X";
    static DownloadManager dm;
    public long id;
    public Uri uri;
    public static final String EXTRA_MESSENGER = "com.example.nfer.mediaplayer.EXTRA_MESSENGER";

    public int onStartCommand(Intent intent, int flags, int startID) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadService.url));
        dm.enqueue(request);

        final Bundle extras = intent.getExtras();
        final Messenger messenger = (Messenger) extras.get(EXTRA_MESSENGER);
        final Message msg = Message.obtain();

        class MyDownloadReceiever extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                uri = dm.getUriForDownloadedFile(id);
                String title = dm.COLUMN_TITLE;
                Log.i("Song title", title);

                if (extras != null) {
                    msg.arg1 = Activity.RESULT_OK;
                    Bundle data = new Bundle();
                    data.putLong("downloadID", id);
                    data.putString("uri", uri.toString());
                    data.putString("title", title);
                    msg.setData(data);

                    try {
                        messenger.send(msg);
                    } catch (RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }
                }
                unregisterReceiver(this);
                stopSelf();
            }
        }
        registerReceiver(new MyDownloadReceiever(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

/*
 * Copyright 2015 © Johnnie Ruffin
 *
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package com.example.johnnie.podcastfun;

/////////////////////////////////////////////////////////////////////////////
//
/// @class CustomList
//
/// @brief CustomList class controls the item list
//
/// @author Johnnie Ruffin
//
////////////////////////////////////////////////////////////////////////////

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CustomList extends ArrayAdapter<String> {

    // This is an attempt at a view holder pattern
    static class ViewHolderItem {

        TextView txtTitle;
        TextView txtStatus;

        ImageButton playButton;
        ImageButton stopButton;
        ImageButton closeButton;
        ImageButton deleteButton;
        ImageButton downloadButton;
    }

    private final Activity context;
    private final String[] radioTitle;
    private final Integer[] imageButtonList;
    private MediaPlayer mp;
    private String artist;

    public CustomList(Activity context, String[] radioTitle, Integer[] imageButtonList, String artist) {
        super(context, R.layout.list_single, radioTitle);
        this.context = context;
        this.radioTitle = radioTitle;
        this.imageButtonList = imageButtonList;
        this.mp = new MediaPlayer();
        this.artist = artist;
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Boolean isAvailable() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if(reachable){
                System.out.println("Internet access");
                return reachable;
            }
            else{
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        String TAG = "CustomList";
        ViewHolderItem viewHolder;

        // TODO: set the title based on the radio show to be played.

        context.setTitle(artist);
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            viewHolder = new ViewHolderItem();

            view = inflater.inflate(R.layout.list_single, null, true);
            viewHolder.txtTitle = (TextView) view.findViewById(R.id.txt);
            viewHolder.txtStatus = (TextView) view.findViewById(R.id.txtstatus);

            viewHolder.playButton = (ImageButton) view.findViewById(R.id.playbtn);
            viewHolder.stopButton = (ImageButton) view.findViewById(R.id.stopbtn);
            viewHolder.closeButton = (ImageButton) view.findViewById(R.id.closebtn);
            viewHolder.deleteButton = (ImageButton) view.findViewById(R.id.deletebtn);
            viewHolder.downloadButton = (ImageButton) view.findViewById(R.id.downloadbtn);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderItem) view.getTag();
        }

        final String mediaTitle = radioTitle[position];

        final MediaControl mc =
                new MediaControl(context, mp, artist);

        RadioTitle rt = new RadioTitle();

        rt.initTitles();

        String MediaFile = null;

        switch (artist) {
            case "Burns And Allen": {
                for (String mediaFile : rt.getBaMap().keySet()) {
                    if (rt.getBaMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                Log.d(TAG, "Burns And Allen: Title Find");
                break;
            }

            case "Fibber McGee And Molly":
            {
                Log.d(TAG, "Fibber McGee And Molly: MediaTitle: " + mediaTitle);
                for (String mediaFile : rt.getFbMap().keySet()) {
                    if (rt.getFbMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                        Log.d(TAG, "Fibber McGee And Molly: MediaFile: " + MediaFile);
                    }
                }
                Log.d(TAG, "Fibber McGee And Molly: Title Find");
                break;
            }

            case "Martin And Lewis":
            {
                for (String mediaFile : rt.getMlMap().keySet()) {
                    if (rt.getMlMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "The Great GilderSleeves":
            {
                for (String mediaFile : rt.getGlMap().keySet()) {
                    if (rt.getGlMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "XMinus1":
            {
                for (String mediaFile : rt.getXMMap().keySet()) {
                    if (rt.getXMMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Inner Sanctum":
            {
                for (String mediaFile : rt.getIsMap().keySet()) {
                    if (rt.getIsMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Dimension X":
            {
                for (String mediaFile : rt.getDxMap().keySet()) {
                    if (rt.getDxMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Night Beat":
            {
                for (String mediaFile : rt.getNbMap().keySet()) {
                    if (rt.getNbMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }

            case "Speed":
            {
                for (String mediaFile : rt.getSgMap().keySet()) {
                    if (rt.getSgMap().get(mediaFile).equals(mediaTitle)) {
                        MediaFile = mediaFile;
                    }
                }
                break;
            }
        }

            final String mediaFileName = MediaFile;
            boolean isItInRaw = mc.checkResourceInRaw(MediaFile);
            final boolean doesMediaExist = mc.checkForMedia(MediaFile);

            viewHolder.txtStatus.setVisibility(View.VISIBLE);
            viewHolder.deleteButton.setVisibility(View.INVISIBLE);
            viewHolder.closeButton.setVisibility(View.INVISIBLE);
            viewHolder.stopButton.setVisibility(View.INVISIBLE);
            viewHolder.downloadButton.setVisibility(View.INVISIBLE);

            viewHolder.txtTitle.setText(mediaTitle);

            if (!isItInRaw && !doesMediaExist) {
                viewHolder.downloadButton.setImageResource(imageButtonList[4]);
                viewHolder.downloadButton.setVisibility(View.VISIBLE);
                viewHolder.playButton.setImageResource(imageButtonList[0]);
                viewHolder.stopButton.setImageResource(imageButtonList[8]);
                viewHolder.stopButton.setVisibility(View.VISIBLE);
            }

            if (isItInRaw || doesMediaExist) {
                viewHolder.downloadButton.setVisibility(View.INVISIBLE);
                viewHolder.playButton.setImageResource(imageButtonList[0]);

                viewHolder.deleteButton.setVisibility(View.VISIBLE);
                viewHolder.deleteButton.setImageResource(imageButtonList[7]);
            }

            viewHolder.playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // if we need to stream this, check for internet connection.
                    if (!doesMediaExist) {
                        if (!isNetworkAvailable()) {
                            Toast.makeText(context, "No Internet Connection Detected. Cannot Stream Media.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    final Intent i = new Intent(context, PlayActivity.class);
                    i.putExtra("MediaTitle", mediaFileName);
                    i.putExtra("Selection", artist);
                    i.putExtra("Title", mediaTitle);
                    context.startActivity(i);
                    context.finish();
                }
            });

            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (!isNetworkAvailable())
                    {
                        Toast.makeText(context, "No Internet Connection Detected. Cannot proceed with download.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mc.downloadMedia(mediaFileName);
                    Toast.makeText(context, "Download In Progress: " + mediaFileName, Toast.LENGTH_SHORT).show();
                }
            });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mc.deleteMedia(mediaFileName);
                Toast.makeText(context, "Download In Progress: " + mediaFileName, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
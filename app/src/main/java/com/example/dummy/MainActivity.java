package com.example.dummy;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private MediaPlayer mediaPlayer;
	private ImageView artist;
	private SeekBar seekBar;
	private TextView start;
	private TextView end;
	private Button prev;
	private Button play;
	private Button next;
	private Thread thread;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setui();

		seekBar.setMax(mediaPlayer.getDuration());
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				if(fromUser){
					mediaPlayer.seekTo(progress);
				}
				SimpleDateFormat dateformat =new SimpleDateFormat("mm:ss");
				int currentpos=mediaPlayer.getCurrentPosition();
				int duration=mediaPlayer.getDuration();

				start.setText(dateformat.format(new Date(currentpos) ));
				end.setText(dateformat.format(new Date(duration-currentpos)));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}

	public void setui() {
		mediaPlayer= new MediaPlayer();
		mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.adiyae);

		prev=findViewById(R.id.prevbutton);
		play=findViewById(R.id.playbutton);
		next=findViewById(R.id.nextbutton);
		artist=findViewById(R.id.imageView);
		start=findViewById(R.id.textView);
		end=findViewById(R.id.textView2);
		seekBar=findViewById(R.id.seekBar);

		play.setOnClickListener(this);
		prev.setOnClickListener(this);
		next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.prevbutton:
				prevv();
				break;

			case R.id.playbutton:
				if(mediaPlayer.isPlaying())
					pausemusic();
				else
					startmusic();
				break;

			case R.id.nextbutton:
				nextt();
				break;
		}
	}
	public void pausemusic(){
		if(mediaPlayer!=null){
			mediaPlayer.pause();
			play.setBackgroundResource(android.R.drawable.ic_media_play);
		}
	}
	public void startmusic(){
		if(mediaPlayer!=null){
		mediaPlayer.start();
		update();
		play.setBackgroundResource(android.R.drawable.ic_media_pause);
		}
	}
	public void prevv(){
		if(mediaPlayer.isPlaying())
			mediaPlayer
				.seekTo(0);
	}
	public void nextt(){
		if(mediaPlayer.isPlaying())
			mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
	}
	public void update(){
		thread=new Thread(){
			@Override
			public void run() {


				try{
					while (mediaPlayer!=null&&mediaPlayer.isPlaying()){
						Thread.sleep(50);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								int pos=mediaPlayer.getCurrentPosition();
									int newmax=mediaPlayer.getDuration();
								seekBar.setMax(newmax);
								seekBar.setProgress(pos);
								start.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss")
									.format(new Date(mediaPlayer.getCurrentPosition()))));
								end.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss")
								.format(new Date(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()))));

							}
						});
					}
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		};
		thread.start();

	}


}

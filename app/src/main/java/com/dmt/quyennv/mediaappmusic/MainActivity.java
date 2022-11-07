package com.dmt.quyennv.mediaappmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar sbSong;
    ImageButton ibtnPre, ibtnPlay, ibtnStop, ibtnNext;
    ImageView imgHinh;
    ArrayList<Song> arraySong;
    int pos = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        AddSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        KhoiTaoMediaPlayer();

        ibtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos--;
                if (pos < 0){
                    pos = arraySong.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                ibtnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    // nếu đang phát -> pause -> đổi hình play
                    mediaPlayer.pause();
                    ibtnPlay.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    ibtnPlay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdateTimeSong();
                imgHinh.startAnimation(animation);
            }
        });

        ibtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                ibtnPlay.setImageResource(R.drawable.play);
                KhoiTaoMediaPlayer();
            }
        });

        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos++;
                if (pos > arraySong.size() - 1){
                    pos = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                ibtnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbSong.getProgress());
            }
        });
    }

    private void KhoiTaoMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(pos).getFile());
        txtTitle.setText(arraySong.get(pos).getTitle());
    }

    private void UpdateTimeSong() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
                // update progress Song
                sbSong.setProgress(mediaPlayer.getCurrentPosition());
                // kiểm tra thời gian bài hát -> nếu kết thúc -> next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        pos++;
                        if (pos > arraySong.size() - 1){
                            pos = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }

                        KhoiTaoMediaPlayer();
                        mediaPlayer.start();
                        ibtnPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void SetTimeTotal(){
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        // gán mã của skSong = mediaPlayer.getDuration()
        sbSong.setMax(mediaPlayer.getDuration());
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Bao giờ lấy chồng", R.raw.bao_gio_lay_chong));
        arraySong.add(new Song("Chuyện cũ bỏ qua", R.raw.chuye_cu_bo_qua));
        arraySong.add(new Song("Cớ sao giờ lại chia xa", R.raw.co_sao_gio_lai_chia_xa));
        arraySong.add(new Song("Gửi anh xa nhớ", R.raw.gui_anh_xa_nho));
        arraySong.add(new Song("Mình yêu nhau đi", R.raw.minh_yeu_nhau_di));
        arraySong.add(new Song("Nhớ", R.raw.nho));
        arraySong.add(new Song("Sầu trong em", R.raw.sau_trong_em));
        arraySong.add(new Song("Vẫn", R.raw.van));

    }

    private void AnhXa() {
        txtTitle     = (TextView) findViewById(R.id.textViewTitle);
        txtTimeSong  = (TextView) findViewById(R.id.texxtViewTimeSong);
        txtTimeTotal = (TextView) findViewById(R.id.textViewTimeTotal);
        sbSong       = (SeekBar) findViewById(R.id.seekBarSong);
        ibtnPre      = (ImageButton) findViewById(R.id.imageButtonPre);
        ibtnPlay     = (ImageButton) findViewById(R.id.imageButtonPlay);
        ibtnStop     = (ImageButton) findViewById(R.id.imageButtonStop);
        ibtnNext     = (ImageButton) findViewById(R.id.imageButtonNext);
        imgHinh      = (ImageView) findViewById(R.id.imageViewDisc);

    }
}
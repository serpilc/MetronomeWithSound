package com.example.serpil.seekbarbeats;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String BPM_KEY = "bpm";

    private SoundPool mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    private int mBeatSoundId = -1;
    private VerticalSeekBar mMetronomeView;
    private TextView mBpmText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBeatSoundId = mSoundPool.load(this, R.raw.beat, 1);

        mBpmText = (TextView) findViewById(R.id.verticalSeekbarText);

        mMetronomeView = (VerticalSeekBar) findViewById(R.id.verticalSeekbar);
        mMetronomeView.setBeatRunnable(new PlayBeat());
        mMetronomeView.setBpmChangedRunnable(new UpdateBpmText());
        restoreBpm(mMetronomeView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final TextView sliderText = (TextView) findViewById(R.id.verticalSeekbarText);
        sliderText.setTextSize(48);


        VerticalSeekBar verticalSeebar = (VerticalSeekBar) findViewById(R.id.verticalSeekbar);
        verticalSeebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onStopTrackingTouch(SeekBar seekBar) {


            }


            @Override


            public void onStartTrackingTouch(SeekBar seekBar) {


            }


            @Override


            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                sliderText.setText("" + progress);

            }

        }
        );

    }



    protected void onDestroy() {
        mSoundPool.release();
        mSoundPool = null;

        super.onDestroy();
    }


    protected void onPause() {
        super.onPause();
        storeBpm(mMetronomeView);
    }

    private void storeBpm(VerticalSeekBar source) {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putFloat(BPM_KEY, source.getBpm());
        e.commit();
    }

    private void restoreBpm(VerticalSeekBar destination) {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        destination.setBpm(sp.getFloat(BPM_KEY, destination.getDefaultBpm()));
    }

    class UpdateBpmText implements Runnable {
        @Override
        public void run() {
            int bpm = (int) (mMetronomeView.getBpm() + 0.5f);
            mBpmText.setText(bpm + " " + getString(R.string.bpm_unit_text));
        }
    }

    class PlayBeat implements Runnable {
        @Override
        public void run() {
            if (mSoundPool != null && mBeatSoundId != -1) {
                mSoundPool.play(mBeatSoundId, 1.0f /*leftVolume*/, 1.0f /*rightVolume*/, 0 /*priority*/, 0 /*loop*/, 1.0f /*rate*/);
            }
        }
    }


}






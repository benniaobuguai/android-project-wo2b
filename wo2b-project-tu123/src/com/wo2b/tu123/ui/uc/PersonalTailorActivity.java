package com.wo2b.tu123.ui.uc;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseIntArray;

import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.tu123.R;

/**
 * Personal Tailor.
 * @author Rocky
 * 
 */
public class PersonalTailorActivity extends RockyFragmentActivity
{
	
	private SoundPool mSoundPool;
	private SparseIntArray mSoundPoolArray;
	
	private Runnable mPlaySound = new Runnable()
	{
		
		@Override
		public void run()
		{
//			AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//			int streamVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			int streamMaxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//			
//			int volume = (int) (0.3f * streamMaxVolume);
//			if (volume > streamVolume)
//			{
//				volume = streamVolume;
//			}
//			manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
			
			// TODO: 暂时关闭
			//play(R.raw.yahu, 0);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_personal_tailor);
		
		initView();
		setDefaultValues();
		bindEvents();
		
		initSounds();
	}
	
	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.personal_tailor);
	}
	
	@Override
	protected void bindEvents()
	{
		super.bindEvents();
	}
	
	@Override
	protected void setDefaultValues()
	{
		super.setDefaultValues();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getSubHandler().removeCallbacks(mPlaySound);
		getSubHandler().postDelayed(mPlaySound, 500);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		getSubHandler().removeCallbacks(mPlaySound);
	}
	
	public void initSounds()
	{
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
		
		// 初始化HASH表
		mSoundPoolArray = new SparseIntArray();
		loadSound(R.raw.fish_bubble, R.raw.fish_bubble);
		
		// 获得声音设备和设备音量
	}
	
	/**
	 * 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
	 * 
	 * @param soundId
	 * @param raw
	 */
	public void loadSound(int soundId, int raw)
	{
		mSoundPoolArray.put(soundId, mSoundPool.load(this, raw, soundId));
	}

	/**
	 * 
	 * @param soundId
	 * @param scroll
	 */
	public void play(int soundId, int scroll)
	{
		mSoundPool.play(mSoundPoolArray.get(soundId), 1.0f, 1.0f, 1, scroll, 1f);
	}

}

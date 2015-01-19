package com.wo2b.sdk.assistant.sound;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 声音管理器
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class RockySoundPool
{

	private SoundPool mSoundPool;

	private AudioManager mAudioManager;

	private Context mContext;

	private HashMap<String, Integer> mSoundPoolMap;

	/**
	 * 同时播放流的最大数量
	 */
	private static final int MAX_STREAMS = 3;

	public RockySoundPool(Context context)
	{
		initAudioManager(context);
	}

	/**
	 * 初始化音频管理器
	 * 
	 * @param context
	 */
	private void initAudioManager(Context context)
	{
		mContext = context.getApplicationContext();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<String, Integer>();
	}

	/**
	 * 添加音频
	 * 
	 * @param pSoundName
	 * @param pResId
	 */
	public void addAudio(String pSoundName, int pResId)
	{
		mSoundPoolMap.put(pSoundName, mSoundPool.load(mContext, pResId, 1));
	}

	/**
	 * 播放音效
	 * 
	 * @param pSoundName
	 */
	public void playAudio(String pSoundName)
	{
		if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
		{
			// 在响铃模式为Normal的情况下播放音频
			// 音频对应id，左右声道大小，优先级，循环次数，播放速率:1代表正常播放:范围:0.5-2
			mSoundPool.play(mSoundPoolMap.get(pSoundName).intValue(), 1, 1, 0, 0, 1);
		}
	}

	/**
	 * 停止音效
	 * 
	 * @param pSoundName
	 */
	public void stopAudio(String pSoundName)
	{
		mSoundPool.stop(mSoundPoolMap.get(pSoundName).intValue());
	}

	/**
	 * 回收资源
	 */
	public void release()
	{
		mSoundPool.release();
	}

}

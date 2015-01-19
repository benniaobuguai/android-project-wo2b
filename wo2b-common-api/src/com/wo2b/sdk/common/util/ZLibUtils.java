package com.wo2b.sdk.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * ZLib压缩工具
 * 
 * @author Rocky
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-12-28
 */
public class ZLibUtils
{

	/**
	 * 压缩速度
	 */
	private static final int BUFFER_LENGTH = 8 * 1024;

	/**
	 * 压缩
	 * 
	 * @param data 待压缩的数据
	 * @return
	 */
	public static byte[] compress(byte[] data)
	{
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[BUFFER_LENGTH];
		while (!deflater.finished())
		{
			int byteCount = deflater.deflate(buf);
			baos.write(buf, 0, byteCount);
		}
		deflater.end();

		byte[] compressedBytes = baos.toByteArray();

		return compressedBytes;
	}

	/**
	 * 压缩
	 * 
	 * @param data 待压缩的数据
	 * @param os 输出流
	 * @throws IOException
	 */
	public static void compress(byte[] data, OutputStream os) throws IOException
	{
		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		dos.write(data, 0, data.length);
		dos.finish();
		dos.flush();
	}

	/**
	 * 解压缩
	 * 
	 * @param data 待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data)
	{
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try
		{
			byte[] buf = new byte[BUFFER_LENGTH];
			while (!decompresser.finished())
			{
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		}
		catch (Exception e)
		{
			// output = data;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				o.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 解压缩
	 * 
	 * @param is 输入流
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(InputStream is)
	{
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(BUFFER_LENGTH);
		try
		{
			byte[] buf = new byte[BUFFER_LENGTH];
			int len = -1;
			while ((len = iis.read(buf)) != -1)
			{
				o.write(buf, 0, len);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return o.toByteArray();
	}

}

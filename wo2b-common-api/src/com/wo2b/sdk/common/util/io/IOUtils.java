package com.wo2b.sdk.common.util.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

/**
 * 流工具类
 * 
 * @author 笨鸟不乖
 * 
 */
public class IOUtils
{

	/**
	 * InputStream to bytes.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteArray(InputStream is) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		try
		{
			while ((len = is.read(buffer)) != -1)
			{
				baos.write(buffer, 0, len);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (baos != null)
			{
				baos.close();
			}
		}

		return baos.toByteArray();
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStream2String1(InputStream in)
	{
		BufferedReader bf = null;
		StringBuffer buffer = null;
		try
		{
			bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			buffer = new StringBuffer();
			String line = bf.readLine();
			while (line != null)
			{
				buffer.append(line);
				line = bf.readLine();
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (buffer != null)
		{
			return buffer.toString();
		}

		return null;
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStream2String2(InputStream in) throws IOException
	{
		String result = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int ch = -1;
		byte[] buffer = new byte[1024 * 4];
		try
		{
			ch = in.read(buffer);
			while (ch != -1)
			{
				baos.write(buffer, 0, ch);

				ch = in.read(buffer);
			}
			baos.flush();
			result = baos.toString(HTTP.UTF_8);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (baos != null)
			{
				baos.close();
			}
		}

		return result;
	}

	/**
	 * 关闭输入流
	 * 
	 * @param is
	 */
	public static void close(InputStream is)
	{
		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param os
	 */
	public static void close(OutputStream os)
	{
		if (os != null)
		{
			try
			{
				os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回文件的内容字符串
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException, IOException
	 */
	public static String readContentFromFile(File file) throws FileNotFoundException, IOException
	{
		InputStream in = null;
		ObjectInputStream ois = null;

		try
		{
			if (!file.exists())
			{
				return null;
			}

			in = new FileInputStream(file);
			ois = new ObjectInputStream(in);
			String content = ois.readUTF();
			return content;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			IOUtils.close(ois);
			IOUtils.close(in);
		}
	}

	/**
	 * 需要进行加密存储用户信息
	 * 
	 * @param user
	 * @return
	 * @throws FileNotFoundException, IOException
	 */
	public static boolean writeContentToFile(File file, String content) throws FileNotFoundException, IOException
	{
		OutputStream out = null;
		ObjectOutputStream oos = null;
		try
		{
			if (!file.exists())
			{
				File parentDir = file.getParentFile();
				if (!parentDir.exists())
				{
					parentDir.mkdirs();
				}

				file.createNewFile();
			}

			out = new FileOutputStream(file);
			oos = new ObjectOutputStream(out);

			oos.writeUTF(content);

			return true;
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			IOUtils.close(oos);
			IOUtils.close(out);
		}
	}

}

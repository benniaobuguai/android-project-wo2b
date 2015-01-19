package com.wo2b.sdk.common.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

public class CSVAnalysis
{

	private final Context context;

	public CSVAnalysis(Context context)
	{
		this.context = context;
	}

	
	/**
	 * 解析CSV文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String[]> parseCSV2Array(String csvPath) throws IOException
	{
		InputStream is = context.getResources().getAssets().open(csvPath);
		InputStreamReader fr = new InputStreamReader(is, "gbk");

		BufferedReader br = new BufferedReader(fr);

		br = new BufferedReader(fr);
		List<String[]> listFile = new ArrayList<String[]>();

		CSVReader reader = null;
		int cIndex = -1;// current index
		String cRow = null;// current row

		boolean hasHeader = false;
		String[] tempArray = null;
		
		try
		{
			while ((cRow = br.readLine()) != null)
			{
				++cIndex;

				reader = CSVReader.parse(cRow);

				if (!hasHeader)
				{
					// Read header
					reader.readHeaders();
					tempArray = reader.getHeaders();
					hasHeader = true;
				}
				else
				{
					// Read record
					reader.readRecord();
					tempArray = reader.getValues();
				}
				
				if (!TextUtils.isEmpty(tempArray[0]))
				{
					listFile.add(tempArray);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fr != null)
			{
				fr.close();
			}
			if (br != null)
			{
				br.close();
			}
			if (is != null)
			{
				is.close();
			}
		}

		return listFile;
	}
	
	/**
	 * 解析CSV文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<List<String>> readCSVFile(String csvPath) throws IOException
	{
		InputStream is = context.getResources().getAssets().open(csvPath);
		InputStreamReader fr = new InputStreamReader(is, "gbk");
		
		BufferedReader br = new BufferedReader(fr);
		String rec = null;// 一行
		String str;// 一个单元格
		List<List<String>> listFile = new ArrayList<List<String>>();
		try
		{
			// 读取一行
			while ((rec = br.readLine()) != null)
			{
				Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find())
				{
					str = mCells.group();
					str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				listFile.add(cells);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fr != null)
			{
				fr.close();
			}
			if (br != null)
			{
				br.close();
			}
			if (is != null)
			{
				is.close();
			}
		}

		return listFile;
	}

}
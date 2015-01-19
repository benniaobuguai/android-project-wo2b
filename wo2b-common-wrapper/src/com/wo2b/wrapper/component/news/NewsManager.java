package com.wo2b.wrapper.component.news;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wo2b.sdk.common.util.DateUtils;
import com.wo2b.sdk.common.util.http.AsyncHttpClient;
import com.wo2b.sdk.common.util.http.extra.SyncHttpClient;
import com.wo2b.sdk.common.util.http.extra.SyncHttpResponse;
import com.wo2b.xxx.webapp.Wo2bAsyncHttpClient;

/**
 * 新闻管理器
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-2
 */
public class NewsManager
{

	/**
	 * 获取新闻列表
	 * 
	 * @param keyword
	 * @param page
	 * @return
	 */
	public static List<News> getNewsList(String keyword, int page)
	{
		List<News> newsList = new ArrayList<News>();
		String path = "http://m.baidu.com/news?tn=bdapisearch&word=" + keyword + "&pn=" + page * 20
				+ "&rn=20&t=1386838893136";

		AsyncHttpClient asyncHttpClient = Wo2bAsyncHttpClient.newAsyncHttpClient();
		SyncHttpResponse httpResponse = SyncHttpClient.getSync(asyncHttpClient, path, null);

		if (httpResponse == null || !httpResponse.isOK())
		{
			return null;
		}

		String response = httpResponse.getContent();
		JSONArray jsonArray = JSON.parseArray(response);
		if (jsonArray == null)
		{
			return newsList;
		}

		JSONObject jsonObject = null;
		News news = null;
		int count = jsonArray.size();
		for (int i = 0; i < count; i++)
		{
			jsonObject = jsonArray.getJSONObject(i);
			news = new News();
			news.setTitle(jsonObject.getString("title"));
			news.setSource(jsonObject.getString("author"));
			news.setUrl(jsonObject.getString("url"));
			news.setPhotoUrl(jsonObject.getString("imgUrl"));

			Long sortTime = jsonObject.getLong("sortTime") * 1000;
			String date = DateUtils.getStringByStyle(sortTime, "yyyy-MM-dd HH:mm");
			news.setDate(date);

			newsList.add(news);
		}

		return newsList;
	}

}

package com.example.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
//					URL url = new URL(address);
//					connection = (HttpURLConnection) url.openConnection();
//					connection.setConnectTimeout(8000);
//					connection.setReadTimeout(8000);
//					connection.setRequestMethod("GET");
//					InputStream is = connection.getInputStream();
//					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//					StringBuilder temp = new StringBuilder();
//					String line;
//					while ((line = reader.readLine()) != null) {
//						temp.append(line);
//					}
//					String response = temp.toString();
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(address);
					HttpResponse res = httpClient.execute(httpGet);
					HttpEntity entity = res.getEntity();
					String response = EntityUtils.toString(entity, "utf-8");
					
					if (listener != null) {
						listener.onFinish(response);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}

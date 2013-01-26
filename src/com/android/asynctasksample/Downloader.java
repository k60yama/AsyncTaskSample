package com.android.asynctasksample;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Downloader {

	
	public static Bitmap downloadFile(String url){
		try{
			Bitmap bmp = null;
			
			//DefaultHttpClientクラスのインスタンス生成
			final DefaultHttpClient httpClient = new DefaultHttpClient();
			
			//HttpGetクラスのインスタンス生成
			HttpGet hg = new HttpGet(url);
			
			//ダウンロードURLを実行し、HttpResponceインスタンスを取得
			HttpResponse httpResponse = httpClient.execute(hg);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				//戻り値を設定
				bmp = BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
				hg.abort();
			}
			return bmp;
			
		}catch(ClientProtocolException e){
			return null;
		}catch(IOException e){
			return null;
		}
	}
}

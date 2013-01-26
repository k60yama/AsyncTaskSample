package com.android.asynctasksample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class AsyncTaskSample extends Activity {

	private Integer[] mThumbIds = {
		R.drawable.image7,
		R.drawable.image8,
		R.drawable.image9,
		R.drawable.image10,
		R.drawable.image11,
		R.drawable.image12,
	};
	
	private int mImagePosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//ActivityクラスのonCreateを実行
		super.onCreate(savedInstanceState);
		
		//タイトルバーにプログレスを表示
		this.requestWindowFeature(Window.FEATURE_PROGRESS);
		
		//レイアウト設定ファイルを指定
		this.setContentView(R.layout.main);
		
		//最初の画像を表示
		this.mImagePosition = 0;
		this.setImage(this.mImagePosition);
		
		//AsyncTaskを実行時に指定する引数を設定
		String url1 = "https://sites.google.com/site/yukianzm/tmp/image1.png";
		String url2 = "https://sites.google.com/site/yukianzm/tmp/image2.jpg";
		String url3 = "https://sites.google.com/site/yukianzm/tmp/image3.jpg";
		String url4 = "https://sites.google.com/site/yukianzm/tmp/image4.jpg";
		String url5 = "https://sites.google.com/site/yukianzm/tmp/image5.jpg";
		String url6 = "https://sites.google.com/site/yukianzm/tmp/image6.jpg";
		
		//AsyncTaskを継承しているクラスのインスタンスを生成
		DownloadFilesTask mTask = new DownloadFilesTask();
		
		//AsyncTaskを実行(バックグラウンド処理)
		mTask.execute(url1, url2, url3, url4, url5, url6);
	}
	
	//次の画像を表示 (Nextボタン押下時)
	public void nextImage(View view){
		if(this.mImagePosition < (this.mThumbIds.length - 1)){
			this.mImagePosition = this.mImagePosition + 1;
		}
		
		//画像を設定
		this.setImage(this.mImagePosition);
	}
	
	//前の画像を表示 (Prevボタン押下時)
	public void prevImage(View view){
		if(this.mImagePosition > 0){
			this.mImagePosition = this.mImagePosition - 1;
		}
		
		//画像を設定
		this.setImage(this.mImagePosition);
	}
	
	//ImageView に position の画像をセット
	private void setImage(int position){
		//ImageViewインスタンス取得
		ImageView iv = (ImageView)this.findViewById(R.id.imageview);
		iv.setImageResource(this.mThumbIds[position]);
	}
	
	//Galleryに画像を設定
	public void setGallery(Bitmap[] bmp){
		//Galleryインスタンス生成
		Gallery g = (Gallery)this.findViewById(R.id.gallery);
		
		//アダプター設定
		g.setAdapter(new ImageAdapter(this, bmp));
		
		//クリックリスナー設定(無名クラス)
		g.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//トースト表示
				Toast.makeText(AsyncTaskSample.this, "" + (position + 1), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	//内部クラス：DownloadFilesTaskクラス
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	public class DownloadFilesTask extends AsyncTask<String, Integer, Bitmap[]> {

		@Override
		//1.タスク実行後、呼び出されるメソッド
		protected void onPreExecute(){
			//プログレスバーを表示設定
			AsyncTaskSample.this.setProgressBarVisibility(true);
			
			//プログレスバーの値を 0 % に設定
			AsyncTaskSample.this.setProgress(0); //0から10000
		}
		
		@Override
		//2.onPreExecute()メソッド実行後に呼び出されるメソッド
		protected Bitmap[] doInBackground(String... urls){
			//可変長配列の総要素数を取得
			int count = urls.length;
			
			//Bitmapクラスのインスタンスを生成
			Bitmap bmp[] = new Bitmap[count];
			
			//onProgressUpdate()メソッドを呼び出し、プログレスバーの進捗状況を更新する
			this.publishProgress(0, count);
			
			//画像ダウンロード処理へ
			for(int i=0; i<count; i++){
				//画像をダウンロード処理へ(Downloadクラスのクラスメソッド downloadFile())
				bmp[i] = Downloader.downloadFile(urls[i]);
				
				//onProgressUpdate()メソッドを呼び出し、プログレスバーの進捗状況を更新する
				this.publishProgress(i, count);
			}
			return bmp;
		}
		
		@Override
		//3.publishProgress()メソッドが実行された際に呼び出されるメソッド
		protected void onProgressUpdate(Integer... progress){
			//プログレスバーの値を設定
			AsyncTaskSample.this.setProgress((int) ((progress[0]/(float)progress[1]) * 10000));
		}
		
		@Override
		//doInBackgroundメソッドが終了した際に呼び出されるメソッド
		protected void onPostExecute(Bitmap[] bmp){
			//プログレスバーの値を 100% に設定
			AsyncTaskSample.this.setProgress(100);
			
			//Gallery にダウンロードした画像を設定
			AsyncTaskSample.this.setGallery(bmp);
			
			//プログレスバーを非表示設定
			AsyncTaskSample.this.setProgressBarVisibility(false);
		}
	}
	
}

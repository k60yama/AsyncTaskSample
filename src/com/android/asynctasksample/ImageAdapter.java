package com.android.asynctasksample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Bitmap> {

	private Context mContext;
	private int mGalleryItemBackground;
	
	//コンストラクタ
	public ImageAdapter(Context context, Bitmap[] objects){
		super(context, 0, objects);
		
		//呼び出し元アクティビティを保持
		this.mContext = context;
		
		//Gallery用にテーマで設定されている背景画像のリソースIDを取得
		TypedArray a = mContext.obtainStyledAttributes(R.styleable.MyGallery);
		this.mGalleryItemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
		a.recycle();
	}
	
	//ImageView に対応するビットマップを表示
	//可視領域に表示されている画像数だけ、position の値を変え呼び出させる
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ImageView imageView;
		if(convertView == null){
			//ImageView インスタンス生成
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			
			//Gallery用の背景画像を設定
			imageView.setBackgroundResource(this.mGalleryItemBackground);
		}else{
			imageView = (ImageView)convertView;
		}
		
		//画像を設定
		imageView.setImageBitmap(this.getItem(position));
		return imageView;
	}
}

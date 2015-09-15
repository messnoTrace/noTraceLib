package com.notrace.choosepicture;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.notrace.R;
import com.notrace.activitys.BaseActivity;
import com.notrace.commom.PathConsts;
import com.notrace.upyun.UpYunException;
import com.notrace.upyun.UpYunUtils;
import com.notrace.upyun.Uploader;
import com.notrace.utils.FileDownloadUtil;
import com.notrace.views.CircleImageView;

public class ChoosePictureDemoActivity extends BaseActivity{
	
	private CircleImageView civ;
	private InputMethodManager imm;
	private File vFile;
	private String dir_temp;
	private ChoosePicturePopWindow menuWindow;
	
	
	private String path;
    private String picturePath;
    private String top_bg;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int CROPIMAGES = 4;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosepic);
		
		findViews();
		bindListener();
		initData();
		initView();
	}



	@Override
	public void bindListener() {
		
		civ.setOnClickListener(clickListener);
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void findViews() {
		civ=(CircleImageView) findViewById(R.id.civ);
		
	}

	@Override
	public void initData() {
		dir_temp=FileDownloadUtil.getDefaultLocalDir(PathConsts.DIR_TEMP);
	}
	
	
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.civ:// 更换头像
				menuWindow = new ChoosePicturePopWindow(ChoosePictureDemoActivity.this, clickListener);
				menuWindow.showAtLocation(ChoosePictureDemoActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.btn_take_photo:// 拍照
				menuWindow.dismiss();
				vFile = new File(dir_temp + "user_icon_temp.jpg");
		
				Uri uri = Uri.fromFile(vFile);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, PHOTOHRAPH);
				break;
			case R.id.btn_pick_photo:// 系统相册
				menuWindow.dismiss();
				selectPicFromLocal();// 调用系统相册
				break;
			}
		}
	};
	
	
	
	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		this.startActivityForResult(intent, PHOTORESOULT);
	}

	/**
	 * 裁剪图片
	 */
	public void startCrop(String path) {
		Intent intent = new Intent();
		intent.putExtra("path", path);
		intent.putExtra("flag", false);
		intent.setClass(this, ImageCropActivity.class);
		startActivityForResult(intent, CROPIMAGES);
	}
	
	

	/**
	 * 获取回传数据进行相应的操作
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTOHRAPH) {// 拍照后进行裁剪图片
			if (vFile != null && vFile.exists()) {
				startCrop(Uri.fromFile(vFile).getPath());
			}
		} else if (requestCode == CROPIMAGES) {// 获取裁剪后的图片
			if (data != null) {
				path = data.getStringExtra("path");
				new ImageUploadTask().execute(path);
				ImageLoader.getInstance().displayImage("file://"+path, civ);
			}
		} else if (requestCode == PHOTORESOULT) {// 打开系统相册进行裁剪图片
			if (data == null) {// 处理返回，取消键被点击报空指针异常
				return;
			}
			Uri startCrop = data.getData();
			if (startCrop != null) {
				findPicByUri(startCrop);
			}
		}
	}
	
	/**
	 * 根据图库图片uri获取图片
	 */
	private void findPicByUri(Uri selectedImage) {
		Cursor cursor = null;
		if (selectedImage != null) {
			cursor = getContentResolver().query(selectedImage, null, null, null, null);
		}
		if (cursor != null) {// 判断图片是否存在
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;
			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
		} else {// 判断图片是否存在
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
		}
		startCrop(picturePath);// 进行图片的裁剪
	}

	
	
	/**
	 * 检查加载背景图片
	 */
	class ImageUploadTask extends AsyncTask<String, Void, String> {
		private static final String TEST_API_KEY = "yuIOo0F9DDf8ZbkZa1syRG/zdes="; // 测试使用的表单api验证密钥
		private static final String BUCKET = "notrace"; // 存储空间
		private final long EXPIRATION = System.currentTimeMillis() / 1000 + 1000 * 5 * 10; // 过期时间，必须大于当前时间
		String SAVE_KEY = File.separator + "notrace" + File.separator + System.currentTimeMillis() + ".jpg";

		@Override
		protected String doInBackground(String... arg0) {
			String string = null;
			try {
				String policy = UpYunUtils.makePolicy(SAVE_KEY, EXPIRATION, BUCKET);
				String signature = UpYunUtils.signature(policy + "&" + TEST_API_KEY);
				string = Uploader.upload(policy, signature, BUCKET, arg0[0]);
			} catch (UpYunException e) {
				e.printStackTrace();
			}
			return string;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				top_bg = "http://haomee.b0.upaiyun.com" + SAVE_KEY;
//				user.setUser_head_pic(top_bg);
//				ImageLoaderCharles.getInstance(PersonSettingActivity.this).addTask(top_bg, user_head_pic);
				ImageLoader.getInstance().displayImage(top_bg, civ);
			} else {
				top_bg = null;
//				Toast.makeText(activity_context, "当前网络不可用,无法进行此操作!!", Toast.LENGTH_LONG).show();
			}
		}

	}

}

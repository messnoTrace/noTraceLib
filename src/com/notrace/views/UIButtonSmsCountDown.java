package com.notrace.views;



import com.notrace.R;
import com.notrace.utils.StringUtils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 获取短信验证码按钮
 * 
 * @author chenyang
 * 
 */
public class UIButtonSmsCountDown extends Button {
	private TimeCount timeCount;
	private int count = 60;
	
	private String buttonText;
	private String enableText;
	
	/**
	 * 记录计数器
	 */
	private long millisInFuture;

	public UIButtonSmsCountDown(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public UIButtonSmsCountDown(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public UIButtonSmsCountDown(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		this.setTextSize(14.f);
		this.setTextColor(getResources().getColor(R.color.green_text));
		this.setBackgroundResource(R.drawable.button_sms_bg);
		this.setPadding(20,0,20,0);
//		SWLog.LogD("button text is : " + getText());
		if (null != getText() && !"".equals(getText())) {
			buttonText = getText().toString();
		} else {
			buttonText = "获取验证码";
		}

		this.setText(buttonText);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setEnabled(enabled);

		if (enabled) {
			this.millisInFuture = 0;
			this.setBackgroundResource(R.drawable.button_sms_bg);
			this.setTextColor(getResources().getColor(R.color.white));
			this.setText(buttonText);
		} else {
			if (StringUtils.isEmpty(enableText)) {
				this.setTextColor(getResources().getColor(R.color.grey));
				enableText = "%1$d秒后重发";
			}
			this.setText(String.format(enableText, count));

			// 创建计时器，总时间：60秒，间隔时间1秒
			if (0 != millisInFuture) {
				timeCount = new TimeCount(millisInFuture * 1000, 1000);
			} else {
				timeCount = new TimeCount(count * 1000, 1000);
			}
			
			timeCount.setClickButton(this);
			timeCount.start();
		}
	}
	/**
	 * 发送失败
	 * @param isSuccess
	 */
	public void setSmsCodeFailed(boolean isSuccess) {
		if (!isSuccess) {
			this.setEnabled(true);
			this.setText("重发验证码");
		}
	}
	
	public void setEnableText(String enableText) {
		this.enableText = enableText;
	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Parcelable pl = super.onSaveInstanceState();
		SaveState st = new SaveState(pl);
		st.enabale = this.isEnabled() ? 0 : 1;
		st.millisInFuture = this.millisInFuture;

		return st;
	}
	
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SaveState st = (SaveState) state;
		super.onRestoreInstanceState(st.getSuperState());
		this.millisInFuture = st.millisInFuture;
		this.setEnabled(0 == st.enabale);
	}
	
	protected static class SaveState extends BaseSavedState {
		int enabale;
		long millisInFuture;

		public SaveState(Parcelable pl) {
			super(pl);
		}

		public SaveState(Parcel in) {
			super(in);
			enabale = in.readInt();
			millisInFuture = in.readLong();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(enabale);
			dest.writeLong(millisInFuture);
		}

		public static final Parcelable.Creator<SaveState> CREATOR = new Parcelable.Creator<SaveState>() {
			@Override
			public SaveState createFromParcel(Parcel in) {
				return new SaveState(in);
			}

			@Override
			public SaveState[] newArray(int size) {
				return new SaveState[size];
			}
		};
	}

	@Override
	public void onDetachedFromWindow() {
		// 当按钮销毁时，销毁计时器TimeCount
		if (null != timeCount) {
			timeCount.cancel();
			timeCount = null;
		}
		super.onDetachedFromWindow();
	}

	/**
	 * 倒数计时器
	 * 
	 * @author changtao
	 * 
	 */
	public class TimeCount extends CountDownTimer {
		private Button mButton = null;

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {
			// 计时完毕时触发
			mButton.setEnabled(true);
			enableText="";
			mButton.setText("重发验证码");
		}

		@Override
		public void onTick(long arg0) {
			// 计时过程显示
			mButton.setText(String.format(enableText, arg0 / 1000));
			UIButtonSmsCountDown.this.millisInFuture = arg0 / 1000;
		}

		public void setClickButton(Button b) {
			mButton = b;
		}
	}
}

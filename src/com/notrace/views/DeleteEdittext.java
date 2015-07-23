package com.notrace.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.notrace.R;


/**
 * Created by notrace on 2015/7/22.
 */
public class DeleteEdittext extends RelativeLayout{

    private EditText mEditText;
    private ImageView mDelete;

    //hint
    private CharSequence mHint;
    //textColor
    private int mTextColor;
    //textSize
    private float mTextSize;
    //background
    private int mBgColor;

    //inputtype
    private int mInputType;

    //输入框内容变化接口
    private MyTextWatcher mWatcher;

    public DeleteEdittext(Context context) {
        this(context, null);
    }

    public DeleteEdittext(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public DeleteEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DeleteEdittext, defStyle, 0);
        mHint = a.getText(R.styleable.DeleteEdittext_text_hint);
        mTextColor = a.getColor(R.styleable.DeleteEdittext_text_color,
                Color.BLACK);
        mTextSize = a.getDimension(R.styleable.DeleteEdittext_text_size, 15);
        mBgColor = a.getColor(R.styleable.DeleteEdittext_bg_color,
                Color.parseColor("#ffffff"));

        mInputType=a.getInteger(R.styleable.DeleteEdittext_inputtype, InputType.TYPE_CLASS_TEXT);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.layout_deleteedittet, this);
        mEditText = (EditText) findViewById(R.id.my_et);
        mDelete = (ImageView) findViewById(R.id.my_delete);

        if (!TextUtils.isEmpty(mHint)) {
            mEditText.setHint(mHint);
        }
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
        mEditText.setTextColor(mTextColor);
        mEditText.setBackgroundColor(mBgColor);
        mEditText.setInputType(mInputType);

        mDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEditText.setText("");
                mDelete.setVisibility(View.GONE);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.toString().trim() != null) {
                    mDelete.setVisibility(View.VISIBLE);
                } else {
                    mDelete.setVisibility(View.GONE);
                }

                if (mWatcher != null) {
                    mWatcher.textChange(s);
                }
            }
        });
    }

    public String getText() {
        if (mEditText == null) {
            return null;
        }

        return mEditText.getText().toString().trim();
    }

    public void setText(String text){
        mEditText.setText(text);
    }

    public void setTextWatcher(MyTextWatcher watcher) {
        this.mWatcher = watcher;
    }

    public interface MyTextWatcher {
        void textChange(Editable s);
    }

}

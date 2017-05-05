package com.wyc.mdtextinputlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by 李小明 on 17/5/3.
 * 邮箱:287907160@qq.com
 */

public class MDTextInputLayout extends RelativeLayout implements TextWatcher {

    private EditText editText; //輸入
    private TextView textView; //底部展示

    public MDTextInputLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MDTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MDTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextInput, 0, 0);

        String bottomText = a.getString(R.styleable.CustomTextInput_bottomText);
        float bottomSize = a.getDimension(R.styleable.CustomTextInput_bottomSize, 12);
        int bottomColor = a.getColor(R.styleable.CustomTextInput_bottomColor, Color.BLUE);

        Log.i("lxm",bottomSize + "  size");
        editText = new EditText(context);
        editText.setHint(bottomText);
        editText.setPadding(0, 10, 10, 10);
        editText.setTextSize(14);
        editText.setBackgroundColor(Color.WHITE);
        textView = new TextView(context);
        textView.setTextSize(bottomSize);
        textView.setText(bottomText);
        textView.setTextColor(bottomColor);

        editText.addTextChangedListener(this);
        addView(textView);
        addView(editText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int height = 0;

        MarginLayoutParams cParams;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            height += cHeight + cParams.topMargin + cParams.bottomMargin;
        }

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(getMeasuredWidth(), (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int height = editText.getMeasuredHeight();
        final int paddingLeft = editText.getPaddingLeft();
        final int paddingTop = editText.getPaddingTop();
        String src = editText.getText().toString();

        editText.layout(0,
                (getHeight() - editText.getMeasuredHeight()) / 2,
                getMeasuredWidth(),
                (getHeight() + editText.getMeasuredHeight()) / 2);

        if (src.length() == 0) {
            editText.layout(0,
                    (getHeight() - editText.getMeasuredHeight()) / 2,
                    getMeasuredWidth(),
                    (getHeight() + editText.getMeasuredHeight()) / 2);
            textView.setVisibility(GONE);
            textView.layout(paddingLeft, paddingTop, getMeasuredWidth(), textView.getMeasuredHeight() + paddingTop);

        } else {
            editText.layout(0, 0, getMeasuredWidth(), editText.getMeasuredHeight());
            textView.setVisibility(VISIBLE);
            textView.layout(paddingLeft, height, getMeasuredWidth(), textView.getMeasuredHeight() + height);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        postInvalidate();
    }


    public String getInputText() {
        return editText.getText().toString();
    }

}

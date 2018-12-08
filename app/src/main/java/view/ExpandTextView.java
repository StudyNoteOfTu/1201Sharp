package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tufengyi.sharp.R;

public class ExpandTextView extends LinearLayout {
        private TextView mTextView;
        private ImageView mOpenBtn;
        private boolean isOpen = false;
        private int foldLines = 3; //大于3行的时候折叠
        private int lineCounts;

        public ExpandTextView(Context context) {
            this(context, null);
        }

        public ExpandTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

        }

        private void initView() {   //初始ExpandTextView（LinearLayout的View）

            lineCounts = mTextView.getLineCount();  //获得textView中的总行数
            if(lineCounts <= foldLines){
                mOpenBtn.setVisibility(GONE);   //如果说总行数比预设的折叠后的行数来的小，那么就不绘制小图标，
                //不绘制时候用Gone比inAvailable好，加快绘制速度
            }
            //接下来进行一系列判断
            //预设的高度如果与文字部分高度不一致的话进行调整
            if(isOpen && mTextView.getHeight() != lineCounts * mTextView.getLineHeight()){
                //如果是打开的，并且 高度和文字部分的高度不一致
                mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
                //那么设置textView高度为文字部分高度
            }else if(!isOpen && mTextView.getHeight() != foldLines * mTextView.getLineHeight()){
                //如果是折叠起来的的，并且 高度和文字部分的高度不一致
                mTextView.setHeight(mTextView.getLineHeight() * foldLines);
                //那么设置TextView的高度为折叠行数（3）×文字部分每行的高度
            }
            mOpenBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOpen){
                        //收缩
                        mTextView.setHeight(mTextView.getLineHeight() * foldLines);
                        //设置高度为三行
                        mOpenBtn.setImageResource(R.drawable.icon_up);
                        //设置为向上图标
                        isOpen = false;
                        //收起状态

                    }else{
                        //展开
                        mTextView.setHeight(mTextView.getLineHeight() * mTextView.getLineCount());
                        //设置高度符合文字部分高度
                        mOpenBtn.setImageResource(R.drawable.icon_down);
                        //设置为向下图标
                        isOpen = true;
                        //展开状态
                    }
                }
            });
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            initView();
            //在onMesure部分设置initView
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            if(mTextView==null || mOpenBtn == null){
                mTextView = (TextView)getChildAt(0);
                mOpenBtn = (ImageView)getChildAt(1);
            }

        }
    }
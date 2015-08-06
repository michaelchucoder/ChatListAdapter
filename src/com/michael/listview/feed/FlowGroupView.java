package com.michael.listview.feed;

/**
 * Created with IntelliJ IDEA
 * Package name: com.michael.listview.feed
 * Author: MichaelChuCoder
 * Date: 2015-8-6
 * Time: 10:55
 * To change this template use File | Settings | File and Code Templates.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowGroupView extends ViewGroup {

    /**
     * �������е�view ���м�¼
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * ��¼ÿһ�еĸ߶�
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    private String TAG = "TAG";

    public FlowGroupView(Context context, AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowGroupView(Context context) {
        super(context);
    }

    /**
     * ����childView��λ�õĲ���
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // ��ǰ�е����߶�
        int lineHeight = 0;
        // �洢ÿһ�����е�childView
        List<View> lineViews = new ArrayList<View>();
        int left = 0;
        int top = 0;
        // �õ�������
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            // ÿһ�е����е�views
            lineViews = mAllViews.get(i);
            // ��ǰ�е����߶�
            lineHeight = mLineHeight.get(i);

            Log.e(TAG, "��" + i + "�� ��" + lineViews.size() + " , " + lineViews);
            Log.e(TAG, "��" + i + "�У� ��" + lineHeight);

            // ������ǰ�����е�View
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //����childView��left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
        Log.v(TAG, "onLayout   mAllViews.size() -- > " + mAllViews.size() + "   mLineHeight.size() -- > " + mLineHeight.size());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // �ÿ� view ���� �� lineHeight ����  ���¸�ֵ
        mAllViews.clear();
        mLineHeight.clear();

        //�õ��ϼ�����Ϊ���Ƽ��Ŀ�ߺͼ���ģʽ
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeighMode = MeasureSpec.getMode(heightMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specHeighSize = MeasureSpec.getSize(heightMeasureSpec);
        // ��������е� child �� ��͸�
        measureChildren(specWidthSize, specHeighSize);
        // ��¼����� warp_content �����õĿ�͸�
        int width = 0;
        int height = 0;
        // �õ���view�ĸ���
        int cCount = getChildCount();
        /**
         * ��¼ÿһ�еĿ�ȣ�width����ȡ�����
         */
        int lineWidth = 0;
        /**
         * ÿһ�еĸ߶ȣ��ۼ���height
         */
        int lineHeight = 0;

        // �洢ÿһ�����е�childView
        List<View> lineViews = new ArrayList<View>();

        for (int i = 0; i < cCount; i++) {
            // �õ�ÿ����View
            View child = getChildAt(i);
            // ����ÿ����View�Ŀ��
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // ��ǰ��view��lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // ��view�Ŀ�͸�
            int cWidth = 0;
            int cheight = 0;
            // ��ǰ�� view ʵ��ռ�Ŀ�
            cWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // ��ǰ��View ʵ��ռ�ĸ�
            cheight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // ��Ҫ����
            if (lineWidth + cWidth > specWidthSize) {
                width = Math.max(lineWidth, cWidth);// ȡ���ֵ
                lineWidth = cWidth; // �������е�ʱ�������ۼ�width
                height += lineHeight; // ��������ʱ�ۼ� height
                lineHeight = cheight; // ��¼��һ�еĸ߶�
                mAllViews.add(lineViews);
                mLineHeight.add(lineHeight);
                lineViews = new ArrayList<View>();
                // ���е�ʱ��Ѹ� view �Ž� ������
                lineViews.add(child);// ���  view(child) ����һ�еĵ�һ��view
                Log.v(TAG, "onl  mAllViews.size()  --  > " + mAllViews.size());
            } else {
                // ����Ҫ����
                lineWidth += cWidth;//
                height = Math.max(lineHeight, cheight);// ȡ���ֵ
                // ����Ҫ����ʱ ����View add ������
                lineViews.add(child);
            }

            if (i == cCount - 1) {
                // ��������һ��view
                width = Math.max(lineWidth, cWidth);
                height += lineHeight;
            }
        }
        // ѭ�������� �����һ������add��������
        mLineHeight.add(lineHeight); // ��¼���һ��
        mAllViews.add(lineViews);
        // MeasureSpec.EXACTLY ��ʾ�����˾�ȷ��ֵ
        // ��� mode �� MeasureSpec.EXACTLY ʱ������ warp_content �ü�������ֵ�����������ϼ����ַָ�����ֵ
        setMeasuredDimension(specWidthMode == MeasureSpec.EXACTLY ? specWidthSize : width
                , specHeighMode == MeasureSpec.EXACTLY ? specHeighSize : height);
        Log.v(TAG, "onLayout  onMeasure   mAllViews.size() -- > " + mAllViews.size() + "   mLineHeight.size() -- > " + mLineHeight.size());
    }

    /**
     * ���һ��Ҫ���ã�������ǿת����
     * ������֧�� marginLayoutParams
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new MarginLayoutParams(getContext(), attrs);
    }

}

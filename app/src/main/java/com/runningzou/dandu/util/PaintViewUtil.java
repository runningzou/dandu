package com.runningzou.dandu.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.runningzou.dandu.R;
import com.runningzou.dandu.app.DanduApp;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.ui.SelectTextView;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PaintViewUtil {

    private Typeface typeFace;

    @Inject
    public PaintViewUtil() {
        typeFace = Typeface.createFromAsset(DanduApp.getApplication().getAssets(), "fonts/PMingLiU.ttf");
    }

    private void addBlock(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {
        Context context = parent.getContext();
        SelectTextView txt = new SelectTextView(context);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(layoutParams);
        parent.addView(txt);

    }

    private void addH3TextView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {
        Context context = parent.getContext();
        SelectTextView txt = new SelectTextView(context);
        txt.setSingleLine(false);
        txt.setTextIsSelectable(true);
        txt.setTypeface(typeFace);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }

    private void addH4TextView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {
        Context context = parent.getContext();
        SelectTextView txt = new SelectTextView(context);
        txt.setSingleLine(false);
        txt.setTextIsSelectable(true);
        txt.setTextColor(context.getResources().getColor(R.color.black));
        txt.setTypeface(typeFace);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }

    private void addH5TextView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {
        Context paramContext = parent.getContext();
        SelectTextView txt = new SelectTextView(paramContext);
        txt.setSingleLine(false);
        txt.setTextIsSelectable(true);
        txt.setTextColor(paramContext.getResources().getColor(R.color.green));
        txt.setTextSize(10);
        txt.setTypeface(typeFace);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }

    private void addH6TextView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {
        Context context = parent.getContext();
        SelectTextView txt = new SelectTextView(context);
        txt.setSingleLine(false);
        txt.setTextIsSelectable(true);
        txt.setTextColor(context.getResources().getColor(R.color.black));
        txt.setTextSize(8);
        txt.setLineSpacing(1.5f, 1.8f);
        txt.setTypeface(typeFace);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }

    private void addImageView(ViewGroup parent, String imgWidth, String imgHeight, String imgUrl) {
        Context context = parent.getContext();
        ImageView localImageView = (ImageView) View.inflate(context, R.layout.item_image_view, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if ((imgWidth != null) && (imgHeight != null) && (!imgWidth.isEmpty()) && (!imgHeight.isEmpty())) {
            String str3 = imgWidth.replace("px", "");
            String str4 = imgHeight.replace("px", "");
            int imgW = DimenUtils.dp2px((float) Double.parseDouble(str3));
            int imgH = DimenUtils.dp2px((float) Double.parseDouble(str4));
            layoutParams.height = (DimenUtils.getWindowWidth() * imgH /
                    imgW);
        }
        localImageView.setLayoutParams(layoutParams);
        parent.addView(localImageView);
        Glide.with(context).load(imgUrl).into(localImageView);
    }

    private void addPoetry(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {

        Context paramContext = parent.getContext();
        SelectTextView txt = new SelectTextView(paramContext);
        txt.setSingleLine(false);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setTextColor(paramContext.getResources().getColor(R.color.black));
        txt.setTypeface(typeFace);
        parent.addView(txt);
    }

    private void addTextSpanView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {

        Context paramContext = parent.getContext();
        SelectTextView txt = new SelectTextView(paramContext);
        txt.setSingleLine(false);
        txt.setLineSpacing(1.5f, 1.8f);
        txt.setTextSize(15);
        txt.setTextIsSelectable(true);
        txt.setTextColor(paramContext.getResources().getColor(R.color.black));
        txt.setTypeface(typeFace);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 14, 0, 0);
        txt.setLayoutParams(layoutParams);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }

    private void addStrongTextSpanView(ViewGroup parent, SpannableStringBuilder paramSpannableStringBuilder) {

        Context context = parent.getContext();
        SelectTextView txt = new SelectTextView(context);
        txt.setSingleLine(false);
        txt.setLineSpacing(1.5f, 1.8f);
        txt.setTextSize(15);
        txt.setTextIsSelectable(true);
        txt.setTextColor(context.getResources().getColor(R.color.black));
        txt.setTypeface(typeFace);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 14, 0, 0);
        txt.setGravity(Gravity.LEFT);
        txt.setLayoutParams(layoutParams);
        TextPaint tp = txt.getPaint();
        tp.setFakeBoldText(true);
        txt.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        txt.setGravity(Gravity.LEFT);
        parent.addView(txt);
    }



    public void addTypeView(ContentParseResult result, ViewGroup parent) {
        switch (result.getType()) {
            case 0:
            case 1:
            case 2:
                addTextSpanView(parent, result.getStringBuilder());
                break;
            case 3:
                addH3TextView(parent, result.getStringBuilder());
                break;
            case 4:
                addH4TextView(parent, result.getStringBuilder());
                break;
            case 5:
                addH5TextView(parent, result.getStringBuilder());
                break;
            case 6:
                addH6TextView(parent, result.getStringBuilder());
                break;
            case 7:
                addBlock(parent, result.getStringBuilder());
                break;
            case 8:
                addPoetry(parent, result.getStringBuilder());
                break;
            case 9:
                addImageView(parent, result.getImgWidth(), result.getImgHeight(), result.getImgUrl());
                break;
            case 11:
                addStrongTextSpanView(parent, result.getStringBuilder());
                break;
        }

    }
}

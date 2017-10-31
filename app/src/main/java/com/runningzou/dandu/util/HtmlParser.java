package com.runningzou.dandu.util;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.runningzou.dandu.app.DanduApp;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.model.entity.Detail;
import com.runningzou.dandu.model.entity.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Iterator;

import io.reactivex.ObservableEmitter;

/**
 * Created by runningzou on 2017/10/30.
 */

public class HtmlParser {

    public static void parse(Result<Detail> detailResult, ObservableEmitter<ContentParseResult> e) {
        Detail detail = detailResult.getDatas();
        String content = detail.getContent();

        if (detail.getParseXML() == 1) {
            Document document = Jsoup.parseBodyFragment(content.replaceAll("<br/>", "\n"));

            Iterator localIterator = document.getAllElements().iterator();
            while (localIterator.hasNext()) {
                Element localElement = (Element) localIterator.next();
                parseEle(localElement, e);
            }
        } else {
            ContentParseResult contentParseResult = new ContentParseResult();
            contentParseResult.setType(ContentParseResult.HTML5_TYPE);
            contentParseResult.setHtml5Url(addParams2WezeitUrl(detail.getHtml5(), false));
        }

        e.onComplete();
    }

    private static void parseEle(Element localElement, ObservableEmitter<ContentParseResult> e) {

        if ((localElement.nodeName().matches("p[1-9]?[0-9]?")) || (localElement.nodeName().matches("h[1-9]?[0-9]?")) || (localElement.nodeName().matches("poetry")) || (localElement.nodeName().matches("block"))) {
            parseText(localElement, e);
        }
        if (localElement.nodeName().matches("img")) {

            int viewType = 9;
            String imgUrl = localElement.attr("src");

            if (imgUrl.isEmpty()) {
                imgUrl = localElement.attr("href");
            }


            String imgWidth = localElement.attr("width");
            String imgHeight = localElement.attr("height");

            ContentParseResult result = new ContentParseResult();
            result.setType(viewType);
            result.setImgHeight(imgHeight);
            result.setImgWidth(imgWidth);
            result.setImgUrl(imgUrl);

            e.onNext(result);
        }

    }


    private static void parseText(Element localElement, ObservableEmitter<ContentParseResult> e) {

        String ele = localElement.text();
        SpannableStringBuilder ssb = null;
        int viewType = 0;
        if (!TextUtils.isEmpty(ele)) {
            ssb = new SpannableStringBuilder("\n" + ele);
            if (localElement.nodeName().equals("h1")) {
                viewType = 1;
            } else if (localElement.nodeName().equals("h2")) {
                viewType = 2;
            } else if (localElement.nodeName().equals("h3")) {
                viewType = 3;
            } else if (localElement.nodeName().equals("h4")) {
                viewType = 4;
            } else if (localElement.nodeName().equals("h5")) {
                viewType = 5;
            } else if (localElement.nodeName().equals("h6")) {
                viewType = 6;
            } else if (localElement.nodeName().equals("block")) {
                viewType = 7;
            } else if (localElement.nodeName().equals("poetry")) {
                viewType = 8;
            } else if (localElement.nodeName().equals("hr")) {
                viewType = 10;
            } else {
                viewType = 0;
                if (localElement.nodeName().contains("strong")) {
                    viewType = 11;
                }
                ssb = new SpannableStringBuilder("\n" + setFirstLineSpace(ele, 2));
            }

        }

        ContentParseResult result = new ContentParseResult();
        result.setStringBuilder(ssb);
        result.setType(viewType);

        e.onNext(result);
    }


    private static String setFirstLineSpace(String str, int paramInt) {
        for (int i = paramInt; i >= 0; i--) {
            str = "  " + str;
        }
        return str;
    }

    private static String addParams2WezeitUrl(String url, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(url);
        localStringBuffer.append("?client=android");
        localStringBuffer.append("&device_id=" + DanduApp.getmDeviceId());
        localStringBuffer.append("&version=" + "1.3.0");
        if (paramBoolean)
            localStringBuffer.append("&show_video=0");
        else {
            localStringBuffer.append("&show_video=1");
        }
        return localStringBuffer.toString();
    }

}

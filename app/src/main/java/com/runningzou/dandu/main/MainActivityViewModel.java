package com.runningzou.dandu.main;

import android.arch.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.runningzou.dandu.base.rxjava.RxCommand;
import com.runningzou.dandu.model.entity.GetListParameter;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.model.entity.Result;
import com.runningzou.dandu.model.net.ApiService;
import com.runningzou.dandu.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by runningzou on 17-10-28.
 */

@Singleton
public class MainActivityViewModel extends ViewModel {

    private RxCommand<List<Item>, GetListParameter> mGetListRxCommand;
    private RxCommand<String, String> mGetRecommendRxCommand;

    private ApiService mApiService;

    @Inject
    public MainActivityViewModel(ApiService apiService) {
        mApiService = apiService;
    }

    public RxCommand<List<Item>, GetListParameter> getListRxCommand() {
        if (mGetListRxCommand == null) {
            mGetListRxCommand = RxCommand.create(new Function<GetListParameter, Observable<List<Item>>>() {
                @Override
                public Observable<List<Item>> apply(@NonNull GetListParameter getListParameter) throws Exception {
                    return mApiService.getList("api", "getList", getListParameter.getPage(), getListParameter.getModel(),
                            getListParameter.getPageId(), getListParameter.getCreateTime(), "android", "1.3.0",
                            TimeUtil.getCurrentSeconds(), getListParameter.getDeviceId(), 1)
                            .map(new Function<Result<List<Item>>, List<Item>>() {
                                @Override
                                public List<Item> apply(@NonNull Result<List<Item>> itemResult) throws Exception {
                                    return itemResult.getDatas();
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }
        return mGetListRxCommand;
    }

    public RxCommand<String, String> getRecommendRxCommand() {
        if (mGetRecommendRxCommand == null) {
            mGetRecommendRxCommand = RxCommand.create(new Function<String, Observable<String>>() {
                @Override
                public Observable<String> apply(@NonNull String deviceId) throws Exception {
                    return mApiService.getRecommend("home", "Api", "getLunar", "android", deviceId, deviceId)
                            .map(new Function<String, String>() {
                                @Override
                                public String apply(@NonNull String s) throws Exception {
                                    String key = TimeUtil.getDate("yyyyMMdd");
                                    try {
                                        JsonParser jsonParser = new JsonParser();
                                        JsonElement jel = jsonParser.parse(s);
                                        JsonObject jsonObject = jel.getAsJsonObject();
                                        jsonObject = jsonObject.getAsJsonObject("datas");
                                        jsonObject = jsonObject.getAsJsonObject(key);
                                        String result = jsonObject.get("thumbnail").getAsString();
                                        Logger.d(result);
                                        return result;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }

                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mGetRecommendRxCommand;
    }
}

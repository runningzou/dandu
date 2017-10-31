package com.runningzou.dandu.detail;

import com.runningzou.dandu.base.rxjava.RxCommand;
import com.runningzou.dandu.model.entity.ContentParseResult;
import com.runningzou.dandu.model.entity.Detail;
import com.runningzou.dandu.model.entity.Result;
import com.runningzou.dandu.model.net.ApiService;
import com.runningzou.dandu.util.HtmlParser;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by runningzou on 2017/10/30.
 */

@Singleton
public class DetailViewModel {

    private ApiService mApiService;

    private RxCommand<ContentParseResult, String> mGetDetailRxCommand;

    @Inject
    public DetailViewModel(ApiService apiService) {
        mApiService = apiService;
    }

    public RxCommand<ContentParseResult, String> getGetDetailRxCommand() {
        if (mGetDetailRxCommand == null) {
            mGetDetailRxCommand = RxCommand.create(new Function<String, Observable<ContentParseResult>>() {
                @Override
                public Observable<ContentParseResult> apply(@NonNull String itemId) throws Exception {

                    return mApiService.getDetail("api", "getPost", itemId, 1)
                            .flatMap(new Function<Result<Detail>, ObservableSource<ContentParseResult>>() {
                                @Override
                                public ObservableSource<ContentParseResult> apply(@NonNull final Result<Detail> detailResult) throws Exception {

                                    return Observable.create(new ObservableOnSubscribe<ContentParseResult>() {
                                        @Override
                                        public void subscribe(@NonNull ObservableEmitter<ContentParseResult> e) throws Exception {
                                            HtmlParser.parse(detailResult,e);
                                        }
                                    });
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            });
        }

        return mGetDetailRxCommand;
    }

}



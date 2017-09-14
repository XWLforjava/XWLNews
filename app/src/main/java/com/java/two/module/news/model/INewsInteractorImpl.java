package com.java.two.module.news.model;

import com.java.two.R;
import com.java.two.app.App;
import com.java.two.callback.RequestCallback;
import com.java.two.greendao.NewsChannelTable;
import com.java.two.greendao.NewsChannelTableDao;
import com.java.two.utils.SpUtil;
import com.socks.library.KLog;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * ClassName: INewsInteractorImpl<p>
 * Author: oubowu<p>
 * Fuction: 新闻Model层接口实现,数据库操作，第一次初始化频道，之后查询选中的频道<p>
 * CreateDate: 2016/2/20 15:05<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class INewsInteractorImpl implements INewsInteractor<List<NewsChannelTable>> {

    @Override
    public Subscription operateChannelDb(final RequestCallback<List<NewsChannelTable>> callback) {

        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {

                final NewsChannelTableDao dao = ((App) App.getContext()).getDaoSession()
                        .getNewsChannelTableDao();
                //SpUtil.writeBoolean("initDb", false);
                //dao.deleteAll();
                //修改数据库时需要加入以上两行 xys
                KLog.e("初始化了数据库了吗？ " + SpUtil.readBoolean("initDb"));
                if (!SpUtil.readBoolean("initDb")) {

                    List<String> channelName = Arrays.asList(App.getContext().getResources()
                            .getStringArray(R.array.news_channel));

                    List<Integer> category = Arrays.asList(ArrayUtils.toObject(App.getContext().getResources()
                            .getIntArray(R.array.category)));

                    for (int i = 0; i < channelName.size(); i++) {
                        NewsChannelTable table = new NewsChannelTable(channelName.get(i),
                                category.get(i), "1", i <= 2,
                                // 改为第一个固定
                                i, i == 0);
                        dao.insert(table);
                    }
                    SpUtil.writeBoolean("initDb", true);
                    KLog.e("数据库初始化完毕！");
                }

                final Query<NewsChannelTable> build = dao.queryBuilder()
                        .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                        .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
                subscriber.onNext(build.list());
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callback.beforeRequest();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.getLocalizedMessage() + "\n" + e);
                        callback.requestError(e.getLocalizedMessage() + "\n" + e);
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannels) {
                        callback.requestSuccess(newsChannels);
                    }
                });
    }
}

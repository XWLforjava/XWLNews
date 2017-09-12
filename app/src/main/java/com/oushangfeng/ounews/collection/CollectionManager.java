package com.oushangfeng.ounews.collection;

import com.oushangfeng.ounews.app.App;
import com.oushangfeng.ounews.base.BaseSchedulerTransformer;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by l on 2017/9/12.
 * author:lcy
 * func:存取收藏文件
 */

public class CollectionManager {
    private CollectionManager(){}

    private static CollectionManager manager = null;

    public static synchronized CollectionManager getInstance() {
        if (manager == null) {
            manager = new CollectionManager();
        }
        return manager;
    }

    public Observable<ArrayList<TsinghuaNewsSummary>> getCollections(){
        return Observable.create(new Observable.OnSubscribe<ArrayList<TsinghuaNewsSummary>>() {
            @Override
            public void call(Subscriber<? super ArrayList<TsinghuaNewsSummary>> subscriber) {
                subscriber.onNext(getCollectionsList());
                subscriber.onCompleted();
            }
        });
    }

    public ArrayList<TsinghuaNewsSummary> getCollectionsList(){
        ArrayList<TsinghuaNewsSummary> newslist = new ArrayList<TsinghuaNewsSummary>();
        String path = App.getContext().getFilesDir().getAbsolutePath();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for(int i = 0; i < tempList.length; i ++)
        {
            if(tempList[i].isFile() && tempList[i].toString().endsWith(".colc")){
                try {
                    FileInputStream fis = new FileInputStream(tempList[i]);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    newslist.add((TsinghuaNewsSummary)ois.readObject());
                    ois.close();
                } catch(IOException e) {
                    System.out.println(e);
                } catch(ClassNotFoundException e) {
                    System.out.println(e);
                }
            }
        }
        return newslist;
    }

    public void addCollection(TsinghuaNewsSummary news){
        String path = App.getContext().getFilesDir().getAbsolutePath();
        try {
            FileOutputStream ff = new FileOutputStream(new File(path + "/" + news.id + ".colc"));
            ObjectOutputStream ss = new ObjectOutputStream(ff);
            ss.writeObject(news);
            ss.flush();
            ss.close();
        }catch(IOException e) {
            System.out.println(e);
        }
    }

    public void deleteCollection(TsinghuaNewsSummary news){
        String path = App.getContext().getFilesDir().getAbsolutePath();
        File file = new File(path + "/" + news.id + ".colc");
        file.delete();
    }

    public boolean checkInCollections(TsinghuaNewsSummary news){
        String path = App.getContext().getFilesDir().getAbsolutePath();
        File file = new File(path + "/" + news.id + ".colc");
        return file.exists();
    }
}

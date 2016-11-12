package com.longforus.resulthandle.bean;

import java.util.List;

/**
 * Created by Void Young on 10/11/2016  8:00 PM.
 * 	http://www.tngou.net/tnfs/api/list
 * 	测试bean
 */

public class GalleryListBean {
    public boolean status ;
    public int total;
    public List<TngouBean> tngou;
    public static class TngouBean {
        public int count;
        public int fcount;
        public int galleryclass;
        public int id;
        public String img;
        public int rcount;
        public int size;
        public long time;
        public String title;
    }
}

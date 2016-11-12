package com.longforus.resulthandle.net;

import com.longforus.resulthandle.bean.GalleryListBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Void Young on 11/10/2016  9:36 PM.
 * Description :
 */

public interface GalleryListInt {
    @GET("list")//请求方法(请求rul)
    Observable<GalleryListBean> getGalleryList(@Query("id") int id, @Query("page") int pageNum,
        @Query("rows") int rowNum);//参数是请求参数,返回结果的observable
}

# 使用OkHttp的addInterceptor功能实现RxAndroid+Retrofit请求结果的统一处理
 昨天看了有2个文章:

Retrofit响应数据及异常处理策略:[http://blog.csdn.net/dd864140130/article/details/52689010](http://blog.csdn.net/dd864140130/article/details/52689010)

以及github上另外一种方法实现的demo:
[https://github.com/ysmintor/Retrofit2RxjavaDemo#retrofit2rxjavademo](https://github.com/ysmintor/Retrofit2RxjavaDemo#retrofit2rxjavademo)

主要就是分离错误逻辑把所有即使返回了结果但是结果非预期的error全部放到onErroe方法中去处理.在拜读了以后,心里有一点小小的疑问,這样虽然分离了error的处理,在实际使用中出错的情况总不会很多吧?多出這么多的跳转处理,性能上的开销会不会大过收益,得不偿失?

 今天又看了一篇文章:
 关于Retrofit2+Okhttp3实现统一添加请求参数和重定向:
[http://blog.csdn.net/yyh352091626/article/details/53082350](http://blog.csdn.net/yyh352091626/article/details/53082350)

我就在想能不能使用第三种方法来实现,上面2篇文章所实现的功能呢?

实验了一下发现是可以的,而且好像更加的容易实现:

## 概览
### 一.定义检测我们需要的error的Interceptor,并添加到OkHttpClient

	private static class ErrorInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Response response = chain.proceed(oldRequest);
            byte[] respBytes = response.body()
                                   .bytes();
            String respString = new String(respBytes);
            try {
                JSONObject object = new JSONObject(respString);
                int code = (int) object.get("total");//模拟校验情况,实际使用中根据情况来实际判断
                Log.i(TAG, "intercept: code = "+code);//实际返回的code是303  這里模拟使用中返回了结果但是,结果并非完全预期的情况
                if (code != ResultStatus.OK) {//开启判断制造异常,查看效果
                    throw new ResultException("返回码异常", code);//抛出自定义异常,在subscriber的onError中被接收,达到分离处理的目的
                }
                return response.newBuilder()
                               .body(ResponseBody.create(null, respBytes))
                               .build();//在前面获取bytes的时候response的stream已经被关闭了,要重新生成response
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ResultException("解析异常", ResultStatus.PARSE_ERROR);
            }
        }
    }


### 二.自定义ResultException,标记非预期情况下要抛出的异常
	public class ResultException extends RuntimeException {
   	 private int errorCode;

    public ResultException(String message,int code) {
        this(message);
        errorCode = code;
    }

   
    public ResultException(Throwable cause) {
        super(cause);
    }
	}

### 三.在Subscriber的OnError中接收异常并进行处理

 	 @Override
   	 protected void onError(ResultException e) {
        Log.w(TAG, "onError: "+e.getMessage(),e );
        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
             .show();
  	  }

貌似就完成了,简单测试确实可以达到上面的目的,但是是否合用?以及效率问题,我功力太浅,不敢随便乱说,有一个疑问就是在intercept()方法的执行机制,在这个方法里面执行: 
         
	  Response response = chain.proceed(oldRequest);
会不会导致2次请求的发送?如果是這样的话,感觉这个方法就不太可行了.

待研究,待指点....

github:
[https://github.com/longforus/InterceptorHandleError](https://github.com/longforus/InterceptorHandleError)  
水平有限,没错误不正常,希望得到你的指点

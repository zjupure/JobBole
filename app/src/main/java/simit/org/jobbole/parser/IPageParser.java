package simit.org.jobbole.parser;

import okhttp3.Response;

/**
 * Created by liuchun on 2016/4/2.
 */
public interface IPageParser<T> {
    /** parse data mode from network response */
    T parse(Response response) throws Exception;
    /** parse success callback */
    void onSuccess(T response);
    /** parse failured callback */
    void onFailure(Exception e);
}

package simit.org.jobbole.parser;

import java.io.InputStream;

import okhttp3.Response;
import simit.org.jobbole.bean.RSSFeed;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DefaultRssParser implements IPageParser<RSSFeed> {
    @Override
    public RSSFeed parse(Response response) throws Exception {
        InputStream is = response.body().byteStream();
        IRSSParser parser = RSSParserFactory.newInstance();
        RSSFeed rssFeed = parser.parse(is);

        return rssFeed;
    }

    @Override
    public void onSuccess(RSSFeed response) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}

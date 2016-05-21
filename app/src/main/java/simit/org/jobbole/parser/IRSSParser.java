package simit.org.jobbole.parser;

import java.io.InputStream;

import simit.org.jobbole.bean.RSSFeed;

/**
 * Created by liuchun on 2016/3/27.
 */
public interface IRSSParser {
    /**
     * 解析输入流,获取RSSFeed
     * @param is
     * @return
     * @throws Exception
     */
    RSSFeed parse(InputStream is) throws Exception;

    /**
     * 序列化RSSFeed得到Json字符串
     * @param feed
     * @return
     * @throws Exception
     */
    String serialize(RSSFeed feed) throws Exception;
}

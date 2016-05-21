package simit.org.jobbole.parser;


/**
 * Created by liuchun on 2016/3/27.
 */
public class RSSParserFactory {
    public static final int PULL_PARSER = 0;
    public static final int SAX_PARSER = 1;
    public static final int DOM_PARSER = 2;

    /** the default parser is PULL parser*/
    public static IRSSParser newInstance() {
        return new RSSFeedPULLParser();
    }

    /** specify a custom parser */
    public static IRSSParser newInstance(int config){
        IRSSParser parser = null;
        switch (config){
            case SAX_PARSER:
                parser = new RSSFeedSAXParser();
                break;
            case DOM_PARSER:
                parser = new RSSFeedDOMParser();
                break;
            case PULL_PARSER:
            default:
                parser = new RSSFeedPULLParser();
                break;
        }
        return parser;
    }
}

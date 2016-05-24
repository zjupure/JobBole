package simit.org.jobbole.locationbean;

import java.util.List;

/**
 * Created by liuchun on 2016/5/23.
 */
public class AddrWrapper {

    private List<FullAddress> results;

    private String status;

    public List<FullAddress> getResults() {
        return results;
    }

    public void setResults(List<FullAddress> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

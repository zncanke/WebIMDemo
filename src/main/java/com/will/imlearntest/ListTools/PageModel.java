package com.will.imlearntest.ListTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by willl on 5/19/16.
 */
public class PageModel<T> {
    private Class<T> clazz;
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 偏移量
     */
    private int offset = 0;
    /**
     * 页容量
     */
    private int limit = 10;
    /**
     * 总量
     */
    private int total = 0;
    /**
     * 查询条件
     */
    private Map<String, String> params;

    public void setData(List<T> data) {
        this.data = data;
    }
    public List<T> getData() {
        return data;
    }
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
    public Class<T> getClazz() {
        return clazz;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    public int getOffset() {
        return offset;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getLimit() {
        return limit;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public int getCurPage() {
        if (offset == 0) {
            return 1;
        }

        if (offset / limit == 0) {
            return 1;
        }

        if (offset >= total) {
            if (total % limit == 0) {
                return total / limit;
            } else {
                return total / limit + 1;
            }
        }

        return offset / limit + 1;
    }

    public int getTotalPage() {
        if (total % limit == 0) {
            return total / limit;
        } else {
            return total / limit + 1;
        }
    }

    public boolean isFirst() {
        return getCurPage() <= 1;
    }

    public String getFirstUrl() {
        return getUrl(1);
    }

    public boolean hasPre() {
        return getCurPage() > 1;
    }

    public String getPreUrl() {
        int prePage = 1;

        if (hasPre()) {
            prePage = getCurPage() - 1;
        }
        return getUrl(prePage);
    }

    public boolean hasNext() {
        return getCurPage() < getTotalPage();
    }

    public String getNextUrl() {
        int nextPage = getTotalPage();
        if (hasNext()) {
            nextPage = getCurPage() + 1;
        }
        return getUrl(nextPage);
    }

    public String getLastUrl() {
        return getUrl(getTotalPage());
    }

    public boolean isLast() {
        return getCurPage() >= getTotalPage();
    }

    public String getUrl(int page) {
        String baseURL = getBaseUrl();
        String url = baseURL + "&page=" + page;
        return url;
    }

    public String getBaseUrl() {
        String baseURL = BaseURLUtil.getBaseURL(clazz);
        baseURL = baseURL + "&limit=" + limit;
        if (this.params != null) {
            for (String key : this.params.keySet()) {
                baseURL = baseURL + "&" + key + "=" + this.params.get(key);
            }
        }
        return baseURL;
    }

    public void addParam(String paramKey, String paramValue) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put(paramKey, paramValue);
    }
}

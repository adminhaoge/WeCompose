package com.google.samples.apps.http;

import com.google.samples.apps.http.utils.Preconditions;

/**
 * Created by hlx-wc on 2017/11/11.
 * <p>
 * Query key-value int url when execute http GET request
 */
public class Query {
    final String mKey;
    final String mValue;

    public Query(String key, String value) {
        mKey = Preconditions.checkNotNull(key);
        mValue = value;

    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue == null ? "" : mValue;
    }

    @Override
    public String toString() {
        return "Query{" +
                "mKey='" + mKey + '\'' +
                ", mValue='" + mValue + '\'' +
                '}';
    }
}

package com.example.rxjavapractice;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.nio.ByteBuffer;

public class WXDataFetcher implements DataFetcher<ByteBuffer> {
    private String model;

    public WXDataFetcher(String model) {
        this.model = model;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super ByteBuffer> callback) {
        String base64Section = getBase64SectionOfModel();
        byte[] data = Base64.decode(base64Section, Base64.DEFAULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        callback.onDataReady(byteBuffer);
    }

    private String getBase64SectionOfModel() {
        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs.
        int startOfBase64Section = model.indexOf(',');
        return model.substring(startOfBase64Section + 1);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<ByteBuffer> getDataClass() {
        return ByteBuffer.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}

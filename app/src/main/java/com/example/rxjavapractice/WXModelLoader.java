package com.example.rxjavapractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class WXModelLoader implements ModelLoader<String, ByteBuffer> {
    @Nullable
    @Override
    public LoadData<ByteBuffer> buildLoadData(@NonNull String s, int width, int height, @NonNull Options options) {

        Key diskCacheKey = new ObjectKey(s);

        return new LoadData<>(diskCacheKey, /*fetcher=*/ new WXDataFetcher(s));
    }

    @Override
    public boolean handles(@NonNull String s) {
        return s.startsWith("data:");
    }
}

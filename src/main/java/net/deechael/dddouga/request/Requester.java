package net.deechael.dddouga.request;

import com.google.common.base.Preconditions;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

// TODO a python-requester-like Requester
public final class Requester {

    private final String url;
    private final Map<String, String> withHeaders = new HashMap<>();
    private final Map<String, String> addHeaders = new HashMap<>();

    private Requester(@NotNull String url) {
        this.url = url;
    }

    public Future<Response> post() {
        Request.Builder builder = new Request.Builder().url(url);
        for (Map.Entry<String, String> entry : withHeaders.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : addHeaders.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        return null;
    }

    public void withHeader(@NotNull String key, @NotNull String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        this.withHeaders.put(key, value);
    }

    public void addHeader(@NotNull String key, @NotNull String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        this.addHeaders.put(key, value);
    }

    public void withParam(@NotNull String key, @NotNull Object value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);

    }

    @NotNull
    public static Requester request(@NotNull String url) {
        Preconditions.checkNotNull(url);
        return new Requester(url);
    }

}

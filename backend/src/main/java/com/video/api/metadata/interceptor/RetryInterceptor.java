package com.video.api.metadata.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class RetryInterceptor implements Interceptor {
    private final int maxTryCount = 3;
    private final long waitBetweenRetriesMilliseconds = 2000;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;

        for (int tryCount = 1; tryCount <= maxTryCount; tryCount++) {
            try {
                response = chain.proceed(request);

                if ((response.code() >= 500 || response.code() == 429) && tryCount < maxTryCount) {
                    response.close();
                    Thread.sleep(waitBetweenRetriesMilliseconds * tryCount);
                    continue;
                }

                return response;

            } catch (IOException e) {
                if (tryCount >= maxTryCount) {
                    throw e;
                }

                try {
                    Thread.sleep(waitBetweenRetriesMilliseconds * tryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Retry interrupted", ie);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IOException("Retry interrupted", ie);
            }
        }

        if (response != null) return response;
        else throw new IOException("Request failed after " + maxTryCount + " attempts");
    }
}

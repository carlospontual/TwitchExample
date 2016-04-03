package com.carlospontual.twitch.list.data.remote;

/**
 * Created by carlospontual on 02/04/16.
 */
public interface ResponseHandler<T> {
    void onSuccess(T response);
    void onError(int responseCode, String message);
}

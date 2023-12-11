package com.origami.service;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @by: origami
 * @date: {2023/8/28}
 * @info:
 **/
public interface AppUpdateApi {

    @GET
    @Streaming
    Call<ResponseBody> downFile(@Url String url);


}

interface FileC{
    void onOk(File file);
    void onP(float p);
    void onE(Exception e);
}

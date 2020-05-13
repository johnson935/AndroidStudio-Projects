package com.parse.chatappwithfirebase.Fragment;

import com.parse.chatappwithfirebase.Notifications.MyResponse;
import com.parse.chatappwithfirebase.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAACgMknIw:APA91bGoMp7h9wehqhLEP5atIUZLfsxXxeQS90YFMkFYcOdIIsC-MSDPx6w6-Rm9rPXbp71Y7fCzBypEn1V6KTZEdZgMjXNwau5dr1hoxpXmo7BiJ9yy4rsiYSeZU_RlsdvkBhnLDFXa"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifications(@Body Sender body);
}

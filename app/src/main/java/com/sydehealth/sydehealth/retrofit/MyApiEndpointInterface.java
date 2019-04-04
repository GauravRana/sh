package com.sydehealth.sydehealth.retrofit;

import com.sydehealth.sydehealth.model.PowerAIRes;
import com.sydehealth.sydehealth.screen_share.Screen_share_activity;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApiEndpointInterface {


    /**
     * gaurav
     * @param doc
     * @return
     * retrofit is used to simplify the process of API call and to make the process fast IBM POWER AI (Unsecure SSL Certifiate).
     */

    @Multipart
    @POST(Definitions.PowerAI +"{id}")
    Call<PowerAIRes> uploadImage(@Path(value = "id", encoded = true) String id, @Part MultipartBody.Part doc);


    @Multipart
    @POST(Definitions.PowerAIURL)
    Call<PowerAIRes> uploadImageUpload(@Part MultipartBody.Part doc);

    @POST(Definitions.GETPORTFOLIOIMAGES)
    Call<Void> getPortfolioPics();
}


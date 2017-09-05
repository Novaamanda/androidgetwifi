package id.myapplication.com.getwifi.Network;

import id.myapplication.com.getwifi.Model.DataRadius;
import id.myapplication.com.getwifi.Model.DataWifi;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Manda on 11/08/2017.
 */

public interface IAPI {
    @GET(ApiConfig.GET_URL)
    Call<DataWifi> getData(@Path("sort") String order,
                           @Query("api_key") String apiKey);

//    @FormUrlEncoded
//    @POST(ApiConfig.POST_URL)
//    Call<DataWifi> postData(@Field("radius") String deviceID);

    @FormUrlEncoded
    @POST(ApiConfig.POST_URL)
    Call<DataRadius> postDataRadius(@Field("radius1") String deviceRadius1, @Field("radius2") String
            deviceRadius2, @Field("radius3") String deviceRadius3);
}

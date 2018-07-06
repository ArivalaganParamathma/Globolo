package com.idl.globolo.retrofitSupport;

import com.idl.globolo.POJO.CategoryDetailInput;
import com.idl.globolo.POJO.CategoryDetailsOutput;
import com.idl.globolo.POJO.MainAd;
import com.idl.globolo.POJO.UserDetailResult;
import com.idl.globolo.POJO.UserIdInput;
import com.idl.globolo.pojoAllInformation.AllInformation;
import com.idl.globolo.pojoFavourite.CreateFavouriteInput;
import com.idl.globolo.pojoFavourite.FavouriteResponse;
import com.idl.globolo.pojoLanguage.Language;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by arivalagan on 3/8/2018.
 */

public interface ApiInterface {
    @GET("/globol/globolapp/getMainAdd")
    Call<MainAd> getMainAd();

    @POST("/globol/globolapp/UserDetail")
    Call<UserDetailResult> getUserId(@Body UserIdInput userIdInput);

    @GET("/globol/globolapp/getLanguages")
    Call<Language> getLanguages();

    @POST("/globol/globolapp/getAllInformation")
    Call<AllInformation> getAllInformation(@Query("userId") long userId, @Query("firstNode") int firstNode, @Query("lastNode") int lastNode);

    @POST("/globol/globolapp/getCategoryDetailsPage")
    Call<CategoryDetailsOutput> getCategoryDetails(@Body CategoryDetailInput categoryDetailInput);

    @POST("/globol/globolapp/createWordFavourite")
    Call<FavouriteResponse> createWordFavourite(@Body CreateFavouriteInput createFavouriteInput);

    @POST("/globol/globolapp/deleteWordFavoutite")
    Call<FavouriteResponse> deleteWordFavoutite(@Query("userId") long userId, @Query("favId") int favId);

//    @GET("movie/top_rated")
//    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
//
//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}

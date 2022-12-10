package com.example.hmshop.retrofit;

import com.example.hmshop.model.NewProductModel;
import com.example.hmshop.model.ProductModel;
import com.example.hmshop.model.UserModel;

import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Apishop {
    @GET("getProduct.php")
    Observable<ProductModel> getProduct();

    @GET("getnewproduct.php")
    Observable<NewProductModel> getNewProduct();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<NewProductModel> getproductPhone(
      @Field("page") int page,
      @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("user") String user,
            @Field("mobile") String mobile
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass

    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetpass(
            @Field("email") String email


    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<NewProductModel> search(
            @Field("search") String search


    );
}

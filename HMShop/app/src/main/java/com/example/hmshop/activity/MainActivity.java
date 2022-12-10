package com.example.hmshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.hmshop.R;
import com.example.hmshop.adapter.NewProductAdapter;
import com.example.hmshop.adapter.ProductAdapter;
import com.example.hmshop.model.NewProduct;
import com.example.hmshop.model.NewProductModel;
import com.example.hmshop.model.Product;
import com.example.hmshop.retrofit.Apishop;
import com.example.hmshop.retrofit.RetrofitClient;
import com.example.hmshop.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    ProductAdapter productAdapter;
    List<Product> arrayproduct;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Apishop apishop;
    List<NewProduct> arrayNewproduct;
    NewProductAdapter newProductAdapter;
    ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apishop = RetrofitClient.getInstance(Utils.BASE_URL).create(Apishop.class);


        Anhxa();
        ActionBar();

    if(isConnected(this)){
        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
        ActionViewFliper();
        getMProduct();
        getNewProduct();
        getEventClick();
    }else{
        Toast.makeText(getApplicationContext(),"No Internet, Vui Lòng Kết Nối",Toast.LENGTH_LONG).show();
    }
    }

    private void getEventClick() {
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                switch (i){
                                                    case 0:
                                                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(home);
                                                        break;
                                                    case 1:
                                                        Intent laptop = new Intent(getApplicationContext(), PhoneActivity.class);
                                                        laptop.putExtra("loai",2);
                                                        startActivity(laptop);
                                                        break;
                                                    case 2:
                                                        Intent phone = new Intent(getApplicationContext(), PhoneActivity.class);
                                                        phone.putExtra("loai",1);
                                                        startActivity(phone);
                                                        break;



                                                }
                                            }
                                        }

        );
    }

    private void getNewProduct() {
        compositeDisposable.add(apishop.getNewProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    newProductModel -> {
                    if(newProductModel.isSuccess()){
                        arrayNewproduct = newProductModel.getResult();
                        newProductAdapter = new NewProductAdapter(getApplicationContext(),arrayNewproduct);
                        recyclerViewHome.setAdapter(newProductAdapter);

                    }
                    },
                        throwable -> {
                        Toast.makeText(getApplicationContext(),"Không kết nối với sever" + throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getMProduct() {
        compositeDisposable.add(apishop.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                            arrayproduct = productModel.getResult();
                            productAdapter = new ProductAdapter(getApplicationContext(),arrayproduct);
                            listViewHome.setAdapter(productAdapter);
                            }
                        }

                ));
    }


    private void ActionViewFliper(){
        List<String> listads = new ArrayList<>();
        listads.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        listads.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        listads.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < listads.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext())
                    .load(listads.get(i))
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);


        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);


            }
        });
    }

    private void Anhxa(){
        imgsearch = findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toolbarhome);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewHome = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listViewHome = findViewById(R.id.listviewhome);
        drawerLayout = findViewById(R.id.drawerlayout);


        arrayproduct = new ArrayList<>();

        arrayNewproduct = new ArrayList<>();
        if(Utils.arrayGiohang == null) {
            Utils.arrayGiohang = new ArrayList<>();
        }
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
            }
        });
    }
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;

        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
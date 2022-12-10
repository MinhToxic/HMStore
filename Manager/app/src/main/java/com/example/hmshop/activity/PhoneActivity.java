package com.example.hmshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hmshop.R;
import com.example.hmshop.adapter.PhoneAdapter;
import com.example.hmshop.model.NewProduct;
import com.example.hmshop.model.NewProductModel;
import com.example.hmshop.retrofit.Apishop;
import com.example.hmshop.retrofit.RetrofitClient;
import com.example.hmshop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PhoneActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Apishop apishop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai ;
    PhoneAdapter phoneAdapter;
    List<NewProduct> newProducts;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        apishop = RetrofitClient.getInstance(Utils.BASE_URL).create(Apishop.class);
        loai = getIntent().getIntExtra("loai",1);
        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == newProducts.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });

    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                newProducts.add(null);
                phoneAdapter.notifyItemInserted(newProducts.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newProducts.remove(newProducts.size()-1);
                phoneAdapter.notifyItemRemoved(newProducts.size());
                page = page + 1;
                getData(page);
                phoneAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apishop.getproductPhone(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if(newProductModel.isSuccess()){
                                if(phoneAdapter == null){
                                    newProducts = newProductModel.getResult();
                                    phoneAdapter = new PhoneAdapter(getApplicationContext(), newProducts);
                                    recyclerView.setAdapter(phoneAdapter);
                                }else{
                                    int address = newProducts.size()-1;
                                    int soluongadd = newProductModel.getResult().size();
                                    for(int i = 0; i< soluongadd; i++){
                                        newProducts.add(newProductModel.getResult().get(i));
                                    }
                                    phoneAdapter.notifyItemRangeInserted(address,soluongadd);
                                }


                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không Kết Nối Sever",Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void ActionToolBar() {
     setSupportActionBar(toolbar);
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     toolbar.setNavigationOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             finish();
         }
     });

    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarphone);
        recyclerView = findViewById(R.id.recycleviewphone);
       linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        newProducts = new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
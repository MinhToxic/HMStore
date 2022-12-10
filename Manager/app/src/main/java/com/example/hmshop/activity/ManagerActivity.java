package com.example.hmshop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hmshop.R;
import com.example.hmshop.adapter.NewProductAdapter;
import com.example.hmshop.model.EventBus.RepairRemoveEvent;
import com.example.hmshop.model.NewProduct;
import com.example.hmshop.retrofit.Apishop;
import com.example.hmshop.retrofit.RetrofitClient;
import com.example.hmshop.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManagerActivity extends AppCompatActivity {
    ImageView img_add;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Apishop apishop;
    List<NewProduct> arrayNewproduct;
    NewProductAdapter newProductAdapter;
    NewProduct productRemoveRepair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        apishop = RetrofitClient.getInstance(Utils.BASE_URL).create(Apishop.class);
        initView();
        initControl();
        getNewProduct();
    }
    private void initControl() {
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(getApplicationContext(),AddProductActivity.class);
                startActivity(add);

            }
        });
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
                                recyclerView.setAdapter(newProductAdapter);

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối với sever" + throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void initView() {
        img_add = findViewById(R.id.img_add);
        recyclerView = findViewById(R.id.recycleview_pl);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Repair")){
            repairProduct();

        }else if(item.getTitle().equals("Remove")){
            removeProduct();
        }
        return super.onContextItemSelected(item);
    }

    private void removeProduct() {
        compositeDisposable.add(apishop.xoaProduct(productRemoveRepair.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                        if(messageModel.isSuccess()){
                            Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            getNewProduct();
                        }else{
                        Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());
                                    
                        }
                ));
    }

    private void repairProduct() {
        Intent intent = new Intent(getApplicationContext(),AddProductActivity.class);
        intent.putExtra("sua",productRemoveRepair);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventRepairRemove(RepairRemoveEvent event){
        if(event != null){
            productRemoveRepair = event.getNewProduct();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
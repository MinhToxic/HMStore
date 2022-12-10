package com.example.hmshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hmshop.R;
import com.example.hmshop.databinding.ActivityAddProductBinding;
import com.example.hmshop.model.NewProduct;
import com.example.hmshop.retrofit.Apishop;
import com.example.hmshop.retrofit.RetrofitClient;
import com.example.hmshop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddProductActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityAddProductBinding binding;
    Apishop apishop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    NewProduct repairProduct;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        apishop = RetrofitClient.getInstance(Utils.BASE_URL).create(Apishop.class);
        setContentView(binding.getRoot());
        initView();
        initData();

        Intent intent = new Intent();
        repairProduct = (NewProduct) intent.getSerializableExtra("sua");
        if(repairProduct == null){
            // add
            flag = false;
        }else{
            //repair
            flag = true;
            binding.btnaddproduct.setText("Repair Product");
            //show data
            binding.descriptionproductadd.setText(repairProduct.getDescription());
            binding.priceproductadd.setText(repairProduct.getPrice() + "");
            binding.nameproductadd.setText(repairProduct.getNameproduct());
            binding.imageproductadd.setText(repairProduct.getImage());
            binding.spinerLoai.setSelected(repairProduct.getLoai());
        }

    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Please choose a product");
        stringList.add("Phone");
        stringList.add("Laptop");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag == false ){
                    addProduct();
                }else{
                    suaProduct();
                }

            }
        });


    }

    private void suaProduct() {
        String str_ten = binding.nameproductadd.getText().toString().trim();
        String str_price = binding.priceproductadd.getText().toString().trim();
        String str_image = binding.imageproductadd.getText().toString().trim();
        String str_description = binding.descriptionproductadd.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_price) || TextUtils.isEmpty(str_image) || TextUtils.isEmpty(str_description) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter Full Information", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apishop.repairProduct(str_ten,str_price,str_image,str_description,loai,repairProduct.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable ->{
                                Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private void addProduct() {
        String str_ten = binding.nameproductadd.getText().toString().trim();
        String str_price = binding.priceproductadd.getText().toString().trim();
        String str_image = binding.imageproductadd.getText().toString().trim();
        String str_description = binding.descriptionproductadd.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_price) || TextUtils.isEmpty(str_image) || TextUtils.isEmpty(str_description) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Please Enter Full Information", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apishop.insertSp(str_ten,str_price,str_image,str_description,(loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                       messageModel -> {
                            if(messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                       },
                            throwable ->{
                           Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private void initView() {
        spinner = findViewById(R.id.spiner_loai);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
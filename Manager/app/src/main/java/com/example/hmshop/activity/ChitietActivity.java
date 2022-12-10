package com.example.hmshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hmshop.R;
import com.example.hmshop.model.GioHang;
import com.example.hmshop.model.NewProduct;
import com.example.hmshop.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChitietActivity extends AppCompatActivity  {
    TextView nameproduct, price, description;
    Button btnaddtocart;
    ImageView imgimage;
    Spinner spinner;
    Toolbar toolbar;
    NewProduct newProduct;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        initView();
        ActionToolBar();
        initData();
        initControl();

    }

    private void initControl() {
    btnaddtocart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            themGioHang();
        }
    });
    }

    private void themGioHang() {
        if(Utils.arrayGiohang.size()>0){
            boolean flag = false;
        int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.arrayGiohang.size(); i++) {
                if(Utils.arrayGiohang.get(i).getIdsp() == newProduct.getId()){
                     Utils.arrayGiohang.get(i).setSoluong(soluong + Utils.arrayGiohang.get(i).getSoluong());
                    long giacart = Long.parseLong(newProduct.getPrice()) * Utils.arrayGiohang.get(i).getSoluong();

                    Utils.arrayGiohang.get(i).setPricePrice(giacart);
                    flag = true;

                }

            }
            if(flag == false){

                long giacart = Long.parseLong(newProduct.getPrice()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setPricePrice(giacart);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(newProduct.getId());
                gioHang.setNameProduct(newProduct.getNameproduct());
                gioHang.setImgimage(newProduct.getImage());
                Utils.arrayGiohang.add(gioHang);
            }
        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long giacart = Long.parseLong(newProduct.getPrice()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setPricePrice(giacart);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(newProduct.getId());
            gioHang.setNameProduct(newProduct.getNameproduct());
            gioHang.setImgimage(newProduct.getImage());
            Utils.arrayGiohang.add(gioHang);
        }
        badge.setText(String.valueOf(Utils.arrayGiohang.size()));
    }

    private void initData() {
         newProduct = (NewProduct) getIntent().getSerializableExtra ("chitiet");
        nameproduct.setText(newProduct.getNameproduct());
        description.setText(newProduct.getDescription());
        Glide.with(getApplicationContext()).load(newProduct.getImage()).into(imgimage);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        price.setText("Price:" + decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
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

    private void initView() {
    nameproduct = findViewById(R.id.txtname);
    price = findViewById(R.id.txtprice);
    description = findViewById(R.id.txtmota);
    imgimage = findViewById(R.id.imgchitiet);
    btnaddtocart = findViewById(R.id.btnaddtocart);
    spinner = findViewById(R.id.spinner);
    toolbar = findViewById(R.id.toolbarchitiet);
    badge = findViewById(R.id.menu_sl);
    FrameLayout frameLayoutgiohang = findViewById(R.id.framegiohang);
    frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
            startActivity(giohang);
        }
    });
    if(Utils.arrayGiohang != null){
        int totalItem = 0;
        for (int i = 0; i < Utils.arrayGiohang.size(); i++) {
            totalItem = totalItem + Utils.arrayGiohang.get(i).getSoluong();

        }
        badge.setText(String.valueOf(Utils.arrayGiohang.size()));
    }
    }
}
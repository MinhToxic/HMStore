package com.example.hmshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hmshop.R;
import com.example.hmshop.retrofit.Apishop;
import com.example.hmshop.retrofit.RetrofitClient;
import com.example.hmshop.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {
    EditText email,pass,repass,mobile,username;
    AppCompatButton button;
    Apishop apishop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        initControll();
    }

    private void initControll() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_email = email.getText().toString().trim();
        String str_user = username.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"You have not entered gmail",Toast.LENGTH_SHORT).show();

        }else
        if(TextUtils.isEmpty(str_user)){
            Toast.makeText(getApplicationContext(),"You have not entered username",Toast.LENGTH_SHORT).show();

        }else

            if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"You have not entered password",Toast.LENGTH_SHORT).show();

        }else
        if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"You have not entered repass",Toast.LENGTH_SHORT).show();

        }else
        if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"You have not entered numberphone",Toast.LENGTH_SHORT).show();

        }else{
            if(str_pass.equals(str_repass)){
            //post data
                compositeDisposable.add(apishop.dangki(str_email,str_pass,str_user,str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {

                                    if(userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);
                                        Intent intent1 = new Intent(getApplicationContext(),SigninActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                        ));
            }
            else{
                Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initView() {
        apishop = RetrofitClient.getInstance(Utils.BASE_URL).create(Apishop.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btndangki);




    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
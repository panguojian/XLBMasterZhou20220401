package com.example.xlb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.xlb.databinding.XlLoginBinding;

public class Login extends Activity {

    private XlLoginBinding xlbLoginBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        xlbLoginBinding = XlLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(xlbLoginBinding.getRoot());

        SpannableString account=new SpannableString("请输入您的账号");
        AbsoluteSizeSpan ass=new AbsoluteSizeSpan(18,true);
        account.setSpan(ass,0,account.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        xlbLoginBinding.account.setHint(new SpannableString(account));

        SpannableString password=new SpannableString("请输入您的密码");
        password.setSpan(ass,0,password.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        xlbLoginBinding.password.setHint(new SpannableString(password));


        xlbLoginBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=xlbLoginBinding.account.getText().toString();
                String password=xlbLoginBinding.password.getText().toString();
                Intent intent=new Intent(Login.this,DBDataSetting.class);
                startActivity(intent);

                if(account.equals("admin")&&password.equals("admin"))
                {
//                    Intent intent=new Intent(Login.this,DBDataSetting.class);
//                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }

            }
        });

        xlbLoginBinding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(Login.this,MainActivity.class);
                    startActivity(intent);

            }
        });

    }
}
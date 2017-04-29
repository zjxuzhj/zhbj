package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordActivity extends AppCompatActivity {

    @BindView(R.id.textView5)
    TextView mTextView5;
    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser!=null){
            mEditText.setText(currentUser.getAddress());
        }

    }

    private void initView() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().toString().isEmpty()){
                    Toast.makeText(PasswordActivity.this,"当前密码为空，无法保存！",Toast.LENGTH_SHORT).show();
                }else{
                    User newUser = new User();
                    User bmobUser = User.getCurrentUser(User.class);
                    newUser.setPassword(mEditText.getText().toString());
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PasswordActivity.this,"保存密码成功！",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PasswordActivity.this,"保存密码失败，请重新尝试！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}

package com.zhj.zhbj.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhj.zhbj.R;

/**
 * Created by HongJay on 2016/7/8.
 */
public class ContentFragment extends  BaseFragment {
    private  RadioGroup rgGroup;
    @Override
    public View initViews() {
        View view= View.inflate(getActivity(), R.layout.fragment_content,null);

        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);
    }
}

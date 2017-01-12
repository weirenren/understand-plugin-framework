package com.example.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mvp.app.BizServiceContext;
import com.example.mvp.base.BaseActivity;
import com.example.mvp.base.Presenter;
import com.example.mvp.mvp.presenter.MainPresenter;
import com.example.mvp.mvp.view.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weichao13 on 2017/1/10.
 */

public class MainActivity extends BaseActivity implements MainView {

    MainPresenter mMainPresenter;

    TextView mShowTxt;

    Button mNextBtn;

    @Override
    public List<Presenter> registerPresenters() {

        mMainPresenter = new MainPresenter(this);

        ArrayList list = new ArrayList();
        list.add(mMainPresenter);

        return list;
    }


    @Override
    public void initArguments(Bundle arguments) {

        mMainPresenter.getString();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mNextBtn = (Button) findViewById(R.id.btn);
    }

    @Override
    public void initActions() {

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMainPresenter.jumpNextActivity();;
            }
        });

    }

    @Override
    public int getContentLayoutRes() {
        return android.R.layout.activity_list_item;
    }

    @Override
    public List<Presenter> getPresenters() {
        ArrayList list = new ArrayList();
        list.add(mMainPresenter);
        return list;
    }


    @Override
    public void onShowString(String json) {

        mShowTxt.setText(json);
    }

    @Override
    public void jumpNextActivity(long orderId) {

        NextActivity.launchActivity(getContext(),orderId);
    }

}

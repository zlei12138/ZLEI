package com.example.zl.zlei.View.frg.channalfrg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zl.zlei.R;
import com.example.zl.zlei.adapter.MyRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zl on 2017/5/8.
 */

public class WomenFragment extends TopFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.women, container, false);
        super.unbinder = ButterKnife.bind(this, view);
        super.adapter = new MyRecyclerAdapter(null);
        super.recyclerView = this.recyclerView;
        super.channal = "女性";
        super.swipeRefreshLayout = swipeRefreshLayout;
        return view;
    }

}
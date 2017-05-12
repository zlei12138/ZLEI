package com.example.zl.zlei.View.activi;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zl.zlei.Present.SearchActivityPresent;
import com.example.zl.zlei.R;
import com.example.zl.zlei.adapter.HomeAdapter;
import com.example.zl.zlei.adapter.SearchMultyItemBean;
import com.example.zl.zlei.adapter.SearchRecyclerAdapter;
import com.example.zl.zlei.global.Global;
import com.example.zl.zlei.listener.OnSearchDataListener;
import com.example.zl.zlei.others.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity<SearchActivityInterface, SearchActivityPresent> implements SearchActivityInterface {

    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_Edit)
    EditText searchEdit;
    @BindView(R.id.search_text)
    TextView searchText;
    @BindView(R.id.search_progressBar)
    ProgressBar searchProgressBar;
    @BindView(R.id.ll_history)
    RelativeLayout llHistory;
    @BindView(R.id.history_RecyclerView)
    RecyclerView historyRecyclerView;
    @BindView(R.id.errorView)
    RelativeLayout errorView;
    @BindView(R.id.main_tool_icon)
    ImageButton mainToolIcon;
    private SearchRecyclerAdapter searchRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        init();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchText.setTextColor(Color.GRAY);
                searchText.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    searchText.setTextColor(Color.GRAY);
                    searchText.setEnabled(false);
                } else {
                    searchText.setTextColor(Color.WHITE);
                    searchText.setEnabled(true);

                    searchEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                                Log.e("sout","软键盘监听");
                                searchData();
                                //收起键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                // 隐藏软键盘
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                return true;
                            }
                            return false;
                        }
                    });

                    searchText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchData();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mainToolIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void searchData() {
        String searchContent = searchEdit.getText().toString().trim();
        // TODO: 2017/5/12 存储搜索记录

        Toast.makeText(SearchActivity.this, "开始加载", Toast.LENGTH_SHORT).show();
        mPresenter.loadData(searchContent, Global.APPKEY, new OnSearchDataListener() {
            @Override
            public void OnSucceed(ArrayList<SearchMultyItemBean> data) {
               // Log.e("sout", "加载成功--SearchActivity");
                searchRecyclerAdapter.setNewData(data);
                //Log.e("sout", "setData成功--SearchActivity");
                Toast.makeText(SearchActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(Throwable e) {
                Log.e("sout", "加载失败--SearchActivity");
                e.printStackTrace();
                Toast.makeText(SearchActivity.this, "开始失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        searchRecyclerAdapter = new SearchRecyclerAdapter(null);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(searchRecyclerAdapter);
        searchRecyclerView.addItemDecoration(new SpaceItemDecoration(30));
        searchRecyclerAdapter.setContext(this);
        searchRecyclerAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        searchRecyclerAdapter.openLoadMore(10, true);
        searchText.setTextColor(Color.GRAY);
        searchText.setEnabled(false);
        searchRecyclerView.setVisibility(View.INVISIBLE);
        historyRecyclerView.setVisibility(View.VISIBLE);
        llHistory.setVisibility(View.VISIBLE);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        historyRecyclerView.setAdapter(new HomeAdapter(this));
        errorView.setVisibility(View.GONE);
    }

    @Override
    protected SearchActivityPresent createPresenter() {
        return new SearchActivityPresent(this);
    }

    @Override
    public void showProgressBar() {
        searchText.setVisibility(View.GONE);
        searchProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        searchProgressBar.setVisibility(View.GONE);
        searchText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        llHistory.setVisibility(View.INVISIBLE);
        searchRecyclerView.setVisibility(View.INVISIBLE);
        searchProgressBar.setVisibility(View.GONE);
        searchText.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void hideHistoryrecord() {
        historyRecyclerView.setVisibility(View.GONE);
        llHistory.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }
}

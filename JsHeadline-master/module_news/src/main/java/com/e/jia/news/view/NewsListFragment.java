package com.e.jia.news.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e.jia.news.R;
import com.e.jia.news.adapter.NewsListAdapter;
import com.e.jia.news.contract.NewsListContract;
import com.e.jia.news.diffutil.NewsDiffCallback;
import com.e.jia.news.presenter.NewsListPresenter;
import com.jia.base.BaseFragment;
import com.jia.libnet.bean.news.NewsBean;
import com.jia.libui.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表界面
 * Created by jia on 2018/3/31.
 */

public class NewsListFragment extends BaseFragment<NewsListContract.NewsListView, NewsListPresenter>
        implements NewsListContract.NewsListView {

    private SwipeRefreshLayout refresh_layout;
    private RecyclerView recycler_view;
    private TextView tv_no_data;
    private NewsListAdapter adapter;

    private List<NewsBean.DataEntity> list = new ArrayList<>();

    private String tag = "";

    @Override
    protected View initFragmentView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.frag_news_pager, null);
        return view;
    }

    @Override
    protected void initFragmentChildView(View view) {
        refresh_layout = view.findViewById(R.id.refresh_layout);
        recycler_view = view.findViewById(R.id.recycler_view);
        tv_no_data = view.findViewById(R.id.tv_no_data);

        adapter = new NewsListAdapter(getActivity());
        adapter.setData(list);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNewsListByTag(tag);
            }
        });
        refresh_layout.setRefreshing(true);
        mPresenter.getNewsListByTag(tag);
        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setText("加载中...");

        adapter.setListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(NewsBean.DataEntity data) {
                if (TextUtils.isEmpty(data.getMedia_name())) return;

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("url", data.getArticle_url());
                intent.putExtra("title", data.getMedia_name());
                intent.putExtra("shareUrl", data.getShare_url());
                intent.putExtra("desc", data.getTitle());
                intent.putExtra("groupId", data.getGroup_id());
                intent.putExtra("itemId", data.getItem_id());

                if (data.getImage_list() != null && data.getImage_list().size() > 0) {
                    intent.putExtra("imgUrl", data.getImage_list().get(0).getUrl());
                }
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {}

    @Override
    public void onResume() {
        super.onResume();
        String theme= SPUtils.getData(getActivity(),"theme","#3F51B5");
        refresh_layout.setColorSchemeColors(Color.parseColor(theme));
    }

    @Override
    protected NewsListPresenter createPresenter() {
//        return new NewsListPresenter(this.<Long>bindUntilEvent(FragmentEvent.DESTROY));
        return new NewsListPresenter(null);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onRefreshSuccess(NewsBean bean) {
        refresh_layout.setRefreshing(false);

        // 使用DiffUtil进行刷新
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NewsDiffCallback(list, bean.getData()));
        diffResult.dispatchUpdatesTo(adapter);
        list.clear();
        list.addAll(bean.getData());
        adapter.setData(list);

        tv_no_data.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshFail(String info) {
        refresh_layout.setRefreshing(false);

        tv_no_data.setVisibility(View.VISIBLE);
        tv_no_data.setText("暂无数据");
    }
}

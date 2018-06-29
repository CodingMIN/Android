package com.mangosteen.headline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.jia.news.adapter.ImgNewsViewHolder;
import com.e.jia.news.adapter.NewsBaseViewHolder;
import com.e.jia.news.adapter.TextNewsViewHolder;
import com.e.jia.news.adapter.VideoNewsViewHolder;
import com.jia.libnet.bean.news.NewsBean;

import java.util.List;

/**
 * Description: 搜索结果适配器
 * Created by jia on 2018/4/28.
 * 人之所以能，是相信能。
 */

public class SearchResultAdapter extends RecyclerView.Adapter<NewsBaseViewHolder> {

    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int VIDEO = 3;

    private Context context;
    private List<NewsBean.DataEntity> list;

    public SearchResultAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<NewsBean.DataEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public NewsBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsBaseViewHolder viewHolder = null;

        switch (viewType) {
            case TEXT:
                View v1 = LayoutInflater.from(context).inflate(com.e.jia.news.R.layout.item_news_article_text, parent, false);
                viewHolder = new TextNewsViewHolder(v1);
                break;
            case IMG:
                View v2 = LayoutInflater.from(context).inflate(com.e.jia.news.R.layout.item_news_article_img, parent, false);
                viewHolder = new ImgNewsViewHolder(v2);
                break;
            case VIDEO:
                View v3 = LayoutInflater.from(context).inflate(com.e.jia.news.R.layout.item_news_article_video, parent, false);
                viewHolder = new VideoNewsViewHolder(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsBaseViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        int type = TEXT;
        if (list.get(position).isHas_video()) {
            type = VIDEO;
            return type;
        }
        if (list.get(position).getImage_list() != null && list.get(position).getImage_list().size() > 0) {
            type = IMG;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}

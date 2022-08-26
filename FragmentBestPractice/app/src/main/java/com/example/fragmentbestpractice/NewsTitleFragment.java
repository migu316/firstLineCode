package com.example.fragmentbestpractice;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 展示新闻列表的碎片
 */
public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;

    /**
     *  为碎片创建视图
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag, container, false);

        // 创建了RecyclerView，同时使用适配器（通过getNews作为适配器的参数）作为RecyclerView的数据源
        RecyclerView newsTitleRecyclerView = (RecyclerView) view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * 创建50个新闻
     * @return 将新闻数组返回
     */
    private List<News> getNews() {
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            News news = new News();
            news.setTitle("this is news title " + i);
            news.setContent(getRandomLengthContent("this is news content" + i + "."));
            newsList.add(news);
        }

        return newsList;
    }

    /**
     * 拼接新闻内容
     * @param content 需压拼接的内容
     * @return 返回拼接好的内容给getNews();
     */
    private String getRandomLengthContent(String content) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(content);
        }

        return builder.toString();
    }


    /**
     * 当活动创建的时候判断是否为双页模式
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ((getActivity().findViewById(R.id.news_content_layout)) != null) {
            // 可以找到news_content_layout布局时，为双页模式
            isTwoPane = true;
        } else
            // 找不到news_content_layout布局时，为单页模式
            isTwoPane = false;
    }

    /**
     * 内部类 作为RecyclerView的适配器
     */
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView newsTitleText;

            public ViewHolder(View view) {
                super(view);
                newsTitleText = (TextView) view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News> mNewsList) {
            this.mNewsList = mNewsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 为view设置标题子项的布局
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item, parent, false);
            // 将子项装填到title列表中
            final ViewHolder holder = new ViewHolder(view);
            // 设置子项的点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        // 如果是双页模式，则刷新NewsContentFragment中的内容
                        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else {
                        // 如果是单页模式，则直接启动NewsContentActivity
                        NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }




    }


}

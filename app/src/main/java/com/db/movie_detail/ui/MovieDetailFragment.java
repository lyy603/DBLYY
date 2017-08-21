package com.db.movie_detail.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.db.R;
import com.db.movie_detail.adapter.MovieDetailFilmMakerListAdapter;
import com.db.movie_detail.adapter.MovieDetailListAdapter;
import com.db.movie_detail.mvp.model.bean.MovieDetailBean;
import com.db.movie_detail.mvp.model.bean.MovieDetailListBean;
import com.db.movie_detail.mvp.model.bean.PersonDetailBean;
import com.db.movie_detail.mvp.presenter.impl.MovieDetailListPresenterImpl;
import com.db.movie_detail.mvp.view.IMovieDetailListView;
import com.db.widget.ExpandableTextView;
import com.db.widget.fragment.BaseFragment;
import com.db.widget.rating_bar.MaterialRatingBar;
import com.db.widget.recyclerview.animation.CustomAnimation;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends BaseFragment implements IMovieDetailListView, View.OnClickListener {

    private static final String KEY = "movie_detail";

    private TextView tv_movie_title, tv_movie_src, tv_movie_name, tv_movie_date, tv_movie_time,
            tv_ticket_price, tv_rating_count, tv_avg_rating, tv_title;

    private ExpandableTextView summary_content;

    private MaterialRatingBar mrb_avg_rating;

    private ImageView iv_movie;

    private FrameLayout fl_img;

    private LinearLayout ll_toolbar, ll_background;

    private CardView cv_rating;

    private View header;

    private RecyclerView recycler_view, film_maker_recycler_view;

    private MovieDetailListPresenterImpl presenter;

    private MovieDetailFilmMakerListAdapter filmMakerListAdapter;

    private MovieDetailListAdapter listAdapter;

    private Context context;

    private String movieId = "0";

    //标题改变的滑动高度
    private int changeHeight;

    //已经滑动的距离
    private int mDistance = 0;

    private int bg_rgb;

    private String title;

    public static MovieDetailFragment newInstance() {
        return newInstance("");
    }

    public static MovieDetailFragment newInstance(String game_type) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY, game_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        recycler_view = (RecyclerView) view.findViewById(R.id.rl_movie_detail);
        ll_toolbar = (LinearLayout) view.findViewById(R.id.ll_movie_detail_toolbar);
        ll_background = (LinearLayout) view.findViewById(R.id.ll_background);
        header = getActivity().getLayoutInflater()
                .inflate(R.layout.item_movie_detail_header, (ViewGroup) recycler_view.getParent(), false);

        context = view.getContext();

        movieId = getArguments().getString(KEY);

        initView();

        initHeader(header);

        initListener();

        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            presenter.getMovieDetailList(movieId, "0b2bdeda43b5688921839c8ecb20399b", "0", "5");
            presenter.getMovieDetail(movieId, "0b2bdeda43b5688921839c8ecb20399b", "北京");
        }
    }

    private void initView() {

        //初始化MVP
        presenter = new MovieDetailListPresenterImpl(this);

        //设置RecyclerView
        listAdapter = new MovieDetailListAdapter(new ArrayList<>(), this, this);
        listAdapter.openLoadAnimation(new CustomAnimation());
        listAdapter.addHeaderView(header);

        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(listAdapter);


        recycler_view.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                MovieDetailListBean bean = (MovieDetailListBean) adapter.getData().get(position);
//                if (bean.getReviewsBean() != null)
//                    start(MovieReviewDetailFragment.newInstance(bean.getReviewsBean().getAlt()));
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        //其他设置
        tv_title.setText(R.string.movie_detail_title);
    }

    private void initHeader(View header) {
        fl_img = (FrameLayout) header.findViewById(R.id.fl_img);
        iv_movie = (ImageView) header.findViewById(R.id.iv_movie);
        cv_rating = (CardView) header.findViewById(R.id.cv_rating);
        tv_movie_src = (TextView) header.findViewById(R.id.tv_movie_src);
        tv_avg_rating = (TextView) header.findViewById(R.id.tv_avg_rating);
        tv_movie_name = (TextView) header.findViewById(R.id.tv_movie_name);
        tv_movie_date = (TextView) header.findViewById(R.id.tv_movie_date);
        tv_movie_time = (TextView) header.findViewById(R.id.tv_movie_time);
        tv_movie_title = (TextView) header.findViewById(R.id.tv_movie_title);
        tv_rating_count = (TextView) header.findViewById(R.id.tv_rating_count);
        tv_ticket_price = (TextView) header.findViewById(R.id.tv_ticket_price);
        mrb_avg_rating = (MaterialRatingBar) header.findViewById(R.id.mrb_avg_rating);
        film_maker_recycler_view = (RecyclerView) header.findViewById(R.id.rv_filmmaker);
        summary_content = (ExpandableTextView) header.findViewById(R.id.tv_summary_content);

        filmMakerListAdapter = new MovieDetailFilmMakerListAdapter(new ArrayList<>());
        film_maker_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        film_maker_recycler_view.setAdapter(filmMakerListAdapter);
    }

    private void initListener() {
        ViewTreeObserver vto = iv_movie.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                changeHeight = tv_movie_src.getTop();
                iv_movie.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);
                        mDistance += dy;
                        if (mDistance > 0 && mDistance < fl_img.getBottom() - ll_background.getHeight()) {
                            ll_background.setAlpha(1);
                            ll_background.setBackgroundColor(getResources().getColor(R.color.color_gray_3));
                        } else {
                            ll_background.setBackgroundColor(bg_rgb);
                            float scale = (float) mDistance / changeHeight;
                            ll_background.setAlpha(scale);
                        }

                        if (mDistance > changeHeight - ll_background.getHeight())
                            tv_title.setText(title);
                        else
                            tv_title.setText(R.string.movie_detail_title);
                    }
                });
            }
        });
    }

    @Override
    public void showError(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void updateRecyclerView(ArrayList<MovieDetailListBean> list) {
        listAdapter.addData(list);
    }

    @Override
    public void updateMovieDetail(MovieDetailBean item) {

        String genres = item.getYear();

        for (String s : item.getGenres()) {
            genres = genres + "/" + s;
        }

//        Glide.with(context)
//                .load(item.getImages().getLarge())
//                .centerCrop()
//                .crossFade()
//                .into(iv_movie);

        Glide.with(context)
                .load(item.getImages().getLarge())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        // Palette的部分
                        Palette.Builder builder = Palette.from(resource);
                        builder.generate(palette -> {
                            //获取到充满活力的这种色调
                            Palette.Swatch vibrant = palette.getVibrantSwatch();
                            bg_rgb = vibrant != null ? vibrant.getRgb() : 0;
                            //根据调色板Palette获取到图片中的颜色设置到toolbar和tab中背景，标题等，使整个UI界面颜色统一
                            fl_img.setBackgroundColor(bg_rgb);
//                            ll_background.setBackgroundColor(vibrant != null ? vibrant.getRgb() : 0);
                            iv_movie.setImageBitmap(resource);
                        });
                    }
                });

        title = item.getTitle();
        tv_movie_src.setText(genres);
        tv_movie_title.setText(item.getTitle());
        summary_content.setText(item.getSummary());
        tv_avg_rating.setText(item.getRating().getAverage() + "");
        tv_ticket_price.setText(getString(R.string.movie_detail_buy_ticket_price, "23"));
        tv_movie_date.setText(getString(R.string.movie_detail_pubdate, item.getPubdate()));
        tv_movie_time.setText(getString(R.string.movie_detail_durations, item.getDurations().get(0)));
        tv_movie_name.setText(getString(R.string.movie_detail_original_name, item.getOriginal_title()));
        tv_rating_count.setText(getString(R.string.movie_detail_rating_count, item.getRatings_count() + ""));

        mrb_avg_rating.setRating((float) (item.getRating().getAverage() / 2));

        List<PersonDetailBean> list = new ArrayList<>();
        list.addAll(item.getDirectors());
        list.addAll(item.getCasts());
        filmMakerListAdapter.setDirectorSize(item.getDirectors().size());
        filmMakerListAdapter.setNewData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_footer_short_comment:
                start(MovieCommentListFragment.newInstance(1, title, movieId));
                break;
            case R.id.ll_footer_review:
                start(MovieCommentListFragment.newInstance(2, title, movieId));
                break;

        }
    }
}

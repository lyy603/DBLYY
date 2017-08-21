package com.db.movie_detail.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.db.R;
import com.db.movie_detail.mvp.model.bean.MovieDetailListBean;
import com.db.movie_detail.ui.MovieDetailFragment;
import com.db.movie_detail.ui.MovieReviewDetailFragment;
import com.db.widget.rating_bar.MaterialRatingBar;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MovieDetailListAdapter extends BaseMultiItemQuickAdapter<MovieDetailListBean, BaseViewHolder> {

    private MaterialRatingBar ratingBar;

    private int reviews_first_index = -1;

    private int short_comments_first_index = -1;

    private MovieDetailFragment fragment;

    private String title;

    private View.OnClickListener listener;

    public MovieDetailListAdapter(List<MovieDetailListBean> data, MovieDetailFragment fragment, View.OnClickListener listener) {
        super(data);
        this.fragment = fragment;
        this.listener = listener;
        addItemType(MovieDetailListBean.REVIEWS, R.layout.item_move_detail_review);
        addItemType(MovieDetailListBean.SHORTCOMMENTS, R.layout.item_move_detail_short_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, MovieDetailListBean item) {
        switch (helper.getItemViewType()) {
            case MovieDetailListBean.SHORTCOMMENTS:

                if (short_comments_first_index == -1)
                    short_comments_first_index = helper.getAdapterPosition();

                //分类标题的显示控制
                if (short_comments_first_index == helper.getAdapterPosition())
                    helper.getView(R.id.tv_type).setVisibility(View.VISIBLE);
                else
                    helper.getView(R.id.tv_type).setVisibility(View.GONE);

                //底部全部评价数量的显示控制
                if (helper.getAdapterPosition() == getData().size() / 2) {
                    helper.getView(R.id.ll_footer_short_comment).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_num, mContext.getString(R.string.movie_detail_short_comment_num, item.getShortCommentNum() + ""));
                } else
                    helper.getView(R.id.ll_footer_short_comment).setVisibility(View.GONE);

                helper.setText(R.id.tv_content, item.getShortCommentsBean().getContent())
                        .setText(R.id.tv_date, item.getShortCommentsBean().getCreated_at())
                        .setText(R.id.tv_name, item.getShortCommentsBean().getAuthor().getName())
                        .setText(R.id.tv_rating_count, item.getShortCommentsBean().getUseful_count() + "");

                Glide.with(mContext)
                        .load(item.getShortCommentsBean().getAuthor().getAvatar())
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into((ImageView) helper.getView(R.id.iv_avatar));

                ratingBar = helper.getView(R.id.mrb_avg_rating);
                ratingBar.setRating(item.getShortCommentsBean().getRating().getValue());

                helper.getView(R.id.ll_footer_short_comment).setOnClickListener(listener);

                break;
            case MovieDetailListBean.REVIEWS:

                if (reviews_first_index == -1)
                    reviews_first_index = helper.getAdapterPosition();

                //分类标题的显示控制
                if (reviews_first_index == helper.getAdapterPosition())
                    helper.getView(R.id.tv_type).setVisibility(View.VISIBLE);
                else
                    helper.getView(R.id.tv_type).setVisibility(View.GONE);

                //底部全部评价数量的显示控制
                if (helper.getAdapterPosition() == getData().size()) {
                    helper.getView(R.id.ll_footer_review).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_num, mContext.getString(R.string.movie_detail_review_num, item.getReviewNum() + ""));
                } else
                    helper.getView(R.id.ll_footer_review).setVisibility(View.GONE);

                String usefulNum = item.getReviewsBean().getUseful_count() + "/"
                        + (item.getReviewsBean().getUseful_count() + item.getReviewsBean().getUseless_count());

                helper.setText(R.id.tv_title, item.getReviewsBean().getTitle())
                        .setText(R.id.tv_name, item.getReviewsBean().getAuthor().getName())
                        .setText(R.id.tv_content_review, item.getReviewsBean().getSummary())
                        .setText(R.id.tv_useful_num, item.getReviewsBean().getUseful_count() + "")
                        .setText(R.id.tv_useful_num, mContext.getString(R.string.movie_detail_useful_count, usefulNum));

                ratingBar = helper.getView(R.id.mrb_avg_rating);
                ratingBar.setRating(item.getReviewsBean().getRating().getValue());

                helper.getView(R.id.ll_footer_review).setOnClickListener(listener);
                helper.getView(R.id.rl_review).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.start(MovieReviewDetailFragment.newInstance(item.getReviewsBean().getAlt()));
                    }
                });
                break;
            default:
                break;
        }
    }

    protected void setTitle(String title) {
        this.title = title;
    }

}

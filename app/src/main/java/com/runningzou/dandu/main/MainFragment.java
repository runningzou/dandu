package com.runningzou.dandu.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.runningzou.dandu.R;
import com.runningzou.dandu.audio.AudioDetailActivity;
import com.runningzou.dandu.databinding.ItemMainPageBinding;
import com.runningzou.dandu.detail.DetailActivity;
import com.runningzou.dandu.model.entity.Item;
import com.runningzou.dandu.video.VideoDetailActivity;

/**
 * Created by runningzou on 17-10-28.
 */

public class MainFragment extends Fragment {

    private String mTitle;


    public static Fragment newInstance(Item item) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }


    private ItemMainPageBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_main_page, container, false);

        final Item item = getArguments().getParcelable("item");
        final int model = Integer.valueOf(item.getModel());
        if (model == 5) {
            mBinding.pagerContent.setVisibility(View.GONE);
            mBinding.homeAdvertiseIv.setVisibility(View.VISIBLE);
            Glide.with(this.getContext()).load(item.getThumbnail()).into(mBinding.homeAdvertiseIv);
        } else {
            mBinding.pagerContent.setVisibility(View.VISIBLE);
            mBinding.homeAdvertiseIv.setVisibility(View.GONE);
            mTitle = item.getTitle();
            Glide.with(this.getContext()).load(item.getThumbnail()).into(mBinding.imageIv);
            mBinding.commentTv.setText(item.getComment());
            mBinding.likeTv.setText(item.getGood());
            mBinding.readcountTv.setText(item.getView());
            mBinding.titleTv.setText(item.getTitle());
            mBinding.contentTv.setText(item.getExcerpt());
            mBinding.authorTv.setText(item.getAuthor());
            mBinding.typeTv.setText(item.getCategory());
            switch (model) {
                case 2:
                    mBinding.imageType.setVisibility(View.VISIBLE);
                    mBinding.downloadStartWhite.setVisibility(View.GONE);
                    mBinding.imageType.setImageResource(R.drawable.library_video_play_symbol);
                    break;
                case 3:
                    mBinding.imageType.setVisibility(View.VISIBLE);
                    mBinding.downloadStartWhite.setVisibility(View.VISIBLE);
                    mBinding.imageType.setImageResource(R.drawable.library_voice_play_symbol);
                    break;
                default:
                    mBinding.downloadStartWhite.setVisibility(View.GONE);
                    mBinding.imageType.setVisibility(View.GONE);
            }
        }

        mBinding.typeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (model){
                    case 5:
                        Uri uri = Uri.parse(item.getHtml5());
                        intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), AudioDetailActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), VideoDetailActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                        break;
                    default:
                        intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("item",item);
                        startActivity(intent);
                }
            }
        });

        return mBinding.getRoot();
    }


}

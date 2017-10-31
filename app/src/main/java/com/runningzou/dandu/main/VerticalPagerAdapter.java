package com.runningzou.dandu.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.runningzou.dandu.model.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runningzou on 17-10-28.
 */

public class VerticalPagerAdapter extends FragmentStatePagerAdapter {

    private List<Item> mDataList = new ArrayList<>();

    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(mDataList.get(position));
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    public void setDataList(List<Item> data) {
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (mDataList.size() == 0) {
            return "0";
        }
        Item item = mDataList.get(mDataList.size() - 1);
        return item.getId();
    }

    public String getLastItemCreateTime() {
        if (mDataList.size() == 0) {
            return "0";
        }
        Item item = mDataList.get(mDataList.size() - 1);
        return item.getCreate_time();
    }
}

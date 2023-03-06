package com.origami.origami.base.utils;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * @by: origami
 * @date: {2022/1/24}
 * @info:
 * 绑定 {@link TabLayout} 与 {@link ViewPager2}
 * 并为 ViewPager2 设置 Adapter
 * @see MViewPagerAdapter
 **/
public class ViewPagerAndTabBar {

    private final ViewPager2 viewPager2;
    private final TabLayout tabLayout;

    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mViewPagerAdapter;

    private ViewPagerAndTabBar(ViewPager2 viewPager2, TabLayout tabLayout) {
        this.viewPager2 = viewPager2;
        this.tabLayout = tabLayout;
    }

    /**
     * 实例化
     * @param viewPager2 viewPager2
     * @param tabLayout tabLayout
     * @return instance
     */
    public static ViewPagerAndTabBar new_(ViewPager2 viewPager2, TabLayout tabLayout){
        return new ViewPagerAndTabBar(viewPager2, tabLayout);
    }

    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getViewPagerAdapter() {
        return mViewPagerAdapter;
    }

    /**
     * 为 viewPager2 设置 Adapter
     * @param activity activity
     * @param fragments 碎片数组集合
     * @return this
     */
    public ViewPagerAndTabBar setFragmentAdapter(AppCompatActivity activity, List<Fragment> fragments){
        viewPager2.setAdapter(mViewPagerAdapter = new MViewPagerAdapter(activity.getSupportFragmentManager(),
                        activity.getLifecycle(), fragments));
        return this;
    }

    public ViewPagerAndTabBar setFragmentAdapter(AppCompatActivity activity, SparseArray<Fragment> fragments){
        viewPager2.setAdapter(mViewPagerAdapter = new MViewPagerAdapter2(activity.getSupportFragmentManager(),
                activity.getLifecycle(), fragments));
        return this;
    }

    public ViewPagerAndTabBar setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter){
        viewPager2.setAdapter(mViewPagerAdapter = adapter);
        return this;
    }

    /**
     * 设置默认选中 position
     * @param position position
     */
    public ViewPagerAndTabBar initCurrentItem(int position){
        tabLayout.selectTab(tabLayout.getTabAt(position));
        viewPager2.setCurrentItem(position);
        return this;
    }

    public ViewPagerAndTabBar bindVP_TB(){
        return bindVP_TB(null, null);
    }

    public ViewPagerAndTabBar bindVP_TB(TabLayout.OnTabSelectedListener listener, ViewPager2.OnPageChangeCallback changeCallback){
        tabLayout.addOnTabSelectedListener(listener == null? new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        } : listener);
        viewPager2.registerOnPageChangeCallback(changeCallback == null? new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, false, true);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        } : changeCallback);
        return this;
    }


    public static class MViewPagerAdapter extends FragmentStateAdapter{
        private final List<Fragment> fragments;
        public MViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                                 @NonNull Lifecycle lifecycle,
                                 @NonNull List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    public static class MViewPagerAdapter2 extends FragmentStateAdapter{
        private final SparseArray<Fragment> fragments;
        public MViewPagerAdapter2(@NonNull FragmentManager fragmentManager,
                                 @NonNull Lifecycle lifecycle,
                                 @NonNull SparseArray<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.valueAt(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

}

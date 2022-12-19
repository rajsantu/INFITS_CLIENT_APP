package com.example.infits;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReferViewPagerAdapter extends FragmentStateAdapter {
    public ReferViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ReferralActiveUsersFragment();
        }
        return new ReferFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

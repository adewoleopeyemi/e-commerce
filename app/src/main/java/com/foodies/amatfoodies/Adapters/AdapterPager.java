package com.foodies.amatfoodies.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.res.Resources;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.foodies.amatfoodies.ActivitiesAndFragments.RestaurantsFragment;
import com.foodies.amatfoodies.ActivitiesAndFragments.UserAccountFragment;
import com.foodies.amatfoodies.ActivitiesAndFragments.CartFragment;
import com.foodies.amatfoodies.ActivitiesAndFragments.OrdersFragment;

/**
 * Created by qboxus on 10/18/2019.
 */
public class AdapterPager extends FragmentPagerAdapter {

    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public AdapterPager(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch (position) {

            case 0:
                result = new RestaurantsFragment();
                break;
            case 1:
                result = new OrdersFragment();
                break;

            case 2:
                result = new CartFragment();
                break;
            case 3:
                result = new UserAccountFragment();
                break;

        }
        return result;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(final int position) {

        return null;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}


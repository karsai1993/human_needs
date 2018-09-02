package laszlo.karsai.human.needs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.fragment.StatementFragment;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class StatementsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mStatementArray;
    private String mName;

    public StatementsPagerAdapter(FragmentManager fm, Context context, String name) {
        super(fm);
        this.mStatementArray = context.getResources().getStringArray(R.array.statements);
        this.mName = name;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationUtils.STATEMENT_DATA, mStatementArray[position]);
        bundle.putString(ApplicationUtils.NAME, mName);
        bundle.putInt(ApplicationUtils.STATEMENT_POS, position);
        Fragment fragment = new StatementFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new StringBuilder().append(position + 1).append(".").toString();
    }

    @Override
    public int getCount() {
        return ApplicationUtils.COUNT;
    }
}

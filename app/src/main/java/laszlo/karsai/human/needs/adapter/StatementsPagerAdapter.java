package laszlo.karsai.human.needs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class StatementsPagerAdapter extends FragmentStatePagerAdapter {

    private final static int COUNT = 84;

    private String[] mStatementArray;

    public StatementsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}

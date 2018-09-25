package laszlo.karsai.human.needs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.fragment.ResultFragment;
import laszlo.karsai.human.needs.model.Need;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class ResultsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<Need> mNeedList;

    public ResultsPagerAdapter(FragmentManager fm, Context context, List<Need> needList) {
        super(fm);
        this.mContext = context;
        this.mNeedList = needList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
            case 1:
                bundle.putString(ApplicationUtils.NEED, String.valueOf(mNeedList.get(position).getIndex()));
                break;
            case 3:
                bundle.putString(ApplicationUtils.NEED, String.valueOf(mNeedList.get(5).getIndex()));
                break;
            default:
                bundle.putString(
                        ApplicationUtils.NEED,
                        new StringBuilder()
                                .append(mNeedList.get(0).getIndex())
                                .append("-")
                                .append(mNeedList.get(1).getIndex())
                                .toString()
                );
        }
        bundle.putBoolean(ApplicationUtils.IS_LEAST, position == 3);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.result_first_need);
            case 1:
                return mContext.getResources().getString(R.string.result_second_need);
            case 2:
                return mContext.getResources().getString(R.string.result_first_and_second_need);
            default:
                return mContext.getResources().getString(R.string.result_last_need);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}

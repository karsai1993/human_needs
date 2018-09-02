package laszlo.karsai.human.needs.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class StatementFragment extends Fragment {

    private TextView mStatementTextView;
    private RadioGroup mStatementAnswersRadioGroup;
    private String mStatement;
    private String mName;
    private int mPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statement, container, false);
        mStatementTextView = rootView.findViewById(R.id.tv_statement);
        mStatementAnswersRadioGroup = rootView.findViewById(R.id.rg_answer);
        init();
        populateRadioGroup();

        mStatementAnswersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ApplicationUtils.saveValueToPrefs(getContext(), mName, mPosition, i);
                Activity activity = getActivity();
                if (activity != null) {
                    ViewPager viewPager = activity.findViewById(R.id.viewpager_statements);
                    loadNextPage(viewPager);
                    TextView textView = activity.findViewById(R.id.tv_process_status);
                    View containerView = activity.findViewById(R.id.v_container);
                    View view = activity.findViewById(R.id.v_content);
                    ApplicationUtils.updateProgressStatus(textView, containerView, view, getContext(), mName);
                    TextView missingSequenceTextView = activity.findViewById(R.id.tv_missing);
                    if (mPosition == ApplicationUtils.COUNT - 1
                            || missingSequenceTextView.getVisibility() == View.VISIBLE) {
                        ApplicationUtils.showNotAnsweredStatementsIfAny(
                                activity,
                                missingSequenceTextView,
                                mName
                        );
                    }
                    if (mPosition == ApplicationUtils.COUNT - 1) {
                        ApplicationUtils.saveUserWentThroughValueToPrefs(getContext(), mName);
                    }
                }
                /*new UpdateAsyncTask(
                        new WeakReference<Context>(getContext()),
                        mName,
                        mPosition,
                        i,
                        new WeakReference<Activity>(activity)
                ).execute();*/
            }
        });
        return rootView;
    }

    static class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> mContext;
        private String mName;
        private int mPosition;
        private int mCheckedId;
        private WeakReference<Activity> mActivity;

        public UpdateAsyncTask(
                WeakReference<Context> context,
                String name,
                int pos,
                int checkedId,
                WeakReference<Activity> activity) {
            this.mContext = context;
            this.mName = name;
            this.mPosition = pos;
            this.mCheckedId = checkedId;
            this.mActivity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApplicationUtils.saveValueToPrefs(mContext.get(), mName, mPosition, mCheckedId);
            Activity activity = mActivity.get();
            if (activity != null) {
                TextView textView = activity.findViewById(R.id.tv_process_status);
                View containerView = activity.findViewById(R.id.v_container);
                View view = activity.findViewById(R.id.v_content);
                ApplicationUtils.updateProgressStatus(textView, containerView, view, mContext.get(), mName);
                TextView missingSequenceTextView = activity.findViewById(R.id.tv_missing);
                if (mPosition == ApplicationUtils.COUNT - 1
                        || missingSequenceTextView.getVisibility() == View.VISIBLE) {
                    ApplicationUtils.showNotAnsweredStatementsIfAny(
                            activity,
                            missingSequenceTextView,
                            mName
                    );
                }
                if (mPosition == ApplicationUtils.COUNT - 1) {
                    ApplicationUtils.saveUserWentThroughValueToPrefs(mContext.get(), mName);
                }
            }
            return null;
        }
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            ApplicationUtils.showError(getContext());
        } else {
            mStatement = bundle.getString(ApplicationUtils.STATEMENT_DATA);
            mName = bundle.getString(ApplicationUtils.NAME);
            mPosition = bundle.getInt(ApplicationUtils.STATEMENT_POS, -1);
            if (mStatement == null || mName == null || mPosition == -1) {
                ApplicationUtils.showError(getContext());
            } else {
                mStatementTextView.setText(mStatement);
            }
        }
    }

    private void populateRadioGroup() {
        int storedIndex = ApplicationUtils.getValueFromPrefs(
                getContext(),
                mName,
                mPosition
        );
        if (storedIndex != -1) {
            mStatementAnswersRadioGroup.check(storedIndex);
        }
    }

    private void loadNextPage(ViewPager viewPager) {
        int currentPos = viewPager.getCurrentItem();
        if (currentPos != ApplicationUtils.COUNT - 1) {
            viewPager.setCurrentItem(currentPos + 1);
        }
    }
}

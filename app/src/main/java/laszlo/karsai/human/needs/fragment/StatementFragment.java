package laszlo.karsai.human.needs.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
                View rb = radioGroup.findViewById(i);
                ApplicationUtils.saveValueToPrefs(
                        getContext(),
                        mName,
                        mPosition,
                        radioGroup.indexOfChild(rb));
                Activity activity = getActivity();
                if (activity != null) {
                    ViewPager viewPager = activity.findViewById(R.id.viewpager_statements);
                    loadNextPage(viewPager);
                    TextView textView = activity.findViewById(R.id.tv_process_status);
                    View containerView = activity.findViewById(R.id.v_container);
                    View view = activity.findViewById(R.id.v_content);
                    Button btnShowResult = activity.findViewById(R.id.btn_show_result);
                    ApplicationUtils.updateProgressStatus(
                            textView,
                            containerView,
                            view,
                            btnShowResult,
                            getContext(),
                            mName
                    );
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
            }
        });
        return rootView;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            ApplicationUtils.exit(getContext());
        } else {
            mStatement = bundle.getString(ApplicationUtils.STATEMENT_DATA);
            mName = bundle.getString(ApplicationUtils.NAME);
            mPosition = bundle.getInt(ApplicationUtils.STATEMENT_POS, -1);
            if (mStatement == null || mName == null || mPosition == -1) {
                ApplicationUtils.exit(getContext());
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
            RadioButton rb = (RadioButton) mStatementAnswersRadioGroup.getChildAt(storedIndex);
            rb.setChecked(true);
        }
    }

    private void loadNextPage(ViewPager viewPager) {
        int currentPos = viewPager.getCurrentItem();
        if (currentPos != ApplicationUtils.COUNT - 1) {
            viewPager.setCurrentItem(currentPos + 1);
        }
    }
}

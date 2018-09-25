package laszlo.karsai.human.needs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.adapter.StatementsPagerAdapter;
import laszlo.karsai.human.needs.model.Need;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class StatementsActivity extends AppCompatActivity {

    @BindView(R.id.tab_statements)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_statements)
    ViewPager mViewPager;
    @BindView(R.id.tv_process_status)
    TextView mProcessStatusTextView;
    @BindView(R.id.v_container)
    View mContainerView;
    @BindView(R.id.v_content)
    View mContentView;
    @BindView(R.id.btn_show_result)
    Button mShowResultBtn;
    @BindView(R.id.pb_statements)
    ProgressBar mProgressBar;

    private StatementsPagerAdapter mStatementsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        ButterKnife.bind(this);

        Intent receivedData = getIntent();
        if (receivedData != null) {
            final String name = receivedData.getStringExtra(Intent.EXTRA_TEXT);
            if (name == null) {
                ApplicationUtils.exit(this);
            } else {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar == null) {
                    ApplicationUtils.exit(this);
                } else {
                    actionBar.setTitle(
                            new StringBuilder()
                                    .append(name)
                                    .append(getResources().getString(R.string.statements_header_part))
                                    .toString()
                    );
                    ApplicationUtils.updateProgressStatus(
                            mProcessStatusTextView,
                            mContainerView,
                            mContentView,
                            mShowResultBtn,
                            this,
                            name);
                    boolean hasUserWentThrough = ApplicationUtils
                            .getUserWentThroughValueFromPrefs(this, name);
                    if (hasUserWentThrough) {
                        ApplicationUtils.showNotAnsweredStatementsIfAny(
                                this,
                                (TextView) findViewById(R.id.tv_missing),
                                name
                        );
                    }
                    mStatementsPagerAdapter = new StatementsPagerAdapter(
                            getSupportFragmentManager(),
                            this,
                            name
                    );
                    mViewPager.setAdapter(mStatementsPagerAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                    mShowResultBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            Intent resultActivityIntent = new Intent(
                                    StatementsActivity.this,
                                    ResultsActivity.class
                            );
                            List<Need> needScoreList = getNeedInformation(
                                    StatementsActivity.this,
                                    name
                            );
                            resultActivityIntent.putExtra(ApplicationUtils.NAME, name);
                            resultActivityIntent.putParcelableArrayListExtra(
                                    ApplicationUtils.SCORES,
                                    (ArrayList<Need>) getSortedNeedList(needScoreList));
                            mProgressBar.setVisibility(View.GONE);
                            startActivity(resultActivityIntent);
                        }
                    });
                }
            }
        } else {
            ApplicationUtils.exit(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statements_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_about) {
            startActivity(new Intent(StatementsActivity.this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Need> getSortedNeedList(List<Need> needList) {
        List<Need> needListForHighImportance = new ArrayList<>(needList);
        Collections.sort(needListForHighImportance, new Comparator<Need>() {
            @Override
            public int compare(Need needOne, Need needTwo) {
                int scoreCompareResult = needTwo.getScore().compareTo(needOne.getScore());
                if (scoreCompareResult == 0) {
                    int absolutelyAgreeCompareResult = needTwo.getAbsolutelyAgreeNum()
                            .compareTo(needOne.getAbsolutelyAgreeNum());
                    if (absolutelyAgreeCompareResult == 0) {
                        int agreeCompareResult = needTwo.getAgreeNum()
                                .compareTo(needOne.getAgreeNum());
                        if (agreeCompareResult == 0) {
                            int partlyCompareResult = needTwo.getPartlyAgreeNum()
                                    .compareTo(needOne.getPartlyAgreeNum());
                            if (partlyCompareResult == 0) {
                                int absolutelyDisagreeCompareResult = needOne
                                        .getAbsolutelyDisagreeNum()
                                        .compareTo(needTwo.getAbsolutelyDisagreeNum());
                                if (absolutelyDisagreeCompareResult == 0) {
                                    return needOne.getDisagreeNum()
                                            .compareTo(needTwo.getDisagreeNum());
                                } else {
                                    return absolutelyDisagreeCompareResult;
                                }
                            } else {
                                return partlyCompareResult;
                            }
                        } else {
                            return agreeCompareResult;
                        }
                    } else {
                        return absolutelyAgreeCompareResult;
                    }
                } else {
                    return scoreCompareResult;
                }
            }
        });
        return needListForHighImportance;
    }

    private List<Need> getNeedInformation(Context context, String name) {
        List<Need> needList = new ArrayList<>();
        Need certaintyResultInformation = addNeed(
                new int[]{3, 10, 13, 19, 24, 28, 33, 37, 45, 53, 63, 67, 73, 80},
                context,
                name,
                0
        );
        Need uncertaintyResultInformation = addNeed(
                new int[]{4, 5, 12, 16, 27, 31, 39, 47, 62, 68, 74, 78, 81, 83},
                context,
                name,
                1
        );
        Need significanceResultInformation = addNeed(
                new int[]{1, 9, 15, 23, 26, 34, 40, 48, 52, 56, 61, 69, 75, 82},
                context,
                name,
                2
        );
        Need loveResultInformation = addNeed(
                new int[]{2, 8, 17, 20, 25, 32, 38, 43, 49, 51, 60, 66, 71, 76},
                context,
                name,
                3
        );
        Need growthResultInformation = addNeed(
                new int[]{6, 18, 21, 29, 35, 41, 46, 50, 54, 57, 59, 65, 70, 79},
                context,
                name,
                4
        );
        Need contributionResultInformation = addNeed(
                new int[]{7, 11, 14, 22, 30, 36, 42, 44, 55, 58, 64, 72, 77, 82},
                context,
                name,
                5
        );
        needList.add(certaintyResultInformation);
        needList.add(uncertaintyResultInformation);
        needList.add(significanceResultInformation);
        needList.add(loveResultInformation);
        needList.add(growthResultInformation);
        needList.add(contributionResultInformation);
        return needList;
    }

    private Need addNeed(int[] arr, Context context, String name, int index) {
        double scoreNum = 0.0;
        double[] scoreArray = new double[]{1.0, 0.75, 0.5, 0.25, 0.0};
        int counterAbsolutelyAgree = 0;
        int counterAgree = 0;
        int counterPartlyAgree = 0;
        int counterDisagree = 0;
        int counterAbsolutelyDisagree = 0;
        for (int num : arr) {
            int storedIndexValue = ApplicationUtils.getValueFromPrefs(context, name, num - 1);
            scoreNum += scoreArray[storedIndexValue];
            switch (storedIndexValue) {
                case 0: counterAbsolutelyAgree ++; break;
                case 1: counterAgree ++; break;
                case 2: counterPartlyAgree ++; break;
                case 3: counterDisagree ++; break;
                default: counterAbsolutelyDisagree ++; break;
            }
        }
        String needName;
        switch (index) {
            case 0: needName = context.getResources().getString(R.string.need_certainty); break;
            case 1: needName = context.getResources().getString(R.string.need_uncertainty_variety); break;
            case 2: needName = context.getResources().getString(R.string.need_significance); break;
            case 3: needName = context.getResources().getString(R.string.need_love_connection); break;
            case 4: needName = context.getResources().getString(R.string.need_growth); break;
            default: needName = context.getResources().getString(R.string.need_contribution); break;
        }
        return new Need(
                needName,
                index,
                scoreNum,
                counterAbsolutelyAgree,
                counterAgree,
                counterPartlyAgree,
                counterDisagree,
                counterAbsolutelyDisagree
        );
    }
}
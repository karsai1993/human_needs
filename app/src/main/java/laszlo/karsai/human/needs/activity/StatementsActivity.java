package laszlo.karsai.human.needs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.adapter.StatementsPagerAdapter;
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

    private StatementsPagerAdapter mStatementsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements);
        ButterKnife.bind(this);

        Intent receivedData = getIntent();
        if (receivedData != null) {
            String name = receivedData.getStringExtra(Intent.EXTRA_TEXT);
            if (name == null) {
                ApplicationUtils.showError(this);
            } else {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar == null) {
                    ApplicationUtils.showError(this);
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
                            this,
                            name);
                    boolean hasUserWentThrough = ApplicationUtils.getUserWentThroughValueFromPrefs(this, name);
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
                }
            }
        } else {
            ApplicationUtils.showError(this);
        }
    }
}

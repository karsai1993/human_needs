package laszlo.karsai.human.needs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.Toast;

import butterknife.BindView;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.adapter.StatementsPagerAdapter;

public class StatementsActivity extends AppCompatActivity {

    @BindView(R.id.tab_statements)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager_statements)
    ViewPager mViewPager;

    private StatementsPagerAdapter mStatementsPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedData = getIntent();
        if (receivedData != null) {
            String name = receivedData.getStringExtra(Intent.EXTRA_TEXT);
            if (name == null) {
                Toast.makeText(
                        this,
                        getResources().getString(R.string.problem),
                        Toast.LENGTH_LONG
                ).show();
            } else {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar == null) {
                    Toast.makeText(
                            this,
                            getResources().getString(R.string.problem),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    actionBar.setTitle(
                            new StringBuilder()
                                    .append(getResources().getString(R.string.statements_header_part))
                                    .append(name)
                                    .append("!")
                                    .toString()
                    );
                    mStatementsPagerAdapter = new StatementsPagerAdapter(
                            getSupportFragmentManager()
                    );
                    mViewPager.setAdapter(mStatementsPagerAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            }
        } else {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.problem),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}

package laszlo.karsai.human.needs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.adapter.ResultsPagerAdapter;
import laszlo.karsai.human.needs.model.Need;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class ResultsActivity extends AppCompatActivity {

    @BindView(R.id.tab_results)
    TabLayout mResultsTabLayout;
    @BindView(R.id.viewpager_results)
    ViewPager mResultsViewPager;

    private String mName;
    private List<Need> mNeedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);

        Intent receivedData = getIntent();
        if (receivedData != null) {
            mName = receivedData.getStringExtra(ApplicationUtils.NAME);
            mNeedList = receivedData.getParcelableArrayListExtra(ApplicationUtils.SCORES);
            if (mName != null && mNeedList != null) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar == null) {
                    ApplicationUtils.exit(this);
                } else {
                    actionBar.setTitle(
                            new StringBuilder()
                            .append(mName)
                            .append(getResources().getString(R.string.results))
                            .toString()
                    );
                }
                ResultsPagerAdapter resultsPagerAdapter = new ResultsPagerAdapter(
                        getSupportFragmentManager(),
                        this,
                        mNeedList
                );
                mResultsViewPager.setAdapter(resultsPagerAdapter);
                mResultsTabLayout.setupWithViewPager(mResultsViewPager);
            } else {
                ApplicationUtils.exit(this);
            }
        } else {
            ApplicationUtils.exit(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.results_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            share();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(getResources().getString(R.string.share_welcome), mName));
        sb.append(getResources().getString(R.string.share_brief_title));
        Need mostImportantNeed = mNeedList.get(0);
        Need secondaryImportantNeed = mNeedList.get(1);
        Need leastImportantNeed = mNeedList.get(5);
        sb.append(
                String.format(
                        getResources().getString(R.string.share_brief),
                        mostImportantNeed.getName(),
                        secondaryImportantNeed.getName(),
                        leastImportantNeed.getName())
        );
        sb.append(getResources().getString(R.string.share_details));
        for (Need need : mNeedList) {
            sb.append(
                    String.format(
                            getResources().getString(R.string.share_detail_item),
                            need.getName(),
                            String.valueOf(need.getScore()),
                            String.valueOf(need.getAbsolutelyAgreeNum()),
                            String.valueOf(need.getAgreeNum()),
                            String.valueOf(need.getPartlyAgreeNum()),
                            String.valueOf(need.getDisagreeNum()),
                            String.valueOf(need.getAbsolutelyDisagreeNum())
                    )
            );
        }
        sb.append(getResources().getString(R.string.share_from));
        Intent sendReportIntent = new Intent();
        sendReportIntent.setAction(Intent.ACTION_SEND);
        sendReportIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendReportIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        sendReportIntent.setType("text/plain");
        if (sendReportIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sendReportIntent, getResources().getString(R.string.share_title)));
        }
    }
}

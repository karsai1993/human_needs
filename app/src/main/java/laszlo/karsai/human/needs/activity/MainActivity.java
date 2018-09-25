package laszlo.karsai.human.needs.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.btn_start)
    Button mStartBtn;
    @BindView(R.id.et_name)
    TextInputEditText mNameEditText;
    @BindView(R.id.tv_main_terms_policy)
    TextView mTermsAndPolicyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mStartBtn = findViewById(R.id.btn_show_result);

        final String storedName = ApplicationUtils.getNameFromPrefs(this);
        if (storedName != null) {
            mNameEditText.setText(storedName);
        }

        ApplicationUtils.addHyperlinkedText(
                mTermsAndPolicyTextView,
                getResources().getString(R.string.agree_terms_privacy)
        );

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(
                            MainActivity.this,
                            getResources().getString(R.string.main_name_empty),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    if (!name.equals(storedName)) {
                        ApplicationUtils.saveNameToPrefs(MainActivity.this, name);
                    }
                    Intent intent = new Intent(MainActivity.this, StatementsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, name);
                    startActivity(intent);
                }
            }
        });
    }
}

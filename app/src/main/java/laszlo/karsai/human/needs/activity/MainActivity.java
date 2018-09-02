package laszlo.karsai.human.needs.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
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

        final String storedName = ApplicationUtils.getNameFromPrefs(this);
        if (storedName  != null) {
            mNameEditText.setText(storedName);
        }

        Spanned termsAndPolicy;
        String text = getResources().getString(R.string.agree_terms_privacy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            termsAndPolicy = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            termsAndPolicy = Html.fromHtml(text);
        }
        mTermsAndPolicyTextView.setText(termsAndPolicy);
        mTermsAndPolicyTextView.setMovementMethod(LinkMovementMethod.getInstance());

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

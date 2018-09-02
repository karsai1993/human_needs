package laszlo.karsai.human.needs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import laszlo.karsai.human.needs.R;

public class ApplicationUtils {

    public static final String STATEMENT_DATA = "statement_data";
    public static final String STATEMENT_POS = "statement_pos";
    public static final String NAME = "name";
    private static final String USER_WENT_THROUGH = "user_went_through";
    private static final String USER_DONE = "user_done";
    public final static int COUNT = 84;

    public static void showError(Context context) {
        Toast.makeText(
                context,
                context.getResources().getString(R.string.problem),
                Toast.LENGTH_LONG
        ).show();
    }

    public static void saveValueToPrefs(Context context, String name, int position, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(createPrefKey(context, name, position), value);
        editor.apply();
    }

    public static int getValueFromPrefs(Context context, String name, int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(createPrefKey(context, name, position), -1);
    }

    public static void saveNameToPrefs(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(createNamePrefKey(context), name);
        editor.apply();
    }

    public static String getNameFromPrefs(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(createNamePrefKey(context), null);
    }

    private static String createPrefKey(Context context, String name, int position) {
        return new StringBuilder()
                .append(context.getResources().getString(R.string.app_name))
                .append("_")
                .append(name)
                .append("_")
                .append(position)
                .toString();
    }

    private static String createNamePrefKey(Context context) {
        return new StringBuilder()
                .append(context.getResources().getString(R.string.app_name))
                .append("_")
                .append(NAME)
                .toString();
    }

    public static void saveUserWentThroughValueToPrefs(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(createUserWentThroughPrefKey(context, name), true);
        editor.apply();
    }

    public static boolean getUserWentThroughValueFromPrefs(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(createUserWentThroughPrefKey(context, name), false);
    }

    private static String createUserWentThroughPrefKey(Context context, String name) {
        return new StringBuilder()
                .append(context.getResources().getString(R.string.app_name))
                .append("_")
                .append(USER_WENT_THROUGH)
                .append("_")
                .append(name)
                .toString();
    }

    public static void saveUserDoneValueToPrefs(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(createUserDonePrefKey(context, name), true);
        editor.apply();
    }

    public static boolean getUserDoneValueFromPrefs(Context context, String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(createUserDonePrefKey(context, name), false);
    }

    private static String createUserDonePrefKey(Context context, String name) {
        return new StringBuilder()
                .append(context.getResources().getString(R.string.app_name))
                .append("_")
                .append(USER_DONE)
                .append("_")
                .append(name)
                .toString();
    }

    public static void updateProgressStatus(
            TextView textView,
            final View containerView,
            final View view,
            Context context,
            String name) {
        int setDataCounter = 0;
        for (int i = 0; i < ApplicationUtils.COUNT; i++) {
            int storedValue = ApplicationUtils.getValueFromPrefs(context, name, i);
            if (storedValue != -1) {
                setDataCounter ++;
            }
        }
        final double ratio = (double) setDataCounter / ApplicationUtils.COUNT;
        double percentageValueRounded = round(ratio * 100);
        containerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = containerView.getWidth();
                int height = containerView.getHeight();
                double widthPercentage = (double) width * ratio;
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        (int) widthPercentage,
                        height
                );
                view.setLayoutParams(layoutParams);
            }
        });
        textView.setText(
                new StringBuilder()
                        .append(percentageValueRounded)
                        .append("% (")
                        .append(setDataCounter)
                        .append(context.getResources().getString(R.string.of))
                        .append(COUNT)
                        .append(")")
        );
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void openWebPage (Context context, String url) {
        Uri webPage = Uri.parse(url);
        Intent openWebPageIntent = new Intent(Intent.ACTION_VIEW, webPage);
        if (openWebPageIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(openWebPageIntent);
        }
    }

    public static void showNotAnsweredStatementsIfAny(
            Activity activity,
            TextView missingSequenceTextView,
            String name) {
        List<Integer> missedStatementSequenceList = getMissingStatementSequences(
                activity.getApplicationContext(),
                name
        );
        if (!missedStatementSequenceList.isEmpty()) {
            StringBuilder textToApplyBuilder = new StringBuilder();
            textToApplyBuilder.append(
                    activity.getResources().getString(R.string.statements_missing)
            );
            int size = missedStatementSequenceList.size();
            for (int j = 0; j < size; j ++) {
                if (j != 0) {
                    textToApplyBuilder.append(", ");
                }
                textToApplyBuilder.append(missedStatementSequenceList.get(j));
                if (j == size - 1) {
                    textToApplyBuilder.append(".");
                }
            }
            missingSequenceTextView.setText(textToApplyBuilder.toString());
            missingSequenceTextView.setVisibility(View.VISIBLE);
        } else {
            missingSequenceTextView.setVisibility(View.GONE);
        }
    }

    private static List<Integer> getMissingStatementSequences(Context context, String name) {
        List<Integer> missingList = new ArrayList<>();
        for (int i = 0; i < ApplicationUtils.COUNT; i++) {
            int storedValue = ApplicationUtils.getValueFromPrefs(context, name, i);
            if (storedValue == -1) {
                missingList.add(i + 1);
            }
        }
        return missingList;
    }
}

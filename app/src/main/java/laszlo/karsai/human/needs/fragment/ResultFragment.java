package laszlo.karsai.human.needs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import laszlo.karsai.human.needs.R;
import laszlo.karsai.human.needs.adapter.NeedAdapter;
import laszlo.karsai.human.needs.utils.ApplicationUtils;

public class ResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        TextView needNameTextView = rootView.findViewById(R.id.tv_need_name);
        RecyclerView needRecyclerView = rootView.findViewById(R.id.rv_need);
        needRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        needRecyclerView.setHasFixedSize(true);
        Bundle receivedData = getArguments();
        if (receivedData != null) {
            String need = receivedData.getString(ApplicationUtils.NEED);
            boolean isLeast = receivedData.getBoolean(ApplicationUtils.IS_LEAST);
            if (need != null) {
                boolean isFirstAndSecond = need.contains("-");
                String[] headers;
                String[] values;
                Context context = getContext();
                if (context != null) {
                    if (isFirstAndSecond) {
                        needNameTextView.setText(getCombinationName(context, need));
                        headers = context.getResources().getStringArray(R.array.combination_need_header);
                        values = getCombinationValues(context, need);
                    } else {
                        needNameTextView.setText(getName(context, need));
                        headers = getHeaders(context, need);
                        values = getValues(context, need);
                    }
                    if (isLeast) {
                        TextView leastAlertTextView = rootView.findViewById(R.id.tv_need_least_desc);
                        leastAlertTextView.setVisibility(View.VISIBLE);
                        leastAlertTextView.setText(context.getResources().getString(R.string.least_imp_alert));
                    }
                    NeedAdapter needAdapter = new NeedAdapter(context, headers, values);
                    needRecyclerView.setAdapter(needAdapter);
                } else {
                    ApplicationUtils.exit(getContext());
                }
            } else {
                ApplicationUtils.exit(getContext());
            }
        } else {
            ApplicationUtils.exit(getContext());
        }
        return rootView;
    }

    private String getCombinationName(Context context, String need) {
        String[] needParts = need.split("-");
        String firstNeed = needParts[0];
        String secondNeed = needParts[1];
        return getName(context, firstNeed) + " & " + getName(context, secondNeed);
    }

    private String getName(Context context, String index) {
        switch (index) {
            case "0": return context.getResources().getString(R.string.need_certainty).toUpperCase();
            case "1": return context.getResources().getString(R.string.need_uncertainty_variety).toUpperCase();
            case "2": return context.getResources().getString(R.string.need_significance).toUpperCase();
            case "3": return context.getResources().getString(R.string.need_love_connection).toUpperCase();
            case "4": return context.getResources().getString(R.string.need_growth).toUpperCase();
            default: return context.getResources().getString(R.string.need_contribution).toUpperCase();
        }
    }

    private String[] getHeaders(Context context, String need) {
        switch (need) {
            case "0": return context.getResources().getStringArray(R.array.certainty_headers);
            case "1": return context.getResources().getStringArray(R.array.uncertainty_headers);
            case "2": return context.getResources().getStringArray(R.array.significance_headers);
            case "3": return context.getResources().getStringArray(R.array.love_headers);
            case "4": return context.getResources().getStringArray(R.array.growth_headers);
            default: return context.getResources().getStringArray(R.array.contribution_headers);
        }
    }

    private String[] getValues(Context context, String need) {
        switch (need) {
            case "0": return context.getResources().getStringArray(R.array.certainty_values);
            case "1": return context.getResources().getStringArray(R.array.uncertainty_values);
            case "2": return context.getResources().getStringArray(R.array.significance_values);
            case "3": return context.getResources().getStringArray(R.array.love_values);
            case "4": return context.getResources().getStringArray(R.array.growth_values);
            default: return context.getResources().getStringArray(R.array.contribution_values);
        }
    }

    private String[] getCombinationValues(Context context, String need) {
        String[] needParts = need.split("-");
        int firstElement = Integer.parseInt(needParts[0]);
        int secondElement = Integer.parseInt(needParts[1]);
        String tempNeed = need;
        if (firstElement > secondElement) {
            tempNeed = secondElement + "-" + firstElement;
        }
        switch (tempNeed){
            case "0-1": return context.getResources().getStringArray(R.array.certainty_uncertainty_need_value);
            case "0-2": return context.getResources().getStringArray(R.array.certainty_significance_need_value);
            case "0-3": return context.getResources().getStringArray(R.array.certainty_love_need_value);
            case "0-4": return context.getResources().getStringArray(R.array.certainty_growth_need_value);
            case "0-5": return context.getResources().getStringArray(R.array.certainty_contribution_need_value);
            case "1-2": return context.getResources().getStringArray(R.array.uncertainty_significance_need_value);
            case "1-3": return context.getResources().getStringArray(R.array.uncertainty_love_need_value);
            case "1-4": return context.getResources().getStringArray(R.array.uncertainty_growth_need_value);
            case "1-5": return context.getResources().getStringArray(R.array.uncertainty_contribution_need_value);
            case "2-3": return context.getResources().getStringArray(R.array.significance_love_need_value);
            case "2-4": return context.getResources().getStringArray(R.array.significance_growth_need_value);
            case "2-5": return context.getResources().getStringArray(R.array.significance_contribution_need_value);
            case "3-4": return context.getResources().getStringArray(R.array.love_growth_need_value);
            case "3-5": return context.getResources().getStringArray(R.array.love_contribution_need_value);
            default: return context.getResources().getStringArray(R.array.growth_contribution_need_value);
        }
    }
}

package com.stiletto.tr.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.view.StyleCallback;
import com.stiletto.tr.widget.ClickableTextView;

/**
 * Created by yana on 10.01.17.
 */

public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ClickableTextView textView = (ClickableTextView) view.findViewById(R.id.test_view);
        TextView t = (TextView) view.findViewById(R.id.text_view);

        String text =
                "In the 2000s, there was a trend of print and e-book sales moving to the Internet, where readers buy traditional paper books and e-books on websites using e-commerce systems.";

        textView.setText(text);
        t.setText(text);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        t.setMovementMethod(LinkMovementMethod.getInstance());

//        textView.setCustomSelectionActionModeCallback(new StyleCallback(textView));
//        t.setCustomSelectionActionModeCallback(new StyleCallback(t));

    }
}

package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.view.text.ClickableTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 25.12.16.
 */

public class FragmentTop extends Fragment {

    static final String PAGE = "PAGE";
    String page;

    @Bind(R.id.item_content) ClickableTextView textView;

    public static FragmentTop newInstance(String page) {
        Bundle args = new Bundle();
        FragmentTop fragmentTop = new FragmentTop();
        args.putString(PAGE, page);
        fragmentTop.setArguments(args);
        return fragmentTop;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            page = args.getString(PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_front, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (page != null) {
            textView.setText(page);
//            textView.setOnWordClickListener(new ClickableTextView.OnWordClickListener() {
//                @Override
//                public void onClick(String word) {
//                    Toast.makeText(getContext(), word, Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }

    @SuppressWarnings("unchecked")
    private void startInfoActivity(View view, String page) {
//        FragmentActivity activity = getActivity();
//        ActivityCompat.startActivity(activity,
//                InfoActivity.newInstance(activity, travel),
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity,
//                        new Pair<>(view, getString(R.string.transition_image)))
//                        .toBundle());
    }
}
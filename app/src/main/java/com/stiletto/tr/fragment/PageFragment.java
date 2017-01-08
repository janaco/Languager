package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.Translation;
import com.stiletto.tr.translator.yandex.Translator;
import com.stiletto.tr.utils.TextUtils;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.view.PopupFragment;
import com.stiletto.tr.widget.ClickableTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment implements ClickableTextView.OnWordClickListener {

    @Bind(R.id.item_content)
    ClickableTextView textView;

    private View view;

    public static final String ARG_PAGE = "page";
    public static final String ARG_CONTENT = "content";
    private int pageNumber;
    private CharSequence content;

    public static PageFragment create(int pageNumber, CharSequence content) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putCharSequence(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
        content = getArguments().getCharSequence(ARG_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.page, container, false);
        ButterKnife.bind(this, view);
        textView.setOnWordClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView.setText(TextUtils.makeTextClickable(content.toString(), this));
    }


    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return pageNumber;
    }


    @Override
    public void onClick(final String word) {

        start();

    }

    public void start() {
        final PopupFragment mScalingActivityAnimator =
                new PopupFragment(
                        getActivity(), view, R.layout.pop_view);
        View popView = mScalingActivityAnimator.showPopup();
        Button mButtonBack = (Button) popView.findViewById(R.id.btn_cancel);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScalingActivityAnimator.hidePopup();
            }
        });

    }


    private void translate(final String word) {
        Translator.translate(word, Language.ENGLISH, Language.UKRAINIAN, new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {

                if (response.isSuccessful()) {
                    String res = response.body().getTranslationAsString();
                    Toast.makeText(getContext(), word + ": " + res, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {

            }
        });
    }


}


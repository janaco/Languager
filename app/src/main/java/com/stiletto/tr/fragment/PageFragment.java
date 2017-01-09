package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionaryAdapter;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.Translation;
import com.stiletto.tr.translator.yandex.Translator;
import com.stiletto.tr.translator.yandex.model.Dictionary;
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

    private View popView;
    private PopupFragment popupFragment;


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
        view = inflater.inflate(R.layout.page, container, false);
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

        showPopup();
        setPopupTitle(word);
        translate(word);
        lookup(word);

    }

    public void showPopup() {
        popupFragment = new PopupFragment(getActivity(), view, R.layout.pop_view);
        popView = popupFragment.showPopup();
        popView.findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFragment.hidePopup();
            }
        });

    }

    private void setPopupTitle(String title) {
        TextView textOrigin = (TextView) popView.findViewById(R.id.item_origin);
        textOrigin.setText(title);
    }

    private void setUpDictionary(Dictionary dictionary) {
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DictionaryAdapter adapter = new DictionaryAdapter(getContext(), dictionary.getDictionary());
        recyclerView.setAdapter(adapter);
    }


    private void translate(final String word) {
        Translator.translate(word, Language.ENGLISH, Language.UKRAINIAN, new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {

                if (response.isSuccessful()) {
                    String res = response.body().getTranslationAsString();

                    if (popView == null) {
                        showPopup();
                    }

                    setPopupTitle(word + " - " + res);

                }
            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {

            }
        });
    }


    private void lookup(final String word) {
        Log.d("DICTIONARY_", "lookup: " + word);

        Translator.getDictionary(word, Language.ENGLISH, Language.UKRAINIAN, new Callback<Dictionary>() {
            @Override
            public void onResponse(Call<Dictionary> call, Response<Dictionary> response) {
                Log.d("DICTIONARY_", "onResponse: " + response.code() + ", " + response.message());

                if (response.isSuccessful()) {
                    String res = response.body().toString();
                    Log.d("DICTIONARY_", "orgin: " + word + "\n" + res);
                    if (popView == null) {
                        showPopup();
                    }
                    setUpDictionary(response.body());

                }
            }

            @Override
            public void onFailure(Call<Dictionary> call, Throwable t) {

            }
        });
    }


}


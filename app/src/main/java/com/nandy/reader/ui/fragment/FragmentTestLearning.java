package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.mvp.contract.LearningContract;
import com.nandy.reader.mvp.model.LearningModel;
import com.nandy.reader.mvp.presenter.LearningPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 27.05.17.
 */

public class FragmentTestLearning extends Fragment implements LearningContract.View {

    @Bind(R.id.languages)
    TextView viewLanguages;
    @Bind(R.id.text)
    TextView viewText;
    @Bind(R.id.transcription)
    TextView viewTranscription;
    @Bind(R.id.translation)
    TextView viewTranslation;
    @Bind(R.id.count)
    TextView viewCountSmall;
    @Bind(R.id.count_large)
    TextView viewCountLarge;
    @Bind(R.id.prev)
    ImageView viewPrev;
    @Bind(R.id.next)
    ImageView viewNext;
    @Bind(R.id.pauseplay)
    ImageView viewPausePlay;

    private LearningContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_test_learning, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setLanguages(String languages) {
        viewLanguages.setText(languages);
    }

    @Override
    public void setLargeCount(String text) {
        viewCountLarge.setText(text);
    }

    @Override
    public void setSmallCount(String text) {
        viewCountSmall.setText(text);
    }

    @Override
    public void setPreviousButtonEnabled(boolean enabled) {
        if (enabled) {
            viewPrev.setImageAlpha(255);
            viewPrev.setClickable(true);
        } else {
            viewPrev.setImageAlpha(50);
            viewPrev.setClickable(false);
        }
    }

    @Override
    public void setTranslation(String translation) {
        viewTranslation.setText(translation);
    }

    @Override
    public void setTranscription(String transcription) {
        viewTranscription.setText(transcription);
    }

    @Override
    public void setText(String text) {
        viewText.setText(text);
    }

    @Override
    public void setNextButtonEnabled(boolean enabled) {
        if (enabled) {
            viewNext.setImageAlpha(255);
            viewNext.setClickable(true);
        } else {
            viewNext.setImageAlpha(50);
            viewNext.setClickable(false);
        }
    }

    @OnClick(R.id.prev)
    void goToPreviousItem() {
        presenter.previousWord();
    }

    @OnClick(R.id.next)
    void goToNextItem() {
        presenter.nextWord();
    }

    @OnClick(R.id.close)
    void onCloseWindowClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.google_translate)
    void onTranslateWithGoogleClick() {
        //TODO
    }

    @OnClick(R.id.speetch)
    void onTextToSpeetchClick() {
        //TODO
    }


    @Override
    public void setPresenter(LearningContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public static FragmentTestLearning newInstence(String originLanguage, String translationLanguage) {

        FragmentTestLearning fragment = new FragmentTestLearning();

        LearningPresenter presenter = new LearningPresenter(fragment);
        LearningModel model = new LearningModel(originLanguage, translationLanguage);
        presenter.setLearningModel(model);
        fragment.setPresenter(presenter);

        return fragment;
    }

}

package com.stiletto.tr.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stiletto.tr.Preferences;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.readers.TxtReader;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.utils.ReaderPrefs;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ClickableTextView;
import com.stiletto.tr.widget.JCTextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.yuweiguocn.lib.squareloading.SquareLoading;

/**
 * Created by yana on 04.01.17.
 */

public class PageViewerFragment extends Fragment
        implements ViewPager.OnPageChangeListener, DiscreteSeekBar.OnProgressChangeListener {

//    @Bind(R.id.pager)
    ViewPager viewPager;
//    @Bind(R.id.item_content)
    JCTextView itemBookPage;
//    @Bind(R.id.item_progress)
    SquareLoading progressBar;
//    @Bind(R.id.item_alert)
    TextView itemAlert;
//    @Bind(R.id.item_position)
//    TextView itemPageNumber;
//    @Bind(R.id.item_prev)
//    ImageView itemToPrevPage;
//    @Bind(R.id.item_next)
//    ImageView itemToNextPage;
//    @Bind(R.id.layout_page_control)
//    LinearLayout layoutPageControl;
//    @Bind(R.id.seekbar)
//    DiscreteSeekBar seekBar;

    private boolean isFullScreen = false;

    private PagerAdapter pagerAdapter;
    private Pagination pagination;
    private String path;
    private String bookName;

    private int bookmark = 0;

    private Language languagePrimary;
    private Language languageTranslation;


    String content = "Peer-to-peer, P2P (з англ. — рівний до рівного) — варіант архітектури системи, в основі якої стоїть мережа рівноправних вузлів. " +
            "Комп'ютерні мережі типу peer-to-peer (або P2P) засновані на принципі рівноправності учасників і характеризуються тим, що їх елементи " +
            "можуть зв'язуватися між собою, на відміну від традиційної архітектури, коли лише окрема категорія учасників, яка називається серверами " +
            "може надавати певні сервіси іншим. Фраза «peer-to-peer» була вперше використана у 1984 році Парбауелом Йохнухуйтсманом " +
            "(Parbawell Yohnuhuitsman) при розробці архітектури Advanced Peer to Peer Networking фірми IBM. В чистій «peer-to-peer» мережі не " +
            "існує поняття клієнтів або серверів, лише рівні вузли, які одночасно функціонують як клієнти та сервери по відношенню до інших вузлів " +
            "мережі. Ця модель мережевої взаємодії відрізняється від клієнт-серверної архітектури, в якій зв'язок відбувається лише між клієнтами " +
            "та центральним сервером. Така організація дозволяє зберігати працездатність мережі при будь-якій конфігурації доступних її учасників. " +
            "Проте практикується використання P2P мереж які все ж таки мають сервери, але їх роль полягає вже не у наданні сервісів, а у підтримці " +
            "інформації з приводу сервісів, що надаються клієнтами мережі. В P2P системі автономні вузли взаємодіють з іншими автономними вузлами. " +
            "Вузли є автономними в тому сенсі, що не існує загальної влади, яка може контролювати їх. В результаті автономії вузлів, вони не " +
            "можуть довіряти один одному та покладатися на поведінку інших вузлів, тому проблеми масштабування та надмірності стають важливішими " +
            "ніж у випадку традиційної архітектури. Сучасні P2P-мережі набули розвитку завдяки ідеям, пов'язаними з обміном інформацією, які формувалися у руслі того, кожен вузол може надавати та отримувати ресурси які надаються будь-якими іншими учасниками. У випадку мережі Napster, це був обмін музикою, в інших випадках це може бути надання процесорного часу для пошуку інопланетних цивілізацій (SETI@home) або ліків від раку (Folding@home)."+
            "P2P не є новим. Цей термін, звичайно, новий винахід, але сама технологія існує з часів появи Usenet та FidoNet — двох дуже успішних, цілком децентралізованих мереж. Розподілені обчислення з'явилися навіть раніше, але цих двох прикладів достатньо, щоб продемонструвати вік P2P." +
            "Usenet, який народився в 1979 році, — це розподілена мережа, яка забезпечує спілкування у групах новин. На початку це була праця двох студентів, Тома Траскота та Джіма Еллиса. В той час Інтернету, який ми знаємо зараз, ще не існувало. Обмін файлами відбувався за допомогою телефонних ліній, зазвичай протягом ночі, тому що це було дешевше. Таким чином не було ефективного способу централізувати такий сервіс як Usenet." +
            "Іншим видатним успіхом P2P був FidoNet. FidoNet, як і Usenet, — це децентралізована, розподілена мережа для обміну повідомленнями. FidoNet був створений у 1984 році Томом Дженнінгсом як засіб для обміну повідомленнями між користувачами різних BBS. Він був потрібен, тому він швидко виріс та, як і Usenet, існує по цей день."+
            "В залежності від того, як вузли з'єднуються один з одним можна поділити мережі на структуровані та неструктуровані:" +
            "    Неструктурована мережа P2P формується, коли з'єднання встановлюються довільно. Такі мережі можуть бути легко сконструйовані, оскільки новий вузол, який хоче приєднатися до мережі, може скопіювати існуючі з'єднання іншого вузла, а вже потім почати формувати свої власні. У неструктурованій мережі P2P, якщо вузол бажає знайти певні дані в мережі, запит доведеться передати майже через всю мережу, щоб охопити так багато вузлів, як можливо. Головним недоліком таких мереж є те, що запити, можливо, не завжди вирішуються. Скоріш за все популярні дані будуть доступні в багатьох вузлів та пошук швидко знайде потрібне, але якщо вузол шукає рідкісні дані, наявні лише в декількох інших вузлів, то надзвичайно малоймовірно, що пошук буде успішним. Оскільки немає ніякої кореляції між вузлами та даними, що вони зберігають, немає ніякої гарантії, що запит знайде вузол, який має бажані дані." +
            "    Структурована мережа P2P використовує єдиний алгоритм, щоб гарантувати, що будь-який вузол може ефективно передати запит іншому вузлу, який має бажаний файл, навіть якщо файл надзвичайно рідкісний. Така гарантія потребує структуровану систему з'єднань. У наш час найпопулярнішим типом структурованої мережі P2P є розподілені хеш-таблиці, в яких хешування використовується для встановлення зв'язку між даними та конкретним вузлом, який за них відповідає.";

    public static PageViewerFragment create(Bundle arguments, Language from, Language to) {

        PageViewerFragment fragment = new PageViewerFragment();

        arguments.putString("lang_from", from.toString());
        arguments.putString("lang_to", to.toString());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
        bookName= getArguments().getString("name");
        languagePrimary = Language.getLanguage(getArguments().getString("lang_from"));
        languageTranslation = Language.getLanguage(getArguments().getString("lang_to"));

        bookmark = Preferences.getBookmark(getContext(), bookName);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viewer, container, false);
//        ButterKnife.bind(this, view);
         viewPager =(ViewPager) view.findViewById(R.id.pager);
         progressBar = (SquareLoading) view.findViewById(R.id.item_progress);
        progressBar.setVisibility(View.GONE);
         itemAlert = (TextView) view.findViewById(R.id.item_alert);
        itemAlert.setVisibility(View.GONE);

        itemBookPage = (JCTextView) view.findViewById(R.id.item_content);

        viewPager.addOnPageChangeListener(this);
//        seekBar.setMin(1);
//        seekBar.setProgress(bookmark);
//        seekBar.setOnProgressChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setDecorViewState();

        ReaderPrefs prefs = ReaderPrefs.getPreferences(getContext());

        pagination = new Pagination(content, prefs);
//        pagination.splitOnPages();

        Log.d("PAGINATION_", ""+ pagination);
        Log.d("PAGINATION_", "pagesCount: "+ pagination.getPagesCount());
        pagerAdapter = new PagerAdapter(getChildFragmentManager(),
                pagination, languagePrimary, languageTranslation);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(bookmark);

        progressBar.setVisibility(View.GONE);
        itemAlert.setVisibility(View.GONE);

    }



    private void setUpPages() {

        if (path.endsWith(".pdf")) {
            parsePdf(new File(path));
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

//                pagination = new Pagination(getBookContent(), itemBookPage);
                Log.d("PAGINATION_", "pagination:" + pagination);
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
//                layoutPageControl.setVisibility(View.VISIBLE);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(bookmark);
//                seekBar.setMax(pagination.getPagesCount());
//                seekBar.setProgress(bookmark);
            }
        }.execute();

    }

    private void parsePdf(final File file) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

//                layoutPageControl.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
                viewPager.setAdapter(pagerAdapter);
            }
        };

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

//                pagination = new Pagination(PDFReader.parseAsText(file.getPath(), 1, 10), itemBookPage);
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));

                handler.sendEmptyMessage(1);

//                pagination = new Pagination(PDFReader.parseAsText(file.getPath()), itemBookPage);
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
//                layoutPageControl.setVisibility(View.VISIBLE);
                itemAlert.setVisibility(View.GONE);
                int currentPage = viewPager.getCurrentItem();
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(currentPage, false);

            }
        }.execute();

    }


    private CharSequence getBookContent() {

        File file = new File(path);

        String extension = file.getName().substring(file.getName().indexOf(".")).toLowerCase();

        switch (extension) {

            case ".pdf":
                return PDFReader.parseAsText(file.getPath());

            case ".epub":
                return EPUBReader.parseAsText(file);

            case ".txt":
                return TxtReader.parseAsText(file);
        }


        return "";
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void setDecorViewState() {
        View decorView = getActivity().getWindow().getDecorView();
        int newUiOptions = decorView.getSystemUiVisibility();

        if (!isFullScreen) {
            isFullScreen = true;
            Log.d("DECOR_VIEW", "hide");
            if (Build.VERSION.SDK_INT > 18) {

                newUiOptions ^=
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else if (Build.VERSION.SDK_INT >= 16) {

                newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            decorView.setSystemUiVisibility(newUiOptions);
        } else {
            Log.d("DECOR_VIEW", "show");
            isFullScreen = false;

            getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            if (Build.VERSION.SDK_INT >= 16) {
                getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }

            if (Build.VERSION.SDK_INT >= 19) {

                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ActivitiesCurrentContentView.requestLayout();

//            getView().requestLayout();


        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

//        if (position < 1) {
//            itemToPrevPage.setVisibility(View.GONE);
//            itemToNextPage.setVisibility(View.VISIBLE);
//        } else if (position == viewPager.getAdapter().getCount()) {
//            itemToNextPage.setVisibility(View.GONE);
//            itemToPrevPage.setVisibility(View.VISIBLE);
//        } else {
//            itemToPrevPage.setVisibility(View.VISIBLE);
//            itemToNextPage.setVisibility(View.VISIBLE);
//        }
//
//        itemPageNumber.setText(String.valueOf(position + 1));
//        seekBar.setProgress(position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.item_prev)
    void toPrevPage() {
        int currentPage = viewPager.getCurrentItem();
        if (currentPage >= 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }
    }


    @OnClick(R.id.item_next)
    void toNextPage() {

        int currentPage = viewPager.getCurrentItem();
        if (currentPage < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    private void setAdapter(PagerAdapter adapter) {
        this.pagerAdapter = adapter;

    }

    @Override
    public void onDestroy() {
        Preferences.setBookmark(getContext(), bookName, viewPager.getCurrentItem());
        super.onDestroy();

    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean byUser) {

        if (byUser){
            viewPager.setCurrentItem(value);
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }
}

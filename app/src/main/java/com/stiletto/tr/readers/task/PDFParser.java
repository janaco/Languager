package com.stiletto.tr.readers.task;

import android.content.Context;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PagesParserCallback;
import com.stiletto.tr.utils.ReaderPrefs;

import java.io.IOException;
import java.util.List;

/**
 * Created by yana on 21.05.17.
 */

public class PDFParser extends AsyncTask<Book, Void, Pagination>{


    private static final int STEP = 10;

    private Context context;

    private PagesParserCallback parserCallback;

    public PDFParser(Context context){
        this.context = context;
    }

    public PDFParser parserCallback(PagesParserCallback parserCallback) {
        this.parserCallback = parserCallback;

        return this;
    }

    @Override
    protected Pagination doInBackground(Book... books) {

        Book book = books[0];
        Pagination pagination = new Pagination(ReaderPrefs.getPreferences(context));

        String filePath = book.getPath();


        try {
            PdfReader reader = new PdfReader(filePath);

            int pages = reader.getNumberOfPages();
//            Message message = new Message();
//            message.what = 1;
//            Bundle bundle = new Bundle();
//            bundle.putInt("pages", pages);
//            message.setData(bundle);

            int bookmark = 1;
            int currentStep = 0;
            if (book.getBookmark() > 0 && book.getBookmark() > STEP / 2) {
                bookmark = book.getBookmark() - STEP / 2;
            }

            StringBuilder builder = new StringBuilder();
            for (int page = bookmark; page <= pages; page++, currentStep++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page));

                if (currentStep > STEP) {

                    List<CharSequence> content = pagination.appendContent(builder.toString());
                    parserCallback.onPagesParsed(pagination, content);

                    currentStep = 0;
                    builder.setLength(0);
                    builder.trimToSize();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pagination;
    }

    @Override
    protected void onPostExecute(Pagination pagination) {
        parserCallback.afterPagesParsingFinished(pagination);

    }


//    final Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//
//                case 1:
//                    int count = msg.getData().getInt("pages");
//                    seekBar.setMax(count);
//                    seekBar.setProgress(book.getBookmark());
//                    String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
//                    itemPages.setText(textProgress);
//                    pageNumber.setText(textProgress);
//
//                    int bookmark = viewPager.getCurrentItem();
//                    book.setBookmark(bookmark);
//                    BooksTable.setBookmark(getContext(), bookmark, pagination.getPagesCount(), book.getPath());
//
//                    break;
//
//                case 2:
//
//                    bookLoading.stop();
//                    layoutLoading.setVisibility(View.GONE);
//
//                    try {
//                        int currentItem = viewPager.getCurrentItem();
//                        viewPager.setAdapter(pagerAdapter);
//                        viewPager.setCurrentItem(currentItem);
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//
//        }
//    };



}

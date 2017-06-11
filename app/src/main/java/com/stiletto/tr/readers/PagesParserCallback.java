package com.stiletto.tr.readers;

import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;

import java.util.List;

/**
 * Created by yana on 21.05.17.
 */

public interface PagesParserCallback {

    void onPagesParsed(Pagination pagination);

    void afterPagesParsingFinished(Pagination pagination);
}

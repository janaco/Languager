package com.nandy.reader.parser;

import com.nandy.reader.pagination.Pagination;

/**
 * Created by yana on 21.05.17.
 */

public interface PagesParserCallback {

    void afterPagesParsingFinished(Pagination pagination);
}

package com.stiletto.tr.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.widget.ClickableTextView;

import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class PagesListAdapter extends RecyclerView.Adapter<PagesListAdapter.ViewHolder> {

    private List<CharSequence> list;

    private String text;

    public PagesListAdapter(String text) {
        this.text = text;
    }

    //    public PagesListAdapter(List<CharSequence> list) {
//        this.list = list;
//        Log.d("PAGINATION_", "pages: " + list.size());
//
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (list == null) {

            holder.itemContent.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onGlobalLayout() {

                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                                Pagination pagination = new Pagination(text,
                                        holder.itemContent.getWidth(),
                                        holder.itemContent.getHeight(),
                                        holder.itemContent.getPaint(),
                                        holder.itemContent.getLineSpacingMultiplier(),
                                        holder.itemContent.getLineSpacingExtra(),
                                        holder.itemContent.getIncludeFontPadding());

                                text = null;
                                list = pagination.getPages();

                                Log.d("PAGINATION_", "pagination: " + pagination);
                                Log.d("PAGINATION_", "size: " + list.size() + "{" + pagination.size() + "}");

                            }
                        }
                    });
        }else {
            CharSequence content = list.get(position);
            holder.itemContent.setText(content);
            holder.itemContent.setOnWordClickListener(new ClickableTextView.OnWordClickListener() {
                @Override
                public void onClick(String word) {
                    Toast.makeText(holder.itemContent.getContext(), word, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ClickableTextView itemContent;

        public ViewHolder(View itemView) {
            super(itemView);


            itemContent = (ClickableTextView) itemView.findViewById(R.id.item_content);
            Log.d("PAGINATION_", "VH: " + itemContent);
        }
    }
}

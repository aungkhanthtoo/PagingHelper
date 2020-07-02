package com.droiddev.paging;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created with love by A.K.HTOO on 01/07/2020,July,2020.
 */
public abstract class PagingListAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH> implements PagingHelper.LoadingAdapter<T, VH> {

    boolean loading;
    boolean refreshing;
    int loadingPosition = Integer.MAX_VALUE;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    protected PagingListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == getLoadingItemLayoutRes()) {
            return (VH) new RecyclerView.ViewHolder(
                    LayoutInflater.from(
                            parent.getContext()).inflate(getLoadingItemLayoutRes(), parent, false)
            ) {
            };
        }

        return onCreateItemViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        if (!getCurrentList().isEmpty() && position != loadingPosition) {
            onBindItemViewHolder(holder, position < loadingPosition ? position : position - 1);
        }
    }

    public final void setPaging(@NonNull List<T> pageList) {
        if (!refreshing) {
            if (getCurrentList().size() == pageList.size()) {
                return;
            }
        }
        submitList(pageList, this::hideLoading);
    }

    @Override
    public void showLoading() {
        if (!loading) {
            loading = true;
            loadingPosition = getDataItemCount();
            notifyItemInserted(loadingPosition);
        }
    }

    @Override
    public void hideLoading() {
        if (loading) {
            loading = false;
            final int position = loadingPosition;
            loadingPosition = getCurrentList().size();
            notifyItemRemoved(position);
        }
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    @Override
    public int getDataItemCount() {
        return getCurrentList().size();
    }

    @Override
    public final int getItemCount() {
        return loading ? super.getItemCount() + 1 : super.getItemCount();
    }

    protected int getDataItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == loadingPosition) {
            return getLoadingItemLayoutRes();
        } else {
            return getDataItemViewType(position);
        }
    }
}

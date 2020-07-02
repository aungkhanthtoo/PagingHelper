package com.droiddev.paging;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
public abstract class PagingAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements PagingHelper.LoadingAdapter<T, VH> {

    protected final List<T> list = new ArrayList<>(0);
    boolean loading;
    boolean refreshing;
    int loadingPosition = Integer.MAX_VALUE;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public PagingAdapter() {
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (positionStart > loadingPosition) {
                    handler.postDelayed(PagingAdapter.this::hideLoading, 50);
                }
            }
        });
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
        if (!list.isEmpty() && position != loadingPosition) {
            onBindItemViewHolder(holder, position < loadingPosition ? position : position - 1);
        }
    }

    public final void setPaging(@NonNull List<T> pageList) {
        if (refreshing) {
            final boolean isEmpty = list.isEmpty();
            list.clear();
            list.addAll(pageList);

            handler.post(() -> {
                if (isEmpty) {
                    notifyItemRangeInserted(0, pageList.size());
                } else {
                    notifyItemRangeChanged(0, pageList.size());
                }
            });
        } else {
            list.addAll(pageList);
            handler.post(() -> notifyItemRangeInserted(loadingPosition + 1, pageList.size()));
        }
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
            loadingPosition = getDataItemCount();
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
    public final int getItemCount() {
        return loading ? getDataItemCount() + 1 : getDataItemCount();
    }

    @Override
    public int getDataItemCount() {
        return list.size();
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

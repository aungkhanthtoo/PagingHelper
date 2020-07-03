package com.droiddev.paging;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Predicate;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with love by A.K.HTOO on 01/07/2020,July,2020.
 */
public abstract class PagingAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH> implements PagingHelper.LoadingAdapter<T, VH> {

    private final DiffUtil.ItemCallback<T> diff;
    boolean loading;
    boolean refreshing;
    int mPageSize;
    int loadingPosition = Integer.MAX_VALUE;

    protected PagingAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        diff = diffCallback;
    }

    public final void setPaging(@NonNull List<T> pageList) {
        if (!refreshing && computeDiff(pageList)) {
            pageList.addAll(0, getCurrentList()); // for network only paging
        }
        submitList(pageList);
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
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean isRefreshing() {
        return refreshing;
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

    @Override
    public void onCurrentListChanged(@NonNull List<T> previousList, @NonNull List<T> currentList) {
        if (previousList.size() < currentList.size()) {
            hideLoading();
        }
        if (refreshing && currentList.size() == mPageSize) {
            refreshing = false;
        }
    }

    public final void insertItem(int position, T item) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            current.add(position, item);
            submitList(current);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void insertItemRange(int position, Collection<? extends T> collection) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            if (current.addAll(position, collection)) {
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void insertItem(T item) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            if (current.add(item)) {
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void insertItemRange(Collection<? extends T> collection) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            if (current.addAll(collection)) {
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void removeItemAt(int position) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            current.remove(position);
            submitList(current);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void removeItemRange(Collection<? extends T> collection) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            if (current.removeAll(collection)) {
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void removeItemRange(int startPosition, int itemCount) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            final int lastPosition = startPosition + itemCount - 1;
            if (lastPosition < current.size()) {
                for (int i = startPosition; i <= lastPosition; i++) {
                    if (!current.remove(current.get(i))) return;
                }
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void removeItem(T item) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            current.remove(item);
            submitList(current);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void removeItemIf(Predicate<T> predicate) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            if (current.removeIf(predicate::test)) {
                submitList(current);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void changeItem(int position, T item) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            current.set(position, item);
            submitList(current);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public final void changeItemIf(T item, Predicate<T> predicate) {
        final List<T> current = new ArrayList<>(getCurrentList());
        try {
            for (int i = 0; i < current.size(); i++) {
                if (predicate.test(current.get(i))) current.set(i, item);
            }
            submitList(current);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private boolean computeDiff(List<T> pageList) {
        if (pageList.size() < getCurrentList().size()) return true;

        if (getCurrentList().isEmpty() || pageList.size() > getCurrentList().size()) {
            return false;
        }
        T oldItem = getCurrentList().get(0);
        T newItem = pageList.get(0);
        if (diff.areItemsTheSame(oldItem, newItem) && diff.areContentsTheSame(oldItem, newItem)) {
            return false;
        }
        oldItem = getCurrentList().get(getCurrentList().size() - 1);
        newItem = pageList.get(pageList.size() - 1);
        if (diff.areItemsTheSame(oldItem, newItem) && diff.areContentsTheSame(oldItem, newItem)) {
            return false;
        }
        oldItem = getCurrentList().get(getCurrentList().size() / 2);
        newItem = pageList.get(pageList.size() / 2);
        if (diff.areItemsTheSame(oldItem, newItem) && diff.areContentsTheSame(oldItem, newItem)) {
            return false;
        }

        return true;
    }

}

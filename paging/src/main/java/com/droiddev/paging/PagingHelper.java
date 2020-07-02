package com.droiddev.paging;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
public class PagingHelper extends RecyclerView.OnScrollListener {

    private static final int DEFAULT_THRESHOLD = 1;
    private static final int DEFAULT_PAGE_SIZE = 1;
    static final String TAG = "PagingHelper";

    private int mThreshold = DEFAULT_THRESHOLD;
    private int mPageSize = DEFAULT_PAGE_SIZE;
    private int mTotalPages = Integer.MAX_VALUE;
    private boolean mFetchOnAttach = true;
    private boolean refreshing = true;
    private boolean idle = true;

    private Callback mCallback;
    private RecyclerView mRecyclerView;
    private LoadingAdapter mController;
    private LayoutHelper<? extends RecyclerView.LayoutManager> mLayoutHelper;

    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            onItemRangeChanged(0, 0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (positionStart == 0 && refreshing) {
                setRefreshing(false);
                if (idle) {
                    idle = false;
                    if (mPageSize == DEFAULT_PAGE_SIZE) {
                        if (mRecyclerView.getAdapter() instanceof PagingAdapter) {
                            mPageSize = mRecyclerView.getAdapter().getItemCount();
                        } else {
                            throw new IllegalStateException("PagingHelper cannot infer pageSize for PagingListAdapter, set PageSize explicitly!");
                        }
                    }
                }
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onItemRangeChanged(toPosition, itemCount);
        }

    };

    private final Runnable loadMoreRunnable = new Runnable() {

        @Override
        public void run() {
            mController.showLoading();
            mCallback.onLoadMore(getPage() + 1);
        }
    };

    private final Runnable refreshRunnable = new Runnable() {

        @Override
        public void run() {
            mCallback.onLoadMore(1);
        }
    };

    public PagingHelper() {
    }

    public PagingHelper(@NonNull Callback callback) {
        this(DEFAULT_THRESHOLD, DEFAULT_PAGE_SIZE, callback);
    }

    public PagingHelper(int threshold, @NonNull Callback callback) {
        this(threshold, DEFAULT_PAGE_SIZE, callback);
    }

    public PagingHelper(int threshold, int pageSize, @NonNull Callback callback) {
        mThreshold = threshold;
        mCallback = callback;
        mPageSize = pageSize;
    }

    /**
     * Attaches the {@link PagingHelper} to the provided RecyclerView, by calling
     * {@link RecyclerView#addOnScrollListener(RecyclerView.OnScrollListener)}.
     * You can call this method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove PagingHelper from the current
     *                     RecyclerView.
     */
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        if (mRecyclerView == recyclerView) {
            return; // nothing to do
        }

        if (mRecyclerView != null) {
            destroyCallbacks();
        }

        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setupCallbacks();
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        int d = mLayoutHelper.canScrollVertically() ? dy : dx;
        d *= mLayoutHelper.isReverseLayout() ? -1 : 1;

        if (d > 0 && !refreshing && !mController.isLoading() && getPage() < mTotalPages) {
            if (mThreshold >= (mLayoutHelper.getItemCount() - 1 - mLayoutHelper.getLastCompletelyVisibleItemPosition())) {
                post(loadMoreRunnable);
            }
        }
    }

    public void fallback() {
        mController.hideLoading();
    }

    public int getPage() {
        final int itemCount = getItemCount();
        return refreshing ? 1 : Math.min(itemCount % mPageSize, 1) + itemCount / mPageSize;
    }

    public void refresh() {
        setRefreshing(true);
        mController.hideLoading();
        post(refreshRunnable);
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int total) {
        if (total != mTotalPages && total > 1) {
            mTotalPages = total;
        }
    }

    void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        mController.setRefreshing(refreshing);
    }

    public int getThreshold() {
        return mThreshold;
    }

    public void setThreshold(int threshold) {
        if (threshold > DEFAULT_THRESHOLD) {
            mThreshold = threshold;
        }
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > DEFAULT_PAGE_SIZE) {
            mPageSize = pageSize;
        }
    }

    public void setOnLoadMore(Callback callback) {
        this.mCallback = callback;
    }

    public Callback getOnLoadMore() {
        return mCallback;
    }

    public void setFetchOnAttach(boolean fetchOnAttach) {
        this.mFetchOnAttach = fetchOnAttach;
    }

    public boolean getFetchOnAttach() {
        return mFetchOnAttach;
    }

    public void setLayoutHelper(LayoutHelper<? extends RecyclerView.LayoutManager> helper) {
        mLayoutHelper = helper;
    }

    private int getItemCount() {
        final RecyclerView.Adapter adapter = requireNonNull(mRecyclerView.getAdapter());
        if (adapter instanceof LoadingAdapter) {
            return ((LoadingAdapter) adapter).getDataItemCount();
        }
        return adapter.getItemCount();
    }

    private void setupCallbacks() throws IllegalStateException {
        if (mCallback == null) {
            throw new IllegalStateException("RecyclerView Callback must be set before attaching with PagingHelper!");
        }

        if (mRecyclerView.getAdapter() == null) {
            throw new IllegalStateException("RecyclerView Adapter must not be null before attaching with PagingHelper!");
        }

        if (!(mRecyclerView.getAdapter() instanceof LoadingAdapter)) {
            throw new IllegalStateException("RecyclerView Adapter must implement PagingHelper.LoadingAdapter!");
        }

        if (mLayoutHelper == null) {
            final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                mLayoutHelper = new LinearGridLayoutHelper((LinearLayoutManager) layoutManager);
            } else {
                throw new IllegalStateException("Unknown LayoutManager class " + layoutManager.getClass().getSimpleName() + "\n" +
                        "Provide your LayoutHelper via setLayoutHelper");
            }
        }

        if (mController == null) {
            mController = ((LoadingAdapter) mRecyclerView.getAdapter());
        }

        mRecyclerView.addOnScrollListener(this);
        mRecyclerView.getAdapter().registerAdapterDataObserver(mObserver);

        if (mFetchOnAttach) refresh();
    }

    private void destroyCallbacks() {
        mRecyclerView.removeOnScrollListener(this);
        requireNonNull(mRecyclerView.getAdapter()).unregisterAdapterDataObserver(mObserver);
    }

    private void post(Runnable runnable) {
        mRecyclerView.removeCallbacks(runnable);
        mRecyclerView.post(runnable);
    }

    private void setupLayoutHelper() throws IllegalStateException {

    }

    public interface LoadingController {

        void showLoading();

        void hideLoading();

        boolean isLoading();

        void setRefreshing(boolean refreshing);
    }

    public interface LoadingAdapter<T, VH extends RecyclerView.ViewHolder> extends LoadingController {

        int getDataItemCount();

        @LayoutRes
        int getLoadingItemLayoutRes();

        @NonNull
        VH onCreateItemViewHolder(@NonNull ViewGroup parent, int viewType);

        void onBindItemViewHolder(@NonNull VH holder, int position);

        void setPaging(@NonNull List<T> pageList);
    }

    @FunctionalInterface
    public interface Callback {

        void onLoadMore(int nextPage);
    }

    public static abstract class LayoutHelper<L extends RecyclerView.LayoutManager> {

        protected L mLayoutManager;

        protected LayoutHelper(L layoutManager) {
            mLayoutManager = layoutManager;
        }

        public boolean isReverseLayout() {
            return false;
        }

        public boolean canScrollVertically() {
            return mLayoutManager.canScrollVertically();
        }

        public int getItemCount() {
            return mLayoutManager.getItemCount();
        }

        public abstract int getLastCompletelyVisibleItemPosition();

    }

    static class LinearGridLayoutHelper extends LayoutHelper<LinearLayoutManager> {

        LinearGridLayoutHelper(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public int getLastCompletelyVisibleItemPosition() {
            return mLayoutManager.findLastCompletelyVisibleItemPosition();
        }

        @Override
        public boolean isReverseLayout() {
            return mLayoutManager.getReverseLayout();
        }

    }

}

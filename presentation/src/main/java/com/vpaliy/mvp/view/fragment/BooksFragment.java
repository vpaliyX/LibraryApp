package com.vpaliy.mvp.view.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;
import com.vpaliy.domain.model.BookModel;
import com.vpaliy.mvp.App;
import com.vpaliy.mvp.R;
import com.vpaliy.mvp.di.component.DaggerFragmentComponent;
import com.vpaliy.mvp.di.module.PresenterModule;
import com.vpaliy.mvp.mvp.contract.BookDetailsContract;
import com.vpaliy.mvp.mvp.contract.BookListContract;
import com.vpaliy.mvp.view.adapter.BookAdapter;
import com.vpaliy.mvp.view.utils.Constant;
import com.vpaliy.mvp.view.utils.eventBus.ExternalAction;
import com.vpaliy.mvp.view.utils.snackbarUtils.SnackbarWrapper;

import java.util.List;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.vpaliy.mvp.mvp.contract.BookListContract.Presenter;

public class BooksFragment extends Fragment
        implements BookListContract.View {

    private Presenter presenter;
    private BookAdapter adapter;

    @BindView(R.id.recycleView)
    protected RecyclerView bookList;

    @BindView(R.id.actionButton)
    protected FloatingActionButton actionButton;

    @Inject
    protected Bus eventBus;

    @BindView(R.id.refresher)
    protected SwipeRefreshLayout refresher;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        presenter.onAttachView(this);
        View view=inflater.inflate(R.layout.fragment_models,container,false);
        unbinder=ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View root, @Nullable Bundle savedInstanceState) {
        if(root!=null) {
            refresher.setOnRefreshListener(()->presenter.requestUpdate());
            actionButton.setOnClickListener(this::addClick);
        }
    }

    private void initializeInjector() {
        DaggerFragmentComponent.builder()
                .applicationComponent(App.app().provideAppComponent())
                .presenterModule(new PresenterModule())
                .build().inject(this);
    }



    @Override
    public void showBookList(@NonNull List<BookModel> userModelList) {
        adapter=new BookAdapter(getContext(),userModelList);
        bookList.setLayoutManager(new GridLayoutManager(getContext(),
            getResources().getInteger(R.integer.spanCount),
            GridLayoutManager.VERTICAL,false));
        bookList.setAdapter(adapter);
    }

    @Override
    public void showDeleteBook() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showLoadingError() {
        View root=getView();
        if(root!=null) {
            SnackbarWrapper
                .start(root,R.string.loadingError, Snackbar.LENGTH_INDEFINITE)
                .show();
        }
    }

    @Override
    public void appendBookList(@NonNull List<BookModel> bookModelList) {
        adapter.appendData(bookModelList);
    }

    @Override
    public void setLoadingIndicator(boolean isVisible) {
        refresher.setRefreshing(isVisible);
    }

    @Override
    public void switchToUsers() {
        eventBus.post(new ExternalAction<Void>(Constant.SWAP_TO_USERS));
    }

    @OnClick(R.id.actionButton)
    public void addClick(View view) {
        presenter.addBook();
    }

    @Override
    public void addBookAction() {
        eventBus.post(new ExternalAction<Void>(Constant.ADD_BOOK));
    }

    @Override
    @Inject
    public void attachPresenter(@NonNull Presenter presenter) {
        this.presenter=presenter;
    }
}

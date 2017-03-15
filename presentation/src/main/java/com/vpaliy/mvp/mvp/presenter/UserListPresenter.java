package com.vpaliy.mvp.mvp.presenter;

import android.support.annotation.NonNull;
import com.vpaliy.common.scheduler.SchedulerProvider;
import com.vpaliy.domain.interactor.AddUseCase;
import com.vpaliy.domain.interactor.DeleteUseCase;
import com.vpaliy.domain.interactor.GetListUseCase;
import com.vpaliy.domain.model.UserModel;
import java.util.Collection;
import java.util.List;

import static com.vpaliy.mvp.mvp.contract.UserListContract.*;


public class UserListPresenter implements Presenter {

    /* Use cases */
    private final GetListUseCase<UserModel> getListUseCase;
    private final AddUseCase<UserModel> addUseCase;
    private final DeleteUseCase<UserModel> deleteUseCase;

    private final SchedulerProvider schedulerProvider;
    private View view;

    public UserListPresenter(@NonNull GetListUseCase<UserModel> getListUseCase,
                             @NonNull AddUseCase<UserModel> addUseCase,
                             @NonNull DeleteUseCase<UserModel> deleteUseCase,
                             @NonNull SchedulerProvider schedulerProvider) {
        this.getListUseCase=getListUseCase;
        this.addUseCase=addUseCase;
        this.deleteUseCase=deleteUseCase;
        this.schedulerProvider=schedulerProvider;
    }


    @Override
    public void onAttachView(@NonNull View view) {
        this.view=view;
    }

    @Override
    public void add(@NonNull UserModel user) {

    }

    @Override
    public void delete(@NonNull UserModel user) {

    }

    @Override
    public void delete(Collection<UserModel> users) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    private void initialize() {
        getListUseCase.execute()
                .observeOn(schedulerProvider.ui())
                .subscribe(this::processData,
                           this::errorHasOccurred,
                        ()->view.setLoadingIndicator(false));
    }

    private void processData(@NonNull List<UserModel> userList) {
        view.showUserList(userList);
    }

    private void errorHasOccurred(Throwable throwable) {
        view.showLoadingError();
    }


}

package service.sitter.ui.manageRequests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageRequestsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ManageRequestsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manage requests fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
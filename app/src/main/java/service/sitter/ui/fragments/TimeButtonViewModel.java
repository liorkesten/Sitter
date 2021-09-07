package service.sitter.ui.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimeButtonViewModel extends ViewModel{

    private MutableLiveData<String> time;

    public TimeButtonViewModel() {
        time = new MutableLiveData<>();
        time.setValue("00:00");
    }

    public LiveData<String> getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.setValue(time);
    }
}
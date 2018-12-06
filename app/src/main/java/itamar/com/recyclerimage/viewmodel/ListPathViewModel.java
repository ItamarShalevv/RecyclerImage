package itamar.com.recyclerimage.viewmodel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;

public class ListPathViewModel extends ViewModel {
    private final List<String> pathImagesList;

    public ListPathViewModel(List<String> pathImagesList) {
        this.pathImagesList = pathImagesList;
    }

    public List<String> getPathImagesList() {
        return pathImagesList;
    }
}

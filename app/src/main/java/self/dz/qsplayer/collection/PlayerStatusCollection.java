package self.dz.qsplayer.collection;

import java.util.ArrayList;
import java.util.List;

import self.dz.qsplayer.enums.TitleFunctions;

public class PlayerStatusCollection {

    private static PlayerStatusCollection INSTANCE;

    private PlayerStatusCollection() {

    }

    public static PlayerStatusCollection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerStatusCollection();
        }
        return INSTANCE;
    }

    private List<StatusListener> statusListenerList = new ArrayList<>();

    public interface StatusListener {
        void statusUpdate(TitleFunctions function);
    }

    public void subscribeListener(StatusListener listener) {
        statusListenerList.add(listener);
    }

    public void unScribeListener(StatusListener listener){
        statusListenerList.remove(listener);
    }

    public void statusUpdate(TitleFunctions function){
        for (StatusListener listener: statusListenerList) {
            listener.statusUpdate(function);
        }
    }
}

package logic;

import machineDtos.EngineInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class CodeSetEventHandler {

    private List<CodeSetEventListener> listenersList;

    public void addListener(CodeSetEventListener listener){

        listenersList.add(listener);
    }

    public void removeListener(CodeSetEventListener listener){
        listenersList.remove(listener);
    }

    private void fireEvent (EngineInfoDTO updatedValue) {

        List<CodeSetEventListener> listenersToInvoke = new ArrayList<>(listenersList);
        for (CodeSetEventListener listener : listenersToInvoke) {
            listener.update(updatedValue);
        }
    }
}

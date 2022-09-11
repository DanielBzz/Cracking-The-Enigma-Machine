package logic.events;

import logic.events.handler.Invokable;
import machineDtos.EngineInfoDTO;

public interface CodeSetEventListener extends Invokable<EngineInfoDTO> {

    void invoke(EngineInfoDTO updatedValue);
}

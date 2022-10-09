package logic;

import manager.DecryptionManager;

public class ContestDetails {

    private final EnigmaSystemEngine machineEngine;
    private final DecryptionManager decryptionManager;
    private final BattleField field;

    public ContestDetails(EnigmaSystemEngine machineEngine, DecryptionManager decryptionManager, BattleField field) {
        this.machineEngine = machineEngine;
        this.decryptionManager = decryptionManager;
        this.field = field;
    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public EnigmaSystemEngine getMachineEngine() {
        return machineEngine;
    }

    public BattleField getField() {
        return field;
    }
}

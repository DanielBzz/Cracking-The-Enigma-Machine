package machineDtos;

public class EngineWithEncryptDTO extends EngineDTO{

    private String encryptedMsg;

    public EngineWithEncryptDTO(EngineDTO engine,String encryptedMsg) {
        super(engine.getNumOfOptionalRotors(), engine.getNumOfOptionalReflectors(),
                engine.getNumOfUsedRotors(), engine.getNumOfEncryptedMsg(), engine.getMachineInitialInfo(),
                engine.getMachineCurrentInfo(),engine.getEngineComponentsInfo());

        this.encryptedMsg = encryptedMsg;
    }

    public String getEncryptedMsg() {
        return encryptedMsg;
    }

    public EngineDTO getEngineDTO(){
        return this;
    }
}

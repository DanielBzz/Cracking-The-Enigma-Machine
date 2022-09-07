package machineDtos;

public class EngineInfoDTO {

        private final int numOfOptionalRotors;
        private final int numOfOptionalReflectors;
        private final int numOfUsedRotors;
        private final int numOfEncryptedMsg;
        private final MachineInfoDTO machineInitialInfo;
        private final MachineInfoDTO machineCurrentInfo;
        private final EngineComponentsDTO engineComponentsInfo;

        public EngineInfoDTO(int numOfOptionalRotors, int numOfOptionalReflectors, int numOfUsedRotors, int numOfEncryptedMsg,
                             MachineInfoDTO initialInfo,MachineInfoDTO currentInfo,EngineComponentsDTO componentsInfo ) {

                this.numOfOptionalRotors = numOfOptionalRotors;
                this.numOfOptionalReflectors = numOfOptionalReflectors;
                this.numOfUsedRotors = numOfUsedRotors;
                this.numOfEncryptedMsg = numOfEncryptedMsg;
                this.machineInitialInfo = initialInfo;
                this.machineCurrentInfo = currentInfo;
                engineComponentsInfo = componentsInfo;
        }

        public int getNumOfOptionalRotors() {

                return numOfOptionalRotors;
        }

        public int getNumOfOptionalReflectors() {

                return numOfOptionalReflectors;
        }

        public int getNumOfUsedRotors() {

                return numOfUsedRotors;
        }

        public int getNumOfEncryptedMsg() {

                return numOfEncryptedMsg;
        }

        public MachineInfoDTO getMachineInitialInfo() {

                return machineInitialInfo;
        }

        public MachineInfoDTO getMachineCurrentInfo() {

                return machineCurrentInfo;
        }

        public EngineComponentsDTO getEngineComponentsInfo() {
                return engineComponentsInfo;
        }
}

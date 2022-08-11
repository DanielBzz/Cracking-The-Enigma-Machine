public class EngineInfoDTO {

        private final int numOfOptionalRotors;
        private final int numOfOptionalReflectors;
        private final int numOfUsedRotors;
        private final int numOfEncryptedMsg;
        private final MachineInfoDTO machineInfo;

        public EngineInfoDTO(int numOfOptionalRotors, int numOfOptionalReflectors, int numOfUsedRotors, int numOfEncryptedMsg, MachineInfoDTO machineInfo) {
                this.numOfOptionalRotors = numOfOptionalRotors;
                this.numOfOptionalReflectors = numOfOptionalReflectors;
                this.numOfUsedRotors = numOfUsedRotors;
                this.numOfEncryptedMsg = numOfEncryptedMsg;
                this.machineInfo = machineInfo;
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

        public MachineInfoDTO getMachineInfo() {
                return machineInfo;
        }
}

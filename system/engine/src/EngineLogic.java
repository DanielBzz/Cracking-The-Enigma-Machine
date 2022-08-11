import scheme.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class EngineLogic  {

    public static CTEEnigma createEnigmaFromFile(String path) {

        CTEEnigma enigmaMachine = null;

        try {
            InputStream inputStream = new FileInputStream(new File(path.trim()));
            enigmaMachine = deserializeFrom(inputStream);
        } catch (FileNotFoundException var3) {
            System.out.println("shittttttttttttt");
        } catch (JAXBException var3) {
            System.out.println("tihsss");
            var3.printStackTrace();
        }

        return enigmaMachine;
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("scheme.generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static boolean checkMachineIsValid(CTEMachine machine) {

        boolean validMachine = true;

        if (machine.getABC().trim().length() % 2 != 0) {
            validMachine = false;

        }
        else if (!rotorsIsValid(machine.getCTERotors(), machine.getRotorsCount(), machine.getABC().trim())) {
            validMachine = false;
        }
        else if (!reflectorsIsValid(machine.getCTEReflectors(), machine.getABC())) {
            validMachine = false;
        }

        return validMachine;
    }

    private static boolean rotorsIsValid(CTERotors rotorsList, int rotorsCount, String ABC) {

        int i = 1, notchLocation;
        boolean validRotors = rotorsCount > 1 && rotorsCount <= rotorsList.getCTERotor().size();

        rotorsList.getCTERotor().sort((rotor1, rotor2) -> rotor1.getId() - rotor2.getId());
        for (CTERotor rotor : rotorsList.getCTERotor()) {
            notchLocation = rotor.getNotch();
            if (rotor.getId() != i || notchLocation > ABC.length() || notchLocation < 1) {
                validRotors = false;
            }

            if (!checkPositioningSingleValueMapping(rotor.getCTEPositioning(), ABC)) {
                validRotors = false;
            }

            i++;
        }

        return validRotors;
    }

    private static boolean checkPositioningSingleValueMapping(List<CTEPositioning> positioningList, String ABC) {

        Set<String> leftSet = new HashSet<>();
        Set<String> rightSet = new HashSet<>();

        for (CTEPositioning positioning : positioningList) {
            if (ABC.contains(positioning.getLeft())) {
                leftSet.add(positioning.getLeft());
            }

            if (ABC.contains(positioning.getRight())) {
                rightSet.add(positioning.getRight());
            }
        }

        return leftSet.size() == rightSet.size() && rightSet.size() == ABC.length();
    }

    private static boolean checkReflectSingleValueMapping(List<CTEReflect> reflectList, String ABC) {

        Set<Integer> leftSet = new HashSet<>();
        Set<Integer> rightSet = new HashSet<>();

        for (CTEReflect reflect : reflectList) {
            if ((reflect.getInput() < ABC.length())) {
                leftSet.add(reflect.getInput());
            }

            if (reflect.getOutput() < ABC.length()) {
                rightSet.add(reflect.getOutput());
            }

            if(reflect.getInput() == reflect.getOutput()){
                return false;
            }
        }

        return (leftSet.size() + rightSet.size()) == ABC.trim().length();
    }

    private static boolean reflectorsIsValid(CTEReflectors reflectorsList, String ABC) {

        boolean validReflectors = reflectorsList.getCTEReflector().size() > 0 && reflectorsList.getCTEReflector().size() <= 5;

        boolean[] checkId = new boolean[reflectorsList.getCTEReflector().size()];

        for (CTEReflector reflector : reflectorsList.getCTEReflector()) {
            int index = idTranslator(reflector.getId()) - 1;
            if(index<reflectorsList.getCTEReflector().size()) {
                checkId[index] = true;
            }

            if(!checkReflectSingleValueMapping(reflector.getCTEReflect(), ABC)) {
                validReflectors = false;
            }
        }

        for (boolean checked : checkId) {
            if(checked == false){
                validReflectors = false;
            }
        }

        return validReflectors;
    }

    private static int idTranslator(String id){

        switch (id){
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
        }

        return -1;
    }
}
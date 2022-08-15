import exceptions.*;
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

    public static CTEEnigma createEnigmaFromFile(String path) throws FileNotFoundException, JAXBException {

        CTEEnigma enigmaMachine = null;

        try {
            InputStream inputStream = new FileInputStream(new File(path.trim()));
            enigmaMachine = deserializeFrom(inputStream);
        } catch (FileNotFoundException var3) {
            throw new FileNotFoundException("There isn't file according to the path that you insert");
        } catch (JAXBException var3) {
            throw new JAXBException("Your xml file is not following the rules of the scheme");
        }

        return enigmaMachine;
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("scheme.generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static void checkMachineIsValid(CTEMachine machine) throws InvalidAbcException, MultipleMappingException, NotchOutOfRangeException, IdMissingInRangeException, ConvertorsInMachineOutOfRangeException, TooManyRotorsInUseException {

        if (machine.getABC().trim().length() % 2 != 0) {
            throw new InvalidAbcException(machine.getABC().trim().length());
        }

        rotorsIsValid(machine.getCTERotors(), machine.getRotorsCount(), machine.getABC().trim());
        reflectorsIsValid(machine.getCTEReflectors(), machine.getABC());
    }

    private static void rotorsIsValid(CTERotors rotorsList, int rotorsCount, String ABC) throws IdMissingInRangeException, NotchOutOfRangeException, MultipleMappingException, ConvertorsInMachineOutOfRangeException, TooManyRotorsInUseException {

        int i = 1, notchLocation;

        if (rotorsCount > 99) {
            throw new TooManyRotorsInUseException(rotorsCount);
        }
        else if(rotorsCount < 2 || rotorsCount > rotorsList.getCTERotor().size()) {
            throw new ConvertorsInMachineOutOfRangeException("Rotors", rotorsCount, 2, rotorsList.getCTERotor().size());
        }

        rotorsList.getCTERotor().sort((rotor1, rotor2) -> rotor1.getId() - rotor2.getId());
        for (CTERotor rotor : rotorsList.getCTERotor()) {
            notchLocation = rotor.getNotch();
            if (rotor.getId() != i) {
                throw  new IdMissingInRangeException(rotor, i);
            }
            else if (notchLocation > ABC.length() || notchLocation < 1) {
                throw new NotchOutOfRangeException(rotor.getId(), notchLocation, ABC.length());
            }
            else if (!checkPositioningSingleValueMapping(rotor, ABC)) {
                throw new MultipleMappingException(rotor, rotor.getId());
            }

            i++;
        }
    }

    private static void reflectorsIsValid(CTEReflectors reflectorsList, String ABC) throws MultipleMappingException, IdMissingInRangeException, ConvertorsInMachineOutOfRangeException {

        boolean[] checkId = new boolean[reflectorsList.getCTEReflector().size()];

        if(reflectorsList.getCTEReflector().size() < 1 || reflectorsList.getCTEReflector().size() > 5) {
            throw new ConvertorsInMachineOutOfRangeException("Reflectors", reflectorsList.getCTEReflector().size(), 1 , 5);
        }

        for (CTEReflector reflector : reflectorsList.getCTEReflector()) {
            int index = idDecoder(reflector.getId()) - 1;
            if(index<reflectorsList.getCTEReflector().size() && index >= 0) {
                checkId[index] = true;
            }

            if(!checkReflectSingleValueMapping(reflector.getCTEReflect(), ABC)) {
                throw new MultipleMappingException(reflector, reflector.getId());
            }
        }

        for (int i=0 ; i< checkId.length ; ++i) {
            if(!checkId[i]){
                throw new IdMissingInRangeException(reflectorsList.getCTEReflector().get(0), idEncoder(i+1));
            }
        }
    }

    private static boolean checkPositioningSingleValueMapping(CTERotor currentRotor, String ABC) {

        Set<String> leftSet = new HashSet<>();
        Set<String> rightSet = new HashSet<>();

        for (CTEPositioning positioning : currentRotor.getCTEPositioning()) {
            if (ABC.contains(positioning.getLeft())) {
                leftSet.add(positioning.getLeft());
            }

            if (ABC.contains(positioning.getRight())) {
                rightSet.add(positioning.getRight());
            }
        }

        return leftSet.size() == rightSet.size() && rightSet.size() == ABC.trim().length();
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
        // need to improve the check here
        return (leftSet.size() + rightSet.size()) == ABC.trim().length();
    }
/*
    private static <T> boolean f(List<T> reflectList, String ABC) {

        Set<Integer> leftSet = new HashSet<>();
        Set<Integer> rightSet = new HashSet<>();

        for (T reflect : reflectList) {

            if(reflect instanceof CTEReflect){
                ((CTEReflect) reflect).getInput();
                ((CTEReflect) reflect).getOutput();
            }
            if(reflect instanceof CTEPositioning){
                ((CTEPositioning) reflect).getLeft();
                ((CTEPositioning) reflect).getRight();

            }

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
*/

    private static int idDecoder(String id){

        return RomanNumeral.valueOf(id).evaluateNumber();
    }
        // switch encoder and decoder with ENUMS
    private static RomanNumeral idEncoder(int id){

        return Arrays.stream(RomanNumeral.values()).filter((romanNumeral) -> romanNumeral.evaluateNumber() == id).findFirst().get();
    }
}
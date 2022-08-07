import java.util.ArrayList;
import java.util.List;

public  class Main {

    public static void main(String[] args) {

        PlugBoard plug = new PlugBoard();
        plug.add('A','F');
        List<Character> ABC = new ArrayList<Character>();
        ABC.add('A');
        ABC.add('B');
        ABC.add('C');
        ABC.add('D');
        ABC.add('E');
        ABC.add('F');
        Rotor rotor1 = new Rotor(1, 4);
        Rotor rotor2 = new Rotor(2, 1);
        rotor1.getRotorConversions().add('A','F');
        rotor1.getRotorConversions().add('B','E');
        rotor1.getRotorConversions().add('C','D');
        rotor1.getRotorConversions().add('D','C');
        rotor1.getRotorConversions().add('E','B');
        rotor1.getRotorConversions().add('F','A');

        rotor2.getRotorConversions().add('A','E');
        rotor2.getRotorConversions().add('B','B');
        rotor2.getRotorConversions().add('C','D');
        rotor2.getRotorConversions().add('D','F');
        rotor2.getRotorConversions().add('E','C');
        rotor2.getRotorConversions().add('F','A');

        Reflector ref = new Reflector();
        ref.add(0,3);
        ref.add(1,4);
        ref.add(2,5);

        EnigmaMachine machine = new Machine(ref, rotor1,rotor2,2,2,ABC,plug);

        System.out.println( machine.encryption('A'));
        System.out.println( machine.encryption('A'));
        System.out.println( machine.encryption('A'));
        System.out.println( machine.encryption('E'));
        System.out.println( machine.encryption('E'));
        System.out.println( machine.encryption('E'));
        System.out.println( machine.encryption('B'));
        System.out.println( machine.encryption('B'));
        System.out.println( machine.encryption('B'));
        System.out.println( machine.encryption('D'));
        System.out.println( machine.encryption('D'));
        System.out.println( machine.encryption('D'));
        System.out.println( machine.encryption('C'));
        System.out.println( machine.encryption('C'));
        System.out.println( machine.encryption('C'));
        System.out.println( machine.encryption('F'));
        System.out.println( machine.encryption('F'));
        System.out.println( machine.encryption('F'));
    }

}

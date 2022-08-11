import exceptions.IdOutOfRangeException;

public class MainClass {

    public static void main(String[] args) {

       // EnigmaEngine engine = new EnigmaEngine();

       // engine.loadXmlFile("C:\\Users\\DanielBazis\\IdeaProjects\\CarckingTheEnigma\\resources\\ex1-error-3.xml");

        IdOutOfRangeException check = new IdOutOfRangeException(new Reflector("III", null), "III");

        System.out.println(check.getMessage());
    }
}

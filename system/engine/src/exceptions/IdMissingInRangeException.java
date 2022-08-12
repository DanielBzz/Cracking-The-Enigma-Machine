package exceptions;

public class IdMissingInRangeException extends Exception{

    Object obj;
    Object id;

    public IdMissingInRangeException(Object obj, Object id){
        this.obj = obj;
        this.id = id;
    }


    public String getMessage(){

        /*while (!clazz.getSimpleName().equals("Object")) {
            for (Field clazzField : clazz.getDeclaredFields()) {
                if(clazzField.getName().toUpperCase().equals("ID")){

                    try {
                        idValue = clazzField.get(()obj);
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }*/
        return String.format(obj.getClass().getSimpleName() + " with id number - " + id + " does not exist in the system");
    }
}


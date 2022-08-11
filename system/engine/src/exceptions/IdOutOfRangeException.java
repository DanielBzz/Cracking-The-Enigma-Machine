package exceptions;

public class IdOutOfRangeException extends Exception{

    Object obj;
    Object id;

    public IdOutOfRangeException(Object obj, Object id){
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
        return String.format(obj.getClass().getSimpleName() + " with id number - " + id + " is out of range ");
    }
}


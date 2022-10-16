package contestDtos;

public class ActivePlayerDTO {
    private final String name;
    private final int amount;
    private final int size;

    public ActivePlayerDTO(String name, int amount, int size){
        this.name = name;
        this.amount = amount;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getSize() {
        return size;
    }
}

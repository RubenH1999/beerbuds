package be.thomasmore.drinkingbuds;

public class Beer {
    private String name;
    private double abv;

    public Beer() {
    }

    public Beer(String name, double abv) {
        this.name = name;
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public double getAbv() {
        return abv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }
}

package cs.vsu.ru.FuzzySystem.model;

public class Function {
    private String name;
    private int[] points;

    public Function() {
    }

    public Function(String name, int[] points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getPoints() {
        return this.points;
    }

    public void setPoints(int[] points) {
        this.points = points;
    }

}

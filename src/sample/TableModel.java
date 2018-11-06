package sample;

public class TableModel {
    private String name, person, country;
    private int count;

    public TableModel(String name, String person, int count, String country) {
        this.name = name;
        this.person = person;
        this.count = count;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getPerson() {
        return person;
    }

    public String getCountry() {
        return country;
    }

    public int getCount() {
        return count;
    }
}
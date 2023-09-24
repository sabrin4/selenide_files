package parsing;

import java.util.List;

public class Person {
    public String name;
    public Boolean hasJobOffer;
    public Integer age;
    public List<String> languages;
    Address address;

    public static class Address {
        public String city;
        public String street;
        public String building;
    }
}

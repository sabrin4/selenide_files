package parsing;

import java.util.List;

//{
//        "name": "Sergey",
//        "hasJobOffer": false,
//        "age": "35",
//        "languages": ["english", "russian"],
//        "address": {
//        "city": "Tarkov",
//        "street": "Chekannaya",
//        "building": 12
//        }
//        }
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

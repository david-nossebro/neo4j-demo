package org.demo;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by david on 2016-10-25.
 */
public class CarRandomizer {


    public static String randomModel() {
        int m = RandomUtils.nextInt(0, models.length);
        return models[m];
    }

    public static String randomRegNr() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<3; i++) {
            sb.append(randomLetter());
        }

        for(int i=0; i<3; i++) {
            sb.append(RandomUtils.nextInt(0, 10));
        }

        return randomRegNr();
    }

    private static char randomLetter() {
        return (char) (RandomUtils.nextInt(0, 26) + 'a');
    }

    private static String[] models = {
            "Alfa Romeo",
            "Aston Martin",
            "Audi",
            "Bentley",
            "Benz",
            "BMW",
            "Bugatti",
            "Cadillac",
            "Chevrolet",
            "Chrysler",
            "Citroen",
            "Corvette",
            "DAF",
            "Dacia",
            "Daewoo",
            "Daihatsu",
            "Datsun",
            "De Lorean",
            "Dino",
            "Dodge",
            "Farboud",
            "Ferrari",
            "Fiat",
            "Ford",
            "Honda",
            "Hummer",
            "Hyundai",
            "Jaguar",
            "Jeep",
            "KIA",
            "Koenigsegg",
            "Lada",
            "Lamborghini",
            "Lancia",
            "Land Rover",
            "Lexus",
            "Ligier",
            "Lincoln",
            "Lotus",
            "Martini",
            "Maserati",
            "Maybach",
            "Mazda",
            "McLaren",
            "Mercedes",
            "Mercedes-Benz",
            "Mini",
            "Mitsubishi",
            "Nissan",
            "Noble",
            "Opel",
            "Peugeot",
            "Pontiac",
            "Porsche",
            "Renault",
            "Rolls-Royce",
            "Rover",
            "Saab",
            "Seat",
            "Skoda",
            "Smart",
            "Spyker",
            "Subaru",
            "Suzuki",
            "Toyota",
            "Vauxhall",
            "Volkswagen",
            "Volvo"
    };
}

package jpa1;

import java.text.DecimalFormat;
import java.util.Random;

public class RandomValue {

    private final Random RND = new Random();

    public String randomDistrict() {
        String[] rDistrict = new String[]{"Soloma", "Dnipro", "Troja", "Pechersk", "Obolon"};
        return rDistrict[RND.nextInt(rDistrict.length)];
    }

    public String randomAddress() {
        String[] rAddress = new String[]{"pr. Peremogy", "provulok Politehnichny", "pl. Konstytucii", "vul. Sobornosti", "vul. Kyjivska"};
        return rAddress[RND.nextInt(rAddress.length)];
    }

    public double randomArea() {
        return RND.nextInt(25, 125);
    }

    public int randomRooms() {
        return RND.nextInt(1, 5);
    }

    public double randomPrice() {
        return RND.nextInt(25000, 150000);
    }
}

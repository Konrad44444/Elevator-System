package src.simulation;

import java.util.Random;

import src.person.Person;

public class Helper {
    private Helper() {};

    private static Random random = new Random();

    public static Person generatePerson(Integer floors) {
        Integer startFloor = Integer.valueOf(random.nextInt(floors));
        Integer endFloor = getRandomWithExclusion(random, 0, floors, startFloor);

        return new Person(startFloor, endFloor);
    }

    public static int getRandomWithExclusion(Random random, int start, int end, int... exclude) {
        int result = start + random.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (result < ex) {
                break;
            }
            result++;
        }
        return result;
    }
}

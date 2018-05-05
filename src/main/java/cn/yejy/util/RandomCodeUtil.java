package cn.yejy.util;


import java.util.Random;

public class RandomCodeUtil {

    public static String numberSequence(int length) {
        Random random = new Random();
        int[] sets = new int[length];
        for (int i = 0; i < length; i++) {
            sets[i] = random.nextInt(10);
        }
        return ArrayUtil.toString(sets);
    }
}

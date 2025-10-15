package net.narutoxboruto.client;

public class PlayerData {

    //INFO
    private static int chakra;
    public static int maxChakra;
    private static int shinobi_points;
   // private static String clan, affiliation, rank, releaseList;
   // private static boolean chakraControl, narutoRun;
   // //STATS
   // private static int taijutsu, ninjutsu, genjutsu, kenjutsu, kinjutsu, medical, senjutsu, shurikenjutsu, speed, summoning;
    //JUTSUS
   // private static String fireList, waterList, yangList, yinList, earthList, windList, lightningList;
   // private static String release, fireJutsu, waterJutsu, yangJutsu, yinJutsu, earthJutsu, windJutsu, lightningJutsu;
   // private static String selectedRelease;
   // private static ItemStack secondOffhandStack = ItemStack.EMPTY;

    public static int getChakra() {
        return chakra;
    }

    public static void setChakra(int chakra) {
        PlayerData.chakra = chakra;
    }

    public static int getMaxChakra() {
        return maxChakra;
    }

    public static int setMaxChakra(int maxChakra) {
        PlayerData.maxChakra = maxChakra;
        return maxChakra;
    }

}

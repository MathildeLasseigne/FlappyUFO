package modele;

public class Score {
    private static int highestScore = 0;

    public Score(){

    }

    /**
     * Si le score donne est plus grand que le plus haut score enregiste, alors le score actuel va etre remplace
     * @param score
     */
    public static void setScore(int score){
        if(score > highestScore){
            highestScore = score;
        }
    }

    /**
     * Renvoie le plus haut score enregistre
     * @return
     */
    public static int getHighScore(){
        return highestScore;
    }
}

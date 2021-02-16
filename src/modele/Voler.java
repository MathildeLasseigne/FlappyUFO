package modele;

public class Voler extends Thread {

    private Etat etat;

    /**
     * Thread permettant a l Ovale de descendre constament
     * </br> Fait appel a la fonction moveDown de Etat.
     * </br> A besoin d initialiser sont Etat avec setEtat()
     */
    public Voler(){

    }

    /**
     * Lie l etat
     * @param e
     */
    public void setEtat(Etat e){
        this.etat = e;
    }

    @Override
    public void run() {
        while(! etat.isEndGame()){
            etat.moveDown();
            //etat.aff.testEndGame();
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

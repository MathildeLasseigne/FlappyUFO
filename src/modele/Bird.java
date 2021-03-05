package modele;

import vue.VueBird;

import java.util.Random;

public class Bird extends Thread {

    private VueBird vue;

    /**Compte actuel de Bird**/
    private static int id = 0;
    /**Identifie le Bird par rapport aux autres**/
    private final int idb;

    private int delais = 20;
    private int etat;
    private int hauteur;
    private int position;
    private int saut = 5;
    /**Temps d attente entre 2 changements d etats**/
    private int bufferEtat;
    private int currentWait;

    /**
     * Vitesse maximale et minimale pour le random de delais. maxVit est le plus lent
     */
    private final int minVit = 25;
    private final int maxVit = 60;

    /**
     * Flag d arret de thread
     */
    private boolean stop = false;

    /**
     *Cree un oiseau avec une hauteur et vitesse (delais d actualisation) arbitraire
     * L oiseau est initialise a maxX+100 sur l axe X
     * @param maxHauteur La hauteur maximale que peux atteindre l oiseau
     * @param maxX La largeur max de la fenetre
     */
    public Bird(VueBird vue, int maxHauteur, int maxX){
        this.vue = vue;
        this.hauteur = rangedRandomInt(20, maxHauteur);
        //Vitesse
        this.delais = rangedRandomInt(this.minVit, this.maxVit);
        this.position = maxX + 100;

        this.etat = 1;
        id++;
        this.idb = id;

        //Initialisation du temps d attente
        //Plus le delais est eleve, plus le temps d attente est long
        int comp = this.maxVit - this.minVit;
        float perc = (this.delais-this.minVit)/comp;//On prend l etat d attente en fonction du % du delais par rapport au delais autorise
        int att = Math.round((perc*1000)*(this.delais)); //On multiplie perc pour ralentir encore les plus rapides
        //this.attente = att*10;
        this.bufferEtat = Math.round((this.maxVit-this.delais)/(this.maxVit - this.minVit)*4);
        this.currentWait = 0;
    }


    /**
     * Retourne la position de l oiseau sur l axe x
     * @return
     */
    public int getPosition(){
        return this.position;
    }

    /**
     * Retourne la position de l oiseau sur l axe y
     * @return
     */
    public int getHauteur(){
        return this.hauteur;
    }

    /**
     * Renvoie l etat actuel de l oiseeau
     * @return un int entre 1 et 8
     */
    public int getEtat(){
        return this.etat;
    }

    /**
     * Verifie si le run est termine
     * @return
     */
    public boolean isStopped(){
        return this.stop;
    }

    /**
     * Fait avancer l etat a la position suivante
     * </br> Plus la vitesse est rapide, plus les changements d etat sont lents
     */
    private void nextEtat(){
        if(this.currentWait == this.bufferEtat){
            this.currentWait = 0;
            if(this.etat == 8){
                this.etat = 1;
            } else {
                this.etat++;
            }
        } else {
            this.currentWait++;
        }
    }

    @Override
    public void run() {
        while(!this.stop){ //A revoir au cas ou on voit toujours l image dans la fenetre lors de sa disparition
            this.nextEtat();
            this.position = this.position-saut;
            //System.out.println("\n     *changement etat Bird*");
            if(this.position<-100){ //Force la sortie de fenetre
                //System.out.println("\n     *Bird "+idb+" is Stopped*");
                this.stop = true;
            }
            try {
                Thread.sleep(delais);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Cree un int random compris dans un certain intervalle [rangeMin, rangeMax]
     * @param rangeMin
     * @param rangeMax La borne maximale du random (doit etre positive !!)
     * @return la valeur du int
     */
    public static int rangedRandomInt(int rangeMin, int rangeMax) {
        Random r = new Random();
        int randomValue = rangeMin + r.nextInt(rangeMax - rangeMin);
        return randomValue;
    }
}

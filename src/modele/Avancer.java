package modele;

import vue.Affichage;

public class Avancer extends Thread {

    private Parcours parcours;
    private Affichage aff;

    public Avancer(Affichage a, Parcours p){
        this.aff = a;
        this.parcours = p;
    }

    @Override
    public void run() {
        while(! this.aff.getEtat().isEndGame()){
            parcours.moveParcours();
            /*if(parcours.isTuto()){
                aff.setScore(0);
                parcours.posEndTuto = parcours.getPosition();
            } else {
                aff.setScore(parcours.getPosition()-parcours.posEndTuto);
            }

             */
            aff.setScore(parcours.getSaut());


            //aff.setScore(parcours.getPosition());

            //System.out.println("New position = "+parcours.getPosition());
            aff.update();
            parcours.updateList();
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

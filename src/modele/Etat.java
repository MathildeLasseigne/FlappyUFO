package modele;

import vue.Affichage;

import java.awt.*;

public class Etat {

    public Affichage aff;
    private Voler fly;
    private Parcours parcours;

    private boolean finPartie;

    /**Position y de l oval**/
    private int hauteur;
    /**Taille d un saut en pixel**/
    private int saut;

    /**
     * Modele de l oval et initialise sa hauteur a 300
     */
    public Etat(Voler f) {
        this.saut = 40;
        this.hauteur = 50;
        this.fly = f;
        this.fly.setEtat(this);
        this.finPartie = false;


    }

    /**
     * Lie l affichage a la classe etat.
     * </br> Instancie la creation de Parcours
     * @param a
     */
    public void setAffichage(Affichage a){
        this.aff = a;
        this.parcours = new Parcours(this.aff.getLargeurMap(), this.aff.getHauteurMap());
    }

    /**
     * Renvoie le parcours lie a l etat
     * @return
     */
    public Parcours getParcours(){
        return this.parcours;
    }

    /**
     * Recupere la position y de l objet
     * @return
     */
    public int getHauteur() {
        return this.hauteur;
    }

    /**
     * Modifie la position y de l objet en le faisant monter
     */
    public void jump() {
        this.hauteur = this.hauteur-saut;
    }

    /**
     * Descend l ovale de quelques pixels et redessine l affichage
     */
    public void moveDown(){
        if((this.hauteur+this.aff.hauteurOvale) < this.aff.getHauteurMap()){
            int move = 4;
            this.hauteur = this.hauteur + move;
            this.aff.revalidate();
            this.aff.update();
        }

    }

    /**
     * Verifie si il y a une collision entre le segment [p1,p2] et l'ovale
     * @param p1 Le premier point relatif du segment
     * @param p2 Le deuxieme point relatif du segment
     * @return
     */
    public boolean testPerdu(Point p1, Point p2){
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        float y = p1.y - pente *(p1.x - this.aff.posDepart);
        if(y <= this.hauteur || y<= (this.hauteur+this.aff.hauteurOvale)){
            System.out.println("\n     *Test fin de jeu*");
            System.out.println("y p1 = "+p1.y+" / y p2 = "+p2.y);
            System.out.println("y = "+y+" / Hauteur ovale ="+this.hauteur+ " / Hauteur ovale bas ="+(this.hauteur+this.aff.hauteurOvale));
            return true;
        }
        return false;
    }



    /**
     *Definie la partie comme finie
     */
    public void endGame(){
        this.finPartie = true;
    }

    /**
     * Renvoie true si la partie est finie, false sinon
     * @return
     */
    public boolean isEndGame(){
        return this.finPartie;
    }
}

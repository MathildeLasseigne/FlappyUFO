package modele;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Parcours {

    /**
     * La liste des points de la courbe brisee
     */
    private ArrayList<Point> ptList = new ArrayList<>();
    /**
     * La taille maximale de la largeur de la fenetre
     */
    private final int maxX;
    /**
     * La taille maximale de la hauteur de la fenetre
     */
    private final int maxY;

    /**
     * La position active (absolue) sur l axe X
     */
    private int position;

    /**
     * Taille d un saut, nb de pixels dont la position se deplace lors d un mouvement
     */
    private int saut = 5;
    /**
     * La pente possible entre lancienne et la nouvelle donnee Y
     */
    private final int range = 150;

    /**
     * Position x du dernier point de la liste
     */
    private int lastX;

    /**
     * Empeche du relief d apparaitre tant que = true
     */
    private boolean tuto = true;


    private Instant lastTime;

    /**
     * Cree les points du parcours constituant les lignes brisees.
     * @param maxX La taille maximale de la largeur de la fenetre
     * @param maxY La taille maximale de la hauteur de la fenetre
     */
    public Parcours(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
        this.position = 0;
        int x = 5;
        int y = this.maxY-20;
        ptList.add(new Point(x, y));
        while(x < this.maxX){
            x = x + rangedRandomInt(40, 100);

            /* Implementation du tuto !! Donc tout a plat
            //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
            int newy = rangedRandomInt(y-range, y+range);
            while((newy > this.maxY) || (newy < 80)){
                newy = rangedRandomInt(y-range, y+range);
            }
            y = newy;

            if(y>this.maxY){
                System.out.println("y = "+y+" out of range "+maxY+" !!");
            }

             */

            this.lastX = x;
            ptList.add(new Point(x, y));
        }

       // System.out.println ("    *Liste des points de parcours :* \n");
        //for(Point elem: ptList)
        //{
        //    System.out.println (elem.toString()+" ;");
        //}
    }

    public void setTime(Instant t){
        this.lastTime = t;
    }


    /**
     * Renvoie la valeur de saut, nb de pixels dont la position avance a chaque appel de moveParcours()
     * @return
     */
    public int getSaut(){
        return this.saut;
    }

    /**
     * Permet l apparition de relief
     */
    public void endTuto(){
        this.tuto = false;

        //System.out.println("posEndTuto = "+posEndTuto);
    }

    /**
     * Renvoie true si le tuto est en cours
     * @return
     */
    public boolean isTuto(){
        return this.tuto;
    }

    public void updateList(){
        if(tuto){
            if((this.lastX - this.position)<=this.maxX){
                //System.out.println("Add point after : "+this.ptList.get(this.ptList.size()-1).toString());

                int x = this.lastX + rangedRandomInt(30, 100);
                int y = maxY-20;

                //System.out.println("Sortie de boucle");
                this.ptList.add(new Point(x,y));
                this.lastX = x;

            }
        } else {
            //System.out.println("LastX = "+this.lastX+" ?");
            if((this.lastX - this.position)<=this.maxX){
                //System.out.println("Add point after : "+this.ptList.get(this.ptList.size()-1).toString());
                int oldY = this.ptList.get(this.ptList.size()-1).y;
                //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
                int x = this.lastX + rangedRandomInt(30, 100);
                int y = rangedRandomInt(oldY-range, oldY+range);

                //Au cas ou y soit depasse les limites de la fenetre ou va trop haut
                while((y > this.maxY) || (y < 80)){ //On considere que l ovale peut passer avec 80 entre la montagne et le haut de la fenetre
                    y = rangedRandomInt(y-range, y+range);
                }
                //System.out.println("Sortie de boucle");
                this.ptList.add(new Point(x,y));
                this.lastX = x;

            }
        }

        if(this.ptList.size()>=2){ //Au cas ou la liste ne s actualise pas
            if((this.ptList.get(1).x-this.position)<0){
                //System.out.println("Remove point : "+this.ptList.get(0).toString());
                this.ptList.remove(0);
            }
        } else {
            System.out.println("Plus beaucoup de");
        }

        if(this.ptList.size()==0){
            System.out.println("Plus de points");
        } else if(this.ptList.size()==1){
            System.out.println("Un seul point : "+this.ptList.get(0).toString());
        }

    }


    /**
     * Cree un int random compris dans un certain intervalle [rangeMin, rangeMax]
     * @param rangeMin
     * @param rangeMax La borne maximale du random (doit etre positive !!)
     * @return la valeur du int
     */
    public int rangedRandomInt(int rangeMin, int rangeMax) {
        Random r = new Random();
        int randomValue = rangeMin + r.nextInt(rangeMax - rangeMin);
        return randomValue;
    }


    /**
     * Renvoie la liste des points de la ligne brisee, vue par rapport a la fenetre (position relative)
     * @return
     */
    public ArrayList<Point> getParcours(){
        int x = this.ptList.get(0).x;
        //ArrayList<Point> l = (ArrayList<Point>) this.ptList.clone();
        //for(Point elt : l){
        //    elt.move(elt.x - position, elt.y);
        //}
        ArrayList<Point> l = new ArrayList<>();
        ArrayList<Point> ptListClone = (ArrayList<Point>) this.ptList.clone(); //Pour eviter les problemes de co-ecriture
        for(Point elt : ptListClone){
            l.add(new Point(elt.x - position, elt.y));
        }
        if(x != this.ptList.get(0).x){
            System.out.println("           *Changement sur la liste principale !!!*");
            System.out.println("Ancien x0 = " + x + "Nouveau x0 = "+this.ptList.get(0).x);
        }
        return l;
    }

    /**
     * Met a jour le saut de deplacement en fonction du temps passe depuis le debut du jeu
     */
    private void updateSaut(){
        if(this.getSaut()<100){ // Le saut est au maximum a 100
            Instant now = Instant.now();
            Duration d = Duration.between( this.lastTime , now );
            if(d.toSeconds() >15){
                System.out.println("           *Plus rapide !!*");
                this.saut++;
                this.lastTime = now;
            }
        }

    }

    /**
     * Renvoie la position actuelle absolue (score du joueur)
     * @return
     */
    public int getPosition(){
        return this.position;
    }

    /**
     * Fait avancer la position d un nombre defini de pixels
     */
    public void moveParcours(){
        this.position = this.position + this.saut;
        this.updateSaut();
    }
}


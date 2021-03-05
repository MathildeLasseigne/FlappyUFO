package vue;

import controleur.Controleur;
import modele.Etat;
import modele.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Affichage extends JPanel {
    /**Modele de l oval**/
    private final Etat modele;

    private VueBird bird;

    //Hauteur et largeur de la fenetre
    private final static int largeur = 600;
    private final static int hauteur = 400;

    /**
     * Largeur de l'oval
     */
    public final int largeurOvale = 55;
    /**
     * Hauteur de l'oval
     */
    public final int hauteurOvale = 50;
    /**
     * Position de départ de l'ovale sur l'axe x
     */
    public final int posDepart = 60;

    /**
     * La position absolue
     */
    private int score;
    private float bonus;

    /**
     * Debut du jeu
     */
    public Instant timerStart;
    private boolean tuto = true;

    /**
     * Cree l affichage de la fenetre principale
     * Lie l affichage a l etat
     * @param ctrl Le controleur
     * @param etat Le modele
     */
    public Affichage(Controleur ctrl, Etat etat) {
        this.setPreferredSize(new Dimension(largeur, hauteur));
        this.modele = etat;
        this.modele.setAffichage(this);
        this.score =0;
        this.bonus = 0;

        this.addMouseListener(ctrl);
    }

    /**
     * Renvoie l etat lie a Affichage
     * @return
     */
    public Etat getEtat(){
        return this.modele;
    }

    /**
     * Initialise le timer au moment où la partie a debutee
     * @param time
     */
    public void setTimer(Instant time){
        this.timerStart = time;
    }

    /**
     * Lie la vueBird a l affichage
     * @param bird
     */
    public void setVueBird(VueBird bird){
        this.bird = bird;
    }


    /**
     * Recupere la hauteur de l'affichage sur l axe y
     * </br> ! Attention, le point le plus haut est 0 !
     * @return
     */
    public int getHauteurMap() {
        return Affichage.hauteur;
    }

    /**
     * Recupere la largeur de l affichage
     * @return
     */
    public int getLargeurMap() {
        return Affichage.largeur;
    }

    /**
     * Defini le score en lui ajoutant la valeur passee en parametre, plus le bonus
     * @param score La valeur a ajouter au score pour cet appel
     */
    public void setScore(int score){
        //this.score = score;
        if(!this.modele.getParcours().isTuto()){
            this.score = this.score + score + Math.round(score*(this.bonus/100));
        }
    }






    /**
     * Renvoie une Area representant la soucoupe
     * </br> Area implemente shape
     * @return
     */
    private Area getShapeOvale(){
        Area a1 = new Area(new Ellipse2D.Double(this.posDepart, modele.getHauteur(), this.largeurOvale, this.hauteurOvale));
        Area a2 = new Area(new Ellipse2D.Double(this.posDepart-(this.largeurOvale/6),
                modele.getHauteur()+(this.hauteurOvale*4/9),
                this.largeurOvale+(this.largeurOvale*2/6),this.hauteurOvale*2/8));
        a1.add(a2);
        return a1;
    }

    /**
     * Cree une etoile a la position de l ovale
     * @return
     */
    private GeneralPath getShapeStar(){
        double hauteurO = (double) this.modele.getHauteur();
        double yMid = hauteurO+(this.hauteurOvale/2);
        double xMid = this.posDepart+(this.largeurOvale/2);

        double points[][] = {
                { this.posDepart, yMid }, { this.posDepart+(this.largeurOvale/4), hauteurO+ (this.hauteurOvale/4)},
                { xMid, hauteurO }, { this.posDepart+((this.largeurOvale*3)/4), hauteurO+ (this.hauteurOvale/4) },
                { this.posDepart+this.largeurOvale, yMid }, { this.posDepart+((this.largeurOvale*3)/4), hauteurO+ (this.hauteurOvale*3/4) },
                { this.posDepart+this.largeurOvale, hauteurO+this.hauteurOvale }, { xMid, hauteurO+(this.hauteurOvale*3/4) },
                { this.posDepart,  hauteurO + this.hauteurOvale}, { this.posDepart+(this.largeurOvale/4) , hauteurO+ (this.hauteurOvale*3/4) },
                { this.posDepart, yMid }
        };

        GeneralPath star = new GeneralPath();
        star.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            star.lineTo(points[k][0], points[k][1]);
        star.closePath();
        return star;
    }

    /**
     * Dessine l objet principal (ici un ovale) en fonction des parametres donnes par le modele
     *  et des dimensions definies par l affichage
     * @param g
     */
    private void drawObject(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //g.drawOval(this.posDepart, modele.getHauteur(), this.largeurOvale, this.hauteurOvale);
        //g.fillOval(this.posDepart, modele.getHauteur(), this.largeurOvale, this.hauteurOvale);
        if(this.modele.isEndGame()){
            g2.setColor(new Color(139,0,0));
            g2.fill(this.getShapeStar());
        } else {
            //g2.setColor(new Color(219, 23, 2));
            g2.setColor(new Color(116, 208, 241));
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(0.5f));
            g2.fill(this.getShapeOvale());
            g2.setColor(Color.BLACK);
            g2.draw(this.getShapeOvale());
            Ellipse2D ring = new Ellipse2D.Double(this.posDepart-(this.largeurOvale/6),
                    modele.getHauteur()+(this.hauteurOvale*4/9),
                    this.largeurOvale+(this.largeurOvale*2/6),this.hauteurOvale*2/8);
            g2.setColor(new Color(219, 23, 2));
            g2.fill(ring);
            g2.setColor(Color.BLACK);
            g2.draw(ring);
            g2.setStroke(oldStroke);
        }


    }

    private Shape getBezierShapeParcours(){
        //Recuperer la liste des points :
        ArrayList<Point> list = this.modele.getParcours().getParcours();
        int[] tabX = new int[list.size()+2];//Pas besoin de fermer le polygone
        int[] tabY = new int[list.size()+2];
        tabX[0] = 0;
        tabY[0] = this.getHauteurMap();
        int length = list.size()+1;
        for(int i = 1; i<length; i++){
            tabX[i] = list.get(i-1).x;
            tabY[i] = list.get(i-1).y;

        }
        tabX[list.size()+1] = this.getLargeurMap();
        tabY[list.size()+1] = this.getHauteurMap();

        //Source : https://stackoverflow.com/questions/1257168/how-do-i-create-a-bezier-curve-to-represent-a-smoothed-polyline

        //Calcul des derivees des points
        double a = 2; //Coeff
        double[] tabX_ = new double[tabX.length];
        double[] tabY_ = new double[tabY.length];
        tabX_[0] = (tabX[1]-tabX[0])/a;
        tabY_[0] = (tabY[1]-tabY[0])/a;
        for(int i = 1; i<tabX_.length-1;i++){
            tabX_[i] = (tabX[i+1]-tabX[i-1])/a;
            tabY_[i] = (tabY[i+1]-tabY[i-1])/a;
        }
        tabX_[tabX_.length-1] = (tabX[tabX_.length-1]-tabX[tabX_.length-2])/a;
        tabY_[tabY_.length-1] = (tabY[tabY_.length-1]-tabY[tabY_.length-2])/a;

        //Calcul des points de Bezier avec 2 points de controle
        double[][] BezX = new double[tabX.length][2];
        double[][] BezY = new double[tabY.length][2];
        for(int i = 0; i<BezX.length-1; i++){
            BezX[i][0] = tabX[i] + tabX_[i]/3;
            BezX[i][1] = tabX[i+1] - tabX_[i+1]/3;
            BezY[i][0] = tabY[i] + tabY_[i]/3;
            BezY[i][1] = tabY[i+1] - tabY_[i+1]/3;

        }
       //Source : https://stackoverflow.com/questions/6368293/generalpath-curveto
        //Creation de la shape
        GeneralPath path = new GeneralPath();
        double xPointStart = tabX[0];
        double yPointStart = tabY[0];
        path.moveTo(xPointStart, yPointStart);
        for (int i = 1; i < list.size() - 1; i++) {
            double xPoint = tabX[i];
            double yPoint = tabY[i];
            double xControlPoint1 = BezX[i][0];
            double yControlPoint1 = BezY[i][0];
            double xControlPoint2 = BezX[i][1];
            double yControlPoint2 = BezY[i][1];
            path.curveTo(xControlPoint1, yControlPoint1, xControlPoint2, yControlPoint2, xPoint, yPoint);
            //((Graphics2D) g).draw(path);

        }
        path.lineTo(tabX[tabX.length-1], tabY[tabY.length-1]);
        path.lineTo(xPointStart, yPointStart);
        path.closePath();
        return path;
    }


    /**
     * Renvoie un polygon cree a partir des points relatifs de Parcours
     * et comprenant les 2 points inferieurs de la fenetre.
     * </br> Le polygone implemente Shape
     * @return
     */
    private Polygon getShapeParcours(){
        ArrayList<Point> list = this.modele.getParcours().getParcours();
        int[] tabX = new int[list.size()+2+1];//+1 Pour pouvoir relier au premier point
        int[] tabY = new int[list.size()+2+1];
        tabX[0] = 0;
        tabY[0] = this.getHauteurMap();
        int length = list.size()+1;
        for(int i = 1; i<length; i++){
            tabX[i] = list.get(i-1).x;
            tabY[i] = list.get(i-1).y;

        }
        tabX[list.size()+1] = this.getLargeurMap();
        tabY[list.size()+1] = this.getHauteurMap();
        tabX[list.size()+2] = tabX[0];//Fermer le polygon
        tabY[list.size()+2] = tabY[0];

        return new Polygon(tabX,tabY, list.size()+2+1);
    }


    /**
     * Dessine le parcours selon une liste de points donnes par la classe parcours
     * @param g
     */
    private void drawParcours(Graphics g){


        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(22, 184, 78));
        Shape parcours = this.getShapeParcours();
        g2.fill(parcours);  //Fill par poly

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(Color.BLACK);
        g2.draw(parcours);
        //g2.setColor(Color.BLACK);
        g2.setStroke(oldStroke);
        //g2.draw(this.getBezierShapeParcours());
        //g2.fill(this.getBezierShapeParcours());  //Fill par Bezier

        //Pour avoir la ligne brisee non remplie, decommenter les lignes suivantes et commenter les precedentes
        /*
        for(int i = 1; i<list.size(); i++){

            Point p = list.get(i-1);
            Point x = list.get(i);
            g.drawLine(p.x,p.y,x.x,x.y);
        }
         */

    }

    /**
     * Dessine l ecran de fin a l ecran
     */
    private void drawEndScreen(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        String str1 = "Fin de partie !";
        String str2 = "Score : "+this.score;
        String str3 = "Meilleur score : "+ Score.getHighScore();
        String str4 = "Appuyer sur [ENTRER] pour recommencer !";
        String res = "<html>"+str1+"<br>"+str2+"<br>"+str3+"<br>"+"<b>"+str4+"</b>"+"</html>";

        Font oldFont = g2.getFont();
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(str1, (this.getLargeurMap()/2)-75, (this.getHauteurMap()/2)-130);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(str2, (this.getLargeurMap()/2)-50, (this.getHauteurMap()/2)-75);
        g2.drawString(str3, (this.getLargeurMap()/2)-70, (this.getHauteurMap()/2)-25);
        g2.drawString(str4, (this.getLargeurMap()/2)-170, (this.getHauteurMap()/2)+45);
        /* Ne marche pas
        JLabel JendScreen = new JLabel(res, JLabel.CENTER);
        this.add(JendScreen);
        JendScreen.setVisible(true);
        Dimension size = JendScreen.getPreferredSize();

        this.Jscore.setBounds((this.getLargeurMap()/2)-(size.width/2), (this.getHauteurMap()/2)-(size.height/2), size.width, size.height);

         */

        g2.setFont(oldFont);

    }

    /**
     * Affiche les regles pendant 7 secondes et empeche du relief d'arriver
     * @param g
     */
    private void drawRegles(Graphics g){
        if(tuto){
            Instant now = Instant.now();
            Duration d = Duration.between( timerStart , now );
            Graphics2D g2 = (Graphics2D) g;
            Font oldFont = g2.getFont();
            if(d.toSeconds() < 4){
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                String str1 = "Appuyer sur la touche [ESPACE] pour faire monter l'UFO";
                String str2 = "Attention à ne pas toucher les montagnes !";
                g2.drawString(str1, 10, 40);
                g2.drawString(str2, 10, 70);


            } else if((d.toSeconds() >= 4) && (d.toSeconds() <= 6)){
                g2.setFont(new Font("Arial", Font.BOLD, 20+(int)(d.toSeconds()*2)));
                String str = "C'est partit !";
                g2.drawString(str, (this.getLargeurMap()/2)-80, (this.getHauteurMap()/2));
            } else if((d.toSeconds() >= 6) && (d.toSeconds() <= 8)) { //Enlever la cond si probleme
                this.modele.getParcours().endTuto();
                this.tuto = false;
            }
            g2.setFont(oldFont);
        }
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0,0, this.getLargeurMap(), this.getHauteurMap());
        super.paint(g);
        g.setColor(new Color(169, 234, 254));

        //g.fillRect(0,0,this.getLargeurMap()-120, this.getHauteurMap());
        //g.fillRect(this.getLargeurMap()-120,50,this.getLargeurMap(), this.getHauteurMap());
        g.fillRect(0,0, this.getLargeurMap(), this.getHauteurMap()); //Ciel
        if(!this.modele.isEndGame()){//En fond d ecran mais devant le ciel
            this.bird.dessiner(g);
        }
        drawParcours(g);
        drawObject(g);
        g.setColor(Color.BLACK);

        testEndGame();
        if(this.modele.isEndGame()){
            this.drawEndScreen(g);
        } else { //N affiche le score en petit que en cours de partie
            Font oldFont = g.getFont();
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score : "+String.valueOf(this.score), this.getLargeurMap()-110, 25);
            g.drawString("Bonus : "+String.valueOf(Math.round(this.bonus))+"%", this.getLargeurMap()-100, 45);
            g.setFont(oldFont);
            this.drawRegles(g);
            //this.bird.dessiner(g);
        }
    }

    /**
     * Calcul du pourcentage de bonus de score en fonction de la distance de l ovale du parcours
     * Plus l'ovale est pres de la courbe, plus le bonus est important
     * @return
     */
    private float getBonus(){
        if(this.modele.getHauteur()<=0){
            return 0;
        }
        //Choix de p1 & p2
        ArrayList<Point> list = this.modele.getParcours().getParcours();
        Point p1 = list.get(0);
        Point p2 = list.get(1);
        int i = 1;
        float posObjet = this.posDepart + (this.largeurOvale/2); //Milieu de l ovale
        while(posObjet>p2.x){
            p1 = list.get(i);
            p2 = list.get(i+1);
            i++;
        }
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        float y = p1.y - pente *(p1.x - posObjet);
        float heightAbove = y - (this.modele.getHauteur()+this.hauteurOvale);//Distance entre courbe et bas de l ovale
        float percentage = 100 - (heightAbove/y)*100;
        return percentage;
    }

    /**
     * Utilise les proprietes de shape et area pour verifier si il y a collision entre l ovale et le parcours.
     * </br>Avantage sur la methode de calcul sur point de segment : Plus precis, prend en compte le contour de l objet plutot que la box
     * @return
     */
    private boolean testEndGameWithShape(){
        Polygon parcours = this.getShapeParcours();
        Area ovale = this.getShapeOvale();
        if( ovale.getBounds().intersects(parcours.getBounds()) ){//Verifie dabord les boxes pour eviter trop de calcul
            Area a = new Area(ovale);
            a.intersect(new Area(parcours));
            if(!a.isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * Enregistre le score actuel dans la classe Score,
     * pour le proposer en tant que meilleur score
     */
    public void applyScore(){
        Score.setScore(this.score);
    }

    /**
     * Verifie si la partie est finie, et si oui affiche un message
     */
    public void testEndGame(){
        //Decommenter les lignes suivantes pour un test sur le segement
        /*
        //Choix de p1 & p2
        ArrayList<Point> list = this.modele.getParcours().getParcours();
        Point p1 = list.get(0);
        Point p2 = list.get(1);
        int i = 1;
        while(this.posDepart>p2.x){
            p1 = list.get(i);
            p2 = list.get(i+1);
            i++;
        }

        if(this.modele.testPerdu(p1, p2)){
        */
        if(this.testEndGameWithShape()){ //Ligne a mettre en commentaire si changement de test
            this.modele.endGame();
            this.applyScore();
            /* Decommenter les lignes suivantes pour afficher la fin du jeu sur un JOptionPane
            String str1 = "Fin de partie !";
            String str2 = "Score : "+this.modele.getParcours().getPosition();
            String str3 = "Meilleur score : "+ Score.getHighScore();
            JOptionPane.showMessageDialog(
                    null,
                    new JLabel("<html>"+str1+"<br>"+str2+"<br>"+str3+"</html>", JLabel.CENTER),
                    "Fin de partie",  JOptionPane.INFORMATION_MESSAGE);

             */
            this.repaint();
        }
    }

    /**
     * Met à jour l affichage
     */
    public void update() {
        this.repaint();
        this.bonus = this.getBonus();
        //System.out.println("Bird = "+ this.bird);
        if(! this.modele.isEndGame()){
            this.bird.updateBird();
        }

        //testEndGame();
    }



}
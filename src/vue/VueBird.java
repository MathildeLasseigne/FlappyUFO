package vue;

import modele.Bird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;

public class VueBird {

    Affichage aff;
    ArrayList<Bird> listBird = new ArrayList<>();

    BufferedImage bird1;
    BufferedImage bird2;
    BufferedImage bird3;
    BufferedImage bird4;
    BufferedImage bird5;
    BufferedImage bird6;
    BufferedImage bird7;
    BufferedImage bird8;

    /**
     *Affichage et gestion des oiseaux.
     *</br> Lance la thread des oiseaux par lui meme
     * @param aff
     */
    public VueBird(Affichage aff){
        this.aff = aff;
        this.aff.setVueBird(this);
        String path = "/ressources/extractBirdTrans/";
        try{
            bird1 = ImageIO.read(getClass().getResource(path+"animate-bird-1.png"));
            bird2 = ImageIO.read(getClass().getResource(path+"animate-bird-2.png"));
            bird3 = ImageIO.read(getClass().getResource(path+"animate-bird-3.png"));
            bird4 = ImageIO.read(getClass().getResource(path+"animate-bird-4.png"));
            bird5 = ImageIO.read(getClass().getResource(path+"animate-bird-5.png"));
            bird6 = ImageIO.read(getClass().getResource(path+"animate-bird-6.png"));
            bird7 = ImageIO.read(getClass().getResource(path+"animate-bird-7.png"));
            bird8 = ImageIO.read(getClass().getResource(path+"animate-bird-8.png"));
        }catch(IOException e){e.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
    }



    /**
     * Dessine l ensemble des oiseaux
     * @param g
     */
    public void dessiner(Graphics g){
        if(this.listBird.size() != 0){
            Graphics2D g2 = (Graphics2D) g;
            /*
            for(Bird elm : this.listBird){//Modification concurrente
                this.paintSingleBird(g2, elm);
            }

             */
            for(int i = 0; i<this.listBird.size(); i++){
                this.paintSingleBird(g2, this.listBird.get(i));
            }
        }
    }

    /**
     * Ajoute un nouvel oiseau dans la liste avec une certaine probabilite.
     * </br>Ne permet pas d avoir plus de 4 oiseaux a la fois
     */
    private void birdAdd(){
        //System.out.println("\nTry new Bird");
        if(this.listBird.size()<4){
            int rand = Bird.rangedRandomInt(0,200);
            if(rand <1){
                //System.out.println("\n     *Add Bird*");
                this.listBird.add(new Bird(this,aff.getHauteurMap()/6, aff.getLargeurMap()));
                this.listBird.get(this.listBird.size()-1).start();
                //System.out.println("\nNew Bird,  nb of birds = "+ this.listBird.size());
            }
        }
    }

    /**
     * Met a jour tout les oiseaux
     */
    public void updateBird(){
        if(this.listBird.size() != 0){
            for(int i = 0; i<this.listBird.size(); i++){
                if(this.listBird.get(i).isStopped()){//Ne doit interomptre que quand il n y a plus de run
                    //System.out.println("\n Try stop");
                    //this.listBird.get(i).interrupt();
                    //System.out.println("\n Interupt");
                    //int test = 1;
                    /*
                    while(! this.listBird.get(i).isInterrupted()){ //Au cas ou le thread soit en sleep

                        if(test < 5){
                            System.out.println("\n Boucle interrupt, i = "+test);
                        }

                        this.listBird.get(i).interrupt();
                        test++;
                    }

                     */
                    //this.listBird.get(i).stop();
                    //System.out.println("\n Try remove");
                    this.listBird.remove(i);
                    //System.out.println("Removed");
                }
            }
        }
        this.birdAdd();
    }

    /**
     * Paint un unique oiseau selon ses caracteristiques
     * @param g2
     * @param b
     */
    public void paintSingleBird(Graphics2D g2, Bird b){
        BufferedImage img = deepCopy(this.getImageEtat(b.getEtat()));
        img = this.scale(img, 0.3, 0.3);
        AffineTransform at = new AffineTransform();
        at.translate(b.getPosition(),b.getHauteur());
        //at.translate((int)x + radius/2.5,(int)y + radius/2.5);
        //at.rotate(Math.PI/2 + angle);
        //at.translate(-iconeNave.getWidth()/2, -iconeNave.getHeight()/2);
        g2.drawImage(img, at, null);
    }

    private BufferedImage getImageEtat(int etat){
        BufferedImage img = null;
        if(etat==1){
            img = this.bird1;
        } else if(etat==2){
            img = this.bird2;
        } else if(etat==3){
            img = this.bird3;
        } else if(etat==4){
            img = this.bird4;
        } else if(etat==5){
            img = this.bird5;
        } else if(etat==6){
            img = this.bird6;
        } else if(etat==7){
            img = this.bird7;
        } else if(etat==8){
            img = this.bird8;
        } else {
            img = this.bird1;
        }
            return img;
    }

    private BufferedImage scale(BufferedImage before, double scaleX, double scaleY){
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        //at.scale(2.0, 2.0);
        at.scale(scaleX, scaleY);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);
        return after;
    }

    /**
     * Cree une copie d un BufferedImage modifiable sans interferer avec l image source
     * @param bi BufferedImage source
     * @return
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}

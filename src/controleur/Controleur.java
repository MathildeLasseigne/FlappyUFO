package controleur;

import modele.Etat;
import vue.Affichage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controleur implements MouseListener {

    private Affichage vue;
    private final Etat modele;

    /**
     * Controleur, gere les changements d etat
     * </br>Doit etre lie a la vue avec <i>setVue(Affichage vue)</i> apres sa creation
     * </br><b>Optionnel :</b> L utilisation de la methode <i>enableKeyPad()</i> APRES la liaison de la vue permet l utilisation de la touche [Espace]
     *  comme autre moyen d interaction avec le joueur.
     * @param etat le modele
     */
    public Controleur(Etat etat){
        this.modele = etat;
    }

    /**
     * Lie la vue au controleur.
     * </br> Doit etre instancie lors de la creation du controleur
     * @param aff la vue principale
     */
    public void setVue(Affichage aff) {
        this.vue = aff;
    }

    /**
     * Permet l utilisation de la touche [Espace] du clavier pour sauter
     * </br> Doit etre instancie lors de la creation du controleur,
     *  <u>apres avoir lie la vue au controleur</u>
     */
    public void enableKeyPad() {
        vue.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "Space");
        //KeyEvent.KEY_LOCATION_RIGHT
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //System.out.print("Jump !! \n");
                jump();
            }
        };
        vue.getActionMap().put("Space", action);
    }

    /**
     * Action a faire lors de l interaction avec le joueur
     * </br>Fait monter l objet sur l axe y de la taille d un saut
     * </br>Prend en compte la collision avec le plafond
     */
    private void jump() {
        if(! this.modele.isEndGame()){
            if(modele.getHauteur() >= -10) { //On laisse une marge pour un meilleur effet visuel (pas effet plafond)
                this.modele.jump();
            }
            this.vue.update();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.jump();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }



}

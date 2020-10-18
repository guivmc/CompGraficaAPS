package com.company.input;

import com.company.cena.Cena;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class Mouse implements MouseListener {
    private Cena cena;
    private Integer previousX = null;

    public Mouse(Cena cena) {
        this.cena = cena;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(!Cena.pause) {
            int currentX = e.getX();

            if (previousX == null)
                previousX = currentX;

            if (currentX < previousX) {

                if(Cena.xRectTranslate + -.8f > -1.7f)
                    Cena.xRectTranslate += -0.05f;
                //System.out.println("esquerda");
            } else if (e.getX() > previousX) {
                if(Cena.xRectTranslate  < .5f)
                    Cena.xRectTranslate += 0.05f;
                //System.out.println("direita");
            } else
                System.out.println("parado");

            previousX = currentX;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {


    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }
}

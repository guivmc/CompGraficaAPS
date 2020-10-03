package com.company.input;

import com.company.cena.Cena;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class Mouse implements  MouseListener {
    private Cena cena;

    public Mouse(Cena cena){
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
        System.out.println( e.getX());
    }

    @Override
    public void mouseDragged(MouseEvent e) {


    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {

    }
}

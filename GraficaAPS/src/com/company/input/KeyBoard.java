package com.company.input;

import com.company.cena.Cena;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 *
 * @author Siabreu
 */
public class KeyBoard implements KeyListener{
    private Cena cena;
    
    public KeyBoard(Cena cena){
        this.cena = cena;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {        
        //System.out.println("Key pressed: " + e.getKeyCode());

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);

        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Cena.drawMenu = false;
        
        if(e.getKeyChar() == 'a' || e.getKeyCode() == 149) {
            System.out.println("esquerda");
            Cena.xRectTranslate += -0.1f;
        }
        else if(e.getKeyChar() == 'd' || e.getKeyCode() ==  151) {
            System.out.println("direita");
            Cena.xRectTranslate += 0.1f;
        }
        else
            Cena.xRectTranslate = 0;
    }

    @Override
    public void keyReleased(KeyEvent e) { }

}

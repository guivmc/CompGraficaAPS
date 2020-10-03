package com.company.cena;

import com.company.input.KeyBoard;
import com.company.input.Mouse;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;

/**
 *
 * @author Siabreu
 */
public class Renderer {
    private static GLWindow window = null;

    //Cria a janela de rendeziração do JOGL
    public static void init(){        
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);        
        window = GLWindow.create(caps);
        window.setFullscreen(true);
        //window.setSize(screenWidth, screenHeight);
        window.setResizable(false);
        
        Cena cena = new Cena();
        
        window.addGLEventListener(cena); //adiciona a Cena a Janela  
        //Habilita o teclado : cena
        window.addKeyListener(new KeyBoard(cena));
        window.addMouseListener(new Mouse(cena));
        
        //window.requestFocus();
        FPSAnimator animator = new FPSAnimator(window, 60);
        animator.start(); //inicia o loop de animação
        
        //encerrar a aplicacao adequadamente
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });       
        
        //window.setFullscreen(true);        
        window.setVisible(true);
    }

    public static void main(String[] args) {
        init();
    }
}

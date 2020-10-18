package com.company.cena;

import com.company.input.KeyBoard;
import com.company.input.Mouse;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

/**
 * @author Siabreu
 */
public class Renderer {
    private static GLWindow window = null;
    public static int screenWidth, screenHeight;

    //Cria a janela de rendeziração do JOGL
    public static void init() {
        GLProfile.initSingleton();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        window = GLWindow.create(caps);
        window.setSize(640, 480);
        window.setResizable(true);

        screenWidth = window.getWidth();
        screenHeight = window.getHeight();

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

        window.setFullscreen(true);
        window.setVisible(true);
    }

    //método para desenho de texto
    public static void desenhaTexto(int xPosicao, int yPosicao, Color cor, TextRenderer renderer,String frase) {
        //Retorna a largura e altura da janela
        renderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        renderer.setColor(cor);
        renderer.draw(frase, xPosicao, yPosicao);
        renderer.endRendering();
    }


    public static void main(String[] args) {
        init();
    }
}

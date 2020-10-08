package com.company.cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

/**
 * @author Siabreu
 */
public class Cena implements GLEventListener {

    public static final float DEG2RAD = (float) (3.14159 / 180.0);
    public static float xRectTranslate = 0;
    public static boolean drawMenu = true;


    private float xMin, xMax, yMin, yMax, zMin, zMax;
    GLU glu;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        glu = new GLU();

        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -1;
        xMax = yMax = zMax = 1;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade
        
        /*
            desenho da cena        
        *
        */

        gl.glColor3f(1, 1, 1); //cor branca

        //TODO: REMOVER
        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex2f(0, 0);
        gl.glEnd();

        if (drawMenu)
            DesenhaMenu();
        else {
            this.DesenhaRetangulo(gl);
            this.DesenhaBola(gl);
        }
        gl.glFlush();
    }

    private void DesenhaMenu() {
        TextRenderer title = new TextRenderer(new Font("Default", Font.PLAIN, 18));
        TextRenderer rules = new TextRenderer(new Font("Default", Font.PLAIN, 15));

        Renderer.desenhaTexto(120, 400, Color.BLUE, title, "Super Pong 2020 edition");

        Renderer.desenhaTexto(90, 360, Color.BLUE, rules, "Rules: -Use A/left arrow or D/right arrow ");
        Renderer.desenhaTexto(90, 340, Color.BLUE, rules, "or the mouse to control the paddle.");
        Renderer.desenhaTexto(90, 320, Color.BLUE, rules, "-Score 300 points to go to the next level.");
        Renderer.desenhaTexto(90, 300, Color.BLUE, rules, "-Lose 2 times and it is game over.");
        Renderer.desenhaTexto(90, 280, Color.BLUE, rules, "-Press enter to start, esc to exit.");

        Renderer.desenhaTexto(90, 200, Color.BLUE, rules, "Guilherme V. M. Carvalhal (21002514)");
    }

    private void DesenhaRetangulo(GL2 gl) {
        //desenha um retangulo
        gl.glPushMatrix();
        gl.glTranslatef(Cena.xRectTranslate, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-0.8f, -0.1f); //top left
        gl.glVertex2f(0.2f, -0.1f); //top right
        gl.glVertex2f(0.2f, -0.2f); //bottom right
        gl.glVertex2f(-0.8f, -0.2f); // bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void DesenhaBola(GL2 gl) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);

        for (int i = 0; i < 360; i++) {
            //convert degrees into radians
            float degInRad = i * Cena.DEG2RAD;
            gl.glVertex2f((float) Math.cos(degInRad) * 0.15f, (float) Math.sin(degInRad) * 0.15f);
        }

        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();

        //evita a divisão por zero
        if (height == 0) height = 1;
        //calcula a proporção da janela (aspect ratio) da nova janela
        float aspect = (float) width / height;

        //seta o viewport para abranger a janela inteira
        gl.glViewport(0, 0, width, height);

        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); //lê a matriz identidade

        //Projeção ortogonal
        //true:   aspect >= 1 configura a altura de -1 para 1 : com largura maior
        //false:  aspect < 1 configura a largura de -1 para 1 : com altura maior
        if (width >= height)
            gl.glOrtho(xMin * aspect, xMax * aspect, yMin, yMax, zMin, zMax);
        else
            gl.glOrtho(xMin, xMax, yMin / aspect, yMax / aspect, zMin, zMax);

        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); //lê a matriz identidade
        System.out.println("Reshape: " + width + ", " + height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}

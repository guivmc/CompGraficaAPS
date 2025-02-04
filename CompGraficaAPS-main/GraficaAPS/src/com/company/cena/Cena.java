package com.company.cena;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;

/**
 * @author Siabreu
 */
public class Cena implements GLEventListener {

    public static final float DEG2RAD = (float) (3.14159 / 180.0);
    public static float xRectTranslate = -0.25f;
    public static float xBallTranslate = -0.55f;
    public static float yBallTranslate = 0;
    public static boolean drawMenu = true;
    public static boolean pause = false;
    public static int lives = 5;
    public static int score = 0;
    private float ballSpeed = 0.005f;

    public static directions currentDirection = directions.leftUp;

    public enum directions {
        leftUp,
        leftDown,
        rightUp,
        rightDown,
    }

    private float xMin, xMax, yMin, yMax, zMin, zMax;
    public GLU glu;
    public int mode;
    public int tonalizacao = GL2.GL_SMOOTH;
    public float luzR = .2f, luzG = 0.2f, luzB = .2f;

    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();

        //Estabelece as coordenadas do SRU (Sistema de Referencia do Universo)
        xMin = yMin = zMin = -1;
        xMax = yMax = zMax = 1;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();
        //objeto para desenho 3D
        GLUT glut = new GLUT();

        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);
        //limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity(); //lê a matriz identidade

        iluminacao(gl);
        ligaLuz(gl);

        /*
            desenho da cena        
        *
        */


        if (drawMenu)
            DesenhaMenu();
        else {
            if (pause)
                this.DesenhaPause();

            this.DesenhaBoarda(gl);
            this.DesenhaHUD();
            this.drawFlower2(gl);
            this.DesenhaRetangulo(gl);
            this.DesenhaBola(gl);

            if(score >= 200)
            {
                this.ballSpeed = 0.009f;
                this.DesenhaObstaculo(gl);
                this.luzB = 1f;
                this.luzR = 1f;
            }

            if(lives <= 0)
                System.exit(0);

            if(!pause)
                this.UpdateBola();
        }
        gl.glFlush();
    }

    private void DesenhaPause() {
        TextRenderer pause = new TextRenderer(new Font("Default", Font.PLAIN, 18));
        Renderer.desenhaTexto(90, 360, Color.BLUE, pause, "PAUSE");
    }

    private void DesenhaBoarda(GL2 gl) {
        gl.glBegin(GL2.GL_LINES);

        gl.glVertex2f(.5f, -.3f); //right
        gl.glVertex2f(.5f, .8f); //right

        gl.glVertex2f(.5f, -.3f);//bottom
        gl.glVertex2f(-1.7f, -.3f);//bottom

        gl.glVertex2f(-1.7f, -.3f);//left
        gl.glVertex2f(-1.7f, .8f);//left

        gl.glVertex2f(-1.7f, .8f);//top
        gl.glVertex2f(.5f, .8f);//top

        gl.glEnd();
    }

    private void DesenhaMenu() {
        TextRenderer title = new TextRenderer(new Font("Default", Font.PLAIN, 18));
        TextRenderer rules = new TextRenderer(new Font("Default", Font.PLAIN, 15));

        Renderer.desenhaTexto(120, 400, Color.BLUE, title, "Super Pong 2020 edition");

        Renderer.desenhaTexto(90, 360, Color.BLUE, rules, "Rules: -Use A/left arrow or D/right arrow ");
        Renderer.desenhaTexto(90, 340, Color.BLUE, rules, "or the mouse to control the paddle.");
        Renderer.desenhaTexto(90, 320, Color.BLUE, rules, "-Score 200 points to go to the next level.");
        Renderer.desenhaTexto(90, 300, Color.BLUE, rules, "-Lose 5 times and it is game over.");
        Renderer.desenhaTexto(90, 280, Color.BLUE, rules, "-P to (un)pause.");
        Renderer.desenhaTexto(90, 260, Color.BLUE, rules, "-Press enter to start, esc to exit.");

        Renderer.desenhaTexto(90, 200, Color.BLUE, rules, "Guilherme V. M. Carvalhal (21002514)");
    }

    private void DesenhaHUD() {
        TextRenderer HUD = new TextRenderer(new Font("Default", Font.PLAIN, 15));

        Renderer.desenhaTexto(10, 450, Color.WHITE, HUD, "Score: " + score);
        Renderer.desenhaTexto(400, 450, Color.WHITE, HUD, "" + lives);
    }

    private void DesenhaRetangulo(GL2 gl) {
        //desenha um retangulo
        gl.glPushMatrix();
        gl.glColor3f(0, 1, 1); //cor branca
        gl.glTranslatef(Cena.xRectTranslate, 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(-0.8f, -0.1f); //top left
        gl.glVertex2f(0.0f, -0.1f); //top right
        gl.glVertex2f(0.0f, -0.2f); //bottom right
        gl.glVertex2f(-0.8f, -0.2f); //bottom left
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void DesenhaObstaculo(GL2 gl)
    {
        gl.glBegin(GL2.GL_LINES);
        //gl.glColor3f(0, 1, 0); //cor branca
        gl.glVertex2f(-.55f, .3f); //right
        gl.glVertex2f(-.55f, .8f); //right
        gl.glEnd();
    }

    private void DesenhaBola(GL2 gl) {

        gl.glPushMatrix();
        gl.glColor3f(1, 1, 1); //cor branca
        gl.glTranslatef(Cena.xBallTranslate, Cena.yBallTranslate, 0);
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        for (int i = 0; i < 360; i++) {
            //convert degrees into radians
            float degInRad = i * Cena.DEG2RAD;
            gl.glVertex2f((float) Math.cos(degInRad) * 0.05f, (float) Math.sin(degInRad) * 0.05f);
        }

        gl.glEnd();
        gl.glPopMatrix();
    }

    public void drawFlower2(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0.35f, 0.9f, 0);
        gl.glColor3f(1, 0, 0); //cor branca
        gl.glBegin(GL2.GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            //convert degrees into radians
            float degInRad = i * DEG2RAD;

            float radius = (float) Math.sin(degInRad * 4);

            gl.glVertex2f((float) Math.cos(degInRad) * radius * 0.05f, (float) Math.sin(degInRad) * radius * 0.05f);

        }

        gl.glEnd();
        gl.glPopMatrix();
    }

    private void UpdateBola() {

        //region collision
        //if hit left border
        if (xBallTranslate + 0.05f < -1.6f) {
            if (Cena.currentDirection == directions.leftUp)
                Cena.currentDirection = directions.rightUp;
            else if (Cena.currentDirection == directions.leftDown)
                Cena.currentDirection = directions.rightDown;
        }
        //if hit right border
        else if (xBallTranslate + 0.05f > .5f) {
            if (Cena.currentDirection == directions.rightUp)
                Cena.currentDirection = directions.leftUp;
            else if (Cena.currentDirection == directions.rightDown)
                Cena.currentDirection = directions.leftDown;
        }
        //if hit top border
        else if (yBallTranslate + 0.05f > .8f) {
            if (Cena.currentDirection == directions.leftUp)
                Cena.currentDirection = directions.leftDown;
            else if (Cena.currentDirection == directions.rightUp)
                Cena.currentDirection = directions.rightDown;
        }
        //if hit paddle
        else if (yBallTranslate + 0.05f < -0.1f && (xBallTranslate > -.8f + Cena.xRectTranslate && (xBallTranslate <  Cena.xRectTranslate))) {
            if (Cena.currentDirection == directions.leftDown)
                Cena.currentDirection = directions.leftUp;
            else if (Cena.currentDirection == directions.rightDown)
                Cena.currentDirection = directions.rightUp;

            score += 100;
        } else if (yBallTranslate + 0.05f < -0.3f) {
            lives--;
           resetPos();
        }
        //if hit level 2
        else if(score >= 200 && ((yBallTranslate > .3f && yBallTranslate < .8f) && xBallTranslate + 0.05f >= -.6f && xBallTranslate + 0.05f <= -.4f)) //&& xBallTranslate + 0.05f >= -.53f && xBallTranslate + 0.05f <= -.58f))
        {

            if(Cena.currentDirection == directions.leftDown)
                Cena.currentDirection = directions.rightDown;
            else if(Cena.currentDirection == directions.rightDown)
                Cena.currentDirection = directions.leftDown;
            else if(Cena.currentDirection == directions.rightUp)
                Cena.currentDirection = directions.leftUp;
            else if(Cena.currentDirection == directions.leftUp)
                Cena.currentDirection = directions.rightUp;
        }

        //endregion

        switch (Cena.currentDirection) {
            case leftUp:
                Cena.xBallTranslate += -ballSpeed;
                Cena.yBallTranslate += +ballSpeed;
                break;
            case leftDown:
                Cena.xBallTranslate += -ballSpeed;
                Cena.yBallTranslate += -ballSpeed;
                break;
            case rightUp:
                Cena.xBallTranslate += +ballSpeed;
                Cena.yBallTranslate += +ballSpeed;
                break;
            case rightDown:
                Cena.xBallTranslate += +ballSpeed;
                Cena.yBallTranslate += -ballSpeed;
                break;
        }
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

    private static void resetPos()
    {
        xRectTranslate = -0.25f;
        xBallTranslate = -0.55f;
        yBallTranslate = 0;
        currentDirection = directions.leftUp;
    }

    public void iluminacao(GL2 gl) {
        float luzAmbiente[] = new float[4];
        luzAmbiente[0] = luzR;
        luzAmbiente[1] = luzG;
        luzAmbiente[2] = luzB;
        luzAmbiente[3] = 1.0f;

        float luzDifusa[] = {0.7f, 0.7f, 0.7f, 1.0f};
        float luzEspecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float posicaoLuz[] = {0.0f, 0.0f, 0.0f, 0.0f};

        // capacidade de brilho do material
        int especMaterial = 60;

        // define a concentra��o do brilho
        gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, especMaterial);

        // ativa o uso da luz ambiente
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, luzAmbiente, 0);

        // define os par�metros de luz de n�mero 0 (zero)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }

    public void ligaLuz(GL2 gl) {
        // habilita a defini��o da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // habilita o uso da ilumina��o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de n�mero 0
        gl.glEnable(GL2.GL_LIGHT0);

        /*
         * Especifica o Modelo de tonaliza��o a ser utilizado GL_FLAT -> modelo de
         * tonaliza��o flat GL_SMOOTH -> modelo de tonaliza��o GOURAUD (default)
         */
        gl.glShadeModel(tonalizacao);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}

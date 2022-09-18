/*******************************************************************************
 *                                Puyo.java                                    *
 *                           (C)César Himura Elric                                *
 *******************************************************************************/

package gameloft.examen;
        
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Font;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

import java.net.URL;

public class Puyo extends Canvas implements Stage, KeyListener
{
    private BufferStrategy strategy;
    
    private long elapsedTime;
    
    private Sprite sprite;
    
    private ArrayList characters;
    
    private int rows = 13;
    private int columns = 7;
    
    private boolean gameOver = false;
    
    private Sphere sphere1 = null;
    private Sphere sphere2 = null;
    
    private Sphere[][] red;
    
    private Floor floor;
    
    public Puyo() 
    {
       sprite = new Sprite();
        
       JFrame frame = new JFrame("Puyo - Himura César");
       
       JPanel panel = (JPanel)frame.getContentPane();
       setBounds(0, 0, Stage.WIDTH_WINDOW, Stage.HEIGHT_WINDOW);
       panel.setPreferredSize(new Dimension(Stage.WIDTH_WINDOW, Stage.HEIGHT_WINDOW));
       panel.setLayout(null);
       panel.add(this);

       CenterWindow cw = new CenterWindow(Stage.WIDTH_WINDOW, Stage.HEIGHT_WINDOW);
       
       frame.setBounds(cw.getWindowX(), cw.getWindowY(), Stage.WIDTH_WINDOW, Stage.HEIGHT_WINDOW);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true); 
       frame.setResizable(false);
       
       createBufferStrategy(2);
       strategy = getBufferStrategy();
       
       requestFocus();
       
       addKeyListener(this);
	
       setIgnoreRepaint(true);
    
       /*BufferedImage cursor = sprite.createCompatible(10,10,Transparency.BITMASK);
       Toolkit t = Toolkit.getDefaultToolkit();
       Cursor c = t.createCustomCursor(cursor,new Point(5,5),"null");
       setCursor(c);*/
    }

    public void keyPressed(KeyEvent evt)
    {
        if(evt.getKeyCode() == KeyEvent.VK_SPACE)
            sphere1.keyPress(evt);
        else
        {
            sphere1.keyPress(evt);
            sphere2.keyPress(evt);
        }
    }
    
    public void keyReleased(KeyEvent evt)
    {}
    
    public void keyTyped(KeyEvent evt)
    {}
    
    private void checkCollisions()
    {
        Character fc = (Character)characters.get(0);
        Rectangle r0 = fc.getBounds();
        for(int i = 1; i < characters.size(); i++)
        {
            Character c1 = (Character)characters.get(i);
            
            Rectangle r1 = c1.getBounds();
            
            for(int j = i + 1; j < characters.size(); j++)
            {
                Character c2 = (Character)characters.get(j);
                
                Rectangle r2 = c2.getBounds();
                
                if(r1.intersects(r2))
                {                    
                    Sphere sphereA = (Sphere)c1;
                    Sphere sphereB = (Sphere)c2;                    
                    
                    boolean b = false;
                    if(sphereB.getVy() > 0)
                    {
                        b = true;
                    }
                    
                    c1.collision(c2);
                    c2.collision(c1);
                    
                    if(sphereA.getY() < sphereB.getY())
                    {
                        if(sphereA.getX() == sphereB.getX())
                        {
                            sphereA.setY(sphereA.getY() - 2);                    
                            sphereB.setY(sphereB.getY() - 1);
                        }
                        else
                        {
                            sphereA.setY(sphereA.getY() - 1);
                            sphereB.setY(sphereB.getY() - 1);
                        }
                    }
                    else
                        if(sphereA.getY() > sphereB.getY())
                        {
                            if(sphereA.getX() == sphereB.getX())
                            {
                                sphereA.setY(sphereA.getY() - 1);                    
                                sphereB.setY(sphereB.getY() - 2);
                            }
                            else
                            {
                                sphereA.setY(sphereA.getY() - 1);                    
                                sphereB.setY(sphereB.getY() - 1);
                            }
                        }
                        else
                        {
                            sphereA.setY(sphereA.getY() - 1);                    
                            sphereB.setY(sphereB.getY() - 1);
                        }
                    
                    if(b)
                        sphereA = sphereB;
                    
                    //sphereA.setY(sphereA.getY() - 1);
                    
                    int row = (int)((sphereA.getY() - 13) / sphereA.getHeight());
                    int column = (int)((sphereA.getX() - 4) / sphereA.getWidth());
                    
                    red[row][column] = sphereA;
                    
                    checkCombos(row, column);
                }//collision
                else
                {
                    if(r1.intersects(r0))
                    { 
                        c1.collision(fc);
                        //System.out.println(".:COLLISION FLOOR:.");
                        
                        Sphere sphereA = (Sphere)c1;
                       
                        sphereA.setY(sphereA.getY() - 1);     
                        
                        int row = (int)((sphereA.getY() - 13) / sphereA.getHeight());
                        int column = (int)((sphereA.getX() - 4) / sphereA.getWidth());
                        
                        red[row][column] = sphereA;
                        
                        checkCombos(row, column);
                        
                    }//if collision(inrtersect) spheres with floor
                    if(r2.intersects(r0))
                    {
                        c2.collision(fc);
                        //System.out.println(".:COLLISION FLOOR II:.");
                        
                        Sphere sphereB = (Sphere)c2;
                        
                        sphereB.setY(sphereB.getY() - 1);
                        
                        int row = (int)((sphereB.getY() - 13) / sphereB.getHeight());
                        int column = (int)((sphereB.getX() - 4) / sphereB.getWidth());

                        red[row][column] = sphereB;
                        
                        checkCombos(row, column);
                    }
                }//else collision
            }//for i+1
        }
    }
    
    private void checkCombos(int row, int column)
    {
        int rAux = row;
        int cAux = column;
        int paso = 0;
        int giro = 0;
        int countSpheres = 1;
        int girosTotales = 7;
        
        char direction = 'N';
        
        red[row][column].setCheck(true);
        
        while(paso < 4)
        {
            switch(direction)
            {
                case 'N': try
                          {
                                if(red[rAux - 1][cAux] != null)
                                {
                                    if(red[rAux - 1][cAux].getIDColor() == red[row][column].getIDColor())
                                    {
                                        if(!red[rAux - 1][cAux].getCheck())
                                        {
                                            rAux--;
                                            giro = 0;
                                            red[rAux][cAux].setCheck(true);
                                            countSpheres++;
                                        }
                                        else
                                        {
                                            if(giro < girosTotales)
                                            {
                                                giro++;
                                                direction = 'E';
                                            }
                                            else
                                            {
                                                giro = 0;
                                                rAux--;
                                                paso += (row == rAux && cAux == column) ? 1 : 0;
                                            }
                                            if(row == rAux && cAux == column && paso == 0)
                                            {
                                                paso++;
                                            }
                                        }
                                    }//if color
                                    else
                                    {
                                        direction = 'E';
                                        giro++;
                                        if(rAux == row && cAux == column && paso == 0)
                                        {
                                            paso++;
                                        }
                                    }
                                }//null
                                else
                                {
                                    direction = 'E';
                                    giro++;
                                    if(rAux == row && cAux == column && paso == 0)
                                    {
                                        paso++;
                                    }
                                }
                          }
                          catch(ArrayIndexOutOfBoundsException ex)
                          {
                              direction = 'E';
                              giro++;
                              if(rAux == row && cAux == column && paso == 0)
                              {
                                    paso++;
                              }
                          }
                          break;
                case 'E': try
                          {
                                if(red[rAux][cAux + 1] != null)
                                {
                                    if(red[rAux][cAux + 1].getIDColor() == red[row][column].getIDColor())
                                    {
                                        if(!red[rAux][cAux + 1].getCheck())
                                        {
                                            cAux++;
                                            giro = 0;
                                            red[rAux][cAux].setCheck(true);
                                            
                                            countSpheres++;
                                        }
                                        else
                                        {
                                            if(giro < girosTotales)
                                            {
                                                giro++;
                                                direction = 'S';
                                            }
                                            else
                                            {
                                                giro = 0;
                                                cAux++;
                                                paso += (row == rAux && cAux == column) ? 1 : 0;
                                            }
                                            if(row == rAux && cAux == column && paso == 1)
                                            {
                                                paso++;
                                            }
                                        }
                                    }//if color
                                    else
                                    {
                                        direction = 'S';
                                        giro++;
                                        if(rAux == row && cAux == column && paso == 1)
                                        {
                                            paso++;
                                        }
                                    }
                                }//null
                                else
                                {
                                    direction = 'S';
                                    giro++;
                                    if(rAux == row && cAux == column && paso == 1)
                                    {
                                        paso++;
                                    }
                                }
                          }
                          catch(ArrayIndexOutOfBoundsException ex)
                          {
                              direction = 'S';
                              giro++;
                              if(rAux == row && cAux == column && paso == 1)
                              {
                                    paso++;
                              }
                          }
                          break;
                case 'S': try
                          {
                                if(red[rAux + 1][cAux] != null)
                                {
                                    if(red[rAux + 1][cAux].getIDColor() == red[row][column].getIDColor())
                                    {
                                        if(!red[rAux + 1][cAux].getCheck())
                                        {
                                            rAux++;
                                            giro = 0;
                                            red[rAux][cAux].setCheck(true);
                                            countSpheres++;
                                        }
                                        else
                                        {
                                            if(giro < girosTotales)
                                            {
                                                giro++;
                                                direction = 'O';
                                            }
                                            else
                                            {
                                                giro = 0;
                                                rAux++;
                                                paso += (row == rAux && cAux == column) ? 1 : 0;
                                            }
                                            if(row == rAux && cAux == column && paso == 2)
                                            {
                                                paso++;
                                            }
                                        }
                                    }//if color
                                    else
                                    {
                                        direction = 'O';
                                        giro++;
                                        if(rAux == row && cAux == column && paso == 2)
                                        {
                                            paso++;
                                        }
                                    }
                                }//null
                                else
                                {
                                    direction = 'O';
                                    giro++;
                                    if(rAux == row && cAux == column && paso == 2)
                                    {
                                        paso++;
                                    }
                                }
                          }
                          catch(ArrayIndexOutOfBoundsException ex)
                          {
                              direction = 'O';
                              giro++;
                              if(rAux == row && cAux == column && paso == 2)
                              {
                                    paso++;
                              }
                          }
                          break;
                 case 'O': try
                          {
                                if(red[rAux][cAux - 1] != null)
                                {
                                    if(red[rAux][cAux - 1].getIDColor() == red[row][column].getIDColor())
                                    {
                                        if(!red[rAux][cAux - 1].getCheck())
                                        {
                                            cAux--;
                                            giro = 0;
                                            red[rAux][cAux].setCheck(true);
                                            
                                            countSpheres++;
                                        }
                                        else
                                        {
                                            if(giro < girosTotales)
                                            {
                                                giro++;
                                                direction = 'N';
                                            }
                                            else
                                            {
                                                giro = 0;
                                                cAux--;
                                                paso += (row == rAux && cAux == column) ? 1 : 0;
                                            }
                                            if(row == rAux && cAux == column && paso == 3)
                                            {
                                                paso++;
                                            }
                                        }
                                    }//if color
                                    else
                                    {
                                        direction = 'N';
                                        giro++;
                                        if(rAux == row && cAux == column && paso == 3)
                                        {
                                            paso++;
                                        }
                                    }
                                }//null
                                else
                                {
                                    direction = 'N';
                                    giro++;
                                    if(rAux == row && cAux == column && paso == 3)
                                    {
                                        paso++;
                                    }
                                }
                          }
                          catch(ArrayIndexOutOfBoundsException ex)
                          {
                              direction = 'N';
                              giro++;
                              if(rAux == row && cAux == column && paso == 3)
                              {
                                    paso++;
                              }
                          }
                          break;
            }//switch
        }//while
        
        if(countSpheres >= 4)
        {
            deleteSpheres();
        }else
        {
            cleanSpheres();
        }
    }
    
    private void deleteSpheres()
    {
        //***** DELETE *****
        for(int f = 0; f < rows; f++)
        {
            for(int c = 0; c < columns; c++)
            {
                if(red[f][c] != null)
                    if(red[f][c].getCheck())
                    {
                        red[f][c].setMarkedToRemove(true);
                        red[f][c].setVx(0);
                        red[f][c].setVy(0);
                        
                        for(int index = 1; index < characters.size(); index++)
                        {
                            Sphere s = (Sphere)characters.get(index);
                            if(s == red[f][c])
                            {
                                characters.remove(index);
                            }
                        }
                        
                        red[f][c].remove();
                        red[f][c] = null;
                        
                        fallSpheres(f, c);
                    }
            }
        }
    }
    
    private void fallSpheres(int row, int column)
    {
        row--;
        try
        {
            while(red[row][column] != null)
            {
                red[row][column].setCollision(false);
                red[row][column].setVy(1);
                row--;
            }
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            
        }
    }
    
    private void cleanSpheres()
    {
        //***** CLEAN THE CHECK *****
        for(int f = 0; f < rows; f++)
        {
            for(int c = 0; c < columns; c++)
            {
                if(red[f][c] != null)
                {
                    red[f][c].setCheck(false);
                }
            }
        }
    }
    
    private void initRed()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                red[i][j] = null;
            }
        }
    }
    
    private void paintGameOver()
    {
        Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("GAME", Stage.WIDTH_WINDOW / 2 - 50, Stage.HEIGHT_WINDOW / 2 - 30);
        g.drawString("OVER", Stage.WIDTH_WINDOW / 2 - 50, Stage.HEIGHT_WINDOW / 2 );
        strategy.show();
    }
    
    private void paintGame()
    {
        Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        
        for(int i = 0; i < characters.size(); i++)
        {
          Character c = (Character)characters.get(i);
          c.paint(g);
        }
    
        /*g.setColor(Color.BLACK);
        if(elapsedTime > 0)
          g.drawString(String.valueOf(1000/elapsedTime)+" fps",0,Stage.HEIGHT_WINDOW - 50);
        else
          g.drawString("--- fps",0,Stage.HEIGHT_WINDOW - 50);*/
        strategy.show();
    }
    
    private void updateGame()
    {
        checkAreas();
        
        boolean gen = false;
        for(int index = 1; index < characters.size(); index++)
        {
            Sphere s = (Sphere)characters.get(index);
            if(s.getVy() == 0)
            {
                gen = true;
            }
            else
            {
                gen = false;
                break;
            }
        }
        if(gen || characters.size() == 1)
            generateSpheres();
        
        if(sphere1.getVy() == 0 || sphere2.getVy() == 0)
        {
            sphere1.setMove(false);
            sphere2.setMove(false);
        }
        
        for(int index = 0; index < characters.size(); index++)
        {
            Character c = (Character)characters.get(index); 
            
            if(c.getY() < 0)
                gameOver = true;
            
            if(c.isMarkedToRemove())
            {
                characters.remove(index);
                characters.trimToSize();
            }
            else
            {
                c.action();
            }
        }
    }
    
    private void initGame()
    {
        characters = new ArrayList();
        
        floor = new Floor(this);
        floor.setX(0);
        floor.setY(Stage.HEIGHT_WINDOW - 35);
        
        characters.add(floor);
        
        red = new Sphere[rows][columns];
        
        initRed();
        
        generateSpheres();
    }
    
    private void game()
    {
        elapsedTime = 1000;
        initGame();
        while(isVisible() && !gameOver)
        {
            long startTime = System.currentTimeMillis();
            updateGame();
            
            checkCollisions();
            
            paintGame();
            
            elapsedTime = System.currentTimeMillis() - startTime;
            /*do{
                Thread.yield();
            }while (System.currentTimeMillis()-startTime< 17);*/

            try
            {
                Thread.sleep(Stage.SPEED);
            }
            catch(InterruptedException ex)
            {
                
            }
        }
        
        paintGameOver();
    }
    
    private void generateSpheres()
    {
         for(int index = 0; index < 2; index++)
        {
            Sphere sphere = new Sphere(this);
            sphere.setX(Stage.WIDTH_WINDOW / 2 - 16);
            sphere.setY(index * 32 + 15);
            
            characters.add(sphere);
        }
         
        sphere1 = (Sphere)characters.get(characters.size() - 2);
        sphere2 = (Sphere)characters.get(characters.size() - 1);
    }
    
    private void checkAreas()
    {
        for(int index = 1; index < characters.size(); index++)
        {
            int ci = 0;
            int cd = 0;
            int ra = 0;
            
            if(sphere1.getX() == sphere2.getX())
            {
                ci = 1;
                cd = 1;
                ra = (sphere1.getY() < sphere2.getY()) ? sphere1.getHeight() * 2: sphere1.getHeight();
            }
            else
            {
                if(sphere1.getX() < sphere2.getX())
                {
                    ci = 1;
                    cd = 2;
                    ra = sphere1.getHeight();
                }
                else
                {
                    if(sphere1.getX() > sphere2.getX())
                    {
                        ci = 2;
                        cd = 1;
                        ra = sphere1.getHeight();
                    }
                }
            }
            
            Sphere s = (Sphere)characters.get(index);
            
            int xs = (int)((s.getX() - 4) / s.getWidth());
            int x = (int)((sphere1.getX() - 4) / sphere1.getWidth());
            
            if(x - ci >= 0)
            {
                if(s.getVy() == 0 && xs == x-ci)
                {
                    if((sphere1.getY() + ra) > s.getY())
                    {
                        sphere1.setCanMoveLeft(false);
                        sphere2.setCanMoveLeft(false);
                        break;
                    }
                }
                else
                {
                    sphere1.setCanMoveLeft(true);
                    sphere2.setCanMoveLeft(true);
                }
            }//if ci
            else
            {
                sphere1.setCanMoveLeft(false);
                sphere2.setCanMoveLeft(false);
            }
            if(x+cd <= 6)
            {
                if(s.getVy() == 0 && xs == x + cd)
                {
                    if(sphere1.getY() + ra > s.getY())
                    {
                        sphere1.setCanMoveRight(false);
                        sphere2.setCanMoveRight(false);
                        break;
                    }
                }
                else
                {
                    sphere1.setCanMoveRight(true);
                    sphere2.setCanMoveRight(true);
                }
            }//if cd
            else
            {
                sphere1.setCanMoveRight(false);
                sphere2.setCanMoveRight(false);
            }
        }
    }
    
    public Sprite getSprite()
    {
        return sprite;
    }

    public static void main(String[] args)
    {
        Puyo puyo = new Puyo();
        
        puyo.game();
    }
}
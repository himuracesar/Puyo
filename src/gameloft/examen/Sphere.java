/*******************************************************************************
 *                                Puyo.java                                    *
 *                          (C)César Himura Elric                              *
 *******************************************************************************/

package gameloft.examen;

import java.awt.event.KeyEvent;

public class Sphere extends Character
{
    private int vX = 0;
    private int vY = 1;
    private int color = -1;
    
    private char rs = 'N';
    
    private boolean canMove = true;
    //*****
    private boolean canMoveLeft = true;
    private boolean canMoveRight = true;
    //*****
    private boolean collision = false;
    private boolean check = false;
    
    public Sphere(Stage stage)
    {
        super(stage);
        
        String spriteName = "";
        
        color = (int)(Math.random() * 4);
        
        switch(color)
        {
            case 0: spriteName = "puyo_blue.gif";
                    break;
            case 1: spriteName = "puyo_green.gif";
                    break;
            case 2: spriteName = "puyo_red.gif";
                    break;
            case 3: spriteName = "puyo_yellow.gif";
                    break;
        }
        
        setSpriteNames(new String[] {spriteName});
        
        setFrameSpeed(35);
    }
    
    public void action()
    {
        super.action();
        this.setY(this.getY() + vY);
        
        if(getX() + vX > 0 && getX() + vX < Stage.WIDTH_WINDOW - getWidth())
            this.setX(this.getX() + vX);
        
        vX = 0;
        vY = (collision) ? 0 : 1;
        
        if(getY() == Stage.HEIGHT_WINDOW - getHeight() * 2)
        {
            vY = 0;
        }
    }
    
    public void collision(Character c)
    {
        if(c instanceof Sphere || c instanceof Floor)
        {
            vX = 0;
            vY = 0;
            
            collision = true;
            canMove = false;
        }
    }
    
    public void keyPress(KeyEvent evt)
    {
        if(canMove)
        {
            switch(evt.getKeyCode())
            {
                case KeyEvent.VK_RIGHT: vX = (canMoveRight) ? 32 : 0;
                
                break;                  
            
                case KeyEvent.VK_LEFT: vX = (canMoveLeft) ? -32 : 0;
                break;
                
                case KeyEvent.VK_SPACE: RosaVientos();
                break;
            }
        }//if
    }
    
    public void setMove(boolean m)
    {
        canMove = m;
    }
    
    private void RosaVientos()
    {
        switch(rs)
        {
            case 'N': if((getX() + getWidth() * 2) < Stage.WIDTH_WINDOW && canMoveRight)
                      {   
                            vX = 32;
                            vY = 32;
                            rs = 'E';
                      }
            break;
            case 'E': if((getY() + 32) < Stage.HEIGHT_WINDOW)
                      {
                            vX = -32;
                            vY = 36;
                            rs = 'S';
                      }
            break;
            case 'S': if((getX() - 32) > 0 && canMoveLeft)
                      {
                            vX = -32;
                            vY = -32;
                            rs = 'O';
                      }
            break;
            case 'O': vX = 32;
                      vY = -32;
                      rs = 'N';
            break;
        }
    }
    
    public void setVx(int v)
    {
        vX = v;
    }
    
    public void setVy(int v)
    {
        vY = v;
    }
    
    public int getVx()
    {
        return vX;
    }
    
    public int getVy()
    {
        return vY;
    }
    
    public int getIDColor()
    {
        return color;
    }
    
    public void setCollision(boolean c)
    {
        collision = c;
    }
    
    public void setCheck(boolean check)
    {
        this.check = check;
    }
    
    public boolean getCheck()
    {
        return check;
    }
    
    public void setCanMoveLeft(boolean move)
    {
        canMoveLeft = move;
    }
    
    public void setCanMoveRight(boolean move)
    {
        canMoveRight = move;
    }
}
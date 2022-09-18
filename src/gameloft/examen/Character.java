/*******************************************************************************
 *                              Character.java                                 *
 *            This class manage the sprites and features in the screen         *
 *                          (C)César Himura Elric                              *
 *******************************************************************************/

package gameloft.examen;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Character
{
    private int x;
    private int y;
    private int width;
    private int height;
    
    private Stage stage;
    
    private Sprite sprite;
    
    private boolean remove = false;
    
    private String[] spriteNames;
    
    protected int currentFrame;

    private int frameSpeed;
    private int t;
    
    public Character(Stage stage)
    {
        this.stage = stage;
        
        this.sprite = stage.getSprite();
        
        currentFrame = 0;
        frameSpeed = 1;
        t = 0;
    }
    
    public void paint(Graphics2D g)
    {
        g.drawImage(this.sprite.getSprite(spriteNames[currentFrame]), x, y, stage);
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public void setSpriteNames(String[] names)
    { 
        spriteNames = names;
        height = 0;
        width = 0;
        for (int i = 0; i < names.length; i++ )
        {
            BufferedImage image = sprite.getSprite(spriteNames[i]);
            height = Math.max(height,image.getHeight());
            width = Math.max(width,image.getWidth());
        }
    }

    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public void action()
    {
        t++;
        if (t % frameSpeed == 0)
        {
            t = 0;
            currentFrame = (currentFrame + 1) % spriteNames.length;
        }
    }
    
    public void remove()
    {
        remove = true;
    }
    
    public boolean isMarkedToRemove()
    {
        return remove;
    }
    
    public void setMarkedToRemove(boolean r)
    {
        remove = r;
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }
    
    public void collision(Character c)
    {}
    
    public int getFrameSpeed()
    {
        return frameSpeed;
    }
    
    public void setFrameSpeed(int i)
    {
        frameSpeed = i;
    }
}
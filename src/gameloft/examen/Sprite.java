/*******************************************************************************
 *                              Spirte.java                                    *
 *               This class load all sprites for the game                      *
 *                          (C)César Himura Elric                              *
 *******************************************************************************/

package gameloft.examen;

import java.util.HashMap;

import java.net.URL;

import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class Sprite
{
    private HashMap sprites;
    
    public Sprite()
    {
        sprites = new HashMap();
    }
    
    public BufferedImage loadImage(String name)
    {
        URL url = null;
        try 
        {
            url = getClass().getClassLoader().getResource(name);
            return ImageIO.read(url); //*** FALLA!! >> BUG Problema al leer PNG 32 bits
        }
        catch(IOException ex)
        {
            System.out.println("¡¡ERROR!!" + ex.getClass().getName()+ " " + ex.getMessage());
            System.out.println("¡¡ERROR!! IOException:: " + ex);
            //System.exit(0);
            return null;
        }
      }
    
    public BufferedImage getSprite(String name)
    {
        BufferedImage img = (BufferedImage)sprites.get(name);
        if(img == null)
        {
            img = loadImage("gameloft/examen/images/" + name);
            sprites.put(name, img);
        }
        
        return img;
    }
}
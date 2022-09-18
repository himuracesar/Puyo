/*******************************************************************************
 *                               Stage.java                                    *
 *            This class manage the sprites and features in the screen         *
 *                          (C)César Himura Elric                              *
 *******************************************************************************/

package gameloft.examen;

import java.awt.image.ImageObserver;

public interface Stage extends ImageObserver
{
    public static final int WIDTH_WINDOW = 233;
    public static final int HEIGHT_WINDOW = 480;
    public static final int SPEED = 8;
    
    public Sprite getSprite();
}
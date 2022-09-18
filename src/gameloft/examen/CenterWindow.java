/*******************************************************************************
 *  Clase que centra una ventana en la pantalla dependiendo de la "Resolución" *
 *                                 que se tenga                                *
 *                              César Himura Elric                             *
 *                             (C)2007 RomaComputer                            *
 *******************************************************************************/

package gameloft.examen;

import java.awt.Toolkit;
import java.awt.Dimension;

public class CenterWindow
{
    private int windowX = 0;
    private int windowY = 0;
    private int screenWidth = 0;
    private int screenHeight = 0;
    
    public CenterWindow(int width, int height)
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
    
        Dimension screenSize = kit.getScreenSize();

        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;
        
        windowX = centerX - width/2;
        windowY = centerY - height/2;
    }
    
    public int getScreenWidth()
    {
        return screenWidth;
    }
    
    public int getScreenHeight()
    {
        return screenHeight;
    }
    
    public int getWindowX()
    {
        return windowX;
    }
    
    public int getWindowY()
    {
        return windowY;
    }
}

/*******************************************************************************
 *                                Floor.java                                    *
 *                           César Himura Elric                                *
 *                         (C)2007  RomaComputer                               *
 *******************************************************************************/

package gameloft.examen;

public class Floor extends Character
{
    public Floor(Stage stage)
    {
        super(stage);
        
        setSpriteNames(new String[] {"floorBlue.gif"});
        
        setFrameSpeed(35);
    }
}
/*******************************************************************************
 *                              Spirte.java                                    *
 *               This class load all sprites for the game                      *
 *                          (C)César Himura Elric                              *
 *******************************************************************************/

package gameloft.examen;

import java.net.URL;

import java.util.HashMap;

public abstract class Resource
{
    private HashMap resources;
    
    public Resource()
    {
        resources = new HashMap();
    }
    
    public Object loadResource(String name)
    {
        URL url = null;
        
        url = getClass().getClassLoader().getResource(name);
        
        return loadResource(url);
    }
    
    public Object getResource(String name)
    {
        Object res = resources.get(name);
        
        if(res == null)
        {
            res = loadResource("gameloft/examen/images/" + name);
            
            resources.put(name,res);
        }
        
        return res;
    }
    
    public abstract Object loadResource(URL url);
}
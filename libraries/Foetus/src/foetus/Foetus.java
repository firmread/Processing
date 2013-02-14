/*
Copyright 2008 Ilias Bergstrom.
  
This file is part of "Mother".

Mother is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Foobar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Mother.  If not, see <http://www.gnu.org/licenses/>.
 
onar3d@hotmail.com, www.onar3d.com
 
*/

package foetus;

import processing.core.*;
import processing.core.PApplet.RegisteredMethods;

import java.util.*;

public class Foetus
{
	PApplet parent;
	
	int[] m_BGColor;
	
	Hashtable<String, String> m_Messages;
	
	Hashtable<String, Boolean> m_Updating;
	
	/**
	 * For use by Mother, do not alter!
	 */
	public boolean standalone = true;

	float m_SpeedFraction = 1;
	
	public final String VERSION = "0.4.0";
	
	ArrayList<FoetusParameter> m_Parameters;
	
	public ArrayList<FoetusParameter> getParameters() { return m_Parameters; }
	
	/**
	 * Constructor
	 * @param parent
	 */
	public Foetus(PApplet parent)
	{
		this.parent = parent;
	
		m_Parameters = new ArrayList<FoetusParameter>();
		
		m_Messages = new Hashtable<String,String>();
		
		m_Updating = new Hashtable<String,Boolean>();
				
		parent.registerDispose(this);
		parent.registerPre(this);
		parent.registerPost(this);
		parent.registerDraw(this);
		
		m_BGColor = new int[] {128, 128, 128};
	}
	
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public String version() 
	{
		return VERSION;
	}

	/**
	 * For use by Mother, do not alter!
	 * @param inSF
	 */
	public void setSpeedFraction(float inSF) 
	{
		m_SpeedFraction = inSF; 
	}
	
	/**
	 * For use by Mother!
	 * @param inSF
	 */
	public float getSpeedFraction() 
	{
		return m_SpeedFraction; 
	}
	
	/**
	 * millis() is the same as the Processing millis() function, 
	 * with the additional feature that it takes the specified speed fraction into account. 
	 * This is useful when running in non real-time mode, 
	 * as the f.millis() call returns the time value at a given frame number 
	 * that it would have if it were running in real-time. 
	 * @return int
	 */
	public int millis() 
	{
		double pm = parent.millis();
	    return (int)(pm/m_SpeedFraction);
	}
	
	/**
	 * Use this to set the background color of the sketch when it is running in standalone mode,
	 * i.e. when it is not hosted by Mother.
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setBGColor(int r, int g, int b)
	{
		m_BGColor[0] = r;
		m_BGColor[1] = g;
		m_BGColor[2] = b;
	}
	
	public void dispose()
	{
//		System.out.println("Dispose: " + parent.toString());
	}

	public void draw()
	{
//		System.out.println("draw: " + parent.toString());
	}
	
	/**
	 * Use this method to register your sketches methods with Mother. This means they will 
	 * then be listed when the sketch is asked for a list of its capabilities.
	 * @param OSC address
	 * @param OSC typetag
	 */
	public void registerMethod(String address, String typetag)
	{
		m_Messages.put(address, typetag);
		m_Updating.put(address, false);
	}

	/**
	 * For use by Mother!
	 * @return
	 */
	public void unregisterMethod(String address)
	{
		m_Messages.remove(address);
		m_Updating.remove(address);
	}
	
	/**
	 * For use by Mother!
	 * @return
	 */
	public Hashtable<String,String> getSupportedMessages()
	{
		return m_Messages;
	}
	
	/**
	 * For use by Mother, do not alter!
	 * @return
	 */
	public void setUpdatingStatus(String address, boolean status)
	{
		synchronized(m_Updating)
		{
			m_Updating.put(address, status);
		}
	}
	
	/**
	 * For use by Mother!
	 * @return
	 */
	public boolean getUpdatingStatus()
	{
		synchronized(m_Updating)
		{
			if(m_Updating.containsValue(true))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	/**
	 * For use by Mother!
	 * @return
	 */
	public void pre()
	{
		if (standalone)
			parent.background(m_BGColor[0],m_BGColor[1],m_BGColor[2]);
		
//		System.out.println("Pre: " + parent.toString());
	}
	
	/**
	 * For use by Mother!
	 * @return
	 */
	public void post()
	{
//		System.out.println("Post: " + parent.toString());
	}
	
	
	public void addParameter(FoetusParameter f)
	{
		m_Parameters.add(f);	
	}
}
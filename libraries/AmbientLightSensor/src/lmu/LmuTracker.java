/*
  LmuTracker - Ambient Light Sensor detection for Apple MacBookPro.
  
  (c) copyright 2008 Martin Raedlinger (www.formatlos.de)
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package lmu;

/**
 * this class is the interface to the Ambient Light Sensor
 * 
 * @example LmuTest 
 * @author Martin Raedlinger (www.formatlos.de)
 * @version 1.0
 */

public class LmuTracker {

    // Load the JNI Interface
    static 
    {    	
        System.loadLibrary("LmuTracker");
    }

    // Native function
    private static native int[] readLMU();


    /**
     * @return array of left and right sensor values
     */
    public static int[] getLMUArray() 
    {
        return readLMU(); 
    }
    
    /**
     * @return left ambient light sensor reading
     */
    public static int getLMULeft() 
    {
        return readLMU()[0]; 
    }

    /**
     * @return right ambient light sensor reading
     */
    public static int getLMURight() 
    {
        return readLMU()[1]; 
    }
}


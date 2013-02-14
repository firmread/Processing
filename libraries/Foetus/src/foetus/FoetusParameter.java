package foetus;


import megamu.shapetween.Shaper;
import megamu.shapetween.Tween;

import processing.core.PApplet;

public class FoetusParameter
{
	float m_Value;
	float m_LastValue;
	float m_NewValue;
	float m_Factor;
	
	String m_Address;
	
	boolean m_Splerp = true;

	Foetus r_f; 
			
	Tween ani;
	
	/**
	 * Allows spline interpolation for individual floating point synth parameters.
	 * @param f
	 * @param value
	 * @param address
	 * @param typetag
	 */
	public FoetusParameter(Foetus f, float value, String address, String typetag )
	{
		r_f 					= f;		
		m_Value 				= value;
		m_NewValue				= value;
		m_LastValue				= value;

		m_Address = address;
		
		if(r_f!=null)
			r_f.registerMethod(address, typetag);
		
		r_f.addParameter(this);

		timeStarted = System.currentTimeMillis();
		
		ani = new Tween(null/*r_f.parent*/, r_f.getSpeedFraction(), Tween.SECONDS, Shaper.COSINE);
	}

	
/*	public void setFactor(Float factor)
	{
		m_Factor = factor;
		
		if(m_Factor>=1.0f)
		{
			r_f.setUpdatingStatus(m_Address, false);
		}
		else
		{
			r_f.setUpdatingStatus(m_Address, true);	
		}
	}*/
	
	
	/**
	 * Returns the interpolated value held by the parameter at the current time.
	 * @return
	 */
	public float getValue()
	{
		if(m_Splerp)
		{
			m_Value = PApplet.lerp(m_LastValue, m_NewValue, ani.position());
			//System.out.println(m_Value + ", " + ani.position());
		}
		
		if(ani.position()>=1.0f)
		{
			r_f.setUpdatingStatus(m_Address, false);
		}
		else
		{
			r_f.setUpdatingStatus(m_Address, true);	
		}
		
		//System.out.println("Last: " + m_LastValue + " New: " + m_NewValue + " Factor: " + m_Factor);
		//System.out.println(m_Value + ", " + PApplet.lerp(m_LastValue, m_NewValue, ani.position()));
		
		return m_Value;
	}
	
	long timeStarted;
	
	/**
	 * Set a new value for the parameter. This will trigger an interpolation with the 
	 * new value as target.
	 * @param val
	 */
	public void setValue(float val)
	{
		long elapsed;
		long elapsedTime = System.currentTimeMillis() - timeStarted;
		
	    m_LastValue = m_Value;
	    m_NewValue  = val;
		    
	    elapsed = (long)(elapsedTime/r_f.getSpeedFraction());
	  
	//    System.out.println(elapsed/1000f);
	    
	    ani.end();
	    
	    if(elapsed<(500/r_f.getSpeedFraction()))
	    {
	    	m_Splerp 	= false;
	    	m_LastValue = val;
	    	m_Value 	= val;
	    	r_f.setUpdatingStatus(m_Address, false);
	    }
	    else
	    {
	    	m_Splerp = true;
	    	
	    	if(elapsed>(3000/r_f.getSpeedFraction()))
	    		elapsed = (long)(3000/r_f.getSpeedFraction());
	  	    	
	    	ani.setDuration(elapsed/1000f * r_f.parent.frameRate*r_f.getSpeedFraction(), Tween.FRAMES);
	    //	ani.setDuration(elapsed/1000f , Tween.SECONDS);
	    	
	    	ani.start();
	    	
	    	timeStarted = System.currentTimeMillis();
	    }
	}
	
	public void tick()
	{
		if(ani.isTweening())
		{
			ani.tick();
		}
 		//System.out.println("tick " + ani.position());
	}
}

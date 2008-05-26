package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;

/**
 * 
 * @author Frank Sch�ler, Alexander Ro�wog
 *
 * @param <E>
 */
public abstract class BaseActivity<E extends TActivity>
{
	private E _data;

	/**
	 * 
	 */
	public BaseActivity(String Name)
	{
		this.genTActivity(Name);
	}
	
	/**
	 * 
	 * @return
	 */
	public final E getActivity()
	{
		return this._data;
	}
	
	/**
	 * 
	 * @param ta
	 */
	public final void setActivity(E ta)
	{
		this._data = ta;
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getName(){
		return this._data.getName();
	}
	
	/**
	 * 
	 * @param Name
	 */
	protected abstract void genTActivity(String Name);
	
	/**
	 * 
	 * @param bip
	 */
	public abstract BaseActivity<?> saveInformation(BPELadditionalPanel bip);
	
	/**
	 * 
	 * @param bip
	 */
	public abstract void setInformationToPanel(BPELadditionalPanel bip);
	
	/**
	 * 
	 * @return
	 */
	public static TProcess genBpelProcess()
	{
		return TProcess.Factory.newInstance();
	}
	
	/**
	 * @return String
	 */
	public String toString()
	{
		return this.getName();
	}	
}

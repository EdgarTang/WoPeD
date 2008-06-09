package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.woped.bpel.gui.transitionproperties.BaseActivity;
import org.woped.core.model.petrinet.TransitionModel;


public class TimeTriggerTransition extends Transition<TransitionModel>
{


	public TimeTriggerTransition(TransitionModel data)
	{
		super(data);
	}
	
	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!TimeTriggerTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((TimeTriggerTransition) e).getData().getId())
			return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		BaseActivity<?> ba = (BaseActivity<?>) this.getData().getBpelData();
		// TActivity activity = null;
		if (ba != null)
		{
			return ba.getActivity();

		} else
		{
			TEmpty iEmpty = TEmpty.Factory.newInstance();
			iEmpty.setName(this.getData().getNameValue());
			return iEmpty;
		}
	}

}

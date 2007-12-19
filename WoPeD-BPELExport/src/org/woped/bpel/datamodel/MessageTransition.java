package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.PetriNetModelElement;


public class MessageTransition extends NonterminalElement
{

	public MessageTransition(PetriNetModelElement data)
	{
		super(data);
	}

	@Override
	public boolean accept_post_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}

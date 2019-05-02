package org.onlperations.entity;

import java.util.ArrayList;
import java.util.List;

public class ConversationOutput {
	
	private List<NamedEntity> namedEntities;
	
	public ConversationOutput() {
		this.namedEntities = new ArrayList<NamedEntity>();
	}

	public List<NamedEntity> getNamedEntities() {
		return namedEntities;
	}

	public void setNamedEntities(List<NamedEntity> namedEntities) {
		this.namedEntities = namedEntities;
	}

}

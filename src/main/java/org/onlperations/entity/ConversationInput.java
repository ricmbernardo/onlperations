package org.onlperations.entity;

public class ConversationInput {
	
	private String sentence;
	private String entityType;
	private String categorizerType;
	private String language;

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getCategorizerType() {
		return categorizerType;
	}

	public void setCategorizerType(String categorizerType) {
		this.categorizerType = categorizerType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}

package br.com.e7.exploringmars.util;

public enum ConfigProperties {
	DEFAULT_ENCODING("EXP_MARS_DEFAULT_ENCODING", "utf-8"),
	SOLR_URL("EXP_MARS_SOLR_URL", "http://localhost:8983/solr/missions");

	private final String property;
	private final String value;
	
	private ConfigProperties(final String property, final String value) {
		this.property = property;
		this.value = value;
	}
	
	public String value() {
		return System.getProperty(property, value);
	}
}

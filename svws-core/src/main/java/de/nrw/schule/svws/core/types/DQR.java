package de.nrw.schule.svws.core.types;

import jakarta.validation.constraints.NotNull;

/**
 * Eine Aufzählung der Niveaus des Deutschen Qualifikationsrahmens (DQR).
 * 
 * @see <a href="https://www.dqr.de"> www.dqr.de </a>
 */
public enum DQR {

	/** DQR Niveau 1 */
	NIVEAU_1("DQR Niveau 1", "Niveau 1 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 2 */
	NIVEAU_2("DQR Niveau 2", "Niveau 2 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 3 */
	NIVEAU_3("DQR Niveau 3", "Niveau 3 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 4 */
	NIVEAU_4("DQR Niveau 4", "Niveau 4 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 5 */
	NIVEAU_5("DQR Niveau 5", "Niveau 5 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 6 */
	NIVEAU_6("DQR Niveau 6", "Niveau 6 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 7 */
	NIVEAU_7("DQR Niveau 7", "Niveau 7 des Deutschen Qualifikationsrahmens (DQR)"),

	/** DQR Niveau 8 */
	NIVEAU_8("DQR Niveau 8", "Niveau 8 des Deutschen Qualifikationsrahmens (DQR)");


	/** Die Bezeichnung des Niveaus */
	public final String bezeichnung;
	
	/** Eine Beschreibung des DQR-Niveaus */
	public final String beschreibung;


	/**
	 * Erstell ein neues DQR-Niveau.
	 * 
	 * @param bezeichnung    die Bezeichnung des Niveaus
	 * @param beschreibung   eine kurze Beschreibung des Niveaus
	 */
	private DQR(@NotNull String bezeichnung, @NotNull String beschreibung) {
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
	}

}

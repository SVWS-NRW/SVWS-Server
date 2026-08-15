package de.svws_nrw.module.reporting.diagnose;

/**
 * Die öffentlichen Kategorien, in denen ein Ausgabeproblem den Anwender erreicht. Der Katalog ist bewusst klein und folgt der Auswirkung, nicht der
 * Ursache - ob ein Datensatz fehlte oder sein Laden scheiterte, gehört ins Server-Log. Einzige Ausnahme: Ein vorhandener, aber nicht darstellbarer Wert
 * bildet eine eigene Kategorie, denn dort liegen die Daten vor. Jede Kategorie ist dauerhafter Vertrag mit dem Client.
 */
public enum ReportingHinweisKategorie {

	/** Angeforderte Datensätze erscheinen nicht in der Ausgabe. */
	DATENSAETZE_FEHLEN("datensaetze", "Angeforderte Datensätze erscheinen nicht in der Ausgabe"),

	/** Die Datensätze erscheinen, ihnen fehlen aber einzelne Angaben. */
	ANGABEN_FEHLEN("angaben", "Einzelne Angaben fehlen in den ausgegebenen Datensätzen"),

	/** Ein vorhandener Wert ließ sich nicht ausgeben - etwa die Signatur einer Bescheinigung. */
	WERT_NICHT_DARSTELLBAR("darstellung", "Vorhandene Werte konnten nicht ausgegeben werden");


	/** Der Schlüssel im Hinweis-Header, kleingeschrieben nach RFC 9651. Er ist Vertrag - der Enum-Name ist es nicht. */
	private final String schluessel;

	/** Die lesbare Bezeichnung für den Empfänger, etwa in der Hinweisdatei. Anzeigetext, darf sich ändern - der Schlüssel nicht. */
	private final String bezeichnung;


	/**
	 * Erzeugt eine Kategorie mit ihrem öffentlichen Schlüssel und ihrer lesbaren Bezeichnung.
	 *
	 * @param schluessel  Der Schlüssel im Hinweis-Header.
	 * @param bezeichnung Die Bezeichnung für den Empfänger der Ausgabe.
	 */
	ReportingHinweisKategorie(final String schluessel, final String bezeichnung) {
		this.schluessel = schluessel;
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Gibt den Schlüssel dieser Kategorie im öffentlichen Hinweis-Header zurück.
	 *
	 * @return Der Schlüssel.
	 */
	public String schluessel() {
		return this.schluessel;
	}

	/**
	 * Gibt die Bezeichnung dieser Kategorie für den Empfänger einer Ausgabe zurück.
	 *
	 * @return Die Bezeichnung.
	 */
	public String bezeichnung() {
		return this.bezeichnung;
	}


	/**
	 * Bildet ein internes Ausgabeproblem auf seine öffentliche Kategorie ab - die einzige Stelle dieser Zuordnung. Der {@code switch} über die Auswirkungen
	 * hat bewusst kein {@code default}: Eine neue Auswirkung erzwingt so eine bewusste Entscheidung, statt still als fehlende Angabe zu gelten.
	 *
	 * @param problem Das interne Ausgabeproblem.
	 *
	 * @return Die öffentliche Kategorie des Problems.
	 */
	public static ReportingHinweisKategorie fuer(final ReportingProblem problem) {
		// Der nicht darstellbare Wert steht vor der Auswirkung: Er bildet unabhängig davon eine eigene Kategorie, weil dort die Daten vorliegen.
		if (problem.ursache() == ReportingProblemursache.NICHT_DARSTELLBAR) {
			return WERT_NICHT_DARSTELLBAR;
		}
		return switch (problem.auswirkung()) {
			case DATENSATZ_AUSGELASSEN -> DATENSAETZE_FEHLEN;
			case TEILDATEN_FEHLEN -> ANGABEN_FEHLEN;
		};
	}

}

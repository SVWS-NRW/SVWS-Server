package de.svws_nrw.core.types.reporting;

/**
 * Legt fest, wer also Empfänger von E-Mails verwendet werden soll, wenn diese im Rahmen des Reportings gesendet werden.
 * Die Interpretation der IDs aus den Reporting-Daten hängt von diesem Modus ab.
 */
public enum ReportingEMailEmpfaengerTyp {

	/** Undefiniert, ein Versand kann nicht stattfinden. */
	UNDEFINED(0),

	/** Versand an Schüler. Die IDs werden als Schüler-IDs interpretiert. */
	SCHUELER(1),

	/** Versand an Lehrkräfte. Die IDs werden als Lehrer-IDs interpretiert. */
	LEHRER(2),

	/** Versand an Klassenlehrer (Leitungen). Die IDs werden als Klassen-IDs interpretiert, welche dann intern die Klassenleitung-IDs ermitteln. */
	KLASSENLEHRER(3),

	/** Versand an Kurslehrer (im Kurs unterrichtende Lehrer). Die IDs werden als Kurs-IDs interpretiert, welche dann intern die Kurslehrer-IDs ermitteln. */
	KURSLEHRER(4),

	/** Versand an Kurslehrer (im Kurs unterrichtende Lehrer) im Rahmen der Kursplanung für die Oberstufe. Die IDs werden als Kurs-IDs in der Kursplanung
	 * interpretiert, welche dann intern die Kurslehrer-IDs ermitteln. */
	GOSTKURSPLANUNG_KURSLEHRER(5);

	/** Die ID des Reporting-E-Mail-Empfaenger-Typs */
	private final int id;

	/**
	 * Erstellt ein neues Reporting-E-Mail-Empfaenger-Typ-Objekt.
	 * @param id Die ID des Reporting-E-Mail-Empfaenger-Typs
	 */
	ReportingEMailEmpfaengerTyp(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID dieses Reporting-E-Mail-Empfaenger-Typs zurück
	 * @return Die ID dieses Reporting-E-Mail-Empfaenger-Typs
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den Reporting-E-Mail-Empfaenger-Typ anhand der übergebenen ID.
	 * @param id   	Die ID des gesuchten Reporting-E-Mail-Empfaenger-Typs
	 * @return 		Der Reporting-E-Mail-Empfaenger-Typ
	 */
	public static ReportingEMailEmpfaengerTyp getByID(final int id) {
		for (final ReportingEMailEmpfaengerTyp em : ReportingEMailEmpfaengerTyp.values()) {
			if (em.id == id) {
				return em;
			}
		}
		return UNDEFINED;
	}
}

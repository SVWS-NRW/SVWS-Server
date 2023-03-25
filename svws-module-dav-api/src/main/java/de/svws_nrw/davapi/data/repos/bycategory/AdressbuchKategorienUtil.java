package de.svws_nrw.davapi.data.repos.bycategory;

/**
 * Diese Klasse dient der Namensgebung der verschiedenen Adressbuchkategorien
 * abhängig von Jahrgang, Jahresabschnitt, Kurs-/Klassennamen, Fachschaften,
 * etc.
 *
 */
public class AdressbuchKategorienUtil {
	private static final class AdressbuchKategorienStrings {
		/** Format für die Fachschaft, bspw. Fachschaft Mathematik */
		private static final String LEHRER_FACHSCHAFT = "Fachschaft %s";
		/** Format für das Jahrgangsteam, bspw Team JG 09 */
		private static final String LEHRER_JAHRGANGSTEAM = "Team JG %s";
		/** Format für die Klassenlehrer, bspw KL 9c S2 2022/23 */
		private static final String LEHRER_KLASSENLEHRER = "KL %s %s";
		/** Format für die Klassenlehrer eines Jahrgangs, bspw KL JG 09 */
		private static final String LEHRER_KLASSENLEHRER_JAHRGANG = "KL JG %s";
		/** Format für alle Klassenlehrer */
		private static final String LEHRER_KLASSENLEHRER_ALLE = "Klassenlehrer";
		/** Format für alle lehrer einer Klasse, bspw Team 9c S2 2022/23 */
		private static final String LEHRER_KLASSE_TEAM = "Team %s %s";
		/** Format für Klassen, bspw. 9c S2 2022/23 */
		private static final String SCHUELER_KLASSE = "Klasse %s %s";
		/** Format für Kurse, bspw. CH-GK1 09 Q2 2022/23 */
		private static final String SCHUELER_KURS = "Kurs %s %s %s";
		/** Format für Jahrgang, bspw JG 09 Q2 2022/23 */
		private static final String SCHUELER_JAHRGANG = "JG %s";
		/** Format für Neuaufnahmen nach Klasse, bspw Neuaufnahmen Klasse 9c */
		private static final String SCHUELER_NEUAUFNAHMEN_KLASSE = "Neuaufnahmen %s %s";
		/** Format für Neuaufnahmen nach Jahrgang, bspw Neuaufnahmen JG 09" */
		private static final String SCHUELER_NEUAUFNAHMEN_JAHRGANG = "Neuaufnahmen JG %s";
		/** Format für alle Neuaufnahmen, bspw Neuaufnahmen " */
		private static final String SCHUELER_NEUAUFNAHMEN = "Neuaufnahmen";
		/** Format für Eltern einer Klasse, bspw Eltern 9c S2 2022/23 */
		private static final String ERZIEHER_KLASSE = "Eltern %s %s";
		/** Format für Eltern eines Jahrgangs, bspw Eltern JG 09 Q2 2022/23 */
		private static final String ERZIEHER_JAHRGANG = "Eltern JG %s";
		/** Format für Eltern eines Kurs, bspw Eltern CH-GK1 09 Q2 2022/23 */
		private static final String ERZIEHER_KURS = "Eltern %s %s %s";
		/** Format für alle Eltern */
		private static final String ERZIEHER_ALLE = "Eltern";
		/**
		 * Format für Eltern der Neuaufnahmen einer Klasse, bspw Eltern Neufnahmen
		 * Klasse 9c S2 2022/23
		 */
		private static final String ERZIEHER_NEUAUFNAHMEN_KLASSE = "Eltern Neuaufnahmen %s %s";
		/**
		 * Format für Eltern der Neuaufnahmen eines Jahrgangs, bspw. Eltern Neuaufnahmen
		 * JG 09 Q2 2022/23
		 */
		private static final String ERZIEHER_NEUAUFNAHMEN_JAHRGANG = "Eltern Neuaufnahmen JG %s";
		/** Format für alle Eltern der Neuaufnahmen */
		private static final String ERZIEHER_NEUAUFNAHMEN_ALLE = "Eltern Neuaufnahmen";
	}

	/**
	 * String-Repr. des aktuellen Schuljahresabschnitts
	 */
	private String abschnitt;

	/**
	 * Konstruktor für den diese Utility mit dem Schuljahresabschnitt.
	 * 
	 * @param abschnitt eine Stringrepräsentation des Schuljahresabschnitts, die
	 *                  beim Formatieren genutzt werden soll
	 */
	public AdressbuchKategorienUtil(String abschnitt) {
		this.abschnitt = abschnitt;
	}

	/**
	 * Gibt den formatierten String für die Kategorie Lehrer der Fachschaft wieder
	 * 
	 * @param fachschaft die Fachschaft
	 * @return der formatierte String
	 */
	public String formatLehrerFachschaft(String fachschaft) {
		return String.format(AdressbuchKategorienStrings.LEHRER_FACHSCHAFT, fachschaft);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Lehrer eines Jahrgangs wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatLehrerJahrgangsteam(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.LEHRER_JAHRGANGSTEAM, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Klassenlehrer einer Klasse
	 * wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatKlassenlehrer(String klasse) {
		return String.format(AdressbuchKategorienStrings.LEHRER_KLASSENLEHRER, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Klassenlehrer eines Jahrgangs
	 * wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatKlassenlehrerJahrgang(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.LEHRER_KLASSENLEHRER_JAHRGANG, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie alle Klassenlehrer wieder
	 * 
	 * @return der formatierte String
	 */
	public String formatKlassenlehrerAlle() {
		return String.format(AdressbuchKategorienStrings.LEHRER_KLASSENLEHRER_ALLE);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Lehrerteam einer Klasse wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatLehrerKlasseTeam(String klasse) {
		return String.format(AdressbuchKategorienStrings.LEHRER_KLASSE_TEAM, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Schueler einer Klasse wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatSchuelerKlasse(String klasse) {
		return String.format(AdressbuchKategorienStrings.SCHUELER_KLASSE, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Schueler eines Kurs wieder
	 * 
	 * @param kurs        der Kurs
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatSchuelerKurs(String kurs, String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.SCHUELER_KURS, kurs, jahrgangKrz, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Schueler eines Jahrgangs
	 * wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatSchuelerJahrgang(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.SCHUELER_JAHRGANG, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Neuaufnahmen einer Klasse
	 * wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatSchuelerNeuaufnahmeKlasse(String klasse) {
		return String.format(AdressbuchKategorienStrings.SCHUELER_NEUAUFNAHMEN_KLASSE, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Neuaufnahmen eines Jahrgangs
	 * wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatSchuelerNeuaufnahmeJahrgang(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.SCHUELER_NEUAUFNAHMEN_JAHRGANG, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie alle Neuaufnahmen wieder
	 * 
	 * @return der formatierte String
	 */
	public String formatSchuelerNeuaufnahmenAlle() {
		return AdressbuchKategorienStrings.SCHUELER_NEUAUFNAHMEN;
	}

	/**
	 * Gibt den formatierten String für die Kategorie Erzieher einer Klasse wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatErzieherKlasse(String klasse) {
		return String.format(AdressbuchKategorienStrings.ERZIEHER_KLASSE, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Erzieher eines Jahrgangs
	 * wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatErzieherJahrgang(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.ERZIEHER_JAHRGANG, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Erzieher eines Kurs wieder
	 * 
	 * @param kurs        der Kurs
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatErzieherKurs(String kurs, String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.ERZIEHER_KURS, kurs, jahrgangKrz, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie alle Erzieher wieder
	 * 
	 * @return der formatierte String
	 */
	public String formatErzieherAlle() {
		return AdressbuchKategorienStrings.ERZIEHER_ALLE;
	}

	/**
	 * Gibt den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * einer Klasse wieder
	 * 
	 * @param klasse die Klasse
	 * @return der formatierte String
	 */
	public String formatErzieherNeuaufnahmenKlasse(String klasse) {
		return String.format(AdressbuchKategorienStrings.ERZIEHER_NEUAUFNAHMEN_KLASSE, klasse, abschnitt);
	}

	/**
	 * Gibt den formatierten String für die Kategorie Erzieher der Neuaufnahmen
	 * eines Jahrgangs wieder
	 * 
	 * @param jahrgangKrz der Jahrgang
	 * @return der formatierte String
	 */
	public String formatErzieherNeuaufnahmenJahrgang(String jahrgangKrz) {
		return String.format(AdressbuchKategorienStrings.ERZIEHER_NEUAUFNAHMEN_JAHRGANG, jahrgangKrz);
	}

	/**
	 * Gibt den formatierten String für die Kategorie alle Erzieher von Neuaufnahmen
	 * wieder
	 * 
	 * @return der formatierte String
	 */
	public String formatErzieherNeuaufnahmenAlle() {
		return AdressbuchKategorienStrings.ERZIEHER_NEUAUFNAHMEN_ALLE;
	}
}

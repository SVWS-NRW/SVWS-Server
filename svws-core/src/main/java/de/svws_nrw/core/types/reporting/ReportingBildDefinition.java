package de.svws_nrw.core.types.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die im Rahmen des Reportings verwendeten Bilddefinitionen, die in der DB unter Nutzung der hier definierten Kennung persistiert werden
 * können.
 * <p>
 * Die Definitionen enthalten die folgenden Informationen zum Bild:
 * </p>
 * <ul>
 *   <li>Kennung für die Persistierung in der DB. Diese Kennung muss eindeutig über alle Definitionen hinweg sein.</li>
 *   <li>Bezeichnung der Bilddefinition, z. B. zur Anzeige in Listen oder Auswahldialogen.</li>
 *   <li>Beschreibung der Bilddefinition, z. B. zur Erklärung des Bildinhalts oder des Verwendungszwecks.</li>
 *   <li>Geforderte Breite des Bildes in mm</li>
 *   <li>Geforderte Höhe des Bildes in mm</li>
 *   <li>Schulformen, für die die Bilddefinition gültig ist. Eine leere Liste der Schulformen wird interpretiert als für alle Schulformen gültig.</li>
 * </ul>
 */
public enum ReportingBildDefinition {

	/** Definition für SchILD-NRW-Schullogo */
	SCHULLOGO_SCHILD("SCHULLOGO_SCHILD", "SchILD-NRW-Schullogo", "Das Schullogo, welches aus der SchILD-NRW Datenbank übernommen wurde.", 45, 45,
			List.of()),

	/** Definition für quadratisches Schullogo */
	SCHULLOGO_QUADRATISCH("SCHULLOGO_QUADRATISCH", "Quadratisches Schullogo", "Das Schullogo in einer quadratischen Abmessung.", 40, 40,
			List.of()),

	/** Definition für quadratisches Schulträgerlogo */
	SCHULTRAEGERLOGO_QUADRATISCH("SCHULTRAEGERLOGO_QUADRATISCH", "Quadratisches Schulträgerlogo", "Das Schulträgerlogo in einer quadratischen Abmessung.", 40,
			40, List.of()),

	/** Definition für DIN5008-Briefkopf */
	DIN5008_BRIEFKOPF("DIN5008_BRIEFKOPF", "DIN5008-Briefkopf", "Vollständiger Briefkopf für Anschreiben nach DIN5008", 190, 45, List.of());


	/** Die Kennung für die Persistierung in der DB. Diese Kennung muss eindeutig über alle Definitionen hinweg sein. */
	private final String kennung;

	/** Die Bezeichnung der Bilddefinition, z. B. zur Anzeige in Listen oder Auswahldialogen. */
	private final String bezeichnung;

	/** Die Beschreibung der Bilddefinition, z. B. zur Erklärung des Bildinhalts oder des Verwendungszwecks. */
	private final String beschreibung;

	/** Die geforderte Breite des Bildes in mm. */
	private final int breite;

	/** Die geforderte Höhe des Bildes in mm. */
	private final int hoehe;

	/** Die Schulformen, für die die Bilddefinition gültig ist. Eine leere Liste der Schulformen wird interpretiert als für alle Schulformen gültig. */
	private final @NotNull List<Schulform> schulformen;

	/**
	 * Erzeugt eine neue Bilddefinition.
	 *
	 * @param kennung      Die Kennung für die Persistierung in der DB. Diese Kennung muss eindeutig über alle Definitionen hinweg sein.
	 * @param bezeichnung  Die Bezeichnung der Bilddefinition, z. B. zur Anzeige in Listen oder Auswahldialogen.
	 * @param beschreibung Die Beschreibung der Bilddefinition, z. B. zur Erklärung des Bildinhalts oder des Verwendungszwecks.
	 * @param breite       Die geforderte Breite des Bildes in mm.
	 * @param hoehe        Die geforderte Höhe des Bildes in mm.
	 * @param schulformen  Die Schulformen, für die die Bilddefinition gültig ist. Eine leere Liste der Schulformen wird interpretiert als für alle Schulformen gültig.
	 */
	ReportingBildDefinition(final String kennung, final String bezeichnung, final String beschreibung, final int breite, final int hoehe,
			final List<Schulform> schulformen) {
		this.kennung = kennung;
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
		this.breite = breite;
		this.hoehe = hoehe;
		this.schulformen = (schulformen != null) ? schulformen : new ArrayList<>();
	}

	/**
	 * Diese Methode ermittelt die Bilddefinition anhand der übergebenen Kennung.
	 *
	 * @param kennung   die Kennung der Bilddefinition für die DB.
	 *
	 * @return die Bilddefinition oder {@code null}, falls die Kennung ungültig ist
	 */
	public static ReportingBildDefinition getByKennung(final String kennung) {
		if (kennung == null) {
			return null;
		}

		for (@NotNull final ReportingBildDefinition bildDefinition : ReportingBildDefinition.values()) {
			if (bildDefinition.getKennung().equals(kennung)) {
				return bildDefinition;
			}
		}
		return null;
	}

	/**
	 * Diese Methode ermittelt die Bilddefinitionen, die für die übergebene Schulform gültig sind.
	 *
	 * @param schulform  die Schulform, für die dei zulässigen Bilddefinitionen gesucht werden sollen.
	 *
	 * @return die Bilddefinitionen oder {@code null}, falls die Schulform nicht unterstützt wird.
	 */
	public static @NotNull List<ReportingBildDefinition> getBySchulform(final Schulform schulform) {
		if (schulform == null) {
			return new ArrayList<>();
		}

		final List<ReportingBildDefinition> bildDefinitionen = new ArrayList<>();
		for (@NotNull final ReportingBildDefinition bildDefinition : ReportingBildDefinition.values()) {
			if (isSchulformGueltig(schulform, bildDefinition.getSchulformen())) {
				bildDefinitionen.add(bildDefinition);
			}
		}
		return bildDefinitionen;
	}

	/**
	 * Diese Methode ermittelt die Bilddefinition, die für die übergebene Kennung und Schulform gültig ist.
	 * Wenn keine gültige Bilddefinition für die Kennung und Schulform gefunden wird, wird {@code Optional.empty()} zurückgegeben.
	 *
	 * @param kennung    die Kennung der Bilddefinition für die DB.
	 * @param schulform   die Schulform, für die dei zulässigen Bilddefinitionen gesucht werden sollen.
	 *
	 * @return die Bilddefinition oder {@code Optional.empty()}, falls die Kennung ungültig ist oder die Schulform nicht unterstützt wird. Wenn keine
	 * Schulform angegeben wird, wird die Bilddefinition für die Kennung ohne Schulform-Filterung zurückgegeben.
	 */
	public static ReportingBildDefinition getByKennungAndSchulform(final String kennung, final Schulform schulform) {
		final var bildDefinition = getByKennung(kennung);
		return ((bildDefinition != null) && isSchulformGueltig(schulform, bildDefinition.getSchulformen())) ? bildDefinition : null;
	}

	private static boolean isSchulformGueltig(final Schulform schulform, final List<Schulform> schulformen) {
		return (schulform == null) || (schulformen == null) || schulformen.isEmpty() || schulformen.contains(schulform);
	}

	/**
	 * Gibt die DB-Kennung der Bilddefinition zurück. Diese Kennung dient als eindeutiger technischer Schlüssel
	 * zur Persistierung und zum Wiederauffinden der Definition in der Datenbank.
	 *
	 * @return die DB-Kennung der Bilddefinition
	 */
	public String getKennung() {
		return this.kennung;
	}

	/**
	 * Gibt die Bezeichnung der Bilddefinition zurück. Die Bezeichnung ist für die fachliche oder
	 * benutzerfreundliche Anzeige des Bildes vorgesehen.
	 *
	 * @return die Bezeichnung der Bilddefinition
	 */
	public String getBezeichnung() {
		return this.bezeichnung;
	}

	/**
	 * Gibt die Beschreibung der Bilddefinition zurück. Die Beschreibung erläutert den fachlichen Zweck
	 * oder die Verwendung des Bildes innerhalb des Reportings.
	 *
	 * @return die Beschreibung der Bilddefinition
	 */
	public String getBeschreibung() {
		return this.beschreibung;
	}

	/**
	 * Gibt die Breite des Bildes in Millimetern zurück.
	 *
	 * @return die Breite des Bildes in mm
	 */
	public int getBreite() {
		return this.breite;
	}

	/**
	 * Gibt die Höhe des Bildes in Millimetern zurück.
	 *
	 * @return die Höhe des Bildes in mm
	 */
	public int getHoehe() {
		return this.hoehe;
	}

	/**
	 * Gibt die Schulformen zurück, für die das Bild gültig ist. Ist die zurückgegebene Liste leer,
	 * so gilt die Bilddefinition für alle Schulformen.
	 *
	 * @return die Schulformen, für die die Bilddefinition gültig ist
	 */
	public List<Schulform> getSchulformen() {
		return this.schulformen;
	}

}

package de.svws_nrw.core.data.jahrgang;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Jahrgangs.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Jahrgangs.")
@TranspilerDTO
public class JahrgangsDaten {

	/** Die ID des Jahrgangs. */
	@Schema(description = "die ID des Jahrgangs", example = "4711")
	public long id;

	/** Das schulinterne Kürzel des Jahrgangs. */
	@Schema(description = "Das schulinterne Kürzel des Jahrgangs.", example = "ABC")
	public String kuerzel;

	/** Die schulinterne Kurzbezeichnung */
	@Schema(description = "die schulinterne Kurzbezeichnung", example = "ABC")
	public String kurzbezeichnung;

	/** Das dem Jahrgang zugeordnete Statistik-Kürzel. */
	@Schema(description = "das dem Jahrgang zugeordnete Statistik-Kürzel", example = "EF")
	public String kuerzelStatistik;

	/** Die dem Jahrgang zugeordnete schulinterne Bezeichnung. */
	@Schema(description = "Die dem Jahrgang zugeordnete schulinterne Bezeichnung.", example = "Einführungsphase")
	public @NotNull String bezeichnung = "";

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/** Die ID der Schulgliederung, der der Eintrag zugeordnet ist. */
	@Schema(description = "die ID der Schulgliederung, der der Eintrag zugeordnet ist", example = "***")
	public String kuerzelSchulgliederung;

	/** Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null */
	@Schema(description = "die ID des Folgejahrgangs, sofern einer definiert ist", example = "4712")
	public Long idFolgejahrgang;

	/** Gibt die Anzahl der Restabschnitte bis zum Abschluss bei der Schulform an */
	@Schema(description = "Gibt die Anzahl der Restabschnitte bis zum Abschluss bei der Schulform an", example = "null")
	public Integer anzahlRestabschnitte;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an */
	@Schema(description = "Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an",
			example = "null")
	public Long gueltigVon;

	/** Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen */
	@Schema(description = "Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen",
			example = "null")
	public Long gueltigBis;

	/** Gibt an, ob der Jahrgang in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Jahrgang in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true")
	public Boolean referenziertInAnderenTabellen = null;

	// TODO Weitere Daten

	/**
	 * Leerer Standardkonstruktor.
	 */
	public JahrgangsDaten() {
		// leer
	}

}

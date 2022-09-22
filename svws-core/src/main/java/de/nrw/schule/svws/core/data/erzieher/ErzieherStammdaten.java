package de.nrw.schule.svws.core.data.erzieher;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Auswahl von Daten eines Erziehereintrags aus einer Liste.  
 */
@XmlRootElement
@Schema(description="Die Stammdaten eines Erziehers.")
@TranspilerDTO
public class ErzieherStammdaten {

	/** Die ID des Erziehers. */
	@Schema(required = true, description = "die ID des Erziehers", example="4711")
	public long id;

	/** Die ID des Schülers, welchem der Erzieher zugeordnet ist. */
	@Schema(required = true, description = "die ID des Schülers, welchem der Erzieher zugeordnet ist", example="4712")
	public long idSchueler;
		
	/** Die ID der Art des Erziehereintrages */
	@Schema(required = true, description = "Die ID der Art des Erziehereintrages", example="56")
	public Long idErzieherArt;

	/** Die Titel des Erziehers. */
	@Schema(required = true, description = "Titel des Erziehers", example="Dr. rer. nat.")
	public String titel;

	/** Die Anrede des Erziehers. */
	@Schema(required = true, description = "Anrede des Erziehers", example="Herr")
	public String anrede;

	/** Der Name des Erziehers. */
	@Schema(required = true, description = "Der Nachname des Erziehers", example="Müller")
	public String nachname;

	/** Ggf. Zusatz zum Nachnamen des Erziehers. */
	@Schema(required = true, description = "ggf. ein Zusatz zum Nachnamen", example="von")
	public String zusatzNachname;

	/** Der Vorname des Erziehers. */
	@Schema(required = true, description = "Der Vorname des Erziehers", example="Max")
	public String vorname;

	/** Ggf. der Straßenname im Wohnort des Erziehers. */
	@Schema(required = false, description = "Ggf. der Straßenname im Wohnort des Erziehers.", example="Musterweg")
	public String strassenname;

	/** Ggf. die Hausnummer zur Straße im Wohnort des Erziehers. */
	@Schema(required = false, description = "Ggf. die Hausnummer zur Straße im Wohnort des Erziehers.", example="4711")
	public String hausnummer;

	/** Ggf. der Hausnummerzusatz zur Straße im Wohnort des Erziehers. */
	@Schema(required = false, description = "Ggf. der Hausnummerzusatz zur Straße im Wohnort des Erziehers.", example="a-d")
	public String hausnummerZusatz;

	/** Die ID des Wohnortes des Erziehers. */
	@Schema(required = false, description = "ggf. die ID des Wohnortes", example="4711")
	public Long wohnortID;

	/** Die ID des Ortsteils des Erziehers. */
	@Schema(required = false, description = "ggf. die ID des Ortsteils im Wohnort", example="Muster")	
	public Long ortsteilID;

	/** Gibt an, ob der Erzieher Anschreiben erhält oder nicht. */
	@Schema(required = true, description = "Gibt an, ob der Erzieher Anschreiben erhält oder nicht", example="max@test.de")
	public Boolean erhaeltAnschreiben;

	/** Die E-Mailadresse des Erziehers. */
	@Schema(required = true, description = "Die Email-Adresse des Erziehers", example="max@test.de")
	public String eMail;

	/** Die ID der Staatsangehörigkeit des Erziehers. */
	@Schema(required = false, description = "die ID der Staatsangehörigkeit", example="000")
	public String staatsangehoerigkeitID;

	/** Anmerkungen zum Erziehers. */
	@Schema(required = true, description = "Anmerkungen zum Erzieher", example="was auch immer")
	public String bemerkungen;
	
}

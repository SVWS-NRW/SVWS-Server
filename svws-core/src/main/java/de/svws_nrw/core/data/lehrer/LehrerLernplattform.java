package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten einer Lernplattformeinwilligung bei einem Lehrer.
 */
@XmlRootElement
@Schema(description = "Die Daten der Lernplattformeinwilligung.")
@TranspilerDTO
public class LehrerLernplattform {

	/** Die ID des zugehörigen Lehrers. */
	@Schema(description = "Die ID des zugehörigen Lehrers.", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long idLehrer;

	/** Die ID der Lernplattform. */
	@Schema(description = "Die ID der Lernplattform", example = "4713", accessMode = Schema.AccessMode.READ_ONLY)
	public long idLernplattform;

	/** Die ID der Credential für den Lernplattform-Datensatz. */
	@Schema(description = "Die ID der Credential für den Lernplattform-Datensat", example = "4713", accessMode = Schema.AccessMode.READ_ONLY)
	public long idCredential;

	/** Die Abfrage der Einwilligung zu einer Lernplattform. */
	@Schema(description = "Die Abfrage der Einwilligung zu einer Lernplattform", example = "true")
	public boolean einwilligungAbgefragt;

	/** Die Einwilligung zur Nutzung einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur Nutzung einer Lernplattform.", example = "true")
	public boolean einwilligungNutzung;

	/** Die Einwilligung zur Audiokonferenz einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur Audiokonferenz einer Lernplattform.", example = "true")
	public boolean einwilligungAudiokonferenz;

	/** Die Einwilligung zur VideoKonferenz einer Lernplattform. */
	@Schema(description = "Die Einwilligung zur VideoKonferenz einer Lernplattform.", example = "true")
	public boolean einwilligungVideokonferenz;

	/** Benutzername für die Lernplattform eines Lehrers */
	@Schema(description = "Benutzername für die Lernplattform eines Lehrers.", example = "Mustermann")
	public String benutzername;

	/** Initialkennwort für die Lernplattform eines Lehrers */
	@Schema(description = "Initialkennwort für die Lernplattform eines Lehrers.", example = "InitialeKennwort123")
	public String initialKennwort;
}

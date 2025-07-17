package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-Api-Schnittstelle verwendet.
 * Sie beschreibt wie die Daten der Haltestelle übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Haltestellen")
@TranspilerDTO
public class Haltestelle {

	/** Die ID der Haltestelle */
	@Schema(description = "Die ID der Haltestelle", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Bezeichnung der Haltestelle */
	@Schema(description = "Die Bezeichnung der Haltestelle", example = "RE7 Hauptbahnhof")
	public String bezeichnung;

	/** Die Entfernung zwischen Schule und Haltestelle */
	@Schema(description = "Die Entfernung zwischen Schule und Haltestelle", example = "7")
	public Double entfernungSchule;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example = "true")
	public boolean istAenderbar = true;
}

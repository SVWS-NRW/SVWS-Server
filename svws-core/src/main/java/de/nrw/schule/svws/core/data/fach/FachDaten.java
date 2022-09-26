package de.nrw.schule.svws.core.data.fach;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Faches.  
 */
@XmlRootElement
@Schema(description="Die Daten eines Faches.")
@TranspilerDTO
public class FachDaten {

	/** Die ID des Faches. */
	@Schema(required = true, description = "die ID des Faches", example="42")
	public long id;

	/** Das eindeutige Kürzel des Faches */
	@Schema(required = true, description = "das eindeutige Kürzel des Faches", example="M")
	public String kuerzel;
	
	/** Die Bezeichnung des Faches */
	@Schema(required = true, description = "die Bezeichnung des Faches ", example="Mathematik")
	public String bezeichnung;
	
	/** Das Statistik-Kürzel des Faches */
	@Schema(required = true, description = "das Statistik-Kürzel des Faches ", example="M")
	public String kuerzelStatistik;
	
	
	
	// istFremdsprache
	// Fächergruppe
	// Aufgabenfeld - nur BK
	// Sortierung und Sortierung SII
	// ggf. bilinguale Sachfachsprache
	
	// istSichtbar

	// istNachprüfung erlaubt
	// aufZeugnis?
	// Zeugnis-Bezeichnung
	// Zeugnis-Bezeichnung für Überweisungszeugnisse
	// maximale Zeichenanzahl in Fachbemerkungen
	
	// schriftliches Fach für ZK
	// bei "abgeschlossenen Fächer holen" berücksichtigen
	
	// Fach der Oberstufe
	// TODO GostFachdaten integrieren
	
	
	// TODO Liste der Teilleistungsarten
}

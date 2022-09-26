package de.nrw.schule.svws.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt einen Core-Types für einen Beratungslehrer in der gymnasialen Oberstufe
 * zur Verfügung. 
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description="Informationen zu einem Beratungslehrer.")
@TranspilerDTO
public class GostBeratungslehrer {

	/** Die ID des Lehrers. */
	@Schema(required = true, description = "die ID des Lehrers", example="4711")
	public long id;

	/** Das Kürzel des Lehrers. */
	@Schema(required = true, description = "das Kürzel des Lehrers", example="MUS")
	public String kuerzel;

	/** Der Nachname des Lehrers. */
	@Schema(required = true, description = "der Nachname des Lehrers", example="Mustermann")
	public String nachname;

	/** Der Vorname des Lehrers. */
	@Schema(required = true, description = "der Vorname des Lehrers", example="Max")
	public String vorname;

}

package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Organisationsformen 
 * bei allgemeinbildenden Schulen, bei berufsbildenden Schule und beim 
 * Weiterbildungskolleg.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Organistationsformen.")
@TranspilerDTO
public class OrganisationsformKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel der Organisationsform */
	@Schema(required = true, description = "das Kürzel der Organisationsform", example="1")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung der Organisationsform. */
	@Schema(required = true, description = "die Beschreibung der Organisationsform", example="Teilnahme am gebundenen Ganztag")
	public @NotNull String beschreibung = "";

	/** Die Kürzel der Schulformen, bei welchen die Organisationsform vorkommt. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen die Organisationsform vorkommt")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr die Organisationsform einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr die Organisationsform einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Organisationsform gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr die Organisationsform gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;

	
	/**
	 * Erstellt einen Organisationsform-Eintrag mit Standardwerten
	 */
	public OrganisationsformKatalogEintrag() {
	}


	/**
	 * Erstellt einen Organisationsform-Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel 
	 * @param beschreibung    die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges
	 * @param schulformen     die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public OrganisationsformKatalogEintrag(long id, @NotNull String kuerzel, @NotNull String beschreibung,
			@NotNull List<@NotNull Schulform> schulformen, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		for (@NotNull Schulform schulform : schulformen)
			this.schulformen.add(schulform.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}
	
}

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
 * Sie liefert die gültigen Statistikwerte für den Katalog der Förderschwerpunkte.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Förderschwerpunkte.")
@TranspilerDTO
public class FoerderschwerpunktKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel des Förderschwerpunktes, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "das Kürzel des Förderschwerpunktes, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="SH")
	public @NotNull String kuerzel = "";

	/** Die textuelle Beschreibung des Förderschwerpunktes. */
	@Schema(required = true, description = "die textuelle Beschreibung des Förderschwerpunktes", example="Sehen (Sehbehinderte)")
	public @NotNull String beschreibung = "";

	/** Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr der Förderschwerpunkt einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Förderschwerpunkt einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Förderschwerpunkt gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Förderschwerpunkt-Eintrag mit Standardwerten
	 */
	public FoerderschwerpunktKatalogEintrag() {
	}


	/**
	 * Erstellt einen Förderschwerpunkt-Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel 
	 * @param beschreibung    die textuelle Beschreibung des Förderschwerpunktes
	 * @param schulformen     die Schulformen, bei welchen der Förderschwerpunkt vorkommt
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public FoerderschwerpunktKatalogEintrag(long id, @NotNull String kuerzel, @NotNull String beschreibung,  
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

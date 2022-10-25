package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der allgemeinen Merkmale bei Schulen
 * und Schüler.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der allgemeinen Merkmale.")
@TranspilerDTO
public class AllgemeineMerkmaleKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel des allgemeinen Merkmals */
	@Schema(required = true, description = "das Kürzel des allgemeinen Merkmals", example="GANZTAG")
	public @NotNull String kuerzel = "";

	/** Eine kurze Bezeichnung für das allgemeine Merkmal. */
	@Schema(required = true, description = "eine kurze Bezeichnung für das allgemeine Merkmal", example="Ganztagsschule")
	public @NotNull String bezeichnung = "";

	/** Gibt an, das das Merkmal bei der Schule gesetzt werden kann. */
	@Schema(required = true, description = "gibt an, das das Merkmal bei der Schule gesetzt werden kann", example="false")
	public boolean beiSchule = false;
	
	/** Gibt an, das das Merkmal bei einem Schüler gesetzt werden kann. */
	@Schema(required = true, description = "gibt an, das das Merkmal bei einem Schüler gesetzt werden kann", example="false")
	public boolean beiSchueler = false;
	
	/** Ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird. */
	@Schema(required = true, description = "ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="X")
	public String kuerzelASD = "";

	/** Die Kürzel der Schulformen, bei welchen das allgemeine Merkmal vorkommen kann. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen das allgemeine Merkmal vorkommen kann")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public AllgemeineMerkmaleKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param bezeichnung     die Bezeichnung des Merkmals
	 * @param beiSchule       gibt an, das das Merkmal bei der Schule gesetzt werden kann
	 * @param beiSchueler     gibt an, das das Merkmal bei einem Schüler gesetzt werden kann
	 * @param kuerzelASD      ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird
	 * @param schulformen     die Schulformen, bei welchen das allgemeine Merkmal vorkommen kann
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public AllgemeineMerkmaleKatalogEintrag(long id, @NotNull String kuerzel, @NotNull String bezeichnung,
			boolean beiSchule, boolean beiSchueler, String kuerzelASD, 
			@NotNull List<@NotNull Schulform> schulformen, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.beiSchule = beiSchule;
		this.beiSchueler = beiSchueler;
		this.kuerzelASD = kuerzelASD;
		for (@NotNull Schulform sf : schulformen)
			this.schulformen.add(sf.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

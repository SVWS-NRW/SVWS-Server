package de.nrw.schule.svws.core.data.kaoa;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA 
 * gültigen Einträge für die SBO Ebene 4.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Einträge für die SBO Ebene 4.")
@TranspilerDTO
public class KAOAEbene4Eintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="1")
	public long id;

	/** Das Kürzel des Eintrags für die SBO Ebene 4. */
	@Schema(required = true, description = "das Kürzel des Eintrags für die SBO Ebene 4", example="SBO 6.5.1.1")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung des Eintrags für die SBO Ebene 4. */
	@Schema(required = true, description = "die Beschreibung des Eintrags für die SBO Ebene 4", example="Langzeitpraktikum 1-tägig")
	public @NotNull String beschreibung = "";

    /** Das Zusatzmerkmal, welcher der Eintrag zugeordnet ist. */
    @Schema(required = true, description = "das Zusatzmerkmal, welcher der Eintrag zugeordnet ist", example="SBO 6.5.1")
    public @NotNull String zusatzmerkmal = "";
	
	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Eintrag der SBO Ebene 4 mit Standardwerten
	 */
	public KAOAEbene4Eintrag() {
	}

	/**
	 * Erstellt einen KAoA-Eintrag der SBO Ebene 4 mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param zusatzmerkmal  das Zusatzmerkmal, dem der Eintrag der SBO Ebene 4 zugeordnet ist
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOAEbene4Eintrag(long id, @NotNull String kuerzel, @NotNull String beschreibung, 
	        @NotNull KAOAZusatzmerkmal zusatzmerkmal, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.zusatzmerkmal = zusatzmerkmal.daten.kuerzel;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

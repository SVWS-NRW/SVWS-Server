package de.nrw.schule.svws.core.data.kaoa;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.kaoa.KAOAMerkmal;
import de.nrw.schule.svws.core.types.kaoa.KAOAZusatzmerkmaleOptionsarten;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA 
 * gültigen Zusatzmerkmalen.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der KAoA-Zusatzmerkmale.")
@TranspilerDTO
public class KAOAZusatzmerkmalEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="1")
	public long id;

	/** Das Kürzel des Zusatzmerkmals. */
	@Schema(required = true, description = "das Kürzel des Zusatzmerkmals", example="SBO 2.1.1")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung des Zusatzmerkmals. */
	@Schema(required = true, description = "die Beschreibung des Zusatzmerkmals", example="Schulisches individuelles Beratungsgespräch durchgeführt")
	public @NotNull String beschreibung = "";

    /** Das Merkmal, welcher das Zusatzmerkmal zugeordnet ist. */
    @Schema(required = true, description = "das Merkmal, welcher das Zusatzmerkmal zugeordnet ist", example="SBO 2.1")
    public @NotNull String merkmal = "";
	
    /** Die Optionsart des Zusatzmerkmals. */
    @Schema(required = true, description = "die Optionsart des Zusatzmerkmals", example="Anschlussoption")
    public String optionsart = null;
	
	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit Standardwerten
	 */
	public KAOAZusatzmerkmalEintrag() {
	}

	/**
	 * Erstellt einen KAoA-Zusatzmerkmal-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param merkmal        das Merkmal, dem das Zusatzmerkmal zugeordnet ist
     * @param optionsart     die Optionsart bei dem Zusatzmerkmal
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOAZusatzmerkmalEintrag(long id, @NotNull String kuerzel, @NotNull String beschreibung, 
	        @NotNull KAOAMerkmal merkmal, @NotNull KAOAZusatzmerkmaleOptionsarten optionsart,
	        Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.merkmal = merkmal.daten.kuerzel;
		this.optionsart = optionsart.kuerzel;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

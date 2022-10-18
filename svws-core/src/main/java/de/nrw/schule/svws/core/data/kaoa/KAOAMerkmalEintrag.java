package de.nrw.schule.svws.core.data.kaoa;

import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.kaoa.KAOAKategorie;
import de.nrw.schule.svws.core.types.kaoa.KAOAMerkmaleOptionsarten;
import de.nrw.schule.svws.core.types.statkue.Schulgliederung;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA 
 * gültigen Merkmalen.   
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der KAoA-Merkmale.")
@TranspilerDTO
public class KAOAMerkmalEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="1")
	public long id;

	/** Das Kürzel des Merkmals. */
	@Schema(required = true, description = "das Kürzel des Merkmals", example="SBO 2")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung des Merkmals. */
	@Schema(required = true, description = "die Beschreibung des Merkmals", example="Formen der Orientierung und Beratung")
	public @NotNull String beschreibung = "";

    /** Die Kategorie, welcher das Merkmal zugeordnet ist. */
    @Schema(required = true, description = "die Kategorie, welcher das Merkmal zugeordnet ist", example="Formen der Orientierung und Beratung")
    public @NotNull String kategorie = "";
	
    /** Die Optionsart des Merkmals. */
    @Schema(required = true, description = "die Optionsart des Merkmals", example="Formen der Orientierung und Beratung")
    public String optionsart = null;
	
    /** Die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf */
    @Schema(required = true, description = "die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf")  
    public @NotNull List<@NotNull String> bkAnlagen = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Merkmal-Eintrag mit Standardwerten
	 */
	public KAOAMerkmalEintrag() {
	}

	/**
	 * Erstellt einen KAoA-Merkmal-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param kategorie      die Kategorie, dem das Merkmal zugeordnet ist
     * @param optionsart     die Optionsart bei dem Merkmal
     * @param bkAnlagen      die zulässigen Anlagen beim Berufskolleg
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOAMerkmalEintrag(long id, @NotNull String kuerzel, @NotNull String beschreibung, 
	        @NotNull KAOAKategorie kategorie, @NotNull KAOAMerkmaleOptionsarten optionsart,
	        @NotNull List<@NotNull Schulgliederung> bkAnlagen, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.kategorie = kategorie.daten.kuerzel;
		this.optionsart = optionsart.kuerzel;
		for (Schulgliederung gl : bkAnlagen) {
		    if (gl.daten.bkAnlage == null)
		        throw new NullPointerException("Es wurde keine Gliederung des Berufskollges als Anlage angegeben.");
		    this.bkAnlagen.add(gl.daten.kuerzel);
		}
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

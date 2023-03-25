package de.svws_nrw.core.data.kaoa;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA 
 * gültigen Kategorien.   
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der KAoA-Kategorien.")
@TranspilerDTO
public class KAOAKategorieEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="1")
	public long id;

	/** Das Kürzel der Kategorie. */
	@Schema(required = true, description = "das Kürzel der Kategorie", example="SBO 2")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung der Kategorie. */
	@Schema(required = true, description = "die Beschreibung der Kategorie", example="Formen der Orientierung und Beratung")
	public @NotNull String beschreibung = "";

    /** Jahrgangsstufen in denen der Eintrag gemacht werden darf */
    @Schema(required = true, description = "Jahrgangsstufen in denen der Eintrag gemacht werden darf")  
    public @NotNull List<@NotNull String> jahrgaenge = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Kategorie-Eintrag mit Standardwerten
	 */
	public KAOAKategorieEintrag() {
	}

	/**
	 * Erstellt einen KAoA-Kategorie-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param jahrgaenge     die zulässigen Jahrgänge
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOAKategorieEintrag(final long id, final @NotNull String kuerzel, final @NotNull String beschreibung, 
	        final @NotNull List<@NotNull Jahrgaenge> jahrgaenge, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		for (final Jahrgaenge j : jahrgaenge)
		    this.jahrgaenge.add(j.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

package de.svws_nrw.core.data.kaoa;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA 
 * gültigen Berufsfelder.   
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der KAoA-Berufsfelder.")
@TranspilerDTO
public class KAOABerufsfeldEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="1")
	public long id;

	/** Das Kürzel des Berufsfeldes. */
	@Schema(required = true, description = "das Kürzel des Berufsfeldes", example="BAV")
	public @NotNull String kuerzel = "";
	
	/** Die Beschreibung des Berufsfeldes. */
	@Schema(required = true, description = "die Beschreibung des Berufsfeldes", example="Bau, Architektur, Vermessung")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2020")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen KAoA-Berufsfeld-Eintrag mit Standardwerten
	 */
	public KAOABerufsfeldEintrag() {
	}

	/**
	 * Erstellt einen KAoA-Berufsfeld-Eintrag mit den angegebenen Werten
	 * 
	 * @param id             die ID
	 * @param kuerzel        das Kürzel 
	 * @param beschreibung   die Beschreibung
	 * @param gueltigVon     das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis     das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public KAOABerufsfeldEintrag(final long id, final @NotNull String kuerzel, final @NotNull String beschreibung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

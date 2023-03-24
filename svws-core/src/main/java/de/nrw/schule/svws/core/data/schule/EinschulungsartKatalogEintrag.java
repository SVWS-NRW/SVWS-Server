package de.nrw.schule.svws.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der Einschulungsarten.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Einschulungsarten.")
@TranspilerDTO
public class EinschulungsartKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel der Einschulungsart, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "das Kürzel der Einschulungsart, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="51")
	public @NotNull String kuerzel = "";

	/** Eine kurze Bezeichnung für die Einschulungsart. */
	@Schema(required = true, description = "eine kurze Bezeichnung für die Einschulungsart", example="6. Lebensjahr vollendet")
	public @NotNull String bezeichnung = "";

	/** Die textuelle Beschreibung der Einschulungsart. */
	@Schema(required = true, description = "die textuelle Beschreibung der Einschulungsart", example="Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public EinschulungsartKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel der Einschulungsart
	 * @param bezeichnung     eine kurze Bezeichnung der Einschulungsart
	 * @param beschreibung    die Beschreibung der Einschulungsart
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public EinschulungsartKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung,
			final @NotNull String beschreibung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

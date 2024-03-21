package de.svws_nrw.core.data.jahrgang;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der zulässigen Jahrgänge.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der zulässigen Jahrgänge.")
@TranspilerDTO
public class PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "276000")
	public long id;

	/** Das Kürzel für die Besuchsjahre */
	@Schema(description = "das Kürzel für die Besuchsjahre", example = "E1")
	public @NotNull String kuerzel = "";

	/** Der Text für die Besuchsjahre */
	@Schema(description = "der Text für die Besuchsjahre", example = "Schuleingangsphase, 1. Schulbesuchsjahr")
	public @NotNull String text = "";

	/** Gibt an, in welchem Schuljahr die Besuchsjahre ergänzt wurden. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Besuchsjahre ergänzt wurden. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Besuchsjahre verwendet werden. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Besuchsjahre verwendet werden. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag() {
	}


	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id            die ID des Katalog-Eintrags
	 * @param kuerzel       das Kürzel für die Besuchsjahre
	 * @param text          die textuelle Bezeichung für die Besuchsjahre
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag(final long id, final @NotNull String kuerzel,
			final @NotNull String text, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

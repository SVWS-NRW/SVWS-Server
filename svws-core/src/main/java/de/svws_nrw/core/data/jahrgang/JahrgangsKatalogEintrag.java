package de.svws_nrw.core.data.jahrgang;

import java.util.List;
import java.util.Vector;

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
public class JahrgangsKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "276000")
	public long id;

	/** Das 2-stellige Jahrgangskürzel */
	@Schema(description = "das 2-stellige Jahrgangskürzel", example = "EF")
	public @NotNull String kuerzel = "";

	/** Die Jahrgangsbezeichungen bei den zulässigen Schulformen. */
	@Schema(description = "die Jahrgangsbezeichungen bei den zulässigen Schulformen")
	public @NotNull List<@NotNull JahrgangsKatalogEintragBezeichnung> bezeichnungen = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr der Jahrgang ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Jahrgang ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Jahrgang verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Jahrgang verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public JahrgangsKatalogEintrag() {
	}


	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID des Katalog-Eintrags
	 * @param kuerzel         das zweistellige Kürzel des Jahrgangs
	 * @param bezeichnungen   die Jahrgangsbezeichungen bei den zulässigen Schulformen
	 * @param gueltigVon    das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis    das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public JahrgangsKatalogEintrag(final long id, final @NotNull String kuerzel, 
			final @NotNull List<@NotNull JahrgangsKatalogEintragBezeichnung> bezeichnungen, 
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnungen = bezeichnungen;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	

}

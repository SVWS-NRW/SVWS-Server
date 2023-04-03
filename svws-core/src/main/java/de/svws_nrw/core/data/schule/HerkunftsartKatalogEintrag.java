package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für den Katalog der Herkunftsarten.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Herkunftsarten.")
@TranspilerDTO
public class HerkunftsartKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example="99000")
	public long id;

	/** Das 2-stellige Kürzel der Herkunftsart */
	@Schema(description = "das 2-stellige Kürzel der Herkunftsart", example="99")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnungen bei den jeweils zulässigen Schulformen. */
	@Schema(description = "die Bezeichnungen bei den jeweils zulässigen Schulformen")
	public @NotNull List<@NotNull HerkunftsartKatalogEintragBezeichnung> bezeichnungen = new Vector<>();
	
	/** Gibt an, in welchem Schuljahr die Herkunftsart ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Herkunftsart ergänzt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Herkunftsart verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Herkunftsart verwendet wird. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Katalog-Eintrag mit Standardwerten
	 */
	public HerkunftsartKatalogEintrag() {
	}


	/**
	 * Erstellt einen Katalog-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID des Katalog-Eintrags
	 * @param kuerzel         das 2-stellige Kürzel der Herkunftsart
	 * @param bezeichnungen   die Bezeichnungen bei den jeweils zulässigen Schulformen
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public HerkunftsartKatalogEintrag(final long id, final @NotNull String kuerzel, 
			final @NotNull List<@NotNull HerkunftsartKatalogEintragBezeichnung> bezeichnungen, 
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnungen = bezeichnungen;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}	

}

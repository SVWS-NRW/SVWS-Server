package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gütligen Statistikwerte für den Katalog der LehrerAbgangsgründe.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Lehrer-Abgangsgründe.")
@TranspilerDTO
public class LehrerKatalogAbgangsgrundEintrag {

	/** Die ID des Abgangsgrundes. */
	@Schema(description = "die ID des Abgangsgrundes", example = "4711")
	public long id;

	/** Das Kürzel für den Abgangsgrund. */
	@Schema(description = "das Kürzel für den Abgangsgrund", example = "RUHEST")
	public @NotNull String kuerzel = "";

	/** Der Klartext des Abgangsgrunds. */
	@Schema(description = "die textuelle Beschreibung des Abgangsgrunds", example = "Eintritt in den Ruhestand")
	public @NotNull String text = "";

	/** Der Schlüssel für die ASD-Schulstatistik.*/
	@Schema(description = "der Schlüssel des Abgangsgrund für die ASD-Schulstatistik", example = "11")
	public @NotNull String schluessel = "";

	/** Gibt an, in welchem Schuljahr der Abgangsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr der Abgangsgrund einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Abgangsgrund gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Abgangsgrund  gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Abgangsgrund-Eintrag mit Standardwerten
	 */
	public LehrerKatalogAbgangsgrundEintrag() {
	}


	/**
	 * Erstellt einen Abgangsgrund-Eintrag mit den angegebenen Werten
	 *
	 * @param id           die ID
	 * @param kuerzel      das Kürzel
	 * @param text         die textuelle Beschreibung
	 * @param schluessel   der Schlüssel für die ASD-Schulstatistik
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public LehrerKatalogAbgangsgrundEintrag(final long id, final @NotNull String kuerzel, final @NotNull String text,
			final @NotNull String schluessel, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.text = text;
		this.schluessel = schluessel;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

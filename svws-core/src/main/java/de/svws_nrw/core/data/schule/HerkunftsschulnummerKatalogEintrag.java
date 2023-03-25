package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der zusätzlichen Schulnummern für Herkünfte 
 * aus anderen Bundesländern und Ländern.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Kursarten.")
@TranspilerDTO
public class HerkunftsschulnummerKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Die Herkunfts-Schulnummer */
	@Schema(required = true, description = "die Herkunfts-Schulnummer", example="999000")
	public int schulnummer = -1;
	
	/** Die Bezeichnung der Herkunfts-Schulnummer */
	@Schema(required = true, description = "die Bezeichnung der Herkunfts-Schulnummer", example="Schule aus dem sonstigen Ausland")
	public @NotNull String bezeichnung = "";
	
	/** Die Informationen zu Schulformen und -gliederungen, wo die Herkunfts-Schulnummer zulässig ist. */
	@Schema(required = true, description = "die Informationen zu Schulformen und -gliederungen, wo die Herkunfts-Schulnummer zulässig ist.")
	public @NotNull List<@NotNull SchulformSchulgliederung> zulaessig = new Vector<>();

	/** Gibt an, in welchem Schuljahr die Herkunfts-Schulnummer einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem die Herkunfts-Schulnummer einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Herkunfts-Schulnummer gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem die Herkunfts-Schulnummer gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftsschulnummerKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param schulnummer        die Nummer der Herkunfts-Schulnummer
	 * @param bezeichnung        die Bezeichnung der Kursart
	 * @param zulaessig          die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist 
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                           "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public HerkunftsschulnummerKatalogEintrag(final long id, final int schulnummer, final @NotNull String bezeichnung, 
			final List<@NotNull Pair<@NotNull Schulform, Schulgliederung>> zulaessig, 
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.schulnummer = schulnummer;
		this.bezeichnung = bezeichnung;
		if (zulaessig != null) {
			for (final @NotNull Pair<@NotNull Schulform, Schulgliederung> zul : zulaessig) {
				final SchulformSchulgliederung sfsgl = new SchulformSchulgliederung();
				final @NotNull Schulform sf = zul.a;
				if (sf.daten == null)
					continue;
				sfsgl.schulform = sf.daten.kuerzel;
				final Schulgliederung sgl = zul.b;
				sfsgl.gliederung = ((sgl == null) || (sgl.daten == null)) ? null : sgl.daten.kuerzel;
				this.zulaessig.add(sfsgl);
			}
		}
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

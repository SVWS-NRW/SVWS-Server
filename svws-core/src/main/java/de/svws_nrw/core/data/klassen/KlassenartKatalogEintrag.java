package de.svws_nrw.core.data.klassen;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.schule.SchulformSchulgliederung;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der zulässigen Klassenarten und die Information für welche Schulformen
 * diese zulässig sind.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Klassenarten.")
@TranspilerDTO
public class KlassenartKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das eindeutige Kürzel der Klassenart entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Schema(description = "das eindeutige Kürzel der Klassenart entsprechend der Vorgaben der amtlichen Schulstatistik", example="RK")
	public @NotNull String kuerzel = "";
	
	/** Die Bezeichnung der Klassenart */
	@Schema(description = "die Bezeichnung der Klassenart", example="Regelklasse")
	public @NotNull String bezeichnung = "";
	
	/** Die Informationen zu Schulformen und -gliederungen, wo die Klassenart zulässig ist. */
	@Schema(description = "die Informationen zu Schulformen und -gliederungen, wo die Klassenart zulässig ist.")
	public @NotNull List<@NotNull SchulformSchulgliederung> zulaessig = new Vector<>();

	/** Gibt an, in welchem Schuljahr die Klassenart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem die Klassenart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Klassenart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem die Klassenart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public KlassenartKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param bezeichnung        die Bezeichnung der Kursart
	 * @param zulaessig          die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist 
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                           "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public KlassenartKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung, 
			final @NotNull List<@NotNull Pair<@NotNull Schulform, Schulgliederung>> zulaessig, 
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
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
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

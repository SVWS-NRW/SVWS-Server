package de.svws_nrw.core.data.kurse;

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
 * Sie liefert den Katalog der Kursarten und die Information für welche Schulformen
 * diese zulässig sind.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Kursarten.")
@TranspilerDTO
public class KursartKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das eindeutige Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Schema(required = true, description = "das eindeutige Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik", example="AB3")
	public @NotNull String kuerzel = "";
	
	/** Die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Schema(required = true, description = "die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik", example="71")
	public @NotNull String nummer = "";

	/** Die Bezeichnung der Kursart */
	@Schema(required = true, description = "die Bezeichnung der Kursart", example="3. Abiturfach")
	public @NotNull String bezeichnung = "";
	
	/** Ergänzende Bemerkungen zu der Kursart */
	@Schema(required = true, description = "ergänzende Bemerkungen zu der Kursart", example="gemäß § 9 Abs. 2, 3  SchulG")
	public String bemerkungen = null;
	
	/** Das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist */
	@Schema(required = true, description = "das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist", example="GK")
	public String kuerzelAllg = null;
	
	/** Die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist */
	@Schema(required = true, description = "die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist", example="Grundkurs")
	public String bezeichnungAllg = null;
	
	/** Gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist */
	@Schema(required = true, description = "gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist", example="true")
	public boolean erlaubtGOSt = false;
	
	/** Die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist. */
	@Schema(required = true, description = "die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist.")
	public @NotNull List<@NotNull SchulformSchulgliederung> zulaessig = new Vector<>();

	/** Gibt an, in welchem Schuljahr die Kursart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem die Kursart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Kursart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem die Kursart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public KursartKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param nummer             die Nummer der Kursart entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param bezeichnung        die Bezeichnung der Kursart
	 * @param bemerkungen        ergänzende Bemerkungen zu der Kursart
	 * @param kuerzelAllg        das Kürzel einer verallgemeinerten Kursart, sofern diese angegeben ist
	 * @param bezeichnungAllg    die Bezeichnung der verallgemeinerter Kursart, sofern diese angegeben ist
	 * @param erlaubtGOSt        gibt an, ob die Kursart in der Gymnasialen Oberstufe zulässig ist
	 * @param zulaessig          die Informationen zu Schulformen und -gliederungen, wo die Kursart zulässig ist 
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                           "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public KursartKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String nummer, 
			final @NotNull String bezeichnung, final String bemerkungen, final String kuerzelAllg, final String bezeichnungAllg, final boolean erlaubtGOSt,
			final @NotNull List<@NotNull Pair<@NotNull Schulform, Schulgliederung>> zulaessig, 
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.nummer = nummer;
		this.bezeichnung = bezeichnung;
		this.bemerkungen = bemerkungen;
		this.kuerzelAllg = kuerzelAllg;
		this.bezeichnungAllg = bezeichnungAllg;
		this.erlaubtGOSt = erlaubtGOSt;
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

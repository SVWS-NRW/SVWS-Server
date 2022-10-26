package de.nrw.schule.svws.core.data.fach;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.core.data.schule.SchulformSchulgliederung;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.fach.Fachgruppe;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert den Katalog der Fächer und die Information für welche Schulformen
 * diese zulässig sind.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Fächer.")
@TranspilerDTO
public class FachKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Schema(required = true, description = "das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik", example="S0")
	public @NotNull String kuerzelASD = "";
	
	/** Die texttuelle Beschreibung des Faches */
	@Schema(required = true, description = "die texttuelle Beschreibung des Faches", example="Fach Spanisch, regulärer Beginn in der Einführungsphase")
	public @NotNull String bezeichnung = "";
	
	/** Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik */
	@Schema(required = true, description = "das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik", example="S")
	public @NotNull String kuerzel = "";
	
	/** Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3) */
	@Schema(required = true, description = "das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)", example="1")
	public Integer aufgabenfeld = -1;
	
	/** Das Kürzel der zugeordneten Fachgruppe */
	@Schema(required = true, description = "das Kürzel der zugeordneten Fachgruppe", example="FS")
	public String fachgruppe = "";
	
	/** Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen) */
	@Schema(required = true, description = "der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen) - Teil des Kürzels für die amtliche Schulstatistik", example="EF")
	public String abJahrgang = "";
	
	/** Gibt an, ob es sich um eine Fremdsprache handelt */
	@Schema(required = true, description = "gibt an, ob es sich um eine Fremdsprache handelt", example="true")
	public boolean istFremdsprache = false; 
	
	/** Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	@Schema(required = true, description = "gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)", example="false")
	public boolean istHKFS = false;
	
	/** Gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird. */
	@Schema(required = true, description = "gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird", example="false")
	public boolean istAusRegUFach = false;
	
	/** Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) */
	@Schema(required = true, description = "gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)", example="false")
	public boolean istErsatzPflichtFS = false; 
	
	/** Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik */
	@Schema(required = true, description = "gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik", example="false")
	public boolean istKonfKoop = false; 
	
	/** Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird. */
	@Schema(required = true, description = "gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird", example="true")
	public boolean nurSII = false;
	
	/** Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht", example="true")
	public boolean exportASD = false;

	/** Die Informationen zu Schulformen und -gliederungen, wo das Fach zulässig ist. */
	@Schema(required = true, description = "die Informationen zu Schulformen und -gliederungen, wo das Fach zulässig ist.")
	public @NotNull List<@NotNull SchulformSchulgliederung> zulaessig = new Vector<>();

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem die Kursart einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem die Kursart gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public FachKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                    die ID
	 * @param kuerzelASD            das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param bezeichnung           die texttuelle Beschreibung des Faches
	 * @param kuerzel               das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) 
	 *                              - Teil des Kürzels für die amtliche Schulstatistik 
	 * @param aufgabenfeld          das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 * @param fachgruppe            das Kürzel der zugeordneten Fachgruppe
	 * @param abJahrgang            der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 * @param istFremdsprache       gibt an, ob es sich um eine Fremdsprache handelt
	 * @param istHKFS               gibt an, ob es sich um ein Fach der Herkuntftsprache handelt 
	 *                              (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer 
	 *                              Pflichtfremdsprache)
	 * @param istAusRegUFach        gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird
	 * @param istErsatzPflichtFS    gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt 
	 *                              (siehe auch istHKFS)
	 * @param istKonfKoop           gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht
	 *                              - Teil des Kürzels für die amtliche Schulstatistik 
	 * @param nurSII                gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird
	 * @param exportASD             gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden 
	 *                              soll oder nicht
	 * @param zulaessig             die Informationen zu Schulformen und -gliederungen, wo das Fach zulässig ist 
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und 
	 *                              "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */	
	public FachKatalogEintrag(long id, 	@NotNull String kuerzelASD, @NotNull String bezeichnung, @NotNull String kuerzel, 
			Integer aufgabenfeld, Fachgruppe fachgruppe, Jahrgaenge abJahrgang, boolean istFremdsprache, boolean istHKFS, 
			boolean istAusRegUFach, boolean istErsatzPflichtFS, boolean istKonfKoop, boolean nurSII, boolean exportASD,
			@NotNull List<@NotNull Pair<@NotNull Schulform, Schulgliederung>> zulaessig,
			Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzelASD = kuerzelASD;
		this.bezeichnung = bezeichnung;
		this.kuerzel = kuerzel;
		this.aufgabenfeld = aufgabenfeld;
		this.fachgruppe = (fachgruppe == null) ? null : fachgruppe.daten.kuerzel;
		this.abJahrgang = (abJahrgang == null) ? null : abJahrgang.daten.kuerzel;
		this.istFremdsprache = istFremdsprache;
		this.istHKFS = istHKFS;
		this.istAusRegUFach = istAusRegUFach;
		this.istErsatzPflichtFS = istErsatzPflichtFS;
		this.istKonfKoop = istKonfKoop;
		this.nurSII = nurSII;
		this.exportASD = exportASD;
		for (@NotNull Pair<@NotNull Schulform, Schulgliederung> zul : zulaessig) {
			SchulformSchulgliederung sfsgl = new SchulformSchulgliederung();
			@NotNull Schulform sf = zul.a;
			if (sf.daten == null)
				continue;
			sfsgl.schulform = sf.daten.kuerzel;
			Schulgliederung sgl = zul.b;
			sfsgl.gliederung = ((sgl == null) || (sgl.daten == null)) ? null : sgl.daten.kuerzel;
			this.zulaessig.add(sfsgl);
		}
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

package de.svws_nrw.core.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.BerufskollegAnlage;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class SchulgliederungKatalogEintrag extends CoreTypeData {

	/** Das Kürzel der Schulgliederung, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(description = "das Kürzel der Schulgliederung, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example = "A01")
	public @NotNull String kuerzel = "";

	/** Gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt", example = "true")
	public boolean istBK = false;

	/** Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

	/** Gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt. */
	@Schema(description = "gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt", example = "false")
	public boolean istAuslaufend = false;

	/** Gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt. */
	@Schema(description = "gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt", example = "false")
	public boolean istAusgelaufen = false;

	/** Die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges. */
	@Schema(description = "die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges", example = "Fachklassen (BS; TZ)")
	public @NotNull String beschreibung = "";

	/** Die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example = "A")
	public String bkAnlage = null;

	/** Der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example = "01")
	public String bkTyp = null;

	/** Der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein. */
	@Schema(description = "der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein.",
			example = "10")
	public Integer bkIndex = null;

	/** Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht */
	@Schema(description = "gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht.", example = "false")
	public boolean istVZ = false;

	/** Gibt eine Liste von berufsbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "gibt den berufsbildenden Abschluss an, der in diesem Bildungsgang erreicht werden kann, wenn es sich um einen Bildungsgang am Berufskolleg handelt",
			example = "BS")
	public @NotNull List<String> bkAbschlussBerufsbildend = new ArrayList<>();

	/** Gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(description = "gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt",
			example = "HA9")
	public @NotNull List<String> bkAbschlussAllgemeinbildend = new ArrayList<>();


	/**
	 * Erstellt einen Schulgliederung-Eintrag mit Standardwerten
	 */
	public SchulgliederungKatalogEintrag() {
	}


	/**
	 * Erstellt einen Schulgliederung-Eintrag mit den angegebenen Werten
	 *
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param istBK           gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param schulformen     die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt
	 * @param istAuslaufend   gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt
	 * @param istAusgelaufen  gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt
	 * @param beschreibung    die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges
	 * @param bkAnlage        die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param bkTyp           der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param bkIndex         der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei
	 *                        unterschiedlichen Gliederungen identisch sein
	 * @param istVZ           gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht
	 * @param bkAbschlussBerufsbildend
	 *                        gibt eine Liste von berufsbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können,
	 *                        wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param bkAbschlussAllgemeinbildend
	 *                        gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können,
	 *                        wenn es sich um einen Bildungsgang am Berufskolleg handelt
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public SchulgliederungKatalogEintrag(final long id, final @NotNull String kuerzel, final boolean istBK,
			final @NotNull List<Schulform> schulformen, final boolean istAuslaufend, final boolean istAusgelaufen,
			final @NotNull String beschreibung, final BerufskollegAnlage bkAnlage, final String bkTyp, final Integer bkIndex, final boolean istVZ,
			final List<SchulabschlussBerufsbildend> bkAbschlussBerufsbildend,
			final List<SchulabschlussAllgemeinbildend> bkAbschlussAllgemeinbildend,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.istBK = istBK;
		for (final @NotNull Schulform schulform : schulformen)
			this.schulformen.add(schulform.daten.kuerzel);
		this.istAuslaufend = istAuslaufend;
		this.istAusgelaufen = istAusgelaufen;
		this.beschreibung = beschreibung;
		this.bkAnlage = (bkAnlage == null) ? null : bkAnlage.daten.kuerzel;
		this.bkTyp = bkTyp;
		this.bkIndex = bkIndex;
		this.istVZ = istVZ;
		if (bkAbschlussBerufsbildend != null)
			for (final @NotNull SchulabschlussBerufsbildend sbb : bkAbschlussBerufsbildend)
				this.bkAbschlussBerufsbildend.add(sbb.daten.kuerzel);
		if (bkAbschlussAllgemeinbildend != null)
			for (final @NotNull SchulabschlussAllgemeinbildend sab : bkAbschlussAllgemeinbildend)
				this.bkAbschlussAllgemeinbildend.add(sab.daten.kuerzel);
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

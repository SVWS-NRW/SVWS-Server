package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.schule.BerufskollegAnlage;
import de.nrw.schule.svws.core.types.schule.SchulabschlussAllgemeinbildend;
import de.nrw.schule.svws.core.types.schule.SchulabschlussBerufsbildend;
import de.nrw.schule.svws.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class SchulgliederungKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel der Schulgliederung, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "das Kürzel der Schulgliederung, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="A01")
	public @NotNull String kuerzel = "";

	/** Gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(required = true, description = "gibt an, ob es sich um einen Bildungsgang am Berufskolleg handelt", example="true")
	public boolean istBK = false;
	
	/** Die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen die Schulgliederung vorkommt")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt. */
	@Schema(required = true, description = "gibt an, ob es sich um eine auslaufende Schulgliederung oder einen auslaufenden Bildungsgang handelt", example="false")
	public boolean istAuslaufend = false;

	/** Gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt. */
	@Schema(required = true, description = "gibt an, ob es sich um eine ausgelaufene Schulgliederung oder einen ausgelaufenen Bildungsgang handelt", example="false")
	public boolean istAusgelaufen = false;

	/** Die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges. */
	@Schema(required = true, description = "die textuelle Beschreibung der Schulgliederung bzw. des Bildungsganges", example="Fachklassen (BS; TZ)")
	public @NotNull String beschreibung = "";

	/** Die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(required = true, description = "die Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example="A")
	public String bkAnlage = null;

	/** Der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(required = true, description = "der Typ der Anlage, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example="01")
	public String bkTyp = null;

	/** Der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein. */
	@Schema(required = true, description = "der Index für den Zugriff auf die Fachklassen am Berufskolleg. Dieser kann bei unterschiedlichen Gliederungen identisch sein.", example="10")
	public Integer bkIndex = null;

	/** Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht */
	@Schema(required = true, description = "gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt oder nicht.", example="false")
	public boolean istVZ = false;

	/** Gibt eine Liste von berufsbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(required = true, description = "gibt den berufsbildenden Abschluss an, der in diesem Bildungsgang erreicht werden kann, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example="BS")
	public @NotNull List<@NotNull String> bkAbschlussBerufsbildend = new Vector<>(); 

	/** Gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt. */
	@Schema(required = true, description = "gibt eine Liste von allgemeinbildenden Abschlüssen an, die in diesem Bildungsgang erreicht werden können, wenn es sich um einen Bildungsgang am Berufskolleg handelt", example="HA9")
	public @NotNull List<@NotNull String> bkAbschlussAllgemeinbildend = new Vector<>();

	/** Gibt an, in welchem Schuljahr die Schulgliederung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem die Schulgliederung einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem die Schulgliederung gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


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
			final @NotNull List<@NotNull Schulform> schulformen, final boolean istAuslaufend, final boolean istAusgelaufen,
			final @NotNull String beschreibung, final BerufskollegAnlage bkAnlage, final String bkTyp, final Integer bkIndex, final boolean istVZ,
			final List<@NotNull SchulabschlussBerufsbildend> bkAbschlussBerufsbildend, 
			final List<@NotNull SchulabschlussAllgemeinbildend> bkAbschlussAllgemeinbildend, 
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

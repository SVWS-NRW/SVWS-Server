package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkunftsschulformen.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der möglichen Herkunftsschulformen.")
@TranspilerDTO
public class HerkunftSchulformKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Schulform verwendet wird */
	@Schema(required = true, description = "das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Schulform verwendet wird", example="SK")
	public @NotNull String kuerzel = "";

	/** Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird */
	@Schema(required = true, description = "das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird", example="SE")
	public @NotNull String kuerzelStatistik = "";

	/** Die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Die textuelle Beschreibung der Herkunftsschulform. */
	@Schema(required = true, description = "die textuelle Beschreibung der Herkunftsschulform", example="Sekundarschule")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftSchulformKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id                 die ID
	 * @param kuerzel            das Kürzel, welches bei dem Core-Type Schulform verwendet wird
	 * @param kuerzelStatistik   das Kürzel, welches bei der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird 
	 * @param schulformen        die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann
	 * @param beschreibung       die textuelle Beschreibung der Herkunftsschulform
	 * @param gueltigVon         das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis         das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public HerkunftSchulformKatalogEintrag(long id, @NotNull String kuerzel, @NotNull String kuerzelStatistik,
			@NotNull List<@NotNull Schulform> schulformen, @NotNull String beschreibung, 
			Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.kuerzelStatistik = kuerzelStatistik;
		for (@NotNull Schulform schulform : schulformen)
			this.schulformen.add(schulform.daten.kuerzel);
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

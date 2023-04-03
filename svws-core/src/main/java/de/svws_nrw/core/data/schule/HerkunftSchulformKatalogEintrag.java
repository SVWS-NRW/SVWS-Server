package de.svws_nrw.core.data.schule;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkunftsschulformen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der möglichen Herkunftsschulformen.")
@TranspilerDTO
public class HerkunftSchulformKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Schulform verwendet wird */
	@Schema(description = "das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Schulform verwendet wird", example = "SK")
	public @NotNull String kuerzel = "";

	/** Das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird */
	@Schema(description = "das Kürzel der möglichen Herkunftsschulform, welches im Rahmen der amtlichen Schulstatistik für die Herkunftsschulform verwendet wird", example = "SE")
	public @NotNull String kuerzelStatistik = "";

	/** Die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen die Herkunftsschulform vorkommen kann")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();

	/** Die textuelle Beschreibung der Herkunftsschulform. */
	@Schema(description = "die textuelle Beschreibung der Herkunftsschulform", example = "Sekundarschule")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schulgliederung bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
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
	public HerkunftSchulformKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String kuerzelStatistik,
			final @NotNull List<@NotNull Schulform> schulformen, final @NotNull String beschreibung,
			final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.kuerzelStatistik = kuerzelStatistik;
		for (final @NotNull Schulform schulform : schulformen)
			this.schulformen.add(schulform.daten.kuerzel);
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

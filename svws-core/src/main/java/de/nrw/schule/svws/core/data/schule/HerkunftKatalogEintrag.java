package de.nrw.schule.svws.core.data.schule;

import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Werte für den Katalog der möglichen Herkünfte eines Schülers.
 * Dieser Katalog-Einträge dienen der Kompatibilität zu den Statkue-Tabellen und der
 * Handhabung in Schild. Für die Verwendung im SVWS-Server und -Client sollten
 * die aufgeteilten Katalog und Core-Types verwendet werden.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Herkünfte eines Schülers.")
@TranspilerDTO
public class HerkunftKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id = -1;

	/** Das der Herkunft eines Schülers, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "das der Herkunft eines Schülers, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example="UN")
	public @NotNull String kuerzel = "";

	/** Die Kürzel der Schulformen, bei welchen die Herkunft des Schülers vorkommen kann. */
	@Schema(required = true, description = "die Kürzel der Schulformen, bei welchen die Herkunft des Schülers vorkommen kann")
	public @NotNull List<@NotNull String> schulformen = new Vector<>();
	
	/** Die textuelle Beschreibung der Herkunft. */
	@Schema(required = true, description = "die textuelle Beschreibung der Herkunft", example="Herkunft noch unbekannt (nur Gliederung A12, A13)")
	public @NotNull String beschreibung = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example="null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(required = false, description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example="2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public HerkunftKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel der Herkunft
	 * @param schulformen     die Schulformen, für welche die Herkunft zulässig ist
	 * @param beschreibung    die textuelle Beschreibung der Herkunft
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public HerkunftKatalogEintrag(long id, @NotNull String kuerzel, @NotNull List<@NotNull String> schulformen, 
			@NotNull String beschreibung, Integer gueltigVon, Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.schulformen = schulformen;
		this.beschreibung = beschreibung;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}

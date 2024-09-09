package de.svws_nrw.asd.data.fach;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.data.RGBFarbe;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieser DTO beinhaltet die Daten für den Katalog der möglichen Einträge bei den
 * Fachgruppen
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Fachgruppen")
@TranspilerDTO
public class FachgruppeKatalogEintrag extends CoreTypeData {

	/** Die Nummer für den Fachbereich, sofern festgelegt, ansonsten null. */
	@Schema(description = "die Nummer für den Fachbereich, sofern festgelegt, ansonsten null", example = "8")
	public Integer nummer = null;

	// idSchild(die alte Fachgruppen-ID, welche in Schild_NRW 2.x verwendet wurde) -> schluessel

	/** Die Farbe, welche der Fachgruppe zugeordnet wurde */
	@Schema(description = "die Farbe, welche der Fachgruppe zugeordnet wurde.", example = "{ \"red\": 141, \"green\": 180, \"blue\": 227 }")
	public @NotNull RGBFarbe farbe = new RGBFarbe();

	/** Die Kürzel der Schulformen, bei welchen die Fachgruppe vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen die Fachgruppe vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

	/** Ein Zahlwert, welche eine Sortier-Reihenfolge der Fachgruppen angibt (aus Schild 2.x). */
	@Schema(description = "ein Zahlwert, welche eine Sortier-Reihenfolge der Fachgruppen angibt (aus Schild 2.x)", example = "10")
	public @NotNull Integer sortierung = 0;

	/** Gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht */
	@Schema(description = "gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht", example = "true")
	public boolean fuerZeugnis = false;

}

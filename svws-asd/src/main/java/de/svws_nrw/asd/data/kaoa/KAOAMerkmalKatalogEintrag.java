package de.svws_nrw.asd.data.kaoa;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA
 * gültigen Merkmalen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Merkmale.")
@TranspilerDTO
public class KAOAMerkmalKatalogEintrag extends CoreTypeData {

	/** Die Kategorie, welcher das Merkmal zugeordnet ist. */
	@Schema(description = "die Kategorie, welcher das Merkmal zugeordnet ist", example = "SBO_2")
	public @NotNull String kategorie = "";

	/** Die Optionsart des Merkmals. */
	@Schema(description = "die Optionsart des Merkmals", example = "Formen der Orientierung und Beratung")
	public String optionsart = null;

	/** Die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf */
	@Schema(description = "die Anlagen des Berufskollegs bei denen der Eintrag gemacht werden darf")
	public @NotNull List<String> bkAnlagen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KAOAMerkmalKatalogEintrag() {
		// leer
	}

}

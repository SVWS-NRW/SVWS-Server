package de.svws_nrw.asd.data.kaoa;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Objekte dieser Klasse enthalten die im Rahmen von KAoA gültigen Anschlussoptionen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Anschlussoptionen.")
@TranspilerDTO
public class KAOAAnschlussoptionenKatalogEintrag extends CoreTypeData {

	/** Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII) */
	@Schema(description = "Jahrgangsstufen in denen der Eintrag gemacht werden darf (SI bzw. SII)")
	public @NotNull List<String> stufen = new ArrayList<>();

	/** Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden */
	@Schema(description = "Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden")
	public @NotNull List<String> anzeigeZusatzmerkmal = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KAOAAnschlussoptionenKatalogEintrag() {
		// leer
	}


}

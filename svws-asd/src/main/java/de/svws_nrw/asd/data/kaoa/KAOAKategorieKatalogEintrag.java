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
 * gültigen Kategorien.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der KAoA-Kategorien.")
@TranspilerDTO
public class KAOAKategorieKatalogEintrag extends CoreTypeData {

	/** Jahrgangsstufen in denen der Eintrag gemacht werden darf */
	@Schema(description = "Jahrgangsstufen in denen der Eintrag gemacht werden darf")
	public @NotNull List<String> jahrgaenge = new ArrayList<>();

}

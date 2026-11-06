package de.svws_nrw.transpiler.test;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Test-Klasse: Ein DTO mit String-Listen und String-Arrays, um die JSON-Maskierung
 * der Elemente in transpilerToJSON und transpilerToJSONPatch zu testen.
 */
@TranspilerDTO
public class StringListDTO {

	/** Eine Liste mit Strings, die nicht null sein dürfen */
	public @NotNull List<@NotNull String> liste = new ArrayList<>();

	/** Eine Liste mit Strings, die auch null sein dürfen */
	public List<String> listeNullable = new ArrayList<>();

	/** Ein Array mit Strings, die nicht null sein dürfen */
	public String @NotNull [] array = new String[0];

	/** Ein Array mit Strings, die auch null sein dürfen */
	public @NotNull String[] arrayNullable = new String[0];

}

package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Kopplung bei {@link StundenplanblockungInput}. <br>
 * Eine Kopplung erlaubt parallele Kurse innerhalb einer Klasse. In der Oberstufe ist in der Regel jeder Kurs einer Kopplung zugeordnet,
 * in den anderen Stufen ist es üblich bei Religion, Wahlpflicht und der 2. Fremdsprache.
 *
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungKopplung")
@Schema(name = "StundenplanblockungKopplung", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Kopplung bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungKopplung {

	/** Die Datenbank-ID der Kopplung. */
	public long id;

	/** Das Kürzel der Kopplung. Beispielsweise '5RE'. */
	public @NotNull String kuerzel = "";

	/** Alle Stundenelemente, die dieser Kopplung zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungStundenelement.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungStundenelement> stundenelemente = new ArrayList<>();

}

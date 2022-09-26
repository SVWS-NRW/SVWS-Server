package de.nrw.schule.svws.core.data.klausurblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Spezifiziert die grundlegende Struktur von JSON-Daten für die Rückgabe einer Ausgabe bezüglich der Eingabe
 * {@link KlausurblockungSchienenInput}. */
@XmlRootElement(name = "KlausurblockungSchienenInput")
@Schema(name = "KlausurblockungSchienenInput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Rückgabe einer Ausgabe bezüglich der Eingabe KlausurblockungSchienenInput.")
@TranspilerDTO
public class KlausurblockungSchienenOutput {

	/** Die Datenbank-ID der zugehörigen Klausurblockung, für die dieses Ergebnis gilt. */
	@Schema(required = true, description = "Die Datenbank-ID der zugehörigen Klausurblockung, für die dieses Ergebnis gilt.", example = "42")
	public long datenbankID = -1;

	/** Die Anzahl an Schienen (ergo Klausurtage) für dieses Ergebnis. */
	@Schema(required = true, description = "Die Anzahl an Schienen (ergo Klausurtage) für dieses Ergebnis.", example = "17")
	public int schienenAnzahl = -1;

	/** Alle Klausuren mit ihren Schienen-Zuordnungen */
	@ArraySchema(schema = @Schema(implementation = KlausurblockungSchienenOutputKlausur.class))
	public @NotNull Vector<@NotNull KlausurblockungSchienenOutputKlausur> klausuren = new Vector<>();
}

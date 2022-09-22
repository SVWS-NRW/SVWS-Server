package de.nrw.schule.svws.core.data.klausurblockung;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Spezifiziert die grundlegende Struktur von JSON-Daten für Eingabedaten, die dem Klausurblockungs-Algorithmus
 * übergeben werden. Der Algorithmus sucht nach einer minimalen Anzahl an Schienen für alle Klausuren. */
@XmlRootElement(name = "KlausurblockungSchienenInput")
@Schema(name = "KlausurblockungSchienenInput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für Eingabedaten, die dem Klausurblockungs-Algorithmus übergeben werden. Der Algorithmus sucht nach einer minimalen Anzahl an Schienen für alle Klausuren.")
@TranspilerDTO
public class KlausurblockungSchienenInput {

	/** Die Datenbank-ID der zugehörigen Klausurblockung. Sie muss positiv sein, sonst wird ein Fehler erzeugt. */
	@Schema(required = true, description = "Die Datenbank-ID der zugehörigen Klausurblockung. Sie muss positiv sein, sonst wird ein Fehler erzeugt.", example = "42") public long datenbankID = -1;

	/** Die maximale Zeit (in Millisekunden), die der Algorithmus verwenden darf. */
	@Schema(required = true, description = "Die maximale Zeit (in Millisekunden), die der Algorithmus verwenden darf.", example = "1000") public long maxTimeMillis = 1000L;

	/** Eine Sammlung aller Schüler mit den zugeordneten Klausuren. */
	@ArraySchema(schema = @Schema(implementation = KlausurblockungSchienenInputSchueler.class)) 
	public @NotNull Vector<@NotNull KlausurblockungSchienenInputSchueler> schueler = new Vector<>();

}

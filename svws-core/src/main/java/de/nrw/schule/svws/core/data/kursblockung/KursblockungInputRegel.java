package de.nrw.schule.svws.core.data.kursblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Regel, die bei der Kursblockung zu
 * beachten ist. Jede Regel hat einen Typ {@link #typ} und eine Menge an primitiven Daten in einem Long-Array. Der Typ
 * der Regel bestimmt, wie das Array zu parsen ist. Im Laufe der Zeit werden neue Regeln hinzukommen.
 *
 * @author Benjamin A. Bartsch */
@XmlRootElement(name = "KursblockungInputRegel")
@Schema(name = "KursblockungInputRegel", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für geltende Regeln beim Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInputRegel {

	/** Die ID der Regel, in der Datenbank. Ein Wert von -1 bedeutet, dass die Regel keine Datenbankzuordnung hat. */
	@Schema(required = true, description = "Die ID der Regel, in der Datenbank. Ein Wert von -1 bedeutet, dass die Regel keine Datenbankzuordnung hat.", example = "5621")
	public long databaseID;

	/** Der Typ der Regel beginnend mit 1. Siehe: {@link GostKursblockungRegelTyp} */
	@Schema(required = true, description = "Der Typ der Regel, beginnend mit 1.", example = "1")
	public int typ;

	/** Die zu interpretierende Daten relativ zum {@link #typ} der Regel. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull Long @NotNull [] daten = new Long[0];

}

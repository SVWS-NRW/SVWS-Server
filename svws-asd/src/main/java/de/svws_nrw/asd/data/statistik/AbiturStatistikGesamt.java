package de.svws_nrw.asd.data.statistik;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 */
@XmlRootElement
@Schema(description = "Die Stammdaten eines Lehrer-Eintrags.")
@TranspilerDTO

public class AbiturStatistikGesamt {

	/** Die Liste der Statistikkuerzel der Abiturfächer. */
	@ArraySchema(schema = @Schema(implementation = String.class))
	public @NotNull List<String> abifach = new ArrayList<>();

	/** Die Abiturnote. */
	@Schema(description = "die Abiturnote", example = "1.8")
	public String note;

	/** Gibt an, ob der Schüler zum Abitur zugelassen wurde. */
	@Schema(description = "gibt an, ob der Schüler zum Abitur zugelassen wurde", example = "true")
	public boolean istZugelassen;

	/** Gibt an, ob das Abitur bestanden wurde. */
	@Schema(description = "gibt an, ob das Abitur bestanden wurde", example = "false")
	public boolean hatBestanden;

	/** Gibt an, ob der Schüler freiwillig von der Abiturprüfung zurückgetreten ist. */
	@Schema(description = "gibt an, ob der Schüler freiwillig von der Abiturprüfung zurückgetreten ist", example = "true")
	public boolean istZurueckgetreten;

}

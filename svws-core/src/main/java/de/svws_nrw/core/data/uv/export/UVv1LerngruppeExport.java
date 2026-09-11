package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt eine Lerngruppe innerhalb eines Planungsabschnitts im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt eine Lerngruppe innerhalb eines Planungsabschnitts im UV-Export.")
@TranspilerDTO
public class UVv1LerngruppeExport {

	/** Die eindeutige UV-ID der Lerngruppe. */
	@Schema(description = "die eindeutige UV-ID der Lerngruppe", example = "4711")
	public long uvId = -1;

	/** Die UV-ID der Klasse, zu der die Lerngruppe gehört. */
	@Schema(description = "die UV-ID der Klasse, zu der die Lerngruppe gehört", example = "101")
	public Long klasseUvId = null;

	/** Die UV-ID des Faches, das in der Lerngruppe unterrichtet wird. */
	@Schema(description = "die UV-ID des Faches, das in der Lerngruppe unterrichtet wird", example = "32")
	public Long fachUvId = null;

	/** Die UV-ID des Kurses, der mit dieser Lerngruppe verknüpft ist. */
	@Schema(description = "die UV-ID des Kurses, der mit dieser Lerngruppe verknüpft ist", example = "55")
	public Long kursUvId = null;

	/** Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe. */
	@Schema(description = "die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe", example = "4.0")
	public double wochenstunden = 0.0;

	/** Die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe. */
	@Schema(description = "die Anzahl der tatsächlich unterrichteten Wochenstunden der Lerngruppe", example = "3.5")
	public double wochenstundenUnterrichtet = 0.0;

	/** Die Schulnummer einer möglichen Koop-Schule. */
	@Schema(description = "die Schulnummer einer möglichen Koop-Schule", example = "178902")
	public String koopSchulNr = null;

	/** Die Anzahl der externen Schüler von Koop-Schulen. */
	@Schema(description = "die Anzahl der externen Schüler von Koop-Schulen", example = "2")
	public int koopAnzahlExterne = 0;

	/** Ein Array mit den Lehrer-Zuordnungen der Lerngruppe. */
	@ArraySchema(schema = @Schema(implementation = UVv1LerngruppenLehrerExport.class, description = "Ein Array mit den Lehrer-Zuordnungen der Lerngruppe"))
	public @NotNull List<UVv1LerngruppenLehrerExport> lehrer = new ArrayList<>();

	/** Ein Array mit den UV-IDs der Schienen dieser Lerngruppe. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den UV-IDs der Schienen dieser Lerngruppe"))
	public @NotNull List<Long> schienenUvIds = new ArrayList<>();

}

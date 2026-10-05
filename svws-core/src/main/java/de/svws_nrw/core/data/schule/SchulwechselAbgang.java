package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Wechselvorgangsdaten eines Schüler-Abgangs.
 */
@XmlRootElement
@Schema(description = "Die Wechselvorgangsdaten eines Schüler-Abgangs")
@TranspilerDTO
public class SchulwechselAbgang {

	/** Die ID des Wechselvorgangs. */
	@Schema(description = "Die Id des Wechselvorgangs", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die Id des Schülerdatensatzes auf den sich der Wechselvorgang bezieht. */
	@Schema(description = "Die Id des Schülers auf den sich der Wechselvorgang bezieht", example = "4423")
	public long idSchueler;

	/** Die Id des aktuellen Status des Wechselvorgangs. */
	@Schema(description = "Die Id des aktuellen Status des Wechselvorgangs", example = "1")
	public int idStatus;

	/** Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs. */
	@Schema(description = "Der Zeitpunkt der letzten Statusänderung des Wechselvorgangs", example = "2026-01-22 13:39:19")
	public String lastModified;

	/** Die Id des XSchule-Dokuments, das zu diesem Wechselvorgang gehört. */
	@Schema(description = "Die Id des XSchule-Dokuments, das zu diesem Wechselvorgang gehört", example = "1")
	public Long idDocument;

	/** Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang. */
	@Schema(description = "Die eindeutige UUID des Schulkinds bei schulbewerbung.de – identifiziert den Wechselvorgang", example = "123e4567-e89b-12d3-a456-426614174000")
	public String idSchulkindSchulbewerbung;
}

package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class SchuelerMerkmalCreateRequest {


	/** Die ID des Schülers */
	@Schema(description = "Die ID des Schülers", example = "3")
	@NotNull
	public Long idSchueler;

	/** Die ID des Merkmals */
	@Schema(description = "die ID des Merkmals", example = "1")
	@NotNull
	public Long idMerkmal;

	/** Das Datum, ab dem das Merkmal vorliegt. */
	@Schema(description = "das Datum, ab dem das Merkmal vorliegt", example = "2007-08-01")
	@ValidDateFormat
	public String datumVon;

	/** Das Datum, bis wann das Merkmal vorliegt. */
	@Schema(description = "das Datum, bis wann das Merkmal vorliegt", example = "2008-07-31")
	@ValidDateFormat
	public String datumBis;

}

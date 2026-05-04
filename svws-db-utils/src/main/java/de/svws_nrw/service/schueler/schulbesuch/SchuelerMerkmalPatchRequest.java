package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.openapitools.jackson.nullable.JsonNullable;

public class SchuelerMerkmalPatchRequest {


	/** Die ID des Merkmals */
	@Schema(description = "die ID des Merkmals", example = "1")
	public JsonNullable<Long> idMerkmal = JsonNullable.undefined();

	/** Das Datum, ab dem das Merkmal vorliegt. */
	@Schema(description = "das Datum, ab dem das Merkmal vorliegt", example = "2007-08-01")
	public JsonNullable<@ValidDateFormat String> datumVon = JsonNullable.undefined();

	/** Das Datum, bis wann das Merkmal vorliegt. */
	@Schema(description = "das Datum, bis wann das Merkmal vorliegt", example = "2008-07-31")
	public JsonNullable<@ValidDateFormat String> datumBis = JsonNullable.undefined();

}

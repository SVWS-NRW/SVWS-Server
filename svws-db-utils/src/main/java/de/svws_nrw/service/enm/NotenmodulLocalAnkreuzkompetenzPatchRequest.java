package de.svws_nrw.service.enm;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Informationenen zur Aktualisierung eines Lernabschnitts im lokalen Notenmodul
 */
@Schema(description = "Die Informationenen zur Aktualisierung eines Lernabschnitts im lokalen Notenmodul")
public class NotenmodulLocalAnkreuzkompetenzPatchRequest {

	/** Die ID der Schüler-Ankreuzkompetenz auf welchen sich der Patch bezieht (z.B. 307956) */
	@Schema(description = "Die ID des Schüler-Lernabschnittes auf welchen sich der Patch bezieht.", example = "307956")
	public long id;

	/** Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen an, ob diese zugewiesen ist oder nicht (hier mit einer Verschiebung von 1 zum Array-Index). */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen an, ob diese zugewiesen ist oder nicht (hier mit einer Verschiebung von 1 zum Array-Index)."))
	public @NotNull boolean[] stufen = new boolean[5];

}

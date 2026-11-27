package de.svws_nrw.core.data.uv;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/** Atomare Anlage einer Klasse mit vorhandener oder neuer Schülergruppe. */
@TranspilerDTO
@Schema(description = "Atomare Anlage einer Klasse mit vorhandener oder neuer Schülergruppe.")
public class UvKlasseAnlageRequest {

	/** Die anzulegende Klasse. */
	@Schema(description = "Die anzulegende Klasse.")
	@Valid
	public @NotNull UvKlasse klasse = new UvKlasse();

	/** Die neue Schülergruppe; null verwendet die Gruppen-ID der Klasse. */
	@Schema(description = "Die neue Schülergruppe; null verwendet die Gruppen-ID der Klasse.")
	@Valid
	public UvSchuelergruppeCreateRequest schuelergruppe = null;

	/** Erstellt eine leere Anlageanfrage. */
	public UvKlasseAnlageRequest() {
		// leer
	}
}

package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt einen Eintrag zu Anrechnungsstunden einer Lehrkraft im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt einen Eintrag zu Anrechnungsstunden einer Lehrkraft im UV-Export.")
@TranspilerDTO
public class UVv1LehrerAnrechnungsstundenExport {

	/** Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft). */
	@Schema(description = "das Kürzel des Anrechnungsgrundes", example = "AG")
	public @NotNull String anrechnungsgrundKrz = "";

	/** Die Anzahl der angerechneten Stunden. */
	@Schema(description = "die Anzahl der angerechneten Stunden", example = "2.5")
	public double anzahlStunden;

	/** Das Datum, ab dem die Anrechnungsstunde gültig ist. */
	@Schema(description = "das Datum, ab dem die Anrechnungsstunde gültig ist", example = "2025-08-01")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis wann die Anrechnungsstunde gültig ist. */
	@Schema(description = "das Datum, bis wann die Anrechnungsstunde gültig ist", example = "2026-07-31")
	public String gueltigBis = null;

}

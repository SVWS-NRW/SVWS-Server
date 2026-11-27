package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/** Atomare Anlage eines Kurses mit Lerngruppe und optional neuer Schülergruppe. */
@TranspilerDTO
@Schema(description = "Atomare Anlage eines Kurses mit Lerngruppe und optional neuer Schülergruppe.")
public class UvKursunterrichtAnlageRequest {

	/** Der anzulegende Kurs. */
	@Schema(description = "Der anzulegende Kurs.")
	@Valid
	public @NotNull UvKursCreateRequest kurs = new UvKursCreateRequest();

	/** Die anzulegende Kurslerngruppe. */
	@Schema(description = "Die anzulegende Kurslerngruppe.")
	@Valid
	public @NotNull UvLerngruppeCreateRequest lerngruppe = new UvLerngruppeCreateRequest();

	/** Die neue Schülergruppe; null verwendet die Gruppen-ID des Kurses. */
	@Schema(description = "Die neue Schülergruppe; null verwendet die Gruppen-ID des Kurses.")
	@Valid
	public UvSchuelergruppeCreateRequest schuelergruppe = null;

	/** Die zusätzlich zuzuordnenden Schüler-IDs. */
	@Schema(description = "Die zusätzlich zuzuordnenden Schüler-IDs.")
	public @NotNull List<Long> idsSchueler = new ArrayList<>();

	/** Erstellt eine leere Anlageanfrage. */
	public UvKursunterrichtAnlageRequest() {
		// leer
	}
}

package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt ein Unterrichtsfach einer Lehrkraft im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt ein Unterrichtsfach einer Lehrkraft im UV-Export.")
@TranspilerDTO
public class UVv1LehrerUnterrichtsfachExport {

	/** Die ID des Fachs in den Fachdaten. */
	@Schema(description = "die ID des Fachs in den Fachdaten", example = "13")
	public long idFach = -1;

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf. */
	@Schema(description = "gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf", example = "true")
	public boolean istSek1 = false;

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf. */
	@Schema(description = "gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf", example = "false")
	public boolean istSek2 = false;

	/** Die Bemerkung zum Unterrichtsfach. */
	@Schema(description = "die Bemerkung zum Unterrichtsfach", example = "")
	public String bemerkung = null;

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "das Datum, ab dem die Lehrkraft das Fach unterrichtet", example = "2025-08-01")
	public String gueltigVon = null;

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "das Datum, bis zu dem die Lehrkraft das Fach unterrichtet", example = "2026-07-31")
	public String gueltigBis = null;

}

package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt das Pflichtstundensoll einer Lehrkraft für einen Gültigkeitszeitraum.
 */
@Schema(description = "Diese Klasse beschreibt das Pflichtstundensoll einer Lehrkraft für einen Gültigkeitszeitraum.")
@TranspilerDTO
public class UVv1LehrerPflichtstundensollExport {

	/** Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat. */
	@Schema(description = "Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.")
	public double pflichtstdSoll;

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public @NotNull String gueltigVon = "";

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis = null;

}

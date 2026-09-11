package de.svws_nrw.core.data.uv.export;

import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse beschreibt einen Eintrag innerhalb eines Zeitrasters im UV-Export.
 */
@Schema(description = "Diese Klasse beschreibt einen Eintrag innerhalb eines Zeitrasters im UV-Export.")
@TranspilerDTO
public class UVv1ZeitrasterEintragExport {

	/** Die UV-ID des Zeitraster-Eintrags. */
	@Schema(description = "die UV-ID des Zeitraster-Eintrags", example = "4711")
	public long uvId = -1;

	/** Der {@link Wochentag} an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag) */
	@Schema(description = "der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)", example = "1")
	public int wochentag = -1;

	/** Die Stunde (z. B. 1 = erste Stunde). */
	@Schema(description = "die Stunde (z. B. 1 = erste Stunde)", example = "1")
	public int stunde = -1;

	/** Beginn der Stunde (als Minuten seit Mitternacht). */
	@Schema(description = "Beginn der Stunde (als Minuten seit Mitternacht)", example = "480")
	public int beginn = -1;

	/** Ende der Stunde (als Minuten seit Mitternacht). */
	@Schema(description = "Ende der Stunde (als Minuten seit Mitternacht)", example = "525")
	public int ende = -1;

}

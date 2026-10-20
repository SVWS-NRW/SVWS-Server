package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@TranspilerDTO
public class SchulKatalogStatistikGesamt {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Eine Kurzbezeichnung für die Schule. */
	@Schema(description = "eine Kurzbezeichnung für die Schule", example = "Erich-Kästner-Schule")
	public String kurzbezeichnung;

	/** Die Statistik-Schulnummer der Schule */
	@Schema(description = "Die Statistik-Schulnummer der Schule", example = "989123")
	public String schulnummerStatistik;

	/** Der Name des Schule. */
	@Schema(description = "der Name des Schule", example = "Erich-Kästner-Realschule der Stadt Düsseldorf - Sekundarstufe I -")
	public @NotNull String name = "";

	/** Die ID der Schulform. */
	@Schema(description = "die ID der Schulform", example = "1")
	public Long idSchulform = null;

}

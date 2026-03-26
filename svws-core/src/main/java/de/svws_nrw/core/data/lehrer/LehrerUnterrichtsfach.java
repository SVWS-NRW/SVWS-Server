package de.svws_nrw.core.data.lehrer;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Zuordnung eines Unterrichtsfachs zu einer Lehrkraft.
 */
@XmlRootElement
@Schema(description = "Die Zuordnung eines Unterrichtsfachs zu einer Lehrkraft.")
@TranspilerDTO
public class LehrerUnterrichtsfach {

	/** Die ID des Eintrags. */
	@Schema(description = "die ID des Eintrags", example = "4711")
	public long id = -1;

	/** Die ID des Lehrers. */
	@Schema(description = "die ID des Lehrers", example = "42")
	public long idLehrer = -1;

	/** Die ID des Fachs. */
	@Schema(description = "die ID des Fachs", example = "13")
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

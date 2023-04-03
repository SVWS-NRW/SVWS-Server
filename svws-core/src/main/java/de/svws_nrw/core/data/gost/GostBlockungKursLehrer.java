package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse repräsentiert einen Kurs in einer Blockung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu einem Kurs der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBlockungKursLehrer {

	/** Die ID des Lehrers */
	public long id = -1;
	/** Das Kürzel des Lehrers. */
	public @NotNull String kuerzel = "";

	/** Der Vorname des Lehrers. */
	public @NotNull String vorname = "";

	/** Der Nachname des Lehrers. */
	public @NotNull String nachname = "";

	/** Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung des eigentlichen Kurslehrer (z.B. 1) und einer Zusatzkraft (z.B. 2) */
	public int reihenfolge = 1;

	/** Die Wochenstunden, welche die Lehrkraft in dem Kurs unterrichtet (Default: Wochenstunden des Kurses) */
	public int wochenstunden = 3;

	/** Gibt an, ob es sich um eine externe Lehrkraft handelt (z.B. bei einem Kooperationskurs an einer anderen Schule) */
	public boolean istExtern = false;

}

package de.svws_nrw.core.data.schueler;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Core-DTO für die Sprachbelegungen und die Sprachprüfungen eines Schülers.
 *
 * Die Sprachenfolge stellt im Wesentlichen eine Liste von {@link Sprachbelegung}-Objekten und
 * {@link Sprachpruefung}-Objekten dar.
 */
@TranspilerDTO
public class Sprachendaten {

	/** Die ID des Schülers, dessen Sprachenfolge in diesem Objekt gespeichert ist. */
	public long schuelerID;

	/** Die Liste der Sprachbelegungen. */
	public @NotNull ArrayList<@NotNull Sprachbelegung> belegungen = new ArrayList<>();

	/** Die Liste der Sprachpruefungen. */
	public @NotNull ArrayList<@NotNull Sprachpruefung> pruefungen = new ArrayList<>();

}

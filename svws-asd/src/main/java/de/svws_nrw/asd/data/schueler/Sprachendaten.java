package de.svws_nrw.asd.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
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
	public @NotNull List<Sprachbelegung> belegungen = new ArrayList<>();

	/** Die Liste der Sprachprüfungen. */
	public @NotNull List<Sprachpruefung> pruefungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Sprachendaten() {
		// leer
	}

}

package de.nrw.schule.svws.core.data;

import java.util.Vector;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types für die Sprachbelegungen und die Sprachprüfungen
 * eines Schülers zur Verfügung. Die Sprachenfolge stellt im
 * Wesentlichen eine Liste von {@link Sprachbelegung}-Objekten und
 * {@link Sprachpruefung}-Objekten dar.
 * Der Core-Type dient als grundlegender abstrakter Datentyp sowohl
 * für die Core-Algorithmen als auch für die OpenAPI-Schnittstelle.
 */
@TranspilerDTO
public class Sprachendaten {

    /** Die ID des Schülers, dessen Sprachenfolge in diesem Objekt gespeichert ist. */
    public long schuelerID;

    /** Die Liste der Sprachbelegungen. */
	public @NotNull Vector<@NotNull Sprachbelegung> belegungen = new Vector<>();

    /** Die Liste der Sprachpruefungen. */
    public @NotNull Vector<@NotNull Sprachpruefung> pruefungen = new Vector<>();

}

package de.svws_nrw.core.utils.uv;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/** Interne, transpilierbare Normalform eines aus einer Quelle gelesenen Kurses. */
final class UvKursImportQuellkurs {

	/** Die ID des Quellkurses. */
	final long id;
	/** Die ID des Fachs. */
	final long idFach;
	/** Die Kursart. */
	final @NotNull String kursart;
	/** Die Kursnummer. */
	final int nummer;
	/** Die Wochenstunden. */
	final double wochenstunden;
	/** Die IDs der zugeordneten Lehrer. */
	final @NotNull List<Long> lehrerIds = new ArrayList<>();
	/** Die IDs der zugeordneten Schüler. */
	final @NotNull List<Long> schuelerIds = new ArrayList<>();
	/** Die IDs der zugeordneten Schienen. */
	final @NotNull List<Long> schienenIds = new ArrayList<>();

	/**
	 * Erstellt einen normalisierten Quellkurs.
	 *
	 * @param id die ID des Quellkurses
	 * @param idFach die Fach-ID
	 * @param kursart die Kursart
	 * @param nummer die Kursnummer
	 * @param wochenstunden die Wochenstunden
	 */
	UvKursImportQuellkurs(final long id, final long idFach, final @NotNull String kursart, final int nummer, final double wochenstunden) {
		this.id = id;
		this.idFach = idFach;
		this.kursart = kursart;
		this.nummer = nummer;
		this.wochenstunden = wochenstunden;
	}
}

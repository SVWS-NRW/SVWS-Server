package de.svws_nrw.base.untis;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

/**
 * Gibt die Variante an, mit welcher die Schüler-Bezeichnungen in den Export- und
 * Import-Formaten von Untis erzeugt werden.
 */
public enum UntisSchuelerBezeichner {

	/** Der Bezeichner wird aus der ID generiert und hat die Form "S-4711" */
	SCHUELER_IDS(1),

	/** Der Bezeichner wird aus dem Nachnamen, den ersten drei Zeichen des Vornamens und dem Geburtsdatum erzeugt "MUSTERMANN_MAX_19571124" */
	SCHUELER_NAME_GEBURTSDATUM_KURZ(2),

	/** Der Bezeichner wird aus dem Nachnamen, dem Vornamen und dem Geburtsdatum erzeugt "MUSTERMANN_MAXIMILIAN_19571124" */
	SCHUELER_NAME_GEBURTSDATUM_LANG(3);


	/** Die ID der Variante, welche bei der Übermittlung über die OpenAPI-verwendet wird */
	public final int id;


	/**
	 * Erzeugt einen neue Variante
	 *
	 * @param id   die zugehörige ID
	 */
	private UntisSchuelerBezeichner(final int id) {
		this.id = id;
	}


	/**
	 * Gibt den Bezeichner für den Schüler mit dieser Variante zurück.
	 *
	 * @param id         die ID des Schülers
	 * @param nachname   der Nachname des Schülers
	 * @param vorname    der Vorname des Schülers
	 * @param isoDate    das Geburtsdatum des Schülers
	 *
	 * @return der Bezeichner für den Schüler
	 */
	public @NotNull String get(final long id, final String nachname, final String vorname, final String isoDate) {
		return switch (this.id) {
			case 1 -> "S-" + id;
			case 2 -> {
				final LocalDate date = ((isoDate == null) || "".equals(isoDate)) ? null : LocalDate.parse(isoDate);
				final String geburtsdatum = (date == null) ? null : "%04d%02d%02d".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				yield ((nachname == null) || ("".equals(nachname.trim())) ? "???" : nachname.trim().replace(" ", ""))
						+ "_" + (((vorname == null) || "".equals(vorname.trim())) ? "???" : vorname.trim().replace(" ", "").substring(0, 3))
						+ "_" + ((geburtsdatum == null) ? "????????" : geburtsdatum);
			}
			case 3 -> {
				final LocalDate date = ((isoDate == null) || "".equals(isoDate)) ? null : LocalDate.parse(isoDate);
				final String geburtsdatum = (date == null) ? null : "%04d%02d%02d".formatted(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
				yield ((nachname == null) || ("".equals(nachname.trim())) ? "???" : nachname.trim().replace(" ", ""))
						+ "_" + (((vorname == null) || "".equals(vorname.trim())) ? "???" : vorname.trim().replace(" ", ""))
						+ "_" + ((geburtsdatum == null) ? "????????" : geburtsdatum);
			}
			default -> throw new IllegalArgumentException("Die %d ist nicht gültig.".formatted(id));
		};
	}


	/**
	 * Gibt den Enum-Wert für die ID zurück.
	 *
	 * @param id   die ID der Variante
	 *
	 * @return die Variante
	 */
	public static UntisSchuelerBezeichner getByID(final int id) {
		return switch (id) {
			case 1 -> SCHUELER_IDS;
			case 2 -> SCHUELER_NAME_GEBURTSDATUM_KURZ;
			case 3 -> SCHUELER_NAME_GEBURTSDATUM_LANG;
			default -> throw new IllegalArgumentException("Die %d ist nicht gültig.".formatted(id));
		};
	}


	/**
	 * Gibt den Bezeichner für den Schüler mit der übergebene Variante zurück.
	 *
	 * @param idVariante   die ID der zu verwendenden Variante
	 * @param id           die ID des Schülers
	 * @param nachname     der Nachname des Schülers
	 * @param vorname      der Vorname des Schülers
	 * @param isoDate      das Geburtsdatum des Schülers
	 *
	 * @return der Bezeichner für den Schüler
	 */
	public static @NotNull String getBezeichner(final int idVariante, final long id, final String nachname, final String vorname, final String isoDate) {
		final UntisSchuelerBezeichner variante = getByID(idVariante);
		return variante.get(id, nachname, vorname, isoDate);
	}

}

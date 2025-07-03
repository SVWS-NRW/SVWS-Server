package de.svws_nrw.core.abschluss.gost.belegpruefung;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die Belegprüfungen bezüglich eines möglichen Schwerpunktes eines Schüler
 * im naturwissenschaftlichen oder sprachlichen Bereich für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen.
 */
public final class Schwerpunkt extends GostBelegpruefung {

	/// Gibt an, ob ein ein fremdsprachlicher Schwerpunkt vorliegt oder nicht
	private boolean _hatSchwerpunktFremdsprachen;

	/// Gibt an, ob ein ein naturwissenschaftlicher Schwerpunkt vorliegt oder nicht
	private boolean _hatSchwerpunktNaturwissenschaften;

	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager            der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungSprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefungNawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public Schwerpunkt(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull Fremdsprachen pruefungSprachen, final @NotNull Naturwissenschaften pruefungNawi) {
		super(manager, pruefungsArt, pruefungSprachen, pruefungNawi);
	}


	@Override
	protected void init() {
		// Keine Initialisierung notwendig
	}




	@Override
	protected void pruefeEF1() {
		final @NotNull Fremdsprachen pruefung_sprachen = ((@NotNull Fremdsprachen) pruefungen_vorher[0]);
		final @NotNull Naturwissenschaften pruefung_nawi = ((@NotNull Naturwissenschaften) pruefungen_vorher[1]);

		this._hatSchwerpunktFremdsprachen = (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2);
		this._hatSchwerpunktNaturwissenschaften =
				(pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1);

		// Prüfe, ob insgesamt so viele Fremdsprachen und Naturwissenschaften gewählt wurden, dass zunächst kein Schwerpunkt vorliegt.
		// Dann liegt kein Belegungsfehler vor.
		if (_hatSchwerpunktFremdsprachen && (_hatSchwerpunktNaturwissenschaften))
			return;

		// Prüfe, ob ein sprachlicher Schwerpunkt vorliegt
		if (_hatSchwerpunktFremdsprachen) {
			addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}

		// Prüfe, ob ein naturwissenschaftlicher Schwerpunkt vorliegt
		if (_hatSchwerpunktNaturwissenschaften) {
			addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}

		// Es wurden zu wenig Fremdsprachen und Naturwissenschaften belegt -> Belegungsfehler
		addFehler(GostBelegungsfehler.NW_FS_10);
	}




	@Override
	protected void pruefeGesamt() {
		final @NotNull Fremdsprachen pruefung_sprachen = ((@NotNull Fremdsprachen) pruefungen_vorher[0]);
		final @NotNull Naturwissenschaften pruefung_nawi = ((@NotNull Naturwissenschaften) pruefungen_vorher[1]);

		this._hatSchwerpunktFremdsprachen = (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2);
		this._hatSchwerpunktNaturwissenschaften =
				(pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1);

		// Prüfe, ob insgesamt so viele Fremdsprachen und Naturwissenschaften gewählt wurden, dass zunächst kein Schwerpunkt vorliegt.
		// Dann liegt kein Belegungsfehler vor.
		if (_hatSchwerpunktFremdsprachen && _hatSchwerpunktNaturwissenschaften)
			return;

		// Prüfe, ob ein sprachlicher Schwerpunkt vorliegt
		if (_hatSchwerpunktFremdsprachen) {
			addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}

		// Prüfe, ob ein naturwissenschaftlicher Schwerpunkt vorliegt
		if (_hatSchwerpunktNaturwissenschaften) {
			addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}

		// Es wurden zu wenig Fremdsprachen und Naturwissenschaften belegt -> Belegungsfehler
		addFehler(GostBelegungsfehler.NW_FS_10);
	}


	/**
	 * Gibt an, ob ein fremsprachlicher Schwerpunkt vorliegt oder nicht.
	 *
	 * @return true, wenn ein fremdsprachlicher Schwerpunkt vorliegt
	 */
	public boolean hatSchwerpunktFremdsprachen() {
		return this._hatSchwerpunktFremdsprachen;
	}


	/**
	 * Gibt an, ob ein naturwissenschaftlicher Schwerpunkt vorliegt oder nicht.
	 *
	 * @return true, wenn ein naturwissenschaftlicher Schwerpunkt vorliegt
	 */
	public boolean hatSchwerpunktNaturwissenschaften() {
		return this._hatSchwerpunktNaturwissenschaften;
	}

}

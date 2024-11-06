package de.svws_nrw.core.kursblockung.test;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.DTOUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Dummy-Manager zur Handhabung von Daten des Typs {@link GostBlockungsergebnis}.
 * Dieser Manager ist für Unit-Tests-Zwecke gedacht.
 */
public class DummyGostBlockungsergebnisManager {

	/** Der Blockungsdaten-Manager ist das Elternteil dieses Objektes. */
	private final @NotNull DummyGostBlockungsdatenManager _parent;

	/** Das Blockungsergebnis ist das zugehörige Eltern-Datenobjekt. */
	private final @NotNull GostBlockungsergebnis _ergebnis = new GostBlockungsergebnis();

	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 *
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public DummyGostBlockungsergebnisManager(final @NotNull DummyGostBlockungsdatenManager pParent, final long pGostBlockungsergebnisID) {
		_parent = pParent;
		_ergebnis.id = pGostBlockungsergebnisID;
		_ergebnis.blockungID = _parent.getID();
		_ergebnis.gostHalbjahr = _parent.daten().gostHalbjahr;
		stateClear();
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	public void stateRevalidateEverything() {
		stateClear();
	}

	/**
	 * ...
	 */
	public void stateClear() {
		// Lösche alle E-Schienen, die es im Elternteil nicht mehr gibt.
		final List<GostBlockungsergebnisSchiene> listZuLoeschen = new ArrayList<>();
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			if (!_parent.schieneGetExistiert(eSchiene.id))
				listZuLoeschen.add(eSchiene);
		_ergebnis.schienen.removeAll(listZuLoeschen);

		// Erzeuge fehlende E-Schienen, die nur das Elternteil derzeit hat.
		for (final @NotNull GostBlockungSchiene gSchiene : _parent.daten().schienen)
			if (!getOfSchieneExists(gSchiene.id)) {
				final @NotNull GostBlockungsergebnisSchiene eSchiene = DTOUtils.newGostBlockungsergebnisSchiene(gSchiene.id);
				_ergebnis.schienen.add(eSchiene);
			}
	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public void setAddSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager hinzugefügt werden!",
				!_parent.schieneGetExistiert(idSchiene));

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls die Schiene noch Kurszuordnungen hat.
	 */
	public void setRemoveSchieneByID(final long idSchiene) throws DeveloperNotificationException {
		// Datenkonsistenz überprüfen.
		DeveloperNotificationException.ifTrue("Die Schiene " + _parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager entfernt werden!",
				_parent.schieneGetExistiert(idSchiene));
		final int nKurse = getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + _parent.toStringSchiene(idSchiene) + " hat noch " + nKurse + " Kurse!",
				nKurse > 0);

		// Entfernen (eigentlich nicht nötig, da es bei der Revalidierung auch passiert)
		// _ergebnis.schienen.remove(getSchieneE(idSchiene))

		// Bewertungen aktualisieren.
		stateRevalidateEverything();
	}

	/**
	 * Liefert die Anzahl an E-Schienen.
	 *
	 * @return die Anzahl an E-Schienen.
	 */
	public int getAnzahlSchienen() {
		return _ergebnis.schienen.size();
	}

	/**
	 * Liefert TRUE, falls die E-Schiene existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 * @return TRUE, falls die E-Schiene existiert.
	 */
	private boolean getOfSchieneExists(final long idSchiene) {
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			if (eSchiene.id == idSchiene)
				return true;
		return false;
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	private @NotNull GostBlockungsergebnisSchiene getSchieneE(final long idSchiene) throws DeveloperNotificationException {
		for (final @NotNull GostBlockungsergebnisSchiene eSchiene : _ergebnis.schienen)
			if (eSchiene.id == idSchiene)
				return eSchiene;
		throw new DeveloperNotificationException("Es gibt keine E-Schiene mit ID " + idSchiene + "!");
	}

	/**
	 * Liefert TRUE, falls die Schiene keine Kurse enthält.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene keine Kurse enthält.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public boolean getOfSchieneIstLeer(final long idSchiene) throws DeveloperNotificationException {
		return getSchieneE(idSchiene).kurse.isEmpty();
	}

}

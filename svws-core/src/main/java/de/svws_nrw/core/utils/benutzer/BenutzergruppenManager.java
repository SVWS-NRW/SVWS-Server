package de.svws_nrw.core.utils.benutzer;

import java.util.HashSet;
import java.util.List;

import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link BenutzergruppeDaten}. Hierbei werden
 * auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class BenutzergruppenManager {

    /** Die Daten der Benutzergruppe, die im Manager vorhanden sind. */
    private final @NotNull BenutzergruppeDaten _daten;

    /** Die Menge an Kompetenzen, die dieser Gruppe zugeordnet ist. */
    private final @NotNull HashSet<@NotNull Long> _setKompetenzen = new HashSet<>();


    /**
     * Erstellt einen neuen Manager mit leeren Daten für eine Benutzergruppe.
     *
     * @param id            die ID der Benutzergruppe
     * @param bezeichnung   die Bezeichnung der Benutzergruppe
     */
    public BenutzergruppenManager(final long id, final @NotNull String bezeichnung) {
        _daten = new BenutzergruppeDaten();
        _daten.id = id;
        _daten.bezeichnung = bezeichnung;
        _daten.istAdmin = false;
    }

    /**
     * Erstellt einen neuen Manager mit den Daten einer Benutzergruppe
     *
     * @param pDaten          die Benutzergruppendaten
     */
    public BenutzergruppenManager(final @NotNull BenutzergruppeDaten pDaten) {
        this._daten = pDaten;
        // Aktualisiere die lokalen Datenstrukturen
        for (final Long kID : pDaten.kompetenzen) {
            if (kID == null)
                throw new NullPointerException("Fehlerhafte Daten: Die Liste der Kompetenzen darf keine Null-Werte enthalten.");
            if (_setKompetenzen.contains(kID))
                throw new IllegalArgumentException("Die Kompetenz mit der ID " + kID + " wurde mehrfach bei der Gruppe eingetragen.");
            _setKompetenzen.add(kID);
        }
    }


    /**
     * Gibt die Benutzergruppen-Daten zurück.
     *
     * @return die Benutzergruppen-Daten (siehe {@link BenutzergruppeDaten})
     */
    public @NotNull BenutzergruppeDaten daten() {
        return this._daten;
    }


    /**
     * Gibt die ID der Benutzergruppe zurück.
     *
     * @return die ID der Benutzergruppe
     */
    public long getID() {
        return this._daten.id;
    }

    /**
     * Gibt die Bezeichnung der Benutzergruppe zurück.
     *
     * @return die Bezeichnung der Benutzergruppe
     */
    public @NotNull String getBezeichnung() {
        return this._daten.bezeichnung;
    }


    /**
     * Setzt die Bezeichnung der Benutzergruppe.
     *
     * @param bezeichnung  die neue Bezeichnung der Benutzergruppe
     */
    public void setBezeichnung(final @NotNull String bezeichnung) {
        if ("".equals(bezeichnung))
            throw new IllegalArgumentException("Die Bezeichnung einer Benutzergruppe darf nicht leer sein.");
        this._daten.bezeichnung = bezeichnung;
    }

    /**
     * Setzt, ob es sich um eine administrative Gruppe handelt oder nicht
     *
     * @param istAdmin   true, falls die Gruppe administrativ ist und ansonsten
     */
    public void setAdmin(final boolean istAdmin) {
        _daten.istAdmin = istAdmin;
    }


    /**
     * Gibt zurück, ob es sich um eine administrative Gruppe handelt oder nicht.
     *
     * @return true, falls es sich um eine administrative Gruppe handelt und ansonsten false
     */
    public boolean istAdmin() {
        return this._daten.istAdmin;
    }


    /**
     * Prüft, ob die Gruppe die angebene Kompetenz besitzt oder nicht.
     *
     * @param kompetenz   die zu prüfende Kompetenz
     *
     * @return true, falls die Gruppe die Kompetenz besitzt.
     */
    public boolean hatKompetenz(final @NotNull BenutzerKompetenz kompetenz) {
        if (this._daten.istAdmin)
            return true;
        return _setKompetenzen.contains(kompetenz.daten.id);
    }

    /**
     * Prüft, ob die Gruppe alle angebenen Kompetenzen besitzt oder nicht.
     *
     * @param kompetenzen   die zu prüfenden Kompetenzen
     *
     * @return true, falls die Gruppe die Kompetenzen besitzt.
     */
    public boolean hatKompetenzen(final @NotNull List<@NotNull BenutzerKompetenz> kompetenzen) {
        if (this._daten.istAdmin)
            return true;
        for (final @NotNull BenutzerKompetenz kompetenz : kompetenzen)
            if (!_setKompetenzen.contains(kompetenz.daten.id))
                return false;
        return true;
    }


    /**
     * Prüft, ob die Gruppe mindestens eine der angebenen Kompetenzen besitzt oder nicht.
     *
     * @param kompetenzen   die zu prüfenden Kompetenzen
     *
     * @return true, falls die Gruppe mindestens eine der Kompetenzen besitzt.
     */
    public boolean hatKompetenzenMindestensEine(final @NotNull List<@NotNull BenutzerKompetenz> kompetenzen) {
        if (this._daten.istAdmin)
            return true;
        for (final @NotNull BenutzerKompetenz kompetenz : kompetenzen)
            if (_setKompetenzen.contains(kompetenz.daten.id))
                return true;
        return false;
    }


    /**
     * Fügt die übergebene Kompetenz zu der Gruppe hinzu.
     *
     * @param kompetenz   die Kompetenz, die hinzugefügt wird
     *
     * @throws IllegalArgumentException   wenn die Gruppe die Kompetenz bereits enthält
     */
    public void addKompetenz(final BenutzerKompetenz kompetenz) throws IllegalArgumentException {
        if (kompetenz == null)
            throw new NullPointerException("Die übergenene Kompetenz darf nicht null sein.");
        if (_setKompetenzen.contains(kompetenz.daten.id))
            throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " wurde bereits zuvor zu der Gruppe hinzugefügt.");
        this._daten.kompetenzen.add(kompetenz.daten.id);
        _setKompetenzen.add(kompetenz.daten.id);
    }


    /**
     * Entfernt die übergebene Kompetenz aus der Gruppe.
     *
     * @param kompetenz   die Kompetenz, die entfernt wird
     *
     * @throws IllegalArgumentException   wenn die Gruppe die Kompetenz nicht enthält
     */
    public void removeKompetenz(final @NotNull BenutzerKompetenz kompetenz) throws IllegalArgumentException {
        if (!_setKompetenzen.contains(kompetenz.daten.id))
            throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " ist in der Gruppe nicht vorhanden.");
        this._daten.kompetenzen.remove(kompetenz.daten.id);
        _setKompetenzen.remove(kompetenz.daten.id);
    }

}

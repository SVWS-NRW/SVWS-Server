package de.svws_nrw.core.utils.benutzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.benutzer.BenutzerDaten;
import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten des Typs {@link BenutzerDaten}. Hierbei
 * werden
 * auch Hilfsmethoden zur Interpretation der Daten erzeugt.
 */
public class BenutzerManager {

    /** Die Daten des Benutzers, die im Manager vorhanden sind. */
    private final @NotNull BenutzerDaten _daten;

    /** Eine Map zum schnellen Zugriff auf die Benutzergruppendaten */
    private final @NotNull HashMap<@NotNull Long, @NotNull BenutzergruppeDaten> _mapGruppen = new HashMap<>();

    /** Die Ids der Benutzergruppen des Benutzers */
    private final @NotNull HashSet<@NotNull Long> _setGruppenIDs = new HashSet<>();

    /**
     * Eine Map, welche zu einer Kompetenz eine Liste zuordnet, welche alle
     * Benutzer-Gruppen beinhaltet, von denen
     * der Benutzer die Kompetenz erhalten hat.
     */
    private final @NotNull HashMap<@NotNull BenutzerKompetenz, @NotNull Vector<@NotNull BenutzergruppeDaten>> _mapKompetenzenVonGruppe = new HashMap<>();

    /** Die Menge an Kompetenzen, die diesem Benutzer direkt zugeordnet ist. */
    private final @NotNull HashSet<@NotNull BenutzerKompetenz> _setKompetenzen = new HashSet<>();

    /**
     * Die Menge an Kompetenzen, die diesem Benutzer entweder direkt oder über
     * mindestens eine Gruppe zugeordnet ist.
     */
    private final @NotNull HashSet<@NotNull BenutzerKompetenz> _setKompetenzenAlle = new HashSet<>();

    /**
     * Erstellt einen neuen Manager mit leeren Daten für einen Benutzer
     * 
     * @param id die ID des Benutzers
     * 
     */
    public BenutzerManager(final long id) {
        init();
        _daten = new BenutzerDaten();
        _daten.id = id;
        _daten.istAdmin = false;
    }

    /**
     * Erstellt einen neuen Manager mit den Daten eines Benutzers
     * 
     * @param pDaten die BenutzerDaten
     */
    public BenutzerManager(final @NotNull BenutzerDaten pDaten) {
        init();
        this._daten = pDaten;
        // Aktualisiere die lokalen Datenstrukturen - die direkt zugeordnete Kompetenzen
        for (final @NotNull Long kID : pDaten.kompetenzen) {
            if (kID == null)
                throw new NullPointerException("Fehlerhafte Daten: Die Liste der Kompetenzen darf keine Null-Werte enthalten.");
            final BenutzerKompetenz komp = BenutzerKompetenz.getByID(kID);
            if (komp == null)
                throw new NullPointerException("Fehlerhafte Daten: Die Kompetenz mit der ID " + kID + " existiert nicht.");
            if (_setKompetenzen.contains(komp))
                throw new IllegalArgumentException("Die Kompetenz mit der ID " + kID + " wurde mehrfach bei der Gruppe eingetragen.");
            _setKompetenzen.add(komp);
            _setKompetenzenAlle.add(komp);
        }
        // Aktualisiere die lokalen Datenstrukturen - die über Gruppen zugeordneten
        // Kompetenzen
        for (final @NotNull BenutzergruppeDaten bgd : _daten.gruppen)
            addGruppe(bgd);
    }

    /**
     * Aktualisiere die lokalen Datenstrukturen - die über Gruppen zugeordneten  Kompetenzen
     * 
     * @param bgd die Benutzergruppendaten
     */
    private void addGruppe(final BenutzergruppeDaten bgd) {
        if (bgd == null)
            return;
        _mapGruppen.put(bgd.id, bgd);
        _setGruppenIDs.add(bgd.id);
        for (final @NotNull Long kid : bgd.kompetenzen) {
            final BenutzerKompetenz komp = BenutzerKompetenz.getByID(kid);
            if (komp == null)
                throw new NullPointerException("Fehlerhafte Daten: Die Kompetenz mit der ID " + kid + " existiert nicht.");
            // Aktualisiere die Menge der zugeordneten Kompetenzen
            _setKompetenzenAlle.add(komp);
            // Speichere über welche Gruppe die Kompetenz zugeordnet wurde.
            final Vector<@NotNull BenutzergruppeDaten> gruppen = _mapKompetenzenVonGruppe.get(komp) ;
            if (gruppen == null)
                throw new NullPointerException("Vector existiert nicht, müsste aber zuvor initialisiert worden sein.");
            gruppen.add(bgd);
         }        
    }
    
    
    /**
     * Aktualisiere die lokalen Datenstrukturen - die über Gruppen zugeordneten  Kompetenzen
     * 
     * @param bgd die Benutzergruppendaten
     */
    private void removeGruppe(final BenutzergruppeDaten bgd) {
    	if (bgd == null)
    		return;
    	_mapGruppen.remove(bgd.id);
    	_setGruppenIDs.remove(bgd.id);
    	 for (final @NotNull Long kid : bgd.kompetenzen) {
            final BenutzerKompetenz komp = BenutzerKompetenz.getByID(kid);
    		if (komp != null){
    		    final Vector< @NotNull BenutzergruppeDaten> gruppen = _mapKompetenzenVonGruppe.get(komp);
    		    if (gruppen == null)
                    throw new NullPointerException("Vector existiert nicht, müsste aber zuvor initialisiert worden sein.");
		        for (int i = gruppen.size() - 1; i >= 0; i--)
		            if (gruppen.elementAt(i).id == bgd.id)
		                gruppen.removeElementAt(i);
		        if (gruppen.isEmpty() && !this._setKompetenzen.contains(komp))
		            this._setKompetenzenAlle.remove(komp);
    		}
    	 }	
	}
    
    /**
     * Liefert true, wenn der Benutzer in einer adminstrativen Gruppe ist, sonst false.
     * 
     * @return true, wenn der Benutzer in einer administrativen Gruppe ist.
     */

    public boolean  istInAdminGruppe() {
       for(final BenutzergruppeDaten bg : _mapGruppen.values()) {
           if(bg.istAdmin)
               return true;
       }
       return false;
    }
    
//    /**
//     * Liefert true, wenn der Benutzer durch die übergebene Gruppe in einer adminstrativen Gruppe ist, sonst false.
//     * 
//     *
//     * 
//     * @return true, wenn der Benutzer in einer administrativen Gruppe ist.
//     */
//    public @NotNull List<@NotNull BenutzergruppeDaten> getAdminGruppen() {
//        Vector<@NotNull BenutzergruppeDaten> gruppen = new Vector<@NotNull BenutzergruppeDaten>();
//        
//        return null;
//        
//    }
    
    
    /**
     * Initialisiert die lokalen Datenstrukturen.
     */
    private void init() {
    	// Erzeuge leere Vektoren für einzelnen Komptenzen
    	for (final @NotNull BenutzerKompetenz p : BenutzerKompetenz.values())
    		_mapKompetenzenVonGruppe.put(p, new Vector<>());
    }

    /**
     * Gibt für die übergebene Benutzerkompetenz eine Liste der
     * Benutzergruppen-Daten
     * zurück, welche dem Benutzer die Kompetenz zu geordnet haben.
     * 
     * @param kompetenz die Benutzerkompetenz
     * 
     * @return die Liste der Benutzergruppen-Daten
     */
    public @NotNull List<@NotNull BenutzergruppeDaten> getGruppen(final @NotNull BenutzerKompetenz kompetenz) {
        final Vector<@NotNull BenutzergruppeDaten> gruppen = _mapKompetenzenVonGruppe.get(kompetenz);
        if (gruppen == null)
            throw new NullPointerException("Die interne Datenstruktur _mapKompetenzenVonGruppe wurde nich korrekt initialisiert.");
        return gruppen;
    }

    /**
     * Gibt die Benutzer-Daten zurück.
     * 
     * @return die Benutzer-Daten (siehe {@link BenutzerDaten})
     */
    public @NotNull BenutzerDaten daten() {
        return this._daten;
    }

    /**
     * Gibt die ID des Benutzers zurück.
     * 
     * @return die ID des Benutzers
     */
    public long getID() {
        return this._daten.id;
    }

    /**
     * Gibt die BenutzerGruppen des Benutzers zurück.
     * 
     * @return Gibt die BenutzerGruppen des Benutzers zurück.
     */
    public @NotNull List<@NotNull BenutzergruppeDaten> getBenutzerGruppen() {
        return this._daten.gruppen;
    }
    
    /**
     * Gibt den Anmeldenamen des Benutzers zurück.
     * 
     * @return Gibt den Anmeldenamen des Benutzers zurück.
     */
    public @NotNull String getAnmeldename() {
        return this._daten.name;
    }

    /**
     * Setzt den Anmeldenamen des Benutzers.
     * 
     * @param name der neue Anmeldename des Benutzers
     */
    public void setAnmeldename(final @NotNull String name) {
        if ("".equals(name))
            throw new IllegalArgumentException("Der Anmeldename eines Benutzers darf nicht leer sein.");
        this._daten.name = name;
    }

    /**
     * Gibt denAnzeigenamen des Benutzers zurück.
     * 
     * @return Gibt den Anzeigenamen des Benutzers zurück.
     */
    public @NotNull String getAnzeigename() {
        return this._daten.anzeigename;
    }

    /**
     * Setzt den Anzeigenamen des Benutzers.
     * 
     * @param name der neue Anzeigenamen des Benutzers
     */
    public void setAnzeigename(final @NotNull String name) {
        if ("".equals(name))
            throw new IllegalArgumentException("Der Anmeldename eines Benutzers darf nicht leer sein.");
        this._daten.anzeigename = name;
    }

    /**
     * Setzt, ob es sich um einen administrativen Benutzer handelt oder nicht
     * 
     * @param istAdmin true, falls der Benutzer administrativ ist und ansonsten
     *                 false
     */
    public void setAdmin(final boolean istAdmin) {
        _daten.istAdmin = istAdmin;
    }

    /**
     * Gibt zurück, ob es sich um einen administrativen Benutzer handelt oder nicht.
     * 
     * @return true, falls es sich um einen administrativen Benutzer handelt und
     *         ansonsten false
     */
    public boolean istAdmin() {
        return this._daten.istAdmin || this.istInAdminGruppe();
    }

    /**
     * Prüft, ob der Benutzer die angebene Kompetenz direkt oder über eine Gruppe
     * besitzt oder nicht.
     * 
     * @param kompetenz die zu prüfende Kompetenz
     * 
     * @return true, falls der Benutzer die Kompetenz besitzt.
     */
    public boolean hatKompetenz(final @NotNull BenutzerKompetenz kompetenz) {
        if (this._daten.istAdmin || istInAdminGruppe())
            return true;
        return _setKompetenzenAlle.contains(kompetenz);
    }

    /**
     * Prüft, ob der Benutzer alle angebenen Kompetenzen direkt oder über eine
     * Gruppe
     * besitzt oder nicht.
     * 
     * @param kompetenzen die zu prüfenden Kompetenzen
     * 
     * @return true, falls der Benutzer die Kompetenzen besitzt.
     */
    public boolean hatKompetenzen(final @NotNull List<@NotNull BenutzerKompetenz> kompetenzen) {
        if (this._daten.istAdmin)
            return true;
        for (final @NotNull BenutzerKompetenz kompetenz : kompetenzen)
            if (!_setKompetenzenAlle.contains(kompetenz))
                return false;
        return true;
    }

    /**
     * Prüft, ob der Benutzer mindestens eine der angebenen Kompetenzen direkt oder
     * über eine Gruppe
     * besitzt oder nicht.
     * 
     * @param kompetenzen die zu prüfenden Kompetenzen
     * 
     * @return true, falls der Benutzer mindestens eine der Kompetenzen besitzt.
     */
    public boolean hatKompetenzenMindestensEine(final @NotNull List<@NotNull BenutzerKompetenz> kompetenzen) {
        if (this._daten.istAdmin)
            return true;
        for (final @NotNull BenutzerKompetenz kompetenz : kompetenzen)
            if (_setKompetenzenAlle.contains(kompetenz))
                return true;
        return false;
    }

    /**
     * Fügt die übergebene Kompetenz direkt bei dem Benutzer hinzu.
     * 
     * @param kompetenz die Kompetenz, die hinzugefügt wird
     * 
     * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
     */
    public void addKompetenz(final BenutzerKompetenz kompetenz) throws IllegalArgumentException {
        if (kompetenz == null)
            throw new NullPointerException("Die übergebene Kompetenz darf nicht null sein.");
        if (_setKompetenzen.contains(kompetenz))
            throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id
                    + " wurde bereits zuvor zu dem Benutzer hinzugefügt.");
        this._daten.kompetenzen.add(kompetenz.daten.id);
        _setKompetenzen.add(kompetenz);
        _setKompetenzenAlle.add(kompetenz);
    }

    /**
     * Entfernt die übergebene Kompetenz bei dem Benutzer, sofern diese Kompetenz
     * ihm direkt
     * zugeordnet wure. Sollte die Kompetenz zusätzlich über eine Gruppe zugeordnet
     * sein,
     * so bleibt diese Zuordnung erhalten.
     * 
     * @param kompetenz die zu entfernende Kompetenz
     * 
     * @throws IllegalArgumentException wenn der Benutzer die Kompetenz nicht hat
     *                                  oder nur über eine Gruppe hat
     */
    public void removeKompetenz(final @NotNull BenutzerKompetenz kompetenz) throws IllegalArgumentException {
        if (!_setKompetenzen.contains(kompetenz))
            throw new IllegalArgumentException("Die Kompetenz mit der ID " + kompetenz.daten.id + " ist nicht direkt beim Benutzer vorhanden.");
        this._daten.kompetenzen.removeElement(kompetenz.daten.id);
        _setKompetenzen.remove(kompetenz);
        final @NotNull List<@NotNull BenutzergruppeDaten> gruppen = getGruppen(kompetenz);
        if (gruppen.size() == 0)
            _setKompetenzenAlle.remove(kompetenz);
    }

    /**
     * Überprüft, ob der Benutzer in einer Grupper mit der id Mitglied ist.
     * 
     * @param id  ID der Gruppe
     * 
     * @return true, falls der Benutzer in der Gruppe ist.
     */

    public boolean IstInGruppe(final @NotNull long id) {
        return this._setGruppenIDs.contains(id);
    }

    /**
     * Liefert die Anzahl der Gruppen des Benutzers
     * 
     * 
     * @return anzahl   der Gruppen
     */

    public long anzahlGruppen() {
        return this._setGruppenIDs.size();
    }   
    
    
    /**
     * Fügt den Benutzer in eine Gruppe ein
     * 
     * @param bgd die Benutzergruppe
     * 
     * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
     */

    public void addToGruppe(final @NotNull BenutzergruppeDaten bgd) {
    	if (bgd != null) {
    		this.addGruppe(bgd);
        } else
        	throw new IllegalArgumentException("Der Benutzer ist bereits in der Gruppe ");
    }

    /**
     * Entfernt den Benutzer aus der Gruppe
     * 
     * @param bgd  dei Benutzergruppe
     * 
     * @throws IllegalArgumentException wenn der Benutzer die Kompetenz bereits hat
     */

    public void removeFromGruppe(final @NotNull BenutzergruppeDaten bgd) {
    	this.removeGruppe(bgd);
    }
    
    

}

package de.svws_nrw.schuldatei.v1.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGrunddaten;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient dem Verwalten einer Organisationseinheit
 * aus der Schuldatei.
 */
public class SchuldateiOrganisationseinheitManager {

	/** Die Referenz auf den Manager für die Schuldatei */
	private final @NotNull SchuldateiManager _managerSchuldatei;

	/** Das Datenobjekt für die Schuldatei */
	private final @NotNull SchuldateiOrganisationseinheit _organisationseinheit;

	/** Cache: Eine Map der Grunddaten anhand des Schuljahres */
	private final @NotNull Map<@NotNull Integer, @NotNull SchuldateiOrganisationseinheitGrunddaten> _mapGrunddatenBySchuljahr = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Organisationseinheit und
	 * dem zugehörigen Manager für die Schuldatei.
	 *
	 * @param managerSchuldatei      der Manager für die Schuldatei
	 * @param organisationseinheit   die Organisationseinheit aus der Schuldatei
	 */
	public SchuldateiOrganisationseinheitManager(final @NotNull SchuldateiManager managerSchuldatei, final @NotNull SchuldateiOrganisationseinheit organisationseinheit) {
		this._managerSchuldatei = managerSchuldatei;
		this._organisationseinheit = organisationseinheit;
		if ((organisationseinheit.oeart != null) && (!_managerSchuldatei.katalogOrganisationseinheitarten.hatEintrag(organisationseinheit.oeart)))
			throw new IllegalArgumentException("Die Art " + organisationseinheit.oeart + " der Organisationseinheit mit der Schulnummer " + organisationseinheit.schulnummer + " hat keinen zugehörigen Katalog-Eintrag.");
		// Durchwandere die Einträge für die Grunddaten
		for (final @NotNull SchuldateiOrganisationseinheitGrunddaten grunddaten : this._organisationseinheit.grunddaten) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (this._organisationseinheit.schulnummer.intValue() != grunddaten.schulnummer)
				throw new IllegalArgumentException("Die Schulnummer " + grunddaten.schulnummer + " bei den Grunddaten passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".");
			// Prüfe den Rechtsstatus
			if (!_managerSchuldatei.katalogRechtsstatus.hatEintrag(grunddaten.rechtsstatus))
				throw new IllegalArgumentException("Der Rechtstatus " + grunddaten.rechtsstatus + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");
			/* TODO Aktuell enthält die vollständige Schuldatei nicht alle Organisationseinheiten bezüglich Schulträger
			// Prüfe, ob die Organisationseinheit für den Schulträger existiert, sofern eine angeben ist
			if ((grunddaten.schultraegernummer != 0) && (_managerSchuldatei.getOrganisationsheinheitManager(grunddaten.schultraegernummer) == null))
				throw new IllegalArgumentException("Der Schulträger " + grunddaten.schultraegernummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
			*/
			// Prüfe die Art der Trägerschaft
			if ((grunddaten.artdertraegerschaft != 0) && (!_managerSchuldatei.katalogArtDerTraegerschaft.hatEintrag(grunddaten.artdertraegerschaft)))
				throw new IllegalArgumentException("Die Art der Trägerschaft " + grunddaten.artdertraegerschaft + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");
			// Prüfe den Schulbetriebsschlüssel
			if (!_managerSchuldatei.katalogSchulbetriebsschluessel.hatEintrag(grunddaten.schulbetriebsschluessel))
				throw new IllegalArgumentException("Der Schulbetriebsschlüssel " + grunddaten.schulbetriebsschluessel + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");
			/* TODO Aktuell ist die Zuordnung von obereschulaufsicht noch nicht umsetzbar
			// Prüfe, ob die Organisationseinheit für die obere Schulaufsicht existiert, sofern eine angeben ist
			if ((grunddaten.obereschulaufsicht != 0) && (_managerSchuldatei.getOrganisationsheinheitManager(grunddaten.obereschulaufsicht) == null))
				throw new IllegalArgumentException("Die obere Schulaufsicht " + grunddaten.obereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
			*/
			/* TODO Aktuell ist die Zuordnung von untereschulaufsicht noch nicht umsetzbar
			// Prüfe, ob die Organisationseinheit für die untere Schulaufsicht existiert, sofern eine angeben ist
			if ((grunddaten.untereschulaufsicht != 0) && (_managerSchuldatei.getOrganisationsheinheitManager(grunddaten.untereschulaufsicht) == null))
				throw new IllegalArgumentException("Die untere Schulaufsicht " + grunddaten.untereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
			*/
			/* TODO
			// Prüfe, ob die Organisationseinheit für das ZfsL existiert, sofern eines angeben ist
			if ((grunddaten.zfsl != 0) && (_managerSchuldatei.getOrganisationsheinheitManager(grunddaten.zfsl) == null))
				throw new IllegalArgumentException("Das ZfsL " + grunddaten.zfsl + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
			*/
			// TODO Validiere internatsbetrieb (null oder zukünftig: Katalog-Eintrag)
			// TODO Validiere internatsplaetze (null oder > 0)
			// TODO Validiere die Schulformen
		}
		// TODO Validiere weitere Einträge der Organisationseinheit
	}


	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück.
	 *
	 * @return die Schulnummer
	 */
	public @NotNull Integer getSchulnummer() {
		return this._organisationseinheit.schulnummer;
	}

	/**
	 * Gib die Bundeslandkennung (NRW) der Organisationseinheit zurück.
	 *
	 * @return die Bundeslandkennung
	 */
	public String getBundeslandkennung() {
		return this._organisationseinheit.bundeslandkennung;
	}

	/**
	 * Gibt die eindeutige Identifier für das XSCHULE-Format zurück.
	 *
	 * @return der Identifier
	 */
	public String getXscid() {
		return this._organisationseinheit.xscid;
	}

	/**
	 * Gibt die Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Art der Organisationseinheit
	 */
	public String getArt() {
		return this._organisationseinheit.oeart;
	}

	/**
	 * Gibt die Bezeichnung der Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Bezeichnung der Art der Organisationseinheit
	 */
	public String getArtBezeichnung() {
		final SchuldateiKatalogeintrag eintrag = _managerSchuldatei.katalogOrganisationseinheitarten.getEintrag(this._organisationseinheit.oeart);
		return (eintrag == null) ? "" : eintrag.bezeichnung;
	}

	/**
	 * Gibt die amtliche Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public @NotNull String getAmtlicheBezeichnung() {
		return this._organisationseinheit.amtsbez;
	}

	/**
	 * Gibt das Datum der Errichtung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Errichtung der Organisationseinheit
	 */
	public String getDatumErrichtung() {
		return this._organisationseinheit.errichtung;
	}

	/**
	 * Gibt das Datum der Auflösung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Auflösung
	 */
	public String getDatumAufloesung() {
		return this._organisationseinheit.aufloesung;
	}


	/**
	 * Bestimmt die Grunddaten für das Schuljahr anhand der vorhandenen Grunddaten-Einträge und
	 * erzeugt einen Cache für den schnellen Zugriff auf diese Grunddaten.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Grunddaten für das Schuljahr
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	private @NotNull SchuldateiOrganisationseinheitGrunddaten getGrunddaten(final int schuljahr) throws IllegalArgumentException {
		// Prüfe, ob die Anfrage aus dem Cache beantwortet werden kann
		final SchuldateiOrganisationseinheitGrunddaten daten = _mapGrunddatenBySchuljahr.get(schuljahr);
		if (daten != null)
			return daten;
		// Wenn nicht, dann bestimme alle (!) Einträge, welche in den Zeitraum fallen ...
		final @NotNull List<@NotNull SchuldateiOrganisationseinheitGrunddaten> grunddaten = new ArrayList<>();
		for (final @NotNull SchuldateiOrganisationseinheitGrunddaten eintrag : this._organisationseinheit.grunddaten)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				grunddaten.add(eintrag);
		if (grunddaten.isEmpty())
			throw new IllegalArgumentException("Es konnten keine Grunddaten für das Schuljahr " + schuljahr + "/" + (schuljahr + 1) + " gefunden werden.");
		// ... und übernehme den Eintrag aus dem Ergebnis, dessen Gültigkeit noch am längsten währt.
		@NotNull SchuldateiOrganisationseinheitGrunddaten eintrag = grunddaten.get(0);
		for (int i = 1; i < grunddaten.size(); i++) {
			@NotNull
			final SchuldateiOrganisationseinheitGrunddaten other = grunddaten.get(0);
			if (SchuldateiUtils.istFrueher(eintrag.gueltigbis, other.gueltigbis))
				eintrag = other;
		}
		_mapGrunddatenBySchuljahr.put(schuljahr, eintrag);
		return eintrag;
	}


	/**
	 * Gibt die Kurzbezeichnung der Organisationseinheit zurück, die in dem
	 * angegebenen Schuljahr gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Kurzbezeichnung der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull String getKurzbezeichnung(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).kurzbezeichnung;
	}


	/**
	 * Gibt den Rechtsstatus der Organisationseinheit zurück ("1" für öffentlich und "2" für privat),
	 * der in dem angegebenen Schuljahr gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Rechtsstatus der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getRechtsstatus(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).rechtsstatus;
	}


	/**
	 * Gibt die Schulträgernummer der Organisationseinheit zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulträgernummer der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
    public int getSchultraegernummer(final int schuljahr) throws IllegalArgumentException {
    	return this.getGrunddaten(schuljahr).schultraegernummer;
    }


	/**
	 * Gibt die Organisationseinheit des Schulträgers dieser Organisationseinheit zurück,
	 * sofern ein Schulträger in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit des Schulträgers
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für den Schulträger in der Schuldatei keine Daten enthalten sind.
	 */
    public @NotNull SchuldateiOrganisationseinheitManager getSchultraeger(final int schuljahr) throws IllegalArgumentException {
    	final int nummer = this.getGrunddaten(schuljahr).schultraegernummer;
    	final SchuldateiOrganisationseinheitManager schultraeger = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
    	if (schultraeger == null)
    		throw new IllegalArgumentException("Der Schulträger " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
    	return schultraeger;
    }


	/**
	 * Gibt die Art der Trägerschaft der Organisationseinheit zurück,
	 * welche in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Art der Trägerschaft der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getArtDerTraegerschaft(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).artdertraegerschaft;
	}

	/**
	 * Gibt den Betriebsschlüssel der Schule zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Betriebsschlüssel der Schule
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getSchulbetriebsschluessel(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).schulbetriebsschluessel;
	}


	/**
	 * Gibt das Kapitel der Schule zurück, welches in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return das Kapitel der Schule
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getKapitel(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).kapitel;
	}


	/**
	 * Gibt die Schulnummer der oberen Schulaufsichtsbehörde zurück, welche in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getObereSchulaufsichtNummer(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).obereschulaufsicht;
	}


	/**
	 * Gibt die Organisationseinheit der oberen Schulaufsichtsbehörde dieser Organisationseinheit zurück,
	 * sofern eine obere Schulaufsichtsbehörde in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit der oberen Schulaufsichtsbehörde
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für obere Schulaufsichtsbehörde in der Schuldatei keine Daten enthalten sind.
	 */
    public @NotNull SchuldateiOrganisationseinheitManager getObereSchulaufsicht(final int schuljahr) throws IllegalArgumentException {
    	final int nummer = this.getGrunddaten(schuljahr).obereschulaufsicht;
    	final SchuldateiOrganisationseinheitManager schulaufsicht = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
    	if (schulaufsicht == null)
    		throw new IllegalArgumentException("Die obere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
    	return schulaufsicht;
    }


	/**
	 * Gibt die Schulnummer der unteren Schulaufsichtsbehörde zurück, welche in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getUntererSchulaufsichtNummer(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).untereschulaufsicht;
	}


	/**
	 * Gibt die Organisationseinheit der unteren Schulaufsichtsbehörde dieser Organisationseinheit zurück,
	 * sofern eine untere Schulaufsichtsbehörde in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit der unteren Schulaufsichtsbehörde
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für untere Schulaufsichtsbehörde in der Schuldatei keine Daten enthalten sind.
	 */
    public @NotNull SchuldateiOrganisationseinheitManager getUntereSchulaufsicht(final int schuljahr) throws IllegalArgumentException {
    	final int nummer = this.getGrunddaten(schuljahr).untereschulaufsicht;
    	final SchuldateiOrganisationseinheitManager schulaufsicht = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
    	if (schulaufsicht == null)
    		throw new IllegalArgumentException("Die untere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
    	return schulaufsicht;
    }


	/**
	 * Gibt die Schulnummer des ZfsL zurück, welches in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getZfsLNummer(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).zfsl;
	}


	/**
	 * Gibt die Organisationseinheit des ZfsL dieser Organisationseinheit zurück,
	 * sofern ein ZfsL in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit des ZfsL
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für das ZfsL in der Schuldatei keine Daten enthalten sind.
	 */
    public @NotNull SchuldateiOrganisationseinheitManager getZfsL(final int schuljahr) throws IllegalArgumentException {
    	final int nummer = this.getGrunddaten(schuljahr).zfsl;
    	final SchuldateiOrganisationseinheitManager zfsl = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
    	if (zfsl == null)
    		throw new IllegalArgumentException("Das ZfsL " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
    	return zfsl;
    }


	/**
	 * Gibt den Dienststellenschlüssel der Organisationseinheit zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Dienststellenschlüssel der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getDienststellenschluessel(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).dienststellenschluessel;
	}


	/**
	 * Gibt den Personalteilbereich der Organisationseinheit an, sofern einer in dem
	 * angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Personalteilbereich der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public String getPersonalteilbereich(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).ptb;
	}


	/**
	 * Gibt die Art des Internatsbetriebs an, sofern einer in dem
	 * angegebenen Schuljahr vorhanden ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Art des Internatsbetriebs
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public String getInternatsbetrieb(final int schuljahr) throws IllegalArgumentException {
		return this.getGrunddaten(schuljahr).internatsbetrieb;
	}


	/**
	 * Gibt die Anzahl der Internatsplätze zurück, sofern ein Internatsbetrieb
	 * vorliegt und ansonsten 0;
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Anzahl der Internatsplätze
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public int getInternatsplaetze(final int schuljahr) throws IllegalArgumentException {
		final SchuldateiOrganisationseinheitGrunddaten grunddaten = this.getGrunddaten(schuljahr);
		return grunddaten.internatsplaetze == null ? 0 : grunddaten.internatsplaetze;
	}

}

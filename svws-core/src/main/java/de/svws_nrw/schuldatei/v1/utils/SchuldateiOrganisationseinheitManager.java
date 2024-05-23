package de.svws_nrw.schuldatei.v1.utils;

import de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit;
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

}

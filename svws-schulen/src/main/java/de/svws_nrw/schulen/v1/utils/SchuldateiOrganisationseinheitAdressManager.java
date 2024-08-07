package de.svws_nrw.schulen.v1.utils;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitAdresse;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitErreichbarkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient dem Verwalten einer Adresse einer Organisationseinheit
 * aus der Schuldatei.
 */
public class SchuldateiOrganisationseinheitAdressManager {

	/** Die Referenz auf den Manager für die Schuldatei */
	private final @NotNull SchuldateiManager _managerSchuldatei;

	/** Die Referenz auf den Manager der Organisationseinheit */
	private final @NotNull SchuldateiOrganisationseinheitManager _managerOrganisationseinheit;

	/** Das Datenobjekt für die Adresse aus der Organisationseinheit */
	private final @NotNull SchuldateiOrganisationseinheitAdresse _adresse;

	/** Die Festnetznummern (codekey=02) zu der Adresse */
	private final @NotNull List<String> _festnetzNummern;

	/** Die Mobilnummern (codekey=03) zu der Adresse */
	private final @NotNull List<String> _mobilNummern;

	/** Die Faxnummern (codekey=04) zu der Adresse */
	private final @NotNull List<String> _faxNummern;

	/** Die Emails (codekey=01) zu der Adresse */
	private final @NotNull List<String> _emailAdressen;

	/** Die URLs (codekey=09) zu der Adresse */
	private final @NotNull List<String> _webAdressen;

	/** Die Art der Adresse */
	private final @NotNull String _artDerAdresse;

	/** Gibt an, ob es sich um den Hauptstandort der Organisationseinheit handelt oder nicht */
	private final boolean _istHauptstandort;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Adresse und
	 * den beiden zugehörigen Managern für die Schuldatei sowie die Organisationseinheit.
	 *
	 * @param managerSchuldatei             der Manager für die Schuldatei
	 * @param managerOrganisationseinheit   der Manager für die Organisationseinheit
	 * @param adresse                       die Adresse der Organisationseinheit
	 * @param erreichbarkeiten				die Erreichbarkeiten der Organisationseinheit
	 */
	public SchuldateiOrganisationseinheitAdressManager(final @NotNull SchuldateiManager managerSchuldatei,
			final @NotNull SchuldateiOrganisationseinheitManager managerOrganisationseinheit,
			final @NotNull SchuldateiOrganisationseinheitAdresse adresse,
			final @NotNull List<SchuldateiOrganisationseinheitErreichbarkeit> erreichbarkeiten) {
		this._managerSchuldatei = managerSchuldatei;
		this._managerOrganisationseinheit = managerOrganisationseinheit;
		this._adresse = adresse;
		this._festnetzNummern = new ArrayList<>();
		this._mobilNummern = new ArrayList<>();
		this._faxNummern = new ArrayList<>();
		this._emailAdressen = new ArrayList<>();
		this._webAdressen = new ArrayList<>();

		// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
		if (this._managerOrganisationseinheit.getSchulnummer() != _adresse.schulnummer)
			throw new IllegalArgumentException("Die Schulnummer " + _adresse.schulnummer + " bei der Adresse mit der ID " + _adresse.id
					+ " passt nicht zu der Schulnummer der Organisationseinheit " + this._managerOrganisationseinheit.getSchulnummer() + ".");

		// Prüfe das Feld qualitaetverortung - Hier wird noch ein Katalog ergänzt
		if (!_managerSchuldatei.katalogQualitaetenVerortung.hatEintrag(_adresse.qualitaetverortung))
			throw new IllegalArgumentException("Der Wert von QualitätVerortung '" + _adresse.qualitaetverortung + "' bei der Adresse mit der ID " + _adresse.id
					+ " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer()
					+ " ist im zugehörigen Katalog nicht vorhanden.");

		// Prüfe, ob die Art der Adresse (bzw. adresstypid) im Katalog existiert
		this._artDerAdresse = (_adresse.adresstypeid == null) ? "" : ("" + _adresse.adresstypeid);
		if (!_managerSchuldatei.katalogAddressarten.hatEintrag(this._artDerAdresse))
			throw new IllegalArgumentException("Die Art der Adresse '" + this._artDerAdresse + "' bei der Adresse mit der ID " + _adresse.id
					+ " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer()
					+ " ist im zugehörigen Katalog nicht vorhanden.");

		// Prüfe, ob der Wert von hauptstandortadresse bei dieser Adresse gültig ist
		// hauptstandortadresse 1->ja (Sitz), 0->nein (kein Sitz) - Hier wird noch ein Katalog ergänzt
		if (!_managerSchuldatei.katalogHauptstandort.hatEintrag(_adresse.hauptstandortadresse))
			throw new IllegalArgumentException(
					"Der Wert von Hauptstandortadresse '" + _adresse.hauptstandortadresse + "' bei der Adresse mit der ID " + _adresse.id
							+ " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer()
							+ " ist im zugehörigen Katalog nicht vorhanden.");
		// Ermittle, ob es sich um einen Hauptstandort handelt oder nicht
		this._istHauptstandort = "1".equals(adresse.hauptstandortadresse);

		// Ermittle die Erreichbarkeiten zu dieser Adresse und speichere sie typbezogen in der Map
		for (final @NotNull SchuldateiOrganisationseinheitErreichbarkeit erreichbarkeit : erreichbarkeiten) {
			if (((erreichbarkeit.liegenschaft == 0) || (erreichbarkeit.liegenschaft == adresse.liegenschaft)) // Erreichbarkeit gehört zur Adresse
					&& (erreichbarkeit.codekey != null) && (SchuldateiUtils.pruefeUeberlappung(erreichbarkeit, adresse))) { // Überlappung zeitlich vorhanden (codekey != null wegen Transpiler)
				if (erreichbarkeit.codekey.compareTo("01") == 0)
					this._emailAdressen.add(erreichbarkeit.codewert);
				else if (erreichbarkeit.codekey.compareTo("02") == 0)
					this._festnetzNummern.add(erreichbarkeit.codewert);
				else if (erreichbarkeit.codekey.compareTo("03") == 0)
					this._mobilNummern.add(erreichbarkeit.codewert);
				else if (erreichbarkeit.codekey.compareTo("04") == 0)
					this._faxNummern.add(erreichbarkeit.codewert);
				else if (erreichbarkeit.codekey.compareTo("09") == 0)
					this._webAdressen.add(erreichbarkeit.codewert);
				// TODO DeveloperNotificationException verfügbar machen
				//else
				//	throw new DeveloperNotificationException("Der Erreichbarkeitsart codekey=" +  erreichbarkeit.codekey + " wird noch nicht unterstützt");
			}
		}
	}


	/**
	 * Gibt die ID der Adresse zurück.
	 *
	 * @return die ID der Adresse
	 */
	public int getID() {
		return this._adresse.id;
	}


	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück, zu der die Adresse gehört.
	 *
	 * @return die Schulnummer der Organisationseinheit
	 */
	public int getSchulnummer() {
		return this._adresse.schulnummer;
	}


	/**
	 * Gibt die Nummer der Liegenschaft der Organisationseinheit zurück, zu der die Adresse gehört.
	 *
	 * @return die Nummer der Liegenschaft der Organisationseinheit
	 */
	public int getLiegenschaftnummer() {
		return this._adresse.liegenschaft;
	}


	/**
	 * Gibt die Straße zurück.
	 *
	 * @return die Straße
	 */
	public @NotNull String getStrasse() {
		return this._adresse.strasse;
	}


	/**
	 * Gibt die Postleitzahl zurück.
	 *
	 * @return die Postleitzahl
	 */
	public @NotNull String getPostleitzahl() {
		return this._adresse.postleitzahl;
	}


	/**
	 * Gibt den Ort zurück.
	 *
	 * @return der Ort
	 */
	public @NotNull String getOrt() {
		return this._adresse.ort;
	}


	/**
	 * Gibt den Regionalschlüssel zurück.
	 *
	 * @return der Regionalschlüssel
	 */
	public @NotNull String getRegionalschluessel() {
		return this._adresse.regionalschluessel;
	}


	/**
	 * Gibt die Qualität der Verortung (siehe zugehöriger Katalog) zurück.
	 *
	 * @return die Qualität der Verortung
	 */
	public long getQualitaetVerortung() {
		return this._adresse.qualitaetverortung;
	}


	/**
	 * Gibt den Koordinatenrechtswert zurück.
	 *
	 * @return der Koordinatenrechtswert
	 */
	public long getKoordinatenrechtswert() {
		return this._adresse.koordinaterechtswert;
	}


	/**
	 * Gibt den Koordinatenhochwert zurück.
	 *
	 * @return der Koordinatenhochwert
	 */
	public long getKoordinatenhochwert() {
		return this._adresse.koordinatehochwert;
	}


	/**
	 * Gibt die Art der Adresse (siehe zugehöriger Katalog) zurück.
	 *
	 * @return die Art der Adresse
	 */
	public @NotNull String getArtDerAdresse() {
		return this._artDerAdresse;
	}


	/**
	 * Gibt das Standortkennzeichen des Teilstandorts zurück.
	 *
	 * @return das Standortkennzeichen des Teilstandorts
	 */
	public int getStandortkennzeichen() {
		return this._adresse.standortkennzeichen;
	}


	/**
	 * Gibt das Adresskennzeichen zurück.
	 *
	 * @return das Adresskennzeichen
	 */
	public @NotNull String getAdresskennzeichen() {
		return this._adresse.adresskennzeichen;
	}


	/**
	 * Gibt die Information zur Hauptstandortadresse (siehe zugehöriger Katalog)
	 * zurück.
	 *
	 * @return die Information zur Hauptstandortadresse
	 */
	public @NotNull String getHauptstandortadresse() {
		return this._adresse.hauptstandortadresse;
	}


	/**
	 * Gibt zurück, ob es sich um den Hauptstandort handelt.
	 *
	 * @return true, wenn es sich um den Hauptstandort handelt, und ansonsten false
	 */
	public boolean istHauptstandort() {
		return this._istHauptstandort;
	}


	/**
	 * Gibt das Datum zurück, ab dem diese Adresse gültig ist.
	 *
	 * @return das Datum, ab dem diese Adresse gültig ist.
	 */
	public String getGueltigAb() {
		return this._adresse.gueltigab;
	}


	/**
	 * Gibt das Datum zurück, bis zu welchem diese Adresse gültig ist.
	 *
	 * @return das Datum, bis zu welchem diese Adresse gültig ist.
	 */
	public String getGueltigBis() {
		return this._adresse.gueltigbis;
	}


	/**
	 * Gibt die Festnetznummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Festnetznummern
	 */
	public @NotNull List<String> getFestnetznummern() {
		return this._festnetzNummern;
	}


	/**
	 * Gibt die Mobilnummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Mobilnummern
	 */
	public @NotNull List<String> getMobilnummern() {
		return this._mobilNummern;
	}


	/**
	 * Gibt die Faxnummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Faxnummern
	 */
	public @NotNull List<String> getFaxnummern() {
		return this._faxNummern;
	}


	/**
	 * Gibt die Emailadressen zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Emailadressen
	 */
	public @NotNull List<String> getEmailadressen() {
		return this._emailAdressen;
	}


	/**
	 * Gibt die Webadressen zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Webadressen
	 */
	public @NotNull List<String> getWebadressen() {
		return this._webAdressen;
	}

}

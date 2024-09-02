package de.svws_nrw.schulen.v1.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schulen.v1.data.SchuldateiKatalogeintrag;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheit;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitAdresse;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitErreichbarkeit;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGliederung;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGrunddaten;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitMerkmal;
import de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitSchulform;
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

	/** Die Manager für die Adressen anhand ihrer ID */
	private final @NotNull Map<Integer, SchuldateiOrganisationseinheitAdressManager> _mapAdressManagerByID = new HashMap<>();


	/** Cache: Eine Map der Grunddaten anhand des Schuljahres */
	private final @NotNull Map<Integer, SchuldateiOrganisationseinheitGrunddaten> _mapGrunddatenBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Gliederung anhand des Schuljahres */
	private final @NotNull Map<Integer, List<SchuldateiOrganisationseinheitGliederung>> _mapGliederungenBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Eigenschaften anhand des Schuljahres */
	private final @NotNull Map<Integer, List<SchuldateiOrganisationseinheitEigenschaft>> _mapEigenschaftenBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Merkmale anhand des Schuljahres */
	private final @NotNull Map<Integer, List<SchuldateiOrganisationseinheitMerkmal>> _mapMerkmaleBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Schulform anhand des Schuljahres */
	private final @NotNull Map<Integer, String> _mapSchulformBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der SchulformASD anhand des Schuljahres */
	private final @NotNull Map<Integer, String> _mapSchulformASDBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Schulart anhand des Schuljahres */
	private final @NotNull Map<Integer, String> _mapSchulartBySchuljahr = new HashMap<>();

	/** Cache: Eine Map der Adress-Manager anhand des Schuljahres */
	private final @NotNull Map<Integer, List<SchuldateiOrganisationseinheitAdressManager>> _mapAdressenBySchuljahr = new HashMap<>();

	/** Cache: Eine Map des Hauptstandortes anhand des Schuljahres */
	private final @NotNull Map<Integer, SchuldateiOrganisationseinheitAdressManager> _mapHauptstandortBySchuljahr = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Organisationseinheit und
	 * dem zugehörigen Manager für die Schuldatei.
	 *
	 * @param managerSchuldatei      der Manager für die Schuldatei
	 * @param organisationseinheit   die Organisationseinheit aus der Schuldatei
	 */
	public SchuldateiOrganisationseinheitManager(final @NotNull SchuldateiManager managerSchuldatei,
			final @NotNull SchuldateiOrganisationseinheit organisationseinheit) {
		this._managerSchuldatei = managerSchuldatei;
		this._organisationseinheit = organisationseinheit;
		this.validate();
	}


	/**
	 * Validiere die Daten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validate() throws IllegalArgumentException {
		if ((_organisationseinheit.oeart != null) && (!_managerSchuldatei.katalogOrganisationseinheitarten.hasEintrag(_organisationseinheit.oeart)))
			throw new IllegalArgumentException("Die Art %s der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
					.formatted(_organisationseinheit.oeart, _organisationseinheit.schulnummer));
		validateGrunddaten();
		validateAdressen();
		validateMerkmale();
		validateErreichbarkeiten();
		validateEigenschaften();
		validateGliederungen();
	}


	/**
	 * Validiere die Referenzenzen auf Organisationseinheiten von dieser Organisationseinheit
	 * Die Prüfung kann erst erfolgen, wenn alle Organisationseinheiten eingelesen wurden, weswegen
	 * diese Prüfung separat erfolgt.
	 * Folgender Referenzen sind vorhanden und werden geprüft:
	 *  - grunddaten.schultraegernummer
	 *  - grunddaten.obereschulaufsicht
	 *  - grunddaten.untereschulaufsicht
	 *  - grunddaten.zfsl
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht widerspruchsfrei sind
	 */
	public void validateOeReferenzen() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitGrunddaten grunddaten : this._organisationseinheit.grunddaten) {
			// Prüfe, ob die Organisationseinheit für den Schulträger existiert, sofern eines angeben ist
			if ((!grunddaten.schultraegernummer.equals("0")) && (!grunddaten.schultraegernummer.isEmpty())
					&& (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.schultraegernummer) == null))
				throw new IllegalArgumentException(
						"Der Schulträger " + grunddaten.schultraegernummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");

			// Prüfe, ob die Organisationseinheit für die obere Schulaufsicht existiert, sofern eines angeben ist
			if ((!grunddaten.obereschulaufsicht.equals("0")) && (!grunddaten.obereschulaufsicht.isEmpty())
					&& (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.obereschulaufsicht) == null))
				throw new IllegalArgumentException(
						"Die obere Schulaufsicht " + grunddaten.obereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");

			// Prüfe, ob die Organisationseinheit für die untere Schulaufsicht, sofern eines angeben ist
			if ((!grunddaten.untereschulaufsicht.equals("0")) && (!grunddaten.untereschulaufsicht.isEmpty())
					&& (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.untereschulaufsicht) == null))
				throw new IllegalArgumentException(
						"Die untere Schulaufsicht " + grunddaten.untereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");

			// Prüfe, ob die Organisationseinheit für das ZfsL existiert, sofern eines angeben ist
			if ((!grunddaten.zfsl.equals("0")) && (!grunddaten.zfsl.isEmpty())
					&& (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.zfsl) == null))
				throw new IllegalArgumentException("Das ZfsL " + grunddaten.zfsl + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
						+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
		}
	}


	/**
	 * Validiere die Grunddaten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateGrunddaten() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitGrunddaten grunddaten : this._organisationseinheit.grunddaten) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (!this._organisationseinheit.schulnummer.equals(grunddaten.schulnummer))
				throw new IllegalArgumentException("Die Schulnummer " + grunddaten.schulnummer
						+ " bei den Grunddaten passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".");

			// Prüfe den Rechtsstatus
			if (!_managerSchuldatei.katalogRechtsstatus.hasEintragInZeitraum(grunddaten, grunddaten.rechtsstatus))
				throw new IllegalArgumentException(
						"Der Rechtstatus " + grunddaten.rechtsstatus + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");

			// Prüfe die Art der Trägerschaft
			if ((!grunddaten.artdertraegerschaft.isEmpty())
					&& (!_managerSchuldatei.katalogArtDerTraegerschaft.hasEintragInZeitraum(grunddaten, grunddaten.artdertraegerschaft)))
				throw new IllegalArgumentException(
						"Die Art der Trägerschaft " + grunddaten.artdertraegerschaft + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");

			// Prüfe den Schulbetriebsschlüssel
			if (!_managerSchuldatei.katalogSchulbetriebsschluessel.hasEintragInZeitraum(grunddaten, grunddaten.schulbetriebsschluessel))
				throw new IllegalArgumentException(
						"Der Schulbetriebsschlüssel " + grunddaten.schulbetriebsschluessel + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
								+ this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");

			// Prüfe die Internatsdaten
			if ((grunddaten.internatsbetrieb != null) && (!grunddaten.internatsbetrieb.equals("0"))) {
				// Prüfe die Schlüssel für den Internatsbetrieb
				if (!_managerSchuldatei.katalogHeimInternat.hasEintragInZeitraum(grunddaten, grunddaten.internatsbetrieb))
					throw new IllegalArgumentException(
							"Der Schlüssel für den Internatsbetrieb " + grunddaten.internatsbetrieb
									+ " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
									+ this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.");
				else if (grunddaten.internatsplaetze == 0)
					throw new IllegalArgumentException(
							"Die Internatsplätze haben einen Wert von 0 bei den Grunddaten der Organisationseinheit mit der Schulnummer "
									+ this._organisationseinheit.schulnummer + " ,obwohl Internatsbetrieb vorliegt.");
			} else {
				// Internatsbetrieb ist null OR "0" -> internatsplätze auf null oder "0" prüfen
				if (grunddaten.internatsplaetze > 0)
					throw new IllegalArgumentException(
							"Die Internatsplätze haben einen Wert > 0 bei den Grunddaten der Organisationseinheit mit der Schulnummer "
									+ this._organisationseinheit.schulnummer + " ,obwohl kein Internatsbetrieb vorliegt.");
			}

			// Validiere die Einträge bezüglich der Schulform
			validateSchulform(grunddaten);
		}
	}


	/**
	 * Bestimmt die Informationen zu der Schulform aus den Grunddaten. Diese bestehen aus
	 * drei Werte:
	 * <ol>
	 *   <li>die allgemeine Schulform</li>
	 *   <li>die speziellere Schulform (auch SchulformASD)</li>
	 *   <li>die Schulart, mit weiteren Information zu der Schule</li>
	 * </ol>
	 *
	 * @param schuljahr    das schuljahr
	 * @param grunddaten   die Grunddaten
	 *
	 * @return ein Array mit den drei Werten zur Schulform
	 */
	private static @NotNull String @NotNull [] getSchulformInfo(final int schuljahr, final @NotNull SchuldateiOrganisationseinheitGrunddaten grunddaten) {
		final @NotNull String @NotNull [] sf = { "", "", "" };
		for (final @NotNull SchuldateiOrganisationseinheitSchulform schulform : grunddaten.schulform) {
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, schulform)) {
				if ("Schulform".equals(schulform.schulformcode)) {
					sf[0] = schulform.schulformwert;
				} else if ("SchulformASD".equals(schulform.schulformcode)) {
					sf[1] = schulform.schulformwert;
				} else if ("Schulart".equals(schulform.schulformcode)) {
					sf[2] = schulform.schulformwert;
				}
			}
		}
		return sf;
	}

	/**
	 * Validiere die Schulform der übergebenen Grunddaten der Organisationseinheit
	 * Die Schulformcodes "Schulform" und "SchulformASD" müssen immer zusammen auftreten und der
	 * Schlüsselwert des Katalogeintrags von SchulformASD muss dem Wert von Schulform entsprechen!
	 *
	 * @param grunddaten   die Grundaten der Organisationseinheit.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateSchulform(final @NotNull SchuldateiOrganisationseinheitGrunddaten grunddaten) throws IllegalArgumentException {
		boolean isSchulform = false;
		boolean isSchulformASD = false;
		@NotNull String schulformWert = "";
		@NotNull String schulformAsdWert = "";
		int schuljahr = SchuldateiUtils._immerGueltigBis;
		for (final @NotNull SchuldateiOrganisationseinheitSchulform schulform : grunddaten.schulform) {
			if ("Schulform".equals(schulform.schulformcode)) {
				schulformWert = schulform.schulformwert;
				isSchulform = true;
			} else if ("SchulformASD".equals(schulform.schulformcode)) {
				if (!_managerSchuldatei.katalogSchulformen.hasEintragInZeitraum(schulform, schulform.schulformwert))
					throw new IllegalArgumentException(
							"Die SchulformASD '" + schulform.schulformwert + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer
									+ " nicht im Katalog enthalten.");
				schulformAsdWert = schulform.schulformwert;
				schuljahr = (schulform.gueltigbis == null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(schulform.gueltigbis));
				isSchulformASD = true;
			} else if ("Schulart".equals(schulform.schulformcode)) {
				if (!_managerSchuldatei.katalogSchularten.hasEintragInZeitraum(schulform, schulform.schulformwert))
					throw new IllegalArgumentException(
							"Die Schulart '" + schulform.schulformwert + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer
									+ " nicht im Katalog enthalten.");
			}
		}
		//prüfen ob Schulform und SchulformASD gesetzt sind.
		if (!isSchulform || !isSchulformASD)
			throw new IllegalArgumentException(
					"Die Schulform ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht gesetzt.");
		//prüfen ob Schlüssel des Katalogeintrags von SchulformASD == Schulform
		final SchuldateiKatalogeintrag eintrag = _managerSchuldatei.katalogSchulformen.getEintragBySchuljahrAndWert(schuljahr, schulformAsdWert);
		if (eintrag == null || !eintrag.schluessel.equals(schulformWert))
			throw new IllegalArgumentException(
					"Die Schulformen Schulform und SchulformASD passen nicht zusammen bei der Organisationseinheit mit der Schulnummer "
							+ grunddaten.schulnummer + ".");
	}


	/**
	 * Validiere die Adressen der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateAdressen() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitAdresse adresse : this._organisationseinheit.adressen) {
			// Prüfe, ob die ID für die Organisationseinheit eindeutig ist und erzeuge den Adress-Manager. Dieser validiert auch die Adresse.
			if (_mapAdressManagerByID.containsKey(adresse.id))
				throw new IllegalArgumentException("Die Addressen bei der Organisationseinheit mit der Schulnummer %d hat Duplikate."
						.formatted(_organisationseinheit.schulnummer));
			_mapAdressManagerByID.put(adresse.id,
					new SchuldateiOrganisationseinheitAdressManager(_managerSchuldatei, this, adresse, this._organisationseinheit.erreichbarkeiten));
		}
	}


	/**
	 * Validiere die Merkmale der Organisationseinheit
	 * Validiert werden die
	 *  - Die Schulnummer muss identisch sein mit der Nummer dieser Organisationseinheit
	 *  - Nummer der Liegenschaft :
	 *       == 0  für alle Liegenschaften immer ok
	 *        > 0  die Liegenschaftsnummer muss in den Adressen existieren
	 *  - Das Merkmal muss im Katalog der Merkmale existieren
	 *  - Das Attribut muss im Katalog der Attribute existieren
	 *  Die Properties Merkmalgruppe, Attributgruppe und Wert sollen laut Thomas Heyn nicht berücksichtigt werden,
	 *  da die Felder nur zur Differenzierung in der Schulaufsicht benötigt werden.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateMerkmale() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitMerkmal merkmal : this._organisationseinheit.merkmal) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (!this._organisationseinheit.schulnummer.equals(merkmal.schulnummer))
				throw new IllegalArgumentException("Die Schulnummer " + merkmal.schulnummer
						+ " bei dem Merkmal mit der ID " + merkmal.id + " passt nicht zu der Schulnummer der Organisationseinheit "
						+ this._organisationseinheit.schulnummer + ".");

			// Prüfe, ob die Liegenschaftsnummer gültig ist
			if ((merkmal.liegenschaft != 0) && (!existsLiegenschaftInAdressen(merkmal.liegenschaft)))
				throw new IllegalArgumentException("Für die Liegenschaftsnummer " + merkmal.liegenschaft
						+ " bei dem Merkmal mit der ID " + merkmal.id
						+ " existiert keine Adresse mit der gleichen Liegenschaftsnummer bei der Organisationseinheit mit der Schulnummer "
						+ this._organisationseinheit.schulnummer + ".");

			// Prüfe, ob die Merkmale im Katalog existieren
			if (!_managerSchuldatei.katalogMerkmale.hasEintragInZeitraum(merkmal, merkmal.merkmal))
				throw new IllegalArgumentException(
						"Das Merkmal " + merkmal.merkmal + " der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(_organisationseinheit.schulnummer));

			// Prüfe, ob die Attribute im Katalog existieren
			if (!_managerSchuldatei.katalogAttribute.hasEintragInZeitraum(merkmal, merkmal.attribut))
				throw new IllegalArgumentException(
						"Das Attribut " + merkmal.attribut + " der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(_organisationseinheit.schulnummer));
		}
	}


	/**
	 * Validiere die Erreichbarkeiten der Organisationseinheit
	 * Erreichbarkeiten referenzieren über die Eigenschaft liegenschaft die entsprechende Adresse über die
	 * gleiche Eigenschaft.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateErreichbarkeiten() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitErreichbarkeit erreichbarkeit : this._organisationseinheit.erreichbarkeiten) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (!this._organisationseinheit.schulnummer.equals(erreichbarkeit.schulnummer))
				throw new IllegalArgumentException("Die Schulnummer " + erreichbarkeit.schulnummer
						+ " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".");

			// prüfe auf adresse mit gleicher liegenschaftsnummer
			if ((erreichbarkeit.liegenschaft != 0) && !existsLiegenschaftInAdressen(erreichbarkeit.liegenschaft))
				throw new IllegalArgumentException("Für die Liegenschaftsnummer " + erreichbarkeit.liegenschaft
						+ " existiert keine Adresse mit der gleichen Liegenschaftsnummer bei der Organisationseinheit mit Schulnummer "
						+ this._organisationseinheit.schulnummer + ".");

			// prüfe auf existierenden codekey
			//TODO workaround solange die Values nicht als String in der WEB-Schuldatei.json
			//final @NotNull String codekey = "0" + erreichbarkeit.codekey;
			if (!_managerSchuldatei.katalogErreichbarkeiten.hasEintragInZeitraum(erreichbarkeit, erreichbarkeit.codekey))
				throw new IllegalArgumentException(
						"Der Typ (codekey) der Erreichbarkeit mit der Id %s in der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(erreichbarkeit.id, erreichbarkeit.schulnummer));

			// prüfe auf existierende kommgruppe
			if (!_managerSchuldatei.katalogKommunikationsgruppen.hasEintragInZeitraum(erreichbarkeit, erreichbarkeit.kommgruppe))
				throw new IllegalArgumentException(
						"Die Kommunikationsgruppe >%s< der Erreichbarkeit mit der Id %s in der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(erreichbarkeit.kommgruppe, erreichbarkeit.id, erreichbarkeit.schulnummer));
		}
	}


	/**
	 * Validiere die Eigenschaften der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateEigenschaften() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitEigenschaft eigenschaft : this._organisationseinheit.oeEigenschaften) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (!this._organisationseinheit.schulnummer.equals(eigenschaft.schulnummer))
				throw new IllegalArgumentException("Die Schulnummer " + eigenschaft.schulnummer
						+ " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".");

			// Prüfe, ob die Eigenschaften der Organisationseinheit im Katalog existieren
			if (!_managerSchuldatei.katalogOergangisationseinheitEigenschaften.hasEintragInZeitraum(eigenschaft, eigenschaft.eigenschaft))
				throw new IllegalArgumentException("Die Eigenschaft " + eigenschaft.eigenschaft
						+ " mit der Id %d in der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(eigenschaft.id, _organisationseinheit.schulnummer));
		}
	}


	/**
	 * Validiere die Gliederungen der Organisationseinheit
	 * (Der Bereich Gliederungen wird in Zukunft wegfallen, da diese Informationen dann unter Merkmale
	 *  eingestellt werden)
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private void validateGliederungen() throws IllegalArgumentException {
		for (final @NotNull SchuldateiOrganisationseinheitGliederung gliederung : this._organisationseinheit.gliederung) {
			// Prüfe, ob die Schulnummer passend zur Organisationseinheit ist
			if (!this._organisationseinheit.schulnummer.equals(gliederung.schulnummer))
				throw new IllegalArgumentException("Die Schulnummer " + gliederung.schulnummer
						+ " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".");

			// Prüfe, ob die Gliederungen der Organisationseinheit im Katalog existieren
			if ((!gliederung.gliederung.isEmpty()) && (!_managerSchuldatei.katalogGliederungen.hasEintragInZeitraum(gliederung, gliederung.gliederung)))
				throw new IllegalArgumentException("Die Gliederung " + gliederung.gliederung
						+ "mit der Id %d in der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(gliederung.id, _organisationseinheit.schulnummer));

			// Prüfe, ob die Förderschwerpunkte der Organisationseinheit im Katalog existieren
			if ((!gliederung.foerderschwerpunkt.isEmpty())
					&& (!_managerSchuldatei.katalogFoerderschwerpunkte.hasEintragInZeitraum(gliederung, gliederung.foerderschwerpunkt)))
				throw new IllegalArgumentException("Der Förderschwerpunkt " + gliederung.foerderschwerpunkt
						+ "mit der Id %d in der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag."
								.formatted(gliederung.id, _organisationseinheit.schulnummer));
		}
	}


	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück.
	 *
	 * @return die Schulnummer
	 */
	public @NotNull String getSchulnummer() {
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
	 * @param	schuljahr	das Schuljahr
	 *
	 * @return die Bezeichnung der Art der Organisationseinheit
	 */
	public String getArtBezeichnung(final int schuljahr) {
		return _managerSchuldatei.katalogOrganisationseinheitarten.getBezeichnung(schuljahr, this._organisationseinheit.oeart);
	}

	/**
	 * Gibt den ersten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public @NotNull String getAmtlicheBezeichnung1() {
		return this._organisationseinheit.amtsbez1;
	}

	/**
	 * Gibt den zweiten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public @NotNull String getAmtlicheBezeichnung2() {
		return this._organisationseinheit.amtsbez2;
	}

	/**
	 * Gibt den dritten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public @NotNull String getAmtlicheBezeichnung3() {
		return this._organisationseinheit.amtsbez3;
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
		final @NotNull List<SchuldateiOrganisationseinheitGrunddaten> grunddaten = new ArrayList<>();
		for (final @NotNull SchuldateiOrganisationseinheitGrunddaten eintrag : this._organisationseinheit.grunddaten)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				grunddaten.add(eintrag);
		if (grunddaten.isEmpty())
			throw new IllegalArgumentException("Es konnten keine Grunddaten für das Schuljahr " + schuljahr + "/" + (schuljahr + 1) + " gefunden werden.");
		// ... und übernehme den Eintrag aus dem Ergebnis, dessen Gültigkeit noch am längsten währt.
		@NotNull SchuldateiOrganisationseinheitGrunddaten eintrag = grunddaten.get(0);
		for (int i = 1; i < grunddaten.size(); i++) {
			final @NotNull SchuldateiOrganisationseinheitGrunddaten other = grunddaten.get(0);
			if (SchuldateiUtils.istFrueher(eintrag.gueltigbis, other.gueltigbis))
				eintrag = other;
		}
		_mapGrunddatenBySchuljahr.put(schuljahr, eintrag);
		// Bestimme nun die Informationen zu der Schulform und speicher diese in den zugehörigen Maps
		final @NotNull String @NotNull [] sf = getSchulformInfo(schuljahr, eintrag);
		_mapSchulformBySchuljahr.put(schuljahr, sf[0]);
		_mapSchulformASDBySchuljahr.put(schuljahr, sf[1]);
		_mapSchulartBySchuljahr.put(schuljahr, sf[2]);
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
	public @NotNull String getRechtsstatus(final int schuljahr) throws IllegalArgumentException {
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
	public @NotNull String getSchultraegernummer(final int schuljahr) throws IllegalArgumentException {
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
		final @NotNull String nummer = this.getGrunddaten(schuljahr).schultraegernummer;
		final SchuldateiOrganisationseinheitManager schultraeger = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schultraeger == null)
			throw new IllegalArgumentException("Der Schulträger " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
					+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
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
	public @NotNull String getArtDerTraegerschaft(final int schuljahr) throws IllegalArgumentException {
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
	public @NotNull String getSchulbetriebsschluessel(final int schuljahr) throws IllegalArgumentException {
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
	public @NotNull String getKapitel(final int schuljahr) throws IllegalArgumentException {
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
	public @NotNull String getObereSchulaufsichtNummer(final int schuljahr) throws IllegalArgumentException {
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
		final @NotNull String nummer = this.getGrunddaten(schuljahr).obereschulaufsicht;
		final SchuldateiOrganisationseinheitManager schulaufsicht = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schulaufsicht == null)
			throw new IllegalArgumentException("Die obere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
					+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
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
	public @NotNull String getUntererSchulaufsichtNummer(final int schuljahr) throws IllegalArgumentException {
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
		final @NotNull String nummer = this.getGrunddaten(schuljahr).untereschulaufsicht;
		final SchuldateiOrganisationseinheitManager schulaufsicht = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schulaufsicht == null)
			throw new IllegalArgumentException("Die untere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
					+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
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
	public @NotNull String getZfsLNummer(final int schuljahr) throws IllegalArgumentException {
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
		final @NotNull String nummer = this.getGrunddaten(schuljahr).zfsl;
		final SchuldateiOrganisationseinheitManager zfsl = _managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (zfsl == null)
			throw new IllegalArgumentException("Das ZfsL " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer "
					+ this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.");
		return zfsl;
	}


	/**
	 * Gibt den Dienststellenschlüssel/Personalbereich der Organisationseinheit zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Dienststellenschlüssel der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull String getDienststellenschluessel(final int schuljahr) throws IllegalArgumentException {
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
		return grunddaten.internatsplaetze;
	}


	/**
	 * Gibt die Schulform der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulform
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull String getSchulform(final int schuljahr) throws IllegalArgumentException {
		if (!_mapSchulformBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		final String result = _mapSchulformBySchuljahr.get(schuljahr);
		if (result == null)
			throw new IllegalArgumentException("Es konnte keine Schulform für die Organisationseinheit mit der Schulnummer " + _organisationseinheit.schulnummer
					+ " in diesem Schuljahr gefunden werden.");
		return result;
	}


	/**
	 * Gibt die SchulformASD der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die SchulformASD
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull String getSchulformASD(final int schuljahr) throws IllegalArgumentException {
		if (!_mapSchulformASDBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		final String result = _mapSchulformASDBySchuljahr.get(schuljahr);
		if (result == null)
			throw new IllegalArgumentException("Es konnte keine SchulformASD für die Organisationseinheit mit der Schulnummer "
					+ _organisationseinheit.schulnummer + " in diesem Schuljahr gefunden werden.");
		return result;
	}


	/**
	 * Gibt die Schulart der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulart
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull String getSchulart(final int schuljahr) throws IllegalArgumentException {
		if (!_mapSchulartBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		final String result = _mapSchulartBySchuljahr.get(schuljahr);
		return (result == null) ? "" : result;
	}


	/**
	 * Bestimmt die Liste der Adress-Manager für das Schuljahr anhand der vorhandenen Adress-Einträge und
	 * erzeugt einen Cache für den schnellen Zugriff auf diese Adressen.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Adress-Manager für die Adressen des Schuljahres
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull List<SchuldateiOrganisationseinheitAdressManager> getAdressManager(final int schuljahr) throws IllegalArgumentException {
		// Prüfe, ob die Anfrage aus dem Cache beantwortet werden kann
		List<SchuldateiOrganisationseinheitAdressManager> listManager = _mapAdressenBySchuljahr.get(schuljahr);
		if (listManager != null)
			return listManager;
		// Wenn nicht, dann bestimme alle Adressen, welche in dem Zeitraum gültig sind ...
		listManager = new ArrayList<>();
		for (final @NotNull SchuldateiOrganisationseinheitAdresse eintrag : this._organisationseinheit.adressen)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listManager.add(_mapAdressManagerByID.get(eintrag.id));
		if (listManager.isEmpty())
			throw new IllegalArgumentException("Es konnten keine Adressen für das Schuljahr " + schuljahr + "/" + (schuljahr + 1) + " gefunden werden.");
		// ... und übernehme diese in den Cache
		_mapAdressenBySchuljahr.put(schuljahr, listManager);
		// ... und bestimme den Eintrag mit dem Hauptstandort aus der Liste, dessen Gültigkeit noch am längsten währt.
		SchuldateiOrganisationseinheitAdressManager managerHauptstandort = null;
		for (final @NotNull SchuldateiOrganisationseinheitAdressManager managerOther : listManager) {
			if ((managerOther.istHauptstandort())
					&& ((managerHauptstandort == null) || SchuldateiUtils.istFrueher(managerHauptstandort.getGueltigBis(), managerOther.getGueltigBis())))
				managerHauptstandort = managerOther;
		}
		if (managerHauptstandort != null)
			_mapHauptstandortBySchuljahr.put(schuljahr, managerHauptstandort);
		// Gebe die Liste der Adress-Manager zurück.
		return listManager;
	}


	/**
	 * Gibt den Adress-Manager für den Hauptstandort der Organisationseinheit in
	 * dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Adress-Manager für den Hauptstandort
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public @NotNull SchuldateiOrganisationseinheitAdressManager getHauptstandort(final int schuljahr) throws IllegalArgumentException {
		if (!_mapHauptstandortBySchuljahr.containsKey(schuljahr))
			this.getAdressManager(schuljahr);
		final SchuldateiOrganisationseinheitAdressManager result = _mapHauptstandortBySchuljahr.get(schuljahr);
		if (result == null)
			throw new IllegalArgumentException("Es konnte kein Hauptstandort für die Organisationseinheit mit der Schulnummer "
					+ _organisationseinheit.schulnummer + " in diesem Schuljahr gefunden werden.");
		return result;
	}


	/**
	 * Gibt die Gliederungen der Organisationseinheit in dem angegebenen Schuljahr zurück
	 *
	 * @param schuljahr		das Schuljahr
	 *
	 * @return die Gliederungen im betreffenden Schuljahr
	 */
	public @NotNull List<SchuldateiOrganisationseinheitGliederung> getGliederungen(final int schuljahr) {
		// Prüfe, ob die Anfrage aus dem Cache beantwortet werden kann
		List<SchuldateiOrganisationseinheitGliederung> listGliederungen = _mapGliederungenBySchuljahr.get(schuljahr);
		if (listGliederungen != null)
			return listGliederungen;
		// Wenn nicht, dann bestimme alle Gliederungen, die in dem Zeitraum gültig sind ...
		listGliederungen = new ArrayList<>();
		for (final @NotNull SchuldateiOrganisationseinheitGliederung eintrag : this._organisationseinheit.gliederung)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listGliederungen.add(eintrag);
		// if (listManager.isEmpty()) : leere Liste ist zulässig
		// ... und übernehme diese in den Cache
		_mapGliederungenBySchuljahr.put(schuljahr, listGliederungen);
		// Gebe die Liste der Adress-Manager zurück.
		return listGliederungen;
	}


	/**
	 * Prüfe ob eine Adresse mit der Liegenschaftsnummer existiert
	 *
	 * @param liegenschaft		die Liegenschaftsnummer
	 *
	 * @return Die Existenz der Liegenschaftsnummer
	 */
	public boolean existsLiegenschaftInAdressen(final int liegenschaft) {
		for (final @NotNull SchuldateiOrganisationseinheitAdressManager adresse : this._mapAdressManagerByID.values()) {
			if (adresse.getLiegenschaftnummer() == liegenschaft)
				return true;
		}
		return false;
	}


	/**
	 * Gibt die Eigenschaften der Organisationseinheit in dem angegebenen Schuljahr zurück
	 *
	 * @param schuljahr		das Schuljahr
	 *
	 * @return die Eigenschaften im betreffenden Schuljahr
	 */
	public @NotNull List<SchuldateiOrganisationseinheitEigenschaft> getEigenschaften(final int schuljahr) {
		// Prüfe, ob die Anfrage aus dem Cache beantwortet werden kann
		List<SchuldateiOrganisationseinheitEigenschaft> listEigenschaften = _mapEigenschaftenBySchuljahr.get(schuljahr);
		if (listEigenschaften != null)
			return listEigenschaften;
		// Wenn nicht, dann bestimme alle Eigenschaften, die in dem Zeitraum gültig sind ...
		listEigenschaften = new ArrayList<>();
		for (final @NotNull SchuldateiOrganisationseinheitEigenschaft eintrag : this._organisationseinheit.oeEigenschaften)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listEigenschaften.add(eintrag);
		// if (listManager.isEmpty()) : leere Liste ist zulässig
		// ... und übernehme diese in den Cache
		_mapEigenschaftenBySchuljahr.put(schuljahr, listEigenschaften);
		// Gebe die Liste der Eigenschaften zurück.
		return listEigenschaften;
	}


	/**
	 * Prüft, ob die Merkmal/Attributs-Kombination der Organisationseinheit in dem angegebenen Schuljahr gesetzt ist.
	 *
	 * @param merkmal       die Merkmals-ID
	 * @param attribut      die Attributs-ID
	 * @param schuljahr		das Schuljahr
	 *
	 * @return boolean, ob die Kombi im betreffenden Schuljahr vorhanden ist
	 */
	public boolean hatMerkmalAttributInSchuljahr(@NotNull final int merkmal, final int attribut, final int schuljahr) {
		// Prüfe, ob die Merkmalsliste für das Schuljahr schon vorliegt
		List<SchuldateiOrganisationseinheitMerkmal> listMerkmale = _mapMerkmaleBySchuljahr.get(schuljahr);
		if (listMerkmale == null) {
			// Merkmale einmalig für das Schuljahr zusammenstellen und cashen ...
			listMerkmale = new ArrayList<>();
			for (final @NotNull SchuldateiOrganisationseinheitMerkmal eintrag : this._organisationseinheit.merkmal)
				if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
					listMerkmale.add(eintrag);
			// if (listManager.isEmpty()) : leere Liste ist zulässig
			// ... und übernehme diese in den Cache
			_mapMerkmaleBySchuljahr.put(schuljahr, listMerkmale);
		}
		// Prüfe, ob das geforderte Merkmal in der Liste ist
		for (final @NotNull SchuldateiOrganisationseinheitMerkmal eintrag : listMerkmale)
			if ((eintrag.merkmal == merkmal) && (eintrag.attribut == attribut))
				return true;
		return false;
	}

	//TODO ggfs. Methoden, die bestimmte Merkmale zurückliefern.
	//hier wären zum Beispiel die Liste der Förderschwerpunkte denkbar
}

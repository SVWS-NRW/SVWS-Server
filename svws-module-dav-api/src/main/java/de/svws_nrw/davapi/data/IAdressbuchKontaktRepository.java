package de.svws_nrw.davapi.data;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.AdressbuchKontakt;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;

/**
 * Interface für ein Repository für Adressbuchkontakte
 *
 */
public interface IAdressbuchKontaktRepository {

	/**
	 * Filter für SchuelerDTOs
	 */
	Predicate<DTOSchueler> SCHUELER_FILTER = s -> !s.Geloescht && (s.Status == SchuelerStatus.AKTIV
			|| s.Status == SchuelerStatus.EXTERN || s.Status == SchuelerStatus.NEUAUFNAHME);

	/**
	 * Ermittelt eine Liste aller Kontakte aus einem Adressbuch mir der angegebenen
	 * Id.
	 *
	 * @param adressbuchId Id des Adressbuchs, dessen Kontakte ermittelt werden
	 *                     sollen.
	 * @param params       QueryParameter zum filtern der Adressbucheinträge auf
	 *                     notwendigt Informationen
	 * @return Liste mit Kontakten des angegebenen Adressbuchs.
	 */
	List<AdressbuchEintrag> getKontakteByAdressbuch(String adressbuchId, CollectionRessourceQueryParameters params);

	/**
	 * statische Methode zum mappen von SchuelerDTOs zu Kontakten
	 *
	 * @param dtoSchueler    das zu mappende DTO
	 * @param telefonnummern die Liste der Telefonnummern zu dem Schueler
	 * @param ort            das Datenbankobjekt für den Wohnort des Schuelers
	 * @param categories     die Liste an Kategorien, die dem Schueler zugeordnet
	 *                       werden sollen
	 * @param schulName      der name der eigenen Schule
	 * @return einen AdressbuchEintrag für den Schueler
	 */
	static AdressbuchEintrag mapDTOSchuelerToKontakt(final DTOSchueler dtoSchueler, final List<Telefonnummer> telefonnummern,
			final DTOOrt ort, @NotNull final Set<String> categories, final String schulName) {
		final AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = createSchuelerId(dtoSchueler.ID);
		k.email = dtoSchueler.Email;
		final List<Telefonnummer> nummern = (telefonnummern == null) ? new ArrayList<>() : telefonnummern;
		addStandardTelefonnummer(dtoSchueler.Fax, nummern, "cell");
		addStandardTelefonnummer(dtoSchueler.Telefon, nummern, "voice");
		k.telefonnummern.addAll(nummern);

		k.hausnummer = dtoSchueler.HausNr;
		k.hausnummerZusatz = dtoSchueler.HausNrZusatz;
		k.nachname = dtoSchueler.Nachname;

		applyOrtToKontakt(k, ort);
		k.kategorien.addAll(categories);
		k.strassenname = dtoSchueler.Strassenname;
		k.vorname = dtoSchueler.Vorname;
		k.rolle = "Schüler";
		k.organisation = schulName;
		return k;
	}

	/**
	 * Prüft, ob eine Liste mit Telefonnummern eine gegebene Nummer bereits enthält.
	 * Falls nicht, wird diese gegebene Nummer der Liste hinzugefügt
	 *
	 * @param standardTelefonnummer die gegebene Telefonnummer
	 * @param telefonnummern        eine vorhandene Liste mit Telefonnummern
	 * @param type                  der Typ der hinzuzufügenden Telefonnummer
	 */
	static void addStandardTelefonnummer(final String standardTelefonnummer, final List<Telefonnummer> telefonnummern,
			final String type) {
		// kommentiert als Fax oder Mobilnummer, laut Hr Bachran i.d.R. Mobilnummer
		if (standardTelefonnummer == null || type == null)
			return;
		final boolean duplicateNumber = telefonnummern.stream()
				.anyMatch(t -> t.number.replace("\\D+", "").equals(standardTelefonnummer.replace("\\D+", "")));
		if (!duplicateNumber) {
			final Telefonnummer tel = new Telefonnummer();
			tel.number = standardTelefonnummer;
			tel.type = type;
			telefonnummern.add(tel);
		}
	}

	/**
	 * Sucht anhand einer gegebenen Liste von {@link DTOOrt#ID} die {@link DTOOrt}
	 * und gibt eine Map auf die jeweilige ID wieder
	 *
	 * @param ortIds Ids der Orte
	 * @param conn   die Datenbankverbindung
	 * @return die gesuchten Orte gemappt auf ihre ID
	 */
	static Map<Long, DTOOrt> queryOrteByOrtIds(final Set<Long> ortIds, final DBEntityManager conn) {
		final List<DTOOrt> dtoOrtQueryResult = conn.queryNamed("DTOOrt.id.multiple", ortIds, DTOOrt.class);
		return dtoOrtQueryResult.stream().collect(Collectors.toMap(o -> o.ID, Function.identity()));
	}

	/**
	 * Fügt einem Kontakt die Ortsdaten hinzu
	 *
	 * @param k   der Kontakt
	 * @param ort der zuzufügende Ort
	 */
	static void applyOrtToKontakt(final AdressbuchKontakt k, final DTOOrt ort) {
		if (ort != null) {
			k.ort = ort.Bezeichnung;
			k.plz = ort.PLZ;
		}
	}

	/**
	 * Erstellt eine SchuelerID für den Adressbuchkontakt
	 *
	 * @param schuelerID die ID des Schuelers
	 * @return eine Stringrepräsentation der SchuelerID
	 */
	static String createSchuelerId(final Long schuelerID) {
		return "Schueler_" + schuelerID;
	}

	/**
	 * Erstellt eine ErzieherID für den Adressbuchkontakt
	 *
	 * @param erzieherID die ID des Erziehers
	 * @return eine Stringrepräsentation der ErzieherID
	 */
	static String createErzieherId(final Long erzieherID) {
		return "Erzieher_" + erzieherID;
	}

	/**
	 * Erstellt eine LehrerID für den Adressbuchkontakt
	 *
	 * @param lehrerID die ID des Lehrer
	 * @return eine Stringrepräsentation der LehrerID
	 */
	static String createLehrerId(final Long lehrerID) {
		return "Lehrer_" + lehrerID;
	}

	/**
	 * Sucht den Namen der eigenen Schule mit gegebener Datenbankverbindung
	 *
	 * @param conn die Datenbankverbindung
	 * @return der Name der eigenen Schule
	 */
	static String getSchulname(final DBEntityManager conn) {
		return conn.queryNamed("DTOEigeneSchule.all", DTOEigeneSchule.class).getResultList().get(0).Bezeichnung1;
	}
}

package de.nrw.schule.svws.davapi.data;

import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchKontakt;
import de.nrw.schule.svws.core.data.adressbuch.Telefonnummer;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrt;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
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
	static final Predicate<DTOSchueler> SCHUELER_FILTER = s -> !s.Geloescht && (s.Status == SchuelerStatus.AKTIV
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
	static AdressbuchEintrag mapDTOSchuelerToKontakt(DTOSchueler dtoSchueler, List<Telefonnummer> telefonnummern,
			DTOOrt ort, @NotNull Set<String> categories, String schulName) {
		AdressbuchKontakt k = new AdressbuchKontakt();
		k.id = createSchuelerId(dtoSchueler.ID);
		k.email = dtoSchueler.Email;
		if (telefonnummern == null) {
			telefonnummern = new Vector<>();
		}
		addStandardTelefonnummer(dtoSchueler.Fax, telefonnummern, "cell");
		addStandardTelefonnummer(dtoSchueler.Telefon, telefonnummern, "voice");
		k.telefonnummern.addAll(telefonnummern);

		k.hausnummer = dtoSchueler.HausNr;
		k.hausnummerZusatz = dtoSchueler.HausNrZusatz;
		k.nachname = dtoSchueler.Nachname;

		applyOrtToKontakt(k, ort);
		k.kategorien.addAll(categories);
		k.strassenname = dtoSchueler.Strassenname;
		k.vorname = dtoSchueler.Vorname;
		k.zusatzNachname = dtoSchueler.ZusatzNachname;
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
	static void addStandardTelefonnummer(String standardTelefonnummer, List<Telefonnummer> telefonnummern,
			String type) {
		// kommentiert als Fax oder Mobilnummer, laut Hr Bachran i.d.R. Mobilnummer
		if (standardTelefonnummer == null || type == null)
			return;
		boolean duplicateNumber = telefonnummern.stream()
				.anyMatch(t -> t.number.replace("\\D+", "").equals(standardTelefonnummer.replace("\\D+", "")));
		if (!duplicateNumber) {
			Telefonnummer tel = new Telefonnummer();
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
	static Map<Long, DTOOrt> queryOrteByOrtIds(Set<Long> ortIds, DBEntityManager conn) {
		List<DTOOrt> dtoOrtQueryResult = conn.queryNamed("DTOOrt.id.multiple", ortIds, DTOOrt.class);
		return dtoOrtQueryResult.stream().collect(Collectors.toMap(o -> o.ID, Function.identity()));
	}

	/**
	 * Fügt einem Kontakt die Ortsdaten hinzu
	 *
	 * @param k   der Kontakt
	 * @param ort der zuzufügende Ort
	 */
	static void applyOrtToKontakt(AdressbuchKontakt k, DTOOrt ort) {
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
	static String createSchuelerId(Long schuelerID) {
		return "Schueler_" + schuelerID;
	}

	/**
	 * Erstellt eine ErzieherID für den Adressbuchkontakt
	 *
	 * @param erzieherID die ID des Erziehers
	 * @return eine Stringrepräsentation der ErzieherID
	 */
	static String createErzieherId(Long erzieherID) {
		return "Erzieher_" + erzieherID;
	}

	/**
	 * Erstellt eine LehrerID für den Adressbuchkontakt
	 *
	 * @param lehrerID die ID des Lehrer
	 * @return eine Stringrepräsentation der LehrerID
	 */
	static String createLehrerId(Long lehrerID) {
		return "Lehrer_" + lehrerID;
	}

	/**
	 * Sucht den Namen der eigenen Schule mit gegebener Datenbankverbindung
	 *
	 * @param conn die Datenbankverbindung
	 * @return der Name der eigenen Schule
	 */
	static String getSchulname(DBEntityManager conn) {
		return conn.queryNamed("DTOEigeneSchule.all", DTOEigeneSchule.class).getResultList().get(0).Bezeichnung1;
	}
}

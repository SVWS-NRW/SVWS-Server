package de.svws_nrw.davapi.data.carddav;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.adressbuch.Telefonnummer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Eine abstrakte Basisklasse für den Zugriff auf Daten für ein Adressbuch.
 */
public abstract class DataManagerCardDav {

	/** Die Datenbank-Verbindung */
	protected final DBEntityManager conn;

	/** Die ID des Schuljahresabschnittes bei generierten Adressbüchern für welchen die Daten ermittelt werden sollen */
	protected final Schuljahresabschnitt schuljahresabschnitt;

	/** Die String-Darstellung für den Schuljahresabschnitt, welche auch in den Kategorien genutzt wird. */
	protected final String strSchuljahresabschnitt;


	/**
	 * Erstellt einen neuen Daten-Manager für den Zugriff auf ein Adressbuch
	 *
	 * @param conn                      die Verbindung für den Datenbank-Zugriff
	 * @param idSchuljahresabschnitt    die ID des Schuljahresabschnittes, auf welchen sich die Anfrage bezieht
	 *
	 * @throws ApiOperationException
	 */
	protected DataManagerCardDav(final DBEntityManager conn, final long idSchuljahresabschnitt) throws ApiOperationException {
		this.conn = conn;
		this.schuljahresabschnitt = conn.getUser().schuleGetAbschnittById(idSchuljahresabschnitt);
		if (this.schuljahresabschnitt == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Schuljahresabschnittes ist ungültig.");
		this.strSchuljahresabschnitt = schuljahresabschnitt.schuljahr + "/" + (schuljahresabschnitt.schuljahr + 1) + "." + schuljahresabschnitt.abschnitt;
	}


	/**
	 * Erstellt einen Kontakt ohne Payload, d.h. nur mit der Kontakt-ID der zugrundliegenden Daten.
	 *
	 * @param id   die ID der zugrundliegenden Daten (z.B. Schüler-ID)
	 *
	 * @return der Adressbuch-Eintrag ohne Payload
	 */
	public AdressbuchEintrag mapEmptyContact(final long id) {
		final AdressbuchEintrag a = new AdressbuchEintrag();
		a.id = getKontaktId(id);
		return a;
	}


	/**
	 * Gibt die ID für den Kontakt auf Grundlage der übergebenen ID zurück.
	 *
	 * @param id   die ID der zugrundeliegenden Daten (z.B. Schüler-ID 375)
	 *
	 * @return die ID für den Kontakt, z.B. "Schueler_2024_1_375"
	 */
	public abstract String getKontaktId(long id);


	/**
	 * Gibt die Liste der Kontakte aus dem Adressbuch mit der übergebenen ID zurück.
	 *
	 * @param idBook        die ID des Adressbuchs
	 * @param withPayload   gibt an, ob die eigentlichen Kontaktdaten bei der Generierung der Kontakte mit erstellt werden
	 *
	 * @return die Liste mit Kontakten des angegebenen Adressbuchs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public abstract List<AdressbuchEintrag> getKontakte(String idBook, boolean withPayload) throws ApiOperationException;


	/**
	 * Erstellt für die übergebenen IDs von Orten eine Map auf deren DTOs.
	 *
	 * @param ortIds   die IDs der Orte
	 *
	 * @return die Map
	 */
	protected Map<Long, DTOOrt> queryMapOrte(final Set<Long> ortIds) {
		return conn.queryByKeyList(DTOOrt.class, ortIds).stream().collect(Collectors.toMap(o -> o.ID, Function.identity()));
	}


	/**
	 * Eine Hilfsmethode, die prüft, ob eine Liste mit Telefonnummern eine gegebene Nummer bereits enthält.
	 * Ist dies nicht der Fall, so wird diese Nummer der Liste der Telefonnummern hinzugefügt.
	 *
	 * @param telefonnummer    die Telefonnummer für das Hinzufügen
	 * @param telefonnummern   eine vorhandene Liste mit Telefonnummern
	 * @param type             der Typ der Telefonnummer, z.B. cell or voice
	 */
	protected void addStandardTelefonnummer(final String telefonnummer, final List<Telefonnummer> telefonnummern, final String type) {
		if ((telefonnummer == null) || (type == null))
			return;
		final boolean isDuplicate = telefonnummern.stream().anyMatch(t -> t.number.replace("\\D+", "").equals(telefonnummer.replace("\\D+", "")));
		if (isDuplicate)
			return;
		final Telefonnummer tel = new Telefonnummer();
		tel.number = telefonnummer;
		tel.type = type;
		telefonnummern.add(tel);
	}


	/**
	 * Gibt den Namen der Schule zurück.
	 *
	 * @return der Name der Schule
	 */
	protected String getSchulname() {
		return conn.getUser().schuleGetStammdaten().bezeichnung1;
	}

}

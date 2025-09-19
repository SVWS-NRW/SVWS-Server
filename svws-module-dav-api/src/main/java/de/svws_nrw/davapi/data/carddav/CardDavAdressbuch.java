package de.svws_nrw.davapi.data.carddav;

import java.util.List;

import de.svws_nrw.core.data.adressbuch.Adressbuch;
import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;


/**
 * Die Klasse verwaltet die Zugriffe auf Adressbücher und steuert, wie auf die Daten der zugehörigen Kontakte
 * der Adressbücher zugegriffen wird. Die einzelnen Kontakte werden dabei mit Kategorien versehen,
 * so dass sie in einem geeigneten CardDAV-Client ggf. danach sortierbar und durchsuchbar sind.
 */
public final class CardDavAdressbuch {

	/** Die Datenbank-Verbindung */
	private final DBEntityManager conn;


	/**
	 * Erstellt eine Instanz zum Zugriff auf die Adressbücher mithilfe der angegebenen Datenbankverbindung
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public CardDavAdressbuch(final DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Stellt ein Adressbuch anhand der übergebenen Adressbuch-ID aus der Datenbank zusammen.
	 *
	 * @param idBook         die ID des Adressbuches
	 * @param withContacts   gibt an, ob die Kontaktdaten im Adressbuch enthalten sein sollen
	 * @param withPayload    gibt an, ob die eigentlichen Kontaktdaten bei der Generierung der Kontakte mit erstellt werden
	 *
	 * @return das Adressbuch oder null, falls kein Adressbuch mit der übergebenen ID gefunden wurde.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Adressbuch getAdressbuchById(final String idBook, final boolean withContacts, final boolean withPayload) throws ApiOperationException {
		final Adressbuch book = CardDavAdressbuchTyp.getAdressbuchByID(idBook);
		if (book == null)
			return null;
		if (!withContacts)
			book.adressbuchEintraege.addAll(getKontakteByAdressbuch(idBook, withPayload));
		return book;
	}


	/**
	 * Gibt die Liste der für den angemeldeten Benutzer verfügbaren Adressbücher zurück.
	 *
	 * @return die Liste der Adressbücher
	 */
	@SuppressWarnings("static-method")
	public List<Adressbuch> getAvailableAdressbuecher() {
		// TODO Hier muss später noch gefiltert werden, so dass anhand der Berechtigungen des Connection-Users die Adressbücher zusammengestellt werden
		return CardDavAdressbuchTyp.getAdressbuecher();
	}


	/**
	 * Stellt die Liste aller Kontakte aus dem Adressbuch mit der angegebenen ID zusammen.
	 *
	 * @param idBook        die ID des Adressbuches
	 * @param withPayload   gibt an, ob die eigentlichen Kontaktdaten bei der Generierung der Kontakte mit erstellt werden
	 *
	 * @return die Liste mit den Kontakten des Adressbuches.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private List<AdressbuchEintrag> getKontakteByAdressbuch(final @NotNull String idBook, final boolean withPayload)
			throws ApiOperationException {
		// Bestimme zunächst den Typ und die ID des Schuljahresabschnittes für das Einlesen der Kontakte
		final CardDavAdressbuchTyp typ = CardDavAdressbuchTyp.valueOf(idBook.toUpperCase());
		final long idSchuljahresabschnitt = conn.getUser().schuleGetSchuljahresabschnitt().id;

		// Lese die Kontakte anhand des Typs und des Schuljahresabschnittes aus der DB ein
		final List<AdressbuchEintrag> result = switch (typ) {
			case SCHUELER -> new DataCardDavSchueler(conn, idSchuljahresabschnitt).getKontakte(idBook, withPayload);
			case ERZIEHER -> new DataCardDavErzieher(conn, idSchuljahresabschnitt).getKontakte(idBook, withPayload);
			case LEHRER -> new DataCardDavLehrer(conn, idSchuljahresabschnitt).getKontakte(idBook, withPayload);
			default -> throw new IllegalArgumentException("Unexpected value: " + idBook);
		};

		// Setze bei allen Kontakten die ID des Adressbuches
		result.forEach(k -> k.adressbuchId = idBook);
		return result;
	}

}

package de.svws_nrw.davapi.data;

import java.util.Optional;

import de.svws_nrw.core.data.kalender.KalenderEintrag;
import de.svws_nrw.davapi.data.repos.dav.DavException;

/**
 * Interface Definition für Repositories von Kalendereinträgen.
 * 
 */
public interface IKalenderEintragRepository {

	/**
	 * Ermittelt eine Liste aller Einträge aus einem Kalender mir der angegebenen
	 * Id.
	 *
	 * @param kalenderId         Id des Kalenders
	 * @param kalenderEintragUID Id des Kalendereintrags
	 * @param params             Parameter zum filtern der Datenmenge, die abgefragt
	 *                           wird
	 * @return Optional des Kalendereintrags
	 */
	Optional<KalenderEintrag> getKalenderEintragByKalenderAndUID(String kalenderId, String kalenderEintragUID,
			CollectionRessourceQueryParameters params);

	/**
	 * Speichern einen Kalendereintrag. Existiert dieser bereits, erfährt der
	 * Eintrag ein Update, anderfalls wird der Eintrag neu angelegt.
	 *
	 * @param kalenderEintrag Kalendereintrag
	 * @return Gespeicherter Kalendereintrag
	 * @throws DavException bei fehlenden Rechten oder Fehlern beim Schreiben in die
	 *                      Datenbank
	 */
	KalenderEintrag saveKalenderEintrag(KalenderEintrag kalenderEintrag) throws DavException;
}

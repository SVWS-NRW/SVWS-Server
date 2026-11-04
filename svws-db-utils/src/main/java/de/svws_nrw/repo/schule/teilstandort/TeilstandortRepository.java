package de.svws_nrw.repo.schule.teilstandort;

import java.util.Collection;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.repo.RepositoryBase;

/**
 * Das Interface für ein Repository zum Zugriff auf die Teilstandorttabelle der SVWS-Datenbank
 */
public interface TeilstandortRepository extends RepositoryBase<DTOTeilstandorte, String> {

	/**
	 * Liefert die Adressmerkmale der Teilstandorte, die in anderen Tabellen referenziert werden.
	 *
	 * @param adrMerkmale   die zu prüfenden Adressmerkmale
	 *
	 * @return die Menge der referenzierten Adressmerkmale
	 */
	Set<String> getReferencedIds(Collection<String> adrMerkmale);

	/**
	 * Prüft, ob ein Teilstandort mit dem angegebenen Adressmerkmal bereits in der Datenbank existiert.
	 *
	 * @param adrMerkmal   das zu prüfende Adressmerkmal
	 *
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(String adrMerkmal);
}

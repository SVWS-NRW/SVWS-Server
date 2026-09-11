package de.svws_nrw.repo.uv;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import de.svws_nrw.repo.uv.faecher.UvFachRepositoryImpl;
import de.svws_nrw.repo.uv.lehrer.UvLehrerAnrechnungsstundenRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerAnrechnungsstundenRepositoryImpl;
import de.svws_nrw.repo.uv.lehrer.UvLehrerPflichtstundensollRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerPflichtstundensollRepositoryImpl;
import de.svws_nrw.repo.uv.lehrer.UvLehrerRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerRepositoryImpl;
import de.svws_nrw.repo.uv.lehrer.UvLehrerUnterrichtsfachRepository;
import de.svws_nrw.repo.uv.lehrer.UvLehrerUnterrichtsfachRepositoryImpl;
import de.svws_nrw.repo.uv.raeume.UvRaumRepository;
import de.svws_nrw.repo.uv.raeume.UvRaumRepositoryImpl;
import de.svws_nrw.repo.uv.raeume.UvRaumgruppeRepository;
import de.svws_nrw.repo.uv.raeume.UvRaumgruppeRepositoryImpl;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelFachRepository;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelFachRepositoryImpl;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelRepository;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelRepositoryImpl;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterEintragRepository;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterEintragRepositoryImpl;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterRepository;
import de.svws_nrw.repo.uv.zeitraster.UvZeitrasterRepositoryImpl;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class UvGrunddatenRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static UvGrunddatenRepositoryFactory getNewInstance() {
		return new UvGrunddatenRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrer}.
	 *
	 * @return das Repository-Objekt
	 */
	public UvLehrerRepository getUvLehrerRepository() {
		return getOrCreate(UvLehrerRepository.class, () -> new UvLehrerRepositoryImpl(conn));
	}

	/**
	 * Retrieves an instance of {@link UvLehrerPflichtstundensollRepository}, which provides
	 * access to data related to mandatory teacher hours in the database.
	 *
	 * @return an instance of {@link UvLehrerPflichtstundensollRepository}, enabling interaction
	 *         with teacher mandatory hours data.
	 */
	public UvLehrerPflichtstundensollRepository getUvLehrerPflichtstundensollRepository() {
		return getOrCreate(UvLehrerPflichtstundensollRepository.class, () -> new UvLehrerPflichtstundensollRepositoryImpl(conn));
	}

	/**
	 * Returns the repository instance for accessing teacher compensation hours data.
	 *
	 * @return an instance of {@link UvLehrerAnrechnungsstundenRepository},
	 *         providing methods to interact with the teacher compensation hours data in the database.
	 */
	public UvLehrerAnrechnungsstundenRepository getUvLehrerAnrechnungsstundenRepository() {
		return getOrCreate(UvLehrerAnrechnungsstundenRepository.class, () -> new UvLehrerAnrechnungsstundenRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvLehrerUnterrichtsfachRepository}, das den Zugriff
	 * auf die Unterrichtsfächer von UV-Lehrkräften (Tabelle UV_LehrerUnterrichtsfaecher) ermöglicht.
	 *
	 * @return eine Instanz des {@link UvLehrerUnterrichtsfachRepository}
	 */
	public UvLehrerUnterrichtsfachRepository getUvLehrerUnterrichtsfaecherRepository() {
		return getOrCreate(UvLehrerUnterrichtsfachRepository.class, () -> new UvLehrerUnterrichtsfachRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvFachRepository}, das den Zugriff
	 * auf die UV-Fächer (Tabelle UV_Faecher) ermöglicht.
	 *
	 * @return eine Instanz des {@link UvFachRepository}
	 */
	public UvFachRepository getUvFachRepository() {
		return getOrCreate(UvFachRepository.class, () -> new UvFachRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvRaumRepository}.
	 *
	 * @return eine Instanz des {@link UvRaumRepository}
	 */
	public UvRaumRepository getUvRaumRepository() {
		return getOrCreate(UvRaumRepository.class, () -> new UvRaumRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvRaumgruppeRepository}.
	 *
	 * @return eine Instanz des {@link UvRaumgruppeRepository}
	 */
	public UvRaumgruppeRepository getUvRaumgruppeRepository() {
		return getOrCreate(UvRaumgruppeRepository.class, () -> new UvRaumgruppeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvStundentafelRepository}.
	 *
	 * @return eine Instanz des {@link UvStundentafelRepository}
	 */
	public UvStundentafelRepository getUvStundentafelRepository() {
		return getOrCreate(UvStundentafelRepository.class, () -> new UvStundentafelRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvStundentafelFachRepository}.
	 *
	 * @return eine Instanz des {@link UvStundentafelFachRepository}
	 */
	public UvStundentafelFachRepository getUvStundentafelFachRepository() {
		return getOrCreate(UvStundentafelFachRepository.class, () -> new UvStundentafelFachRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvZeitrasterRepository}.
	 *
	 * @return eine Instanz des {@link UvZeitrasterRepository}
	 */
	public UvZeitrasterRepository getUvZeitrasterRepository() {
		return getOrCreate(UvZeitrasterRepository.class, () -> new UvZeitrasterRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvZeitrasterEintragRepository}.
	 *
	 * @return eine Instanz des {@link UvZeitrasterEintragRepository}
	 */
	public UvZeitrasterEintragRepository getUvZeitrasterEintragRepository() {
		return getOrCreate(UvZeitrasterEintragRepository.class, () -> new UvZeitrasterEintragRepositoryImpl(conn));
	}

}

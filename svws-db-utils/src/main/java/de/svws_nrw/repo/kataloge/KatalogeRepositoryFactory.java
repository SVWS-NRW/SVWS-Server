package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.faecher.FachRepository;
import de.svws_nrw.repo.faecher.FachRepositoryImpl;
import de.svws_nrw.repo.jahrgaenge.JahrgaengeRepository;
import de.svws_nrw.repo.jahrgaenge.JahrgaengeRepositoryImpl;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class KatalogeRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static KatalogeRepositoryFactory getNewInstance() {
		return new KatalogeRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOFach}.
	 *
	 * @return das Repository-Objekt
	 */
	public FachRepository getFachRepository() {
		return new FachRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOJahrgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public JahrgaengeRepository getJahrgaengeRepository() {
		return new JahrgaengeRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOFoerderschwerpunkt}.
	 *
	 * @return das Repository-Objekt
	 */
	public FoerderschwerpunkteRepository getFoerderschwerpunkteRepository() {
		return new FoerderschwerpunkteRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOOrt}.
	 *
	 * @return das Repository-Objekt
	 */
	public OrteRepository getOrteRepository() {
		return new OrteRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOKonfession}.
	 *
	 * @return das Repository-Objekt
	 */
	public ReligionRepository getReligionRepository() {
		return new ReligionRepositoryImpl(conn);
	}

}

package de.svws_nrw.repo.kataloge;

import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelnJahrgaenge;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
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

	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenKonfigurationRepository getAnkreuzkompetenzenKonfigurationRepository() {
		return new AnkreuzkompetenzenKonfigurationRepositoryImpl(conn);
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzfloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenRepository getAnkreuzkompetenzenRepository() {
		return new AnkreuzkompetenzenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzkompetenzJahrgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenJahrgaengeRepository getAnkreuzkompetenzenJahrgaengeRepository() {
		return new AnkreuzkompetenzenJahrgaengeRepositoryImpl(conn);
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelRepository getFloskelRepository() {
		return new FloskelRepositoryImpl(conn);
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskelgruppen}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelgruppenRepository getFloskelgruppenRepository() {
		return new FloskelgruppenRepositoryImpl(conn);
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskelnJahrgaenge}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelJahrgaengeRepository getFloskelJahrgaengeRepository() {
		return new FloskelJahrgaengeRepositoryImpl(conn);
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOTeilleistungsarten}.
	 *
	 * @return das Repository-Objekt
	 */
	public TeilleistungsartenRepository getTeilleistungsartenRepository() {
		return new TeilleistungsartenRepositoryImpl(conn);
	}

}

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
		return getOrCreate(FachRepository.class, () -> new FachRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOJahrgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public JahrgaengeRepository getJahrgaengeRepository() {
		return getOrCreate(JahrgaengeRepository.class, () -> new JahrgaengeRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOFoerderschwerpunkt}.
	 *
	 * @return das Repository-Objekt
	 */
	public FoerderschwerpunkteRepository getFoerderschwerpunkteRepository() {
		return getOrCreate(FoerderschwerpunkteRepository.class, () -> new FoerderschwerpunkteRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOOrt}.
	 *
	 * @return das Repository-Objekt
	 */
	public OrteRepository getOrteRepository() {
		return getOrCreate(OrteRepository.class, () -> new OrteRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOKonfession}.
	 *
	 * @return das Repository-Objekt
	 */
	public ReligionRepository getReligionRepository() {
		return getOrCreate(ReligionRepository.class, () -> new ReligionRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenKonfigurationRepository getAnkreuzkompetenzenKonfigurationRepository() {
		return getOrCreate(AnkreuzkompetenzenKonfigurationRepository.class, () -> new AnkreuzkompetenzenKonfigurationRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzfloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenRepository getAnkreuzkompetenzenRepository() {
		return getOrCreate(AnkreuzkompetenzenRepository.class, () -> new AnkreuzkompetenzenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzkompetenzJahrgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzenJahrgaengeRepository getAnkreuzkompetenzenJahrgaengeRepository() {
		return getOrCreate(AnkreuzkompetenzenJahrgaengeRepository.class, () -> new AnkreuzkompetenzenJahrgaengeRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelRepository getFloskelRepository() {
		return getOrCreate(FloskelRepository.class, () -> new FloskelRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskelgruppen}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelgruppenRepository getFloskelgruppenRepository() {
		return getOrCreate(FloskelgruppenRepository.class, () -> new FloskelgruppenRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskelnJahrgaenge}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelJahrgaengeRepository getFloskelJahrgaengeRepository() {
		return getOrCreate(FloskelJahrgaengeRepository.class, () -> new FloskelJahrgaengeRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOTeilleistungsarten}.
	 *
	 * @return das Repository-Objekt
	 */
	public TeilleistungsartenRepository getTeilleistungsartenRepository() {
		return getOrCreate(TeilleistungsartenRepository.class, () -> new TeilleistungsartenRepositoryImpl(conn));
	}

}

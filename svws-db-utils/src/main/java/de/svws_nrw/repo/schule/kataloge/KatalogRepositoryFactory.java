package de.svws_nrw.repo.schule.kataloge;

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
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepository;
import de.svws_nrw.repo.schule.kataloge.merkmal.MerkmalRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelJahrgangRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.floskelgruppe.FloskelgruppeRepository;
import de.svws_nrw.repo.schule.kataloge.floskelgruppe.FloskelgruppeRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzKonfigurationRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzKonfigurationRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepositoryImpl;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepositoryImpl;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class KatalogRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static KatalogRepositoryFactory getNewInstance() {
		return new KatalogRepositoryFactory();
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
	public JahrgangRepository getJahrgangRepository() {
		return getOrCreate(JahrgangRepository.class, () -> new JahrgangRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOFoerderschwerpunkt}.
	 *
	 * @return das Repository-Objekt
	 */
	public FoerderschwerpunktRepository getFoerderschwerpunktRepository() {
		return getOrCreate(FoerderschwerpunktRepository.class, () -> new FoerderschwerpunktRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOOrt}.
	 *
	 * @return das Repository-Objekt
	 */
	public OrtRepository getOrtRepository() {
		return getOrCreate(OrtRepository.class, () -> new OrtRepositoryImpl(conn));
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
	public AnkreuzkompetenzKonfigurationRepository getAnkreuzkompetenzKonfigurationRepository() {
		return getOrCreate(AnkreuzkompetenzKonfigurationRepository.class, () -> new AnkreuzkompetenzKonfigurationRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzfloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzRepository getAnkreuzkompetenzRepository() {
		return getOrCreate(AnkreuzkompetenzRepository.class, () -> new AnkreuzkompetenzRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOAnkreuzkompetenzJahrgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public AnkreuzkompetenzJahrgangRepository getAnkreuzkompetenzJahrgangRepository() {
		return getOrCreate(AnkreuzkompetenzJahrgangRepository.class, () -> new AnkreuzkompetenzJahrgangRepositoryImpl(conn));
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
	public FloskelgruppeRepository getFloskelgruppeRepository() {
		return getOrCreate(FloskelgruppeRepository.class, () -> new FloskelgruppeRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOFloskelnJahrgaenge}.
	 *
	 * @return das Repository-Objekt
	 */
	public FloskelJahrgangRepository getFloskelJahrgangRepository() {
		return getOrCreate(FloskelJahrgangRepository.class, () -> new FloskelJahrgangRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOTeilleistungsarten}.
	 *
	 * @return das Repository-Objekt
	 */
	public TeilleistungsartRepository getTeilleistungsartRepository() {
		return getOrCreate(TeilleistungsartRepository.class, () -> new TeilleistungsartRepositoryImpl(conn));
	}

	/**
	 * @return {@link FachklasseRepository}
	 */
	public FachklasseRepository getFachklasseRepository() {
		return this.getOrCreate(FachklasseRepository.class, () -> new FachklasseRepositoryImpl(this.conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOMerkmale}.
	 *
	 * @return das Repository-Objekt
	 */
	public MerkmalRepository getMerkmalRepository() {
		return this.getOrCreate(MerkmalRepository.class, () -> new MerkmalRepositoryImpl(this.conn));
	}

	/**
	 * @return {@link SchuleRepository}
	 */
	public SchuleRepository getSchulenRepository() {
		return this.getOrCreate(SchuleRepository.class, () -> new SchuleRepositoryImpl(conn));
	}


}

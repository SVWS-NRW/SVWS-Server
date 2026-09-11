package de.svws_nrw.repo.uv;

import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.uv.klassen.UvKlassenLehrerRepository;
import de.svws_nrw.repo.uv.klassen.UvKlassenLehrerRepositoryImpl;
import de.svws_nrw.repo.uv.kurse.UvKursRepository;
import de.svws_nrw.repo.uv.kurse.UvKursRepositoryImpl;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenLehrerRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenLehrerRepositoryImpl;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenSchieneRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppenSchieneRepositoryImpl;
import de.svws_nrw.repo.uv.klassen.UvKlasseRepository;
import de.svws_nrw.repo.uv.klassen.UvKlasseRepositoryImpl;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepositoryImpl;
import de.svws_nrw.repo.uv.lehrer.UvPlanungsabschnittLehrerRepository;
import de.svws_nrw.repo.uv.lehrer.UvPlanungsabschnittLehrerRepositoryImpl;
import de.svws_nrw.repo.uv.planungsabschnitte.UvPlanungsabschnittRepository;
import de.svws_nrw.repo.uv.planungsabschnitte.UvPlanungsabschnittRepositoryImpl;
import de.svws_nrw.repo.uv.schueler.UvPlanungsabschnittSchuelerRepository;
import de.svws_nrw.repo.uv.schueler.UvPlanungsabschnittSchuelerRepositoryImpl;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterConstraintJahrgangRepositoryImpl;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterRepository;
import de.svws_nrw.repo.uv.zeitraster.UvPlanungsabschnittZeitrasterRepositoryImpl;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintJahrgangRepositoryImpl;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintSchuelergruppeRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeConstraintSchuelergruppeRepositoryImpl;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeRepositoryImpl;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeSchuelerRepository;
import de.svws_nrw.repo.uv.schueler.UvSchuelergruppeSchuelerRepositoryImpl;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRepository;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRepositoryImpl;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtLerngruppenlehrerRepository;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtLerngruppenlehrerRepositoryImpl;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRaumRepository;
import de.svws_nrw.repo.uv.unterrichte.UvUnterrichtRaumRepositoryImpl;
import de.svws_nrw.repo.uv.schienen.UvSchieneConstraintJahrgangRepository;
import de.svws_nrw.repo.uv.schienen.UvSchieneConstraintJahrgangRepositoryImpl;
import de.svws_nrw.repo.uv.schienen.UvSchieneRepository;
import de.svws_nrw.repo.uv.schienen.UvSchieneRepositoryImpl;

/**
 * Eine Factory zum Erstellen von Repositories für die UV-Planungsabschnitte und zugehörige Zuordnungen.
 */
public final class UvPlanungsabschnittRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz.
	 *
	 * @return die neue Factory
	 */
	public static UvPlanungsabschnittRepositoryFactory getNewInstance() {
		return new UvPlanungsabschnittRepositoryFactory();
	}

	/**
	 * Liefert eine Instanz des {@link UvPlanungsabschnittRepository}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittRepository}
	 */
	public UvPlanungsabschnittRepository getUvPlanungsabschnittRepository() {
		return getOrCreate(UvPlanungsabschnittRepository.class, () -> new UvPlanungsabschnittRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvPlanungsabschnittLehrerRepository}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittLehrerRepository}
	 */
	public UvPlanungsabschnittLehrerRepository getUvPlanungsabschnittLehrerRepository() {
		return getOrCreate(UvPlanungsabschnittLehrerRepository.class, () -> new UvPlanungsabschnittLehrerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvPlanungsabschnittSchuelerRepository}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittSchuelerRepository}
	 */
	public UvPlanungsabschnittSchuelerRepository getUvPlanungsabschnittSchuelerRepository() {
		return getOrCreate(UvPlanungsabschnittSchuelerRepository.class, () -> new UvPlanungsabschnittSchuelerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvKlasseRepository}.
	 *
	 * @return eine Instanz des {@link UvKlasseRepository}
	 */
	public UvKlasseRepository getUvKlasseRepository() {
		return getOrCreate(UvKlasseRepository.class, () -> new UvKlasseRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvLerngruppeRepository}.
	 *
	 * @return eine Instanz des {@link UvLerngruppeRepository}
	 */
	public UvLerngruppeRepository getUvLerngruppeRepository() {
		return getOrCreate(UvLerngruppeRepository.class, () -> new UvLerngruppeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchuelergruppeRepository}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeRepository}
	 */
	public UvSchuelergruppeRepository getUvSchuelergruppeRepository() {
		return getOrCreate(UvSchuelergruppeRepository.class, () -> new UvSchuelergruppeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchuelergruppeConstraintJahrgangRepository}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeConstraintJahrgangRepository}
	 */
	public UvSchuelergruppeConstraintJahrgangRepository getUvSchuelergruppeConstraintJahrgangRepository() {
		return getOrCreate(UvSchuelergruppeConstraintJahrgangRepository.class, () -> new UvSchuelergruppeConstraintJahrgangRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchuelergruppeConstraintSchuelergruppeRepository}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeConstraintSchuelergruppeRepository}
	 */
	public UvSchuelergruppeConstraintSchuelergruppeRepository getUvSchuelergruppeConstraintSchuelergruppeRepository() {
		return getOrCreate(UvSchuelergruppeConstraintSchuelergruppeRepository.class,
				() -> new UvSchuelergruppeConstraintSchuelergruppeRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchuelergruppeSchuelerRepository}.
	 *
	 * @return eine Instanz des {@link UvSchuelergruppeSchuelerRepository}
	 */
	public UvSchuelergruppeSchuelerRepository getUvSchuelergruppeSchuelerRepository() {
		return getOrCreate(UvSchuelergruppeSchuelerRepository.class, () -> new UvSchuelergruppeSchuelerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvUnterrichtRepository}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtRepository}
	 */
	public UvUnterrichtRepository getUvUnterrichtRepository() {
		return getOrCreate(UvUnterrichtRepository.class, () -> new UvUnterrichtRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvUnterrichtRaumRepository}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtRaumRepository}
	 */
	public UvUnterrichtRaumRepository getUvUnterrichtRaumRepository() {
		return getOrCreate(UvUnterrichtRaumRepository.class, () -> new UvUnterrichtRaumRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvUnterrichtLerngruppenlehrerRepository}.
	 *
	 * @return eine Instanz des {@link UvUnterrichtLerngruppenlehrerRepository}
	 */
	public UvUnterrichtLerngruppenlehrerRepository getUvUnterrichtLerngruppenlehrerRepository() {
		return getOrCreate(UvUnterrichtLerngruppenlehrerRepository.class, () -> new UvUnterrichtLerngruppenlehrerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvKlassenLehrerRepository}.
	 *
	 * @return eine Instanz des {@link UvKlassenLehrerRepository}
	 */
	public UvKlassenLehrerRepository getUvKlassenLehrerRepository() {
		return getOrCreate(UvKlassenLehrerRepository.class, () -> new UvKlassenLehrerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvLerngruppenLehrerRepository}.
	 *
	 * @return eine Instanz des {@link UvLerngruppenLehrerRepository}
	 */
	public UvLerngruppenLehrerRepository getUvLerngruppenLehrerRepository() {
		return getOrCreate(UvLerngruppenLehrerRepository.class, () -> new UvLerngruppenLehrerRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvLerngruppenSchieneRepository}.
	 *
	 * @return eine Instanz des {@link UvLerngruppenSchieneRepository}
	 */
	public UvLerngruppenSchieneRepository getUvLerngruppenSchieneRepository() {
		return getOrCreate(UvLerngruppenSchieneRepository.class, () -> new UvLerngruppenSchieneRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvKursRepository}.
	 *
	 * @return eine Instanz des {@link UvKursRepository}
	 */
	public UvKursRepository getUvKursRepository() {
		return getOrCreate(UvKursRepository.class, () -> new UvKursRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchieneRepository}.
	 *
	 * @return eine Instanz des {@link UvSchieneRepository}
	 */
	public UvSchieneRepository getUvSchieneRepository() {
		return getOrCreate(UvSchieneRepository.class, () -> new UvSchieneRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvSchieneConstraintJahrgangRepository}.
	 *
	 * @return eine Instanz des {@link UvSchieneConstraintJahrgangRepository}
	 */
	public UvSchieneConstraintJahrgangRepository getUvSchieneConstraintJahrgangRepository() {
		return getOrCreate(UvSchieneConstraintJahrgangRepository.class, () -> new UvSchieneConstraintJahrgangRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvPlanungsabschnittZeitrasterRepository}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittZeitrasterRepository}
	 */
	public UvPlanungsabschnittZeitrasterRepository getUvPlanungsabschnittZeitrasterRepository() {
		return getOrCreate(UvPlanungsabschnittZeitrasterRepository.class, () -> new UvPlanungsabschnittZeitrasterRepositoryImpl(conn));
	}

	/**
	 * Liefert eine Instanz des {@link UvPlanungsabschnittZeitrasterConstraintJahrgangRepository}.
	 *
	 * @return eine Instanz des {@link UvPlanungsabschnittZeitrasterConstraintJahrgangRepository}
	 */
	public UvPlanungsabschnittZeitrasterConstraintJahrgangRepository getUvPlanungsabschnittZeitrasterConstraintJahrgangRepository() {
		return getOrCreate(UvPlanungsabschnittZeitrasterConstraintJahrgangRepository.class,
				() -> new UvPlanungsabschnittZeitrasterConstraintJahrgangRepositoryImpl(conn));
	}

}

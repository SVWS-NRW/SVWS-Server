package de.svws_nrw.service.enm;

import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class EnmV2ServiceFactory {

	private final KatalogeRepositoryFactory katalogeRepositoryFactory;
	private final KlassenRepositoryFactory klassenRepositoryFactory;
	private final KurseRepositoryFactory kurseRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final NotenmodulRepositoryFactory notenmodulRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final SchuleRepositoryFactory schuleRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param katalogeRepositoryFactory     die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory      die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory        die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Services
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory       die Factory für Schul-Services
	 */
	private EnmV2ServiceFactory(final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory) {
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.notenmodulRepositoryFactory = notenmodulRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param katalogeRepositoryFactory     die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory      die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory        die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory       die Factory für Lehrer-Repositories
	 * @param notenmodulRepositoryFactory   die Factory für Notenmodul-Services
	 * @param schuelerRepositoryFactory     die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory       die Factory für Schul-Services
	 *
	 * @return die neue Factory-Instanz
	 */
	public static EnmV2ServiceFactory getNewInstance(final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final NotenmodulRepositoryFactory notenmodulRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory) {
		return new EnmV2ServiceFactory(katalogeRepositoryFactory, klassenRepositoryFactory, kurseRepositoryFactory, lehrerRepositoryFactory,
				notenmodulRepositoryFactory, schuelerRepositoryFactory, schuleRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für das Auslesen der ENM-Daten in der Version 2
	 *
	 * @return der Service
	 */
	public EnmV2GetService getEnmV2GetService() {
		return new EnmV2GetService(EnmV2GetServiceKontext.of(
				schuleRepositoryFactory.getSchuleRepository(),
				schuleRepositoryFactory.getSchulleitungRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				schuleRepositoryFactory.getAbteilungenRepository(),
				schuleRepositoryFactory.getAbteilungenKlassenRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				katalogeRepositoryFactory.getFachRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerZP10Repository(),
				kurseRepositoryFactory.getKurseRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				katalogeRepositoryFactory.getJahrgaengeRepository(),
				katalogeRepositoryFactory.getFoerderschwerpunkteRepository(),
				katalogeRepositoryFactory.getAnkreuzkompetenzenKonfigurationRepository(),
				katalogeRepositoryFactory.getAnkreuzkompetenzenRepository(),
				katalogeRepositoryFactory.getAnkreuzkompetenzenJahrgaengeRepository(),
				katalogeRepositoryFactory.getFloskelRepository(),
				katalogeRepositoryFactory.getFloskelgruppenRepository(),
				katalogeRepositoryFactory.getFloskelJahrgaengeRepository(),
				katalogeRepositoryFactory.getTeilleistungsartRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository()));
	}


	/**
	 * Erstellt einen Service für den Import von ENM-Daten in der Version 2
	 *
	 * @return der Service
	 */
	public EnmV2ImportService getEnmV2ImportService() {
		return new EnmV2ImportService(lehrerRepositoryFactory.getLehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenRepository(),
				schuelerRepositoryFactory.getSchuelerTeilleistungenTimestampsRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenRepository(),
				schuelerRepositoryFactory.getSchuelerAnkreuzkompetenzenTimestampsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsRepository(),
				notenmodulRepositoryFactory.getNotenmodulCredentialsTimestampsRepository());
	}

}

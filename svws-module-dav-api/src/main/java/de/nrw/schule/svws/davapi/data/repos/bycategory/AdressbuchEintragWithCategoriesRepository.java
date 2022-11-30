package de.nrw.schule.svws.davapi.data.repos.bycategory;

import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.core.data.schule.Schuljahresabschnitt;
import de.nrw.schule.svws.core.utils.schule.SchuljahresAbschnittsManager;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IAdressbuchKontaktRepository;
import de.nrw.schule.svws.davapi.data.repos.bycategory.AdressbuchWithCategoriesRepository.AdressbuchContactTypes;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Dieses Repository für Adressbucheinträge ergänzt an jedem Kontakt die
 * Kategorien, in denen dieser Kontakt gefunden werden soll, beispielsweise
 * Klassen oder Kurse, Fachschaft, etc. Dies ermöglicht Clientabhängige
 * Interpretation der Kategorien als Suchparameter oder als Mailingliste.
 *
 */
class AdressbuchEintragWithCategoriesRepository implements IAdressbuchKontaktRepository {

	/**
	 * das Kontaktrepository für Schueler
	 */
	private IAdressbuchKontaktRepository schuelerRepository;

	/**
	 * das Kontaktrepository für Erzieher
	 */
	private IAdressbuchKontaktRepository erzieherRepository;

	/**
	 * das Kontaktrepository für Lehrer
	 */
	private IAdressbuchKontaktRepository lehrerRepository;

	/**
	 * der aktuelle Schuljahresabschnitt
	 */
	private DTOSchuljahresabschnitte aktuellerSchuljahresabschnitt;

	/**
	 * Utility zum erzeugen von Kategorie-Strings
	 */
	private AdressbuchKategorienUtil kategorienUtil;

	/**
	 * Die Datenbankverbindung
	 */
	private DBEntityManager conn;

	/** der Benutzer, dessen Adressbuecher gesucht werden */
	private Benutzer user;

	/**
	 * Konstruktor zum Erstellen des Repositories mit einer Datenbankverbindung
	 *
	 * @param conn die Datenbankverbindung
	 */
	public AdressbuchEintragWithCategoriesRepository(DBEntityManager conn) {
		this.conn = conn;
		this.user = conn.getUser();
		querySchuljahresabschnitt(conn);
	}

	/**
	 * instantiieren des SchuelerRepositories
	 */
	private void instantiateSchuelerRepository() {
		if (this.schuelerRepository == null) {
			this.schuelerRepository = new SchuelerWithCategoriesRepository(conn, user, aktuellerSchuljahresabschnitt,
					kategorienUtil);
		}
	}

	/**
	 * instantiieren des LehrerRepositories
	 */

	private void instantiateLehrerRepository() {
		if (this.lehrerRepository == null) {
			this.lehrerRepository = new LehrerWithCategoriesRepository(conn, user, aktuellerSchuljahresabschnitt,
					kategorienUtil);
		}
	}

	/**
	 * instantiieren des ErzieherRepositories
	 */

	private void instantiateErzieherRepository() {
		if (this.erzieherRepository == null) {
			this.erzieherRepository = new ErzieherWithCategoriesRepository(conn, aktuellerSchuljahresabschnitt,
					kategorienUtil);
		}
	}

	/**
	 * Sucht den aktuellen Schuljahresabschnitt und erstellt das
	 * {@link AdressbuchKategorienUtil} für die repositories
	 *
	 * @param conn die Datenbankverbindung
	 */
	private void querySchuljahresabschnitt(DBEntityManager conn) {
		DTOEigeneSchule dtoEigeneSchule = conn.queryNamed("DTOEigeneSchule.all", DTOEigeneSchule.class)
				.getSingleResult();
		List<DTOSchuljahresabschnitte> dtoSchuljahresAbschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id",
				dtoEigeneSchule.Schuljahresabschnitts_ID, DTOSchuljahresabschnitte.class);
		aktuellerSchuljahresabschnitt = dtoSchuljahresAbschnitte.get(0);
		Schuljahresabschnitt abschnitt = new Schuljahresabschnitt();
		abschnitt.abschnitt = aktuellerSchuljahresabschnitt.Abschnitt;
		abschnitt.schuljahr = aktuellerSchuljahresabschnitt.Jahr;
		abschnitt.id = aktuellerSchuljahresabschnitt.ID;
		String schuljahresAbschnittAsString = SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt,
				dtoEigeneSchule.AnzahlAbschnitte);
		kategorienUtil = new AdressbuchKategorienUtil(schuljahresAbschnittAsString);
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(@NotNull String adressbuchId,
			CollectionRessourceQueryParameters params) {
		AdressbuchContactTypes adressbuchEnum = AdressbuchContactTypes.valueOf(adressbuchId.toUpperCase());

		List<AdressbuchEintrag> result = switch (adressbuchEnum) {
		case SCHUELER: {
			instantiateSchuelerRepository();
			yield schuelerRepository.getKontakteByAdressbuch(adressbuchId, params);
		}
		case ERZIEHER: {
			instantiateErzieherRepository();
			yield erzieherRepository.getKontakteByAdressbuch(adressbuchId, params);
		}
		case LEHRER: {
			instantiateLehrerRepository();
			yield lehrerRepository.getKontakteByAdressbuch(adressbuchId, params);
		}
		default: {
			throw new IllegalArgumentException("Unexpected value: " + adressbuchId);
		}
		};
		result.forEach(k -> k.adressbuchId = adressbuchId);
		return result;
	}

}

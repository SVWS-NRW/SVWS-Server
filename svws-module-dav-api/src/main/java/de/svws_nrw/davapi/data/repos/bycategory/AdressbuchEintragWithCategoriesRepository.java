package de.svws_nrw.davapi.data.repos.bycategory;

import de.svws_nrw.core.data.adressbuch.AdressbuchEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.utils.schule.SchuljahresAbschnittsManager;
import de.svws_nrw.davapi.data.CollectionRessourceQueryParameters;
import de.svws_nrw.davapi.data.IAdressbuchKontaktRepository;
import de.svws_nrw.davapi.data.repos.bycategory.AdressbuchWithCategoriesRepository.AdressbuchContactTypes;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;

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
	private final DBEntityManager conn;

	/** der Benutzer, dessen Adressbuecher gesucht werden */
	private final Benutzer user;

	/**
	 * Konstruktor zum Erstellen des Repositories mit einer Datenbankverbindung
	 *
	 * @param conn die Datenbankverbindung
	 */
	AdressbuchEintragWithCategoriesRepository(final DBEntityManager conn) {
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
	private void querySchuljahresabschnitt(final DBEntityManager conn) {
		final DTOEigeneSchule dtoEigeneSchule = conn.queryNamed("DTOEigeneSchule.all", DTOEigeneSchule.class)
				.getSingleResult();
		final List<DTOSchuljahresabschnitte> dtoSchuljahresAbschnitte = conn.queryNamed("DTOSchuljahresabschnitte.id",
				dtoEigeneSchule.Schuljahresabschnitts_ID, DTOSchuljahresabschnitte.class);
		aktuellerSchuljahresabschnitt = dtoSchuljahresAbschnitte.get(0);
		final Schuljahresabschnitt abschnitt = new Schuljahresabschnitt();
		abschnitt.abschnitt = aktuellerSchuljahresabschnitt.Abschnitt;
		abschnitt.schuljahr = aktuellerSchuljahresabschnitt.Jahr;
		abschnitt.id = aktuellerSchuljahresabschnitt.ID;
		final String schuljahresAbschnittAsString = SchuljahresAbschnittsManager.createSchuljahresAbschnittString(abschnitt,
				dtoEigeneSchule.AnzahlAbschnitte);
		kategorienUtil = new AdressbuchKategorienUtil(schuljahresAbschnittAsString);
	}

	@Override
	public List<AdressbuchEintrag> getKontakteByAdressbuch(@NotNull final String adressbuchId, final CollectionRessourceQueryParameters params) {
		final AdressbuchContactTypes adressbuchEnum = AdressbuchContactTypes.valueOf(adressbuchId.toUpperCase());
		final List<AdressbuchEintrag> result = switch (adressbuchEnum) {
			case SCHUELER -> {
				instantiateSchuelerRepository();
				yield schuelerRepository.getKontakteByAdressbuch(adressbuchId, params);
			}
			case ERZIEHER -> {
				instantiateErzieherRepository();
				yield erzieherRepository.getKontakteByAdressbuch(adressbuchId, params);
			}
			case LEHRER -> {
				instantiateLehrerRepository();
				yield lehrerRepository.getKontakteByAdressbuch(adressbuchId, params);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + adressbuchId);
		};
		result.forEach(k -> k.adressbuchId = adressbuchId);
		return result;
	}

}

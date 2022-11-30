package de.nrw.schule.svws.davapi.data.repos.bycategory;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import de.nrw.schule.svws.core.data.adressbuch.Adressbuch;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchEintrag;
import de.nrw.schule.svws.core.data.adressbuch.AdressbuchKontakt;
import de.nrw.schule.svws.davapi.data.CollectionRessourceQueryParameters;
import de.nrw.schule.svws.davapi.data.IAdressbuchKontaktRepository;
import de.nrw.schule.svws.davapi.data.IAdressbuchRepository;
import de.nrw.schule.svws.davapi.util.AdressbuchTyp;
import de.nrw.schule.svws.db.DBEntityManager;

/**
 * Eine Implementierung des Adressbuchrepositories für Datenbankzugriffe. Bei
 * dieser Implementierung werden Adressbücher nur für die
 * {@link AdressbuchContactTypes} erstellt, Kontakte innerhalb des Adressbuchs
 * mit Kategorien ergänzt und damit im Client sortier- und suchbar.
 *
 */
public class AdressbuchWithCategoriesRepository implements IAdressbuchRepository {

	/**
	 * Kontaktrepository zum Anreichern eines Adressbuchs um
	 * {@link AdressbuchKontakt}e
	 */
	private IAdressbuchKontaktRepository adressbuchKontaktRepository;
	
	/**
	 * Erstellt ein neues Repository mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Repository benutzt werden
	 *             soll
	 */
	public AdressbuchWithCategoriesRepository(DBEntityManager conn) {
		this.adressbuchKontaktRepository = new AdressbuchEintragWithCategoriesRepository(conn);
	}

	@Override
	public Optional<Adressbuch> getAdressbuchById(String adressbuchId, CollectionRessourceQueryParameters params) {
		Optional<Adressbuch> adressbuchOpt = this.getAvailableAdressbuecher(params).stream()
				.filter(a -> a.id.equals(adressbuchId)).findFirst();
		if (adressbuchOpt.isEmpty()) {
			return adressbuchOpt;
		}
		if (params.includeRessources) {
			List<AdressbuchEintrag> kontakteByAdressbuch = adressbuchKontaktRepository
					.getKontakteByAdressbuch(adressbuchId, params);
			adressbuchOpt.get().adressbuchEintraege.addAll(kontakteByAdressbuch);
		}
		return adressbuchOpt;
	}

	@Override
	public List<Adressbuch> getAvailableAdressbuecher(CollectionRessourceQueryParameters params) {
		List<Adressbuch> result = new Vector<>();
		result.add(createAdressbuch(AdressbuchContactTypes.SCHUELER));
		result.add(createAdressbuch(AdressbuchContactTypes.LEHRER));
		result.add(createAdressbuch(AdressbuchContactTypes.ERZIEHER));
		// TODO persönliche, öffentiche Adressbuecher und Adressbuecher für
		// Personengruppen sind noch nicht implementiert
		return result;
	}

	/**
	 * Erstellt ein Adressbuch mit gegebener Adressbuch Kontakt Art und gegebenem
	 * AdressbuchTyp
	 *
	 * @param adressbuchContactTypes die Art des Adressbuchs nach Kontakten, gibt
	 *                               die ID und Beschreibung des Adressbuchs
	 * @param typ                    der AdressbuchTyp unterscheidet, ob ein
	 *                               Adressbuch persönlich, generiert oder
	 *                               öffentlich ist
	 * @return das erzeugte Adressbuch
	 */
	private static Adressbuch createAdressbuch(AdressbuchContactTypes adressbuchContactTypes, AdressbuchTyp typ) {
		return createAdressbuch(adressbuchContactTypes.toString().toLowerCase(), adressbuchContactTypes.anzeigeName,
				adressbuchContactTypes.beschreibung);
	}

	/**
	 * Erstellt ein neues Adressbuch anhand eines {@link AdressbuchContactTypes}
	 *
	 * @param adressbuchType die Art des Adressbuchs
	 * @return das erstellte Adressbuch
	 */
	private static Adressbuch createAdressbuch(AdressbuchContactTypes adressbuchType) {
		return createAdressbuch(adressbuchType, AdressbuchTyp.GENERIERT);
	}

	/**
	 * Erstellt ein neues Adressbuch mit Id, anzeigename und Beschreibung
	 *
	 * @param id           die ID des Adressbuchs
	 * @param displayName  der Anzeigename des Adressbuchs
	 * @param beschreibung die Beschreibung des Adressbuchs
	 * @return das erstellte Adressbuch
	 */
	private static Adressbuch createAdressbuch(String id, String displayName, String beschreibung) {
		Adressbuch a = new Adressbuch();
		a.adressbuchTyp = AdressbuchTyp.GENERIERT.bezeichnung;
		a.beschreibung = beschreibung;
		a.displayname = displayName;
		a.id = id;
		return a;
	}

	/**
	 * Enum für die verschiedenen Adressbuecher, die dieses Repository abbildet
	 *
	 */
	public enum AdressbuchContactTypes {
		/** ein Adressbuch mit Schuelerdaten */
		SCHUELER("Schüler", "Ein generiertes Adressbuch mit allen Schülern"),
		/** ein Adressbuch mit Lehrerdaten */
		LEHRER("Lehrer", "Ein generiertes Adressbuch mit allen Lehrern"),
		/** ein Adressbuch mit Erzieherdaten */
		ERZIEHER("Erzieher", "Ein generiertes Adressbuch mit allen Erziehungsberechtigten"),
		/** ein persönliches Adressbuch */
		PERSOENLICH("Persönlich", "Ihr persönliches Adressbuch"),
		/** ein Adressbuch mit Daten von Personengruppen */
		PERSONENGRUPPEN("Personengruppen", "Ein generiertes Adressbuch mit Personengruppen"),
		/** ein öffentliches Adressbuch */
		OEFFENTLICH("Öffentlich", "Das öffentliche Adressbuch der Schule");

		/** der Anzeigename des Adressbuchs */
		private String anzeigeName;
		/** die Beschreibung des Adressbuchs */
		private String beschreibung;

		/**
		 * erstellt eine neue Adressbuchkonstakte mit Anzeigename und Beschreibung
		 *
		 * @param anzeigeName  der Anzeigename
		 * @param beschreibung die Beschreibung
		 */
		private AdressbuchContactTypes(String anzeigeName, String beschreibung) {
			this.anzeigeName = anzeigeName;
			this.beschreibung = beschreibung;
		}
	}

}

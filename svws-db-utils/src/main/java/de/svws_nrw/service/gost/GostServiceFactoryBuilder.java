package de.svws_nrw.service.gost;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.crypto.CryptoServiceFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;

/**
 * Diese Klasse wird in der Übergangszeit verwendet, wo Data-Klassen noch verwendet werden und nicht alles
 * auf Controller, Services und Repositories umgestellt wurde. Sie dient dazu den Services in Data-Klassen zu verwenden.
 * TODO Data-Klassen schrittweise umstellen, die Methoden dieser Klasse dabei zurückbauen und zum Abschluss dann die Klassen entfernen
 */
public final class GostServiceFactoryBuilder {

	private GostServiceFactoryBuilder() {
		throw new IllegalStateException("Instantiation not allowed.");
	}

	/**
	 * Gibt die Instanz einer {@link GostServiceFactory} zurück.
	 *
	 * @return die {@link GostServiceFactory}
	 */
	public static GostServiceFactory getGostServiceFactory() {
		final BenutzerRepositoryFactory benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final SchuelerRepositoryFactory schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final BenutzerServiceFactory benutzerServiceFactory = BenutzerServiceFactory.getNewInstance(benutzerRepositoryFactory);
		return GostServiceFactory.getNewInstance(
				GostRepositoryFactory.getNewInstance(),
				schuelerRepositoryFactory,
				LehrerRepositoryFactory.getNewInstance(),
				benutzerRepositoryFactory,
				KatalogRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				benutzerServiceFactory,
				CryptoServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
				SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
				GostKlausurenRepositoryFactory.getNewInstance());
	}

}

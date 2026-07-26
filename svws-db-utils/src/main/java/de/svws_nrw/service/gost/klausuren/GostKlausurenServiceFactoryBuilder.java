package de.svws_nrw.service.gost.klausuren;

import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;

/**
 * Diese Klasse wird in der Übergangszeit verwendet, wo Data-Klassen noch verwendet werden und nicht alles
 * auf Controller, Services und Repositories umgestellt wurde. Sie dient dazu den Services in Data-Klassen zu verwenden.
 *
 * TODO Data-Klassen schrittweise umstellen, die Methoden dieser Klasse dabei zurückbauen und zum Abschluss dann die Klassen entfernen
 */
public final class GostKlausurenServiceFactoryBuilder {

	private GostKlausurenServiceFactoryBuilder() {
		throw new IllegalStateException("Instantiation not allowed.");
	}

	/**
	 * Gibt die Instanz einer {@link GostKlausurenServiceFactory} zurück.
	 *
	 * @return die {@link GostKlausurenServiceFactory}
	 */
	public static GostKlausurenServiceFactory getGostKlausurenServiceFactory() {
		return GostKlausurenServiceFactory.getNewInstance(
			GostKlausurenRepositoryFactory.getNewInstance(),
			GostServiceFactoryBuilder.getGostServiceFactory());
	}

}

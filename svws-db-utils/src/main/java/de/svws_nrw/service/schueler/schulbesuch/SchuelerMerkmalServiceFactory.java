package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmaleRepositoryFactory;
import de.svws_nrw.repo.schule.merkmale.MerkmalRepositoryFactory;

public final class SchuelerMerkmalServiceFactory {

	private final SchuelerMerkmaleRepositoryFactory repoFactory;
	private final MerkmalRepositoryFactory merkmalRepoFactory;
	private final SchuelerMerkmalMapper mapper;

	private SchuelerMerkmalServiceFactory(
			final SchuelerMerkmaleRepositoryFactory repoFactory,
			final MerkmalRepositoryFactory merkmalRepoFactory,
			final SchuelerMerkmalMapper mapper) {
		this.repoFactory = repoFactory;
		this.merkmalRepoFactory = merkmalRepoFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der {@code SchuelerMerkmalServiceFactory}.
	 *
	 * @param repoFactory 			das Repository-Factory für {@link SchuelerMerkmalService}-Instanzen
	 * @param merkmalRepoFactory 	das Repository-Factory für {@link MerkmalRepositoryFactory}-Instanzen
	 * @param mapper     			der Mapper zur Konvertierung zwischen Entity und API-Modell
	 * @return eine neue {@code SchuelerMerkmalServiceFactory}
	 */
	public static SchuelerMerkmalServiceFactory getNewInstance(
			final SchuelerMerkmaleRepositoryFactory repoFactory,
			final MerkmalRepositoryFactory merkmalRepoFactory,
			final SchuelerMerkmalMapper mapper) {
		return new SchuelerMerkmalServiceFactory(repoFactory, merkmalRepoFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerMerkmalService}.
	 * <p>
	 * Schulen- und Entlassgrundkataloge werden über die aktuelle Datenbankverbindung geladen.
	 * </p>
	 *
	 * @return ein neuer {@code SchuelerMerkmalService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerMerkmalService getSchuelerMerkmalService() {
		return new SchuelerMerkmalService(repoFactory.getSchuelerMerkmaleRepository(), merkmalRepoFactory.getMerkmalRepository(), mapper);
	}
}

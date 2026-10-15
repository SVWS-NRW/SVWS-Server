package de.svws_nrw.repo.schulwechsel;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselAbgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselErfolgreicheBewerbung;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepository;
import de.svws_nrw.repo.schulwechsel.abgang.SchulwechselAbgangRepositoryImpl;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepository;
import de.svws_nrw.repo.schulwechsel.dokument.SchulwechselDokumentRepositoryImpl;
import de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungRepository;
import de.svws_nrw.repo.schulwechsel.erfolgreiche_bewerbung.SchulwechselErfolgreicheBewerbungRepositoryImpl;

public class SchulwechselRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchulwechselRepositoryFactory getNewInstance() {
		return new SchulwechselRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchulwechselAbgang}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchulwechselAbgangRepository getSchulwechselAbgangRepository() {
		return this.getOrCreate(SchulwechselAbgangRepository.class, () -> new SchulwechselAbgangRepositoryImpl(this.conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchulwechselDokument}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchulwechselDokumentRepository getSchulwechselDokumentRepository() {
		return this.getOrCreate(SchulwechselDokumentRepository.class, () -> new SchulwechselDokumentRepositoryImpl(this.conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchulwechselErfolgreicheBewerbung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchulwechselErfolgreicheBewerbungRepository getSchulwechselErfolgreicheBewerbungRepository() {
		return this.getOrCreate(SchulwechselErfolgreicheBewerbungRepository.class, () -> new SchulwechselErfolgreicheBewerbungRepositoryImpl(this.conn));
	}
}

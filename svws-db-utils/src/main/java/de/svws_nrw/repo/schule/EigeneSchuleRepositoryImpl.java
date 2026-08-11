package de.svws_nrw.repo.schule;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.repo.RepositoryException;
import de.svws_nrw.repo.RepositoryImpl;
import jakarta.annotation.Nonnull;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schuldaten.
 */
public final class EigeneSchuleRepositoryImpl extends RepositoryImpl<DTOEigeneSchule> implements EigeneSchuleRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public EigeneSchuleRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOEigeneSchule.class, o -> o.ID, (o, id) -> o.ID = id);
	}


	@Override
	public long getIdSchuljahresabschnitt() {
		return super.getFirst().Schuljahresabschnitts_ID;
	}

	@Override
	public int getSchulnummer() {
		final var schule = super.getFirst();
		if (schule.SchulNr == null) {
			throw new RepositoryException("Die aktuelle Schule hat keine SchulNr hinterlegt");
		}
		return schule.SchulNr;
	}

	@Override
	@Nonnull
	public Schulform getSchulform() {
		final var schule = super.getFirst();
		if (schule.SchulformKuerzel == null) {
			throw new RepositoryException("Die aktuelle Schule hat keine Schulform hinterlegt");
		}

		final var schulform = Schulform.data().getWertByKuerzel(schule.SchulformKuerzel);
		if (schulform == null) {
			throw new RepositoryException("Die aktuelle Schule hat keine gültige Schulform hinterlegt");
		}

		return schulform;
	}

}

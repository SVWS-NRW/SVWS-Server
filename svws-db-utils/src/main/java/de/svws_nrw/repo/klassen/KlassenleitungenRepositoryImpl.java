package de.svws_nrw.repo.klassen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitungPK;
import de.svws_nrw.repo.RepositoryBaseImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf Klassenleitungen von Klassen.
 */
public final class KlassenleitungenRepositoryImpl extends RepositoryBaseImpl<DTOKlassenLeitung, DTOKlassenLeitungPK> implements KlassenleitungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public KlassenleitungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOKlassenLeitung.class);
	}

	@Override
	protected Object[] mapIdToParameter(final DTOKlassenLeitungPK id) {
		return new Object[] { id.Klassen_ID, id.Lehrer_ID };
	}

	@Override
	public Map<Long, List<Long>> getMapKlassenleitungen(final Collection<Long> idsKlassen) {
		if ((idsKlassen == null) || (idsKlassen.isEmpty()))
			return Collections.emptyMap();

		final var listLeitungen = conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, idsKlassen);
		listLeitungen.sort(Comparator.comparingInt(kl -> kl.Reihenfolge));

		final Map<Long, List<Long>> mapLeitungen = HashMap.newHashMap(idsKlassen.size());
		for (final Long idKlasse : idsKlassen) {
			mapLeitungen.put(idKlasse, new ArrayList<>());
		}

		for (final var kl : listLeitungen) {
			final List<Long> lehrerListe = mapLeitungen.get(kl.Klassen_ID);
			if ((lehrerListe != null) && !lehrerListe.contains(kl.Lehrer_ID)) {
				lehrerListe.add(kl.Lehrer_ID);
			}
		}
		return mapLeitungen;
	}

}

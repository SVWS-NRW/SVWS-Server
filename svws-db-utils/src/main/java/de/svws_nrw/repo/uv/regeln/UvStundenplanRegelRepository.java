package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegel;
import de.svws_nrw.repo.Repository;

/** Repository für die Blockungsregeln der UV. */
public interface UvStundenplanRegelRepository extends Repository<DTOUvStundenplanRegel> {

	/**
	 * Liefert die Regeln einer Stundenplankonfiguration.
	 *
	 * @param idKonfiguration die ID der Konfiguration
	 * @return die Regeln
	 */
	List<DTOUvStundenplanRegel> getListByKonfiguration(long idKonfiguration);
}

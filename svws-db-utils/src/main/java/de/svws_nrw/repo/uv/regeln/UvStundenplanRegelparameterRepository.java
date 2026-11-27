package de.svws_nrw.repo.uv.regeln;

import java.util.List;

import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegelparameter;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegelparameterPK;
import de.svws_nrw.repo.RepositoryBase;

/** Repository für die Parameter der UV-Blockungsregeln. */
public interface UvStundenplanRegelparameterRepository extends RepositoryBase<DTOUvStundenplanRegelparameter, DTOUvStundenplanRegelparameterPK> {

	/**
	 * Liefert die Parameter einer Regel.
	 *
	 * @param idRegel die ID der Regel
	 * @return die Parameter
	 */
	List<DTOUvStundenplanRegelparameter> getListByRegel(long idRegel);
}

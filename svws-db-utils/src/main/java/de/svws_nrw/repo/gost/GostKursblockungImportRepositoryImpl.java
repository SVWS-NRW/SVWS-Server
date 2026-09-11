package de.svws_nrw.repo.gost;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;

/**
 * Implementierung des Repositories für den Import aus Gost-Kursblockungen.
 */
public final class GostKursblockungImportRepositoryImpl implements GostKursblockungImportRepository {

	private final DBEntityManager conn;

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public GostKursblockungImportRepositoryImpl(final DBEntityManager conn) {
		this.conn = conn;
	}

	@Override
	public DTOGostBlockung getBlockung(final long idBlockung) {
		return conn.queryByKey(DTOGostBlockung.class, idBlockung);
	}

	@Override
	public DTOGostBlockungZwischenergebnis getZwischenergebnis(final long idErgebnis) {
		return conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
	}

	@Override
	public List<DTOGostBlockungKurs> getKurseByBlockungId(final long idBlockung) {
		return conn.queryList(DTOGostBlockungKurs.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungKurs.class, idBlockung);
	}

	@Override
	public List<DTOGostBlockungKurslehrer> getKurslehrerByKursIds(final Collection<Long> kursIds) {
		return kursIds.isEmpty() ? List.of()
				: conn.queryList(DTOGostBlockungKurslehrer.QUERY_LIST_BY_BLOCKUNG_KURS_ID, DTOGostBlockungKurslehrer.class, kursIds);
	}

	@Override
	public List<DTOGostBlockungSchiene> getSchienenByBlockungId(final long idBlockung) {
		return conn.queryList(DTOGostBlockungSchiene.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungSchiene.class, idBlockung);
	}

	@Override
	public List<DTOGostBlockungZwischenergebnisKursSchiene> getErgebnisKursSchienenByZwischenergebnisId(final long idErgebnis) {
		return conn.queryList(DTOGostBlockungZwischenergebnisKursSchiene.QUERY_BY_ZWISCHENERGEBNIS_ID,
				DTOGostBlockungZwischenergebnisKursSchiene.class, idErgebnis);
	}

	@Override
	public List<DTOGostBlockungZwischenergebnisKursSchueler> getErgebnisKursSchuelerByZwischenergebnisId(final long idErgebnis) {
		return conn.queryList(DTOGostBlockungZwischenergebnisKursSchueler.QUERY_BY_ZWISCHENERGEBNIS_ID,
				DTOGostBlockungZwischenergebnisKursSchueler.class, idErgebnis);
	}

}

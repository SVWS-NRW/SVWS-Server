package de.svws_nrw.data.stundenplan;

import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.data.stundenplan.StundenplanAufsichtsbereich;
import de.svws_nrw.core.data.stundenplan.StundenplanJahrgang;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenzeit;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanAufsichtsbereich;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO
 * {@link Stundenplan}.
 */
public final class DataStundenplan extends DataManagerRevised<Long, DTOStundenplan, Stundenplan> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link Stundenplan}.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public DataStundenplan(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("idSchuljahresabschnitt", "gueltigAb", "gueltigBis", "wochenTypModell");
		setAttributesNotPatchable("id", "idSchuljahresabschnitt");
	}

	/**
	 * Gibt die Daten eines Stundenplans zu dessen ID zurück.
	 *
	 * @param id   Die ID des Stundenplans.
	 *
	 * @return die Daten des Stundenplans zur ID.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	@Override
	public Stundenplan getById(final Long id) throws ApiOperationException {
		final DTOStundenplan dto = getDTO(id);
		return map(dto);
	}

	@Override
	protected long getLongId(final DTOStundenplan dto) throws ApiOperationException {
		return dto.ID;
	}


	@Override
	protected Stundenplan map(final DTOStundenplan stundenplan) throws ApiOperationException {
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, stundenplan.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			return null;
		final List<StundenplanZeitraster> zeitraster = DataStundenplanZeitraster.getZeitraster(conn, stundenplan.ID);
		final List<StundenplanRaum> raeume = DataStundenplanRaeume.getRaeume(conn, stundenplan.ID);
		final List<StundenplanSchiene> schienen = DataStundenplanSchienen.getSchienen(conn, stundenplan.ID);
		final List<StundenplanJahrgang> jahrgaenge = DataStundenplanJahrgaenge.getJahrgaenge(conn, stundenplan.ID);
		final List<StundenplanPausenzeit> pausenzeiten = new DataStundenplanPausenzeiten(conn, stundenplan.ID).getList();
		final List<StundenplanAufsichtsbereich> aufsichtsbereiche = DataStundenplanAufsichtsbereiche.getAufsichtsbereiche(conn, stundenplan.ID);
		final List<StundenplanKalenderwochenzuordnung> kalenderwochenzuordnung = new DataStundenplanKalenderwochenzuordnung(conn, stundenplan.ID).getList();
		// Erstelle das Core-DTO-Objekt für die Response
		final Stundenplan daten = new Stundenplan();
		daten.id = stundenplan.ID;
		daten.idSchuljahresabschnitt = stundenplan.Schuljahresabschnitts_ID;
		daten.schuljahr = schuljahresabschnitt.Jahr;
		daten.abschnitt = schuljahresabschnitt.Abschnitt;
		daten.gueltigAb = stundenplan.Beginn;
		if (stundenplan.Ende == null) {
			daten.gueltigBis = "%04d-%02d-%02d".formatted(schuljahresabschnitt.Jahr + 1, 7, 31);
		} else {
			daten.gueltigBis = stundenplan.Ende;
		}
		daten.aktiv = stundenplan.Aktiv;
		daten.bezeichnungStundenplan = stundenplan.Beschreibung;
		daten.wochenTypModell = stundenplan.WochentypModell;
		daten.zeitraster.addAll(zeitraster);
		daten.raeume.addAll(raeume);
		daten.schienen.addAll(schienen);
		daten.jahrgaenge.addAll(jahrgaenge);
		daten.pausenzeiten.addAll(pausenzeiten);
		daten.aufsichtsbereiche.addAll(aufsichtsbereiche);
		daten.kalenderwochenZuordnung.addAll(kalenderwochenzuordnung);
		return daten;
	}

	@Override
	protected void initDTO(final DTOStundenplan dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected void mapAttribute(final DTOStundenplan dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> dto.ID = JSONMapper.convertToLong(value, false);
			case "idSchuljahresabschnitt" -> dto.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false);
			case "gueltigAb" -> dto.Beginn = JSONMapper.convertToString(value, false, false, null);
			case "gueltigBis" -> dto.Ende = JSONMapper.convertToString(value, false, false, null);
			case "aktiv" -> dto.Aktiv = JSONMapper.convertToBoolean(value, false);
			case "bezeichnungStundenplan" -> dto.Beschreibung = JSONMapper.convertToString(value, false, false, 1000);
			case "wochenTypModell" -> {
				final long idStundenplan = dto.ID;
				int wochentypmodell = JSONMapper.convertToIntegerInRange(value, false, 0, 100);
				if (wochentypmodell == 1)
					wochentypmodell = 0;
				if (dto.WochentypModell == wochentypmodell)
					return;
				// Bestimme den kompletten Unterricht, der einem Wochentyp > als dem Wert für das Wochentyp-Modell zugeordnet ist und passe diesen ggf. an.
				final List<Long> idsZeitraster = conn.queryList(DTOStundenplanZeitraster.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanZeitraster.class, idStundenplan)
						.stream().map(z -> z.ID).toList();
				if (!idsZeitraster.isEmpty()) {
					final List<DTOStundenplanUnterricht> unterrichte =
							conn.queryList("SELECT e FROM DTOStundenplanUnterricht e WHERE e.Zeitraster_ID IN ?1 AND e.Wochentyp > ?2",
									DTOStundenplanUnterricht.class, idsZeitraster, wochentypmodell);
					if (!unterrichte.isEmpty()) {
						for (final DTOStundenplanUnterricht unterricht : unterrichte)
							unterricht.Wochentyp = 0;
						conn.transactionPersistAll(unterrichte);
						conn.transactionFlush();
					}
				}
				// Bestimme alle Aufsichtsbereich des Stundenplans, wo zugeordnete Pausenaufsichten einen Wochentyp > als dem Wert für das Wochentyp-Modell zugeordnet haben und passe diesen ggf. an.
				final List<Long> idsAufsichtsbereiche =
						conn.queryList(DTOStundenplanAufsichtsbereich.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanAufsichtsbereich.class, idStundenplan)
								.stream().map(z -> z.ID).toList();
				if (!idsAufsichtsbereiche.isEmpty()) {
					final List<DTOStundenplanPausenaufsichtenBereiche> bereiche =
							conn.queryList("SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID IN ?1 AND e.Wochentyp > ?2",
									DTOStundenplanPausenaufsichtenBereiche.class, idsAufsichtsbereiche, wochentypmodell);
					if (!bereiche.isEmpty()) {
						final HashMap2D<Long, Long, Boolean> updatedPausenaufsichtsbereich = new HashMap2D<>();
						for (final DTOStundenplanPausenaufsichtenBereiche bereich : bereiche) {
							final List<DTOStundenplanPausenaufsichtenBereiche> andereBereiche = conn.queryList(
									"SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID = ?1 AND e.Pausenaufsicht_ID = ?2 AND e.Wochentyp <= ?3",
									DTOStundenplanPausenaufsichtenBereiche.class, bereich.Aufsichtsbereich_ID, bereich.Pausenaufsicht_ID, wochentypmodell);
							if (andereBereiche.isEmpty() && !updatedPausenaufsichtsbereich.contains(bereich.Aufsichtsbereich_ID, bereich.Pausenaufsicht_ID)) {
								bereich.Wochentyp = 0;
								conn.transactionPersist(bereich);
								updatedPausenaufsichtsbereich.put(bereich.Aufsichtsbereich_ID, bereich.Pausenaufsicht_ID, true);
							} else {
								conn.transactionRemove(bereich);
							}
						}
						conn.transactionFlush();
					}
				}
				// Lösche die Kalenderwochenzuordnungen
				conn.transactionExecuteDelete("DELETE FROM DTOStundenplanKalenderwochenZuordnung e WHERE e.Stundenplan_ID = %d".formatted(dto.ID));
				// Setze das Wochentyp-Modell
				dto.WochentypModell = wochentypmodell;
			}
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Das Patchen des Attributes %s wird nicht unterstützt.".formatted(name));
		}
	}

	/**
	 * Die Methode ermittelt das entsprechende {@link DTOStundenplan} Objekt zur angegebenen Stundenplan-ID.
	 *
	 * @param id ID des Stundenplans
	 *
	 * @return Ein {@link DTOStundenplan} Objekt.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public DTOStundenplan getDTO(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Stundenplan darf nicht null sein.");
		final DTOStundenplan dto = conn.queryByKey(DTOStundenplan.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein Stundenplan zur ID " + id + " gefunden.");
		return dto;
	}

	/**
	 * Prüft, ob ein Stundenplan mit der angegegeben ID vorhanden ist und gibt das Datenbank-DTO
	 * ggf. zurück. Ist der Stundenplan nicht vorhanden, so wird eine {@link ApiOperationException}
	 * geworfen.
	 *
	 * @param conn   die zu verwendende Datenbank-Verbindung
	 * @param id     die ID des Stundenplans
	 *
	 * @return das Datenbank-DTO
	 *
	 * @throws ApiOperationException falls kein Stundenplan mit der ID gefunden wurde
	 */
	public static DTOStundenplan getDTOStundenplan(final DBEntityManager conn, final Long id) throws ApiOperationException {
		return new DataStundenplan(conn).getDTO(id);
	}

	/**
	 * Fügt ein neues DTO des übergebenen Typ in die Datenbank hinzu, indem in der
	 * Datenbank eine neue ID abgefragt wird und die Attribute des JSON-Objektes gemäß dem
	 * Attribut-Mapper integriert werden. Um zu gewährleisten, dass der Primärschlüssel
	 * angelegt ist, wird das Patchen von einzelnen Attributen zurückgestellt und erst nach
	 * dem Persistieren des Objektes in einem zweiten Schritt durchgeführt.
	 *
	 * @param initAttributes  die Map mit den initialen Attributen für das neue DTO
	 *
	 * @return das Core-DTO
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public Stundenplan add(final Map<String, Object> initAttributes) throws ApiOperationException {
		Stundenplan stundenplan = super.add(initAttributes);
		// Füge die Schienen, welche bereits in der Kursliste angegeben sind zum Stundenplan hinzu
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, stundenplan.idSchuljahresabschnitt, false);
		DataStundenplanSchienen.updateSchienenFromKursliste(conn, stundenplan.id, kurse);
		return stundenplan;
	}

	/**
	 * Methode prüft vor dem Persistieren eines Datenbank-DTOs, ob alle Vorbedingungen zum Patch erfüllt sind.
	 * Standardmäßig hat diese Methode keine Implementierung.
	 * Wenn eine Prüfung durchgeführt werden soll, muss diese Methode überschrieben werden.
	 *
	 * @param dto              das Stundenplan-DTO
	 * @param patchedAttributes    die Map mit dem Mapping der Attributnamen auf die Werte der Attribute im Patch
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	@Override
	public void checkBeforePersist(final DTOStundenplan dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		if (Boolean.TRUE.equals(dto.Aktiv) && (patchedAttributes.containsKey("gueltigAb") || patchedAttributes.containsKey("gueltigBis") || patchedAttributes.containsKey("aktiv"))) {
			final List<StundenplanListeEintrag> plaene = DataStundenplanListe.getStundenplaene(conn, dto.Schuljahresabschnitts_ID);
			for (final StundenplanListeEintrag plan : plaene) {
                if (plan.id == dto.ID || !plan.aktiv)
                    continue;
                if (DateUtils.berechneGemeinsameTage(plan.gueltigAb, plan.gueltigBis, dto.Beginn, dto.Ende).length > 0)
                    throw new ApiOperationException(Status.CONFLICT,
                            "Der Gültigkeit des Stundenplans steht in Konflikt zum Stundenplan mit der ID %d.".formatted(plan.id));
            }
		}
	}

	@Override
	protected void saveDatabaseDTO(final DTOStundenplan dto) throws ApiOperationException {
		super.saveDatabaseDTO(dto);
		DataGostKlausurenRaum.dbHookStundenplangueltigkeitMinus(conn, dto);
		DataGostKlausurenRaum.dbHookStundenplangueltigkeitPlus(conn, dto);
	}

	@Override
	protected void deleteDatabaseDTO(final DTOStundenplan dto) throws ApiOperationException {
		final String cacheBeginn = dto.Beginn;
		final String cacheEnde = dto.Ende;
		dto.Beginn = "1970-01-01";
		dto.Ende = "1970-01-02";
		DataGostKlausurenRaum.dbHookStundenplangueltigkeitMinus(conn, dto);
		dto.Beginn = cacheBeginn;
		dto.Ende = cacheEnde;
		super.deleteDatabaseDTO(dto);
	}

}

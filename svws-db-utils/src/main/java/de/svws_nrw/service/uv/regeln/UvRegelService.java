package de.svws_nrw.service.uv.regeln;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.List;

import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.db.dto.current.uv.DTOUvKlasse;
import de.svws_nrw.db.dto.current.uv.DTOUvLerngruppe;
import de.svws_nrw.db.dto.current.uv.DTOUvPlanungsabschnittLehrerPK;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanKonfiguration;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegel;
import de.svws_nrw.db.dto.current.uv.DTOUvStundenplanRegelparameter;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import de.svws_nrw.repo.uv.klassen.UvKlasseRepository;
import de.svws_nrw.repo.uv.lehrer.UvPlanungsabschnittLehrerRepository;
import de.svws_nrw.repo.uv.lerngruppen.UvLerngruppeRepository;
import de.svws_nrw.repo.uv.planungsabschnitte.UvPlanungsabschnittRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanKonfigurationRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanRegelRepository;
import de.svws_nrw.repo.uv.regeln.UvStundenplanRegelparameterRepository;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.data.TransactionSupport.transactional;

/** Speichert die UV-Blockungsregeln in der Stundenplankonfiguration eines Planungsabschnitts. */
public final class UvRegelService {

	private final UvPlanungsabschnittRepository planungsabschnitte;
	private final UvStundenplanKonfigurationRepository konfigurationen;
	private final UvStundenplanRegelRepository regeln;
	private final UvStundenplanRegelparameterRepository parameter;
	private final UvPlanungsabschnittLehrerRepository lehrer;
	private final UvKlasseRepository klassen;
	private final UvLerngruppeRepository lerngruppen;
	private final UvFachRepository faecher;

	/**
	 * Erstellt den Service mit den benötigten UV-Repositories.
	 *
	 * @param planungsabschnitte das Repository der Planungsabschnitte
	 * @param konfigurationen das Repository der Stundenplankonfigurationen
	 * @param regeln das Repository der Regeln
	 * @param parameter das Repository der Regelparameter
	 * @param lehrer das Repository der Planungsabschnitt-Lehrkräfte
	 * @param klassen das Repository der Klassen
	 * @param lerngruppen das Repository der Lerngruppen
	 * @param faecher das Repository der UV-Fächer
	 */
	public UvRegelService(final UvPlanungsabschnittRepository planungsabschnitte,
			final UvStundenplanKonfigurationRepository konfigurationen, final UvStundenplanRegelRepository regeln,
			final UvStundenplanRegelparameterRepository parameter, final UvPlanungsabschnittLehrerRepository lehrer,
			final UvKlasseRepository klassen, final UvLerngruppeRepository lerngruppen, final UvFachRepository faecher) {
		this.planungsabschnitte = planungsabschnitte;
		this.konfigurationen = konfigurationen;
		this.regeln = regeln;
		this.parameter = parameter;
		this.lehrer = lehrer;
		this.klassen = klassen;
		this.lerngruppen = lerngruppen;
		this.faecher = faecher;
	}

	private void assertPlan(final long idPlan) {
		if (planungsabschnitte.findById(idPlan).isEmpty()) {
			throw new ApiOperationException(Status.NOT_FOUND, "UV-Planungsabschnitt nicht gefunden.");
		}
	}

	private DTOUvStundenplanKonfiguration konfiguration(final long idPlan, final boolean create) {
		assertPlan(idPlan);
		final List<DTOUvStundenplanKonfiguration> list = konfigurationen.getListByPlanungsabschnitt(idPlan);
		if (!list.isEmpty()) {
			return list.stream().min(Comparator.comparingLong(dto -> dto.ID)).orElseThrow();
		}
		if (!create) {
			return null;
		}
		final DTOUvStundenplanKonfiguration dto = konfigurationen.create(new DTOUvStundenplanKonfiguration(0, idPlan));
		konfigurationen.flush();
		return dto;
	}

	/**
	 * Gibt alle Regeln der ersten Stundenplankonfiguration eines Planungsabschnitts zurück.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @return die Regeln
	 */
	public List<UvBlockungRegel> getList(final long idPlan) {
		final DTOUvStundenplanKonfiguration config = konfiguration(idPlan, false);
		if (config == null) {
			return List.of();
		}
		final List<UvBlockungRegel> result = new ArrayList<>();
		for (final DTOUvStundenplanRegel dto : regeln.getListByKonfiguration(config.ID)) {
			result.add(toApi(dto));
		}
		result.sort(Comparator.comparingLong(regel -> regel.id));
		return result;
	}

	private UvBlockungRegel toApi(final DTOUvStundenplanRegel dto) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = dto.ID;
		regel.typ = dto.Typ;
		final List<DTOUvStundenplanRegelparameter> regelparameter = parameter.getListByRegel(dto.ID);
		regelparameter.sort(Comparator.comparingInt(item -> item.Nummer));
		for (final DTOUvStundenplanRegelparameter item : regelparameter) {
			if (item.Nummer == 0) {
				regel.prioritaet = Math.toIntExact(item.Parameter);
			} else {
				regel.parameter.add(item.Parameter);
			}
		}
		return regel;
	}

	private static void validate(final UvBlockungRegel regel) {
		if (UvBlockungRegelTyp.ofNr(regel.typ) == UvBlockungRegelTyp.UNDEFINIERT || regel.parameter == null
				|| regel.prioritaet < 0 || regel.prioritaet > UvBlockungRegelPrioritaet.DEAKTIVIERT.nr
				|| regel.parameter.isEmpty() || regel.parameter.stream().anyMatch(value -> value == null || value < 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ungültiger Regeltyp, Priorität oder Parameter.");
		}
		final int anzahl = regel.parameter.size();
		final boolean ungueltigeAnzahl = switch (regel.typ) {
			case 1, 3, 5, 7, 9, 11, 44 -> anzahl != 1;
			case 17 -> anzahl != 4;
			case 20, 21 -> anzahl != 3;
			case 24, 42, 43 -> anzahl < 3;
			case 29, 30, 31 -> anzahl < 2;
			default -> anzahl != 2;
		};
		if (ungueltigeAnzahl) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Falsche Parameterzahl für den UV-Regeltyp.");
		}
	}

	/* Prüft Abschnittsbezüge und Fächer vor dem Schreiben der Regel. */
	private void validateReferenzen(final long idPlan, final UvBlockungRegel regel) {
		switch (regel.typ) {
			case 2, 4 -> assertKlasse(idPlan, regel.parameter.get(0));
			case 6, 8, 10, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
					32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 45 -> assertLehrer(idPlan, regel.parameter.get(0));
			default -> {
				// Für die übrigen Regeltypen ist hier keine zusätzliche Referenzprüfung nötig.
			}
		}
		switch (regel.typ) {
			case 13, 14, 15, 16 -> assertLerngruppe(idPlan, regel.parameter.get(1));
			case 17, 28 -> assertLerngruppe(idPlan, regel.parameter.get(0));
			case 18, 32, 33, 34, 35, 36, 37, 38, 39 -> assertKlasse(idPlan, regel.parameter.get(1));
			case 20, 21 -> assertFach(regel.parameter.get(2));
			case 24, 25 -> assertFach(regel.parameter.get(1));
			case 27 -> assertLehrer(idPlan, regel.parameter.get(1));
			case 29, 30 -> assertLerngruppen(idPlan, regel.parameter, 0);
			case 31 -> assertLerngruppen(idPlan, regel.parameter, 1);
			case 42, 43 -> assertLerngruppen(idPlan, regel.parameter, 2);
			default -> {
				// Für die übrigen Regeltypen ist hier keine zusätzliche Referenzprüfung nötig.
			}
		}
	}

	private void assertLehrer(final long idPlan, final long id) {
		if (lehrer.findById(new DTOUvPlanungsabschnittLehrerPK(idPlan, id)).isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Lehrkraft gehört nicht zum Planungsabschnitt.");
		}
	}

	private void assertKlasse(final long idPlan, final long id) {
		final DTOUvKlasse dto = klassen.findById(id).orElse(null);
		if (dto == null || dto.Planungsabschnitt_ID != idPlan) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Klasse gehört nicht zum Planungsabschnitt.");
		}
	}

	private void assertLerngruppe(final long idPlan, final long id) {
		final DTOUvLerngruppe dto = lerngruppen.findById(id).orElse(null);
		if (dto == null || dto.Planungsabschnitt_ID != idPlan) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Lerngruppe gehört nicht zum Planungsabschnitt.");
		}
	}

	private void assertLerngruppen(final long idPlan, final List<Long> ids, final int start) {
		for (int i = start; i < ids.size(); i++) {
			assertLerngruppe(idPlan, ids.get(i));
		}
	}

	private void assertFach(final long id) {
		if (faecher.findById(id).isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "UV-Fach nicht gefunden.");
		}
	}

	private void persistParameter(final UvBlockungRegel regel) {
		parameter.update(new DTOUvStundenplanRegelparameter(regel.id, 0, regel.prioritaet));
		for (int i = 0; i < regel.parameter.size(); i++) {
			parameter.update(new DTOUvStundenplanRegelparameter(regel.id, i + 1, regel.parameter.get(i)));
		}
	}

	/**
	 * Erstellt eine Regel im angegebenen Planungsabschnitt.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param regel die neue Regel
	 * @return die gespeicherte Regel
	 */
	public UvBlockungRegel create(final long idPlan, final UvBlockungRegel regel) {
		validate(regel);
		return transactional(() -> {
			final DTOUvStundenplanKonfiguration config = konfiguration(idPlan, true);
			validateReferenzen(idPlan, regel);
			final DTOUvStundenplanRegel dto = regeln.create(new DTOUvStundenplanRegel(0, config.ID, regel.typ));
			regel.id = dto.ID;
			regeln.flush();
			persistParameter(regel);
			return regel;
		});
	}

	private DTOUvStundenplanRegel getOwned(final long idPlan, final long idRegel) {
		final DTOUvStundenplanKonfiguration config = konfiguration(idPlan, false);
		final DTOUvStundenplanRegel dto = regeln.findById(idRegel).orElse(null);
		if (config == null || dto == null || dto.StundenplanKonfiguration_ID != config.ID) {
			throw new ApiOperationException(Status.NOT_FOUND, "UV-Regel nicht gefunden.");
		}
		return dto;
	}

	private void removeParameter(final long idRegel) {
		for (final DTOUvStundenplanRegelparameter item : parameter.getListByRegel(idRegel)) {
			parameter.delete(item);
		}
		parameter.flush();
	}

	/**
	 * Ersetzt eine Regel und ihre geordneten Parameter.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param idRegel die ID der Regel
	 * @param regel die neuen Werte
	 * @return die gespeicherte Regel
	 */
	public UvBlockungRegel update(final long idPlan, final long idRegel, final UvBlockungRegel regel) {
		validate(regel);
		return transactional(() -> {
			final DTOUvStundenplanRegel dto = getOwned(idPlan, idRegel);
			validateReferenzen(idPlan, regel);
			dto.Typ = regel.typ;
			regeln.update(dto);
			removeParameter(idRegel);
			regel.id = idRegel;
			persistParameter(regel);
			return regel;
		});
	}

	/**
	 * Ändert mehrere Regeln einschließlich ihrer Parameter in einer gemeinsamen Transaktion.
	 *
	 * @param idPlan der Planungsabschnitt
	 * @param regelnNeu die vollständigen neuen Werte der betroffenen Regeln
	 * @return die gespeicherten Regeln
	 */
	public List<UvBlockungRegel> updateMultiple(final long idPlan, final Collection<UvBlockungRegel> regelnNeu) {
		if (regelnNeu == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Regeln müssen angegeben werden.");
		}
		return transactional(() -> {
			final Set<Long> ids = new HashSet<>();
			for (final UvBlockungRegel regel : regelnNeu) {
				if ((regel == null) || !ids.add(regel.id)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Regelliste enthält leere oder doppelte Einträge.");
				}
				validate(regel);
				getOwned(idPlan, regel.id);
				validateReferenzen(idPlan, regel);
			}
			final List<UvBlockungRegel> result = new ArrayList<>();
			for (final UvBlockungRegel regel : regelnNeu) {
				result.add(update(idPlan, regel.id, regel));
			}
			return result;
		});
	}

	/**
	 * Löscht eine Regel einschließlich ihrer Parameter.
	 *
	 * @param idPlan die ID des Planungsabschnitts
	 * @param idRegel die ID der Regel
	 */
	public void delete(final long idPlan, final long idRegel) {
		transactional(() -> {
			final DTOUvStundenplanRegel dto = getOwned(idPlan, idRegel);
			removeParameter(idRegel);
			regeln.delete(dto);
		});
	}
}

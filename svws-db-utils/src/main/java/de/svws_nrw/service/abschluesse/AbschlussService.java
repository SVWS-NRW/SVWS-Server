package de.svws_nrw.service.abschluesse;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.List;

import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.abschluss.Abschlussdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;


/**
 * Ein Service für den Zugriff auf die Abschlüsse
 */
public final class AbschlussService {

	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private final EigeneSchuleRepository schuleRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
	 * @param schuleRepository                  das Repository für die Informationen zur Schule
	 * @param schuelerLernabschnittRepository   das Repository für die Schülerlernabschnitte
	 */
	public AbschlussService(final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final EigeneSchuleRepository schuleRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository) {
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.schuleRepository = schuleRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
	}


	private static String toApiPruefOrdnungGesamtschule(final String val) {
		if (val == null) {
			return null;
		}
		return switch (val) {
			case "GE/APO-SI05/5-10" -> "APO-SI05";
			case "GE/APO-SI20/5-10" -> "APO-SI20";
			default -> null;
		};
	}


	private static Long toApiSchulabschlussAllgemeinbildendGesamtschule(final String pruefungsOrdnung, final int schuljahr, final String val) {
		if ((pruefungsOrdnung == null) || (val == null)) {
			return null;
		}
		// TODO Was ist mit val == "GE/APO-SI05/AGZ" bzw. val == "GE/APO-SI05/AGZ_FSL"
		final String[] tmp = val.split("/");
		if ((tmp.length != 3) || (!"GE".equals(tmp[0]))) {
			return null;
		}
		return switch (tmp[2]) {
			case "OA" -> SchulabschlussAllgemeinbildend.OA.daten(schuljahr).id;
			case "ESA" -> SchulabschlussAllgemeinbildend.HA9.daten(schuljahr).id;
			case "EESA" -> SchulabschlussAllgemeinbildend.HA10.daten(schuljahr).id;
			case "MSA" -> SchulabschlussAllgemeinbildend.MSA.daten(schuljahr).id;
			case "MSAQ-E" -> SchulabschlussAllgemeinbildend.MSA_Q.daten(schuljahr).id;
			case "MSAQ-Q" -> SchulabschlussAllgemeinbildend.MSA_Q1.daten(schuljahr).id;
			default -> null;
		};
	}

	private static Abschlussdaten toApiGesamtschule(final DTOSchuelerLernabschnittsdaten dto, final Schulform schulform,
			final DTOSchuljahresabschnitte schuljahresabschnitt) {
		// Bestimme den Jahrgang des Lernabschnittes
		final var jahrgang = Jahrgaenge.data().getBySchuljahrAndSchulformAndSchluessel(schuljahresabschnitt.Jahr, schulform, dto.ASDJahrgang);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Jahrgang, welcher sich aus den Daten des Schülerlernabschnittes ergibt ist nicht gültig (%s).".formatted(dto.ASDJahrgang));
		}

		// Für die Oberstufe wird die Schnittstelle aktuell noch nicht unterstützt
		if (jahrgang.istGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Gymnasiale Oberstufe an der Gesamtschule wird aktuell von dieser API-Methode noch nicht unterstützt.");
		}

		// Bestimme das vorraussichtliche Abschluss-Schuljahr im zehnten Jahrgang
		final int abschlussSchuljahr = switch (jahrgang) {
			case JAHRGANG_09 -> schuljahresabschnitt.Jahr + 1;
			case JAHRGANG_10 -> schuljahresabschnitt.Jahr + 0;
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Für den Jahrgang wird die Abschlussberechnung aktuell nicht unterstützt.");
		};

		final var daten = new Abschlussdaten();
		daten.idLernabschnitt = dto.ID;
		daten.pruefungsordnung = toApiPruefOrdnungGesamtschule(dto.PruefOrdnung);
		daten.idAbschluss = toApiSchulabschlussAllgemeinbildendGesamtschule(daten.pruefungsordnung, abschlussSchuljahr, dto.Abschluss);
		daten.idAbschlussQuartalsprognose = toApiSchulabschlussAllgemeinbildendGesamtschule(daten.pruefungsordnung, abschlussSchuljahr, dto.PrognoseAbschluss);
		daten.idAbschlussBerufsbildend = null;
		daten.istAbschlussPrognose = (dto.AbschlIstPrognose != null) && dto.AbschlIstPrognose;
		daten.idAbschlussart = (dto.AbschlussArt == null) ? null : dto.AbschlussArt.longValue();
		daten.textErgebnisPruefungsalgorithmus = dto.PruefAlgoErgebnis;
		daten.textErgebniseQuartalsprognose = dto.PrognoseLog;
		return daten;
	}


	private Abschlussdaten getBySchulform(final DTOSchuelerLernabschnittsdaten lernabschnittsdaten) {
		final var schulformKuerzel = schuleRepository.getFirst().SchulformKuerzel;
		final var schulform = Schulform.data().getWertByKuerzelOrException(schulformKuerzel);
		final var schuljahresabschnitt = schuljahresabschnitteRepository.getById(lernabschnittsdaten.Schuljahresabschnitts_ID);
		return switch (schulform) {
			case GE, SK, PS -> toApiGesamtschule(lernabschnittsdaten, schulform, schuljahresabschnitt);
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Schulform %s wird aktuell von der API noch nicht unterstützt.".formatted(schulformKuerzel));
		};
	}



	/**
	 * Ermittelt die Abschlussinformationen für den angegebenen Lernabschnitt
	 *
	 * @param idLernabschnitt   die ID des Schülerlernabschnittes
	 *
	 * @return die Abschlussinformationen
	 */
	public Abschlussdaten getByIdLernabschnitt(final long idLernabschnitt) {
		final var lernabschnittsdaten = schuelerLernabschnittRepository.findById(idLernabschnitt).orElseThrow(
				() -> new ApiOperationException(Status.BAD_REQUEST, "Es konnte kein Lernabschnitt mit der ID %d gefunden werden.".formatted(idLernabschnitt)));
		return getBySchulform(lernabschnittsdaten);
	}


	/**
	 * Ermittelt die Abschlussinformationen für den angebenen Schüler in dem angegebenen Schuljahrsabschnitt
	 *
	 * @param idSchueler               die ID des Schülers
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @return die Abschlussinformationen
	 */
	public Abschlussdaten getByIdSchuelerAndIdSchuljahresabschnitt(final long idSchueler, final long idSchuljahresabschnitt) {
		final var mapLernabschnitte = schuelerLernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(List.of(idSchueler), idSchuljahresabschnitt);
		if (mapLernabschnitte.size() != 1) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es konnte kein Lernabschnitt für den Schüler mit der ID %d und den Schuljahresabschnitt mir der ID %d gefunden werden."
							.formatted(idSchueler, idSchuljahresabschnitt));
		}
		return getBySchulform(mapLernabschnitte.values().iterator().next());
	}


	private static String fromApiSchulabschlussAllgemeinbildendGesamtschule(final String pruefungsOrdnung, final int schuljahr, final Long val) {
		if (val ==  null) {
			return null;
		}
		final StringBuilder sb = new StringBuilder("GE/");
		if ("APO-SI05".equals(pruefungsOrdnung)) {
			sb.append("APO-SI05");
		} else if ("APO-SI20".equals(pruefungsOrdnung)) {
			sb.append("APO-SI20");
		} else {
			return null;
		}
		if (val == SchulabschlussAllgemeinbildend.OA.daten(schuljahr).id) {
			sb.append("/OA");
		} else if (val == SchulabschlussAllgemeinbildend.HA9.daten(schuljahr).id) {
			sb.append("/ESA");
		} else if (val == SchulabschlussAllgemeinbildend.HA10.daten(schuljahr).id) {
			sb.append("/EESA");
		} else if (val == SchulabschlussAllgemeinbildend.MSA.daten(schuljahr).id) {
			sb.append("/MSA");
		} else if (val == SchulabschlussAllgemeinbildend.MSA_Q.daten(schuljahr).id) {
			sb.append("/MSAQ-E");
		} else if (val == SchulabschlussAllgemeinbildend.MSA_Q1.daten(schuljahr).id) {
			sb.append("/MSAQ-Q");
		} else {
			return null;
		}
		return sb.toString();
	}


	private static void patchGesamtschule(final DTOSchuelerLernabschnittsdaten entity, final Schulform schulform,
			final DTOSchuljahresabschnitte schuljahresabschnitt, final AbschlussPatchRequest patch) {
		// Bestimme den Jahrgang des Lernabschnittes
		final var jahrgang = Jahrgaenge.data().getBySchuljahrAndSchulformAndSchluessel(schuljahresabschnitt.Jahr, schulform, entity.ASDJahrgang);
		if (jahrgang == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Jahrgang, welcher sich aus den Daten des Schülerlernabschnittes ergibt ist nicht gültig (%s).".formatted(entity.ASDJahrgang));
		}

		// Für die Oberstufe wird die Schnittstelle aktuell noch nicht unterstützt
		if (jahrgang.istGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Gymnasiale Oberstufe an der Gesamtschule wird aktuell von dieser API-Methode noch nicht unterstützt.");
		}

		int jahreBisAbschluss = 0;
		patch.istAbschlussPrognose.ifPresent(val -> entity.AbschlIstPrognose = val);
		if (Boolean.TRUE.equals(entity.AbschlIstPrognose)) {
			switch (jahrgang) {
				case JAHRGANG_09 -> jahreBisAbschluss++;
				case JAHRGANG_10 -> {
					/* do nothing */ }
				default -> throw new ApiOperationException(Status.BAD_REQUEST, "Für den Jahrgang wird die Abschlussberechnung aktuell nicht unterstützt.");
			}
		}
		final int schuljahr = schuljahresabschnitt.Jahr + jahreBisAbschluss;

		patch.pruefungsordnung.ifPresent(val -> {
			switch (val) {
				case "APO-SI05" -> entity.PruefOrdnung = "GE/APO-SI05/5-10";
				case "APO-SI20" -> entity.PruefOrdnung = "GE/APO-SI20/5-10";
				default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Prüfungsordnung wird aktuell für diese Schulform nicht unterstützt.");
			}
		});
		final var pruefOrdnung = toApiPruefOrdnungGesamtschule(entity.PruefOrdnung);

		patch.idAbschluss.ifPresent(val -> entity.Abschluss = fromApiSchulabschlussAllgemeinbildendGesamtschule(pruefOrdnung, schuljahr, val));
		patch.idAbschlussBerufsbildend.ifPresent(val -> {
			if (val != null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "An einer Gesamtschule kann kein allgemeinbildender Abschluss erworben werden.");
			}
			entity.Abschluss_B = null;
		});
		patch.idAbschlussart.ifPresent(val -> entity.AbschlussArt = (val == null) ? null : val.intValue());
		patch.textErgebnisPruefungsalgorithmus.ifPresent(val -> entity.PruefAlgoErgebnis = val);
		patch.idAbschlussQuartalsprognose
				.ifPresent(val -> entity.PrognoseAbschluss = fromApiSchulabschlussAllgemeinbildendGesamtschule(pruefOrdnung, schuljahr, val)
				);
		patch.textErgebniseQuartalsprognose.ifPresent(val -> entity.PrognoseLog = val);
	}


	private void patchBySchulform(final DTOSchuelerLernabschnittsdaten entity, final AbschlussPatchRequest patch) {
		final var schulformKuerzel = schuleRepository.getFirst().SchulformKuerzel;
		final var schulform = Schulform.data().getWertByKuerzelOrException(schulformKuerzel);
		final var schuljahresabschnitt = schuljahresabschnitteRepository.getById(entity.Schuljahresabschnitts_ID);
		switch (schulform) {
			case GE, SK, PS -> patchGesamtschule(entity, schulform, schuljahresabschnitt, patch);
			default -> throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Schulform %s wird aktuell von der API noch nicht unterstützt.".formatted(schulformKuerzel));
		}
	}


	/**
	 * Führt einen Patch auf dem Unterrichtsfach mit der angegebenen ID aus.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnittes
	 * @param patch             der Patch
	 *
	 * @return die gepatchten Abschlussinformationen
	 */
	public Abschlussdaten patch(final long idLernabschnitt, final AbschlussPatchRequest patch) {
		return transactional(() -> {
			final var entity = schuelerLernabschnittRepository.findById(idLernabschnitt).orElseThrow(
					() -> new ApiOperationException(Status.BAD_REQUEST,
							"Es konnte kein Lernabschnitt mit der ID %d gefunden werden.".formatted(idLernabschnitt)));
			patchBySchulform(entity, patch);
			schuelerLernabschnittRepository.update(entity);
			schuelerLernabschnittRepository.flush();
			return getBySchulform(entity);
		});
	}

}

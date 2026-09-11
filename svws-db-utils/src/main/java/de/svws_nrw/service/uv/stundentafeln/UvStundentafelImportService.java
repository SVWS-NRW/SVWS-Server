package de.svws_nrw.service.uv.stundentafeln;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.core.data.uv.UvStundentafelImportOptions;
import de.svws_nrw.db.dto.current.uv.DTOUvFach;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafel;
import de.svws_nrw.db.dto.current.uv.DTOUvStundentafelFach;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.uv.faecher.UvFachRepository;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelFachRepository;
import de.svws_nrw.repo.uv.stundentafeln.UvStundentafelRepository;
import jakarta.ws.rs.core.Response.Status;

/** Importiert eine UV-Stundentafel aus den Leistungsdaten einer Schild-Klasse. */
public final class UvStundentafelImportService {
	private final UvStundentafelRepository stundentafeln;
	private final UvStundentafelFachRepository stundentafelFaecher;
	private final UvFachRepository uvFaecher;
	private final SchuelerLernabschnittRepository lernabschnitte;
	private final SchuelerLeistungsdatenRepository leistungsdaten;
	private final SchuljahresabschnitteRepository schuljahresabschnitte;
	private final KlassenRepository klassen;

	/**
	 * Erstellt einen Service für den Import von UV-Stundentafeln.
	 *
	 * @param stundentafeln          das Repository für UV-Stundentafeln
	 * @param stundentafelFaecher    das Repository für UV-Stundentafel-Fächer
	 * @param uvFaecher              das Repository für UV-Fächer
	 * @param lernabschnitte         das Repository für Schüler-Lernabschnitte
	 * @param leistungsdaten         das Repository für Schüler-Leistungsdaten
	 * @param schuljahresabschnitte  das Repository für Schuljahresabschnitte
	 * @param klassen                das Repository für Klassen
	 */
	public UvStundentafelImportService(final UvStundentafelRepository stundentafeln, final UvStundentafelFachRepository stundentafelFaecher,
			final UvFachRepository uvFaecher, final SchuelerLernabschnittRepository lernabschnitte,
			final SchuelerLeistungsdatenRepository leistungsdaten, final SchuljahresabschnitteRepository schuljahresabschnitte,
			final KlassenRepository klassen) {
		this.stundentafeln = stundentafeln;
		this.stundentafelFaecher = stundentafelFaecher;
		this.uvFaecher = uvFaecher;
		this.lernabschnitte = lernabschnitte;
		this.leistungsdaten = leistungsdaten;
		this.schuljahresabschnitte = schuljahresabschnitte;
		this.klassen = klassen;
	}

	/**
	 * Führt den Import vollständig innerhalb einer Transaktion aus.
	 *
	 * @param request  die Importdaten
	 *
	 * @return die importierten UV-Grunddaten
	 */
	public UvGrunddatenBundle importiere(final UvStundentafelImportOptions request) {
		return transactional(() -> doImportiere(request));
	}

	private UvGrunddatenBundle doImportiere(final UvStundentafelImportOptions request) {
		// Die Klasse bestimmt die Quellmenge. Insbesondere bei historischen Daten ist die
		// Jahrgangszuordnung im Lernabschnitt nicht immer gepflegt; der ausgewählte Jahrgang
		// wird deshalb ausschließlich für die neu anzulegende Stundentafel verwendet.
		final var abschnitt1 = schuljahresabschnitte.findBySchuljahrAndAbschnitt(request.schuljahr, 1)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Der erste Schuljahresabschnitt wurde nicht gefunden."));
		final var abschnitt2 = schuljahresabschnitte.findBySchuljahrAndAbschnitt(request.schuljahr, 2)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Der zweite Schuljahresabschnitt wurde nicht gefunden."));
		final var klasse1 = klassen.getById(request.idKlasse);
		if (klasse1.Schuljahresabschnitts_ID != abschnitt1.ID) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ausgewählte Klasse gehört nicht zum ersten Abschnitt des gewählten Schuljahres.");
		}
		final var klasse2 = klassen.getListBySchuljahresabschnitt(abschnitt2.ID).stream()
				.filter(k -> k.Klasse.equals(klasse1.Klasse)).findFirst()
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Für das zweite Halbjahr wurde keine Klasse mit demselben Kürzel gefunden."));
		final Map<Long, Double> wochenstunden1 = ermittleWochenstunden(idsLernabschnitte(klasse1.ID, abschnitt1.ID));
		final Map<Long, Double> wochenstunden2 = ermittleWochenstunden(idsLernabschnitte(klasse2.ID, abschnitt2.ID));
		final DTOUvStundentafel tafel = new DTOUvStundentafel(stundentafeln.getNextID(), request.idJahrgang, request.bezeichnung, request.gueltigVon);
		tafel.GueltigBis = request.gueltigBis;
		tafel.Beschreibung = request.beschreibung;
		stundentafeln.update(tafel);
		stundentafeln.flush();

		final UvGrunddatenBundle result = new UvGrunddatenBundle();
		result.stundentafeln.add(toApi(tafel));
		long nextUvFachId = uvFaecher.getNextID();
		long nextTafelFachId = stundentafelFaecher.getNextID();
		final List<DTOUvFach> neueUvFaecher = new ArrayList<>();
		final List<DTOUvStundentafelFach> neueTafelFaecher = new ArrayList<>();
		final Map<Long, DTOUvFach> uvFachBySchildFach = new HashMap<>();
		for (final long idSchildFach : java.util.stream.Stream.concat(wochenstunden1.keySet().stream(), wochenstunden2.keySet().stream()).collect(Collectors.toSet())) {
			DTOUvFach uvFach = findeUvFach(idSchildFach, request.gueltigVon);
			if ((uvFach == null) && request.fehlendeUvFaecherAnlegen) {
				uvFach = new DTOUvFach(nextUvFachId++, idSchildFach, request.gueltigVon);
				uvFach.GueltigBis = request.gueltigBis;
				neueUvFaecher.add(uvFach);
				result.faecher.add(toApi(uvFach));
			}
			if (uvFach != null) {
				uvFachBySchildFach.put(idSchildFach, uvFach);
			}
		}
		for (final var entry : wochenstunden1.entrySet()) {
			final DTOUvFach uvFach = uvFachBySchildFach.get(entry.getKey());
			if (uvFach != null) {
				neueTafelFaecher.add(new DTOUvStundentafelFach(nextTafelFachId++, tafel.ID, 1, uvFach.ID, entry.getValue(), 0.0));
			}
		}
		for (final var entry : wochenstunden2.entrySet()) {
			final DTOUvFach uvFach = uvFachBySchildFach.get(entry.getKey());
			if (uvFach != null) {
				neueTafelFaecher.add(new DTOUvStundentafelFach(nextTafelFachId++, tafel.ID, 2, uvFach.ID, entry.getValue(), 0.0));
			}
		}
		if (!neueUvFaecher.isEmpty()) {
			uvFaecher.update(neueUvFaecher);
			uvFaecher.flush();
		}
		if (!neueTafelFaecher.isEmpty()) {
			stundentafelFaecher.update(neueTafelFaecher);
			stundentafelFaecher.flush();
			neueTafelFaecher.forEach(f -> result.stundentafelfaecher.add(toApi(f)));
		}
		return result;
	}

	private Set<Long> idsLernabschnitte(final long idKlasse, final long idSchuljahresabschnitt) {
		final Set<Long> result = lernabschnitte.findListByKlasseAndSchuljahresabschnitt(idKlasse, idSchuljahresabschnitt).stream()
				.map(a -> a.ID).collect(Collectors.toSet());
		if (result.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ausgewählte Klasse enthält im Schuljahresabschnitt keine Schüler.");
		}
		return result;
	}

	private Map<Long, Double> ermittleWochenstunden(final Set<Long> idsLernabschnitte) {
		final Map<Long, List<Integer>> werteByFach = leistungsdaten.findListByLernabschnitt(idsLernabschnitte).stream()
				.filter(l -> l.Wochenstunden != null)
				.collect(Collectors.groupingBy(l -> l.Fach_ID, Collectors.mapping(l -> l.Wochenstunden, Collectors.toList())));
		final Map<Long, Double> result = new HashMap<>();
		for (final var entry : werteByFach.entrySet()) {
			final int stunden = entry.getValue().stream().collect(Collectors.groupingBy(v -> v, Collectors.counting())).entrySet().stream()
					.max(Comparator.<Map.Entry<Integer, Long>>comparingLong(Map.Entry::getValue).thenComparing(Map.Entry::getKey)).orElseThrow().getKey();
			result.put(entry.getKey(), (double) stunden);
		}
		return result;
	}

	private DTOUvFach findeUvFach(final long idSchildFach, final String datum) {
		return uvFaecher.getListByFachId(idSchildFach).stream()
				.filter(f -> (f.GueltigVon.compareTo(datum) <= 0) && ((f.GueltigBis == null) || (f.GueltigBis.compareTo(datum) >= 0)))
				.findFirst().orElse(null);
	}

	private static UvStundentafel toApi(final DTOUvStundentafel dto) {
		final var result = new UvStundentafel();
		result.id = dto.ID;
		result.idJahrgang = dto.Jahrgang_ID;
		result.bezeichnung = dto.Bezeichnung;
		result.gueltigVon = dto.GueltigVon;
		result.gueltigBis = dto.GueltigBis;
		result.beschreibung = dto.Beschreibung;
		return result;
	}

	private static UvFach toApi(final DTOUvFach dto) {
		final var result = new UvFach();
		result.id = dto.ID;
		result.idFach = dto.Fach_ID;
		result.gueltigVon = dto.GueltigVon;
		result.gueltigBis = dto.GueltigBis;
		return result;
	}

	private static UvStundentafelFach toApi(final DTOUvStundentafelFach dto) {
		final var result = new UvStundentafelFach();
		result.id = dto.ID;
		result.idStundentafel = dto.Stundentafel_ID;
		result.idFach = dto.Fach_ID;
		result.abschnitt = dto.Abschnitt;
		result.wochenstunden = dto.Wochenstunden;
		result.davonErgaenzungsstunden = dto.DavonErgaenzungsstunden;
		return result;
	}
}

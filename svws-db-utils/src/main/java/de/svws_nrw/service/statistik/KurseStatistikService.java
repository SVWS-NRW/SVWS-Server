package de.svws_nrw.service.statistik;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.statistik.KursStatistikGesamt;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Service für den Zugriff auf die Kursdaten für die Statistik
 */
public final class KurseStatistikService {

	/** Das Repository für den Zugriff auf die Schul-Daten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Das Repository für den Zugriff auf die Kurse-Daten */
	private final KurseRepository kurseRepository;

	/** Das Repository für den Zugriff auf die Zusatzkräfte */
	private final KurslehrerRepository kurslehrerRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param eigeneSchuleRepository               das Repository für die Schuldaten
	 * @param kurseRepository                das Repository für die Kursdaten
	 * @param kurslehrerRepository           das Repository für die Zusatzkräfte im Kurs
	 */
	public KurseStatistikService(final EigeneSchuleRepository eigeneSchuleRepository,
			final KurseRepository kurseRepository,
			final KurslehrerRepository kurslehrerRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.kurseRepository = kurseRepository;
		this.kurslehrerRepository = kurslehrerRepository;
	}


	private static List<Long> convertJahrgaenge(final DTOKurs kurs) {
		final List<Long> result = new ArrayList<>();
		if (kurs.Jahrgang_ID != null) {
			result.add(kurs.Jahrgang_ID);
		}
		if (kurs.Jahrgaenge != null) {
			for (final String jahrgang : kurs.Jahrgaenge.split(",")) {
				if (jahrgang.matches("^\\d+$")) {
					result.add(Long.parseLong(jahrgang));
				}
			}
		}
		return result;
	}


	private static KursStatistikGesamt map(final DTOKurs dto, final List<DTOKursLehrer> weitereLehrer) {
		final var daten = new KursStatistikGesamt();
		daten.id = dto.ID;
		daten.kuerzel = dto.KurzBez;
		daten.idJahrgaenge.addAll(convertJahrgaenge(dto));
		daten.idFach = dto.Fach_ID;
		daten.lehrer = dto.Lehrer_ID;
		daten.kursartAllg = (dto.KursartAllg == null) ? "" : dto.KursartAllg;
		daten.wochenstunden = (dto.WochenStd == null) ? 0 : dto.WochenStd;
		daten.wochenstundenLehrer = (dto.WochenstdKL == null) ? daten.wochenstunden : dto.WochenstdKL;
		daten.schulnummer = dto.SchulNr;
		for (final var lehrer : weitereLehrer) {
			final var kurslehrer = new KursLehrer();
			kurslehrer.idKurs = lehrer.Kurs_ID;
			kurslehrer.idLehrer = lehrer.Lehrer_ID;
			kurslehrer.wochenstundenLehrer = ((lehrer.Anteil == null) || (lehrer.Anteil < 0.0)) ? 0.0 : lehrer.Anteil;
			daten.weitereLehrer.add(kurslehrer);
		}
		return daten;
	}


	/**
	 * Gibt die Liste aller Statistik-relevanten Kurse für den an der Schule aktuellen Schuljahresabschnitt zurück.
	 *
	 * @return die Liste aller Statistik-relevanten Kurse
	 */
	public @NotNull List<KursStatistikGesamt> getList() {
		// Bestimme den aktuellen Schuljahresabschnitt der Schule
		final long idSchuljahresabschnitt = eigeneSchuleRepository.getIdSchuljahresabschnitt();

		// Bestimme zunächst die Statistik-Relevanten Lehrkräfte und deren IDs
		final List<DTOKurs> listKurse = kurseRepository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);
		final List<Long> listKursIDs = listKurse.stream().map(l -> l.ID).toList();

		// Bestimme die Zuordnung der weitere Lehrkräfte zu den einzelnen Kursen
		final var mapWeitereLehrer = kurslehrerRepository.getMapZusatzkraefte(listKursIDs);

		// Erstelle die Liste der Core-DTOs anhand der zuvor geladenen Daten
		return listKurse.stream().map(dto -> map(dto, mapWeitereLehrer.getOrDefault(dto.ID, Collections.emptyList()))).toList();
	}

}

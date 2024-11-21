package de.svws_nrw.module.reporting.proxytypes.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.faecher.DataFachdaten;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.module.reporting.proxytypes.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.klasse.ProxyReportingKlasse;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt und erweitert die Klasse {@link ReportingSchuljahresabschnitt}.
 */
public class ProxyReportingSchuljahresabschnitt extends ReportingSchuljahresabschnitt {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuljahresabschnitt}.
	 *
	 * @param reportingRepository 	Repository für das Reporting.
	 * @param schuljahresabschnitt	Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingRepository reportingRepository, final Schuljahresabschnitt schuljahresabschnitt) {
		super(schuljahresabschnitt.id, schuljahresabschnitt.schuljahr, schuljahresabschnitt.abschnitt, schuljahresabschnitt.idFolgeAbschnitt,
				schuljahresabschnitt.idVorigerAbschnitt, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		this.reportingRepository = reportingRepository;

		super.folgenderAbschnitt =
				(schuljahresabschnitt.idFolgeAbschnitt != null) ? this.reportingRepository.schuljahresabschnitt(schuljahresabschnitt.idFolgeAbschnitt) : null;
		super.vorherigerAbschnitt =
				(schuljahresabschnitt.idVorigerAbschnitt != null) ? this.reportingRepository.schuljahresabschnitt(schuljahresabschnitt.idVorigerAbschnitt)
						: null;

		ersetzeStringNullDurchEmpty(this, false);

		// Die weiteren Daten werden später durch lazy-loading ergänzt
	}


	/**
	 * Gibt die Liste der Fächer dieses Schuljahresabschnitts zurück.
	 *
	 * @return Liste der Fächer, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public List<ReportingFach> faecher() {
		if ((super.faecher == null) || super.faecher.isEmpty()) {
			super.faecher = new ArrayList<>();
			final List<FachDaten> faecherdaten = new ArrayList<>();

			try {
				faecherdaten.addAll(new DataFachdaten(this.reportingRepository.conn())
						.getMapFachdatenFromDTOFachList(this.reportingRepository.mapFaecher().values().stream().toList()).values().stream().toList());
			} catch (final Exception e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Fächerliste für den Schuljahresabschnitt %s.".formatted(this.textSchuljahresabschnittKurz()), e,
						reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (faecherdaten.isEmpty())
				return super.faecher;

			final Map<Long, DTOFach> mapfaecherFuerFaecherdatenGost =
					this.reportingRepository.mapFaecher().values().stream().collect(Collectors.toMap(f -> f.ID, f -> f));
			final Map<Long, GostFach> mapFaecherdatenGost = mapfaecherFuerFaecherdatenGost.values().stream()
					.collect(Collectors.toMap(f -> f.ID, f -> DBUtilsFaecherGost.mapFromDTOFach(this.schuljahr, f, mapfaecherFuerFaecherdatenGost)));
			for (final FachDaten fach : faecherdaten)
				super.faecher.add(new ProxyReportingFach(fach, mapFaecherdatenGost.get(fach.id), this.schuljahr));
		}
		return super.faecher;
	}

	/**
	 * Gibt die Liste der Jahrgänge dieses Schuljahresabschnitts zurück.
	 *
	 * @return Liste der Jahrgänge, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public List<ReportingJahrgang> jahrgaenge() {
		if ((super.jahrgaenge == null) || super.jahrgaenge.isEmpty()) {
			super.jahrgaenge = new ArrayList<>();
			// TODO: Wenn die Jahrgänge eine Gültigkeit erhalten, dann ist diese hier auch zu implementieren. Aktuell werden in alle Schuljahresabschnitte alle
			//  Jahrgänge übernommen.
			for (final JahrgangsDaten jahrgang : this.reportingRepository.mapJahrgaenge().values().stream().toList())
				super.jahrgaenge.add(new ProxyReportingJahrgang(this.reportingRepository, jahrgang, this));
		}
		return super.jahrgaenge;
	}

	/**
	 * Gibt die Liste der Klassen dieses Schuljahresabschnitts zurück.
	 *
	 * @return Liste der Klassen, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public List<ReportingKlasse> klassen() {
		if ((super.klassen == null) || super.klassen.isEmpty()) {
			super.klassen = new ArrayList<>();
			List<KlassenDaten> klassendaten = new ArrayList<>();
			try {
				this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Klassendaten.");
				klassendaten = new DataKlassendaten(this.reportingRepository.conn()).getListBySchuljahresabschnittID(this.id(), true);
			} catch (final Exception e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Klassenliste für den Schuljahresabschnitt %s.".formatted(this.textSchuljahresabschnittKurz()), e,
						reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (klassendaten.isEmpty())
				return super.klassen;

			for (final KlassenDaten klasse : klassendaten) {
				final ReportingKlasse reportingKlasse = new ProxyReportingKlasse(this.reportingRepository, klasse);
				super.klassen.add(reportingKlasse);
				this.reportingRepository.mapKlassen().putIfAbsent(reportingKlasse.id(), reportingKlasse);
			}

		}
		return super.klassen;
	}

	/**
	 * Gibt die Liste der Kurse dieses Schuljahresabschnitts zurück.
	 *
	 * @return Liste der Kurse, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public List<ReportingKurs> kurse() {
		if ((super.kurse == null) || super.kurse.isEmpty()) {
			super.kurse = new ArrayList<>();
			List<KursDaten> kurseDaten = new ArrayList<>();
			try {
				this.reportingRepository.logger().logLn(LogLevel.DEBUG, 8, "Ermittle die Kursdaten.");
				kurseDaten = new DataKurse(this.reportingRepository.conn()).getListBySchuljahresabschnittID(this.id(), true);
			} catch (final Exception e) {
				ReportingExceptionUtils.putStacktraceInLog(
						"FEHLER: Fehler bei der Erstellung der Liste der Kurse für den Schuljahresabschnitt %s.".formatted(this.textSchuljahresabschnittKurz()),
						e, reportingRepository.logger(), LogLevel.ERROR, 0);
			}
			if (kurseDaten.isEmpty())
				return super.kurse;

			for (final KursDaten kurs : kurseDaten) {
				final ReportingKurs reportingKurs = new ProxyReportingKurs(this.reportingRepository, kurs);
				super.kurse.add(reportingKurs);
				this.reportingRepository.mapKurse().putIfAbsent(reportingKurs.id(), reportingKurs);
			}
		}
		return super.kurse;
	}
}

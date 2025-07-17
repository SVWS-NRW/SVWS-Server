package de.svws_nrw.module.reporting.types.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistikenAbiturjahrgang.
 */
public class ReportingGostFachwahlstatistikenAbiturjahrgang extends ReportingBaseType {

	/**
	 * Ein Comparator zur Sortierung von Fachwahlstatistiken der gymnasialen Oberstufe nach Fach.
	 * Die Sortierung erfolgt zunächst anhand der Gost-Fachsortierung über die Methode
	 * {@link ReportingFach#compareToGost}. Falls die Fachnamen identisch sind, erfolgt eine zusätzliche
	 * Sortierung nach der ID des Halbjahres.
	 */
	private static final Comparator<ReportingGostFachwahlstatistik> comparatorFach =
			(o1, o2) -> ReportingFach.compareToGost(o1.fach(), o2.fach());

	/**
	 * Ein Comparator zur Sortierung von Fachwahlstatistiken der gymnasialen Oberstufe nach Fach und anschließend nach Halbjahr.
	 * Die Sortierung erfolgt zunächst anhand der Gost-Fachsortierung über die Methode
	 * {@link ReportingFach#compareToGost}. Falls die Fachnamen identisch sind, erfolgt eine zusätzliche
	 * Sortierung nach der ID des Halbjahres.
	 */
	private static final Comparator<ReportingGostFachwahlstatistikHalbjahr> comparatorFachGostHalbjahr =
			(o1, o2) -> (ReportingFach.compareToGost(o1.fach(), o2.fach()) == 0)
					? Integer.compare(o1.gostHalbjahr().id, o2.gostHalbjahr().id)
					: ReportingFach.compareToGost(o1.fach(), o2.fach());


	/** Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll. */
	protected int abiturjahr;

	/** Liste mit den Fachwahlstatistiken der gymnasialen Oberstufe im angegebenen Abiturjahr. */
	private final List<ReportingGostFachwahlstatistik> listGostFachwahlstatistiken = new ArrayList<>();

	/** Map mit den Fachwahlstatistiken zur Fach ID der gymnasialen Oberstufe im angegebenen Abiturjahr. */
	private final Map<Long, ReportingGostFachwahlstatistik> mapGostFachwahlstatistiken = new HashMap<>();

	/** Eine ListMap, die zur ID des Faches ID und der des Gost-Halbjahres dessen Fachwahlstatistik enthält. */
	private ListMap2DLongKeys<ReportingGostFachwahlstatistikHalbjahr> listmapGostFachwahlstatistikenHalbjahre = new ListMap2DLongKeys<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll.
	 * @param gostFachwahlstatistiken Eine Liste mit den Fachwahlstatistiken des Faches im angegebenen Abiturjahr.
	 */
	public ReportingGostFachwahlstatistikenAbiturjahrgang(final int abiturjahr,
			final List<ReportingGostFachwahlstatistik> gostFachwahlstatistiken) {
		this.abiturjahr = abiturjahr;

		setFachwahlstatistiken(gostFachwahlstatistiken);
	}




	// ##### Getter #####

	/**
	 * Gibt das Abiturjahr zurück, dem diese Fachwahlstatistik zugeordnet ist.
	 *
	 * @return Das Abiturjahr zur Fachwahlstatistik.
	 */
	public int abiturjahr() {
		return abiturjahr;
	}


	/**
	 * Gibt eine Liste aller enthaltenen Fachwahlstatistiken für alle Halbjahre der gymnasialen Oberstufe zurück.
	 *
	 * @return Eine sortierte Liste aller Fachwahlstatistiken der gymnasialen Oberstufe.
	 */
	public List<ReportingGostFachwahlstatistik> fachwahlstatistiken() {
		return listGostFachwahlstatistiken.stream()
				.sorted(comparatorFach)
				.toList();
	}

	/**
	 * Gibt die Fachwahlstatistiken für die angegebenen Fach-IDs zurück.
	 *
	 * @param idsFach Eine Liste der IDs der Fächer, deren Fachwahlstatistiken ermittelt werden sollen.
	 *
	 * @return Eine Liste mit den Fachwahlstatistiken für die angegebenen Fächer oder eine leere Liste, falls keine Statistiken existieren.
	 */
	public List<ReportingGostFachwahlstatistik> fachwahlstatistikenByIds(final List<Long> idsFach) {
		if ((idsFach == null) || idsFach.isEmpty())
			return new ArrayList<>();

		return idsFach.stream()
				.filter(Objects::nonNull)
				.distinct()
				.map(mapGostFachwahlstatistiken::get)
				.filter(Objects::nonNull)
				.sorted(comparatorFach)
				.toList();
	}

	/**
	 * Gibt die Fachwahlstatistiken für die angegebenen Fächer zurück.
	 *
	 * @param faecher Eine Liste der Fächer, deren Fachwahlstatistiken ermittelt werden sollen.
	 *
	 * @return Eine Liste mit den Fachwahlstatistiken für die angegebenen Fächer oder eine leere Liste, falls keine Statistiken existieren.
	 */
	public List<ReportingGostFachwahlstatistik> fachwahlstatistikenByFaecher(final List<ReportingFach> faecher) {
		if ((faecher == null) || faecher.isEmpty())
			return new ArrayList<>();

		final List<Long> idsFaecher = faecher.stream()
				.filter(Objects::nonNull)
				.map(ReportingFach::id)
				.distinct()
				.toList();

		return fachwahlstatistikenByIds(idsFaecher);
	}


	/**
	 * Gibt eine Liste der Fachwahlstatistiken für die angegebenen Fach-IDs und Halbjahres-IDs
	 * der gymnasialen Oberstufe zurück.
	 *
	 * @param idsFaecher Eine Liste der IDs der Fächer, deren Fachwahlstatistiken ermittelt werden sollen.
	 * @param idsGostHalbjahre Eine Liste der IDs der Halbjahre, deren Fachwahlstatistiken erfasst werden sollen.
	 *
	 * @return Eine sortierte Liste der Fachwahlstatistiken für die angegebenen Fach-IDs und Halbjahres-IDs.
	 */
	public List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikenHalbjahreByIds(final List<Long> idsFaecher, final List<Integer> idsGostHalbjahre) {
		if ((idsFaecher == null) || (idsGostHalbjahre == null) || idsFaecher.isEmpty() || idsGostHalbjahre.isEmpty())
			return new ArrayList<>();

		final List<Long> idsCheckedFeacher = idsFaecher.stream().filter(Objects::nonNull).distinct().toList();
		final List<Integer> idsCheckedHalbjahre = idsGostHalbjahre.stream().filter(Objects::nonNull).distinct().toList();

		if (idsCheckedFeacher.isEmpty() || idsCheckedHalbjahre.isEmpty())
			return new ArrayList<>();

		return listmapGostFachwahlstatistikenHalbjahre.keySet1().stream()
				.filter(idsCheckedFeacher::contains)
				.flatMap(fach -> listmapGostFachwahlstatistikenHalbjahre.get1(fach).stream()
						.filter(statistik -> idsCheckedHalbjahre.contains(statistik.gostHalbjahr().id)))
				.sorted(comparatorFachGostHalbjahr)
				.toList();
	}

	/**
	 * Gibt eine Liste der Fachwahlstatistiken für die angegebenen Fächer und Halbjahre der gymnasialen Oberstufe zurück.
	 *
	 * @param faecher Eine Liste der ausgewählten Fächer, deren Fachwahlstatistiken ermittelt werden sollen.
	 * @param gostHalbjahre Eine Liste der ausgewählten Halbjahre, deren Fachwahlstatistiken erfasst werden sollen.
	 *
	 * @return Eine sortierte Liste der Fachwahlstatistiken für die angegebenen Fächer und Halbjahre.
	 */
	public List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikenHalbjahreByFaecherHalbjahre(final List<ReportingFach> faecher,
			final List<GostHalbjahr> gostHalbjahre) {
		if ((faecher == null) || (gostHalbjahre == null))
			return new ArrayList<>();

		final List<Long> idsFaecher = faecher.stream().filter(Objects::nonNull).distinct().map(ReportingFach::id).toList();
		final List<Integer> idsHalbjahre = gostHalbjahre.stream().filter(Objects::nonNull).distinct().map(halbjahr -> halbjahr.id).toList();

		if (idsFaecher.isEmpty() || idsHalbjahre.isEmpty())
			return new ArrayList<>();

		return fachwahlstatistikenHalbjahreByIds(idsFaecher, idsHalbjahre);
	}



	// ##### Setter #####

	/**
	 * Setzt die Fachwahlstatistiken für die Halbjahre der gymnasialen Oberstufe.
	 *
	 * @param gostFachwahlstatistiken Eine Liste mit den Halbjahresstatistiken im angegebenen Abiturjahr.
	 */
	public void setFachwahlstatistiken(final List<ReportingGostFachwahlstatistik> gostFachwahlstatistiken) {
		if (gostFachwahlstatistiken == null)
			return;

		listGostFachwahlstatistiken.clear();
		mapGostFachwahlstatistiken.clear();
		listmapGostFachwahlstatistikenHalbjahre = new ListMap2DLongKeys<>();

		final List<ReportingGostFachwahlstatistik> tempGostFachwahlstatistiken =
				gostFachwahlstatistiken.stream().filter(Objects::nonNull).distinct()
						.filter(fs -> (fs.abiturjahr == this.abiturjahr))
						.filter(fs -> fs.fach().istGostFach())
						.toList();

		tempGostFachwahlstatistiken.forEach(fs -> mapGostFachwahlstatistiken.put(fs.fach.id(), fs));
		mapGostFachwahlstatistiken.forEach((fachId, fs) -> listGostFachwahlstatistiken.add(fs));
		listGostFachwahlstatistiken.sort(comparatorFach);

		for (final ReportingGostFachwahlstatistik fs : listGostFachwahlstatistiken) {
			for (final ReportingGostFachwahlstatistikHalbjahr fshj : fs.fachwahlstatistik()) {
				listmapGostFachwahlstatistikenHalbjahre.add(fshj.fach().id(), fshj.gostHalbjahr().id, fshj);
			}
		}
	}

}

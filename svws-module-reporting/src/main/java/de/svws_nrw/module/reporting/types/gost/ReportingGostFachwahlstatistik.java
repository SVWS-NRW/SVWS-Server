package de.svws_nrw.module.reporting.types.gost;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostFachwahlstatistik.
 */
public class ReportingGostFachwahlstatistik extends ReportingBaseType {

	/** Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll. */
	protected int abiturjahr;

	/** Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden. */
	protected ReportingFach fach;

	/** Eine Liste, die die Fachwahlstatistiken der GOSt-Halbjahre enthält. */
	private final List<ReportingGostFachwahlstatistikHalbjahr> listGostFachwahlstatistikHalbjahre = new ArrayList<>();

	/** Eine Map, die zur ID des Gost-Halbjahres dessen Fachwahlstatistik enthält. */
	private final Map<Integer, ReportingGostFachwahlstatistikHalbjahr> mapGostFachwahlstatistikHalbjahre = new HashMap<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr Der Abiturjahrgang in Form des Abiturjahres, dessen Fachwahlstatistik betrachtet werden soll.
	 * @param fach Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 * @param gostFachwahlstatistikenHalbjahre Eine Liste mit den Halbjahresstatistiken des Faches im angegebenen Abiturjahr.
	 */
	public ReportingGostFachwahlstatistik(final int abiturjahr, final ReportingFach fach,
			final List<ReportingGostFachwahlstatistikHalbjahr> gostFachwahlstatistikenHalbjahre) {
		this.abiturjahr = abiturjahr;
		this.fach = fach;

		setFachwahlstatistikHalbjahre(gostFachwahlstatistikenHalbjahre);
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
	 * Das Fach, für welches die Statistikdaten der Fachwahlen ermittelt wurden.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Gibt eine Liste der Fachwahlstatistiken für die Halbjahre der gymnasialen Oberstufe zurück.
	 *
	 * @return Eine sortierte Liste der Halbjahreseinträge der Fachwahlstatistik.
	 */
	public List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistik() {
		return List.copyOf(listGostFachwahlstatistikHalbjahre);
	}

	/**
	 * Gibt die Fachwahlstatistik für die EF.1 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der EF.1 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr ef1() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.EF1.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für die EF.2 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der EF.2 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr ef2() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.EF2.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für die Q1.1 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der Q1.1 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr q11() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.Q11.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für die Q1.2 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der Q1.2 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr q12() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.Q12.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für die Q2.1 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der Q2.1 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr q21() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.Q21.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für die Q2.2 der gymnasialen Oberstufe zurück.
	 *
	 * @return Die Fachwahlstatistik der Q2.2 oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr q22() {
		return mapGostFachwahlstatistikHalbjahre.get(GostHalbjahr.Q22.id);
	}

	/**
	 * Gibt die Fachwahlstatistik für das angegebene Halbjahr der gymnasialen Oberstufe zurück.
	 *
	 * @param idGostHalbjahr Die ID des Halbjahres, für welches die Statistik zurückgegeben werden soll.
	 *
	 * @return Die Fachwahlstatistik des angegebenen Halbjahres oder null, falls keine Statistik existiert.
	 */
	public ReportingGostFachwahlstatistikHalbjahr fachwahlstatistikByHalbjahrId(final int idGostHalbjahr) {
		return mapGostFachwahlstatistikHalbjahre.get(idGostHalbjahr);
	}

	/**
	 * Gibt eine Liste der Fachwahlstatistiken für die angegebenen F Halbjahres-IDs der gymnasialen Oberstufe zurück.
	 *
	 * @param idsGostHalbjahre Eine Liste der IDs der Halbjahre, die erfasst werden sollen.
	 *
	 * @return Eine sortierte Liste der Fachwahlstatistiken für die angegebenen Halbjahres-IDs.
	 */
	public List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikByHalbjahrenIds(final List<Integer> idsGostHalbjahre) {
		if ((idsGostHalbjahre == null) || idsGostHalbjahre.isEmpty())
			return new ArrayList<>();

		final List<Integer> idsCheckedHalbjahre = idsGostHalbjahre.stream().filter(Objects::nonNull).distinct().toList();

		return listGostFachwahlstatistikHalbjahre.stream().filter(fs -> idsCheckedHalbjahre.contains(fs.gostHalbjahr().id)).toList();
	}

	/**
	 * Gibt eine Liste der Fachwahlstatistiken für die angegebenen Halbjahre der gymnasialen Oberstufe zurück.
	 *
	 * @param gostHalbjahre Eine Liste der ausgewählten Halbjahre, die erfasst werden sollen.
	 *
	 * @return Eine sortierte Liste der Fachwahlstatistiken für die angegebenen Halbjahre.
	 */
	public List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikByHalbjahren(final List<GostHalbjahr> gostHalbjahre) {
		if ((gostHalbjahre == null) || gostHalbjahre.isEmpty())
			return new ArrayList<>();

		final List<Integer> idsHalbjahre = gostHalbjahre.stream().filter(Objects::nonNull).distinct().map(halbjahr -> halbjahr.id).toList();
		return fachwahlstatistikByHalbjahrenIds(idsHalbjahre);
	}



	// ##### Setter #####

	/**
	 * Erzeugt die Einträge der Fachwahlstatistik aus den Werten der einzelnen übergebenen Halbjahre der gymnasialen Oberstufe.
	 *
	 * @param fachwahlstatistikHalbjahre Eine Liste mit den Halbjahresstatistiken.
	 */
	public void setFachwahlstatistikHalbjahre(final List<ReportingGostFachwahlstatistikHalbjahr> fachwahlstatistikHalbjahre) {
		if ((fachwahlstatistikHalbjahre == null) || fachwahlstatistikHalbjahre.isEmpty())
			return;
		listGostFachwahlstatistikHalbjahre.clear();
		mapGostFachwahlstatistikHalbjahre.clear();

		fachwahlstatistikHalbjahre.stream().filter(Objects::nonNull)
				.filter(fs -> fs.abiturjahr == this.abiturjahr)
				.filter(fs -> fs.fach() == this.fach)
				.toList()
				.forEach(fs -> mapGostFachwahlstatistikHalbjahre.put(fs.gostHalbjahr.id, fs));

		listGostFachwahlstatistikHalbjahre.addAll(mapGostFachwahlstatistikHalbjahre.values().stream()
				.sorted(Comparator.comparing(fs -> fs.gostHalbjahr().id))
				.toList());
	}

}

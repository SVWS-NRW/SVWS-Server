package de.svws_nrw.core.utils.gost.klausuren;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.asd.adt.PairNN;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.adt.map.HashMap5D;
import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.adt.map.ListMap5DLongKeys;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenRaumdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenPatchResponseData;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausuren.GostKursklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurtermin;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurterminRich;
import de.svws_nrw.core.data.gost.klausuren.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanKalenderwochenzuordnung;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.KursManager;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten der Gost-Klausurplanung.
 * Es können Daten mehrerer Abiturjahrgänge und Gost-Halbjahre verwaltet werden.
 */
@SuppressWarnings({ "unused", "java:S100", "java:S1192", "java:S117" })
public class GostKlausurplanManager {

	/** Sentinel für technische Map-Keys, wenn eine optionale ID fachlich <code>null</code> ist. */
	private static final long _ID_OHNE_ZUORDNUNG = -1L;

	// externe Manager, klausurplanfremde Daten
	private final @NotNull Map<Integer, GostFaecherManager> _faechermanager_by_abijahr = new HashMap<>();
	private final @NotNull KursManager _kursManager = new KursManager();
	private final @NotNull HashMap2D<Long, String, StundenplanManager> _stundenplanmanager_by_schuljahresabschnitt_and_datum = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Integer, StundenplanManager> _stundenplanmanager_by_schuljahresabschnitt_and_kw = new HashMap2D<>();
	private final @NotNull Map<Long, List<StundenplanManager>> _stundenplanmanagermenge_by_schuljahresabschnitt = new HashMap<>();
	private final @NotNull Map<Long, LehrerListeEintrag> _lehrerMap = new HashMap<>();
	private final @NotNull Map<Long, SchuelerListeEintrag> _schuelerlisteeintrag_by_id = new HashMap<>();
	private final @NotNull HashMap2D<Integer, Integer, Long> _schuljahresabschnitt_by_abijahr_and_halbjahr = new HashMap2D<>();

	// Status des Managers
	private boolean _vorgabenInitialized = false;
	private boolean _klausurenInitialized = false;
	private final @NotNull Set<Long> _terminidmenge_manager_enthaelt_raumdaten = new HashSet<>();
	private final @NotNull HashMap2D<Integer, Integer, Boolean> _klausurdatenEnthalten = new HashMap2D<>();
	private final @NotNull HashMap2D<Integer, Integer, Boolean> _fehlenddatenEnthalten = new HashMap2D<>();

	// Comparators
	private final @NotNull Comparator<GostKlausurvorgabe> _compVorgabe =
			(final @NotNull GostKlausurvorgabe a, final @NotNull GostKlausurvorgabe b) -> {
				if (a.kursart.compareTo(b.kursart) < 0) {
					return +1;
				}
				if (a.kursart.compareTo(b.kursart) > 0) {
					return -1;
				}
				if (a.abiturjahrgang != b.abiturjahrgang) {
					return Integer.compare(a.abiturjahrgang, b.abiturjahrgang);
				}
				final GostFaecherManager faechermanager = getFaecherManagerOrNull(a.abiturjahrgang);
				if (faechermanager != null) {
					final GostFach aFach = faechermanager.get(a.idFach);
					final GostFach bFach = faechermanager.get(b.idFach);
					if ((aFach != null) && (bFach != null)) {
						if (aFach.sortierung > bFach.sortierung) {
							return +1;
						}
						if (aFach.sortierung < bFach.sortierung) {
							return -1;
						}
					}
				}
				if (a.halbjahr != b.halbjahr) {
					return Integer.compare(a.halbjahr, b.halbjahr);
				}
				if (a.quartal != b.quartal) {
					return Integer.compare(a.quartal, b.quartal);
				}
				return Long.compare(a.id, b.id);
			};

	private static final @NotNull Comparator<GostKlausurtermin> _compTermin = (final @NotNull GostKlausurtermin a,
			final @NotNull GostKlausurtermin b) -> {
		if ((a.datum != null) && (b.datum != null)) {
			return a.datum.compareTo(b.datum);
		}
		if (b.datum != null) {
			return +1;
		}
		if (a.datum != null) {
			return -1;
		}
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostKursklausur> _compKursklausur = (final @NotNull GostKursklausur a,
			final @NotNull GostKursklausur b) -> {
		final @NotNull GostKlausurvorgabe va = vorgabeByKursklausur(a);
		final @NotNull GostKlausurvorgabe vb = vorgabeByKursklausur(b);
		if (va.kursart.compareTo(vb.kursart) < 0) {
			return +1;
		}
		if (va.kursart.compareTo(vb.kursart) > 0) {
			return -1;
		}
		if (va.abiturjahrgang != vb.abiturjahrgang) {
			return Integer.compare(va.abiturjahrgang, vb.abiturjahrgang);
		}
		final GostFaecherManager faechermanager = getFaecherManagerOrNull(va.abiturjahrgang);
		if (faechermanager != null) {
			final GostFach aFach = faechermanager.get(va.idFach);
			final GostFach bFach = faechermanager.get(vb.idFach);
			if ((aFach != null) && (bFach != null)) {
				if (aFach.sortierung > bFach.sortierung) {
					return +1;
				}
				if (aFach.sortierung < bFach.sortierung) {
					return -1;
				}
			}
		}
		if (va.halbjahr != vb.halbjahr) {
			return va.halbjahr - vb.halbjahr;
		}
		if (va.quartal != vb.quartal) {
			return va.quartal - vb.quartal;
		}
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostSchuelerklausur> _compSchuelerklausur = (final @NotNull GostSchuelerklausur a,
			final @NotNull GostSchuelerklausur b) -> {
		final GostKlausurvorgabe aV = vorgabeBySchuelerklausur(a);
		final GostKlausurvorgabe bV = vorgabeBySchuelerklausur(b);
		if (aV.quartal != bV.quartal) {
			return aV.quartal - bV.quartal;
		}
		if (aV.kursart.compareTo(bV.kursart) < 0) {
			return +1;
		}
		if (aV.kursart.compareTo(bV.kursart) > 0) {
			return -1;
		}
		if (aV.abiturjahrgang != bV.abiturjahrgang) {
			return Integer.compare(aV.abiturjahrgang, bV.abiturjahrgang);
		}
		final GostFaecherManager faechermanager = getFaecherManagerOrNull(aV.abiturjahrgang);
		if (faechermanager != null) {
			final GostFach aFach = faechermanager.get(aV.idFach);
			final GostFach bFach = faechermanager.get(bV.idFach);
			if ((aFach != null) && (bFach != null)) {
				if (aFach.sortierung > bFach.sortierung) {
					return +1;
				}
				if (aFach.sortierung < bFach.sortierung) {
					return -1;
				}
			}
		}
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostSchuelerklausur> _compSchuelerklausurByDatumHT =
			(final @NotNull GostSchuelerklausur a, final @NotNull GostSchuelerklausur b) -> {
				final GostKlausurvorgabe aV = vorgabeBySchuelerklausur(a);
				final GostKlausurvorgabe bV = vorgabeBySchuelerklausur(b);
				final int quartalComp = Integer.compare(aV.quartal, bV.quartal);
				if (quartalComp != 0) {
					return quartalComp;
				}
				final String aDatum = datumSchuelerklausurHT(a);
				final String bDatum = datumSchuelerklausurHT(b);
				if ((aDatum == null) && (bDatum != null)) {
					return 1;
				}
				if ((aDatum != null) && (bDatum == null)) {
					return -1;
				}
				if (aDatum != null) {
					final int datumComp = aDatum.compareTo(bDatum);
					if (datumComp != 0) {
						return datumComp;
					}
				}
				return _compSchuelerklausur.compare(a, b);
			};

	private final @NotNull Comparator<GostSchuelerklausurtermin> _compSchuelerklausurtermin = (
			final @NotNull GostSchuelerklausurtermin a, final @NotNull GostSchuelerklausurtermin b) -> {
		if ((a == b) || (a.id == b.id)) {
			return 0;
		}
		// Zuerst prüfen, ob es sich um die gleiche Schülerklausur handelt
		if (a.idSchuelerklausur != b.idSchuelerklausur) {
			final @NotNull GostSchuelerklausur kA = schuelerklausurBySchuelerklausurtermin(a);
			final @NotNull GostSchuelerklausur kB = schuelerklausurBySchuelerklausurtermin(b);

			// Wenn _schuelerMap existiert und unterschiedliche Schüler, nach Namen sortieren
			if (kA.idSchueler != kB.idSchueler) {
				final SchuelerListeEintrag sA = _schuelerlisteeintrag_by_id.get(kA.idSchueler);
				final SchuelerListeEintrag sB = _schuelerlisteeintrag_by_id.get(kB.idSchueler);

				if ((sA != null) && (sB != null)) {
					final int nameComparison = (sA.nachname + "," + sA.vorname).compareTo(sB.nachname + "," + sB.vorname);
					if (nameComparison != 0) {
						return nameComparison;
					}
				} else if (((sA != null) && (sB == null)) || ((sA == null) && (sB != null))) {
					throw new DeveloperNotificationException("Schüler nicht gefunden: " + kA.idSchueler + " oder " + kB.idSchueler);
				}
			}
		}

		// Wenn es sich um die gleiche Schülerklausur handelt, nach FolgeNr sortieren
		if (a.idSchuelerklausur == b.idSchuelerklausur) {
			final int folgeNrComparison = Integer.compare(a.folgeNr, b.folgeNr);
			if (folgeNrComparison != 0) {
				return folgeNrComparison;
			}
		}

		// Als letzte Instanz nach der ID des Termins sortieren
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<SchuelerListeEintrag> _compSchuelerListeEintrag =
			(final @NotNull SchuelerListeEintrag a, final @NotNull SchuelerListeEintrag b) -> {
				final int compareNachname = a.nachname.compareToIgnoreCase(b.nachname);
				if (compareNachname != 0) {
					return compareNachname;
				}
				final int compareVorname = a.vorname.compareToIgnoreCase(b.vorname);
				if (compareVorname != 0) {
					return compareVorname;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> _compSchuelerWochenkonflikt =
			(final @NotNull PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>> a,
					final @NotNull PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>> b) -> _compSchuelerListeEintrag.compare(a.a, b.a);

	private final @NotNull Comparator<PairNN<GostKursklausur, List<SchuelerListeEintrag>>> _compKursklausurKonflikt =
			(final @NotNull PairNN<GostKursklausur, List<SchuelerListeEintrag>> a,
					final @NotNull PairNN<GostKursklausur, List<SchuelerListeEintrag>> b) -> _compKursklausur.compare(a.a, b.a);

	private final @NotNull Comparator<PairNN<PairNN<Integer, SchuelerListeEintrag>, List<GostSchuelerklausurtermin>>> _compKwSchuelerWochenkonflikt =
			(final @NotNull PairNN<PairNN<Integer, SchuelerListeEintrag>, List<GostSchuelerklausurtermin>> a,
					final @NotNull PairNN<PairNN<Integer, SchuelerListeEintrag>, List<GostSchuelerklausurtermin>> b) -> {
				final int compareSchueler = _compSchuelerListeEintrag.compare(a.a.b, b.a.b);
				if (compareSchueler != 0) {
					return compareSchueler;
				}
				return Integer.compare(a.a.a, b.a.a);
			};

	private final @NotNull Comparator<GostSchuelerklausurtermin> _compSchuelerklausurterminWochenkonflikt =
			(final @NotNull GostSchuelerklausurtermin a, final @NotNull GostSchuelerklausurtermin b) -> {
				final String datumA = datumSchuelerklausurterminOrNull(a);
				final String datumB = datumSchuelerklausurterminOrNull(b);
				if ((datumA != null) && (datumB != null)) {
					final int compareDatum = datumA.compareTo(datumB);
					if (compareDatum != 0) {
						return compareDatum;
					}
				} else if (datumA != null) {
					return -1;
				} else if (datumB != null) {
					return 1;
				}
				return _compSchuelerklausurtermin.compare(a, b);
			};

	private static final @NotNull Comparator<StundenplanManager> _compStundenplanManager = (final @NotNull StundenplanManager a,
			final @NotNull StundenplanManager b) -> a.getGueltigAb().compareTo(b.getGueltigAb());

	private static final @NotNull Comparator<GostKlausurraum> _compRaum =
			(final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> Long.compare(a.id, b.id);

	// Fehlende Elemente
	private final @NotNull List<GostKlausurvorgabe> _vorgabenfehlendmenge = new ArrayList<>();
	private final @NotNull HashMap5D<Integer, Integer, Integer, String, Long, GostKlausurvorgabe> _vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach =
			new HashMap5D<>();
	private final @NotNull List<GostKursklausur> _kursklausurfehlendmenge = new ArrayList<>();
	private final @NotNull HashMap5D<Integer, Integer, Integer, Long, Long, GostKursklausur> _kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs =
			new HashMap5D<>();
	private final @NotNull List<GostSchuelerklausur> _schuelerklausurfehlendmenge = new ArrayList<>();
	private final @NotNull HashMap5D<Integer, Integer, Integer, Long, Long, GostSchuelerklausur> _schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur =
			new HashMap5D<>();

	// GostKlausurvorgabe
	private final @NotNull Map<Long, GostKlausurvorgabe> _vorgabe_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurvorgabe> _vorgabenmenge = new ArrayList<>();
	private @NotNull ListMap5DLongKeys<GostKlausurvorgabe> _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach =
			new ListMap5DLongKeys<>();

	// GostKursklausur
	private final @NotNull Map<Long, GostKursklausur> _kursklausur_by_id = new HashMap<>();
	private final @NotNull List<GostKursklausur> _kursklausurmenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<GostKursklausur> _kursklausur_by_idVorgabe_and_idKurs = new ListMap2DLongKeys<>();
	private @NotNull ListMap4DLongKeys<GostKursklausur> _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal =
			new ListMap4DLongKeys<>();

	// GostKlausurtermin
	private final @NotNull Map<Long, GostKlausurtermin> _termin_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurtermin> _terminmenge = new ArrayList<>();
	private @NotNull ListMap3DLongKeys<GostKlausurtermin> _terminmenge_by_abijahr_and_halbjahr_and_quartal = new ListMap3DLongKeys<>();
	private @NotNull ListMap3DLongKeys<GostKlausurtermin> _terminmenge_by_jahr_and_kw_and_abijahr = new ListMap3DLongKeys<>();
	private @NotNull ListMap2DLongKeys<GostKlausurtermin> _terminmenge_by_datum_and_abijahr = new ListMap2DLongKeys<>();

	// GostSchuelerklausur
	private final @NotNull Map<Long, GostSchuelerklausur> _schuelerklausur_by_id = new HashMap<>();
	private final @NotNull List<GostSchuelerklausur> _schuelerklausurmenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<GostSchuelerklausur> _schuelerklausur_by_idKursklausur_and_idSchueler = new ListMap2DLongKeys<>();
	private final @NotNull HashMap2D<Integer, Long, List<GostSchuelerklausur>> _schuelerklausurmenge_by_abijahr_and_idSchueler = new HashMap2D<>();

	// GostSchuelerklausurtermin
	private final @NotNull Map<Long, GostSchuelerklausurtermin> _schuelerklausurtermin_by_id = new HashMap<>();
	private final @NotNull List<GostSchuelerklausurtermin> _schuelerklausurterminmenge = new ArrayList<>();
	private final @NotNull List<GostSchuelerklausurtermin> _schuelerklausurterminaktuellmenge = new ArrayList<>();
	private final @NotNull Map<Long, GostSchuelerklausurtermin> _schuelerklausurterminaktuell_by_idSchuelerklausur = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurtermin>> _schuelerklausurterminmenge_by_idSchuelerklausur = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurtermin>> _schuelerklausurterminmenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurtermin>> _schuelerklausurterminmenge_by_idKursklausur = new HashMap<>();
	private @NotNull ListMap2DLongKeys<GostSchuelerklausurtermin> _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur =
			new ListMap2DLongKeys<>();
	private @NotNull ListMap4DLongKeys<GostSchuelerklausurtermin> _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin =
			new ListMap4DLongKeys<>();
	private @NotNull ListMap2DLongKeys<GostSchuelerklausurtermin> _schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin = new ListMap2DLongKeys<>();
	private @NotNull ListMap2DLongKeys<GostSchuelerklausurtermin> _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur =
			new ListMap2DLongKeys<>();
	private final @NotNull HashMap3D<Integer, Integer, Long, List<GostSchuelerklausurtermin>> _schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId =
			new HashMap3D<>();

	// GostKlausurraum
	private final @NotNull Map<Long, GostKlausurraum> _raum_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraum> _raummenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraum>> _raummenge_by_idTermin = new HashMap<>();
	private @NotNull ListMap2DLongKeys<GostKlausurraum> _raum_by_idTermin_and_idStundenplanraum = new ListMap2DLongKeys<>();
	private final @NotNull Map<Long, GostKlausurraum> _klausurraum_by_idSchuelerklausurtermin = new HashMap<>();
	private @NotNull ListMap2DLongKeys<GostKlausurraum> _raummenge_by_idTermin_and_idKursklausur = new ListMap2DLongKeys<>();

	// GostKlausurraumstunde
	private final @NotNull Map<Long, GostKlausurraumstunde> _raumstunde_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraumstunde> _raumstundenmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idRaum = new HashMap<>();
	private @NotNull ListMap2DLongKeys<GostKlausurraumstunde> _raumstunde_by_idRaum_and_idZeitraster = new ListMap2DLongKeys<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idSchuelerklausurtermin = new HashMap<>();

	// GostSchuelerklausurraumstunde
	private final @NotNull ListMap2DLongKeys<GostSchuelerklausurterminraumstunde> _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde =
			new ListMap2DLongKeys<>();
	private final @NotNull List<GostSchuelerklausurterminraumstunde> _schuelerklausurterminraumstundenmenge = new ArrayList<>();

	// SchuelerListeEintrag
	private final @NotNull Map<Integer, List<SchuelerListeEintrag>> _schuelermenge_by_abijahr = new HashMap<>();

	/**
	 * Erstellt einen leeren Manager.
	 */
	public GostKlausurplanManager() {
		super();
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n
	 *
	 * @param listVorgaben die Liste der {@link GostKlausurvorgabe}n
	 */
	public GostKlausurplanManager(final @NotNull Collection<GostKlausurvorgabe> listVorgaben) {
		vorgabeAddAll(listVorgaben);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n und dem übergebenen {@link GostFaecherManager}, der für den Vorlagen-Jahrgang (ID = -1) gilt
	 *
	 * @param faecherManagerVorgaben der GostFaecherManager, der für den Vorlagen-Jahrgang gilt
	 * @param listVorgaben 	die Liste der GostKlausurvorgaben
	 */
	public GostKlausurplanManager(final GostFaecherManager faecherManagerVorgaben, final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		_faechermanager_by_abijahr.put(-1, faecherManagerVorgaben);
		vorgabeAddAll(listVorgaben);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n, {@link GostKursklausur}en, {@link GostKlausurtermin}en,
	 * {@link GostSchuelerklausur}en und {@link GostSchuelerklausurtermin}en
	 *
	 * @param listVorgaben 			die Liste der {@link GostKlausurvorgabe}n
	 * @param listKlausuren         die Liste der {@link GostKursklausur}en
	 * @param listTermine           die Liste der {@link GostKlausurtermin}e
	 * @param listSchuelerklausuren die Liste der {@link GostSchuelerklausur}en
	 * @param listSchuelerklausurtermine die Liste der {@link GostSchuelerklausurtermin}e
	 */
	public GostKlausurplanManager(final @NotNull Collection<GostKlausurvorgabe> listVorgaben, final @NotNull Collection<GostKursklausur> listKlausuren,
			final @NotNull Collection<GostKlausurtermin> listTermine,
			final @NotNull Collection<GostSchuelerklausur> listSchuelerklausuren,
			final @NotNull Collection<GostSchuelerklausurtermin> listSchuelerklausurtermine) {
		addKlausurDataListenOhneUpdate(listVorgaben, listKlausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
		update_all();
	}

	/**
	 * Erstellt einen neuen Manager mit den übergebenen {@link GostKlausurenAlleKlausurdaten} enthaltenen Daten
	 *
	 * @param allData            das {@link GostKlausurenAlleKlausurdaten}-Objekt, das alle Informationen enthält
	 */
	public GostKlausurplanManager(final @NotNull GostKlausurenAlleKlausurdaten allData) {
		addAllData(allData);
	}

	/**
	 * Erstellt einen neuen Manager mit den übergebenen {@link GostKlausurenKlausurdaten} enthaltenen Daten
	 *
	 * @param data            das {@link GostKlausurenKlausurdaten}-Objekt, das alle Informationen enthält
	 */
	public GostKlausurplanManager(final @NotNull GostKlausurenKlausurdaten data) {
		addKlausurData(data);
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenAlleKlausurdaten}-Objekt enthaltenen Daten hinzu
	 *
	 * @param allData            das {@link GostKlausurenAlleKlausurdaten}-Objekt, das alle Informationen enthält
	 */
	public void addAllData(final @NotNull GostKlausurenAlleKlausurdaten allData) {
		addMetadata(allData);
		addKlausurAllDataOhneUpdate(allData);
		addRaumAllDataOhneUpdate(allData);
		update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenAlleKlausurdaten}-Objekt enthaltenen Klausurdaten hinzu ({@link GostKlausurvorgabe}n, {@link GostKursklausur}en, {@link GostKlausurtermin}e, {@link GostSchuelerklausur}en, {@link GostSchuelerklausurtermin}e)
	 *
	 * @param allData            das {@link GostKlausurenAlleKlausurdaten}-Objekt, das alle Informationen enthält
	 */
	public void addKlausurData(final @NotNull GostKlausurenKlausurdaten allData) {
		addKlausurDataListenOhneUpdate(allData.vorgaben, allData.kursklausuren, allData.termine, allData.schuelerklausuren, allData.schuelerklausurtermine);
		update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenRaumdaten}-Objekt enthaltenen Raumplanungsdaten hinzu
	 *
	 * @param raumData            das {@link GostKlausurenRaumdaten}-Objekt, das Raumplanungsdaten enthält
	 */
	public void addRaumData(final @NotNull GostKlausurenRaumdaten raumData) {
		addRaumDataOhneUpdate(raumData);
		update_all();
	}

	private void addRaumDataOhneUpdate(final @NotNull GostKlausurenRaumdaten data) {
		addRaumDataListenOhneUpdate(data.raeume, data.raumstunden, data.schuelerklausurterminRaumstunden, data.idsKlausurtermine);
	}

	private void addRaumDataListenOhneUpdate(final @NotNull Collection<GostKlausurraum> raeume, final @NotNull Collection<GostKlausurraumstunde> raumstunden,
			final @NotNull Collection<GostSchuelerklausurterminraumstunde> schuelerklausurterminRaumstunden,
			final @NotNull List<Long> idsKlausurtermine) {
		raumAddAllOhneUpdate(raeume);
		raumstundeAddAllOhneUpdate(raumstunden);
		schuelerklausurraumstundeAddAllOhneUpdate(schuelerklausurterminRaumstunden);
		_terminidmenge_manager_enthaelt_raumdaten.addAll(idsKlausurtermine);
	}

	private void addRaumAllDataOhneUpdate(final @NotNull GostKlausurenAlleKlausurdaten allData) {
		final @NotNull Set<GostKlausurraum> raeume = new HashSet<>();
		final @NotNull Set<GostKlausurraumstunde> raumstunden = new HashSet<>();
		final @NotNull Set<GostSchuelerklausurterminraumstunde> schuelerklausurterminRaumstunden = new HashSet<>();
		final @NotNull List<Long> idsKlausurtermine = new ArrayList<>();
		for (final GostKlausurenHalbjahresdaten data : allData.halbjahresdaten) {
			raeume.addAll(data.raumdaten.raeume);
			raumstunden.addAll(data.raumdaten.raumstunden);
			schuelerklausurterminRaumstunden.addAll(data.raumdaten.schuelerklausurterminRaumstunden);
			idsKlausurtermine.addAll(data.raumdaten.idsKlausurtermine);
		}
		addRaumDataListenOhneUpdate(removeDuplicatesFromSet(raeume), removeDuplicatesFromSet(raumstunden),
				schuelerklausurterminRaumstunden, idsKlausurtermine);
	}

	private static <T> @NotNull Set<T> removeDuplicatesFromSet(final @NotNull Set<T> objects) {
		final @NotNull Set<T> unique = new HashSet<>();

		for (final T o : objects) {
			boolean seen = false;
			for (final T o2 : unique) {
				if (o.equals(o2)) {  // Wenn es nicht hinzugefügt werden konnte, ist es ein Duplikat
					seen = true;
					break;
				}
			}
			if (!seen) {
				unique.add(o);
			}
		}
		return unique;
	}

	private void addMetadata(final @NotNull GostKlausurenAlleKlausurdaten meta) {
		final @NotNull List<KursDaten> kurse = new ArrayList<>();
		final @NotNull List<SchuelerListeEintrag> schueler = new ArrayList<>();
		for (final GostKlausurenHalbjahresdaten data : meta.halbjahresdaten) {
			_schuljahresabschnitt_by_abijahr_and_halbjahr.put(data.abiturjahrgang, data.gostHalbjahr, data.idSchuljahresabschnitt);
			if (data.faecher != null) {
				_faechermanager_by_abijahr.put(data.abiturjahrgang, new GostFaecherManager(data.abiturjahrgang, data.faecher));
			}
			if (data.schueler != null) {
				schueler.addAll(data.schueler);
			}
			if (data.kurse != null) {
				kurse.addAll(data.kurse);
			}
		}
		_kursManager.addAll(kurse);
		for (final LehrerListeEintrag lehrer : meta.lehrer) {
			_lehrerMap.put(lehrer.id, lehrer);
		}
		schuelerAddAllOhneUpdate(schueler, true);
	}

	private void addKlausurDataListenOhneUpdate(final @NotNull Collection<GostKlausurvorgabe> listVorgaben,
			final @NotNull Collection<GostKursklausur> listKlausuren,
			final Collection<GostKlausurtermin> listTermine,
			final Collection<GostSchuelerklausur> listSchuelerklausuren,
			final Collection<GostSchuelerklausurtermin> listSchuelerklausurtermine) {
		vorgabeAddAllOhneUpdate(listVorgaben);
		kursklausurAddAllOhneUpdate(listKlausuren);
		if (listTermine != null) {
			terminAddAllOhneUpdate(listTermine);
		}
		if (listSchuelerklausuren != null) {
			schuelerklausurAddAllOhneUpdate(listSchuelerklausuren);
		}
		if (listSchuelerklausurtermine != null) {
			schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		}
	}

	private void addKlausurAllDataOhneUpdate(final @NotNull GostKlausurenAlleKlausurdaten allData) {
		final @NotNull List<GostKlausurvorgabe> listVorgaben = new ArrayList<>();
		final @NotNull List<GostKursklausur> listKlausuren = new ArrayList<>();
		final @NotNull Set<GostKlausurtermin> listTermine = new HashSet<>();
		final @NotNull List<GostSchuelerklausur> listSchuelerklausuren = new ArrayList<>();
		final @NotNull List<GostSchuelerklausurtermin> listSchuelerklausurtermine = new ArrayList<>();
		for (final GostKlausurenHalbjahresdaten data : allData.halbjahresdaten) {
			_klausurdatenEnthalten.put(data.abiturjahrgang, data.gostHalbjahr, true);
			listVorgaben.addAll(data.klausurdaten.vorgaben);
			listKlausuren.addAll(data.klausurdaten.kursklausuren);
			listTermine.addAll(data.klausurdaten.termine);
			listSchuelerklausuren.addAll(data.klausurdaten.schuelerklausuren);
			listSchuelerklausurtermine.addAll(data.klausurdaten.schuelerklausurtermine);
		}
		addKlausurDataListenOhneUpdate(listVorgaben, listKlausuren, removeDuplicatesFromSet(listTermine), listSchuelerklausuren, listSchuelerklausurtermine);
	}

	private void addKlausurDataFehlendOhneUpdate(final @NotNull GostKlausurenHalbjahresdaten fehlendData) {
		vorgabefehlendAddAllOhneUpdate(fehlendData.klausurdaten.vorgaben);
		kursklausurfehlendAddAllOhneUpdate(fehlendData.klausurdaten.kursklausuren);
		schuelerklausurfehlendAddAllOhneUpdate(fehlendData.klausurdaten.schuelerklausuren);
	}

	/**
	 * Setzt die Problemdaten der Klausurplanung für einen bestimmten Abiturjahrgang und ein bestimmtes Halbjahr
	 *
	 * @param fehlendData die {@link GostKlausurenHalbjahresdaten} mit den fehlenden Klausurdaten
	 */
	public void setKlausurDataFehlend(final @NotNull GostKlausurenHalbjahresdaten fehlendData) {
		_vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.removeMap2(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr);
		_kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.removeMap2(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr);
		_schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.removeMap2(fehlendData.abiturjahrgang,
				fehlendData.gostHalbjahr);
		addKlausurDataFehlendOhneUpdate(fehlendData);
		_fehlenddatenEnthalten.put(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr, true);

		update_all();
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurvorgaben enthält.
	 *
	 * @return <code>true</code>, falls der Manager Klausurvorgaben enthält.
	 */
	public boolean isVorgabenInitialized() {
		return _vorgabenInitialized;
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurdaten enthält.
	 *
	 * @return <code>true</code>, falls der Manager Klausurdaten enthält.
	 */
	public boolean isKlausurenInitialized() {
		return _klausurenInitialized;
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Raumplanungsdaten zum übergebenen Termin enthält.
	 *
	 * @param termin der {@link GostKlausurtermin}, für den geprüft werden soll.
	 *
	 * @return <code>true</code>, falls der Manager Raumplanungsdaten zum übergebenen Termin enthält.
	 */
	public boolean hasRaumdataZuTermin(final @NotNull GostKlausurtermin termin) {
		return _terminidmenge_manager_enthaelt_raumdaten.contains(termin.id);
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurdaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 *
	 * @param abiturjahrgang der Abiturjahrgang
	 * @param halbjahr das Halbjahr
	 *
	 * @return <code>true</code>, falls der Manager Klausurdaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 */
	public boolean hasKlausurdatenZuAbijahrUndHalbjahr(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr) {
		return _klausurdatenEnthalten.contains(abiturjahrgang, halbjahr.id);
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Fehlenddaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 *
	 * @param abiturjahrgang der Abiturjahrgang
	 * @param halbjahr das Halbjahr
	 *
	 * @return <code>true</code>, falls der Manager Fehlenddaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 */
	public boolean hasFehlenddatenZuAbijahrUndHalbjahr(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr) {
		return _fehlenddatenEnthalten.contains(abiturjahrgang, halbjahr.id);
	}

	/**
	 * Setzt den {@link GostFaecherManager}
	 *
	 * @param abiturjahrgang der Abiturjahrgang, zu dem der {@link GostFaecherManager} gehört
	 * @param faecherManager der {@link GostFaecherManager}
	 */
	public void setFaecherManager(final int abiturjahrgang, final @NotNull GostFaecherManager faecherManager) {
		_faechermanager_by_abijahr.put(abiturjahrgang, faecherManager);
	}

	/**
	 * Liefert den {@link GostFaecherManager} zum übergebenen Abiturjahr, falls dieser gesetzt ist, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param abiturjahrgang der Abiturjahrgang, zu dem der {@link GostFaecherManager} geliefert werden soll
	 *
	 * @return den {@link GostFaecherManager}
	 */
	public @NotNull GostFaecherManager getFaecherManager(final int abiturjahrgang) {
		return DeveloperNotificationException.ifMapGetIsNull(_faechermanager_by_abijahr, abiturjahrgang);
	}

	/**
	 * Liefert den {@link GostFaecherManager} zum übergebenen Abiturjahr, falls dieser gesetzt ist, sonst <code>null</code>.
	 *
	 * @param abiturjahrgang der Abiturjahrgang, zu dem der {@link GostFaecherManager} geliefert wird
	 *
	 * @return den {@link GostFaecherManager} oder <code>null</code>
	 */
	public GostFaecherManager getFaecherManagerOrNull(final int abiturjahrgang) {
		return _faechermanager_by_abijahr.get(abiturjahrgang);
	}

	/**
	 * Liefert den {@link KursManager}.
	 *
	 * @return den {@link KursManager}
	 */
	public @NotNull KursManager getKursManager() {
		return _kursManager;
	}

	/**
	 * Liefert die Map mit den {@link SchuelerListeEintrag}enn
	 *
	 * @return die Map mit den {@link SchuelerListeEintrag}en
	 */
	public @NotNull Map<Long, SchuelerListeEintrag> getSchuelerMap() {
		return _schuelerlisteeintrag_by_id;
	}

	/**
	 * Prüft, ob zu dem angegebenen Schuljahresabschnitt bereits die StundenplanManager aus der Datenbank geladen wurden.
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn der StundenplanManager bereits geladen wurde, sonst false
	 */
	public boolean stundenplanManagerGeladenByAbschnitt(final long idSchuljahresabschnitt) {
		return _stundenplanmanagermenge_by_schuljahresabschnitt.containsKey(idSchuljahresabschnitt);
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public boolean stundenplanManagerExistsByAbschnitt(final long idSchuljahresabschnitt) {
		if (!stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt)) {
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.");
		}
		final List<StundenplanManager> liste = _stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		return (liste != null) && !liste.isEmpty();
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert.
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public boolean stundenplanManagerGeladenAndExistsByAbschnitt(final long idSchuljahresabschnitt) {
		if (!stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt)) {
			return false;
		}
		final List<StundenplanManager> liste = _stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		return (liste != null) && !liste.isEmpty();
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public boolean stundenplanManagerExistsByAbschnittAndDatum(final long idSchuljahresabschnitt, final @NotNull String datum) {
		if (!stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt)) {
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.");
		}
		return _stundenplanmanager_by_schuljahresabschnitt_and_datum.contains(idSchuljahresabschnitt, datum);
	}

	private static int gibIntkeyJahrUndKwDesDatumsISO8601(final @NotNull String datumISO8601) {
		final int[] split = DateUtils.extractFromDateISO8601(datumISO8601);
		return gibIntkeyJahrUndKw(split[6], split[5]);
	}

	private static int gibIntkeyJahrUndKw(final int jahr, final int kw) {
		return Integer.parseInt(jahr + "" + kw);
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param jahr das Jahr
	 * @param kw die Kalenderwoche
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public boolean stundenplanManagerExistsByAbschnittAndJahrAndKw(final long idSchuljahresabschnitt, final int jahr, final int kw) {
		if (!stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt)) {
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.");
		}
		return _stundenplanmanager_by_schuljahresabschnitt_and_kw.contains(idSchuljahresabschnitt, gibIntkeyJahrUndKw(jahr, kw));
	}

	/**
	 * Setzt die {@link StundenplanManager} für den angegebenen Schuljahresabschnitt
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param stundenplanManagerList die Liste der {@link StundenplanManager}
	 */
	public void stundenplanManagerAddAllBySchuljahresabschnittsid(final long idSchuljahresabschnitt,
			final @NotNull List<StundenplanManager> stundenplanManagerList) {
		for (final @NotNull StundenplanManager stundenplanManager : stundenplanManagerList) {
			if (stundenplanManager.getIDSchuljahresabschnitt() != idSchuljahresabschnitt) {
				throw new DeveloperNotificationException("ID des Schuljahresabschnitts stimmt nicht überein.");
			}
			stundenplanManagerAdd(stundenplanManager);
		}
		if (stundenplanManagerList.isEmpty()) {
			MapUtils.getOrCreateArrayList(_stundenplanmanagermenge_by_schuljahresabschnitt, idSchuljahresabschnitt);
		}
	}

	/**
	 * Setzt den {@link StundenplanManager}
	 *
	 * @param stundenplanManager der {@link StundenplanManager}
	 */
	public void stundenplanManagerAdd(final @NotNull StundenplanManager stundenplanManager) {
		final @NotNull List<StundenplanManager> stundenplanManagerList =
				MapUtils.getOrCreateArrayList(_stundenplanmanagermenge_by_schuljahresabschnitt, stundenplanManager.getIDSchuljahresabschnitt());
		DeveloperNotificationException.ifListAddsDuplicate("_stundenplanmanagermenge_by_schuljahresabschnitt", stundenplanManagerList, stundenplanManager);
		stundenplanManagerList.sort(_compStundenplanManager);
		for (final @NotNull String datum : DateUtils.gibTageAlsDatumZwischen(stundenplanManager.getGueltigAb(), stundenplanManager.getGueltigBis())) {
			if (datum != null) {
				stundenplanManagerAddByAbschnittAndDatum(stundenplanManager.getIDSchuljahresabschnitt(), datum, stundenplanManager);
			}
		}
	}

	/**
	 * Setzt den {@link StundenplanManager} für die übergebenen Parameter
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der Stundenplan gültig ist
	 * @param stundenplanManager der {@link StundenplanManager}
	 */
	public void stundenplanManagerAddByAbschnittAndDatum(final long idSchuljahresabschnitt, final @NotNull String datum,
			final @NotNull StundenplanManager stundenplanManager) {
		DeveloperNotificationException.ifMap2DPutOverwrites(_stundenplanmanager_by_schuljahresabschnitt_and_datum, idSchuljahresabschnitt, datum,
				stundenplanManager);
		final int kw = gibIntkeyJahrUndKwDesDatumsISO8601(datum);
		if (!_stundenplanmanager_by_schuljahresabschnitt_and_kw.contains(idSchuljahresabschnitt, kw)) {
			_stundenplanmanager_by_schuljahresabschnitt_and_kw.put(idSchuljahresabschnitt, kw, stundenplanManager);
		}
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public StundenplanManager stundenplanManagerGetByAbschnittAndDatumOrNull(final long idSchuljahresabschnitt, final @NotNull String datum) {
		return _stundenplanmanager_by_schuljahresabschnitt_and_datum.getOrNull(idSchuljahresabschnitt, datum);
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param jahr das Jahr
	 * @param kw die Kalenderwoche, zu der der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public StundenplanManager stundenplanManagerGetByAbschnittAndKwOrNull(final long idSchuljahresabschnitt, final int jahr, final int kw) {
		return _stundenplanmanager_by_schuljahresabschnitt_and_kw.getOrNull(idSchuljahresabschnitt, gibIntkeyJahrUndKw(jahr, kw));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public @NotNull StundenplanManager stundenplanManagerGetByAbschnittAndDatumOrClosest(final long idSchuljahresabschnitt, final @NotNull String datum) {
		final StundenplanManager exactMatch = stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch != null) {
			return exactMatch;
		}
		final List<StundenplanManager> stundenplanManagerList = _stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if ((stundenplanManagerList == null) || stundenplanManagerList.isEmpty()) {
			throw new DeveloperNotificationException("Kein Stundenplanmanager zu Abschnitt %d gefunden.".formatted(idSchuljahresabschnitt));
		}
		if ((stundenplanManagerList.size() == 1) || (stundenplanManagerList.getFirst().getGueltigAb().compareTo(datum) > 0)) {
			return stundenplanManagerList.getFirst();
		}
		if (stundenplanManagerList.getLast().getGueltigBis().compareTo(datum) < 0) {
			return stundenplanManagerList.getLast();
		}
		for (final @NotNull StundenplanManager manager : stundenplanManagerList) {
			if (manager.getGueltigAb().compareTo(datum) > 0) {
				return manager;
			}
		}
		throw new DeveloperNotificationException("Kein StundenplanManager passend zu Suchkriterien.");
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst eine DeveloperNotificationException.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist. Wird kein gültiger Plan gefunden, soll der Plan geliefert werden, der vor dem Datum gültig war.
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern.
	 */
	public StundenplanManager stundenplanManagerGetByAbschnittAndDatumOrBeforeOrNull(final long idSchuljahresabschnitt, final @NotNull String datum) {
		final StundenplanManager exactMatch = stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch != null) {
			return exactMatch;
		}
		final List<StundenplanManager> stundenplanManagerList = _stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if ((stundenplanManagerList == null) || stundenplanManagerList.isEmpty()) {
			return null;
		}
		if ((stundenplanManagerList.size() == 1) && (stundenplanManagerList.getFirst().getGueltigBis().compareTo(datum) < 0)) {
			return stundenplanManagerList.getFirst();
		}
		StundenplanManager lastManager = null;
		for (final @NotNull StundenplanManager manager : stundenplanManagerList) {
			if (manager.getGueltigAb().compareTo(datum) > 0) {
				return lastManager;
			}
			lastManager = manager;
		}
		return null;
	}

	/**
	 * Liefert eine Liste mit allen {@link StundenplanKalenderwochenzuordnung}-Objekten, die zu dem übergebenen Schuljahresabschnitt gehören.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 *
	 * @return eine Liste mit allen {@link StundenplanKalenderwochenzuordnung}-Objekten, die zu dem übergebenen Schuljahresabschnitt gehören.
	 */
	public @NotNull List<StundenplanKalenderwochenzuordnung> stundenplanManagerKalenderwochenzuordnungenGetMengeByAbschnitt(final long idSchuljahresabschnitt) {
		if (!stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt)) {
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.");
		}
		final @NotNull List<StundenplanKalenderwochenzuordnung> kwzAll = new ArrayList<>();
		for (final @NotNull StundenplanManager manager : DeveloperNotificationException.ifNull(
				"_stundenplanmanagermenge_by_schuljahresabschnitt null für Abschnitt %d".formatted(idSchuljahresabschnitt),
				_stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt))) {
			kwzAll.addAll(manager.kalenderwochenzuordnungGetMengeGueltigeAsList());
		}
		return kwzAll;
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst eine DeveloperNotificationException.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist. Wird kein gültiger Plan gefunden, soll der Plan geliefert werden, der nach dem Datum gültig war.
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public StundenplanManager stundenplanManagerGetByAbschnittAndDatumOrAfterOrNull(final long idSchuljahresabschnitt, final @NotNull String datum) {
		final StundenplanManager exactMatch = stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch != null) {
			return exactMatch;
		}
		final List<StundenplanManager> stundenplanManagerList = _stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if ((stundenplanManagerList == null) || stundenplanManagerList.isEmpty()) {
			return null;
		}
		if ((stundenplanManagerList.size() == 1) && (stundenplanManagerList.getFirst().getGueltigAb().compareTo(datum) > 0)) {
			return stundenplanManagerList.getFirst();
		}
		for (final @NotNull StundenplanManager manager : stundenplanManagerList) {
			if (manager.getGueltigAb().compareTo(datum) > 0) {
				return manager;
			}
		}
		return null;
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public @NotNull StundenplanManager stundenplanManagerGetByAbschnittAndDatumOrException(final long idSchuljahresabschnitt, final @NotNull String datum) {
		return DeveloperNotificationException.ifNull("Kein Stundenplanmanager zu Abschnitt %d und Datum %s gefunden.".formatted(idSchuljahresabschnitt, datum),
				stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param jahr das Jahr
	 * @param kw die Kalenderwoche, zu der der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public @NotNull StundenplanManager stundenplanManagerGetByAbschnittAndKwOrException(final long idSchuljahresabschnitt, final int jahr, final int kw) {
		return DeveloperNotificationException.ifNull("Kein Stundenplanmanager zu Abschnitt %d und Datum %s gefunden.".formatted(idSchuljahresabschnitt, kw),
				stundenplanManagerGetByAbschnittAndKwOrNull(idSchuljahresabschnitt, jahr, kw));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public StundenplanManager stundenplanManagerGetByTerminOrNull(final @NotNull GostKlausurtermin termin) {
		return stundenplanManagerGetByAbschnittAndDatumOrNull(termin.idSchuljahresabschnitt,
				DeveloperNotificationException.ifNull("Kein Datum zum Termin %d gefunden.".formatted(termin.id), termin.datum));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public @NotNull StundenplanManager stundenplanManagerGetByTerminOrException(final @NotNull GostKlausurtermin termin) {
		return DeveloperNotificationException.ifNull("Kein Stundenplan zu Termin %d (%s) gefunden.".formatted(termin.id, termin.datum),
				stundenplanManagerGetByTerminOrNull(termin));
	}

	private @NotNull StundenplanManager stundenplanManagerGetByDatumLinearSearch(final @NotNull String datum) {
		for (final StundenplanManager stundenplanManager : _stundenplanmanager_by_schuljahresabschnitt_and_datum.getNonNullValuesAsList()) {
			if ((stundenplanManager != null) && (stundenplanManager.getGueltigAb().compareTo(datum) <= 0)
					&& (stundenplanManager.getGueltigBis().compareTo(datum) >= 0)) {
				return stundenplanManager;
			}
		}
		throw new DeveloperNotificationException("Kein Stundenplan zu Datum %s gefunden.".formatted(datum));
	}

	/**
	 * Liefert die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}, falls diese gesetzt ist, sonst wird eine
	 * {@link DeveloperNotificationException} geworfen.
	 *
	 * @return die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}
	 */
	public @NotNull Map<Long, LehrerListeEintrag> getLehrerMap() {
		return _lehrerMap;
	}

	/**
	 * Liefert den {@link SchuelerListeEintrag} zur übergebenen Schüler-ID, falls dieser existiert, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param id die Schüler-ID
	 *
	 * @return den {@link SchuelerListeEintrag} zur übergebenen Schüler-ID, falls dieser existiert, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public @NotNull SchuelerListeEintrag schuelerGetByIdOrException(final long id) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerlisteeintrag_by_id, id);
	}

	/**
	 * Setzt die Maps, die zu den {@link SchuelerListeEintrag}en gehören.
	 *
	 * @param listSchueler Liste von {@link SchuelerListeEintrag}en
	 * @param ignoreExists wenn true, wird bei bereits existierenden Schülern kein Fehler geworfen und der alte Eintrag wird beibehalten
	 */
	private void schuelerAddAllOhneUpdate(final @NotNull List<SchuelerListeEintrag> listSchueler, final boolean ignoreExists) {
		for (final SchuelerListeEintrag sle : listSchueler) {
			if (ignoreExists) {
				if (!_schuelerlisteeintrag_by_id.containsKey(sle.id)) {
					_schuelerlisteeintrag_by_id.put(sle.id, sle);
				}
			} else {
				DeveloperNotificationException.ifMapPutOverwrites(_schuelerlisteeintrag_by_id, sle.id, sle);
			}
		}
	}

	private void addSchuljahr(final @NotNull List<GostKlausurenHalbjahresdaten> jahrgaenge, final int abiturjahrgang, final int hjStart,
			final @NotNull Set<Integer> abijahreAngefordert) {
		if (!_klausurdatenEnthalten.contains(abiturjahrgang, hjStart)) {
			final @NotNull GostKlausurenHalbjahresdaten data = new GostKlausurenHalbjahresdaten(abiturjahrgang, hjStart);
			if (!_klausurdatenEnthalten.containsKey1(abiturjahrgang) && !abijahreAngefordert.contains(abiturjahrgang)) {
				data.schueler = new ArrayList<>();
				data.faecher = new ArrayList<>();
				abijahreAngefordert.add(abiturjahrgang);
			}
			jahrgaenge.add(data);
		}
	}

	// Methode zum Hinzufügen von Jahrgangsdaten
	private void addSchuljahresPaare(final @NotNull List<GostKlausurenHalbjahresdaten> jahrgaenge, final int abiturjahrgang, final int hjStart,
			final @NotNull Set<Integer> abijahreAngefordert) {
		addSchuljahr(jahrgaenge, abiturjahrgang, hjStart, abijahreAngefordert);
		addSchuljahr(jahrgaenge, abiturjahrgang, hjStart + 1, abijahreAngefordert);
	}

	/**
	 * Berechnet zu den Parametern die Liste von {@link GostKlausurenHalbjahresdaten}-Objekten, für die Klausurdaten geladen werden.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das aktuell betrachtete GostHalbjahr
	 * @return die Liste von {@link GostKlausurenHalbjahresdaten} -Objekten
	 */
	public @NotNull List<GostKlausurenHalbjahresdaten> getMissingHjKlausurdata(final int abiturjahr, final int halbjahr) {
		final @NotNull List<GostKlausurenHalbjahresdaten> jahrgaenge = new ArrayList<>();
		final @NotNull Set<Integer> abijahreAngefordert = new HashSet<>();

		final int hjStart = ((halbjahr % 2) == 0) ? halbjahr : (halbjahr - 1);
		addSchuljahresPaare(jahrgaenge, abiturjahr, hjStart, abijahreAngefordert);

		switch (halbjahr) {
			case 0, 1 -> {
				addSchuljahresPaare(jahrgaenge, abiturjahr - 1, 2, abijahreAngefordert);
				addSchuljahresPaare(jahrgaenge, abiturjahr - 2, 4, abijahreAngefordert);
			}
			case 2, 3 -> {
				addSchuljahresPaare(jahrgaenge, abiturjahr - 1, 4, abijahreAngefordert);
				addSchuljahresPaare(jahrgaenge, abiturjahr + 1, 0, abijahreAngefordert);
			}
			case 4, 5 -> {
				addSchuljahresPaare(jahrgaenge, abiturjahr + 1, 2, abijahreAngefordert);
				addSchuljahresPaare(jahrgaenge, abiturjahr + 2, 0, abijahreAngefordert);
			}
			default -> throw new DeveloperNotificationException("Ungültiges GostHalbjahr %d.".formatted(halbjahr));
		}
		return jahrgaenge;
	}

	private static long datumStringToLong(final @NotNull String date) {
		return Long.parseLong(date.replace("-", ""));
	}

	private void update_all() {

		update_schuelermenge_by_abijahr();

		update_vorgabemenge();
		update_vorgabefehlendmenge();
		update_kursklausurmenge();
		update_kursklausurfehlendmenge();
		update_terminmenge();
		update_schuelerklausurmenge();
		update_schuelerklausurfehlendmenge();
		update_schuelerklausurterminmenge();
		update_raummenge();
		update_raumstundenmenge();
		update_schuelerklausurraumstundenmenge();

		update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach();

		update_kursklausurmenge_by_idVorgabe_and_idKurs();
		update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();

		update_terminmenge_by_halbjahr_and_quartal();
		update_terminmenge_by_jahr_and_kw_and_abijahr();
		update_terminmenge_by_datum();

		update_raummenge_by_idTermin();
		update_raum_by_idTermin_and_idStundenplanraum();

		update_raumstundenmenge_by_idRaum();
		update_raumstunde_by_idRaum_and_idZeitraster();
		update_raumstundenmenge_by_idSchuelerklausurtermin(); // benötigt _raumstunde_by_id
		update_klausurraum_by_idSchuelerklausurtermin(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin,

		update_schuelerklausurterminaktuell_by_idSchuelerklausur();
		update_schuelerklausurterminaktuellmenge(); // benötigt _schuelerklausurterminaktuell_by_idSchuelerklausur
		update_schuelerklausurmenge_by_abijahr_and_idSchueler();
		update_schuelerklausurmenge_by_idKursklausur();
		update_schuelerklausurterminmenge_by_idSchuelerklausur();
		update_schuelerklausurterminmenge_by_idTermin();
		update_schuelerklausurterminmenge_by_idKursklausur();
		update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur(); // benötigt _schuelerklausurterminaktuell_by_idSchuelerklausur
		update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId(); // benötigt _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur
		update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal(); // benötigt _schuelerklausurterminaktuell_by_idSchuelerklausur
		update_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin
		update_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin

		update_raummenge_by_idTermin_and_idKursklausur(); // benötigt _schuelerklausurterminaktuellmenge

	}

	private void update_schuelermenge_by_abijahr() {
		_schuelermenge_by_abijahr.clear();
		for (final @NotNull SchuelerListeEintrag s : _schuelerlisteeintrag_by_id.values()) {
			MapUtils.getOrCreateArrayList(_schuelermenge_by_abijahr, s.abiturjahrgang).add(s);
		}
	}

	private void update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach() {
		_vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach = new ListMap5DLongKeys<>();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge) {
			_vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.add(v.abiturjahrgang, v.halbjahr, v.quartal,
					GostKursart.fromKuerzelOrException(v.kursart).id, v.idFach, v);
		}
	}

	private void update_kursklausurmenge_by_idVorgabe_and_idKurs() {
		_kursklausur_by_idVorgabe_and_idKurs = new ListMap2DLongKeys<>();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(_kursklausur_by_idVorgabe_and_idKurs, kk.idVorgabe, kk.idKurs, kk);
		}
	}

	private void update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin() {
		_kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal = new ListMap4DLongKeys<>();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			final @NotNull GostKlausurvorgabe v = vorgabeByKursklausur(kk);
			_kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.add(
					v.abiturjahrgang, v.halbjahr, (kk.idTermin != null) ? kk.idTermin : _ID_OHNE_ZUORDNUNG, v.quartal, kk);
		}
	}

	private void update_terminmenge_by_halbjahr_and_quartal() {
		_terminmenge_by_abijahr_and_halbjahr_and_quartal = new ListMap3DLongKeys<>();
		for (final @NotNull GostKlausurtermin t : _terminmenge) {
			_terminmenge_by_abijahr_and_halbjahr_and_quartal.add(t.abiturjahrgang, t.halbjahr, t.quartal, t);
		}
	}

	private void update_terminmenge_by_jahr_and_kw_and_abijahr() {
		_terminmenge_by_jahr_and_kw_and_abijahr = new ListMap3DLongKeys<>();
		for (final @NotNull GostKlausurtermin t : _terminmenge) {
			if (t.datum != null) {
				_terminmenge_by_jahr_and_kw_and_abijahr.add(DateUtils.gibKwJahrDesDatumsISO8601(t.datum), DateUtils.gibKwDesDatumsISO8601(t.datum),
						t.abiturjahrgang, t);
			}
		}
	}

	private void update_terminmenge_by_datum() {
		_terminmenge_by_datum_and_abijahr = new ListMap2DLongKeys<>();
		for (final @NotNull GostKlausurtermin t : _terminmenge) {
			if (t.datum != null) {
				_terminmenge_by_datum_and_abijahr.add(datumStringToLong(t.datum), t.abiturjahrgang, t);
			}
		}
	}

	private void update_schuelerklausurterminaktuellmenge() {
		_schuelerklausurterminaktuellmenge.clear();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminmenge) {
			if (istSchuelerklausurterminAktuell(skt) && istSchuelerklausurAktiv(schuelerklausurBySchuelerklausurtermin(skt))) {
				_schuelerklausurterminaktuellmenge.add(skt);
			}
		}
	}

	private void update_schuelerklausurterminaktuell_by_idSchuelerklausur() {
		_schuelerklausurterminaktuell_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminmenge) {
			final GostSchuelerklausurtermin sktMaxFolgenummer = _schuelerklausurterminaktuell_by_idSchuelerklausur.get(skt.idSchuelerklausur);
			if ((sktMaxFolgenummer == null) || (sktMaxFolgenummer.folgeNr < skt.folgeNr)) {
				_schuelerklausurterminaktuell_by_idSchuelerklausur.put(skt.idSchuelerklausur, skt);
			}
		}
	}

	private void update_schuelerklausurmenge_by_idKursklausur() {
		_schuelerklausur_by_idKursklausur_and_idSchueler = new ListMap2DLongKeys<>();
		for (final @NotNull GostSchuelerklausur sk : _schuelerklausurmenge) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(_schuelerklausur_by_idKursklausur_and_idSchueler, sk.idKursklausur, sk.idSchueler,
					sk);
		}
	}

	private void update_schuelerklausurmenge_by_abijahr_and_idSchueler() {
		_schuelerklausurmenge_by_abijahr_and_idSchueler.clear();
		for (final @NotNull GostSchuelerklausur sk : _schuelerklausurmenge) {
			Map2DUtils.getOrCreateArrayList(_schuelerklausurmenge_by_abijahr_and_idSchueler, vorgabeBySchuelerklausur(sk).abiturjahrgang, sk.idSchueler).add(sk);
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId() {
		_schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.clear();
		for (final long idTermin : _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.keySet1()) {
			if (idTermin == _ID_OHNE_ZUORDNUNG) {
				continue;
			}
			final @NotNull GostKlausurtermin termin = terminGetByIdOrException(idTermin);
			if (termin.datum == null) {
				continue;
			}
			final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
			for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(idTermin)) {
				final @NotNull GostSchuelerklausur sk = schuelerklausurBySchuelerklausurtermin(skt);
				Map3DUtils.getOrCreateArrayList(_schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId,
						vorgabeBySchuelerklausur(sk).abiturjahrgang, kw, sk.idSchueler).add(skt);
			}
		}
	}

	private void update_schuelerklausurterminmenge_by_idSchuelerklausur() {
		_schuelerklausurterminmenge_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminmenge) {
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur)
					.add(skt);
		}
		for (final @NotNull List<GostSchuelerklausurtermin> sktList : _schuelerklausurterminmenge_by_idSchuelerklausur
				.values()) {
			sktList.sort(_compSchuelerklausurtermin);
		}
	}

	private void update_schuelerklausurterminmenge_by_idTermin() {
		_schuelerklausurterminmenge_by_idTermin.clear();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminmenge) {
			if (!schuelerklausurBySchuelerklausurtermin(skt).aktiv) {
				continue;
			}
			if (skt.folgeNr == 0) {
				final Long idTermin = kursklausurBySchuelerklausurtermin(skt).idTermin;
				MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idTermin, idTermin == null ? _ID_OHNE_ZUORDNUNG : idTermin).add(skt);
			} else {
				MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idTermin,
						skt.idTermin == null ? _ID_OHNE_ZUORDNUNG : skt.idTermin).add(skt);
			}
		}
	}

	private void update_schuelerklausurterminmenge_by_idKursklausur() {
		_schuelerklausurterminmenge_by_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminmenge) {
			if ((skt.folgeNr == 0) && schuelerklausurBySchuelerklausurtermin(skt).aktiv) {
				MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idKursklausur,
						schuelerklausurBySchuelerklausurtermin(skt).idKursklausur).add(skt);
			}
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur() {
		_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur = new ListMap2DLongKeys<>();
		for (final @NotNull Entry<Long, List<GostSchuelerklausurtermin>> e : _schuelerklausurterminmenge_by_idTermin
				.entrySet()) {
			for (final @NotNull GostSchuelerklausurtermin skt : e.getValue()) {
				if (istSchuelerklausurterminAktuell(skt) && schuelerklausurBySchuelerklausurtermin(skt).aktiv) {
					_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.add(e.getKey(), schuelerklausurBySchuelerklausurtermin(skt).idKursklausur,
							skt);
				}
			}
		}
	}

	private void update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal() {
		_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin = new ListMap4DLongKeys<>();
		for (final @NotNull GostSchuelerklausur sk : _schuelerklausurmenge) {
			if (!sk.aktiv) {
				continue;
			}
			final @NotNull GostSchuelerklausurtermin sktLast = schuelerklausurterminAktuellBySchuelerklausur(sk);
			if (sktLast.folgeNr > 0) {
				final @NotNull GostKlausurvorgabe v = vorgabeBySchuelerklausurtermin(sktLast);
				_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.add(
						v.abiturjahrgang, v.halbjahr, v.quartal, (sktLast.idTermin != null) ? sktLast.idTermin : _ID_OHNE_ZUORDNUNG, sktLast);
			}
		}
	}

	private void update_raum_by_idTermin_and_idStundenplanraum() {
		_raum_by_idTermin_and_idStundenplanraum = new ListMap2DLongKeys<>();
		for (final @NotNull GostKlausurraum raum : _raummenge) {
			if (raum.idStundenplanRaum != null) {
				DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(_raum_by_idTermin_and_idStundenplanraum, raum.idTermin, raum.idStundenplanRaum,
						raum);
			}
		}
	}

	private void update_raummenge_by_idTermin() {
		_raummenge_by_idTermin.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge) {
			MapUtils.getOrCreateArrayList(_raummenge_by_idTermin, raum.idTermin).add(raum);
		}
	}

	private void update_raummenge_by_idTermin_and_idKursklausur() {
		_raummenge_by_idTermin_and_idKursklausur = new ListMap2DLongKeys<>();
		for (final @NotNull GostSchuelerklausurtermin skt : _schuelerklausurterminaktuellmenge) {
			final GostKlausurtermin termin = terminOrNullBySchuelerklausurtermin(skt);
			if (termin != null) {
				final GostKlausurraum raum = raumGetBySchuelerklausurtermin(skt);
				if (raum != null) {
					_raummenge_by_idTermin_and_idKursklausur.add(termin.id, kursklausurBySchuelerklausurtermin(skt).id, raum);
				}
			}
		}
	}

	private void update_raumstundenmenge_by_idRaum() {
		_raumstundenmenge_by_idRaum.clear();
		for (final @NotNull GostKlausurraumstunde krs : _raumstundenmenge) {
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idRaum, krs.idRaum).add(krs);
		}
	}

	private void update_raumstunde_by_idRaum_and_idZeitraster() {
		_raumstunde_by_idRaum_and_idZeitraster = new ListMap2DLongKeys<>();
		for (final @NotNull GostKlausurraumstunde rs : _raumstundenmenge) {
			if (rs.idZeitraster != null) {
				DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(_raumstunde_by_idRaum_and_idZeitraster, rs.idRaum, rs.idZeitraster, rs);
			} else {
				_raumstunde_by_idRaum_and_idZeitraster.add(rs.idRaum, _ID_OHNE_ZUORDNUNG, rs);
			}
		}
	}

	private void update_raumstundenmenge_by_idSchuelerklausurtermin() {
		_raumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge) {
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin)
					.add(DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, skrs.idRaumstunde));
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin() {
		_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin = new ListMap2DLongKeys<>();
		for (final @NotNull GostSchuelerklausurtermin k : _schuelerklausurterminaktuellmenge) {
			final GostKlausurtermin termin = terminOrNullBySchuelerklausurtermin(k);
			if (termin != null) {
				final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
				_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.add(
						((raumstunden == null) || raumstunden.isEmpty()) ? _ID_OHNE_ZUORDNUNG : raumstunden.getFirst().idRaum,
						termin.id, k);
			}
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur() {
		_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur = new ListMap2DLongKeys<>();
		for (final @NotNull GostSchuelerklausurtermin k : _schuelerklausurterminaktuellmenge) {
			final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur
					.add(((raumstunden == null) || raumstunden.isEmpty()) ? _ID_OHNE_ZUORDNUNG : raumstunden.getFirst().idRaum, kursklausurBySchuelerklausurtermin(k).id, k);
		}
	}

	private void update_klausurraum_by_idSchuelerklausurtermin() {
		_klausurraum_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge) {
			final @NotNull List<GostKlausurraumstunde> krsList =
					DeveloperNotificationException.ifMapGetIsNull(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin);
			for (final @NotNull GostKlausurraumstunde krs : krsList) {
				final @NotNull GostKlausurraum kr = DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, krs.idRaum);
				final GostKlausurraum krAlt = _klausurraum_by_idSchuelerklausurtermin.put(skrs.idSchuelerklausurtermin, kr);
				if ((krAlt != null) && (krAlt != kr)) {
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausurtermin + " ist zwei Klausurräumen zugeordnet.");
				}
			}
		}
	}


	// #####################################################################
	// #################### GostKlausurvorgabe ################################
	// #####################################################################

	private void update_vorgabemenge() {
		_vorgabenmenge.clear();
		_vorgabenmenge.addAll(_vorgabe_by_id.values());
		_vorgabenmenge.sort(_compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void vorgabeAdd(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeAddAll(ListUtils.create1(vorgabe));
	}

	private void vorgabeAddAllOhneUpdate(final @NotNull Collection<GostKlausurvorgabe> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurvorgabe vorgabe : list) {
			vorgabeCheck(vorgabe);
			DeveloperNotificationException.ifTrue(
					"vorgabeAddAllOhneUpdate: ID=%d existiert bereits!".formatted(vorgabe.id),
					_vorgabe_by_id.containsKey(vorgabe.id));
			DeveloperNotificationException.ifTrue(
					"vorgabeAddAllOhneUpdate: ID=%d doppelt in der Liste!".formatted(vorgabe.id),
					!setOfIDs.add(vorgabe.id));
		}

		// add all
		for (final @NotNull GostKlausurvorgabe vorgabe : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.id, vorgabe);
			vorgabefehlendRemoveOhneUpdate(vorgabe);
		}
		_vorgabenInitialized = true;
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void vorgabeAddAll(final @NotNull Collection<GostKlausurvorgabe> listVorgaben) {
		vorgabeAddAllOhneUpdate(listVorgaben);
		update_all();
	}

	private static void vorgabeCheck(final @NotNull GostKlausurvorgabe vorgabe) {
		DeveloperNotificationException.ifInvalidID("vorgabe.idVorgabe", vorgabe.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idVorgabe Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurvorgabe}-Objekt.
	 */
	public @NotNull GostKlausurvorgabe vorgabeGetByIdOrException(final long idVorgabe) {
		return DeveloperNotificationException.ifMapGetIsNull(_vorgabe_by_id, idVorgabe);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeAsList() {
		return _vorgabenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurvorgabe}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param vorgabe Das neue {@link GostKlausurvorgabe}-Objekt.
	 */
	public void vorgabePatchAttributes(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabeCheck(vorgabe);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, vorgabe.id);
		DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.id, vorgabe);

		update_all();
	}

	private void vorgabeRemoveOhneUpdateById(final long idVorgabe) {
		final @NotNull GostKlausurvorgabe vorgabe = DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, idVorgabe);
		_kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.removeMap4(vorgabe.abiturjahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.id);
		vorgabe.id = -1;
		// vorgabefehlendAddAllOhneUpdate(ListUtils.create1(vorgabe)); Fehlende Vorgaben müssen neu geladen werden.
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param idVorgabe Die ID des {@link GostKlausurvorgabe}-Objekts.
	 */
	public void vorgabeRemoveById(final long idVorgabe) {
		vorgabeRemoveOhneUpdateById(idVorgabe);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public void vorgabeRemoveAll(final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		for (final @NotNull GostKlausurvorgabe vorgabe : listVorgaben) {
			vorgabeRemoveOhneUpdateById(vorgabe.id);
		}

		update_all();
	}

	// #####################################################################
	// #################### GostKlausurvorgabe fehlend #####################
	// #####################################################################

	private void update_vorgabefehlendmenge() {
		_vorgabenfehlendmenge.clear();
		_vorgabenfehlendmenge.addAll(_vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesAsList());
		_vorgabenfehlendmenge.sort(_compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void vorgabefehlendAdd(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabefehlendAddAll(ListUtils.create1(vorgabe));
	}

	private void vorgabefehlendAddAllOhneUpdate(final @NotNull List<GostKlausurvorgabe> list) {
		for (final @NotNull GostKlausurvorgabe vorgabe : list) {
			DeveloperNotificationException.ifMap5DPutOverwrites(_vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach,
					vorgabe.abiturjahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.kursart, vorgabe.idFach, vorgabe);
		}
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void vorgabefehlendAddAll(final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		vorgabefehlendAddAllOhneUpdate(listVorgaben);
		update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabefehlendGetMengeAsList() {
		return _vorgabenfehlendmenge;
	}

	private void vorgabefehlendRemoveOhneUpdate(final @NotNull GostKlausurvorgabe vorgabe) {
		_vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.remove(vorgabe.abiturjahrgang, vorgabe.halbjahr, vorgabe.quartal,
				vorgabe.kursart, vorgabe.idFach);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param vorgabe die zu löschende {@link GostKlausurvorgabe}
	 */
	public void vorgabefehlendRemove(final @NotNull GostKlausurvorgabe vorgabe) {
		vorgabefehlendRemoveOhneUpdate(vorgabe);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public void vorgabefehlendRemoveAll(final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		for (final @NotNull GostKlausurvorgabe vorgabe : listVorgaben) {
			vorgabefehlendRemoveOhneUpdate(vorgabe);
		}

		update_all();
	}

	// #####################################################################
	// #################### GostKursklausur ################################
	// #####################################################################

	private void update_kursklausurmenge() {
		_kursklausurmenge.clear();
		_kursklausurmenge.addAll(_kursklausur_by_id.values());
		_kursklausurmenge.sort(_compKursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void kursklausurAdd(final @NotNull GostKursklausur kursklausur) {
		kursklausurAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void kursklausurAddAllOhneUpdate(final @NotNull Collection<GostKursklausur> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKursklausur klausur : list) {
			kursklausurCheck(klausur);
			DeveloperNotificationException.ifTrue(
					"kursklausurAddAllOhneUpdate: ID=%d existiert bereits!".formatted(klausur.id),
					_kursklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue(
					"kursklausurAddAllOhneUpdate: ID=%d doppelt in der Liste!".formatted(klausur.id),
					!setOfIDs.add(klausur.id));
		}

		// add all
		for (final @NotNull GostKursklausur klausur : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, klausur.id, klausur);
			kursklausurfehlendRemoveOhneUpdate(klausur);
		}
		_klausurenInitialized = true;
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void kursklausurAddAll(final @NotNull List<GostKursklausur> listKursklausuren) {
		kursklausurAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	private static void kursklausurCheck(final @NotNull GostKursklausur kursklausur) {
		DeveloperNotificationException.ifInvalidID("kursklausur.id", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public @NotNull GostKursklausur kursklausurGetByIdOrException(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_kursklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMengeAsList() {
		return _kursklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public void kursklausurPatchAttributes(final @NotNull GostKursklausur kursklausur) {
		kursklausurPatchAttributesOhneUpdate(kursklausur);
		update_all();
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	private void kursklausurPatchAttributesOhneUpdate(final @NotNull GostKursklausur kursklausur) {
		kursklausurCheck(kursklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, kursklausur.id, kursklausur);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausurMenge Das neue {@link GostKursklausur}-Objekt.
	 */
	public void kursklausurMengePatchAttributes(final @NotNull List<GostKursklausur> kursklausurMenge) {

		for (final @NotNull GostKursklausur kursklausur : kursklausurMenge) {
			kursklausurPatchAttributesOhneUpdate(kursklausur);
		}

		update_all();
	}

	private void kursklausurRemoveOhneUpdateById(final long idKursklausur) {
		schuelerklausurRemoveAllOhneUpdate(_schuelerklausur_by_idKursklausur_and_idSchueler.get1(idKursklausur));
		final GostKursklausur removed = DeveloperNotificationException.ifMapRemoveFailes(_kursklausur_by_id, idKursklausur);
		kursklausurfehlendRemoveOhneUpdate(removed);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public void kursklausurRemoveById(final long idKursklausur) {
		kursklausurRemoveOhneUpdateById(idKursklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public void kursklausurRemoveAll(final @NotNull List<GostKursklausur> listKursklausuren) {
		for (final @NotNull GostKursklausur kursklausur : listKursklausuren) {
			kursklausurRemoveOhneUpdateById(kursklausur.id);
		}

		update_all();
	}

	// #####################################################################
	// #################### GostKursklausur fehlend#########################
	// #####################################################################

	private void update_kursklausurfehlendmenge() {
		_kursklausurfehlendmenge.clear();
		_kursklausurfehlendmenge.addAll(_kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesAsList());
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public void kursklausurfehlendAdd(final @NotNull GostKursklausur kursklausur) {
		kursklausurfehlendAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void kursklausurfehlendAddAllOhneUpdate(final @NotNull List<GostKursklausur> list) {
		for (final @NotNull GostKursklausur klausur : list) {
			final @NotNull GostKlausurvorgabe v = vorgabeByKursklausur(klausur);
			DeveloperNotificationException.ifMap5DPutOverwrites(_kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs, v.abiturjahrgang, v.halbjahr,
					v.quartal, v.id, klausur.idKurs, klausur);
		}
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void kursklausurfehlendAddAll(final @NotNull List<GostKursklausur> listKursklausuren) {
		kursklausurfehlendAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<GostKursklausur> kursklausurfehlendGetMengeAsList() {
		return _kursklausurfehlendmenge;
	}

	private void kursklausurfehlendRemoveOhneUpdate(final @NotNull GostKursklausur kursklausur) {
		final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(kursklausur);
		_kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.remove(vorgabe.abiturjahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.id, kursklausur.idKurs);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param kursklausur das zu löschende {@link GostKursklausur}-Objekt.
	 */
	public void kursklausurfehlendRemove(final @NotNull GostKursklausur kursklausur) {
		kursklausurfehlendRemoveOhneUpdate(kursklausur);

		update_all();
	}

	// #####################################################################
	// #################### GostKlausurtermin ################################
	// #####################################################################

	private void update_terminmenge() {
		_terminmenge.clear();
		_terminmenge.addAll(_termin_by_id.values());
		_terminmenge.sort(_compTermin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public void terminAdd(final @NotNull GostKlausurtermin termin) {
		terminAddAll(ListUtils.create1(termin));
	}

	private void terminAddAllOhneUpdate(final @NotNull Collection<GostKlausurtermin> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurtermin termin : list) {
			terminCheck(termin);
			DeveloperNotificationException.ifTrue(
					"terminAddAllOhneUpdate: ID=%d existiert bereits!".formatted(termin.id),
					_termin_by_id.containsKey(termin.id)
			);
			DeveloperNotificationException.ifTrue(
					"terminAddAllOhneUpdate: ID=%d doppelt in der Liste!".formatted(termin.id),
					!setOfIDs.add(termin.id)
			);

		}

		// add all
		for (final @NotNull GostKlausurtermin termin : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);
		}
	}

	/**
	 * Fügt alle {@link GostKlausurtermin}-Objekte hinzu.
	 *
	 * @param listTermine Die Menge der {@link GostKlausurtermin}-Objekte, welche
	 *                    hinzugefügt werden soll.
	 */
	public void terminAddAll(final @NotNull List<GostKlausurtermin> listTermine) {
		terminAddAllOhneUpdate(listTermine);
		update_all();
	}

	private static void terminCheck(final @NotNull GostKlausurtermin termin) {
		DeveloperNotificationException.ifInvalidID("termin.id", termin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idTermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public @NotNull GostKlausurtermin terminGetByIdOrException(final long idTermin) {
		return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, idTermin);
	}

	/**
	 * Liefert das zum {@link GostKlausurraum} zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param raum das {@link GostKlausurtermin}-Objekt.
	 *
	 * @return das zum Parameter zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public @NotNull GostKlausurtermin terminGetByRaumOrException(final @NotNull GostKlausurraum raum) {
		return terminGetByIdOrException(raum.idTermin);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurtermin}-Objekt oder null.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idTermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurtermin}-Objekt oder null.
	 */
	public GostKlausurtermin terminGetByIdOrNull(final long idTermin) {
		return _termin_by_id.get(idTermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurtermin}-Objekte.
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeAsList() {
		return _terminmenge;
	}

	private void terminPatchAttributesOhneUpdate(final @NotNull GostKlausurtermin termin) {
		terminCheck(termin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public void terminPatchAttributes(final @NotNull GostKlausurtermin termin) {
		terminPatchAttributesOhneUpdate(termin);

		update_all();
	}

	private void terminRemoveOhneUpdateById(final long idTermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, idTermin);
		final List<GostKursklausur> kursklausurenZuTermin = _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get3(idTermin);
		for (final @NotNull GostKursklausur k : kursklausurenZuTermin) {
			k.idTermin = null;
		}
		final List<GostSchuelerklausurtermin> schuelerklausurtermineZuTermin = _schuelerklausurterminmenge_by_idTermin
				.get(idTermin);
		if (schuelerklausurtermineZuTermin != null) {
			for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurtermineZuTermin) {
				skt.idTermin = null;
			}
		}
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurtermin}-Objekt.
	 *
	 * @param idTermin Die ID des {@link GostKlausurtermin}-Objekts.
	 */
	public void terminRemoveById(final long idTermin) {
		terminRemoveOhneUpdateById(idTermin);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurtermin}-Objekte.
	 *
	 * @param listTermine Die Liste der zu entfernenden
	 *                    {@link GostKlausurtermin}-Objekte.
	 */
	public void terminRemoveAll(final @NotNull List<GostKlausurtermin> listTermine) {
		for (final @NotNull GostKlausurtermin termin : listTermine) {
			terminRemoveOhneUpdateById(termin.id);
		}

		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausur ################################
	// #####################################################################

	private void update_schuelerklausurmenge() {
		_schuelerklausurmenge.clear();
		_schuelerklausurmenge.addAll(_schuelerklausur_by_id.values());
		_schuelerklausurmenge.sort(_compSchuelerklausur);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                    hinzugefügt werden soll.
	 */
	public void schuelerklausurAdd(final @NotNull GostSchuelerklausur kursklausur) {
		schuelerklausurAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void schuelerklausurAddAllOhneUpdate(final @NotNull Collection<GostSchuelerklausur> list) {
	    // check all
	    final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
	    for (final @NotNull GostSchuelerklausur klausur : list) {
	        schuelerklausurCheck(klausur);
	        DeveloperNotificationException.ifTrue(
	            "schuelerklausurAddAllOhneUpdate: ID=%d existiert bereits!".formatted(klausur.id),
	            _schuelerklausur_by_id.containsKey(klausur.id));
	        DeveloperNotificationException.ifTrue(
	            "schuelerklausurAddAllOhneUpdate: ID=%d doppelt in der Liste!".formatted(klausur.id),
	            !setOfIDs.add(klausur.id));
	    }

	    // add all
	    for (final @NotNull GostSchuelerklausur klausur : list) {
	        DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, klausur.id, klausur);
	        schuelerklausurfehlendRemoveOhneUpdate(klausur);
	    }
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void schuelerklausurAddAll(final @NotNull List<GostSchuelerklausur> listKursklausuren) {
		schuelerklausurAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	private static void schuelerklausurCheck(final @NotNull GostSchuelerklausur kursklausur) {
		DeveloperNotificationException.ifInvalidID("schuelerklausur.idSchuelerklausur", kursklausur.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausur}-Objekt.
	 */
	public @NotNull GostSchuelerklausur schuelerklausurGetByIdOrException(final long idSchuelerklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausur_by_id, idSchuelerklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public void schuelerklausurPatchAttributes(final @NotNull GostSchuelerklausur kursklausur) {
		schuelerklausurCheck(kursklausur);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, kursklausur.id, kursklausur);

		update_all();
	}

	private void schuelerklausurRemoveOhneUpdateById(final long idSchuelerklausur) {
		final GostSchuelerklausur removed = DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, idSchuelerklausur);
		schuelerklausurterminRemoveAllOhneUpdate(schuelerklausurterminGetMengeBySchuelerklausur(removed));
		schuelerklausurfehlendRemoveOhneUpdate(removed);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public void schuelerklausurRemoveById(final long idSchuelerklausur) {
		schuelerklausurRemoveOhneUpdateById(idSchuelerklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausuren Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausur}-Objekte.
	 */
	private void schuelerklausurRemoveAllOhneUpdate(final @NotNull List<GostSchuelerklausur> listSchuelerklausuren) {
		for (final @NotNull GostSchuelerklausur schuelerklausur : listSchuelerklausuren) {
			schuelerklausurRemoveOhneUpdateById(schuelerklausur.id);
		}
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausuren Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausur}-Objekte.
	 */
	public void schuelerklausurRemoveAll(final @NotNull List<GostSchuelerklausur> listSchuelerklausuren) {
		schuelerklausurRemoveAllOhneUpdate(listSchuelerklausuren);
		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausur fehlend ####################
	// #####################################################################

	private void update_schuelerklausurfehlendmenge() {
		_schuelerklausurfehlendmenge.clear();
		_schuelerklausurfehlendmenge
				.addAll(_schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesAsList());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                    hinzugefügt werden soll.
	 */
	public void schuelerklausurfehlendAdd(final @NotNull GostSchuelerklausur kursklausur) {
		schuelerklausurfehlendAddAll(ListUtils.create1(kursklausur));
		update_all();
	}

	private void schuelerklausurfehlendAddAllOhneUpdate(final @NotNull List<GostSchuelerklausur> list) {
		for (final @NotNull GostSchuelerklausur klausur : list) {
			final @NotNull GostKursklausur kursklausur = kursklausurBySchuelerklausur(klausur);
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(kursklausur);
			DeveloperNotificationException.ifMap5DPutOverwrites(
					_schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur, vorgabe.abiturjahrgang, vorgabe.halbjahr,
					vorgabe.quartal, klausur.idSchueler, kursklausur.id, klausur);
		}
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void schuelerklausurfehlendAddAll(final @NotNull List<GostSchuelerklausur> listKursklausuren) {
		schuelerklausurfehlendAddAllOhneUpdate(listKursklausuren);
		update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurfehlendGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurfehlendmenge);
	}

	private void schuelerklausurfehlendRemoveOhneUpdate(final @NotNull GostSchuelerklausur klausur) {
		final @NotNull GostKursklausur kursklausur = kursklausurBySchuelerklausur(klausur);
		final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(kursklausur);
		_schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.remove(vorgabe.abiturjahrgang, vorgabe.halbjahr,
				vorgabe.quartal, klausur.idSchueler, kursklausur.id);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param klausur die {@link GostKursklausur}
	 */
	public void schuelerklausurfehlendRemove(final @NotNull GostSchuelerklausur klausur) {
		schuelerklausurfehlendRemoveOhneUpdate(klausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public void schuelerklausurfehlendRemoveAll(final @NotNull List<GostSchuelerklausur> listKursklausuren) {
		for (final @NotNull GostSchuelerklausur kursklausur : listKursklausuren) {
			schuelerklausurfehlendRemoveOhneUpdate(kursklausur);
		}

		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausurtermin
	// ################################
	// #####################################################################

	private void update_schuelerklausurterminmenge() {
		_schuelerklausurterminmenge.clear();
		_schuelerklausurterminmenge.addAll(_schuelerklausurtermin_by_id.values());
		_schuelerklausurterminmenge.sort(_compSchuelerklausurtermin);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurtermin}-Objekt hinzu.
	 *
	 * @param schuelerklausurtermin Das {@link GostSchuelerklausurtermin}-Objekt,
	 *                              welches hinzugefügt werden soll.
	 */
	public void schuelerklausurterminAdd(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		schuelerklausurterminAddAll(ListUtils.create1(schuelerklausurtermin));
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurtermin}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausurtermin}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public void schuelerklausurAddOhneUpdate(final @NotNull GostSchuelerklausurtermin schuelerklausur) {
		schuelerklausurterminAddAllOhneUpdate(ListUtils.create1(schuelerklausur));
	}

	private void schuelerklausurterminAddAllOhneUpdate(final @NotNull Collection<GostSchuelerklausurtermin> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausurtermin schuelerklausurtermin : list) {
			schuelerklausurterminCheck(schuelerklausurtermin);
			DeveloperNotificationException.ifTrue(
				"schuelerklausurterminAddAllOhneUpdate: ID=%d existiert bereits!".formatted(schuelerklausurtermin.id),
				_schuelerklausurtermin_by_id.containsKey(schuelerklausurtermin.id));
			DeveloperNotificationException.ifTrue(
				"schuelerklausurterminAddAllOhneUpdate: ID=%d doppelt in der Liste!".formatted(schuelerklausurtermin.id),
				!setOfIDs.add(schuelerklausurtermin.id));
		}

		// add all
		for (final @NotNull GostSchuelerklausurtermin schuelerklausurtermin : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id,
				schuelerklausurtermin);
		}
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurtermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurtermine die Menge der
	 *                                   {@link GostSchuelerklausurtermin}-Objekte,
	 *                                   welche hinzugefügt werden sollen.
	 */
	public void schuelerklausurterminAddAll(final @NotNull List<GostSchuelerklausurtermin> listSchuelerklausurtermine) {
		schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		update_all();
	}

	private static void schuelerklausurterminCheck(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		DeveloperNotificationException.ifInvalidID("schuelerschuelerklausurtermin.idSchuelerschuelerklausurtermin",
				schuelerklausurtermin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurtermin}-Objekt.
	 */
	public @NotNull GostSchuelerklausurtermin schuelerklausurterminGetByIdOrException(
			final long idSchuelerklausurtermin) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurtermin}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurterminmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurtermin}-Objekt durch
	 * das neue Objekt.
	 *
	 * @param schuelerklausurtermin Das neue
	 *                              {@link GostSchuelerklausurtermin}-Objekt.
	 */
	public void schuelerklausurterminPatchAttributes(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		schuelerklausurterminPatchAttributesOhneUpdate(schuelerklausurtermin);
		update_all();
	}

	private void schuelerklausurterminPatchAttributesOhneUpdate(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		schuelerklausurterminCheck(schuelerklausurtermin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, schuelerklausurtermin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id,
				schuelerklausurtermin);
	}

	private void schuelerklausurterminRemoveOhneUpdateById(final long idSchuelerklausurtermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
		schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurtermin}-Objekt.
	 *
	 * @param idSchuelerklausurtermin die ID des
	 *                                {@link GostSchuelerklausurtermin}-Objekts.
	 */
	public void schuelerklausurterminRemoveById(final long idSchuelerklausurtermin) {
		schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin);
		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurtermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine die Liste der zu entfernenden
	 *                                   {@link GostSchuelerklausurtermin}-Objekte.
	 */
	public void schuelerklausurterminRemoveAllOhneUpdate(final @NotNull List<GostSchuelerklausurtermin> listSchuelerklausurtermine) {
		for (final @NotNull GostSchuelerklausurtermin schuelerklausurtermin : listSchuelerklausurtermine) {
			schuelerklausurterminRemoveOhneUpdateById(schuelerklausurtermin.id);
		}
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurtermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine die Liste der zu entfernenden
	 *                                   {@link GostSchuelerklausurtermin}-Objekte.
	 */
	public void schuelerklausurterminRemoveAll(final @NotNull List<GostSchuelerklausurtermin> listSchuelerklausurtermine) {
		schuelerklausurterminRemoveAllOhneUpdate(listSchuelerklausurtermine);
		update_all();
	}

	// #####################################################################
	// #################### Klausurraum ################################
	// #####################################################################

	private void update_raummenge() {
		_raummenge.clear();
		_raummenge.addAll(_raum_by_id.values());
		_raummenge.sort(_compRaum);
	}

	/**
	 * Fügt ein {@link GostKlausurraum}-Objekt hinzu.
	 *
	 * @param raum Das {@link GostKlausurraum}-Objekt, welches hinzugefügt werden
	 *             soll.
	 */
	public void raumAdd(final @NotNull GostKlausurraum raum) {
		raumAddAll(ListUtils.create1(raum));
	}

	private void raumAddAllOhneUpdate(final @NotNull Collection<GostKlausurraum> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraum raum : list) {
			raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", _raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}

		// add all
		for (final @NotNull GostKlausurraum raum : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);
		}
	}

	/**
	 * Fügt alle {@link GostKlausurraum}-Objekte hinzu.
	 *
	 * @param listRaum Die Menge der {@link GostKlausurraum}-Objekte, welche
	 *                 hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull List<GostKlausurraum> listRaum) {
		raumAddAllOhneUpdate(listRaum);
		update_all();
	}

	private static void raumCheck(final @NotNull GostKlausurraum raum) {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraum}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaum Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraum}-Objekt.
	 */
	public @NotNull GostKlausurraum raumGetByIdOrException(final long idRaum) {
		return DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraum}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraum}-Objekte.
	 */
	public @NotNull List<GostKlausurraum> raumGetMengeAsList() {
		return _raummenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraum}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param raum Das neue {@link GostKlausurraum}-Objekt.
	 */
	public void raumPatchAttributes(final @NotNull GostKlausurraum raum) {
		raumCheck(raum);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);

		update_all();
	}

	private void raumRemoveOhneUpdateById(final long idRaum) {
		DeveloperNotificationException.ifMapRemoveFailes(_raum_by_id, idRaum);
		final List<GostKlausurraumstunde> rsList = _raumstundenmenge_by_idRaum.get(idRaum);
		if (rsList != null) {
			for (final @NotNull GostKlausurraumstunde rs : rsList) {
				raumstundeRemoveOhneUpdateById(rs.id);
			}
		}
	}

	private void raumRemoveIfExistsNoCascadeOhneUpdateById(final long idRaum) {
		_raum_by_id.remove(idRaum);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link GostKlausurraum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);

		update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	private void raumRemoveAllIfExistsNoCascadeOhneUpdate(final @NotNull Collection<GostKlausurraum> listRaum) {
		for (final @NotNull GostKlausurraum raum : listRaum) {
			raumRemoveIfExistsNoCascadeOhneUpdateById(raum.id);
		}
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull List<GostKlausurraum> listRaum) {
		for (final @NotNull GostKlausurraum raum : listRaum) {
			raumRemoveOhneUpdateById(raum.id);
		}

		update_all();
	}

	// #####################################################################
	// #################### Klausurraumstunde ################################
	// #####################################################################

	private void update_raumstundenmenge() {
		_raumstundenmenge.clear();
		_raumstundenmenge.addAll(_raumstunde_by_id.values());
	}

	/**
	 * Fügt ein {@link GostKlausurraumstunde}-Objekt hinzu.
	 *
	 * @param raumstunde Das {@link GostKlausurraumstunde}-Objekt, welches
	 *                   hinzugefügt werden soll.
	 */
	public void raumstundeAdd(final @NotNull GostKlausurraumstunde raumstunde) {
		raumstundeAddAll(ListUtils.create1(raumstunde));
	}

	private void raumstundeAddAllOhneUpdate(final @NotNull Collection<GostKlausurraumstunde> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraumstunde raumstunde : list) {
			raumstundeCheck(raumstunde);
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " existiert bereits!",
					_raumstunde_by_id.containsKey(raumstunde.id));
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " doppelt in der Liste!", !setOfIDs.add(raumstunde.id));
		}

		// add all
		for (final @NotNull GostKlausurraumstunde raumstunde : list) {
			DeveloperNotificationException.ifMapPutOverwrites(_raumstunde_by_id, raumstunde.id, raumstunde);
		}
	}

	/**
	 * Fügt alle {@link GostKlausurraumstunde}-Objekte hinzu.
	 *
	 * @param listRaumstunde Die Menge der {@link GostKlausurraumstunde}-Objekte,
	 *                       welche hinzugefügt werden soll.
	 */
	public void raumstundeAddAll(final @NotNull Collection<GostKlausurraumstunde> listRaumstunde) {
		raumstundeAddAllOhneUpdate(listRaumstunde);
		update_all();
	}

	private static void raumstundeCheck(final @NotNull GostKlausurraumstunde raumstunde) {
		DeveloperNotificationException.ifInvalidID("raumstunde.id", raumstunde.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idRaumstunde Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKlausurraumstunde}-Objekt.
	 */
	public @NotNull GostKlausurraumstunde raumstundeGetByIdOrException(final long idRaumstunde) {
		return DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraumstunde}-Objekte.
	 */
	public @NotNull List<GostKlausurraumstunde> raumstundeGetMengeAsList() {
		return _raumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraumstunde}-Objekt durch das
	 * neue Objekt.
	 *
	 * @param raumstunde Das neue {@link GostKlausurraumstunde}-Objekt.
	 */
	public void raumstundePatchAttributes(final @NotNull GostKlausurraumstunde raumstunde) {
		raumstundeCheck(raumstunde);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_raumstunde_by_id, raumstunde.id);
		DeveloperNotificationException.ifMapPutOverwrites(_raumstunde_by_id, raumstunde.id, raumstunde);

		update_all();
	}

	private void raumstundeRemoveOhneUpdateById(final long idRaumstunde) {
		DeveloperNotificationException.ifMapRemoveFailes(_raumstunde_by_id, idRaumstunde);
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get2(idRaumstunde);
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList) {
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
		}
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraumstunde}-Objekt.
	 *
	 * @param idRaumstunde Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void raumstundeRemoveById(final long idRaumstunde) {
		raumstundeRemoveOhneUpdateById(idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public void raumstundeRemoveAllOhneUpdate(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
		for (final @NotNull GostKlausurraumstunde raumstunde : listRaumstunde) {
			raumstundeRemoveOhneUpdateById(raumstunde.id);
		}
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public void raumstundeRemoveAll(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
		raumstundeRemoveAllOhneUpdate(listRaumstunde);
		update_all();
	}

	// #####################################################################
	// #################### Schuelerklausurraumstunde
	// ################################
	// #####################################################################

	private void update_schuelerklausurraumstundenmenge() {
		_schuelerklausurterminraumstundenmenge.clear();
		_schuelerklausurterminraumstundenmenge.addAll(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getAllValues());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurterminraumstunde}-Objekt hinzu.
	 *
	 * @param schuelerklausurraumstunde Das
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt,
	 *                                  welches hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAdd(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAll(ListUtils.create1(schuelerklausurraumstunde));
	}

	private void schuelerklausurraumstundeAddAllOhneUpdate(final @NotNull Collection<GostSchuelerklausurterminraumstunde> list) {
		// check all
		final @NotNull HashMap2D<Long, Long, GostSchuelerklausurterminraumstunde> setOfIDs = new HashMap2D<>();
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list) {
			schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=(" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") existiert bereits!",
					_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.containsKey12(schuelerklausurraumstunde.idSchuelerklausurtermin,
							schuelerklausurraumstunde.idRaumstunde));
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") doppelt in der Liste!",
					setOfIDs.contains(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde));
			setOfIDs.put(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}

		// add all
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
					schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurterminraumstunde}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurraumstunde Die Menge der
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte,
	 *                                      welche hinzugefügt werden soll.
	 */
	public void schuelerklausurraumstundeAddAll(final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurraumstunde) {
		schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);
		update_all();
	}

	private static void schuelerklausurraumstundeCheck(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idSchuelerklausur", schuelerklausurraumstunde.idSchuelerklausurtermin);
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idRaumstunde", schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurtermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstundeGetByIdSchuelerklausurterminAndIdRaumstundeOrException(
			final long idSchuelerklausurtermin,
			final long idRaumstunde) {
		return _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getSingle12OrException(idSchuelerklausurtermin, idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurtermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public GostSchuelerklausurterminraumstunde schuelerklausurraumstundeGetByIdSchuelerklausurterminAndIdRaumstundeOrNull(final long idSchuelerklausurtermin,
			final long idRaumstunde) {
		return _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getSingle12OrNull(idSchuelerklausurtermin, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte zur angegebenen Schülerklausurtermin-ID.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurtermin}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurraumstundeGetMengeByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		return _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get1(idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurraumstundeGetMengeAsList() {
		return _schuelerklausurterminraumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurterminraumstunde}-Objekt
	 * durch das neue Objekt.
	 *
	 * @param schuelerklausurraumstunde Das neue
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public void schuelerklausurraumstundePatchAttributes(final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde) {
		schuelerklausurraumstundeCheck(schuelerklausurraumstunde);

		// Altes Objekt durch neues Objekt ersetzen
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(schuelerklausurraumstunde.idSchuelerklausurtermin,
				schuelerklausurraumstunde.idRaumstunde);
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.add(schuelerklausurraumstunde.idSchuelerklausurtermin,
				schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);

		update_all();
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(idSchuelerklausur, idRaumstunde);
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeAllByKey1(idSchuelerklausurtermin);
	}

	private void schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get1(idSchuelerklausurtermin);
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList) {
			_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
		}
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurtermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausurterminAndIdRaumstunde(final long idSchuelerklausurtermin, final long idRaumstunde) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(idSchuelerklausurtermin, idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurtermin}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	private void schuelerklausurraumstundeRemoveAllOhneUpdateByIdSchuelerklausurtermin(final @NotNull List<Long> idsSchuelerklausurtermine) {
		for (final long idSchuelerklausurtermin : idsSchuelerklausurtermine) {
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
		}
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	public void schuelerklausurraumstundeRemoveAllByIdSchuelerklausurtermin(final @NotNull List<Long> idsSchuelerklausurtermine) {
		schuelerklausurraumstundeRemoveAllOhneUpdateByIdSchuelerklausurtermin(idsSchuelerklausurtermine);
		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public void schuelerklausurraumstundeRemoveAll(final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurRaumstunde) {
		schuelerklausurraumstundeRemoveAllOhneUpdate(listSchuelerklausurRaumstunde);
		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public void schuelerklausurraumstundeRemoveAllOhneUpdate(final @NotNull List<GostSchuelerklausurterminraumstunde> listSchuelerklausurRaumstunde) {
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : listSchuelerklausurRaumstunde) {
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausurtermin,
					schuelerklausurraumstunde.idRaumstunde);
		}
	}

	// ################################################################################


	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurvorgabe}n
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		if (quartal == 0) {
			return _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get12(abiturjahrgang, halbjahr.id);
		}
		return _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get123(abiturjahrgang, halbjahr.id, quartal);
	}

	/**
	 * Gibt das {@link GostKlausurvorgabe}-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     das Quartal
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return das {@link GostKlausurvorgabe}-Objekt
	 */
	public GostKlausurvorgabe vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getSingle12345OrNull(
				abiturjahrgang, halbjahr.id, quartal, kursartAllg.id, idFach);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals oder 0 für alle Quartale
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der {@link GostKlausurvorgabe}-Objekte
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		if (quartal > 0) {
			final List<GostKlausurvorgabe> retList = new ArrayList<>();
			final GostKlausurvorgabe vorgabe = vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiturjahrgang, halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe != null) {
				retList.add(vorgabe);
			}
			return retList;
		}
		return vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiturjahrgang, halbjahr, kursartAllg, idFach);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der {@link GostKlausurvorgabe}-Objekte
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get1245(abiturjahrgang, halbjahr.id,
				GostKursart.fromKuerzelOrException(kursartAllg.kuerzel).id, idFach);
	}

	/**
	 * Gibt die Vorgänger-{@link GostKlausurvorgabe} zum übergebenen Parameter zurück (vorhergehendes Quartal des aktuellen Schuljahres) oder <code>null</code>, falls es keinen Vorgänger gibt.
	 *
	 * @param vorgabe das {@link GostKlausurvorgabe}-Objekt, dessen Vorgänger gesucht ist.
	 *
	 * @return die Vorgänger-{@link GostKlausurvorgabe} oder <code>null</code>, falls es keinen Vorgänger gibt.
	 */
	public GostKlausurvorgabe vorgabeGetPrevious(final @NotNull GostKlausurvorgabe vorgabe) {
		final @NotNull List<GostKlausurvorgabe> vorgabenSchuljahr = _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach
				.get1245OrException(vorgabe.abiturjahrgang, vorgabe.halbjahr, GostKursart.fromKuerzelOrException(vorgabe.kursart).id, vorgabe.idFach);
		if ((vorgabe.halbjahr % 2) == 1) {
			vorgabenSchuljahr.addAll(_vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach
					.get1245(vorgabe.abiturjahrgang, vorgabe.halbjahr - 1L, GostKursart.fromKuerzelOrException(vorgabe.kursart).id, vorgabe.idFach));
		}
		vorgabenSchuljahr.sort(_compVorgabe);
		final int listIndex = vorgabenSchuljahr.indexOf(vorgabe);
		if (listIndex == 0) {
			return null;
		}
		return vorgabenSchuljahr.get(listIndex - 1);
	}

	/**
	 * Liefert die {@link GostKursklausur} zum übergebenen {@link GostKlausurtermin} und Kursid
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKursklausur} gesucht wird
	 * @param idKurs   die ID des Kurses, zu dem die {@link GostKursklausur} gesucht wird
	 *
	 * @return die {@link GostKursklausur} zum übergebenen {@link GostKlausurtermin} und Kursid
	 */
	public GostKursklausur kursklausurGetByTerminAndKursid(final @NotNull GostKlausurtermin termin, final long idKurs) {
		final List<GostKursklausur> klausuren = kursklausurGetMengeByTerminid(termin.id);
		for (final @NotNull GostKursklausur klaus : klausuren) {
			if (klaus.idKurs == idKurs) {
				return klaus;
			}
		}
		return null;
	}

	/**
	 * Liefert die {@link GostKursklausur}en des übergebenen {@link GostKlausurtermin}s, deren Kurse am übergebenen Datum,
	 * Wochentag und in der übergebenen Stunde Unterricht haben.
	 *
	 * @param termin    der {@link GostKlausurtermin}, dessen Kursklausuren gefiltert werden
	 * @param datum     das Datum, zu dem der Stundenplan gesucht wird
	 * @param wochentag der {@link Wochentag}, zu dem gefiltert wird
	 * @param stunde    die Stunde, zu der gefiltert wird
	 *
	 * @return die {@link GostKursklausur}en des Termins mit Unterricht zu den übergebenen Parametern
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMengeMitUnterrichtByTerminAndDatumAndWochentagAndStunde(
			final @NotNull GostKlausurtermin termin, final @NotNull String datum, final @NotNull Wochentag wochentag, final int stunde) {
		final StundenplanManager stundenplanManager = stundenplanManagerGetByAbschnittAndDatumOrNull(termin.idSchuljahresabschnitt, datum);
		if (stundenplanManager == null) {
			return new ArrayList<>();
		}
		final @NotNull List<Long> kursIds = new ArrayList<>();
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByTermin(termin)) {
			kursIds.add(klausur.idKurs);
		}
		final int wochentyp = stundenplanManager.kalenderwochenzuordnungGetByDatum(datum).wochentyp;
		final @NotNull List<GostKursklausur> result = new ArrayList<>();
		for (final @NotNull Long idKurs : stundenplanManager.kursGetMengeGefiltertByWochentypAndWochentagAndStunde(kursIds, wochentyp, wochentag, stunde)) {
			final GostKursklausur klausur = kursklausurGetByTerminAndKursid(termin, idKurs);
			if (klausur != null) {
				result.add(klausur);
			}
		}
		return result;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zum übergebenen Datum
	 *
	 * @param datum das Datum der {@link GostKlausurtermin}e im Format <code>YYYY-MM-DD</code>
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zum übergebenen Datum
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByDatum(final @NotNull String datum) {
		return _terminmenge_by_datum_and_abijahr.get1(datumStringToLong(datum));
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die dasselbe Datum wie der als Parameter übergebene {@link GostKlausurtermin} haben.
	 *
	 * @param termin der {@link GostKlausurtermin}, an dessen Datum die {@link GostKlausurtermin}e gesucht werden.
	 * @param mitTermin wenn <code>true</code>, enthält die Rückgabe auch den {@link GostKlausurtermin} <code>termin</code>, bei <code>false</code> wird er entfernt.
	 *
	 * @return die {@link GostKlausurtermin}en, die dasselbe Datum wie der als Parameter übergebene {@link GostKlausurtermin} haben.
	 */
	public @NotNull List<GostKlausurtermin> terminSelbesDatumGetMengeByTermin(final @NotNull GostKlausurtermin termin, final boolean mitTermin) {
		final @NotNull List<GostKlausurtermin> ergebnis =
				terminGetMengeByDatum(DeveloperNotificationException.ifNull("Datum des Termins %d".formatted(termin.id), termin.datum));
		if (!mitTermin) {
			ergebnis.remove(termin);
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 *
	 * @param datum das gesuchte Datum der {@link GostKlausurtermin}e im Format <code>YYYY-MM-DD</code>
	 *
	 * @return die Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 */
	public @NotNull List<List<GostKlausurtermin>> terminGruppierteUeberschneidungenGetMengeByDatum(
			final @NotNull String datum) {
		return gruppiereUeberschneidungen(terminGetMengeByDatum(datum));
	}

	/**
	 * Liefert eine Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum und Abiturjahrgang. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 *
	 * @param datum das gesuchte Datum der {@link GostKlausurtermin}e im Format <code>YYYY-MM-DD</code>
	 * @param abiturjahrgang der Abiturjahrgang, innerhalb dessen die {@link GostKlausurtermin}e gesucht werden
	 *
	 * @return die Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 */
	public @NotNull List<List<GostKlausurtermin>> terminGruppierteUeberschneidungenGetMengeByDatumAndAbijahr(
			final @NotNull String datum, final Integer abiturjahrgang) {
		if (abiturjahrgang == null) {
			return terminGruppierteUeberschneidungenGetMengeByDatum(datum);
		}
		return gruppiereUeberschneidungen(_terminmenge_by_datum_and_abijahr.get12(datumStringToLong(datum), abiturjahrgang));
	}

	private @NotNull List<List<GostKlausurtermin>> gruppiereUeberschneidungen(
			final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<List<GostKlausurtermin>> ergebnis = new ArrayList<>();
		// Teste alle übergebenen Termine
		for (final @NotNull GostKlausurtermin terminToAdd : termine) {
			boolean added = false;
			// Not supported by transpiler outerloop:
			// Teste alle bereits gefundenen gruppierten Terminlisten
			for (final @NotNull List<GostKlausurtermin> listToCheck : ergebnis) {
				// Teste jeden Termin innerhalb einer gruppierten Terminliste
				for (final @NotNull GostKlausurtermin terminInListe : listToCheck) {
					if (checkTerminUeberschneidung(terminInListe, terminToAdd)) {
						listToCheck.add(terminToAdd);
						added = true; // Not supported by transpiler break outerloop;
					}
					// Transpiler-Workaround
					if (added) {
						break;
					}
				}
				// Transpiler-Workaround
				if (added) {
					break;
				}
			}
			// Keine Überschneidung gefunden, also neue Liste
			if (!added) {
				ergebnis.add(ListUtils.create1(terminToAdd));
			}
		}
		return ergebnis;
	}

	private boolean checkTerminUeberschneidung(final @NotNull GostKlausurtermin t1,
			final @NotNull GostKlausurtermin t2) {
		final Integer s1 = minKlausurstartzeitByTerminOrNull(t1, true);
		final Integer s2 = minKlausurstartzeitByTerminOrNull(t2, true);
		final Integer e1 = maxKlausurendzeitByTerminOrNull(t1, true);
		final Integer e2 = maxKlausurendzeitByTerminOrNull(t2, true);
		if ((s1 == null) || (s2 == null) || (e1 == null) || (e2 == null)) {
			return false;
		}
		return (e1 >= s2) && (e2 >= s1);
	}

	private Integer minKlausurstartzeitByTerminOrNull(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (skts.isEmpty()) {
			return termin.startzeit;
		}
		final Integer minStart = minKlausurstartzeitBySchuelerklausurterminMengeOrNull(skts, includeNachschreiber);
		return (minStart != null) ? minStart : termin.startzeit;
	}

	private Integer maxKlausurendzeitByTerminOrNull(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		final Integer maxEnd = maxKlausurendzeitBySchuelerklausurterminMengeOrNull(skts, includeNachschreiber);
		if (maxEnd != null) {
			return maxEnd;
		}
		final Integer start = minKlausurstartzeitByTerminOrNull(termin, includeNachschreiber);
		return (start != null) ? (start + 1) : null;
	}

	private @NotNull List<GostKursklausur> kursklausurGetMengeByTerminid(final Long idTermin) {
		return _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get3((idTermin != null) ? idTermin : _ID_OHNE_ZUORDNUNG);
	}

	/**
	 * Liefert die Liste von {@link GostKursklausur}en, die zum übergebenen Termin gehören.
	 *
	 * @param termin der {@link GostKlausurtermin}, zuu dem die {@link GostKursklausur}en gesucht werden
	 *
	 * @return die Liste von {@link GostKursklausur}en, die zum übergebenen Termin gehören.
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		return kursklausurGetMengeByTerminid(termin.id);
	}

	/**
	 * Liefert die Menge von {@link GostKursklausur}en, die zum übergebenen Termin gehören, die ggf. auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin enthalten.
	 *
	 * @param termin der {@link GostKlausurtermin}, zuu dem die {@link GostKursklausur}en gesucht werden
	 * @param mitNachschreibern falls <code>true</code>, werden auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin in der Rückgabe enthalten sein.
	 *
	 * @return die Menge von {@link GostKursklausur}en, die zum übergebenen Termin gehören, die ggf. auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin enthalten.
	 */
	public @NotNull Set<GostKursklausur> kursklausurMitNachschreibernGetMengeByTermin(final @NotNull GostKlausurtermin termin,
			final boolean mitNachschreibern) {
		final Set<GostKursklausur> klausuren = new HashSet<>(kursklausurGetMengeByTermin(termin));
		if (mitNachschreibern) {
			for (final GostSchuelerklausurtermin skt : schuelerklausurterminGetMengeByTermin(termin)) {
				klausuren.add(kursklausurBySchuelerklausurtermin(skt));
			}
		}
		return klausuren;
	}

	/**
	 * Liefert eine Liste von {@link GostKursklausur}en zu den übergebenen Parametern, für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<GostKursklausur> kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			return _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get1234(abiturjahrgang, halbjahr.id, _ID_OHNE_ZUORDNUNG, quartal);
		}
		return _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get123(abiturjahrgang, halbjahr.id, _ID_OHNE_ZUORDNUNG);
	}

	/**
	 * Liefert eine {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@link PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang, zu dem die Paralleljahrgänge gesucht werden
	 * @param halbjahr    das {@link GostHalbjahr}, zu dem die Paralleljahrgänge gesucht werden
	 * @param includeSelf falls <code>true</code>, ist das {@link PairNN} aus <code>abiturjahrgang</code> und <code>halbjahr</code> in der Rückgabe inkludiert
	 *
	 * @return die {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@link PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 */
	public static @NotNull List<PairNN<Integer, GostHalbjahr>> halbjahreParallelUndAktivGetMenge(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final boolean includeSelf) {
		final @NotNull List<PairNN<Integer, GostHalbjahr>> ergebnis = new ArrayList<>();
		if (includeSelf) {
			ergebnis.add(new PairNN<>(abiturjahrgang, halbjahr));
		}
		if (halbjahr.id >= 2) {
			ergebnis.add(new PairNN<>(abiturjahrgang + 1, GostHalbjahr.fromIDorException(halbjahr.id - 2)));
		}
		if (halbjahr.id >= 4) {
			ergebnis.add(new PairNN<>(abiturjahrgang + 2, GostHalbjahr.fromIDorException(halbjahr.id - 4)));
		}
		if (halbjahr.id <= 3) {
			ergebnis.add(new PairNN<>(abiturjahrgang - 1, GostHalbjahr.fromIDorException(halbjahr.id + 2)));
		}
		if (halbjahr.id <= 1) {
			ergebnis.add(new PairNN<>(abiturjahrgang - 2, GostHalbjahr.fromIDorException(halbjahr.id + 4)));
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		if (!multijahrgang) {
			return terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal);
		}
		final List<GostKlausurtermin> termine = new ArrayList<>();

		for (final @NotNull PairNN<Integer, GostHalbjahr> jgHj : halbjahreParallelUndAktivGetMenge(abiturjahrgang, halbjahr, true)) {
			termine.addAll(terminGetMengeByAbijahrAndHalbjahrAndQuartal(jgHj.a, jgHj.b, quartal));
		}

		return termine;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final List<GostKlausurtermin> termine = new ArrayList<>();
			termine.addAll(_terminmenge_by_abijahr_and_halbjahr_and_quartal.get123(abiturjahrgang, halbjahr.id, quartal));
			termine.addAll(_terminmenge_by_abijahr_and_halbjahr_and_quartal.get123(abiturjahrgang, halbjahr.id, 0));
			return termine;
		}
		return _terminmenge_by_abijahr_and_halbjahr_and_quartal.get12(abiturjahrgang, halbjahr.id);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param jahr           das Kalenderwochenjahr, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param kw             die Kalenderwoche, zu der die {@link GostKlausurtermin}e gesucht werden
	 * @param abiturjahrgang der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param multijahrgang  wenn <code>true</code>, werden die {@link GostKlausurtermin}e aller Abiturjahrgänge in der Kalenderwoche zurückgegeben
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByJahrAndKwAndAbijahrMultijahrgang(final int jahr, final int kw,
			final int abiturjahrgang, final boolean multijahrgang) {
		if (!multijahrgang) {
			return terminGetMengeByJahrAndKwAndAbijahr(jahr, kw, abiturjahrgang);
		}
		return _terminmenge_by_jahr_and_kw_and_abijahr.get12(jahr, kw);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param jahr           das Kalenderwochenjahr, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param kw             die Kalenderwoche, zu der die {@link GostKlausurtermin}e gesucht werden
	 * @param abiturjahrgang der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByJahrAndKwAndAbijahr(final int jahr, final int kw, final int abiturjahrgang) {
		return _terminmenge_by_jahr_and_kw_and_abijahr.get123(jahr, kw, abiturjahrgang);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die für Nachschreiber zugelassen sind, zu den übergebenen Parametern
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die für Nachschreiber zugelassen, zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		final List<GostKlausurtermin> termine = new ArrayList<>();
		for (final @NotNull GostKlausurtermin t : terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiturjahrgang, halbjahr,
				quartal, multijahrgang)) {
			if (!t.istHaupttermin || t.nachschreiberZugelassen) {
				termine.add(t);
			}
		}
		termine.sort(_compTermin);
		return termine;
	}

	/**
	 * Prüft, ob in einem Nachschreibtermin {@link GostSchuelerklausurtermin}e anderer
	 * Jahrgangsstufen enthalten sind
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, dessen Nachschreibtermine geprüft werden
	 * @param halbjahr      das {@link GostHalbjahr}, dessen Nachschreibtermine geprüft werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return <code>true</code>, falls in einem Nachschreibtermin {@link GostSchuelerklausurtermin}e anderer
	 * Jahrgangsstufen enthalten sind
	 */
	public boolean terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		for (final @NotNull GostKlausurtermin t : terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiturjahrgang, halbjahr,
				quartal, multijahrgang)) {
			if (terminMitAnderenJgst(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden zu den übergebenen Parametern
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termine = new ArrayList<>();
		for (final @NotNull GostKlausurtermin t : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal)) {
			if (t.istHaupttermin) {
				termine.add(t);
			}
		}
		termine.sort(_compTermin);
		return termine;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr,
				quartal)) {
			if (termin.datum != null) {
				ergebnis.add(termin);
			}
		}
		ergebnis.sort(_compTermin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminMitDatumGetMenge() {
		final List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : _terminmenge) {
			if (termin.datum != null) {
				ergebnis.add(termin);
			}
		}
		ergebnis.sort(_compTermin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminOhneDatumGetMenge() {
		final List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : _terminmenge) {
			if (termin.datum == null) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr,
				quartal)) {
			if (termin.datum == null) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden und denen bereits ein Datum zugewiesen wurde.
	 *
	 * @param abiturjahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden und denen bereits ein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminHtMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr,
				quartal)) {
			if (termin.datum != null) {
				termineMitDatum.add(termin);
			}
		}
		termineMitDatum.sort(_compTermin);
		return termineMitDatum;
	}

	/**
	 * Gibt das allen Kursklausuren gemeinsame Quartal innerhalb des übergebenen {@link GostKlausurtermin}s zurück. Falls es verschiedene Quartale sind, wird <code>-1</code> zurückgegeben.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return das allen Kursklausuren gemeinsame Quartal innerhalb des übergebenen {@link GostKlausurtermin}s, sonst <code>-1</code>.
	 */
	public int quartalGetByTermin(final @NotNull GostKlausurtermin termin) {
		final @NotNull List<GostKursklausur> klausuren = kursklausurGetMengeByTerminid(termin.id);
		final @NotNull List<GostSchuelerklausurtermin> schuelertermine = schuelerklausurterminNtGetMengeByTermin(termin);
		if (klausuren.isEmpty() && schuelertermine.isEmpty()) {
			return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, termin.id).quartal;
		}
		final @NotNull List<GostKlausurvorgabe> vorgaben = new ArrayList<>();
		for (final @NotNull GostKursklausur k : klausuren) {
			vorgaben.add(vorgabeByKursklausur(k));
		}
		for (final @NotNull GostSchuelerklausurtermin k : schuelertermine) {
			vorgaben.add(vorgabeBySchuelerklausurtermin(k));
		}
		int quartal = -1;
		for (final @NotNull GostKlausurvorgabe v : vorgaben) {
			if (quartal == -1) {
				quartal = v.quartal;
			}
			if (quartal != v.quartal) {
				return -1;
			}
		}
		return quartal;
	}

	/**
	 * Liefert die minimale Startzeit des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die minimale Startzeit des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int minKlausurstartzeitByTermin(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (skts.isEmpty()) {
			return DeveloperNotificationException.ifNull("Die Startzeit des Termins darf an dieser Stelle nicht null sein.", termin.startzeit);
		}
		return minKlausurstartzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die minimale Startzeit des {@link GostKlausurraum}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die minimale Startzeit des {@link GostKlausurraum}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int minKlausurstartzeitByRaum(final @NotNull GostKlausurraum raum, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminGetMengeByRaum(raum);
		if (skts.isEmpty()) {
			return DeveloperNotificationException.ifNull("Die Startzeit des Termins darf an dieser Stelle nicht null sein.",
					terminGetByRaumOrException(raum).startzeit);
		}
		return minKlausurstartzeitByKlausurraumAndSchuelerklausurterminMenge(raum, skts, includeNachschreiber);
	}

	/**
	 * Liefert die minimale Startzeit der {@link GostSchuelerklausurtermin}e in Minuten im Kontext des übergebenen {@link GostKlausurraum}s.
	 * Der Raumkontext wird benötigt, wenn Kursklausuren in Räumen eines anderen, z.B. jahrgangsübergreifenden, Termins geschrieben werden.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param skts die zu prüfenden {@link GostSchuelerklausurtermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die minimale Startzeit der {@link GostSchuelerklausurtermin}e im Kontext des {@link GostKlausurraum}s
	 */
	public int minKlausurstartzeitByKlausurraumAndSchuelerklausurterminMenge(final @NotNull GostKlausurraum raum,
			final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			throw new DeveloperNotificationException("Keine Schülerklausurtermine zur Ermittlung der minimalen Klausurstartzeit gefunden.");
		}
		return DeveloperNotificationException.ifNull("Fehler bei der Ermittlung der minimalen Klausurstartzeit.",
				minKlausurstartzeitByKlausurraumAndSchuelerklausurterminMengeIntern(raum, skts, includeNachschreiber, true));
	}

	/**
	 * Liefert die minimale Startzeit der {@link GostSchuelerklausurtermin}e in Minuten im jeweiligen Terminkontext des Schülerklausurtermins.
	 * Für die Ermittlung von Raumstunden muss die Methode mit Raumkontext verwendet werden.
	 *
	 * @param skts die zu prüfenden {@link GostSchuelerklausurtermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die minimale Startzeit der {@link GostSchuelerklausurtermin}e in Minuten im jeweiligen Terminkontext
	 */
	private int minKlausurstartzeitBySchuelerklausurterminMenge(final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			throw new DeveloperNotificationException("Keine Schülerklausurtermine zur Ermittlung der minimalen Klausurstartzeit gefunden.");
		}
		return DeveloperNotificationException.ifNull("Fehler bei der Ermittlung der minimalen Klausurstartzeit.",
				minKlausurstartzeitBySchuelerklausurterminMengeIntern(null, skts, includeNachschreiber, true));
	}

	private Integer minKlausurstartzeitBySchuelerklausurterminMengeOrNull(final @NotNull List<GostSchuelerklausurtermin> skts,
			final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			return null;
		}
		return minKlausurstartzeitBySchuelerklausurterminMengeIntern(null, skts, includeNachschreiber, false);
	}

	private Integer minKlausurstartzeitBySchuelerklausurterminMengeIntern(final GostKlausurraum raum, final @NotNull List<GostSchuelerklausurtermin> skts,
			final boolean includeNachschreiber, final boolean strict) {
		Integer minStart = strict ? 1440 : null;
		for (final @NotNull GostSchuelerklausurtermin skt : skts) {
			if (!includeNachschreiber && (skt.folgeNr > 0)) {
				continue;
			}
			final Integer skStartzeit = startzeitBySchuelerklausurterminIntern(raum, skt, strict);
			if (skStartzeit == null) {
				continue;
			}
			minStart = ((minStart == null) || (skStartzeit < minStart)) ? skStartzeit : minStart;
		}
		return minStart;
	}

	private Integer minKlausurstartzeitByKlausurraumAndSchuelerklausurterminMengeIntern(final @NotNull GostKlausurraum raum,
			final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber, final boolean strict) {
		return minKlausurstartzeitBySchuelerklausurterminMengeIntern(raum, skts, includeNachschreiber, strict);
	}

	/**
	 * Liefert die maximale Endzeit des {@link GostKlausurraum}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Endzeit des {@link GostKlausurraum}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int maxKlausurendzeitByRaum(final @NotNull GostKlausurraum raum, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminGetMengeByRaum(raum);
		return maxKlausurendzeitByKlausurraumAndSchuelerklausurterminMenge(raum, skts, includeNachschreiber);
	}

	/**
	 * Liefert die maximale Endzeit der {@link GostSchuelerklausurtermin}e in Minuten im Kontext des übergebenen {@link GostKlausurraum}s.
	 * Der Raumkontext wird benötigt, wenn Kursklausuren in Räumen eines anderen, z.B. jahrgangsübergreifenden, Termins geschrieben werden.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param skts die zu prüfenden {@link GostSchuelerklausurtermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die maximale Endzeit der {@link GostSchuelerklausurtermin}e im Kontext des {@link GostKlausurraum}s
	 */
	public int maxKlausurendzeitByKlausurraumAndSchuelerklausurterminMenge(final @NotNull GostKlausurraum raum,
			final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			throw new DeveloperNotificationException("Keine Schülerklausurtermine zur Ermittlung der maximalen Klausurendzeit gefunden.");
		}
		return DeveloperNotificationException.ifNull("Fehler bei der Ermittlung der maximalen Klausurendzeit.",
				maxKlausurendzeitByKlausurraumAndSchuelerklausurterminMengeIntern(raum, skts, includeNachschreiber, true));
	}

	/**
	 * Liefert die maximale Endzeit des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Endzeit des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int maxKlausurendzeitByTermin(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		return maxKlausurendzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die maximale Endzeit der {@link GostSchuelerklausurtermin}e in Minuten im jeweiligen Terminkontext des Schülerklausurtermins.
	 * Für die Ermittlung von Raumstunden muss die Methode mit Raumkontext verwendet werden.
	 *
	 * @param skts die zu prüfenden {@link GostSchuelerklausurtermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die maximale Endzeit der {@link GostSchuelerklausurtermin}e in Minuten im jeweiligen Terminkontext
	 */
	private int maxKlausurendzeitBySchuelerklausurterminMenge(final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			throw new DeveloperNotificationException("Keine Schülerklausurtermine zur Ermittlung der maximalen Klausurendzeit gefunden.");
		}
		return DeveloperNotificationException.ifNull("Fehler bei der Ermittlung der maximalen Klausurendzeit.",
				maxKlausurendzeitBySchuelerklausurterminMengeIntern(null, skts, includeNachschreiber, true));
	}

	private Integer maxKlausurendzeitBySchuelerklausurterminMengeOrNull(final @NotNull List<GostSchuelerklausurtermin> skts,
			final boolean includeNachschreiber) {
		if (skts.isEmpty()) {
			return null;
		}
		return maxKlausurendzeitBySchuelerklausurterminMengeIntern(null, skts, includeNachschreiber, false);
	}

	private Integer maxKlausurendzeitBySchuelerklausurterminMengeIntern(final GostKlausurraum raum, final @NotNull List<GostSchuelerklausurtermin> skts,
			final boolean includeNachschreiber, final boolean strict) {
		final Integer minStart = minKlausurstartzeitBySchuelerklausurterminMengeIntern(raum, skts, includeNachschreiber, strict);
		if (minStart == null) {
			return null;
		}
		int maxEnd = minStart + 1;
		for (final @NotNull GostSchuelerklausurtermin skt : skts) {
			if (!includeNachschreiber && (skt.folgeNr > 0)) {
				continue;
			}
			final Integer skStartzeit = startzeitBySchuelerklausurterminIntern(raum, skt, strict);
			if (skStartzeit == null) {
				continue;
			}
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurtermin(skt);
			final int endzeit = skStartzeit + vorgabe.dauer + vorgabe.auswahlzeit;
			if (endzeit > maxEnd) {
				maxEnd = endzeit;
			}
		}
		return maxEnd;
	}

	private Integer maxKlausurendzeitByKlausurraumAndSchuelerklausurterminMengeIntern(final @NotNull GostKlausurraum raum,
			final @NotNull List<GostSchuelerklausurtermin> skts, final boolean includeNachschreiber, final boolean strict) {
		return maxKlausurendzeitBySchuelerklausurterminMengeIntern(raum, skts, includeNachschreiber, strict);
	}

	/**
	 * Liefert die minimale Klausurdauer des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die minimale Klausurdauer des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int minKlausurdauerGetByTermin(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		int minDauer = -1;
		final List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		for (final @NotNull GostSchuelerklausurtermin skt : skts) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurtermin(skt);
			minDauer = ((minDauer == -1) || (vorgabe.dauer < minDauer)) ? vorgabe.dauer : minDauer;
		}
		return (minDauer == -1) ? 0 : minDauer;
	}

	/**
	 * Liefert die maximale Klausurdauer des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Klausurdauer des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public int maxKlausurdauerGetByTermin(final @NotNull GostKlausurtermin termin, final boolean includeNachschreiber) {
		int maxDauer = 0;
		final @NotNull List<GostSchuelerklausurtermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (!skts.isEmpty()) {
			for (final @NotNull GostSchuelerklausurtermin skt : skts) {
				final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurtermin(skt);
				maxDauer = (vorgabe.dauer > maxDauer) ? vorgabe.dauer : maxDauer;
			}
			return maxDauer;
		}
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByTermin(termin)) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(klausur);
			maxDauer = (vorgabe.dauer > maxDauer) ? vorgabe.dauer : maxDauer;
		}
		return maxDauer;
	}

	// #####################################################################
	// #################### Konfliktberechnung Start ################################
	// #####################################################################

	/**
	 * Liefert die Konflikt-Paare, wenn {@link GostSchuelerklausurtermin}e aus der Menge <code>menge2</code> in die
	 * Menge <code>menge1</code> hinzugefügt werden. Falls ein {@link GostSchuelerklausurtermin} aus
	 * <code>menge1</code> bereits in <code>menge2</code> enthalten ist, wird dies nicht als Konflikt
	 * bewertet.
	 *
	 * @param menge1 die Liste der Ziel-{@link GostSchuelerklausurtermin}e, in die die Integration geprüft werden soll
	 * @param menge2 die Liste der Quell-{@link GostSchuelerklausurtermin}e, aus der die Integration in <code>menge1</code> geprüft werden soll
	 *
	 * @return die Liste der Konflikt-Paare
	 */
	private @NotNull List<PairNN<GostSchuelerklausurtermin, GostSchuelerklausurtermin>> konfliktPaarSchuelerklausurtermineGetMenge(
			final List<GostSchuelerklausurtermin> menge1, final List<GostSchuelerklausurtermin> menge2) {
		final @NotNull List<PairNN<GostSchuelerklausurtermin, GostSchuelerklausurtermin>> ergebnis = new ArrayList<>();
		if ((menge1 == null) || (menge2 == null) || menge1.isEmpty() || menge2.isEmpty()) {
			return ergebnis;
		}
		final boolean gleicheMenge = menge1 == menge2;
		for (final @NotNull GostSchuelerklausurtermin skt2 : menge2) {
			for (final @NotNull GostSchuelerklausurtermin skt1 : menge1) {
				if (gleicheMenge && (skt1.id > skt2.id)) {
					continue;
				}
				if (hatKonfliktBySchuelerklausurterminen(skt1, skt2)) {
					ergebnis.add(new PairNN<>(skt1, skt2));
				}
			}
		}
		return ergebnis;
	}

	private boolean hatKonfliktBySchuelerklausurterminen(final @NotNull GostSchuelerklausurtermin skt1,
			final @NotNull GostSchuelerklausurtermin skt2) {
		if (skt1.id == skt2.id) {
			return false;
		}
		final @NotNull GostSchuelerklausur sk1 = schuelerklausurBySchuelerklausurtermin(skt1);
		final @NotNull GostSchuelerklausur sk2 = schuelerklausurBySchuelerklausurtermin(skt2);
		return sk1.aktiv && sk2.aktiv && (sk1.idSchueler == sk2.idSchueler);
	}

	/**
	 * Berechnet die Konflikt-Menge, wenn der übergebene {@link GostSchuelerklausurtermin} in den übergebenen {@link GostKlausurtermin} hinzugefügt wird. Falls der übergebene {@link GostSchuelerklausurtermin} bereits im {@link GostKlausurtermin} enthalten ist, wird dies nicht als Konflikt
	 * bewertet.
	 *
	 * @param termin der {@link GostKlausurtermin}, in den <code>skt</code> hinzugefügt werden soll
	 * @param skt der {@link GostSchuelerklausurtermin}, der in <code>termin</code> hinzugefügt werden soll
	 *
	 * @return die Liste von {@link PairNN}en aus den beiden am Konflikt beteiligten {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<PairNN<GostSchuelerklausurtermin, GostSchuelerklausurtermin>> konfliktPaarGetMengeTerminAndSchuelerklausurtermin(
			final @NotNull GostKlausurtermin termin,
			final @NotNull GostSchuelerklausurtermin skt) {
		return konfliktPaarGetMengeTerminAndSchuelerklausurtermine(termin, ListUtils.create1(skt));
	}

	/**
	 * Berechnet die Konflikt-Menge, wenn die übergebenen {@link GostSchuelerklausurtermin}e in den übergebenen
	 * {@link GostKlausurtermin} hinzugefügt werden. Konflikte gegen bereits enthaltene identische Schülerklausurtermine
	 * werden nicht bewertet.
	 *
	 * @param termin der {@link GostKlausurtermin}, in den <code>skts</code> hinzugefügt werden sollen
	 * @param skts die {@link GostSchuelerklausurtermin}e, die in <code>termin</code> hinzugefügt werden sollen
	 *
	 * @return die Liste von {@link PairNN}en aus den beiden am Konflikt beteiligten {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<PairNN<GostSchuelerklausurtermin, GostSchuelerklausurtermin>> konfliktPaarGetMengeTerminAndSchuelerklausurtermine(
			final @NotNull GostKlausurtermin termin,
			final @NotNull List<GostSchuelerklausurtermin> skts) {
		final List<PairNN<GostSchuelerklausurtermin, GostSchuelerklausurtermin>> result =
				konfliktPaarSchuelerklausurtermineGetMenge(schuelerklausurterminAktuellGetMengeByTermin(termin), skts);
		result.addAll(konfliktPaarSchuelerklausurtermineGetMenge(skts, skts));
		return result;
	}

	/**
	 * Prüft, ob der zu einem {@link GostSchuelerklausurtermin} gehörige Schüler in einer {@link GostKursklausur} enthalten ist.
	 *
	 * @param schuelerklausurtermin der zu prüfende {@link GostSchuelerklausurtermin}
	 * @param kursklausur     die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls der zum {@link GostSchuelerklausurtermin} gehörige Schüler in der {@link GostKursklausur} enthalten ist
	 */
	public boolean konfliktZuKursklausurBySchuelerklausur(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin,
			final @NotNull GostKursklausur kursklausur) {
		final long idSchueler = schuelerklausurBySchuelerklausurtermin(schuelerklausurtermin).idSchueler;
		final GostSchuelerklausur sk = schuelerklausurByKursklausurAndSchuelerid(kursklausur, idSchueler);
		return (sk != null) && sk.aktiv;
	}

	/**
	 * Prüft, ob der übergebene {@link GostSchuelerklausurtermin} im übergebenen {@link GostKlausurtermin} mit einem anderen
	 * aktuellen {@link GostSchuelerklausurtermin} desselben Schülers kollidiert.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param schuelerklausurtermin der zu prüfende {@link GostSchuelerklausurtermin}
	 *
	 * @return <code>true</code>, falls im Termin ein anderer aktueller Schülerklausurtermin desselben Schülers vorhanden ist
	 */
	public boolean hatKonfliktByTerminAndSchuelerklausurtermin(final @NotNull GostKlausurtermin termin,
			final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		for (final @NotNull GostSchuelerklausurtermin terminSchuelerklausur : schuelerklausurterminAktuellGetMengeByTermin(termin)) {
			if (hatKonfliktBySchuelerklausurterminen(terminSchuelerklausur, schuelerklausurtermin)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die übergebene {@link GostKursklausur} fachlich in den übergebenen {@link GostKlausurtermin} passt.
	 * Eine Kursklausur passt, wenn der Termin für alle Quartale gilt oder dem Quartal der Klausurvorgabe entspricht.
	 *
	 * @param termin      der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, wenn die Kursklausur in den Termin passt
	 */
	public boolean kursklausurPasstInTermin(final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur) {
		return (termin.quartal == 0) || (termin.quartal == vorgabeByKursklausur(kursklausur).quartal);
	}

	/**
	 * Prüft, ob der übergebene {@link GostSchuelerklausurtermin} fachlich in den übergebenen Nachschreibtermin passt.
	 * Der Schülerklausurtermin passt, wenn der Termin für alle Quartale gilt oder dem Quartal der Klausurvorgabe entspricht
	 * und für den Schüler im Zieltermin noch kein Schülerklausurtermin existiert.
	 *
	 * @param termin                  der zu prüfende {@link GostKlausurtermin}
	 * @param schuelerklausurtermin  der zu prüfende {@link GostSchuelerklausurtermin}
	 *
	 * @return <code>true</code>, wenn der Schülerklausurtermin in den Nachschreibtermin passt
	 */
	public boolean schuelerklausurterminPasstInNachschreibtermin(final @NotNull GostKlausurtermin termin,
			final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		return schuelerklausurterminePassenInNachschreibtermin(termin, ListUtils.create1(schuelerklausurtermin));
	}

	/**
	 * Prüft, ob die übergebenen {@link GostSchuelerklausurtermin}e fachlich gemeinsam in den übergebenen Nachschreibtermin passen.
	 * Die Schülerklausurtermine passen, wenn der Termin für alle Quartale gilt oder dem Quartal der jeweiligen Vorgabe entspricht
	 * und für keinen Schüler im Zieltermin oder in der übergebenen Menge ein weiterer Schülerklausurtermin existiert.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param schuelerklausurtermine die zu prüfenden {@link GostSchuelerklausurtermin}e
	 *
	 * @return <code>true</code>, wenn die Schülerklausurtermine gemeinsam in den Nachschreibtermin passen
	 */
	public boolean schuelerklausurterminePassenInNachschreibtermin(final @NotNull GostKlausurtermin termin,
			final @NotNull List<GostSchuelerklausurtermin> schuelerklausurtermine) {
		for (final @NotNull GostSchuelerklausurtermin schuelerklausurtermin : schuelerklausurtermine) {
			if ((termin.quartal != 0) && (termin.quartal != vorgabeBySchuelerklausurtermin(schuelerklausurtermin).quartal)) {
				return false;
			}
		}
		return konfliktPaarGetMengeTerminAndSchuelerklausurtermine(termin, schuelerklausurtermine).isEmpty();
	}

	/**
	 * Liefert eine Liste mit {@link GostKursklausur} und den zugehörigen Schülern, die bereits existierende Konflikte in jeder
	 * {@link GostKursklausur} des übergebenen {@link GostKlausurtermin}s enthält.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return die Liste mit {@link GostKursklausur} und den zugehörigen Schülern, die bereits existierende Konflikte in jeder
	 * {@link GostKursklausur} des übergebenen {@link GostKlausurtermin}s enthält
	 */
	public @NotNull List<PairNN<GostKursklausur, List<SchuelerListeEintrag>>> konflikteKursklausurSchuelerByTermin(final @NotNull GostKlausurtermin termin) {
		return toKursklausurSchuelerKonflikte(konflikteMapByTermin(termin));
	}

	/**
	 * Liefert die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 */
	public int konflikteAnzahlGetByTermin(final @NotNull GostKlausurtermin termin) {
		return countKonflikte(konflikteMapByTermin(termin));
	}

	private @NotNull Map<GostKursklausur, Set<Long>> konflikteMapByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostKursklausur> klausuren = kursklausurGetMengeByTermin(termin);
		return berechneKonflikte(klausuren, klausuren,
				getSchuelerIDsFromSchuelerklausurterminen(schuelerklausurterminAktuellNtGetMengeByTermin(termin)));
	}

	/**
	 * Liefert eine Liste mit {@link GostKursklausur} und den zugehörigen Schülern, die nur die neuen Konflikte liefert,
	 * die die übergebe {@link GostKursklausur} beim Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Liste mit {@link GostKursklausur} und den zugehörigen Schülern, die nur die neuen Konflikte liefert,
	 * die die Übergebe {@link GostKursklausur} beim Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 */
	public @NotNull List<PairNN<GostKursklausur, List<SchuelerListeEintrag>>> konflikteNeuKursklausurSchuelerByTerminAndKursklausur(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur) {
		return toKursklausurSchuelerKonflikte(konflikteNeuMapByTerminAndKursklausur(termin, kursklausur));
	}

	/**
	 * Liefert die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} beim Hinzufügen im übergebenen {@link GostKlausurtermin}
	 * verursacht.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} beim Hinzufügen im übergebenen {@link GostKlausurtermin}
	 * verursacht.
	 */
	public int konflikteAnzahlZuTerminGetByTerminAndKursklausur(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKursklausur kursklausur) {
		return countKonflikte(konflikteNeuMapByTerminAndKursklausur(termin, kursklausur));
	}

	private @NotNull Map<GostKursklausur, Set<Long>> konflikteNeuMapByTerminAndKursklausur(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKursklausur kursklausur) {
		final Map<GostKursklausur, Set<Long>> result = berechneKonflikte(kursklausurGetMengeByTermin(termin), ListUtils.create1(kursklausur), null);
		addNachschreiberKonflikteByKursklausur(result, kursklausur, termin);
		return result;
	}

	private @NotNull Map<GostKursklausur, Set<Long>> konflikteZuEigenemTerminMapByKursklausur(final @NotNull GostKursklausur klausur) {
		final @NotNull List<GostKursklausur> klausuren1 = _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal
				.get3OrException(DeveloperNotificationException.ifNull("idTermin", klausur.idTermin));
		klausuren1.remove(klausur);
		final Map<GostKursklausur, Set<Long>> result = berechneKonflikte(klausuren1, ListUtils.create1(klausur), null);
		addNachschreiberKonflikteByKursklausur(result, klausur, terminOrExceptionByKursklausur(klausur));
		return result;
	}

	/**
	 * Liefert die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 */
	public int konflikteAnzahlZuEigenemTerminGetByKursklausur(final @NotNull GostKursklausur klausur) {
		return countKonflikte(konflikteZuEigenemTerminMapByKursklausur(klausur));
	}

	private @NotNull Map<GostKursklausur, Set<Long>> berechneKonflikte(final @NotNull List<GostKursklausur> klausuren1,
			final @NotNull List<GostKursklausur> klausuren2, final List<Long> skts) {
		if (klausuren1.isEmpty() || klausuren2.isEmpty()) {
			return new HashMap<>();
		}
		final Map<GostKursklausur, Set<Long>> result = new HashMap<>();
		final List<GostKursklausur> kursklausuren2Copy = new ArrayList<>(klausuren2);
		for (final @NotNull GostKursklausur kk1 : klausuren1) {
			kursklausuren2Copy.remove(kk1);
			for (final @NotNull GostKursklausur kk2 : kursklausuren2Copy) {
				final Set<Long> konflikte = berechneKlausurKonflikte(kk1, kk2);
				if (!konflikte.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte);
					MapUtils.getOrCreateHashSet(result, kk2).addAll(konflikte);
				}
			}
			if (skts != null) {
				final Set<Long> konflikte2 = berechneIdKonflikte(getSchuelerIDsAktivFromKursklausur(kk1), skts);
				if (!konflikte2.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte2);
				}
			}
		}
		return result;
	}

	private void addNachschreiberKonflikteByKursklausur(final @NotNull Map<GostKursklausur, Set<Long>> result,
			final @NotNull GostKursklausur kursklausur, final @NotNull GostKlausurtermin termin) {
		final Set<Long> konflikte = berechneIdKonflikte(getSchuelerIDsAktivFromKursklausur(kursklausur),
				getSchuelerIDsFromSchuelerklausurterminen(schuelerklausurterminAktuellNtGetMengeByTermin(termin)));
		if (!konflikte.isEmpty()) {
			MapUtils.getOrCreateHashSet(result, kursklausur).addAll(konflikte);
		}
	}

	private @NotNull List<PairNN<GostKursklausur, List<SchuelerListeEintrag>>> toKursklausurSchuelerKonflikte(
			final @NotNull Map<GostKursklausur, Set<Long>> konflikte) {
		final @NotNull List<PairNN<GostKursklausur, List<SchuelerListeEintrag>>> result = new ArrayList<>();
		for (final @NotNull Entry<GostKursklausur, Set<Long>> konflikt : konflikte.entrySet()) {
			final @NotNull List<SchuelerListeEintrag> schueler = new ArrayList<>();
			for (final @NotNull Long idSchueler : konflikt.getValue()) {
				schueler.add(schuelerGetByIdOrException(idSchueler));
			}
			schueler.sort(_compSchuelerListeEintrag);
			result.add(new PairNN<>(konflikt.getKey(), schueler));
		}
		result.sort(_compKursklausurKonflikt);
		return result;
	}

	private @NotNull Set<Long> berechneKlausurKonflikte(final @NotNull GostKursklausur kk1,
			final @NotNull GostKursklausur kk2) {
		return berechneIdKonflikte(getSchuelerIDsAktivFromKursklausur(kk1), getSchuelerIDsAktivFromKursklausur(kk2));
	}

	private static @NotNull Set<Long> berechneIdKonflikte(final @NotNull List<Long> kk1,
			final @NotNull List<Long> kk2) {
		final @NotNull HashSet<Long> konflikte = new HashSet<>(kk1);
		konflikte.retainAll(kk2);
		return konflikte;
	}

	private static int countKonflikte(final @NotNull Map<GostKursklausur, Set<Long>> konflikte) {
		final @NotNull HashSet<Long> susIds = new HashSet<>();
		for (final @NotNull Set<Long> klausurSids : konflikte.values()) {
			susIds.addAll(klausurSids);
		}
		return susIds.size();
	}

	// #####################################################################
	// #################### Konfliktberechnung Ende ################################
	// #####################################################################


	// #####################################################################
	// #################### Thresholdberechnung Start ################################
	// #####################################################################

	/**
	 * Liefert für einen Schwellwert und einen {@link GostKlausurtermin} eine Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e.
	 *
	 * @param termin    der {@link GostKlausurtermin}, dessen Kalenderwoche geprüft wird
	 * @param threshold der Schwellwert (z. B. 3), der mindestens erreicht sein muss, damit die Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 *
	 * @return die Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurtermin}e.
	 */
	public @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(
			final @NotNull GostKlausurtermin termin, final int threshold) {
		if (termin.datum == null) {
			return new ArrayList<>();
		}
		final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abiturjahrgang, null, threshold, false);
	}

	/**
	 * Liefert für einen Schwellwert, einen {@link GostKlausurtermin} und eine {@link GostKursklausur} eine Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e, wenn die übergebene {@link GostKursklausur} in den übergebenen {@link GostKlausurtermin} integriert würde.
	 *
	 * @param termin    der {@link GostKlausurtermin}, dessen Kalenderwoche geprüft wird
	 * @param klausur   die {@link GostKursklausur}, deren Integration in den {@link GostKlausurtermin} <code>termin</code> angenommen wird
	 * @param threshold der Schwellwert (z. B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 *
	 * @return die Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e, wenn die übergebene {@link GostKursklausur} in den übergebenen {@link GostKlausurtermin} integriert würde.
	 */
	public @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur, final int threshold) {
		if (termin.datum == null) {
			return new ArrayList<>();
		}
		final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abiturjahrgang,
				schuelerklausurterminGetMengeByKursklausur(klausur), threshold, false);
	}

	/**
	 * Liefert für einen Schwellwert und ein Datum eine Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e, wenn der übergebene {@link GostKlausurtermin} in die Kalenderwoche zusätzlich geplant würde.
	 *
	 * @param termin        der Klausurtermin, der zusätzlich in die durch <code>datum</code> angegebene Kalenderwoche geplant werden soll
	 * @param datum         das Datum, dessen Kalenderwoche auf die Klausuranzahl geprüft wird
	 * @param threshold     der Schwellwert (z. B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdOnly wenn <code>true</code> wird die Schüler-ID nur bei exaktem Erreichen des <code>threshold</code> in die Rückgabe-Map aufgenommen. Größere Werte werden nicht berücksichtigt.
	 *
	 * @return die Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e, wenn der übergebene {@link GostKlausurtermin} in die Kalenderwoche zusätzlich geplant würde.
	 */
	public @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(
			final @NotNull GostKlausurtermin termin, final @NotNull String datum, final int threshold,
			final boolean thresholdOnly) {
		final int kwDatum = DateUtils.gibKwDesDatumsISO8601(datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kwDatum, termin.abiturjahrgang,
				schuelerklausurterminAktuellGetMengeByTermin(termin), threshold, thresholdOnly);
	}

	private @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(
			final int kw, final int abiturjahrgang, final List<GostSchuelerklausurtermin> addMenge, final int threshold, final boolean thresholdOnly) {

		final Map<Long, List<GostSchuelerklausurtermin>> schuelerklausurterminaktuellmenge_by_schuelerId =
				_schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.getMap3OrNull(abiturjahrgang, kw);
		if (schuelerklausurterminaktuellmenge_by_schuelerId == null) {
			return new ArrayList<>();
		}

		final @NotNull Map<Long, List<GostSchuelerklausurtermin>> addTerminMap = new HashMap<>();
		if (addMenge != null) {
			for (final GostSchuelerklausurtermin addSkt : addMenge) {
				final GostSchuelerklausur sk = schuelerklausurBySchuelerklausurtermin(addSkt);
				if (sk.aktiv) {
					MapUtils.getOrCreateArrayList(addTerminMap, sk.idSchueler).add(addSkt);
				}
			}
		}

		final @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> ergebnis = new ArrayList<>();
		for (final @NotNull Entry<Long, List<GostSchuelerklausurtermin>> entry : schuelerklausurterminaktuellmenge_by_schuelerId.entrySet()) {
			final Set<GostSchuelerklausurtermin> klausuren = new HashSet<>();
			for (final GostSchuelerklausurtermin skt : entry.getValue()) {
				if (schuelerklausurBySchuelerklausurtermin(skt).aktiv) {
					klausuren.add(skt);
				}
			}

			if (addMenge != null) {
				final List<GostSchuelerklausurtermin> addSkts = addTerminMap.get(entry.getKey());
				if (addSkts != null) {
					klausuren.addAll(addSkts);
				}
			}
			if ((klausuren.size() == threshold) || ((klausuren.size() > threshold) && !thresholdOnly)) {
				final @NotNull List<GostSchuelerklausurtermin> klausurenListe = new ArrayList<>(klausuren);
				klausurenListe.sort(_compSchuelerklausurterminWochenkonflikt);
				ergebnis.add(new PairNN<>(schuelerGetByIdOrException(entry.getKey()), klausurenListe));
			}
		}
		ergebnis.sort(_compSchuelerWochenkonflikt);
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert, eine Kalenderwoche und ein Abiturjahr eine Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e.
	 *
	 * @param kw            die Kalenderwoche, für die die Klausuranzahl geprüft wird
	 * @param abiturjahrgang       das Abiturjahr der gesuchten Konflikt-Schüler
	 * @param threshold     der Schwellwert (z. B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdOnly wenn <code>true</code> wird die Schüler-ID nur bei exaktem Erreichen des <code>threshold</code> in die Rückgabe-Map aufgenommen. Größere Werte werden nicht berücksichtigt.
	 *
	 * @return die Liste mit Schülern und zugehörigen {@link GostSchuelerklausurtermin}en für Schüler, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurtermin}e.
	 */
	public @NotNull List<PairNN<SchuelerListeEintrag, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(
			final int kw, final int abiturjahrgang, final int threshold, final boolean thresholdOnly) {
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, abiturjahrgang, null, threshold, thresholdOnly);
	}

	/**
	 * Liefert für einen Schwellwert, eine Kalenderwoche und ein Abiturjahr eine Map Schüler-ID → {@link GostSchuelerklausurtermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert, und die betreffenden {@link GostSchuelerklausurtermin}e.
	 *
	 * @param abiturjahrgang       das Abiturjahr der gesuchten Konflikt-Schüler
	 * @param halbjahr das GostHalbjahr
	 * @param quartal das Quartal
	 * @param threshold     der Schwellwert (z. B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdMinus der Schwellwert (z. B. 4), dessen Menge von der Threshold-Menge abgezogen wird, damit Warnungen nicht die Fehler enthalten, bei -1 wird nichts abgezogen
	 *
	 * @return die Map Schüler-ID → {@link GostSchuelerklausurtermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurtermin}e.
	 */
	public @NotNull List<PairNN<PairNN<Integer, SchuelerListeEintrag>, List<GostSchuelerklausurtermin>>> klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(
			final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal, final int threshold, final int thresholdMinus) {
		final Map<Integer, Map<Long, List<GostSchuelerklausurtermin>>> schuelerklausurterminaktuellmenge_by_schuelerId =
				_schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.getMap2OrNull(abiturjahrgang);
		final @NotNull List<PairNN<PairNN<Integer, SchuelerListeEintrag>, List<GostSchuelerklausurtermin>>> ergebnis = new ArrayList<>();
		if (schuelerklausurterminaktuellmenge_by_schuelerId == null) {
			return ergebnis;
		}

		for (final @NotNull Entry<Integer, Map<Long, List<GostSchuelerklausurtermin>>> kwEntry : schuelerklausurterminaktuellmenge_by_schuelerId.entrySet()) {
			for (final @NotNull Entry<Long, List<GostSchuelerklausurtermin>> schuelerEntry : kwEntry.getValue().entrySet()) {
				final List<GostSchuelerklausurtermin> activeSkts = new ArrayList<>();
				for (final GostSchuelerklausurtermin skt : schuelerEntry.getValue()) {
					if (schuelerklausurBySchuelerklausurtermin(skt).aktiv) {
						activeSkts.add(skt);
					}
				}
				if ((activeSkts.size() >= threshold) && ((thresholdMinus < 0) || (activeSkts.size() < thresholdMinus))) {
					for (final @NotNull GostSchuelerklausurtermin skt : activeSkts) {
						final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurtermin(skt);
						if ((vorgabe.abiturjahrgang == abiturjahrgang) && (vorgabe.halbjahr == halbjahr.id) && ((quartal == 0) || (vorgabe.quartal == quartal))
								&& !((vorgabe.halbjahr == 5) && (vorgabe.quartal == 2))) {
							activeSkts.sort(_compSchuelerklausurterminWochenkonflikt);
							ergebnis.add(new PairNN<>(new PairNN<>(kwEntry.getKey(), schuelerGetByIdOrException(schuelerEntry.getKey())), activeSkts));
							break;
						}
					}
				}
			}
		}
		ergebnis.sort(_compKwSchuelerWochenkonflikt);
		return ergebnis;
	}

	// #####################################################################
	// #################### Thresholdberechnung Ende ################################
	// #####################################################################

	/**
	 * Liefert für eine Liste von {@link GostSchuelerklausur}en die zugehörigen
	 * Schüler-IDs als Liste.
	 *
	 * @param sks die Liste von {@link GostSchuelerklausur}en
	 *
	 * @return die Liste der Schüler-IDs
	 */
	private @NotNull List<Long> getSchuelerIDsFromSchuelerklausurterminen(final @NotNull List<GostSchuelerklausurtermin> sks) {
		final @NotNull List<Long> ids = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin sk : sks) {
			ids.add(schuelerklausurBySchuelerklausurtermin(sk).idSchueler);
		}
		return ids;
	}

	/**
	 * Liefert für eine Liste von {@link GostSchuelerklausur}en die zugehörigen
	 * Schüler-IDs als Liste.
	 *
	 * @param sks die Liste von {@link GostSchuelerklausur}en
	 *
	 * @return die Liste der Schüler-IDs
	 */
	public @NotNull List<Long> getSchuelerIDsFromSchuelerklausuren(final @NotNull List<GostSchuelerklausur> sks) {
		final @NotNull List<Long> ids = new ArrayList<>();
		for (final @NotNull GostSchuelerklausur sk : sks) {
			ids.add(sk.idSchueler);
		}
		return ids;
	}

	/**
	 * Liefert für eine {@link GostKursklausur} die zugehörigen Schüler-IDs als Liste.
	 *
	 * @param kk die {@link GostKursklausur}, zu der die Schüler-IDs gesucht werden
	 *
	 * @return die Liste der Schüler-IDs
	 */
	private @NotNull List<Long> getSchuelerIDsAktivFromKursklausur(final @NotNull GostKursklausur kk) {
		return getSchuelerIDsFromSchuelerklausurterminen(schuelerklausurterminAktuellByKursklausur(kk));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostKursklausur}, sonst <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin} oder <code>null</code>
	 */
	public GostKlausurtermin terminOrNullByKursklausur(final @NotNull GostKursklausur klausur) {
		return _termin_by_id.get(klausur.idTermin);
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostSchuelerklausur}, sonst <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin} oder <code>null</code>
	 */
	public GostKlausurtermin terminOrNullBySchuelerklausur(final @NotNull GostSchuelerklausur sk) {
		return terminOrNullByKursklausur(kursklausurBySchuelerklausur(sk));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostKursklausur}. Wenn noch kein Termin bestimmt ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public @NotNull GostKlausurtermin terminOrExceptionByKursklausur(final @NotNull GostKursklausur klausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, DeveloperNotificationException
				.ifNull(String.format("idTermin von Klausur %d darf nicht NULL sein", klausur.id), klausur.idTermin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurtermin} oder <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param termin der {@link GostSchuelerklausurtermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public GostKlausurtermin terminOrNullBySchuelerklausurtermin(final @NotNull GostSchuelerklausurtermin termin) {
		if (termin.folgeNr > 0) {
			return (termin.idTermin == null) ? null : terminGetByIdOrException(termin.idTermin);
		}
		return terminOrNullByKursklausur(kursklausurBySchuelerklausurtermin(termin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurtermin}. Wenn noch kein Termin bestimmt ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param termin der {@link GostSchuelerklausurtermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public @NotNull GostKlausurtermin terminOrExceptionBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin termin) {
		if (termin.folgeNr > 0) {
			return terminGetByIdOrException(DeveloperNotificationException
					.ifNull(String.format("idTermin von Termin %d", termin.id), termin.idTermin));
		}
		return terminOrExceptionByKursklausur(kursklausurBySchuelerklausurtermin(termin));
	}

	private String datumSchuelerklausurterminOrNull(final @NotNull GostSchuelerklausurtermin skt) {
		final GostKlausurtermin termin = terminOrNullBySchuelerklausurtermin(skt);
		return (termin == null) ? null : termin.datum;
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostSchuelerklausur} oder <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public GostKlausurtermin terminKursklausurBySchuelerklausur(final @NotNull GostSchuelerklausur sk) {
		return terminOrNullByKursklausur(kursklausurBySchuelerklausur(sk));
	}




	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einer {@link GostKursklausur}.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public @NotNull GostKlausurvorgabe vorgabeByKursklausur(final @NotNull GostKursklausur klausur) {
		return vorgabeGetByIdOrException(klausur.idVorgabe);
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einer {@link GostSchuelerklausur}.
	 *
	 * @param klausur die {@link GostSchuelerklausur}, zu der die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public @NotNull GostKlausurvorgabe vorgabeBySchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		final @NotNull GostKursklausur kk = kursklausurGetByIdOrException(klausur.idKursklausur);
		return vorgabeGetByIdOrException(kk.idVorgabe);
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einem {@link GostSchuelerklausurtermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurtermin}, zu dem die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public @NotNull GostKlausurvorgabe vorgabeBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin klausur) {
		return vorgabeBySchuelerklausur(schuelerklausurGetByIdOrException(klausur.idSchuelerklausur));
	}

	/**
	 * Liefert die {@link GostSchuelerklausur} zu einem {@link GostSchuelerklausurtermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurtermin}, zu der die {@link GostSchuelerklausur} gesucht wird.
	 *
	 * @return die {@link GostSchuelerklausur}
	 */
	public @NotNull GostSchuelerklausur schuelerklausurBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin klausur) {
		return schuelerklausurGetByIdOrException(klausur.idSchuelerklausur);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einer {@link GostSchuelerklausur}.
	 *
	 * @param klausur die {@link GostSchuelerklausur}, zu der die {@link GostKursklausur} gesucht wird.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public @NotNull GostKursklausur kursklausurBySchuelerklausur(final @NotNull GostSchuelerklausur klausur) {
		return kursklausurGetByIdOrException(klausur.idKursklausur);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einer {@link GostKlausurvorgabe} und einer Kurs-ID.
	 *
	 * @param vorgabe die {@link GostKlausurvorgabe}, zu der die {@link GostKursklausur} gesucht wird.
	 * @param idKurs die ID des Kurses der {@link GostKursklausur}.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public GostKursklausur kursklausurByVorgabeAndKursid(final @NotNull GostKlausurvorgabe vorgabe, final long idKurs) {
		return _kursklausur_by_idVorgabe_and_idKurs.getSingle12OrNull(vorgabe.id, idKurs);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einem {@link GostSchuelerklausurtermin}.
	 *
	 * @param termin der {@link GostSchuelerklausurtermin}, zu der die {@link GostKursklausur} gesucht wird.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public @NotNull GostKursklausur kursklausurBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin termin) {
		return kursklausurBySchuelerklausur(schuelerklausurGetByIdOrException(termin.idSchuelerklausur));
	}

	/**
	 * Liefert zurück, ob die übergebene {@link GostKlausurvorgabe} von einer {@link GostKursklausur}
	 * verwendet wird.
	 *
	 * @param vorgabe die {@link GostKlausurvorgabe}, die auf Verwendung geprüft werden soll.
	 *
	 * @return <code>true</code>, falls die {@link GostKlausurvorgabe} verwendet wird, sonst <code>false</code>
	 */
	public boolean istVorgabeVerwendetByKursklausur(final @NotNull GostKlausurvorgabe vorgabe) {
		final @NotNull List<GostKursklausur> klausuren = _kursklausur_by_idVorgabe_and_idKurs.get1(vorgabe.id);
		return !klausuren.isEmpty();
	}

	/**
	 * Liefert die Vorgänger-{@link GostKursklausur} aus dem letzten Quartal, wenn eine solche existiert, sonst <code>null</code>.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Vorgänger gesucht wird
	 *
	 * @return die {@link GostKursklausur} oder <code>null</code>
	 */
	public GostKursklausur kursklausurVorterminByKursklausur(final @NotNull GostKursklausur klausur) {
		final GostKlausurvorgabe previousVorgabe = vorgabeGetPrevious(vorgabeGetByIdOrException(klausur.idVorgabe));
		if (previousVorgabe == null) {
			return null;
		}
		if (!_kursklausur_by_idVorgabe_and_idKurs.containsKey1(previousVorgabe.id)) {
			return null;
		}
		final @NotNull List<GostKursklausur> klausuren = _kursklausur_by_idVorgabe_and_idKurs.get1(previousVorgabe.id);
		for (final @NotNull GostKursklausur k : klausuren) {
			final KursDaten kKurs = getKursManager().get(k.idKurs);
			final KursDaten klausurKurs = getKursManager().get(klausur.idKurs);
			if ((kKurs == null) || (klausurKurs == null)) {
				throw new DeveloperNotificationException("Keine Kurszuordnung im kursManager zu Kurs-ID");
			}
			if (kKurs.kuerzel.equals(klausurKurs.kuerzel)) { // TODO unsauber, aber KursId geht nicht, weil ggf. in
				// Schuljahresabschnitten unterschiedlich
				return k;
			}
		}
		return null;
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurtermin}s aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit der {@link GostKursklausur} zurückgegeben, sonst die des {@link GostKlausurtermin}s. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird <code>null</code>
	 * zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, dessen Startzeit gesucht wird.
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurtermin}s oder <code>null</code>
	 */
	public Integer startzeitBySchuelerklausurterminOrNull(final @NotNull GostSchuelerklausurtermin skt) {
		return (skt.startzeit != null) ? skt.startzeit : startzeitByKursklausurOrNull(kursklausurBySchuelerklausurtermin(skt));
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurtermin}s aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit der {@link GostKursklausur} zurückgegeben, sonst die des {@link GostKlausurtermin}s. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird eine <code>DeveloperNotificationException</code> geworfen.
	 * zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, dessen Startzeit gesucht wird.
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurtermin}s
	 */
	public int startzeitBySchuelerklausurterminOrException(final @NotNull GostSchuelerklausurtermin skt) {
		if (skt.startzeit != null) {
			return skt.startzeit;
		} else if (skt.folgeNr == 0) {
			return startzeitByKursklausurOrException(kursklausurBySchuelerklausurtermin(skt));
		} else {
			final long idTermin = DeveloperNotificationException.ifNull("idTermin von SchülerklausurTermin %d".formatted(skt.id), skt.idTermin);
			return DeveloperNotificationException.ifNull("startzeit von Termin %d".formatted(idTermin), terminGetByIdOrException(idTermin).startzeit);
		}
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurtermin}s im Kontext des übergebenen {@link GostKlausurraum}s zurück.
	 * Dieser Zugriff wird für Raumstunden benötigt, wenn Schülerklausuren in Räumen eines anderen Termins geschrieben werden.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param skt  der {@link GostSchuelerklausurtermin}, dessen Startzeit gesucht wird
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurtermin}s im Kontext des {@link GostKlausurraum}s oder <code>null</code>
	 */
	public Integer startzeitByKlausurraumAndSchuelerklausurterminOrNull(final @NotNull GostKlausurraum raum, final @NotNull GostSchuelerklausurtermin skt) {
		if (skt.startzeit != null) {
			return skt.startzeit;
		}
		if (skt.folgeNr == 0) {
			return startzeitByKlausurraumAndKursklausurOrNull(raum, kursklausurBySchuelerklausurtermin(skt));
		}
		return terminGetByRaumOrException(raum).startzeit;
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurtermin}s im Kontext des übergebenen {@link GostKlausurraum}s zurück.
	 * Falls keine Startzeit ermittelt werden kann, wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param skt  der {@link GostSchuelerklausurtermin}, dessen Startzeit gesucht wird
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurtermin}s im Kontext des {@link GostKlausurraum}s
	 */
	public int startzeitByKlausurraumAndSchuelerklausurterminOrException(final @NotNull GostKlausurraum raum, final @NotNull GostSchuelerklausurtermin skt) {
		return DeveloperNotificationException.ifNull("Startzeit des Schülerklausurtermins %d im Raum %d".formatted(skt.id, raum.id),
				startzeitByKlausurraumAndSchuelerklausurterminOrNull(raum, skt));
	}

	private Integer startzeitBySchuelerklausurterminIntern(final GostKlausurraum raum, final @NotNull GostSchuelerklausurtermin skt,
			final boolean strict) {
		if (raum == null) {
			return strict ? startzeitBySchuelerklausurterminOrException(skt) : startzeitBySchuelerklausurterminOrNull(skt);
		}
		return strict ? startzeitByKlausurraumAndSchuelerklausurterminOrException(raum, skt) : startzeitByKlausurraumAndSchuelerklausurterminOrNull(raum, skt);
	}

	/**
	 * Gibt die Startzeit der übergebenen {@link GostKursklausur} aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit des {@link GostKlausurtermin}s zurückgegeben. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird <code>null</code>
	 * zurückgegeben.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der {@link GostKursklausur} oder <code>null</code>
	 */
	public Integer startzeitByKursklausurOrNull(final @NotNull GostKursklausur klausur) {
		if (klausur.startzeit != null) {
			return klausur.startzeit;
		}
		final GostKlausurtermin termin = terminOrNullByKursklausur(klausur);
		return (termin == null) ? null : termin.startzeit;
	}

	/**
	 * Gibt die Startzeit der übergebenen {@link GostKursklausur} im Kontext des übergebenen {@link GostKlausurraum}s zurück.
	 * Falls eine individuelle Startzeit gesetzt ist, wird diese verwendet. Ist die {@link GostKursklausur} dem Termin des Raums
	 * zugeordnet, wird die effektive Startzeit der Kursklausur zurückgegeben. Andernfalls wird die Startzeit des Termins des
	 * Raums zurückgegeben.
	 *
	 * @param raum    der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param klausur die {@link GostKursklausur}, deren Startzeit gesucht wird
	 *
	 * @return die Startzeit der {@link GostKursklausur} im Kontext des {@link GostKlausurraum}s oder <code>null</code>
	 */
	public Integer startzeitByKlausurraumAndKursklausurOrNull(final @NotNull GostKlausurraum raum, final @NotNull GostKursklausur klausur) {
		if (klausur.startzeit != null) {
			return klausur.startzeit;
		}
		if ((klausur.idTermin != null) && (klausur.idTermin == raum.idTermin)) {
			return startzeitByKursklausurOrNull(klausur);
		}
		return terminGetByRaumOrException(raum).startzeit;
	}

	/**
	 * Gibt die Startzeit der übergebenen {@link GostKursklausur} im Kontext des übergebenen {@link GostKlausurraum}s zurück.
	 * Falls keine Startzeit ermittelt werden kann, wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param raum    der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param klausur die {@link GostKursklausur}, deren Startzeit gesucht wird
	 *
	 * @return die Startzeit der {@link GostKursklausur} im Kontext des {@link GostKlausurraum}s
	 */
	public int startzeitByKlausurraumAndKursklausurOrException(final @NotNull GostKlausurraum raum, final @NotNull GostKursklausur klausur) {
		return DeveloperNotificationException.ifNull("Startzeit der Kursklausur %d im Raum %d".formatted(klausur.id, raum.id),
				startzeitByKlausurraumAndKursklausurOrNull(raum, klausur));
	}

	/**
	 * Gibt die Startzeit der übergebenen {@link GostKursklausur} aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit des {@link GostKlausurtermin}s zurückgegeben. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Startzeit gesucht wird.
	 *
	 * @return die Startzeit der {@link GostKursklausur}
	 */
	public int startzeitByKursklausurOrException(final @NotNull GostKursklausur klausur) {
		return (klausur.startzeit != null) ? klausur.startzeit
				: DeveloperNotificationException.ifNull("Startzeit des Termins %d".formatted(terminOrExceptionByKursklausur(klausur).id),
						terminOrExceptionByKursklausur(klausur).startzeit);
	}

	/**
	 * Prüft, ob die übergebene {@link GostKursklausur} eine vom Terminkontext des übergebenen {@link GostKlausurraum}s abweichende Startzeit hat.
	 *
	 * @param raum    der {@link GostKlausurraum}, dessen Terminkontext verwendet wird
	 * @param klausur die {@link GostKursklausur}, deren Startzeit geprüft wird
	 *
	 * @return <code>true</code>, wenn die {@link GostKursklausur} eine vom Raumtermin abweichende Startzeit aufweist.
	 */
	public boolean hatAbweichendeStartzeitByRaumAndKursklausur(final @NotNull GostKlausurraum raum, final @NotNull GostKursklausur klausur) {
		final GostKlausurtermin termin = terminGetByRaumOrException(raum);
		return !((klausur.startzeit == null) || (termin.startzeit == null) || termin.startzeit.equals(klausur.startzeit));
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurtermin}en zu einer {@link GostSchuelerklausur} zurück.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der die {@link GostSchuelerklausurtermin}e gesucht werden.
	 *
	 * @return die Liste von {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeBySchuelerklausur(
			final @NotNull GostSchuelerklausur sk) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, sk.id);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausurtermin> list = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		return (list != null) ? list : new ArrayList<>();
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. werden Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code>, werden Fremdtermine am selben Datum wie <code>termin</code> aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @return die Liste von {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. sind Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(final @NotNull GostKlausurtermin termin,
			final boolean fremdTermine) {
		return fremdTermine
				? schuelerklausurterminaktuellGetMengeByTerminmenge(
						terminGetMengeByDatum(DeveloperNotificationException.ifNull("Termin muss ein Datum haben", termin.datum)))
				: schuelerklausurterminAktuellGetMengeByTermin(termin);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurtermin}en zu einer Menge von {@link GostKlausurtermin}en zurück.
	 *
	 * @param termine die Liste der {@link GostKlausurtermin}e
	 *
	 * @return die Liste von zugehörigen {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByTerminmenge(final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : termine) {
			final List<GostSchuelerklausurtermin> teilListe = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
			if (teilListe != null) {
				ergebnis.addAll(teilListe);
			}
		}
		return ergebnis;
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurtermin}en zu einer Menge von {@link GostKlausurtermin}en zurück.
	 *
	 * @param termine die Liste der {@link GostKlausurtermin}e
	 *
	 * @return die Liste von zugehörigen {@link GostSchuelerklausurtermin}en
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminaktuellGetMengeByTerminmenge(final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : termine) {
			ergebnis.addAll(schuelerklausurterminAktuellGetMengeByTermin(termin));
		}
		return ergebnis;
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur) {
		return schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(termin, kursklausur, false);
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück. Ggf. werden auch die jahrgangsübergreifenden datumsgleichen {@link GostKlausurtermin}e berücksichtigt.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 * @param multijahrgang wenn <code>true</code>, werden auch jahrgangsübergreifende datumsgleiche {@link GostKlausurtermin}e berücksichtigt.
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur, final boolean multijahrgang) {
		final List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>(_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(termin.id, kursklausur.id));
		if (multijahrgang && (termin.datum != null)) {
			for (final GostKlausurtermin terminMulti : terminSelbesDatumGetMengeByTermin(termin, false)) {
				ergebnis.addAll(_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(terminMulti.id, kursklausur.id));
			}
		}
		return ergebnis;
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurtermin}en zu einem {@link GostKlausurtermin} zurück.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellGetMengeByTermin(
			final @NotNull GostKlausurtermin termin) {
		return _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(termin.id);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausur}en zu einem Klausurtermin zurück.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostSchuelerklausur}en gesucht werden
	 *
	 * @return die Liste von {@link GostSchuelerklausur}en zu einem Klausurtermin
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausur> ergebnis = new ArrayList<>();
		final List<GostSchuelerklausurtermin> list = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (list == null) {
			return ergebnis;
		}
		for (final @NotNull GostSchuelerklausurtermin t : list) {
			ergebnis.add(schuelerklausurBySchuelerklausurtermin(t));
		}
		return ergebnis;
	}

	/**
	 * Prüft, ob der übergebene {@link GostSchuelerklausurtermin} der aktuellste Termin der zugehörigen {@link GostSchuelerklausur} ist.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, der auf Aktualität geprüft werden soll
	 *
	 * @return <code>true</code>, wenn es sich um den aktuellsten {@link GostSchuelerklausurtermin} handelt, sonst <code>false</code>
	 */
	public boolean istSchuelerklausurterminAktuell(final @NotNull GostSchuelerklausurtermin skt) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminaktuell_by_idSchuelerklausur, skt.idSchuelerklausur) == skt;
	}

	/**
	 * Gibt an, ob die übergebene {@link GostSchuelerklausur} aktiv ist, d.h. der Schüler mitschreibt.
	 *
	 * @param sk die zu prüfende {@link GostSchuelerklausur}
	 *
	 * @return true, falls der Schüler bei der Klausur mitschreibt
	 */
	public boolean istSchuelerklausurAktiv(final @NotNull GostSchuelerklausur sk) {
		return sk.aktiv;
	}

	/**
	 * Liefert den aktuellen {@link GostSchuelerklausurtermin} zu einer übergebenen
	 * {@link GostSchuelerklausur}
	 *
	 * @param schuelerklausur die {@link GostSchuelerklausur}, deren aktueller
	 *                          {@link GostSchuelerklausurtermin} gesucht wird
	 *
	 * @return den aktuellen {@link GostSchuelerklausurtermin} zur übergebenen {@link GostSchuelerklausur}
	 */
	public @NotNull GostSchuelerklausurtermin schuelerklausurterminAktuellBySchuelerklausur(
			final @NotNull GostSchuelerklausur schuelerklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminaktuell_by_idSchuelerklausur, schuelerklausur.id);
	}

	private @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellByKursklausur(
			final @NotNull GostKursklausur kursklausur) {
		return _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get2(kursklausur.id);
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen ein {@link GostKlausurtermin} zugewiesen wurde.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen ein {@link GostKlausurtermin} zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(
			final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		@NotNull List<GostSchuelerklausurtermin> ergebnis;
		if (quartal > 0) {

			ergebnis = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get123(abiturjahrgang, halbjahr.id, quartal);
			final Iterator<GostSchuelerklausurtermin> iterator = ergebnis.iterator();
			while (iterator.hasNext()) {
				final Long idTermin = iterator.next().idTermin;
				if ((idTermin == null) || (idTermin == _ID_OHNE_ZUORDNUNG)) {
					iterator.remove();
				}
			}
		} else {
			ergebnis = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get12(abiturjahrgang, halbjahr.id);
			final Iterator<GostSchuelerklausurtermin> iterator = ergebnis.iterator();
			while (iterator.hasNext()) {
				final Long idTermin = iterator.next().idTermin;
				if ((idTermin == null) || (idTermin == _ID_OHNE_ZUORDNUNG)) {
					iterator.remove();
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(
			final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			return _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get123(abiturjahrgang, halbjahr.id, quartal);
		}
		return _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get12(abiturjahrgang, halbjahr.id);
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen ein {@link GostKlausurtermin} inklusive Datum zugewiesen wurde.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen ein {@link GostKlausurtermin} inklusive Datum zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(
			final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin termin : schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(
				abiturjahrgang, halbjahr, quartal)) {
			final GostKlausurtermin t = terminOrNullBySchuelerklausurtermin(termin);
			if ((t != null) && (t.datum != null)) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen noch kein {@link GostKlausurtermin} zugewiesen wurde.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen noch kein {@link GostKlausurtermin} zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(
			final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final @NotNull List<GostSchuelerklausurtermin> skts = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
					.get1234(abiturjahrgang, halbjahr.id, quartal, _ID_OHNE_ZUORDNUNG);
			skts.sort(_compSchuelerklausurtermin);
			return skts;
		}
		final @NotNull List<GostSchuelerklausurtermin> skts = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
				.get124(abiturjahrgang, halbjahr.id, _ID_OHNE_ZUORDNUNG);
		skts.sort(_compSchuelerklausurtermin);
		return skts;
	}

	/**
	 * Liefert eine Liste von Haupttermin-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von Haupttermin-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellHtGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminAktuellGetMengeByTermin(termin)) {
			if (skt.folgeNr == 0) {
				ergebnis.add(skt);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminNtGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminGetMengeByTermin(termin)) {
			if (skt.folgeNr > 0) {
				ergebnis.add(skt);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurtermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminAktuellNtGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminAktuellGetMengeByTermin(termin)) {
			if (skt.folgeNr > 0) {
				ergebnis.add(skt);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert den {@link GostSchuelerklausurtermin}, sofern vorhanden, zu einem {@link GostKlausurtermin} und einer Schüler-ID.
	 *
	 * @param termin   der {@link GostKlausurtermin}
	 * @param idSchueler die ID des Schülers
	 *
	 * @return das {@link GostSchuelerklausurtermin} zu einem {@link GostKlausurtermin} und einer Schüler-ID, sonst <code>null</code>.
	 */
	public GostSchuelerklausurtermin schuelerklausurterminByTerminAndSchuelerid(final @NotNull GostKlausurtermin termin,
			final long idSchueler) {
		final List<GostSchuelerklausurtermin> skts = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts != null) {
			for (final @NotNull GostSchuelerklausurtermin skt : skts) {
				if (schuelerklausurGetByIdOrException(skt.idSchuelerklausur).idSchueler == idSchueler) {
					return skt;
				}
			}
		}
		return null;
	}

	/**
	 * Liefert die {@link GostSchuelerklausur}en zur übergebenen {@link GostKursklausur}
	 *
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die {@link GostSchuelerklausur}en zur übergebenen {@link GostKursklausur}
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurGetMengeByKursklausur(final @NotNull GostKursklausur kursklausur) {
		return _schuelerklausur_by_idKursklausur_and_idSchueler.get1(kursklausur.id);
	}

	/**
	 * Liefert die {@link GostSchuelerklausur} zur übergebenen {@link GostKursklausur} und zur Schüler-ID
	 *
	 * @param kursklausur die {@link GostKursklausur}
	 * @param idSchueler die ID des Schülers
	 *
	 * @return die {@link GostSchuelerklausur} zur übergebenen {@link GostKursklausur} und zur Schüler-ID
	 */
	public GostSchuelerklausur schuelerklausurByKursklausurAndSchuelerid(final @NotNull GostKursklausur kursklausur, final long idSchueler) {
		return _schuelerklausur_by_idKursklausur_and_idSchueler.getSingle12OrNull(kursklausur.id, idSchueler);
	}

	/**
	 * Liefert den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur} oder <code>null</code> falls kein Lehrer zugeordnet ist.
	 */
	public LehrerListeEintrag kursLehrerByKursklausur(final @NotNull GostKursklausur k) {
		final @NotNull KursDaten kurs = kursdatenByKursklausur(k);
		return (kurs.lehrer == null) ? null : getLehrerMap().get(kurs.lehrer);
	}


	/**
	 * Liefert das Lehrerkürzel zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das Lehrerkürzel zur übergebenen {@link GostKursklausur} oder <code>null</code> falls kein Lehrer zugeordnet ist.
	 */
	public String kursLehrerKuerzelByKursklausur(final @NotNull GostKursklausur k) {
		final LehrerListeEintrag lle = kursLehrerByKursklausur(k);
		return (lle == null) ? null : lle.kuerzel;
	}

	/**
	 * Liefert die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull KursDaten kursdatenByKursklausur(final @NotNull GostKursklausur k) {
		final KursDaten kurs = getKursManager().get(k.idKurs);
		return DeveloperNotificationException.ifNull("Kurs mit ID " + k.idKurs + " nicht in KursManager vorhanden.", kurs);
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull GostFach fachByKursklausur(final @NotNull GostKursklausur k) {
		final GostKlausurvorgabe vorgabe = vorgabeByKursklausur(k);
		return fachByVorgabe(vorgabe);
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 */
	public GostFach fachOrNullByKursklausur(final @NotNull GostKursklausur k) {
		final GostKlausurvorgabe vorgabe = vorgabeByKursklausur(k);
		return fachOrNullByVorgabe(vorgabe);
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKlausurvorgabe}.
	 *
	 * @param v die {@link GostKlausurvorgabe}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKlausurvorgabe}.
	 */
	public @NotNull GostFach fachByVorgabe(final @NotNull GostKlausurvorgabe v) {
		return DeveloperNotificationException.ifNull("Fach mit ID " + v.idFach + " nicht in GostFaecherManager vorhanden.",
				fachOrNullByVorgabe(v));
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKlausurvorgabe}.
	 *
	 * @param v die {@link GostKlausurvorgabe}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKlausurvorgabe}.
	 */
	public GostFach fachOrNullByVorgabe(final @NotNull GostKlausurvorgabe v) {
		return getFaecherManager(v.abiturjahrgang).get(v.idFach);
	}

	/**
	 * Liefert die Liste der Kursschienen des Kurses einer {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die Liste der Kursschienen des Kurses einer {@link GostKursklausur}.
	 */
	public @NotNull List<Integer> kursSchieneByKursklausur(final @NotNull GostKursklausur k) {
		return kursdatenByKursklausur(k).schienen;
	}

	/**
	 * Liefert die Kurzbezeichnung des Kurses zu einer übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die Kurzbezeichnung des Kurses zu einer übergebenen {@link GostKursklausur}.
	 */
	public @NotNull String kursKurzbezeichnungByKursklausur(final @NotNull GostKursklausur k) {
		return kursdatenByKursklausur(k).kuerzel;
	}

	/**
	 * Liefert die {@link KursDaten} zur übergebenen {@link GostSchuelerklausur}.
	 *
	 * @param k die {@link GostSchuelerklausur}
	 *
	 * @return die {@link KursDaten} zur übergebenen {@link GostSchuelerklausur}.
	 */
	public @NotNull KursDaten kursdatenBySchuelerklausur(final @NotNull GostSchuelerklausur k) {
		return kursdatenByKursklausur(kursklausurBySchuelerklausur(k));
	}

	/**
	 * Liefert die {@link KursDaten} zum übergebenen {@link GostSchuelerklausurtermin}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die {@link KursDaten} zum übergebenen {@link GostSchuelerklausurtermin}.
	 */
	public @NotNull KursDaten kursdatenBySchuelerklausurtermin(final @NotNull GostSchuelerklausurtermin k) {
		return kursdatenByKursklausur(kursklausurBySchuelerklausurtermin(k));
	}

	/**
	 * Liefert die Anzahl aller Schüler im Kurs zu einer übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}.
	 *
	 * @return die Anzahl aller Schüler im Kurs zu einer übergebenen {@link GostKursklausur}.
	 */
	public int kursAnzahlSchuelerGesamtByKursklausur(final @NotNull GostKursklausur k) {
		return kursdatenByKursklausur(k).schueler.size();
	}

	/**
	 * Liefert die Anzahl der Klausurscheiber im Kurs zu einer übergebenen
	 * {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die Anzahl der Klausurscheiber im Kurs zu einer übergebenen
	 * {@link GostKursklausur}.
	 */
	public int kursAnzahlKlausurschreiberByKursklausur(final @NotNull GostKursklausur k) {
		return schuelerklausurGetMengeByKursklausur(k).size();
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches zur übergebenen {@link GostKursklausur} als Aufruf der rgba-Funktion
	 * mit der Transparenz 1.0 zurück.
	 *
	 * @param k           die {@link GostKursklausur}
	 *
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public @NotNull String fachHTMLFarbeRgbaByKursklausur(final @NotNull GostKursklausur k) {
		final GostFach fach = fachOrNullByKursklausur(k);
		if (fach == null) {
			return "rgba(220,220,220,1.0)";
		}
		final GostKlausurvorgabe vorgabe = vorgabeByKursklausur(k);
		return Fach.getBySchluesselOrDefault(fach.kuerzel).getHMTLFarbeRGBA(vorgabe.abiturjahrgang - 1, 1.0);
	}

	/**
	 * Liefert den Vorgänger-{@link GostSchuelerklausurtermin}, sofern vorhanden, zu einem {@link GostSchuelerklausurtermin}, also den
	 * versäumten Schülerklausurtermin.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, dessen Vorgänger gesucht wird
	 *
	 * @return den Vorgänger-{@link GostSchuelerklausurtermin} oder <code>null</code>
	 */
	public GostSchuelerklausurtermin schuelerklausurterminVorgaengerBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin skt) {
		final @NotNull List<GostSchuelerklausurtermin> alleTermine = DeveloperNotificationException
				.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur);
		for (final @NotNull GostSchuelerklausurtermin skAktuell : alleTermine) {
			if (skAktuell.folgeNr == (skt.folgeNr - 1)) {
				return skAktuell;
			}
		}
		return null;
	}

	/**
	 * Prüft, ob eine {@link GostKursklausur} externe Klausurschreiber enthält.
	 *
	 * @param k die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls externe Schüler in der {@link GostKursklausur} enthalten sind, sonst <code>false</code>
	 */
	public boolean kursklausurMitExternenS(final @NotNull GostKursklausur k) {
		for (final @NotNull GostSchuelerklausur sk : schuelerklausurGetMengeByKursklausur(k)) {
			if (DeveloperNotificationException.ifMapGetIsNull(_schuelerlisteeintrag_by_id,
					sk.idSchueler).externeSchulNr != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob einem {@link GostKlausurtermin} Schüler anderer Jahrgangsstufen zugeordnet sind
	 *
	 * @param t der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code>, falls Schüler anderer Jahrgangsstufen zugeordnet sind
	 */
	public boolean terminMitAnderenJgst(final @NotNull GostKlausurtermin t) {
		final List<GostSchuelerklausurtermin> listSkts = _schuelerklausurterminmenge_by_idTermin.get(t.id);
		if (listSkts != null) {
			for (final @NotNull GostSchuelerklausurtermin skt : listSkts) {
				if (vorgabeBySchuelerklausurtermin(skt).abiturjahrgang != t.abiturjahrgang) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gibt das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurtermin}
	 * zurück. Falls kein Vorgängertermin existiert, wird eine <code>DeveloperNotificationException</code> geworfen. Falls noch kein Termin oder kein Datum zugewiesen ist, wird <code>null</code> zurückgegeben.
	 *
	 * @param sk der {@link GostSchuelerklausurtermin}, dessen Vorgänger-Datum gesucht wird.
	 *
	 * @return das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurtermin}
	 */
	public String datumSchuelerklausurHT(final @NotNull GostSchuelerklausur sk) {
		final GostKlausurtermin termin = terminOrNullBySchuelerklausur(sk);
		return (termin == null) ? null : termin.datum;
	}

	/**
	 * Gibt das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurtermin}
	 * zurück. Falls kein Vorgängertermin existiert, wird eine <code>DeveloperNotificationException</code> geworfen. Falls noch kein Termin oder kein Datum zugewiesen ist, wird <code>null</code> zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, dessen Vorgänger-Datum gesucht wird.
	 *
	 * @return das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurtermin}
	 */
	public String datumSchuelerklausurVorgaenger(final @NotNull GostSchuelerklausurtermin skt) {
		final @NotNull GostSchuelerklausurtermin vorgaengerSkt = DeveloperNotificationException.ifNull("Kein Vorgängertermin zu Schülerklausurtermin gefunden.",
				schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt));
		final GostKlausurtermin termin = terminOrNullBySchuelerklausurtermin(vorgaengerSkt);
		return (termin == null) ? null : termin.datum;
	}

	/**
	 * Prüft, ob ein Schüler an einem {@link GostKlausurtermin} gesetzt ist.
	 *
	 * @param idSchueler die ID des zu prüfenden Schülers
	 * @param termin   der {@link GostKlausurtermin}
	 *
	 * @return <code>true</code>, wenn der Schüler an dem {@link GostKlausurtermin} eine Klausur schreibt, sonst
	 *         <code>false</code>
	 */
	public boolean schuelerSchreibtKlausurtermin(final long idSchueler, final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausurtermin> skts = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts == null) {
			return false;
		}
		for (final @NotNull GostSchuelerklausurtermin skt : skts) {
			if ((schuelerklausurBySchuelerklausurtermin(skt).idSchueler == idSchueler)
					&& istSchuelerklausurterminAktuell(skt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert zu einer {@link GostKursklausur} die {@link GostSchuelerklausurtermin}e der Schüler, die den
	 * Kurs schriftlich belegt haben
	 *
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die {@link GostSchuelerklausurtermin}e der Schüler, die den
	 * Kurs schriftlich belegt haben
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByKursklausur(
			final @NotNull GostKursklausur kursklausur) {
		final List<GostSchuelerklausurtermin> ergebnis = _schuelerklausurterminmenge_by_idKursklausur
				.get(kursklausur.id);
		if (ergebnis == null) {
			return new ArrayList<>();
		}
		ergebnis.sort(_compSchuelerklausurtermin);
		return ergebnis;
	}

	/**
	 * Liefert zu einer {@link GostSchuelerklausur} den zugehörigen {@link SchuelerListeEintrag}
	 *
	 * @param sk die {@link GostSchuelerklausur}
	 *
	 * @return der zugehörige {@link SchuelerListeEintrag}
	 */
	public @NotNull SchuelerListeEintrag schuelerGetBySchuelerklausur(
			final @NotNull GostSchuelerklausur sk) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerlisteeintrag_by_id, sk.idSchueler);
	}

	/**
	 * Liefert zu einem {@link GostSchuelerklausurtermin} den zugehörigen {@link SchuelerListeEintrag}
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}
	 *
	 * @return der zugehörige {@link SchuelerListeEintrag}
	 */
	public @NotNull SchuelerListeEintrag schuelerGetBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin skt) {
		return schuelerGetBySchuelerklausur(schuelerklausurBySchuelerklausurtermin(skt));
	}

	/**
	 * Liefert die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 *
	 * @param raum       der {@link GostKlausurraum}
	 * @param zeitraster das {@link StundenplanZeitraster}
	 *
	 * @return die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 */
	public GostKlausurraumstunde raumstundeGetByRaumAndZeitrasterOrNull(final @NotNull GostKlausurraum raum, final @NotNull StundenplanZeitraster zeitraster) {
		return _raumstunde_by_idRaum_and_idZeitraster.getSingle12OrNull(raum.id, zeitraster.id);
	}

	/**
	 * Liefert die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 *
	 * @param raum       der {@link GostKlausurraum}
	 * @param zeitraster das {@link StundenplanZeitraster}
	 *
	 * @return die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 */
	public @NotNull GostKlausurraumstunde raumstundeGetByRaumAndZeitrasterOrException(final @NotNull GostKlausurraum raum,
			final @NotNull StundenplanZeitraster zeitraster) {
		return _raumstunde_by_idRaum_and_idZeitraster.getSingle12OrException(raum.id, zeitraster.id);
	}

	/**
	 * Liefert die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum} zurück.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum}
	 */
	public @NotNull List<GostKlausurraumstunde> raumstundeGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final List<GostKlausurraumstunde> stunden = _raumstundenmenge_by_idRaum.get(raum.id);
		return (stunden != null) ? stunden : new ArrayList<>();
	}

	private void setzeRaumZuSchuelerklausurenOhneUpdate(final @NotNull GostKlausurenPatchResponseData patchResponseData) {
		raumRemoveAllIfExistsNoCascadeOhneUpdate(patchResponseData.raumdaten.raeume);
		schuelerklausurraumstundeRemoveAllOhneUpdate(patchResponseData.schuelerklausurterminraumstundenGeloescht);
		raumstundeRemoveAllOhneUpdate(patchResponseData.raumstundenGeloescht);
		raumAddAllOhneUpdate(patchResponseData.raumdaten.raeume);
		raumstundeAddAllOhneUpdate(patchResponseData.raumdaten.raumstunden);
		schuelerklausurraumstundeAddAllOhneUpdate(patchResponseData.raumdaten.schuelerklausurterminRaumstunden);
	}

	/**
	 * Aktualisiert die internen Strukturen anhand der übergebenen {@link GostKlausurenPatchResponseData}. Diese Methode
	 * sollte nur nach einem API-Call aufgerufen werden, in dem das {@link GostKlausurenPatchResponseData}-Objekt erzeugt wurde.
	 *
	 * @param patchResponseData die {@link GostKlausurenPatchResponseData}
	 */
	public void setzeRaumZuSchuelerklausuren(final @NotNull GostKlausurenPatchResponseData patchResponseData) {
		setzeRaumZuSchuelerklausurenOhneUpdate(patchResponseData);

		update_all();
	}

	/**
	 * Liefert die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 * @param includeNachschreiber <code>true</code>, wenn auch Nachschreiber berücksichtigt werden sollen
	 *
	 * @return die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 */
	public @NotNull Set<GostKursklausur> kursklausurGetMengeByRaum(final @NotNull GostKlausurraum raum, final boolean includeNachschreiber) {
		final Set<GostKursklausur> kursklausuren = new HashSet<>();
		if (!_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.containsKey1(raum.id)) {
			return kursklausuren;
		}
		for (final GostSchuelerklausurtermin skt : _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get1(raum.id)) {
			if ((skt.folgeNr == 0) || includeNachschreiber) {
				kursklausuren.add(kursklausurBySchuelerklausurtermin(skt));
			}
		}
		return kursklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, wenn es sich um Nachschreibklausuren handelt.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, wenn es sich um Nachschreibklausuren handelt.
	 */
	public @NotNull Set<GostKursklausur> nachschreiberGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final Set<GostKursklausur> kursklausuren = new HashSet<>();
		if (!_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.containsKey1(raum.id)) {
			return kursklausuren;
		}
		for (final GostSchuelerklausurtermin skt : _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get1(raum.id)) {
			if (skt.folgeNr > 0) {
				kursklausuren.add(kursklausurBySchuelerklausurtermin(skt));
			}
		}
		return kursklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurtermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden und zu einer {@link GostKursklausur} gehören.
	 *
	 * @param raum der {@link GostKlausurraum}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByRaumAndKursklausur(final @NotNull GostKlausurraum raum,
			final @NotNull GostKursklausur kursklausur) {
		return _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get12OrException(raum.id, kursklausur.id);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausurtermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausurtermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		return schuelerklausurterminGetMengeByRaumid(raum.id);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausurtermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param idRaum die ID des {@link GostKlausurraum}s
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausurtermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByRaumid(final long idRaum) {
		return _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get1(idRaum);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausur}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausur}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final @NotNull List<GostSchuelerklausur> schuelerklausuren = new ArrayList<>();
		final @NotNull List<GostSchuelerklausurtermin> schuelerklausurtermine = schuelerklausurterminGetMengeByRaum(raum);
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurtermine) {
			schuelerklausuren.add(schuelerklausurBySchuelerklausurtermin(skt));
		}
		return schuelerklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurtermin}e zu einem {@link GostKlausurtermin} zurück, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Menge aller {@link GostSchuelerklausurtermin}e zu einem {@link GostKlausurtermin}, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurOhneRaumGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		return _schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.get12(_ID_OHNE_ZUORDNUNG, termin.id);
	}

	/**
	 * Liefert die {@link GostSchuelerklausurtermin}e eines {@link GostKlausurtermin}s, die bei einer Raumzuweisung des ganzen Termins einem Raum
	 * zugeordnet werden.
	 * <br>
	 * Anwendungsfall ist das Ziehen eines ganzen Klausurtermins in der Raum- und Zeitplanung auf einen Raum. Es werden nur die bisher raumlosen
	 * Schülerklausurtermine des Termins ermittelt. Änderungen an Raumzuweisungen werden nicht durchgeführt.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die {@link GostSchuelerklausurtermin}e für die Raumzuweisung
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFuerRaumzuweisungGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		return schuelerklausurOhneRaumGetMengeByTermin(termin);
	}

	/**
	 * Liefert die {@link GostSchuelerklausurtermin}e einer {@link GostKursklausur} in einem {@link GostKlausurtermin}, die bei einer
	 * Raumzuweisung der Kursklausur einem Raum zugeordnet werden. Dabei werden auch jahrgangsübergreifende datumsgleiche Termine berücksichtigt.
	 * <br>
	 * Anwendungsfall ist das Ziehen einer Kursklausur aus der Raum- und Zeitplanung auf einen Raum. Es werden die betroffenen
	 * Schülerklausurtermine ermittelt. Änderungen an Raumzuweisungen werden nicht durchgeführt.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die {@link GostSchuelerklausurtermin}e für die Raumzuweisung
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFuerRaumzuweisungGetMengeByTerminAndKursklausur(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKursklausur kursklausur) {
		return schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(termin, kursklausur, true);
	}

	/**
	 * Liefert den übergebenen {@link GostSchuelerklausurtermin} für eine Raumzuweisung.
	 * <br>
	 * Anwendungsfall ist das Ziehen eines einzelnen Schülerklausurtermins in der Raum- und Zeitplanung auf einen Raum. Änderungen an
	 * Raumzuweisungen werden nicht durchgeführt.
	 *
	 * @param schuelerklausurtermin der {@link GostSchuelerklausurtermin}
	 *
	 * @return der {@link GostSchuelerklausurtermin} für die Raumzuweisung
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFuerRaumzuweisungGetMengeBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		return ListUtils.create1(schuelerklausurtermin);
	}

	/**
	 * Liefert die {@link GostSchuelerklausurtermin}e einer {@link GostKursklausur} in einem {@link GostKlausurtermin}, deren Raumzuweisung
	 * aufgehoben wird.
	 * <br>
	 * Anwendungsfall ist das Zurücklegen einer Kursklausur aus einem Raum in die Planungsliste der Raum- und Zeitplanung. Es werden die betroffenen
	 * Schülerklausurtermine ermittelt. Änderungen an Raumzuweisungen werden nicht durchgeführt.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die {@link GostSchuelerklausurtermin}e für das Aufheben der Raumzuweisung
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFuerRaumzuweisungAufhebenGetMengeByTerminAndKursklausur(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKursklausur kursklausur) {
		return schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(termin, kursklausur);
	}

	/**
	 * Liefert den übergebenen {@link GostSchuelerklausurtermin} für das Aufheben einer Raumzuweisung.
	 * <br>
	 * Anwendungsfall ist das Zurücklegen eines einzelnen Schülerklausurtermins aus einem Raum in die Planungsliste der Raum- und Zeitplanung.
	 * Änderungen an Raumzuweisungen werden nicht durchgeführt.
	 *
	 * @param schuelerklausurtermin der {@link GostSchuelerklausurtermin}
	 *
	 * @return der {@link GostSchuelerklausurtermin} für das Aufheben der Raumzuweisung
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFuerRaumzuweisungAufhebenGetMengeBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurtermin schuelerklausurtermin) {
		return ListUtils.create1(schuelerklausurtermin);
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurtermin}e zu einem {@link GostKlausurraum} und {@link GostKlausurtermin} zurück.
	 *
	 * @param raum der {@link GostKlausurraum}
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Menge aller {@link GostSchuelerklausurtermin}e zu einem {@link GostKlausurraum} und {@link GostKlausurtermin} zurück.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminGetMengeByRaumAndTermin(final @NotNull GostKlausurraum raum,
			final @NotNull GostKlausurtermin termin) {
		return _schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.get12(raum.id, termin.id);
	}

	/**
	 * Liefert eine Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param multijahrgang ob die Liste für einen Termin oder für alle Termine des gleichen Datums gelten soll
	 *
	 * @return die Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 */
	public @NotNull List<StundenplanRaum> stundenplanraumVerfuegbarGetMengeByTermin(final @NotNull GostKlausurtermin termin, final boolean multijahrgang) {
		final List<StundenplanRaum> raeume = new ArrayList<>();
		final @NotNull List<GostKlausurtermin> termine = multijahrgang ? terminSelbesDatumGetMengeByTermin(termin, true) : ListUtils.create1(termin);
		for (final @NotNull StundenplanRaum raum : stundenplanManagerGetByTerminOrException(termin).raumGetMengeAsList()) {
			boolean raumVerwendet = false;
			for (final @NotNull GostKlausurtermin t : termine) {
				if (_raum_by_idTermin_and_idStundenplanraum.containsKey12(t.id, raum.id)) {
					raumVerwendet = true;
					break;
				}
			}
			if (!raumVerwendet) {
				raeume.add(raum);
			}
		}
		return raeume;
	}

	/**
	 * Liefert den {@link GostKlausurraum}, zu den übergebenen Parametern oder null
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param stundenplanRaum der {@link StundenplanRaum}
	 *
	 * @return den {@link GostKlausurraum}, zu den übergebenen Parametern oder null
	 */
	public GostKlausurraum raumGetByTerminUndStundenplanraum(final @NotNull GostKlausurtermin termin, final @NotNull StundenplanRaum stundenplanRaum) {
		return _raum_by_idTermin_and_idStundenplanraum.getSingle12OrNull(termin.id, stundenplanRaum.id);
	}

	/**
	 * Prüft, ob alle zu einer {@link GostKursklausur} gehörenden {@link GostSchuelerklausurtermin}e an einem bestimmten {@link GostKlausurtermin} einem {@link GostKlausurraum}
	 * zugeordnet sind. Wird kein {@link GostKlausurtermin} übergeben, wird der Haupttermin der {@link GostKursklausur} geprüft.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}. Wird kein <code>null</code> übergeben, wird der Haupttermin der {@link GostKursklausur} geprüft.
	 * @param kk die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, wenn alle {@link GostSchuelerklausurtermin}e verplant sind, sonst <code>false</code>.
	 */
	public boolean isKursklausurAlleSchuelerklausurenVerplant(final @NotNull GostKursklausur kk, final GostKlausurtermin termin) {
		final long idTermin = (termin != null) ? termin.id : DeveloperNotificationException.ifNull("idTermin der Kursklausur %d".formatted(kk.id), kk.idTermin);
		if (_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.containsKey12(idTermin, kk.id)) {
			final List<GostSchuelerklausurtermin> skts = _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(
					idTermin, kk.id);
			for (final @NotNull GostSchuelerklausurtermin sk : skts) {
				if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Prüft, ob alle zu einer {@link GostKlausurtermin} gehörenden {@link GostSchuelerklausurtermin}e einem {@link GostKlausurraum}
	 * zugeordnet sind.
	 *
	 * @param t der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code>, wenn alle {@link GostSchuelerklausurtermin}e verplant sind, sonst <code>false</code>.
	 */
	public boolean isTerminAlleSchuelerklausurenVerplant(final @NotNull GostKlausurtermin t) {
		if (!_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.containsKey1(t.id)) {
			return true;
		}
		for (final @NotNull GostSchuelerklausurtermin sk : _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(t.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob eine {@link GostKursklausur} im übergebenen {@link GostKlausurraum} enthalten ist.
	 *
	 * @param raum der {@link GostKlausurraum}, in dem die {@link GostKursklausur} geprüft wird
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, wenn die {@link GostKursklausur} im übergebenen {@link GostKlausurraum} enthalten ist, sonst <code>false</code>.
	 */
	public boolean containsKlausurraumKursklausur(final @NotNull GostKlausurraum raum, final @NotNull GostKursklausur kursklausur) {
		return _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.containsKey12(raum.id, kursklausur.id);
	}

	/**
	 * Liefert die gemeinsame Klausurdauer aller {@link GostKursklausur}en, die im übergebenen {@link GostKlausurraum} geschrieben werden.
	 * Falls die Dauern sich unterscheiden, wird <code>null</code> zurückgegeben.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Klausurdauern überprüft werden.
	 *
	 * @return die gemeinsame Klausurdauer aller {@link GostKursklausur}en oder <code>null</code>, falls keine solche existiert.
	 */
	public Integer getGemeinsameKursklausurdauerByKlausurraum(final @NotNull GostKlausurraum raum) {
		Integer dauer = null;
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaum(raum, true)) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(klausur);
			if (dauer == null) {
				dauer = vorgabe.dauer;
			}
			if (!dauer.equals(vorgabe.dauer)) {
				return null;
			}
		}
		return dauer;
	}

	/**
	 * Liefert die gemeinsame Klausurstartzeit aller {@link GostKursklausur}en, die im übergebenen {@link GostKlausurraum} geschrieben werden.
	 * Falls die Startzeiten sich unterscheiden, wird <code>null</code> zurückgegeben.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Startzeiten überprüft werden.
	 *
	 * @return die gemeinsame Klausurstartzeit aller {@link GostKursklausur}en oder <code>null</code>, falls keine solche existiert.
	 */
	public Integer getGemeinsamerKursklausurstartByKlausurraum(final @NotNull GostKlausurraum raum) {
		Integer start = null;
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaum(raum, true)) {
			final Integer effStart = startzeitByKlausurraumAndKursklausurOrNull(raum, klausur);
			if (effStart == null) {
				return null;
			}
			if (start == null) {
				start = effStart;
			} else if (!start.equals(effStart)) {
				return null;
			}
		}
		return start;
	}

	/**
	 * Liefert <code>true</code> zurück, falls {@link GostSchuelerklausurtermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code> zurück, falls {@link GostSchuelerklausurtermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 */
	public boolean isKlausurenInFremdraeumenByTermin(final @NotNull GostKlausurtermin termin) {
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminGetMengeByTermin(termin)) {
			final GostKlausurraum raum = _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
			if ((raum != null) && (raum.idTermin != terminOrExceptionBySchuelerklausurtermin(skt).id)) {
				return true;
			}
		}
		for (final @NotNull GostKlausurraum raum : raumGetMengeByTermin(termin)) {
			if (raumEnthaeltTerminfremdeKlausuren(raum)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert <code>true</code> zurück, falls {@link GostSchuelerklausurtermin}e des übergebenen {@link GostKlausurraum}s in zu {@link GostKlausurtermin}en anderer Jahrgangsstufen gehören, sonst <code>false</code>.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return <code>true</code> zurück, falls {@link GostSchuelerklausurtermin}e des übergebenen {@link GostKlausurraum}s in zu {@link GostKlausurtermin}en anderer Jahrgangsstufen gehören, sonst <code>false</code>.
	 */
	public boolean raumEnthaeltTerminfremdeKlausuren(final @NotNull GostKlausurraum raum) {
		return !schuelerklausurterminFremdterminGetMengeByRaum(raum).isEmpty();
	}

	/**
	 * Liefert die Liste von {@link GostSchuelerklausurtermin}en aus dem übergebenen {@link GostKlausurraum}, die einem raumfremden Klausurtermin zugeordnet sind.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return die Liste von {@link GostSchuelerklausurtermin}en aus dem übergebenen {@link GostKlausurraum}, die einem raumfremden Klausurtermin zugeordnet sind.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminFremdterminGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminGetMengeByRaum(raum)) {
			if ((raum.idTermin != terminOrExceptionBySchuelerklausurtermin(skt).id)) {
				ergebnis.add(skt);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert die Liste von {@link GostSchuelerklausurtermin}en aus dem übergebenen {@link GostKlausurraum}, die dem Raumtermin zugeordnet sind.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return die Liste von {@link GostSchuelerklausurtermin}en aus dem übergebenen {@link GostKlausurraum}, die dem Raumtermin zugeordnet sind.
	 */
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminRaumterminGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final @NotNull List<GostSchuelerklausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminGetMengeByRaum(raum)) {
			if ((raum.idTermin == terminOrExceptionBySchuelerklausurtermin(skt).id)) {
				ergebnis.add(skt);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurtermin} den zugehörigen {@link GostKlausurraum}, falls ein solcher schon zugeordnet ist.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, zu dem der {@link GostKlausurraum} gesucht wird.
	 *
	 * @return den {@link GostKlausurraum}, falls einer zugewiesen ist, sonst <code>null</code>.
	 */
	public GostKlausurraum raumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurtermin skt) {
		return _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurtermin} zugehörigen {@link StundenplanRaum} zurück.
	 *
	 * @param skt der {@link GostSchuelerklausurtermin}, zu dem der {@link StundenplanRaum} gesucht wird.
	 *
	 * @return den {@link StundenplanRaum}, falls einer zugewiesen ist, sonst <code>null</code>
	 */
	public StundenplanRaum stundenplanraumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurtermin skt) {
		final GostKlausurraum raum = raumGetBySchuelerklausurtermin(skt);
		return ((raum == null) || (raum.idStundenplanRaum == null)) ? null
				: stundenplanManagerGetByTerminOrException(terminOrExceptionBySchuelerklausurtermin(skt)).raumGetByIdOrException(raum.idStundenplanRaum);
	}

	/**
	 * Liefert die Menge von {@link GostKlausurtermin}en aus anderen Jahrgangsstufen, die am selben Datum wie der übergebene {@link GostKlausurtermin} terminiert sind. Der als Parameter übergebene {@link GostKlausurtermin} <code>termin</code> ist in der Rückgabemenge nicht enthalten.
	 *
	 * @param termin der {@link GostKlausurtermin}, an dessen Datum jahrgangsfremde {@link GostKlausurtermin}e gesucht werden. Dieser {@link GostKlausurtermin} ist in der Rückgabeliste nicht enthalten.
	 *
	 * @return die Menge von {@link GostKlausurtermin}en aus anderen Jahrgangsstufen, die am selben Datum wie der übergebene {@link GostKlausurtermin} terminiert sind.
	 */
	public @NotNull List<GostKlausurtermin> getFremdTermineByTermin(final @NotNull GostKlausurtermin termin) {
		return terminSelbesDatumGetMengeByTermin(termin, false);
	}

	/**
	 * Prüft, ggf. jahrgangsübergreifend, ob {@link GostSchuelerklausurtermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 *
	 * @param termin der {@link GostKlausurtermin}, dessen {@link GostSchuelerklausurtermin}e geprüft werden
	 * @param fremdTermine wenn <code>true</code>, werden auch {@link GostSchuelerklausurtermin}e anderer Jahrgänge am selben Datum berücksichtigt.
	 *
	 * @return <code>true</code>, falls {@link GostSchuelerklausurtermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 */
	public boolean isSchuelerklausurenInRaumByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		for (final @NotNull GostSchuelerklausurtermin teilTermin : schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(termin, fremdTermine)) {
			if (_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(teilTermin.id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e zum als Parameter übergebenen {@link GostKlausurtermin}.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKlausurraum}e gesucht werden
	 *
	 * @return die Menge der {@link GostKlausurraum}e zum als Parameter übergebenen {@link GostKlausurtermin}.
	 */
	public @NotNull List<GostKlausurraum> raumGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostKlausurraum> raeume = _raummenge_by_idTermin.get(termin.id);
		return (raeume == null) ? new ArrayList<>() : raeume;
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e zu den als Parameter übergebenen {@link GostKlausurtermin} und {@link GostKursklausur}.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKlausurraum}e gesucht werden
	 * @param klausur die {@link GostKursklausur}, zu der die {@link GostKlausurraum}e gesucht werden
	 *
	 * @return die Menge der {@link GostKlausurraum}e zu den als Parameter übergebenen {@link GostKlausurtermin} und {@link GostKursklausur}.
	 */
	public @NotNull List<GostKlausurraum> raumGetMengeByTerminAndKursklausur(final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur) {
		return _raummenge_by_idTermin_and_idKursklausur.get12(termin.id, klausur.id);
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e, ggf. jahrgangsübergreifend, zum als Parameter übergebenen {@link GostKlausurtermin}.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKlausurraum}e gesucht werden
	 * @param fremdTermine wenn <code>true</code> werden auch die {@link GostKlausurraum}e von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen zurückgegeben
	 *
	 * @return die Menge der {@link GostKlausurraum}e, ggf. jahrgangsübergreifend, zum als Parameter übergebenen {@link GostKlausurtermin}.
	 */
	public @NotNull List<GostKlausurraum> raumGetMengeByTerminIncludingFremdtermine(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		return fremdTermine ? raumGetMengeByTerminmenge(terminSelbesDatumGetMengeByTermin(termin, true)) : raumGetMengeByTermin(termin);
	}


	/**
	 * Liefert die Menge der {@link GostKlausurraum}e zur als Parameter übergebenen {@link GostKlausurtermin}menge.
	 *
	 * @param termine die Menge der {@link GostKlausurtermin}e, zu denen die {@link GostKlausurraum}e gesucht werden
	 *
	 * @return die Menge der {@link GostKlausurraum}e zur als Parameter übergebenen {@link GostKlausurtermin}menge.
	 */
	public @NotNull List<GostKlausurraum> raumGetMengeByTerminmenge(final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<GostKlausurraum> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : termine) {
			final List<GostKlausurraum> teilListe = _raummenge_by_idTermin.get(termin.id);
			if (teilListe != null) {
				ergebnis.addAll(teilListe);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert die Summe der Plätze in allen {@link GostKlausurraum}en eines {@link GostKlausurtermin}s, ggf. jahrgangsübergreifend.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die Summe der Plätze gesucht wird
	 * @param fremdTermine wenn <code>true</code> werden auch die Plätze in {@link GostKlausurraum}en von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen addiert
	 *
	 * @return die Summe der Plätze in allen {@link GostKlausurraum}en eines {@link GostKlausurtermin}s, ggf. jahrgangsübergreifend.
	 */
	public int anzahlPlaetzeAlleRaeumeByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		int kapazitaet = 0;
		for (final @NotNull GostKlausurraum raum : raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine)) {
			if (raum.idStundenplanRaum != null) {
				kapazitaet += stundenplanManagerGetByTerminOrException(termin).raumGetByIdOrException(raum.idStundenplanRaum).groesse;
			}
		}
		return kapazitaet;
	}

	/**
	 * Liefert die Anzahl der benötigten Plätze für alle Schüler eines {@link GostKlausurtermin}s, ggf. jahrgangsübergreifend.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die Anzahl der benötigten Plätze gesucht wird
	 * @param fremdTermine wenn <code>true</code> werden auch die benötigten Plätze von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen addiert
	 *
	 * @return die Anzahl der benötigten Plätze für alle Schüler eines {@link GostKlausurtermin}s, ggf. jahrgangsübergreifend.
	 */
	public int anzahlBenoetigtePlaetzeAlleKlausurenByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		return schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(termin, fremdTermine).size();
	}

	/**
	 * Prüft, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurtermin}en ausreichend ist.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die vorhandenen und benötigten Plätze von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 *
	 * @return <code>true</code>, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurtermin}en ausreichend ist.
	 */
	public boolean isPlatzkapazitaetAusreichendByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		return anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin, fremdTermine) <= anzahlPlaetzeAlleRaeumeByTermin(termin, fremdTermine);
	}

	/**
	 * Erzeugt aus einer Liste von {@link GostSchuelerklausurtermin}en eine um z. B. für Blockungs-Algorithmen relevante Informationen angereicherte Liste von {@link GostSchuelerklausurterminRich}-Objekten.
	 *
	 * @param termine die Liste der {@link GostSchuelerklausurtermin}e.
	 *
	 * @return die Liste von angereicherten {@link GostSchuelerklausurterminRich}-Objekten
	 */
	public @NotNull List<GostSchuelerklausurterminRich> enrichSchuelerklausurtermine(final @NotNull List<GostSchuelerklausurtermin> termine) {
		final @NotNull List<GostSchuelerklausurterminRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurtermin termin : termine) {
			ergebnis.add(new GostSchuelerklausurterminRich(termin, this));
		}
		return ergebnis;
	}

	/**
	 * Erzeugt aus einer Liste von {@link GostKlausurraum}en eine um z. B. für Blockungs-Algorithmen relevante Informationen angereicherte Liste von {@link GostKlausurraumRich}-Objekten.
	 *
	 * @param raeume die Liste der {@link GostKlausurraum}e.
	 *
	 * @return die Liste von angereicherten {@link GostKlausurraumRich}-Objekten
	 */
	public @NotNull List<GostKlausurraumRich> enrichKlausurraeume(final @NotNull List<GostKlausurraum> raeume) {
		final @NotNull List<GostKlausurraumRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurraum raum : raeume) {
			ergebnis.add(new GostKlausurraumRich(raum, stundenplanraumGetByKlausurraum(raum)));
		}
		return ergebnis;
	}

	/**
	 * Liefert den {@link StundenplanRaum} zu einem übergebenen {@link GostKlausurraum}. Falls kein {@link StundenplanRaum} zugeordnet ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return der zugehörige {@link StundenplanRaum}
	 */
	public @NotNull StundenplanRaum stundenplanraumGetByKlausurraum(final @NotNull GostKlausurraum raum) {
		final @NotNull StundenplanManager spm = stundenplanManagerGetByTerminOrException(terminGetByIdOrException(raum.idTermin));
		return DeveloperNotificationException.ifNull("Stundenplan %d enthält keinen Raum zur ID %d".formatted(spm.stundenplanGetID(), raum.idStundenplanRaum),
				spm
						.raumGetByIdOrNull(DeveloperNotificationException.ifNull("StundenplanRaum darf nicht NULL sein", raum.idStundenplanRaum)));
	}

	/**
	 * Liefert den {@link StundenplanRaum} zu einem übergebenen {@link GostKlausurraum}. Falls kein {@link StundenplanRaum} zugeordnet ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return der zugehörige {@link StundenplanRaum}
	 */
	public StundenplanRaum stundenplanraumGetByKlausurraumOrNull(final @NotNull GostKlausurraum raum) {
		return (raum.idStundenplanRaum == null) ? null
				: stundenplanManagerGetByTerminOrException(terminGetByIdOrException(raum.idTermin)).raumGetByIdOrException(raum.idStundenplanRaum);
	}

	/**
	 * Prüft, ob allen zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}en ein {@link StundenplanRaum} zugewiesen ist.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die {@link GostKlausurraum}e von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 * @param nurVerwendet prüft nur Räume, in denen Schülerklausuren geplant wurden.
	 *
	 * @return <code>true</code>, falls allen zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}en ein {@link StundenplanRaum} zugewiesen ist.
	 */
	public boolean alleRaeumeHabenStundenplanRaumByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine, final boolean nurVerwendet) {
		for (final @NotNull GostKlausurraum raum : raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine)) {
			if ((raum.idStundenplanRaum == null) && (!nurVerwendet || !schuelerklausurterminGetMengeByRaum(raum).isEmpty())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob alle zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}e ausreichend Platzkapazität haben.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die {@link GostKlausurraum}e von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 *
	 * @return <code>true</code>, falls alle zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}e ausreichend Platzkapazität haben.
	 */
	public boolean alleRaeumeHabenAusreichendKapazitaetByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		for (final @NotNull GostKlausurraum raum : raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine)) {
			if (!raumHatAusreichendKapazitaetByRaum(raum)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prüft, ob der übergebene {@link GostKlausurraum} ausreichend Platzkapazität hat.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return <code>true</code>, falls der übergebene {@link GostKlausurraum} ausreichend Platzkapazität hat.
	 */
	public boolean raumHatAusreichendKapazitaetByRaum(final @NotNull GostKlausurraum raum) {
		return ((raum.idStundenplanRaum == null) || (schuelerklausurterminGetMengeByRaum(raum).size() <= stundenplanraumGetByKlausurraum(raum).groesse));
	}

	/**
	 * Prüft, ob die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 */
	public boolean hatRaumzuteilungByKursklausur(final @NotNull GostKursklausur klausur) {
		for (final @NotNull GostSchuelerklausurtermin skt : schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(terminOrExceptionByKursklausur(klausur),
				klausur)) {
			final List<GostKlausurraumstunde> stunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(skt.id);
			if ((stunden != null) && !stunden.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert die Menge von {@link GostSchuelerklausur}en zum übergebenen Abiturjahrgang, die zu keinem Schüler im Jahrgang gehören.
	 *
	 * @param abijahrgang der Abiturjahrgang
	 *
	 * @return die Menge von {@link GostSchuelerklausur}en zum übergebenen Abiturjahrgang, die zu keinem Schüler im Jahrgang gehören.
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurOhneSchuelerGetMengeByJahrgang(final int abijahrgang) {
		final @NotNull List<GostSchuelerklausur> ergebnis = new ArrayList<>();
		final Map<Long, List<GostSchuelerklausur>> sksMap = _schuelerklausurmenge_by_abijahr_and_idSchueler.getSubMapOrNull(abijahrgang);
		if ((sksMap == null) || sksMap.isEmpty()) {
			return ergebnis;
		}
		for (final @NotNull Entry<Long, List<GostSchuelerklausur>> sk : sksMap.entrySet()) {
			final SchuelerListeEintrag schueler = _schuelerlisteeintrag_by_id.get(sk.getKey());
			if (!sk.getValue().isEmpty() && ((schueler == null) || (schueler.abiturjahrgang != abijahrgang))) {
				ergebnis.addAll(sk.getValue());
			}
		}
		return ergebnis;
	}

	private boolean ignoreVorgabeMatches(final @NotNull GostKlausurvorgabe v, final @NotNull GostKlausurvorgabe i) {
		return (v.halbjahr == i.halbjahr) && (v.quartal == i.quartal) && (v.idFach == i.idFach) && v.kursart.equals(i.kursart);
	}

	private boolean vorgabeIsIgnored(final @NotNull GostKlausurvorgabe vorgabe,	final @NotNull List<GostKlausurvorgabe> ignoreVorgaben) {
		for (final GostKlausurvorgabe ign : ignoreVorgaben) {
			if (ignoreVorgabeMatches(vorgabe, ign)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKlausurvorgabe}n zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 * @param ignoreVorgaben eine Liste von {@link GostKlausurvorgabe}n, die ignoriert werden sollen
	 *
	 * @return die Liste von fehlenden {@link GostKlausurvorgabe}n
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabefehlendGetMengeByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final List<GostKlausurvorgabe> ignoreVorgaben) {
		List<GostKlausurvorgabe> alle;
		if (quartal == 0) {
			alle = _vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesOfMap3AsList(abiturjahrgang, halbjahr.id);
		} else {
			alle = _vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesOfMap4AsList(abiturjahrgang, halbjahr.id, quartal);
		}
		if ((ignoreVorgaben == null) || ignoreVorgaben.isEmpty()) {
			return alle;
		}
		final List<GostKlausurvorgabe> result = new ArrayList<>();
		for (final GostKlausurvorgabe vorgabe : alle) {
			if (!vorgabeIsIgnored(vorgabe, ignoreVorgaben)) {
				result.add(vorgabe);
			}
		}
		return result;
	}

	/**
	 * Gibt das fehlende {@link GostKlausurvorgabe}-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     das Quartal
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return das fehlende {@link GostKlausurvorgabe}-Objekt
	 */
	public GostKlausurvorgabe vorgabefehlendGetByHalbjahrAndQuartalAndKursartallgAndFachid(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getOrNull(
				abiturjahrgang, halbjahr.id, quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKursklausur}en zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von fehlenden {@link GostKursklausur}en
	 */
	public @NotNull List<GostKursklausur> kursklausurfehlendGetMengeByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		if (quartal == 0) {
			return _kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesOfMap3AsList(abiturjahrgang, halbjahr.id);
		}
		return _kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesOfMap4AsList(abiturjahrgang, halbjahr.id, quartal);
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKursklausur}en zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von fehlenden {@link GostKursklausur}en
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		if (quartal == 0) {
			return _schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesOfMap3AsList(abiturjahrgang,
					halbjahr.id);
		}
		return _schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesOfMap4AsList(abiturjahrgang,
				halbjahr.id, quartal);
	}

	/**
	 * Liefert die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 * @param kwErrorLimit das Errorlimit für die Anzahl der Klausuren pro Schüler und Woche
	 * @param ignoreVorgaben eine Liste von {@link GostKlausurvorgabe}n, die bei der Zählung ignoriert werden sollen
	 *
	 * @return die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public int planungsfehlerGetAnzahlByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final int kwErrorLimit, final List<GostKlausurvorgabe> ignoreVorgaben) {
		int anzahl = 0;
		anzahl += vorgabefehlendGetMengeByHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal, ignoreVorgaben).size();
		anzahl += kursklausurfehlendGetMengeByHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(abiturjahrgang, halbjahr, quartal, kwErrorLimit, -1).size();
		anzahl += terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		if (!stundenplanManagerGeladenAndExistsByAbschnitt(
				DeveloperNotificationException.ifMap2DGetIsNull(_schuljahresabschnitt_by_abijahr_and_halbjahr, abiturjahrgang, halbjahr.id))) {
			anzahl++;
		}
		return anzahl;
	}

	/**
	 * Liefert die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 * @param kwWarnLimit die Anzahl der Klausuren pro Schüler und Woche, ab der eine Warnung ausgegeben wird
	 * @param kwErrorLimit die Anzahl der Klausuren pro Schüler und Woche, ab der ein Fehler ausgegeben wird
	 *
	 * @return die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public int planungshinweiseGetAnzahlByHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final int kwWarnLimit, final int kwErrorLimit) {
		int anzahl = 0;
		anzahl += terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		if (terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).isEmpty()) { // sonst kann die Raumkapazität nicht geprüft werden
			anzahl += terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		}
		anzahl += schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal).size();
		anzahl += klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(abiturjahrgang, halbjahr, quartal, kwWarnLimit, kwErrorLimit).size();
		return anzahl;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die Schülerkonflikte beinhalten zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, die Schülerkonflikte beinhalten zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public @NotNull List<GostKlausurtermin> terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		final @NotNull List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal)) {
			if (konflikteAnzahlGetByTermin(termin) > 0) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von datierten {@link GostKlausurtermin}en, die keinen validen Stundenplan haben zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von datierten {@link GostKlausurtermin}en, die keinen validen Stundenplan haben zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public @NotNull List<GostKlausurtermin> terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		final @NotNull List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal)) {
			if (stundenplanManagerGetByTerminOrNull(termin) == null) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, bei denen die Raumzuweisung noch unvollständig ist zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, bei denen die Raumzuweisung noch unvollständig ist zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public @NotNull List<GostKlausurtermin> terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		final @NotNull List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal)) {
			if (!isTerminAlleSchuelerklausurenVerplant(termin) || !alleRaeumeHabenStundenplanRaumByTermin(termin, false, true)) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, bei denen die Platzkapazität nicht ausreichend ist zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiturjahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, bei denen die Platzkapazität nicht ausreichend ist zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public @NotNull List<GostKlausurtermin> terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiturjahrgang,
			final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		final @NotNull List<GostKlausurtermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiturjahrgang, halbjahr, quartal)) {
			if (!alleRaeumeHabenAusreichendKapazitaetByTermin(termin, false)) {
				ergebnis.add(termin);
			}
		}
		return ergebnis;
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param kursklausur die {@link GostKursklausur}, zu der die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public void kursklausurPatchAttributesAndSetzeRaumZuSchuelerklausuren(final @NotNull GostKursklausur kursklausur,
			final @NotNull GostKlausurenPatchResponseData raumData) {
		kursklausurPatchAttributesOhneUpdate(kursklausur);
		setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		update_all();
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param termin der {@link GostKlausurtermin}, zu dem die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public void terminPatchAttributesAndSetzeRaumZuSchuelerklausuren(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKlausurenPatchResponseData raumData) {
		setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		for (final @NotNull GostSchuelerklausurtermin skt : raumData.schuelerklausurterminePatched) {
			schuelerklausurterminPatchAttributesOhneUpdate(skt);
		}
		terminPatchAttributesOhneUpdate(termin);
		update_all();
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param schuelerklausurtermin der {@link GostSchuelerklausurtermin}, zu dem die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public void schuelerklausurterminPatchAttributesAndSetzeRaumZuSchuelerklausuren(final @NotNull GostSchuelerklausurtermin schuelerklausurtermin,
			final @NotNull GostKlausurenPatchResponseData raumData) {
		schuelerklausurterminPatchAttributesOhneUpdate(schuelerklausurtermin);
		setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		update_all();
	}

	/**
	 * Liefert eine Liste von {@link GostSchuelerklausurterminraumstunde}n, die zu den übergebenen {@link GostKlausurraumstunde}n gehören.
	 * @param raumStunden die Liste von {@link GostKlausurraumstunde}n, zu denen die {@link GostSchuelerklausurterminraumstunde}n geliefert werden sollen
	 * @return die Liste von {@link GostSchuelerklausurterminraumstunde}n, die zu den übergebenen {@link GostKlausurraumstunde}n gehören
	 */
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurraumstundeGetMengeByKlausurraumstundenmenge(
			final @NotNull List<GostKlausurraumstunde> raumStunden) {
		final @NotNull List<GostSchuelerklausurterminraumstunde> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurraumstunde stunde : raumStunden) {
			final List<GostSchuelerklausurterminraumstunde> listStunden = _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get2(stunde.id);
			ergebnis.addAll(listStunden);
		}
		return ergebnis;
	}

	/**
	 * Liefert die Stundenplanzeitraster-Menge zu einem Klausurraum
	 * @param raum der Klausurraum
	 * @return die Stundenplanzeitraster-Menge zu einem Klausurraum
	 */
	public @NotNull List<StundenplanZeitraster> zeitrasterGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final @NotNull List<StundenplanZeitraster> ergebnis = new ArrayList<>();
		final @NotNull StundenplanManager stundenplanManager = stundenplanManagerGetByTerminOrException(terminGetByRaumOrException(raum));
		for (final @NotNull GostKlausurraumstunde stunde : raumstundeGetMengeByRaum(raum)) {
			if (stunde.idZeitraster != null) {
				final StundenplanZeitraster zr = stundenplanManager.zeitrasterGetByIdOrNull(stunde.idZeitraster);
				if (zr != null) {
					ergebnis.add(zr);
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausur}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausur> schuelerklausurGetMengeAsListSortedByDatumHT() {
		final List<GostSchuelerklausur> sorted = new ArrayList<>(_schuelerklausurmenge);
		sorted.sort(_compSchuelerklausurByDatumHT);
		return sorted;
	}

	/**
	 * Berechnet die Klausurdauer laut APO-GOSt basierend auf der übergebenen Vorgabe.
	 *
	 * @param vorgabe  die {@link GostKlausurvorgabe}, die die notwendigen Informationen wie
	 *                 Halbjahr, Kursart und Abiturjahrgang für die Berechnung liefert.
	 * @return die berechnete Klausurdauer in Minuten als {@code int}.
	 */
	public int berechneGostKlausurdauerByVorgabe(final @NotNull GostKlausurvorgabe vorgabe) {
		final GostHalbjahr halbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
		final GostKursart kursart = GostKursart.fromKuerzelOrException(vorgabe.kursart);
		final GostFach fach = fachByVorgabe(vorgabe);
		return berechneGostKlausurdauerByHalbjahrAndKursartAndFach(halbjahr, kursart, fach, vorgabe.abiturjahrgang);
	}

	/**
	 * Berechnet die Klausurdauer gemäß Halbjahr, Kursart, Fach und Abiturjahrgang.
	 * Die Klausurdauer richtet sich nach den APO-GOSt-Vorgaben, abhängig vom Halbjahr und
	 * dem Abiturjahrgang (alte oder neue Verordnung).
	 *
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param kursart die {@link GostKursart}
	 * @param fach das {@link GostFach}
	 * @param abiturjahrgang der Abiturjahrgang
	 * @return die berechnete Klausurdauer in Minuten
	 */
	public static int berechneGostKlausurdauerByHalbjahrAndKursartAndFach(final @NotNull GostHalbjahr halbjahr, final @NotNull GostKursart kursart, final @NotNull GostFach fach,
			final int abiturjahrgang) {
		if (halbjahr.istEinfuehrungsphase()) {
			return 90;
		}
		if (halbjahr.id == 5) { // Abiturhalbjahr
			return berechneAbiturKlausurdauer(kursart, fach);
		}
		if (abiturjahrgang < 2030) { // Alte APO-GOSt
			if (halbjahr.id <= 3) {
				return (kursart == GostKursart.LK) ? 180 : 135;
			}
			if (halbjahr.id == 4) {
				return (kursart == GostKursart.LK) ? 225 : 180;
			}
		} else { // Neue APO-GOSt
			if (halbjahr.id <= 3) {
				return (kursart == GostKursart.LK) ? 135 : 90;
			}
			if (halbjahr.id == 4) {
				return (kursart == GostKursart.LK) ? 180 : 135;
			}
		}
		throw new DeveloperNotificationException("Berechnung Klausurdauer fehlgeschlagen.");
	}

	private static int berechneAbiturKlausurdauer(final @NotNull GostKursart kursart, final @NotNull GostFach fach) {
		// Alte Sprachen
		if (fach.kuerzel.matches("^[GLH]\\d?$")) {
			if (!fach.istFremdSpracheNeuEinsetzend) {
				return (kursart == GostKursart.LK) ? 300 : 240; // fortgeführt
			}
			return 210; // GK neu einsetzend
		}

		// Moderne Fremdsprachen
		if (fach.istFremdsprache) {
			if (!fach.istFremdSpracheNeuEinsetzend) {
				return (kursart == GostKursart.LK) ? 315 : 285; // fortgeführt
			}
			return 255; // GK neu einsetzend
		}

		// Naturwissenschaften
		if (List.of(Fach.BI.toString(), Fach.CH.toString(), Fach.PH.toString()).contains(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 300 : 255;
		}

		if (Fach.D.toString().equals(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 315 : 255;
		}

		if (Fach.M.toString().equals(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 300 : 255;
		}

		// Informatik, Ernährungslehre, Technik
		if (List.of(Fach.IF.toString(), Fach.EL.toString(), Fach.TC.toString()).contains(fach.kuerzel)) {
			return (kursart == GostKursart.LK) ? 270 : 225;
		}

		// alle anderen Fächer
		return (kursart == GostKursart.LK) ? 300 : 240;
	}

}

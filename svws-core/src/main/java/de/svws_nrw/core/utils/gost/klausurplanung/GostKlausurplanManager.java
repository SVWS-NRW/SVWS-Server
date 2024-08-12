package de.svws_nrw.core.utils.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.svws_nrw.core.adt.PairNN;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.HashMap3D;
import de.svws_nrw.core.adt.map.HashMap4D;
import de.svws_nrw.core.adt.map.HashMap5D;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionMetaData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionRaumData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionSkrsKrsData;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenUpdate;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurtermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurvorgabe;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTerminRich;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.KursManager;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.Map2DUtils;
import de.svws_nrw.core.utils.Map3DUtils;
import de.svws_nrw.core.utils.Map4DUtils;
import de.svws_nrw.core.utils.MapUtils;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zur Handhabung von Daten der Gost-Klausurplanung.
 * Es können Daten mehrerer Abiturjahrgänge und Gost-Halbjahre verwaltet.
 */
public class GostKlausurplanManager {

	// externe Manager, klausurplanfremde Daten
	private GostFaecherManager _faecherManager;
	private KursManager _kursManager = null;
	private StundenplanManager _stundenplanManager;
	private Map<Long, LehrerListeEintrag> _lehrerMap = null;
	private Map<Long, SchuelerListeEintrag> _schuelerMap = null;

	// Status des Managers
	private boolean _vorgabenInitialized = false;
	private boolean _klausurenInitialized = false;
	private final @NotNull Set<Long> _terminidmenge_manager_enthaelt_raumdata = new HashSet<>();

	// Comparators
	private final @NotNull Comparator<GostKlausurvorgabe> _compVorgabe =
			(final @NotNull GostKlausurvorgabe a, final @NotNull GostKlausurvorgabe b) -> {
				if (a.kursart.compareTo(b.kursart) < 0)
					return +1;
				if (a.kursart.compareTo(b.kursart) > 0)
					return -1;
				if (getFaecherManagerOrNull() != null) {
					final GostFach aFach = getFaecherManager().get(a.idFach);
					final GostFach bFach = getFaecherManager().get(b.idFach);
					if ((aFach != null) && (bFach != null)) {
						if (aFach.sortierung > bFach.sortierung)
							return +1;
						if (aFach.sortierung < bFach.sortierung)
							return -1;
					}
				}
				if (a.halbjahr != b.halbjahr)
					return Integer.compare(a.halbjahr, b.halbjahr);
				return Integer.compare(a.quartal, b.quartal);
			};

	private static final @NotNull Comparator<GostKlausurtermin> _compTermin = (final @NotNull GostKlausurtermin a,
			final @NotNull GostKlausurtermin b) -> {
		if ((a.datum != null) && (b.datum != null))
			return a.datum.compareTo(b.datum);
		if (b.datum != null)
			return +1;
		if (a.datum != null)
			return -1;
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostKursklausur> _compKursklausur = (final @NotNull GostKursklausur a,
			final @NotNull GostKursklausur b) -> {
		final @NotNull GostKlausurvorgabe va = vorgabeByKursklausur(a);
		final @NotNull GostKlausurvorgabe vb = vorgabeByKursklausur(b);
		if (va.kursart.compareTo(vb.kursart) < 0)
			return +1;
		if (va.kursart.compareTo(vb.kursart) > 0)
			return -1;
		if (_faecherManager != null) {
			final GostFach aFach = _faecherManager.get(va.idFach);
			final GostFach bFach = _faecherManager.get(vb.idFach);
			if ((aFach != null) && (bFach != null)) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (va.halbjahr != vb.halbjahr)
			return va.halbjahr - vb.halbjahr;
		if (va.quartal != vb.quartal)
			return va.quartal - vb.quartal;
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostSchuelerklausur> _compSchuelerklausur = (final @NotNull GostSchuelerklausur a,
			final @NotNull GostSchuelerklausur b) -> {
		final GostKlausurvorgabe aV = vorgabeBySchuelerklausur(a);
		final GostKlausurvorgabe bV = vorgabeBySchuelerklausur(b);
		if (aV.quartal != bV.quartal)
			return aV.quartal - bV.quartal;
		if (aV.kursart.compareTo(bV.kursart) < 0)
			return +1;
		if (aV.kursart.compareTo(bV.kursart) > 0)
			return -1;
		if (_faecherManager != null) {
			final GostFach aFach = _faecherManager.get(aV.idFach);
			final GostFach bFach = _faecherManager.get(bV.idFach);
			if ((aFach != null) && (bFach != null)) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		return Long.compare(a.id, b.id);
	};

	private final @NotNull Comparator<GostSchuelerklausurTermin> _compSchuelerklausurTermin = (
			final @NotNull GostSchuelerklausurTermin a, final @NotNull GostSchuelerklausurTermin b) -> {
		if (a.idSchuelerklausur == b.idSchuelerklausur) {
			return Integer.compare(a.folgeNr, b.folgeNr);
		}
		final @NotNull GostSchuelerklausur kA = schuelerklausurBySchuelerklausurtermin(a);
		final @NotNull GostSchuelerklausur kB = schuelerklausurBySchuelerklausurtermin(b);
		if ((_schuelerMap != null) && (kA.idSchueler != kB.idSchueler)) {
			final SchuelerListeEintrag sA = getSchuelerMap().get(kA.idSchueler);
			final SchuelerListeEintrag sB = getSchuelerMap().get(kB.idSchueler);
			if ((sA != null) && (sB != null))
				return (sA.nachname + sA.vorname).compareTo(sB.nachname + sB.vorname);
		}
		return Long.compare(a.id, b.id);
	};

	private static final @NotNull Comparator<GostKlausurraum> _compRaum =
			(final @NotNull GostKlausurraum a, final @NotNull GostKlausurraum b) -> Long.compare(a.id, b.id);


	// GostKlausurvorgabe
	private final @NotNull Map<Long, GostKlausurvorgabe> _vorgabe_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurvorgabe> _vorgabenmenge = new ArrayList<>();
	private final @NotNull HashMap3D<Integer, Integer, Integer, List<GostKlausurvorgabe>> _vorgabenmenge_by_abijahr_and_halbjahr_and_quartal =
			new HashMap3D<>();
	private final @NotNull HashMap5D<Integer, Integer, Integer, String, Long, GostKlausurvorgabe> _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach =
			new HashMap5D<>();
	private final @NotNull HashMap4D<Integer, Integer, String, Long, List<GostKlausurvorgabe>> _vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach =
			new HashMap4D<>();

	// GostKursklausur
	private final @NotNull Map<Long, GostKursklausur> _kursklausur_by_id = new HashMap<>();
	private final @NotNull List<GostKursklausur> _kursklausurmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKursklausur>> _kursklausurmenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<Long, List<GostKursklausur>> _kursklausurmenge_by_idVorgabe = new HashMap<>();
	private final @NotNull HashMap4D<Integer, Integer, Long, Integer, List<GostKursklausur>> _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal =
			new HashMap4D<>();
	private final @NotNull HashMap3D<Integer, Integer, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId =
			new HashMap3D<>();
	private final @NotNull HashMap2D<Long, Long, List<GostKursklausur>> _kursklausurmenge_by_terminId_and_schuelerId = new HashMap2D<>();

	// GostKlausurtermin
	private final @NotNull Map<Long, GostKlausurtermin> _termin_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurtermin> _terminmenge = new ArrayList<>();
	private final @NotNull HashMap3D<Integer, Integer, Integer, List<GostKlausurtermin>> _terminmenge_by_abijahr_and_halbjahr_and_quartal = new HashMap3D<>();
	private final @NotNull HashMap2D<String, Integer, List<GostKlausurtermin>> _terminmenge_by_datum_and_abijahr = new HashMap2D<>();

	// GostSchuelerklausur
	private final @NotNull Map<Long, GostSchuelerklausur> _schuelerklausur_by_id = new HashMap<>();
	private final @NotNull List<GostSchuelerklausur> _schuelerklausurmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostSchuelerklausur>> _schuelerklausurmenge_by_idKursklausur = new HashMap<>();

	// GostSchuelerklausurTermin
	private final @NotNull Map<Long, GostSchuelerklausurTermin> _schuelerklausurtermin_by_id = new HashMap<>();
	private final @NotNull List<GostSchuelerklausurTermin> _schuelerklausurterminmenge = new ArrayList<>();
	private final @NotNull Map<Long, GostSchuelerklausurTermin> _schuelerklausurterminaktuell_by_idSchuelerklausur = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idSchuelerklausur = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idTermin = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idKursklausur = new HashMap<>();
	private final @NotNull HashMap2D<Long, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur =
			new HashMap2D<>();
	private final @NotNull HashMap4D<Integer, Integer, Integer, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin =
			new HashMap4D<>();
	private final @NotNull HashMap2D<Long, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idRaum_and_idTermin = new HashMap2D<>();
	private final @NotNull HashMap2D<Long, Long, List<GostSchuelerklausurTermin>> _schuelerklausurterminmenge_by_idRaum_and_idKursklausur = new HashMap2D<>();

	// GostKlausurraum
	private final @NotNull Map<Long, GostKlausurraum> _raum_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraum> _raummenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraum>> _raummenge_by_idTermin = new HashMap<>();
	private final @NotNull HashMap2D<Long, Long, GostKlausurraum> _raum_by_idTermin_and_idStundenplanraum = new HashMap2D<>();
	private final @NotNull Map<Long, GostKlausurraum> _klausurraum_by_idSchuelerklausurtermin = new HashMap<>();

	// GostKlausurraumstunde
	private final @NotNull Map<Long, GostKlausurraumstunde> _raumstunde_by_id = new HashMap<>();
	private final @NotNull List<GostKlausurraumstunde> _raumstundenmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idRaum = new HashMap<>();
	private final @NotNull HashMap2D<Long, Long, GostKlausurraumstunde> _raumstunde_by_idRaum_and_idZeitraster = new HashMap2D<>();
	private final @NotNull Map<Long, List<GostKlausurraumstunde>> _raumstundenmenge_by_idSchuelerklausurtermin = new HashMap<>();

	// GostSchuelerklausurraumstunde
	private final @NotNull HashMap2D<Long, Long, GostSchuelerklausurterminraumstunde> _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde =
			new HashMap2D<>();
	private final @NotNull List<GostSchuelerklausurterminraumstunde> _schuelerklausurterminraumstundenmenge = new ArrayList<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurterminraumstunde>> _schuelerklausurterminraumstundenmenge_by_idRaumstunde = new HashMap<>();
	private final @NotNull Map<Long, List<GostSchuelerklausurterminraumstunde>> _schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin = new HashMap<>();


	/**
	 * Erstellt einen leeren Manager.
	 */
	public GostKlausurplanManager() {
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n
	 *
	 * @param listVorgaben die Liste der {@link GostKlausurvorgabe}n
	 */
	public GostKlausurplanManager(final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		vorgabeAddAll(listVorgaben);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n und dem übergebenen {@link GostFaecherManager}
	 *
	 * @param faecherManager der GostFaecherManager
	 * @param listVorgaben 	die Liste der GostKlausurvorgaben
	 */
	public GostKlausurplanManager(final GostFaecherManager faecherManager, final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		_faecherManager = faecherManager;
		vorgabeAddAll(listVorgaben);
	}

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n, {@link GostKursklausur}en, {@link GostKlausurtermin}en,
	 * {@link GostSchuelerklausur}en und {@link GostSchuelerklausurTermin}en
	 *
	 * @param listVorgaben 			die Liste der {@link GostKlausurvorgabe}n
	 * @param listKlausuren         die Liste der {@link GostKursklausur}en
	 * @param listTermine           die Liste der {@link GostKlausurtermin}e
	 * @param listSchuelerklausuren die Liste der {@link GostSchuelerklausur}en
	 * @param listSchuelerklausurtermine die Liste der {@link GostSchuelerklausurTermin}e
	 */
	public GostKlausurplanManager(final @NotNull List<GostKlausurvorgabe> listVorgaben, final @NotNull List<GostKursklausur> listKlausuren,
			final @NotNull List<GostKlausurtermin> listTermine,
			final @NotNull List<GostSchuelerklausur> listSchuelerklausuren,
			final @NotNull List<GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		addKlausurDataOhneUpdate(listVorgaben, listKlausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
		update_all();
	}

	/**
	 * Erstellt einen neuen Manager mit den übergebenen {@link GostKlausurenCollectionAllData} enthaltenen Daten
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public GostKlausurplanManager(final @NotNull GostKlausurenCollectionAllData allData) {
		addAllData(allData);
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionAllData}-Objekt enthaltenen Daten hinzu
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public void addAllData(final @NotNull GostKlausurenCollectionAllData allData) {
		initMetadata(allData.metadata);
		addKlausurDataOhneUpdate(allData.vorgaben, allData.kursklausuren, allData.termine, allData.schuelerklausuren, allData.schuelerklausurtermine);
		addRaumDataOhneUpdate(allData.raumdata);
		update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionAllData}-Objekt enthaltenen Klausurdaten hinzu ({@link GostKlausurvorgabe}n, {@link GostKursklausur}en, {@link GostKlausurtermin}e, {@link GostSchuelerklausur}en, {@link GostSchuelerklausurTermin}e)
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public void addKlausurData(final @NotNull GostKlausurenCollectionAllData allData) {
		addKlausurDataOhneUpdate(allData.vorgaben, allData.kursklausuren, allData.termine, allData.schuelerklausuren, allData.schuelerklausurtermine);
		update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionRaumData}-Objekt enthaltenen Raumplanungsdaten hinzu
	 *
	 * @param raumData            das {@link GostKlausurenCollectionRaumData}-Objekt, das Raumplanungsdaten enthält
	 */
	public void addRaumData(final @NotNull GostKlausurenCollectionRaumData raumData) {
		addRaumDataOhneUpdate(raumData);
		update_all();
	}

	private void addRaumDataOhneUpdate(final @NotNull GostKlausurenCollectionRaumData data) {
		raumAddAllOhneUpdate(data.raeume);
		raumstundeAddAllOhneUpdate(data.raumstunden);
		schuelerklausurraumstundeAddAllOhneUpdate(data.sktRaumstunden);
		_terminidmenge_manager_enthaelt_raumdata.addAll(data.idsKlausurtermine);
	}

	private void initMetadata(final @NotNull GostKlausurenCollectionMetaData meta) {
		_faecherManager = (meta.faecher != null && !meta.faecher.isEmpty()) ? new GostFaecherManager(meta.faecher) : null;
		_kursManager = (meta.kurse != null && !meta.kurse.isEmpty()) ? new KursManager(meta.kurse) : null;
		if (meta.kurse != null && !meta.kurse.isEmpty())
			setKursManager(new KursManager(meta.kurse));
		_lehrerMap = new HashMap<>();
		for (final LehrerListeEintrag lehrer : meta.lehrer)
			_lehrerMap.put(lehrer.id, lehrer);
		_schuelerMap = new HashMap<>();
		for (final SchuelerListeEintrag schueler : meta.schueler)
			_schuelerMap.put(schueler.id, schueler);
	}

	private void addKlausurDataOhneUpdate(final @NotNull List<GostKlausurvorgabe> listVorgaben, final @NotNull List<GostKursklausur> listKlausuren,
			final List<GostKlausurtermin> listTermine,
			final List<GostSchuelerklausur> listSchuelerklausuren,
			final List<GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		vorgabeAddAllOhneUpdate(listVorgaben);
		kursklausurAddAllOhneUpdate(listKlausuren);
		if (listTermine != null)
			terminAddAllOhneUpdate(listTermine);
		if (listSchuelerklausuren != null)
			schuelerklausurAddAllOhneUpdate(listSchuelerklausuren);
		if (listSchuelerklausurtermine != null)
			schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
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
		return _terminidmenge_manager_enthaelt_raumdata.contains(termin.id);
	}


	/**
	 * Setzt den {@link GostFaecherManager}
	 *
	 * @param faecherManager der {@link GostFaecherManager}
	 */
	public void setFaecherManager(final @NotNull GostFaecherManager faecherManager) {
		_faecherManager = faecherManager;
	}

	/**
	 * Liefert den {@link GostFaecherManager}, falls dieser gesetzt ist, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @return den {@link GostFaecherManager}
	 */
	public @NotNull GostFaecherManager getFaecherManager() {
		if (_faecherManager == null)
			throw new DeveloperNotificationException("GostFaecherManager not set.");
		return _faecherManager;
	}

	/**
	 * Liefert den {@link GostFaecherManager}, falls dieser gesetzt ist, sonst <code>null</code>.
	 *
	 * @return den {@link GostFaecherManager} oder <code>null</code>
	 */
	public GostFaecherManager getFaecherManagerOrNull() {
		return _faecherManager;
	}

	/**
	 * Setzt den {@link KursManager}
	 *
	 * @param kursManager der {@link KursManager}
	 */
	public void setKursManager(final @NotNull KursManager kursManager) {
		_kursManager = kursManager;
	}

	/**
	 * Liefert den {@link KursManager}, falls dieser gesetzt ist, sonst wird eine
	 * {@link DeveloperNotificationException} geworfen.
	 *
	 * @return den {@link KursManager}
	 */
	public @NotNull KursManager getKursManager() {
		if (_kursManager == null)
			throw new DeveloperNotificationException("KursManager not set.");
		return _kursManager;
	}

	/**
	 * Setzt den {@link StundenplanManager}
	 *
	 * @param stundenplanManager der {@link StundenplanManager}
	 */
	public void setStundenplanManager(final @NotNull StundenplanManager stundenplanManager) {
		_stundenplanManager = stundenplanManager;
	}

	/**
	 * Liefert den {@link StundenplanManager}, falls dieser gesetzt ist, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @return den {@link StundenplanManager}
	 */
	public @NotNull StundenplanManager getStundenplanManager() {
		if (_stundenplanManager == null)
			throw new DeveloperNotificationException("StundenplanManager not set.");
		return _stundenplanManager;
	}

	/**
	 * Liefert den {@link StundenplanManager}, falls dieser gesetzt ist, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @return den {@link StundenplanManager}
	 */
	public StundenplanManager getStundenplanManagerOrNull() {
		return _stundenplanManager;
	}

	/**
	 * Setzt die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}
	 *
	 * @param lehrerMap die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}
	 */
	public void setLehrerMap(final @NotNull Map<Long, LehrerListeEintrag> lehrerMap) {
		_lehrerMap = lehrerMap;
	}

	/**
	 * Liefert die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}, falls diese gesetzt ist, sonst wird eine
	 * {@link DeveloperNotificationException} geworfen.
	 *
	 * @return die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}
	 */
	public @NotNull Map<Long, LehrerListeEintrag> getLehrerMap() {
		if (_lehrerMap == null)
			throw new DeveloperNotificationException("LehrerMap not set.");
		return _lehrerMap;
	}

	/**
	 * Liefert die SchuelerMap, eine Map von Schüler-ID (Long) -> {@link SchuelerListeEintrag}, falls diese gesetzt ist, sonst wird eine
	 * {@link DeveloperNotificationException} geworfen.
	 *
	 * @return die SchuelerMap, eine Map von Schüler-ID (Long) -> {@link SchuelerListeEintrag}
	 */
	public @NotNull Map<Long, SchuelerListeEintrag> getSchuelerMap() {
		if (_schuelerMap == null)
			throw new DeveloperNotificationException("SchuelerMap not set.");
		return _schuelerMap;
	}

	/**
	 * Setzt die SchuelerMap, eine Map von Schüler-ID (Long) -> {@link SchuelerListeEintrag}
	 *
	 * @param schuelerMap die SchuelerMap, eine Map von Schüler-ID (Long) -> {@link SchuelerListeEintrag}
	 */
	public void setSchuelerMap(final @NotNull Map<Long, SchuelerListeEintrag> schuelerMap) {
		_schuelerMap = schuelerMap;
	}

	private void update_all() {
		update_vorgabemenge();
		update_kursklausurmenge();
		update_terminmenge();
		update_schuelerklausurmenge();
		update_schuelerklausurterminmenge();
		update_raummenge();
		update_raumstundenmenge();
		update_schuelerklausurraumstundenmenge();

		update_vorgabenmenge_by_halbjahr_and_quartal();
		update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach();
		update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach();

		update_kursklausurmenge_by_idTermin();
		update_kursklausurmenge_by_idVorgabe();
		update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();
		update_kursklausurmenge_by_terminId_and_schuelerId();
//		update_kursklausurmenge_by_kw_and_abijahr_and_schuelerId(); // benötigt _kursklausurmenge_by_idTermin

		update_terminmenge_by_halbjahr_and_quartal();
		update_terminmenge_by_datum();

		update_raummenge_by_idTermin();
		update_raum_by_idTermin_and_idStundenplanraum();

		update_raumstundenmenge_by_idRaum();
		update_raumstunde_by_idRaum_and_idZeitraster();
		update_raumstundenmenge_by_idSchuelerklausurtermin(); // benötigt _raumstunde_by_id
		update_klausurraum_by_idSchuelerklausurtermin(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin,

		update_schuelerklausurterminaktuell_by_idSchuelerklausur();
		update_schuelerklausurmenge_by_idKursklausur();
		update_schuelerklausurterminmenge_by_idSchuelerklausur();
		update_schuelerklausurterminmenge_by_idTermin();
		update_schuelerklausurterminmenge_by_idKursklausur();
		update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur(); // benötigt _schuelerklausurterminaktuell_by_idSchuelerklausur
		update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId(); // benötigt _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur
		update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal(); // benötigt _schuelerklausurterminaktuell_by_idSchuelerklausur
		update_schuelerklausurterminmenge_by_idRaum_and_idTermin(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin
		update_schuelerklausurterminmenge_by_idRaum_and_idKursklausur(); // benötigt _raumstundenmenge_by_idSchuelerklausurtermin
		update_schuelerklausurterminraumstundenmenge_by_idRaumstunde();
		update_schuelerklausurraumstundenmenge_by_idSchuelerklausur();
	}

	private void update_vorgabenmenge_by_halbjahr_and_quartal() {
		_vorgabenmenge_by_abijahr_and_halbjahr_and_quartal.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge) {
			Map3DUtils.getOrCreateArrayList(_vorgabenmenge_by_abijahr_and_halbjahr_and_quartal, v.abiJahrgang, v.halbjahr, v.quartal).add(v);
		}
	}

	private void update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach() {
		_vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			_vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.put(v.abiJahrgang, v.halbjahr, v.quartal, v.kursart, v.idFach, v);
	}

	private void update_vorgabenmenge_by_halbjahr_and_kursartAllg_and_idFach() {
		_vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach.clear();
		for (final @NotNull GostKlausurvorgabe v : _vorgabenmenge)
			Map4DUtils.getOrCreateArrayList(_vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach, v.abiJahrgang, v.halbjahr, v.kursart, v.idFach)
					.add(v);
	}

	private void update_kursklausurmenge_by_idTermin() {
		_kursklausurmenge_by_idTermin.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			MapUtils.getOrCreateArrayList(_kursklausurmenge_by_idTermin, (kk.idTermin != null) ? kk.idTermin : -1)
					.add(kk);
	}

	private void update_kursklausurmenge_by_idVorgabe() {
		_kursklausurmenge_by_idVorgabe.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge)
			MapUtils.getOrCreateArrayList(_kursklausurmenge_by_idVorgabe, kk.idVorgabe).add(kk);
	}

	private void update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin() {
		_kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			final @NotNull GostKlausurvorgabe v = vorgabeByKursklausur(kk);
			Map4DUtils.getOrCreateArrayList(_kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal,
					v.abiJahrgang, v.halbjahr, (kk.idTermin != null) ? kk.idTermin : -1, v.quartal).add(kk);
		}
	}

	private void update_terminmenge_by_halbjahr_and_quartal() {
		_terminmenge_by_abijahr_and_halbjahr_and_quartal.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge)
			Map3DUtils.getOrCreateArrayList(_terminmenge_by_abijahr_and_halbjahr_and_quartal, t.abijahr, t.halbjahr,
					t.quartal).add(t);
	}

	private void update_terminmenge_by_datum() {
		_terminmenge_by_datum_and_abijahr.clear();
		for (final @NotNull GostKlausurtermin t : _terminmenge)
			Map2DUtils.getOrCreateArrayList(_terminmenge_by_datum_and_abijahr, t.datum, t.abijahr).add(t);
	}

	private void update_schuelerklausurterminaktuell_by_idSchuelerklausur() {
		_schuelerklausurterminaktuell_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge) {
			final GostSchuelerklausurTermin sktMaxFolgenummer = _schuelerklausurterminaktuell_by_idSchuelerklausur.get(skt.idSchuelerklausur);
			if (sktMaxFolgenummer == null || sktMaxFolgenummer.folgeNr < skt.folgeNr)
				_schuelerklausurterminaktuell_by_idSchuelerklausur.put(skt.idSchuelerklausur, skt);
		}
	}

	private void update_schuelerklausurmenge_by_idKursklausur() {
		_schuelerklausurmenge_by_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausur sk : _schuelerklausurmenge) {
			MapUtils.getOrCreateArrayList(_schuelerklausurmenge_by_idKursklausur, sk.idKursklausur).add(sk);
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId() {
		_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId.clear();
		for (final long idTermin : _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.getKeySet()) {
			final @NotNull GostKlausurtermin termin = terminGetByIdOrException(idTermin);
			if (termin.datum == null)
				continue;
			final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
			for (final @NotNull GostSchuelerklausurTermin skt : ListUtils
					.getFlatted(_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.getNonNullValuesOfKey1AsList(idTermin))) {
				final @NotNull GostSchuelerklausur sk = schuelerklausurBySchuelerklausurtermin(skt);
				Map3DUtils.getOrCreateArrayList(_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId, kw,
						vorgabeBySchuelerklausur(sk).abiJahrgang, sk.idSchueler).add(skt);
			}
		}
	}

	private void update_kursklausurmenge_by_terminId_and_schuelerId() {
		_kursklausurmenge_by_terminId_and_schuelerId.clear();
		for (final @NotNull GostKursklausur kk : _kursklausurmenge) {
			for (final @NotNull GostSchuelerklausur sk : schuelerklausurGetMengeByKursklausur(kk))
				Map2DUtils
						.getOrCreateArrayList(_kursklausurmenge_by_terminId_and_schuelerId, kk.idTermin, sk.idSchueler)
						.add(kk);
		}
	}

	private void update_schuelerklausurterminmenge_by_idSchuelerklausur() {
		_schuelerklausurterminmenge_by_idSchuelerklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur)
					.add(skt);
		for (final @NotNull List<GostSchuelerklausurTermin> sktList : _schuelerklausurterminmenge_by_idSchuelerklausur
				.values())
			sktList.sort(_compSchuelerklausurTermin);
	}

	private void update_schuelerklausurterminmenge_by_idTermin() {
		_schuelerklausurterminmenge_by_idTermin.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge) {
			if (skt.folgeNr == 0) {
				final Long idTermin = kursklausurBySchuelerklausurTermin(skt).idTermin;
				if (idTermin != null)
					MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idTermin,
							kursklausurBySchuelerklausurTermin(skt).idTermin).add(skt);
			} else if (skt.idTermin != null)
				MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idTermin, skt.idTermin).add(skt);
		}
	}

	private void update_schuelerklausurterminmenge_by_idKursklausur() {
		_schuelerklausurterminmenge_by_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin skt : _schuelerklausurterminmenge) {
			if (skt.folgeNr == 0)
				MapUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idKursklausur,
						schuelerklausurBySchuelerklausurtermin(skt).idKursklausur).add(skt);
		}
	}

	private void update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur() {
		_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.clear();
		for (final @NotNull Entry<Long, List<GostSchuelerklausurTermin>> e : _schuelerklausurterminmenge_by_idTermin
				.entrySet())
			for (final @NotNull GostSchuelerklausurTermin skt : e.getValue())
				if (istSchuelerklausurterminAktuell(skt))
					Map2DUtils.getOrCreateArrayList(_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur,
							e.getKey(), schuelerklausurBySchuelerklausurtermin(skt).idKursklausur).add(skt);
	}

	private void update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal() {
		_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.clear();
		for (final @NotNull GostSchuelerklausur sk : _schuelerklausurmenge) {
			final @NotNull GostSchuelerklausurTermin sktLast = schuelerklausurterminAktuellBySchuelerklausur(sk);
			if (sktLast.folgeNr > 0) {
				final @NotNull GostKlausurvorgabe v = vorgabeBySchuelerklausurTermin(sktLast);
				Map4DUtils.getOrCreateArrayList(
						_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin,
						v.abiJahrgang, v.halbjahr, v.quartal, (sktLast.idTermin != null) ? sktLast.idTermin : -1)
						.add(sktLast);
			}
		}
	}

	private void update_raum_by_idTermin_and_idStundenplanraum() {
		_raum_by_idTermin_and_idStundenplanraum.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge)
			if (raum.idStundenplanRaum != null)
				DeveloperNotificationException.ifMap2DPutOverwrites(_raum_by_idTermin_and_idStundenplanraum, raum.idTermin, raum.idStundenplanRaum,
						raum);
	}

	private void update_raummenge_by_idTermin() {
		_raummenge_by_idTermin.clear();
		for (final @NotNull GostKlausurraum raum : _raummenge)
			MapUtils.getOrCreateArrayList(_raummenge_by_idTermin, raum.idTermin).add(raum);
	}

	private void update_raumstundenmenge_by_idRaum() {
		_raumstundenmenge_by_idRaum.clear();
		for (final @NotNull GostKlausurraumstunde krs : _raumstundenmenge)
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idRaum, krs.idRaum).add(krs);
	}

	private void update_raumstunde_by_idRaum_and_idZeitraster() {
		_raumstunde_by_idRaum_and_idZeitraster.clear();
		for (final @NotNull GostKlausurraumstunde rs : _raumstundenmenge)
			DeveloperNotificationException.ifMap2DPutOverwrites(_raumstunde_by_idRaum_and_idZeitraster, rs.idRaum, rs.idZeitraster, rs);
	}

	private void update_raumstundenmenge_by_idSchuelerklausurtermin() {
		_raumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin)
					.add(DeveloperNotificationException.ifMapGetIsNull(_raumstunde_by_id, skrs.idRaumstunde));
	}

	private void update_schuelerklausurterminmenge_by_idRaum_and_idTermin() {
		_schuelerklausurterminmenge_by_idRaum_and_idTermin.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge) {
			final GostKlausurtermin termin = terminOrNullBySchuelerklausurTermin(k);
			if (termin != null) {
				final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
				Map2DUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idRaum_and_idTermin,
						((raumstunden == null) || raumstunden.isEmpty()) ? -1L : raumstunden.get(0).idRaum,
						termin.id).add(k);
			}
		}
	}

	private void update_schuelerklausurterminmenge_by_idRaum_and_idKursklausur() {
		_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.clear();
		for (final @NotNull GostSchuelerklausurTermin k : _schuelerklausurterminmenge) {
			final List<GostKlausurraumstunde> raumstunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			Map2DUtils.getOrCreateArrayList(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur,
					((raumstunden == null) || raumstunden.isEmpty()) ? -1L : raumstunden.get(0).idRaum,
					kursklausurBySchuelerklausurTermin(k).id).add(k);
		}
	}

	private void update_schuelerklausurterminraumstundenmenge_by_idRaumstunde() {
		_schuelerklausurterminraumstundenmenge_by_idRaumstunde.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurterminraumstundenmenge_by_idRaumstunde, skrs.idRaumstunde).add(skrs);
	}

	private void update_schuelerklausurraumstundenmenge_by_idSchuelerklausur() {
		_schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(_schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin).add(skrs);
	}

	private void update_klausurraum_by_idSchuelerklausurtermin() {
		_klausurraum_by_idSchuelerklausurtermin.clear();
		for (final @NotNull GostSchuelerklausurterminraumstunde skrs : _schuelerklausurterminraumstundenmenge) {
			final @NotNull List<GostKlausurraumstunde> krsList =
					DeveloperNotificationException.ifMapGetIsNull(_raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin);
			for (final @NotNull GostKlausurraumstunde krs : krsList) {
				final @NotNull GostKlausurraum kr = DeveloperNotificationException.ifMapGetIsNull(_raum_by_id, krs.idRaum);
				final GostKlausurraum krAlt = _klausurraum_by_idSchuelerklausurtermin.put(skrs.idSchuelerklausurtermin, kr);
				if ((krAlt != null) && (krAlt != kr))
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausurtermin + " ist zwei Klausurräumen zugeordnet.");
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

	private void vorgabeAddAllOhneUpdate(final @NotNull List<GostKlausurvorgabe> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurvorgabe vorgabe : list) {
			vorgabeCheck(vorgabe);
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " existiert bereits!",
					_vorgabe_by_id.containsKey(vorgabe.idVorgabe));
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.idVorgabe + " doppelt in der Liste!",
					!setOfIDs.add(vorgabe.idVorgabe));
		}

		// add all
		for (final @NotNull GostKlausurvorgabe vorgabe : list)
			DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.idVorgabe, vorgabe);
		_vorgabenInitialized = true;
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public void vorgabeAddAll(final @NotNull List<GostKlausurvorgabe> listVorgaben) {
		vorgabeAddAllOhneUpdate(listVorgaben);
		update_all();
	}

	private static void vorgabeCheck(final @NotNull GostKlausurvorgabe vorgabe) {
		DeveloperNotificationException.ifInvalidID("vorgabe.idVorgabe", vorgabe.idVorgabe);
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
		DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, vorgabe.idVorgabe);
		DeveloperNotificationException.ifMapPutOverwrites(_vorgabe_by_id, vorgabe.idVorgabe, vorgabe);

		update_all();
	}

	private void vorgabeRemoveOhneUpdateById(final long idVorgabe) {
		DeveloperNotificationException.ifMapRemoveFailes(_vorgabe_by_id, idVorgabe);
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
		for (final @NotNull GostKlausurvorgabe vorgabe : listVorgaben)
			vorgabeRemoveOhneUpdateById(vorgabe.idVorgabe);

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

	private void kursklausurAddAllOhneUpdate(final @NotNull List<GostKursklausur> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKursklausur klausur : list) {
			kursklausurCheck(klausur);
			DeveloperNotificationException.ifTrue(
					"kursklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!",
					_kursklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue(
					"kursklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!",
					!setOfIDs.add(klausur.id));
		}

		// add all
		for (final @NotNull GostKursklausur klausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_kursklausur_by_id, klausur.id, klausur);
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

		for (final @NotNull GostKursklausur kursklausur : kursklausurMenge)
			kursklausurPatchAttributesOhneUpdate(kursklausur);

		update_all();
	}

	private void kursklausurRemoveOhneUpdateById(final long idKursklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_kursklausur_by_id, idKursklausur);
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
		for (final @NotNull GostKursklausur kursklausur : listKursklausuren)
			kursklausurRemoveOhneUpdateById(kursklausur.id);

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

	private void terminAddAllOhneUpdate(final @NotNull List<GostKlausurtermin> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurtermin termin : list) {
			terminCheck(termin);
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " existiert bereits!",
					_termin_by_id.containsKey(termin.id));
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " doppelt in der Liste!",
					!setOfIDs.add(termin.id));
		}

		// add all
		for (final @NotNull GostKlausurtermin termin : list)
			DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);
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

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public void terminPatchAttributes(final @NotNull GostKlausurtermin termin) {
		terminCheck(termin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_termin_by_id, termin.id, termin);

		update_all();
	}

	private void terminRemoveOhneUpdateById(final long idTermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_termin_by_id, idTermin);
		final List<GostKursklausur> kursklausurenZuTermin = _kursklausurmenge_by_idTermin.get(idTermin);
		if (kursklausurenZuTermin != null)
			for (final @NotNull GostKursklausur k : kursklausurenZuTermin)
				k.idTermin = null;
		final List<GostSchuelerklausurTermin> schuelerklausurtermineZuTermin = _schuelerklausurterminmenge_by_idTermin
				.get(idTermin);
		if (schuelerklausurtermineZuTermin != null)
			for (final @NotNull GostSchuelerklausurTermin skt : schuelerklausurtermineZuTermin)
				skt.idTermin = null;
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
		for (final @NotNull GostKlausurtermin termin : listTermine)
			terminRemoveOhneUpdateById(termin.id);

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

	private void schuelerklausurAddAllOhneUpdate(final @NotNull List<GostSchuelerklausur> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausur klausur : list) {
			schuelerklausurCheck(klausur);
			DeveloperNotificationException.ifTrue(
					"schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!",
					_schuelerklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue(
					"schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!",
					!setOfIDs.add(klausur.id));
		}

		// add all
		for (final @NotNull GostSchuelerklausur klausur : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausur_by_id, klausur.id, klausur);
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
	 * Liefert das zur ID zugehörige {@link GostKursklausur}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idKursklausur Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostKursklausur}-Objekt.
	 */
	public @NotNull GostSchuelerklausur schuelerklausurGetByIdOrException(final long idKursklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausur_by_id, idKursklausur);
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

	private void schuelerklausurRemoveOhneUpdateById(final long idKursklausur) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausur_by_id, idKursklausur);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public void schuelerklausurRemoveById(final long idKursklausur) {
		schuelerklausurRemoveOhneUpdateById(idKursklausur);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public void schuelerklausurRemoveAll(final @NotNull List<GostSchuelerklausur> listKursklausuren) {
		for (final @NotNull GostSchuelerklausur kursklausur : listKursklausuren)
			schuelerklausurRemoveOhneUpdateById(kursklausur.id);

		update_all();
	}

	// #####################################################################
	// #################### GostSchuelerklausurTermin
	// ################################
	// #####################################################################

	private void update_schuelerklausurterminmenge() {
		_schuelerklausurterminmenge.clear();
		_schuelerklausurterminmenge.addAll(_schuelerklausurtermin_by_id.values());
		_schuelerklausurterminmenge.sort(_compSchuelerklausurTermin);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausurtermin Das {@link GostSchuelerklausurTermin}-Objekt,
	 *                              welches hinzugefügt werden soll.
	 */
	public void schuelerklausurterminAdd(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		schuelerklausurterminAddAll(ListUtils.create1(schuelerklausurtermin));
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausurTermin}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public void schuelerklausurAddOhneUpdate(final @NotNull GostSchuelerklausurTermin schuelerklausur) {
		schuelerklausurterminAddAllOhneUpdate(ListUtils.create1(schuelerklausur));
	}

	private void schuelerklausurterminAddAllOhneUpdate(final @NotNull List<GostSchuelerklausurTermin> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : list) {
			schuelerklausurterminCheck(schuelerklausurtermin);
			DeveloperNotificationException.ifTrue(
					"schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " existiert bereits!",
					_schuelerklausurtermin_by_id.containsKey(schuelerklausurtermin.id));
			DeveloperNotificationException.ifTrue(
					"schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " doppelt in der Liste!",
					!setOfIDs.add(schuelerklausurtermin.id));
		}

		// add all
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : list)
			DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id,
					schuelerklausurtermin);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurTermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurtermine die Menge der
	 *                                   {@link GostSchuelerklausurTermin}-Objekte,
	 *                                   welche hinzugefügt werden sollen.
	 */
	public void schuelerklausurterminAddAll(final @NotNull List<GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		update_all();
	}

	private static void schuelerklausurterminCheck(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		DeveloperNotificationException.ifInvalidID("schuelerschuelerklausurtermin.idSchuelerschuelerklausurtermin",
				schuelerklausurtermin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public @NotNull GostSchuelerklausurTermin schuelerklausurterminGetByIdOrException(
			final long idSchuelerklausurtermin) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurTermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeAsList() {
		return new ArrayList<>(_schuelerklausurterminmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurTermin}-Objekt durch
	 * das neue Objekt.
	 *
	 * @param schuelerklausurtermin Das neue
	 *                              {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public void schuelerklausurterminPatchAttributes(final @NotNull GostSchuelerklausurTermin schuelerklausurtermin) {
		schuelerklausurterminCheck(schuelerklausurtermin);

		// Altes Objekt durch neues Objekt ersetzen
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, schuelerklausurtermin.id);
		DeveloperNotificationException.ifMapPutOverwrites(_schuelerklausurtermin_by_id, schuelerklausurtermin.id,
				schuelerklausurtermin);

		update_all();
	}

	private void schuelerklausurterminRemoveOhneUpdateById(final long idSchuelerklausurtermin) {
		DeveloperNotificationException.ifMapRemoveFailes(_schuelerklausurtermin_by_id, idSchuelerklausurtermin);
		schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurTermin}-Objekt.
	 *
	 * @param idSchuelerklausurtermin die ID des
	 *                                {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public void schuelerklausurterminRemoveById(final long idSchuelerklausurtermin) {
		schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin);

		update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine die Liste der zu entfernenden
	 *                                   {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public void schuelerklausurterminRemoveAll(
			final @NotNull List<GostSchuelerklausurTermin> listSchuelerklausurtermine) {
		for (final @NotNull GostSchuelerklausurTermin schuelerklausurtermin : listSchuelerklausurtermine)
			schuelerklausurterminRemoveOhneUpdateById(schuelerklausurtermin.id);

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

	private void raumAddAllOhneUpdate(final @NotNull List<GostKlausurraum> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraum raum : list) {
			raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", _raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}

		// add all
		for (final @NotNull GostKlausurraum raum : list)
			DeveloperNotificationException.ifMapPutOverwrites(_raum_by_id, raum.id, raum);
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
		if (rsList != null)
			for (final @NotNull GostKlausurraumstunde rs : rsList)
				raumstundeRemoveOhneUpdateById(rs.id);
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
	public void raumRemoveAll(final @NotNull List<GostKlausurraum> listRaum) {
		for (final @NotNull GostKlausurraum raum : listRaum)
			raumRemoveOhneUpdateById(raum.id);

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

	private void raumstundeAddAllOhneUpdate(final @NotNull List<GostKlausurraumstunde> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull GostKlausurraumstunde raumstunde : list) {
			raumstundeCheck(raumstunde);
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " existiert bereits!",
					_raumstunde_by_id.containsKey(raumstunde.id));
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " doppelt in der Liste!", !setOfIDs.add(raumstunde.id));
		}

		// add all
		for (final @NotNull GostKlausurraumstunde raumstunde : list)
			DeveloperNotificationException.ifMapPutOverwrites(_raumstunde_by_id, raumstunde.id, raumstunde);
	}

	/**
	 * Fügt alle {@link GostKlausurraumstunde}-Objekte hinzu.
	 *
	 * @param listRaumstunde Die Menge der {@link GostKlausurraumstunde}-Objekte,
	 *                       welche hinzugefügt werden soll.
	 */
	public void raumstundeAddAll(final @NotNull List<GostKlausurraumstunde> listRaumstunde) {
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
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurterminraumstundenmenge_by_idRaumstunde.get(idRaumstunde);
		if (skrsList != null)
			for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList)
				schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
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
		for (final @NotNull GostKlausurraumstunde raumstunde : listRaumstunde)
			raumstundeRemoveOhneUpdateById(raumstunde.id);
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
		_schuelerklausurterminraumstundenmenge.addAll(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getNonNullValuesAsList());
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

	private void schuelerklausurraumstundeAddAllOhneUpdate(final @NotNull List<GostSchuelerklausurterminraumstunde> list) {
		// check all
		final @NotNull HashMap2D<Long, Long, GostSchuelerklausurterminraumstunde> setOfIDs = new HashMap2D<>();
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list) {
			schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=(" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") existiert bereits!",
					_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.contains(schuelerklausurraumstunde.idSchuelerklausurtermin,
							schuelerklausurraumstunde.idRaumstunde));
			DeveloperNotificationException.ifTrue(
					"schuelerklausurraumstundeAddAllOhneUpdate: ID=" + schuelerklausurraumstunde.idSchuelerklausurtermin + ","
							+ schuelerklausurraumstunde.idRaumstunde + ") doppelt in der Liste!",
					setOfIDs.contains(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde));
			setOfIDs.put(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}

		// add all
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : list)
			DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
					schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
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
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstundeGetByIdSchuelerklausurAndIdRaumstundeOrException(final long idSchuelerklausur,
			final long idRaumstunde) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde, idSchuelerklausur,
				idRaumstunde);
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
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
				schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde);
		DeveloperNotificationException.ifMap2DPutOverwrites(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
				schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);

		update_all();
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(final long idSchuelerklausur, final long idRaumstunde) {
		DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde, idSchuelerklausur,
				idRaumstunde);
	}

	private void schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSubMap(idSchuelerklausurtermin);
	}

	private void schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(final long idSchuelerklausurtermin) {
		final List<GostSchuelerklausurterminraumstunde> skrsList = _schuelerklausurraumstundenmenge_by_idSchuelerklausurtermin.get(idSchuelerklausurtermin);
		if (skrsList != null)
			for (final @NotNull GostSchuelerklausurterminraumstunde skrs : skrsList)
				DeveloperNotificationException.ifMap2DRemoveFailes(_schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde,
						skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public void schuelerklausurraumstundeRemoveByIdSchuelerklausurterminAndIdRaumstunde(final long idSchuelerklausurtermin, final long idRaumstunde) {
		schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(idSchuelerklausurtermin, idRaumstunde);

		update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
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
		for (final long idSchuelerklausurtermin : idsSchuelerklausurtermine)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
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
		for (final @NotNull GostSchuelerklausurterminraumstunde schuelerklausurraumstunde : listSchuelerklausurRaumstunde)
			schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausurtermin,
					schuelerklausurraumstunde.idRaumstunde);

		update_all();
	}

	// ################################################################################


	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurvorgabe}n
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartal(final int abiJahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal) {
		if (quartal == 0) {
			final List<GostKlausurvorgabe> vorgaben = new ArrayList<>();
			if (_vorgabenmenge_by_abijahr_and_halbjahr_and_quartal.containsKey1AndKey2(abiJahrgang, halbjahr.id))
				for (final @NotNull List<GostKlausurvorgabe> vQuartal : _vorgabenmenge_by_abijahr_and_halbjahr_and_quartal
						.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id)) {
					vorgaben.addAll(vQuartal);
				}
			return vorgaben;
		}
		final List<GostKlausurvorgabe> vorgaben = _vorgabenmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal);
		return (vorgaben != null) ? vorgaben : new ArrayList<>();
	}

	/**
	 * Gibt das {@link GostKlausurvorgabe}-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     das Quartal
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return das {@link GostKlausurvorgabe}-Objekt
	 */
	public GostKlausurvorgabe vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(final int abiJahrgang, final @NotNull GostHalbjahr halbjahr,
			final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		return _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getOrNull(
				abiJahrgang, halbjahr.id, quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals oder 0 für alle Quartale
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der {@link GostKlausurvorgabe}-Objekte
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final @NotNull GostKursart kursartAllg, final long idFach) {
		if (quartal > 0) {
			final List<GostKlausurvorgabe> retList = new ArrayList<>();
			final GostKlausurvorgabe vorgabe = vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang, halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe != null)
				retList.add(vorgabe);
			return retList;
		}
		return vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiJahrgang, halbjahr, kursartAllg, idFach);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zu den übergebenen Parametern
	 * zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return die Liste der {@link GostKlausurvorgabe}-Objekte
	 */
	public @NotNull List<GostKlausurvorgabe> vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final @NotNull GostKursart kursartAllg, final long idFach) {
		final List<GostKlausurvorgabe> list =
				_vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach.getOrNull(abiJahrgang, halbjahr.id, kursartAllg.kuerzel, idFach);
		return (list != null) ? list : new ArrayList<>();
	}

	/**
	 * Gibt die Vorgänger-{@link GostKlausurvorgabe} zum übergebenen Parameter zurück (vorhergehendes Quartal des aktuellen Schuljahres) oder <code>null</code>, falls es keinen Vorgänger gibt.
	 *
	 * @param vorgabe das {@link GostKlausurvorgabe}-Objekt, dessen Vorgänger gesucht ist.
	 *
	 * @return die Vorgänger-{@link GostKlausurvorgabe} oder <code>null</code>, falls es keinen Vorgänger gibt.
	 */
	public GostKlausurvorgabe vorgabeGetPrevious(final @NotNull GostKlausurvorgabe vorgabe) {
		final List<GostKlausurvorgabe> vorgabenSchuljahr = _vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach
				.getNonNullOrException(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.kursart, vorgabe.idFach);
		if ((vorgabe.halbjahr % 2) == 1) {
			final List<GostKlausurvorgabe> vorgabenVorhalbjahr = _vorgabenmenge_by_abijahr_and_halbjahr_and_kursartAllg_and_idFach
					.getOrNull(vorgabe.abiJahrgang, vorgabe.halbjahr - 1, vorgabe.kursart, vorgabe.idFach);
			if (vorgabenVorhalbjahr != null)
				vorgabenSchuljahr.addAll(vorgabenVorhalbjahr);
		}
		vorgabenSchuljahr.sort(_compVorgabe);
		final int listIndex = vorgabenSchuljahr.indexOf(vorgabe);
		if (listIndex == 0)
			return null;
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
			if (klaus.idKurs == idKurs)
				return klaus;
		}
		return null;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zum übergebenen Datum
	 *
	 * @param datum das Datum der {@link GostKlausurtermin}e im Format <code>YYYY-MM-DD</code>
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zum übergebenen Datum
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByDatum(final @NotNull String datum) {
		final @NotNull List<GostKlausurtermin> ergebnis = new ArrayList<>();
		if (!_terminmenge_by_datum_and_abijahr.containsKey1(datum))
			return ergebnis;
		for (final @NotNull List<GostKlausurtermin> termine : _terminmenge_by_datum_and_abijahr
				.getNonNullValuesOfKey1AsList(datum))
			ergebnis.addAll(termine);
		return ergebnis;
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
		if (!mitTermin)
			ergebnis.remove(termin);
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
	 * @param abiJahrgang der Abiturjahrgang, innerhalb dessen die {@link GostKlausurtermin}e gesucht werden
	 *
	 * @return die Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 */
	public @NotNull List<List<GostKlausurtermin>> terminGruppierteUeberschneidungenGetMengeByDatumAndAbijahr(
			final @NotNull String datum, final Integer abiJahrgang) {
		if (abiJahrgang == null)
			return terminGruppierteUeberschneidungenGetMengeByDatum(datum);
		final List<GostKlausurtermin> termine = _terminmenge_by_datum_and_abijahr.getOrNull(datum, abiJahrgang);
		return (termine != null) ? gruppiereUeberschneidungen(termine) : new ArrayList<>();
	}

	private @NotNull List<List<GostKlausurtermin>> gruppiereUeberschneidungen(
			final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<List<GostKlausurtermin>> ergebnis = new ArrayList<>();
		boolean added = false;
		// Teste alle übergebenen Termine
		for (final @NotNull GostKlausurtermin terminToAdd : termine) {
			// Not supported by transpiler outerloop:
			// Teste alle bereits gefundenen gruppierten Terminlisten
			for (final @NotNull List<GostKlausurtermin> listToCheck : ergebnis) {
				// Teste jeden Termin innerhalb einer gruppierten Terminliste
				for (final @NotNull GostKlausurtermin terminInListe : termine) {
					if (checkTerminUeberschneidung(terminInListe, terminToAdd)) {
						listToCheck.add(terminToAdd);
						// Not supported by transpiler break outerloop;
						added = true;
					}
					// Transpiler-Workaround
					if (added)
						break;
				}
				// Transpiler-Workaround
				if (added)
					break;
			}
			// Keine Überschneidung gefunden, also neue Liste
			if (!added)
				ergebnis.add(ListUtils.create1(terminToAdd));
		}
		return ergebnis;
	}

	private boolean checkTerminUeberschneidung(final @NotNull GostKlausurtermin t1,
			final @NotNull GostKlausurtermin t2) {
		final int s1 = minKlausurstartzeitByTermin(t1, true);
		final int s2 = minKlausurstartzeitByTermin(t2, true);
		final int e1 = maxKlausurendzeitByTermin(t1, true);
		final int e2 = maxKlausurendzeitByTermin(t2, true);
		return (e1 >= s2) && (e2 >= s1);
	}

	private @NotNull List<GostKursklausur> kursklausurGetMengeByTerminid(final Long idTermin) {
		final List<GostKursklausur> klausuren = _kursklausurmenge_by_idTermin.get((idTermin != null) ? idTermin : -1);
		return (klausuren != null) ? klausuren : new ArrayList<>();
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
		if (mitNachschreibern)
			for (final GostSchuelerklausurTermin skt : schuelerklausurterminGetMengeByTermin(termin)) {
				klausuren.add(kursklausurBySchuelerklausurTermin(skt));
			}
		return klausuren;
	}

	/**
	 * Liefert eine Liste von {@link GostKursklausur}en zu den übergebenen Parametern, für
	 * die noch kein Termin / Schiene gesetzt wurde
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von GostKursklausur-Objekten
	 */
	public @NotNull List<GostKursklausur> kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final List<GostKursklausur> klausuren = _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal
					.getOrNull(abiJahrgang, halbjahr.id, -1L, quartal);
			return (klausuren != null) ? klausuren : new ArrayList<>();
		}
		final List<GostKursklausur> klausuren = new ArrayList<>();
		for (final @NotNull List<GostKursklausur> kl : _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal
				.getNonNullValuesOfMap4AsList(abiJahrgang, halbjahr.id, -1L)) {
			klausuren.addAll(kl);
		}
		return klausuren;
	}

	/**
	 * Liefert eine {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@ling PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang, zu dem die Paralleljahrgänge gesucht werden
	 * @param halbjahr    das {@link GostHalbjahr}, zu dem die Paralleljahrgänge gesucht werden
	 * @param includeSelf falls <code>true</code>, ist das {@ling PairNN} aus <code>abiJahrgang</code> und <code>halbjahr</code> in der Rückgabe inkludiert
	 *
	 * @return die {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@ling PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 */
	public static @NotNull List<PairNN<Integer, GostHalbjahr>> halbjahreParallelUndAktivGetMenge(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final boolean includeSelf) {
		final @NotNull List<PairNN<Integer, GostHalbjahr>> ergebnis = new ArrayList<>();
		if (includeSelf)
			ergebnis.add(new PairNN<>(abiJahrgang, halbjahr));
		if (halbjahr.id >= 2)
			ergebnis.add(new PairNN<>(abiJahrgang + 1, GostHalbjahr.fromIDorException(halbjahr.id - 2)));
		if (halbjahr.id >= 4)
			ergebnis.add(new PairNN<>(abiJahrgang + 2, GostHalbjahr.fromIDorException(halbjahr.id - 4)));
		if (halbjahr.id <= 3)
			ergebnis.add(new PairNN<>(abiJahrgang - 1, GostHalbjahr.fromIDorException(halbjahr.id + 2)));
		if (halbjahr.id <= 1)
			ergebnis.add(new PairNN<>(abiJahrgang - 2, GostHalbjahr.fromIDorException(halbjahr.id + 4)));
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		if (!multijahrgang)
			return terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal);
		final List<GostKlausurtermin> termine = new ArrayList<>();

		for (final @NotNull PairNN<Integer, GostHalbjahr> jgHj : halbjahreParallelUndAktivGetMenge(abiJahrgang, halbjahr, true))
			termine.addAll(terminGetMengeByAbijahrAndHalbjahrAndQuartal(jgHj.a, jgHj.b, quartal));

		return termine;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termine = new ArrayList<>();
		if (quartal > 0) {
			if (_terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal) != null)
				termine.addAll(
						_terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, quartal));
			if (_terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, 0) != null)
				termine.addAll(_terminmenge_by_abijahr_and_halbjahr_and_quartal.getOrNull(abiJahrgang, halbjahr.id, 0));
			return termine;
		}
		if (_terminmenge_by_abijahr_and_halbjahr_and_quartal.containsKey1AndKey2(abiJahrgang, halbjahr.id))
			for (final @NotNull List<GostKlausurtermin> qTermine : _terminmenge_by_abijahr_and_halbjahr_and_quartal
					.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id)) {
				termine.addAll(qTermine);
			}
		return termine;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die für Nachschreiber zugelassen, zu den übergebenen Parametern
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die für Nachschreiber zugelassen, zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		final List<GostKlausurtermin> termine = new ArrayList<>();
		for (final @NotNull GostKlausurtermin t : terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang, halbjahr,
				quartal, multijahrgang))
			if (!t.istHaupttermin || t.nachschreiberZugelassen)
				termine.add(t);
		termine.sort(_compTermin);
		return termine;
	}

	/**
	 * Prüft, ob in einem Nachschreibtermin {@link GostSchuelerklausurTermin}e anderer
	 * Jahrgangsstufen enthalten sind
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, dessen Nachschreibtermine geprüft werden
	 * @param halbjahr      das {@link GostHalbjahr}, dessen Nachschreibtermine geprüft werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 * @param multijahrgang wenn <code>true</code>, werden die {@link GostKlausurtermin}e der anderen aktiven Jahrgänge eingeschlossen
	 *
	 * @return <code>true</code>, falls in einem Nachschreibtermin {@link GostSchuelerklausurTermin}e anderer
	 * Jahrgangsstufen enthalten sind
	 */
	public boolean terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal, final boolean multijahrgang) {
		for (final @NotNull GostKlausurtermin t : terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang, halbjahr,
				quartal, multijahrgang))
			if (terminMitAnderenJgst(t))
				return true;
		return false;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden zu den übergebenen Parametern
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden zu den übergebenen Parametern
	 */
	public @NotNull List<GostKlausurtermin> terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termine = new ArrayList<>();
		for (final @NotNull GostKlausurtermin t : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (t.istHaupttermin)
				termine.add(t);
		termine.sort(_compTermin);
		return termine;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr,
				quartal))
			if (termin.datum != null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(_compTermin);
		return termineMitDatum;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden und denen bereits ein Datum zugewiesen wurde.
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, die als Haupttermin angelegt wurden und denen bereits ein Datum zugewiesen wurde.
	 */
	public @NotNull List<GostKlausurtermin> terminHtMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(final int abiJahrgang,
			final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final List<GostKlausurtermin> termineMitDatum = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr,
				quartal))
			if (termin.datum != null)
				termineMitDatum.add(termin);
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
		final @NotNull List<GostSchuelerklausurTermin> schuelertermine = schuelerklausurterminNtGetMengeByTermin(termin);
		if (klausuren.isEmpty() && schuelertermine.isEmpty())
			return DeveloperNotificationException.ifMapGetIsNull(_termin_by_id, termin.id).quartal;
		final @NotNull List<GostKlausurvorgabe> vorgaben = new ArrayList<>();
		for (final @NotNull GostKursklausur k : klausuren)
			vorgaben.add(vorgabeByKursklausur(k));
		for (final @NotNull GostSchuelerklausurTermin k : schuelertermine)
			vorgaben.add(vorgabeBySchuelerklausurTermin(k));
		int quartal = -1;
		for (final @NotNull GostKlausurvorgabe v : vorgaben) {
			if (quartal == -1)
				quartal = v.quartal;
			if (quartal != v.quartal)
				return -1;
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
		int minStart = 1440;
		final @NotNull List<GostSchuelerklausurTermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (skts.isEmpty())
			return termin.startzeit;
		for (final @NotNull GostSchuelerklausurTermin skt : skts) {
			if (!includeNachschreiber && skt.folgeNr > 0)
				continue;
			int skStartzeit = -1;
			final @NotNull GostKursklausur kk = kursklausurBySchuelerklausurTermin(skt);
			if (skt.startzeit != null)
				skStartzeit = skt.startzeit;
			else if (kk.startzeit != null)
				skStartzeit = kk.startzeit;
			else if (termin.startzeit != null)
				skStartzeit = termin.startzeit;
			else
				throw new DeveloperNotificationException(
						"Startzeit des Termins nicht definiert, Termin-ID: " + termin.id);
			if (skStartzeit < minStart)
				minStart = skStartzeit;
		}
		return minStart;
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
		int maxEnd = minKlausurstartzeitByTermin(termin, includeNachschreiber) + 1;
		final @NotNull List<GostSchuelerklausurTermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (skts.isEmpty())
			return maxEnd;
		for (final @NotNull GostSchuelerklausurTermin skt : skts) {
			if (!includeNachschreiber && skt.folgeNr > 0)
				continue;
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurTermin(skt);
			int skStartzeit = -1;
			final @NotNull GostKursklausur kk = kursklausurBySchuelerklausurTermin(skt);
			if (skt.startzeit != null)
				skStartzeit = skt.startzeit;
			else if (kk.startzeit != null)
				skStartzeit = kk.startzeit;
			else if (termin.startzeit != null)
				skStartzeit = termin.startzeit;
			else
				throw new DeveloperNotificationException(
						"Startzeit des Termins nicht definiert, Termin-ID: " + termin.id);
			final int endzeit = skStartzeit + vorgabe.dauer + vorgabe.auswahlzeit;
			if (endzeit > maxEnd)
				maxEnd = endzeit;
		}
		return maxEnd;
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
		final List<GostSchuelerklausurTermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		for (final @NotNull GostSchuelerklausurTermin skt : skts) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurTermin(skt);
			minDauer = (minDauer == -1 || vorgabe.dauer < minDauer) ? vorgabe.dauer : minDauer;
		}
		return minDauer == -1 ? 0 : minDauer;
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
		final @NotNull List<GostSchuelerklausurTermin> skts = schuelerklausurterminAktuellGetMengeByTermin(termin);
		for (final @NotNull GostSchuelerklausurTermin skt : skts) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeBySchuelerklausurTermin(skt);
			maxDauer = (vorgabe.dauer > maxDauer) ? vorgabe.dauer : maxDauer;
		}
		return maxDauer;
	}

	// #####################################################################
	// #################### Konfliktberechnung Start ################################
	// #####################################################################

	/**
	 * Prüft, ob {@link GostSchuelerklausurTermin} aus der Menge <code>menge2</code> konfliktfrei in die
	 * Menge <code>menge1</code> hinzugefügt werden können. Falls ein {@link GostSchuelerklausurTermin} aus
	 * <code>menge1</code> bereits in <code>menge2</code> enthalten ist, wird dies nicht als Konflikt
	 * bewertet.
	 *
	 * @param menge1 die Liste der Ziel-{@link GostSchuelerklausurTermin}e, in die die Integration geprüft werden soll
	 * @param menge2 die Liste der Quell-{@link GostSchuelerklausurTermin}e, aus der die Integration in <code>menge1</code> geprüft werden soll
	 *
	 * @return <code>true</code>, wenn die {@link GostSchuelerklausurTermin} aus der Menge <code>menge2</code> konfliktfrei in die
	 * Menge <code>menge1</code> hinzugefügt werden können.
	 */
	private @NotNull List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> konfliktPaarSchuelerklausurtermineGetMenge(
			final List<GostSchuelerklausurTermin> menge1, final List<GostSchuelerklausurTermin> menge2) {
		// Wenn eine von beiden Mengen leer ist, kann kein Konfklikt entstehen
		if ((menge1 == null) || (menge2 == null) || menge1.isEmpty() || menge2.isEmpty())
			return new ArrayList<>();
		// Erstelle Map von Schueler-ID -> GostSchuelerklausurTermin
		final @NotNull Map<Long, GostSchuelerklausurTermin> map1 = new HashMap<>();
		for (final @NotNull GostSchuelerklausurTermin termin1 : menge1)
//			DeveloperNotificationException.ifMapPutOverwrites(map1, schuelerklausurGetByIdOrException(termin1.idSchuelerklausur).idSchueler, termin1);
			// TODO ifMapPutOverwrites geht nicht, weil innerhalb der Schiene schon Konflikte sein können, so dass hier 1 Schüler regulär mehrfach am selben Termin sein kann.
			map1.put(schuelerklausurGetByIdOrException(termin1.idSchuelerklausur).idSchueler, termin1);
		// Erstellen der Konflikt-Map
		return konfliktPaarByMapSchuelerklausurterminToListSchuelerklausurterminGetMenge(map1, menge2);
	}

	private @NotNull List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> konfliktPaarByMapSchuelerklausurterminToListSchuelerklausurterminGetMenge(
			final Map<Long, GostSchuelerklausurTermin> menge1, final List<GostSchuelerklausurTermin> menge2) {
		// Erstelle leere Map für Rückgabe
		final @NotNull List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> ergebnis = new ArrayList<>();
		// Wenn eine von beiden Mengen leer ist, kann kein Konfklikt entstehen
		if ((menge1 == null) || (menge2 == null) || menge1.isEmpty() || menge2.isEmpty())
			return ergebnis;
		// Prüfe jeden GostSchuelerklausurTermin aus menge2, ob der zugehörige Schüler mit einer anderen Klausur in map1 existiert, falls ja, Konflikt hinzufügen
		for (final @NotNull GostSchuelerklausurTermin skt2 : menge2) {
			final @NotNull GostSchuelerklausur sk = schuelerklausurBySchuelerklausurtermin(skt2);
			final GostSchuelerklausurTermin skt1 = menge1.get(sk.idSchueler);
			if ((skt1 != null) && (skt1.id != skt2.id))
				ergebnis.add(new PairNN<>(skt1, skt2));
		}
		return ergebnis;
	}

	/**
	 * Berechnet die Konflikt-Menge, wenn der übergebene {@link GostSchuelerklausurTermin} in den übergebenen {@link GostKlausurtermin} hinzugefügt wird. Falls der übergebene {@link GostSchuelerklausurTermin} bereits im {@link GostKlausurtermin} enthalten ist, wird dies nicht als Konflikt
	 * bewertet.
	 *
	 * @param termin der {@link GostKlausurtermin}, in den <code>skt</code> hinzugefügt werden soll
	 * @param skt der {@link GostSchuelerklausurTermin}, der in <code>termin</code> hinzugefügt werden soll
	 *
	 * @return die Liste von {@link PairNN}en aus den beiden am Konflikt beteiligten {@link GostSchuelerklausurTermin}en
	 */
	public @NotNull List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> konfliktPaarGetMengeTerminAndSchuelerklausurtermin(
			final @NotNull GostKlausurtermin termin,
			final @NotNull GostSchuelerklausurTermin skt) {
		return konfliktPaarSchuelerklausurtermineGetMenge(schuelerklausurterminAktuellGetMengeByTermin(termin), ListUtils.create1(skt));
	}

	/**
	 * Prüft, ob der zu einem {@link GostSchuelerklausurTermin} gehörige Schüler in einer {@link GostKursklausur} enthalten ist.
	 *
	 * @param schuelerklausurTermin der zu prüfende {@link GostSchuelerklausurTermin}
	 * @param kursklausur     die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls der zum {@link GostSchuelerklausurTermin} gehörige Schüler in der {@link GostKursklausur} enthalten ist
	 */
	public boolean konfliktZuKursklausurBySchuelerklausur(final @NotNull GostSchuelerklausurTermin schuelerklausurTermin,
			final @NotNull GostKursklausur kursklausur) {
		final @NotNull List<Long> schuelerids = new ArrayList<>();
		for (final @NotNull GostSchuelerklausur sk : schuelerklausurGetMengeByKursklausur(kursklausur))
			schuelerids.add(sk.idSchueler);
		return schuelerids.contains(schuelerklausurBySchuelerklausurtermin(schuelerklausurTermin).idSchueler);
	}

	/**
	 * Liefert eine Map {@link GostKursklausur} -> Schülerid-Menge, die die bereits existierenden Schüler-id-Konflikte in jeder
	 * {@link GostKursklausur} des übergebenen {@link GostKlausurtermin}s enthält.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return die Map {@link GostKursklausur} -> Schülerid-Menge, die die bereits existierenden Schüler-id-Konflikte in jeder
	 * {@link GostKursklausur} des übergebenen {@link GostKlausurtermin}s enthält
	 */
	public @NotNull Map<GostKursklausur, Set<Long>> konflikteMapKursklausurSchueleridsByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostKursklausur> klausuren = kursklausurGetMengeByTermin(termin);
		return berechneKonflikte(klausuren, klausuren);
	}

	/**
	 * Liefert die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 */
	public int konflikteAnzahlGetByTermin(final @NotNull GostKlausurtermin termin) {
		return countKonflikte(konflikteMapKursklausurSchueleridsByTermin(termin));
	}

	/**
	 * Liefert eine Map {@link GostKursklausur} -> Schülerid-Menge, die nur die neuen Konflikte liefert,
	 * die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Map {@link GostKursklausur} -> Schülerid-Menge, die nur die neuen Konflikte liefert,
	 * die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 */
	public @NotNull Map<GostKursklausur, Set<Long>> konflikteNeuMapKursklausurSchueleridsByTerminAndKursklausur(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur) {
		return berechneKonflikte(kursklausurGetMengeByTermin(termin), ListUtils.create1(kursklausur));
	}

	/**
	 * Liefert die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 */
	public int konflikteAnzahlZuTerminGetByTerminAndKursklausur(final @NotNull GostKlausurtermin termin,
			final @NotNull GostKursklausur kursklausur) {
		return countKonflikte(konflikteNeuMapKursklausurSchueleridsByTerminAndKursklausur(termin, kursklausur));
	}


	/**
	 * Liefert eine Map {@link GostKursklausur} -> Schülerid-Menge, die die bestehenden Konflikte enthält,
	 * die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Map {@link GostKursklausur} -> Schülerid-Menge, die die bestehenden Konflikte enthält,
	 * die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 */
	public @NotNull Map<GostKursklausur, Set<Long>> konflikteZuEigenemTerminMapGetByKursklausur(final @NotNull GostKursklausur klausur) {
		final @NotNull List<GostKursklausur> klausuren1 = new ArrayList<>(
				DeveloperNotificationException.ifMapGetIsNull(_kursklausurmenge_by_idTermin, klausur.idTermin));
		klausuren1.remove(klausur);
		return berechneKonflikte(klausuren1, ListUtils.create1(klausur));
	}

	/**
	 * Liefert die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 */
	public int konflikteAnzahlZuEigenemTerminGetByKursklausur(final @NotNull GostKursklausur klausur) {
		return countKonflikte(konflikteZuEigenemTerminMapGetByKursklausur(klausur));
	}

	private @NotNull Map<GostKursklausur, Set<Long>> berechneKonflikte(final @NotNull List<GostKursklausur> klausuren1,
			final @NotNull List<GostKursklausur> klausuren2) {
		if (klausuren1.isEmpty() || klausuren2.isEmpty())
			return new HashMap<>();
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
		}
		return result;
	}

	private @NotNull Set<Long> berechneKlausurKonflikte(final @NotNull GostKursklausur kk1,
			final @NotNull GostKursklausur kk2) {
		final @NotNull HashSet<Long> konflikte = new HashSet<>(getSchuelerIDsFromKursklausur(kk1));
		konflikte.retainAll(getSchuelerIDsFromKursklausur(kk2));
		return konflikte;
	}

	private static int countKonflikte(final @NotNull Map<GostKursklausur, Set<Long>> konflikte) {
		final @NotNull HashSet<Long> susIds = new HashSet<>();
		for (final @NotNull Set<Long> klausurSids : konflikte.values())
			susIds.addAll(klausurSids);
		return susIds.size();
	}

	// #####################################################################
	// #################### Konfliktberechnung Ende ################################
	// #####################################################################


	// #####################################################################
	// #################### Thresholdberechnung Start ################################
	// #####################################################################

	/**
	 * Liefert für einen Schwellwert und einen {@link GostKlausurtermin} eine Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 *
	 * @param termin    der {@link GostKlausurtermin}, dessen Kalenderwoche geprüft wird
	 * @param threshold der Schwellwert (z.B. 3), der mindestens erreicht sein muss, damit die Schüler-IDs in die Rückgabe-Map aufgenommen wird
	 *
	 * @return die Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 */
	public @NotNull Map<Long, Set<GostSchuelerklausurTermin>> klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(
			final @NotNull GostKlausurtermin termin, final int threshold) {
		if (termin.datum == null)
			return new HashMap<>();
		final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abijahr, null, threshold, false);
	}

	/**
	 * Liefert für einen Schwellwert, einen {@link GostKlausurtermin} und eine {@link GostKursklausur} eine Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e, wenn die übergebene {@link GostKursklausur} in den übergebenen {@link GostKlausurtermin} integriert würde.
	 *
	 * @param termin    der {@link GostKlausurtermin}, dessen Kalenderwoche geprüft wird
	 * @param klausur   die {@link GostKursklausur}, deren Integration in den {@link GostKlausurtermin} <code>termin</code> angenommen wird
	 * @param threshold der Schwellwert (z.B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 *
	 * @return die Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e, wenn die übergebene {@link GostKursklausur} in den übergebenen {@link GostKlausurtermin} integriert würde.
	 */
	public @NotNull Map<Long, Set<GostSchuelerklausurTermin>> klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur klausur, final int threshold) {
		if (termin.datum == null)
			return new HashMap<>();
		final int kw = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abijahr,
				schuelerklausurterminGetMengeByKursklausur(klausur), threshold, false);
	}

	/**
	 * Liefert für einen Schwellwert und ein Datum eine Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e, wenn der übergebene {@link GostKlausurtermin} in die Kalenderwoche zusätzlich geplant würde.
	 *
	 * @param termin        der Klausurtermin, der zusätzlich in die durch <code>datum</code> angegebene Kalenderwoche geplant werden soll
	 * @param datum         das Datum, dessen Kalenderwoche auf die Klausuranzahl geprüft wird
	 * @param threshold     der Schwellwert (z.B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdOnly wenn <code>true</code> wird die Schüler-ID nur bei exaktem Erreichen des <code>threshold</code> in die Rückgabe-Map aufgenommen. Größere Werte werden nicht berücksichtigt.
	 *
	 * @return die Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e, wenn der übergebene {@link GostKlausurtermin} in die Kalenderwoche zusätzlich geplant würde.
	 */
	public @NotNull Map<Long, Set<GostSchuelerklausurTermin>> klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(
			final @NotNull GostKlausurtermin termin, final @NotNull String datum, final int threshold,
			final boolean thresholdOnly) {
		final int kwDatum = DateUtils.gibKwDesDatumsISO8601(datum);
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kwDatum, termin.abijahr,
				schuelerklausurterminAktuellGetMengeByTermin(termin), threshold, thresholdOnly);
	}

	private @NotNull Map<Long, Set<GostSchuelerklausurTermin>> klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(
			final int kw, final int abijahr, final List<GostSchuelerklausurTermin> addMenge, final int threshold, final boolean thresholdOnly) {

		final Map<Long, List<GostSchuelerklausurTermin>> schuelerklausurterminaktuellmenge_by_schuelerId =
				_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId.getMap3OrNull(kw, abijahr);
		if (schuelerklausurterminaktuellmenge_by_schuelerId == null)
			return new HashMap<>();

		final @NotNull Map<Long, List<GostSchuelerklausurTermin>> addTerminMap = new HashMap<>();
		if (addMenge != null)
			for (final GostSchuelerklausurTermin addSkt : addMenge)
				MapUtils.getOrCreateArrayList(addTerminMap, schuelerklausurBySchuelerklausurtermin(addSkt).idSchueler).add(addSkt);

		final @NotNull Map<Long, Set<GostSchuelerklausurTermin>> ergebnis = new HashMap<>();
		for (final @NotNull Entry<Long, List<GostSchuelerklausurTermin>> entry : schuelerklausurterminaktuellmenge_by_schuelerId.entrySet()) {
			final Set<GostSchuelerklausurTermin> klausuren = new HashSet<>(entry.getValue());
			if (addMenge != null) {
				final List<GostSchuelerklausurTermin> addSkts = addTerminMap.get(entry.getKey());
				if (addSkts != null)
					klausuren.addAll(addTerminMap.get(entry.getKey()));
			}
			if ((klausuren.size() == threshold) || ((klausuren.size() > threshold) && !thresholdOnly))
				ergebnis.put(entry.getKey(), klausuren);
		}
		return ergebnis;
	}

	/**
	 * Liefert für einen Schwellwert, eine Kalenderwoche, und ein Abiturjahr eine Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 *
	 * @param kw            die Kalenderwoche, für die die Klausuranzahl geprüft wird
	 * @param abijahr       das Abiturjahr der gesuchten Konflikt-Schüler
	 * @param threshold     der Schwellwert (z.B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdOnly wenn <code>true</code> wird die Schüler-ID nur bei exaktem Erreichen des <code>threshold</code> in die Rückgabe-Map aufgenommen. Größere Werte werden nicht berücksichtigt.
	 *
	 * @return die Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 */
	public @NotNull Map<Long, Set<GostSchuelerklausurTermin>> klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(
			final int kw, final int abijahr, final int threshold, final boolean thresholdOnly) {
		return klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, abijahr, null, threshold, thresholdOnly);
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
	public @NotNull List<Long> getSchuelerIDsFromKursklausur(final @NotNull GostKursklausur kk) {
		return getSchuelerIDsFromSchuelerklausuren(schuelerklausurGetMengeByKursklausur(kk));
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
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurTermin} oder <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public GostKlausurtermin terminOrNullBySchuelerklausurTermin(final @NotNull GostSchuelerklausurTermin termin) {
		if (termin.folgeNr > 0)
			return (termin.idTermin == null) ? null : terminGetByIdOrException(termin.idTermin);
		return terminOrNullByKursklausur(kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurTermin}. Wenn noch kein Termin bestimmt ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public @NotNull GostKlausurtermin terminOrExceptionBySchuelerklausurTermin(
			final @NotNull GostSchuelerklausurTermin termin) {
		if (termin.folgeNr > 0) {
			return terminGetByIdOrException(DeveloperNotificationException
					.ifNull(String.format("idTermin von Termin %d", termin.id), termin.idTermin));
		}
		return terminOrExceptionByKursklausur(kursklausurBySchuelerklausurTermin(termin));
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
	 * Liefert die {@link GostKlausurvorgabe} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurTermin}, zu dem die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public @NotNull GostKlausurvorgabe vorgabeBySchuelerklausurTermin(
			final @NotNull GostSchuelerklausurTermin klausur) {
		return vorgabeBySchuelerklausur(schuelerklausurGetByIdOrException(klausur.idSchuelerklausur));
	}

	/**
	 * Liefert die {@link GostSchuelerklausur} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurTermin}, zu der die {@link GostSchuelerklausur} gesucht wird.
	 *
	 * @return die {@link GostSchuelerklausur}
	 */
	public @NotNull GostSchuelerklausur schuelerklausurBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurTermin klausur) {
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
	 * Liefert die {@link GostKursklausur} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu der die {@link GostKursklausur} gesucht wird.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public @NotNull GostKursklausur kursklausurBySchuelerklausurTermin(
			final @NotNull GostSchuelerklausurTermin termin) {
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
		final List<GostKursklausur> klausuren = _kursklausurmenge_by_idVorgabe.get(vorgabe.idVorgabe);
		return (klausuren != null) && !klausuren.isEmpty();
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
		if (previousVorgabe == null)
			return null;
		final List<GostKursklausur> klausuren = _kursklausurmenge_by_idVorgabe.get(previousVorgabe.idVorgabe);
		if (klausuren == null)
			return null;
		for (final @NotNull GostKursklausur k : klausuren) {
			final KursDaten kKurs = getKursManager().get(k.idKurs);
			final KursDaten klausurKurs = getKursManager().get(klausur.idKurs);
			if ((kKurs == null) || (klausurKurs == null))
				throw new DeveloperNotificationException("Keine Kurszuordnung im kursManager zu Kurs-ID");
			if (kKurs.kuerzel.equals(klausurKurs.kuerzel)) // TODO unsauber, aber KursId geht nicht, weil ggf. in
				// Schuljahresabschnitten unterschiedlich
				return k;
		}
		return null;
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
		final GostKlausurtermin termin = terminOrNullByKursklausur(klausur);
		if (klausur.startzeit != null)
			return klausur.startzeit;
		return (termin == null) ? null : termin.startzeit;
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
				: DeveloperNotificationException.ifNull("Startzeit darf nicht null sein.",
						terminOrExceptionByKursklausur(klausur).startzeit);
	}

	/**
	 * Prüft, ob die übergebene {@link GostKursklausur} eine vom zugewiesenen {@link GostKlausurtermin} abweichende Startzeit hat. Ist der {@link GostKursklausur} noch kein {@link GostKlausurtermin} zugewiesen oder dem Termin noch keine Startzeit zugewiesen, wird <code>false</code> zurückgegeben.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Startzeit geprüft wird.
	 *
	 * @return <code>true</code>, wenn die {@link GostKursklausur} eine vom {@link GostKlausurtermin} abweichende Startzeit aufweist.
	 */
	public boolean hatAbweichendeStartzeitByKursklausur(final @NotNull GostKursklausur klausur) {
		final GostKlausurtermin termin = terminOrNullByKursklausur(klausur);
		return !((klausur.startzeit == null) || (termin == null) || (termin.startzeit == null)
				|| termin.startzeit.equals(klausur.startzeit));
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einer {@link GostSchuelerklausur} zurück.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der die {@link GostSchuelerklausurTermin}e gesucht werden.
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeBySchuelerklausur(
			final @NotNull GostSchuelerklausur sk) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, sk.id);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausurTermin> list = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		return (list != null) ? list : new ArrayList<>();
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. werden Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code>, werden Fremdtermine am selben Datum wie <code>termin</code> aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. sind Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByTerminIncludingFremdtermine(final @NotNull GostKlausurtermin termin,
			final boolean fremdTermine) {
		return fremdTermine
				? schuelerklausurterminGetMengeByTerminmenge(
						terminGetMengeByDatum(DeveloperNotificationException.ifNull("Termin muss ein Datum haben", termin.datum)))
				: schuelerklausurterminGetMengeByTermin(termin);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einer Menge von {@link GostKlausurtermin}en zurück.
	 *
	 * @param termine die Liste der {@link GostKlausurtermin}e
	 *
	 * @return die Liste von zugehörigen {@link GostSchuelerklausurTermin}en
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByTerminmenge(final @NotNull List<GostKlausurtermin> termine) {
		final @NotNull List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurtermin termin : termine) {
			final List<GostSchuelerklausurTermin> teilListe = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
			if (teilListe != null)
				ergebnis.addAll(teilListe);
		}
		return ergebnis;
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(
			final @NotNull GostKlausurtermin termin, final @NotNull GostKursklausur kursklausur) {
		final List<GostSchuelerklausurTermin> list = _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur
				.getOrNull(termin.id, kursklausur.id);
		return (list != null) ? list : new ArrayList<>();
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminAktuellGetMengeByTermin(
			final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		if (_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.containsKey1(termin.id)) {
			final List<List<GostSchuelerklausurTermin>> lists = _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur
					.getNonNullValuesOfKey1AsList(termin.id);
			for (final List<GostSchuelerklausurTermin> list : lists)
				ergebnis.addAll(list);
		}
		return ergebnis;
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
		final List<GostSchuelerklausurTermin> list = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (list == null)
			return ergebnis;
		for (final @NotNull GostSchuelerklausurTermin t : list)
			ergebnis.add(schuelerklausurBySchuelerklausurtermin(t));
		return ergebnis;
	}

	/**
	 * Prüft, ob der übergebene {@link GostSchuelerklausurTermin} der aktuellste Termin der zugehörigen {@link GostSchuelerklausur} ist.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, der auf Aktualität geprüft werden soll
	 *
	 * @return <code>true</code>, wenn es sich um den aktuellsten {@link GostSchuelerklausurTermin} handelt, sonst <code>false</code>
	 */
	public boolean istSchuelerklausurterminAktuell(final @NotNull GostSchuelerklausurTermin skt) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminaktuell_by_idSchuelerklausur, skt.idSchuelerklausur) == skt;
	}

	/**
	 * Liefert den aktuellen {@link GostSchuelerklausurTermin} zu einer übergebenen
	 * {@link GostSchuelerklausur}
	 *
	 * @param schuelerklausur die {@link GostSchuelerklausur}, deren aktueller
	 *                          {@link GostSchuelerklausurTermin} gesucht wird
	 *
	 * @return den aktuellen {@link GostSchuelerklausurTermin} zur übergebenen {@link GostSchuelerklausur}
	 */
	public @NotNull GostSchuelerklausurTermin schuelerklausurterminAktuellBySchuelerklausur(
			final @NotNull GostSchuelerklausur schuelerklausur) {
		return DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminaktuell_by_idSchuelerklausur, schuelerklausur.id);
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen ein {@link GostKlausurtermin} zugewiesen wurde.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen ein {@link GostKlausurtermin} zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(
			final int abiJahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final @NotNull List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		if (quartal > 0) {
			final Map<Long, List<GostSchuelerklausurTermin>> map4 = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
					.getMap4OrNull(abiJahrgang, halbjahr.id, quartal);
			if (map4 != null)
				for (final @NotNull Entry<Long, List<GostSchuelerklausurTermin>> entry : map4.entrySet())
					if (entry.getKey() != -1)
						ergebnis.addAll(entry.getValue());
		} else {
			final Map<Integer, Map<Long, List<GostSchuelerklausurTermin>>> map3 =
					_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
							.getMap3OrNull(abiJahrgang, halbjahr.id);
			if (map3 != null)
				for (final @NotNull Entry<Integer, Map<Long, List<GostSchuelerklausurTermin>>> entry : map3.entrySet())
					for (final @NotNull Entry<Long, List<GostSchuelerklausurTermin>> entry2 : entry.getValue()
							.entrySet())
						if (entry2.getKey() != -1)
							ergebnis.addAll(entry2.getValue());
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(
			final int abiJahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final @NotNull List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		if (quartal > 0) {
			final Map<Long, List<GostSchuelerklausurTermin>> map4 = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
					.getMap4OrNull(abiJahrgang, halbjahr.id, quartal);
			if (map4 == null)
				return ergebnis;
			for (final @NotNull List<GostSchuelerklausurTermin> terminList : map4.values())
				ergebnis.addAll(terminList);
		} else {
			final Map<Integer, Map<Long, List<GostSchuelerklausurTermin>>> map3 =
					_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
							.getMap3OrNull(abiJahrgang, halbjahr.id);
			if (map3 == null)
				return ergebnis;
			for (final @NotNull Map<Long, List<GostSchuelerklausurTermin>> map4 : map3.values())
				for (final @NotNull List<GostSchuelerklausurTermin> terminList : map4.values())
					ergebnis.addAll(terminList);
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen ein {@link GostKlausurtermin} inklusive Datum zugewiesen wurde.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen ein {@link GostKlausurtermin} inklusive Datum zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(
			final int abiJahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		final @NotNull List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurTermin termin : schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(
				abiJahrgang, halbjahr, quartal)) {
			final GostKlausurtermin t = terminOrNullBySchuelerklausurTermin(termin);
			if ((t != null) && (t.datum != null))
				ergebnis.add(termin);
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen
	 * Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal denen noch kein {@link GostKlausurtermin} zugewiesen wurde.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr    das {@link GostHalbjahr}
	 * @param quartal     die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen Abijahrgang, {@link GostHalbjahr} und Quartal
	 * denen noch kein {@link GostKlausurtermin} zugewiesen wurde.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(
			final int abiJahrgang, final @NotNull GostHalbjahr halbjahr, final int quartal) {
		if (quartal > 0) {
			final List<GostSchuelerklausurTermin> skts = _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
					.getOrNull(abiJahrgang, halbjahr.id, quartal, -1L);
			if (skts != null)
				skts.sort(_compSchuelerklausurTermin);
			return (skts != null) ? skts : new ArrayList<>();
		}
		final @NotNull List<GostSchuelerklausurTermin> skts = new ArrayList<>();
		final Map<Integer, Map<Long, List<GostSchuelerklausurTermin>>> mapHalbjahr =
				_schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin
						.getMap3OrNull(abiJahrgang, halbjahr.id);
		if (mapHalbjahr != null)
			for (final @NotNull Map<Long, List<GostSchuelerklausurTermin>> sktList : mapHalbjahr.values()) {
				final List<GostSchuelerklausurTermin> listTermine = sktList.get(-1L);
				if (listTermine != null)
					skts.addAll(listTermine);
			}
		skts.sort(_compSchuelerklausurTermin);
		return skts;
	}

	/**
	 * Liefert eine Liste von Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminNtGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final @NotNull List<GostSchuelerklausurTermin> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurTermin skt : schuelerklausurterminGetMengeByTermin(termin))
			if (skt.folgeNr > 0)
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert den {@link GostSchuelerklausurTermin}, sofern vorhanden, zu einem {@link GostKlausurtermin} und einer Schüler-ID.
	 *
	 * @param termin   der {@link GostKlausurtermin}
	 * @param idSchueler die ID des Schülers
	 *
	 * @return das {@link GostSchuelerklausurTermin} zu einem {@link GostKlausurtermin} und einer Schüler-ID, sonst <code>null</code>.
	 */
	public GostSchuelerklausurTermin schuelerklausurterminByTerminAndSchuelerid(final @NotNull GostKlausurtermin termin,
			final long idSchueler) {
		final List<GostSchuelerklausurTermin> skts = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts != null)
			for (final @NotNull GostSchuelerklausurTermin skt : skts)
				if (schuelerklausurGetByIdOrException(skt.idSchuelerklausur).idSchueler == idSchueler)
					return skt;
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
		final List<GostSchuelerklausur> listSks = _schuelerklausurmenge_by_idKursklausur.get(kursklausur.id);
		return (listSks == null) ? new ArrayList<>() : listSks;
	}

	/**
	 * Liefert den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull LehrerListeEintrag kursLehrerByKursklausur(final @NotNull GostKursklausur k) {
		final @NotNull KursDaten kurs = kursdatenByKursklausur(k);
		return DeveloperNotificationException.ifMapGetIsNull(getLehrerMap(), kurs.lehrer);
	}


	/**
	 * Liefert das Lehrerkürzel zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das Lehrerkürzel zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull String kursLehrerKuerzelByKursklausur(final @NotNull GostKursklausur k) {
		return kursLehrerByKursklausur(k).kuerzel;
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
		if (kurs == null)
			throw new DeveloperNotificationException("Kurs mit ID " + k.idKurs + " nicht in KursManager vorhanden.");
		return kurs;
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull GostFach fachByKursklausur(final @NotNull GostKursklausur k) {
		final GostFach fach = getFaecherManager().get(vorgabeByKursklausur(k).idFach);
		if (fach == null)
			throw new DeveloperNotificationException(
					"Fach mit ID " + vorgabeByKursklausur(k).idFach + " nicht in GostFaecherManager vorhanden.");
		return fach;
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
	 * Liefert die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 */
	public @NotNull KursDaten kursdatenBySchuelerklausur(final @NotNull GostSchuelerklausur k) {
		return kursdatenByKursklausur(kursklausurBySchuelerklausur(k));
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
		final List<GostSchuelerklausur> liste = _schuelerklausurmenge_by_idKursklausur.get(k.id);
		return (liste == null) ? 0 : liste.size();
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches zur übergebenen {@link GostKursklausur} als Aufruf der rgba-Funktion
	 * mit der Transparenz 1.0 zurück.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public @NotNull String fachHTMLFarbeRgbaByKursklausur(final @NotNull GostKursklausur k) {
		return ZulaessigesFach.getByKuerzelASD(fachByKursklausur(k).kuerzel).getHMTLFarbeRGBA(1.0);
	}

	/**
	 * Liefert den Vorgänger-{@link GostSchuelerklausurTermin}, sofern vorhanden, zu einem {@link GostSchuelerklausurTermin}, also den
	 * versäumte Schülerklausurtermin.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, dessen Vorgänger gesucht wird
	 *
	 * @return den Vorgänger-{@link GostSchuelerklausurTermin} oder <code>null</code>
	 */
	public GostSchuelerklausurTermin schuelerklausurterminVorgaengerBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurTermin skt) {
		final @NotNull List<GostSchuelerklausurTermin> alleTermine = DeveloperNotificationException
				.ifMapGetIsNull(_schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur);
		for (final @NotNull GostSchuelerklausurTermin skAktuell : alleTermine)
			if (skAktuell.folgeNr == (skt.folgeNr - 1))
				return skAktuell;
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
		final List<GostSchuelerklausur> listSks = _schuelerklausurmenge_by_idKursklausur.get(k.id);
		if (listSks != null)
			for (final @NotNull GostSchuelerklausur sk : listSks)
				if (DeveloperNotificationException.ifMapGetIsNull(getSchuelerMap(),
						sk.idSchueler).externeSchulNr != null)
					return true;
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
		final List<GostSchuelerklausurTermin> listSkts = _schuelerklausurterminmenge_by_idTermin.get(t.id);
		if (listSkts != null)
			for (final @NotNull GostSchuelerklausurTermin skt : listSkts)
				if (vorgabeBySchuelerklausurTermin(skt).abiJahrgang != t.abijahr)
					return true;
		return false;
	}

	/**
	 * Gibt das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurTermin}
	 * zurück. Falls kein Vorgängertermin existiert, wird eine <code>DeveloperNotificationException</code> geworfen. Falls noch kein Termin oder kein Datum zugewiesen ist, wird <code>null</code> zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, dessen Vorgänger-Datum gesucht wird.
	 *
	 * @return das Datum des Vorgängertermins zum übergebenen {@link GostSchuelerklausurTermin}
	 */
	public String datumSchuelerklausurVorgaenger(final @NotNull GostSchuelerklausurTermin skt) {
		final GostSchuelerklausurTermin vorgaengerSkt = schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt);
		if (vorgaengerSkt == null)
			throw new DeveloperNotificationException("Kein Vorgängertermin zu Schülerklausurtermin gefunden.");
		final GostKlausurtermin termin = terminOrNullBySchuelerklausurTermin(vorgaengerSkt);
		return (termin == null) ? null : termin.datum;
	}

	/**
	 * Erstellt ein {@link GostKlausurenUpdate}-Objekt für den API-Call, der beim
	 * übergebenen {@link GostKlausurtermin} die Nachschreiberzulassung entfernt und ggf.
	 * schon zugewiesene {@link GostSchuelerklausurTermin}e aus diesem entfernt.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return das {@link GostKlausurenUpdate}-Objekt für den API-Call, der beim
	 * übergebenen {@link GostKlausurtermin} die Nachschreiberzulassung entfernt und ggf.
	 * schon zugewiesene {@link GostSchuelerklausurTermin}e aus diesem entfernt
	 */
	public @NotNull GostKlausurenUpdate patchKlausurterminNachschreiberZuglassenFalse(
			final @NotNull GostKlausurtermin termin) {
		final GostKlausurenUpdate update = new GostKlausurenUpdate();
		update.listKlausurtermineNachschreiberZugelassenFalse.add(termin.id);
		for (final @NotNull GostSchuelerklausurTermin skt : schuelerklausurterminNtGetMengeByTermin(termin))
			update.listSchuelerklausurTermineRemoveIdTermin.add(skt.id);
		return update;
	}

	/**
	 * Führt alle Attribut-Patches aller Objekte im übergeben {@link GostKlausurenUpdate} im
	 * Manager durch.
	 *
	 * @param update das {@link GostKlausurenUpdate}-Objekt mit den zu patchenden Werten
	 *
	 */
	public void updateExecute(final @NotNull GostKlausurenUpdate update) {
		for (final long sktId : update.listSchuelerklausurTermineRemoveIdTermin) {
			final @NotNull GostSchuelerklausurTermin skt = schuelerklausurterminGetByIdOrException(sktId);
			skt.idTermin = null;
			schuelerklausurterminPatchAttributes(skt);
		}
		for (final long ktId : update.listKlausurtermineNachschreiberZugelassenFalse) {
			final @NotNull GostKlausurtermin kt = terminGetByIdOrException(ktId);
			kt.nachschreiberZugelassen = false;
			terminPatchAttributes(kt);
		}
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
		final List<GostSchuelerklausurTermin> skts = _schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts == null)
			return false;
		for (final @NotNull GostSchuelerklausurTermin skt : skts)
			if (schuelerklausurBySchuelerklausurtermin(skt).idSchueler == idSchueler
					&& istSchuelerklausurterminAktuell(skt))
				return true;
		return false;
	}

	/**
	 * Liefert zu einer {@link GostKursklausur} die {@link GostSchuelerklausurTermin}e der Schüler, die den
	 * Kurs schriftlich belegt haben
	 *
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die {@link GostSchuelerklausurTermin}e der Schüler, die den
	 * Kurs schriftlich belegt haben
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByKursklausur(
			final @NotNull GostKursklausur kursklausur) {
		final List<GostSchuelerklausurTermin> ergebnis = _schuelerklausurterminmenge_by_idKursklausur
				.get(kursklausur.id);
		if (ergebnis == null)
			return new ArrayList<>();
		ergebnis.sort(_compSchuelerklausurTermin);
		return ergebnis;
	}

	/**
	 * Liefert zu einem {@link GostSchuelerklausurTermin} den zugehörigen {@link SchuelerListeEintrag}
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}
	 *
	 * @return der zugehörige {@link SchuelerListeEintrag}
	 */
	public @NotNull SchuelerListeEintrag schuelerlisteeintragGetBySchuelerklausurtermin(
			final @NotNull GostSchuelerklausurTermin skt) {
		return DeveloperNotificationException.ifMapGetIsNull(getSchuelerMap(),
				schuelerklausurBySchuelerklausurtermin(skt).idSchueler);
	}

	/**
	 * Liefert die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 *
	 * @param raum       der {@link GostKlausurraum}
	 * @param zeitraster das {@link StundenplanZeitraster}
	 *
	 * @return die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 */
	public GostKlausurraumstunde klausurraumstundeGetByRaumAndZeitraster(final @NotNull GostKlausurraum raum, final @NotNull StundenplanZeitraster zeitraster) {
		return _raumstunde_by_idRaum_and_idZeitraster.getOrNull(raum.id, zeitraster.id);
	}

	/**
	 * Liefert die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum} zurück.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return die die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum} zurück.
	 */
	public @NotNull List<GostKlausurraumstunde> klausurraumstundeGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final List<GostKlausurraumstunde> stunden = _raumstundenmenge_by_idRaum.get(raum.id);
		return (stunden != null) ? stunden : new ArrayList<>();
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich anhand der übergebenen {@link GostKlausurenCollectionSkrsKrsData}. Diese Methode sollte nur nach einem API-Call aufgerufen werden, in dem das {@link GostKlausurenCollectionSkrsKrsData}-Objekt erzeugt wurde.
	 *
	 * @param collectionSkrsKrs die {@link GostKlausurenCollectionSkrsKrsData}
	 */
	public void setzeRaumZuSchuelerklausuren(final @NotNull GostKlausurenCollectionSkrsKrsData collectionSkrsKrs) {
		raumstundeAddAllOhneUpdate(collectionSkrsKrs.raumdata.raumstunden);
		raumstundeRemoveAllOhneUpdate(collectionSkrsKrs.raumstundenGeloescht);
		schuelerklausurraumstundeRemoveAllOhneUpdateByIdSchuelerklausurtermin(collectionSkrsKrs.idsSchuelerklausurtermine);
		schuelerklausurraumstundeAddAllOhneUpdate(collectionSkrsKrs.raumdata.sktRaumstunden);

		update_all();
	}

	/**
	 * Liefert die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 */
	public @NotNull List<GostKursklausur> kursklausurGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final List<GostKursklausur> kursklausuren = new ArrayList<>();
		if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.containsKey1(raum.id))
			return kursklausuren;
		for (final long idKK : _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getKeySetOf(raum.id)) {
			if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getOrException(raum.id, idKK).isEmpty())
				kursklausuren.add(kursklausurGetByIdOrException(idKK));
		}
		return kursklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden und zu einer {@link GostKursklausur} gehören.
	 *
	 * @param raum der {@link GostKlausurraum}
	 * @param kursklausur die {@link GostKursklausur}
	 *
	 * @return die Liste der GostKursklausuren
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByRaumAndKursklausur(final @NotNull GostKlausurraum raum, final @NotNull GostKursklausur kursklausur) {
		return DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur, raum.id, kursklausur.id);
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurterminGetMengeByRaum(final @NotNull GostKlausurraum raum) {
		final List<GostSchuelerklausurTermin> schuelerklausuren = new ArrayList<>();
		if (!_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.containsKey1(raum.id))
			return schuelerklausuren;
		for (final long idKK : _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getKeySetOf(raum.id))
			schuelerklausuren.addAll(_schuelerklausurterminmenge_by_idRaum_and_idKursklausur.getOrException(raum.id, idKK));
		return schuelerklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurTermin}e  zu einem {@link GostKlausurtermin} zurück, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die die Menge aller {@link GostSchuelerklausurTermin}e  zu einem {@link GostKlausurtermin}, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 */
	public @NotNull List<GostSchuelerklausurTermin> schuelerklausurOhneRaumGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<GostSchuelerklausurTermin> schuelerklausuren =
				_schuelerklausurterminmenge_by_idRaum_and_idTermin.getOrNull(-1L, termin.id);
		return (schuelerklausuren == null) ? new ArrayList<>() : schuelerklausuren;
	}

	/**
	 * Liefert eine Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 */
	public @NotNull List<StundenplanRaum> stundenplanraumVerfuegbarGetMengeByTermin(final @NotNull GostKlausurtermin termin) {
		final List<StundenplanRaum> raeume = new ArrayList<>();
		for (final @NotNull StundenplanRaum raum : getStundenplanManager().raumGetMengeAsList())
			if (!_raum_by_idTermin_and_idStundenplanraum.contains(termin.id, raum.id))
				raeume.add(raum);
		return raeume;
	}

	/**
	 * Prüft, ob alle zu einer {@link GostKursklausur} gehörenden {@link GostSchuelerklausurTermin}e an einem bestimmten {@link GostKlausurtermin} einem {@link GostKlausurraum}
	 * zugeordnet sind. Wird kein {@link GostKlausurtermin} übergeben, wird der Haupttermin der {@link GostKursklausur} geprüft.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}. Wird kein <code>null</code> übergeben, wird der Haupttermin der {@link GostKursklausur} geprüft.
	 * @param kk die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, wenn alle {@link GostSchuelerklausurTermin}e verplant sind, sonst <code>false</code>.
	 */
	public boolean isKursklausurAlleSchuelerklausurenVerplant(final @NotNull GostKursklausur kk, final GostKlausurtermin termin) {
		for (final @NotNull GostSchuelerklausurTermin sk : DeveloperNotificationException.ifMap2DGetIsNull(_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur, termin != null ? termin.id : kk.idTermin, kk.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob alle zu einer {@link GostKlausurtermin} gehörenden {@link GostSchuelerklausurTermin}e einem {@link GostKlausurraum}
	 * zugeordnet sind.
	 *
	 * @param t der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code>, wenn alle {@link GostSchuelerklausurTermin}e verplant sind, sonst <code>false</code>.
	 */
	public boolean isTerminAlleSchuelerklausurenVerplant(final @NotNull GostKlausurtermin t) {
		for (final @NotNull GostSchuelerklausurTermin sk : DeveloperNotificationException.ifMapGetIsNull(_schuelerklausurterminmenge_by_idTermin, t.id)) {
			if (!_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
				return false;
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
		return _schuelerklausurterminmenge_by_idRaum_and_idKursklausur.contains(raum.id, kursklausur.id);
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
		int dauer = -1;
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaum(raum)) {
			final @NotNull GostKlausurvorgabe vorgabe = vorgabeByKursklausur(klausur);
			if (dauer == -1)
				dauer = vorgabe.dauer;
			if (dauer != vorgabe.dauer)
				return null;
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
		Integer start = -1;
		for (final @NotNull GostKursklausur klausur : kursklausurGetMengeByRaum(raum)) {
			if ((start != null) && (start == -1))
				start = klausur.startzeit;
			if (hatAbweichendeStartzeitByKursklausur(klausur))
				return null;
		}
		return (start == null) ? terminGetByIdOrException(raum.idTermin).startzeit : start;
	}

	/**
	 * Liefert <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 */
	public boolean isKlausurenInFremdraeumenByTermin(final @NotNull GostKlausurtermin termin) {
		for (final @NotNull GostSchuelerklausurTermin skt : schuelerklausurterminGetMengeByTermin(termin)) {
			final GostKlausurraum raum = _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
			if ((raum != null) && (raum.idTermin != terminOrExceptionBySchuelerklausurTermin(skt).id))
				return true;
		}
		return false;
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurTermin} den zugehörigen {@link GostKlausurraum}, falls ein solcher schon zugeordnet ist.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, zu dem der {@link GostKlausurraum} gesucht wird.
	 *
	 * @return den {@link GostKlausurraum}, falls einer zugewiesen ist, sonst <code>null</code>.
	 */
	public GostKlausurraum klausurraumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurTermin skt) {
		return _klausurraum_by_idSchuelerklausurtermin.get(skt.id);
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurTermin} zugehörigen {@link StundenplanRaum} zurück.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, zu dem der {@link StundenplanRaum} gesucht wird.
	 *
	 * @return den {@link StundenplanRaum}, falls einer zugewiesen ist, sonst <code>null</code>
	 */
	public StundenplanRaum stundenplanraumGetBySchuelerklausurtermin(final @NotNull GostSchuelerklausurTermin skt) {
		final GostKlausurraum raum = klausurraumGetBySchuelerklausurtermin(skt);
		return ((raum == null) || (raum.idStundenplanRaum == null)) ? null : getStundenplanManager().raumGetByIdOrException(raum.idStundenplanRaum);
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
	 * Prüft, ggf. jahrgangsübergreifend, ob {@link GostSchuelerklausurTermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 *
	 * @param termin der {@link GostKlausurtermin}, dessen {@link GostSchuelerklausurTermin}e geprüft werden
	 * @param fremdTermine wenn <code>true</code>, werden auch {@link GostSchuelerklausurTermin}e anderer Jahrgänge am selben Datum berücksichtigt.
	 *
	 * @return <code>true</code>, falls {@link GostSchuelerklausurTermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 */
	public boolean isSchuelerklausurenInRaumByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		for (final @NotNull GostSchuelerklausurTermin teilTermin : schuelerklausurterminGetMengeByTerminIncludingFremdtermine(termin, fremdTermine))
			if (_raumstundenmenge_by_idSchuelerklausurtermin.containsKey(teilTermin.id))
				return true;
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
			if (teilListe != null)
				ergebnis.addAll(teilListe);
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
			if (raum.idStundenplanRaum != null)
				kapazitaet += getStundenplanManager().raumGetByIdOrException(raum.idStundenplanRaum).groesse;
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
		return schuelerklausurterminGetMengeByTerminIncludingFremdtermine(termin, fremdTermine).size();
	}

	/**
	 * Prüft, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurTermin}en ausreichend ist.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die vorhandenen und benötigten Plätze von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 *
	 * @return <code>true</code>, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurTermin}en ausreichend ist.
	 */
	public boolean isPlatzkapazitaetAusreichendByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		return anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin, fremdTermine) <= anzahlPlaetzeAlleRaeumeByTermin(termin, fremdTermine);
	}

	/**
	 * Erzeugt aus einer Liste von {@link GostSchuelerklausurTermin}en eine um z.B. für Blockungs-Algorithmen relevante Informationen angereicherte Liste von {@link GostSchuelerklausurTerminRich}-Objekten.
	 *
	 * @param termine die Liste der {@link GostSchuelerklausurTermin}e.
	 *
	 * @return die Liste von angereicherten {@link GostSchuelerklausurTerminRich}-Objekten
	 */
	public @NotNull List<GostSchuelerklausurTerminRich> enrichSchuelerklausurtermine(final @NotNull List<GostSchuelerklausurTermin> termine) {
		final @NotNull List<GostSchuelerklausurTerminRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostSchuelerklausurTermin termin : termine)
			ergebnis.add(new GostSchuelerklausurTerminRich(termin, this));
		return ergebnis;
	}

	/**
	 * Erzeugt aus einer Liste von {@link GostKlausurraum}en eine um z.B. für Blockungs-Algorithmen relevante Informationen angereicherte Liste von {@link GostKlausurraumRich}-Objekten.
	 *
	 * @param raeume die Liste der {@link GostKlausurraum}e.
	 *
	 * @return die Liste von angereicherten {@link GostKlausurraumRich}-Objekten
	 */
	public @NotNull List<GostKlausurraumRich> enrichKlausurraeume(final @NotNull List<GostKlausurraum> raeume) {
		final @NotNull List<GostKlausurraumRich> ergebnis = new ArrayList<>();
		for (final @NotNull GostKlausurraum raum : raeume)
			ergebnis.add(new GostKlausurraumRich(raum, stundenplanraumGetByKlausurraum(raum)));
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
		return getStundenplanManager()
				.raumGetByIdOrException(DeveloperNotificationException.ifNull("StundenplanRaum darf nicht NULL sein", raum.idStundenplanRaum));
	}

	/**
	 * Prüft, ob allen zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}en ein {@link StundenplanRaum} zugewiesen ist.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die {@link GostKlausurraum}e von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 *
	 * @return <code>true</code>, falls allen zum übergebenen {@link GostKlausurtermin} gehörigen {@link GostKlausurraum}en ein {@link StundenplanRaum} zugewiesen ist.
	 */
	public boolean alleRaeumeHabenStundenplanRaumByTermin(final @NotNull GostKlausurtermin termin, final boolean fremdTermine) {
		for (final @NotNull GostKlausurraum raum : raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine))
			if (raum.idStundenplanRaum == null)
				return false;
		return true;
	}

	/**
	 * Prüft, ob die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 */
	public boolean hatRaumzuteilungByKursklausur(final @NotNull GostKursklausur klausur) {
		for (final @NotNull GostSchuelerklausurTermin skt : schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(terminOrExceptionByKursklausur(klausur), klausur)) {
			final List<GostKlausurraumstunde> stunden = _raumstundenmenge_by_idSchuelerklausurtermin.get(skt.id);
			if (stunden != null && !stunden.isEmpty())
				return true;
		}
		return false;
	}

}

import { JavaObject } from '../../../../java/lang/JavaObject';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostFaecherManager, cast_de_svws_nrw_core_utils_gost_GostFaecherManager } from '../../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { DateUtils } from '../../../../core/utils/DateUtils';
import { GostKlausurenUpdate } from '../../../../core/data/gost/klausurplanung/GostKlausurenUpdate';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../../java/util/Comparator';
import { ListMap2DLongKeys } from '../../../../core/adt/map/ListMap2DLongKeys';
import { GostKlausurenCollectionRaumData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionRaumData';
import type { List } from '../../../../java/util/List';
import { cast_java_util_List } from '../../../../java/util/List';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import { ListMap4DLongKeys } from '../../../../core/adt/map/ListMap4DLongKeys';
import { HashMap5D } from '../../../../core/adt/map/HashMap5D';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { HashSet } from '../../../../java/util/HashSet';
import { Fach } from '../../../../asd/types/fach/Fach';
import { MapUtils } from '../../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../../core/utils/Map2DUtils';
import { StundenplanRaum } from '../../../../core/data/stundenplan/StundenplanRaum';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ListMap5DLongKeys } from '../../../../core/adt/map/ListMap5DLongKeys';
import { PairNN } from '../../../../asd/adt/PairNN';
import { JavaLong } from '../../../../java/lang/JavaLong';
import type { Collection } from '../../../../java/util/Collection';
import { cast_java_util_Collection } from '../../../../java/util/Collection';
import { Class } from '../../../../java/lang/Class';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { GostKlausurenCollectionSkrsKrsData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionSkrsKrsData';
import { HashMap2D } from '../../../../core/adt/map/HashMap2D';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { ListMap3DLongKeys } from '../../../../core/adt/map/ListMap3DLongKeys';
import { KursDaten } from '../../../../asd/data/kurse/KursDaten';
import { Map3DUtils } from '../../../../core/utils/Map3DUtils';
import { KursManager } from '../../../../core/utils/KursManager';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { GostKlausurenCollectionData, cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionData';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { JavaIterator } from '../../../../java/util/JavaIterator';
import { StundenplanKalenderwochenzuordnung } from '../../../../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { HashMap3D } from '../../../../core/adt/map/HashMap3D';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { StundenplanManager } from '../../../../core/utils/stundenplan/StundenplanManager';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostKlausurraumstunde } from '../../../../core/data/gost/klausurplanung/GostKlausurraumstunde';
import { StundenplanZeitraster } from '../../../../core/data/stundenplan/StundenplanZeitraster';
import { GostKlausurenCollectionHjData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionHjData';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import { GostSchuelerklausurterminraumstunde } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurterminraumstunde';
import { GostKlausurenCollectionAllData, cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionAllData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionAllData';
import { GostKlausurraum } from '../../../../core/data/gost/klausurplanung/GostKlausurraum';
import { ListUtils } from '../../../../core/utils/ListUtils';

export class GostKlausurplanManager extends JavaObject {

	private readonly _faechermanager_by_abijahr : JavaMap<number, GostFaecherManager> = new HashMap<number, GostFaecherManager>();

	private readonly _kursManager : KursManager = new KursManager();

	private readonly _stundenplanmanager_by_schuljahresabschnitt_and_datum : HashMap2D<number, string, StundenplanManager> = new HashMap2D<number, string, StundenplanManager>();

	private readonly _stundenplanmanager_by_schuljahresabschnitt_and_kw : HashMap2D<number, number, StundenplanManager> = new HashMap2D<number, number, StundenplanManager>();

	private readonly _stundenplanmanagermenge_by_schuljahresabschnitt : JavaMap<number, List<StundenplanManager>> = new HashMap<number, List<StundenplanManager>>();

	private readonly _lehrerMap : JavaMap<number, LehrerListeEintrag> = new HashMap<number, LehrerListeEintrag>();

	private readonly _schuelerlisteeintrag_by_id : JavaMap<number, SchuelerListeEintrag> = new HashMap<number, SchuelerListeEintrag>();

	private readonly _schuljahresabschnitt_by_abijahr_and_halbjahr : HashMap2D<number, number, number> = new HashMap2D<number, number, number>();

	private _vorgabenInitialized : boolean = false;

	private _klausurenInitialized : boolean = false;

	private readonly _terminidmenge_manager_enthaelt_raumdata : JavaSet<number> = new HashSet<number>();

	private readonly _klausurdatenEnthalten : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private readonly _fehlenddatenEnthalten : HashMap2D<number, number, boolean> = new HashMap2D<number, number, boolean>();

	private readonly _compVorgabe : Comparator<GostKlausurvorgabe> = { compare : (a: GostKlausurvorgabe, b: GostKlausurvorgabe) => {
		if (JavaString.compareTo(a.kursart, b.kursart) < 0)
			return +1;
		if (JavaString.compareTo(a.kursart, b.kursart) > 0)
			return -1;
		if (a.abiJahrgang !== b.abiJahrgang)
			return JavaInteger.compare(a.abiJahrgang, b.abiJahrgang);
		const faechermanager : GostFaecherManager | null = this.getFaecherManagerOrNull(a.abiJahrgang);
		if (faechermanager !== null) {
			const aFach : GostFach | null = faechermanager.get(a.idFach);
			const bFach : GostFach | null = faechermanager.get(b.idFach);
			if ((aFach !== null) && (bFach !== null)) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (a.halbjahr !== b.halbjahr)
			return JavaInteger.compare(a.halbjahr, b.halbjahr);
		return JavaInteger.compare(a.quartal, b.quartal);
	} };

	private static readonly _compTermin : Comparator<GostKlausurtermin> = { compare : (a: GostKlausurtermin, b: GostKlausurtermin) => {
		if ((a.datum !== null) && (b.datum !== null))
			return JavaString.compareTo(a.datum, b.datum);
		if (b.datum !== null)
			return +1;
		if (a.datum !== null)
			return -1;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compKursklausur : Comparator<GostKursklausur> = { compare : (a: GostKursklausur, b: GostKursklausur) => {
		const va : GostKlausurvorgabe = this.vorgabeByKursklausur(a);
		const vb : GostKlausurvorgabe = this.vorgabeByKursklausur(b);
		if (JavaString.compareTo(va.kursart, vb.kursart) < 0)
			return +1;
		if (JavaString.compareTo(va.kursart, vb.kursart) > 0)
			return -1;
		if (va.abiJahrgang !== vb.abiJahrgang)
			return JavaInteger.compare(va.abiJahrgang, vb.abiJahrgang);
		const faechermanager : GostFaecherManager | null = this.getFaecherManagerOrNull(va.abiJahrgang);
		if (faechermanager !== null) {
			const aFach : GostFach | null = faechermanager.get(va.idFach);
			const bFach : GostFach | null = faechermanager.get(vb.idFach);
			if ((aFach !== null) && (bFach !== null)) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		if (va.halbjahr !== vb.halbjahr)
			return va.halbjahr - vb.halbjahr;
		if (va.quartal !== vb.quartal)
			return va.quartal - vb.quartal;
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compSchuelerklausur : Comparator<GostSchuelerklausur> = { compare : (a: GostSchuelerklausur, b: GostSchuelerklausur) => {
		const aV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(a);
		const bV : GostKlausurvorgabe | null = this.vorgabeBySchuelerklausur(b);
		if (aV.quartal !== bV.quartal)
			return aV.quartal - bV.quartal;
		if (JavaString.compareTo(aV.kursart, bV.kursart) < 0)
			return +1;
		if (JavaString.compareTo(aV.kursart, bV.kursart) > 0)
			return -1;
		if (aV.abiJahrgang !== bV.abiJahrgang)
			return JavaInteger.compare(aV.abiJahrgang, bV.abiJahrgang);
		const faechermanager : GostFaecherManager | null = this.getFaecherManagerOrNull(aV.abiJahrgang);
		if (faechermanager !== null) {
			const aFach : GostFach | null = faechermanager.get(aV.idFach);
			const bFach : GostFach | null = faechermanager.get(bV.idFach);
			if ((aFach !== null) && (bFach !== null)) {
				if (aFach.sortierung > bFach.sortierung)
					return +1;
				if (aFach.sortierung < bFach.sortierung)
					return -1;
			}
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly _compSchuelerklausurTermin : Comparator<GostSchuelerklausurTermin> = { compare : (a: GostSchuelerklausurTermin, b: GostSchuelerklausurTermin) => {
		if ((a as unknown === b as unknown) || (a.id === b.id))
			return 0;
		if (a.idSchuelerklausur !== b.idSchuelerklausur) {
			const kA : GostSchuelerklausur = this.schuelerklausurBySchuelerklausurtermin(a);
			const kB : GostSchuelerklausur = this.schuelerklausurBySchuelerklausurtermin(b);
			if (kA.idSchueler !== kB.idSchueler) {
				const sA : SchuelerListeEintrag | null = this._schuelerlisteeintrag_by_id.get(kA.idSchueler);
				const sB : SchuelerListeEintrag | null = this._schuelerlisteeintrag_by_id.get(kB.idSchueler);
				if ((sA !== null) && (sB !== null)) {
					const nameComparison : number = JavaString.compareTo((sA.nachname + "," + sA.vorname), sB.nachname + "," + sB.vorname);
					if (nameComparison !== 0)
						return nameComparison;
				} else
					if ((sA !== null && sB === null) || (sA === null && sB !== null))
						throw new DeveloperNotificationException("Schüler nicht gefunden: " + kA.idSchueler + " oder " + kB.idSchueler)
			}
		}
		if (a.idSchuelerklausur === b.idSchuelerklausur) {
			const folgeNrComparison : number = JavaInteger.compare(a.folgeNr, b.folgeNr);
			if (folgeNrComparison !== 0)
				return folgeNrComparison;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private static readonly _compStundenplanManager : Comparator<StundenplanManager> = { compare : (a: StundenplanManager, b: StundenplanManager) => JavaString.compareTo(a.getGueltigAb(), b.getGueltigAb()) };

	private static readonly _compRaum : Comparator<GostKlausurraum> = { compare : (a: GostKlausurraum, b: GostKlausurraum) => JavaLong.compare(a.id, b.id) };

	private readonly _vorgabenfehlendmenge : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();

	private readonly _vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach : HashMap5D<number, number, number, string, number, GostKlausurvorgabe> = new HashMap5D<number, number, number, string, number, GostKlausurvorgabe>();

	private readonly _kursklausurfehlendmenge : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	private readonly _kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs : HashMap5D<number, number, number, number, number, GostKursklausur> = new HashMap5D<number, number, number, number, number, GostKursklausur>();

	private readonly _schuelerklausurfehlendmenge : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	private readonly _schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur : HashMap5D<number, number, number, number, number, GostSchuelerklausur> = new HashMap5D<number, number, number, number, number, GostSchuelerklausur>();

	private readonly _vorgabe_by_id : JavaMap<number, GostKlausurvorgabe> = new HashMap<number, GostKlausurvorgabe>();

	private readonly _vorgabenmenge : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();

	private _vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach : ListMap5DLongKeys<GostKlausurvorgabe> = new ListMap5DLongKeys<GostKlausurvorgabe>();

	private readonly _kursklausur_by_id : JavaMap<number, GostKursklausur> = new HashMap<number, GostKursklausur>();

	private readonly _kursklausurmenge : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	private _kursklausur_by_idVorgabe_and_idKurs : ListMap2DLongKeys<GostKursklausur> = new ListMap2DLongKeys<GostKursklausur>();

	private _kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal : ListMap4DLongKeys<GostKursklausur> = new ListMap4DLongKeys<GostKursklausur>();

	private readonly _termin_by_id : JavaMap<number, GostKlausurtermin> = new HashMap<number, GostKlausurtermin>();

	private readonly _terminmenge : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();

	private _terminmenge_by_abijahr_and_halbjahr_and_quartal : ListMap3DLongKeys<GostKlausurtermin> = new ListMap3DLongKeys<GostKlausurtermin>();

	private _terminmenge_by_datum_and_abijahr : ListMap2DLongKeys<GostKlausurtermin> = new ListMap2DLongKeys<GostKlausurtermin>();

	private readonly _schuelerklausur_by_id : JavaMap<number, GostSchuelerklausur> = new HashMap<number, GostSchuelerklausur>();

	private readonly _schuelerklausurmenge : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	private _schuelerklausur_by_idKursklausur_and_idSchueler : ListMap2DLongKeys<GostSchuelerklausur> = new ListMap2DLongKeys<GostSchuelerklausur>();

	private readonly _schuelerklausurmenge_by_abijahr_and_idSchueler : HashMap2D<number, number, List<GostSchuelerklausur>> = new HashMap2D<number, number, List<GostSchuelerklausur>>();

	private readonly _schuelerklausurtermin_by_id : JavaMap<number, GostSchuelerklausurTermin> = new HashMap<number, GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminmenge : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminaktuellmenge : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminaktuell_by_idSchuelerklausur : JavaMap<number, GostSchuelerklausurTermin> = new HashMap<number, GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminmenge_by_idSchuelerklausur : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();

	private readonly _schuelerklausurterminmenge_by_idTermin : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();

	private readonly _schuelerklausurterminmenge_by_idKursklausur : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();

	private _schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur : ListMap2DLongKeys<GostSchuelerklausurTermin> = new ListMap2DLongKeys<GostSchuelerklausurTermin>();

	private _schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin : ListMap4DLongKeys<GostSchuelerklausurTermin> = new ListMap4DLongKeys<GostSchuelerklausurTermin>();

	private _schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin : ListMap2DLongKeys<GostSchuelerklausurTermin> = new ListMap2DLongKeys<GostSchuelerklausurTermin>();

	private _schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur : ListMap2DLongKeys<GostSchuelerklausurTermin> = new ListMap2DLongKeys<GostSchuelerklausurTermin>();

	private readonly _schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId : HashMap3D<number, number, number, List<GostSchuelerklausurTermin>> = new HashMap3D<number, number, number, List<GostSchuelerklausurTermin>>();

	private readonly _raum_by_id : JavaMap<number, GostKlausurraum> = new HashMap<number, GostKlausurraum>();

	private readonly _raummenge : List<GostKlausurraum> = new ArrayList<GostKlausurraum>();

	private readonly _raummenge_by_idTermin : JavaMap<number, List<GostKlausurraum>> = new HashMap<number, List<GostKlausurraum>>();

	private _raum_by_idTermin_and_idStundenplanraum : ListMap2DLongKeys<GostKlausurraum> = new ListMap2DLongKeys<GostKlausurraum>();

	private readonly _klausurraum_by_idSchuelerklausurtermin : JavaMap<number, GostKlausurraum> = new HashMap<number, GostKlausurraum>();

	private _raummenge_by_idTermin_and_idKursklausur : ListMap2DLongKeys<GostKlausurraum> = new ListMap2DLongKeys<GostKlausurraum>();

	private readonly _raumstunde_by_id : JavaMap<number, GostKlausurraumstunde> = new HashMap<number, GostKlausurraumstunde>();

	private readonly _raumstundenmenge : List<GostKlausurraumstunde> = new ArrayList<GostKlausurraumstunde>();

	private readonly _raumstundenmenge_by_idRaum : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap<number, List<GostKlausurraumstunde>>();

	private _raumstunde_by_idRaum_and_idZeitraster : ListMap2DLongKeys<GostKlausurraumstunde> = new ListMap2DLongKeys<GostKlausurraumstunde>();

	private readonly _raumstundenmenge_by_idSchuelerklausurtermin : JavaMap<number, List<GostKlausurraumstunde>> = new HashMap<number, List<GostKlausurraumstunde>>();

	private readonly _schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde : ListMap2DLongKeys<GostSchuelerklausurterminraumstunde> = new ListMap2DLongKeys<GostSchuelerklausurterminraumstunde>();

	private readonly _schuelerklausurterminraumstundenmenge : List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();

	private readonly _schuelermenge_by_abijahr : JavaMap<number, List<SchuelerListeEintrag>> = new HashMap<number, List<SchuelerListeEintrag>>();


	/**
	 * Erstellt einen leeren Manager.
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n
	 *
	 * @param listVorgaben die Liste der {@link GostKlausurvorgabe}n
	 */
	public constructor(listVorgaben : Collection<GostKlausurvorgabe>);

	/**
	 * Erstellt einen neuen Manager mit den als Liste angegebenen {@link GostKlausurvorgabe}n und dem übergebenen {@link GostFaecherManager}, der für den Vorlagen-Jahrgang (ID = -1) gilt
	 *
	 * @param faecherManagerVorgaben der GostFaecherManager, der für den Vorlagen-Jahrgang gilt
	 * @param listVorgaben 	die Liste der GostKlausurvorgaben
	 */
	public constructor(faecherManagerVorgaben : GostFaecherManager | null, listVorgaben : List<GostKlausurvorgabe>);

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
	public constructor(listVorgaben : Collection<GostKlausurvorgabe>, listKlausuren : Collection<GostKursklausur>, listTermine : Collection<GostKlausurtermin>, listSchuelerklausuren : Collection<GostSchuelerklausur>, listSchuelerklausurtermine : Collection<GostSchuelerklausurTermin>);

	/**
	 * Erstellt einen neuen Manager mit den übergebenen {@link GostKlausurenCollectionAllData} enthaltenen Daten
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public constructor(allData : GostKlausurenCollectionAllData);

	/**
	 * Erstellt einen neuen Manager mit den übergebenen {@link GostKlausurenCollectionData} enthaltenen Daten
	 *
	 * @param data            das {@link GostKlausurenCollectionData}-Objekt, das alle Informationen enthält
	 */
	public constructor(data : GostKlausurenCollectionData);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Collection<GostKlausurvorgabe> | GostFaecherManager | GostKlausurenCollectionAllData | GostKlausurenCollectionData | null, __param1? : Collection<GostKursklausur> | List<GostKlausurvorgabe>, __param2? : Collection<GostKlausurtermin>, __param3? : Collection<GostSchuelerklausur>, __param4? : Collection<GostSchuelerklausurTermin>) {
		super();
		if ((__param0 === undefined) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.Collection'))) || (__param0 === null)) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const listVorgaben : Collection<GostKlausurvorgabe> = cast_java_util_Collection(__param0);
			this.vorgabeAddAll(listVorgaben);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostFaecherManager'))) || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const faecherManagerVorgaben : GostFaecherManager | null = cast_de_svws_nrw_core_utils_gost_GostFaecherManager(__param0);
			const listVorgaben : List<GostKlausurvorgabe> = cast_java_util_List(__param1);
			this._faechermanager_by_abijahr.put(-1, faecherManagerVorgaben);
			this.vorgabeAddAll(listVorgaben);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.Collection'))) || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.Collection'))) || (__param1 === null)) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.Collection'))) || (__param2 === null)) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('java.util.Collection'))) || (__param3 === null)) && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('java.util.Collection'))) || (__param4 === null))) {
			const listVorgaben : Collection<GostKlausurvorgabe> = cast_java_util_Collection(__param0);
			const listKlausuren : Collection<GostKursklausur> = cast_java_util_Collection(__param1);
			const listTermine : Collection<GostKlausurtermin> = cast_java_util_Collection(__param2);
			const listSchuelerklausuren : Collection<GostSchuelerklausur> = cast_java_util_Collection(__param3);
			const listSchuelerklausurtermine : Collection<GostSchuelerklausurTermin> = cast_java_util_Collection(__param4);
			this.addKlausurDataListenOhneUpdate(listVorgaben, listKlausuren, listTermine, listSchuelerklausuren, listSchuelerklausurtermine);
			this.update_all();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData')))) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const allData : GostKlausurenCollectionAllData = cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionAllData(__param0);
			this.addAllData(allData);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionData')))) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined)) {
			const data : GostKlausurenCollectionData = cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionData(__param0);
			this.addKlausurData(data);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionAllData}-Objekt enthaltenen Daten hinzu
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public addAllData(allData : GostKlausurenCollectionAllData) : void {
		this.addMetadata(allData);
		this.addKlausurAllDataOhneUpdate(allData);
		this.addRaumAllDataOhneUpdate(allData);
		this.update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionAllData}-Objekt enthaltenen Klausurdaten hinzu ({@link GostKlausurvorgabe}n, {@link GostKursklausur}en, {@link GostKlausurtermin}e, {@link GostSchuelerklausur}en, {@link GostSchuelerklausurTermin}e)
	 *
	 * @param allData            das {@link GostKlausurenCollectionAllData}-Objekt, das alle Informationen enthält
	 */
	public addKlausurData(allData : GostKlausurenCollectionData) : void {
		this.addKlausurDataListenOhneUpdate(allData.vorgaben, allData.kursklausuren, allData.termine, allData.schuelerklausuren, allData.schuelerklausurtermine);
		this.update_all();
	}

	/**
	 * Fügt dem Manager alle im übergebenen {@link GostKlausurenCollectionRaumData}-Objekt enthaltenen Raumplanungsdaten hinzu
	 *
	 * @param raumData            das {@link GostKlausurenCollectionRaumData}-Objekt, das Raumplanungsdaten enthält
	 */
	public addRaumData(raumData : GostKlausurenCollectionRaumData) : void {
		this.addRaumDataOhneUpdate(raumData);
		this.update_all();
	}

	private addRaumDataOhneUpdate(data : GostKlausurenCollectionRaumData) : void {
		this.addRaumDataListenOhneUpdate(data.raeume, data.raumstunden, data.sktRaumstunden, data.idsKlausurtermine);
	}

	private addRaumDataListenOhneUpdate(raeume : Collection<GostKlausurraum>, raumstunden : Collection<GostKlausurraumstunde>, sktRaumstunden : Collection<GostSchuelerklausurterminraumstunde>, idsKlausurtermine : List<number>) : void {
		this.raumAddAllOhneUpdate(raeume);
		this.raumstundeAddAllOhneUpdate(raumstunden);
		this.schuelerklausurraumstundeAddAllOhneUpdate(sktRaumstunden);
		this._terminidmenge_manager_enthaelt_raumdata.addAll(idsKlausurtermine);
	}

	private addRaumAllDataOhneUpdate(allData : GostKlausurenCollectionAllData) : void {
		const raeume : JavaSet<GostKlausurraum> = new HashSet<GostKlausurraum>();
		const raumstunden : JavaSet<GostKlausurraumstunde> = new HashSet<GostKlausurraumstunde>();
		const sktRaumstunden : JavaSet<GostSchuelerklausurterminraumstunde> = new HashSet<GostSchuelerklausurterminraumstunde>();
		const idsKlausurtermine : List<number> = new ArrayList<number>();
		for (const data of allData.datacontained) {
			raeume.addAll(data.raumdata.raeume);
			raumstunden.addAll(data.raumdata.raumstunden);
			sktRaumstunden.addAll(data.raumdata.sktRaumstunden);
			idsKlausurtermine.addAll(data.raumdata.idsKlausurtermine);
		}
		this.addRaumDataListenOhneUpdate(GostKlausurplanManager.removeDuplicatesFromSet(raeume), GostKlausurplanManager.removeDuplicatesFromSet(raumstunden), sktRaumstunden, idsKlausurtermine);
	}

	private static removeDuplicatesFromSet<T>(objects : JavaSet<T>) : JavaSet<T> {
		const unique : JavaSet<T> = new HashSet<T>();
		for (const o of objects) {
			let seen : boolean = false;
			for (const o2 of unique) {
				if (JavaObject.equalsTranspiler(o, (o2))) {
					seen = true;
					break;
				}
			}
			if (!seen)
				unique.add(o);
		}
		return unique;
	}

	private addMetadata(meta : GostKlausurenCollectionAllData) : void {
		const kurse : List<KursDaten> = new ArrayList<KursDaten>();
		const schueler : List<SchuelerListeEintrag> = new ArrayList<SchuelerListeEintrag>();
		for (const data of meta.datacontained) {
			this._schuljahresabschnitt_by_abijahr_and_halbjahr.put(data.abiturjahrgang, data.gostHalbjahr, data.schuljahresabschnitt);
			if (data.faecher !== null)
				this._faechermanager_by_abijahr.put(data.abiturjahrgang, new GostFaecherManager(data.abiturjahrgang, data.faecher));
			if (data.schueler !== null)
				schueler.addAll(data.schueler);
			if (data.kurse !== null)
				kurse.addAll(data.kurse);
		}
		this._kursManager.addAll(kurse);
		for (const lehrer of meta.lehrer)
			this._lehrerMap.put(lehrer.id, lehrer);
		this.schuelerAddAllOhneUpdate(schueler, true);
	}

	private addKlausurDataListenOhneUpdate(listVorgaben : Collection<GostKlausurvorgabe>, listKlausuren : Collection<GostKursklausur>, listTermine : Collection<GostKlausurtermin> | null, listSchuelerklausuren : Collection<GostSchuelerklausur> | null, listSchuelerklausurtermine : Collection<GostSchuelerklausurTermin> | null) : void {
		this.vorgabeAddAllOhneUpdate(listVorgaben);
		this.kursklausurAddAllOhneUpdate(listKlausuren);
		if (listTermine !== null)
			this.terminAddAllOhneUpdate(listTermine);
		if (listSchuelerklausuren !== null)
			this.schuelerklausurAddAllOhneUpdate(listSchuelerklausuren);
		if (listSchuelerklausurtermine !== null)
			this.schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
	}

	private addKlausurAllDataOhneUpdate(allData : GostKlausurenCollectionAllData) : void {
		const listVorgaben : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();
		const listKlausuren : List<GostKursklausur> = new ArrayList<GostKursklausur>();
		const listTermine : JavaSet<GostKlausurtermin> = new HashSet<GostKlausurtermin>();
		const listSchuelerklausuren : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();
		const listSchuelerklausurtermine : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const data of allData.datacontained) {
			this._klausurdatenEnthalten.put(data.abiturjahrgang, data.gostHalbjahr, true);
			listVorgaben.addAll(data.data.vorgaben);
			listKlausuren.addAll(data.data.kursklausuren);
			listTermine.addAll(data.data.termine);
			listSchuelerklausuren.addAll(data.data.schuelerklausuren);
			listSchuelerklausurtermine.addAll(data.data.schuelerklausurtermine);
		}
		this.addKlausurDataListenOhneUpdate(listVorgaben, listKlausuren, GostKlausurplanManager.removeDuplicatesFromSet(listTermine), listSchuelerklausuren, listSchuelerklausurtermine);
	}

	private addKlausurDataFehlendOhneUpdate(fehlendData : GostKlausurenCollectionHjData) : void {
		this.vorgabefehlendAddAllOhneUpdate(fehlendData.data.vorgaben);
		this.kursklausurfehlendAddAllOhneUpdate(fehlendData.data.kursklausuren);
		this.schuelerklausurfehlendAddAllOhneUpdate(fehlendData.data.schuelerklausuren);
	}

	/**
	 * Setzt die Problemdaten der Klausurplanung für einen bestimmten Abiturjahrgang und ein bestimmtes Halbjahr
	 *
	 * @param fehlendData die {@link GostKlausurenCollectionHjData} mit den fehlenden Klausurdaten
	 */
	public setKlausurDataFehlend(fehlendData : GostKlausurenCollectionHjData) : void {
		this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.removeMap2(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr);
		this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.removeMap2(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr);
		this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.removeMap2(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr);
		this.addKlausurDataFehlendOhneUpdate(fehlendData);
		this._fehlenddatenEnthalten.put(fehlendData.abiturjahrgang, fehlendData.gostHalbjahr, true);
		this.update_all();
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurvorgaben enthält.
	 *
	 * @return <code>true</code>, falls der Manager Klausurvorgaben enthält.
	 */
	public isVorgabenInitialized() : boolean {
		return this._vorgabenInitialized;
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurdaten enthält.
	 *
	 * @return <code>true</code>, falls der Manager Klausurdaten enthält.
	 */
	public isKlausurenInitialized() : boolean {
		return this._klausurenInitialized;
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Raumplanungsdaten zum übergebenen Termin enthält.
	 *
	 * @param termin der {@link GostKlausurtermin}, für den geprüft werden soll.
	 *
	 * @return <code>true</code>, falls der Manager Raumplanungsdaten zum übergebenen Termin enthält.
	 */
	public hasRaumdataZuTermin(termin : GostKlausurtermin) : boolean {
		return this._terminidmenge_manager_enthaelt_raumdata.contains(termin.id);
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Klausurdaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 *
	 * @param abiJahrgang der Abiturjahrgang
	 * @param halbjahr das Halbjahr
	 *
	 * @return <code>true</code>, falls der Manager Klausurdaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 */
	public hasKlausurdatenZuAbijahrUndHalbjahr(abiJahrgang : number, halbjahr : GostHalbjahr) : boolean {
		return this._klausurdatenEnthalten.contains(abiJahrgang, halbjahr.id);
	}

	/**
	 * Liefert <code>true</code>, falls der Manager Fehlenddaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 *
	 * @param abiJahrgang der Abiturjahrgang
	 * @param halbjahr das Halbjahr
	 *
	 * @return <code>true</code>, falls der Manager Fehlenddaten zum übergebenen Abiturjahrgang und Halbjahr enthält.
	 */
	public hasFehlenddatenZuAbijahrUndHalbjahr(abiJahrgang : number, halbjahr : GostHalbjahr) : boolean {
		return this._fehlenddatenEnthalten.contains(abiJahrgang, halbjahr.id);
	}

	/**
	 * Setzt den {@link GostFaecherManager}
	 *
	 * @param abijahr der Abiturjahrgang, zu dem der {@link GostFaecherManager} gehört
	 * @param faecherManager der {@link GostFaecherManager}
	 */
	public setFaecherManager(abijahr : number, faecherManager : GostFaecherManager) : void {
		this._faechermanager_by_abijahr.put(abijahr, faecherManager);
	}

	/**
	 * Liefert den {@link GostFaecherManager} zum übergebenen Abiturjahr, falls dieser gesetzt ist, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param abijahr der Abiturjahrgang, zu dem der {@link GostFaecherManager} geliefert werden soll
	 *
	 * @return den {@link GostFaecherManager}
	 */
	public getFaecherManager(abijahr : number) : GostFaecherManager {
		return DeveloperNotificationException.ifMapGetIsNull(this._faechermanager_by_abijahr, abijahr);
	}

	/**
	 * Liefert den {@link GostFaecherManager} zum übergebenen Abiturjahr, falls dieser gesetzt ist, sonst <code>null</code>.
	 *
	 * @param abijahr der Abiturjahrgang, zu dem der {@link GostFaecherManager} geliefert wird
	 *
	 * @return den {@link GostFaecherManager} oder <code>null</code>
	 */
	public getFaecherManagerOrNull(abijahr : number) : GostFaecherManager | null {
		return this._faechermanager_by_abijahr.get(abijahr);
	}

	/**
	 * Liefert den {@link KursManager}.
	 *
	 * @return den {@link KursManager}
	 */
	public getKursManager() : KursManager {
		return this._kursManager;
	}

	/**
	 * Liefert die Map mit den {@link SchuelerListeEintrag}enn
	 *
	 * @return die Map mit den {@link SchuelerListeEintrag}en
	 */
	public getSchuelerMap() : JavaMap<number, SchuelerListeEintrag> {
		return this._schuelerlisteeintrag_by_id;
	}

	/**
	 * Prüft, ob zu dem angegebenen Schuljahresabschnitt bereits die StundenplanManager aus der Datenbank geladen wurden.
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn die StundenplanManager bereits geladen wurde, sonst false
	 */
	public stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt : number) : boolean {
		return this._stundenplanmanagermenge_by_schuljahresabschnitt.containsKey(idSchuljahresabschnitt);
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public stundenplanManagerExistsByAbschnitt(idSchuljahresabschnitt : number) : boolean {
		if (!this.stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt))
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.")
		const liste : List<StundenplanManager> | null = this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		return liste !== null && !liste.isEmpty();
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert.
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public stundenplanManagerGeladenAndExistsByAbschnitt(idSchuljahresabschnitt : number) : boolean {
		if (!this.stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt))
			return false;
		const liste : List<StundenplanManager> | null = this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		return liste !== null && !liste.isEmpty();
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public stundenplanManagerExistsByAbschnittAndDatum(idSchuljahresabschnitt : number, datum : string) : boolean {
		if (!this.stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt))
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.")
		return this._stundenplanmanager_by_schuljahresabschnitt_and_datum.contains(idSchuljahresabschnitt, datum);
	}

	private static gibIntkeyJahrUndKwDesDatumsISO8601(datumISO8601 : string) : number {
		const split : Array<number> | null = DateUtils.extractFromDateISO8601(datumISO8601);
		return GostKlausurplanManager.gibIntkeyJahrUndKw(split[6], split[5]);
	}

	private static gibIntkeyJahrUndKw(jahr : number, kw : number) : number {
		return JavaInteger.parseInt(jahr + "" + kw);
	}

	/**
	 * Prüft, ob zu den angegebenen Parametern ein StundenplanManager existiert. Falls noch keine StundenplanManager für den angegebenen Schuljahresabschnitt geladen wurden, wird eine {@link DeveloperNotificationException} geworfen
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param jahr das Jahr
	 * @param kw die Kalenderwoche
	 * @return true, wenn ein StundenplanManager existiert, sonst false
	 */
	public stundenplanManagerExistsByAbschnittAndJahrAndKw(idSchuljahresabschnitt : number, jahr : number, kw : number) : boolean {
		if (!this.stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt))
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.")
		return this._stundenplanmanager_by_schuljahresabschnitt_and_kw.contains(idSchuljahresabschnitt, GostKlausurplanManager.gibIntkeyJahrUndKw(jahr, kw));
	}

	/**
	 * Setzt die {@link StundenplanManager} für den angegebenen Schuljahresabschnitt
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param stundenplanManagerList die Liste der {@link StundenplanManager}
	 */
	public stundenplanManagerAddAllBySchuljahresabschnittsid(idSchuljahresabschnitt : number, stundenplanManagerList : List<StundenplanManager>) : void {
		for (const stundenplanManager of stundenplanManagerList) {
			if (stundenplanManager.getIDSchuljahresabschnitt() !== idSchuljahresabschnitt)
				throw new DeveloperNotificationException("ID des Schuljahresabschnitts stimmt nicht überein.")
			this.stundenplanManagerAdd(stundenplanManager);
		}
		if (stundenplanManagerList.isEmpty())
			MapUtils.getOrCreateArrayList(this._stundenplanmanagermenge_by_schuljahresabschnitt, idSchuljahresabschnitt);
	}

	/**
	 * Setzt den {@link StundenplanManager}
	 *
	 * @param stundenplanManager der {@link StundenplanManager}
	 */
	public stundenplanManagerAdd(stundenplanManager : StundenplanManager) : void {
		const stundenplanManagerList : List<StundenplanManager> = MapUtils.getOrCreateArrayList(this._stundenplanmanagermenge_by_schuljahresabschnitt, stundenplanManager.getIDSchuljahresabschnitt());
		DeveloperNotificationException.ifListAddsDuplicate("_stundenplanmanagermenge_by_schuljahresabschnitt", stundenplanManagerList, stundenplanManager);
		stundenplanManagerList.sort(GostKlausurplanManager._compStundenplanManager);
		for (const datum of DateUtils.gibTageAlsDatumZwischen(stundenplanManager.getGueltigAb(), stundenplanManager.getGueltigBis()))
			if (datum !== null)
				this.stundenplanManagerAddByAbschnittAndDatum(stundenplanManager.getIDSchuljahresabschnitt(), datum, stundenplanManager);
	}

	/**
	 * Setzt den {@link StundenplanManager} für die übergebenen Parameter
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der Stundenplan gültig ist
	 * @param stundenplanManager der {@link StundenplanManager}
	 */
	public stundenplanManagerAddByAbschnittAndDatum(idSchuljahresabschnitt : number, datum : string, stundenplanManager : StundenplanManager) : void {
		DeveloperNotificationException.ifMap2DPutOverwrites(this._stundenplanmanager_by_schuljahresabschnitt_and_datum, idSchuljahresabschnitt, datum, stundenplanManager);
		const kw : number = GostKlausurplanManager.gibIntkeyJahrUndKwDesDatumsISO8601(datum);
		if (!this._stundenplanmanager_by_schuljahresabschnitt_and_kw.contains(idSchuljahresabschnitt, kw))
			this._stundenplanmanager_by_schuljahresabschnitt_and_kw.put(idSchuljahresabschnitt, kw, stundenplanManager);
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt : number, datum : string) : StundenplanManager | null {
		return this._stundenplanmanager_by_schuljahresabschnitt_and_datum.getOrNull(idSchuljahresabschnitt, datum);
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
	public stundenplanManagerGetByAbschnittAndKwOrNull(idSchuljahresabschnitt : number, jahr : number, kw : number) : StundenplanManager | null {
		return this._stundenplanmanager_by_schuljahresabschnitt_and_kw.getOrNull(idSchuljahresabschnitt, GostKlausurplanManager.gibIntkeyJahrUndKw(jahr, kw));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public stundenplanManagerGetByAbschnittAndDatumOrClosest(idSchuljahresabschnitt : number, datum : string) : StundenplanManager {
		const exactMatch : StundenplanManager | null = this.stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch !== null)
			return exactMatch;
		const stundenplanManagerList : List<StundenplanManager> | null = this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if (stundenplanManagerList === null || stundenplanManagerList.isEmpty())
			throw new DeveloperNotificationException(JavaString.format("Kein Stundenplanmanager zu Abschnitt %d gefunden.", idSchuljahresabschnitt))
		if (stundenplanManagerList.size() === 1 || JavaString.compareTo(stundenplanManagerList.getFirst().getGueltigAb(), datum) > 0)
			return stundenplanManagerList.getFirst();
		if (JavaString.compareTo(stundenplanManagerList.getLast().getGueltigBis(), datum) < 0)
			return stundenplanManagerList.getLast();
		for (const manager of stundenplanManagerList)
			if (JavaString.compareTo(manager.getGueltigAb(), datum) > 0)
				return manager;
		throw new DeveloperNotificationException("Kein StundenplanManager passend zu Suchkriterien.")
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst eine DeveloperNotificationException.
	 *
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts
	 * @param datum das Datum, zu dem der gesuchte Stundenplan gültig ist. Wird kein gültiger Plan gefunden, soll der Plan geliefert werden, der vor dem Datum gültig war.
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern.
	 */
	public stundenplanManagerGetByAbschnittAndDatumOrBeforeOrNull(idSchuljahresabschnitt : number, datum : string) : StundenplanManager | null {
		const exactMatch : StundenplanManager | null = this.stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch !== null)
			return exactMatch;
		const stundenplanManagerList : List<StundenplanManager> | null = this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if (stundenplanManagerList === null || stundenplanManagerList.isEmpty())
			return null;
		if (stundenplanManagerList.size() === 1 && JavaString.compareTo(stundenplanManagerList.getFirst().getGueltigBis(), datum) < 0)
			return stundenplanManagerList.getFirst();
		let lastManager : StundenplanManager | null = null;
		for (const manager of stundenplanManagerList) {
			if (JavaString.compareTo(manager.getGueltigAb(), datum) > 0)
				return lastManager;
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
	public stundenplanManagerKalenderwochenzuordnungenGetMengeByAbschnitt(idSchuljahresabschnitt : number) : List<StundenplanKalenderwochenzuordnung> {
		if (!this.stundenplanManagerGeladenByAbschnitt(idSchuljahresabschnitt))
			throw new DeveloperNotificationException("StundenplanManager für Schuljahresabschnitt " + idSchuljahresabschnitt + " wurde nicht geladen.")
		const kwzAll : List<StundenplanKalenderwochenzuordnung> = new ArrayList<StundenplanKalenderwochenzuordnung>();
		for (const manager of DeveloperNotificationException.ifNull(JavaString.format("_stundenplanmanagermenge_by_schuljahresabschnitt null für Abschnitt %d", idSchuljahresabschnitt), this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt))) {
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
	public stundenplanManagerGetByAbschnittAndDatumOrAfterOrNull(idSchuljahresabschnitt : number, datum : string) : StundenplanManager | null {
		const exactMatch : StundenplanManager | null = this.stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum);
		if (exactMatch !== null)
			return exactMatch;
		const stundenplanManagerList : List<StundenplanManager> | null = this._stundenplanmanagermenge_by_schuljahresabschnitt.get(idSchuljahresabschnitt);
		if (stundenplanManagerList === null || stundenplanManagerList.isEmpty())
			return null;
		if (stundenplanManagerList.size() === 1 && JavaString.compareTo(stundenplanManagerList.getFirst().getGueltigAb(), datum) > 0)
			return stundenplanManagerList.getFirst();
		for (const manager of stundenplanManagerList) {
			if (JavaString.compareTo(manager.getGueltigAb(), datum) > 0)
				return manager;
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
	public stundenplanManagerGetByAbschnittAndDatumOrException(idSchuljahresabschnitt : number, datum : string) : StundenplanManager {
		return DeveloperNotificationException.ifNull(JavaString.format("Kein Stundenplanmanager zu Abschnitt %d und Datum %s gefunden.", idSchuljahresabschnitt, datum), this.stundenplanManagerGetByAbschnittAndDatumOrNull(idSchuljahresabschnitt, datum));
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
	public stundenplanManagerGetByAbschnittAndKwOrException(idSchuljahresabschnitt : number, jahr : number, kw : number) : StundenplanManager {
		return DeveloperNotificationException.ifNull(JavaString.format("Kein Stundenplanmanager zu Abschnitt %d und Datum %s gefunden.", idSchuljahresabschnitt, kw), this.stundenplanManagerGetByAbschnittAndKwOrNull(idSchuljahresabschnitt, jahr, kw));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst null.
	 */
	public stundenplanManagerGetByTerminOrNull(termin : GostKlausurtermin) : StundenplanManager | null {
		return this.stundenplanManagerGetByAbschnittAndDatumOrNull(termin.idSchuljahresabschnitt, DeveloperNotificationException.ifNull(JavaString.format("Kein Datum zum Termin %d gefunden.", termin.id), termin.datum));
	}

	/**
	 * Liefert den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return den {@link StundenplanManager}, zu den übergebenen Parametern, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public stundenplanManagerGetByTerminOrException(termin : GostKlausurtermin) : StundenplanManager {
		return DeveloperNotificationException.ifNull(JavaString.format("Kein Stundenplan zu Termin %d (%s) gefunden.", termin.id, termin.datum), this.stundenplanManagerGetByTerminOrNull(termin));
	}

	private stundenplanManagerGetByDatumLinearSearch(datum : string) : StundenplanManager {
		for (const stundenplanManager of this._stundenplanmanager_by_schuljahresabschnitt_and_datum.getNonNullValuesAsList())
			if (stundenplanManager !== null && JavaString.compareTo(stundenplanManager.getGueltigAb(), datum) <= 0 && JavaString.compareTo(stundenplanManager.getGueltigBis(), datum) >= 0)
				return stundenplanManager;
		throw new DeveloperNotificationException(JavaString.format("Kein Stundenplan zu Datum %s gefunden.", datum))
	}

	/**
	 * Liefert die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}, falls diese gesetzt ist, sonst wird eine
	 * {@link DeveloperNotificationException} geworfen.
	 *
	 * @return die LehrerMap, eine Map von Lehrer-ID (Long) -> {@link LehrerListeEintrag}
	 */
	public getLehrerMap() : JavaMap<number, LehrerListeEintrag> {
		return this._lehrerMap;
	}

	/**
	 * Liefert den {@link SchuelerListeEintrag} zur übergebenen Schüler-ID, falls dieser existiert, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 *
	 * @param id die Schüler-ID
	 *
	 * @return den {@link SchuelerListeEintrag} zur übergebenen Schüler-ID, falls dieser existiert, sonst wird eine {@link DeveloperNotificationException} geworfen.
	 */
	public schuelerGetByIdOrException(id : number) : SchuelerListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerlisteeintrag_by_id, id);
	}

	/**
	 * Setzt die Maps, die zu den {@link SchuelerListeEintrag}en gehören.
	 *
	 * @param listSchueler Liste von {@link SchuelerListeEintrag}en
	 * @param ignoreExists wenn true, wird bei bereits existierenden Schülern kein Fehler geworfen und der alte Eintrag beibehalten
	 */
	private schuelerAddAllOhneUpdate(listSchueler : List<SchuelerListeEintrag>, ignoreExists : boolean) : void {
		for (const sle of listSchueler)
			if (ignoreExists) {
				if (!this._schuelerlisteeintrag_by_id.containsKey(sle.id))
					this._schuelerlisteeintrag_by_id.put(sle.id, sle);
			} else
				DeveloperNotificationException.ifMapPutOverwrites(this._schuelerlisteeintrag_by_id, sle.id, sle);
	}

	private addSchuljahr(jahrgaenge : List<GostKlausurenCollectionHjData>, abijahr : number, hjStart : number, abijahreAngefordert : JavaSet<number>) : void {
		if (!this._klausurdatenEnthalten.contains(abijahr, hjStart)) {
			const data : GostKlausurenCollectionHjData = new GostKlausurenCollectionHjData(abijahr, hjStart);
			if (!this._klausurdatenEnthalten.containsKey1(abijahr) && !abijahreAngefordert.contains(abijahr)) {
				data.schueler = new ArrayList();
				data.faecher = new ArrayList();
				abijahreAngefordert.add(abijahr);
			}
			jahrgaenge.add(data);
		}
	}

	private addSchuljahresPaare(jahrgaenge : List<GostKlausurenCollectionHjData>, abijahr : number, hjStart : number, abijahreAngefordert : JavaSet<number>) : void {
		this.addSchuljahr(jahrgaenge, abijahr, hjStart, abijahreAngefordert);
		this.addSchuljahr(jahrgaenge, abijahr, hjStart + 1, abijahreAngefordert);
	}

	/**
	 * Berechnet zu den Parametern die Liste von {@link GostKlausurenCollectionHjData}-Objekten, für die Klausurdaten geladen werden.
	 *
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 * @param halbjahr das aktuell betrachtete GostHalbjahr
	 * @return die Liste von {@link GostKlausurenCollectionHjData} -Objekten
	 */
	public getMissingHjKlausurdata(abiturjahr : number, halbjahr : number) : List<GostKlausurenCollectionHjData> {
		const jahrgaenge : List<GostKlausurenCollectionHjData> = new ArrayList<GostKlausurenCollectionHjData>();
		const abijahreAngefordert : JavaSet<number> = new HashSet<number>();
		const hjStart : number = (halbjahr % 2 === 0) ? halbjahr : halbjahr - 1;
		this.addSchuljahresPaare(jahrgaenge, abiturjahr, hjStart, abijahreAngefordert);
		switch (halbjahr) {
			case 0:
			case 1: {
				this.addSchuljahresPaare(jahrgaenge, abiturjahr - 1, 2, abijahreAngefordert);
				this.addSchuljahresPaare(jahrgaenge, abiturjahr - 2, 4, abijahreAngefordert);
				break;
			}
			case 2:
			case 3: {
				this.addSchuljahresPaare(jahrgaenge, abiturjahr - 1, 4, abijahreAngefordert);
				this.addSchuljahresPaare(jahrgaenge, abiturjahr + 1, 0, abijahreAngefordert);
				break;
			}
			case 4:
			case 5: {
				this.addSchuljahresPaare(jahrgaenge, abiturjahr + 1, 2, abijahreAngefordert);
				this.addSchuljahresPaare(jahrgaenge, abiturjahr + 2, 0, abijahreAngefordert);
				break;
			}
			default: {
				throw new DeveloperNotificationException(JavaString.format("Ungültiges GostHalbjahr %d.", halbjahr))
				break;
			}
		}
		return jahrgaenge;
	}

	private static datumStringToLong(date : string) : number {
		return JavaLong.parseLong(JavaString.replace(date, "-", ""));
	}

	private update_all() : void {
		this.update_schuelermenge_by_abijahr();
		this.update_vorgabemenge();
		this.update_vorgabefehlendmenge();
		this.update_kursklausurmenge();
		this.update_kursklausurfehlendmenge();
		this.update_terminmenge();
		this.update_schuelerklausurmenge();
		this.update_schuelerklausurfehlendmenge();
		this.update_schuelerklausurterminmenge();
		this.update_raummenge();
		this.update_raumstundenmenge();
		this.update_schuelerklausurraumstundenmenge();
		this.update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach();
		this.update_kursklausurmenge_by_idVorgabe_and_idKurs();
		this.update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin();
		this.update_terminmenge_by_halbjahr_and_quartal();
		this.update_terminmenge_by_datum();
		this.update_raummenge_by_idTermin();
		this.update_raum_by_idTermin_and_idStundenplanraum();
		this.update_raumstundenmenge_by_idRaum();
		this.update_raumstunde_by_idRaum_and_idZeitraster();
		this.update_raumstundenmenge_by_idSchuelerklausurtermin();
		this.update_klausurraum_by_idSchuelerklausurtermin();
		this.update_schuelerklausurterminaktuell_by_idSchuelerklausur();
		this.update_schuelerklausurterminaktuellmenge();
		this.update_schuelerklausurmenge_by_abijahr_and_idSchueler();
		this.update_schuelerklausurmenge_by_idKursklausur();
		this.update_schuelerklausurterminmenge_by_idSchuelerklausur();
		this.update_schuelerklausurterminmenge_by_idTermin();
		this.update_schuelerklausurterminmenge_by_idKursklausur();
		this.update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur();
		this.update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId();
		this.update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal();
		this.update_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin();
		this.update_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur();
		this.update_raummenge_by_idTermin_and_idKursklausur();
	}

	private update_schuelermenge_by_abijahr() : void {
		this._schuelermenge_by_abijahr.clear();
		for (const s of this._schuelerlisteeintrag_by_id.values())
			MapUtils.getOrCreateArrayList(this._schuelermenge_by_abijahr, s.abiturjahrgang).add(s);
	}

	private update_vorgabe_by_halbjahr_and_quartal_and_kursartAllg_and_idFach() : void {
		this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach = new ListMap5DLongKeys();
		for (const v of this._vorgabenmenge)
			this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.add(v.abiJahrgang, v.halbjahr, v.quartal, GostKursart.fromKuerzelOrException(v.kursart).id, v.idFach, v);
	}

	private update_kursklausurmenge_by_idVorgabe_and_idKurs() : void {
		this._kursklausur_by_idVorgabe_and_idKurs = new ListMap2DLongKeys();
		for (const kk of this._kursklausurmenge)
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this._kursklausur_by_idVorgabe_and_idKurs, kk.idVorgabe, kk.idKurs, kk);
	}

	private update_kursklausurmenge_by_halbjahr_and_quartal_and_idTermin() : void {
		this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal = new ListMap4DLongKeys();
		for (const kk of this._kursklausurmenge) {
			const v : GostKlausurvorgabe = this.vorgabeByKursklausur(kk);
			this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.add(v.abiJahrgang, v.halbjahr, (kk.idTermin !== null) ? kk.idTermin : -1, v.quartal, kk);
		}
	}

	private update_terminmenge_by_halbjahr_and_quartal() : void {
		this._terminmenge_by_abijahr_and_halbjahr_and_quartal = new ListMap3DLongKeys();
		for (const t of this._terminmenge)
			this._terminmenge_by_abijahr_and_halbjahr_and_quartal.add(t.abijahr, t.halbjahr, t.quartal, t);
	}

	private update_terminmenge_by_datum() : void {
		this._terminmenge_by_datum_and_abijahr = new ListMap2DLongKeys();
		for (const t of this._terminmenge)
			if (t.datum !== null)
				this._terminmenge_by_datum_and_abijahr.add(GostKlausurplanManager.datumStringToLong(t.datum), t.abijahr, t);
	}

	private update_schuelerklausurterminaktuellmenge() : void {
		this._schuelerklausurterminaktuellmenge.clear();
		for (const skt of this._schuelerklausurterminmenge)
			if (this.istSchuelerklausurterminAktuell(skt))
				this._schuelerklausurterminaktuellmenge.add(skt);
	}

	private update_schuelerklausurterminaktuell_by_idSchuelerklausur() : void {
		this._schuelerklausurterminaktuell_by_idSchuelerklausur.clear();
		for (const skt of this._schuelerklausurterminmenge) {
			const sktMaxFolgenummer : GostSchuelerklausurTermin | null = this._schuelerklausurterminaktuell_by_idSchuelerklausur.get(skt.idSchuelerklausur);
			if (sktMaxFolgenummer === null || sktMaxFolgenummer.folgeNr < skt.folgeNr)
				this._schuelerklausurterminaktuell_by_idSchuelerklausur.put(skt.idSchuelerklausur, skt);
		}
	}

	private update_schuelerklausurmenge_by_idKursklausur() : void {
		this._schuelerklausur_by_idKursklausur_and_idSchueler = new ListMap2DLongKeys();
		for (const sk of this._schuelerklausurmenge) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this._schuelerklausur_by_idKursklausur_and_idSchueler, sk.idKursklausur, sk.idSchueler, sk);
		}
	}

	private update_schuelerklausurmenge_by_abijahr_and_idSchueler() : void {
		this._schuelerklausurmenge_by_abijahr_and_idSchueler.clear();
		for (const sk of this._schuelerklausurmenge) {
			Map2DUtils.getOrCreateArrayList(this._schuelerklausurmenge_by_abijahr_and_idSchueler, this.vorgabeBySchuelerklausur(sk).abiJahrgang, sk.idSchueler).add(sk);
		}
	}

	private update_schuelerklausurterminaktuellmenge_by_kw_and_abijahr_and_schuelerId() : void {
		this._schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.clear();
		for (const idTermin of this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.keySet1()) {
			const termin : GostKlausurtermin = this.terminGetByIdOrException(idTermin);
			if (termin.datum === null)
				continue;
			const kw : number = DateUtils.gibKwDesDatumsISO8601(termin.datum);
			for (const skt of this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(idTermin)) {
				const sk : GostSchuelerklausur = this.schuelerklausurBySchuelerklausurtermin(skt);
				Map3DUtils.getOrCreateArrayList(this._schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId, this.vorgabeBySchuelerklausur(sk).abiJahrgang, kw, sk.idSchueler).add(skt);
			}
		}
	}

	private update_schuelerklausurterminmenge_by_idSchuelerklausur() : void {
		this._schuelerklausurterminmenge_by_idSchuelerklausur.clear();
		for (const skt of this._schuelerklausurterminmenge)
			MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur).add(skt);
		for (const sktList of this._schuelerklausurterminmenge_by_idSchuelerklausur.values())
			sktList.sort(this._compSchuelerklausurTermin);
	}

	private update_schuelerklausurterminmenge_by_idTermin() : void {
		this._schuelerklausurterminmenge_by_idTermin.clear();
		for (const skt of this._schuelerklausurterminmenge) {
			if (skt.folgeNr === 0) {
				const idTermin : number | null = this.kursklausurBySchuelerklausurTermin(skt).idTermin;
				if (idTermin !== null)
					MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idTermin, this.kursklausurBySchuelerklausurTermin(skt).idTermin).add(skt);
			} else
				if (skt.idTermin !== null)
					MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idTermin, skt.idTermin).add(skt);
		}
	}

	private update_schuelerklausurterminmenge_by_idKursklausur() : void {
		this._schuelerklausurterminmenge_by_idKursklausur.clear();
		for (const skt of this._schuelerklausurterminmenge) {
			if (skt.folgeNr === 0)
				MapUtils.getOrCreateArrayList(this._schuelerklausurterminmenge_by_idKursklausur, this.schuelerklausurBySchuelerklausurtermin(skt).idKursklausur).add(skt);
		}
	}

	private update_schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur() : void {
		this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur = new ListMap2DLongKeys();
		for (const e of this._schuelerklausurterminmenge_by_idTermin.entrySet())
			for (const skt of e.getValue())
				if (this.istSchuelerklausurterminAktuell(skt))
					this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.add(e.getKey(), this.schuelerklausurBySchuelerklausurtermin(skt).idKursklausur, skt);
	}

	private update_schuelerklausurterminntaktuellmenge_by_halbjahr_and_idTermin_and_quartal() : void {
		this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin = new ListMap4DLongKeys();
		for (const sk of this._schuelerklausurmenge) {
			const sktLast : GostSchuelerklausurTermin = this.schuelerklausurterminAktuellBySchuelerklausur(sk);
			if (sktLast.folgeNr > 0) {
				const v : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(sktLast);
				this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.add(v.abiJahrgang, v.halbjahr, v.quartal, (sktLast.idTermin !== null) ? sktLast.idTermin : -1, sktLast);
			}
		}
	}

	private update_raum_by_idTermin_and_idStundenplanraum() : void {
		this._raum_by_idTermin_and_idStundenplanraum = new ListMap2DLongKeys();
		for (const raum of this._raummenge)
			if (raum.idStundenplanRaum !== null)
				DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this._raum_by_idTermin_and_idStundenplanraum, raum.idTermin, raum.idStundenplanRaum, raum);
	}

	private update_raummenge_by_idTermin() : void {
		this._raummenge_by_idTermin.clear();
		for (const raum of this._raummenge)
			MapUtils.getOrCreateArrayList(this._raummenge_by_idTermin, raum.idTermin).add(raum);
	}

	private update_raummenge_by_idTermin_and_idKursklausur() : void {
		this._raummenge_by_idTermin_and_idKursklausur = new ListMap2DLongKeys();
		for (const skt of this._schuelerklausurterminaktuellmenge) {
			const termin : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(skt);
			if (termin !== null) {
				const raum : GostKlausurraum | null = this.raumGetBySchuelerklausurtermin(skt);
				if (raum !== null)
					this._raummenge_by_idTermin_and_idKursklausur.add(termin.id, this.kursklausurBySchuelerklausurTermin(skt).id, raum);
			}
		}
	}

	private update_raumstundenmenge_by_idRaum() : void {
		this._raumstundenmenge_by_idRaum.clear();
		for (const krs of this._raumstundenmenge)
			MapUtils.getOrCreateArrayList(this._raumstundenmenge_by_idRaum, krs.idRaum).add(krs);
	}

	private update_raumstunde_by_idRaum_and_idZeitraster() : void {
		this._raumstunde_by_idRaum_and_idZeitraster = new ListMap2DLongKeys();
		for (const rs of this._raumstundenmenge)
			if (rs.idZeitraster !== null)
				DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this._raumstunde_by_idRaum_and_idZeitraster, rs.idRaum, rs.idZeitraster, rs);
			else
				this._raumstunde_by_idRaum_and_idZeitraster.add(rs.idRaum, -1, rs);
	}

	private update_raumstundenmenge_by_idSchuelerklausurtermin() : void {
		this._raumstundenmenge_by_idSchuelerklausurtermin.clear();
		for (const skrs of this._schuelerklausurterminraumstundenmenge)
			MapUtils.getOrCreateArrayList(this._raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin).add(DeveloperNotificationException.ifMapGetIsNull(this._raumstunde_by_id, skrs.idRaumstunde));
	}

	private update_schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin() : void {
		this._schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin = new ListMap2DLongKeys();
		for (const k of this._schuelerklausurterminaktuellmenge) {
			const termin : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(k);
			if (termin !== null) {
				const raumstunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
				this._schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.add(((raumstunden === null) || raumstunden.isEmpty()) ? -1 : raumstunden.get(0).idRaum, termin.id, k);
			}
		}
	}

	private update_schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur() : void {
		this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur = new ListMap2DLongKeys();
		for (const k of this._schuelerklausurterminaktuellmenge) {
			const raumstunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idSchuelerklausurtermin.get(k.id);
			this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.add(((raumstunden === null) || raumstunden.isEmpty()) ? -1 : raumstunden.get(0).idRaum, this.kursklausurBySchuelerklausurTermin(k).id, k);
		}
	}

	private update_klausurraum_by_idSchuelerklausurtermin() : void {
		this._klausurraum_by_idSchuelerklausurtermin.clear();
		for (const skrs of this._schuelerklausurterminraumstundenmenge) {
			const krsList : List<GostKlausurraumstunde> = DeveloperNotificationException.ifMapGetIsNull(this._raumstundenmenge_by_idSchuelerklausurtermin, skrs.idSchuelerklausurtermin);
			for (const krs of krsList) {
				const kr : GostKlausurraum = DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, krs.idRaum);
				const krAlt : GostKlausurraum | null = this._klausurraum_by_idSchuelerklausurtermin.put(skrs.idSchuelerklausurtermin, kr);
				if ((krAlt !== null) && (krAlt as unknown !== kr as unknown))
					throw new DeveloperNotificationException("Schülerklausur " + skrs.idSchuelerklausurtermin + " ist zwei Klausurräumen zugeordnet.")
			}
		}
	}

	private update_vorgabemenge() : void {
		this._vorgabenmenge.clear();
		this._vorgabenmenge.addAll(this._vorgabe_by_id.values());
		this._vorgabenmenge.sort(this._compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public vorgabeAdd(vorgabe : GostKlausurvorgabe) : void {
		this.vorgabeAddAll(ListUtils.create1(vorgabe));
	}

	private vorgabeAddAllOhneUpdate(list : Collection<GostKlausurvorgabe>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const vorgabe of list) {
			GostKlausurplanManager.vorgabeCheck(vorgabe);
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.id + " existiert bereits!", this._vorgabe_by_id.containsKey(vorgabe.id));
			DeveloperNotificationException.ifTrue("vorgabeAddAllOhneUpdate: ID=" + vorgabe.id + " doppelt in der Liste!", !setOfIDs.add(vorgabe.id));
		}
		for (const vorgabe of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this._vorgabe_by_id, vorgabe.id, vorgabe);
			this.vorgabefehlendRemoveOhneUpdate(vorgabe);
		}
		this._vorgabenInitialized = true;
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public vorgabeAddAll(listVorgaben : Collection<GostKlausurvorgabe>) : void {
		this.vorgabeAddAllOhneUpdate(listVorgaben);
		this.update_all();
	}

	private static vorgabeCheck(vorgabe : GostKlausurvorgabe) : void {
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
	public vorgabeGetByIdOrException(idVorgabe : number) : GostKlausurvorgabe {
		return DeveloperNotificationException.ifMapGetIsNull(this._vorgabe_by_id, idVorgabe);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabeGetMengeAsList() : List<GostKlausurvorgabe> {
		return this._vorgabenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurvorgabe}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param vorgabe Das neue {@link GostKlausurvorgabe}-Objekt.
	 */
	public vorgabePatchAttributes(vorgabe : GostKlausurvorgabe) : void {
		GostKlausurplanManager.vorgabeCheck(vorgabe);
		DeveloperNotificationException.ifMapRemoveFailes(this._vorgabe_by_id, vorgabe.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._vorgabe_by_id, vorgabe.id, vorgabe);
		this.update_all();
	}

	private vorgabeRemoveOhneUpdateById(idVorgabe : number) : void {
		const vorgabe : GostKlausurvorgabe = DeveloperNotificationException.ifMapRemoveFailes(this._vorgabe_by_id, idVorgabe);
		this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.removeMap4(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.id);
		vorgabe.id = -1;
		this.vorgabefehlendAddAllOhneUpdate(ListUtils.create1(vorgabe));
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param idVorgabe Die ID des {@link GostKlausurvorgabe}-Objekts.
	 */
	public vorgabeRemoveById(idVorgabe : number) : void {
		this.vorgabeRemoveOhneUpdateById(idVorgabe);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabeRemoveAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		for (const vorgabe of listVorgaben)
			this.vorgabeRemoveOhneUpdateById(vorgabe.id);
		this.update_all();
	}

	private update_vorgabefehlendmenge() : void {
		this._vorgabenfehlendmenge.clear();
		this._vorgabenfehlendmenge.addAll(this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesAsList());
		this._vorgabenmenge.sort(this._compVorgabe);
	}

	/**
	 * Fügt ein {@link GostKlausurvorgabe}-Objekt hinzu.
	 *
	 * @param vorgabe Das {@link GostKlausurvorgabe}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public vorgabefehlendAdd(vorgabe : GostKlausurvorgabe) : void {
		this.vorgabefehlendAddAll(ListUtils.create1(vorgabe));
	}

	private vorgabefehlendAddAllOhneUpdate(list : List<GostKlausurvorgabe>) : void {
		for (const vorgabe of list)
			DeveloperNotificationException.ifMap5DPutOverwrites(this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach, vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.kursart, vorgabe.idFach, vorgabe);
	}

	/**
	 * Fügt alle {@link GostKlausurvorgabe}-Objekte hinzu.
	 *
	 * @param listVorgaben Die Menge der {@link GostKlausurvorgabe}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public vorgabefehlendAddAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		this.vorgabefehlendAddAllOhneUpdate(listVorgaben);
		this.update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurvorgabe}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabefehlendGetMengeAsList() : List<GostKlausurvorgabe> {
		return this._vorgabenfehlendmenge;
	}

	private vorgabefehlendRemoveOhneUpdate(vorgabe : GostKlausurvorgabe) : void {
		this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.remove(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.kursart, vorgabe.idFach);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurvorgabe}-Objekt.
	 *
	 * @param vorgabe die zu löschende {@link GostKlausurvorgabe}
	 */
	public vorgabefehlendRemove(vorgabe : GostKlausurvorgabe) : void {
		this.vorgabefehlendRemoveOhneUpdate(vorgabe);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurvorgabe}-Objekte.
	 *
	 * @param listVorgaben Die Liste der zu entfernenden
	 *                          {@link GostKlausurvorgabe}-Objekte.
	 */
	public vorgabefehlendRemoveAll(listVorgaben : List<GostKlausurvorgabe>) : void {
		for (const vorgabe of listVorgaben)
			this.vorgabefehlendRemoveOhneUpdate(vorgabe);
		this.update_all();
	}

	private update_kursklausurmenge() : void {
		this._kursklausurmenge.clear();
		this._kursklausurmenge.addAll(this._kursklausur_by_id.values());
		this._kursklausurmenge.sort(this._compKursklausur);
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public kursklausurAdd(kursklausur : GostKursklausur) : void {
		this.kursklausurAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private kursklausurAddAllOhneUpdate(list : Collection<GostKursklausur>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const klausur of list) {
			GostKlausurplanManager.kursklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", this._kursklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("kursklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}
		for (const klausur of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, klausur.id, klausur);
			this.kursklausurfehlendRemoveOhneUpdate(klausur);
		}
		this._klausurenInitialized = true;
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public kursklausurAddAll(listKursklausuren : List<GostKursklausur>) : void {
		this.kursklausurAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	private static kursklausurCheck(kursklausur : GostKursklausur) : void {
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
	public kursklausurGetByIdOrException(idKursklausur : number) : GostKursklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._kursklausur_by_id, idKursklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public kursklausurGetMengeAsList() : List<GostKursklausur> {
		return this._kursklausurmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurPatchAttributes(kursklausur : GostKursklausur) : void {
		this.kursklausurPatchAttributesOhneUpdate(kursklausur);
		this.update_all();
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	private kursklausurPatchAttributesOhneUpdate(kursklausur : GostKursklausur) : void {
		GostKlausurplanManager.kursklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._kursklausur_by_id, kursklausur.id, kursklausur);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausurMenge Das neue {@link GostKursklausur}-Objekt.
	 */
	public kursklausurMengePatchAttributes(kursklausurMenge : List<GostKursklausur>) : void {
		for (const kursklausur of kursklausurMenge)
			this.kursklausurPatchAttributesOhneUpdate(kursklausur);
		this.update_all();
	}

	private kursklausurRemoveOhneUpdateById(idKursklausur : number) : void {
		this.schuelerklausurRemoveAllOhneUpdate(this._schuelerklausur_by_idKursklausur_and_idSchueler.get1(idKursklausur));
		const removed : GostKursklausur | null = DeveloperNotificationException.ifMapRemoveFailes(this._kursklausur_by_id, idKursklausur);
		this.kursklausurfehlendRemoveOhneUpdate(removed);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idKursklausur Die ID des {@link GostKursklausur}-Objekts.
	 */
	public kursklausurRemoveById(idKursklausur : number) : void {
		this.kursklausurRemoveOhneUpdateById(idKursklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public kursklausurRemoveAll(listKursklausuren : List<GostKursklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.kursklausurRemoveOhneUpdateById(kursklausur.id);
		this.update_all();
	}

	private update_kursklausurfehlendmenge() : void {
		this._kursklausurfehlendmenge.clear();
		this._kursklausurfehlendmenge.addAll(this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesAsList());
	}

	/**
	 * Fügt ein {@link GostKursklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostKursklausur}-Objekt, welches hinzugefügt
	 *                    werden soll.
	 */
	public kursklausurfehlendAdd(kursklausur : GostKursklausur) : void {
		this.kursklausurfehlendAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private kursklausurfehlendAddAllOhneUpdate(list : List<GostKursklausur>) : void {
		for (const klausur of list) {
			const v : GostKlausurvorgabe = this.vorgabeByKursklausur(klausur);
			DeveloperNotificationException.ifMap5DPutOverwrites(this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs, v.abiJahrgang, v.halbjahr, v.quartal, v.id, klausur.idKurs, klausur);
		}
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public kursklausurfehlendAddAll(listKursklausuren : List<GostKursklausur>) : void {
		this.kursklausurfehlendAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public kursklausurfehlendGetMengeAsList() : List<GostKursklausur> {
		return this._kursklausurmenge;
	}

	private kursklausurfehlendRemoveOhneUpdate(kursklausur : GostKursklausur) : void {
		const vorgabe : GostKlausurvorgabe = this.vorgabeByKursklausur(kursklausur);
		this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.remove(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, vorgabe.id, kursklausur.idKurs);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param kursklausur das zu löschende {@link GostKursklausur}-Objekt.
	 */
	public kursklausurfehlendRemove(kursklausur : GostKursklausur) : void {
		this.kursklausurfehlendRemoveOhneUpdate(kursklausur);
		this.update_all();
	}

	private update_terminmenge() : void {
		this._terminmenge.clear();
		this._terminmenge.addAll(this._termin_by_id.values());
		this._terminmenge.sort(GostKlausurplanManager._compTermin);
	}

	/**
	 * Fügt ein {@link GostKlausurtermin}-Objekt hinzu.
	 *
	 * @param termin Das {@link GostKlausurtermin}-Objekt, welches hinzugefügt
	 *               werden soll.
	 */
	public terminAdd(termin : GostKlausurtermin) : void {
		this.terminAddAll(ListUtils.create1(termin));
	}

	private terminAddAllOhneUpdate(list : Collection<GostKlausurtermin>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const termin of list) {
			GostKlausurplanManager.terminCheck(termin);
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " existiert bereits!", this._termin_by_id.containsKey(termin.id));
			DeveloperNotificationException.ifTrue("terminAddAllOhneUpdate: ID=" + termin.id + " doppelt in der Liste!", !setOfIDs.add(termin.id));
		}
		for (const termin of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
	}

	/**
	 * Fügt alle {@link GostKlausurtermin}-Objekte hinzu.
	 *
	 * @param listTermine Die Menge der {@link GostKlausurtermin}-Objekte, welche
	 *                    hinzugefügt werden soll.
	 */
	public terminAddAll(listTermine : List<GostKlausurtermin>) : void {
		this.terminAddAllOhneUpdate(listTermine);
		this.update_all();
	}

	private static terminCheck(termin : GostKlausurtermin) : void {
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
	public terminGetByIdOrException(idTermin : number) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, idTermin);
	}

	/**
	 * Liefert das zum {@link GostKlausurraum} zugehörige {@link GostKlausurtermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param raum das {@link GostKlausurtermin}-Objekt.
	 *
	 * @return das zum Parameter zugehörige {@link GostKlausurtermin}-Objekt.
	 */
	public terminGetByRaumOrException(raum : GostKlausurraum) : GostKlausurtermin {
		return this.terminGetByIdOrException(raum.idTermin);
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
	public terminGetByIdOrNull(idTermin : number) : GostKlausurtermin | null {
		return this._termin_by_id.get(idTermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurtermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurtermin}-Objekte.
	 */
	public terminGetMengeAsList() : List<GostKlausurtermin> {
		return this._terminmenge;
	}

	private terminPatchAttributesOhneUpdate(termin : GostKlausurtermin) : void {
		GostKlausurplanManager.terminCheck(termin);
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, termin.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._termin_by_id, termin.id, termin);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurtermin}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param termin Das neue {@link GostKlausurtermin}-Objekt.
	 */
	public terminPatchAttributes(termin : GostKlausurtermin) : void {
		this.terminPatchAttributesOhneUpdate(termin);
		this.update_all();
	}

	private terminRemoveOhneUpdateById(idTermin : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._termin_by_id, idTermin);
		const kursklausurenZuTermin : List<GostKursklausur> | null = this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get3(idTermin);
		for (const k of kursklausurenZuTermin)
			k.idTermin = null;
		const schuelerklausurtermineZuTermin : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(idTermin);
		if (schuelerklausurtermineZuTermin !== null)
			for (const skt of schuelerklausurtermineZuTermin)
				skt.idTermin = null;
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurtermin}-Objekt.
	 *
	 * @param idTermin Die ID des {@link GostKlausurtermin}-Objekts.
	 */
	public terminRemoveById(idTermin : number) : void {
		this.terminRemoveOhneUpdateById(idTermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurtermin}-Objekte.
	 *
	 * @param listTermine Die Liste der zu entfernenden
	 *                    {@link GostKlausurtermin}-Objekte.
	 */
	public terminRemoveAll(listTermine : List<GostKlausurtermin>) : void {
		for (const termin of listTermine)
			this.terminRemoveOhneUpdateById(termin.id);
		this.update_all();
	}

	private update_schuelerklausurmenge() : void {
		this._schuelerklausurmenge.clear();
		this._schuelerklausurmenge.addAll(this._schuelerklausur_by_id.values());
		this._schuelerklausurmenge.sort(this._compSchuelerklausur);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                    hinzugefügt werden soll.
	 */
	public schuelerklausurAdd(kursklausur : GostSchuelerklausur) : void {
		this.schuelerklausurAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private schuelerklausurAddAllOhneUpdate(list : Collection<GostSchuelerklausur>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const klausur of list) {
			GostKlausurplanManager.schuelerklausurCheck(klausur);
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " existiert bereits!", this._schuelerklausur_by_id.containsKey(klausur.id));
			DeveloperNotificationException.ifTrue("schuelerklausurAddAllOhneUpdate: ID=" + klausur.id + " doppelt in der Liste!", !setOfIDs.add(klausur.id));
		}
		for (const klausur of list) {
			DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, klausur.id, klausur);
			this.schuelerklausurfehlendRemoveOhneUpdate(klausur);
		}
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public schuelerklausurAddAll(listKursklausuren : List<GostSchuelerklausur>) : void {
		this.schuelerklausurAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	private static schuelerklausurCheck(kursklausur : GostSchuelerklausur) : void {
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
	public schuelerklausurGetByIdOrException(idSchuelerklausur : number) : GostSchuelerklausur {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausur_by_id, idSchuelerklausur);
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public schuelerklausurGetMengeAsList() : List<GostSchuelerklausur> {
		return new ArrayList<GostSchuelerklausur>(this._schuelerklausurmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKursklausur}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param kursklausur Das neue {@link GostKursklausur}-Objekt.
	 */
	public schuelerklausurPatchAttributes(kursklausur : GostSchuelerklausur) : void {
		GostKlausurplanManager.schuelerklausurCheck(kursklausur);
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, kursklausur.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausur_by_id, kursklausur.id, kursklausur);
		this.update_all();
	}

	private schuelerklausurRemoveOhneUpdateById(idSchuelerklausur : number) : void {
		const removed : GostSchuelerklausur | null = DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausur_by_id, idSchuelerklausur);
		this.schuelerklausurterminRemoveAllOhneUpdate(this.schuelerklausurterminGetMengeBySchuelerklausur(removed));
		this.schuelerklausurfehlendRemoveOhneUpdate(removed);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param idSchuelerklausur Die ID des {@link GostSchuelerklausur}-Objekts.
	 */
	public schuelerklausurRemoveById(idSchuelerklausur : number) : void {
		this.schuelerklausurRemoveOhneUpdateById(idSchuelerklausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausuren Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausur}-Objekte.
	 */
	private schuelerklausurRemoveAllOhneUpdate(listSchuelerklausuren : List<GostSchuelerklausur>) : void {
		for (const schuelerklausur of listSchuelerklausuren)
			this.schuelerklausurRemoveOhneUpdateById(schuelerklausur.id);
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausur}-Objekte.
	 *
	 * @param listSchuelerklausuren Die Liste der zu entfernenden
	 *                          {@link GostSchuelerklausur}-Objekte.
	 */
	public schuelerklausurRemoveAll(listSchuelerklausuren : List<GostSchuelerklausur>) : void {
		this.schuelerklausurRemoveAllOhneUpdate(listSchuelerklausuren);
		this.update_all();
	}

	private update_schuelerklausurfehlendmenge() : void {
		this._schuelerklausurfehlendmenge.clear();
		this._schuelerklausurfehlendmenge.addAll(this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesAsList());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausur}-Objekt hinzu.
	 *
	 * @param kursklausur Das {@link GostSchuelerklausur}-Objekt, welches
	 *                    hinzugefügt werden soll.
	 */
	public schuelerklausurfehlendAdd(kursklausur : GostSchuelerklausur) : void {
		this.schuelerklausurfehlendAddAll(ListUtils.create1(kursklausur));
		this.update_all();
	}

	private schuelerklausurfehlendAddAllOhneUpdate(list : List<GostSchuelerklausur>) : void {
		for (const klausur of list) {
			const kursklausur : GostKursklausur = this.kursklausurBySchuelerklausur(klausur);
			const vorgabe : GostKlausurvorgabe = this.vorgabeByKursklausur(kursklausur);
			DeveloperNotificationException.ifMap5DPutOverwrites(this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur, vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, klausur.idSchueler, kursklausur.id, klausur);
		}
	}

	/**
	 * Fügt alle {@link GostKursklausur}-Objekte hinzu.
	 *
	 * @param listKursklausuren Die Menge der {@link GostKursklausur}-Objekte,
	 *                          welche hinzugefügt werden soll.
	 */
	public schuelerklausurfehlendAddAll(listKursklausuren : List<GostSchuelerklausur>) : void {
		this.schuelerklausurfehlendAddAllOhneUpdate(listKursklausuren);
		this.update_all();
	}

	/**
	 * Liefert eine Liste aller {@link GostKursklausur}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKursklausur}-Objekte.
	 */
	public schuelerklausurfehlendGetMengeAsList() : List<GostSchuelerklausur> {
		return new ArrayList<GostSchuelerklausur>(this._schuelerklausurfehlendmenge);
	}

	private schuelerklausurfehlendRemoveOhneUpdate(klausur : GostSchuelerklausur) : void {
		const kursklausur : GostKursklausur = this.kursklausurBySchuelerklausur(klausur);
		const vorgabe : GostKlausurvorgabe = this.vorgabeByKursklausur(kursklausur);
		this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.remove(vorgabe.abiJahrgang, vorgabe.halbjahr, vorgabe.quartal, klausur.idSchueler, kursklausur.id);
	}

	/**
	 * Entfernt ein existierendes {@link GostKursklausur}-Objekt.
	 *
	 * @param klausur die {@link GostKursklausur}
	 */
	public schuelerklausurfehlendRemove(klausur : GostSchuelerklausur) : void {
		this.schuelerklausurfehlendRemoveOhneUpdate(klausur);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKursklausur}-Objekte.
	 *
	 * @param listKursklausuren Die Liste der zu entfernenden
	 *                          {@link GostKursklausur}-Objekte.
	 */
	public schuelerklausurfehlendRemoveAll(listKursklausuren : List<GostSchuelerklausur>) : void {
		for (const kursklausur of listKursklausuren)
			this.schuelerklausurfehlendRemoveOhneUpdate(kursklausur);
		this.update_all();
	}

	private update_schuelerklausurterminmenge() : void {
		this._schuelerklausurterminmenge.clear();
		this._schuelerklausurterminmenge.addAll(this._schuelerklausurtermin_by_id.values());
		this._schuelerklausurterminmenge.sort(this._compSchuelerklausurTermin);
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausurtermin Das {@link GostSchuelerklausurTermin}-Objekt,
	 *                              welches hinzugefügt werden soll.
	 */
	public schuelerklausurterminAdd(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		this.schuelerklausurterminAddAll(ListUtils.create1(schuelerklausurtermin));
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurTermin}-Objekt hinzu.
	 *
	 * @param schuelerklausur Das {@link GostSchuelerklausurTermin}-Objekt, welches
	 *                        hinzugefügt werden soll.
	 */
	public schuelerklausurAddOhneUpdate(schuelerklausur : GostSchuelerklausurTermin) : void {
		this.schuelerklausurterminAddAllOhneUpdate(ListUtils.create1(schuelerklausur));
	}

	private schuelerklausurterminAddAllOhneUpdate(list : Collection<GostSchuelerklausurTermin>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const schuelerklausurtermin of list) {
			GostKlausurplanManager.schuelerklausurterminCheck(schuelerklausurtermin);
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " existiert bereits!", this._schuelerklausurtermin_by_id.containsKey(schuelerklausurtermin.id));
			DeveloperNotificationException.ifTrue("schuelerklausurterminAddAllOhneUpdate: ID=" + schuelerklausurtermin.id + " doppelt in der Liste!", !setOfIDs.add(schuelerklausurtermin.id));
		}
		for (const schuelerklausurtermin of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurTermin}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurtermine die Menge der
	 *                                   {@link GostSchuelerklausurTermin}-Objekte,
	 *                                   welche hinzugefügt werden sollen.
	 */
	public schuelerklausurterminAddAll(listSchuelerklausurtermine : List<GostSchuelerklausurTermin>) : void {
		this.schuelerklausurterminAddAllOhneUpdate(listSchuelerklausurtermine);
		this.update_all();
	}

	private static schuelerklausurterminCheck(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		DeveloperNotificationException.ifInvalidID("schuelerschuelerklausurtermin.idSchuelerschuelerklausurtermin", schuelerklausurtermin.id);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt. <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des angefragten-Objektes.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public schuelerklausurterminGetByIdOrException(idSchuelerklausurtermin : number) : GostSchuelerklausurTermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurtermin_by_id, idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurTermin}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public schuelerklausurterminGetMengeAsList() : List<GostSchuelerklausurTermin> {
		return new ArrayList<GostSchuelerklausurTermin>(this._schuelerklausurterminmenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurTermin}-Objekt durch
	 * das neue Objekt.
	 *
	 * @param schuelerklausurtermin Das neue
	 *                              {@link GostSchuelerklausurTermin}-Objekt.
	 */
	public schuelerklausurterminPatchAttributes(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		this.schuelerklausurterminPatchAttributesOhneUpdate(schuelerklausurtermin);
		this.update_all();
	}

	private schuelerklausurterminPatchAttributesOhneUpdate(schuelerklausurtermin : GostSchuelerklausurTermin) : void {
		GostKlausurplanManager.schuelerklausurterminCheck(schuelerklausurtermin);
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._schuelerklausurtermin_by_id, schuelerklausurtermin.id, schuelerklausurtermin);
	}

	private schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._schuelerklausurtermin_by_id, idSchuelerklausurtermin);
		this.schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurTermin}-Objekt.
	 *
	 * @param idSchuelerklausurtermin die ID des
	 *                                {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public schuelerklausurterminRemoveById(idSchuelerklausurtermin : number) : void {
		this.schuelerklausurterminRemoveOhneUpdateById(idSchuelerklausurtermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine die Liste der zu entfernenden
	 *                                   {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public schuelerklausurterminRemoveAllOhneUpdate(listSchuelerklausurtermine : List<GostSchuelerklausurTermin>) : void {
		for (const schuelerklausurtermin of listSchuelerklausurtermine)
			this.schuelerklausurterminRemoveOhneUpdateById(schuelerklausurtermin.id);
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurTermin}-Objekte.
	 *
	 * @param listSchuelerklausurtermine die Liste der zu entfernenden
	 *                                   {@link GostSchuelerklausurTermin}-Objekte.
	 */
	public schuelerklausurterminRemoveAll(listSchuelerklausurtermine : List<GostSchuelerklausurTermin>) : void {
		this.schuelerklausurterminRemoveAllOhneUpdate(listSchuelerklausurtermine);
		this.update_all();
	}

	private update_raummenge() : void {
		this._raummenge.clear();
		this._raummenge.addAll(this._raum_by_id.values());
		this._raummenge.sort(GostKlausurplanManager._compRaum);
	}

	/**
	 * Fügt ein {@link GostKlausurraum}-Objekt hinzu.
	 *
	 * @param raum Das {@link GostKlausurraum}-Objekt, welches hinzugefügt werden
	 *             soll.
	 */
	public raumAdd(raum : GostKlausurraum) : void {
		this.raumAddAll(ListUtils.create1(raum));
	}

	private raumAddAllOhneUpdate(list : Collection<GostKlausurraum>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const raum of list) {
			GostKlausurplanManager.raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!", this._raum_by_id.containsKey(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}
		for (const raum of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
	}

	/**
	 * Fügt alle {@link GostKlausurraum}-Objekte hinzu.
	 *
	 * @param listRaum Die Menge der {@link GostKlausurraum}-Objekte, welche
	 *                 hinzugefügt werden soll.
	 */
	public raumAddAll(listRaum : List<GostKlausurraum>) : void {
		this.raumAddAllOhneUpdate(listRaum);
		this.update_all();
	}

	private static raumCheck(raum : GostKlausurraum) : void {
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
	public raumGetByIdOrException(idRaum : number) : GostKlausurraum {
		return DeveloperNotificationException.ifMapGetIsNull(this._raum_by_id, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraum}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraum}-Objekte.
	 */
	public raumGetMengeAsList() : List<GostKlausurraum> {
		return this._raummenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraum}-Objekt durch das neue
	 * Objekt.
	 *
	 * @param raum Das neue {@link GostKlausurraum}-Objekt.
	 */
	public raumPatchAttributes(raum : GostKlausurraum) : void {
		GostKlausurplanManager.raumCheck(raum);
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, raum.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._raum_by_id, raum.id, raum);
		this.update_all();
	}

	private raumRemoveOhneUpdateById(idRaum : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._raum_by_id, idRaum);
		const rsList : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idRaum.get(idRaum);
		if (rsList !== null)
			for (const rs of rsList)
				this.raumstundeRemoveOhneUpdateById(rs.id);
	}

	private raumRemoveIfExistsNoCascadeOhneUpdateById(idRaum : number) : void {
		this._raum_by_id.remove(idRaum);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraum}-Objekt.
	 *
	 * @param idRaum Die ID des {@link GostKlausurraum}-Objekts.
	 */
	public raumRemoveById(idRaum : number) : void {
		this.raumRemoveOhneUpdateById(idRaum);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	private raumRemoveAllIfExistsNoCascadeOhneUpdate(listRaum : Collection<GostKlausurraum>) : void {
		for (const raum of listRaum)
			this.raumRemoveIfExistsNoCascadeOhneUpdateById(raum.id);
	}

	/**
	 * Entfernt alle {@link StundenplanRaum}-Objekte.
	 *
	 * @param listRaum Die Liste der zu entfernenden
	 *                 {@link StundenplanRaum}-Objekte.
	 */
	public raumRemoveAll(listRaum : List<GostKlausurraum>) : void {
		for (const raum of listRaum)
			this.raumRemoveOhneUpdateById(raum.id);
		this.update_all();
	}

	private update_raumstundenmenge() : void {
		this._raumstundenmenge.clear();
		this._raumstundenmenge.addAll(this._raumstunde_by_id.values());
	}

	/**
	 * Fügt ein {@link GostKlausurraumstunde}-Objekt hinzu.
	 *
	 * @param raumstunde Das {@link GostKlausurraumstunde}-Objekt, welches
	 *                   hinzugefügt werden soll.
	 */
	public raumstundeAdd(raumstunde : GostKlausurraumstunde) : void {
		this.raumstundeAddAll(ListUtils.create1(raumstunde));
	}

	private raumstundeAddAllOhneUpdate(list : Collection<GostKlausurraumstunde>) : void {
		const setOfIDs : HashSet<number> = new HashSet<number>();
		for (const raumstunde of list) {
			GostKlausurplanManager.raumstundeCheck(raumstunde);
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " existiert bereits!", this._raumstunde_by_id.containsKey(raumstunde.id));
			DeveloperNotificationException.ifTrue("raumstundeAddAllOhneUpdate: ID=" + raumstunde.id + " doppelt in der Liste!", !setOfIDs.add(raumstunde.id));
		}
		for (const raumstunde of list)
			DeveloperNotificationException.ifMapPutOverwrites(this._raumstunde_by_id, raumstunde.id, raumstunde);
	}

	/**
	 * Fügt alle {@link GostKlausurraumstunde}-Objekte hinzu.
	 *
	 * @param listRaumstunde Die Menge der {@link GostKlausurraumstunde}-Objekte,
	 *                       welche hinzugefügt werden soll.
	 */
	public raumstundeAddAll(listRaumstunde : Collection<GostKlausurraumstunde>) : void {
		this.raumstundeAddAllOhneUpdate(listRaumstunde);
		this.update_all();
	}

	private static raumstundeCheck(raumstunde : GostKlausurraumstunde) : void {
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
	public raumstundeGetByIdOrException(idRaumstunde : number) : GostKlausurraumstunde {
		return DeveloperNotificationException.ifMapGetIsNull(this._raumstunde_by_id, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostKlausurraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostKlausurraumstunde}-Objekte.
	 */
	public raumstundeGetMengeAsList() : List<GostKlausurraumstunde> {
		return this._raumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostKlausurraumstunde}-Objekt durch das
	 * neue Objekt.
	 *
	 * @param raumstunde Das neue {@link GostKlausurraumstunde}-Objekt.
	 */
	public raumstundePatchAttributes(raumstunde : GostKlausurraumstunde) : void {
		GostKlausurplanManager.raumstundeCheck(raumstunde);
		DeveloperNotificationException.ifMapRemoveFailes(this._raumstunde_by_id, raumstunde.id);
		DeveloperNotificationException.ifMapPutOverwrites(this._raumstunde_by_id, raumstunde.id, raumstunde);
		this.update_all();
	}

	private raumstundeRemoveOhneUpdateById(idRaumstunde : number) : void {
		DeveloperNotificationException.ifMapRemoveFailes(this._raumstunde_by_id, idRaumstunde);
		const skrsList : List<GostSchuelerklausurterminraumstunde> | null = this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get2(idRaumstunde);
		for (const skrs of skrsList)
			this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostKlausurraumstunde}-Objekt.
	 *
	 * @param idRaumstunde Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public raumstundeRemoveById(idRaumstunde : number) : void {
		this.raumstundeRemoveOhneUpdateById(idRaumstunde);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public raumstundeRemoveAllOhneUpdate(listRaumstunde : List<GostKlausurraumstunde>) : void {
		for (const raumstunde of listRaumstunde)
			this.raumstundeRemoveOhneUpdateById(raumstunde.id);
	}

	/**
	 * Entfernt alle {@link GostKlausurraumstunde}-Objekte.
	 *
	 * @param listRaumstunde Die Liste der zu entfernenden
	 *                       {@link GostKlausurraumstunde}-Objekte.
	 */
	public raumstundeRemoveAll(listRaumstunde : List<GostKlausurraumstunde>) : void {
		this.raumstundeRemoveAllOhneUpdate(listRaumstunde);
		this.update_all();
	}

	private update_schuelerklausurraumstundenmenge() : void {
		this._schuelerklausurterminraumstundenmenge.clear();
		this._schuelerklausurterminraumstundenmenge.addAll(this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getAllValues());
	}

	/**
	 * Fügt ein {@link GostSchuelerklausurterminraumstunde}-Objekt hinzu.
	 *
	 * @param schuelerklausurraumstunde Das
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt,
	 *                                  welches hinzugefügt werden soll.
	 */
	public schuelerklausurraumstundeAdd(schuelerklausurraumstunde : GostSchuelerklausurterminraumstunde) : void {
		this.schuelerklausurraumstundeAddAll(ListUtils.create1(schuelerklausurraumstunde));
	}

	private schuelerklausurraumstundeAddAllOhneUpdate(list : Collection<GostSchuelerklausurterminraumstunde>) : void {
		const setOfIDs : HashMap2D<number, number, GostSchuelerklausurterminraumstunde> = new HashMap2D<number, number, GostSchuelerklausurterminraumstunde>();
		for (const schuelerklausurraumstunde of list) {
			GostKlausurplanManager.schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
			DeveloperNotificationException.ifTrue("schuelerklausurraumstundeAddAllOhneUpdate: ID=(" + schuelerklausurraumstunde.idSchuelerklausurtermin + "," + schuelerklausurraumstunde.idRaumstunde + ") existiert bereits!", this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.containsKey12(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde));
			DeveloperNotificationException.ifTrue("schuelerklausurraumstundeAddAllOhneUpdate: ID=" + schuelerklausurraumstunde.idSchuelerklausurtermin + "," + schuelerklausurraumstunde.idRaumstunde + ") doppelt in der Liste!", setOfIDs.contains(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde));
			setOfIDs.put(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		}
		for (const schuelerklausurraumstunde of list)
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde, schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
	}

	/**
	 * Fügt alle {@link GostSchuelerklausurterminraumstunde}-Objekte hinzu.
	 *
	 * @param listSchuelerklausurraumstunde Die Menge der
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte,
	 *                                      welche hinzugefügt werden soll.
	 */
	public schuelerklausurraumstundeAddAll(listSchuelerklausurraumstunde : List<GostSchuelerklausurterminraumstunde>) : void {
		this.schuelerklausurraumstundeAddAllOhneUpdate(listSchuelerklausurraumstunde);
		this.update_all();
	}

	private static schuelerklausurraumstundeCheck(schuelerklausurraumstunde : GostSchuelerklausurterminraumstunde) : void {
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idSchuelerklausur", schuelerklausurraumstunde.idSchuelerklausurtermin);
		DeveloperNotificationException.ifInvalidID("schuelerklausurraumstunde.idRaumstunde", schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundeGetByIdSchuelerklausurterminAndIdRaumstundeOrException(idSchuelerklausurtermin : number, idRaumstunde : number) : GostSchuelerklausurterminraumstunde {
		return this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getSingle12OrException(idSchuelerklausurtermin, idRaumstunde);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundeGetByIdSchuelerklausurterminAndIdRaumstundeOrNull(idSchuelerklausurtermin : number, idRaumstunde : number) : GostSchuelerklausurterminraumstunde | null {
		return this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.getSingle12OrNull(idSchuelerklausurtermin, idRaumstunde);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte zur angegebenen Schülerklausurtermin-ID.
	 * <br>
	 * Laufzeit: O(1)
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 *
	 * @return das zur ID zugehörige {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundeGetMengeByIdSchuelerklausurtermin(idSchuelerklausurtermin : number) : List<GostSchuelerklausurterminraumstunde> {
		return this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get1(idSchuelerklausurtermin);
	}

	/**
	 * Liefert eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte. <br>
	 * Laufzeit: O(1)
	 *
	 * @return eine Liste aller {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public schuelerklausurraumstundeGetMengeAsList() : List<GostSchuelerklausurterminraumstunde> {
		return this._schuelerklausurterminraumstundenmenge;
	}

	/**
	 * Aktualisiert das vorhandene {@link GostSchuelerklausurterminraumstunde}-Objekt
	 * durch das neue Objekt.
	 *
	 * @param schuelerklausurraumstunde Das neue
	 *                                  {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 */
	public schuelerklausurraumstundePatchAttributes(schuelerklausurraumstunde : GostSchuelerklausurterminraumstunde) : void {
		GostKlausurplanManager.schuelerklausurraumstundeCheck(schuelerklausurraumstunde);
		this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde);
		this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.add(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde, schuelerklausurraumstunde);
		this.update_all();
	}

	private schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(idSchuelerklausur : number, idRaumstunde : number) : void {
		this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(idSchuelerklausur, idRaumstunde);
	}

	private schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin : number) : void {
		this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeAllByKey1(idSchuelerklausurtermin);
	}

	private schuelerklausurraumstundenmengeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin : number) : void {
		const skrsList : List<GostSchuelerklausurterminraumstunde> | null = this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get1(idSchuelerklausurtermin);
		if (skrsList !== null)
			for (const skrs of skrsList)
				this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.removeSingleOrException(skrs.idSchuelerklausurtermin, skrs.idRaumstunde);
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 * @param idRaumstunde      Die ID des {@link GostKlausurraumstunde}-Objekts.
	 */
	public schuelerklausurraumstundeRemoveByIdSchuelerklausurterminAndIdRaumstunde(idSchuelerklausurtermin : number, idRaumstunde : number) : void {
		this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(idSchuelerklausurtermin, idRaumstunde);
		this.update_all();
	}

	/**
	 * Entfernt ein existierendes {@link GostSchuelerklausurterminraumstunde}-Objekt.
	 *
	 * @param idSchuelerklausurtermin Die ID des {@link GostSchuelerklausurTermin}-Objekts.
	 */
	public schuelerklausurraumstundeRemoveByIdSchuelerklausurtermin(idSchuelerklausurtermin : number) : void {
		this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	private schuelerklausurraumstundeRemoveAllOhneUpdateByIdSchuelerklausurtermin(idsSchuelerklausurtermine : List<number>) : void {
		for (const idSchuelerklausurtermin of idsSchuelerklausurtermine)
			this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurtermin(idSchuelerklausurtermin);
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte, deren Schülerklausur-ID in der übergebenen Liste enthalten ist.
	 *
	 * @param idsSchuelerklausurtermine die Liste der Schülerklausur-IDs.
	 */
	public schuelerklausurraumstundeRemoveAllByIdSchuelerklausurtermin(idsSchuelerklausurtermine : List<number>) : void {
		this.schuelerklausurraumstundeRemoveAllOhneUpdateByIdSchuelerklausurtermin(idsSchuelerklausurtermine);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public schuelerklausurraumstundeRemoveAll(listSchuelerklausurRaumstunde : List<GostSchuelerklausurterminraumstunde>) : void {
		this.schuelerklausurraumstundeRemoveAllOhneUpdate(listSchuelerklausurRaumstunde);
		this.update_all();
	}

	/**
	 * Entfernt alle {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 *
	 * @param listSchuelerklausurRaumstunde Die Liste der zu entfernenden
	 *                                      {@link GostSchuelerklausurterminraumstunde}-Objekte.
	 */
	public schuelerklausurraumstundeRemoveAllOhneUpdate(listSchuelerklausurRaumstunde : List<GostSchuelerklausurterminraumstunde>) : void {
		for (const schuelerklausurraumstunde of listSchuelerklausurRaumstunde)
			this.schuelerklausurraumstundeRemoveOhneUpdateByIdSchuelerklausurterminAndIdRaumstunde(schuelerklausurraumstunde.idSchuelerklausurtermin, schuelerklausurraumstunde.idRaumstunde);
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurvorgabe}n zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurvorgabe}n
	 */
	public vorgabeGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurvorgabe> {
		if (quartal === 0)
			return this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get12(abiJahrgang, halbjahr.id);
		return this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get123(abiJahrgang, halbjahr.id, quartal);
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
	public vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : GostKlausurvorgabe | null {
		return this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getSingle12345OrNull(abiJahrgang, halbjahr.id, quartal, kursartAllg.id, idFach);
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
	public vorgabeGetMengeByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		if (quartal > 0) {
			const retList : List<GostKlausurvorgabe> | null = new ArrayList<GostKlausurvorgabe>();
			const vorgabe : GostKlausurvorgabe | null = this.vorgabeGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang, halbjahr, quartal, kursartAllg, idFach);
			if (vorgabe !== null)
				retList.add(vorgabe);
			return retList;
		}
		return this.vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiJahrgang, halbjahr, kursartAllg, idFach);
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
	public vorgabeGetMengeByHalbjahrAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, kursartAllg : GostKursart, idFach : number) : List<GostKlausurvorgabe> {
		return this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get1245(abiJahrgang, halbjahr.id, GostKursart.fromKuerzelOrException(kursartAllg.kuerzel).id, idFach);
	}

	/**
	 * Gibt die Vorgänger-{@link GostKlausurvorgabe} zum übergebenen Parameter zurück (vorhergehendes Quartal des aktuellen Schuljahres) oder <code>null</code>, falls es keinen Vorgänger gibt.
	 *
	 * @param vorgabe das {@link GostKlausurvorgabe}-Objekt, dessen Vorgänger gesucht ist.
	 *
	 * @return die Vorgänger-{@link GostKlausurvorgabe} oder <code>null</code>, falls es keinen Vorgänger gibt.
	 */
	public vorgabeGetPrevious(vorgabe : GostKlausurvorgabe) : GostKlausurvorgabe | null {
		const vorgabenSchuljahr : List<GostKlausurvorgabe> = this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get1245OrException(vorgabe.abiJahrgang, vorgabe.halbjahr, GostKursart.fromKuerzelOrException(vorgabe.kursart).id, vorgabe.idFach);
		if ((vorgabe.halbjahr % 2) === 1)
			vorgabenSchuljahr.addAll(this._vorgabe_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.get1245(vorgabe.abiJahrgang, vorgabe.halbjahr - 1, GostKursart.fromKuerzelOrException(vorgabe.kursart).id, vorgabe.idFach));
		vorgabenSchuljahr.sort(this._compVorgabe);
		const listIndex : number = vorgabenSchuljahr.indexOf(vorgabe);
		if (listIndex === 0)
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
	public kursklausurGetByTerminAndKursid(termin : GostKlausurtermin, idKurs : number) : GostKursklausur | null {
		const klausuren : List<GostKursklausur> | null = this.kursklausurGetMengeByTerminid(termin.id);
		for (const klaus of klausuren) {
			if (klaus.idKurs === idKurs)
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
	public terminGetMengeByDatum(datum : string) : List<GostKlausurtermin> {
		return this._terminmenge_by_datum_and_abijahr.get1(GostKlausurplanManager.datumStringToLong(datum));
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die dasselbe Datum wie der als Parameter übergebene {@link GostKlausurtermin} haben.
	 *
	 * @param termin der {@link GostKlausurtermin}, an dessen Datum die {@link GostKlausurtermin}e gesucht werden.
	 * @param mitTermin wenn <code>true</code>, enthält die Rückgabe auch den {@link GostKlausurtermin} <code>termin</code>, bei <code>false</code> wird er entfernt.
	 *
	 * @return die {@link GostKlausurtermin}en, die dasselbe Datum wie der als Parameter übergebene {@link GostKlausurtermin} haben.
	 */
	public terminSelbesDatumGetMengeByTermin(termin : GostKlausurtermin, mitTermin : boolean) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> = this.terminGetMengeByDatum(DeveloperNotificationException.ifNull(JavaString.format("Datum des Termins %d", termin.id), termin.datum));
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
	public terminGruppierteUeberschneidungenGetMengeByDatum(datum : string) : List<List<GostKlausurtermin>> {
		return this.gruppiereUeberschneidungen(this.terminGetMengeByDatum(datum));
	}

	/**
	 * Liefert eine Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum und Abiturjahrgang. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 *
	 * @param datum das gesuchte Datum der {@link GostKlausurtermin}e im Format <code>YYYY-MM-DD</code>
	 * @param abiJahrgang der Abiturjahrgang, innerhalb dessen die {@link GostKlausurtermin}e gesucht werden
	 *
	 * @return die Liste von Listen von {@link GostKlausurtermin}en zum übergebenen Datum. Die inneren Listen enthalten mehrere Termine, falls sich die Termine hinsichtlich ihrer Start- und Endzeiten überschneiden.
	 */
	public terminGruppierteUeberschneidungenGetMengeByDatumAndAbijahr(datum : string, abiJahrgang : number | null) : List<List<GostKlausurtermin>> {
		if (abiJahrgang === null)
			return this.terminGruppierteUeberschneidungenGetMengeByDatum(datum);
		return this.gruppiereUeberschneidungen(this._terminmenge_by_datum_and_abijahr.get12(GostKlausurplanManager.datumStringToLong(datum), abiJahrgang));
	}

	private gruppiereUeberschneidungen(termine : List<GostKlausurtermin>) : List<List<GostKlausurtermin>> {
		const ergebnis : List<List<GostKlausurtermin>> = new ArrayList<List<GostKlausurtermin>>();
		let added : boolean = false;
		for (const terminToAdd of termine) {
			for (const listToCheck of ergebnis) {
				for (const terminInListe of termine) {
					if (this.checkTerminUeberschneidung(terminInListe, terminToAdd)) {
						listToCheck.add(terminToAdd);
						added = true;
					}
					if (added)
						break;
				}
				if (added)
					break;
			}
			if (!added)
				ergebnis.add(ListUtils.create1(terminToAdd));
		}
		return ergebnis;
	}

	private checkTerminUeberschneidung(t1 : GostKlausurtermin, t2 : GostKlausurtermin) : boolean {
		const s1 : number = this.minKlausurstartzeitByTermin(t1, true);
		const s2 : number = this.minKlausurstartzeitByTermin(t2, true);
		const e1 : number = this.maxKlausurendzeitByTermin(t1, true);
		const e2 : number = this.maxKlausurendzeitByTermin(t2, true);
		return (e1 >= s2) && (e2 >= s1);
	}

	private kursklausurGetMengeByTerminid(idTermin : number | null) : List<GostKursklausur> {
		return this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get3((idTermin !== null) ? idTermin : -1);
	}

	/**
	 * Liefert die Liste von {@link GostKursklausur}en, die zum übergebenen Termin gehören.
	 *
	 * @param termin der {@link GostKlausurtermin}, zuu dem die {@link GostKursklausur}en gesucht werden
	 *
	 * @return die Liste von {@link GostKursklausur}en, die zum übergebenen Termin gehören.
	 */
	public kursklausurGetMengeByTermin(termin : GostKlausurtermin) : List<GostKursklausur> {
		return this.kursklausurGetMengeByTerminid(termin.id);
	}

	/**
	 * Liefert die Menge von {@link GostKursklausur}en, die zum übergebenen Termin gehören, die ggf. auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin enthalten.
	 *
	 * @param termin der {@link GostKlausurtermin}, zuu dem die {@link GostKursklausur}en gesucht werden
	 * @param mitNachschreibern falls <code>true</code>, werden auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin in der Rückgabe enthalten sein.
	 *
	 * @return die Menge von {@link GostKursklausur}en, die zum übergebenen Termin gehören, die ggf. auch die {@link GostKursklausur}en der Nachschreiber an diesem Termin enthalten.
	 */
	public kursklausurMitNachschreibernGetMengeByTermin(termin : GostKlausurtermin, mitNachschreibern : boolean) : JavaSet<GostKursklausur> {
		const klausuren : JavaSet<GostKursklausur> | null = new HashSet<GostKursklausur>(this.kursklausurGetMengeByTermin(termin));
		if (mitNachschreibern)
			for (const skt of this.schuelerklausurterminGetMengeByTermin(termin)) {
				klausuren.add(this.kursklausurBySchuelerklausurTermin(skt));
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
	public kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> {
		if (quartal > 0)
			return this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get1234(abiJahrgang, halbjahr.id, -1, quartal);
		return this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get123(abiJahrgang, halbjahr.id, -1);
	}

	/**
	 * Liefert eine {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@link PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang, zu dem die Paralleljahrgänge gesucht werden
	 * @param halbjahr    das {@link GostHalbjahr}, zu dem die Paralleljahrgänge gesucht werden
	 * @param includeSelf falls <code>true</code>, ist das {@link PairNN} aus <code>abiJahrgang</code> und <code>halbjahr</code> in der Rückgabe inkludiert
	 *
	 * @return die {@link PairNN}-Liste aller aktiven Paralleljahrgänge in der Oberstufe. Die {@link PairNN}e bestehen aus dem jeweiligen Abiturjahrgang und dem {@link GostHalbjahr}.
	 */
	public static halbjahreParallelUndAktivGetMenge(abiJahrgang : number, halbjahr : GostHalbjahr, includeSelf : boolean) : List<PairNN<number, GostHalbjahr>> {
		const ergebnis : List<PairNN<number, GostHalbjahr>> = new ArrayList<PairNN<number, GostHalbjahr>>();
		if (includeSelf)
			ergebnis.add(new PairNN<number, GostHalbjahr>(abiJahrgang, halbjahr));
		if (halbjahr.id >= 2)
			ergebnis.add(new PairNN<number, GostHalbjahr>(abiJahrgang + 1, GostHalbjahr.fromIDorException(halbjahr.id - 2)));
		if (halbjahr.id >= 4)
			ergebnis.add(new PairNN<number, GostHalbjahr>(abiJahrgang + 2, GostHalbjahr.fromIDorException(halbjahr.id - 4)));
		if (halbjahr.id <= 3)
			ergebnis.add(new PairNN<number, GostHalbjahr>(abiJahrgang - 1, GostHalbjahr.fromIDorException(halbjahr.id + 2)));
		if (halbjahr.id <= 1)
			ergebnis.add(new PairNN<number, GostHalbjahr>(abiJahrgang - 2, GostHalbjahr.fromIDorException(halbjahr.id + 4)));
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
	public terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, multijahrgang : boolean) : List<GostKlausurtermin> {
		if (!multijahrgang)
			return this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal);
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const jgHj of GostKlausurplanManager.halbjahreParallelUndAktivGetMenge(abiJahrgang, halbjahr, true))
			termine.addAll(this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(jgHj.a, jgHj.b, quartal));
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
	public terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		if (quartal > 0) {
			const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
			termine.addAll(this._terminmenge_by_abijahr_and_halbjahr_and_quartal.get123(abiJahrgang, halbjahr.id, quartal));
			termine.addAll(this._terminmenge_by_abijahr_and_halbjahr_and_quartal.get123(abiJahrgang, halbjahr.id, 0));
			return termine;
		}
		return this._terminmenge_by_abijahr_and_halbjahr_and_quartal.get12(abiJahrgang, halbjahr.id);
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
	public terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, multijahrgang : boolean) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const t of this.terminGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang, halbjahr, quartal, multijahrgang))
			if (!t.istHaupttermin || t.nachschreiberZugelassen)
				termine.add(t);
		termine.sort(GostKlausurplanManager._compTermin);
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
	public terminNtMengeEnthaeltFremdeJgstByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, multijahrgang : boolean) : boolean {
		for (const t of this.terminNTGetMengeByAbijahrAndHalbjahrAndQuartalMultijahrgang(abiJahrgang, halbjahr, quartal, multijahrgang))
			if (this.terminMitAnderenJgst(t))
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
	public terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const termine : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const t of this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (t.istHaupttermin)
				termine.add(t);
		termine.sort(GostKlausurplanManager._compTermin);
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
	public terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (termin.datum !== null)
				ergebnis.add(termin);
		ergebnis.sort(GostKlausurplanManager._compTermin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen bereits ein Datum zugewiesen wurde.
	 */
	public terminMitDatumGetMenge() : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this._terminmenge)
			if (termin.datum !== null)
				ergebnis.add(termin);
		ergebnis.sort(GostKlausurplanManager._compTermin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 */
	public terminOhneDatumGetMenge() : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this._terminmenge)
			if (termin.datum === null)
				ergebnis.add(termin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 *
	 * @param abiJahrgang   der Abitur-Jahrgang, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param halbjahr      das {@link GostHalbjahr}, zu dem die {@link GostKlausurtermin}e gesucht werden
	 * @param quartal       die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von {@link GostKlausurtermin}en, denen noch kein Datum zugewiesen wurde.
	 */
	public terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (termin.datum === null)
				ergebnis.add(termin);
		return ergebnis;
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
	public terminHtMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const termineMitDatum : List<GostKlausurtermin> | null = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminHtGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (termin.datum !== null)
				termineMitDatum.add(termin);
		termineMitDatum.sort(GostKlausurplanManager._compTermin);
		return termineMitDatum;
	}

	/**
	 * Gibt das allen Kursklausuren gemeinsame Quartal innerhalb des übergebenen {@link GostKlausurtermin}s zurück. Falls es verschiedene Quartale sind, wird <code>-1</code> zurückgegeben.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return das allen Kursklausuren gemeinsame Quartal innerhalb des übergebenen {@link GostKlausurtermin}s, sonst <code>-1</code>.
	 */
	public quartalGetByTermin(termin : GostKlausurtermin) : number {
		const klausuren : List<GostKursklausur> = this.kursklausurGetMengeByTerminid(termin.id);
		const schuelertermine : List<GostSchuelerklausurTermin> = this.schuelerklausurterminNtGetMengeByTermin(termin);
		if (klausuren.isEmpty() && schuelertermine.isEmpty())
			return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, termin.id).quartal;
		const vorgaben : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();
		for (const k of klausuren)
			vorgaben.add(this.vorgabeByKursklausur(k));
		for (const k of schuelertermine)
			vorgaben.add(this.vorgabeBySchuelerklausurTermin(k));
		let quartal : number = -1;
		for (const v of vorgaben) {
			if (quartal === -1)
				quartal = v.quartal;
			if (quartal !== v.quartal)
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
	public minKlausurstartzeitByTermin(termin : GostKlausurtermin, includeNachschreiber : boolean) : number {
		const skts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminAktuellGetMengeByTermin(termin);
		if (skts.isEmpty())
			return DeveloperNotificationException.ifNull("Die Startzeit des Termins darf an dieser Stelle nicht null sein.", termin.startzeit);
		return this.minKlausurstartzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die minimale Startzeit des {@link GostKlausurraum}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die minimale Startzeit des {@link GostKlausurraum}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public minKlausurstartzeitByRaum(raum : GostKlausurraum, includeNachschreiber : boolean) : number {
		const skts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminGetMengeByRaum(raum);
		if (skts.isEmpty())
			return DeveloperNotificationException.ifNull("Die Startzeit des Termins darf an dieser Stelle nicht null sein.", this.terminGetByRaumOrException(raum).startzeit);
		return this.minKlausurstartzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die minimale Startzeit der {@link GostSchuelerklausurTermin}e in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren in der Menge
	 *
	 * @param skts die zu prüfenden {@link GostSchuelerklausurTermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die minimale Startzeit der {@link GostSchuelerklausurTermin}e in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren in der Menge
	 */
	public minKlausurstartzeitBySchuelerklausurterminMenge(skts : List<GostSchuelerklausurTermin>, includeNachschreiber : boolean) : number {
		if (skts.isEmpty())
			throw new DeveloperNotificationException("Keine Schülerklausurtermine zur Ermittlung der minimalen Klausurstartzeit gefunden.")
		let minStart : number = 1440;
		for (const skt of skts) {
			if (!includeNachschreiber && skt.folgeNr > 0)
				continue;
			const skStartzeit : number = this.startzeitBySchuelerklausurterminOrException(skt);
			if (skStartzeit < minStart)
				minStart = skStartzeit;
		}
		return minStart;
	}

	/**
	 * Liefert die maximale Endzeit des {@link GostKlausurraum}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Endzeit des {@link GostKlausurraum}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public maxKlausurendzeitByRaum(raum : GostKlausurraum, includeNachschreiber : boolean) : number {
		const skts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminGetMengeByRaum(raum);
		return this.maxKlausurendzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die maximale Endzeit des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Endzeit des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public maxKlausurendzeitByTermin(termin : GostKlausurtermin, includeNachschreiber : boolean) : number {
		const skts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminAktuellGetMengeByTermin(termin);
		return this.maxKlausurendzeitBySchuelerklausurterminMenge(skts, includeNachschreiber);
	}

	/**
	 * Liefert die maximale Endzeit der {@link GostSchuelerklausurTermin}e in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren in der Menge
	 *
	 * @param skts die zu prüfenden {@link GostSchuelerklausurTermin}e
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren in der Menge berücksichtigt
	 *
	 * @return die maximale Endzeit der {@link GostSchuelerklausurTermin}e in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren in der Menge
	 */
	public maxKlausurendzeitBySchuelerklausurterminMenge(skts : List<GostSchuelerklausurTermin>, includeNachschreiber : boolean) : number {
		let maxEnd : number = this.minKlausurstartzeitBySchuelerklausurterminMenge(skts, includeNachschreiber) + 1;
		if (skts.isEmpty())
			return maxEnd;
		for (const skt of skts) {
			if (!includeNachschreiber && skt.folgeNr > 0)
				continue;
			const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
			const endzeit : number = this.startzeitBySchuelerklausurterminOrException(skt) + vorgabe.dauer + vorgabe.auswahlzeit;
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
	public minKlausurdauerGetByTermin(termin : GostKlausurtermin, includeNachschreiber : boolean) : number {
		let minDauer : number = -1;
		const skts : List<GostSchuelerklausurTermin> | null = this.schuelerklausurterminAktuellGetMengeByTermin(termin);
		for (const skt of skts) {
			const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
			minDauer = (minDauer === -1 || vorgabe.dauer < minDauer) ? vorgabe.dauer : minDauer;
		}
		return minDauer === -1 ? 0 : minDauer;
	}

	/**
	 * Liefert die maximale Klausurdauer des {@link GostKlausurtermin}s in Minuten und berücksichtigt dabei auf Wunsch auch Nachschreibklausuren an dem Termin
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param includeNachschreiber wenn <code>true</code> werden auch Nachschreibklausuren an dem Termin berücksichtigt
	 *
	 * @return die maximale Klausurdauer des {@link GostKlausurtermin}s in Minuten ggf. unter Berücksichtigung der Nachschreibklausuren an dem Termin
	 */
	public maxKlausurdauerGetByTermin(termin : GostKlausurtermin, includeNachschreiber : boolean) : number {
		let maxDauer : number = 0;
		const skts : List<GostSchuelerklausurTermin> = this.schuelerklausurterminAktuellGetMengeByTermin(termin);
		for (const skt of skts) {
			const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
			maxDauer = (vorgabe.dauer > maxDauer) ? vorgabe.dauer : maxDauer;
		}
		return maxDauer;
	}

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
	private konfliktPaarSchuelerklausurtermineGetMenge(menge1 : List<GostSchuelerklausurTermin> | null, menge2 : List<GostSchuelerklausurTermin> | null) : List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> {
		if ((menge1 === null) || (menge2 === null) || menge1.isEmpty() || menge2.isEmpty())
			return new ArrayList();
		const map1 : JavaMap<number, GostSchuelerklausurTermin> = new HashMap<number, GostSchuelerklausurTermin>();
		for (const termin1 of menge1)
			map1.put(this.schuelerklausurGetByIdOrException(termin1.idSchuelerklausur).idSchueler, termin1);
		return this.konfliktPaarByMapSchuelerklausurterminToListSchuelerklausurterminGetMenge(map1, menge2);
	}

	private konfliktPaarByMapSchuelerklausurterminToListSchuelerklausurterminGetMenge(menge1 : JavaMap<number, GostSchuelerklausurTermin> | null, menge2 : List<GostSchuelerklausurTermin> | null) : List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> {
		const ergebnis : List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> = new ArrayList<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>>();
		if ((menge1 === null) || (menge2 === null) || menge1.isEmpty() || menge2.isEmpty())
			return ergebnis;
		for (const skt2 of menge2) {
			const sk : GostSchuelerklausur = this.schuelerklausurBySchuelerklausurtermin(skt2);
			const skt1 : GostSchuelerklausurTermin | null = menge1.get(sk.idSchueler);
			if ((skt1 !== null) && (skt1.id !== skt2.id))
				ergebnis.add(new PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>(skt1, skt2));
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
	public konfliktPaarGetMengeTerminAndSchuelerklausurtermin(termin : GostKlausurtermin, skt : GostSchuelerklausurTermin) : List<PairNN<GostSchuelerklausurTermin, GostSchuelerklausurTermin>> {
		return this.konfliktPaarSchuelerklausurtermineGetMenge(this.schuelerklausurterminAktuellGetMengeByTermin(termin), ListUtils.create1(skt));
	}

	/**
	 * Prüft, ob der zu einem {@link GostSchuelerklausurTermin} gehörige Schüler in einer {@link GostKursklausur} enthalten ist.
	 *
	 * @param schuelerklausurTermin der zu prüfende {@link GostSchuelerklausurTermin}
	 * @param kursklausur     die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls der zum {@link GostSchuelerklausurTermin} gehörige Schüler in der {@link GostKursklausur} enthalten ist
	 */
	public konfliktZuKursklausurBySchuelerklausur(schuelerklausurTermin : GostSchuelerklausurTermin, kursklausur : GostKursklausur) : boolean {
		const schuelerids : List<number> = new ArrayList<number>();
		for (const sk of this.schuelerklausurGetMengeByKursklausur(kursklausur))
			schuelerids.add(sk.idSchueler);
		return schuelerids.contains(this.schuelerklausurBySchuelerklausurtermin(schuelerklausurTermin).idSchueler);
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
	public konflikteMapKursklausurSchueleridsByTermin(termin : GostKlausurtermin) : JavaMap<GostKursklausur, JavaSet<number>> {
		const klausuren : List<GostKursklausur> | null = this.kursklausurGetMengeByTermin(termin);
		return this.berechneKonflikte(klausuren, klausuren, this.getSchuelerIDsFromSchuelerklausurterminen(this.schuelerklausurterminAktuellNtGetMengeByTermin(termin)));
	}

	/**
	 * Liefert die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return die Anzahl der bereits existierenden Schüler-Konflikte innerhalb des übergebenen {@link GostKlausurtermin}s.
	 */
	public konflikteAnzahlGetByTermin(termin : GostKlausurtermin) : number {
		return GostKlausurplanManager.countKonflikte(this.konflikteMapKursklausurSchueleridsByTermin(termin));
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
	public konflikteNeuMapKursklausurSchueleridsByTerminAndKursklausur(termin : GostKlausurtermin, kursklausur : GostKursklausur) : JavaMap<GostKursklausur, JavaSet<number>> {
		return this.berechneKonflikte(this.kursklausurGetMengeByTermin(termin), ListUtils.create1(kursklausur), this.getSchuelerIDsFromSchuelerklausurterminen(this.schuelerklausurterminAktuellNtGetMengeByTermin(termin)));
	}

	/**
	 * Liefert die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param kursklausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl der neuen Schüler-Konflikte, die die übergebe {@link GostKursklausur} bei Hinzufügen im übergebenen {@link GostKlausurtermin} verursacht.
	 */
	public konflikteAnzahlZuTerminGetByTerminAndKursklausur(termin : GostKlausurtermin, kursklausur : GostKursklausur) : number {
		return GostKlausurplanManager.countKonflikte(this.konflikteNeuMapKursklausurSchueleridsByTerminAndKursklausur(termin, kursklausur));
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
	public konflikteZuEigenemTerminMapGetByKursklausur(klausur : GostKursklausur) : JavaMap<GostKursklausur, JavaSet<number>> {
		const klausuren1 : List<GostKursklausur> = this._kursklausurmenge_by_abijahr_and_halbjahr_and_idTermin_and_quartal.get3OrException(DeveloperNotificationException.ifNull("idTermin", klausur.idTermin));
		klausuren1.remove(klausur);
		return this.berechneKonflikte(klausuren1, ListUtils.create1(klausur), this.getSchuelerIDsFromSchuelerklausurterminen(this.schuelerklausurterminAktuellNtGetMengeByTermin(this.terminOrExceptionByKursklausur(klausur))));
	}

	/**
	 * Liefert die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return die Anzahl Schüler-Konfilte, die die übergebe {@link GostKursklausur} im zugewiesenen {@link GostKlausurtermin} verursacht.
	 */
	public konflikteAnzahlZuEigenemTerminGetByKursklausur(klausur : GostKursklausur) : number {
		return GostKlausurplanManager.countKonflikte(this.konflikteZuEigenemTerminMapGetByKursklausur(klausur));
	}

	private berechneKonflikte(klausuren1 : List<GostKursklausur>, klausuren2 : List<GostKursklausur>, skts : List<number> | null) : JavaMap<GostKursklausur, JavaSet<number>> {
		if (klausuren1.isEmpty() || klausuren2.isEmpty())
			return new HashMap();
		const result : JavaMap<GostKursklausur, JavaSet<number>> | null = new HashMap<GostKursklausur, JavaSet<number>>();
		const kursklausuren2Copy : List<GostKursklausur> | null = new ArrayList<GostKursklausur>(klausuren2);
		for (const kk1 of klausuren1) {
			kursklausuren2Copy.remove(kk1);
			for (const kk2 of kursklausuren2Copy) {
				const konflikte : JavaSet<number> | null = this.berechneKlausurKonflikte(kk1, kk2);
				if (!konflikte.isEmpty()) {
					MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte);
					MapUtils.getOrCreateHashSet(result, kk2).addAll(konflikte);
				}
				if (skts !== null) {
					const konflikte2 : JavaSet<number> | null = GostKlausurplanManager.berechneIdKonflikte(this.getSchuelerIDsFromKursklausur(kk1), skts);
					if (!konflikte2.isEmpty())
						MapUtils.getOrCreateHashSet(result, kk1).addAll(konflikte2);
				}
			}
		}
		return result;
	}

	private berechneKlausurKonflikte(kk1 : GostKursklausur, kk2 : GostKursklausur) : JavaSet<number> {
		return GostKlausurplanManager.berechneIdKonflikte(this.getSchuelerIDsFromKursklausur(kk1), this.getSchuelerIDsFromKursklausur(kk2));
	}

	private static berechneIdKonflikte(kk1 : List<number>, kk2 : List<number>) : JavaSet<number> {
		const konflikte : HashSet<number> = new HashSet<number>(kk1);
		konflikte.retainAll(kk2);
		return konflikte;
	}

	private static countKonflikte(konflikte : JavaMap<GostKursklausur, JavaSet<number>>) : number {
		const susIds : HashSet<number> = new HashSet<number>();
		for (const klausurSids of konflikte.values())
			susIds.addAll(klausurSids);
		return susIds.size();
	}

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
	public klausurenProSchueleridExceedingKWThresholdByTerminAndThreshold(termin : GostKlausurtermin, threshold : number) : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> {
		if (termin.datum === null)
			return new HashMap();
		const kw : number = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abijahr, null, threshold, false);
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
	public klausurenProSchueleridExceedingKWThresholdByTerminAndKursklausurAndThreshold(termin : GostKlausurtermin, klausur : GostKursklausur, threshold : number) : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> {
		if (termin.datum === null)
			return new HashMap();
		const kw : number = DateUtils.gibKwDesDatumsISO8601(termin.datum);
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, termin.abijahr, this.schuelerklausurterminGetMengeByKursklausur(klausur), threshold, false);
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
	public klausurenProSchueleridExceedingKWThresholdByTerminAndDatumAndThreshold(termin : GostKlausurtermin, datum : string, threshold : number, thresholdOnly : boolean) : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> {
		const kwDatum : number = DateUtils.gibKwDesDatumsISO8601(datum);
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kwDatum, termin.abijahr, this.schuelerklausurterminAktuellGetMengeByTermin(termin), threshold, thresholdOnly);
	}

	private klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw : number, abijahr : number, addMenge : List<GostSchuelerklausurTermin> | null, threshold : number, thresholdOnly : boolean) : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> {
		const schuelerklausurterminaktuellmenge_by_schuelerId : JavaMap<number, List<GostSchuelerklausurTermin>> | null = this._schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.getMap3OrNull(abijahr, kw);
		if (schuelerklausurterminaktuellmenge_by_schuelerId === null)
			return new HashMap();
		const addTerminMap : JavaMap<number, List<GostSchuelerklausurTermin>> = new HashMap<number, List<GostSchuelerklausurTermin>>();
		if (addMenge !== null)
			for (const addSkt of addMenge)
				MapUtils.getOrCreateArrayList(addTerminMap, this.schuelerklausurBySchuelerklausurtermin(addSkt).idSchueler).add(addSkt);
		const ergebnis : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> = new HashMap<number, JavaSet<GostSchuelerklausurTermin>>();
		for (const entry of schuelerklausurterminaktuellmenge_by_schuelerId.entrySet()) {
			const klausuren : JavaSet<GostSchuelerklausurTermin> | null = new HashSet<GostSchuelerklausurTermin>(entry.getValue());
			if (addMenge !== null) {
				const addSkts : List<GostSchuelerklausurTermin> | null = addTerminMap.get(entry.getKey());
				if (addSkts !== null)
					klausuren.addAll(addTerminMap.get(entry.getKey()));
			}
			if ((klausuren.size() === threshold) || ((klausuren.size() > threshold) && !thresholdOnly))
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
	public klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndThreshold(kw : number, abijahr : number, threshold : number, thresholdOnly : boolean) : JavaMap<number, JavaSet<GostSchuelerklausurTermin>> {
		return this.klausurenProSchueleridExceedingKWThresholdByKwAndAbijahrAndAddmengeAndThreshold(kw, abijahr, null, threshold, thresholdOnly);
	}

	/**
	 * Liefert für einen Schwellwert, eine Kalenderwoche, und ein Abiturjahr eine Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 *
	 * @param abijahr       das Abiturjahr der gesuchten Konflikt-Schüler
	 * @param halbjahr das GostHalbjahr
	 * @param quartal das Quartal
	 * @param threshold     der Schwellwert (z.B. 3), der mindestens erreicht sein muss, damit die
	 *                  Schüler-IDs in die Rückgabe-Map aufgenommen werden
	 * @param thresholdMinus der Schwellwert (z.B. 4), dessen Menge von der Threshold-Menge abgezogen wird, damit Warnungen nicht die Fehler enthalten, bei -1 wird nichts abgezogen
	 *
	 * @return die Map Schüler-ID -> {@link GostSchuelerklausurTermin}menge, die Schüler-IDs von Schülern enthalten, die in der den Termin
	 * enthaltenen Kalenderwoche mehr (>=) Klausuren schreiben, als der Schwellwert definiert und die betreffenden {@link GostSchuelerklausurTermin}e.
	 */
	public klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(abijahr : number, halbjahr : GostHalbjahr, quartal : number, threshold : number, thresholdMinus : number) : List<PairNN<PairNN<number, number>, List<GostSchuelerklausurTermin>>> {
		const schuelerklausurterminaktuellmenge_by_schuelerId : JavaMap<number, JavaMap<number, List<GostSchuelerklausurTermin>>> | null = this._schuelerklausurterminaktuellmenge_by_abijahr_and_kw_and_schuelerId.getMap2OrNull(abijahr);
		const ergebnis : List<PairNN<PairNN<number, number>, List<GostSchuelerklausurTermin>>> = new ArrayList<PairNN<PairNN<number, number>, List<GostSchuelerklausurTermin>>>();
		if (schuelerklausurterminaktuellmenge_by_schuelerId === null)
			return ergebnis;
		for (const kwEntry of schuelerklausurterminaktuellmenge_by_schuelerId.entrySet()) {
			for (const schuelerEntry of kwEntry.getValue().entrySet()) {
				if ((schuelerEntry.getValue().size() >= threshold) && ((thresholdMinus < 0) || (schuelerEntry.getValue().size() < thresholdMinus)))
					for (const skt of schuelerEntry.getValue()) {
						const vorgabe : GostKlausurvorgabe = this.vorgabeBySchuelerklausurTermin(skt);
						if (vorgabe.abiJahrgang === abijahr && vorgabe.halbjahr === halbjahr.id && (quartal === 0 || vorgabe.quartal === quartal) && !(vorgabe.halbjahr === 5 && vorgabe.quartal === 2)) {
							ergebnis.add(new PairNN<PairNN<number, number>, List<GostSchuelerklausurTermin>>(new PairNN(kwEntry.getKey(), schuelerEntry.getKey()), schuelerEntry.getValue()));
							break;
						}
					}
			}
		}
		return ergebnis;
	}

	/**
	 * Liefert für eine Liste von {@link GostSchuelerklausur}en die zugehörigen
	 * Schüler-IDs als Liste.
	 *
	 * @param sks die Liste von {@link GostSchuelerklausur}en
	 *
	 * @return die Liste der Schüler-IDs
	 */
	public getSchuelerIDsFromSchuelerklausurterminen(sks : List<GostSchuelerklausurTermin>) : List<number> {
		const ids : List<number> = new ArrayList<number>();
		for (const sk of sks) {
			ids.add(this.schuelerklausurBySchuelerklausurtermin(sk).idSchueler);
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
	public getSchuelerIDsFromSchuelerklausuren(sks : List<GostSchuelerklausur>) : List<number> {
		const ids : List<number> = new ArrayList<number>();
		for (const sk of sks) {
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
	public getSchuelerIDsFromKursklausur(kk : GostKursklausur) : List<number> {
		return this.getSchuelerIDsFromSchuelerklausuren(this.schuelerklausurGetMengeByKursklausur(kk));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostKursklausur}, sonst <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin} oder <code>null</code>
	 */
	public terminOrNullByKursklausur(klausur : GostKursklausur) : GostKlausurtermin | null {
		return this._termin_by_id.get(klausur.idTermin);
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostKursklausur}. Wenn noch kein Termin bestimmt ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public terminOrExceptionByKursklausur(klausur : GostKursklausur) : GostKlausurtermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._termin_by_id, DeveloperNotificationException.ifNull(JavaString.format("idTermin von Klausur %d darf nicht NULL sein", klausur.id), klausur.idTermin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurTermin} oder <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public terminOrNullBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKlausurtermin | null {
		if (termin.folgeNr > 0)
			return (termin.idTermin === null) ? null : this.terminGetByIdOrException(termin.idTermin);
		return this.terminOrNullByKursklausur(this.kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einem {@link GostSchuelerklausurTermin}. Wenn noch kein Termin bestimmt ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu dem der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public terminOrExceptionBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKlausurtermin {
		if (termin.folgeNr > 0) {
			return this.terminGetByIdOrException(DeveloperNotificationException.ifNull(JavaString.format("idTermin von Termin %d", termin.id), termin.idTermin));
		}
		return this.terminOrExceptionByKursklausur(this.kursklausurBySchuelerklausurTermin(termin));
	}

	/**
	 * Liefert den {@link GostKlausurtermin} zu einer {@link GostSchuelerklausur} oder <code>null</code>, wenn noch kein Termin bestimmt wurde.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der der Termin gesucht wird.
	 *
	 * @return den {@link GostKlausurtermin}
	 */
	public terminKursklausurBySchuelerklausur(sk : GostSchuelerklausur) : GostKlausurtermin | null {
		return this.terminOrNullByKursklausur(this.kursklausurBySchuelerklausur(sk));
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einer {@link GostKursklausur}.
	 *
	 * @param klausur die {@link GostKursklausur}, zu der die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public vorgabeByKursklausur(klausur : GostKursklausur) : GostKlausurvorgabe {
		return this.vorgabeGetByIdOrException(klausur.idVorgabe);
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einer {@link GostSchuelerklausur}.
	 *
	 * @param klausur die {@link GostSchuelerklausur}, zu der die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public vorgabeBySchuelerklausur(klausur : GostSchuelerklausur) : GostKlausurvorgabe {
		const kk : GostKursklausur = this.kursklausurGetByIdOrException(klausur.idKursklausur);
		return this.vorgabeGetByIdOrException(kk.idVorgabe);
	}

	/**
	 * Liefert die {@link GostKlausurvorgabe} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurTermin}, zu dem die Vorgabe gesucht wird.
	 *
	 * @return die {@link GostKlausurvorgabe}
	 */
	public vorgabeBySchuelerklausurTermin(klausur : GostSchuelerklausurTermin) : GostKlausurvorgabe {
		return this.vorgabeBySchuelerklausur(this.schuelerklausurGetByIdOrException(klausur.idSchuelerklausur));
	}

	/**
	 * Liefert die {@link GostSchuelerklausur} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param klausur der {@link GostSchuelerklausurTermin}, zu der die {@link GostSchuelerklausur} gesucht wird.
	 *
	 * @return die {@link GostSchuelerklausur}
	 */
	public schuelerklausurBySchuelerklausurtermin(klausur : GostSchuelerklausurTermin) : GostSchuelerklausur {
		return this.schuelerklausurGetByIdOrException(klausur.idSchuelerklausur);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einer {@link GostSchuelerklausur}.
	 *
	 * @param klausur die {@link GostSchuelerklausur}, zu der die {@link GostKursklausur} gesucht wird.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public kursklausurBySchuelerklausur(klausur : GostSchuelerklausur) : GostKursklausur {
		return this.kursklausurGetByIdOrException(klausur.idKursklausur);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einer {@link GostKlausurvorgabe} und einer Kurs-ID.
	 *
	 * @param vorgabe die {@link GostKlausurvorgabe}, zu der die {@link GostKursklausur} gesucht wird.
	 * @param idKurs die ID des Kurses der {@link GostKursklausur}.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public kursklausurByVorgabeAndKursid(vorgabe : GostKlausurvorgabe, idKurs : number) : GostKursklausur | null {
		return this._kursklausur_by_idVorgabe_and_idKurs.getSingle12OrNull(vorgabe.id, idKurs);
	}

	/**
	 * Liefert die {@link GostKursklausur} zu einem {@link GostSchuelerklausurTermin}.
	 *
	 * @param termin der {@link GostSchuelerklausurTermin}, zu der die {@link GostKursklausur} gesucht wird.
	 *
	 * @return die {@link GostKursklausur}
	 */
	public kursklausurBySchuelerklausurTermin(termin : GostSchuelerklausurTermin) : GostKursklausur {
		return this.kursklausurBySchuelerklausur(this.schuelerklausurGetByIdOrException(termin.idSchuelerklausur));
	}

	/**
	 * Liefert zurück, ob die übergebene {@link GostKlausurvorgabe} von einer {@link GostKursklausur}
	 * verwendet wird.
	 *
	 * @param vorgabe die {@link GostKlausurvorgabe}, die auf Verwendung geprüft werden soll.
	 *
	 * @return <code>true</code>, falls die {@link GostKlausurvorgabe} verwendet wird, sonst <code>false</code>
	 */
	public istVorgabeVerwendetByKursklausur(vorgabe : GostKlausurvorgabe) : boolean {
		const klausuren : List<GostKursklausur> = this._kursklausur_by_idVorgabe_and_idKurs.get1(vorgabe.id);
		return !klausuren.isEmpty();
	}

	/**
	 * Liefert die Vorgänger-{@link GostKursklausur} aus dem letzten Quartal, wenn eine solche existiert, sonst <code>null</code>.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Vorgänger gesucht wird
	 *
	 * @return die {@link GostKursklausur} oder <code>null</code>
	 */
	public kursklausurVorterminByKursklausur(klausur : GostKursklausur) : GostKursklausur | null {
		const previousVorgabe : GostKlausurvorgabe | null = this.vorgabeGetPrevious(this.vorgabeGetByIdOrException(klausur.idVorgabe));
		if (previousVorgabe === null)
			return null;
		if (!this._kursklausur_by_idVorgabe_and_idKurs.containsKey1(previousVorgabe.id))
			return null;
		const klausuren : List<GostKursklausur> = this._kursklausur_by_idVorgabe_and_idKurs.get1(previousVorgabe.id);
		for (const k of klausuren) {
			const kKurs : KursDaten | null = this.getKursManager().get(k.idKurs);
			const klausurKurs : KursDaten | null = this.getKursManager().get(klausur.idKurs);
			if ((kKurs === null) || (klausurKurs === null))
				throw new DeveloperNotificationException("Keine Kurszuordnung im kursManager zu Kurs-ID")
			if (JavaObject.equalsTranspiler(kKurs.kuerzel, (klausurKurs.kuerzel)))
				return k;
		}
		return null;
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurTermin}s aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit der {@link GostKursklausur} zurückgegeben, sonst die des {@link GostKlausurtermin}s. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird <code>null</code>
	 * zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, dessen Startzeit gesucht wird.
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurTermin}s oder <code>null</code>
	 */
	public startzeitBySchuelerklausurterminOrNull(skt : GostSchuelerklausurTermin) : number | null {
		return skt.startzeit !== null ? skt.startzeit : this.startzeitByKursklausurOrNull(this.kursklausurBySchuelerklausurTermin(skt));
	}

	/**
	 * Gibt die Startzeit des übergebenen {@link GostSchuelerklausurTermin}s aus. Falls keine individuelle Zeit
	 * gesetzt ist, wird die Zeit der {@link GostKursklausur} zurückgegeben, sonst die des {@link GostKlausurtermin}s. Sollte kein {@link GostKlausurtermin} gesetzt
	 * sein oder der {@link GostKlausurtermin} keine Startzeit definiert haben, wird eine <code>DeveloperNotificationException</code> geworfen.
	 * zurückgegeben.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, dessen Startzeit gesucht wird.
	 *
	 * @return die Startzeit des {@link GostSchuelerklausurTermin}s
	 */
	public startzeitBySchuelerklausurterminOrException(skt : GostSchuelerklausurTermin) : number {
		if (skt.startzeit !== null)
			return skt.startzeit;
		else
			if (skt.folgeNr === 0)
				return this.startzeitByKursklausurOrException(this.kursklausurBySchuelerklausurTermin(skt));
			else {
				const idTermin : number = DeveloperNotificationException.ifNull(JavaString.format("idTermin von SchülerklausurTermin %d", skt.id), skt.idTermin).valueOf();
				return DeveloperNotificationException.ifNull(JavaString.format("startzeit von Termin %d", idTermin), this.terminGetByIdOrException(idTermin).startzeit);
			}
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
	public startzeitByKursklausurOrNull(klausur : GostKursklausur) : number | null {
		if (klausur.startzeit !== null)
			return klausur.startzeit;
		const termin : GostKlausurtermin | null = this.terminOrNullByKursklausur(klausur);
		return (termin === null) ? null : termin.startzeit;
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
	public startzeitByKursklausurOrException(klausur : GostKursklausur) : number {
		return (klausur.startzeit !== null) ? klausur.startzeit : DeveloperNotificationException.ifNull(JavaString.format("Startzeit des Termins %d", this.terminOrExceptionByKursklausur(klausur).id), this.terminOrExceptionByKursklausur(klausur).startzeit);
	}

	/**
	 * Prüft, ob die übergebene {@link GostKursklausur} eine vom zugewiesenen {@link GostKlausurtermin} abweichende Startzeit hat. Ist der {@link GostKursklausur} noch kein {@link GostKlausurtermin} zugewiesen oder dem Termin noch keine Startzeit zugewiesen, wird <code>false</code> zurückgegeben.
	 *
	 * @param klausur die {@link GostKursklausur}, deren Startzeit geprüft wird.
	 *
	 * @return <code>true</code>, wenn die {@link GostKursklausur} eine vom {@link GostKlausurtermin} abweichende Startzeit aufweist.
	 */
	public hatAbweichendeStartzeitByKursklausur(klausur : GostKursklausur) : boolean {
		const termin : GostKlausurtermin | null = this.terminOrNullByKursklausur(klausur);
		return !((klausur.startzeit === null) || (termin === null) || (termin.startzeit === null) || JavaObject.equalsTranspiler(termin.startzeit, (klausur.startzeit)));
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einer {@link GostSchuelerklausur} zurück.
	 *
	 * @param sk die {@link GostSchuelerklausur}, zu der die {@link GostSchuelerklausurTermin}e gesucht werden.
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en
	 */
	public schuelerklausurterminGetMengeBySchuelerklausur(sk : GostSchuelerklausur) : List<GostSchuelerklausurTermin> {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminmenge_by_idSchuelerklausur, sk.id);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en
	 */
	public schuelerklausurterminGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		const list : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(termin.id);
		return (list !== null) ? list : new ArrayList();
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. werden Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code>, werden Fremdtermine am selben Datum wie <code>termin</code> aus anderen Jahrgangsstufen inkludiert.
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück. Ggf. sind Fremdtermine am selben Datum aus anderen Jahrgangsstufen inkludiert.
	 */
	public schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(termin : GostKlausurtermin, fremdTermine : boolean) : List<GostSchuelerklausurTermin> {
		return fremdTermine ? this.schuelerklausurterminaktuellGetMengeByTerminmenge(this.terminGetMengeByDatum(DeveloperNotificationException.ifNull("Termin muss ein Datum haben", termin.datum))) : this.schuelerklausurterminAktuellGetMengeByTermin(termin);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einer Menge von {@link GostKlausurtermin}en zurück.
	 *
	 * @param termine die Liste der {@link GostKlausurtermin}e
	 *
	 * @return die Liste von zugehörigen {@link GostSchuelerklausurTermin}en
	 */
	public schuelerklausurterminGetMengeByTerminmenge(termine : List<GostKlausurtermin>) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const termin of termine) {
			const teilListe : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(termin.id);
			if (teilListe !== null)
				ergebnis.addAll(teilListe);
		}
		return ergebnis;
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausurTermin}en zu einer Menge von {@link GostKlausurtermin}en zurück.
	 *
	 * @param termine die Liste der {@link GostKlausurtermin}e
	 *
	 * @return die Liste von zugehörigen {@link GostSchuelerklausurTermin}en
	 */
	public schuelerklausurterminaktuellGetMengeByTerminmenge(termine : List<GostKlausurtermin>) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const termin of termine)
			ergebnis.addAll(this.schuelerklausurterminAktuellGetMengeByTermin(termin));
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
	public schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(termin : GostKlausurtermin, kursklausur : GostKursklausur) : List<GostSchuelerklausurTermin> {
		return this.schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(termin, kursklausur, false);
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück. Ggf. werden auch die jahrgangsübergreifenden datumsgleichen {@link GostKlausurtermin}e berücksichtigt.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 * @param kursklausur die {@link GostKursklausur}
	 * @param multijahrgang wenn <code>true</code>, werden auch jahrgangsübergreifende datumsgleiche {@link GostKlausurtermin}e berücksichtigt.
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} und einer
	 * {@link GostKursklausur} zurück.
	 */
	public schuelerklausurterminAktuellGetMengeByTerminAndKursklausurMultijahrgang(termin : GostKlausurtermin, kursklausur : GostKursklausur, multijahrgang : boolean) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> | null = new ArrayList<GostSchuelerklausurTermin>();
		ergebnis.addAll(this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(termin.id, kursklausur.id));
		if (termin.datum !== null)
			for (const terminMulti of this.terminSelbesDatumGetMengeByTermin(termin, false))
				ergebnis.addAll(this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(terminMulti.id, kursklausur.id));
		return ergebnis;
	}

	/**
	 * Gibt die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 *
	 * @param termin      der {@link GostKlausurtermin}
	 *
	 * @return die Liste der aktuellen (ohne abwesend gemeldete) {@link GostSchuelerklausurTermin}en zu einem {@link GostKlausurtermin} zurück.
	 */
	public schuelerklausurterminAktuellGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		return this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(termin.id);
	}

	/**
	 * Gibt die Liste von {@link GostSchuelerklausur}en zu einem Klausurtermin zurück.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostSchuelerklausur}en gesucht werden
	 *
	 * @return die Liste von {@link GostSchuelerklausur}en zu einem Klausurtermin
	 */
	public schuelerklausurGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausur> {
		const ergebnis : List<GostSchuelerklausur> | null = new ArrayList<GostSchuelerklausur>();
		const list : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (list === null)
			return ergebnis;
		for (const t of list)
			ergebnis.add(this.schuelerklausurBySchuelerklausurtermin(t));
		return ergebnis;
	}

	/**
	 * Prüft, ob der übergebene {@link GostSchuelerklausurTermin} der aktuellste Termin der zugehörigen {@link GostSchuelerklausur} ist.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, der auf Aktualität geprüft werden soll
	 *
	 * @return <code>true</code>, wenn es sich um den aktuellsten {@link GostSchuelerklausurTermin} handelt, sonst <code>false</code>
	 */
	public istSchuelerklausurterminAktuell(skt : GostSchuelerklausurTermin) : boolean {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminaktuell_by_idSchuelerklausur, skt.idSchuelerklausur) as unknown === skt as unknown;
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
	public schuelerklausurterminAktuellBySchuelerklausur(schuelerklausur : GostSchuelerklausur) : GostSchuelerklausurTermin {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminaktuell_by_idSchuelerklausur, schuelerklausur.id);
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
	public schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		let ergebnis : List<GostSchuelerklausurTermin>;
		if (quartal > 0) {
			ergebnis = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get123(abiJahrgang, halbjahr.id, quartal);
			let iterator : JavaIterator<GostSchuelerklausurTermin> | null = ergebnis.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().idTermin === -1) {
					iterator.remove();
				}
			}
		} else {
			ergebnis = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get12(abiJahrgang, halbjahr.id);
			let iterator : JavaIterator<GostSchuelerklausurTermin> | null = ergebnis.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().idTermin === -1) {
					iterator.remove();
				}
			}
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
	public schuelerklausurterminNtAktuellGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		if (quartal > 0)
			return this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get123(abiJahrgang, halbjahr.id, quartal);
		return this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get12(abiJahrgang, halbjahr.id);
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
	public schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const termin of this.schuelerklausurterminNtAktuellMitTerminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal)) {
			const t : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(termin);
			if ((t !== null) && (t.datum !== null))
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
	public schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausurTermin> {
		if (quartal > 0) {
			const skts : List<GostSchuelerklausurTermin> = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get1234(abiJahrgang, halbjahr.id, quartal, -1);
			skts.sort(this._compSchuelerklausurTermin);
			return skts;
		}
		const skts : List<GostSchuelerklausurTermin> = this._schuelerklausurterminntaktuellmenge_by_abijahr_and_halbjahr_and_quartal_and_idTermin.get124(abiJahrgang, halbjahr.id, -1);
		skts.sort(this._compSchuelerklausurTermin);
		return skts;
	}

	/**
	 * Liefert eine Liste von Haupttermin-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von Haupttermin-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public schuelerklausurterminAktuellHtGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminAktuellGetMengeByTermin(termin))
			if (skt.folgeNr === 0)
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public schuelerklausurterminNtGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminGetMengeByTermin(termin))
			if (skt.folgeNr > 0)
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Liste von aktuellen Nachschreib-{@link GostSchuelerklausurTermin}en zum übergebenen {@link GostKlausurtermin}
	 */
	public schuelerklausurterminAktuellNtGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminAktuellGetMengeByTermin(termin))
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
	public schuelerklausurterminByTerminAndSchuelerid(termin : GostKlausurtermin, idSchueler : number) : GostSchuelerklausurTermin | null {
		const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts !== null)
			for (const skt of skts)
				if (this.schuelerklausurGetByIdOrException(skt.idSchuelerklausur).idSchueler === idSchueler)
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
	public schuelerklausurGetMengeByKursklausur(kursklausur : GostKursklausur) : List<GostSchuelerklausur> {
		return this._schuelerklausur_by_idKursklausur_and_idSchueler.get1(kursklausur.id);
	}

	/**
	 * Liefert die {@link GostSchuelerklausur} zur übergebenen {@link GostKursklausur} und zur Schüler-ID
	 *
	 * @param kursklausur die {@link GostKursklausur}
	 * @param idSchueler die ID des Schülers
	 *
	 * @return die {@link GostSchuelerklausur} zur übergebenen {@link GostKursklausur} und zur Schüler-ID
	 */
	public schuelerklausurByKursklausurAndSchuelerid(kursklausur : GostKursklausur, idSchueler : number) : GostSchuelerklausur | null {
		return this._schuelerklausur_by_idKursklausur_and_idSchueler.getSingle12OrNull(kursklausur.id, idSchueler);
	}

	/**
	 * Liefert den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return den {@link LehrerListeEintrag} zur übergebenen {@link GostKursklausur} oder <code>null</code> falls kein Lehrer zugeordnet ist.
	 */
	public kursLehrerByKursklausur(k : GostKursklausur) : LehrerListeEintrag | null {
		const kurs : KursDaten = this.kursdatenByKursklausur(k);
		return kurs.lehrer === null ? null : this.getLehrerMap().get(kurs.lehrer);
	}

	/**
	 * Liefert das Lehrerkürzel zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das Lehrerkürzel zur übergebenen {@link GostKursklausur} oder <code>null</code> falls kein Lehrer zugeordnet ist.
	 */
	public kursLehrerKuerzelByKursklausur(k : GostKursklausur) : string | null {
		const lle : LehrerListeEintrag | null = this.kursLehrerByKursklausur(k);
		return lle === null ? null : lle.kuerzel;
	}

	/**
	 * Liefert die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die {@link KursDaten} zur übergebenen {@link GostKursklausur}.
	 */
	public kursdatenByKursklausur(k : GostKursklausur) : KursDaten {
		const kurs : KursDaten | null = this.getKursManager().get(k.idKurs);
		return DeveloperNotificationException.ifNull("Kurs mit ID " + k.idKurs + " nicht in KursManager vorhanden.", kurs);
	}

	/**
	 * Liefert das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return das {@link GostFach} zur übergebenen {@link GostKursklausur}.
	 */
	public fachByKursklausur(k : GostKursklausur) : GostFach {
		const vorgabe : GostKlausurvorgabe | null = this.vorgabeByKursklausur(k);
		return DeveloperNotificationException.ifNull("Fach mit ID " + this.vorgabeByKursklausur(k).idFach + " nicht in GostFaecherManager vorhanden.", this.getFaecherManager(vorgabe.abiJahrgang).get(vorgabe.idFach));
	}

	/**
	 * Liefert die Liste der Kursschienen des Kurses einer {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die Liste der Kursschienen des Kurses einer {@link GostKursklausur}.
	 */
	public kursSchieneByKursklausur(k : GostKursklausur) : List<number> {
		return this.kursdatenByKursklausur(k).schienen;
	}

	/**
	 * Liefert die Kurzbezeichnung des Kurses zu einer übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die Kurzbezeichnung des Kurses zu einer übergebenen {@link GostKursklausur}.
	 */
	public kursKurzbezeichnungByKursklausur(k : GostKursklausur) : string {
		return this.kursdatenByKursklausur(k).kuerzel;
	}

	/**
	 * Liefert die {@link KursDaten} zur übergebenen {@link GostSchuelerklausur}.
	 *
	 * @param k die {@link GostSchuelerklausur}
	 *
	 * @return die {@link KursDaten} zur übergebenen {@link GostSchuelerklausur}.
	 */
	public kursdatenBySchuelerklausur(k : GostSchuelerklausur) : KursDaten {
		return this.kursdatenByKursklausur(this.kursklausurBySchuelerklausur(k));
	}

	/**
	 * Liefert die {@link KursDaten} zum übergebenen {@link GostSchuelerklausurTermin}.
	 *
	 * @param k die {@link GostKursklausur}
	 *
	 * @return die {@link KursDaten} zum übergebenen {@link GostSchuelerklausurTermin}.
	 */
	public kursdatenBySchuelerklausurTermin(k : GostSchuelerklausurTermin) : KursDaten {
		return this.kursdatenByKursklausur(this.kursklausurBySchuelerklausurTermin(k));
	}

	/**
	 * Liefert die Anzahl aller Schüler im Kurs zu einer übergebenen {@link GostKursklausur}.
	 *
	 * @param k die {@link GostKursklausur}.
	 *
	 * @return die Anzahl aller Schüler im Kurs zu einer übergebenen {@link GostKursklausur}.
	 */
	public kursAnzahlSchuelerGesamtByKursklausur(k : GostKursklausur) : number {
		return this.kursdatenByKursklausur(k).schueler.size();
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
	public kursAnzahlKlausurschreiberByKursklausur(k : GostKursklausur) : number {
		return this.schuelerklausurGetMengeByKursklausur(k).size();
	}

	/**
	 * Gibt die HTML-Farbe des zulässigen Faches zur übergebenen {@link GostKursklausur} als Aufruf der rgba-Funktion
	 * mit der Transparenz 1.0 zurück.
	 *
	 * @param k           die {@link GostKursklausur}
	 *
	 * @return die RGBA-HTML-Farbdefinition als String
	 */
	public fachHTMLFarbeRgbaByKursklausur(k : GostKursklausur) : string {
		const vorgabe : GostKlausurvorgabe | null = this.vorgabeByKursklausur(k);
		return Fach.getBySchluesselOrDefault(this.fachByKursklausur(k).kuerzel).getHMTLFarbeRGBA(vorgabe.abiJahrgang - 1, 1.0);
	}

	/**
	 * Liefert den Vorgänger-{@link GostSchuelerklausurTermin}, sofern vorhanden, zu einem {@link GostSchuelerklausurTermin}, also den
	 * versäumte Schülerklausurtermin.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, dessen Vorgänger gesucht wird
	 *
	 * @return den Vorgänger-{@link GostSchuelerklausurTermin} oder <code>null</code>
	 */
	public schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt : GostSchuelerklausurTermin) : GostSchuelerklausurTermin | null {
		const alleTermine : List<GostSchuelerklausurTermin> = DeveloperNotificationException.ifMapGetIsNull(this._schuelerklausurterminmenge_by_idSchuelerklausur, skt.idSchuelerklausur);
		for (const skAktuell of alleTermine)
			if (skAktuell.folgeNr === (skt.folgeNr - 1))
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
	public kursklausurMitExternenS(k : GostKursklausur) : boolean {
		const listSks : List<GostSchuelerklausur> = this.schuelerklausurGetMengeByKursklausur(k);
		for (const sk of listSks)
			if (DeveloperNotificationException.ifMapGetIsNull(this._schuelerlisteeintrag_by_id, sk.idSchueler).externeSchulNr !== null)
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
	public terminMitAnderenJgst(t : GostKlausurtermin) : boolean {
		const listSkts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(t.id);
		if (listSkts !== null)
			for (const skt of listSkts)
				if (this.vorgabeBySchuelerklausurTermin(skt).abiJahrgang !== t.abijahr)
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
	public datumSchuelerklausurVorgaenger(skt : GostSchuelerklausurTermin) : string | null {
		const vorgaengerSkt : GostSchuelerklausurTermin = DeveloperNotificationException.ifNull("Kein Vorgängertermin zu Schülerklausurtermin gefunden.", this.schuelerklausurterminVorgaengerBySchuelerklausurtermin(skt));
		const termin : GostKlausurtermin | null = this.terminOrNullBySchuelerklausurTermin(vorgaengerSkt);
		return (termin === null) ? null : termin.datum;
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
	public patchKlausurterminNachschreiberZuglassenFalse(termin : GostKlausurtermin) : GostKlausurenUpdate {
		const update : GostKlausurenUpdate | null = new GostKlausurenUpdate();
		update.listKlausurtermineNachschreiberZugelassenFalse.add(termin.id);
		for (const skt of this.schuelerklausurterminNtGetMengeByTermin(termin))
			update.listSchuelerklausurTermineRemoveIdTermin.add(skt.id);
		return update;
	}

	/**
	 * Führt alle Attribut-Patches aller Objekte im übergeben {@link GostKlausurenUpdate} im
	 * Manager durch.
	 *
	 * @param update das {@link GostKlausurenUpdate}-Objekt mit den zu patchenden Werten
	 */
	public updateExecute(update : GostKlausurenUpdate) : void {
		for (const sktId of update.listSchuelerklausurTermineRemoveIdTermin) {
			const skt : GostSchuelerklausurTermin = this.schuelerklausurterminGetByIdOrException(sktId);
			skt.idTermin = null;
			this.schuelerklausurterminPatchAttributes(skt);
		}
		for (const ktId of update.listKlausurtermineNachschreiberZugelassenFalse) {
			const kt : GostKlausurtermin = this.terminGetByIdOrException(ktId);
			kt.nachschreiberZugelassen = false;
			this.terminPatchAttributes(kt);
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
	public schuelerSchreibtKlausurtermin(idSchueler : number, termin : GostKlausurtermin) : boolean {
		const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idTermin.get(termin.id);
		if (skts === null)
			return false;
		for (const skt of skts)
			if (this.schuelerklausurBySchuelerklausurtermin(skt).idSchueler === idSchueler && this.istSchuelerklausurterminAktuell(skt))
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
	public schuelerklausurterminGetMengeByKursklausur(kursklausur : GostKursklausur) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminmenge_by_idKursklausur.get(kursklausur.id);
		if (ergebnis === null)
			return new ArrayList();
		ergebnis.sort(this._compSchuelerklausurTermin);
		return ergebnis;
	}

	/**
	 * Liefert zu einer {@link GostSchuelerklausur} den zugehörigen {@link SchuelerListeEintrag}
	 *
	 * @param sk die {@link GostSchuelerklausur}
	 *
	 * @return der zugehörige {@link SchuelerListeEintrag}
	 */
	public schuelerGetBySchuelerklausur(sk : GostSchuelerklausur) : SchuelerListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerlisteeintrag_by_id, sk.idSchueler);
	}

	/**
	 * Liefert zu einem {@link GostSchuelerklausurTermin} den zugehörigen {@link SchuelerListeEintrag}
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}
	 *
	 * @return der zugehörige {@link SchuelerListeEintrag}
	 */
	public schuelerGetBySchuelerklausurtermin(skt : GostSchuelerklausurTermin) : SchuelerListeEintrag {
		return this.schuelerGetBySchuelerklausur(this.schuelerklausurBySchuelerklausurtermin(skt));
	}

	/**
	 * Liefert die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 *
	 * @param raum       der {@link GostKlausurraum}
	 * @param zeitraster das {@link StundenplanZeitraster}
	 *
	 * @return die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 */
	public raumstundeGetByRaumAndZeitrasterOrNull(raum : GostKlausurraum, zeitraster : StundenplanZeitraster) : GostKlausurraumstunde | null {
		return this._raumstunde_by_idRaum_and_idZeitraster.getSingle12OrNull(raum.id, zeitraster.id);
	}

	/**
	 * Liefert die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 *
	 * @param raum       der {@link GostKlausurraum}
	 * @param zeitraster das {@link StundenplanZeitraster}
	 *
	 * @return die {@link GostKlausurraumstunde} zum übergebenen {@link GostKlausurraum} und {@link StundenplanZeitraster} zurück.
	 */
	public raumstundeGetByRaumAndZeitrasterOrException(raum : GostKlausurraum, zeitraster : StundenplanZeitraster) : GostKlausurraumstunde {
		return this._raumstunde_by_idRaum_and_idZeitraster.getSingle12OrException(raum.id, zeitraster.id);
	}

	/**
	 * Liefert die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum} zurück.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return die Menge von {@link GostKlausurraumstunde}en zum übergebenen {@link GostKlausurraum}
	 */
	public raumstundeGetMengeByRaum(raum : GostKlausurraum) : List<GostKlausurraumstunde> {
		const stunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idRaum.get(raum.id);
		return (stunden !== null) ? stunden : new ArrayList();
	}

	private setzeRaumZuSchuelerklausurenOhneUpdate(collectionSkrsKrs : GostKlausurenCollectionSkrsKrsData) : void {
		this.raumRemoveAllIfExistsNoCascadeOhneUpdate(collectionSkrsKrs.raumdata.raeume);
		this.schuelerklausurraumstundeRemoveAllOhneUpdate(collectionSkrsKrs.schuelerklausurterminraumstundenGeloescht);
		this.raumstundeRemoveAllOhneUpdate(collectionSkrsKrs.raumstundenGeloescht);
		this.raumAddAllOhneUpdate(collectionSkrsKrs.raumdata.raeume);
		this.raumstundeAddAllOhneUpdate(collectionSkrsKrs.raumdata.raumstunden);
		this.schuelerklausurraumstundeAddAllOhneUpdate(collectionSkrsKrs.raumdata.sktRaumstunden);
	}

	/**
	 * Aktualisiert die internen Strukturen, nachdem sich anhand der übergebenen {@link GostKlausurenCollectionSkrsKrsData}. Diese Methode sollte nur nach einem API-Call aufgerufen werden, in dem das {@link GostKlausurenCollectionSkrsKrsData}-Objekt erzeugt wurde.
	 *
	 * @param collectionSkrsKrs die {@link GostKlausurenCollectionSkrsKrsData}
	 */
	public setzeRaumZuSchuelerklausuren(collectionSkrsKrs : GostKlausurenCollectionSkrsKrsData) : void {
		this.setzeRaumZuSchuelerklausurenOhneUpdate(collectionSkrsKrs);
		this.update_all();
	}

	/**
	 * Liefert die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 * @param includeNachschreiber <code>true</code>, wenn auch Nachschreiber berücksichtigt werden sollen
	 *
	 * @return die Menge aller {@link GostKursklausur}en zurück, die in einem {@link GostKlausurraum} geschrieben werden, auch wenn die {@link GostKursklausur} nur nachgeschrieben wird.
	 */
	public kursklausurGetMengeByRaum(raum : GostKlausurraum, includeNachschreiber : boolean) : JavaSet<GostKursklausur> {
		const kursklausuren : JavaSet<GostKursklausur> | null = new HashSet<GostKursklausur>();
		if (!this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.containsKey1(raum.id))
			return kursklausuren;
		for (const skt of this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get1(raum.id))
			if (skt.folgeNr === 0 || includeNachschreiber)
				kursklausuren.add(this.kursklausurBySchuelerklausurTermin(skt));
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
	public schuelerklausurterminGetMengeByRaumAndKursklausur(raum : GostKlausurraum, kursklausur : GostKursklausur) : List<GostSchuelerklausurTermin> {
		return this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get12OrException(raum.id, kursklausur.id);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public schuelerklausurterminGetMengeByRaum(raum : GostKlausurraum) : List<GostSchuelerklausurTermin> {
		return this.schuelerklausurterminGetMengeByRaumid(raum.id);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param idRaum die ID des {@link GostKlausurraum}s
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausurTermin}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public schuelerklausurterminGetMengeByRaumid(idRaum : number) : List<GostSchuelerklausurTermin> {
		return this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.get1(idRaum);
	}

	/**
	 * Liefert die Menge aller aktueller {@link GostSchuelerklausur}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 *
	 * @param raum  der {@link GostKlausurraum}
	 *
	 * @return die Menge aller aktueller {@link GostSchuelerklausur}e zurück, die in einem {@link GostKlausurraum} geschrieben werden.
	 */
	public schuelerklausurGetMengeByRaum(raum : GostKlausurraum) : List<GostSchuelerklausur> {
		const schuelerklausuren : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();
		const schuelerklausurtermine : List<GostSchuelerklausurTermin> = this.schuelerklausurterminGetMengeByRaum(raum);
		for (const skt of schuelerklausurtermine)
			schuelerklausuren.add(this.schuelerklausurBySchuelerklausurtermin(skt));
		return schuelerklausuren;
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurTermin}e zu einem {@link GostKlausurtermin} zurück, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die die Menge aller {@link GostSchuelerklausurTermin}e  zu einem {@link GostKlausurtermin}, die noch keinem {@link GostKlausurraum} zugewiesen sind.
	 */
	public schuelerklausurOhneRaumGetMengeByTermin(termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		return this._schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.get12(-1, termin.id);
	}

	/**
	 * Liefert die Menge aller {@link GostSchuelerklausurTermin}e zu einem {@link GostKlausurraum} und {@link GostKlausurtermin} zurück.
	 *
	 * @param raum der {@link GostKlausurraum}
	 * @param termin der {@link GostKlausurtermin}
	 *
	 * @return die Menge aller {@link GostSchuelerklausurTermin}e zu einem {@link GostKlausurraum} und {@link GostKlausurtermin} zurück.
	 */
	public schuelerklausurterminGetMengeByRaumAndTermin(raum : GostKlausurraum, termin : GostKlausurtermin) : List<GostSchuelerklausurTermin> {
		return this._schuelerklausurterminaktuellmenge_by_idRaum_and_idTermin.get12(raum.id, termin.id);
	}

	/**
	 * Liefert eine Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 *
	 * @param termin der {@link GostKlausurtermin}
	 * @param multijahrgang ob die Liste für einen Termin oder für alle Termine des gleichen Datums gelten soll
	 *
	 * @return die Liste von {@link StundenplanRaum}en, die nicht für den übergebenen Klausurtermin verplant sind.
	 */
	public stundenplanraumVerfuegbarGetMengeByTermin(termin : GostKlausurtermin, multijahrgang : boolean) : List<StundenplanRaum> {
		const raeume : List<StundenplanRaum> | null = new ArrayList<StundenplanRaum>();
		const termine : List<GostKlausurtermin> = multijahrgang ? this.terminSelbesDatumGetMengeByTermin(termin, true) : ListUtils.create1(termin);
		for (const raum of this.stundenplanManagerGetByTerminOrException(termin).raumGetMengeAsList()) {
			let raumVerwendet : boolean = false;
			for (const t of termine)
				if (this._raum_by_idTermin_and_idStundenplanraum.containsKey12(t.id, raum.id)) {
					raumVerwendet = true;
					break;
				}
			if (!raumVerwendet)
				raeume.add(raum);
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
	public raumGetByTerminUndStundenplanraum(termin : GostKlausurtermin, stundenplanRaum : StundenplanRaum) : GostKlausurraum | null {
		return this._raum_by_idTermin_and_idStundenplanraum.getSingle12OrNull(termin.id, stundenplanRaum.id);
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
	public isKursklausurAlleSchuelerklausurenVerplant(kk : GostKursklausur, termin : GostKlausurtermin | null) : boolean {
		const idTermin : number = termin !== null ? termin.id : DeveloperNotificationException.ifNull(JavaString.format("idTermin der Kursklausur %d", kk.id), kk.idTermin);
		if (this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.containsKey12(idTermin, kk.id)) {
			const skts : List<GostSchuelerklausurTermin> | null = this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get12(idTermin, kk.id);
			for (const sk of skts)
				if (!this._raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
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
	public isTerminAlleSchuelerklausurenVerplant(t : GostKlausurtermin) : boolean {
		if (!this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.containsKey1(t.id))
			return true;
		for (const sk of this._schuelerklausurterminaktuellmenge_by_idTermin_and_idKursklausur.get1(t.id)) {
			if (!this._raumstundenmenge_by_idSchuelerklausurtermin.containsKey(sk.id))
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
	public containsKlausurraumKursklausur(raum : GostKlausurraum, kursklausur : GostKursklausur) : boolean {
		return this._schuelerklausurterminaktuellmenge_by_idRaum_and_idKursklausur.containsKey12(raum.id, kursklausur.id);
	}

	/**
	 * Liefert die gemeinsame Klausurdauer aller {@link GostKursklausur}en, die im übergebenen {@link GostKlausurraum} geschrieben werden.
	 * Falls die Dauern sich unterscheiden, wird <code>null</code> zurückgegeben.
	 *
	 * @param raum der {@link GostKlausurraum}, dessen Klausurdauern überprüft werden.
	 *
	 * @return die gemeinsame Klausurdauer aller {@link GostKursklausur}en oder <code>null</code>, falls keine solche existiert.
	 */
	public getGemeinsameKursklausurdauerByKlausurraum(raum : GostKlausurraum) : number | null {
		let dauer : number = -1;
		for (const klausur of this.kursklausurGetMengeByRaum(raum, true)) {
			const vorgabe : GostKlausurvorgabe = this.vorgabeByKursklausur(klausur);
			if (dauer === -1)
				dauer = vorgabe.dauer;
			if (dauer !== vorgabe.dauer)
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
	public getGemeinsamerKursklausurstartByKlausurraum(raum : GostKlausurraum) : number | null {
		let start : number | null = -1;
		for (const klausur of this.kursklausurGetMengeByRaum(raum, true)) {
			if ((start !== null) && (start === -1))
				start = klausur.startzeit;
			if (this.hatAbweichendeStartzeitByKursklausur(klausur))
				return null;
		}
		return (start === null) ? this.terminGetByIdOrException(raum.idTermin).startzeit : start;
	}

	/**
	 * Liefert <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 *
	 * @return <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurtermin}s in {@link GostKlausurraum}en von {@link GostKlausurtermin}en anderer Jahrgangsstufen zugeordnet sind, sonst <code>false</code>.
	 */
	public isKlausurenInFremdraeumenByTermin(termin : GostKlausurtermin) : boolean {
		for (const skt of this.schuelerklausurterminGetMengeByTermin(termin)) {
			const raum : GostKlausurraum | null = this._klausurraum_by_idSchuelerklausurtermin.get(skt.id);
			if ((raum !== null) && (raum.idTermin !== this.terminOrExceptionBySchuelerklausurTermin(skt).id))
				return true;
		}
		for (const raum of this.raumGetMengeByTermin(termin))
			if (this.raumEnthaeltTerminfremdeKlausuren(raum))
				return true;
		return false;
	}

	/**
	 * Liefert <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurraum}s in zu {@link GostKlausurtermin}en anderer Jahrgangsstufen gehören, sonst <code>false</code>.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return <code>true</code> zurück, falls {@link GostSchuelerklausurTermin}e des übergebenen {@link GostKlausurraum}s in zu {@link GostKlausurtermin}en anderer Jahrgangsstufen gehören, sonst <code>false</code>.
	 */
	public raumEnthaeltTerminfremdeKlausuren(raum : GostKlausurraum) : boolean {
		return !this.schuelerklausurterminFremdterminGetMengeByRaum(raum).isEmpty();
	}

	/**
	 * Liefert die Liste von {@link GostSchuelerklausurTermin}en aus dem übergebenen {@link GostKlausurraum}, die einem raumfremden Klausurtermin zugeordnet sind.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en aus dem übergebenen {@link GostKlausurraum}, die einem raumfremden Klausurtermin zugeordnet sind.
	 */
	public schuelerklausurterminFremdterminGetMengeByRaum(raum : GostKlausurraum) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminGetMengeByRaum(raum))
			if ((raum.idTermin !== this.terminOrExceptionBySchuelerklausurTermin(skt).id))
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert die Liste von {@link GostSchuelerklausurTermin}en aus dem übergebenen {@link GostKlausurraum}, die dem Raumtermin zugeordnet sind.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return die Liste von {@link GostSchuelerklausurTermin}en aus dem übergebenen {@link GostKlausurraum}, die dem Raumtermin zugeordnet sind.
	 */
	public schuelerklausurterminRaumterminGetMengeByRaum(raum : GostKlausurraum) : List<GostSchuelerklausurTermin> {
		const ergebnis : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();
		for (const skt of this.schuelerklausurterminGetMengeByRaum(raum))
			if ((raum.idTermin === this.terminOrExceptionBySchuelerklausurTermin(skt).id))
				ergebnis.add(skt);
		return ergebnis;
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurTermin} den zugehörigen {@link GostKlausurraum}, falls ein solcher schon zugeordnet ist.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, zu dem der {@link GostKlausurraum} gesucht wird.
	 *
	 * @return den {@link GostKlausurraum}, falls einer zugewiesen ist, sonst <code>null</code>.
	 */
	public raumGetBySchuelerklausurtermin(skt : GostSchuelerklausurTermin) : GostKlausurraum | null {
		return this._klausurraum_by_idSchuelerklausurtermin.get(skt.id);
	}

	/**
	 * Liefert den zu einem {@link GostSchuelerklausurTermin} zugehörigen {@link StundenplanRaum} zurück.
	 *
	 * @param skt der {@link GostSchuelerklausurTermin}, zu dem der {@link StundenplanRaum} gesucht wird.
	 *
	 * @return den {@link StundenplanRaum}, falls einer zugewiesen ist, sonst <code>null</code>
	 */
	public stundenplanraumGetBySchuelerklausurtermin(skt : GostSchuelerklausurTermin) : StundenplanRaum | null {
		const raum : GostKlausurraum | null = this.raumGetBySchuelerklausurtermin(skt);
		return ((raum === null) || (raum.idStundenplanRaum === null)) ? null : this.stundenplanManagerGetByTerminOrException(this.terminOrExceptionBySchuelerklausurTermin(skt)).raumGetByIdOrException(raum.idStundenplanRaum);
	}

	/**
	 * Liefert die Menge von {@link GostKlausurtermin}en aus anderen Jahrgangsstufen, die am selben Datum wie der übergebene {@link GostKlausurtermin} terminiert sind. Der als Parameter übergebene {@link GostKlausurtermin} <code>termin</code> ist in der Rückgabemenge nicht enthalten.
	 *
	 * @param termin der {@link GostKlausurtermin}, an dessen Datum jahrgangsfremde {@link GostKlausurtermin}e gesucht werden. Dieser {@link GostKlausurtermin} ist in der Rückgabeliste nicht enthalten.
	 *
	 * @return die Menge von {@link GostKlausurtermin}en aus anderen Jahrgangsstufen, die am selben Datum wie der übergebene {@link GostKlausurtermin} terminiert sind.
	 */
	public getFremdTermineByTermin(termin : GostKlausurtermin) : List<GostKlausurtermin> {
		return this.terminSelbesDatumGetMengeByTermin(termin, false);
	}

	/**
	 * Prüft, ggf. jahrgangsübergreifend, ob {@link GostSchuelerklausurTermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 *
	 * @param termin der {@link GostKlausurtermin}, dessen {@link GostSchuelerklausurTermin}e geprüft werden
	 * @param fremdTermine wenn <code>true</code>, werden auch {@link GostSchuelerklausurTermin}e anderer Jahrgänge am selben Datum berücksichtigt.
	 *
	 * @return <code>true</code>, falls {@link GostSchuelerklausurTermin}e des als Parameter übergebenen {@link GostKlausurtermin}s bereits {@link GostKlausurraum}en zugeordnet sind.
	 */
	public isSchuelerklausurenInRaumByTermin(termin : GostKlausurtermin, fremdTermine : boolean) : boolean {
		for (const teilTermin of this.schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(termin, fremdTermine))
			if (this._raumstundenmenge_by_idSchuelerklausurtermin.containsKey(teilTermin.id))
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
	public raumGetMengeByTermin(termin : GostKlausurtermin) : List<GostKlausurraum> {
		const raeume : List<GostKlausurraum> | null = this._raummenge_by_idTermin.get(termin.id);
		return (raeume === null) ? new ArrayList() : raeume;
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e zu den als Parameter übergebenen {@link GostKlausurtermin} und {@link GostKursklausur}.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKlausurraum}e gesucht werden
	 * @param klausur die {@link GostKursklausur}, zu der die {@link GostKlausurraum}e gesucht werden
	 *
	 * @return die Menge der {@link GostKlausurraum}e zu den als Parameter übergebenen {@link GostKlausurtermin} und {@link GostKursklausur}.
	 */
	public raumGetMengeByTerminAndKursklausur(termin : GostKlausurtermin, klausur : GostKursklausur) : List<GostKlausurraum> {
		return this._raummenge_by_idTermin_and_idKursklausur.get12(termin.id, klausur.id);
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e, ggf. jahrgangsübergreifend, zum als Parameter übergebenen {@link GostKlausurtermin}.
	 *
	 * @param termin der {@link GostKlausurtermin}, zu dem die {@link GostKlausurraum}e gesucht werden
	 * @param fremdTermine wenn <code>true</code> werden auch die {@link GostKlausurraum}e von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen zurückgegeben
	 *
	 * @return die Menge der {@link GostKlausurraum}e, ggf. jahrgangsübergreifend, zum als Parameter übergebenen {@link GostKlausurtermin}.
	 */
	public raumGetMengeByTerminIncludingFremdtermine(termin : GostKlausurtermin, fremdTermine : boolean) : List<GostKlausurraum> {
		return fremdTermine ? this.raumGetMengeByTerminmenge(this.terminSelbesDatumGetMengeByTermin(termin, true)) : this.raumGetMengeByTermin(termin);
	}

	/**
	 * Liefert die Menge der {@link GostKlausurraum}e zur als Parameter übergebenen {@link GostKlausurtermin}menge.
	 *
	 * @param termine die Menge der {@link GostKlausurtermin}e, zu denen die {@link GostKlausurraum}e gesucht werden
	 *
	 * @return die Menge der {@link GostKlausurraum}e zur als Parameter übergebenen {@link GostKlausurtermin}menge.
	 */
	public raumGetMengeByTerminmenge(termine : List<GostKlausurtermin>) : List<GostKlausurraum> {
		const ergebnis : List<GostKlausurraum> = new ArrayList<GostKlausurraum>();
		for (const termin of termine) {
			const teilListe : List<GostKlausurraum> | null = this._raummenge_by_idTermin.get(termin.id);
			if (teilListe !== null)
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
	public anzahlPlaetzeAlleRaeumeByTermin(termin : GostKlausurtermin, fremdTermine : boolean) : number {
		let kapazitaet : number = 0;
		for (const raum of this.raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine)) {
			if (raum.idStundenplanRaum !== null)
				kapazitaet += this.stundenplanManagerGetByTerminOrException(termin).raumGetByIdOrException(raum.idStundenplanRaum).groesse;
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
	public anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin : GostKlausurtermin, fremdTermine : boolean) : number {
		return this.schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(termin, fremdTermine).size();
	}

	/**
	 * Prüft, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurTermin}en ausreichend ist.
	 *
	 * @param termin der zu prüfende {@link GostKlausurtermin}
	 * @param fremdTermine wenn <code>true</code> werden auch die vorhandenen und benötigten Plätze von datumsgleichen {@link GostKlausurtermin}en anderer Jahrgangsstufen geprüft
	 *
	 * @return <code>true</code>, die Platzkapazität aller {@link GostKlausurraum}e des übergebenen {@link GostKlausurtermin}s für die benötigte Platzmenge an {@link GostSchuelerklausurTermin}en ausreichend ist.
	 */
	public isPlatzkapazitaetAusreichendByTermin(termin : GostKlausurtermin, fremdTermine : boolean) : boolean {
		return this.anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin, fremdTermine) <= this.anzahlPlaetzeAlleRaeumeByTermin(termin, fremdTermine);
	}

	/**
	 * Erzeugt aus einer Liste von {@link GostSchuelerklausurTermin}en eine um z.B. für Blockungs-Algorithmen relevante Informationen angereicherte Liste von {@link GostSchuelerklausurTerminRich}-Objekten.
	 *
	 * @param termine die Liste der {@link GostSchuelerklausurTermin}e.
	 *
	 * @return die Liste von angereicherten {@link GostSchuelerklausurTerminRich}-Objekten
	 */
	public enrichSchuelerklausurtermine(termine : List<GostSchuelerklausurTermin>) : List<GostSchuelerklausurTerminRich> {
		const ergebnis : List<GostSchuelerklausurTerminRich> = new ArrayList<GostSchuelerklausurTerminRich>();
		for (const termin of termine)
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
	public enrichKlausurraeume(raeume : List<GostKlausurraum>) : List<GostKlausurraumRich> {
		const ergebnis : List<GostKlausurraumRich> = new ArrayList<GostKlausurraumRich>();
		for (const raum of raeume)
			ergebnis.add(new GostKlausurraumRich(raum, this.stundenplanraumGetByKlausurraum(raum)));
		return ergebnis;
	}

	/**
	 * Liefert den {@link StundenplanRaum} zu einem übergebenen {@link GostKlausurraum}. Falls kein {@link StundenplanRaum} zugeordnet ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return der zugehörige {@link StundenplanRaum}
	 */
	public stundenplanraumGetByKlausurraum(raum : GostKlausurraum) : StundenplanRaum {
		const spm : StundenplanManager = this.stundenplanManagerGetByTerminOrException(this.terminGetByIdOrException(raum.idTermin));
		return DeveloperNotificationException.ifNull(JavaString.format("Stundenplan %d enthält keinen Raum zur ID %d", spm.stundenplanGetID(), raum.idStundenplanRaum), spm.raumGetByIdOrNull(DeveloperNotificationException.ifNull("StundenplanRaum darf nicht NULL sein", raum.idStundenplanRaum)));
	}

	/**
	 * Liefert den {@link StundenplanRaum} zu einem übergebenen {@link GostKlausurraum}. Falls kein {@link StundenplanRaum} zugeordnet ist, wird eine <code>DeveloperNotificationException</code> geworfen.
	 *
	 * @param raum der {@link GostKlausurraum}
	 *
	 * @return der zugehörige {@link StundenplanRaum}
	 */
	public stundenplanraumGetByKlausurraumOrNull(raum : GostKlausurraum) : StundenplanRaum | null {
		return raum.idStundenplanRaum === null ? null : this.stundenplanManagerGetByTerminOrException(this.terminGetByIdOrException(raum.idTermin)).raumGetByIdOrException(raum.idStundenplanRaum);
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
	public alleRaeumeHabenStundenplanRaumByTermin(termin : GostKlausurtermin, fremdTermine : boolean, nurVerwendet : boolean) : boolean {
		for (const raum of this.raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine))
			if (raum.idStundenplanRaum === null && (!nurVerwendet || !this.schuelerklausurterminGetMengeByRaum(raum).isEmpty()))
				return false;
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
	public alleRaeumeHabenAusreichendKapazitaetByTermin(termin : GostKlausurtermin, fremdTermine : boolean) : boolean {
		for (const raum of this.raumGetMengeByTerminIncludingFremdtermine(termin, fremdTermine))
			if (!this.raumHatAusreichendKapazitaetByRaum(raum))
				return false;
		return true;
	}

	/**
	 * Prüft, ob der übergebene {@link GostKlausurraum} ausreichend Platzkapazität hat.
	 *
	 * @param raum der zu prüfende {@link GostKlausurraum}
	 *
	 * @return <code>true</code>, falls der übergebene {@link GostKlausurraum} ausreichend Platzkapazität hat.
	 */
	public raumHatAusreichendKapazitaetByRaum(raum : GostKlausurraum) : boolean {
		return (raum.idStundenplanRaum === null || this.schuelerklausurterminGetMengeByRaum(raum).size() <= this.stundenplanraumGetByKlausurraum(raum).groesse);
	}

	/**
	 * Prüft, ob die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 *
	 * @param klausur die zu prüfende {@link GostKursklausur}
	 *
	 * @return <code>true</code>, falls die {@link GostKursklausur} schon eine Raumzuweisung an einem {@link GostKlausurtermin} hat.
	 */
	public hatRaumzuteilungByKursklausur(klausur : GostKursklausur) : boolean {
		for (const skt of this.schuelerklausurterminAktuellGetMengeByTerminAndKursklausur(this.terminOrExceptionByKursklausur(klausur), klausur)) {
			const stunden : List<GostKlausurraumstunde> | null = this._raumstundenmenge_by_idSchuelerklausurtermin.get(skt.id);
			if (stunden !== null && !stunden.isEmpty())
				return true;
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
	public schuelerklausurOhneSchuelerGetMengeByJahrgang(abijahrgang : number) : List<GostSchuelerklausur> {
		const ergebnis : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();
		const sksMap : JavaMap<number, List<GostSchuelerklausur>> | null = this._schuelerklausurmenge_by_abijahr_and_idSchueler.getSubMapOrNull(abijahrgang);
		if (sksMap === null || sksMap.isEmpty())
			return ergebnis;
		for (const sk of sksMap.entrySet()) {
			const schueler : SchuelerListeEintrag | null = this._schuelerlisteeintrag_by_id.get(sk.getKey());
			if (!sk.getValue().isEmpty() && (schueler === null || schueler.abiturjahrgang !== abijahrgang))
				ergebnis.addAll(sk.getValue());
		}
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKlausurvorgabe}n zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von fehlenden {@link GostKlausurvorgabe}n
	 */
	public vorgabefehlendGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurvorgabe> {
		if (quartal === 0)
			return this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id);
		return this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getNonNullValuesOfMap4AsList(abiJahrgang, halbjahr.id, quartal);
	}

	/**
	 * Gibt das fehlende {@link GostKlausurvorgabe}-Objekt zu den übergebenen Parametern zurück.
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal     das Quartal
	 * @param kursartAllg die {@link GostKursart}
	 * @param idFach      die ID des Fachs
	 *
	 * @return das fehlende {@link GostKlausurvorgabe}-Objekt
	 */
	public vorgabefehlendGetByHalbjahrAndQuartalAndKursartallgAndFachid(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kursartAllg : GostKursart, idFach : number) : GostKlausurvorgabe | null {
		return this._vorgabefehlend_by_abijahr_and_halbjahr_and_quartal_and_kursartAllg_and_idFach.getOrNull(abiJahrgang, halbjahr.id, quartal, kursartAllg.kuerzel, idFach);
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKursklausur}en zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von fehlenden {@link GostKursklausur}en
	 */
	public kursklausurfehlendGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKursklausur> {
		if (quartal === 0)
			return this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id);
		return this._kursklausurfehlend_by_abijahr_and_halbjahr_and_quartal_and_idVorgabe_and_idKurs.getNonNullValuesOfMap4AsList(abiJahrgang, halbjahr.id, quartal);
	}

	/**
	 * Liefert eine Liste von fehlenden {@link GostKursklausur}en zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return die Liste von fehlenden {@link GostKursklausur}en
	 */
	public schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostSchuelerklausur> {
		if (quartal === 0)
			return this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesOfMap3AsList(abiJahrgang, halbjahr.id);
		return this._schuelerklausurfehlendmenge_by_abijahr_and_halbjahr_and_quartal_and_idSchueler_and_idKursklausur.getNonNullValuesOfMap4AsList(abiJahrgang, halbjahr.id, quartal);
	}

	/**
	 * Liefert die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 * @param kwErrorLimit das Errorlimit für die Anzahl der Klausuren pro Schüler und Woche
	 *
	 * @return die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public planungsfehlerGetAnzahlByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kwErrorLimit : number) : number {
		let anzahl : number = 0;
		anzahl += this.vorgabefehlendGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.kursklausurfehlendGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(abiJahrgang, halbjahr, quartal, kwErrorLimit, -1).size();
		anzahl += this.terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		if (!this.stundenplanManagerGeladenAndExistsByAbschnitt(DeveloperNotificationException.ifMap2DGetIsNull(this._schuljahresabschnitt_by_abijahr_and_halbjahr, abiJahrgang, halbjahr.id)))
			anzahl++;
		return anzahl;
	}

	/**
	 * Liefert die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 * @param kwWarnLimit die Anzahl der Klausuren pro Schüler und Woche, ab der eine Warnung ausgegeben wird
	 * @param kwErrorLimit die Anzahl der Klausuren pro Schüler und Woche, ab der ein Fehler ausgegeben wird
	 *
	 * @return die Anzahl möglicher Probleme in der aktuellen Klausurplanung zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public planungshinweiseGetAnzahlByHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number, kwWarnLimit : number, kwErrorLimit : number) : number {
		let anzahl : number = 0;
		anzahl += this.terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal).size();
		anzahl += this.klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(abiJahrgang, halbjahr, quartal, kwWarnLimit, kwErrorLimit).size();
		return anzahl;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, die Schülerkonflikte beinhalten zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, die Schülerkonflikte beinhalten zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (this.konflikteAnzahlGetByTermin(termin) > 0)
				ergebnis.add(termin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von datierten {@link GostKlausurtermin}en, die keinen validen Stundenplan haben zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von datierten {@link GostKlausurtermin}en, die keinen validen Stundenplan haben zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (this.stundenplanManagerGetByTerminOrNull(termin) === null)
				ergebnis.add(termin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, bei denen die Raumzuweisung noch unvollständig ist zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, bei denen die Raumzuweisung noch unvollständig ist zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (!this.isTerminAlleSchuelerklausurenVerplant(termin) || !this.alleRaeumeHabenStundenplanRaumByTermin(termin, false, true))
				ergebnis.add(termin);
		return ergebnis;
	}

	/**
	 * Liefert eine Liste von {@link GostKlausurtermin}en, bei denen die Platzkapazität nicht ausreichend ist zum übergebenen {@link GostHalbjahr} und Quartal
	 *
	 * @param abiJahrgang der Abitur-Jahrgang
	 * @param halbjahr das {@link GostHalbjahr}
	 * @param quartal die Nummer des Quartals, 0 für alle Quartale
	 *
	 * @return eine Liste von {@link GostKlausurtermin}en, bei denen die Platzkapazität nicht ausreichend ist zum übergebenen {@link GostHalbjahr} und Quartal
	 */
	public terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang : number, halbjahr : GostHalbjahr, quartal : number) : List<GostKlausurtermin> {
		const ergebnis : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();
		for (const termin of this.terminMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(abiJahrgang, halbjahr, quartal))
			if (!this.alleRaeumeHabenAusreichendKapazitaetByTermin(termin, false))
				ergebnis.add(termin);
		return ergebnis;
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param kursklausur die {@link GostKursklausur}, zu der die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public kursklausurPatchAttributesAndSetzeRaumZuSchuelerklausuren(kursklausur : GostKursklausur, raumData : GostKlausurenCollectionSkrsKrsData) : void {
		this.kursklausurPatchAttributesOhneUpdate(kursklausur);
		this.setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		this.update_all();
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param termin der {@link GostKlausurtermin}, zu dem die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public terminPatchAttributesAndSetzeRaumZuSchuelerklausuren(termin : GostKlausurtermin, raumData : GostKlausurenCollectionSkrsKrsData) : void {
		this.setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		this.terminPatchAttributesOhneUpdate(termin);
		this.update_all();
	}

	/**
	 *	Fasst zwei Update-Methoden zusammen, um Laufzeit bei update_all() zu sparen.
	 *  @param schuelerklausurtermin der {@link GostSchuelerklausurTermin}, zu dem die Attribute aktualisiert werden sollen
	 * 	@param raumData die Raumdaten, die aktualisiert werden sollen
	 */
	public schuelerklausurterminPatchAttributesAndSetzeRaumZuSchuelerklausuren(schuelerklausurtermin : GostSchuelerklausurTermin, raumData : GostKlausurenCollectionSkrsKrsData) : void {
		this.schuelerklausurterminPatchAttributesOhneUpdate(schuelerklausurtermin);
		this.setzeRaumZuSchuelerklausurenOhneUpdate(raumData);
		this.update_all();
	}

	/**
	 * Liefert eine Liste von {@link GostSchuelerklausurterminraumstunde}n, die zu den übergebenen {@link GostKlausurraumstunde}n gehören.
	 * @param raumStunden die Liste von {@link GostKlausurraumstunde}n, zu denen die {@link GostSchuelerklausurterminraumstunde}n geliefert werden sollen
	 * @return die Liste von {@link GostSchuelerklausurterminraumstunde}n, die zu den übergebenen {@link GostKlausurraumstunde}n gehören
	 */
	public schuelerklausurraumstundeGetMengeByKlausurraumstundenmenge(raumStunden : List<GostKlausurraumstunde>) : List<GostSchuelerklausurterminraumstunde> {
		const ergebnis : List<GostSchuelerklausurterminraumstunde> = new ArrayList<GostSchuelerklausurterminraumstunde>();
		for (let stunde of raumStunden) {
			const listStunden : List<GostSchuelerklausurterminraumstunde> | null = this._schuelerklausurraumstunde_by_idSchuelerklausurtermin_and_idRaumstunde.get2(stunde.id);
			if (listStunden !== null)
				ergebnis.addAll(listStunden);
		}
		return ergebnis;
	}

	/**
	 * Liefert die Stundenplanzeitraster-Menge zu einem Klausurraum
	 * @param raum der Klausurraum
	 * @return die Stundenplanzeitraster-Menge zu einem Klausurraum
	 */
	public zeitrasterGetMengeByRaum(raum : GostKlausurraum) : List<StundenplanZeitraster> {
		const ergebnis : List<StundenplanZeitraster> = new ArrayList<StundenplanZeitraster>();
		const stundenplanManager : StundenplanManager = this.stundenplanManagerGetByTerminOrException(this.terminGetByRaumOrException(raum));
		for (let stunde of this.raumstundeGetMengeByRaum(raum))
			if (stunde.idZeitraster !== null) {
				const zr : StundenplanZeitraster | null = stundenplanManager.zeitrasterGetByIdOrNull(stunde.idZeitraster);
				if (zr !== null)
					ergebnis.add(zr);
			}
		return ergebnis;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager'].includes(name);
	}

	public static class = new Class<GostKlausurplanManager>('de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager');

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_GostKlausurplanManager(obj : unknown) : GostKlausurplanManager {
	return obj as GostKlausurplanManager;
}

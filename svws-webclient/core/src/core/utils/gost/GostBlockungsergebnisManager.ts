import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { Logger } from '../../../core/logger/Logger';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { System } from '../../../java/lang/System';
import { SchuelerStatus } from '../../../core/types/SchuelerStatus';
import type { Comparator } from '../../../java/util/Comparator';
import type { Predicate } from '../../../java/util/function/Predicate';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { SchuelerblockungInput } from '../../../core/data/kursblockung/SchuelerblockungInput';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../java/util/List';
import { Geschlecht } from '../../../core/types/Geschlecht';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { SchuelerblockungOutput } from '../../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs } from '../../../core/data/kursblockung/SchuelerblockungInputKurs';
import { GostBlockungsdatenManager, cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager } from '../../../core/utils/gost/GostBlockungsdatenManager';
import { SchuelerblockungAlgorithmus } from '../../../core/kursblockung/SchuelerblockungAlgorithmus';
import { CollectionUtils } from '../../../core/utils/CollectionUtils';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { MapUtils } from '../../../core/utils/MapUtils';
import { GostBlockungsergebnis, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { ListUtils } from '../../../core/utils/ListUtils';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';

export class GostBlockungsergebnisManager extends JavaObject {

	/**
	 * Der Blockungsdaten-Manager ist das Elternteil dieses Objektes.
	 */
	private readonly _parent : GostBlockungsdatenManager;

	/**
	 * Das Blockungsergebnis ist das zugehörige Eltern-Datenobjekt.
	 */
	private _ergebnis : GostBlockungsergebnis = new GostBlockungsergebnis();

	/**
	 * Schienen-Nummer --> GostBlockungsergebnisSchiene
	 */
	private readonly _map_schienenNr_schiene : JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap();

	/**
	 * Schienen-ID --> GostBlockungSchiene
	 */
	private readonly _map_schienenID_schiene : JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap();

	/**
	 * Schienen-ID --> Integer = Anzahl SuS
	 */
	private readonly _map_schienenID_schuelerAnzahl : JavaMap<number, number> = new HashMap();

	/**
	 * Schienen-ID --> Anzahl Kollisionen
	 */
	private readonly _map_schienenID_kollisionen : JavaMap<number, number> = new HashMap();

	/**
	 * (Schienen-ID, Fachart-ID) --> ArrayList<Kurse> = Alle Kurse in der Schiene mit der Fachart.
	 */
	private readonly _map2D_schienenID_fachartID_kurse : HashMap2D<number, number, List<GostBlockungsergebnisKurs>> = new HashMap2D();

	/**
	 * Schüler-ID --> Set<GostBlockungsergebnisKurs>
	 */
	private readonly _map_schuelerID_kurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Schüler-ID --> Set<GostBlockungsergebnisKurs> = Kurse des Schüler, die aufgrund der aktuellen Fachwahlen ungültig sind.
	 */
	private readonly _map_schuelerID_ungueltige_kurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Schüler-ID --> Integer (Kollisionen)
	 */
	private readonly _map_schuelerID_kollisionen : JavaMap<number, number> = new HashMap();

	/**
	 * (Schüler-ID, Fach-ID) --> GostBlockungsergebnisKurs = Die aktuelle Wahl des Schülers in dem Fach.
	 */
	private readonly _map2D_schuelerID_fachID_kurs : HashMap2D<number, number, GostBlockungsergebnisKurs | null> = new HashMap2D();

	/**
	 * Schüler-ID --> Schienen-ID --> Set<GostBlockungsergebnisKurs> = Alle Kurse des Schülers in der Schiene.
	 */
	private readonly _map2D_schuelerID_schienenID_kurse : HashMap2D<number, number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap2D();

	/**
	 * Kurs-ID --> Set<GostBlockungsergebnisSchiene>
	 */
	private readonly _map_kursID_schienen : JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> = new HashMap();

	/**
	 * Kurs-ID --> GostBlockungsergebnisKurs
	 */
	private readonly _map_kursID_kurs : JavaMap<number, GostBlockungsergebnisKurs> = new HashMap();

	/**
	 * Kurs-ID --> Anzahl an Dummy-SuS
	 */
	private readonly _map_kursID_dummySuS : JavaMap<number, number> = new HashMap();

	/**
	 * Kurs-ID --> Set<SchuelerID>
	 */
	private readonly _map_kursID_schuelerIDs : JavaMap<number, JavaSet<number>> = new HashMap();

	/**
	 * Fach-ID --> ArrayList<GostBlockungsergebnisKurs>
	 */
	private readonly _map_fachID_kurse : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Fachart-ID --> Integer = Kursdifferenz der Fachart.
	 */
	private readonly _map_fachartID_kursdifferenz : JavaMap<number, number> = new HashMap();

	/**
	 * Menge aller Fachart-IDs sortiert nach der aktuellen Sortiervariante.
	 */
	private readonly _fachartmenge_sortiert : List<number> = new ArrayList();

	/**
	 * Entscheidet, welcher Comparator verwendet wird mit 1 = (KURSART, FACH) andernfalls (FACH, KURSART).
	 */
	private _fachartmenge_sortierung : number = 1;

	/**
	 * Comparator für die Facharten nach (KURSART, FACH).
	 */
	private readonly _fachartComparator_kursart_fach : Comparator<number>;

	/**
	 * Comparator für die Facharten nach (FACH, KURSART).
	 */
	private readonly _fachartComparator_fach_kursart : Comparator<number>;

	/**
	 * Fachart-ID --> ArrayList<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart.
	 */
	private readonly _map_fachartID_kurse : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _kursComparator_kursart_fach_kursnummer : Comparator<GostBlockungsergebnisKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _kursComparator_fach_kursart_kursnummer : Comparator<GostBlockungsergebnisKurs>;


	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 *
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public constructor(pParent : GostBlockungsdatenManager, pGostBlockungsergebnisID : number);

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 *
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public constructor(pParent : GostBlockungsdatenManager, pErgebnis : GostBlockungsergebnis);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : GostBlockungsdatenManager, __param1 : GostBlockungsergebnis | number) {
		super();
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && typeof __param1 === "number")) {
			const pParent : GostBlockungsdatenManager = cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(__param0);
			const pGostBlockungsergebnisID : number = __param1 as number;
			this._parent = pParent;
			this._fachartComparator_kursart_fach = this.createComparatorFachartKursartFach();
			this._fachartComparator_fach_kursart = this.createComparatorFachartFachKursart();
			this._kursComparator_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._kursComparator_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this.stateClear(new GostBlockungsergebnis(), pGostBlockungsergebnisID);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnis'))))) {
			const pParent : GostBlockungsdatenManager = cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(__param0);
			const pErgebnis : GostBlockungsergebnis = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(__param1);
			this._parent = pParent;
			this._fachartComparator_kursart_fach = this.createComparatorFachartKursartFach();
			this._fachartComparator_fach_kursart = this.createComparatorFachartFachKursart();
			this._kursComparator_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
			this._kursComparator_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
			this.stateClear(pErgebnis, pErgebnis.id);
		} else throw new Error('invalid method overload');
	}

	private createComparatorFachartKursartFach() : Comparator<number> {
		const comp : Comparator<number> = { compare : (a: number, b: number) => {
			const aKursartID : number = GostKursart.getKursartID(a!);
			const bKursartID : number = GostKursart.getKursartID(b!);
			if (aKursartID < bKursartID)
				return -1;
			if (aKursartID > bKursartID)
				return +1;
			const aFachID : number = GostKursart.getFachID(a!);
			const bFachID : number = GostKursart.getFachID(b!);
			const aFach : GostFach = this._parent.faecherManager().getOrException(aFachID);
			const bFach : GostFach = this._parent.faecherManager().getOrException(bFachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		} };
		return comp;
	}

	private createComparatorFachartFachKursart() : Comparator<number> {
		const comp : Comparator<number> = { compare : (a: number, b: number) => {
			const aFachID : number = GostKursart.getFachID(a!);
			const bFachID : number = GostKursart.getFachID(b!);
			const aFach : GostFach = this._parent.faecherManager().getOrException(aFachID);
			const bFach : GostFach = this._parent.faecherManager().getOrException(bFachID);
			const cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			const aKursartID : number = GostKursart.getKursartID(a!);
			const bKursartID : number = GostKursart.getKursartID(b!);
			if (aKursartID < bKursartID)
				return -1;
			if (aKursartID > bKursartID)
				return +1;
			return 0;
		} };
		return comp;
	}

	private createComparatorKursFachKursartNummer() : Comparator<GostBlockungsergebnisKurs> {
		const comp : Comparator<GostBlockungsergebnisKurs> = { compare : (a: GostBlockungsergebnisKurs, b: GostBlockungsergebnisKurs) => {
			const aFach : GostFach = this._parent.faecherManager().getOrException(a.fachID);
			const bFach : GostFach = this._parent.faecherManager().getOrException(b.fachID);
			const cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;
			const aKurs : GostBlockungKurs = this._parent.kursGet(a.id);
			const bKurs : GostBlockungKurs = this._parent.kursGet(b.id);
			return JavaInteger.compare(aKurs.nummer, bKurs.nummer);
		} };
		return comp;
	}

	private createComparatorKursKursartFachNummer() : Comparator<GostBlockungsergebnisKurs> {
		const comp : Comparator<GostBlockungsergebnisKurs> = { compare : (a: GostBlockungsergebnisKurs, b: GostBlockungsergebnisKurs) => {
			if (a.kursart < b.kursart)
				return -1;
			if (a.kursart > b.kursart)
				return +1;
			const aFach : GostFach = this._parent.faecherManager().getOrException(a.fachID);
			const bFach : GostFach = this._parent.faecherManager().getOrException(b.fachID);
			const cmpFach : number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0)
				return cmpFach;
			const aKurs : GostBlockungKurs = this._parent.kursGet(a.id);
			const bKurs : GostBlockungKurs = this._parent.kursGet(b.id);
			return JavaInteger.compare(aKurs.nummer, bKurs.nummer);
		} };
		return comp;
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	private stateRevalidateEverything() : void {
		this.stateClear(this._ergebnis, this._ergebnis.id);
	}

	private stateClear(pOld : GostBlockungsergebnis, pGostBlockungsergebnisID : number) : void {
		this._map_schienenNr_schiene.clear();
		this._map_schienenID_schiene.clear();
		this._map_schienenID_schuelerAnzahl.clear();
		this._map_schienenID_kollisionen.clear();
		this._map2D_schienenID_fachartID_kurse.clear();
		this._map_schuelerID_kurse.clear();
		this._map_schuelerID_ungueltige_kurse.clear();
		this._map_schuelerID_kollisionen.clear();
		this._map2D_schuelerID_fachID_kurs.clear();
		this._map2D_schuelerID_schienenID_kurse.clear();
		this._map_kursID_schienen.clear();
		this._map_kursID_kurs.clear();
		this._map_kursID_schuelerIDs.clear();
		this._map_kursID_dummySuS.clear();
		this._map_fachID_kurse.clear();
		this._map_fachartID_kurse.clear();
		this._map_fachartID_kursdifferenz.clear();
		this._ergebnis = new GostBlockungsergebnis();
		this._ergebnis.id = pGostBlockungsergebnisID;
		this._ergebnis.blockungID = this._parent.getID();
		this._ergebnis.name = pOld.name;
		this._ergebnis.gostHalbjahr = this._parent.daten().gostHalbjahr;
		this._ergebnis.istAktiv = pOld.istAktiv;
		this._ergebnis.bewertung.kursdifferenzMax = 0;
		this._ergebnis.bewertung.kursdifferenzHistogramm = Array(this._parent.schuelerGetAnzahl() + 1).fill(0);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet = this._parent.daten().fachwahlen.size();
		for (const gSchiene of this._parent.daten().schienen) {
			const eSchiene : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;
			this._ergebnis.schienen.add(eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schienenNr_schiene, gSchiene.nummer, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schienenID_schiene, gSchiene.id, eSchiene);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schienenID_schuelerAnzahl, gSchiene.id, 0);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schienenID_kollisionen, gSchiene.id, 0);
		}
		for (const gKurs of this._parent.daten().kurse) {
			const eKurs : GostBlockungsergebnisKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kursID_kurs, eKurs.id, eKurs);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kursID_schienen, eKurs.id, new HashSet<GostBlockungsergebnisSchiene>());
			DeveloperNotificationException.ifMapPutOverwrites(this._map_kursID_schuelerIDs, eKurs.id, new HashSet<number>());
			MapUtils.getOrCreateArrayList(this._map_fachID_kurse, eKurs.fachID).add(eKurs);
			const fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(this._map_fachartID_kurse, fachartID).add(eKurs);
			if (!this._map_fachartID_kursdifferenz.containsKey(fachartID)) {
				this._map_fachartID_kursdifferenz.put(fachartID, 0);
				this._ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
		}
		for (const gFachwahl of this._parent.daten().fachwahlen)
			MapUtils.getOrCreateArrayList(this._map_fachartID_kurse, GostKursart.getFachartIDByFachwahl(gFachwahl));
		for (const gSchiene of this._parent.daten().schienen)
			for (const fachartID of this._map_fachartID_kursdifferenz.keySet())
				DeveloperNotificationException.ifMap2DPutOverwrites(this._map2D_schienenID_fachartID_kurse, gSchiene.id, fachartID, new ArrayList());
		for (const gSchueler of this._parent.daten().schueler) {
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kurse, gSchueler.id, new HashSet<GostBlockungsergebnisKurs>());
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kollisionen, gSchueler.id, 0);
		}
		for (const gFachwahl of this._parent.daten().fachwahlen)
			DeveloperNotificationException.ifMap2DPutOverwrites(this._map2D_schuelerID_fachID_kurs, gFachwahl.schuelerID, gFachwahl.fachID, null);
		for (const gSchueler of this._parent.daten().schueler)
			for (const gSchiene of this._parent.daten().schienen) {
				const newSet : HashSet<GostBlockungsergebnisKurs> | null = new HashSet();
				DeveloperNotificationException.ifMap2DPutOverwrites(this._map2D_schuelerID_schienenID_kurse, gSchueler.id, gSchiene.id, newSet);
			}
		const kursBearbeitet : HashSet<number> | null = new HashSet();
		for (const schieneOld of pOld.schienen)
			for (const kursOld of schieneOld.kurse) {
				this.setKursSchiene(kursOld.id, schieneOld.id, true);
				if (kursBearbeitet.add(kursOld.id))
					for (const schuelerID of kursOld.schueler)
						this.setSchuelerKurs(schuelerID!, kursOld.id, true);
			}
		this._fachartmenge_sortiert.addAll(this._map_fachartID_kurse.keySet());
		this.stateRegelvalidierung();
	}

	private stateRegelvalidierung() : void {
		const regelVerletzungen : List<number> = this._ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		this._map_kursID_dummySuS.clear();
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			this.stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			this.stateRegelvalidierung2_kurs_fixieren_in_schiene(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE))
			this.stateRegelvalidierung3_kurs_sperren_in_schiene(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			this.stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS))
			this.stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			this.stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS))
			this.stateRegelvalidierung7_kurs_verbieten_mit_kurs(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS))
			this.stateRegelvalidierung8_kurs_zusammen_mit_kurs(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			this.stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN))
			this.stateRegelvalidierung10_lehrkraefte_beachten(r, regelVerletzungen);
		this._parent.ergebnisUpdateBewertung(this._ergebnis);
		this.updateAll();
	}

	private stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (let schienenNr : number = r.parameter.get(1)!; schienenNr <= r.parameter.get(2)!; schienenNr++)
			for (const eKurs of this.getSchieneEmitNr(schienenNr).kurse)
				if (eKurs.kursart === r.parameter.get(0)!)
					regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung2_kurs_fixieren_in_schiene(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		if (!this.getOfKursSchienenmenge(r.parameter.get(0)!).contains(this.getSchieneEmitNr(r.parameter.get(1)!)))
			regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung3_kurs_sperren_in_schiene(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		if (this.getOfKursSchienenmenge(r.parameter.get(0)!).contains(this.getSchieneEmitNr(r.parameter.get(1)!)))
			regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung4_schueler_fixieren_in_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		if (!this.getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0)!, r.parameter.get(1)!))
			regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung5_schueler_verbieten_in_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		if (this.getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0)!, r.parameter.get(1)!))
			regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (const eKurs of this._map_kursID_kurs.values())
			for (const eSchieneID of eKurs.schienen) {
				const nr : number = this.getSchieneG(eSchieneID!).nummer;
				const b1 : boolean = eKurs.kursart === r.parameter.get(0)!;
				const b2 : boolean = (r.parameter.get(1)! <= nr) && (nr <= r.parameter.get(2)!);
				if (b1 !== b2)
					regelVerletzungen.add(r.id);
			}
	}

	private stateRegelvalidierung7_kurs_verbieten_mit_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs1 : number = r.parameter.get(0).valueOf();
		const idKurs2 : number = r.parameter.get(1).valueOf();
		for (const schiene1 of this.getOfKursSchienenmenge(idKurs1))
			for (const schiene2 of this.getOfKursSchienenmenge(idKurs2))
				if (schiene1 as unknown === schiene2 as unknown)
					regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung8_kurs_zusammen_mit_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs1 : number = r.parameter.get(0).valueOf();
		const idKurs2 : number = r.parameter.get(1).valueOf();
		const set1 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs1);
		const set2 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs2);
		if (set1.size() < set2.size()) {
			for (const schiene1 of set1)
				if (!set2.contains(schiene1))
					regelVerletzungen.add(r.id);
		} else {
			for (const schiene2 of set2)
				if (!set1.contains(schiene2))
					regelVerletzungen.add(r.id);
		}
	}

	private stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r : GostBlockungRegel) : void {
		const idKurs : number = r.parameter.get(0).valueOf();
		const anzahl : number = r.parameter.get(1)!;
		DeveloperNotificationException.ifTrue("Regel 9 DummySuS Wert = " + anzahl + " ist ungültig!", (anzahl < 1) || (anzahl > 99));
		DeveloperNotificationException.ifMapPutOverwrites(this._map_kursID_dummySuS, idKurs, anzahl);
	}

	private stateRegelvalidierung10_lehrkraefte_beachten(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (const eSchiene of this._map_schienenID_schiene.values())
			for (const eKurs1 of eSchiene.kurse)
				for (const eKurs2 of eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (const gLehr1 of this.getKursG(eKurs1.id).lehrer)
							for (const gLehr2 of this.getKursG(eKurs2.id).lehrer)
								if (gLehr1.id === gLehr2.id)
									regelVerletzungen.add(r.id);
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, wird der Schüler nicht hinzugefügt.
	 *          Stattdessen wird die ungültige Wahl in einer Map gespeichert.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursHinzufuegen(idSchueler : number, idKurs : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const fachID : number = kurs.fachID;
		if (!this.getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			this.stateSchuelerKursUngueltigeWahlHinzufuegen(idSchueler, kurs);
			return;
		}
		if (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) !== null)
			return;
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(idSchueler);
		const schuelerIDsOfKurs : JavaSet<number> = this.getOfKursSchuelerIDmenge(idKurs);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.add(idSchueler);
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(idSchueler);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		this._map2D_schuelerID_fachID_kurs.put(idSchueler, fachID, kurs);
		this.stateKursdifferenzUpdate(fachartID);
		for (const schieneID of kurs.schienen)
			this.stateSchuelerSchieneHinzufuegen(idSchueler, schieneID!, kurs);
		this.stateRegelvalidierung();
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.<br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, so wird der Schüler aus der zuvor gespeichert
	 *          Zuordnung aller ungültigen Wahlen gelöscht.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursEntfernen(idSchueler : number, idKurs : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const fachID : number = kurs.fachID;
		if (!this.getOfSchuelerHatFachwahl(idSchueler, fachID, kurs.kursart)) {
			this.stateSchuelerKursUngueltigeWahlEntfernen(idSchueler, kurs);
			return;
		}
		if (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, fachID) as unknown !== kurs as unknown)
			return;
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(idSchueler);
		const schuelerIDsOfKurs : JavaSet<number> = this.getOfKursSchuelerIDmenge(idKurs);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.remove(idSchueler);
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(idSchueler);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		this._map2D_schuelerID_fachID_kurs.put(idSchueler, fachID, null);
		this.stateKursdifferenzUpdate(fachartID);
		for (const schieneID of kurs.schienen)
			this.stateSchuelerSchieneEntfernen(idSchueler, schieneID!, kurs);
		this.stateRegelvalidierung();
	}

	private stateSchuelerKursUngueltigeWahlHinzufuegen(idSchueler : number, idKurs : GostBlockungsergebnisKurs) : void {
		MapUtils.getOrCreateHashSet(this._map_schuelerID_ungueltige_kurse, idSchueler).add(idKurs);
	}

	private stateSchuelerKursUngueltigeWahlEntfernen(idSchueler : number, idKurs : GostBlockungsergebnisKurs) : void {
		const set : JavaSet<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_ungueltige_kurse, idSchueler);
		set.remove(idKurs);
		if (set.isEmpty())
			this._map_schuelerID_ungueltige_kurse.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneHinzufuegen(idKurs : number, idSchiene : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		const setSchienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs);
		const idFach : number = kurs.fachID;
		const idFachart : number = GostKursart.getFachartID(idFach, kurs.kursart);
		const kursGruppe : List<GostBlockungsergebnisKurs> = this._map2D_schienenID_fachartID_kurse.getNonNullOrException(idSchiene, idFachart);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListAddsDuplicate("kurs.schienen", kurs.schienen, schiene.id);
		DeveloperNotificationException.ifListAddsDuplicate("schiene.kurse", schiene.kurse, kurs);
		DeveloperNotificationException.ifSetAddsDuplicate("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (const schuelerID of kurs.schueler)
			this.stateSchuelerSchieneHinzufuegen(schuelerID!, schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		DeveloperNotificationException.ifListAddsDuplicate("kursGruppe", kursGruppe, kurs);
		this.stateRegelvalidierung();
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneEntfernen(idKurs : number, idSchiene : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		const setSchienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs);
		const idFach : number = kurs.fachID;
		const idFachart : number = GostKursart.getFachartID(idFach, kurs.kursart);
		const kursGruppe : List<GostBlockungsergebnisKurs> = this._map2D_schienenID_fachartID_kurse.getNonNullOrException(idSchiene, idFachart);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kurs.schienen", kurs.schienen, schiene.id);
		DeveloperNotificationException.ifListRemoveFailes("schiene.kurse", schiene.kurse, kurs);
		DeveloperNotificationException.ifSetRemoveFailes("setSchienenOfKurs", setSchienenOfKurs, schiene);
		for (const schuelerID of kurs.schueler)
			this.stateSchuelerSchieneEntfernen(schuelerID!, schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - setSchienenOfKurs.size());
		DeveloperNotificationException.ifListRemoveFailes("kursGruppe", kursGruppe, kurs);
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
		this.stateRegelvalidierung();
	}

	private stateSchuelerSchieneHinzufuegen(idSchueler : number, idSchiene : number, kurs : GostBlockungsergebnisKurs) : void {
		const schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(idSchiene);
		this._map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl + 1);
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this._map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
		kursmenge.add(kurs);
		if (kursmenge.size() > 1) {
			const schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			this._map_schienenID_kollisionen.put(idSchiene, schieneKollisionen + 1);
			const schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(idSchueler);
			this._map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen + 1);
			this._ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private stateSchuelerSchieneEntfernen(idSchueler : number, idSchiene : number, kurs : GostBlockungsergebnisKurs) : void {
		const schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(idSchiene);
		DeveloperNotificationException.ifTrue("schieneSchuelerzahl == 0 --> Entfernen unmöglich!", schieneSchuelerzahl === 0);
		this._map_schienenID_schuelerAnzahl.put(idSchiene, schieneSchuelerzahl - 1);
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this._map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
		kursmenge.remove(kurs);
		if (!kursmenge.isEmpty()) {
			const schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene);
			DeveloperNotificationException.ifTrue("schieneKollisionen == 0 --> Entfernen unmöglich!", schieneKollisionen === 0);
			this._map_schienenID_kollisionen.put(idSchiene, schieneKollisionen - 1);
			const schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(idSchueler);
			DeveloperNotificationException.ifTrue("schuelerKollisionen == 0 --> Entfernen unmöglich!", schuelerKollisionen === 0);
			this._map_schuelerID_kollisionen.put(idSchueler, schuelerKollisionen - 1);
			DeveloperNotificationException.ifTrue("Gesamtkollisionen == 0 --> Entfernen unmöglich!", this._ergebnis.bewertung.anzahlSchuelerKollisionen === 0);
			this._ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private stateKursdifferenzUpdate(idFachart : number) : void {
		const kursmenge : List<GostBlockungsergebnisKurs> = this.getOfFachartKursmenge(idFachart);
		const kurs1 : GostBlockungsergebnisKurs = DeveloperNotificationException.ifListGetFirstFailes("getOfFachartKursmenge", kursmenge);
		let min : number = kurs1.schueler.size() + this.getOfKursAnzahlSchuelerDummy(kurs1.id);
		let max : number = min;
		for (const kurs of kursmenge) {
			const size : number = kurs.schueler.size() + this.getOfKursAnzahlSchuelerDummy(kurs.id);
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		const newKD : number = max - min;
		const oldKD : number = this.getOfFachartKursdifferenz(idFachart);
		if (newKD === oldKD)
			return;
		this._map_fachartID_kursdifferenz.put(idFachart, newKD);
		const kursdifferenzen : Array<number> | null = this._ergebnis.bewertung.kursdifferenzHistogramm;
		kursdifferenzen[oldKD]--;
		kursdifferenzen[newKD]++;
		if (oldKD === this._ergebnis.bewertung.kursdifferenzMax) {
			if (newKD > oldKD) {
				this._ergebnis.bewertung.kursdifferenzMax = newKD;
			} else {
				if (kursdifferenzen[oldKD] === 0)
					this._ergebnis.bewertung.kursdifferenzMax = newKD;
			}
		}
	}

	private updateAll() : void {
		if (this._fachartmenge_sortierung === 1) {
			this._fachartmenge_sortiert.sort(this._fachartComparator_kursart_fach);
		} else {
			this._fachartmenge_sortiert.sort(this._fachartComparator_fach_kursart);
		}
		for (const idFachart of this._map_fachartID_kurse.keySet()) {
			const kursmenge : List<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kurse, idFachart);
			if (this._fachartmenge_sortierung === 1) {
				kursmenge.sort(this._kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(this._kursComparator_fach_kursart_kursnummer);
			}
		}
		for (const schiene of this._ergebnis.schienen) {
			const kursmenge : List<GostBlockungsergebnisKurs> = schiene.kurse;
			if (this._fachartmenge_sortierung === 1) {
				kursmenge.sort(this._kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(this._kursComparator_fach_kursart_kursnummer);
			}
		}
	}

	/**
	 * Liefert die Anzahl an externen SuS.
	 *
	 * @return die Anzahl an externen SuS.
	 */
	public getAnzahlSchuelerExterne() : number {
		return ListUtils.getCountFiltered(this._parent.daten().schueler, { test : (schueler: Schueler) => this.getOfSchuelerHatStatusExtern(schueler.id) });
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS.
	 *
	 * @return die Anzahl an Dummy-SuS.
	 */
	public getAnzahlSchuelerDummy() : number {
		let summe : number = 0;
		for (const idKurs of this._map_kursID_dummySuS.keySet())
			summe += this.getOfKursAnzahlSchuelerDummy(idKurs);
		return summe;
	}

	/**
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 *
	 * @return den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 */
	public getParent() : GostBlockungsdatenManager | null {
		return this._parent;
	}

	/**
	 * Liefert die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 *
	 * @return die Datenbank-ID der Blockungs. Das ist die ID des Elternteils.
	 */
	public getBlockungsdatenID() : number {
		return this._ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis ohne ungültige Schüler-Kurs-Zuordnungen.
	 * <br>Hinweis: Siehe auch {@link #getErgebnisInklusiveUngueltigerWahlen()}.
	 *
	 * @return das Blockungsergebnis ohne ungültige Schüler-Kurs-Zuordnungen.
	 */
	public getErgebnis() : GostBlockungsergebnis {
		return this._ergebnis;
	}

	/**
	 * Liefert das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 * <br>Hinweis: Siehe auch {@link #getErgebnis()}.
	 *
	 * @return  das Blockungsergebnis inklusive ungültiger Schüler-Kurs-Zuordnungen.
	 */
	public getErgebnisInklusiveUngueltigerWahlen() : GostBlockungsergebnis {
		const copy : GostBlockungsergebnis = new GostBlockungsergebnis();
		copy.blockungID = this._ergebnis.blockungID;
		copy.gostHalbjahr = this._ergebnis.gostHalbjahr;
		copy.id = this._ergebnis.id;
		copy.istAktiv = this._ergebnis.istAktiv;
		copy.name = this._ergebnis.name;
		copy.bewertung = GostBlockungsergebnisManager.copyBewertung(this._ergebnis.bewertung);
		for (const schiene of this._ergebnis.schienen)
			copy.schienen.add(this.copySchiene(schiene));
		for (const e of this._map_schuelerID_ungueltige_kurse.entrySet())
			for (const kurs1 of e.getValue())
				for (const schiene of copy.schienen)
					for (const kurs2 of schiene.kurse)
						if (kurs1.id === kurs2.id)
							kurs2.schueler.add(e.getKey());
		return copy;
	}

	private static copyBewertung(bewertung : GostBlockungsergebnisBewertung) : GostBlockungsergebnisBewertung {
		const c : GostBlockungsergebnisBewertung = new GostBlockungsergebnisBewertung();
		c.anzahlKurseMitGleicherFachartProSchiene = bewertung.anzahlKurseMitGleicherFachartProSchiene;
		c.anzahlKurseNichtZugeordnet = bewertung.anzahlKurseNichtZugeordnet;
		c.anzahlSchuelerKollisionen = bewertung.anzahlSchuelerKollisionen;
		c.anzahlSchuelerNichtZugeordnet = bewertung.anzahlSchuelerNichtZugeordnet;
		c.kursdifferenzHistogramm = GostBlockungsergebnisManager.copyArray(bewertung.kursdifferenzHistogramm);
		c.kursdifferenzMax = bewertung.kursdifferenzMax;
		c.regelVerletzungen = new ArrayList(bewertung.regelVerletzungen);
		return c;
	}

	private static copyArray(a : Array<number>) : Array<number> {
		const c : Array<number> | null = Array(a.length).fill(0);
		System.arraycopy(a, 0, c, 0, a.length);
		return c;
	}

	private copySchiene(schiene : GostBlockungsergebnisSchiene) : GostBlockungsergebnisSchiene {
		const c : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
		return c;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public getOfBewertung1Wert() : number {
		let summe : number = 0;
		summe += this._ergebnis.bewertung.anzahlKurseNichtZugeordnet;
		summe += this._ergebnis.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung1Farbcode() : number {
		const summe : number = this.getOfBewertung1Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public getOfBewertung2Wert() : number {
		let summe : number = 0;
		summe += this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += this._ergebnis.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung2Farbcode() : number {
		const summe : number = this.getOfBewertung2Wert();
		return 1 - 1 / (0.25 * summe + 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public getOfBewertung3Wert() : number {
		return this._ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode() : number {
		let wert : number = this.getOfBewertung3Wert();
		if (wert > 0)
			wert--;
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public getOfBewertung4Wert() : number {
		return this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung4Farbcode() : number {
		const wert : number = this.getOfBewertung4Wert();
		return 1 - 1 / (0.25 * wert + 1);
	}

	/**
	 * Liefert die Anzahl an Schülerkollisionen.<br>
	 * Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies als x-1 Kollisionen gezählt.
	 *
	 * @return die Anzahl an Schülerkollisionen.
	 */
	public getOfBewertungAnzahlKollisionen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerKollisionen;
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse.<br>
	 * Ein Multikurse der über mehrere Schienen geht und gar nicht zugeteilt wurde, wird mehrfach gezählt.
	 *
	 * @return die Anzahl nicht vollständig verteilter Kurse.
	 */
	public getOfBewertungAnzahlNichtZugeordneterKurse() : number {
		return this._ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 *
	 * @return die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public getOfBewertungAnzahlNichtzugeordneterFachwahlen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	/**
	 * Ermittelt das {@link GostFach} für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.<br>
	 * Wirft eine {@link DeveloperNotificationException} falls die ID unbekannt ist.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return Das {@link GostFach}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public getFach(idFach : number) : GostFach {
		return this._parent.faecherManager().getOrException(idFach);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach-ID.<br>
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kurse mit dem angegebenen Fach-ID.
	 * @throws DeveloperNotificationException falls die Fach-ID unbekannt ist.
	 */
	public getOfFachKursmenge(idFach : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachID_kurse, idFach);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public getOfFachAnzahlSchuelerMaennlich(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public getOfFachAnzahlSchuelerWeiblich(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public getOfFachAnzahlSchuelerDivers(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler eines Faches mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public getOfFachAnzahlSchuelerOhneAngabe(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public getOfFachAnzahlSchuelerSchriftlich(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Anzahl aller Schüler des übergebenen Faches mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public getOfFachAnzahlSchuelerMuendlich(idFach : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, 0, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.<br>
	 * <br>Hinweis: Die Kursmenge pro Fachart ist sortiert nach {@link #kursSetSortierungFachKursartNummer()} oder {@link #kursSetSortierungKursartFachNummer()}.
	 * <br>Hinweis: Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param  idFachart  Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursmenge(idFachart : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kurse, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart und beachtet dabei Dummy-SuS von Kursen.
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 * Die Methode beachtet auch Kurse mit Dummy-SuS. <br>
	 * Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param  idFachart  Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursdifferenz der Fachart und beachtet dabei Dummy-SuS von Kursen.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursdifferenz(idFachart : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kursdifferenz, idFachart)!;
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public getOfFachartAnzahlSchuelerMaennlich(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public getOfFachartAnzahlSchuelerWeiblich(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public getOfFachartAnzahlSchuelerDivers(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public getOfFachartAnzahlSchuelerOhneAngabe(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public getOfFachartAnzahlSchuelerSchriftlich(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Anzahl aller Schüler einer Fachart (Fach + Kursart) mit Schriftlichkeit {@link GostSchriftlichkeit#MUENDLICH}.
	 */
	public getOfFachartAnzahlSchuelerMuendlich(idFach : number, idKursart : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, idFach, idKursart, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 * <br>Hinweis: Die Sortierung lässt sich mit {@link #kursSetSortierungFachKursartNummer()} und {@link #kursSetSortierungKursartFachNummer()} ändern.
	 *
	 * @return die Menge aller Facharten (Fach + Kursart) sortiert nach der aktuellen Sortiervariante.
	 */
	public getOfFachartMengeSortiert() : List<number> {
		return this._fachartmenge_sortiert;
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach LK/GK, dann nach der Fachsortierung, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungKursartFachNummer() : void {
		this._fachartmenge_sortierung = 1;
		this.updateAll();
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach der Fachsortierung, dann nach LK/GK, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungFachKursartNummer() : void {
		this._fachartmenge_sortierung = 2;
		this.updateAll();
	}

	/**
	 * Liefert das {@link Schueler}-Objekt zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das {@link Schueler}-Objekt zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die Schüler-ID unbekannt ist.
	 */
	public getSchuelerG(idSchueler : number) : Schueler {
		return this._parent.schuelerGet(idSchueler);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public getOfSchuelerNameVorname(idSchueler : number) : string {
		const schueler : Schueler = this._parent.schuelerGet(idSchueler);
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert die Menge aller Kurse, die dem Schüler zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public getOfSchuelerKursmenge(idSchueler : number) : JavaSet<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kurse, idSchueler);
	}

	/**
	 * Liefert die sortierte Menge aller Kurse, die dem Schüler zugeordnet sind.
	 * <br>Hinweis: Die Sortierung wird mit {@link #kursSetSortierungFachKursartNummer()} und {@link #kursSetSortierungKursartFachNummer()} definiert.
	 * <br>Wirft eine Exception, wenn der ID kein Schüler zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die sortierte Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public getOfSchuelerKursmengeSortiert(idSchueler : number) : List<GostBlockungsergebnisKurs> {
		const list : List<GostBlockungsergebnisKurs> | null = new ArrayList();
		list.addAll(DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kurse, idSchueler));
		if (this._fachartmenge_sortierung === 1)
			list.sort(this._kursComparator_kursart_fach_kursnummer);
		else
			list.sort(this._kursComparator_fach_kursart_kursnummer);
		return list;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public getOfSchuelerKursmengeMitKollisionen(idSchueler : number) : JavaSet<GostBlockungsergebnisKurs> {
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const schiene of this._parent.schieneGetListe()) {
			const kurseDerSchiene : JavaSet<GostBlockungsergebnisKurs> = this._map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, schiene.id);
			if (kurseDerSchiene.size() > 1)
				set.addAll(kurseDerSchiene);
		}
		return set;
	}

	/**
	 * Liefert die Menge aller Fachwahlen eines Schülers, die keinem Kurs zugeordnet sind.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die Menge aller Fachwahlen eines Schülers, die keinem Kurs zugeordnet sind.
	 */
	public getOfSchuelerFachwahlmengeOhneKurszuordnung(idSchueler : number) : List<GostFachwahl> {
		const list : List<GostFachwahl> = this._parent.schuelerGetListeOfFachwahlen(idSchueler);
		const filter : Predicate<GostFachwahl> = { test : (t: GostFachwahl) => (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, t.fachID) === null) };
		return ListUtils.getCopyFiltered(list, filter);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public getOfSchuelerHatNichtwahl(idSchueler : number) : boolean {
		const nIst : number = DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kurse, idSchueler).size();
		const nSoll : number = this._map2D_schuelerID_fachID_kurs.getSubMapSizeOrZero(idSchueler);
		return nIst < nSoll;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl (Fach + Kursart) hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl (Fach + Kursart) hat.
	 */
	public getOfSchuelerHatFachwahl(idSchueler : number, idFach : number, idKursart : number) : boolean {
		return this._parent.schuelerGetHatFachart(idSchueler, idFach, idKursart);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 */
	public getOfSchuelerHatFach(idSchueler : number, idFach : number) : boolean {
		return this._parent.schuelerGetHatFach(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public getOfSchuelerHatKollision(idSchueler : number) : boolean {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kollisionen, idSchueler) > 0;
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers.<br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return die Anzahl an Kollisionen des Schülers.
	 */
	public getOfSchuelerAnzahlKollisionen(idSchueler : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kollisionen, idSchueler)!;
	}

	/**
	 * Liefert die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public getOfSchuelerAnzahlMitKollisionenOderNichtwahlen() : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 3, "", null, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public getOfSchuelerAnzahlMaennlich() : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public getOfSchuelerAnzahlWeiblich() : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public getOfSchuelerAnzahlDivers() : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public getOfSchuelerAnzahlOhneAngabe() : number {
		return this.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 *
	 * @param  idKurs           falls >= 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls >= 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls >= 0 und idFach >= 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 * @param  geschlecht       falls != null, werden die Schüler mit diesem {@link Geschlecht} herausgefiltert.
	 * @param  schriftlichkeit  falls != null, werden die Schüler mit dieser {@link GostSchriftlichkeit} herausgefiltert (isKurs oder idFach/idKursart müssen definiert sein).
	 *
	 * @return die Anzahl der Schüler, die den Filterkriterien entsprechen.
	 */
	public getOfSchuelerAnzahlGefiltert(idKurs : number, idFach : number, idKursart : number, konfliktTyp : number, subString : string, geschlecht : Geschlecht | null, schriftlichkeit : GostSchriftlichkeit | null) : number {
		let summe : number = 0;
		for (const schueler of this._parent.schuelerGetListe())
			if (this.getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht, schriftlichkeit))
				summe++;
		return summe;
	}

	/**
	 * Liefert TRUE, falls sämtliche Fachwahlen aller SuS noch nicht zugeordnet sind.
	 *
	 * @return TRUE, falls sämtliche Fachwahlen aller SuS noch nicht zugeordnet sind.
	 */
	public getOfSchuelerAlleFachwahlenNichtZugeordnet() : boolean {
		return this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet === this._parent.daten().fachwahlen.size();
	}

	/**
	 * Liefert die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 */
	public getOfSchuelerOfSchieneKursmenge(idSchueler : number, idSchiene : number) : JavaSet<GostBlockungsergebnisKurs> {
		return this._map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene);
	}

	/**
	 * Liefert TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 */
	public getOfSchuelerOfSchieneHatKollision(idSchueler : number, idSchiene : number) : boolean {
		return this._map2D_schuelerID_schienenID_kurse.getNonNullOrException(idSchueler, idSchiene).size() > 1;
	}

	/**
	 * Liefert die zu (idSchueler, idFach) die jeweilige Kursart. <br>
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return Die zu (idSchueler, idFach) die jeweilige Kursart.
	 */
	public getOfSchuelerOfFachKursart(idSchueler : number, idFach : number) : GostKursart {
		return this._parent.schuelerGetOfFachKursart(idSchueler, idFach);
	}

	/**
	 * Liefert den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idFach     Die Datenbank-ID des Faches.
	 *
	 * @return den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 */
	public getOfSchuelerOfFachZugeordneterKurs(idSchueler : number, idFach : number) : GostBlockungsergebnisKurs | null {
		return this._map2D_schuelerID_fachID_kurs.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public getOfSchuelerOfKursIstZugeordnet(idSchueler : number, idKurs : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(idSchueler);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	public getOfSchuelerNeuzuordnungMitFixierung(idSchueler : number, fixiereBelegteKurse : boolean) : SchuelerblockungOutput {
		const input : SchuelerblockungInput = new SchuelerblockungInput();
		input.schienen = this._parent.schieneGetAnzahl();
		for (const fachwahl of this._parent.schuelerGetListeOfFachwahlen(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(this._parent.fachwahlGetName(fachwahl));
			const fachartID : number = GostKursart.getFachartIDByFachwahl(fachwahl);
			for (const kursE of this.getOfFachartKursmenge(fachartID)) {
				const kursS : SchuelerblockungInputKurs = new SchuelerblockungInputKurs();
				const idKurs : number = kursE.id;
				kursS.id = idKurs;
				kursS.fach = kursE.fachID;
				kursS.kursart = kursE.kursart;
				kursS.istGesperrt = this.getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs);
				kursS.istFixiert = this.getOfSchuelerOfKursIstFixiert(idSchueler, idKurs) || (fixiereBelegteKurse && this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs));
				DeveloperNotificationException.ifTrue("kursS.istGesperrt && kursS.istFixiert", kursS.istGesperrt && kursS.istFixiert);
				kursS.anzahlSuS = this.getOfKursAnzahlSchueler(idKurs);
				kursS.schienen = this.getOfKursSchienenNummern(idKurs);
				input.kurse.add(kursS);
			}
		}
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	public getOfSchuelerOfKursIstFixiert(idSchueler : number, idKurs : number) : boolean {
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS)) {
			const schuelerID : number = r.parameter.get(0).valueOf();
			const kursID : number = r.parameter.get(1).valueOf();
			if ((schuelerID === idSchueler) && (kursID === idKurs))
				return true;
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 */
	public getOfSchuelerOfKursIstAbiturfach(idSchueler : number, idKurs : number) : boolean {
		const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
		if (fachwahl.abiturfach === null)
			return false;
		return fachwahl.abiturfach >= 1;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 */
	public getOfSchuelerOfKursIstGesperrt(idSchueler : number, idKurs : number) : boolean {
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			const schuelerID : number = r.parameter.get(0).valueOf();
			const kursID : number = r.parameter.get(1).valueOf();
			if ((schuelerID === idSchueler) && (kursID === idKurs))
				return true;
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param subString   Der zu suchende Sub-String.
	 *
	 * @return TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt (Groß- und Kleinschreibung wird dabei ignoriert).
	 */
	public getOfSchuelerHatImNamenSubstring(idSchueler : number, subString : string) : boolean {
		const schueler : Schueler = this.getSchuelerG(idSchueler);
		const text : string = subString.toLowerCase();
		return JavaString.contains(schueler.nachname.toLowerCase(), text) || JavaString.contains(schueler.vorname.toLowerCase(), text);
	}

	/**
	 * Liefert das {@link Geschlecht} des Schülers.<br>
	 * Wirft eine Exception, falls das Enum {@link Geschlecht} nicht definiert ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return das {@link Geschlecht} des Schülers.
	 * @throws DeveloperNotificationException falls das Enum {@link Geschlecht} nicht definiert ist.
	 */
	public getOfSchuelerGeschlechtOrException(idSchueler : number) : Geschlecht {
		const schueler : Schueler = this.getSchuelerG(idSchueler);
		const geschlecht : Geschlecht | null = Geschlecht.fromValue(schueler.geschlecht);
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + idSchueler + " ist nicht definiert!", geschlecht);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 */
	public getOfSchuelerHatStatusExtern(idSchueler : number) : boolean {
		return this.getSchuelerG(idSchueler!).status === SchuelerStatus.EXTERN.id;
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zu den Kurs.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Fachwahl des Schüler passend zu den Kurs.
	 */
	public getOfSchuelerOfKursFachwahl(idSchueler : number, idKurs : number) : GostFachwahl {
		const idFach : number = this.getKursE(idKurs).fachID;
		return this._parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert die Fachwahl des Schüler passend zum Fach.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return die Fachwahl des Schüler passend zum Fach.
	 */
	public getOfSchuelerOfFachFachwahl(idSchueler : number, idFach : number) : GostFachwahl {
		return this._parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert eine nach Kriterien gefilterte Menge aller Schüler.
	 *
	 * @param  idKurs           falls >= 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach           falls >= 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param  idKursart        falls >= 0 und idFach >= 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param  konfliktTyp      falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString        falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine nach Kriterien gefilterte Menge aller Schüler.
	 */
	public getOfSchuelerMengeGefiltert(idKurs : number, idFach : number, idKursart : number, konfliktTyp : number, subString : string) : List<Schueler> {
		const menge : List<Schueler> = new ArrayList();
		for (const schueler of this._parent.schuelerGetListe())
			if (this.getOfSchuelerErfuelltKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, null, null))
				menge.add(schueler);
		return menge;
	}

	/**
	 * Liefert TRUE, falls der Schüler alle definierten Kriterien erfüllt.
	 *
	 * @param idSchueler        Die Datenbank-ID des Schülers.
	 * @param idKurs            Falls >= 0, muss der Schüler in dem Kurs sein.
	 * @param idFach            Falls >= 0, muss der Schüler das Fach haben.
	 * @param idKursart         Falls >= 0, und idFach >= muss der Schüler auch die zugehörige Kursart haben.
	 * @param konfliktTyp       Falls > 0 muss der Schüler "1=Kollisionen", "2=Nichtwahlen" oder "3= Kollisionen und Nichtwahlen" haben.
	 * @param subString         Falls length() > 0 muss der Schüler den Substring im Vor- oder Nachnamen haben.
	 * @param geschlecht        Falls != null, muss der Schüler das definierte Geschlecht haben.
	 * @param schriftlichkeit   Falls != null, muss der Schüler das definierte {@link GostSchriftlichkeit} haben.
	 *
	 * @return TRUE, falls der Schüler alle definierten Kriterien erfüllt.
	 */
	public getOfSchuelerErfuelltKriterien(idSchueler : number, idKurs : number, idFach : number, idKursart : number, konfliktTyp : number, subString : string, geschlecht : Geschlecht | null, schriftlichkeit : GostSchriftlichkeit | null) : boolean {
		if ((konfliktTyp === 1) && (!this.getOfSchuelerHatKollision(idSchueler)))
			return false;
		if ((konfliktTyp === 2) && (!this.getOfSchuelerHatNichtwahl(idSchueler)))
			return false;
		if ((konfliktTyp === 3) && ((!this.getOfSchuelerHatKollision(idSchueler)) && (!this.getOfSchuelerHatNichtwahl(idSchueler))))
			return false;
		if ((subString.length > 0) && (!this.getOfSchuelerHatImNamenSubstring(idSchueler, subString)))
			return false;
		if ((geschlecht !== null) && (this.getOfSchuelerGeschlechtOrException(idSchueler).id !== geschlecht.id))
			return false;
		if (idKurs >= 0) {
			if (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs))
				return false;
			if ((schriftlichkeit !== null) && (schriftlichkeit.getIstSchriftlichOrException() !== this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs).istSchriftlich))
				return false;
		}
		if (idFach >= 0) {
			if (idKursart >= 0) {
				if (!this.getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart))
					return false;
			} else {
				if (!this.getOfSchuelerHatFach(idSchueler, idFach))
					return false;
			}
			if ((schriftlichkeit !== null) && (schriftlichkeit.getIstSchriftlichOrException() !== this.getOfSchuelerOfFachFachwahl(idSchueler, idFach).istSchriftlich))
				return false;
		}
		return true;
	}

	/**
	 * Liefert die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet. <br>
	 * Hinweis 1: Hat ein Schüler keine ungültige Kurse, dann gibt es die ID nicht. <br>
	 * Hinweis 2: Gibt es keine ungültigen Wahlen, so ist die Map leer. <br>
	 *
	 * @return Die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet.
	 */
	public getOfSchuelerMapIDzuUngueltigeKurse() : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		return this._map_schuelerID_ungueltige_kurse;
	}

	/**
	 * Liefert TRUE, falls der Schüler in einer Schiene des Kurses eine Kollision hat.<br>
	 * Die Methode geht davon aus, dass der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler in einer Schiene des Kurses eine Kollision hat.
	 */
	public getOfSchuelerOfKursHatKollision(idSchueler : number, idKurs : number) : boolean {
		if (!this.getOfSchuelerHatKollision(idSchueler))
			return false;
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		for (const idSchiene of kurs.schienen)
			if (this.getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene!).size() > 1)
				return true;
		return false;
	}

	/**
	 * Liefert den {@link GostBlockungKurs} zur übergebenen ID.<br>
	 * Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public getKursG(idKurs : number) : GostBlockungKurs {
		return this._parent.kursGet(idKurs);
	}

	/**
	 * Liefert den {@link GostBlockungsergebnisKurs} zur übergebenen ID.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return den {@link GostBlockungsergebnisKurs} zur übergebenen ID.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public getKursE(idKurs : number) : GostBlockungsergebnisKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_kurs, idKurs);
	}

	/**
	 * Liefert den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 */
	public getOfKursName(idKurs : number) : string {
		return this._parent.kursGetName(idKurs);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public getOfKursOfSchieneIstZugeordnet(idKurs : number, idSchiene : number) : boolean {
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		const schienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert TRUE, falls der Kurs in der Schiene fixiert ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs in der Schiene fixiert ist.
	 */
	public getOfKursOfSchieneIstFixiert(idKurs : number, idSchiene : number) : boolean {
		const nummer : number = this.getSchieneG(idSchiene).nummer;
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if ((regel.parameter.get(0) === idKurs) && (regel.parameter.get(1) === nummer))
				return true;
		return false;
	}

	/**
	 * Liefert zur Kurs-ID die zugehörige Menge aller Schüler-IDs.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return zur Kurs-ID die zugehörige Menge aller Schüler-IDs.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	public getOfKursSchuelerIDmenge(idKurs : number) : JavaSet<number> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_schuelerIDs, idKurs);
	}

	/**
	 * Liefert die Menge aller Schüler-Objekte des Kurses.
	 *
	 * @param idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-Objekte des Kurses.
	 */
	public getOfKursSchuelermenge(idKursID : number) : List<Schueler> {
		const list : List<Schueler> = new ArrayList();
		for (const idSchueler of this.getKursE(idKursID).schueler)
			list.add(this.getSchuelerG(idSchueler!));
		return list;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.<br>
	 * Wirft eine Exception, falls der ID kein Kurs zugeordnet ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Schienenmenge des Kurses.
	 * @throws DeveloperNotificationException falls der ID kein Kurs zugeordnet ist.
	 */
	public getOfKursSchienenmenge(idKurs : number) : JavaSet<GostBlockungsergebnisSchiene> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_schienen, idKurs);
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return ein Array aller Schienen-Nummern des Kurses.
	 */
	public getOfKursSchienenNummern(idKurs : number) : Array<number> {
		const schienenIDs : List<number> = this.getKursE(idKurs).schienen;
		const a : Array<number> | null = Array(schienenIDs.size()).fill(0);
		for (let i : number = 0; i < a.length; i++) {
			const schienenID : number = schienenIDs.get(i).valueOf();
			a[i] = this._parent.schieneGet(schienenID).nummer;
		}
		return a;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Definition: Ein Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public getOfKursHatKollision(idKurs : number) : boolean {
		return this.getOfKursAnzahlKollisionen(idKurs) > 0;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen.<br>
	 * Kollision: Der Schüler muss in mindestens einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public getOfKursAnzahlKollisionen(idKurs : number) : number {
		return this.getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 *
	 * @param idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 */
	public getOfKursSchuelermengeMitKollisionen(idKursID : number) : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schiene of this.getOfKursSchienenmenge(idKursID))
			for (const idSchueler of this.getKursE(idKursID).schueler)
				if (this.getOfSchuelerOfSchieneKursmenge(idSchueler!, schiene.id).size() > 1)
					set.add(idSchueler);
		return set;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 */
	public getOfKursAnzahlSchueler(idKurs : number) : number {
		return this.getKursE(idKurs).schueler.size();
	}

	/**
	 * Liefert die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 */
	public getOfKursAnzahlSchuelerExterne(idKurs : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		return ListUtils.getCountFiltered(kursE.schueler, { test : (idSchueler: number) => this.getOfSchuelerHatStatusExtern(idSchueler) });
	}

	/**
	 * Liefert die Anzahl nicht externer SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl nicht externer SuS die dem Kurs zugeordnet sind.
	 */
	public getOfKursAnzahlSchuelerNichtExtern(idKurs : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		return ListUtils.getCountFiltered(kursE.schueler, { test : (idSchueler: number) => !this.getOfSchuelerHatStatusExtern(idSchueler) });
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS des Kurses.  Dummy-SuS werden durch die Regel mit dem
	 * Typ {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN} einem Kurs zugeordnet.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Dummy-SuS des Kurses.
	 */
	public getOfKursAnzahlSchuelerDummy(idKurs : number) : number {
		return MapUtils.getOrDefault(this._map_kursID_dummySuS, idKurs, 0)!;
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#M}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#M}.
	 */
	public getOfKursAnzahlSchuelerMaennlich(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.M, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#W}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#W}.
	 */
	public getOfKursAnzahlSchuelerWeiblich(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.W, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#D}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#D}.
	 */
	public getOfKursAnzahlSchuelerDivers(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.D, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit dem Geschlecht {@link Geschlecht#X}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler mit dem Geschlecht {@link Geschlecht#X}.
	 */
	public getOfKursAnzahlSchuelerOhneAngabe(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", Geschlecht.X, null);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public getOfKursAnzahlSchuelerSchriftlich(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn mündlich belegt haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn mündlich belegt haben.
	 */
	public getOfKursAnzahlSchuelerMuendlich(idKurs : number) : number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH);
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public getOfKursAnzahlSchienenIst(idKurs : number) : number {
		return this.getOfKursSchienenmenge(idKurs).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public getOfKursAnzahlSchienenSoll(idKurs : number) : number {
		return this.getKursE(idKurs).anzahlSchienen;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 */
	public getOfKursAnzahlSchuelerAbiturLK(idKurs : number) : number {
		let summe : number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler!, idKurs);
			if ((fachwahl.abiturfach !== null) && ((fachwahl.abiturfach === 1) || (fachwahl.abiturfach === 2)))
				summe++;
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 3 gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 3 gewählt haben.
	 */
	public getOfKursAnzahlSchuelerAbitur3(idKurs : number) : number {
		let summe : number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler!, idKurs);
			if ((fachwahl.abiturfach !== null) && (fachwahl.abiturfach === 3))
				summe++;
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 4 gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 4 gewählt haben.
	 */
	public getOfKursAnzahlSchuelerAbitur4(idKurs : number) : number {
		let summe : number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler!, idKurs);
			if ((fachwahl.abiturfach !== null) && (fachwahl.abiturfach === 4))
				summe++;
		}
		return summe;
	}

	/**
	 * Liefert TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs keine Schüler enthält und somit ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public getOfKursRemoveAllowed(idKurs : number) : boolean {
		return this.getOfKursAnzahlSchueler(idKurs) === 0;
	}

	/**
	 * Liefert die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler eines Kurses, die noch nicht fixiert sind.
	 */
	public getOfKursMengeAllerNichtFixiertenSchueler(idKurs : number) : List<Schueler> {
		const list : List<Schueler> = new ArrayList();
		for (const schueler of this.getOfKursSchuelermenge(idKurs))
			if (!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs))
				list.add(schueler);
		return list;
	}

	/**
	 * Liefert die Menge aller Schüler eines Kurses, die noch nicht fixiert sind und den Kurs als Abiturfach (1, 2, 3 oder 4) gewählt haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler eines Kurses, die noch nicht fixiert sind und den Kurs als Abiturfach (1, 2, 3 oder 4) gewählt haben.
	 */
	public getOfKursMengeAllerNichtFixiertenAbiturSchueler(idKurs : number) : List<Schueler> {
		const list : List<Schueler> = new ArrayList();
		for (const schueler of this.getOfKursSchuelermenge(idKurs))
			if ((!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs)) && (this.getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs)))
				list.add(schueler);
		return list;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 */
	public getMappingKursIDSchuelerIDs() : JavaMap<number, JavaSet<number>> {
		return this._map_kursID_schuelerIDs;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public getMappingKursIDSchienenmenge() : JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> {
		return this._map_kursID_schienen;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public getMengeDerKurseMitKollisionen() : JavaSet<GostBlockungsergebnisKurs> {
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const kurs of this._map_kursID_kurs.values())
			if (this.getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert die Regel-Menge aller aktuellen Kurs-Schienen-Fixierungen.
	 *
	 * @return die Regel-Menge aller aktuellen Kurs-Schienen-Fixierungen.
	 */
	public regelGetMengeAllerKursSchienenFixierungen() : List<GostBlockungRegel> {
		return new ArrayList(this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE));
	}

	/**
	 * Liefert die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Kurs-Schienen-Fixierungen eines bestimmten Kurses.
	 */
	public regelGetMengeAnKursSchienenFixierungenDesKurses(idKurs : number) : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (regel.parameter.get(0) === idKurs)
				list.add(regel);
		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Kurs-Schienen-Fixierungen einer bestimmten Kursmenge.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Regel-Menge aller Kurs-Schienen-Fixierungen einer bestimmten Kursmenge.
	 */
	public regelGetMengeAnKursSchienenFixierungenDerKurse(listeDerKursIDs : List<number>) : List<GostBlockungRegel> {
		const setKursIDs : HashSet<number> = new HashSet(listeDerKursIDs);
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE))
			if (setKursIDs.contains(regel.parameter.get(0)))
				list.add(regel);
		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen.
	 */
	public regelGetMengeAllerSchuelerKursFixierungen() : List<GostBlockungRegel> {
		return new ArrayList(this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS));
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen des übergebenen Kurses.
	 */
	public regelGetMengeAllerSchuelerKursFixierungenDesKurses(idKurs : number) : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			if (regel.parameter.get(1) === idKurs)
				list.add(regel);
		return list;
	}

	/**
	 * Liefert die Regel-Menge aller Schüler-Kurs-Fixierungen der übergebenen Kurse.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Regel-Menge aller Schüler-Kurs-Fixierungen der übergebenen Kurse.
	 */
	public regelGetMengeAllerSchuelerKursFixierungenDerKurse(listeDerKursIDs : List<number>) : List<GostBlockungRegel> {
		const setKursIDs : HashSet<number> = new HashSet(listeDerKursIDs);
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS))
			if (setKursIDs.contains(regel.parameter.get(1)))
				list.add(regel);
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 */
	public regelGetDummyMengeAllerKursSchienenFixierungen() : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const kurs of this._map_kursID_kurs.values())
			for (const schiene of this.getOfKursSchienenmenge(kurs.id))
				if (!this.getOfKursOfSchieneIstFixiert(kurs.id, schiene.id)) {
					const schienenNr : number = this._parent.schieneGet(schiene.id).nummer;
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
					regel.parameter.add(kurs.id);
					regel.parameter.add(schienenNr);
					list.add(regel);
				}
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller Kurs-Schienen-Fixierungen der übergebenen Kurse.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller Kurs-Schienen-Fixierungen der übergebenen Kurse.
	 */
	public regelGetDummyMengeAnKursSchienenFixierungen(listeDerKursIDs : List<number>) : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const idKurs of listeDerKursIDs)
			for (const schiene of this.getOfKursSchienenmenge(idKurs!))
				if (!this.getOfKursOfSchieneIstFixiert(idKurs!, schiene.id)) {
					const schienenNr : number = this._parent.schieneGet(schiene.id).nummer;
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
					regel.parameter.add(idKurs);
					regel.parameter.add(schienenNr);
					list.add(regel);
				}
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen.
	 */
	public regelGetDummyMengeAllerSchuelerKursFixierungen() : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const kurs of this._map_kursID_kurs.values())
			for (const schueler of this.getOfKursSchuelermenge(kurs.id))
				if (!this.getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id)) {
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
					regel.parameter.add(schueler.id);
					regel.parameter.add(kurs.id);
					list.add(regel);
				}
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen der Abiturkurse.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen der Abiturkurse.
	 */
	public regelGetDummyMengeAllerSchuelerAbiturKursFixierungen() : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const kurs of this._map_kursID_kurs.values())
			for (const schueler of this.getOfKursSchuelermenge(kurs.id))
				if ((this.getOfSchuelerOfKursIstAbiturfach(schueler.id, kurs.id)) && (!this.getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id))) {
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
					regel.parameter.add(schueler.id);
					regel.parameter.add(kurs.id);
					list.add(regel);
				}
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge.
	 */
	public regelGetDummyMengeAnKursSchuelerFixierungen(listeDerKursIDs : List<number>) : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const idKurs of listeDerKursIDs)
			for (const schueler of this.getOfKursSchuelermenge(idKurs!))
				if (!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs!)) {
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
					regel.parameter.add(schueler.id);
					regel.parameter.add(idKurs);
					list.add(regel);
				}
		return list;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge, welche als Abiturfach gewählt wurden.
	 * <br>Hinweis: Falls ein Schüler bereits fixierte Kurse hat, werden dazu keine Regeln erzeugt.
	 *
	 * @param listeDerKursIDs  Die Liste aller Kurs-IDs.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Schüler-Kurs-Fixierungen einer bestimmten Kursmenge, welche als Abiturfach gewählt wurden.
	 */
	public regelGetDummyMengeAnAbiturKursSchuelerFixierungen(listeDerKursIDs : List<number>) : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const idKurs of listeDerKursIDs)
			for (const schueler of this.getOfKursSchuelermenge(idKurs!))
				if ((this.getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs!)) && (!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs!))) {
					const regel : GostBlockungRegel = new GostBlockungRegel();
					regel.id = -1;
					regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
					regel.parameter.add(schueler.id);
					regel.parameter.add(idKurs);
					list.add(regel);
				}
		return list;
	}

	private static regelGetListeToggleFilteredBetween(list : List<GostBlockungKurs>, kursA : GostBlockungKurs, kursB : GostBlockungKurs) : List<GostBlockungKurs> {
		const result : List<GostBlockungKurs> = new ArrayList();
		let foundA : boolean = false;
		let foundB : boolean = false;
		for (const kursG of list) {
			if (kursG as unknown === kursA as unknown)
				foundA = true;
			if (kursG as unknown === kursB as unknown)
				foundB = true;
			if (foundA || foundB)
				result.add(kursG);
			if (foundA && foundB)
				break;
		}
		return result;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Sperrung in einem Auswahl-Rechteck ändern soll.
	 */
	public regelGetListeToggleSperrung(list : List<GostBlockungKurs>, kursA : GostBlockungKurs, kursB : GostBlockungKurs, schieneA : GostBlockungSchiene, schieneB : GostBlockungSchiene) : List<GostBlockungRegel> {
		const min : number = Math.min(schieneA.nummer, schieneB.nummer);
		const max : number = Math.max(schieneA.nummer, schieneB.nummer);
		const regeln : List<GostBlockungRegel> = new ArrayList();
		for (const kursG of GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (let nr : number = min; nr <= max; nr++)
				regeln.add(this._parent.regelGetRegelOrDummyKursGesperrtInSchiene(kursG.id, nr));
		return regeln;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schienen-Fixierung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 *
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schienen-Fixierung in einem Auswahl-Rechteck ändern soll.
	 */
	public regelGetListeToggleKursfixierung(list : List<GostBlockungKurs>, kursA : GostBlockungKurs, kursB : GostBlockungKurs, schieneA : GostBlockungSchiene, schieneB : GostBlockungSchiene) : List<GostBlockungRegel> {
		const min : number = Math.min(schieneA.nummer, schieneB.nummer);
		const max : number = Math.max(schieneA.nummer, schieneB.nummer);
		const regeln : List<GostBlockungRegel> = new ArrayList();
		for (const kursG of GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_schienen, kursG.id)) {
				const schieneG : GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if ((schieneG.nummer >= min) && (schieneG.nummer <= max))
					regeln.add(this._parent.regelGetRegelOrDummyKursFixierungInSchiene(kursG.id, schieneG.nummer));
			}
		return regeln;
	}

	/**
	 * Liefert eine Liste von Regeln, welche den Status der Kurs-Schueler-Fixierung in einem Auswahl-Rechteck ändern soll.
	 * <br>Hinweis: Die Regeln sind vom Typ {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS}. Eine negative ID steht
	 * symbolisch für eine Regel, die noch nicht existiert, andernfalls erhält man eine existierende Regel. Die GUI kann selbst
	 * entscheiden, wie sie mit den Regeln umgeht (toggle, create, delete).
	 * <br>Hinweis: Wenn ein Multi-Kurs zum Teil im Auswahl-Rechteck liegt, wird der Kurs ebenso beachtet.
	 *
	 * @param list      Die aktuelle sortierte Liste der GUI.
	 * @param kursA     Der erste oder der letzte Kurs der Auswahl.
	 * @param kursB     Der erste oder der letzte Kurs der Auswahl.
	 * @param schieneA  Die erste oder letzte Schiene der Auswahl.
	 * @param schieneB  Die erste oder letzte Schiene der Auswahl.
	 *
	 * @return eine Liste von Regeln, welche den Status der Kurs-Schueler-Fixierung in einem Auswahl-Rechteck ändern soll.
	 */
	public regelGetListeToggleSchuelerfixierung(list : List<GostBlockungKurs>, kursA : GostBlockungKurs, kursB : GostBlockungKurs, schieneA : GostBlockungSchiene, schieneB : GostBlockungSchiene) : List<GostBlockungRegel> {
		const min : number = Math.min(schieneA.nummer, schieneB.nummer);
		const max : number = Math.max(schieneA.nummer, schieneB.nummer);
		const regeln : List<GostBlockungRegel> = new ArrayList();
		for (const kursG of GostBlockungsergebnisManager.regelGetListeToggleFilteredBetween(list, kursA, kursB))
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._map_kursID_schienen, kursG.id)) {
				const schieneG : GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if ((schieneG.nummer >= min) && (schieneG.nummer <= max)) {
					const kursE : GostBlockungsergebnisKurs = this.getKursE(kursG.id);
					for (const idSchueler of kursE.schueler)
						regeln.add(this._parent.regelGetRegelOrDummySchuelerInKursFixierung(idSchueler, kursE.id));
					break;
				}
			}
		return regeln;
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.<br>
	 * Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.<br>
	 * Wirft eine DeveloperNotificationException, falls die ID unbekannt ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public getSchieneG(idSchiene : number) : GostBlockungSchiene {
		return this._parent.schieneGet(idSchiene);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die ID unbekannt ist.
	 */
	public getSchieneE(idSchiene : number) : GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_schiene, idSchiene);
	}

	/**
	 * Liefert das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.<br>
	 * Wirft eine {@link DeveloperNotificationException} falls eine solche Schiene nicht existiert.
	 *
	 * @param nrSchiene Die Nummer der Schiene.
	 *
	 * @return das zur Nummer zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	public getSchieneEmitNr(nrSchiene : number) : GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenNr_schiene, nrSchiene);
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public getMengeAllerSchienen() : List<GostBlockungsergebnisSchiene> {
		return this._ergebnis.schienen;
	}

	/**
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	public getMengeDerSchienenMitKollisionen() : JavaSet<GostBlockungsergebnisSchiene> {
		return CollectionUtils.toFilteredHashSet(this._map_schienenID_schiene.values(), { test : (s: GostBlockungsergebnisSchiene) => this.getOfSchieneHatKollision(s.id) });
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.<br>
	 * Hinweis: Falls ein Schüler mehrfach in der Schiene ist, also mit Kollisionen, wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.
	 */
	public getOfSchieneAnzahlSchueler(idSchiene : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_schuelerAnzahl, idSchiene)!;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public getOfSchieneAnzahl() : number {
		return this._map_schienenID_schiene.size();
	}

	/**
	 * Liefert TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public getOfSchieneHatKollision(idSchiene : number) : boolean {
		return this.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene.<br>
	 * Hinweis Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schüler-Kollisionen der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_kollisionen, idSchiene)!;
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneSchuelermengeMitKollisionen(idSchiene : number) : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schuelerID of this._map_schuelerID_kollisionen.keySet())
			if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, idSchiene).size() > 1)
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneAnzahlKursmengeMitKollisionen(idSchiene : number) : number {
		return this.getOfSchieneKursmengeMitKollisionen(idSchiene).size();
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneKursmengeMitKollisionen(idSchiene : number) : JavaSet<GostBlockungsergebnisKurs> {
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const kurs of this.getSchieneE(idSchiene).kurse)
			if (this.getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert die sortierte Menge an Kursen einer bestimmten Schiene.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die sortierte Menge an Kursen einer bestimmten Schiene.
	 */
	public getOfSchieneKursmengeSortiert(idSchiene : number) : List<GostBlockungsergebnisKurs> {
		return this.getSchieneE(idSchiene).kurse;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.<br>
	 * Kriterium: Es dürfen keine Kurse der Schiene zugeordnet sein.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public getOfSchieneRemoveAllowed(idSchiene : number) : boolean {
		return this.getSchieneE(idSchiene).kurse.isEmpty();
	}

	/**
	 * Liefert die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 *
	 * @return die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 */
	public getOfSchieneMaxKursanzahl() : number {
		let max : number = 0;
		for (const schiene of this._ergebnis.schienen)
			max = Math.max(max, schiene.kurse.size());
		return max;
	}

	/**
	 * Liefert die Anzahl an externen SuS der Schiene.
	 * <br>Hinweis: Ist ein Schüler mehrfach in der Schiene (Kollision) wird er auch mehrfach gezählt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an externen SuS der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerExterne(idSchiene : number) : number {
		let summe : number = 0;
		for (const kurs of this.getSchieneE(idSchiene).kurse)
			for (const idSchueler of kurs.schueler)
				if (this.getOfSchuelerHatStatusExtern(idSchueler))
					summe++;
		return summe;
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS der Schiene.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Dummy-SuS der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerDummy(idSchiene : number) : number {
		let summe : number = 0;
		for (const kurs of this.getSchieneE(idSchiene).kurse)
			summe += this.getOfKursAnzahlSchuelerDummy(kurs.id);
		return summe;
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene.
	 * Die Schiene wird anhand ihrer Nummer (nicht anhand der Datenbank-ID) identifiziert.
	 *
	 * @param  kursID      Die Datenbank-ID des Kurses.
	 * @param  schienenNr  Die Nummer der Schiene (nicht die Datenbank-ID).
	 *
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchienenNr(kursID : number, schienenNr : number) : void {
		const eSchiene : GostBlockungsergebnisSchiene = DeveloperNotificationException.ifMapGetIsNull(this._map_schienenNr_schiene, schienenNr);
		this.stateKursSchieneHinzufuegen(kursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 *
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  idSchiene                 Die Datenbank-ID der Schiene.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @throws DeveloperNotificationException falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchiene(idKurs : number, idSchiene : number, hinzufuegenOderEntfernen : boolean) : void {
		if (hinzufuegenOderEntfernen)
			this.stateKursSchieneHinzufuegen(idKurs, idSchiene);
		else
			this.stateKursSchieneEntfernen(idKurs, idSchiene);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 *
	 * @param  idSchueler                Die Datenbank-ID des Schülers.
	 * @param  idKurs                    Die Datenbank-ID des Kurses.
	 * @param  hinzufuegenOderEntfernen  TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @throws DeveloperNotificationException  falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setSchuelerKurs(idSchueler : number, idKurs : number, hinzufuegenOderEntfernen : boolean) : void {
		if (hinzufuegenOderEntfernen)
			this.stateSchuelerKursHinzufuegen(idSchueler, idKurs);
		else
			this.stateSchuelerKursEntfernen(idSchueler, idKurs);
	}

	/**
	 * Geht die übergebene Fach-Zuordnungen (Fach --> Kurs) eines Schülers durch und
	 * setzt aktualisiert Veränderung die Kurs-Schüler-Zuordnung.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param zuordnung   Die gewünschte Zuordnung.
	 */
	public setSchuelerNeuzuordnung(idSchueler : number, zuordnung : SchuelerblockungOutput) : void {
		for (const z of zuordnung.fachwahlenZuKurs) {
			const kursV : GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			const kursN : GostBlockungsergebnisKurs | null = z.kursID < 0 ? null : this.getKursE(z.kursID);
			if (kursV as unknown !== kursN as unknown) {
				if (kursV !== null)
					this.setSchuelerKurs(idSchueler, kursV.id, false);
				if (kursN !== null)
					this.setSchuelerKurs(idSchueler, kursN.id, true);
			}
		}
	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddSchieneByID(idSchiene : number) : void {
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.schieneGetExistiert(idSchiene));
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls die Schiene noch Kurszuordnungen hat.
	 */
	public setRemoveSchieneByID(idSchiene : number) : void {
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager entfernt werden!", this._parent.schieneGetExistiert(idSchiene));
		const nKurse : number = this.getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + idSchiene + " hat noch " + nKurse + " Kurse!", nKurse > 0);
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebene Regel hinzu.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddRegelByID(idRegel : number) : void {
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.regelGetExistiert(idRegel));
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebenen Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge der Regeln, welche hinzugefügt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddRegelmenge(regelmenge : List<GostBlockungRegel>) : void {
		for (const regel of regelmenge)
			DeveloperNotificationException.ifTrue("Die Regel " + regel.id + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.regelGetExistiert(regel.id));
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Regel.
	 *
	 * @param  idRegel  Die Datenbank-ID der Regel.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveRegelByID(idRegel : number) : void {
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager entfernt werden!", this._parent.regelGetExistiert(idRegel));
		this.stateRevalidateEverything();
	}

	/**
	 * Entfernt die übergebenen Regeln.
	 *
	 * @param regelmenge  Die Menge der Regeln, welche entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException  falls die Regel nicht zuerst im Datenmanager entfernt wurde.
	 */
	public setRemoveRegelmenge(regelmenge : List<GostBlockungRegel>) : void {
		for (const regel of regelmenge)
			DeveloperNotificationException.ifTrue("Die Regel " + regel.id + " muss erst beim Datenmanager entfernt werden!", this._parent.regelGetExistiert(regel.id));
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public setAddKursByID(idKurs : number) : void {
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.kursGetExistiert(idKurs));
		const kurs : GostBlockungKurs = this._parent.kursGet(idKurs);
		const nSchienen : number = this._parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);
		this.stateRevalidateEverything();
		for (let nr : number = 1; nr <= kurs.anzahlSchienen; nr++)
			this.setKursSchienenNr(idKurs, nr);
	}

	/**
	 * Löscht den übergebenen Kurs. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveKursByID(idKurs : number) : void {
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager entfernt werden!", this._parent.kursGetExistiert(idKurs));
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		for (const schienenID of kurs.schienen)
			this.getSchieneE(schienenID!).kurse.remove(kurs);
		this.stateRevalidateEverything();
	}

	/**
	 * Verschiebt alles SuS von pKursID2delete nach pKursID1keep und
	 * löscht dann den Kurs mit der ID beim {@link GostBlockungsdatenManager},
	 * anschließend in diesem Manager.
	 *
	 * @param  idKursID1keep    Die Datenbank-ID des Kurses, der erhalten bleibt.
	 * @param  idKursID2delete  Die Datenbank-ID des Kurses, der gelöscht wird.
	 */
	public setMergeKurseByID(idKursID1keep : number, idKursID2delete : number) : void {
		const kursDelete : GostBlockungsergebnisKurs = this.getKursE(idKursID2delete);
		const kursKeep : GostBlockungsergebnisKurs = this.getKursE(idKursID1keep);
		kursKeep.schueler.addAll(kursDelete.schueler);
		for (const schienenID of kursDelete.schienen)
			this.getSchieneE(schienenID!).kurse.remove(kursDelete);
		this._parent.kursRemoveByID(idKursID2delete);
		this.stateRevalidateEverything();
	}

	/**
	 * Erzeugt einen neuen Kurs2 beim {@link GostBlockungsdatenManager},
	 * dann bei diesem Manager und
	 * verschiebt alle SuS des übergebenen Arrays von Kurs1 nach Kurs2.
	 *
	 * @param  kurs1alt     Der Kurs, der gesplittet wird.
	 * @param  kurs2neu     Der Kurs, der neu erzeugt wird.
	 * @param  susVon1nach2 Die Datenbank-IDs der Schüler, die verschoben werden sollen.
	 */
	public setSplitKurs(kurs1alt : GostBlockungKurs, kurs2neu : GostBlockungKurs, susVon1nach2 : Array<number>) : void {
		this._parent.kursAdd(kurs2neu);
		this.setAddKursByID(kurs2neu.id);
		for (const schuelerID of susVon1nach2) {
			this.stateSchuelerKursEntfernen(schuelerID, kurs1alt.id);
			this.stateSchuelerKursHinzufuegen(schuelerID, kurs2neu.id);
		}
	}

	/**
	 * Verändert die Schienenanzahl eines Kurses. Dies ist nur bei einer Blockungsvorlage erlaubt.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 * @param  anzahlSchienenNeu Die neue Schienenanzahl des Kurses.
	 *
	 * @throws DeveloperNotificationException Falls ein unerwarteter Fehler passiert.
	 */
	public patchOfKursSchienenAnzahl(idKurs : number, anzahlSchienenNeu : number) : void {
		const kursG : GostBlockungKurs = this.getKursG(idKurs);
		const kursE : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const nSchienen : number = this._parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Die Schienenanzahl eines Kurses darf nur bei der Blockungsvorlage verändert werden!", !this._parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Der GostBlockungKurs hat " + kursG.anzahlSchienen + " Schienen, der GostBlockungsergebnisKurs hat hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen !== kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen === 0);
		DeveloperNotificationException.ifTrue("Ein Kurs muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!", anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, der Kurs kann nicht " + anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			let hinzugefuegt : boolean = false;
			for (let nr : number = 1; (nr <= this._map_schienenNr_schiene.size()) && (!hinzugefuegt); nr++) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					this.setKursSchiene(idKurs, schiene.id, true);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine freie Schiene für den Kurs " + idKurs + " gefunden!", !hinzugefuegt);
		}
		while (anzahlSchienenNeu < kursG.anzahlSchienen) {
			let entfernt : boolean = false;
			for (let nr : number = this._map_schienenNr_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					this.setKursSchiene(idKurs, schiene.id, false);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine belegte Schiene von Kurs " + idKurs + " gefunden!", !entfernt);
		}
	}

	/**
	 * Informiert den Manager, dass sich bei mindestens einem Kurs die Lehrkraft geändert hat.
	 * Führt zu einer Revalidierung der Bewertung des Ergebnisses.
	 */
	public patchOfKursLehrkaefteChanged() : void {
		this.stateRevalidateEverything();
	}

	/**
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public debug(logger : Logger) : void {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (const fachartID of this._map_fachartID_kurse.keySet()) {
			logger.logLn("FachartID = " + fachartID! + " (KD = " + this.getOfFachartKursdifferenz(fachartID!) + ")");
			for (const kurs of this.getOfFachartKursmenge(fachartID!)) {
				logger.logLn("    " + this.getOfKursName(kurs.id)! + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + this._ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(this._ergebnis.bewertung.kursdifferenzHistogramm)!);
		logger.modifyIndent(-4);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisManager(obj : unknown) : GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}

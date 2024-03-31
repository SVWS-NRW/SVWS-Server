import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungsergebnisBewertung, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../java/util/Comparator';
import type { Predicate } from '../../../java/util/function/Predicate';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { SchuelerblockungInput } from '../../../core/data/kursblockung/SchuelerblockungInput';
import type { List } from '../../../java/util/List';
import { HashSet } from '../../../java/util/HashSet';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { SetUtils } from '../../../core/utils/SetUtils';
import { SchuelerblockungInputKurs } from '../../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungAlgorithmus } from '../../../core/kursblockung/SchuelerblockungAlgorithmus';
import { CollectionUtils } from '../../../core/utils/CollectionUtils';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { MapUtils } from '../../../core/utils/MapUtils';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from '../../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { PairNN } from '../../../core/adt/PairNN';
import { DTOUtils } from '../../../core/utils/DTOUtils';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisSchiene, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import type { JavaSet } from '../../../java/util/JavaSet';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';
import { SchuelerStatus } from '../../../core/types/SchuelerStatus';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnungUpdate } from '../../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnungUpdate';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { Geschlecht } from '../../../core/types/Geschlecht';
import { Pair } from '../../../core/adt/Pair';
import { GostFach } from '../../../core/data/gost/GostFach';
import { SchuelerblockungOutput } from '../../../core/data/kursblockung/SchuelerblockungOutput';
import { GostBlockungsdatenManager, cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager } from '../../../core/utils/gost/GostBlockungsdatenManager';
import { GostBlockungsergebnis, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostBlockungRegelUpdate } from '../../../core/data/gost/GostBlockungRegelUpdate';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { ListUtils } from '../../../core/utils/ListUtils';

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
	 * Liste aller Fehlermeldungen.
	 */
	private readonly _fehlermeldungen : List<string> = new ArrayList();

	/**
	 * Set aller Schienen-IDs.
	 */
	private readonly _schienenIDset : JavaSet<number> = new HashSet<number>();

	/**
	 * Set aller Kurs-IDs.
	 */
	private readonly _kursIDset : JavaSet<number> = new HashSet<number>();

	/**
	 * Set aller Schienen-Nummern.
	 */
	private readonly _schienenNRset : JavaSet<number> = new HashSet<number>();

	/**
	 * Set aller Fach-IDs.
	 */
	private readonly _fachIDset : JavaSet<number> = new HashSet<number>();

	/**
	 * Set aller Schüler-IDs.
	 */
	private readonly _schuelerIDset : JavaSet<number> = new HashSet<number>();

	/**
	 * Map von Schienen-ID nach {@link GostBlockungsergebnisSchiene}.
	 */
	private readonly _schienenID_to_schiene : JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap();

	/**
	 * Map von Schienen-NR nach {@link GostBlockungsergebnisSchiene}.
	 */
	private readonly _schienenNR_to_schiene : JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap();

	/**
	 * Map von Kurs-ID nach {@link GostBlockungsergebnisKurs}.
	 */
	private readonly _kursID_to_kurs : JavaMap<number, GostBlockungsergebnisKurs> = new HashMap();

	/**
	 * Map von Schueler-ID nach {@link Schueler}.
	 */
	private readonly _schuelerID_to_schueler : JavaMap<number, Schueler> = new HashMap();

	/**
	 * Map von Schienen-ID nach Long-Set (von Kursen).
	 */
	private readonly _schienenID_to_kursIDSet : JavaMap<number, JavaSet<number>> = new HashMap();

	/**
	 * Map von Schueler-ID nach {@link GostBlockungsergebnisKurs}-Set (Kurse des Schüler, die aufgrund der aktuellen Fachwahlen ungültig sind).
	 */
	private readonly _schuelerID_to_ungueltigeKurseSet : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Map von Kurs-ID nach Long-Set (von Schülern).
	 */
	private readonly _kursID_to_schuelerIDSet : JavaMap<number, JavaSet<number>> = new HashMap();

	/**
	 * Map von Fach-ID nach {@link GostBlockungsergebnisKurs}-List.
	 */
	private readonly _fachID_to_kurseList : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Map von Kurs-ID nach {@link GostBlockungsergebnisSchiene}-Set.
	 */
	private readonly _kursID_to_schienenSet : JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> = new HashMap();

	/**
	 * Map von Kurs-ID nach Integer (Anzahl an externen SuS).
	 */
	private readonly _kursID_to_dummySuS : JavaMap<number, number> = new HashMap();

	/**
	 * Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse der selben Fachart).
	 */
	private readonly _fachartID_to_kurseList : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Map von Schüler-ID nach {@link GostBlockungsergebnisKurs}-Set.
	 */
	private readonly _schuelerID_to_kurseSet : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Menge aller Fachart-IDs sortiert nach der aktuellen Sortiervariante.
	 */
	private readonly _fachartIDList_sortiert : List<number> = new ArrayList();

	/**
	 * Map von Fachart-ID nach Integer (Kursdifferenz der Fachart).
	 */
	private readonly _fachartID_to_kursdifferenz : JavaMap<number, number> = new HashMap();

	/**
	 * Map von Schienen-ID nach Integer (Anzahl an Kollisionen in der Schiene).
	 */
	private readonly _schienenID_to_kollisionen : JavaMap<number, number> = new HashMap();

	/**
	 * Map von Schienen-ID nach Integer (Anzahl der SuS in der Schiene).
	 */
	private readonly _schienenID_to_susAnzahl : JavaMap<number, number> = new HashMap();

	/**
	 * Map von Schüler-ID nach Map von Schienen-ID nach {@link GostBlockungsergebnisKurs}-Set (Alle Kurse des Schülers in der Schiene).
	 */
	private readonly _schuelerID_schienenID_to_kurseSet : HashMap2D<number, number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap2D();

	/**
	 * Map von Schienen-ID nach Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse pro Schiene und Fachart).
	 */
	private readonly _schienenID_fachartID_to_kurseList : HashMap2D<number, number, List<GostBlockungsergebnisKurs>> = new HashMap2D();

	/**
	 * Map von Schüler-ID Integer (Summe aller Kollisionen des Schülers).
	 */
	private readonly _schuelerID_to_kollisionen : JavaMap<number, number> = new HashMap();

	/**
	 * Map von Schüler-ID nach Map von Fach-ID nach {@link GostBlockungsergebnisKurs} (Die zugeordnete Wahl des Schülers in dem Fach, auch NULL möglich).
	 */
	private readonly _schuelerID_fachID_to_kurs_or_null : HashMap2D<number, number, GostBlockungsergebnisKurs | null> = new HashMap2D();

	/**
	 * Von Regel-ID Regel-TYP nach List (alle Regelverletzungen des Typs als String-Menge).
	 */
	private readonly _regelTyp_to_verletzungList : JavaMap<number, List<string>> = new HashMap();

	/**
	 * Von Regel-ID nach String (Beschreibung der Regelverletzung).
	 */
	private readonly _regelID_to_verletzungString : JavaMap<number, string> = new HashMap();

	/**
	 * Textuelle Darstellung aller Regelverletzungen der definierten Regeln.
	 */
	private _regelverletzungen_tooltip1_regeln : string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Wahlkonflikte.
	 */
	private _regelverletzungen_tooltip2_wahlkonflikte : string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Kursdifferenzen.
	 */
	private _regelverletzungen_tooltip3_kursdifferenzen : string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Fächerparallelität.
	 */
	private _regelverletzungen_tooltip4_faecherparallelitaet : string = "";

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
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _kursComparator_kursart_fach_kursnummer : Comparator<GostBlockungsergebnisKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _kursComparator_fach_kursart_kursnummer : Comparator<GostBlockungsergebnisKurs>;

	private _bewertung3_KD_nur_LK : number = 0;

	private _bewertung3_KD_nur_GK : number = 0;

	private _bewertung3_KD_nur_REST : number = 0;


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

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	private stateRevalidateEverything() : void {
		this.stateClear(this._ergebnis, this._ergebnis.id);
	}

	private stateClear(pOld : GostBlockungsergebnis, pGostBlockungsergebnisID : number) : void {
		this._ergebnis = new GostBlockungsergebnis();
		this._ergebnis.id = pGostBlockungsergebnisID;
		this._ergebnis.blockungID = this._parent.getID();
		this._ergebnis.name = pOld.name;
		this._ergebnis.gostHalbjahr = this._parent.daten().gostHalbjahr;
		this._ergebnis.istAktiv = pOld.istAktiv;
		this._fehlermeldungen.clear();
		this.update_0_schienenIDset_schienenNRset();
		this.update_0_kursIDset();
		this.update_0_fachIDset();
		this.update_0_schuelerIDset();
		this.update_0_schienenID_to_schiene_schienenNR_to_schiene();
		this.update_0_kursID_to_kurs();
		this.update_0_schuelerID_to_schueler();
		this.update_0_schienenID_to_kursIDSet(pOld);
		this.update_1_kursID_to_schuelerIDSet_schuelerID_to_ungueltigeKurseSet(pOld);
		this.update_1_kursID_to_dummySuS();
		this.update_1_fachID_to_kurseList();
		this.update_1_kursID_to_schienenSet();
		this.update_1_fachartID_to_kurseList();
		this.update_2_schuelerID_to_kurseSet();
		this.update_2_fachartIDList_sortiert();
		this.update_2_fachartID_to_kursdifferenz();
		this.update_2_schienenID_to_kollisionen();
		this.update_2_schienenID_to_susAnzahl();
		this.update_2_schuelerID_schienenID_to_kurseSet();
		this.update_2_schienenID_fachartID_to_kurseList();
		this.update_3_schuelerID_to_kollisionen();
		this.update_3_schuelerID_fachID_to_kurs_or_null();
		if (!this._fehlermeldungen.isEmpty()) {
			console.log(JSON.stringify("Es sind Fehler aufgetreten: "));
			for (const meldung of this._fehlermeldungen)
				console.log(JSON.stringify("    " + meldung!));
		}
		for (const idKurs of this._kursID_to_schuelerIDSet.keySet()) {
			const eKurs : GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs);
			for (const idSchueler of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs))
				eKurs.schueler.add(idSchueler);
			for (const eSchiene of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs)) {
				eKurs.schienen.add(eSchiene.id);
				eSchiene.kurse.add(eKurs);
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
		this._ergebnis.schienen.addAll(this._schienenID_to_schiene.values());
		this.stateClearErgebnisBewertung1();
		this.stateClearErgebnisBewertung2();
		this.stateClearErgebnisBewertung3();
		this.stateClearErgebnisBewertung4();
		this._parent.ergebnisUpdateBewertung(this._ergebnis);
	}

	private stateClearErgebnisBewertung1() : void {
		const regelVerletzungen : List<number> = this._ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		this._regelTyp_to_verletzungList.clear();
		this._regelID_to_verletzungString.clear();
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
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN))
			this.stateRegelvalidierung10_lehrkraefte_beachten(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			this.stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			this.stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			this.stateRegelvalidierung13_schueler_zusammen_mit_schueler(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			this.stateRegelvalidierung14_schueler_verbieten_mit_schueler(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			this.stateRegelvalidierung15_kurs_maximale_schueleranzahl(r, regelVerletzungen);
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE))
			this.stateRegelvalidierung18_fach_kursart_maxProSchiene(r, regelVerletzungen);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet = 0;
		for (const idKurs of this._kursID_to_schienenSet.keySet()) {
			const sizeSoll : number = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs).anzahlSchienen;
			const sizeIst : number = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs).size();
			this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(sizeSoll - sizeIst);
		}
		this._regelverletzungen_tooltip1_regeln = this.stateClearErgebnisTooltip1();
	}

	private stateClearErgebnisTooltip1() : string {
		const sb : StringBuilder = new StringBuilder();
		let konflikte : number = 0;
		let konflikte_ignored : number = 0;
		for (const idRegeltyp of GostKursblockungRegelTyp.ANZEIGE_REIHENFOLGE)
			for (const fehlermeldung of MapUtils.getOrCreateArrayList(this._regelTyp_to_verletzungList, idRegeltyp)) {
				if (konflikte < 10) {
					sb.append(fehlermeldung! + "\n");
				} else {
					konflikte_ignored++;
				}
				konflikte++;
			}
		if (konflikte === 0)
			return "";
		return konflikte + " Regelverletzungen\n" + sb.toString()! + (konflikte_ignored === 0 ? "" : "+" + konflikte_ignored + " weitere Konflikte.");
	}

	private stateClearErgebnisBewertung2() : void {
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet = 0;
		for (const idSchueler of this._schuelerID_fachID_to_kurs_or_null.getKeySet())
			for (const idFach of this._schuelerID_fachID_to_kurs_or_null.getKeySetOf(idSchueler))
				if (this._schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler, idFach) === null)
					this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		for (const regel of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			const idSchueler : number = regel.parameter.get(0).valueOf();
			for (const gFachwahl of this._parent.schuelerGetListeOfFachwahlen(idSchueler))
				if (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, gFachwahl.fachID) === null)
					this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		}
		this._ergebnis.bewertung.anzahlSchuelerKollisionen = 0;
		for (const idSchueler of this._schuelerID_to_kollisionen.keySet()) {
			const kollisionen : number = DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kollisionen, idSchueler).valueOf();
			this._ergebnis.bewertung.anzahlSchuelerKollisionen += kollisionen;
		}
		this._regelverletzungen_tooltip2_wahlkonflikte = this.stateClearErgebnisTooltip2();
	}

	private stateClearErgebnisTooltip2() : string {
		const sb : StringBuilder = new StringBuilder();
		let wahlkonflikte : number = 0;
		let wahlkonflikte_ignored : number = 0;
		for (const idSchueler of this._schuelerID_fachID_to_kurs_or_null.getKeySet())
			for (const e of this._schuelerID_fachID_to_kurs_or_null.getSubMapOrException(idSchueler).entrySet()) {
				if (e.getValue() !== null)
					continue;
				if (wahlkonflikte < 10) {
					const idFach : number = e.getKey().valueOf();
					const kursart : number = this._parent.schuelerGetOfFachFachwahl(idSchueler, idFach).kursartID;
					sb.append(this._parent.toStringSchuelerSimple(idSchueler)! + " ist im Fach " + this._parent.toStringFachartSimple(idFach, kursart)! + " keinem Kurs zugeordnet.\n");
				} else {
					wahlkonflikte_ignored++;
				}
				wahlkonflikte++;
			}
		for (const idSchueler of this._schuelerID_schienenID_to_kurseSet.getKeySet())
			for (const e of this._schuelerID_schienenID_to_kurseSet.getSubMapOrException(idSchueler).entrySet()) {
				const set : JavaSet<GostBlockungsergebnisKurs> | null = e.getValue();
				if (set === null)
					continue;
				if (set.size() <= 1)
					continue;
				const list : ArrayList<GostBlockungsergebnisKurs> = new ArrayList<GostBlockungsergebnisKurs>(set);
				if (wahlkonflikte < 10) {
					sb.append(this._parent.toStringSchuelerSimple(idSchueler)! + " ist in " + this._parent.toStringSchieneSimple(e.getKey()!)! + " in mehreren Kursen:");
					for (let i : number = 0; i < list.size(); i++)
						sb.append((i === 0 ? "" : ", ") + this._parent.toStringKursSimple(list.get(i).id)!);
					sb.append("\n");
				} else {
					wahlkonflikte_ignored++;
				}
				wahlkonflikte += list.size() - 1;
			}
		return "Wahlkonflikte = " + wahlkonflikte + "\n" + sb.toString()! + (wahlkonflikte_ignored === 0 ? "" : "+" + wahlkonflikte_ignored + " weitere Konflikte.");
	}

	private stateClearErgebnisBewertung3() : void {
		this._ergebnis.bewertung.kursdifferenzMax = 0;
		this._ergebnis.bewertung.kursdifferenzHistogramm = Array(this._parent.schuelerGetAnzahl() + 1).fill(0);
		this._bewertung3_KD_nur_LK = 0;
		this._bewertung3_KD_nur_GK = 0;
		this._bewertung3_KD_nur_REST = 0;
		for (const idFachart of this._fachartID_to_kursdifferenz.keySet()) {
			const newKD : number = DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kursdifferenz, idFachart).valueOf();
			this._ergebnis.bewertung.kursdifferenzHistogramm[newKD]++;
			this._ergebnis.bewertung.kursdifferenzMax = Math.max(this._ergebnis.bewertung.kursdifferenzMax, newKD);
			const kursart : number = GostKursart.getKursartID(idFachart);
			if (kursart === GostKursart.LK.id) {
				this._bewertung3_KD_nur_LK = Math.max(this._bewertung3_KD_nur_LK, newKD);
			} else {
				if (kursart === GostKursart.GK.id) {
					this._bewertung3_KD_nur_GK = Math.max(this._bewertung3_KD_nur_GK, newKD);
				} else {
					this._bewertung3_KD_nur_REST = Math.max(this._bewertung3_KD_nur_REST, newKD);
				}
			}
		}
		this._regelverletzungen_tooltip3_kursdifferenzen = this.stateClearErgebnisTooltip3();
	}

	private stateClearErgebnisTooltip3() : string {
		const sb : StringBuilder = new StringBuilder();
		const histo : Array<number> = this._ergebnis.bewertung.kursdifferenzHistogramm;
		sb.append("Maximale Kursdifferenz (LK, GK, REST): " + this._bewertung3_KD_nur_LK + ", " + this._bewertung3_KD_nur_GK + ", " + this._bewertung3_KD_nur_REST + "\n");
		if (histo.length >= 2)
			sb.append("Optimal 0/1: " + (histo[0] + histo[1]) + "x\n");
		for (let i : number = 2; i < histo.length; i++)
			if (histo[i] > 0)
				sb.append("Differenz " + i + ": " + histo[i] + "x\n");
		return sb.toString();
	}

	private stateClearErgebnisBewertung4() : void {
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene = 0;
		for (const idSchiene of this._schienenIDset)
			for (const idFachart of this._fachartID_to_kurseList.keySet()) {
				const gleicheKurseInSchiene : number = Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, idSchiene, idFachart).size();
				if (gleicheKurseInSchiene >= 2)
					this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += gleicheKurseInSchiene - 1;
			}
		this._regelverletzungen_tooltip4_faecherparallelitaet = this.stateClearErgebnisTooltip4();
	}

	private stateClearErgebnisTooltip4() : string {
		const sb : StringBuilder = new StringBuilder();
		for (let nr : number = 1; nr <= this._schienenNR_to_schiene.size(); nr++) {
			const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
			const proSchiene : string = this.stateClearErgebnisTooltip4proSchiene(schiene.id);
			if (!JavaString.isEmpty(proSchiene))
				sb.append("Schiene " + nr + ":\n" + proSchiene!);
		}
		return sb.toString();
	}

	private stateClearErgebnisTooltip4proSchiene(idSchiene : number) : string {
		const sb : StringBuilder = new StringBuilder();
		for (const idFachart of this._fachartIDList_sortiert) {
			const proFachart : string = this.stateClearErgebnisTooltip4proSchieneUndFachart(idSchiene, idFachart);
			if (!JavaString.isEmpty(proFachart))
				sb.append(proFachart! + "\n");
		}
		return sb.toString();
	}

	private stateClearErgebnisTooltip4proSchieneUndFachart(idSchiene : number, idFachart : number) : string {
		const sb : StringBuilder = new StringBuilder();
		if (this._schienenID_fachartID_to_kurseList.contains(idSchiene, idFachart)) {
			const kursGruppe : List<GostBlockungsergebnisKurs> = this._schienenID_fachartID_to_kurseList.getNonNullOrException(idSchiene, idFachart);
			const n : number = kursGruppe.size();
			if (n >= 2) {
				sb.append("  " + this.getOfFachartName(idFachart)! + " (+" + (n - 1) + "):");
				for (let i : number = 0; i < n; i++) {
					const kurs : GostBlockungsergebnisKurs = ListUtils.getNonNullElementAtOrException(kursGruppe, i);
					sb.append((i === 0 ? "" : ",") + " " + this.getOfKursName(kurs.id)!);
				}
			}
		}
		return sb.toString();
	}

	private update_0_schienenIDset_schienenNRset() : void {
		this._schienenIDset.clear();
		this._schienenNRset.clear();
		for (const gSchiene of this._parent.daten().schienen) {
			if (gSchiene.id < 0)
				this._fehlermeldungen.add("Die Schienen-ID " + gSchiene.id + " ist ungültig!");
			if (!this._schienenIDset.add(gSchiene.id))
				this._fehlermeldungen.add("Die Schienen-ID " + gSchiene.id + " ist doppelt!");
			if (gSchiene.nummer <= 0)
				this._fehlermeldungen.add("Die Schienen-NR " + gSchiene.nummer + " ist ungültig!");
			if (!this._schienenNRset.add(gSchiene.nummer))
				this._fehlermeldungen.add("Die Schienen-NR " + gSchiene.nummer + " ist doppelt!");
		}
		for (let schienenNr : number = 1; schienenNr <= this._schienenNRset.size(); schienenNr++)
			if (!this._schienenNRset.contains(schienenNr))
				this._fehlermeldungen.add("Es gibt " + this._schienenNRset.size() + " Schienen, aber es fehlt die Schienen-Nr. " + schienenNr + "!");
	}

	private update_0_kursIDset() : void {
		this._kursIDset.clear();
		for (const gKurs of this._parent.daten().kurse) {
			if (gKurs.id < 0)
				this._fehlermeldungen.add("Die Kurs-ID " + gKurs.id + " ist ungültig!");
			if (!this._kursIDset.add(gKurs.id))
				this._fehlermeldungen.add("Die Kurs-ID " + gKurs.id + " ist doppelt!");
		}
	}

	private update_0_fachIDset() : void {
		this._fachIDset.clear();
		for (const gFach of this._parent.faecherManager().faecher()) {
			if (gFach.id < 0)
				this._fehlermeldungen.add("Die Fach-ID " + gFach.id + " ist ungültig!");
			if (!this._fachIDset.add(gFach.id))
				this._fehlermeldungen.add("Die Fach-ID " + gFach.id + " ist doppelt!");
		}
		for (const gKurs of this._parent.daten().kurse)
			if (this._fachIDset.add(gKurs.fach_id))
				this._fehlermeldungen.add("Kurs " + this._parent.toStringKursSimple(gKurs.id)! + " hat ein undefiniertes Fach (im Fächer-Manager)!");
		for (const gFachwahl of this._parent.daten().fachwahlen)
			if (this._fachIDset.add(gFachwahl.fachID))
				this._fehlermeldungen.add("Fachwahl " + this._parent.toStringFachwahlSimple(gFachwahl)! + " hat ein undefiniertes Fach (im Fächer-Manager)!");
	}

	private update_0_schuelerIDset() : void {
		this._schuelerIDset.clear();
		for (const schueler of this._parent.daten().schueler) {
			if (schueler.id < 0)
				this._fehlermeldungen.add("Die Schüler-ID " + schueler.id + " ist ungültig!");
			if (!this._schuelerIDset.add(schueler.id))
				this._fehlermeldungen.add("Die Schüler-ID " + schueler.id + " ist doppelt!");
		}
	}

	private update_0_schienenID_to_schiene_schienenNR_to_schiene() : void {
		this._schienenID_to_schiene.clear();
		this._schienenNR_to_schiene.clear();
		for (const gSchiene of this._parent.daten().schienen) {
			const eSchiene : GostBlockungsergebnisSchiene = DTOUtils.newGostBlockungsergebnisSchiene(gSchiene.id);
			this._schienenID_to_schiene.put(gSchiene.id, eSchiene);
			this._schienenNR_to_schiene.put(gSchiene.nummer, eSchiene);
		}
	}

	private update_0_kursID_to_kurs() : void {
		this._kursID_to_kurs.clear();
		for (const gKurs of this._parent.daten().kurse) {
			const eKurs : GostBlockungsergebnisKurs = DTOUtils.newGostBlockungsergebnisKurs(gKurs.id, gKurs.fach_id, gKurs.kursart, gKurs.anzahlSchienen);
			this._kursID_to_kurs.put(gKurs.id, eKurs);
		}
	}

	private update_0_schuelerID_to_schueler() : void {
		this._schuelerID_to_schueler.clear();
		for (const gSchueler of this._parent.daten().schueler) {
			this._schuelerID_to_schueler.put(gSchueler.id, gSchueler);
		}
	}

	private update_0_schienenID_to_kursIDSet(ergebnisOld : GostBlockungsergebnis) : void {
		this._schienenID_to_kursIDSet.clear();
		for (const eSchiene of ergebnisOld.schienen)
			for (const eKurs of eSchiene.kurse)
				MapUtils.getOrCreateHashSet(this._schienenID_to_kursIDSet, eSchiene.id).add(eKurs.id);
		for (const idSchiene of this._schienenIDset)
			if (!this._schienenID_to_kursIDSet.containsKey(idSchiene))
				MapUtils.getOrCreateHashSet(this._schienenID_to_kursIDSet, idSchiene);
	}

	private update_1_kursID_to_schuelerIDSet_schuelerID_to_ungueltigeKurseSet(ergebnisOld : GostBlockungsergebnis) : void {
		this._kursID_to_schuelerIDSet.clear();
		for (const eSchiene of ergebnisOld.schienen)
			for (const eKurs of eSchiene.kurse)
				MapUtils.getOrCreateHashSet(this._kursID_to_schuelerIDSet, eKurs.id).addAll(eKurs.schueler);
		for (const idKurs of this._kursIDset)
			if (!this._kursID_to_schuelerIDSet.containsKey(idKurs))
				MapUtils.getOrCreateHashSet(this._kursID_to_schuelerIDSet, idKurs);
		this._schuelerID_to_ungueltigeKurseSet.clear();
		for (const idKurs of this._kursID_to_schuelerIDSet.keySet()) {
			const eKurs : GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs);
			const schuelerIDset : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs);
			for (const idSchueler of new HashSet(schuelerIDset))
				if (!this._parent.schuelerGetHatFachart(idSchueler, eKurs.fachID, eKurs.kursart)) {
					schuelerIDset.remove(idSchueler);
					MapUtils.getOrCreateHashSet(this._schuelerID_to_ungueltigeKurseSet, idSchueler).add(eKurs);
				}
		}
	}

	private update_1_kursID_to_dummySuS() : void {
		this._kursID_to_dummySuS.clear();
		for (const r of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			const idKurs : number = r.parameter.get(0).valueOf();
			const anzahl : number = r.parameter.get(1)!;
			if (!this._kursIDset.contains(idKurs)) {
				this._fehlermeldungen.add("Kurs " + this._parent.toStringKursSimple(idKurs)! + " soll " + anzahl + " externe SuS haben, aber den Kurs gibt es nicht!");
				continue;
			}
			if ((anzahl < 1) || (anzahl > 99)) {
				this._fehlermeldungen.add("Kurs " + this._parent.toStringKursSimple(idKurs)! + " mit " + anzahl + " externen SuS ist ungültig!");
				continue;
			}
			if (this._kursID_to_dummySuS.containsKey(idKurs)) {
				this._fehlermeldungen.add("Kurs " + this._parent.toStringKursSimple(idKurs)! + " mit " + anzahl + " externen SuS. Doppelte Regel gefunden!");
				continue;
			}
			this._kursID_to_dummySuS.put(idKurs, anzahl);
		}
		for (const idKurs of this._kursIDset)
			MapUtils.putNonNullIfNotExists(this._kursID_to_dummySuS, idKurs, 0);
	}

	private update_1_fachID_to_kurseList() : void {
		this._fachID_to_kurseList.clear();
		for (const eKurs of this._kursID_to_kurs.values())
			MapUtils.getOrCreateArrayList(this._fachID_to_kurseList, eKurs.fachID).add(eKurs);
		for (const idFach of this._fachIDset)
			MapUtils.getOrCreateArrayList(this._fachID_to_kurseList, idFach);
	}

	private update_1_kursID_to_schienenSet() : void {
		this._kursID_to_schienenSet.clear();
		for (const idSchiene of this._schienenID_to_kursIDSet.keySet()) {
			const eSchiene : GostBlockungsergebnisSchiene = DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_schiene, idSchiene);
			for (const idKurs of MapUtils.getOrCreateHashSet(this._schienenID_to_kursIDSet, idSchiene))
				MapUtils.getOrCreateHashSet(this._kursID_to_schienenSet, idKurs).add(eSchiene);
		}
		for (const idKurs of this._kursIDset)
			if (!this._kursID_to_schienenSet.containsKey(idKurs))
				MapUtils.getOrCreateHashSet(this._kursID_to_schienenSet, idKurs);
	}

	private update_1_fachartID_to_kurseList() : void {
		this._fachartID_to_kurseList.clear();
		for (const eKurs of this._kursID_to_kurs.values()) {
			const fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(this._fachartID_to_kurseList, fachartID).add(eKurs);
		}
		for (const gFachwahl of this._parent.daten().fachwahlen)
			MapUtils.getOrCreateArrayList(this._fachartID_to_kurseList, GostKursart.getFachartIDByFachwahl(gFachwahl));
		for (const idFachart of this._fachartID_to_kurseList.keySet()) {
			const kursmenge : List<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kurseList, idFachart);
			if (this._fachartmenge_sortierung === 1) {
				kursmenge.sort(this._kursComparator_kursart_fach_kursnummer);
			} else {
				kursmenge.sort(this._kursComparator_fach_kursart_kursnummer);
			}
		}
	}

	private update_2_schuelerID_to_kurseSet() : void {
		this._schuelerID_to_kurseSet.clear();
		for (const idKurs of this._kursID_to_schuelerIDSet.keySet()) {
			const eKurs : GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs);
			for (const idSchueler of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs))
				MapUtils.getOrCreateHashSet(this._schuelerID_to_kurseSet, idSchueler).add(eKurs);
		}
		for (const idSchueler of this._schuelerIDset)
			if (!this._schuelerID_to_kurseSet.containsKey(idSchueler))
				MapUtils.getOrCreateHashSet(this._schuelerID_to_kurseSet, idSchueler);
	}

	private update_2_fachartIDList_sortiert() : void {
		this._fachartIDList_sortiert.clear();
		this._fachartIDList_sortiert.addAll(this._fachartID_to_kurseList.keySet());
		if (this._fachartmenge_sortierung === 1) {
			this._fachartIDList_sortiert.sort(this._fachartComparator_kursart_fach);
		} else {
			this._fachartIDList_sortiert.sort(this._fachartComparator_fach_kursart);
		}
	}

	private update_2_fachartID_to_kursdifferenz() : void {
		this._fachartID_to_kursdifferenz.clear();
		for (const idFachart of this._fachartID_to_kurseList.keySet()) {
			const kursmenge : List<GostBlockungsergebnisKurs> | null = DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kurseList, idFachart);
			let min : number = 10000;
			let max : number = 0;
			for (const kurs of kursmenge) {
				const keyIgnoreID : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, kurs.id]);
				if (this._parent.regelGetByLongArrayKeyOrNull(keyIgnoreID) !== null)
					continue;
				const size : number = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, kurs.id).size() + DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_dummySuS, kurs.id);
				min = Math.min(min, size);
				max = Math.max(max, size);
			}
			let newKD : number = max - min;
			if (newKD < 0)
				newKD = 0;
			this._fachartID_to_kursdifferenz.put(idFachart, newKD);
		}
	}

	private update_2_schienenID_to_kollisionen() : void {
		this._schienenID_to_kollisionen.clear();
		for (const idSchiene of this._schienenID_to_kursIDSet.keySet()) {
			const kursmenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_kursIDSet, idSchiene);
			let summeMitDoppelten : number = 0;
			const summeOhneDoppelte : HashSet<number> = new HashSet();
			for (const idKurs of kursmenge) {
				const schuelermenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs);
				summeMitDoppelten += schuelermenge.size();
				summeOhneDoppelte.addAll(schuelermenge);
			}
			this._schienenID_to_kollisionen.put(idSchiene, summeMitDoppelten - summeOhneDoppelte.size());
		}
	}

	private update_2_schienenID_to_susAnzahl() : void {
		this._schienenID_to_susAnzahl.clear();
		for (const idSchiene of this._schienenID_to_kursIDSet.keySet()) {
			const kursmenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_kursIDSet, idSchiene);
			let summeMitDoppelten : number = 0;
			for (const idKurs of kursmenge) {
				const schuelermenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs);
				summeMitDoppelten += schuelermenge.size();
			}
			this._schienenID_to_susAnzahl.put(idSchiene, summeMitDoppelten);
		}
	}

	private update_2_schuelerID_schienenID_to_kurseSet() : void {
		this._schuelerID_schienenID_to_kurseSet.clear();
		for (const idSchiene of this._schienenID_to_kursIDSet.keySet()) {
			const kursmenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_kursIDSet, idSchiene);
			for (const idKurs of kursmenge) {
				const eKurs : GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs);
				const schuelermenge : JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs);
				for (const idSchueler of schuelermenge) {
					Map2DUtils.getOrCreateHashSet(this._schuelerID_schienenID_to_kurseSet, idSchueler, idSchiene).add(eKurs);
				}
			}
			for (const idSchueler of this._schuelerIDset)
				Map2DUtils.getOrCreateHashSet(this._schuelerID_schienenID_to_kurseSet, idSchueler, idSchiene);
		}
	}

	private update_2_schienenID_fachartID_to_kurseList() : void {
		this._schienenID_fachartID_to_kurseList.clear();
		for (const eKurs of this._kursID_to_kurs.values()) {
			const fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			for (const eSchiene of MapUtils.getOrCreateHashSet(this._kursID_to_schienenSet, eKurs.id))
				Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, eSchiene.id, fachartID).add(eKurs);
		}
		for (const idSchiene of this._schienenIDset)
			for (const idFachart of this._fachartID_to_kurseList.keySet())
				Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, idSchiene, idFachart);
	}

	private update_3_schuelerID_to_kollisionen() : void {
		this._schuelerID_to_kollisionen.clear();
		for (const idSchueler of this._schuelerID_schienenID_to_kurseSet.getKeySet()) {
			let summeAllerKollisionenDesSchuelers : number = 0;
			for (const idSchiene of this._schuelerID_schienenID_to_kurseSet.getKeySetOf(idSchueler)) {
				const kurseInDerSchiene : number = this._schuelerID_schienenID_to_kurseSet.getNonNullOrException(idSchueler, idSchiene).size();
				if (kurseInDerSchiene >= 2)
					summeAllerKollisionenDesSchuelers += kurseInDerSchiene - 1;
			}
			this._schuelerID_to_kollisionen.put(idSchueler, summeAllerKollisionenDesSchuelers);
		}
	}

	private update_3_schuelerID_fachID_to_kurs_or_null() : void {
		this._schuelerID_fachID_to_kurs_or_null.clear();
		for (const idSchueler of this._schuelerID_to_kurseSet.keySet())
			for (const eKurs of DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kurseSet, idSchueler))
				this._schuelerID_fachID_to_kurs_or_null.put(idSchueler, eKurs.fachID, eKurs);
		for (const gFachwahl of this._parent.daten().fachwahlen)
			if (!this._schuelerID_fachID_to_kurs_or_null.contains(gFachwahl.schuelerID, gFachwahl.fachID))
				this._schuelerID_fachID_to_kurs_or_null.put(gFachwahl.schuelerID, gFachwahl.fachID, null);
	}

	private stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (let schienenNr : number = r.parameter.get(1)!; schienenNr <= r.parameter.get(2)!; schienenNr++)
			for (const eKurs of this.getSchieneEmitNr(schienenNr).kurse)
				if (eKurs.kursart === r.parameter.get(0)!) {
					regelVerletzungen.add(r.id);
					const beschreibung : string = "Kursart " + this.getOfKursName(eKurs.id)! + " sollte nicht auf Schiene " + schienenNr + " liegen.";
					MapUtils.addToList(this._regelTyp_to_verletzungList, 1, beschreibung);
					this._regelID_to_verletzungString.put(r.id, beschreibung);
				}
	}

	private stateRegelvalidierung2_kurs_fixieren_in_schiene(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs : number = r.parameter.get(0).valueOf();
		const schienenNr : number = r.parameter.get(1)!;
		if (!this.getOfKursSchienenmenge(idKurs).contains(this.getSchieneEmitNr(schienenNr))) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = "Kurs " + this.getOfKursName(idKurs)! + " sollte fixiert sein in Schiene " + schienenNr + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 2, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung3_kurs_sperren_in_schiene(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs : number = r.parameter.get(0).valueOf();
		const schienenNr : number = r.parameter.get(1)!;
		if (this.getOfKursSchienenmenge(idKurs).contains(this.getSchieneEmitNr(schienenNr))) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = "Kurs " + this.getOfKursName(idKurs)! + " sollte gesperrt sein in Schiene " + schienenNr + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 3, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung4_schueler_fixieren_in_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler : number = r.parameter.get(0).valueOf();
		const idKurs : number = r.parameter.get(1).valueOf();
		if (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler)! + " sollte fixiert sein in Kurs " + this.getOfKursName(idKurs)! + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 4, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung5_schueler_verbieten_in_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler : number = r.parameter.get(0).valueOf();
		const idKurs : number = r.parameter.get(1).valueOf();
		if (this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler)! + " sollte verboten sein in Kurs " + this.getOfKursName(idKurs)! + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 5, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (const eKurs of this._kursID_to_kurs.values())
			for (const eSchieneID of eKurs.schienen) {
				const nr : number = this.getSchieneG(eSchieneID!).nummer;
				const kursart : number = r.parameter.get(0)!;
				const schienenNrVon : number = r.parameter.get(1)!;
				const schienenNrBis : number = r.parameter.get(2)!;
				const b1 : boolean = eKurs.kursart === kursart;
				const b2 : boolean = (schienenNrVon <= nr) && (nr <= schienenNrBis);
				if (b1 !== b2) {
					regelVerletzungen.add(r.id);
					const beschreibung : string = "Kursart von " + this.getOfKursName(eKurs.id)! + " sollte innerhalb der Schienen " + schienenNrVon + " bis " + schienenNrBis + " sein.";
					MapUtils.addToList(this._regelTyp_to_verletzungList, 6, beschreibung);
					this._regelID_to_verletzungString.put(r.id, beschreibung);
				}
			}
	}

	private stateRegelvalidierung7_kurs_verbieten_mit_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs1 : number = r.parameter.get(0).valueOf();
		const idKurs2 : number = r.parameter.get(1).valueOf();
		for (const schiene1 of this.getOfKursSchienenmenge(idKurs1))
			for (const schiene2 of this.getOfKursSchienenmenge(idKurs2))
				if (schiene1 as unknown === schiene2 as unknown) {
					regelVerletzungen.add(r.id);
					const nr : number = this.getSchieneG(schiene1.id).nummer;
					const beschreibung : string = "Kurs " + this.getOfKursName(idKurs1)! + " und Kurs " + this.getOfKursName(idKurs2)! + " sollten nicht gemeinsam in einer Schiene (" + nr + ") sein.";
					MapUtils.addToList(this._regelTyp_to_verletzungList, 7, beschreibung);
					this._regelID_to_verletzungString.put(r.id, beschreibung);
				}
	}

	private stateRegelvalidierung8_kurs_zusammen_mit_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs1 : number = r.parameter.get(0).valueOf();
		const idKurs2 : number = r.parameter.get(1).valueOf();
		const set1 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs1);
		const set2 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs2);
		if (set1.size() < set2.size()) {
			for (const schiene1 of set1)
				if (!set2.contains(schiene1)) {
					regelVerletzungen.add(r.id);
					const beschreibung : string = "Kurs " + this.getOfKursName(idKurs1)! + " und Kurs " + this.getOfKursName(idKurs2)! + " sollten gemeinsam in einer Schiene sein.";
					MapUtils.addToList(this._regelTyp_to_verletzungList, 8, beschreibung);
					this._regelID_to_verletzungString.put(r.id, beschreibung);
				}
		} else {
			for (const schiene2 of set2)
				if (!set1.contains(schiene2)) {
					regelVerletzungen.add(r.id);
					const beschreibung : string = "Kurs " + this.getOfKursName(idKurs1)! + " und Kurs " + this.getOfKursName(idKurs2)! + " sollten gemeinsam in einer Schiene sein.";
					MapUtils.addToList(this._regelTyp_to_verletzungList, 8, beschreibung);
					this._regelID_to_verletzungString.put(r.id, beschreibung);
				}
		}
	}

	private stateRegelvalidierung10_lehrkraefte_beachten(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		for (const eSchiene of this._schienenID_to_schiene.values())
			for (const eKurs1 of eSchiene.kurse)
				for (const eKurs2 of eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (const gLehr1 of this.getKursG(eKurs1.id).lehrer)
							for (const gLehr2 of this.getKursG(eKurs2.id).lehrer)
								if (gLehr1.id === gLehr2.id) {
									regelVerletzungen.add(r.id);
									const nr : number = this.getSchieneG(eSchiene.id).nummer;
									const beschreibung : string = "Kurs " + this.getOfKursName(eKurs1.id)! + " und Kurs " + this.getOfKursName(eKurs2.id)! + " haben die Lehrkraft " + gLehr1.kuerzel + " in der selben Schiene (" + nr + ").";
									MapUtils.addToList(this._regelTyp_to_verletzungList, 10, beschreibung);
									this._regelID_to_verletzungString.put(r.id, beschreibung);
								}
	}

	private stateRegelvalidierung11_schueler_zusammen_mit_schueler_in_fach(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler1 : number = r.parameter.get(0).valueOf();
		const idSchueler2 : number = r.parameter.get(1).valueOf();
		const idFach : number = r.parameter.get(2).valueOf();
		const fach : GostFach = this.getFach(idFach);
		if (!this._parent.schuelerGetHatFach(idSchueler1, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 11, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (!this._parent.schuelerGetHatFach(idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler2)! + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 11, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (!this._parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und " + this.getOfSchuelerNameVorname(idSchueler2)! + " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 11, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (!this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und " + this.getOfSchuelerNameVorname(idSchueler2)! + " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 11, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung12_schueler_verbieten_mit_schueler_in_fach(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler1 : number = r.parameter.get(0).valueOf();
		const idSchueler2 : number = r.parameter.get(1).valueOf();
		const idFach : number = r.parameter.get(2).valueOf();
		const fach : GostFach = this.getFach(idFach);
		if (!this._parent.schuelerGetHatFach(idSchueler1, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 12, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (!this._parent.schuelerGetHatFach(idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler2)! + " hat keine Fachwahl " + fach.kuerzelAnzeige + ", hat aber eine Regel, die das Fach definiert.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 12, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (!this._parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und SchülerIn " + this.getOfSchuelerNameVorname(idSchueler2)! + " haben nicht die selbe Kursart bei " + fach.kuerzelAnzeige + ".";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 12, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
			return;
		}
		if (this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und SchülerIn " + this.getOfSchuelerNameVorname(idSchueler2)! + " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 12, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung13_schueler_zusammen_mit_schueler(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler1 : number = r.parameter.get(0).valueOf();
		const idSchueler2 : number = r.parameter.get(1).valueOf();
		for (const fach of this._parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (!this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				regelVerletzungen.add(r.id);
				const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und " + this.getOfSchuelerNameVorname(idSchueler2)! + " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(this._regelTyp_to_verletzungList, 13, beschreibung);
				this._regelID_to_verletzungString.put(r.id, beschreibung);
			}
	}

	private stateRegelvalidierung14_schueler_verbieten_mit_schueler(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idSchueler1 : number = r.parameter.get(0).valueOf();
		const idSchueler2 : number = r.parameter.get(1).valueOf();
		for (const fach of this._parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2))
			if (this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				regelVerletzungen.add(r.id);
				const beschreibung : string = this.getOfSchuelerNameVorname(idSchueler1)! + " und " + this.getOfSchuelerNameVorname(idSchueler2)! + " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(this._regelTyp_to_verletzungList, 14, beschreibung);
				this._regelID_to_verletzungString.put(r.id, beschreibung);
			}
	}

	private stateRegelvalidierung15_kurs_maximale_schueleranzahl(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idKurs : number = r.parameter.get(0).valueOf();
		const maxSuS : number = r.parameter.get(1)!;
		DeveloperNotificationException.ifTrue("Regel 15: " + this._parent.toStringKurs(idKurs)! + " maximale SuS-Anzahl = " + maxSuS + " ist ungültig!", (maxSuS < 0) || (maxSuS > 100));
		const sus : number = this.getOfKursAnzahlSchuelerPlusDummy(idKurs);
		if (sus > maxSuS) {
			regelVerletzungen.add(r.id);
			const beschreibung : string = "Kurs " + this.getOfKursName(idKurs)! + " hat " + sus + " SuS, sollte aber nicht mehr als " + maxSuS + " haben.";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 15, beschreibung);
			this._regelID_to_verletzungString.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung18_fach_kursart_maxProSchiene(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const idFach : number = r.parameter.get(0).valueOf();
		const kursart : number = r.parameter.get(1)!;
		const maxProSchiene : number = r.parameter.get(2)!;
		const idFachart : number = GostKursart.getFachartID(idFach, kursart);
		for (const idSchiene of this._schienenIDset) {
			const size : number = Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, idSchiene, idFachart).size();
			if (size <= maxProSchiene)
				continue;
			regelVerletzungen.add(r.id);
			const beschreibung : string = "In " + this._parent.toStringSchieneSimple(idSchiene)! + " ist die Fachart " + this._parent.toStringFachartSimpleByFachartID(idFachart)! + " insgesamt " + size + " Mal vertreten, erlaubt sind aber nur " + maxProSchiene + "!";
			MapUtils.addToList(this._regelTyp_to_verletzungList, 18, beschreibung);
			const old : string = MapUtils.getOrDefault(this._regelID_to_verletzungString, r.id, "");
			this._regelID_to_verletzungString.put(r.id, (JavaString.isEmpty(old) ? "" : "\n") + beschreibung!);
		}
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu und revalidiert nicht den Zustand. <br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, wird der Schüler nicht hinzugefügt.
	 *          Stattdessen wird die ungültige Wahl in einer Map gespeichert.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursHinzufuegenOhneRevalidierung(idSchueler : number, idKurs : number) : void {
		const eKurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		eKurs.schueler.add(idSchueler);
	}

	/**
	 * Entfernt den Schüler aus dem Kurs und revalidiert nicht den Zustand. <br>
	 * Hinweis: Ist die Wahl des Kurses für diesen Schüler ungültig, so wird der Schüler aus der zuvor gespeichert
	 *          Zuordnung aller ungültigen Wahlen gelöscht.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursEntfernenOhneRevalidierung(idSchueler : number, idKurs : number) : void {
		const eKurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		eKurs.schueler.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu und revalidiert nicht den Zustand.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs : number, idSchiene : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		kurs.schienen.add(idSchiene);
		schiene.kurse.add(kurs);
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneEntfernenOhneRegelvalidierung(idKurs : number, idSchiene : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		schiene.kurse.remove(kurs);
		for (let i : number = kurs.schienen.size() - 1; i >= 0; i--)
			if (kurs.schienen.get(i) === schiene.id) {
				kurs.schienen.remove(i);
				return;
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
		for (const idKurs of this._kursID_to_dummySuS.keySet())
			summe += this.getOfKursAnzahlSchuelerDummy(idKurs);
		return summe;
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
		const copy : GostBlockungsergebnis = GostBlockungsergebnisManager.deepCopyErgebnis(this._ergebnis);
		for (const entry of this._schuelerID_to_ungueltigeKurseSet.entrySet())
			for (const kurs1 of entry.getValue())
				for (const schiene of copy.schienen)
					for (const kurs2 of schiene.kurse)
						if (kurs1.id === kurs2.id)
							kurs2.schueler.add(entry.getKey());
		return copy;
	}

	/**
	 * Liefert die Menge (meistens eine) aller Fehlermeldungen.
	 * <br>Falls die Liste nicht leer ist, sollte die GUI den Benutzer warnen, dass die Blockung nicht vollständig geladen wurde!
	 *
	 * @return die Menge (meistens eine) aller Fehlermeldungen.
	 */
	public getFehlermeldungen() : List<string> {
		return this._fehlermeldungen;
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
	 * Liefert eine tiefe Kopie des Blockungsergebnisses.
	 *
	 * @param e  Das zu kopierende GostBlockungsergebnis.
	 *
	 * @return eine tiefe Kopie des Blockungsergebnisses.
	 */
	public static deepCopyErgebnis(e : GostBlockungsergebnis) : GostBlockungsergebnis {
		const copy : GostBlockungsergebnis = new GostBlockungsergebnis();
		copy.id = e.id;
		copy.blockungID = e.blockungID;
		copy.name = e.name;
		copy.gostHalbjahr = e.gostHalbjahr;
		copy.istAktiv = e.istAktiv;
		for (const schiene of e.schienen)
			copy.schienen.add(GostBlockungsergebnisManager.deepCopyBewertung(schiene));
		copy.bewertung = GostBlockungsergebnisManager.deepCopyBewertung(e.bewertung);
		return copy;
	}

	private static deepCopyBewertung(b : GostBlockungsergebnisBewertung) : GostBlockungsergebnisBewertung;

	private static deepCopyBewertung(s : GostBlockungsergebnisSchiene) : GostBlockungsergebnisSchiene;

	/**
	 * Implementation for method overloads of 'deepCopyBewertung'
	 */
	private static deepCopyBewertung(__param0 : GostBlockungsergebnisBewertung | GostBlockungsergebnisSchiene) : GostBlockungsergebnisBewertung | GostBlockungsergebnisSchiene {
		if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung'))))) {
			const b : GostBlockungsergebnisBewertung = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisBewertung(__param0);
			const copy : GostBlockungsergebnisBewertung = new GostBlockungsergebnisBewertung();
			copy.regelVerletzungen.addAll(b.regelVerletzungen);
			copy.anzahlKurseNichtZugeordnet = b.anzahlKurseNichtZugeordnet;
			copy.anzahlSchuelerNichtZugeordnet = b.anzahlSchuelerNichtZugeordnet;
			copy.anzahlSchuelerKollisionen = b.anzahlSchuelerKollisionen;
			copy.kursdifferenzMax = b.kursdifferenzMax;
			copy.kursdifferenzHistogramm = GostBlockungsergebnisManager.deepCopyArray(b.kursdifferenzHistogramm);
			copy.anzahlKurseMitGleicherFachartProSchiene = b.anzahlKurseMitGleicherFachartProSchiene;
			return copy;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene'))))) {
			const s : GostBlockungsergebnisSchiene = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisSchiene(__param0);
			const copy : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
			copy.id = s.id;
			for (const kurs of s.kurse)
				copy.kurse.add(GostBlockungsergebnisManager.deepCopyKurs(kurs));
			return copy;
		} else throw new Error('invalid method overload');
	}

	private static deepCopyArray(a : Array<number>) : Array<number> {
		const copy : Array<number> | null = Array(a.length).fill(0);
		System.arraycopy(a, 0, copy, 0, a.length);
		return copy;
	}

	private static deepCopyKurs(k : GostBlockungsergebnisKurs) : GostBlockungsergebnisKurs {
		const copy : GostBlockungsergebnisKurs = new GostBlockungsergebnisKurs();
		copy.id = k.id;
		copy.fachID = k.fachID;
		copy.kursart = k.kursart;
		copy.anzahlSchienen = k.anzahlSchienen;
		copy.schueler.addAll(k.schueler);
		copy.schienen.addAll(k.schienen);
		return copy;
	}

	/**
	 * Liefert eine Güte eines Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 *
	 * @param value   der Wert des Bewertungskriteriums
	 *
	 * @return die Güte des Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static getOfBewertungFarbcodeStatic(value : number) : number {
		return 1 - 1 / (0.25 * value + 1);
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public static getOfBewertung1WertStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		let summe : number = 0;
		summe += bewertung.anzahlKurseNichtZugeordnet;
		summe += bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 */
	public getOfBewertung1Wert() : number {
		return GostBlockungsergebnisManager.getOfBewertung1WertStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param bewertung  die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static getOfBewertung1FarbcodeStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung1WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung1Farbcode() : number {
		return GostBlockungsergebnisManager.getOfBewertung1FarbcodeStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public static getOfBewertung2WertStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		let summe : number = 0;
		summe += bewertung.anzahlSchuelerNichtZugeordnet;
		summe += bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 */
	public getOfBewertung2Wert() : number {
		return GostBlockungsergebnisManager.getOfBewertung2WertStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static getOfBewertung2FarbcodeStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung2WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung2Farbcode() : number {
		return GostBlockungsergebnisManager.getOfBewertung2FarbcodeStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static getOfBewertung3FarbcodeStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		let wert : number = GostBlockungsergebnisManager.getOfBewertung3WertStatic(bewertung);
		if (wert > 0)
			wert--;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(wert);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 */
	public static getOfBewertung3WertStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		return bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 */
	public getOfBewertung3Wert() : number {
		return this._ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode() : number {
		const wert : number = this._ergebnis.bewertung.kursdifferenzMax;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(wert === 0 ? 0 : wert - 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 */
	public getOfBewertung3Wert_nur_LK() : number {
		return this._bewertung3_KD_nur_LK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode_nur_LK() : number {
		const wert : number = this._bewertung3_KD_nur_LK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(wert === 0 ? 0 : wert - 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 */
	public getOfBewertung3Wert_nur_GK() : number {
		return this._bewertung3_KD_nur_GK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode_nur_GK() : number {
		const wert : number = this._bewertung3_KD_nur_GK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(wert === 0 ? 0 : wert - 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 */
	public getOfBewertung3Wert_nur_REST() : number {
		return this._bewertung3_KD_nur_REST;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode_nur_REST() : number {
		const wert : number = this._bewertung3_KD_nur_REST;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(wert === 0 ? 0 : wert - 1);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 * <br>Darin enthalten sind:
	 * <br>- Das Histogramm der ersten 10 Kursdifferenzen (Kursdifferenz 0 bis Kursdifferenz 9).
	 * <br>- Das Histogramm hat eine garantierte Länge von 10.
	 *
	 * @param bewertung  Die Bewertung vom Ergebnis.
	 *
	 * @return den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 */
	public static getOfBewertung3HistogrammStatic(bewertung : GostBlockungsergebnisBewertung) : Array<number> {
		const histo : Array<number> = Array(10).fill(0);
		for (let i : number = 0; i < histo.length; i++)
			histo[i] = bewertung.kursdifferenzHistogramm.length >= histo.length ? bewertung.kursdifferenzHistogramm[i] : 0;
		return histo;
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 * <br>- Das Histogramm der ersten 10 Kursdifferenzen (Kursdifferenz 0 bis Kursdifferenz 9).
	 * <br>- Das Histogramm hat eine garantierte Länge von 10.
	 *
	 * @return den Wert des 3. Bewertungskriteriums als Histogramm (Array der Länge 10).
	 */
	public getOfBewertung3Histogramm() : Array<number> {
		return GostBlockungsergebnisManager.getOfBewertung3HistogrammStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 */
	public static getOfBewertung4WertStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		return bewertung.anzahlKurseMitGleicherFachartProSchiene;
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
		return GostBlockungsergebnisManager.getOfBewertung4WertStatic(this._ergebnis.bewertung);
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param bewertung   die Bewertung vom Ergebnis
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public static getOfBewertung4FarbcodeStatic(bewertung : GostBlockungsergebnisBewertung) : number {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung4WertStatic(bewertung));
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
		return GostBlockungsergebnisManager.getOfBewertung4FarbcodeStatic(this._ergebnis.bewertung);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._fachID_to_kurseList, idFach);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kurseList, idFachart);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kursdifferenz, idFachart)!;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart, übergeben als (Fach, Kursart).
	 * Die Methode beachtet auch Kurse mit Dummy-SuS. <br>
	 * Wirft eine {@link DeveloperNotificationException} falls die Fachart-ID unbekannt ist.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 *
	 * @return die Kursdifferenz der Fachart, übergeben als (Fach, Kursart).
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachOfKursartKursdifferenz(idFach : number, idKursart : number) : number {
		const idFachart : number = GostKursart.getFachartID(idFach, idKursart);
		return DeveloperNotificationException.ifMapGetIsNull(this._fachartID_to_kursdifferenz, idFachart)!;
	}

	/**
	 * Liefert den Namen der Fachart, z. B. D-LK.
	 *
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return den Namen der Fachart, z. B. D-LK.
	 */
	public getOfFachartName(idFachart : number) : string | null {
		const idFach : number = GostKursart.getFachID(idFachart);
		const idKursart : number = GostKursart.getKursartID(idFachart);
		return this._parent.faecherManager().getOrException(idFach).kuerzelAnzeige + "-" + GostKursart.fromID(idKursart).kuerzel;
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
		return this._fachartIDList_sortiert;
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach LK/GK, dann nach der Fachsortierung, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungKursartFachNummer() : void {
		this._fachartmenge_sortierung = 1;
		this.stateRevalidateEverything();
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach der Fachsortierung, dann nach LK/GK, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungFachKursartNummer() : void {
		this._fachartmenge_sortierung = 2;
		this.stateRevalidateEverything();
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kurseSet, idSchueler);
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
		list.addAll(DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kurseSet, idSchueler));
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
			const kurseDerSchiene : JavaSet<GostBlockungsergebnisKurs> = this._schuelerID_schienenID_to_kurseSet.getNonNullOrException(idSchueler, schiene.id);
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
		const nIst : number = DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kurseSet, idSchueler).size();
		const nSoll : number = this._schuelerID_fachID_to_kurs_or_null.getSubMapSizeOrZero(idSchueler);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kollisionen, idSchueler) > 0;
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schuelerID_to_kollisionen, idSchueler)!;
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
	private getOfSchuelerOfSchieneKursmenge(idSchueler : number, idSchiene : number) : JavaSet<GostBlockungsergebnisKurs> {
		return this._schuelerID_schienenID_to_kurseSet.getNonNullOrException(idSchueler, idSchiene);
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
		return this._schuelerID_schienenID_to_kurseSet.getNonNullOrException(idSchueler, idSchiene).size() > 1;
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
		return this._schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler, idFach);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs).contains(idSchueler);
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	private getOfSchuelerNeuzuordnungMitFixierung(idSchueler : number, fixiereBelegteKurse : boolean) : SchuelerblockungOutput {
		console.log(JSON.stringify("getOfSchuelerNeuzuordnung " + idSchueler + ", " + fixiereBelegteKurse));
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
				DeveloperNotificationException.ifTrue(this._parent.toStringKurs(idKurs)! + " von " + this._parent.toStringSchueler(idSchueler)! + " ist gesperrt und fixiert zugleich!", kursS.istGesperrt && kursS.istFixiert);
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
	 * Liefert ein {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 */
	public getOfSchuelerNeuzuordnung(idSchueler : number, fixiereBelegteKurse : boolean) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const zuordnung : SchuelerblockungOutput = this.getOfSchuelerNeuzuordnungMitFixierung(idSchueler, fixiereBelegteKurse);
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate | null = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of zuordnung.fachwahlenZuKurs) {
			const kursV : GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			const kursN : GostBlockungsergebnisKurs | null = z.kursID < 0 ? null : this.getKursE(z.kursID);
			if (kursV as unknown !== kursN as unknown) {
				if (kursV !== null)
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursV.id, idSchueler));
				if (kursN !== null)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursN.id, idSchueler));
			}
		}
		return u;
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
		return this._parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 */
	public getOfSchuelerOfKursIstLK(idSchueler : number, idKurs : number) : boolean {
		const abiturfach : number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach >= 1) && (abiturfach <= 2);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als AB3 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als AB3 gewählt hat.
	 */
	public getOfSchuelerOfKursIstAB3(idSchueler : number, idKurs : number) : boolean {
		const abiturfach : number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach === 3);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als LK oder AB3 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als LK oder AB3 gewählt hat.
	 */
	public getOfSchuelerOfKursIstLKoderAB3(idSchueler : number, idKurs : number) : boolean {
		const abiturfach : number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach >= 1) && (abiturfach <= 3);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als AB4 gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als AB4 gewählt hat.
	 */
	public getOfSchuelerOfKursIstAB4(idSchueler : number, idKurs : number) : boolean {
		const abiturfach : number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return (abiturfach === 4);
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
		const abiturfach : number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
		return abiturfach >= 1;
	}

	/**
	 * Liefert den Wert (1-4) des Abiturfaches oder 0, falls es kein Abiturfach ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return den Wert (1-4) des Abiturfaches oder 0, falls es kein Abiturfach ist.
	 */
	private getOfSchuelerOfKursAbiturfach(idSchueler : number, idKurs : number) : number {
		const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
		return (fachwahl.abiturfach === null) ? 0 : fachwahl.abiturfach;
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als Abiturfach gewählt hat.
	 */
	public getOfSchuelerOfKursIstSchriftlich(idSchueler : number, idKurs : number) : boolean {
		const fachwahl : GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
		return fachwahl.istSchriftlich;
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
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + this._parent.toStringSchueler(idSchueler)! + " ist nicht definiert!", geschlecht);
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
		return this._schuelerID_to_ungueltigeKurseSet;
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
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach gemeinsam im selben Kurs sind.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler im bezogen auf das Fach gemeinsam im selben Kurs sind.
	 */
	public getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1 : number, idSchueler2 : number, idFach : number) : boolean {
		const kurs1 : GostBlockungsergebnisKurs | null = this._schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler1, idFach);
		const kurs2 : GostBlockungsergebnisKurs | null = this._schuelerID_fachID_to_kurs_or_null.getOrNull(idSchueler2, idFach);
		return (kurs1 === null) || (kurs2 === null) ? false : kurs1.id === kurs2.id;
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
		return DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_kurs, idKurs);
	}

	/**
	 * Gibt die Menge der {@link GostBlockungsergebnisKurs} zurück.
	 *
	 * @return die Menge der {@link GostBlockungsergebnisKurs}
	 */
	public getKursmenge() : List<GostBlockungsergebnisKurs> {
		const result : List<GostBlockungsergebnisKurs> = new ArrayList();
		result.addAll(this._kursID_to_kurs.values());
		return result;
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
	 * Liefert TRUE, falls der Kurs der Schiene mit der Nummer zugeordnet ist.
	 *
	 * @param  idKurs      Die Datenbank-ID des Kurses.
	 * @param  schienenNr  Die Nummer der Schiene
	 *
	 * @return TRUE, falls der Kurs der Schiene mit der Nummer zugeordnet ist.
	 */
	public getOfKursOfSchienenNrIstZugeordnet(idKurs : number, schienenNr : number) : boolean {
		for (const nr of this.getOfKursSchienenNummern(idKurs))
			if (nr === schienenNr)
				return true;
		return false;
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
		return this._parent.kursGetHatFixierungInSchiene(idKurs, idSchiene);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schuelerIDSet, idKurs);
	}

	/**
	 * Liefert die Menge aller Schüler-Objekte des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-Objekte des Kurses.
	 */
	public getOfKursSchuelermenge(idKurs : number) : List<Schueler> {
		const list : List<Schueler> = new ArrayList();
		for (const idSchueler of this.getKursE(idKurs).schueler)
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
		return DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs);
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
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 */
	public getOfKursAnzahlSchueler(idKurs : number) : number {
		return this.getKursE(idKurs).schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 */
	public getOfKursAnzahlSchuelerPlusDummy(idKurs : number) : number {
		return this.getKursE(idKurs).schueler.size() + DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_dummySuS, idKurs);
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS des Kurses. Dummy-SuS werden durch die Regel mit dem
	 * Typ {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN} einem Kurs zugeordnet.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Dummy-SuS des Kurses.
	 */
	public getOfKursAnzahlSchuelerDummy(idKurs : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_dummySuS, idKurs)!;
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
		return this._kursID_to_schuelerIDSet;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public getMappingKursIDSchienenmenge() : JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> {
		return this._kursID_to_schienenSet;
	}

	/**
	 * Liefert eine Menge aller Kurse mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Kurse mit mindestens einer Kollision.
	 */
	public getMengeDerKurseMitKollisionen() : JavaSet<GostBlockungsergebnisKurs> {
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const kurs of this._kursID_to_kurs.values())
			if (this.getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
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
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, kursG.id)) {
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
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, kursG.id)) {
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
	 * Liefert die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 * <br>Hinweis: Nur verletzte Regel-IDs sind in der KEY-Menge enthalten.
	 *
	 * @return die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 */
	public regelGetMap_regelID_to_verletzungString() : JavaMap<number, string> {
		return this._regelID_to_verletzungString;
	}

	/**
	 * Liefert die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 * <br>Hinweis: Falls ein Kurs bereits fixierte Schienen hat, werden dazu keine Regeln erzeugt.
	 *
	 * @return die Dummy-Regel-Menge (ID=-1) aller möglichen Kurs-Schienen-Fixierungen.
	 */
	public regelGetDummyMengeAllerKursSchienenFixierungen() : List<GostBlockungRegel> {
		const list : List<GostBlockungRegel> = new ArrayList();
		for (const kurs of this._kursID_to_kurs.values())
			for (const schiene of this.getOfKursSchienenmenge(kurs.id))
				if (!this.getOfKursOfSchieneIstFixiert(kurs.id, schiene.id)) {
					const schienenNr : number = this._parent.schieneGet(schiene.id).nummer;
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr));
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
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs!, schienenNr));
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
		for (const kurs of this._kursID_to_kurs.values())
			for (const schueler of this.getOfKursSchuelermenge(kurs.id))
				if (!this.getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, kurs.id));
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
		for (const kurs of this._kursID_to_kurs.values())
			for (const schueler of this.getOfKursSchuelermenge(kurs.id))
				if ((this.getOfSchuelerOfKursIstAbiturfach(schueler.id, kurs.id)) && (!this.getOfSchuelerOfKursIstFixiert(schueler.id, kurs.id)))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, kurs.id));
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
				if (!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs!))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, idKurs!));
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
				if ((this.getOfSchuelerOfKursIstAbiturfach(schueler.id, idKurs!)) && (!this.getOfSchuelerOfKursIstFixiert(schueler.id, idKurs!)))
					list.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, schueler.id, idKurs!));
		return list;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 */
	regelGetTooltipFuerRegelverletzungen() : string {
		return this._regelverletzungen_tooltip1_regeln;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 */
	regelGetTooltipFuerFaecherparallelitaet() : string {
		return this._regelverletzungen_tooltip4_faecherparallelitaet;
	}

	/**
	 * Liefert einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 *
	 * @return einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 */
	regelGetTooltipFuerWahlkonflikte() : string {
		return this._regelverletzungen_tooltip2_wahlkonflikte;
	}

	/**
	 * Liefert einen Tooltip für alle Kursdifferenzen.
	 *
	 * @return einen Tooltip für alle Kursdifferenzen.
	 */
	regelGetTooltipFuerKursdifferenzen() : string {
		return this._regelverletzungen_tooltip3_kursdifferenzen;
	}

	private static regelupdateIsEqualPair(a1 : number, a2 : number, b1 : number, b2 : number) : boolean {
		return ((a1 === b1) && (a2 === b2)) || ((a1 === b2) && (a2 === b1));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Sperrung zu setzen.
	 * <br>(1) Wenn ein Kurs der Kursart im Schienen-Bereich liegt und gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn ein Kurs der Kursart im Schienen-Bereich liegt und fixiert ist, wird dies entfernt.
	 * <br>(3a) Wenn die Regel in falscher von/bis-Reihenfolge existiert, wird sie entfernt.
	 * <br>(3b) Wenn die Regel nicht bereits existiert, wird sie hinzugefügt.
	 *
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Sperrung zu setzen.
	 */
	public regelupdateCreate_01_KURSART_SPERRE_SCHIENEN_VON_BIS(kursart : number, schienenNrVon : number, schienenNrBis : number) : GostBlockungRegelUpdate {
		const von : number = Math.min(schienenNrVon, schienenNrBis);
		const bis : number = Math.max(schienenNrVon, schienenNrBis);
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const kurs of this.getKursmenge())
			for (let schienenNr : number = von; schienenNr <= bis; schienenNr++)
				if (kurs.kursart === kursart) {
					const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung !== null)
						u.listEntfernen.add(regelSperrung);
					const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung !== null)
						u.listEntfernen.add(regelFixierung);
				}
		const keyBisVon : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, bis, von]);
		const regelBisVon : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyBisVon);
		if (regelBisVon !== null)
			u.listEntfernen.add(regelBisVon);
		const keyVonBis : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis]);
		if (this._parent.regelGetByLongArrayKeyOrNull(keyVonBis) === null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Allein-Zuordnung zu setzen.
	 * <br>(1) Alle Regeln der selben Kursart werden zunächst entfernt, da zwei solcher Regeln sich widersprechen würden.
	 * <br>(2) Wenn eine Kursart-Fixierung im falschen Bereich liegt, wird die Fixierung entfernt.
	 * <br>(3) Wenn eine Kursart-Sperrung im falschen Bereich liegt, wird die Sperrung entfernt.
	 * <br>(4) Zuletzt wird die Regel neu erzeugt und hinzugefügt.
	 *
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursart-Schienenmengen-Allein-Zuordnung zu setzen.
	 */
	public regelupdateCreate_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS(kursart : number, schienenNrVon : number, schienenNrBis : number) : GostBlockungRegelUpdate {
		const von : number = Math.min(schienenNrVon, schienenNrBis);
		const bis : number = Math.max(schienenNrVon, schienenNrBis);
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const rGleicheKursart of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if (kursart === rGleicheKursart.parameter.get(0))
				u.listEntfernen.add(rGleicheKursart);
		for (const kurs of this.getKursmenge())
			for (let schienenNr : number = 1; schienenNr <= this._parent.schieneGetAnzahl(); schienenNr++) {
				const imSchienenBereich : boolean = (von <= schienenNr) && (schienenNr <= bis);
				const richtigeKursart : boolean = (kurs.kursart === kursart);
				if (imSchienenBereich !== richtigeKursart) {
					const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung !== null)
						u.listEntfernen.add(rFixierung);
					const kSperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					const rSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kSperrung);
					if (rSperrung !== null)
						u.listEntfernen.add(rSperrung);
				}
			}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ, kursart, von, bis));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung einer Rechtecks-Auswahl zu realisieren.
	 * <br>(1) Wenn der Kurs markiert ist und eine Sperrung hat, wird die Sperrung entfernt.
	 * <br>(2) Wenn der Kurs markiert ist und keine Fixierung hat, wird eine Fixierung erzeugt.
	 * <br>(3) Fixierungen außerhalb der Kurslage werden vorsichtshalber gelöscht.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller markierten Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung einer Rechtecks-Auswahl zu realisieren.
	 */
	public regelupdateCreate_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const nr of setSchienenNr)
				if (this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					const kSperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nr]);
					const rSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kSperrung);
					if (rSperrung !== null)
						u.listEntfernen.add(rSperrung);
					const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
					const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung === null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				}
			for (let nr : number = 1; nr <= this._schienenNR_to_schiene.size(); nr++)
				if (!this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
					const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
					if (rFixierung !== null)
						u.listEntfernen.add(rFixierung);
				}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Fixierung zu lösen.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und bereits fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Fixierung zu lösen.
	 */
	public regelupdateRemove_02_KURS_FIXIERE_IN_SCHIENE_MARKIERT(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs)) {
				const schieneG : GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					const keyKursInSchiene : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regel : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyKursInSchiene);
					if (regel !== null)
						u.listEntfernen.add(regel);
				}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 * <br>(1) Fixierungen innerhalb der Kurslage werden hinzugefügt, falls noch nicht existend.
	 * <br>(2) Fixierungen außerhalb der Kurslage werden gelöscht.
	 *
	 * @param setKursID  Die Kursmenge, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 */
	public regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (let nr : number = 1; nr <= this._schienenNR_to_schiene.size(); nr++) {
				const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
				const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					if (rFixierung === null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				} else {
					if (rFixierung !== null)
						u.listEntfernen.add(rFixierung);
				}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 * <br>(1) Alle Fixierungen der Kursmenge werden gelöst.
	 *
	 * @param setKursID  Die Kursmenge, deren Fixierungen gelöst werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Fixierung einer Kursmenge komplett zu lösen.
	 */
	public regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (let nr : number = 1; nr <= this._schienenNR_to_schiene.size(); nr++) {
				const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
				const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung !== null)
					u.listEntfernen.add(rFixierung);
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurse komplett in ihrer Lage zu fixieren.
	 * <br>(1) Fixierungen innerhalb der Kurslage werden hinzugefügt, falls noch nicht existend.
	 * <br>(2) Fixierungen außerhalb der Kurslage werden gelöscht.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurse komplett in ihrer Lage zu fixieren.
	 */
	public regelupdateCreate_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(this._parent.kursmengeGetSetDerIDs());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 */
	public regelupdateRemove_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN() : GostBlockungRegelUpdate {
		return this.regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(this._parent.kursmengeGetSetDerIDs());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Fixierung zu realisieren.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und fixiert ist, wird die Fixierung gelöst.
	 * <br>(2) Wenn der Kurs im Schienen-Bereich liegt und nicht fixiert ist, wird er fixiert
	 * <br>(3) und falls dort eine Sperrung vorliegt, dann wird die Sperrung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Fixierung zu realisieren.
	 */
	public regelupdateCreate_02d_KURS_FIXIERE_IN_SCHIENE_TOGGLE(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs)) {
				const schieneG : GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung !== null) {
						u.listEntfernen.add(regelFixierung);
						continue;
					}
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer));
					const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung !== null)
						u.listEntfernen.add(regelSperrung);
				}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um einen Kurs in einer Schiene zu fixieren.
	 * <br>(1) Wenn der Kurs in der Schiene eine Sperrung hat, wird diese entfernt.
	 * <br>(2) Wenn der Kurs bereits in der Schiene fixiert ist, passiert nichts weiteres.
	 * <br>(3) Wenn der Kurs bereits vollständig fixiert ist, werden seine alten Fixierungen entfernt.
	 * <br>(4) Andernfalls wird der Kurs in der Schiene fixiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um einen Kurs in einer Schiene zu fixieren.
	 */
	public regelupdateCreate_02e_KURS_FIXIERE_IN_EINER_SCHIENE(idKurs : number, schienenNr : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const kSperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
		const rSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kSperrung);
		if (rSperrung !== null)
			u.listEntfernen.add(rSperrung);
		const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
		const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung !== null)
			return u;
		if (!this._parent.kursIstWeitereFixierungErlaubt(idKurs))
			for (let nr : number = 1; nr <= this._schienenNR_to_schiene.size(); nr++) {
				const kFixierungAlt : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
				const rFixierungAlt : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierungAlt);
				if (rFixierungAlt !== null)
					u.listEntfernen.add(rFixierungAlt);
			}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung zu lösen.
	 * <br>(1) Wenn der Kurs in der Schiene fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die gelöst werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kurs-Schienen-Fixierung zu lösen.
	 */
	public regelupdateRemove_02e_KURS_FIXIERE_IN_EINER_SCHIENE(idKurs : number, schienenNr : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
		const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung !== null)
			u.listEntfernen.add(rFixierung);
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu setzen.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich nicht gesperrt ist und keine Fixierung vorliegt, wird er gesperrt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu setzen.
	 */
	public regelupdateCreate_03_KURS_SPERRE_IN_SCHIENE(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (const schienenNr of setSchienenNr) {
				const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
				const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
				const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
				const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if ((regelSperrung === null) && (regelFixierung === null))
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr));
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu lösen.
	 * <br>(1) Wenn der Kurs in dem Schienen-Bereich gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Sperrung zu lösen.
	 */
	public regelupdateRemove_03_KURS_SPERRE_IN_SCHIENE(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (const schienenNr of setSchienenNr) {
				const keyGesperrt : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
				const regelGesperrt : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyGesperrt);
				if (regelGesperrt !== null)
					u.listEntfernen.add(regelGesperrt);
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Sperrung zu realisieren.
	 * <br>(1) Wenn der Kurs im Schienen-Bereich liegt und gesperrt ist, wird die Sperrung gelöst.
	 * <br>(2) Wenn der Kurs im Schienen-Bereich liegt und nicht gesperrt ist und keine Fixierung vorliegt, wird er gesperrt.
	 *
	 * @param setKursID      Die Menge aller Kurs-IDs.
	 * @param setSchienenNr  Die Menge aller Schienen-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmengen-Schienemengen-Toggle-Sperrung zu realisieren.
	 */
	public regelupdateCreate_03b_KURS_SPERRE_IN_SCHIENE_TOGGLE(setKursID : JavaSet<number>, setSchienenNr : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID)
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this._kursID_to_schienenSet, idKurs)) {
				const schieneG : GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung !== null) {
						u.listEntfernen.add(regelSperrung);
						continue;
					}
					const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung === null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer));
				}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler bereits im Kurs fixiert ist, wird dies ignoriert.
	 * <br>(4) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 * TODO Wenn der Schüler gar nicht den Kurs wählen kann --> ignorieren.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 */
	public regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(setSchuelerID : JavaSet<number>, setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID)
			for (const idKurs of setKursID) {
				const kurs1 : GostBlockungKurs = this._parent.kursGet(idKurs);
				const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
				const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung !== null)
					u.listEntfernen.add(regelSperrung);
				for (const kurs2 of this._parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
					const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
					const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (kurs1.id === kurs2.id) {
						if (regelFixierung === null) {
							u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs));
						}
					} else {
						if (regelFixierung !== null)
							u.listEntfernen.add(regelFixierung);
					}
				}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu lösen.
	 */
	public regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(setSchuelerID : JavaSet<number>, setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID)
			for (const idKurs of setKursID) {
				const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
				const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung !== null)
					u.listEntfernen.add(regelFixierung);
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler bereits im Kurs fixiert ist, wird dies ignoriert.
	 * <br>(4) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu setzen.
	 */
	public regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const u2 : GostBlockungRegelUpdate = this.regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen einer Kursmenge zu lösen.
	 */
	public regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const u2 : GostBlockungRegelUpdate = this.regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 * <br>Die Methode delegiert alles an {@link #regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS}.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 */
	public regelupdateCreate_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(this._kursID_to_kurs.keySet());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 * <br>Die Methode delegiert alles an {@link #regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS}.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 */
	public regelupdateRemove_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN() : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		u.listEntfernen.addAll(this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um ein Schüler-Kurs-Fixierung-Toggle zu realisieren.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler im Kurs nicht fixiert ist, wird die Fixierung gesetzt.
	 * <br>(3) Potentielle Fixierungen in Nachbar-Kursen werden entfernt, da sie in jedem Fall falsch sind.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um ein Schüler-Kurs-Fixierung-Toggle zu realisieren.
	 */
	public regelupdateRemove_04d_SCHUELER_FIXIEREN_IN_DEN_KURSEN_TOGGLE(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs1)) {
				const kFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1]);
				const rFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung !== null) {
					u.listEntfernen.add(rFixierung);
				} else {
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1));
				}
				const kurs1 : GostBlockungKurs = this._parent.kursGet(idKurs1);
				for (const kurs2 of this._parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart))
					if (kurs1.id !== kurs2.id) {
						const kFixierung2 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
						const rFixierung2 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(kFixierung2);
						if (rFixierung2 !== null)
							u.listEntfernen.add(rFixierung2);
					}
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 */
	public regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle LK-Schüler einer Kursmenge zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle LK-Schüler einer Kursmenge zu fixieren.
	 */
	public regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstLK(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 */
	public regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 */
	public regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 */
	public regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 */
	public regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstLKoderAB3(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 */
	public regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 */
	public regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstAB4(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 */
	public regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 */
	public regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstAbiturfach(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 */
	public regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH() : GostBlockungRegelUpdate {
		return this.regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(this._kursIDset);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param kursIDs  Die Menge der Kurse.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 */
	public regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(kursIDs : JavaSet<number>) : GostBlockungRegelUpdate {
		const schuelerKursPaare : HashSet<PairNN<number, number>> = new HashSet();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (this.getOfSchuelerOfKursIstSchriftlich(idSchueler, idKurs))
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 *
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param schuelerKursPaare  Die Menge aller Schüler-Kurs-Paare.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private regelupdateCreate_04x_SCHUELER_FIXIEREN_IN_KURS(schuelerKursPaare : JavaSet<PairNN<number, number>>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const pair of schuelerKursPaare) {
			const idSchueler : number = pair.a;
			const idKurs : number = pair.b;
			const kurs1 : GostBlockungKurs = this._parent.kursGet(idKurs!);
			const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
			const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
			if (regelSperrung !== null)
				u.listEntfernen.add(regelSperrung);
			for (const kurs2 of this._parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
				const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
				const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (kurs1.id === kurs2.id) {
					if (regelFixierung === null) {
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler!, idKurs!));
					}
				} else {
					if (regelFixierung !== null) {
						u.listEntfernen.add(regelFixierung);
					}
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu setzen.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs gesperrt ist, wird er gesperrt.
	 *
	 * @param setSchuelerID  Die Menge Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu setzen.
	 */
	public regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(setSchuelerID : JavaSet<number>, setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID)
			for (const idKurs of setKursID) {
				const keyFixierung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
				const regelFixierung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if (regelFixierung !== null)
					u.listEntfernen.add(regelFixierung);
				const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
				const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung === null)
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs));
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Sperrung zu lösen.
	 */
	public regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(setSchuelerID : JavaSet<number>, setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID)
			for (const idKurs of setKursID) {
				const keySperrung : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
				const regelSperrung : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySperrung);
				if (regelSperrung !== null)
					u.listEntfernen.add(regelSperrung);
			}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu sperren.
	 * <br>(1) Wenn der Schüler im Kurs fixiert ist, wird die Fixierung entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs gesperrt ist, wird er gesperrt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu sperren.
	 */
	public regelupdateCreate_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const u2 : GostBlockungRegelUpdate = this.regelupdateCreate_05_SCHUELER_VERBIETEN_IN_KURS(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu lösen.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird die Sperrung entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Sperrungen der Kurse zu lösen.
	 */
	public regelupdateRemove_05b_SCHUELER_VERBIETEN_IN_DEN_KURSEN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const u2 : GostBlockungRegelUpdate = this.regelupdateRemove_05_SCHUELER_VERBIETEN_IN_KURS(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs));
			u.listEntfernen.addAll(u2.listEntfernen);
			u.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B zusammen sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel mit bereits existiert, aber die IDs nicht aufsteigend sind, wird dies entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 */
	public regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID)
			for (const idKurs2 of setKursID)
				if (idKurs1 < idKurs2) {
					const keyZusammen12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelZusammen12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 !== null)
						u.listEntfernen.add(regelZusammen12);
					const keyZusammen21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelZusammen21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 !== null)
						u.listEntfernen.add(regelZusammen21);
					const keyVerboten21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelVerboten21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 !== null)
						u.listEntfernen.add(regelVerboten21);
					const keyVerboten12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelVerboten12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 === null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2));
				}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu lösen.
	 * <br>(1) Wenn das Kurs-Kurs-Verbot existiert (in beliebiger Permutation), wird es entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu lösen.
	 */
	public regelupdateRemove_07_KURS_VERBIETEN_MIT_KURS(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID)
			for (const idKurs2 of setKursID)
				if (idKurs1 < idKurs2) {
					const keyVerboten12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelVerboten12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 !== null)
						u.listEntfernen.add(regelVerboten12);
					const keyVerboten21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelVerboten21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 !== null)
						u.listEntfernen.add(regelVerboten21);
				}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B verboten sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel mit bereits existiert, aber die IDs nicht aufsteigend sind, wird dies entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 */
	public regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID)
			for (const idKurs2 of setKursID)
				if (idKurs1 < idKurs2) {
					const keyVerboten12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelVerboten12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten12);
					if (regelVerboten12 !== null)
						u.listEntfernen.add(regelVerboten12);
					const keyVerboten21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelVerboten21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerboten21);
					if (regelVerboten21 !== null)
						u.listEntfernen.add(regelVerboten21);
					const keyZusammen21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelZusammen21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 !== null)
						u.listEntfernen.add(regelZusammen21);
					const keyZusammen12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelZusammen12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 === null)
						u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2));
				}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Gebote von setKursID (alle Paarungen) zu lösen.
	 * <br>(1) Wenn das Kurs-Kurs-Gebot existiert (in beliebiger Permutation), wird es entfernt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Gebote von setKursID (alle Paarungen) zu lösen.
	 */
	public regelupdateRemove_08_KURS_ZUSAMMEN_MIT_KURS(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID)
			for (const idKurs2 of setKursID)
				if (idKurs1 < idKurs2) {
					const keyZusammen12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2]);
					const regelZusammen12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
					if (regelZusammen12 !== null)
						u.listEntfernen.add(regelZusammen12);
					const keyZusammen21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1]);
					const regelZusammen21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
					if (regelZusammen21 !== null)
						u.listEntfernen.add(regelZusammen21);
				}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Anzahl der Dummy-Schüler eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert größer 0 hat, wird die Regel hinzugefügt.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die Anzahl an Dummy-Schülern.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Anzahl der Dummy-Schüler eines Kurses zu setzen.
	 */
	public regelupdateCreate_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKurs : number, anzahl : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const rAlt of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (idKurs === rAlt.parameter.get(0))
				u.listEntfernen.add(rAlt);
		if (anzahl > 0)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, idKurs, anzahl));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um "Lehrkräfte beachten" zu aktivieren/deaktivieren.
	 * <br>(1) Wenn erstellen==FALSE und die Regel existiert, wird sie entfernt.
	 * <br>(2) Wenn erstellen==TRUE und die Regel existiert nicht, wird sie erzeugt.
	 *
	 * @param erstellen  Falls TRUE, wird die Regel aktiviert, andernfalls deaktiviert.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um "Lehrkräfte beachten" zu aktivieren/deaktivieren.
	 */
	public regelupdateCreate_10_LEHRKRAEFTE_BEACHTEN(erstellen : boolean) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const keyDummyAlt : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ]);
		const regelDummyAlt : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyDummyAlt);
		if ((!erstellen) && (regelDummyAlt !== null))
			u.listEntfernen.add(regelDummyAlt);
		if ((erstellen) && (regelDummyAlt === null))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel0(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zusammen zu setzen.
	 * <br>(1) Wenn beide Schüler-IDs identisch sind, wird die Regel ignoriert.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Verbieten-Regel gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(5a) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel in falscher Schüler-ID-Reihenfolge gibt, wird sie entfernt.
	 * <br>(5b) Wenn es keine Schüler-Schüler-Fach-Zusammen-Regel gibt, wird sie hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zusammen zu setzen.
	 */
	public regelupdateCreate_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH(idSchueler1 : number, idSchueler2 : number, idFach : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1 : number = Math.min(idSchueler1, idSchueler2);
		const idS2 : number = Math.max(idSchueler1, idSchueler2);
		if (idS1 === idS2)
			return u;
		const keyVerbietenFach12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		const regelVerbietenFach12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach12);
		if (regelVerbietenFach12 !== null)
			u.listEntfernen.add(regelVerbietenFach12);
		const keyVerbietenFach21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		const regelVerbietenFach21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 !== null)
			u.listEntfernen.add(regelVerbietenFach21);
		const keyVerbieten12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2]);
		const regelVerbieten12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 !== null)
			u.listEntfernen.add(regelVerbieten12);
		const keyVerbieten21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1]);
		const regelVerbieten21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 !== null)
			u.listEntfernen.add(regelVerbieten21);
		const keyZusammen12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2]);
		const regelZusammen12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 !== null)
			u.listEntfernen.add(regelZusammen12);
		const keyZusammen21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1]);
		const regelZusammen21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 !== null)
			u.listEntfernen.add(regelZusammen21);
		const keyZusammenFach21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		const regelZusammenFach21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 !== null)
			u.listEntfernen.add(regelZusammenFach21);
		const keyZusammenFach12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		const regelZusammenFach12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammenFach12);
		if (regelZusammenFach12 === null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zu verbieten.
	 * <br>(1) Wenn beide Schüler-IDs identisch sind, wird die Regel ignoriert.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Verboten-Regel gibt, wird diese entfernt.
	 * <br>(5a) Wenn es eine Schüler-Schüler-Fach-Verboten-Regel in falscher Schüler-ID-Reihenfolge gibt, wird sie entfernt.
	 * <br>(5b) Wenn es keine Schüler-Schüler-Fach-Verboten-Regel gibt, wird sie hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in einem Fach zu verbieten.
	 */
	public regelupdateCreate_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH(idSchueler1 : number, idSchueler2 : number, idFach : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1 : number = Math.min(idSchueler1, idSchueler2);
		const idS2 : number = Math.max(idSchueler1, idSchueler2);
		if (idS1 === idS2)
			return u;
		const keyZusammenFach12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		const regelZusammenFach12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammenFach12);
		if (regelZusammenFach12 !== null)
			u.listEntfernen.add(regelZusammenFach12);
		const keyZusammenFach21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		const regelZusammenFach21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammenFach21);
		if (regelZusammenFach21 !== null)
			u.listEntfernen.add(regelZusammenFach21);
		const keyZusammen12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2]);
		const regelZusammen12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen12);
		if (regelZusammen12 !== null)
			u.listEntfernen.add(regelZusammen12);
		const keyZusammen21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1]);
		const regelZusammen21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyZusammen21);
		if (regelZusammen21 !== null)
			u.listEntfernen.add(regelZusammen21);
		const keyVerbieten12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2]);
		const regelVerbieten12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbieten12);
		if (regelVerbieten12 !== null)
			u.listEntfernen.add(regelVerbieten12);
		const keyVerbieten21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1]);
		const regelVerbieten21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbieten21);
		if (regelVerbieten21 !== null)
			u.listEntfernen.add(regelVerbieten21);
		const keyVerbietenFach21 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		const regelVerbietenFach21 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach21);
		if (regelVerbietenFach21 !== null)
			u.listEntfernen.add(regelVerbietenFach21);
		const keyVerbietenFach12 : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		const regelVerbietenFach12 : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyVerbietenFach12);
		if (regelVerbietenFach12 === null)
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zusammen zu setzen.
	 * <br>(1) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt (aber später hinzugefügt).
	 * <br>(4) Wenn es eine Schüler-Schüler-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(5) Wenn die Schüler-IDs gültig sind, wird nun die Schüler-Schüler-Zusammen-Regel hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zusammen zu setzen.
	 */
	public regelupdateCreate_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER(idSchueler1 : number, idSchueler2 : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1 : number = Math.min(idSchueler1, idSchueler2);
		const idS2 : number = Math.max(idSchueler1, idSchueler2);
		for (const r11 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r11.parameter.get(0)!, r11.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r11);
		for (const r12 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r12.parameter.get(0)!, r12.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r12);
		for (const r13 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r13.parameter.get(0)!, r13.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r13);
		for (const r14 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r14.parameter.get(0)!, r14.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r14);
		if ((0 <= idS1) && (idS1 < idS2))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zu verbieten.
	 * <br>(1) Wenn es eine Schüler-Schüler-Fach-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(2) Wenn es eine Schüler-Schüler-Fach-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(3) Wenn es eine Schüler-Schüler-Zusammen-Regel mit den selben Schülern gibt, wird diese entfernt.
	 * <br>(4) Wenn es eine Schüler-Schüler-Verbieten-Regel mit den selben Schülern gibt, wird diese entfernt (aber später hinzugefügt).
	 * <br>(5) Wenn die Schüler-IDs gültig sind, wird nun die Schüler-Schüler-Verbieten-Regel hinzugefügt.
	 *
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um zwei Schüler in jedem gemeinsamen Fach zu verbieten.
	 */
	public regelupdateCreate_14_SCHUELER_VERBIETEN_MIT_SCHUELER(idSchueler1 : number, idSchueler2 : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1 : number = Math.min(idSchueler1, idSchueler2);
		const idS2 : number = Math.max(idSchueler1, idSchueler2);
		for (const r11 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r11.parameter.get(0)!, r11.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r11);
		for (const r12 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r12.parameter.get(0)!, r12.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r12);
		for (const r13 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r13.parameter.get(0)!, r13.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r13);
		for (const r14 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER))
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(r14.parameter.get(0)!, r14.parameter.get(1)!, idS1, idS2))
				u.listEntfernen.add(r14);
		if ((0 <= idS1) && (idS1 < idS2))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert im Intervall [0;99], wird die Regel hinzugefügt.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die Anzahl an Dummy-Schülern.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 */
	public regelupdateCreate_15_KURS_MAXIMALE_SCHUELERANZAHL(idKurs : number, anzahl : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const rAlt of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL))
			if (idKurs === rAlt.parameter.get(0))
				u.listEntfernen.add(rAlt);
		if ((anzahl >= 0) && (anzahl <= 99))
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, idKurs, anzahl));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermenge beim Blocken zu ignorieren.
	 * <br>(1) Wenn diese Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermenge beim Blocken zu ignorieren.
	 */
	public regelupdateCreate_16_SCHUELER_IGNORIEREN(setSchuelerID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID) {
			const keySchuelerIgnorieren : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler]);
			const regelSchuelerIgnorieren : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keySchuelerIgnorieren);
			if (regelSchuelerIgnorieren === null)
				u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge von Kursdifferenz-Berechnungen auszuschließen.
	 * <br>(1) Wenn diese Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge von Kursdifferenz-Berechnungen auszuschließen.
	 */
	public regelupdateCreate_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN(setKursID : JavaSet<number>) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const keyKurs_KD_ignorieren : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs]);
			const regelKurs_KD_ignorieren : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyKurs_KD_ignorieren);
			if (regelKurs_KD_ignorieren === null)
				u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Kursen einer bestimmten Fachart (in jeder Schiene) zu begrenzen.
	 * <br>(1) Falls die Regel bereits existiert, wird sie zunächst entfernt.
	 * <br>(2) Falls der "maximal"-Wert 1 ist und es Regeln KURS_VERBIETEN_MIT_KURS bei Paaren dieser Fachart gibt, werden die Regeln entfernt.
	 * <br>(3) Zuletzt wird die neue Regel hinzugefügt.
	 *
	 * @param idFach     Die Datenbank-ID des Faches.
	 * @param idKursart  Die ID der Kursart.
	 * @param maximal    Die maximale Kursanzahl dieser Fachart (in jeder Schiene). Gültige Werte sind 1 bis 9.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Kursen einer bestimmten Fachart (in jeder Schiene) zu begrenzen.
	 */
	public regelupdateCreate_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE(idFach : number, idKursart : number, maximal : number) : GostBlockungRegelUpdate {
		const u : GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const r18 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE))
			if ((r18.parameter.get(0) === idFach) && (r18.parameter.get(1) === idKursart))
				u.listEntfernen.add(r18);
		if (maximal === 1)
			for (const r7 of this._parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
				const idKurs1 : number = r7.parameter.get(0).valueOf();
				const idKurs2 : number = r7.parameter.get(1).valueOf();
				const kurs1 : GostBlockungsergebnisKurs = this.getKursE(idKurs1);
				const kurs2 : GostBlockungsergebnisKurs = this.getKursE(idKurs2);
				if ((kurs1.fachID === idFach) && (kurs2.fachID === idFach) && (kurs1.kursart === idKursart) && (kurs2.kursart === idKursart))
					u.listEntfernen.add(r7);
			}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ, idFach, idKursart, maximal));
		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungRegelUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungRegelUpdate#listHinzuzufuegen} hinzu.
	 *
	 * @param update  Das {@link GostBlockungRegelUpdate}-Objekt.
	 */
	public regelupdateExecute(update : GostBlockungRegelUpdate) : void {
		DeveloperNotificationException.ifTrue("Ein RegelUpdate ist nur bei einer Blockungsvorlage erlaubt!", !this._parent.getIstBlockungsVorlage());
		this._parent.regelRemoveListe(update.listEntfernen);
		this._parent.regelAddListe(update.listHinzuzufuegen);
		this.stateRevalidateEverything();
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus den derzeit zugeordneten Kursen zu entfernen.
	 * <br>(1) Wenn ein Schüler in einem Kurs ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn ein Schüler in einem Kurs ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist.
	 *
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus den derzeit zugeordneten Kursen zu entfernen.
	 */
	public kursSchuelerUpdate_01_LEERE_ALLE_KURSE(entferneAuchFixierte : boolean) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		return this.kursSchuelerUpdate_01b_LEERE_KURSMENGE(this._kursIDset, entferneAuchFixierte);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus der übergebenen Kursmenge zu entfernen.
	 * <br>(1) Wenn ein Schüler in einem Kurs ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn ein Schüler in einem Kurs ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist.
	 *
	 * @param kursIDs               Die Menge der Kurse.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um alle Schüler aus der übergebenen Kursmenge zu entfernen.
	 */
	public kursSchuelerUpdate_01b_LEERE_KURSMENGE(kursIDs : JavaSet<number>, entferneAuchFixierte : boolean) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const idKurs of kursIDs)
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs))
				if (entferneAuchFixierte || !this._parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs)) {
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 * <br>(1) Wenn der Schüler dem Kurs zugeordnet ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn der Schüler dem Kurs zugeordnet ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist. Auch die Fixierungs-Regel wird entfernt.
	 *
	 * @param schuelerIDs           Die Menge der Schüler-IDs.
	 * @param idKurs                Die Datenbank-ID des Kurses aus dem die Schüler entfernt werden sollen.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 */
	public kursSchuelerUpdate_02a_ENTFERNE_SCHUELERMENGE_AUS_KURS(schuelerIDs : JavaSet<number>, idKurs : number, entferneAuchFixierte : boolean) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		const setSchulerOfKurs : JavaSet<number> = this.getOfKursSchuelerIDmenge(idKurs);
		for (const idSchueler of schuelerIDs) {
			if (!setSchulerOfKurs.contains(idSchueler))
				continue;
			const keyFixiert : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
			const regelFixiert : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixiert);
			if (regelFixiert === null) {
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				continue;
			}
			if (entferneAuchFixierte) {
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				u.regelUpdates.listEntfernen.add(regelFixiert);
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 * <br>(1) Wenn der Schüler den Kurs gar nicht gewählt hat, wird dies ignoriert.
	 * <br>(2) Wenn der Schüler nicht im Kurs ist, wird er hinzugefügt.
	 * <br>(3) Wenn der Schüler in einem Nachbar-Kurs ist, wird er entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 */
	public kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(kursSchuelerZuordnungen : JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			const kurs1 : GostBlockungKurs = this._parent.kursGet(z.idKurs);
			if (!this.getOfSchuelerHatFachwahl(z.idSchueler, kurs1.fach_id, kurs1.kursart))
				continue;
			if (!this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listHinzuzufuegen.add(z);
			for (const kurs2 of this._parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart))
				if ((kurs1.id !== kurs2.id) && (this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, kurs2.id)))
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs2.id, z.idSchueler));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 * <br>(1) Wenn der Schüler den Ziel-Kurs nicht wählen darf (falsche Fachwahlen), dann passiert nichts.
	 * <br>(2) Wenn ein Schüler aus einem fixierten Kurs verschoben werden soll, dies aber nicht erlaubt ist, dann passiert nichts.
	 * <br>(3) Der Schüler wird ggf. aus einem alten Kurs entfernt und die Fixier-Regel des alten Kurses wird ggf. entfernt.
	 * <br>(4) Der Schüler wird einem neuen Kurs hinzugefügt und wird ggf. im neuen Kurs fixiert.
	 *
	 * @param kursSchuelerZuordnungen           Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 * @param verschiebeFixierteDesQuellkurses  TRUE, dann werden fixierte SuS aus potentiell alten Kursen entfernt.
	 * @param fixiereImZielkurs                 TRUE, dann werden die SuS im Zielkurs fixiert.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 */
	public kursSchuelerUpdate_03a_VERSCHIEBE_SCHUELER_ZU_KURSEN(kursSchuelerZuordnungen : JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>, verschiebeFixierteDesQuellkurses : boolean, fixiereImZielkurs : boolean) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			const kursNeu : GostBlockungKurs = this._parent.kursGet(z.idKurs);
			if (!this.getOfSchuelerHatFachwahl(z.idSchueler, kursNeu.fach_id, kursNeu.kursart))
				continue;
			const kursAlt : GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(z.idSchueler, kursNeu.fach_id);
			if (kursAlt !== null) {
				const keyFixiertAlt : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, kursAlt.id]);
				const regelFixiertAlt : GostBlockungRegel | null = this._parent.regelGetByLongArrayKeyOrNull(keyFixiertAlt);
				if ((regelFixiertAlt !== null) && (!verschiebeFixierteDesQuellkurses))
					continue;
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursAlt.id, z.idSchueler));
				if (regelFixiertAlt !== null)
					u.regelUpdates.listEntfernen.add(regelFixiertAlt);
			}
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursNeu.id, z.idSchueler));
			if (fixiereImZielkurs)
				u.regelUpdates.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, kursNeu.id));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 * <br>(1) Wenn der Schüler im Kurs ist, wird er entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 */
	public kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(kursSchuelerZuordnungen : JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>) : GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			if (this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				u.listEntfernen.add(z);
		}
		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate#listHinzuzufuegen} hinzu.
	 * Macht das selbe für die potentiell enthaltenen RegelUpdates.
	 *
	 * @param update  Das {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt.
	 */
	public kursSchuelerUpdateExecute(update : GostBlockungsergebnisKursSchuelerZuordnungUpdate) : void {
		if (this._parent.getIstBlockungsVorlage())
			this._parent.regelRemoveListe(update.regelUpdates.listEntfernen);
		for (const z of update.listEntfernen)
			if (this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				this.stateSchuelerKursEntfernenOhneRevalidierung(z.idSchueler, z.idKurs);
		for (const z of update.listHinzuzufuegen)
			if (!this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs))
				this.stateSchuelerKursHinzufuegenOhneRevalidierung(z.idSchueler, z.idKurs);
		if (this._parent.getIstBlockungsVorlage())
			this._parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);
		this.stateRevalidateEverything();
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse in Schienen zu setzen.
	 * <br>(1) Wenn der Kurs nicht in der Schiene ist, wird er hinzugefügt.
	 *
	 * @param kursSchienenZuordnungen  Alle Kurs-Schienen-Paare, welche gesetzt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse in Schienen zu setzen.
	 */
	public kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen : JavaSet<GostBlockungsergebnisKursSchienenZuordnung>) : GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		for (const z of kursSchienenZuordnungen) {
			if (!this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				u.listHinzuzufuegen.add(z);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 * <br>(1) Wenn der Kurs nicht in der Schiene ist, wird er hinzugefügt.
	 *
	 * @param kursSchienenZuordnungen  Alle Kurs-Schienen-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 */
	public kursSchienenUpdate_01b_ENTFERNE_KURS_SCHIENEN_PAARE(kursSchienenZuordnungen : JavaSet<GostBlockungsergebnisKursSchienenZuordnung>) : GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		for (const z of kursSchienenZuordnungen) {
			if (this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				u.listEntfernen.add(z);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu uverschieben.
	 *
	 * <br>(1) Wenn der Kurs nicht in der Quell-Schiene ist, passiert nichts.
	 * <br>(2) Wenn der Kurs bereits in der Ziel-Schiene ist, passiert nichts.
	 * <br>(3) Andernfalls, wird der Kurs aus der Quell-Schiene entfernt und der Ziel-Schiene hinzugefügt.
	 *
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idSchieneQuelle  Die Quell-Schiene, aus der der Kurs entfernt wird.
	 * @param idSchieneZiel    Die Ziel-Schiene, zu welcher der Kurs hinzugefügt wird.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu uverschieben.
	 */
	public kursSchienenUpdate_02a_VERSCHIEBE_KURS_VON_SCHIENE_NACH_SCHIENE(idKurs : number, idSchieneQuelle : number, idSchieneZiel : number) : GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u : GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		if (!this.getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneQuelle))
			return u;
		if (this.getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneZiel))
			return u;
		u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchieneQuelle));
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(idKurs, idSchieneZiel));
		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungsergebnisKursSchienenZuordnungUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungsergebnisKursSchienenZuordnungUpdate#listHinzuzufuegen} hinzu.
	 * Macht das selbe für die potentiell enthaltenen RegelUpdates.
	 *
	 * @param update  Das {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt.
	 */
	public kursSchienenUpdateExecute(update : GostBlockungsergebnisKursSchienenZuordnungUpdate) : void {
		if (this._parent.getIstBlockungsVorlage())
			this._parent.regelRemoveListe(update.regelUpdates.listEntfernen);
		for (const z of update.listEntfernen)
			if (this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				this.stateKursSchieneEntfernenOhneRegelvalidierung(z.idKurs, z.idSchiene);
		for (const z of update.listHinzuzufuegen)
			if (!this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene))
				this.stateKursSchieneHinzufuegenOhneRegelvalidierung(z.idKurs, z.idSchiene);
		if (this._parent.getIstBlockungsVorlage())
			this._parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);
		this.stateRevalidateEverything();
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_schiene, idSchiene);
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schienenNR_to_schiene, nrSchiene);
	}

	/**
	 * Liefert die ID einer Schiene mit einer bestimmten Nummer.
	 *
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die ID einer Schiene mit einer bestimmten Nummer.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	public getOfSchieneID(nrSchiene : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._schienenNR_to_schiene, nrSchiene).id;
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
		return CollectionUtils.toFilteredHashSet(this._schienenID_to_schiene.values(), { test : (s: GostBlockungsergebnisSchiene) => this.getOfSchieneHatKollision(s.id) });
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_susAnzahl, idSchiene)!;
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public getOfSchieneAnzahl() : number {
		return this._schienenID_to_schiene.size();
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
		return DeveloperNotificationException.ifMapGetIsNull(this._schienenID_to_kollisionen, idSchiene)!;
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
		for (const schuelerID of this._schuelerID_to_kollisionen.keySet())
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
	 * Liefert einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return einen Tooltip für die Schiene, welche alle Kollisionen pro Kurs-Paarung darstellt.
	 */
	public getOfSchieneTooltipKurskollisionen(idSchiene : number) : string {
		const sbZeilen : StringBuilder | null = new StringBuilder();
		for (const kurs1 of this.getSchieneE(idSchiene).kurse) {
			let summe : number = 0;
			const sbZeile : StringBuilder | null = new StringBuilder();
			for (const kurs2 of this.getSchieneE(idSchiene).kurse) {
				if (kurs2.id !== kurs1.id) {
					const anzahl : number = GostBlockungsergebnisManager.getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						summe += anzahl;
						sbZeile.append((sbZeile.isEmpty() ? "" : ", ") + this.getOfKursName(kurs2.id)! + "(" + anzahl + ")");
					}
				}
			}
			if (summe > 0) {
				sbZeilen.append(this.getOfKursName(kurs1.id)! + "(" + summe + "): " + sbZeile.toString()! + "\n");
			}
		}
		return sbZeilen.isEmpty() ? "Keine Kollisionen in der Schiene" : sbZeilen.toString();
	}

	/**
	 * Liefert alle Kollisionen einer Schiene, als Liste von Liste von Kurs-Anzahl-Paaren.
	 * <br>Pro innerer Liste gilt: Das erste Paar ist der Kurs, welcher mit allen anderen verglichen wurde, zusammen mit der Kollisions-Summe.
	 * <br>Anschließend folgen alle anderen Kurse mit ihrer Kollisions-Anzahl, falls diese größer 0 ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return alle Kollisionen einer Schiene, als Liste von Liste von Kurs-Anzahl-Paaren.
	 */
	public getOfSchieneTooltipKurskollisionenAsData(idSchiene : number) : List<List<Pair<GostBlockungsergebnisKurs, number>>> {
		const listOfLists : List<List<Pair<GostBlockungsergebnisKurs, number>>> = new ArrayList();
		for (const kurs1 of this.getSchieneE(idSchiene).kurse) {
			let summe : number = 0;
			const listOfPairs : List<Pair<GostBlockungsergebnisKurs, number>> = new ArrayList();
			for (const kurs2 of this.getSchieneE(idSchiene).kurse) {
				if (kurs2.id !== kurs1.id) {
					const anzahl : number = GostBlockungsergebnisManager.getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						listOfPairs.add(new Pair<GostBlockungsergebnisKurs, number>(kurs2, anzahl));
						summe += anzahl;
					}
				}
			}
			if (summe > 0) {
				listOfPairs.add(0, new Pair<GostBlockungsergebnisKurs, number>(kurs1, summe));
				listOfLists.add(listOfPairs);
			}
		}
		return listOfLists;
	}

	private static getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1 : GostBlockungsergebnisKurs, kurs2 : GostBlockungsergebnisKurs) : number {
		const set : HashSet<number | null> = new HashSet();
		set.addAll(kurs1.schueler);
		set.retainAll(kurs2.schueler);
		return set.size();
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
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddSchieneByID(idSchiene : number) : void {
		DeveloperNotificationException.ifTrue("Die Schiene " + this._parent.toStringSchiene(idSchiene)! + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.schieneGetExistiert(idSchiene));
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
		DeveloperNotificationException.ifTrue("Die Schiene " + this._parent.toStringSchiene(idSchiene)! + " muss erst beim Datenmanager entfernt werden!", this._parent.schieneGetExistiert(idSchiene));
		const nKurse : number = this.getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + this._parent.toStringSchiene(idSchiene)! + " hat noch " + nKurse + " Kurse!", nKurse > 0);
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
		DeveloperNotificationException.ifTrue("Die Regel " + this._parent.toStringRegel(idRegel)! + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.regelGetExistiert(idRegel));
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
			DeveloperNotificationException.ifTrue("Die Regel " + this._parent.toStringRegel(regel.id)! + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.regelGetExistiert(regel.id));
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
		DeveloperNotificationException.ifTrue("Die Regel " + this._parent.toStringRegel(idRegel)! + " muss erst beim Datenmanager entfernt werden!", this._parent.regelGetExistiert(idRegel));
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
			DeveloperNotificationException.ifTrue("Die Regel " + this._parent.toStringRegel(regel.id)! + " muss erst beim Datenmanager entfernt werden!", this._parent.regelGetExistiert(regel.id));
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
		DeveloperNotificationException.ifTrue("" + this._parent.toStringKurs(idKurs)! + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.kursGetExistiert(idKurs));
		const kurs : GostBlockungKurs = this._parent.kursGet(idKurs);
		const nSchienen : number = this._parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);
		this.stateRevalidateEverything();
		for (let nr : number = 1; nr <= kurs.anzahlSchienen; nr++) {
			const eSchiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
			this.stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, eSchiene.id);
		}
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht den übergebenen Kurs. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveKursByID(idKurs : number) : void {
		this.setRemoveKurseByID(ListUtils.create1(idKurs));
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param kurse  Die Liste der {@link GostBlockungsergebnisKurs}-Objekte.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveKurse(kurse : List<GostBlockungsergebnisKurs>) : void {
		const idKurse : List<number> = new ArrayList();
		for (const kursExtern of kurse)
			idKurse.add(kursExtern.id);
		this.setRemoveKurseByID(idKurse);
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param idKurse  Die Liste der Datenbank-IDs der Kurse.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveKurseByID(idKurse : List<number>) : void {
		for (const idKurs of idKurse)
			DeveloperNotificationException.ifTrue(this._parent.toStringKurs(idKurs)! + " muss erst beim Datenmanager entfernt werden!", this._parent.kursGetExistiert(idKurs));
		for (const idKurs of idKurse) {
			const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
			for (const schienenID of kurs.schienen) {
				const i : JavaIterator<GostBlockungsergebnisKurs> = this.getSchieneE(schienenID!).kurse.iterator();
				while (i.hasNext())
					if (i.next().id === kurs.id)
						i.remove();
			}
		}
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
		this._parent.kursMerge(idKursID1keep, idKursID2delete);
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
		this.stateRevalidateEverything();
		for (const eSchiene of this.getOfKursSchienenmenge(kurs1alt.id))
			this.stateKursSchieneHinzufuegenOhneRegelvalidierung(kurs2neu.id, eSchiene.id);
		for (const schuelerID of susVon1nach2) {
			this.stateSchuelerKursEntfernenOhneRevalidierung(schuelerID, kurs1alt.id);
			this.stateSchuelerKursHinzufuegenOhneRevalidierung(schuelerID, kurs2neu.id);
		}
		this.stateRevalidateEverything();
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
		DeveloperNotificationException.ifTrue("Schienenanzahl von KursE (" + kursE.anzahlSchienen + ") ist ungleich der von KursG (" + kursG.anzahlSchienen + ")!", kursE.anzahlSchienen !== kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Schienenanzahl von " + this._parent.toStringKurs(idKurs)! + " darf nur bei der Blockungsvorlage verändert werden!", !this._parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue(this._parent.toStringKurs(idKurs)! + " hat als GostBlockungKurs " + kursG.anzahlSchienen + " Schienen, als GostBlockungsergebnisKurs hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen !== kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen === 0);
		DeveloperNotificationException.ifTrue(this._parent.toStringKurs(idKurs)! + " muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!", anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, somit kann " + this._parent.toStringKurs(idKurs)! + " nicht " + anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			let hinzugefuegt : boolean = false;
			for (let nr : number = 1; (nr <= this._schienenNR_to_schiene.size()) && (!hinzugefuegt); nr++) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					this.stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine freie Schiene für " + this._parent.toStringKurs(idKurs)! + " gefunden!", !hinzugefuegt);
		}
		while (anzahlSchienenNeu < kursG.anzahlSchienen) {
			let entfernt : boolean = false;
			for (let nr : number = this._schienenNR_to_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					this.stateKursSchieneEntfernenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine belegte Schiene von " + this._parent.toStringKurs(idKurs)! + " gefunden!", !entfernt);
		}
		this.stateRevalidateEverything();
	}

	/**
	 * Informiert den Manager, dass sich bei mindestens einem Kurs die Lehrkraft geändert hat.
	 * Führt zu einer Revalidierung der Bewertung des Ergebnisses.
	 */
	public patchOfKursLehrkaefteChanged() : void {
		this.stateRevalidateEverything();
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
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public debug(logger : Logger) : void {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (const fachartID of this._fachartID_to_kurseList.keySet()) {
			logger.logLn("FachartID = " + fachartID! + " (KD = " + this.getOfFachartKursdifferenz(fachartID!) + ")");
			for (const kurs of this.getOfFachartKursmenge(fachartID!)) {
				logger.logLn("    " + this.getOfKursName(kurs.id)! + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + this._ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(this._ergebnis.bewertung.kursdifferenzHistogramm)!);
		logger.modifyIndent(-4);
	}

	/**
	 * Liefert einen String, der alle Schienen-Fachart-Kurs-Zuordnungen zeigt.
	 *
	 * @return einen String, der alle Schienen-Fachart-Kurs-Zuordnungen zeigt.
	 */
	public debugKursSchienenZuordnungen() : string {
		const sb : StringBuilder = new StringBuilder();
		sb.append("\n\nSchienen-Fachart-Kurs-Zuordnungen");
		for (const idSchiene of this._schienenIDset) {
			sb.append("Schiene " + this._parent.toStringSchieneSimple(idSchiene)! + "\n");
			for (const idFachart of this._schienenID_fachartID_to_kurseList.getKeySetOf(idSchiene)) {
				if (!Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, idSchiene, idFachart).isEmpty()) {
					sb.append("    Fachart " + this._parent.toStringFachartSimpleByFachartID(idFachart)! + "\n");
					for (const eKurs of Map2DUtils.getOrCreateArrayList(this._schienenID_fachartID_to_kurseList, idSchiene, idFachart)) {
						sb.append("        Kurs " + this._parent.toStringKursSimple(eKurs.id)! + "\n");
					}
				}
			}
		}
		return sb.toString();
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

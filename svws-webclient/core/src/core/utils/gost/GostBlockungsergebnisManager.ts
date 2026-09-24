import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { JavaMath } from '../../../java/lang/JavaMath';
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
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { MapUtils } from '../../../core/utils/MapUtils';
import { GostKursblockungRegelParameterTyp } from '../../../core/types/kursblockung/GostKursblockungRegelParameterTyp';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from '../../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate';
import { Schueler } from '../../../asd/data/schueler/Schueler';
import { PairNN } from '../../../asd/adt/PairNN';
import { Class } from '../../../java/lang/Class';
import { DTOUtils } from '../../../core/utils/DTOUtils';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import type { JavaSet } from '../../../java/util/JavaSet';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { Logger } from '../../../core/logger/Logger';
import { SchuelerStatus } from '../../../asd/types/schueler/SchuelerStatus';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnungUpdate } from '../../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnungUpdate';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { Pair } from '../../../asd/adt/Pair';
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
	 * Zeilenumbruch.
	 */
	private readonly lineSeparator: string = "\n";

	/**
	 * Der Blockungsdaten-Manager ist das Elternteil dieses Objektes.
	 */
	private readonly parent: GostBlockungsdatenManager;

	/**
	 * Das Blockungsergebnis ist das zugehörige Eltern-Datenobjekt.
	 */
	private ergebnis: GostBlockungsergebnis = new GostBlockungsergebnis();

	/**
	 * Liste aller Fehlermeldungen.
	 */
	private fehlermeldungen: List<string> = new ArrayList<string>();

	/**
	 * Set aller Schienen-IDs.
	 */
	private schienenIDs: HashSet<number> = new HashSet<number>();

	/**
	 * Set aller Schienen-Nummern.
	 */
	private schienenNRs: HashSet<number> = new HashSet<number>();

	/**
	 * Set aller Kurs-IDs.
	 */
	private kursIDs: HashSet<number> = new HashSet<number>();

	/**
	 * Set aller Fach-IDs.
	 */
	private fachIDs: HashSet<number> = new HashSet<number>();

	/**
	 * Set aller Schüler-IDs.
	 */
	private schuelerIDs: HashSet<number> = new HashSet<number>();

	/**
	 * Map von Schienen-ID nach {@link GostBlockungsergebnisSchiene}.
	 */
	private schieneByID: JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap<number, GostBlockungsergebnisSchiene>();

	/**
	 * Map von Schienen-NR nach {@link GostBlockungsergebnisSchiene}.
	 */
	private schieneByNR: JavaMap<number, GostBlockungsergebnisSchiene> = new HashMap<number, GostBlockungsergebnisSchiene>();

	/**
	 * Map von Kurs-ID nach {@link GostBlockungsergebnisKurs}.
	 */
	private kursByID: JavaMap<number, GostBlockungsergebnisKurs> = new HashMap<number, GostBlockungsergebnisKurs>();

	/**
	 * Map von Schueler-ID nach {@link Schueler}.
	 */
	private schuelerByID: JavaMap<number, Schueler> = new HashMap<number, Schueler>();

	/**
	 * Map von Schienen-ID nach Long-Set (von Kursen).
	 */
	private kursIDsBySchienenID: JavaMap<number, JavaSet<number>> = new HashMap<number, JavaSet<number>>();

	/**
	 * Map von Schueler-ID nach {@link GostBlockungsergebnisKurs}-Set (Kurse des Schüler, die aufgrund der aktuellen Fachwahlen ungültig sind).
	 */
	private kursmengeUngueltigBySchuelerID: JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap<number, JavaSet<GostBlockungsergebnisKurs>>();

	/**
	 * Map von Kurs-ID nach Long-Set (von Schülern).
	 */
	private schuelerIDsByKursID: JavaMap<number, JavaSet<number>> = new HashMap<number, JavaSet<number>>();

	/**
	 * Map von Fach-ID nach {@link GostBlockungsergebnisKurs}-List.
	 */
	private kursmengeByFachID: JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap<number, List<GostBlockungsergebnisKurs>>();

	/**
	 * Map von Kurs-ID nach {@link GostBlockungsergebnisSchiene}-Set.
	 */
	private schienenmengeByKursID: JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> = new HashMap<number, JavaSet<GostBlockungsergebnisSchiene>>();

	/**
	 * Map von Kurs-ID nach Integer (Anzahl der Dummy Schülerinnen und Schüler).
	 */
	private schuelerAnzahlDummyByKursID: JavaMap<number, number> = new HashMap<number, number>();

	/**
	 * Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse der selben Fachart).
	 */
	private kursmengeByFachartID: JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap<number, List<GostBlockungsergebnisKurs>>();

	/**
	 * Map von Schüler-ID nach {@link GostBlockungsergebnisKurs}-Set.
	 */
	private kursmengeBySchuelerID: JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap<number, JavaSet<GostBlockungsergebnisKurs>>();

	/**
	 * Menge aller Fachart-IDs sortiert nach der aktuellen Sortiervariante.
	 */
	private fachartIDsSortiert: List<number> = new ArrayList<number>();

	/**
	 * Map von Fachart-ID nach Integer (Kursdifferenz der Fachart).
	 */
	private kursdifferenzByFachartID: JavaMap<number, number> = new HashMap<number, number>();

	/**
	 * Map von Schienen-ID nach Integer (Anzahl an Kollisionen in der Schiene).
	 */
	private kollisionenBySchienenID: JavaMap<number, number> = new HashMap<number, number>();

	/**
	 * Map von Schienen-ID nach Integer (Anzahl der SuS in der Schiene).
	 */
	private schuelerAnzahlBySchienenID: JavaMap<number, number> = new HashMap<number, number>();

	/**
	 * Map von Schüler-ID nach Map von Schienen-ID nach {@link GostBlockungsergebnisKurs}-Set (Alle Kurse des Schülers in der Schiene).
	 */
	private kursmengeBySchuelerIDAndSchienenID: HashMap2D<number, number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap2D<number, number, JavaSet<GostBlockungsergebnisKurs>>();

	/**
	 * Map von Schienen-ID nach Map von Fachart-ID nach {@link GostBlockungsergebnisKurs}-List (Alle Kurse pro Schiene und Fachart).
	 */
	private kursmengeBySchienenIDAndFachartID: HashMap2D<number, number, List<GostBlockungsergebnisKurs>> = new HashMap2D<number, number, List<GostBlockungsergebnisKurs>>();

	/**
	 * Map von Kursdifferenz nach String-List (Facharten mit dieser Kursdifferenzen).
	 */
	private fachartBeschreibungsmengeByKursdifferenz: JavaMap<number, List<string>> = new HashMap<number, List<string>>();

	/**
	 * Map von Schüler-ID Integer (Summe aller Kollisionen des Schülers).
	 */
	private kollisionenBySchuelerID: JavaMap<number, number> = new HashMap<number, number>();

	/**
	 * Map von (Schüler-ID, Fach-ID) nach {@link GostBlockungsergebnisKurs} (Die zugeordnete Wahl des Schülers in dem Fach, auch NULL möglich).
	 */
	private kursOrNullBySchuelerIDAndFachID: HashMap2D<number, number, GostBlockungsergebnisKurs | null> = new HashMap2D<number, number, GostBlockungsergebnisKurs | null>();

	/**
	 * Von Regel-ID Regel-TYP nach List (alle Regelverletzungen des Typs als String-Menge).
	 */
	private regelverletzungsmengeByRegelTyp: JavaMap<number, List<string>> = new HashMap<number, List<string>>();

	/**
	 * Von Regel-ID nach String (Beschreibung der Regelverletzung).
	 */
	private regelverletzungsBeschreibungByRegelID: JavaMap<number, string> = new HashMap<number, string>();

	/**
	 * Textuelle Darstellung aller Regelverletzungen der definierten Regeln.
	 */
	private regelverletzungenTooltipRegeln: string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Wahlkonflikte.
	 */
	private regelverletzungenTooltipWahlkonflikte: string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Kursdifferenzen.
	 */
	private regelverletzungenTooltipKursdifferenzen: string = "";

	/**
	 * Textuelle Darstellung aller Regelverletzungen der Fächerparallelität.
	 */
	private regelverletzungenTooltipFaecherparallelitaet: string = "";

	/**
	 * Entscheidet, welcher Comparator verwendet wird mit 1 = (KURSART, FACH) andernfalls (FACH, KURSART).
	 */
	private fachartmengeSortierArt: number = 1;

	/**
	 * Comparator für die Facharten nach (KURSART, FACH).
	 */
	private readonly comparatorFachartByKursartAndFach: Comparator<number>;

	/**
	 * Comparator für die Facharten nach (FACH, KURSART).
	 */
	private readonly comparatorFachartByFachAndKursart: Comparator<number>;

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly comparatorKursByKursartAndFachAndKursnummer: Comparator<GostBlockungsergebnisKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly comparatorKursByFachAndKursartAndKursnummer: Comparator<GostBlockungsergebnisKurs>;

	/**
	 * Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 */
	private bewertungKursdifferenzNurLK: number = 0;

	/**
	 * Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 */
	private bewertungKursdifferenzNurGK: number = 0;

	/**
	 * Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 */
	private bewertungKursdifferenzRest: number = 0;


	/**
	 * Erstellt einen leeren GostBlockungsergebnisManager in Bezug auf GostBlockungsdatenManager. Die ID des leeren
	 * Ergebnisses ist -1 und muss noch gesetzt werden.
	 *
	 * @param pParent                  Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der
	 *                                 Blockung)
	 * @param pGostBlockungsergebnisID Die ID des Blockungsergebnisses.
	 */
	public constructor(pParent: GostBlockungsdatenManager, pGostBlockungsergebnisID: number);

	/**
	 * Erstellt einen neuen Manager mit den Daten aus dem übergebenen Ergebnis.
	 *
	 * @param pParent   Das Eltern-Objekt. (Daten-Manager für die grundlegenden Definitionen der Blockung)
	 * @param pErgebnis Das Ergebnis, welches kopiert wird.
	 */
	public constructor(pParent: GostBlockungsdatenManager, pErgebnis: GostBlockungsergebnis);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0: GostBlockungsdatenManager, __param1: GostBlockungsergebnis | number) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager')))) && ((__param1 !== undefined) && typeof __param1 === "number")) {
			const pParent: GostBlockungsdatenManager = cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(__param0);
			const pGostBlockungsergebnisID: number = __param1 as number;
			this.parent = pParent;
			this.comparatorFachartByKursartAndFach = this.createComparatorFachartKursartFach();
			this.comparatorFachartByFachAndKursart = this.createComparatorFachartFachKursart();
			this.comparatorKursByFachAndKursartAndKursnummer = this.createComparatorKursFachKursartNummer();
			this.comparatorKursByKursartAndFachAndKursnummer = this.createComparatorKursKursartFachNummer();
			this.ergebnis = new GostBlockungsergebnis();
			this.ergebnis.id = pGostBlockungsergebnisID;
			this.ergebnis.blockungID = this.parent.getID();
			this.ergebnis.gostHalbjahr = this.parent.daten().gostHalbjahr;
			this.stateClear();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnis'))))) {
			const pParent: GostBlockungsdatenManager = cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(__param0);
			const pErgebnis: GostBlockungsergebnis = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(__param1);
			this.parent = pParent;
			this.comparatorFachartByKursartAndFach = this.createComparatorFachartKursartFach();
			this.comparatorFachartByFachAndKursart = this.createComparatorFachartFachKursart();
			this.comparatorKursByFachAndKursartAndKursnummer = this.createComparatorKursFachKursartNummer();
			this.comparatorKursByKursartAndFachAndKursnummer = this.createComparatorKursKursartFachNummer();
			this.ergebnis = pErgebnis;
			this.ergebnis.blockungID = this.parent.getID();
			this.ergebnis.gostHalbjahr = this.parent.daten().gostHalbjahr;
			this.stateClear();
		} else throw new Error('invalid method overload');
	}

	/**
	 * Baut alle Datenstrukturen neu auf.
	 */
	public stateRevalidateEverything(): void {
		this.stateClear();
	}

	private stateClear(): void {
		this.ergebnis.bewertung = new GostBlockungsergebnisBewertung();
		this.fehlermeldungen = new ArrayList();
		this.update0schienenIDsUndschienenNRs();
		this.update0kursIDs();
		this.update0fachIDs();
		this.update0schuelerIDs();
		this.update0schieneByIDUndschieneByNR();
		this.update0kursByID();
		this.update0schuelerByID();
		this.update0kursIDsBySchienenID();
		this.update1schuelerIDsByKursIDUndkursmengeUngueltigBySchuelerID();
		this.update1schuelerAnzahlDummyByKursID();
		this.update1kursmengeByFachID();
		this.update1schienenmengeByKursID();
		this.update1kursmengeByFachartID();
		this.update2kursmengeBySchuelerID();
		this.update2fachartIDsSortiert();
		this.update2kursdifferenzByFachartID();
		this.update2kollisionenBySchienenID();
		this.update2schuelerAnzahlBySchienenID();
		this.update2kursmengeBySchuelerIDAndSchienenID();
		this.update2kursmengeBySchienenIDAndFachartID();
		this.update3fachartBeschreibungsmengeByKursdifferenz();
		this.update3kollisionenBySchuelerID();
		this.update3kursOrNullBySchuelerIDAndFachID();
		for (const schiene of this.ergebnis.schienen) {
			const kursmenge: List<GostBlockungsergebnisKurs> = schiene.kurse;
			if (this.fachartmengeSortierArt === 1) {
				kursmenge.sort(this.comparatorKursByKursartAndFachAndKursnummer);
			} else {
				kursmenge.sort(this.comparatorKursByFachAndKursartAndKursnummer);
			}
		}
		this.stateClearErgebnisTooltipRegelverletzungenBewertung();
		this.stateClearErgebnisTooltipWahlkonflikteBewertung();
		this.stateClearErgebnisTooltipKursdifferenzenBewertung();
		this.stateClearErgebnisTooltipFaecherparallelitaetBewertung();
		this.parent.ergebnisUpdateBewertung(this.ergebnis);
	}

	private stateClearErgebnisTooltipRegelverletzungenBewertung(): void {
		this.regelverletzungsmengeByRegelTyp = new HashMap();
		this.regelverletzungsBeschreibungByRegelID = new HashMap();
		for (const r of this.parent.regelGetListe()) {
			const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			switch (typ) {
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					this.stateRegelvalidierung1(r)
					break;
				}
				case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE: {
					this.stateRegelvalidierung2(r)
					break;
				}
				case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
					this.stateRegelvalidierung3(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS: {
					this.stateRegelvalidierung4(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					this.stateRegelvalidierung5(r)
					break;
				}
				case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
					this.stateRegelvalidierung6(r)
					break;
				}
				case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS: {
					this.stateRegelvalidierung7(r)
					break;
				}
				case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
					this.stateRegelvalidierung8(r)
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
					this.stateRegelvalidierung10(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH: {
					this.stateRegelvalidierung11(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH: {
					this.stateRegelvalidierung12(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER: {
					this.stateRegelvalidierung13(r)
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER: {
					this.stateRegelvalidierung14(r)
					break;
				}
				case GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL: {
					this.stateRegelvalidierung15(r)
					break;
				}
				case GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE: {
					this.stateRegelvalidierung18(r)
					break;
				}
				default: {
					// empty block
					break;
				}
			}
		}
		this.ergebnis.bewertung.anzahlKurseNichtZugeordnet = 0;
		for (const idKurs of this.schienenmengeByKursID.keySet()) {
			const sizeSoll: number = DeveloperNotificationException.ifMapGetIsNull(this.kursByID, idKurs).anzahlSchienen;
			const sizeIst: number = DeveloperNotificationException.ifMapGetIsNull(this.schienenmengeByKursID, idKurs).size();
			this.ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(sizeSoll - sizeIst);
		}
		this.regelverletzungenTooltipRegeln = this.stateClearErgebnisTooltipRegelverletzungenString();
	}

	private stateClearErgebnisTooltipRegelverletzungenString(): string {
		const sb: StringBuilder = new StringBuilder();
		let konflikte: number = 0;
		let konflikteIgnoriert: number = 0;
		for (const idRegeltyp of GostKursblockungRegelTyp.ANZEIGE_REIHENFOLGE) {
			for (const fehlermeldung of MapUtils.getOrCreateArrayList(this.regelverletzungsmengeByRegelTyp, idRegeltyp)) {
				if (konflikte < 10) {
					sb.append(fehlermeldung);
					sb.append(this.lineSeparator);
				} else {
					konflikteIgnoriert++;
				}
				konflikte++;
			}
		}
		if (konflikte === 0) {
			return "";
		}
		if (konflikteIgnoriert !== 0) {
			sb.append("+" + konflikteIgnoriert + " weitere Konflikte.");
		}
		return konflikte + " Regelverletzungen" + this.lineSeparator + sb.toString();
	}

	private stateClearErgebnisTooltipWahlkonflikteBewertung(): void {
		this.ergebnis.bewertung.anzahlSchuelerNichtZugeordnet = 0;
		for (const idSchueler of this.kursOrNullBySchuelerIDAndFachID.getKeySet()) {
			for (const idFach of this.kursOrNullBySchuelerIDAndFachID.getKeySetOf(idSchueler)) {
				if (this.kursOrNullBySchuelerIDAndFachID.getOrNull(idSchueler, idFach) === null) {
					this.ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
				}
			}
		}
		for (const regel of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN)) {
			const idSchueler: number = regel.parameter.get(0).valueOf();
			for (const gFachwahl of this.parent.schuelerGetListeOfFachwahlen(idSchueler)) {
				if (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, gFachwahl.fachID) === null) {
					this.ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
				}
			}
		}
		this.ergebnis.bewertung.anzahlSchuelerKollisionen = 0;
		for (const idSchueler of this.kollisionenBySchuelerID.keySet()) {
			const kollisionen: number = DeveloperNotificationException.ifMapGetIsNull(this.kollisionenBySchuelerID, idSchueler).valueOf();
			this.ergebnis.bewertung.anzahlSchuelerKollisionen += kollisionen;
		}
		this.regelverletzungenTooltipWahlkonflikte = this.stateClearErgebnisTooltipWahlkonflikteString();
	}

	private appendNichtwahlenLinien(sb: StringBuilder, wahlkonflikte: Array<number>, wahlkonflikteIgnoriert: Array<number>): void {
		for (const idSchueler of this.kursOrNullBySchuelerIDAndFachID.getKeySet()) {
			const entries = this.kursOrNullBySchuelerIDAndFachID.getSubMapOrException(idSchueler).entrySet();
			for (const e of entries) {
				if (e.getValue() !== null) {
					continue;
				}
				if (wahlkonflikte[0] < 10) {
					const idFach: number = e.getKey().valueOf();
					const kursart: number = this.parent.schuelerGetOfFachFachwahl(idSchueler, idFach).kursartID;
					sb.append(JavaString.format("%s ist im Fach %s keinem Kurs zugeordnet.", this.parent.toStringSchuelerSimple(idSchueler), this.parent.toStringFachartSimple(idFach, kursart)));
					sb.append(this.lineSeparator);
				} else {
					wahlkonflikteIgnoriert[0]++;
				}
				wahlkonflikte[0]++;
			}
		}
	}

	private appendKollisionenLinien(sb: StringBuilder, wahlkonflikte: Array<number>, wahlkonflikteIgnoriert: Array<number>): void {
		for (const idSchueler of this.kursmengeBySchuelerIDAndSchienenID.getKeySet()) {
			for (const e of this.kursmengeBySchuelerIDAndSchienenID.getSubMapOrException(idSchueler).entrySet()) {
				if (e.getValue().size() >= 2) {
					const list: ArrayList<GostBlockungsergebnisKurs> = new ArrayList<GostBlockungsergebnisKurs>(e.getValue());
					if (wahlkonflikte[0] < 10) {
						this.appendKollisionVonSchuelerInSchiene(sb, idSchueler, e.getKey(), list);
					} else {
						wahlkonflikteIgnoriert[0]++;
					}
					wahlkonflikte[0] += list.size() - 1;
				}
			}
		}
	}

	private appendKollisionVonSchuelerInSchiene(sb: StringBuilder, idSchueler: number, idSchiene: number, list: ArrayList<GostBlockungsergebnisKurs>): void {
		sb.append(JavaString.format("%s ist in %s in mehreren Kursen:", this.parent.toStringSchuelerSimple(idSchueler), this.parent.toStringSchieneSimple(idSchiene)));
		for (let i: number = 0; i < list.size(); i++) {
			sb.append(JavaString.format("%s%s", i === 0 ? "" : ", ", this.parent.toStringKursSimple(list.get(i).id)));
		}
		sb.append(this.lineSeparator);
	}

	private stateClearErgebnisTooltipWahlkonflikteString(): string {
		const sb: StringBuilder = new StringBuilder();
		const wahlkonflikte: Array<number> = [0];
		const wahlkonflikteIgnoriert: Array<number> = [0];
		this.appendNichtwahlenLinien(sb, wahlkonflikte, wahlkonflikteIgnoriert);
		this.appendKollisionenLinien(sb, wahlkonflikte, wahlkonflikteIgnoriert);
		if (wahlkonflikteIgnoriert[0] !== 0) {
			sb.append("+" + wahlkonflikteIgnoriert[0] + " weitere Konflikte.");
		}
		return "Wahlkonflikte = " + wahlkonflikte[0] + this.lineSeparator + sb.toString();
	}

	private stateClearErgebnisTooltipKursdifferenzenBewertung(): void {
		this.ergebnis.bewertung.kursdifferenzMax = 0;
		this.ergebnis.bewertung.kursdifferenzHistogramm = Array(this.parent.schuelerGetAnzahl() + GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX + 1).fill(0);
		this.bewertungKursdifferenzNurLK = 0;
		this.bewertungKursdifferenzNurGK = 0;
		this.bewertungKursdifferenzRest = 0;
		for (const idFachart of this.kursdifferenzByFachartID.keySet()) {
			const newKD: number = DeveloperNotificationException.ifMapGetIsNull(this.kursdifferenzByFachartID, idFachart).valueOf();
			this.ergebnis.bewertung.kursdifferenzHistogramm[newKD]++;
			this.ergebnis.bewertung.kursdifferenzMax = Math.max(this.ergebnis.bewertung.kursdifferenzMax, newKD);
			const kursart: number = GostKursart.getKursartID(idFachart);
			if (kursart === GostKursart.LK.id) {
				this.bewertungKursdifferenzNurLK = Math.max(this.bewertungKursdifferenzNurLK, newKD);
			} else {
				if (kursart === GostKursart.GK.id) {
					this.bewertungKursdifferenzNurGK = Math.max(this.bewertungKursdifferenzNurGK, newKD);
				} else {
					this.bewertungKursdifferenzRest = Math.max(this.bewertungKursdifferenzRest, newKD);
				}
			}
		}
		this.regelverletzungenTooltipKursdifferenzen = this.stateClearErgebnisTooltipKursdifferenzenString();
	}

	private stateClearErgebnisTooltipKursdifferenzenString(): string {
		const sb: StringBuilder = new StringBuilder();
		const histo: Array<number> = this.ergebnis.bewertung.kursdifferenzHistogramm;
		sb.append("Maximale Kursdifferenz (LK, GK, REST): " + this.bewertungKursdifferenzNurLK + ", " + this.bewertungKursdifferenzNurGK + ", " + this.bewertungKursdifferenzRest);
		sb.append(this.lineSeparator);
		if (histo.length >= 2) {
			sb.append("Optimal 0/1: " + (histo[0] + histo[1]) + "x");
			sb.append(this.lineSeparator);
		}
		for (let i: number = 2; i < histo.length; i++) {
			if (histo[i] <= 0) {
				continue;
			}
			const listFacharten: List<string> = DeveloperNotificationException.ifMapGetIsNull(this.fachartBeschreibungsmengeByKursdifferenz, i);
			sb.append(JavaString.format("Differenz %d: %dx (%s", i, histo[i], listFacharten.get(0)));
			for (let j: number = 1; j < listFacharten.size(); j++) {
				sb.append(JavaString.format(", %s", listFacharten.get(j)));
			}
			sb.append(")");
			sb.append(this.lineSeparator);
		}
		return sb.toString();
	}

	private stateClearErgebnisTooltipFaecherparallelitaetBewertung(): void {
		this.ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene = 0;
		for (const idSchiene of this.schienenIDs) {
			for (const idFachart of this.kursmengeByFachartID.keySet()) {
				const gleicheKurseInSchiene: number = Map2DUtils.getOrCreateArrayList(this.kursmengeBySchienenIDAndFachartID, idSchiene, idFachart).size();
				if (gleicheKurseInSchiene >= 2) {
					this.ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += gleicheKurseInSchiene - 1;
				}
			}
		}
		this.regelverletzungenTooltipFaecherparallelitaet = this.stateClearErgebnisTooltipFaecherparallelitaetString();
	}

	private stateClearErgebnisTooltipFaecherparallelitaetString(): string {
		const sb: StringBuilder = new StringBuilder();
		for (let nr: number = 1; nr <= this.schieneByNR.size(); nr++) {
			const schiene: GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
			const proSchiene: string = this.stateClearErgebnisTooltipFaecherparallelitaetStringProSchiene(schiene.id);
			if (!JavaString.isEmpty(proSchiene)) {
				sb.append(JavaString.format("Schiene %d:", nr));
				sb.append(this.lineSeparator);
				sb.append(proSchiene);
			}
		}
		return sb.toString();
	}

	private stateClearErgebnisTooltipFaecherparallelitaetStringProSchiene(idSchiene: number): string {
		const sb: StringBuilder = new StringBuilder();
		for (const idFachart of this.fachartIDsSortiert) {
			const proFachart: string = this.stateClearErgebnisTooltipFaecherparallelitaetStringProSchieneUndFachart(idSchiene, idFachart);
			if (!JavaString.isEmpty(proFachart)) {
				sb.append(proFachart);
				sb.append(this.lineSeparator);
			}
		}
		return sb.toString();
	}

	private stateClearErgebnisTooltipFaecherparallelitaetStringProSchieneUndFachart(idSchiene: number, idFachart: number): string {
		const sb: StringBuilder = new StringBuilder();
		if (this.kursmengeBySchienenIDAndFachartID.contains(idSchiene, idFachart)) {
			const kursGruppe: List<GostBlockungsergebnisKurs> = this.kursmengeBySchienenIDAndFachartID.getOrException(idSchiene, idFachart);
			const n: number = kursGruppe.size();
			if (n >= 2) {
				sb.append("  " + this.getOfFachartName(idFachart) + " (+" + (n - 1) + "):");
				for (let i: number = 0; i < n; i++) {
					const kurs: GostBlockungsergebnisKurs = ListUtils.getNonNullElementAtOrException(kursGruppe, i);
					sb.append(JavaString.format("%s %s", i === 0 ? "" : ",", this.getOfKursName(kurs.id)));
				}
			}
		}
		return sb.toString();
	}

	private update0schienenIDsUndschienenNRs(): void {
		this.schienenIDs = new HashSet();
		this.schienenNRs = new HashSet();
		for (const gSchiene of this.parent.daten().schienen) {
			if (gSchiene.id < 0) {
				this.fehlermeldungen.add(JavaString.format("Die Schienen-ID %d ist ungültig!", gSchiene.id));
			}
			if (!this.schienenIDs.add(gSchiene.id)) {
				this.fehlermeldungen.add(JavaString.format("Die Schienen-ID %d ist doppelt!", gSchiene.id));
			}
			if (gSchiene.nummer <= 0) {
				this.fehlermeldungen.add(JavaString.format("Die Schienen-NR %d ist ungültig!", gSchiene.nummer));
			}
			if (!this.schienenNRs.add(gSchiene.nummer)) {
				this.fehlermeldungen.add(JavaString.format("Die Schienen-NR %d ist doppelt!", gSchiene.nummer));
			}
		}
		for (let schienenNr: number = 1; schienenNr <= this.schienenNRs.size(); schienenNr++) {
			if (!this.schienenNRs.contains(schienenNr)) {
				this.fehlermeldungen.add(JavaString.format("Es gibt %d Schienen, aber es fehlt die Schienen-Nr. %d!", this.schienenNRs.size(), schienenNr));
			}
		}
	}

	private update0kursIDs(): void {
		this.kursIDs = new HashSet();
		for (const gKurs of this.parent.daten().kurse) {
			if (gKurs.id < 0) {
				this.fehlermeldungen.add(JavaString.format("Die Kurs-ID %d ist ungültig!", gKurs.id));
			}
			if (!this.kursIDs.add(gKurs.id)) {
				this.fehlermeldungen.add(JavaString.format("Die Kurs-ID %d ist doppelt!", gKurs.id));
			}
		}
	}

	private update0fachIDs(): void {
		this.fachIDs = new HashSet();
		for (const gFach of this.parent.faecherManager().faecher()) {
			if (gFach.id < 0) {
				this.fehlermeldungen.add(JavaString.format("Die Fach-ID %d ist ungültig!", gFach.id));
			}
			if (!this.fachIDs.add(gFach.id)) {
				this.fehlermeldungen.add(JavaString.format("Die Fach-ID %d ist doppelt!", gFach.id));
			}
		}
		for (const gKurs of this.parent.daten().kurse) {
			if (this.fachIDs.add(gKurs.fach_id)) {
				this.fehlermeldungen.add(JavaString.format("Kurs %s hat ein undefiniertes Fach (im Fächer-Manager)!", this.parent.toStringKursSimple(gKurs.id)));
			}
		}
		for (const gFachwahl of this.parent.daten().fachwahlen) {
			if (this.fachIDs.add(gFachwahl.fachID)) {
				this.fehlermeldungen.add(JavaString.format("Fachwahl %s hat ein undefiniertes Fach (im Fächer-Manager)!", this.parent.toStringFachwahlSimple(gFachwahl)));
			}
		}
	}

	private update0schuelerIDs(): void {
		this.schuelerIDs = new HashSet();
		for (const schueler of this.parent.daten().schueler) {
			if (schueler.id < 0) {
				this.fehlermeldungen.add(JavaString.format("Die Schüler-ID %d ist ungültig!", schueler.id));
			}
			if (!this.schuelerIDs.add(schueler.id)) {
				this.fehlermeldungen.add(JavaString.format("Die Schüler-ID %d ist doppelt!", schueler.id));
			}
		}
	}

	/**
	 * Wichtig: Die Methode muss auf gelöschte und hinzugefügt Schienen reagieren
	 * und die eigene Datenstruktur anpassen.
	 */
	private update0schieneByIDUndschieneByNR(): void {
		this.schieneByID = new HashMap();
		this.schieneByNR = new HashMap();
		const listZuLoeschen: List<GostBlockungsergebnisSchiene> = new ArrayList<GostBlockungsergebnisSchiene>();
		for (const eSchiene of this.ergebnis.schienen) {
			if (!this.parent.schieneGetExistiert(eSchiene.id)) {
				listZuLoeschen.add(eSchiene);
				if (!eSchiene.kurse.isEmpty()) {
					this.fehlermeldungen.add("Schiene ID=" + eSchiene.id + " wird gelöscht, obwohl " + eSchiene.kurse.size() + " Kurse in der Schiene enthalten sind!");
				}
			}
		}
		this.ergebnis.schienen.removeAll(listZuLoeschen);
		for (const eSchiene of this.ergebnis.schienen) {
			this.schieneByID.put(eSchiene.id, eSchiene);
			const nr: number = this.parent.schieneGet(eSchiene.id).nummer;
			this.schieneByNR.put(nr, eSchiene);
		}
		for (const gSchiene of this.parent.daten().schienen) {
			if (!this.schieneByID.containsKey(gSchiene.id)) {
				const eSchiene: GostBlockungsergebnisSchiene = DTOUtils.newGostBlockungsergebnisSchiene(gSchiene.id);
				this.schieneByID.put(gSchiene.id, eSchiene);
				this.schieneByNR.put(gSchiene.nummer, eSchiene);
				this.ergebnis.schienen.add(eSchiene);
			}
		}
	}

	/**
	 * Um Bugs zu verhindern, muss die DTO-Datenstruktor hier korrigiert werden,
	 * denn Multi-Schienen-Kurse existieren in der jeweiligen Schiene als Kopie.
	 * Alle Kopien müssen durch das selbe Kurs-Objekt ersetzt werden.
	 */
	private update0kursByID(): void {
		this.kursByID = new HashMap();
		for (const eSchiene of this.ergebnis.schienen) {
			for (let i: number = 0; i < eSchiene.kurse.size(); i++) {
				const eKurs: GostBlockungsergebnisKurs = eSchiene.kurse.get(i);
				const eKursAlt: GostBlockungsergebnisKurs | null = this.kursByID.get(eKurs.id);
				if (eKursAlt !== null) {
					eSchiene.kurse.set(i, eKursAlt);
				} else {
					this.kursByID.put(eKurs.id, eKurs);
				}
			}
		}
		for (const gKurs of this.parent.daten().kurse) {
			if (!this.kursByID.containsKey(gKurs.id)) {
				const eKurs: GostBlockungsergebnisKurs = DTOUtils.newGostBlockungsergebnisKurs(gKurs.id, gKurs.fach_id, gKurs.kursart, gKurs.anzahlSchienen);
				this.kursByID.put(gKurs.id, eKurs);
			}
		}
	}

	private update0schuelerByID(): void {
		this.schuelerByID = new HashMap();
		for (const gSchueler of this.parent.daten().schueler) {
			this.schuelerByID.put(gSchueler.id, gSchueler);
		}
	}

	private update0kursIDsBySchienenID(): void {
		this.kursIDsBySchienenID = new HashMap();
		for (const eSchiene of this.ergebnis.schienen) {
			for (const eKurs of eSchiene.kurse) {
				MapUtils.getOrCreateHashSet(this.kursIDsBySchienenID, eSchiene.id).add(eKurs.id);
			}
		}
		for (const idSchiene of this.schienenIDs) {
			if (!this.kursIDsBySchienenID.containsKey(idSchiene)) {
				MapUtils.getOrCreateHashSet(this.kursIDsBySchienenID, idSchiene);
			}
		}
	}

	private update1schuelerIDsByKursIDUndkursmengeUngueltigBySchuelerID(): void {
		this.schuelerIDsByKursID = new HashMap();
		for (const eSchiene of this.ergebnis.schienen) {
			for (const eKurs of eSchiene.kurse) {
				MapUtils.getOrCreateHashSet(this.schuelerIDsByKursID, eKurs.id).addAll(eKurs.schueler);
			}
		}
		for (const idKurs of this.kursIDs) {
			if (!this.schuelerIDsByKursID.containsKey(idKurs)) {
				MapUtils.getOrCreateHashSet(this.schuelerIDsByKursID, idKurs);
			}
		}
		this.kursmengeUngueltigBySchuelerID = new HashMap();
		for (const idKurs of this.schuelerIDsByKursID.keySet()) {
			const eKurs: GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this.kursByID, idKurs);
			const schuelerIDset: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs);
			for (const idSchueler of new HashSet(schuelerIDset)) {
				if (!this.parent.schuelerGetHatFachart(idSchueler, eKurs.fachID, eKurs.kursart)) {
					MapUtils.getOrCreateHashSet(this.kursmengeUngueltigBySchuelerID, idSchueler).add(eKurs);
				}
			}
		}
	}

	private update1schuelerAnzahlDummyByKursID(): void {
		this.schuelerAnzahlDummyByKursID = new HashMap();
		for (const r of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			const idKurs: number = r.parameter.get(0).valueOf();
			const anzahl: number = r.parameter.get(1);
			if (!this.kursIDs.contains(idKurs)) {
				this.fehlermeldungen.add(JavaString.format("Kurs %s soll %d externe SuS haben, aber den Kurs gibt es nicht!", this.parent.toStringKursSimple(idKurs), anzahl));
			} else
				if ((anzahl < GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN) || (anzahl > GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX)) {
					this.fehlermeldungen.add(JavaString.format("Kurs %s mit %d externen SuS ist ungültig!", this.parent.toStringKursSimple(idKurs), anzahl));
				} else
					if (this.schuelerAnzahlDummyByKursID.containsKey(idKurs)) {
						this.fehlermeldungen.add(JavaString.format("Kurs %s mit %d externen SuS. Doppelte Regel gefunden!", this.parent.toStringKursSimple(idKurs), anzahl));
					} else {
						this.schuelerAnzahlDummyByKursID.put(idKurs, anzahl);
					}
		}
		for (const idKurs of this.kursIDs) {
			MapUtils.putNonNullIfNotExists(this.schuelerAnzahlDummyByKursID, idKurs, 0);
		}
	}

	private update1kursmengeByFachID(): void {
		this.kursmengeByFachID = new HashMap();
		for (const eKurs of this.kursByID.values()) {
			MapUtils.getOrCreateArrayList(this.kursmengeByFachID, eKurs.fachID).add(eKurs);
		}
		for (const idFach of this.fachIDs) {
			MapUtils.getOrCreateArrayList(this.kursmengeByFachID, idFach);
		}
	}

	private update1schienenmengeByKursID(): void {
		this.schienenmengeByKursID = new HashMap();
		for (const idSchiene of this.kursIDsBySchienenID.keySet()) {
			const eSchiene: GostBlockungsergebnisSchiene = DeveloperNotificationException.ifMapGetIsNull(this.schieneByID, idSchiene);
			for (const idKurs of MapUtils.getOrCreateHashSet(this.kursIDsBySchienenID, idSchiene)) {
				MapUtils.getOrCreateHashSet(this.schienenmengeByKursID, idKurs).add(eSchiene);
			}
		}
		for (const idKurs of this.kursIDs) {
			if (!this.schienenmengeByKursID.containsKey(idKurs)) {
				MapUtils.getOrCreateHashSet(this.schienenmengeByKursID, idKurs);
			}
		}
	}

	private update1kursmengeByFachartID(): void {
		this.kursmengeByFachartID = new HashMap();
		for (const eKurs of this.kursByID.values()) {
			const fachartID: number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			MapUtils.getOrCreateArrayList(this.kursmengeByFachartID, fachartID).add(eKurs);
		}
		for (const gFachwahl of this.parent.daten().fachwahlen) {
			MapUtils.getOrCreateArrayList(this.kursmengeByFachartID, GostKursart.getFachartIDByFachwahl(gFachwahl));
		}
		for (const idFachart of this.kursmengeByFachartID.keySet()) {
			const kursmenge: List<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this.kursmengeByFachartID, idFachart);
			if (this.fachartmengeSortierArt === 1) {
				kursmenge.sort(this.comparatorKursByKursartAndFachAndKursnummer);
			} else {
				kursmenge.sort(this.comparatorKursByFachAndKursartAndKursnummer);
			}
		}
	}

	private update2kursmengeBySchuelerID(): void {
		this.kursmengeBySchuelerID = new HashMap();
		for (const idKurs of this.schuelerIDsByKursID.keySet()) {
			const eKurs: GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this.kursByID, idKurs);
			for (const idSchueler of DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs)) {
				MapUtils.getOrCreateHashSet(this.kursmengeBySchuelerID, idSchueler).add(eKurs);
			}
		}
		for (const idSchueler of this.schuelerIDs) {
			if (!this.kursmengeBySchuelerID.containsKey(idSchueler)) {
				MapUtils.getOrCreateHashSet(this.kursmengeBySchuelerID, idSchueler);
			}
		}
	}

	private update2fachartIDsSortiert(): void {
		this.fachartIDsSortiert = new ArrayList(this.kursmengeByFachartID.keySet());
		if (this.fachartmengeSortierArt === 1) {
			this.fachartIDsSortiert.sort(this.comparatorFachartByKursartAndFach);
		} else {
			this.fachartIDsSortiert.sort(this.comparatorFachartByFachAndKursart);
		}
	}

	private update2kursdifferenzByFachartID(): void {
		this.kursdifferenzByFachartID = new HashMap();
		for (const idFachart of this.kursmengeByFachartID.keySet()) {
			const kursmenge: List<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this.kursmengeByFachartID, idFachart);
			let min: number = 10000;
			let max: number = 0;
			for (const kurs of kursmenge) {
				const keyIgnoreID: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, kurs.id]);
				if (this.parent.regelGetByLongArrayKeyOrNull(keyIgnoreID) !== null) {
					continue;
				}
				const size: number = DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, kurs.id).size() + DeveloperNotificationException.ifMapGetIsNull(this.schuelerAnzahlDummyByKursID, kurs.id);
				min = Math.min(min, size);
				max = Math.max(max, size);
			}
			let newKD: number = max - min;
			if (newKD < 0) {
				newKD = 0;
			}
			this.kursdifferenzByFachartID.put(idFachart, newKD);
		}
	}

	private update2kollisionenBySchienenID(): void {
		this.kollisionenBySchienenID = new HashMap();
		for (const idSchiene of this.kursIDsBySchienenID.keySet()) {
			const kursmenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.kursIDsBySchienenID, idSchiene);
			let summeMitDoppelten: number = 0;
			const summeOhneDoppelte: JavaSet<number> = new HashSet<number>();
			for (const idKurs of kursmenge) {
				const schuelermenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs);
				summeMitDoppelten += schuelermenge.size();
				summeOhneDoppelte.addAll(schuelermenge);
			}
			this.kollisionenBySchienenID.put(idSchiene, summeMitDoppelten - summeOhneDoppelte.size());
		}
	}

	private update2schuelerAnzahlBySchienenID(): void {
		this.schuelerAnzahlBySchienenID = new HashMap();
		for (const idSchiene of this.kursIDsBySchienenID.keySet()) {
			const kursmenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.kursIDsBySchienenID, idSchiene);
			let summeMitDoppelten: number = 0;
			for (const idKurs of kursmenge) {
				const schuelermenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs);
				summeMitDoppelten += schuelermenge.size();
			}
			this.schuelerAnzahlBySchienenID.put(idSchiene, summeMitDoppelten);
		}
	}

	private update2kursmengeBySchuelerIDAndSchienenID(): void {
		this.kursmengeBySchuelerIDAndSchienenID = new HashMap2D();
		for (const idSchiene of this.kursIDsBySchienenID.keySet()) {
			const kursmenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.kursIDsBySchienenID, idSchiene);
			for (const idKurs of kursmenge) {
				const eKurs: GostBlockungsergebnisKurs = DeveloperNotificationException.ifMapGetIsNull(this.kursByID, idKurs);
				const schuelermenge: JavaSet<number> = DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs);
				for (const idSchueler of schuelermenge) {
					Map2DUtils.getOrCreateHashSet(this.kursmengeBySchuelerIDAndSchienenID, idSchueler, idSchiene).add(eKurs);
				}
			}
			for (const idSchueler of this.schuelerIDs) {
				Map2DUtils.getOrCreateHashSet(this.kursmengeBySchuelerIDAndSchienenID, idSchueler, idSchiene);
			}
		}
	}

	private update2kursmengeBySchienenIDAndFachartID(): void {
		this.kursmengeBySchienenIDAndFachartID = new HashMap2D();
		for (const eKurs of this.kursByID.values()) {
			const fachartID: number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			for (const eSchiene of MapUtils.getOrCreateHashSet(this.schienenmengeByKursID, eKurs.id)) {
				Map2DUtils.getOrCreateArrayList(this.kursmengeBySchienenIDAndFachartID, eSchiene.id, fachartID).add(eKurs);
			}
		}
		for (const idSchiene of this.schienenIDs) {
			for (const idFachart of this.kursmengeByFachartID.keySet()) {
				Map2DUtils.getOrCreateArrayList(this.kursmengeBySchienenIDAndFachartID, idSchiene, idFachart);
			}
		}
	}

	private update3fachartBeschreibungsmengeByKursdifferenz(): void {
		this.fachartBeschreibungsmengeByKursdifferenz = new HashMap();
		for (const idFachart of this.kursdifferenzByFachartID.keySet()) {
			const kursdifferenz: number = DeveloperNotificationException.ifMapGetIsNull(this.kursdifferenzByFachartID, idFachart).valueOf();
			const sFachart: string = this.parent.toStringFachartSimpleByFachartID(idFachart);
			MapUtils.getOrCreateArrayList(this.fachartBeschreibungsmengeByKursdifferenz, kursdifferenz).add(sFachart);
		}
	}

	private update3kollisionenBySchuelerID(): void {
		this.kollisionenBySchuelerID = new HashMap();
		for (const idSchueler of this.kursmengeBySchuelerIDAndSchienenID.getKeySet()) {
			let summeAllerKollisionenDesSchuelers: number = 0;
			for (const idSchiene of this.kursmengeBySchuelerIDAndSchienenID.getKeySetOf(idSchueler)) {
				const kurseInDerSchiene: number = this.kursmengeBySchuelerIDAndSchienenID.getOrException(idSchueler, idSchiene).size();
				if (kurseInDerSchiene >= 2) {
					summeAllerKollisionenDesSchuelers += kurseInDerSchiene - 1;
				}
			}
			this.kollisionenBySchuelerID.put(idSchueler, summeAllerKollisionenDesSchuelers);
		}
	}

	private update3kursOrNullBySchuelerIDAndFachID(): void {
		this.kursOrNullBySchuelerIDAndFachID = new HashMap2D();
		for (const idSchueler of this.kursmengeBySchuelerID.keySet()) {
			for (const eKurs of DeveloperNotificationException.ifMapGetIsNull(this.kursmengeBySchuelerID, idSchueler)) {
				this.kursOrNullBySchuelerIDAndFachID.put(idSchueler, eKurs.fachID, eKurs);
			}
		}
		for (const gFachwahl of this.parent.daten().fachwahlen) {
			if (!this.kursOrNullBySchuelerIDAndFachID.contains(gFachwahl.schuelerID, gFachwahl.fachID)) {
				this.kursOrNullBySchuelerIDAndFachID.put(gFachwahl.schuelerID, gFachwahl.fachID, null);
			}
		}
	}

	private stateRegelvalidierung1(r: GostBlockungRegel): void {
		for (let schienenNr: number = r.parameter.get(1); schienenNr <= r.parameter.get(2); schienenNr++) {
			for (const eKurs of this.getSchieneEmitNr(schienenNr).kurse) {
				if (eKurs.kursart === r.parameter.get(0)) {
					this.ergebnis.bewertung.regelVerletzungen.add(r.id);
					const beschreibung: string = "Kursart " + this.getOfKursName(eKurs.id) + " sollte nicht auf Schiene " + schienenNr + " liegen.";
					MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 1, beschreibung);
					this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
				}
			}
		}
	}

	private stateRegelvalidierung2(r: GostBlockungRegel): void {
		const idKurs: number = r.parameter.get(0).valueOf();
		const schienenNr: number = r.parameter.get(1);
		if (!this.getOfKursSchienenmenge(idKurs).contains(this.getSchieneEmitNr(schienenNr))) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("Kurs %s sollte fixiert sein in Schiene %d.", this.getOfKursName(idKurs), schienenNr);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 2, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung3(r: GostBlockungRegel): void {
		const idKurs: number = r.parameter.get(0).valueOf();
		const schienenNr: number = r.parameter.get(1);
		if (this.getOfKursSchienenmenge(idKurs).contains(this.getSchieneEmitNr(schienenNr))) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("Kurs %s sollte gesperrt sein in Schiene %d.", this.getOfKursName(idKurs), schienenNr);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 3, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung4(r: GostBlockungRegel): void {
		const idSchueler: number = r.parameter.get(0).valueOf();
		const idKurs: number = r.parameter.get(1).valueOf();
		if (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = this.getOfSchuelerNameVorname(idSchueler) + " sollte fixiert sein in Kurs " + this.getOfKursName(idKurs) + ".";
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 4, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung5(r: GostBlockungRegel): void {
		const idSchueler: number = r.parameter.get(0).valueOf();
		const idKurs: number = r.parameter.get(1).valueOf();
		if (this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = this.getOfSchuelerNameVorname(idSchueler) + " sollte verboten sein in Kurs " + this.getOfKursName(idKurs) + ".";
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 5, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung6(r: GostBlockungRegel): void {
		for (const eKurs of this.kursByID.values()) {
			for (const eSchieneID of eKurs.schienen) {
				const nr: number = this.getSchieneG(eSchieneID).nummer;
				const kursart: number = r.parameter.get(0);
				const schienenNrVon: number = r.parameter.get(1);
				const schienenNrBis: number = r.parameter.get(2);
				const b1: boolean = eKurs.kursart === kursart;
				const b2: boolean = (schienenNrVon <= nr) && (nr <= schienenNrBis);
				if (b1 !== b2) {
					this.ergebnis.bewertung.regelVerletzungen.add(r.id);
					const beschreibung: string = JavaString.format("Kursart von %s sollte innerhalb der Schienen %d bis %d sein.", this.getOfKursName(eKurs.id), schienenNrVon, schienenNrBis);
					MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 6, beschreibung);
					this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
				}
			}
		}
	}

	private stateRegelvalidierung7(r: GostBlockungRegel): void {
		const idKurs1: number = r.parameter.get(0).valueOf();
		const idKurs2: number = r.parameter.get(1).valueOf();
		for (const schiene1 of this.getOfKursSchienenmenge(idKurs1)) {
			for (const schiene2 of this.getOfKursSchienenmenge(idKurs2)) {
				if (schiene1 as unknown === schiene2 as unknown) {
					this.ergebnis.bewertung.regelVerletzungen.add(r.id);
					const nr: number = this.getSchieneG(schiene1.id).nummer;
					const beschreibung: string = JavaString.format("Kurs %s und Kurs %s sollten nicht gemeinsam in einer Schiene (%d) sein.", this.getOfKursName(idKurs1), this.getOfKursName(idKurs2), nr);
					MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 7, beschreibung);
					this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
				}
			}
		}
	}

	private stateRegelvalidierung8(r: GostBlockungRegel): void {
		const idKurs1: number = r.parameter.get(0).valueOf();
		const idKurs2: number = r.parameter.get(1).valueOf();
		const set1: JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs1);
		const set2: JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs2);
		if (set1.size() < set2.size()) {
			for (const schiene1 of set1) {
				if (!set2.contains(schiene1)) {
					this.ergebnis.bewertung.regelVerletzungen.add(r.id);
					const beschreibung: string = JavaString.format("Kurs %s und Kurs %s sollten gemeinsam in einer Schiene sein.", this.getOfKursName(idKurs1), this.getOfKursName(idKurs2));
					MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 8, beschreibung);
					this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
				}
			}
		} else {
			for (const schiene2 of set2) {
				if (!set1.contains(schiene2)) {
					this.ergebnis.bewertung.regelVerletzungen.add(r.id);
					const beschreibung: string = JavaString.format("Kurs %s und Kurs %s sollten gemeinsam in einer Schiene sein.", this.getOfKursName(idKurs1), this.getOfKursName(idKurs2));
					MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 8, beschreibung);
					this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
				}
			}
		}
	}

	private stateRegelvalidierung10(r: GostBlockungRegel): void {
		for (const eSchiene of this.schieneByID.values()) {
			const nr: number = this.getSchieneG(eSchiene.id).nummer;
			const lehrerZuKursen: JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap<number, List<GostBlockungsergebnisKurs>>();
			for (const eKurs of eSchiene.kurse) {
				for (const gLehr of this.getKursG(eKurs.id).lehrer) {
					const kursList: List<GostBlockungsergebnisKurs> = MapUtils.getOrCreateArrayList(lehrerZuKursen, gLehr.id);
					for (const konfliktKurs of kursList) {
						this.ergebnis.bewertung.regelVerletzungen.add(r.id);
						const beschreibung: string = JavaString.format("Kurs %s und Kurs %s haben die Lehrkraft %s in der selben Schiene (%d).", this.getOfKursName(eKurs.id), this.getOfKursName(konfliktKurs.id), gLehr.kuerzel, nr);
						MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 10, beschreibung);
						this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
					}
					kursList.add(eKurs);
				}
			}
		}
	}

	private stateRegelvalidierung11(r: GostBlockungRegel): void {
		const idSchueler1: number = r.parameter.get(0).valueOf();
		const idSchueler2: number = r.parameter.get(1).valueOf();
		const idFach: number = r.parameter.get(2).valueOf();
		const fach: GostFach = this.getFach(idFach);
		if (!this.parent.schuelerGetHatFach(idSchueler1, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s hat keine Fachwahl %s, aber eine Regel, die das Fach definiert.", this.getOfSchuelerNameVorname(idSchueler1), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 11, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (!this.parent.schuelerGetHatFach(idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s hat keine Fachwahl %s, aber eine Regel, die das Fach definiert.", this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 11, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (!this.parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s und %s haben nicht die selbe Kursart bei %s.", this.getOfSchuelerNameVorname(idSchueler1), this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 11, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (!this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s und %s sollten gemeinsam in %s sein.", this.getOfSchuelerNameVorname(idSchueler1), this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 11, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung12(r: GostBlockungRegel): void {
		const idSchueler1: number = r.parameter.get(0).valueOf();
		const idSchueler2: number = r.parameter.get(1).valueOf();
		const idFach: number = r.parameter.get(2).valueOf();
		const fach: GostFach = this.getFach(idFach);
		if (!this.parent.schuelerGetHatFach(idSchueler1, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s hat keine Fachwahl %s, hat aber eine Regel, die das Fach definiert.", this.getOfSchuelerNameVorname(idSchueler1), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 12, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (!this.parent.schuelerGetHatFach(idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s hat keine Fachwahl %s, hat aber eine Regel, die das Fach definiert.", this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 12, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (!this.parent.schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s und SchülerIn %s haben nicht die selbe Kursart bei %s.", this.getOfSchuelerNameVorname(idSchueler1), this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 12, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			return;
		}
		if (this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, idFach)) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("%s und SchülerIn %s sollten nicht gemeinsam in %s sein.", this.getOfSchuelerNameVorname(idSchueler1), this.getOfSchuelerNameVorname(idSchueler2), fach.kuerzelAnzeige);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 12, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung13(r: GostBlockungRegel): void {
		const idSchueler1: number = r.parameter.get(0).valueOf();
		const idSchueler2: number = r.parameter.get(1).valueOf();
		for (const fach of this.parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2)) {
			if (!this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				this.ergebnis.bewertung.regelVerletzungen.add(r.id);
				const beschreibung: string = this.getOfSchuelerNameVorname(idSchueler1) + " und " + this.getOfSchuelerNameVorname(idSchueler2) + " sollten gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 13, beschreibung);
				this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			}
		}
	}

	private stateRegelvalidierung14(r: GostBlockungRegel): void {
		const idSchueler1: number = r.parameter.get(0).valueOf();
		const idSchueler2: number = r.parameter.get(1).valueOf();
		for (const fach of this.parent.schuelerGetFachListeGemeinsamerFacharten(idSchueler1, idSchueler2)) {
			if (this.getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1, idSchueler2, fach.id)) {
				this.ergebnis.bewertung.regelVerletzungen.add(r.id);
				const beschreibung: string = this.getOfSchuelerNameVorname(idSchueler1) + " und " + this.getOfSchuelerNameVorname(idSchueler2) + " sollten nicht gemeinsam in " + fach.kuerzelAnzeige + " sein.";
				MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 14, beschreibung);
				this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
			}
		}
	}

	private stateRegelvalidierung15(r: GostBlockungRegel): void {
		const idKurs: number = r.parameter.get(0).valueOf();
		const maxSuS: number = r.parameter.get(1);
		DeveloperNotificationException.ifTrue("Regel 15: " + this.parent.toStringKurs(idKurs) + " maximale SuS-Anzahl = " + maxSuS + " ist ungültig!", (maxSuS < GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN) || (maxSuS > GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX));
		const sus: number = this.getOfKursAnzahlSchuelerPlusDummy(idKurs);
		if (sus > maxSuS) {
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = JavaString.format("Kurs %s hat %d SuS, sollte aber nicht mehr als %d haben.", this.getOfKursName(idKurs), sus, maxSuS);
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 15, beschreibung);
			this.regelverletzungsBeschreibungByRegelID.put(r.id, beschreibung);
		}
	}

	private stateRegelvalidierung18(r: GostBlockungRegel): void {
		const idFach: number = r.parameter.get(0).valueOf();
		const kursart: number = r.parameter.get(1);
		const maxProSchiene: number = r.parameter.get(2);
		const idFachart: number = GostKursart.getFachartID(idFach, kursart);
		for (const idSchiene of this.schienenIDs) {
			const size: number = Map2DUtils.getOrCreateArrayList(this.kursmengeBySchienenIDAndFachartID, idSchiene, idFachart).size();
			if (size <= maxProSchiene) {
				continue;
			}
			this.ergebnis.bewertung.regelVerletzungen.add(r.id);
			const beschreibung: string = "In " + this.parent.toStringSchieneSimple(idSchiene) + " ist die Fachart " + this.parent.toStringFachartSimpleByFachartID(idFachart) + " insgesamt " + size + " Mal vertreten, erlaubt sind aber nur " + maxProSchiene + "!";
			MapUtils.addToList(this.regelverletzungsmengeByRegelTyp, 18, beschreibung);
			const old: string = MapUtils.getOrDefault(this.regelverletzungsBeschreibungByRegelID, r.id, "");
			this.regelverletzungsBeschreibungByRegelID.put(r.id, (JavaString.isEmpty(old) ? "" : this.lineSeparator) + beschreibung);
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
	private stateSchuelerKursHinzufuegenOhneRevalidierung(idSchueler: number, idKurs: number): void {
		const eKurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
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
	private stateSchuelerKursEntfernenOhneRevalidierung(idSchueler: number, idKurs: number): void {
		const eKurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		eKurs.schueler.remove(idSchueler);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu und revalidiert nicht den Zustand.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs: number, idSchiene: number): void {
		const kurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene: GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		kurs.schienen.add(idSchiene);
		schiene.kurse.add(kurs);
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 */
	private stateKursSchieneEntfernenOhneRegelvalidierung(idKurs: number, idSchiene: number): void {
		const kurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const schiene: GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		schiene.kurse.remove(kurs);
		kurs.schienen.remove(idSchiene);
	}

	/**
	 * Liefert die Anzahl an externen SuS.
	 *
	 * @return die Anzahl an externen SuS.
	 */
	public getAnzahlSchuelerExterne(): number {
		return ListUtils.getCountFiltered(this.parent.daten().schueler, { test: (schueler: Schueler) => this.getOfSchuelerHatStatusExtern(schueler.id) });
	}

	private static dividiereUndSchneideNachZweiNachkommastellenAb(zaehler: number, nenner: number): number {
		const hochskaliert: number = Math.trunc((zaehler * 100) / nenner);
		return hochskaliert / 100.0;
	}

	/**
	 * Liefert die Kursfrequenz als String, berechnet als (Summe aller Fachwahlen) / (interne Kurse).
	 * <br>Hinweis: DummySuS werden ignoriert.
	 * <br>Hinweis: Es werden 2 Nachkommastellen maximal angezeigt.
	 *
	 * @return die Kursfrequenz als String, berechnet als (Summe aller Fachwahlen) / (interne Kurse).
	 */
	public getKursfrequenz1AsString(): string {
		const nKurse: number = this.parent.kursGetAnzahlIntener();
		if (nKurse === 0) {
			return "Kursfrequenz = ?";
		}
		const nFachwahlen: number = this.parent.fachwahlGetAnzahl();
		const avg1: number = GostBlockungsergebnisManager.dividiereUndSchneideNachZweiNachkommastellenAb(nFachwahlen, nKurse);
		return JavaString.replace(("" + avg1), '.', ',');
	}

	/**
	 * Liefert die Kursfrequenz als String, berechnet als (Summe aller auf interne Kurse verteilten SuS) / (interne Kurse).
	 * <br>Hinweis: DummySuS werden ignoriert.
	 * <br>Hinweis: Es werden 2 Nachkommastellen maximal angezeigt.
	 *
	 * @return die Kursfrequenz als String, berechnet als (Summe aller auf interne Kurse verteilten SuS) / (interne Kurse).
	 */
	public getKursfrequenz2AsString(): string {
		const nKurse: number = this.parent.kursGetAnzahlIntener();
		if (nKurse === 0) {
			return "Kursfrequenz = ?";
		}
		let nVerteilt: number = 0;
		for (const gKurs of this.parent.daten().kurse) {
			if (!gKurs.istKoopKurs) {
				nVerteilt += this.getOfKursAnzahlSchueler(gKurs.id);
			}
		}
		const avg2: number = GostBlockungsergebnisManager.dividiereUndSchneideNachZweiNachkommastellenAb(nVerteilt, nKurse);
		return JavaString.replace(("" + avg2), '.', ',');
	}

	/**
	 * Liefert die Anzahl an E-Schienen.
	 *
	 * @return die Anzahl an E-Schienen.
	 */
	public getAnzahlSchienen(): number {
		return this.ergebnis.schienen.size();
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS.
	 *
	 * @return die Anzahl an Dummy-SuS.
	 */
	public getAnzahlSchuelerDummy(): number {
		let summe: number = 0;
		for (const idKurs of this.schuelerAnzahlDummyByKursID.keySet()) {
			summe += this.getOfKursAnzahlSchuelerDummy(idKurs);
		}
		return summe;
	}

	/**
	 * Liefert die Datenbank-ID der Blockung. Das ist die ID des Elternteils.
	 *
	 * @return die Datenbank-ID der Blockung. Das ist die ID des Elternteils.
	 */
	public getBlockungsdatenID(): number {
		return this.ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis, potentiell auch mit Schüler-Kurs-Zuordnungen, die nicht zu den Fachwahlen passen.
	 *
	 * @return das Blockungsergebnis, potentiell auch mit Schüler-Kurs-Zuordnungen, die nicht zu den Fachwahlen passen.
	 */
	public getErgebnis(): GostBlockungsergebnis {
		return this.ergebnis;
	}

	/**
	 * Liefert die Menge (meistens eine) aller Fehlermeldungen.
	 * <br>Falls die Liste nicht leer ist, sollte die GUI den Benutzer warnen, dass die Blockung nicht vollständig geladen wurde!
	 *
	 * @return die Menge (meistens eine) aller Fehlermeldungen.
	 */
	public getFehlermeldungen(): List<string> {
		return this.fehlermeldungen;
	}

	/**
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 *
	 * @return den zugehörigen Daten-Manager für diesen Ergebnis-Manager.
	 */
	public getParent(): GostBlockungsdatenManager {
		return this.parent;
	}

	/**
	 * Liefert eine Güte eines Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 *
	 * @param value   der Wert des Bewertungskriteriums
	 *
	 * @return die Güte des Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	private static getOfBewertungFarbcodeStatic(value: number): number {
		return 1 - (1 / ((0.25 * value) + 1));
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
	public static getOfBewertung1WertStatic(bewertung: GostBlockungsergebnisBewertung): number {
		let summe: number = 0;
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
	public getOfBewertung1Wert(): number {
		return GostBlockungsergebnisManager.getOfBewertung1WertStatic(this.ergebnis.bewertung);
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
	private static getOfBewertung1FarbcodeStatic(bewertung: GostBlockungsergebnisBewertung): number {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung1WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung1Farbcode(): number {
		return GostBlockungsergebnisManager.getOfBewertung1FarbcodeStatic(this.ergebnis.bewertung);
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
	private static getOfBewertung2WertStatic(bewertung: GostBlockungsergebnisBewertung): number {
		let summe: number = 0;
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
	public getOfBewertung2Wert(): number {
		return GostBlockungsergebnisManager.getOfBewertung2WertStatic(this.ergebnis.bewertung);
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
	private static getOfBewertung2FarbcodeStatic(bewertung: GostBlockungsergebnisBewertung): number {
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic(GostBlockungsergebnisManager.getOfBewertung2WertStatic(bewertung));
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung2Farbcode(): number {
		return GostBlockungsergebnisManager.getOfBewertung2FarbcodeStatic(this.ergebnis.bewertung);
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz).
	 */
	public getOfBewertung3Wert(): number {
		return this.ergebnis.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3Farbcode(): number {
		const wert: number = this.ergebnis.bewertung.kursdifferenzMax;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert === 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart LK.
	 */
	public getOfBewertung3WertNurLk(): number {
		return this.bewertungKursdifferenzNurLK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur LK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3FarbcodeNurLk(): number {
		const wert: number = this.bewertungKursdifferenzNurLK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert === 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf die Kursart GK.
	 */
	public getOfBewertung3WertNurGk(): number {
		return this.bewertungKursdifferenzNurGK;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, nur GK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3FarbcodeNurGk(): number {
		const wert: number = this.bewertungKursdifferenzNurGK;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert === 0) ? 0 : (wert - 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 *
	 * @return den Wert des 3. Bewertungskriteriums (Kursdifferenz) nur bezogen auf Kursarten die nicht LK oder GK sind.
	 */
	public getOfBewertung3WertNurRest(): number {
		return this.bewertungKursdifferenzRest;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 *
	 * @return eine Güte des 3. Bewertungskriteriums (Kursdifferenz, alles außer LK und GK) im Bereich [0;1], mit 0=optimal.
	 */
	public getOfBewertung3FarbcodeNurRest(): number {
		const wert: number = this.bewertungKursdifferenzRest;
		return GostBlockungsergebnisManager.getOfBewertungFarbcodeStatic((wert === 0) ? 0 : (wert - 1));
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
	private static getOfBewertung4WertStatic(bewertung: GostBlockungsergebnisBewertung): number {
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
	public getOfBewertung4Wert(): number {
		return GostBlockungsergebnisManager.getOfBewertung4WertStatic(this.ergebnis.bewertung);
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
	private static getOfBewertung4FarbcodeStatic(bewertung: GostBlockungsergebnisBewertung): number {
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
	public getOfBewertung4Farbcode(): number {
		return GostBlockungsergebnisManager.getOfBewertung4FarbcodeStatic(this.ergebnis.bewertung);
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse.<br>
	 * Ein Multikurse der über mehrere Schienen geht und gar nicht zugeteilt wurde, wird mehrfach gezählt.
	 *
	 * @return die Anzahl nicht vollständig verteilter Kurse.
	 */
	public getOfBewertungAnzahlNichtZugeordneterKurse(): number {
		return this.ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 *
	 * @return die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public getOfBewertungAnzahlNichtzugeordneterFachwahlen(): number {
		return this.ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
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
	public getFach(idFach: number): GostFach {
		return this.parent.faecherManager().getOrException(idFach);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach-ID.<br>
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kurse mit dem angegebenen Fach-ID.
	 * @throws DeveloperNotificationException falls die Fach-ID unbekannt ist.
	 */
	public getOfFachKursmenge(idFach: number): List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursmengeByFachID, idFach);
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
	public getOfFachartKursmenge(idFachart: number): List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursmengeByFachartID, idFachart);
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
	public getOfFachartKursdifferenz(idFachart: number): number {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursdifferenzByFachartID, idFachart);
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
	public getOfFachOfKursartKursdifferenz(idFach: number, idKursart: number): number {
		const idFachart: number = GostKursart.getFachartID(idFach, idKursart);
		return DeveloperNotificationException.ifMapGetIsNull(this.kursdifferenzByFachartID, idFachart);
	}

	/**
	 * Liefert den Namen der Fachart, z. B. D-LK.
	 *
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return den Namen der Fachart, z. B. D-LK.
	 */
	private getOfFachartName(idFachart: number): string | null {
		const idFach: number = GostKursart.getFachID(idFachart);
		const idKursart: number = GostKursart.getKursartID(idFachart);
		return this.parent.faecherManager().getOrException(idFach).kuerzelAnzeige + "-" + GostKursart.fromID(idKursart).kuerzel;
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach LK/GK, dann nach der Fachsortierung, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungKursartFachNummer(): void {
		this.fachartmengeSortierArt = 1;
		this.stateRevalidateEverything();
	}

	/**
	 * Ändert die aktuelle Sortierung von Facharten und Kursen.
	 * <br>Hinweis: Sortiert zuerst nach der Fachsortierung, dann nach LK/GK, zuletzt nach der Kursnummer.
	 */
	public kursSetSortierungFachKursartNummer(): void {
		this.fachartmengeSortierArt = 2;
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
	private getSchuelerG(idSchueler: number): Schueler {
		return this.parent.schuelerGet(idSchueler);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public getOfSchuelerNameVorname(idSchueler: number): string {
		const schueler: Schueler = this.parent.schuelerGet(idSchueler);
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
	public getOfSchuelerKursmenge(idSchueler: number): JavaSet<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursmengeBySchuelerID, idSchueler);
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public getOfSchuelerKursmengeMitKollisionen(idSchueler: number): JavaSet<GostBlockungsergebnisKurs> {
		const set: JavaSet<GostBlockungsergebnisKurs> = new HashSet<GostBlockungsergebnisKurs>();
		for (const schiene of this.parent.schieneGetListe()) {
			const kurseDerSchiene: JavaSet<GostBlockungsergebnisKurs> = this.kursmengeBySchuelerIDAndSchienenID.getOrException(idSchueler, schiene.id);
			if (kurseDerSchiene.size() > 1) {
				set.addAll(kurseDerSchiene);
			}
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
	public getOfSchuelerFachwahlmengeOhneKurszuordnung(idSchueler: number): List<GostFachwahl> {
		const list: List<GostFachwahl> = this.parent.schuelerGetListeOfFachwahlen(idSchueler);
		const filter: Predicate<GostFachwahl> = { test: (t: GostFachwahl) => (this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, t.fachID) === null) };
		return ListUtils.getCopyFiltered(list, filter);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public getOfSchuelerHatNichtwahl(idSchueler: number): boolean {
		const nIst: number = DeveloperNotificationException.ifMapGetIsNull(this.kursmengeBySchuelerID, idSchueler).size();
		const nSoll: number = this.kursOrNullBySchuelerIDAndFachID.getSubMapSizeOrZero(idSchueler);
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
	public getOfSchuelerHatFachwahl(idSchueler: number, idFach: number, idKursart: number): boolean {
		return this.parent.schuelerGetHatFachart(idSchueler, idFach, idKursart);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach (unabhängig von der Kursart) gewählt hat.
	 */
	private getOfSchuelerHatFach(idSchueler: number, idFach: number): boolean {
		return this.parent.schuelerGetHatFach(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public getOfSchuelerHatKollision(idSchueler: number): boolean {
		return DeveloperNotificationException.ifMapGetIsNull(this.kollisionenBySchuelerID, idSchueler) > 0;
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
	public getOfSchuelerAnzahlGefiltert(idKurs: number, idFach: number, idKursart: number, konfliktTyp: number, subString: string, geschlecht: Geschlecht | null, schriftlichkeit: GostSchriftlichkeit | null): number {
		const menge: List<Schueler> = this.getOfSchuelerMengeBasisGefiltert(idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht);
		if (schriftlichkeit === null) {
			return menge.size();
		}
		let summe: number = 0;
		for (const schueler of menge) {
			if (this.getOfSchuelerErfuelltNachfilterKriterien(schueler.id, idKurs, idFach, schriftlichkeit)) {
				summe++;
			}
		}
		return summe;
	}

	/**
	 * Prüft das Nachfilter-Kriterium (Schriftlichkeit) für einen Schüler.
	 * <br>Hinweis: Sind Kurs-Filter und Fach-Filter gleichzeitig gesetzt, werden beide Schriftlichkeiten geprüft.
	 *
	 * @param idSchueler        die ID des zu prüfenden Schülers
	 * @param idKurs            die ID des Kurses (für Schriftlichkeits-Prüfung)
	 * @param idFach            die ID des Faches (für Schriftlichkeits-Prüfung)
	 * @param schriftlichkeit   die geforderte Schriftlichkeit oder null
	 *
	 * @return true, wenn der Schüler die Kriterien erfüllt
	 */
	private getOfSchuelerErfuelltNachfilterKriterien(idSchueler: number, idKurs: number, idFach: number, schriftlichkeit: GostSchriftlichkeit | null): boolean {
		if (schriftlichkeit === null) {
			return true;
		}
		const istSchriftlich: boolean = schriftlichkeit.getIstSchriftlichOrException();
		if (idKurs >= 0) {
			if (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
				return false;
			}
			const ungueltig: boolean = this.getOfSchuelerOfKursIstUngueltig(idSchueler, idKurs);
			if ((ungueltig) && (schriftlichkeit as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown)) {
				return false;
			}
			if (!ungueltig && (istSchriftlich !== this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs).istSchriftlich)) {
				return false;
			}
		}
		if (idFach < 0) {
			return true;
		}
		return (istSchriftlich === this.getOfSchuelerOfFachFachwahl(idSchueler, idFach).istSchriftlich);
	}

	/**
	 * Liefert die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der zugeordneten Kurse des Schülers in der Schiene.
	 */
	private getOfSchuelerOfSchieneKursmenge(idSchueler: number, idSchiene: number): JavaSet<GostBlockungsergebnisKurs> {
		return this.kursmengeBySchuelerIDAndSchienenID.getOrException(idSchueler, idSchiene);
	}

	/**
	 * Liefert TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idSchiene   Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Schüler in der Schiene mehr als einen Kurs belegt hat.
	 */
	public getOfSchuelerOfSchieneHatKollision(idSchueler: number, idSchiene: number): boolean {
		return this.kursmengeBySchuelerIDAndSchienenID.getOrException(idSchueler, idSchiene).size() > 1;
	}

	/**
	 * Liefert die zu (idSchueler, idFach) die jeweilige Kursart. <br>
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idFach     Die Datenbank-ID des Faches.
	 *
	 * @return Die zu (idSchueler, idFach) die jeweilige Kursart.
	 */
	public getOfSchuelerOfFachKursart(idSchueler: number, idFach: number): GostKursart {
		return this.parent.schuelerGetOfFachKursart(idSchueler, idFach);
	}

	/**
	 * Liefert den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idFach     Die Datenbank-ID des Faches.
	 *
	 * @return den zu (idSchueler, idFach) passenden Kurs oder NULL.
	 */
	public getOfSchuelerOfFachZugeordneterKurs(idSchueler: number, idFach: number): GostBlockungsergebnisKurs | null {
		return this.kursOrNullBySchuelerIDAndFachID.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 *
	 * @param idSchueler           Die ID des {@link Schueler}-Objekts.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	private getOfSchuelerNeuzuordnungMitFixierung(idSchueler: number, fixiereBelegteKurse: boolean): SchuelerblockungOutput {
		const input: SchuelerblockungInput = new SchuelerblockungInput();
		input.schienen = this.parent.schieneGetAnzahl();
		for (const fachwahl of this.parent.schuelerGetListeOfFachwahlen(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(this.parent.fachwahlGetName(fachwahl));
			const fachartID: number = GostKursart.getFachartIDByFachwahl(fachwahl);
			for (const kursE of this.getOfFachartKursmenge(fachartID)) {
				input.kurse.add(this.getOfSchuelerNeuzuordnungErzeugeKurs(kursE.id, kursE.fachID, kursE.kursart, idSchueler, fixiereBelegteKurse));
			}
		}
		if (input.kurse.isEmpty()) {
			return new SchuelerblockungOutput();
		}
		return new SchuelerblockungAlgorithmus().handle(input);
	}

	private getOfSchuelerNeuzuordnungErzeugeKurs(idKurs: number, idFach: number, kursart: number, idSchueler: number, fixiereBelegteKurse: boolean): SchuelerblockungInputKurs {
		const kursS: SchuelerblockungInputKurs = new SchuelerblockungInputKurs();
		kursS.id = idKurs;
		kursS.fach = idFach;
		kursS.kursart = kursart;
		kursS.schienen = this.getOfKursSchienenNummern(idKurs);
		kursS.istGesperrt = this.getOfSchuelerOfKursIstGesperrt(idSchueler, idKurs);
		kursS.istFixiert = this.getOfSchuelerOfKursIstFixiert(idSchueler, idKurs) || (fixiereBelegteKurse && this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs));
		kursS.anzahlSuS = this.getOfKursAnzahlSchuelerPlusDummy(idKurs);
		if (this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)) {
			kursS.anzahlSuS--;
		}
		const maxSuS: number = this.getOfKursMaxSuS(idKurs);
		if (kursS.anzahlSuS >= maxSuS) {
			kursS.istGesperrt = true;
		}
		if (kursS.istGesperrt && kursS.istFixiert) {
			kursS.istGesperrt = false;
		}
		kursS.anzahlZusammenMitWuensche = this.getOfSchuelerOfKursAnzahlZusammenWuensche(idSchueler, idKurs);
		kursS.anzahlVerbotenMitWuensche = this.getOfSchuelerOfKursAnzahlVerbotenWuensche(idSchueler, idKurs);
		return kursS;
	}

	/**
	 * Liefert ein {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, welches für den Schüler eine berechnete Neuzuordnung der Kurse beinhaltet.
	 *
	 * @param idSchueler           Die ID des {@link Schueler}-Objekts.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return ein {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse beinhaltet.
	 */
	public getOfSchuelerNeuzuordnung(idSchueler: number, fixiereBelegteKurse: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const zuordnung: SchuelerblockungOutput = this.getOfSchuelerNeuzuordnungMitFixierung(idSchueler, fixiereBelegteKurse);
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of zuordnung.fachwahlenZuKurs) {
			const kursV: GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			const kursN: GostBlockungsergebnisKurs | null = (z.kursID < 0) ? null : this.getKursE(z.kursID);
			if (kursV as unknown !== kursN as unknown) {
				if (kursV !== null) {
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursV.id, idSchueler));
				}
				if (kursN !== null) {
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursN.id, idSchueler));
				}
			}
		}
		return u;
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public getOfSchuelerOfKursIstZugeordnet(idSchueler: number, idKurs: number): boolean {
		return MapUtils.getOrCreateHashSet(this.schuelerIDsByKursID, idKurs).contains(idSchueler);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist, aber keine entsprechende Fachwahl hat.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler dem Kurs zugeordnet ist, aber keine entsprechende Fachwahl hat.
	 */
	public getOfSchuelerOfKursIstUngueltig(idSchueler: number, idKurs: number): boolean {
		if (this.kursmengeUngueltigBySchuelerID.containsKey(idSchueler)) {
			for (const kurs of MapUtils.getOrCreateHashSet(this.kursmengeUngueltigBySchuelerID, idSchueler)) {
				if (kurs.id === idKurs) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	private getOfSchuelerOfKursIstFixiert(idSchueler: number, idKurs: number): boolean {
		return this.parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler den Kurs als LK gewählt hat.
	 */
	private getOfSchuelerOfKursIstLK(idSchueler: number, idKurs: number): boolean {
		const abiturfach: number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
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
	private getOfSchuelerOfKursIstAB3(idSchueler: number, idKurs: number): boolean {
		const abiturfach: number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
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
	private getOfSchuelerOfKursIstLKoderAB3(idSchueler: number, idKurs: number): boolean {
		const abiturfach: number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
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
	private getOfSchuelerOfKursIstAB4(idSchueler: number, idKurs: number): boolean {
		const abiturfach: number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
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
	private getOfSchuelerOfKursIstAbiturfach(idSchueler: number, idKurs: number): boolean {
		const abiturfach: number = this.getOfSchuelerOfKursAbiturfach(idSchueler, idKurs);
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
	private getOfSchuelerOfKursAbiturfach(idSchueler: number, idKurs: number): number {
		const fachwahl: GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
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
	private getOfSchuelerOfKursIstSchriftlich(idSchueler: number, idKurs: number): boolean {
		const fachwahl: GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
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
	public getOfSchuelerOfKursIstGesperrt(idSchueler: number, idKurs: number): boolean {
		for (const r of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS)) {
			const schuelerID: number = r.parameter.get(0).valueOf();
			const kursID: number = r.parameter.get(1).valueOf();
			if ((schuelerID === idSchueler) && (kursID === idKurs)) {
				return true;
			}
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
	private getOfSchuelerHatImNamenSubstring(idSchueler: number, subString: string): boolean {
		const schueler: Schueler = this.getSchuelerG(idSchueler);
		const text: string = subString.toLowerCase();
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
	private getOfSchuelerGeschlechtOrException(idSchueler: number): Geschlecht {
		const schueler: Schueler = this.getSchuelerG(idSchueler);
		const geschlecht: Geschlecht | null = Geschlecht.fromValue(schueler.geschlecht);
		return DeveloperNotificationException.ifNull("Das Geschlecht des Schülers " + this.parent.toStringSchueler(idSchueler) + " ist nicht definiert!", geschlecht);
	}

	/**
	 * Liefert TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler den Status {@link SchuelerStatus#EXTERN} hat.
	 */
	private getOfSchuelerHatStatusExtern(idSchueler: number): boolean {
		const idStatus: number = this.getSchuelerG(idSchueler).status;
		const status: SchuelerStatus = SchuelerStatus.data().getWertByID(idStatus as number);
		return (status as unknown === SchuelerStatus.EXTERN as unknown);
	}

	/**
	 * Liefert die Fachwahl des Schülers passend zum Kurs.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Fachwahl des Schülers passend zum Kurs.
	 */
	public getOfSchuelerOfKursFachwahl(idSchueler: number, idKurs: number): GostFachwahl {
		const idFach: number = this.getKursE(idKurs).fachID;
		return this.parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
	}

	/**
	 * Liefert die Fachwahl des Schülers passend zum Fach.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return die Fachwahl des Schülers passend zum Fach.
	 */
	private getOfSchuelerOfFachFachwahl(idSchueler: number, idFach: number): GostFachwahl {
		return this.parent.schuelerGetOfFachFachwahl(idSchueler, idFach);
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
	public getOfSchuelerMengeGefiltert(idKurs: number, idFach: number, idKursart: number, konfliktTyp: number, subString: string): List<Schueler> {
		return this.getOfSchuelerMengeBasisGefiltert(idKurs, idFach, idKursart, konfliktTyp, subString, null);
	}

	/**
	 * Liefert eine Liste aller Schüler, deren Abiturjahrgang nicht dem der Blockung entspricht.
	 * <br>Hinweis: Das kann passieren, wenn nach der Erstellung eines Blockungsergebnisses, ein Schüler nicht versetzt wird.
	 *
	 * @return eine Liste aller Schüler, deren Abiturjahrgang nicht dem der Blockung entspricht.
	 */
	public getOfSchuelerMengeMitAbweichendemAbijahrgang(): List<Schueler> {
		const menge: List<Schueler> = new ArrayList<Schueler>();
		for (const schueler of this.parent.schuelerGetListe()) {
			if (schueler.abschlussjahrgang === this.parent.daten().abijahrgang) {
				continue;
			}
			if (!this.kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(SetUtils.create1(schueler.id)).listEntfernen.isEmpty() || !this.regelupdateCreateSchuelermengeEntfernen(SetUtils.create1(schueler.id)).listEntfernen.isEmpty()) {
				menge.add(schueler);
			}
		}
		return menge;
	}

	/**
	 * Liefert eine nach den Basis-Kriterien gefilterte Menge aller Schüler.
	 *
	 * @param idKurs       falls >= 0, werden Schüler des Kurses herausgefiltert.
	 * @param idFach       falls >= 0, werden Schüler mit diesem Fach herausgefiltert.
	 * @param idKursart    falls >= 0 und idFach >= 0, werden Schüler mit dieser Fach/Kursart Kombination herausgefiltert.
	 * @param konfliktTyp  falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param subString    falls der String nicht leer ist, werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 * @param geschlecht   falls != null, werden nur Schüler mit diesem {@link Geschlecht} herausgefiltert.
	 *
	 * @return eine nach den Basis-Kriterien gefilterte Menge aller Schüler.
	 */
	private getOfSchuelerMengeBasisGefiltert(idKurs: number, idFach: number, idKursart: number, konfliktTyp: number, subString: string, geschlecht: Geschlecht | null): List<Schueler> {
		const menge: List<Schueler> = new ArrayList<Schueler>();
		for (const schueler of this.parent.schuelerGetListe()) {
			if (this.getOfSchuelerErfuelltBasisKriterien(schueler.id, idKurs, idFach, idKursart, konfliktTyp, subString, geschlecht)) {
				menge.add(schueler);
			}
		}
		return menge;
	}

	/**
	 * Prüft die Basis-Kriterien (Konflikt-Typ, String, Geschlecht, Kurs und Fach) für einen Schüler.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       falls >= 0, muss der Schüler dem Kurs zugeordnet sein.
	 * @param idFach       falls >= 0, muss der Schüler dieses Fach haben.
	 * @param idKursart    falls >= 0 und idFach >= 0, muss der Schüler diese Fach/Kursart Kombination haben.
	 * @param konfliktTyp  falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param subString    falls der String nicht leer ist, muss der Vor- oder Nachname diesen String enthalten.
	 * @param geschlecht   falls != null, muss der Schüler dieses {@link Geschlecht} haben.
	 *
	 * @return TRUE, falls der Schüler die Basis-Kriterien erfüllt.
	 */
	private getOfSchuelerErfuelltBasisKriterien(idSchueler: number, idKurs: number, idFach: number, idKursart: number, konfliktTyp: number, subString: string, geschlecht: Geschlecht | null): boolean {
		if ((konfliktTyp === 1) && (!this.getOfSchuelerHatKollision(idSchueler))) {
			return false;
		}
		if ((konfliktTyp === 2) && (!this.getOfSchuelerHatNichtwahl(idSchueler))) {
			return false;
		}
		if ((konfliktTyp === 3) && ((!this.getOfSchuelerHatKollision(idSchueler)) && (!this.getOfSchuelerHatNichtwahl(idSchueler)))) {
			return false;
		}
		if (!JavaString.isEmpty(subString) && (!this.getOfSchuelerHatImNamenSubstring(idSchueler, subString))) {
			return false;
		}
		if ((geschlecht !== null) && (this.getOfSchuelerGeschlechtOrException(idSchueler).id !== geschlecht.id)) {
			return false;
		}
		if ((idKurs >= 0) && (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs))) {
			return false;
		}
		if (idFach < 0) {
			return true;
		}
		if ((idKursart >= 0) && (!this.getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart))) {
			return false;
		}
		return this.getOfSchuelerHatFach(idSchueler, idFach);
	}

	/**
	 * Liefert die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet. <br>
	 * Hinweis 1: Hat ein Schüler keine ungültige Kurse, dann gibt es die ID nicht. <br>
	 * Hinweis 2: Gibt es keine ungültigen Wahlen, so ist die Map leer. <br>
	 *
	 * @return Die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet.
	 */
	public getOfSchuelerMapIDzuUngueltigeKurse(): JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		return this.kursmengeUngueltigBySchuelerID;
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
	public getOfSchuelerOfKursHatKollision(idSchueler: number, idKurs: number): boolean {
		if (!this.getOfSchuelerHatKollision(idSchueler)) {
			return false;
		}
		const kurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		for (const idSchiene of kurs.schienen) {
			if (this.getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene).size() > 1) {
				return true;
			}
		}
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
	public getOfSchuelerIstZusammenMitSchuelerInFach(idSchueler1: number, idSchueler2: number, idFach: number): boolean {
		const kurs1: GostBlockungsergebnisKurs | null = this.kursOrNullBySchuelerIDAndFachID.getOrNull(idSchueler1, idFach);
		const kurs2: GostBlockungsergebnisKurs | null = this.kursOrNullBySchuelerIDAndFachID.getOrNull(idSchueler2, idFach);
		return ((kurs1 !== null) && (kurs2 !== null)) && (kurs1.id === kurs2.id);
	}

	/**
	 * Liefert die Anzahl an SuS, die mit dem Schüler im Kurs sein wollen.
	 *
	 * @param idS1    Die ID des {@link Schueler}-Objekts.
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die Anzahl an SuS, die mit dem Schüler im Kurs sein wollen.
	 */
	public getOfSchuelerOfKursAnzahlZusammenWuensche(idS1: number, idKurs: number): number {
		let anzahl: number = 0;
		const idFach: number = this.getKursE(idKurs).fachID;
		for (const idS2 of this.getKursE(idKurs).schueler) {
			if (idS1 === idS2) {
				continue;
			}
			const typ1: number = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
			const typ2: number = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
			if ((this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ1, idS1, idS2])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ1, idS2, idS1])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ2, idS1, idS2, idFach])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ2, idS2, idS1, idFach])) !== null)) {
				anzahl++;
			}
		}
		return anzahl;
	}

	/**
	 * Liefert die Anzahl an SuS, die mit dem Schüler nicht im Kurs sein sollen.
	 *
	 * @param idS1    Die ID des {@link Schueler}-Objekts.
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die Anzahl an SuS, die mit dem Schüler nicht im Kurs sein sollen.
	 */
	public getOfSchuelerOfKursAnzahlVerbotenWuensche(idS1: number, idKurs: number): number {
		let anzahl: number = 0;
		const idFach: number = this.getKursE(idKurs).fachID;
		for (const idS2 of this.getKursE(idKurs).schueler) {
			if (idS1 === idS2) {
				continue;
			}
			const typ1: number = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
			const typ2: number = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
			if ((this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ1, idS1, idS2])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ1, idS2, idS1])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ2, idS1, idS2, idFach])) !== null) || (this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey([typ2, idS2, idS1, idFach])) !== null)) {
				anzahl++;
			}
		}
		return anzahl;
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
	public getKursG(idKurs: number): GostBlockungKurs {
		return this.parent.kursGet(idKurs);
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
	public getKursE(idKurs: number): GostBlockungsergebnisKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursByID, idKurs);
	}

	/**
	 * Gibt die Menge der {@link GostBlockungsergebnisKurs} zurück.
	 *
	 * @return die Menge der {@link GostBlockungsergebnisKurs}
	 */
	public getKursmenge(): List<GostBlockungsergebnisKurs> {
		const result: List<GostBlockungsergebnisKurs> = new ArrayList<GostBlockungsergebnisKurs>();
		result.addAll(this.kursByID.values());
		return result;
	}

	/**
	 * Liefert den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses, erzeugt aus Fach, der Kursart und der Nummer, beispielsweise D-GK1.
	 */
	public getOfKursName(idKurs: number): string {
		return this.parent.kursGetName(idKurs);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 *
	 * @param  idKurs     Die Datenbank-ID des Kurses.
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public getOfKursOfSchieneIstZugeordnet(idKurs: number, idSchiene: number): boolean {
		const schiene: GostBlockungsergebnisSchiene = this.getSchieneE(idSchiene);
		const schienenOfKurs: JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(idKurs);
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
	private getOfKursOfSchienenNrIstZugeordnet(idKurs: number, schienenNr: number): boolean {
		for (const nr of this.getOfKursSchienenNummern(idKurs)) {
			if (nr === schienenNr) {
				return true;
			}
		}
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
	private getOfKursSchuelerIDmenge(idKurs: number): JavaSet<number> {
		return DeveloperNotificationException.ifMapGetIsNull(this.schuelerIDsByKursID, idKurs);
	}

	/**
	 * Liefert die Menge aller Schüler-Objekte des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-Objekte des Kurses.
	 */
	public getOfKursSchuelermenge(idKurs: number): List<Schueler> {
		const list: List<Schueler> = new ArrayList<Schueler>();
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			list.add(this.getSchuelerG(idSchueler));
		}
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
	public getOfKursSchienenmenge(idKurs: number): JavaSet<GostBlockungsergebnisSchiene> {
		return DeveloperNotificationException.ifMapGetIsNull(this.schienenmengeByKursID, idKurs);
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return ein Array aller Schienen-Nummern des Kurses.
	 */
	public getOfKursSchienenNummern(idKurs: number): Array<number> {
		const schienenIDmenge: List<number> = this.getKursE(idKurs).schienen;
		const a: Array<number> = Array(schienenIDmenge.size()).fill(0);
		for (let i: number = 0; i < a.length; i++) {
			const schienenID: number = schienenIDmenge.get(i).valueOf();
			a[i] = this.parent.schieneGet(schienenID).nummer;
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
	private getOfKursHatKollision(idKurs: number): boolean {
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
	private getOfKursAnzahlKollisionen(idKurs: number): number {
		return this.getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 *
	 * @param idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen (in den Schienen des Kurses).
	 */
	private getOfKursSchuelermengeMitKollisionen(idKursID: number): JavaSet<number> {
		const set: JavaSet<number> = new HashSet<number>();
		for (const schiene of this.getOfKursSchienenmenge(idKursID)) {
			for (const idSchueler of this.getKursE(idKursID).schueler) {
				if (this.getOfSchuelerOfSchieneKursmenge(idSchueler, schiene.id).size() > 1) {
					set.add(idSchueler);
				}
			}
		}
		return set;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind ohne Dummy SuS.
	 */
	public getOfKursAnzahlSchueler(idKurs: number): number {
		return this.getKursE(idKurs).schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind plus potentiell zugeordnete Dummy SuS.
	 */
	public getOfKursAnzahlSchuelerPlusDummy(idKurs: number): number {
		return this.getKursE(idKurs).schueler.size() + DeveloperNotificationException.ifMapGetIsNull(this.schuelerAnzahlDummyByKursID, idKurs);
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS des Kurses. Dummy-SuS werden durch die Regel mit dem
	 * Typ {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN} einem Kurs zugeordnet.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Dummy-SuS des Kurses.
	 */
	public getOfKursAnzahlSchuelerDummy(idKurs: number): number {
		return DeveloperNotificationException.ifMapGetIsNull(this.schuelerAnzahlDummyByKursID, idKurs);
	}

	/**
	 * Liefert die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl externer SuS die dem Kurs zugeordnet sind.
	 */
	public getOfKursAnzahlSchuelerExterne(idKurs: number): number {
		const kursE: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		return ListUtils.getCountFiltered(kursE.schueler, { test: (idSchueler: number) => this.getOfSchuelerHatStatusExtern(idSchueler) });
	}

	/**
	 * Liefert die Anzahl externer SuS und der Dummy-SuS die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl externer SuS und der Dummy-SuS die dem Kurs zugeordnet sind.
	 */
	public getOfKursAnzahlSchuelerExternePlusDummies(idKurs: number): number {
		return this.getOfKursAnzahlSchuelerExterne(idKurs) + this.getOfKursAnzahlSchuelerDummy(idKurs);
	}

	/**
	 * Liefert die Anzahl internen SuS. Das sind alle SuS der Kursliste abzüglich der SuS mit Status "extern".
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl internen SuS. Das sind alle SuS der Kursliste abzüglich der SuS mit Status "extern".
	 */
	public getOfKursAnzahlSchuelerInterne(idKurs: number): number {
		return this.getOfKursAnzahlSchueler(idKurs) - this.getOfKursAnzahlSchuelerExterne(idKurs);
	}

	/**
	 * Liefert die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl aller Schüler des Kurses mit Schriftlichkeit {@link GostSchriftlichkeit#SCHRIFTLICH}.
	 */
	public getOfKursAnzahlSchuelerSchriftlich(idKurs: number): number {
		return this.getOfSchuelerAnzahlGefiltert(idKurs, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH);
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public getOfKursAnzahlSchienenIst(idKurs: number): number {
		return this.getOfKursSchienenmenge(idKurs).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public getOfKursAnzahlSchienenSoll(idKurs: number): number {
		return this.getKursE(idKurs).anzahlSchienen;
	}

	/**
	 * Liefert die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern, die den Kurs mit Abiturfach 1 oder 2 gewählt (also LK) haben.
	 */
	public getOfKursAnzahlSchuelerAbiturLK(idKurs: number): number {
		let summe: number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			try {
				const fachwahl: GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach !== null) && ((fachwahl.abiturfach === 1) || (fachwahl.abiturfach === 2))) {
					summe++;
				}
			} catch(dne : any) {
				// empty block
			}
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
	public getOfKursAnzahlSchuelerAbitur3(idKurs: number): number {
		let summe: number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			try {
				const fachwahl: GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach !== null) && (fachwahl.abiturfach === 3)) {
					summe++;
				}
			} catch(dne : any) {
				// empty block
			}
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
	public getOfKursAnzahlSchuelerAbitur4(idKurs: number): number {
		let summe: number = 0;
		for (const idSchueler of this.getKursE(idKurs).schueler) {
			try {
				const fachwahl: GostFachwahl = this.getOfSchuelerOfKursFachwahl(idSchueler, idKurs);
				if ((fachwahl.abiturfach !== null) && (fachwahl.abiturfach === 4)) {
					summe++;
				}
			} catch(dne : any) {
				// empty block
			}
		}
		return summe;
	}

	/**
	 * Liefert die maximale Anzahl an SuS, die in dem Kurs sein dürfen, oder 999 falls es keine Begrenzung gibt.
	 *
	 * @param idKurs  Die ID des {@link StundenplanKurs}-Objekts.
	 *
	 * @return die maximale Anzahl an SuS, die in dem Kurs sein dürfen, oder 999 falls es keine Begrenzung gibt.
	 */
	public getOfKursMaxSuS(idKurs: number): number {
		for (const rAlt of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL)) {
			if (idKurs === rAlt.parameter.get(0)) {
				return rAlt.parameter.get(1);
			}
		}
		return 999;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schülermenge zuordnet.
	 */
	public getMappingKursIDSchuelerIDs(): JavaMap<number, JavaSet<number>> {
		return this.schuelerIDsByKursID;
	}

	/**
	 * Liefert die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 *
	 * @return Die Map, welche jedem Kurs seine Schienenmenge zuordnet.
	 */
	public getMappingKursIDSchienenmenge(): JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> {
		return this.schienenmengeByKursID;
	}

	/**
	 * Liefert die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 * <br>Hinweis: Nur verletzte Regel-IDs sind in der KEY-Menge enthalten.
	 *
	 * @return die Map, welche der verletzten Regel-ID (long) die Beschreibung (String) zuordnet.
	 */
	public regelGetMapRegelIdToVerletzungString(): JavaMap<number, string> {
		return this.regelverletzungsBeschreibungByRegelID;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der definierten Regeln.
	 */
	public regelGetTooltipFuerRegelverletzungen(): string {
		return this.regelverletzungenTooltipRegeln;
	}

	/**
	 * Liefert einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 *
	 * @return einen Tooltip für alle Regelverletzungen der Fächerparallelität.
	 */
	public regelGetTooltipFuerFaecherparallelitaet(): string {
		return this.regelverletzungenTooltipFaecherparallelitaet;
	}

	/**
	 * Liefert einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 *
	 * @return einen Tooltip für alle Wahlkonflikte (Kollisionen und Nichtwahlen) ggf. gekürzt.
	 */
	public regelGetTooltipFuerWahlkonflikte(): string {
		return this.regelverletzungenTooltipWahlkonflikte;
	}

	/**
	 * Liefert einen Tooltip für alle Kursdifferenzen.
	 *
	 * @return einen Tooltip für alle Kursdifferenzen.
	 */
	public regelGetTooltipFuerKursdifferenzen(): string {
		return this.regelverletzungenTooltipKursdifferenzen;
	}

	private static regelupdateIsEqualPair(a1: number, a2: number, b1: number, b2: number): boolean {
		return ((a1 === b1) && (a2 === b2)) || ((a1 === b2) && (a2 === b1));
	}

	private static regelupdateAppend(u1: GostBlockungRegelUpdate, u2: GostBlockungRegelUpdate): void {
		u1.listEntfernen.addAll(u2.listEntfernen);
		u1.listHinzuzufuegen.addAll(u2.listHinzuzufuegen);
	}

	/**
	 * Entfernt die Regel mit dem übergebenen Schlüssel aus dem Update, falls sie existiert.
	 *
	 * @param u          das Update, in dem die Regel entfernt werden soll
	 * @param parameter  der Schlüssel (Typ und Parameter) der zu entfernenden Regel
	 */
	private regelupdateEntferneFallsVorhanden(u: GostBlockungRegelUpdate, parameter: Array<number>): void {
		const regel: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(new LongArrayKey(parameter));
		if (regel !== null) {
			u.listEntfernen.add(regel);
		}
	}

	/**
	 * Fügt die Regel dem Update hinzu, falls sie noch nicht existiert.
	 *
	 * @param u      das Update, in dem die Regel hinzugefügt werden soll
	 * @param key    der Schlüssel (Typ und Parameter) der Regel
	 * @param regel  die hinzuzufügende Regel
	 */
	private regelupdateHinzufuegenFallsNichtVorhanden(u: GostBlockungRegelUpdate, key: LongArrayKey, regel: GostBlockungRegel): void {
		if (this.parent.regelGetByLongArrayKeyOrNull(key) === null) {
			u.listHinzuzufuegen.add(regel);
		}
	}

	/**
	 * Entfernt alle Regeln des Typs, deren erster Parameter mit param0 übereinstimmt, aus dem Update.
	 *
	 * @param u       das Update, in dem die Regeln entfernt werden sollen
	 * @param typ     der Typ der zu entfernenden Regeln
	 * @param param0  der erste Parameter, der übereinstimmen muss
	 */
	private regelupdateEntferneAlleVonTypMitParameter0(u: GostBlockungRegelUpdate, typ: GostKursblockungRegelTyp, param0: number): void {
		for (const rAlt of this.parent.regelGetListeOfTyp(typ)) {
			if (param0 === rAlt.parameter.get(0)) {
				u.listEntfernen.add(rAlt);
			}
		}
	}

	/**
	 * Entfernt alle Regeln des Typs, deren Parameterpaar (0,1) mit dem übergebenen Paar übereinstimmt, aus dem Update.
	 *
	 * @param u    das Update, in dem die Regeln entfernt werden sollen
	 * @param typ  der Typ der zu entfernenden Regeln
	 * @param id1  die erste ID des Paares
	 * @param id2  die zweite ID des Paares
	 */
	private regelupdateEntferneAlleVonTypMitPair(u: GostBlockungRegelUpdate, typ: GostKursblockungRegelTyp, id1: number, id2: number): void {
		for (const rAlt of this.parent.regelGetListeOfTyp(typ)) {
			if (GostBlockungsergebnisManager.regelupdateIsEqualPair(rAlt.parameter.get(0), rAlt.parameter.get(1), id1, id2)) {
				u.listEntfernen.add(rAlt);
			}
		}
	}

	/**
	 * Prüft, ob ein Regel-Patch ausgeführt werden darf: Der Typ der alten Regel muss dem erwarteten Typ entsprechen und es darf noch
	 * keine Regel mit den neuen Parametern existieren. <br>
	 * Hinweis: Die Prüfung erfolgt bewusst vor der Berechnung des Create-Updates, damit z. B. bei falschem Regeltyp keine
	 * ungültigen Ziel-Parameter mehr ausgewertet werden.
	 *
	 * @param idRegelAlt    Die ID der alten zu modifizierenden Regel.
	 * @param typ           Der erwartete Typ der alten Regel.
	 * @param parameterNeu  Die Parameter der neuen Regel (Typ und Parameter).
	 *
	 * @return die alte Regel, falls der Patch ausgeführt werden darf, andernfalls NULL.
	 */
	private regelupdatePatchByIdPruefe(idRegelAlt: number, typ: GostKursblockungRegelTyp, parameterNeu: Array<number>): GostBlockungRegel | null {
		const rAlt: GostBlockungRegel = this.parent.regelGet(idRegelAlt);
		if (rAlt.typ !== typ.typ) {
			return null;
		}
		const kNeu: LongArrayKey = new LongArrayKey(parameterNeu);
		if (this.parent.regelGetByLongArrayKeyOrNull(kNeu) !== null) {
			return null;
		}
		return rAlt;
	}

	/**
	 * Baut das Update eines Regel-Patches zusammen: Übernimmt das Update uNeu und entfernt die alte Regel (falls nicht bereits
	 * durch Kaskaden gelöscht).
	 *
	 * @param u      Das Update, in das die Veränderungen eingetragen werden.
	 * @param rAlt   Die alte zu entfernende Regel.
	 * @param uNeu   Das Update mit den nötigen Veränderungen für die neue Regel.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt.
	 */
	private regelupdatePatchByIdZusammenbauen(u: GostBlockungRegelUpdate, rAlt: GostBlockungRegel, uNeu: GostBlockungRegelUpdate): GostBlockungRegelUpdate {
		GostBlockungsergebnisManager.regelupdateAppend(u, uNeu);
		if (!u.listEntfernen.contains(rAlt)) {
			u.listEntfernen.add(rAlt);
		}
		return u;
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
	public regelupdateCreateKursartSperreSchienenVonBis(kursart: number, schienenNrVon: number, schienenNrBis: number): GostBlockungRegelUpdate {
		const von: number = Math.min(schienenNrVon, schienenNrBis);
		const bis: number = Math.max(schienenNrVon, schienenNrBis);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const kurs of this.getKursmenge()) {
			for (let schienenNr: number = von; schienenNr <= bis; schienenNr++) {
				if (kurs.kursart === kursart) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr]);
				}
			}
		}
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, bis, von]);
		const keyVonBis: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis]);
		this.regelupdateHinzufuegenFallsNichtVorhanden(u, keyVonBis, DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis));
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
	public regelupdateCreateKursFixiereInSchieneMarkiert(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const nr of setSchienenNr) {
				if (this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nr]);
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				}
			}
			for (let nr: number = 1; nr <= this.schieneByNR.size(); nr++) {
				if (!this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 * <br>(1) Fixierungen innerhalb der Kurslage werden hinzugefügt, falls noch nicht existent.
	 * <br>(2) Fixierungen außerhalb der Kurslage werden gelöscht.
	 *
	 * @param setKursID  Die Kursmenge, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Kursmenge komplett in ihrer Lage zu fixieren.
	 */
	public regelupdateCreateKursFixiereMengeInIhrenSchienen(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (let nr: number = 1; nr <= this.schieneByNR.size(); nr++) {
				if (this.getOfKursOfSchienenNrIstZugeordnet(idKurs, nr)) {
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr));
				} else {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
				}
			}
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
	public regelupdateCreateKursFixiereAlleInIhrenSchienen(): GostBlockungRegelUpdate {
		return this.regelupdateCreateKursFixiereMengeInIhrenSchienen(this.parent.kursmengeGetSetDerIDs());
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
	public regelupdateCreateKursFixiereInSchieneToggle(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this.schienenmengeByKursID, idKurs)) {
				const schieneG: GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					const keyFixierung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelFixierung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keyFixierung);
					if (regelFixierung !== null) {
						u.listEntfernen.add(regelFixierung);
						continue;
					}
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer));
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
				}
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
	public regelupdateCreateKursFixiereInEinerSchiene(idKurs: number, schienenNr: number): GostBlockungRegelUpdate {
		return this.regelupdateCreateKursFixiereInEinerSchieneHelper(idKurs, schienenNr, true);
	}

	private regelupdateCreateKursFixiereInEinerSchieneHelper(idKurs: number, schienenNr: number, checkErlaubt: boolean): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
		const kFixierung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
		const rFixierung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(kFixierung);
		if (rFixierung !== null) {
			return u;
		}
		if (checkErlaubt && !this.parent.kursIstWeitereFixierungErlaubt(idKurs)) {
			for (let nr: number = 1; nr <= this.schieneByNR.size(); nr++) {
				this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
			}
		}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr));
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
	public regelupdateCreateKursSperreInSchiene(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const schienenNr of setSchienenNr) {
				const keySperrung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
				const regelSperrung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keySperrung);
				const keyFixierung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
				const regelFixierung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keyFixierung);
				if ((regelSperrung === null) && (regelFixierung === null)) {
					u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr));
				}
			}
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
	public regelupdateCreateKursSperreInSchieneToggle(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this.schienenmengeByKursID, idKurs)) {
				const schieneG: GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					const keySperrung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
					const regelSperrung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keySperrung);
					if (regelSperrung !== null) {
						u.listEntfernen.add(regelSperrung);
						continue;
					}
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schieneG.nummer));
				}
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
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 * @param setKursID      Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Schülermengen-Kursmengen-Fixierung zu setzen.
	 */
	public regelupdateCreateSchuelerFixierenInKurs(setSchuelerID: JavaSet<number>, setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idSchueler of setSchuelerID) {
			for (const idKurs of setKursID) {
				schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
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
	public regelupdateCreateSchuelerFixierenInDenKursen(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			GostBlockungsergebnisManager.regelupdateAppend(u, this.regelupdateCreateSchuelerFixierenInKurs(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs)));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 * <br>Die Methode delegiert alles an {@link #regelupdateCreateSchuelerFixierenInKurs}.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler in ihren aktuellen Kursen zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenInAllenKursen(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenInDenKursen(this.kursByID.keySet());
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LK-Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypLk(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypLkDerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypLkDerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstLK(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB3-Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypAb3(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypAb3DerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypAb3DerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstAB3(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller LKs und AB3-Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypLkUndAb3(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstLKoderAB3(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB4-Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypAb4(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypAb4DerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypAb4DerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstAB4(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller AB-Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypAb(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypAbDerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypAbDerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstAbiturfach(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Menge aller schriftlichen Schüler zu fixieren.
	 */
	public regelupdateCreateSchuelerFixierenTypSchriftlich(): GostBlockungRegelUpdate {
		return this.regelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge(this.kursIDs);
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
	public regelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge(kursIDs: JavaSet<number>): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (this.getOfSchuelerOfKursIstSchriftlich(idSchueler, idKurs)) {
					schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
				}
			}
		}
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Schüler-Kurs-Fixierung.
	 *
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param idSchueler             Die ID des Schülers.
	 * @param idKurs                 Die ID des Kurses.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private regelupdateCreate04xSchuelerFixierenInKurs(idSchueler: number, idKurs: number): GostBlockungRegelUpdate {
		const schuelerKursPaare: JavaSet<PairNN<number, number>> = new HashSet<PairNN<number, number>>();
		schuelerKursPaare.add(new PairNN<number, number>(idSchueler, idKurs));
		return this.regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare);
	}

	/**
	 * Liefert alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 *
	 * <br>(0) Wenn der Schüler den Kurs nicht wählen kann, wird das Paar ignoriert.
	 * <br>(1) Wenn der Schüler im Kurs gesperrt ist, wird dies entfernt.
	 * <br>(2) Wenn der Schüler nicht im Kurs fixiert ist, wird er fixiert.
	 * <br>(3) Wenn der Schüler im Nachbar-Kurs fixiert ist, wird dies entfernt.
	 *
	 * @param schuelerKursPaare      Die Menge aller Schüler-Kurs-Paare.
	 *
	 * @return alle GostBlockungRegelUpdate-Objekte für die Umsetzung einer Menge von Schüler-Kurs-Fixierungen.
	 */
	private regelupdateCreateSchuelerFixierenInKursmenge(schuelerKursPaare: JavaSet<PairNN<number, number>>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const pair of schuelerKursPaare) {
			const idSchueler: number = pair.a;
			const idKurs: number = pair.b;
			const kurs1: GostBlockungKurs = this.parent.kursGet(idKurs);
			if (!this.parent.schuelerGetHatFachart(idSchueler, kurs1.fach_id, kurs1.kursart)) {
				continue;
			}
			this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
			for (const kurs2 of this.parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
				const keyFixierung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
				if (kurs1.id === kurs2.id) {
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, keyFixierung, DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs));
				} else {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
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
	public regelupdateCreateSchuelerVerbietenInKurs(setSchuelerID: JavaSet<number>, setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID) {
			for (const idKurs of setKursID) {
				this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
				this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs));
			}
		}
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
	public regelupdateCreateKursartAlleinInSchienenVonBis(kursart: number, schienenNrVon: number, schienenNrBis: number): GostBlockungRegelUpdate {
		const von: number = Math.min(schienenNrVon, schienenNrBis);
		const bis: number = Math.max(schienenNrVon, schienenNrBis);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		this.regelupdateEntferneAlleVonTypMitParameter0(u, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS, kursart);
		for (const kurs of this.getKursmenge()) {
			for (let schienenNr: number = 1; schienenNr <= this.parent.schieneGetAnzahl(); schienenNr++) {
				const imSchienenBereich: boolean = (von <= schienenNr) && (schienenNr <= bis);
				const richtigeKursart: boolean = (kurs.kursart === kursart);
				if (imSchienenBereich !== richtigeKursart) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, kurs.id, schienenNr]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schienenNr]);
				}
			}
		}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ, kursart, von, bis));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B zusammen (Regel 8) sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel bereits existiert, aber die IDs nicht aufsteigend sind, wird die (MaxKursID, MinKursID)-Kombination entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Verbote der Kursmenge (alle Paarungen) zu setzen.
	 */
	public regelupdateCreateKursVerbietenMitKurs(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID) {
			for (const idKurs2 of setKursID) {
				if (idKurs1 < idKurs2) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1]);
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2));
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 * <br>(1) Wenn Kurs A mit Kurs B verboten (Regel 7) sein soll, wird dies entfernt.
	 * <br>(2) Wenn die Regel bereits existiert, aber die IDs nicht aufsteigend sind, wird die (MaxKursID, MinKursID)-Kombination entfernt.
	 * <br>(3) Wenn die Regel nicht existiert, wird sie hinzugefügt.
	 *
	 * @param setKursID  Die Menge der Kurs-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Kurs-Zusammen-Gebote von setKursID (alle Paarungen) zu setzen.
	 */
	public regelupdateCreateKursZusammenMitKurs(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID) {
			for (const idKurs2 of setKursID) {
				if (idKurs1 < idKurs2) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs1, idKurs2]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKurs2, idKurs1]);
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs2, idKurs1]);
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2]), DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKurs1, idKurs2));
				}
			}
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
	public regelupdateCreateKursMitDummySusAuffuellen(idKurs: number, anzahl: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		this.regelupdateEntferneAlleVonTypMitParameter0(u, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN, idKurs);
		if (anzahl >= GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN) {
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, idKurs, anzahl));
		}
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
	public regelupdateCreateLehrkaefteBeachten(erstellen: boolean): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const keyDummyAlt: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ]);
		if (!erstellen) {
			this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ]);
		}
		if (erstellen) {
			this.regelupdateHinzufuegenFallsNichtVorhanden(u, keyDummyAlt, DTOUtils.newGostBlockungRegel0(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ));
		}
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
	public regelupdateCreateSchuelerZusammenMitSchuelerInFach(idSchueler1: number, idSchueler2: number, idFach: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1: number = Math.min(idSchueler1, idSchueler2);
		const idS2: number = Math.max(idSchueler1, idSchueler2);
		if (idS1 === idS2) {
			return u;
		}
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]), DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));
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
	public regelupdateCreateSchuelerVerbietenMitSchuelerInFach(idSchueler1: number, idSchueler2: number, idFach: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1: number = Math.min(idSchueler1, idSchueler2);
		const idS2: number = Math.max(idSchueler1, idSchueler2);
		if (idS1 === idS2) {
			return u;
		}
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS2, idS1]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS2, idS1]);
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS2, idS1, idFach]);
		this.regelupdateHinzufuegenFallsNichtVorhanden(u, new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach]), DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idS1, idS2, idFach));
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
	public regelupdateCreateSchuelerZusammenMitSchueler(idSchueler1: number, idSchueler2: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1: number = Math.min(idSchueler1, idSchueler2);
		const idS2: number = Math.max(idSchueler1, idSchueler2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER, idS1, idS2);
		if ((0 <= idS1) && (idS1 < idS2)) {
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idS1, idS2));
		}
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
	public regelupdateCreateSchuelerVerbietenMitSchueler(idSchueler1: number, idSchueler2: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const idS1: number = Math.min(idSchueler1, idSchueler2);
		const idS2: number = Math.max(idSchueler1, idSchueler2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER, idS1, idS2);
		this.regelupdateEntferneAlleVonTypMitPair(u, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER, idS1, idS2);
		if ((0 <= idS1) && (idS1 < idS2)) {
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idS1, idS2));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 * <br>(1) Wenn die Regel bereits existiert, wird sie (zunächst) entfernt.
	 * <br>(2) Wenn danach die Anzahl einen Wert im Intervall [0;99] hat, wird die Regel hinzugefügt.
	 * <br>Hinweis: Dummy-SuS sind in der maximalen Schüleranzahl inklusive.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 * @param anzahl  Die maximale Anzahl an SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die maximale Anzahl an Schülern eines Kurses zu setzen.
	 */
	public regelupdateCreateKursMaximaleSchueleranzahl(idKurs: number, anzahl: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		this.regelupdateEntferneAlleVonTypMitParameter0(u, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL, idKurs);
		if ((anzahl >= GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN) && (anzahl <= GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX)) {
			u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, idKurs, anzahl));
		}
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
	public regelupdateCreateSchuelerIgnorieren(setSchuelerID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID) {
			const keySchuelerIgnorieren: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler]);
			this.regelupdateHinzufuegenFallsNichtVorhanden(u, keySchuelerIgnorieren, DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler));
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
	public regelupdateCreateKursKursdifferenzBeiDerVisualisierungIgnorieren(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			const keyKursKursdifferenzIgnorieren: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs]);
			this.regelupdateHinzufuegenFallsNichtVorhanden(u, keyKursKursdifferenzIgnorieren, DTOUtils.newGostBlockungRegel1(GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKurs));
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
	public regelupdateCreateFachKursartMaximaleAnzahlProSchiene(idFach: number, idKursart: number, maximal: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const r18 of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE)) {
			if ((r18.parameter.get(0) === idFach) && (r18.parameter.get(1) === idKursart)) {
				u.listEntfernen.add(r18);
			}
		}
		if (maximal === GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN) {
			for (const r7 of this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS)) {
				const idKurs1: number = r7.parameter.get(0).valueOf();
				const idKurs2: number = r7.parameter.get(1).valueOf();
				const kurs1: GostBlockungsergebnisKurs = this.getKursE(idKurs1);
				const kurs2: GostBlockungsergebnisKurs = this.getKursE(idKurs2);
				if ((kurs1.fachID === idFach) && (kurs2.fachID === idFach) && (kurs1.kursart === idKursart) && (kurs2.kursart === idKursart)) {
					u.listEntfernen.add(r7);
				}
			}
		}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel3(GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ, idFach, idKursart, maximal));
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Regeln einer Schülermenge zu entfernen.
	 *
	 * @param setSchuelerID  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Regeln einer Schülermenge zu entfernen.
	 */
	public regelupdateCreateSchuelermengeEntfernen(setSchuelerID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const regel of this.parent.regelGetListe()) {
			const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
			for (let i: number = 0; i < typ.getParamCount(); i++) {
				if ((typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.SCHUELER_ID as unknown) && setSchuelerID.contains(regel.parameter.get(i))) {
					u.listEntfernen.add(regel);
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursartSperreSchienenVonBis(idRegelAlt: number, kursart: number, schienenNrVon: number, schienenNrBis: number): GostBlockungRegelUpdate {
		const von: number = Math.min(schienenNrVon, schienenNrBis);
		const bis: number = Math.max(schienenNrVon, schienenNrBis);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS, [GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ, kursart, von, bis]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursartSperreSchienenVonBis(kursart, von, bis));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param schienenNr  Die Nummer der Schiene, die fixiert werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursFixiereInEinerSchiene(idRegelAlt: number, idKurs: number, schienenNr: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursFixiereInEinerSchieneHelper(idKurs, schienenNr, false));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param idKurs         Die ID des Kurses.
	 * @param schienenNr     Die Nummer der Schiene.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursSperreInSchiene(idRegelAlt: number, idKurs: number, schienenNr: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursSperreInSchiene(SetUtils.create1(idKurs), SetUtils.create1(schienenNr)));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Wenn der Ziel-Kurs für den Schüler nicht wählbar ist (fehlende Fachwahl), bleibt das Update leer und die alte
	 * Fixierung erhalten.
	 * <br>(4) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 * @param idKurs      Die ID des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 *         Das Update kann leer sein, falls der Patch nicht ausgeführt wird (siehe (2) und (3)).
	 */
	public regelupdatePatchByIdSchuelerFixierenInKurs(idRegelAlt: number, idSchueler: number, idKurs: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		if (rAlt === null) {
			return u;
		}
		const zielKurs: GostBlockungKurs = this.parent.kursGet(idKurs);
		if (!this.parent.schuelerGetHatFachart(idSchueler, zielKurs.fach_id, zielKurs.kursart)) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreate04xSchuelerFixierenInKurs(idSchueler, idKurs));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 * @param idKurs      Die ID des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerVerbietenInKurs(idRegelAlt: number, idSchueler: number, idKurs: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerVerbietenInKurs(SetUtils.create1(idSchueler), SetUtils.create1(idKurs)));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt     Die ID der alten zu modifizierenden Regel.
	 * @param kursart        Die Kursart der Kurse für welche diese Regel gilt.
	 * @param schienenNrVon  Der Anfangsbereich der Schienen.
	 * @param schienenNrBis  Der Endbereich der Schienen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursartAlleinInSchienenVonBis(idRegelAlt: number, kursart: number, schienenNrVon: number, schienenNrBis: number): GostBlockungRegelUpdate {
		const von: number = Math.min(schienenNrVon, schienenNrBis);
		const bis: number = Math.max(schienenNrVon, schienenNrBis);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS, [GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ, kursart, von, bis]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursartAlleinInSchienenVonBis(kursart, von, bis));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs1     Die ID des 1. Kurses.
	 * @param idKurs2     Die ID des 2. Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursVerbietenMitKurs(idRegelAlt: number, idKurs1: number, idKurs2: number): GostBlockungRegelUpdate {
		const idKursMin: number = Math.min(idKurs1, idKurs2);
		const idKursMax: number = Math.max(idKurs1, idKurs2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS, [GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ, idKursMin, idKursMax]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursVerbietenMitKurs(SetUtils.create2(idKursMin, idKursMax)));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs1     Die ID des 1. Kurses.
	 * @param idKurs2     Die ID des 2. Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursZusammenMitKurs(idRegelAlt: number, idKurs1: number, idKurs2: number): GostBlockungRegelUpdate {
		const idKursMin: number = Math.min(idKurs1, idKurs2);
		const idKursMax: number = Math.max(idKurs1, idKurs2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS, [GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ, idKursMin, idKursMax]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursZusammenMitKurs(SetUtils.create2(idKursMin, idKursMax)));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die ID des Kurses.
	 * @param anzahl      Die Anzahl an Dummy-SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursMitDummySusAuffuellen(idRegelAlt: number, idKurs: number, anzahl: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN, [GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, idKurs, anzahl]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursMitDummySusAuffuellen(idKurs, anzahl));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerZusammenMitSchuelerInFach(idRegelAlt: number, idSchueler1: number, idSchueler2: number, idFach: number): GostBlockungRegelUpdate {
		const idSchuelerMin: number = Math.min(idSchueler1, idSchueler2);
		const idSchuelerMax: number = Math.max(idSchueler1, idSchueler2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, idSchuelerMin, idSchuelerMax, idFach]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerZusammenMitSchuelerInFach(idSchuelerMin, idSchuelerMax, idFach));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 * @param idFach       Die Datenbank-ID des Faches
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerVerbietenMitSchuelerInFach(idRegelAlt: number, idSchueler1: number, idSchueler2: number, idFach: number): GostBlockungRegelUpdate {
		const idSchuelerMin: number = Math.min(idSchueler1, idSchueler2);
		const idSchuelerMax: number = Math.max(idSchueler1, idSchueler2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, idSchuelerMin, idSchuelerMax, idFach]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerVerbietenMitSchuelerInFach(idSchuelerMin, idSchuelerMax, idFach));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerZusammenMitSchueler(idRegelAlt: number, idSchueler1: number, idSchueler2: number): GostBlockungRegelUpdate {
		const idSchuelerMin: number = Math.min(idSchueler1, idSchueler2);
		const idSchuelerMax: number = Math.max(idSchueler1, idSchueler2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER, [GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, idSchuelerMin, idSchuelerMax]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerZusammenMitSchueler(idSchuelerMin, idSchuelerMax));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler1  Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2  Die Datenbank-ID des 2. Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerVerbietenMitSchueler(idRegelAlt: number, idSchueler1: number, idSchueler2: number): GostBlockungRegelUpdate {
		const idSchuelerMin: number = Math.min(idSchueler1, idSchueler2);
		const idSchuelerMax: number = Math.max(idSchueler1, idSchueler2);
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER, [GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, idSchuelerMin, idSchuelerMax]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerVerbietenMitSchueler(idSchuelerMin, idSchuelerMax));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idKurs      Die ID des Kurses.
	 * @param anzahl      Die maximale Anzahl der SuS des Kurses.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdKursMaximaleSchueleranzahl(idRegelAlt: number, idKurs: number, anzahl: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL, [GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, idKurs, anzahl]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateKursMaximaleSchueleranzahl(idKurs, anzahl));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 * <br>(1) Wenn die alte Regel nicht gefunden wird, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn eine Regel mit genau den selben Parametern bereits existiert, passiert nichts.
	 * <br>(3) Andernfalls wird die alte Regel entfernt und eine neue Regel hinzugefügt.
	 *
	 * @param idRegelAlt  Die ID der alten zu modifizierenden Regel.
	 * @param idSchueler  Die ID des Schülers.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um eine Regel dieses Typs zu patchen.
	 */
	public regelupdatePatchByIdSchuelerIgnorieren(idRegelAlt: number, idSchueler: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel | null = this.regelupdatePatchByIdPruefe(idRegelAlt, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN, [GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ, idSchueler]);
		if (rAlt === null) {
			return u;
		}
		return this.regelupdatePatchByIdZusammenbauen(u, rAlt, this.regelupdateCreateSchuelerIgnorieren(SetUtils.create1(idSchueler)));
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Regel von einem zu einem anderen Kurs zu patchen.
	 *
	 * <br>(1) Wenn die alte Regel nicht existiert, wird eine {@link DeveloperNotificationException} geworfen.
	 * <br>(2) Wenn die neue Kurs-ID bereits existiert, passiert nichts.
	 * <br>(3) Wenn die alte Kurs-ID der neuen Kurs-ID gleicht, passiert nichts.
	 * <br>(4) Andernfalls wird die alte Regel gelöscht (idKursAlt) und eine neue Regel wird erzeugt (idKursNeu).
	 *
	 * @param idRegelAlt  Die Regel, die modifiziert wird.
	 * @param idKursNeu   Die neue Kurs-ID deren Regel hinzugefügt werden soll.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um die Regel von einem zu einem anderen Kurs zu patchen.
	 */
	public regelupdatePatchByIdKursKursdifferenzBeiDerVisualisierungIgnorieren(idRegelAlt: number, idKursNeu: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		const rAlt: GostBlockungRegel = this.parent.regelGet(idRegelAlt);
		if (rAlt.typ !== GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ) {
			return u;
		}
		const kNeu: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ, idKursNeu]);
		const rNeu: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(kNeu);
		if (rNeu !== null) {
			return u;
		}
		const idKursAlt: number = rAlt.parameter.get(0).valueOf();
		if (idKursAlt === idKursNeu) {
			return u;
		}
		GostBlockungsergebnisManager.regelupdateAppend(u, this.regelupdateCreateKursKursdifferenzBeiDerVisualisierungIgnorieren(SetUtils.create1(idKursNeu)));
		if (!u.listEntfernen.contains(rAlt)) {
			u.listEntfernen.add(rAlt);
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
	public regelupdateRemoveKursFixiereInSchieneMarkiert(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const schieneE of DeveloperNotificationException.ifMapGetIsNull(this.schienenmengeByKursID, idKurs)) {
				const schieneG: GostBlockungSchiene = this.getSchieneG(schieneE.id);
				if (setSchienenNr.contains(schieneG.nummer)) {
					this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schieneG.nummer]);
				}
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
	public regelupdateRemoveKursFixiereMengeInIhrenSchienen(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (let nr: number = 1; nr <= this.schieneByNR.size(); nr++) {
				this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Kurs-Schienen-Fixierungen zu lösen.
	 */
	public regelupdateRemoveKursFixiereAlleInIhrenSchienen(): GostBlockungRegelUpdate {
		return this.regelupdateRemoveKursFixiereMengeInIhrenSchienen(this.parent.kursmengeGetSetDerIDs());
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
	public regelupdateRemoveKursFixiereInEinerSchiene(idKurs: number, schienenNr: number): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, schienenNr]);
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
	public regelupdateRemoveKursSperreInSchiene(setKursID: JavaSet<number>, setSchienenNr: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			for (const schienenNr of setSchienenNr) {
				this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, schienenNr]);
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
	public regelupdateRemoveSchuelerFixierenInKurs(setSchuelerID: JavaSet<number>, setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idSchueler of setSchuelerID) {
			for (const idKurs of setKursID) {
				this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
			}
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
	public regelupdateRemoveSchuelerFixierenInDenKursen(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs of setKursID) {
			GostBlockungsergebnisManager.regelupdateAppend(u, this.regelupdateRemoveSchuelerFixierenInKurs(this.getOfKursSchuelerIDmenge(idKurs), SetUtils.create1(idKurs)));
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 * <br>Es werden alle Regeln des Typs {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungRegelUpdate}-Objekt, um alle Schüler-Kurs-Fixierungen zu lösen.
	 */
	public regelupdateRemoveSchuelerFixierenInAllenKursen(): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		u.listEntfernen.addAll(this.parent.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS));
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
	public regelupdateRemoveSchuelerFixierenInDenKursenToggle(setKursID: JavaSet<number>): GostBlockungRegelUpdate {
		const u: GostBlockungRegelUpdate = new GostBlockungRegelUpdate();
		for (const idKurs1 of setKursID) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs1)) {
				const kFixierung: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1]);
				const rFixierung: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(kFixierung);
				if (rFixierung !== null) {
					u.listEntfernen.add(rFixierung);
				} else {
					this.regelupdateHinzufuegenFallsNichtVorhanden(u, kFixierung, DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs1));
				}
				const kurs1: GostBlockungKurs = this.parent.kursGet(idKurs1);
				for (const kurs2 of this.parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
					if (kurs1.id !== kurs2.id) {
						this.regelupdateEntferneFallsVorhanden(u, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, kurs2.id]);
					}
				}
			}
		}
		return u;
	}

	/**
	 * Entfernt erst alle Regeln aus {@link GostBlockungRegelUpdate#listEntfernen} und
	 * fügt dann die neuen Regeln aus {@link GostBlockungRegelUpdate#listHinzuzufuegen} hinzu.
	 *
	 * @param update  Das {@link GostBlockungRegelUpdate}-Objekt.
	 */
	public regelupdateExecute(update: GostBlockungRegelUpdate): void {
		this.parent.regelRemoveListe(update.listEntfernen);
		this.parent.regelAddListe(update.listHinzuzufuegen);
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
	public kursSchuelerUpdateLeereAlleKurse(entferneAuchFixierte: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		return this.kursSchuelerUpdateLeereKursmenge(this.kursIDs, entferneAuchFixierte);
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
	public kursSchuelerUpdateLeereKursmenge(kursIDs: JavaSet<number>, entferneAuchFixierte: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const idKurs of kursIDs) {
			for (const idSchueler of this.getOfKursSchuelerIDmenge(idKurs)) {
				if (entferneAuchFixierte || !this.parent.schuelerGetIstFixiertInKurs(idSchueler, idKurs)) {
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 * <br>(1) Wenn der Schüler dem Kurs zugeordnet ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn der Schüler dem Kurs zugeordnet ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist. Auch die Fixierungs-Regel wird entfernt.
	 * <br>Hinweis: Ungültige Zuordnungen (ohne passende Fachwahl) werden wie normale Zuordnungen entfernt.
	 *
	 * @param schuelerIDs           Die Menge der Schüler-IDs.
	 * @param idKurs                Die Datenbank-ID des Kurses aus dem die Schüler entfernt werden sollen.
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus einem Kurs zu entfernen.
	 */
	public kursSchuelerUpdateEntferneSchuelermengeAusKurs(schuelerIDs: JavaSet<number>, idKurs: number, entferneAuchFixierte: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		const setSchulerOfKurs: JavaSet<number> = this.getOfKursSchuelerIDmenge(idKurs);
		for (const idSchueler of schuelerIDs) {
			this.kursSchuelerUpdateEntferneSchuelermengeAusKursVerarbeite(u, idSchueler, idKurs, setSchulerOfKurs, entferneAuchFixierte);
		}
		return u;
	}

	/**
	 * Verarbeitet einen einzelnen Schüler der Methode {@link #kursSchuelerUpdateEntferneSchuelermengeAusKurs}.
	 * <br>(1) Wenn der Schüler dem Kurs zugeordnet ist und nicht fixiert ist, wird er entfernt.
	 * <br>(2) Wenn der Schüler dem Kurs zugeordnet ist und fixiert ist, wird er entfernt, falls entferneAuchFixierte==TRUE ist. Auch die Fixierungs-Regel wird entfernt.
	 *
	 * @param u                    das Update, in das die Veränderungen eingetragen werden
	 * @param idSchueler           die Datenbank-ID des Schülers
	 * @param idKurs               die Datenbank-ID des Kurses aus dem die Schüler entfernt werden sollen
	 * @param setSchulerOfKurs     die Menge der Schüler-IDs des Kurses
	 * @param entferneAuchFixierte  Falls TRUE, werden auch fixiert SuS entfernt.
	 */
	private kursSchuelerUpdateEntferneSchuelermengeAusKursVerarbeite(u: GostBlockungsergebnisKursSchuelerZuordnungUpdate, idSchueler: number, idKurs: number, setSchulerOfKurs: JavaSet<number>, entferneAuchFixierte: boolean): void {
		if (!setSchulerOfKurs.contains(idSchueler)) {
			return;
		}
		const keyFixiert: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		const regelFixiert: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keyFixiert);
		if (regelFixiert === null) {
			u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			return;
		}
		if (entferneAuchFixierte) {
			u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idKurs, idSchueler));
			u.regelUpdates.listEntfernen.add(regelFixiert);
		}
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus allen Kursen entfernen.
	 * <br>Hinweis: Es werden keine Kurs-Fixierungs-Regeln entfernt. Dafür muss die zugehörige regelupdate-Methode aufgerufen werden.
	 *
	 * @param schuelerIDs  Die Menge der Schüler-IDs.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um eine Schülermenge aus allen Kursen entfernen.
	 */
	public kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(schuelerIDs: JavaSet<number>): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const idSchueler of schuelerIDs) {
			for (const kurs of this.getOfSchuelerKursmenge(idSchueler)) {
				u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs.id, idSchueler));
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 * <br>(1) Wenn der Schüler nicht im Kurs ist, wird er hinzugefügt.
	 * <br>(2) Wenn der Schüler in einem Nachbar-Kurs ist, wird er entfernt.
	 *
	 * <br>Hinweis: Wenn der Schüler den Kurs gar nicht gewählt hat, wird dies trotzdem erlaubt. Denn bei einer Umwahl, kann eine Kurszuordnung ungültig sein!
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen.
	 */
	public kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(kursSchuelerZuordnungen: JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			const kurs1: GostBlockungKurs = this.parent.kursGet(z.idKurs);
			if (!this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs)) {
				u.listHinzuzufuegen.add(z);
			}
			for (const kurs2 of this.parent.kursGetListeByFachUndKursart(kurs1.fach_id, kurs1.kursart)) {
				if ((kurs1.id !== kurs2.id) && (this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, kurs2.id))) {
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs2.id, z.idSchueler));
				}
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 * <br>(1) Wenn der Schüler den Ziel-Kurs nicht wählen darf (falsche Fachwahlen), dann passiert nichts.
	 * <br>(2) Wenn der Schüler aus einem fixierten Kurs verschoben werden soll, dies aber nicht erlaubt ist, dann passiert nichts.
	 * <br>(3) Der Schüler ggf. aus einem alten Kurs entfernt und die Fixier-Regel des alten Kurses wird ggf. entfernt.
	 * <br>(4) Der Schüler wird einem neuen Kurs hinzugefügt und wird ggf. im neuen Kurs fixiert.
	 *
	 * @param kursSchuelerZuordnungen           Alle Kurs-Schüler-Paare, welche hinzugefügt werden sollen.
	 * @param verschiebeFixierteDesQuellkurses  TRUE, dann werden fixierte SuS aus potentiell alten Kursen entfernt.
	 * @param fixiereImZielkurs                 TRUE, dann werden die SuS im Zielkurs fixiert.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 */
	private kursSchuelerUpdate03aVerschiebeSchuelerZuKursen(kursSchuelerZuordnungen: JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>, verschiebeFixierteDesQuellkurses: boolean, fixiereImZielkurs: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			this.kursSchuelerUpdate03aVerschiebeSchuelerZuKursenVerarbeite(u, z, verschiebeFixierteDesQuellkurses, fixiereImZielkurs);
		}
		return u;
	}

	/**
	 * Verarbeitet eine einzelne Kurs-Schüler-Zuordnung der Methode {@link #kursSchuelerUpdate03aVerschiebeSchuelerZuKursen}.
	 * <br>(1) Wenn der Schüler den Ziel-Kurs nicht wählen darf (falsche Fachwahlen), dann passiert nichts.
	 * <br>(2) Wenn der Schüler aus einem fixierten Kurs verschoben werden soll, dies aber nicht erlaubt ist, dann passiert nichts.
	 * <br>(3) Der Schüler ggf. aus einem alten Kurs entfernt und die Fixier-Regel des alten Kurses wird ggf. entfernt.
	 * <br>(4) Der Schüler wird einem neuen Kurs hinzugefügt und wird ggf. im neuen Kurs fixiert.
	 *
	 * @param u                                 das Update, in das die Veränderungen eingetragen werden
	 * @param z                                 die zu verarbeitende Kurs-Schüler-Zuordnung
	 * @param verschiebeFixierteDesQuellkurses  TRUE, dann werden fixierte SuS aus potentiell alten Kursen entfernt.
	 * @param fixiereImZielkurs                 TRUE, dann werden die SuS im Zielkurs fixiert.
	 */
	private kursSchuelerUpdate03aVerschiebeSchuelerZuKursenVerarbeite(u: GostBlockungsergebnisKursSchuelerZuordnungUpdate, z: GostBlockungsergebnisKursSchuelerZuordnung, verschiebeFixierteDesQuellkurses: boolean, fixiereImZielkurs: boolean): void {
		const kursNeu: GostBlockungKurs = this.parent.kursGet(z.idKurs);
		if (!this.getOfSchuelerHatFachwahl(z.idSchueler, kursNeu.fach_id, kursNeu.kursart)) {
			return;
		}
		const kursAlt: GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(z.idSchueler, kursNeu.fach_id);
		if (kursAlt !== null) {
			const keyFixiertAlt: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, kursAlt.id]);
			const regelFixiertAlt: GostBlockungRegel | null = this.parent.regelGetByLongArrayKeyOrNull(keyFixiertAlt);
			if ((regelFixiertAlt !== null) && (!verschiebeFixierteDesQuellkurses)) {
				return;
			}
			u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursAlt.id, z.idSchueler));
			if (regelFixiertAlt !== null) {
				u.regelUpdates.listEntfernen.add(regelFixiertAlt);
			}
		}
		u.listHinzuzufuegen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursNeu.id, z.idSchueler));
		if (fixiereImZielkurs) {
			u.regelUpdates.listHinzuzufuegen.add(DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, kursNeu.id));
		}
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 * <br>(1) Wenn der Schüler im Kurs ist, wird er entfernt.
	 * <br>(2) Falls es Schüler-Kurs-Fixierungen gibt, werden diese entfernt.
	 *
	 * @param kursSchuelerZuordnungen  Alle Kurs-Schüler-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler aus Kursen zu entfernen.
	 */
	public kursSchuelerUpdateEntferneKursSchuelerPaare(kursSchuelerZuordnungen: JavaSet<GostBlockungsergebnisKursSchuelerZuordnung>): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		for (const z of kursSchuelerZuordnungen) {
			if (this.getOfSchuelerOfKursIstZugeordnet(z.idSchueler, z.idKurs)) {
				u.listEntfernen.add(z);
			}
			this.regelupdateEntferneFallsVorhanden(u.regelUpdates, [GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, z.idSchueler, z.idKurs]);
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler-Kerngruppe auf mehrere Kurse zu verteilen.
	 * <br>(1) Die SuS werden entsprechende der Methode {@link #kursSchuelerUpdate03aVerschiebeSchuelerZuKursen} verschoben.
	 * <br>(2) Falls "zielKurseLeeren", werden alle SuS aus dem Ziel-Kurs entfernt, die nicht zur Kerngruppe gehören.
	 *
	 * @param idQuellKurs                       Der Quell-Kurs definiert die Kerngruppe.
	 * @param idZielKurse                       Alle Ziel-Kurs-Schülermengen werden der Kerngruppe angeglichen.
	 * @param verschiebeFixierteDesQuellkurses  Falls TRUE, dann werden Fixierungen im Quell-Kurs gelöst, andernfalls wird der Schüler nicht verschoben.
	 * @param inZielKursenFixieren              Falls TRUE, wird nach der Verschiebung der Schüler im Ziel-Kurs fixiert.
	 * @param zielKurseLeeren                   Falls TRUE, werden alle SuS aus dem Ziel-Kurs entfernt, die nicht zur Kerngruppe gehören.
	 *
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchuelerZuordnungUpdate}-Objekt, um Schüler auf Kurse zu verteilen mit Nebenbedingungen.
	 */
	public kursSchuelerUpdateBildeKerngruppen(idQuellKurs: number, idZielKurse: JavaSet<number>, verschiebeFixierteDesQuellkurses: boolean, inZielKursenFixieren: boolean, zielKurseLeeren: boolean): GostBlockungsergebnisKursSchuelerZuordnungUpdate {
		const fachartSet: JavaSet<number> = new HashSet<number>();
		for (const idZielKurs of idZielKurse) {
			const kurs: GostBlockungsergebnisKurs = this.getKursE(idZielKurs);
			const fachartID: number = GostKursart.getFachartID(kurs.fachID, kurs.kursart);
			if (!fachartSet.add(fachartID)) {
				const sKursQuelle: string = this.parent.toStringKursSimple(idQuellKurs);
				const sFachartZiel: string = this.parent.toStringFachartSimpleByFachartID(fachartID);
				throw new UserNotificationException("Die Kerngruppe des Kurses " + sKursQuelle + " kann nicht auf zwei Kurse der Fachart " + sFachartZiel + " verteilt werden!");
			}
		}
		const idSchuelerKerngruppe: JavaSet<number> = this.getOfKursSchuelerIDmenge(idQuellKurs);
		const kursSchuelerZuordnungen: JavaSet<GostBlockungsergebnisKursSchuelerZuordnung> = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const idZielKurs of idZielKurse) {
			for (const idSchueler of idSchuelerKerngruppe) {
				kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idZielKurs, idSchueler));
			}
		}
		const u: GostBlockungsergebnisKursSchuelerZuordnungUpdate = this.kursSchuelerUpdate03aVerschiebeSchuelerZuKursen(kursSchuelerZuordnungen, verschiebeFixierteDesQuellkurses, inZielKursenFixieren);
		if (zielKurseLeeren) {
			for (const idZielKurs of idZielKurse) {
				const menge: JavaSet<number> = new HashSet<number>(this.getOfKursSchuelerIDmenge(idZielKurs));
				menge.removeAll(idSchuelerKerngruppe);
				for (const idSchueler of menge) {
					u.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(idZielKurs, idSchueler));
				}
			}
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
	public kursSchuelerUpdateExecute(update: GostBlockungsergebnisKursSchuelerZuordnungUpdate): void {
		this.parent.regelRemoveListe(update.regelUpdates.listEntfernen);
		for (const z of update.listEntfernen) {
			this.stateSchuelerKursEntfernenOhneRevalidierung(z.idSchueler, z.idKurs);
		}
		for (const z of update.listHinzuzufuegen) {
			this.stateSchuelerKursHinzufuegenOhneRevalidierung(z.idSchueler, z.idKurs);
		}
		this.parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);
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
	public kursSchienenUpdateFuegeKursSchienenPaareHinzu(kursSchienenZuordnungen: JavaSet<GostBlockungsergebnisKursSchienenZuordnung>): GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		for (const z of kursSchienenZuordnungen) {
			if (!this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene)) {
				u.listHinzuzufuegen.add(z);
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 * <br>(1) Wenn der Kurs in der Schiene ist, wird er entfernt.
	 *
	 * @param kursSchienenZuordnungen  Alle Kurs-Schienen-Paare, welche entfernt werden sollen.
	 *
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um Kurse aus Schienen zu entfernen.
	 */
	public kursSchienenUpdateEntferneKursSchienenPaare(kursSchienenZuordnungen: JavaSet<GostBlockungsergebnisKursSchienenZuordnung>): GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		for (const z of kursSchienenZuordnungen) {
			if (this.getOfKursOfSchieneIstZugeordnet(z.idKurs, z.idSchiene)) {
				u.listEntfernen.add(z);
			}
		}
		return u;
	}

	/**
	 * Liefert alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu verschieben.
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
	 * @return alle nötigen Veränderungen als {@link GostBlockungsergebnisKursSchienenZuordnungUpdate}-Objekt, um einen Kurs von einer Schiene zu einer anderen Schiene zu verschieben.
	 */
	public kursSchienenUpdateVerschiebeKursVonSchieneNachSchiene(idKurs: number, idSchieneQuelle: number, idSchieneZiel: number): GostBlockungsergebnisKursSchienenZuordnungUpdate {
		const u: GostBlockungsergebnisKursSchienenZuordnungUpdate = new GostBlockungsergebnisKursSchienenZuordnungUpdate();
		if (!this.getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneQuelle)) {
			return u;
		}
		if (this.getOfKursOfSchieneIstZugeordnet(idKurs, idSchieneZiel)) {
			return u;
		}
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
	public kursSchienenUpdateExecute(update: GostBlockungsergebnisKursSchienenZuordnungUpdate): void {
		this.parent.regelRemoveListe(update.regelUpdates.listEntfernen);
		for (const z of update.listEntfernen) {
			this.stateKursSchieneEntfernenOhneRegelvalidierung(z.idKurs, z.idSchiene);
		}
		for (const z of update.listHinzuzufuegen) {
			this.stateKursSchieneHinzufuegenOhneRegelvalidierung(z.idKurs, z.idSchiene);
		}
		this.parent.regelAddListe(update.regelUpdates.listHinzuzufuegen);
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
	public getSchieneG(idSchiene: number): GostBlockungSchiene {
		return this.parent.schieneGet(idSchiene);
	}

	/**
	 * Liefert das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return das zur ID zugehörige {@link GostBlockungsergebnisSchiene}-Objekt.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	private getSchieneE(idSchiene: number): GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this.schieneByID, idSchiene);
	}

	/**
	 * Liefert TRUE, falls die Schiene keine Kurse enthält.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene keine Kurse enthält.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public getOfSchieneIstLeer(idSchiene: number): boolean {
		return this.getSchieneE(idSchiene).kurse.isEmpty();
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
	private getSchieneEmitNr(nrSchiene: number): GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this.schieneByNR, nrSchiene);
	}

	/**
	 * Liefert die ID einer Schiene mit einer bestimmten Nummer.
	 *
	 * @param nrSchiene  Die Nummer der Schiene.
	 *
	 * @return die ID einer Schiene mit einer bestimmten Nummer.
	 * @throws DeveloperNotificationException falls eine solche Schiene nicht existiert.
	 */
	public getOfSchieneID(nrSchiene: number): number {
		return DeveloperNotificationException.ifMapGetIsNull(this.schieneByNR, nrSchiene).id;
	}

	/**
	 * Liefert die Menge aller Schienen.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public getMengeAllerSchienen(): List<GostBlockungsergebnisSchiene> {
		return this.ergebnis.schienen;
	}

	/**
	 * Liefert TRUE, falls die E-Schiene existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 * @return TRUE, falls die E-Schiene existiert.
	 */
	public getOfSchieneExists(idSchiene: number): boolean {
		return this.schieneByID.containsKey(idSchiene);
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.<br>
	 * Hinweis: Falls ein Schüler mehrfach in der Schiene ist (Kollision), wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück.
	 */
	public getOfSchieneAnzahlSchueler(idSchiene: number): number {
		return DeveloperNotificationException.ifMapGetIsNull(this.schuelerAnzahlBySchienenID, idSchiene);
	}

	/**
	 * Liefert TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public getOfSchieneHatKollision(idSchiene: number): boolean {
		return this.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene.<br>
	 * Hinweis: Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Schüler-Kollisionen der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene: number): number {
		return DeveloperNotificationException.ifMapGetIsNull(this.kollisionenBySchienenID, idSchiene);
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneSchuelermengeMitKollisionen(idSchiene: number): JavaSet<number> {
		const set: JavaSet<number> = new HashSet<number>();
		for (const schuelerID of this.kollisionenBySchuelerID.keySet()) {
			if (this.getOfSchuelerOfSchieneKursmenge(schuelerID, idSchiene).size() > 1) {
				set.add(schuelerID);
			}
		}
		return set;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneKursmengeMitKollisionen(idSchiene: number): JavaSet<GostBlockungsergebnisKurs> {
		const set: JavaSet<GostBlockungsergebnisKurs> = new HashSet<GostBlockungsergebnisKurs>();
		for (const kurs of this.getSchieneE(idSchiene).kurse) {
			if (this.getOfKursHatKollision(kurs.id)) {
				set.add(kurs);
			}
		}
		return set;
	}

	/**
	 * Liefert die sortierte Menge an Kursen einer bestimmten Schiene.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return die sortierte Menge an Kursen einer bestimmten Schiene.
	 */
	public getOfSchieneKursmengeSortiert(idSchiene: number): List<GostBlockungsergebnisKurs> {
		return this.getSchieneE(idSchiene).kurse;
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
	public getOfSchieneTooltipKurskollisionenAsData(idSchiene: number): List<List<Pair<GostBlockungsergebnisKurs, number>>> {
		const listOfLists: List<List<Pair<GostBlockungsergebnisKurs, number>>> = new ArrayList<List<Pair<GostBlockungsergebnisKurs, number>>>();
		for (const kurs1 of this.getSchieneE(idSchiene).kurse) {
			let summe: number = 0;
			const listOfPairs: List<Pair<GostBlockungsergebnisKurs, number>> = new ArrayList<Pair<GostBlockungsergebnisKurs, number>>();
			for (const kurs2 of this.getSchieneE(idSchiene).kurse) {
				if (kurs2.id !== kurs1.id) {
					const anzahl: number = GostBlockungsergebnisManager.getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1, kurs2);
					if (anzahl > 0) {
						listOfPairs.add(new Pair<GostBlockungsergebnisKurs, number>(kurs2, anzahl));
						summe += anzahl;
					}
				}
			}
			if (summe > 0) {
				const neu: Pair<GostBlockungsergebnisKurs, number> = new Pair<GostBlockungsergebnisKurs, number>(kurs1, summe);
				listOfPairs.add(0, neu);
				listOfLists.add(listOfPairs);
			}
		}
		return listOfLists;
	}

	/**
	 * Liefert alle Kollisionen einer Schiene (pro Element ein Schüler).
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return alle Kollisionen einer Schiene (pro Element ein Schüler).
	 */
	public getOfSchieneTooltipSchuelerkollisionen(idSchiene: number): string {
		const sb: StringBuilder = new StringBuilder();
		let zeilen: number = 0;
		let zeilenIgnoriert: number = 0;
		for (const idSchueler of this.kursmengeBySchuelerIDAndSchienenID.getKeySet()) {
			const set: JavaSet<GostBlockungsergebnisKurs> | null = this.kursmengeBySchuelerIDAndSchienenID.getOrNull(idSchueler, idSchiene);
			if ((set === null) || (set.size() <= 1)) {
				continue;
			}
			const list: ArrayList<GostBlockungsergebnisKurs> = new ArrayList<GostBlockungsergebnisKurs>(set);
			if (zeilen < 10) {
				sb.append(JavaString.format("%s ist in mehreren Kursen:", this.parent.toStringSchuelerSimple(idSchueler)));
				for (let i: number = 0; i < list.size(); i++) {
					sb.append(JavaString.format("%s%s", i === 0 ? "" : ", ", this.parent.toStringKursSimpleOhneID(list.get(i).id)));
				}
				sb.append(this.lineSeparator);
			} else {
				zeilenIgnoriert++;
			}
			zeilen++;
		}
		return sb.toString() + (zeilenIgnoriert === 0 ? "" : "+" + zeilenIgnoriert + " weitere Zeilen.");
	}

	private static getOfKursOfKursAnzahlGemeinsamerSchueler(kurs1: GostBlockungsergebnisKurs, kurs2: GostBlockungsergebnisKurs): number {
		const set: JavaSet<number> = new HashSet<number>();
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
	public getOfSchieneRemoveAllowed(idSchiene: number): boolean {
		return this.getSchieneE(idSchiene).kurse.isEmpty();
	}

	/**
	 * Liefert die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 *
	 * @return die maximale Anzahl an Kursen, die es in einer Schiene gibt.
	 */
	public getOfSchieneMaxKursanzahl(): number {
		let max: number = 0;
		for (const schiene of this.ergebnis.schienen) {
			max = Math.max(max, schiene.kurse.size());
		}
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
	public getOfSchieneAnzahlSchuelerExterne(idSchiene: number): number {
		let summe: number = 0;
		for (const kurs of this.getSchieneE(idSchiene).kurse) {
			for (const idSchueler of kurs.schueler) {
				if (this.getOfSchuelerHatStatusExtern(idSchueler)) {
					summe++;
				}
			}
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Dummy-SuS der Schiene.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Anzahl an Dummy-SuS der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerDummy(idSchiene: number): number {
		let summe: number = 0;
		for (const kurs of this.getSchieneE(idSchiene).kurse) {
			summe += this.getOfKursAnzahlSchuelerDummy(kurs.id);
		}
		return summe;
	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @throws DeveloperNotificationException  falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddSchieneByID(idSchiene: number): void {
		DeveloperNotificationException.ifTrue("Die Schiene " + this.parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager hinzugefügt werden!", !this.parent.schieneGetExistiert(idSchiene));
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
	public setRemoveSchieneByID(idSchiene: number): void {
		DeveloperNotificationException.ifTrue("Die Schiene " + this.parent.toStringSchiene(idSchiene) + " muss erst beim Datenmanager entfernt werden!", this.parent.schieneGetExistiert(idSchiene));
		const nKurse: number = this.getSchieneE(idSchiene).kurse.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Schiene " + this.parent.toStringSchiene(idSchiene) + " hat noch " + nKurse + " Kurse!", nKurse > 0);
		this.ergebnis.schienen.remove(this.getSchieneE(idSchiene));
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public setAddKursByID(idKurs: number): void {
		DeveloperNotificationException.ifTrue("" + this.parent.toStringKurs(idKurs) + " muss erst beim Datenmanager hinzugefügt werden!", !this.parent.kursGetExistiert(idKurs));
		const kurs: GostBlockungKurs = this.parent.kursGet(idKurs);
		const nSchienen: number = this.parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);
		this.stateRevalidateEverything();
		for (let nr: number = 1; nr <= kurs.anzahlSchienen; nr++) {
			const eSchiene: GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
			this.stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, eSchiene.id);
		}
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht alle übergebenen Kurse. Entfernt zuvor potentiell vorhandene Schülerinnen und Schüler aus dem Kurs.
	 *
	 * @param idKurse  Die Liste der Datenbank-IDs der Kurse.
	 *
	 * @throws DeveloperNotificationException  Falls mindestens einer der Kurse nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveKurseByID(idKurse: List<number>): void {
		for (const idKurs of idKurse) {
			DeveloperNotificationException.ifTrue(this.parent.toStringKurs(idKurs) + " muss erst beim Datenmanager entfernt werden!", this.parent.kursGetExistiert(idKurs));
		}
		for (const idKurs of idKurse) {
			const kurs: GostBlockungsergebnisKurs = this.getKursE(idKurs);
			for (const schienenID of kurs.schienen) {
				const i: JavaIterator<GostBlockungsergebnisKurs> = this.getSchieneE(schienenID).kurse.iterator();
				while (i.hasNext()) {
					if (i.next().id === kurs.id) {
						i.remove();
					}
				}
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
	public setMergeKurseByID(idKursID1keep: number, idKursID2delete: number): void {
		const kursDelete: GostBlockungsergebnisKurs = this.getKursE(idKursID2delete);
		const kursKeep: GostBlockungsergebnisKurs = this.getKursE(idKursID1keep);
		kursKeep.schueler.addAll(kursDelete.schueler);
		for (const schienenID of kursDelete.schienen) {
			this.getSchieneE(schienenID).kurse.remove(kursDelete);
		}
		this.parent.kursMerge(idKursID1keep, idKursID2delete);
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
	public setSplitKurs(kurs1alt: GostBlockungKurs, kurs2neu: GostBlockungKurs, susVon1nach2: Array<number>): void {
		this.parent.kursAdd(kurs2neu);
		this.stateRevalidateEverything();
		for (const eSchiene of this.getOfKursSchienenmenge(kurs1alt.id)) {
			this.stateKursSchieneHinzufuegenOhneRegelvalidierung(kurs2neu.id, eSchiene.id);
		}
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
	public patchOfKursSchienenAnzahl(idKurs: number, anzahlSchienenNeu: number): void {
		const kursG: GostBlockungKurs = this.getKursG(idKurs);
		const kursE: GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const nSchienen: number = this.parent.schieneGetAnzahl();
		DeveloperNotificationException.ifTrue("Schienenanzahl von KursE (" + kursE.anzahlSchienen + ") ist ungleich der von KursG (" + kursG.anzahlSchienen + ")!", kursE.anzahlSchienen !== kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Schienenanzahl von " + this.parent.toStringKurs(idKurs) + " darf nur bei der Blockungsvorlage verändert werden!", !this.parent.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue(this.parent.toStringKurs(idKurs) + " hat als GostBlockungKurs " + kursG.anzahlSchienen + " Schienen, als GostBlockungsergebnisKurs hingegen " + kursE.anzahlSchienen + " Schienen!", kursE.anzahlSchienen !== kursG.anzahlSchienen);
		DeveloperNotificationException.ifTrue("Die Blockung hat 0 Schienen. Das darf nicht passieren!", nSchienen === 0);
		DeveloperNotificationException.ifTrue(this.parent.toStringKurs(idKurs) + " muss mindestens einer Schiene zugeordnet sein, statt " + anzahlSchienenNeu + " Schienen!", anzahlSchienenNeu <= 0);
		DeveloperNotificationException.ifTrue("Es gibt nur " + nSchienen + " Schienen, somit kann " + this.parent.toStringKurs(idKurs) + " nicht " + anzahlSchienenNeu + " Schienen zugeordnet werden!", anzahlSchienenNeu > nSchienen);
		while (anzahlSchienenNeu > kursG.anzahlSchienen) {
			let hinzugefuegt: boolean = false;
			for (let nr: number = 1; (nr <= this.schieneByNR.size()) && (!hinzugefuegt); nr++) {
				const schiene: GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					this.stateKursSchieneHinzufuegenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine freie Schiene für " + this.parent.toStringKurs(idKurs) + " gefunden!", !hinzugefuegt);
		}
		while (anzahlSchienenNeu < kursG.anzahlSchienen) {
			let entfernt: boolean = false;
			for (let nr: number = this.schieneByNR.size(); (nr >= 1) && (!entfernt); nr--) {
				const schiene: GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					this.stateKursSchieneEntfernenOhneRegelvalidierung(idKurs, schiene.id);
				}
			}
			DeveloperNotificationException.ifTrue("Es wurde keine belegte Schiene von " + this.parent.toStringKurs(idKurs) + " gefunden!", !entfernt);
		}
		this.stateRevalidateEverything();
	}

	/**
	 * Informiert den Manager, dass sich bei mindestens einem Kurs die Lehrkraft geändert hat.
	 * Führt zu einer Revalidierung der Bewertung des Ergebnisses.
	 */
	public patchOfKursLehrkaefteChanged(): void {
		this.stateRevalidateEverything();
	}

	private createComparatorFachartKursartFach(): Comparator<number> {
		const comp: Comparator<number> = { compare: (a: number, b: number) => {
			const aKursartID: number = GostKursart.getKursartID(a);
			const bKursartID: number = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID) {
				return -1;
			}
			if (aKursartID > bKursartID) {
				return +1;
			}
			const aFachID: number = GostKursart.getFachID(a);
			const bFachID: number = GostKursart.getFachID(b);
			const aFach: GostFach | null = this.parent.faecherManager().get(aFachID);
			const bFach: GostFach | null = this.parent.faecherManager().get(bFachID);
			return GostFaecherManager.comp.compare(aFach, bFach);
		} };
		return comp;
	}

	private createComparatorFachartFachKursart(): Comparator<number> {
		const comp: Comparator<number> = { compare: (a: number, b: number) => {
			const aFachID: number = GostKursart.getFachID(a);
			const bFachID: number = GostKursart.getFachID(b);
			const aFach: GostFach | null = this.parent.faecherManager().get(aFachID);
			const bFach: GostFach | null = this.parent.faecherManager().get(bFachID);
			const cmpFach: number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			const aKursartID: number = GostKursart.getKursartID(a);
			const bKursartID: number = GostKursart.getKursartID(b);
			if (aKursartID < bKursartID) {
				return -1;
			}
			if (aKursartID > bKursartID) {
				return +1;
			}
			return 0;
		} };
		return comp;
	}

	private createComparatorKursFachKursartNummer(): Comparator<GostBlockungsergebnisKurs> {
		const comp: Comparator<GostBlockungsergebnisKurs> = { compare: (a: GostBlockungsergebnisKurs, b: GostBlockungsergebnisKurs) => {
			const aFach: GostFach | null = this.parent.faecherManager().get(a.fachID);
			const bFach: GostFach | null = this.parent.faecherManager().get(b.fachID);
			const cmpFach: number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			if (a.kursart < b.kursart) {
				return -1;
			}
			if (a.kursart > b.kursart) {
				return +1;
			}
			const aKurs: GostBlockungKurs = this.parent.kursGet(a.id);
			const bKurs: GostBlockungKurs = this.parent.kursGet(b.id);
			return JavaInteger.compare(aKurs.nummer, bKurs.nummer);
		} };
		return comp;
	}

	private createComparatorKursKursartFachNummer(): Comparator<GostBlockungsergebnisKurs> {
		const comp: Comparator<GostBlockungsergebnisKurs> = { compare: (a: GostBlockungsergebnisKurs, b: GostBlockungsergebnisKurs) => {
			if (a.kursart < b.kursart) {
				return -1;
			}
			if (a.kursart > b.kursart) {
				return +1;
			}
			const aFach: GostFach | null = this.parent.faecherManager().get(a.fachID);
			const bFach: GostFach | null = this.parent.faecherManager().get(b.fachID);
			const cmpFach: number = GostFaecherManager.comp.compare(aFach, bFach);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			const aKurs: GostBlockungKurs = this.parent.kursGet(a.id);
			const bKurs: GostBlockungKurs = this.parent.kursGet(b.id);
			return JavaInteger.compare(aKurs.nummer, bKurs.nummer);
		} };
		return comp;
	}

	/**
	 * Eine Logger-Ausgabe für Debug-Zwecke.
	 *
	 * @param logger Ein Logger für Debug-Zwecke.
	 */
	public debug(logger: Logger): void {
		logger.modifyIndent(+4);
		logger.logLn("----- Kurse sortiert nach Fachart -----");
		for (const fachartID of this.kursmengeByFachartID.keySet()) {
			logger.logLn("FachartID = " + fachartID + " (KD = " + this.getOfFachartKursdifferenz(fachartID) + ")");
			for (const kurs of this.getOfFachartKursmenge(fachartID)) {
				logger.logLn("    " + this.getOfKursName(kurs.id) + " : " + kurs.schueler.size() + " SuS");
			}
		}
		logger.logLn("KursdifferenzMax = " + this.ergebnis.bewertung.kursdifferenzMax);
		logger.logLn("KursdifferenzHistogramm = " + Arrays.toString(this.ergebnis.bewertung.kursdifferenzHistogramm));
		logger.modifyIndent(-4);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

	public static readonly class = new Class<GostBlockungsergebnisManager>('de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager');

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisManager(obj: unknown): GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}

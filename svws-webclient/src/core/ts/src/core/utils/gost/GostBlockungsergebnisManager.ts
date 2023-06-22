import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { Logger } from '../../../core/logger/Logger';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { SchuelerblockungInput } from '../../../core/data/kursblockung/SchuelerblockungInput';
import type { List } from '../../../java/util/List';
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
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
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
	 * Schienen-ID --> Fachart-ID --> ArrayList<Kurse> = Alle Kurse in der Schiene mit der Fachart.
	 */
	private readonly _map_schienenID_fachartID_kurse : JavaMap<number, JavaMap<number, List<GostBlockungsergebnisKurs>>> = new HashMap();

	/**
	 * Schüler-ID --> Set<GostBlockungsergebnisKurs>
	 */
	private readonly _map_schuelerID_kurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Schüler-ID --> Set<GostBlockungsergebnisKurs>
	 */
	private readonly _map_schuelerID_ungueltige_kurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Schüler-ID --> Integer (Kollisionen)
	 */
	private readonly _map_schuelerID_kollisionen : JavaMap<number, number> = new HashMap();

	/**
	 * Schüler-ID --> Fach-ID --> GostBlockungsergebnisKurs
	 */
	private readonly _map_schuelerID_fachID_kurs : JavaMap<number, JavaMap<number, GostBlockungsergebnisKurs | null>> = new HashMap();

	/**
	 * Schüler-ID --> Schienen-ID --> Set<GostBlockungsergebnisKurs> = Alle Kurse des Schülers in der Schiene.
	 */
	private readonly _map_schuelerID_schienenID_kurse : JavaMap<number, JavaMap<number, JavaSet<GostBlockungsergebnisKurs>>> = new HashMap();

	/**
	 * Kurs-ID --> Set<GostBlockungsergebnisSchiene>
	 */
	private readonly _map_kursID_schienen : JavaMap<number, JavaSet<GostBlockungsergebnisSchiene>> = new HashMap();

	/**
	 * Kurs-ID --> GostBlockungsergebnisKurs
	 */
	private readonly _map_kursID_kurs : JavaMap<number, GostBlockungsergebnisKurs> = new HashMap();

	/**
	 * Kurs-ID --> Set<SchuelerID>
	 */
	private readonly _map_kursID_schuelerIDs : JavaMap<number, JavaSet<number>> = new HashMap();

	/**
	 * Fach-ID --> ArrayList<GostBlockungsergebnisKurs>
	 */
	private readonly _map_fachID_kurse : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Fachart-ID --> ArrayList<GostBlockungsergebnisKurs> = Alle Kurse der selben Fachart.
	 */
	private readonly _map_fachartID_kurse : JavaMap<number, List<GostBlockungsergebnisKurs>> = new HashMap();

	/**
	 * Fachart-ID --> Integer = Kursdifferenz der Fachart.
	 */
	private readonly _map_fachartID_kursdifferenz : JavaMap<number, number> = new HashMap();


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
			this.stateClear(new GostBlockungsergebnis(), pGostBlockungsergebnisID);
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager')))) && ((typeof __param1 !== "undefined") && ((__param1 instanceof JavaObject) && ((__param1 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnis'))))) {
			const pParent : GostBlockungsdatenManager = cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(__param0);
			const pErgebnis : GostBlockungsergebnis = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(__param1);
			this._parent = pParent;
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
		this._map_schienenNr_schiene.clear();
		this._map_schienenID_schiene.clear();
		this._map_schienenID_schuelerAnzahl.clear();
		this._map_schienenID_kollisionen.clear();
		this._map_schienenID_fachartID_kurse.clear();
		this._map_schuelerID_kurse.clear();
		this._map_schuelerID_ungueltige_kurse.clear();
		this._map_schuelerID_kollisionen.clear();
		this._map_schuelerID_fachID_kurs.clear();
		this._map_schuelerID_schienenID_kurse.clear();
		this._map_kursID_schienen.clear();
		this._map_kursID_kurs.clear();
		this._map_kursID_schuelerIDs.clear();
		this._map_fachID_kurse.clear();
		this._map_fachartID_kurse.clear();
		this._map_fachartID_kursdifferenz.clear();
		this._ergebnis = new GostBlockungsergebnis();
		this._ergebnis.id = pGostBlockungsergebnisID;
		this._ergebnis.blockungID = this._parent.getID();
		this._ergebnis.name = pOld.name;
		this._ergebnis.gostHalbjahr = this._parent.daten().gostHalbjahr;
		this._ergebnis.istMarkiert = pOld.istMarkiert;
		this._ergebnis.istVorlage = pOld.istVorlage;
		this._ergebnis.bewertung.kursdifferenzMax = 0;
		this._ergebnis.bewertung.kursdifferenzHistogramm = Array(this._parent.getSchuelerAnzahl() + 1).fill(0);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet += this._parent.daten().fachwahlen.size();
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
		for (const gSchiene of this._parent.daten().schienen) {
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schienenID_fachartID_kurse, gSchiene.id, new HashMap());
			for (const fachartID of this._map_fachartID_kursdifferenz.keySet())
				this.getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new ArrayList());
		}
		for (const gSchueler of this._parent.daten().schueler) {
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kurse, gSchueler.id, new HashSet<GostBlockungsergebnisKurs>());
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kollisionen, gSchueler.id, 0);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_fachID_kurs, gSchueler.id, new HashMap());
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_schienenID_kurse, gSchueler.id, new HashMap());
		}
		const strErrorDoppeltesFach : string | null = "Schüler %d hat Fach %d doppelt!";
		for (const gFachwahl of this._parent.daten().fachwahlen) {
			const mapFachKurs : JavaMap<number, GostBlockungsergebnisKurs | null> = DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_fachID_kurs, gFachwahl.schuelerID);
			if (mapFachKurs.put(gFachwahl.fachID, null) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppeltesFach, gFachwahl.schuelerID, gFachwahl.fachID))
		}
		for (const gSchueler of this._parent.daten().schueler)
			for (const gSchiene of this._parent.daten().schienen) {
				const newSet : HashSet<GostBlockungsergebnisKurs> | null = new HashSet();
				this.getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, newSet);
			}
		const kursBearbeitet : HashSet<number> | null = new HashSet();
		for (const schieneOld of pOld.schienen)
			for (const kursOld of schieneOld.kurse) {
				this.setKursSchiene(kursOld.id, schieneOld.id, true);
				if (kursBearbeitet.add(kursOld.id))
					for (const schuelerID of kursOld.schueler)
						this.setSchuelerKurs(schuelerID!, kursOld.id, true);
			}
		this.stateRegelvalidierung();
	}

	private stateRegelvalidierung() : void {
		const regelVerletzungen : List<number> = this._ergebnis.bewertung.regelVerletzungen;
		regelVerletzungen.clear();
		for (const r of this._parent.getMengeOfRegeln()) {
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			switch (typ) {
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					this.stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE: {
					this.stateRegelvalidierung2_kurs_fixieren_in_schiene(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
					this.stateRegelvalidierung3_kurs_sperren_in_schiene(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS: {
					this.stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					this.stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
					this.stateRegelvalidierung6_kursart_allein_in_schiene_von_bis(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS: {
					this.stateRegelvalidierung7_kurs_verbieten_mit_kurs(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
					this.stateRegelvalidierung8_kurs_zusammen_mit_kurs(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN: {
					this.stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
					this.stateRegelvalidierung10_lehrkraefte_beachten(r, regelVerletzungen);
					break;
				}
				default: {
					throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ)
				}
			}
		}
		this._parent.updateErgebnisBewertung(this._ergebnis);
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

	private stateRegelvalidierung9_kurs_mit_dummy_sus_auffuellen(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const anzahl : number = r.parameter.get(0).valueOf();
		DeveloperNotificationException.ifTrue("Regel 9 Dummy SuS " + anzahl + " ungültig!", (anzahl < 0) || (anzahl > 1000));
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
		const mapSFK : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(idSchueler);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.add(idSchueler);
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(idSchueler);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
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
		const mapSFK : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(idSchueler);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.remove(idSchueler);
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(idSchueler);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
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
		const kursGruppe : List<GostBlockungsergebnisKurs> = this.getOfSchieneOfFachartKursmenge(idSchiene, idFachart);
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
		const kursGruppe : List<GostBlockungsergebnisKurs> = this.getOfSchieneOfFachartKursmenge(idSchiene, idFachart);
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
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene);
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
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(idSchueler, idSchiene);
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
		let min : number = kurs1.schueler.size();
		let max : number = min;
		for (const kurs of kursmenge) {
			const size : number = kurs.schueler.size();
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
	 * Liefert das Blockungsergebnis.
	 *
	 * @return das Blockungsergebnis.
	 */
	public getErgebnis() : GostBlockungsergebnis {
		return this._ergebnis;
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
	 * Ermittelt das {@link GostFach} für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param idFach Die Datenbank-ID des Faches.
	 *
	 * @return Das GostFach-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
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
	 * Liefert die Kursmenge, die zur Fachart gehört.<br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursmenge(idFachart : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kurse, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart.<br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursdifferenz der Fachart.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursdifferenz(idFachart : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kursdifferenz, idFachart)!;
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
		return this._parent.getSchueler(idSchueler);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public getOfSchuelerNameVorname(idSchueler : number) : string {
		const schueler : Schueler = this._parent.getSchueler(idSchueler);
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
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public getOfSchuelerKursmengeMitKollisionen(idSchueler : number) : JavaSet<GostBlockungsergebnisKurs> {
		const mapSchieneKurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = this.getOfSchuelerSchienenKursmengeMap(idSchueler);
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const kurseDerSchiene of mapSchieneKurse.values())
			if (kurseDerSchiene.size() > 1)
				set.addAll(kurseDerSchiene);
		return set;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat. <br>
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public getOfSchuelerHatNichtwahl(idSchueler : number) : boolean {
		const map : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(idSchueler);
		for (const e of map.entrySet())
			if (e.getValue() === null)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 */
	public getOfSchuelerHatFachwahl(idSchueler : number, idFach : number, idKursart : number) : boolean {
		return this._parent.getOfSchuelerHatFachart(idSchueler, idFach, idKursart);
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
		return this.getOfSchuelerAnzahlKollisionen(idSchueler) > 0;
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
	 * Liefert TRUE, falls alle Schüler-Fachwahlen noch nicht zugeordnet sind.
	 *
	 * @return TRUE, falls alle Schüler-Fachwahlen noch nicht zugeordnet sind.
	 */
	public getOfSchuelerAlleFachwahlenNichtZugeordnet() : boolean {
		return this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet === this._parent.daten().fachwahlen.size();
	}

	/**
	 * Liefert die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public getOfSchuelerFachIDKursMap(idSchueler : number) : JavaMap<number, GostBlockungsergebnisKurs | null> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_fachID_kurs, idSchueler);
	}

	/**
	 * Liefert die Map des Schülers, die pro Schiene die belegten Kurse des Schülers zuordnet.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return die Map des Schülers, die pro Schiene die belegten Kurse des Schülers zuordnet.
	 */
	public getOfSchuelerSchienenKursmengeMap(idSchueler : number) : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_schienenID_kurse, idSchueler);
	}

	/**
	 * Liefert die Menge der belegten Kurse des Schülers in der Schiene.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Menge der belegten Kurse des Schülers in der Schiene.
	 */
	public getOfSchuelerOfSchieneKursmenge(idSchueler : number, idSchiene : number) : JavaSet<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.getOfSchuelerSchienenKursmengeMap(idSchueler), idSchiene);
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
		return this._parent.getOfSchuelerOfFachKursart(idSchueler, idFach);
	}

	/**
	 * Liefert den zu (idSchueler, idFach) passenden Kurs.
	 *
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 * @param  idFach     Die Datenbank-ID des Faches.
	 *
	 * @return den zu (idSchueler, idFach) passenden Kurs.
	 */
	public getOfSchuelerOfFachZugeordneterKurs(idSchueler : number, idFach : number) : GostBlockungsergebnisKurs | null {
		const mapFachZuKurs : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(idSchueler);
		DeveloperNotificationException.ifMapNotContains("mapFachZuKurs", mapFachZuKurs, idFach);
		return mapFachZuKurs.get(idFach);
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
	 * @param  idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return ein {@link SchuelerblockungOutput}-Objekt, welches für den Schüler eine Neuzuordnung der Kurse vorschlägt.
	 */
	public getOfSchuelerNeuzuordnung(idSchueler : number) : SchuelerblockungOutput {
		const input : SchuelerblockungInput = new SchuelerblockungInput();
		input.schienen = this.getOfSchieneAnzahl();
		const fachwahlenDesSchuelers : List<GostFachwahl> = this._parent.getOfSchuelerFacharten(idSchueler);
		input.fachwahlen.addAll(fachwahlenDesSchuelers);
		for (const fachwahl of fachwahlenDesSchuelers) {
			const representation : string = this._parent.getNameOfFachwahl(fachwahl);
			input.fachwahlenText.add(representation);
		}
		for (const fachwahl of fachwahlenDesSchuelers) {
			const fachartID : number = GostKursart.getFachartIDByFachwahl(fachwahl);
			const kurse : List<GostBlockungsergebnisKurs> = this.getOfFachartKursmenge(fachartID);
			for (const kurs of kurse) {
				const inKurs : SchuelerblockungInputKurs = new SchuelerblockungInputKurs();
				inKurs.id = kurs.id;
				inKurs.fach = kurs.fachID;
				inKurs.kursart = kurs.kursart;
				inKurs.istGesperrt = this.getOfSchuelerOfKursIstGesperrt(idSchueler, kurs.id);
				inKurs.istFixiert = this.getOfSchuelerOfKursIstFixiert(idSchueler, kurs.id);
				inKurs.anzahlSuS = this.getOfKursAnzahlSchueler(kurs.id);
				inKurs.schienen = this.getOfKursSchienenNummern(kurs.id);
				input.kurse.add(inKurs);
			}
		}
		if (input.kurse.isEmpty())
			return new SchuelerblockungOutput();
		const algorithmus : SchuelerblockungAlgorithmus = new SchuelerblockungAlgorithmus();
		const output : SchuelerblockungOutput = algorithmus.handle(input);
		return output;
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
		input.schienen = this._parent.getSchienenAnzahl();
		for (const fachwahl of this._parent.getOfSchuelerFacharten(idSchueler)) {
			input.fachwahlen.add(fachwahl);
			input.fachwahlenText.add(this._parent.getNameOfFachwahl(fachwahl));
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
		for (const r of this._parent.getMengeOfRegeln()) {
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			if (typ as unknown === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS as unknown) {
				const schuelerID : number = r.parameter.get(0).valueOf();
				const kursID : number = r.parameter.get(1).valueOf();
				if ((schuelerID === idSchueler) && (kursID === idKurs))
					return true;
			}
		}
		return false;
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
		for (const r of this._parent.getMengeOfRegeln()) {
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			if (typ as unknown === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS as unknown) {
				const schuelerID : number = r.parameter.get(0).valueOf();
				const kursID : number = r.parameter.get(1).valueOf();
				if ((schuelerID === idSchueler) && (kursID === idKurs))
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
	public getOfSchuelerHatImNamenSubstring(idSchueler : number, subString : string) : boolean {
		const schueler : Schueler = this.getSchuelerG(idSchueler);
		const text : string = subString.toLowerCase();
		return JavaString.contains(schueler.nachname.toLowerCase(), text) || JavaString.contains(schueler.vorname.toLowerCase(), text);
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
		return this._parent.getKurs(idKurs);
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
		return this._parent.getNameOfKurs(idKurs);
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
			a[i] = this._parent.getSchiene(schienenID).nummer;
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
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public getOfKursAnzahlKollisionen(idKurs : number) : number {
		return this.getOfKursSchuelermengeMitKollisionen(idKurs).size();
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 *
	 * @param  idKursID  Die Datenbank-ID des Kurses.
	 *
	 * @return die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public getOfKursSchuelermengeMitKollisionen(idKursID : number) : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schiene of this.getOfKursSchienenmenge(idKursID))
			for (const schuelerID of this.getKursE(idKursID).schueler)
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, schiene.id).size() > 1)
					set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind.
	 */
	public getOfKursAnzahlSchueler(idKurs : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		return kursE.schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 */
	public getOfKursAnzahlSchuelerSchriftlich(idKurs : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		const idFach : number = kursE.fachID;
		let summe : number = 0;
		for (const idSchueler of kursE.schueler) {
			const fachwahl : GostFachwahl = this._parent.getOfSchuelerOfFachFachwahl(idSchueler!, idFach);
			DeveloperNotificationException.ifTrue("fachwahl.schuelerID != idSchueler", fachwahl.schuelerID !== idSchueler);
			DeveloperNotificationException.ifTrue("fachwahl.kursartID != kursE.kursart", fachwahl.kursartID !== kursE.kursart);
			DeveloperNotificationException.ifTrue("fachwahl.fachID != idFach", fachwahl.fachID !== idFach);
			if (fachwahl.istSchriftlich)
				summe++;
		}
		return summe;
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
		return this._parent.getSchiene(idSchiene);
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
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneAnzahlKursmengeMitKollisionen(pSchienenID : number) : number {
		let summe : number = 0;
		for (const kurs of this.getSchieneE(pSchienenID).kurse)
			if (this.getOfKursHatKollision(kurs.id))
				summe++;
		return summe;
	}

	/**
	 * Liefert die Menge an Kursen, die in der Schiene eine Kollision haben.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Kursen, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneKursmengeMitKollisionen(pSchienenID : number) : JavaSet<GostBlockungsergebnisKurs> {
		const set : HashSet<GostBlockungsergebnisKurs> = new HashSet();
		for (const kurs of this.getSchieneE(pSchienenID).kurse)
			if (this.getOfKursHatKollision(kurs.id))
				set.add(kurs);
		return set;
	}

	/**
	 * Liefert eine "FachartID --> Kurse" Map der Schiene.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Eine "FachartID --> Kurse" Map der Schiene.
	 */
	public getOfSchieneFachartKursmengeMap(pSchienenID : number) : JavaMap<number, List<GostBlockungsergebnisKurs>> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_fachartID_kurse, pSchienenID);
	}

	/**
	 * Liefert alle Kurse, die in einer bestimmten Schiene eine bestimmte Fachart haben.
	 *
	 * @param idSchienen Die Datenbank-ID der Schiene.
	 * @param idFachart  Die ID der Fachart.
	 *
	 * @return alle Kurse, die in einer bestimmten Schiene eine bestimmte Fachart haben.
	 */
	public getOfSchieneOfFachartKursmenge(idSchienen : number, idFachart : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.getOfSchieneFachartKursmengeMap(idSchienen), idFachart);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist. <br>
	 * Kriterium: Es dürfen keine Kurse der Schiene zugeordnet sein.
	 *
	 * @param  pSchienenID          Die Datenbank-ID der Schiene.
	 * @return                      TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert.
	 */
	public getOfSchieneRemoveAllowed(pSchienenID : number) : boolean {
		return this.getSchieneE(pSchienenID).kurse.isEmpty();
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
	 * Liefert die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet. <br>
	 * Hinweis 1: Hat ein Schüler keine ungültige Kurse, dann gibt es die ID nicht. <br>
	 * Hinweis 2: Gibt es keine ungültigen Wahlen, so ist die Map leer. <br>
	 *
	 * @return Die Map, welche einer Schüler-ID die Menge aller ungültigen Kurse zuordnet.
	 */
	public getMappingSchuelerIDzuUngueltigeKurse() : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		return this._map_schuelerID_ungueltige_kurse;
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
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision.
	 */
	public getMengeDerSchuelerMitKollisionen() : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schuelerID of this._map_schuelerID_kollisionen.keySet())
			if (this.getOfSchuelerHatKollision(schuelerID!))
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert eine Menge aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Eine Menge aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public getMengeDerSchuelerMitKollisionenOderNichtwahlen() : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schuelerID of this._map_schuelerID_kollisionen.keySet())
			if (this.getOfSchuelerHatKollision(schuelerID!) || this.getOfSchuelerHatNichtwahl(schuelerID!))
				set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert eine gefilterte Menge aller Schüler.
	 *
	 * @param  idKurs      Falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  idFach      Falls > 0 und
	 * @param  idKursart   falls > 0, werden Schüler mit dieser Fachwahl herausgefiltert.
	 * @param  konfliktTyp Falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  subString   Falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine gefilterte Menge aller Schüler.
	 */
	public getMengeDerSchuelerGefiltert(idKurs : number, idFach : number, idKursart : number, konfliktTyp : number, subString : string) : List<Schueler> {
		const menge : List<Schueler> = new ArrayList();
		for (const schueler of this._parent.getMengeOfSchueler()) {
			const idSchueler : number = schueler.id;
			if ((konfliktTyp === 1) && (!this.getOfSchuelerHatKollision(idSchueler)))
				continue;
			if ((konfliktTyp === 2) && (!this.getOfSchuelerHatNichtwahl(idSchueler)))
				continue;
			if ((konfliktTyp === 3) && ((!this.getOfSchuelerHatKollision(idSchueler)) && (!this.getOfSchuelerHatNichtwahl(idSchueler))))
				continue;
			if ((subString.length > 0) && (!this.getOfSchuelerHatImNamenSubstring(idSchueler, subString)))
				continue;
			if ((idKurs > 0) && (!this.getOfSchuelerOfKursIstZugeordnet(idSchueler, idKurs)))
				continue;
			if (((idFach > 0) && (idKursart > 0)) && (!this.getOfSchuelerHatFachwahl(idSchueler, idFach, idKursart)))
				continue;
			menge.add(schueler);
		}
		return menge;
	}

	/**
	 * Liefert die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 *
	 * @return Die Anzahl aller Schüler-IDs mit mindestens einer Kollision oder Nichtwahl.
	 */
	public getAnzahlDerSchuelerMitKollisionenOderNichtwahlen() : number {
		return this.getMengeDerSchuelerMitKollisionenOderNichtwahlen().size();
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
	 * Liefert eine Menge aller Schienen mit mindestens einer Kollision.
	 *
	 * @return Eine Menge aller Schienen mit mindestens einer Kollision.
	 */
	public getMengeDerSchienenMitKollisionen() : JavaSet<GostBlockungsergebnisSchiene> {
		return CollectionUtils.toFilteredHashSet(this._map_schienenID_schiene.values(), { test : (s: GostBlockungsergebnisSchiene) => this.getOfSchieneHatKollision(s.id) });
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
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.getSchieneExistiert(idSchiene));
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
		DeveloperNotificationException.ifTrue("Die Schiene " + idSchiene + " muss erst beim Datenmanager entfernt werden!", this._parent.getSchieneExistiert(idSchiene));
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
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.getRegelExistiert(idRegel));
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
		DeveloperNotificationException.ifTrue("Die Regel " + idRegel + " muss erst beim Datenmanager entfernt werden!", this._parent.getRegelExistiert(idRegel));
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
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager hinzugefügt werden!", !this._parent.getKursExistiert(idKurs));
		const kurs : GostBlockungKurs = this._parent.getKurs(idKurs);
		const nSchienen : number = this._parent.getSchienenAnzahl();
		DeveloperNotificationException.ifTrue("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!", nSchienen < kurs.anzahlSchienen);
		this.stateRevalidateEverything();
		for (let nr : number = 1; nr <= kurs.anzahlSchienen; nr++)
			this.setKursSchienenNr(idKurs, nr);
	}

	/**
	 * Löscht den übergebenen Kurs.
	 *
	 * @param  idKurs Die Datenbank-ID des Kurses.
	 *
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                                         falls der Kurs noch Schülerzuordnungen hat.
	 */
	public setRemoveKursByID(idKurs : number) : void {
		DeveloperNotificationException.ifTrue("Der Kurs " + idKurs + " muss erst beim Datenmanager entfernt werden!", this._parent.getKursExistiert(idKurs));
		const nSchueler : number = this.getKursE(idKurs).schueler.size();
		DeveloperNotificationException.ifTrue("Entfernen unmöglich: Kurs " + idKurs + " hat noch " + nSchueler + " Schüler!", nSchueler > 0);
		const kurs : GostBlockungsergebnisKurs = this.getKursE(idKurs);
		for (const schienenID of kurs.schienen)
			this.getSchieneE(schienenID!).kurse.remove(kurs);
		kurs.schienen.clear();
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
		const kurs2 : GostBlockungsergebnisKurs = this.getKursE(idKursID2delete);
		for (const schuelerID of new ArrayList(kurs2.schueler)) {
			this.stateSchuelerKursEntfernen(schuelerID!, idKursID2delete);
			this.stateSchuelerKursHinzufuegen(schuelerID!, idKursID1keep);
		}
		this._parent.removeKursByID(idKursID2delete);
		this.setRemoveKursByID(idKursID2delete);
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
		this._parent.addKurs(kurs2neu);
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
		const nSchienen : number = this._parent.getSchienenAnzahl();
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisManager(obj : unknown) : GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}

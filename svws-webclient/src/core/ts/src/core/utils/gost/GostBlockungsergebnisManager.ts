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
			const fachKursliste : List<GostBlockungsergebnisKurs> = MapUtils.getOrCreateArrayList(this._map_fachID_kurse, eKurs.fachID);
			fachKursliste.add(eKurs);
			const fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			const facharKursliste : List<GostBlockungsergebnisKurs> = MapUtils.getOrCreateArrayList(this._map_fachartID_kurse, fachartID);
			facharKursliste.add(eKurs);
			if (!this._map_fachartID_kursdifferenz.containsKey(fachartID)) {
				this._map_fachartID_kursdifferenz.put(fachartID, 0);
				this._ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
		}
		for (const gFachwahl of this._parent.daten().fachwahlen) {
			const fachartID : number = GostKursart.getFachartIDByFachwahl(gFachwahl);
			if (!this._map_fachartID_kurse.containsKey(fachartID))
				this._map_fachartID_kurse.put(fachartID, new ArrayList());
		}
		for (const gSchiene of this._parent.daten().schienen) {
			this._map_schienenID_fachartID_kurse.put(gSchiene.id, new HashMap());
			for (const fachartID of this._map_fachartID_kursdifferenz.keySet())
				this.getOfSchieneFachartKursmengeMap(gSchiene.id).put(fachartID, new ArrayList());
		}
		for (const gSchueler of this._parent.daten().schueler) {
			const eSchuelerID : number = gSchueler.id;
			const newSetKurse : HashSet<GostBlockungsergebnisKurs> | null = new HashSet();
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kurse, eSchuelerID, newSetKurse);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_schuelerID_kollisionen, eSchuelerID, 0);
		}
		for (const gSchueler of this._parent.daten().schueler)
			this._map_schuelerID_fachID_kurs.put(gSchueler.id, new HashMap());
		const strErrorUnbekannteFachwahl : string | null = "Schüler %d hat eine Fachwahl ist aber unbekannt!";
		const strErrorDoppeltesFach : string | null = "Schüler %d hat Fach %d doppelt!";
		for (const gFachwahl of this._parent.daten().fachwahlen) {
			const mapFachKurs : JavaMap<number, GostBlockungsergebnisKurs | null> | null = this._map_schuelerID_fachID_kurs.get(gFachwahl.schuelerID);
			if (mapFachKurs === null)
				throw new DeveloperNotificationException(JavaString.format(strErrorUnbekannteFachwahl, gFachwahl.schuelerID))
			if (mapFachKurs.put(gFachwahl.fachID, null) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppeltesFach, gFachwahl.schuelerID, gFachwahl.fachID))
		}
		for (const gSchueler of this._parent.daten().schueler) {
			this._map_schuelerID_schienenID_kurse.put(gSchueler.id, new HashMap());
			for (const gSchiene of this._parent.daten().schienen) {
				const newSet : HashSet<GostBlockungsergebnisKurs> | null = new HashSet();
				this.getOfSchuelerSchienenKursmengeMap(gSchueler.id).put(gSchiene.id, newSet);
			}
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
				case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS: {
					this.stateRegelvalidierung4_schueler_fixieren_in_kurs(r, regelVerletzungen);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					this.stateRegelvalidierung5_schueler_verbieten_in_kurs(r, regelVerletzungen);
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
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					this.stateRegelvalidierung1_kursart_sperren_in_schiene_von_bis(r, regelVerletzungen);
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
				case GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN: {
					this.stateRegelvalidierung9_lehrkraft_beachten(r, regelVerletzungen);
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
		for (const schiene1 of this.getOfKursSchienenmenge(r.parameter.get(0)!))
			for (const schiene2 of this.getOfKursSchienenmenge(r.parameter.get(1)!))
				if (schiene1 as unknown === schiene2 as unknown)
					regelVerletzungen.add(r.id);
	}

	private stateRegelvalidierung8_kurs_zusammen_mit_kurs(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const set1 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(r.parameter.get(0)!);
		const set2 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(r.parameter.get(1)!);
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

	private stateRegelvalidierung9_lehrkraft_beachten(r : GostBlockungRegel, regelVerletzungen : List<number>) : void {
		const externBeachten : boolean = r.parameter.get(0) === 1;
		for (const eSchiene of this._map_schienenID_schiene.values())
			for (const eKurs1 of eSchiene.kurse)
				for (const eKurs2 of eSchiene.kurse)
					if (eKurs1.id < eKurs2.id)
						for (const gLehr1 of this.getKursG(eKurs1.id).lehrer)
							for (const gLehr2 of this.getKursG(eKurs2.id).lehrer)
								if ((gLehr1.id === gLehr2.id) && ((externBeachten) || (!gLehr1.istExtern)))
									regelVerletzungen.add(r.id);
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
	 *          Stattdessen wird diese ungültige Wahl gespeichert.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursHinzufuegen(pSchuelerID : number, pKursID : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const fachID : number = kurs.fachID;
		if (!this.getOfSchuelerHatFachwahl(pSchuelerID, fachID, kurs.kursart)) {
			this.stateSchuelerKursUngueltigeWahlHinzufuegen(pSchuelerID, kurs);
			return;
		}
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) !== null)
			return;
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		const schuelerIDsOfKurs : JavaSet<number> = this.getOfKursSchuelerIDmenge(pKursID);
		const mapSFK : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(pSchuelerID);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.add(pSchuelerID);
		kurseOfSchueler.add(kurs);
		schuelerIDsOfKurs.add(pSchuelerID);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet--;
		mapSFK.put(fachID, kurs);
		this.stateKursdifferenzUpdate(fachartID);
		for (const schieneID of kurs.schienen)
			this.stateSchuelerSchieneHinzufuegen(pSchuelerID, schieneID!, kurs);
		this.stateRegelvalidierung();
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 */
	private stateSchuelerKursEntfernen(pSchuelerID : number, pKursID : number) : void {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const fachID : number = kurs.fachID;
		if (!this.getOfSchuelerHatFachwahl(pSchuelerID, fachID, kurs.kursart)) {
			this.stateSchuelerKursUngueltigeWahlEntfernen(pSchuelerID, kurs);
			return;
		}
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) as unknown !== kurs as unknown)
			return;
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		const schuelerIDsOfKurs : JavaSet<number> = this.getOfKursSchuelerIDmenge(pKursID);
		const mapSFK : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(pSchuelerID);
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		kurs.schueler.remove(pSchuelerID);
		kurseOfSchueler.remove(kurs);
		schuelerIDsOfKurs.remove(pSchuelerID);
		this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet++;
		mapSFK.put(fachID, null);
		this.stateKursdifferenzUpdate(fachartID);
		for (const schieneID of kurs.schienen)
			this.stateSchuelerSchieneEntfernen(pSchuelerID, schieneID!, kurs);
		this.stateRegelvalidierung();
	}

	private stateSchuelerKursUngueltigeWahlHinzufuegen(pSchuelerID : number, pKurs : GostBlockungsergebnisKurs) : void {
		const set : JavaSet<GostBlockungsergebnisKurs> = MapUtils.getOrCreateHashSet(this._map_schuelerID_ungueltige_kurse, pSchuelerID);
		set.add(pKurs);
	}

	private stateSchuelerKursUngueltigeWahlEntfernen(pSchuelerID : number, pKurs : GostBlockungsergebnisKurs) : void {
		const set : JavaSet<GostBlockungsergebnisKurs> = DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_ungueltige_kurse, pSchuelerID);
		set.remove(pKurs);
		if (set.isEmpty())
			this._map_schuelerID_ungueltige_kurse.remove(pSchuelerID);
	}

	/**
	 * Fügt den Kurs der Schiene hinzu.
	 *
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 *
	 * @return             FALSE, falls der Kurs bereits in der Schiene war, sonst TRUE.
	 */
	private stateKursSchieneHinzufuegen(pKursID : number, pSchienenID : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		const schienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		const fachID : number = kurs.fachID;
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		if (schienenOfKurs.contains(schiene))
			return false;
		const kursGruppe : List<GostBlockungsergebnisKurs> | null = this.getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe === null)
			throw new DeveloperNotificationException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL")
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.add(schiene.id);
		schiene.kurse.add(kurs);
		schienenOfKurs.add(schiene);
		for (const schuelerID of kurs.schueler)
			this.stateSchuelerSchieneHinzufuegen(schuelerID!, schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene += kursGruppe.isEmpty() ? 0 : 1;
		kursGruppe.add(kurs);
		this.stateRegelvalidierung();
		return true;
	}

	/**
	 * Entfernt den Kurs aus der Schiene.
	 *
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 *
	 * @return             FALSE, falls der Kurs bereits nicht in der Schiene war, sonst TRUE.
	 */
	private stateKursSchieneEntfernen(pKursID : number, pSchienenID : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		const schienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		const fachID : number = kurs.fachID;
		const fachartID : number = GostKursart.getFachartID(fachID, kurs.kursart);
		if (!schienenOfKurs.contains(schiene))
			return false;
		const kursGruppe : List<GostBlockungsergebnisKurs> | null = this.getOfSchieneFachartKursmengeMap(pSchienenID).get(fachartID);
		if (kursGruppe === null)
			throw new DeveloperNotificationException("SchieneID=" + pSchienenID + " --> FachartID=" + fachartID + " --> NULL")
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet -= Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kurs.schienen.remove(schiene.id);
		schiene.kurse.remove(kurs);
		schienenOfKurs.remove(schiene);
		for (const schuelerID of kurs.schueler)
			this.stateSchuelerSchieneEntfernen(schuelerID!, schiene.id, kurs);
		this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += Math.abs(kurs.anzahlSchienen - schienenOfKurs.size());
		kursGruppe.remove(kurs);
		this._ergebnis.bewertung.anzahlKurseMitGleicherFachartProSchiene -= kursGruppe.isEmpty() ? 0 : 1;
		this.stateRegelvalidierung();
		return true;
	}

	private stateSchuelerSchieneHinzufuegen(pSchuelerID : number, pSchienenID : number, pKurs : GostBlockungsergebnisKurs) : void {
		const schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(pSchienenID);
		this._map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl + 1);
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.add(pKurs);
		if (kursmenge.size() > 1) {
			const schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			this._map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen + 1);
			const schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(pSchuelerID);
			this._map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen + 1);
			this._ergebnis.bewertung.anzahlSchuelerKollisionen++;
		}
	}

	private stateSchuelerSchieneEntfernen(pSchuelerID : number, pSchienenID : number, pKurs : GostBlockungsergebnisKurs) : void {
		const schieneSchuelerzahl : number = this.getOfSchieneAnzahlSchueler(pSchienenID);
		DeveloperNotificationException.ifTrue("schieneSchuelerzahl == 0 --> Entfernen unmöglich!", schieneSchuelerzahl === 0);
		this._map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);
		if (!kursmenge.isEmpty()) {
			const schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			DeveloperNotificationException.ifTrue("schieneKollisionen == 0 --> Entfernen unmöglich!", schieneKollisionen === 0);
			this._map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);
			const schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(pSchuelerID);
			DeveloperNotificationException.ifTrue("schuelerKollisionen == 0 --> Entfernen unmöglich!", schuelerKollisionen === 0);
			this._map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);
			DeveloperNotificationException.ifTrue("Gesamtkollisionen == 0 --> Entfernen unmöglich!", this._ergebnis.bewertung.anzahlSchuelerKollisionen === 0);
			this._ergebnis.bewertung.anzahlSchuelerKollisionen--;
		}
	}

	private stateKursdifferenzUpdate(pFachartID : number) : void {
		const oldKD : number = this.getOfFachartKursdifferenz(pFachartID);
		const kurs1 : GostBlockungsergebnisKurs | null = this.getOfFachartKursmenge(pFachartID).get(0);
		if (kurs1 === null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " ohne Kurs!")
		let min : number = kurs1.schueler.size();
		let max : number = min;
		for (const kurs of this.getOfFachartKursmenge(pFachartID)) {
			const size : number = kurs.schueler.size();
			min = Math.min(min, size);
			max = Math.max(max, size);
		}
		const newKD : number = max - min;
		if (newKD === oldKD)
			return;
		this._map_fachartID_kursdifferenz.put(pFachartID, newKD);
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
	 * Liefert den zugehörigen Daten-Manager für diesen Ergebnis-Manager zurück.
	 *
	 * @return Der Daten-Manager, der zu diesem Ergebnis-Manager gehört.
	 */
	public getParent() : GostBlockungsdatenManager | null {
		return this._parent;
	}

	/**
	 * Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 *
	 * @return Liefert die Blockungs-ID. Das ist die ID des Elternteils.
	 */
	public getBlockungsdatenID() : number {
		return this._ergebnis.blockungID;
	}

	/**
	 * Liefert das Blockungsergebnis zurück.
	 *
	 * @return das Blockungsergebnis
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
	 * Liefert die Anzahl an Schülerkollisionen. Ist ein Schüler x mal in einer Schiene und ist x > 1, dann wird dies
	 * als x-1 Kollisionen gezählt.
	 *
	 * @return Die Gesamtzahl der Kollisionen zurück.
	 */
	public getOfBewertungAnzahlKollisionen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerKollisionen;
	}

	/**
	 * Liefert die Anzahl nicht vollständig verteilter Kurse. Ein Multikurse der über zwei Schienen geht und gar nicht
	 * zugeteilt wurde, wird doppelt gezählt.
	 *
	 * @return Liefert die Anzahl nicht vollständig verteilter Kurse.
	 */
	public getOfBewertungAnzahlNichtZugeordneterKurse() : number {
		return this._ergebnis.bewertung.anzahlKurseNichtZugeordnet;
	}

	/**
	 * Liefert die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 *
	 * @return Die Anzahl an Fachwahlen, die nicht zugeordnet wurden.
	 */
	public getOfBewertungAnzahlNichtzugeordneterFachwahlen() : number {
		return this._ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
	}

	/**
	 * Ermittelt das Fach für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pFachID Die Datenbank-ID des Faches.
	 * @return Das GostFach-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public getFach(pFachID : number) : GostFach {
		return this._parent.faecherManager().getOrException(pFachID);
	}

	/**
	 * Liefert die Menge aller Kurse mit dem angegebenen Fach. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param pFachID  Die Datenbank-ID des Faches.
	 * @return         Die Menge aller Kurse mit dem angegebenen Fach.
	 */
	public getOfFachKursmenge(pFachID : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachID_kurse, pFachID);
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  idFachart  Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return die Kursmenge, die zur Fachart gehört.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursmenge(idFachart : number) : List<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kurse, idFachart);
	}

	/**
	 * Liefert die Kursdifferenz der Fachart. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return                      Die Kursdifferenz der Fachart.
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursdifferenz(pFachartID : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_fachartID_kursdifferenz, pFachartID);
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 *
	 * @return Das Schueler-Objekt.
	 * @throws     DeveloperNotificationException im Falle, dass die ID nicht bekannt ist
	 */
	public getSchuelerG(pSchuelerID : number) : Schueler {
		return this._parent.getSchueler(pSchuelerID);
	}

	/**
	 * Liefert einen Schüler-String im Format: 'Nachname, Vorname'.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 *
	 * @return             Einen Schüler-String im Format: 'Nachname, Vorname'.
	 */
	public getOfSchuelerNameVorname(pSchuelerID : number) : string {
		const schueler : Schueler = this._parent.getSchueler(pSchuelerID);
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
	 * Liefert TRUE, falls der Schüler mindestens eine Kollision hat. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return TRUE, falls der Schüler mindestens eine Kollision hat.
	 */
	public getOfSchuelerHatKollision(pSchuelerID : number) : boolean {
		return this.getOfSchuelerAnzahlKollisionen(pSchuelerID) > 0;
	}

	/**
	 * Liefert TRUE, falls der Schüler mindestens eine Nichtwahl hat. <br>
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @return             TRUE, falls der Schüler mindestens eine Nichtwahl hat.
	 */
	public getOfSchuelerHatNichtwahl(pSchuelerID : number) : boolean {
		const map : JavaMap<number, GostBlockungsergebnisKurs | null> = this.getOfSchuelerFachIDKursMap(pSchuelerID);
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
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public getOfSchuelerAnzahlKollisionen(idSchueler : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_kollisionen, idSchueler);
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
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public getOfSchuelerFachIDKursMap(idSchueler : number) : JavaMap<number, GostBlockungsergebnisKurs | null> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_fachID_kurs, idSchueler);
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 *
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public getOfSchuelerSchienenKursmengeMap(idSchueler : number) : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schuelerID_schienenID_kurse, idSchueler);
	}

	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 *
	 * @param idSchueler Die Datenbank-ID des Schülers.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public getOfSchuelerOfSchieneKursmenge(idSchueler : number, idSchiene : number) : JavaSet<GostBlockungsergebnisKurs> {
		return DeveloperNotificationException.ifMapGetIsNull(this.getOfSchuelerSchienenKursmengeMap(idSchueler), idSchiene);
	}

	/**
	 * Liefert die zu (Schüler, Fach) die jeweilige Kursart. <br>
	 * Liefert eine Exception, falls (Schüler, Fach) nicht existiert.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pFachID     Die Datenbank-ID des Faches.
	 *
	 * @return Die zu (Schüler, Fach) die jeweilige Kursart.
	 */
	public getOfSchuelerOfFachKursart(pSchuelerID : number, pFachID : number) : GostKursart {
		return this._parent.getOfSchuelerOfFachKursart(pSchuelerID, pFachID);
	}

	/**
	 * Ermittelt für den Schüler mit einem Fach den zugeordneten Kurs.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pFachID     Die Datenbank-ID des Faches.
	 * @return             Der zugeordnete Kurs oder null, falls es keine Zuordnung gibt.
	 */
	public getOfSchuelerOfFachZugeordneterKurs(pSchuelerID : number, pFachID : number) : GostBlockungsergebnisKurs | null {
		if (!this.getOfSchuelerFachIDKursMap(pSchuelerID).containsKey(pFachID))
			throw new DeveloperNotificationException("Schüler " + pSchuelerID + " hat das Fach " + pFachID + " nicht gewählt!")
		return this.getOfSchuelerFachIDKursMap(pSchuelerID).get(pFachID);
	}

	/**
	 * Liefert TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 *
	 * @return             TRUE, falls der Schüler dem Kurs zugeordnet ist.
	 */
	public getOfSchuelerOfKursIstZugeordnet(pSchuelerID : number, pKursID : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const kurseOfSchueler : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerKursmenge(pSchuelerID);
		return kurseOfSchueler.contains(kurs);
	}

	/**
	 * Liefert für den übergebenen Schüler einen Vorschlag für eine Neuzuordnung der Kurse.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return             Die Neuzuordnung der Kurse im {@link SchuelerblockungOutput}-Objekt.
	 */
	public getOfSchuelerNeuzuordnung(pSchuelerID : number) : SchuelerblockungOutput {
		const input : SchuelerblockungInput = new SchuelerblockungInput();
		input.schienen = this.getOfSchieneAnzahl();
		const fachwahlenDesSchuelers : List<GostFachwahl> = this._parent.getOfSchuelerFacharten(pSchuelerID);
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
				inKurs.istGesperrt = this.getOfSchuelerOfKursIstGesperrt(pSchuelerID, kurs.id);
				inKurs.istFixiert = this.getOfSchuelerOfKursIstFixiert(pSchuelerID, kurs.id);
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
	 * Liefert TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pKursID      Die Datenbank-ID des Kurses.
	 * @return             TRUE, falls der Schüler im Kurs via Regel fixiert sein soll.
	 */
	public getOfSchuelerOfKursIstFixiert(pSchuelerID : number, pKursID : number) : boolean {
		for (const r of this._parent.getMengeOfRegeln()) {
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			if (typ as unknown === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS as unknown) {
				const schuelerID : number = r.parameter.get(0).valueOf();
				const kursID : number = r.parameter.get(1).valueOf();
				if ((schuelerID === pSchuelerID) && (kursID === pKursID))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pKursID      Die Datenbank-ID des Kurses.
	 * @return             TRUE, falls der Schüler im Kurs via Regel gesperrt sein soll.
	 */
	public getOfSchuelerOfKursIstGesperrt(pSchuelerID : number, pKursID : number) : boolean {
		for (const r of this._parent.getMengeOfRegeln()) {
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			if (typ as unknown === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS as unknown) {
				const schuelerID : number = r.parameter.get(0).valueOf();
				const kursID : number = r.parameter.get(1).valueOf();
				if ((schuelerID === pSchuelerID) && (kursID === pKursID))
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt.
	 * Groß- und Kleinschreibung wird dabei ignoriert.
	 *
	 * @param pSchuelerID  Die Datenbank-ID des Schülers.
	 * @param pSubString   Der zu suchende Sub-String.
	 * @return             TRUE, falls der Sub-String im Nachnamen oder im Vornamen des Schülers vorkommt.
	 */
	public getOfSchuelerHatImNamenSubstring(pSchuelerID : number, pSubString : string) : boolean {
		const schueler : Schueler = this.getSchuelerG(pSchuelerID);
		const text : string = pSubString.toLowerCase();
		if (JavaString.contains(schueler.nachname.toLowerCase(), text))
			return true;
		return (JavaString.contains(schueler.vorname.toLowerCase(), text));
	}

	/**
	 * Ermittelt den Kurs für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param  pKursID Die ID des Kurses.
	 * @return Das GostBlockungKurs-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public getKursG(pKursID : number) : GostBlockungKurs {
		return this._parent.getKurs(pKursID);
	}

	/**
	 * Liefert den Kurs (des Blockungsergebnisses) zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 *
	 * @return         Den Kurs (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public getKursE(pKursID : number) : GostBlockungsergebnisKurs {
		const kurs : GostBlockungsergebnisKurs | null = this._map_kursID_kurs.get(pKursID);
		if (kurs === null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!")
		return kurs;
	}

	/**
	 * Liefert den Namen des Kurses. Der Name wird automatisch erzeugt aus dem Fach, der Kursart und der Nummer,
	 * beispielsweise D-GK1.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 *
	 * @return         Die Datenbank-ID des Kurses.
	 */
	public getOfKursName(pKursID : number) : string {
		return this._parent.getNameOfKurs(pKursID);
	}

	/**
	 * Liefert TRUE, falls der Kurs der Schiene zugeordnet ist.
	 *
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 * @param  pSchienenID Die Datenbank-ID der Schiene.
	 *
	 * @return             TRUE, falls der Kurs der Schiene zugeordnet ist.
	 */
	public getOfKursOfSchieneIstZugeordnet(pKursID : number, pSchienenID : number) : boolean {
		const schiene : GostBlockungsergebnisSchiene = this.getSchieneE(pSchienenID);
		const schienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(pKursID);
		return schienenOfKurs.contains(schiene);
	}

	/**
	 * Liefert die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind. <br>
	 * Wirft eine Exception, wenn der ID kein Kurs zugeordnet ist.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs, die dem Kurs zugeordnet sind.
	 */
	public getOfKursSchuelerIDmenge(pKursID : number) : JavaSet<number> {
		const schuelerIDs : JavaSet<number> | null = this._map_kursID_schuelerIDs.get(pKursID);
		if (schuelerIDs === null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!")
		return schuelerIDs;
	}

	/**
	 * Liefert die Schienenmenge des Kurses.
	 *
	 * @param pKursID Die Datenbank-ID des Kurses.
	 * @return Die Schienenmenge des Kurses.
	 */
	public getOfKursSchienenmenge(pKursID : number) : JavaSet<GostBlockungsergebnisSchiene> {
		const schienenOfKurs : JavaSet<GostBlockungsergebnisSchiene> | null = this._map_kursID_schienen.get(pKursID);
		if (schienenOfKurs === null)
			throw new DeveloperNotificationException("Kurs-ID " + pKursID + " unbekannt!")
		return schienenOfKurs;
	}

	/**
	 * Liefert ein Array aller Schienen-Nummern des Kurses.
	 *
	 * @param pKursID  Die Datenbank-ID des Kurses.
	 * @return         Ein Array aller Schienen-Nummern des Kurses.
	 */
	public getOfKursSchienenNummern(pKursID : number) : Array<number> {
		const schienenIDs : List<number> = this.getKursE(pKursID).schienen;
		const a : Array<number> | null = Array(schienenIDs.size()).fill(0);
		for (let i : number = 0; i < a.length; i++) {
			const schienenID : number = schienenIDs.get(i).valueOf();
			a[i] = this._parent.getSchiene(schienenID).nummer;
		}
		return a;
	}

	/**
	 * Liefert TRUE, falls der Kurs mindestens eine Kollision hat. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return TRUE, falls der Kurs mindestens eine Kollision hat.
	 */
	public getOfKursHatKollision(pKursID : number) : boolean {
		for (const schiene of this.getOfKursSchienenmenge(pKursID))
			for (const schuelerID of this.getKursE(pKursID).schueler)
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, schiene.id).size() > 1)
					return true;
		return false;
	}

	/**
	 * Liefert die Anzahl an Schülern des Kurses mit Kollisionen. <br>
	 * Kollision: Der Schüler muss in einer Schiene des Kurses eine Kollision haben.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schülern des Kurses mit Kollisionen.
	 */
	public getOfKursAnzahlKollisionen(pKursID : number) : number {
		let summe : number = 0;
		for (const schiene of this.getOfKursSchienenmenge(pKursID))
			for (const schuelerID of this.getKursE(pKursID).schueler)
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, schiene.id).size() > 1)
					summe++;
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Die Anzahl an Schülern die dem Kurs zugeordnet.
	 */
	public getOfKursAnzahlSchueler(pKursID : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		return kursE.schueler.size();
	}

	/**
	 * Liefert die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 *
	 * @param  pKursID  Die Datenbank-ID des Kurses.
	 * @return          Die Anzahl an Schülern die dem Kurs zugeordnet sind und ihn schriftlich belegt haben.
	 */
	public getOfKursAnzahlSchuelerSchriftlich(pKursID : number) : number {
		const kursE : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const fachID : number = kursE.fachID;
		let summe : number = 0;
		for (const schuelerID of kursE.schueler) {
			const fachwahl : GostFachwahl = this._parent.getOfSchuelerOfFachFachwahl(schuelerID!, fachID);
			if (fachwahl.istSchriftlich)
				summe++;
		}
		return summe;
	}

	/**
	 * Liefert die Anzahl an Schienen in denen der Kurs gerade ist.
	 *
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen in denen der Kurs gerade ist.
	 */
	public getOfKursAnzahlSchienenIst(kursID : number) : number {
		return this.getOfKursSchienenmenge(kursID).size();
	}

	/**
	 * Liefert die Anzahl an Schienen, die der Kurs haben sollte.
	 *
	 * @param kursID Die Datenbank-ID des Kurses.
	 * @return Die Anzahl an Schienen, die der Kurs haben sollte.
	 */
	public getOfKursAnzahlSchienenSoll(kursID : number) : number {
		return this.getKursE(kursID).anzahlSchienen;
	}

	/**
	 * Liefert die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @return Die Menge aller Schüler-IDs des Kurses mit Kollisionen.
	 */
	public getOfKursSchuelermengeMitKollisionen(pKursID : number) : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schiene of this.getOfKursSchienenmenge(pKursID))
			for (const schuelerID of this.getKursE(pKursID).schueler)
				if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, schiene.id).size() > 1)
					set.add(schuelerID);
		return set;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Es dürfen keine Schüler dem Kurs zugeordnet sein.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @return                       TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht existiert.
	 */
	public getOfKursRemoveAllowed(pKursID : number) : boolean {
		return this.getKursE(pKursID).schueler.isEmpty();
	}

	/**
	 * Ermittelt die Schiene für die angegebene ID. Delegiert den Aufruf an den Fächer-Manager des Eltern-Objektes
	 * {@link GostBlockungsdatenManager}. <br>
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchienenID Die ID der Schiene
	 * @return Das GostBlockungSchiene-Objekt.
	 * @throws DeveloperNotificationException im Falle, dass die ID nicht bekannt ist.
	 */
	public getSchieneG(pSchienenID : number) : GostBlockungSchiene {
		return this._parent.getSchiene(pSchienenID);
	}

	/**
	 * Liefert die Schiene zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Schiene zur übergebenen ID.
	 */
	public getSchieneE(idSchiene : number) : GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_schiene, idSchiene);
	}

	/**
	 * Liefert die Schiene mit der übergebenen Nummer. <br>
	 * Wirft eine {@link DeveloperNotificationException} wenn eine solche Schiene nicht existiert.
	 *
	 * @param nrSchiene Die Nummer der Schiene.
	 *
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public getSchieneEmitNr(nrSchiene : number) : GostBlockungsergebnisSchiene {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenNr_schiene, nrSchiene);
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public getOfSchieneAnzahlSchueler(idSchiene : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_schuelerAnzahl, idSchiene);
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
	 * TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public getOfSchieneHatKollision(idSchiene : number) : boolean {
		return this.getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param idSchiene Die Datenbank-ID der Schiene.
	 *
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerMitKollisionen(idSchiene : number) : number {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_schienenID_kollisionen, idSchiene);
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
	 * Liefert die Menge aller Schienen.
	 *
	 * @param idSchueler           Die Datenbank-ID des Schülers.
	 * @param fixiereBelegteKurse  falls TRUE, werden alle Kurse fixiert, in denen der Schüler momentan ist.
	 *
	 * @return Die Menge aller Schienen.
	 */
	public getSchuelerblockungOutput(idSchueler : number, fixiereBelegteKurse : boolean) : SchuelerblockungOutput {
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
				kursS.schienen = Array(kursE.schienen.size()).fill(0);
				for (let i : number = 0; i < kursS.schienen.length; i++)
					kursS.schienen[i] = this._parent.getSchiene(kursE.schienen.get(i)!).nummer;
				input.kurse.add(kursS);
			}
		}
		return new SchuelerblockungAlgorithmus().handle(input);
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
	 * @return  TRUE, falls die jeweilige Operation erfolgreich war.
	 *
	 * @throws DeveloperNotificationException  falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchiene(idKurs : number, idSchiene : number, hinzufuegenOderEntfernen : boolean) : boolean {
		if (hinzufuegenOderEntfernen)
			return this.stateKursSchieneHinzufuegen(idKurs, idSchiene);
		return this.stateKursSchieneEntfernen(idKurs, idSchiene);
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

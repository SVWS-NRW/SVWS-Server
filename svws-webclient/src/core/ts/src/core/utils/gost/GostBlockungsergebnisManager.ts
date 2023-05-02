import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../../core/logger/Logger';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { SchuelerblockungInput } from '../../../core/data/kursblockung/SchuelerblockungInput';
import { List } from '../../../java/util/List';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { SchuelerblockungOutput } from '../../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs } from '../../../core/data/kursblockung/SchuelerblockungInputKurs';
import { GostBlockungsdatenManager, cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager } from '../../../core/utils/gost/GostBlockungsdatenManager';
import { SchuelerblockungAlgorithmus } from '../../../core/kursblockung/SchuelerblockungAlgorithmus';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { GostBlockungsergebnis, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { Arrays } from '../../../java/util/Arrays';
import { JavaMap } from '../../../java/util/JavaMap';

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
		const strErrorDoppelteSchienennummer : string | null = "Schienen NR %d doppelt!";
		const strErrorDoppelteSchienenID : string | null = "Schienen ID %d doppelt!";
		for (const gSchiene of this._parent.daten().schienen) {
			const eSchiene : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
			eSchiene.id = gSchiene.id;
			this._ergebnis.schienen.add(eSchiene);
			if (this._map_schienenNr_schiene.put(gSchiene.nummer, eSchiene) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchienennummer, gSchiene.nummer))
			if (this._map_schienenID_schiene.put(gSchiene.id, eSchiene) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchienenID, gSchiene.id))
			if (this._map_schienenID_schuelerAnzahl.put(gSchiene.id, 0) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchienenID, gSchiene.id))
			if (this._map_schienenID_kollisionen.put(gSchiene.id, 0) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchienenID, gSchiene.id))
		}
		const strErrorDoppelteKursID : string | null = "Kurs-ID %d doppelt!";
		for (const gKurs of this._parent.daten().kurse) {
			const eKurs : GostBlockungsergebnisKurs = new GostBlockungsergebnisKurs();
			eKurs.id = gKurs.id;
			eKurs.fachID = gKurs.fach_id;
			eKurs.kursart = gKurs.kursart;
			eKurs.anzahlSchienen = gKurs.anzahlSchienen;
			this._ergebnis.bewertung.anzahlKurseNichtZugeordnet += eKurs.anzahlSchienen;
			if (this._map_kursID_kurs.put(eKurs.id, eKurs) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteKursID, eKurs.id))
			const newSetSchiene : HashSet<GostBlockungsergebnisSchiene> | null = new HashSet();
			if (this._map_kursID_schienen.put(eKurs.id, newSetSchiene) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteKursID, eKurs.id))
			const newSetSchueler : HashSet<number> | null = new HashSet();
			if (this._map_kursID_schuelerIDs.put(eKurs.id, newSetSchueler) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteKursID, eKurs.id))
			if (!this._map_fachID_kurse.containsKey(eKurs.fachID))
				this._map_fachID_kurse.put(eKurs.fachID, new ArrayList());
			const fachgruppe : List<GostBlockungsergebnisKurs> | null = this._map_fachID_kurse.get(eKurs.fachID);
			if (fachgruppe !== null)
				fachgruppe.add(eKurs);
			const fachartID : number = GostKursart.getFachartID(eKurs.fachID, eKurs.kursart);
			if (!this._map_fachartID_kurse.containsKey(fachartID)) {
				this._map_fachartID_kurse.put(fachartID, new ArrayList());
				this._map_fachartID_kursdifferenz.put(fachartID, 0);
				this._ergebnis.bewertung.kursdifferenzHistogramm[0]++;
			}
			const fachartgruppe : List<GostBlockungsergebnisKurs> | null = this._map_fachartID_kurse.get(fachartID);
			if (fachartgruppe !== null)
				fachartgruppe.add(eKurs);
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
		const strErrorDoppelteSchuelerID : string | null = "Schüler-ID %d doppelt!";
		for (const gSchueler of this._parent.daten().schueler) {
			const eSchuelerID : number = gSchueler.id;
			const newSetKurse : HashSet<GostBlockungsergebnisKurs> | null = new HashSet();
			if (this._map_schuelerID_kurse.put(eSchuelerID, newSetKurse) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchuelerID, eSchuelerID))
			if (this._map_schuelerID_kollisionen.put(eSchuelerID, 0) !== null)
				throw new DeveloperNotificationException(JavaString.format(strErrorDoppelteSchuelerID, eSchuelerID))
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
					if (!this.getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0)!, r.parameter.get(1)!))
						regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS: {
					if (this.getOfSchuelerOfKursIstZugeordnet(r.parameter.get(0)!, r.parameter.get(1)!))
						regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE: {
					if (!this.getOfKursSchienenmenge(r.parameter.get(0)!).contains(this.getSchieneEmitNr(r.parameter.get(1)!)))
						regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE: {
					if (this.getOfKursSchienenmenge(r.parameter.get(0)!).contains(this.getSchieneEmitNr(r.parameter.get(1)!)))
						regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS: {
					for (let schienenNr : number = r.parameter.get(1)!; schienenNr <= r.parameter.get(2)!; schienenNr++)
						for (const eKurs of this.getSchieneEmitNr(schienenNr).kurse)
							if (eKurs.kursart === r.parameter.get(0)!)
								regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
					for (const eKurs of this._map_kursID_kurs.values())
						for (const eSchieneID of eKurs.schienen) {
							const nr : number = this.getSchieneG(eSchieneID!).nummer;
							const b1 : boolean = eKurs.kursart === r.parameter.get(0)!;
							const b2 : boolean = (r.parameter.get(1)! <= nr) && (nr <= r.parameter.get(2)!);
							if (b1 !== b2)
								regelVerletzungen.add(r.id);
						}
					break;
				}
				case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS: {
					for (const schiene1 of this.getOfKursSchienenmenge(r.parameter.get(0)!))
						for (const schiene2 of this.getOfKursSchienenmenge(r.parameter.get(1)!))
							if (schiene1 as unknown === schiene2 as unknown)
								regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS: {
					const set1 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(r.parameter.get(0)!)
					const set2 : JavaSet<GostBlockungsergebnisSchiene> = this.getOfKursSchienenmenge(r.parameter.get(1)!)
					if (set1.size() < set2.size()) {
						for (const schiene1 of set1)
							if (!set2.contains(schiene1))
								regelVerletzungen.add(r.id);
					} else {
						for (const schiene2 of set2)
							if (!set1.contains(schiene2))
								regelVerletzungen.add(r.id);
					}
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN: {
					const externBeachten : boolean = r.parameter.get(0) === 1
					for (const eSchiene of this._map_schienenID_schiene.values())
						for (const eKurs1 of eSchiene.kurse)
							for (const eKurs2 of eSchiene.kurse)
								if (eKurs1.id < eKurs2.id)
									for (const gLehr1 of this.getKursG(eKurs1.id).lehrer)
										for (const gLehr2 of this.getKursG(eKurs2.id).lehrer)
											if ((gLehr1.id === gLehr2.id) && ((externBeachten) || (!gLehr1.istExtern)))
												regelVerletzungen.add(r.id);
					break;
				}
				case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN: {
					for (const eSchiene of this._map_schienenID_schiene.values())
						for (const eKurs1 of eSchiene.kurse)
							for (const eKurs2 of eSchiene.kurse)
								if (eKurs1.id < eKurs2.id)
									for (const gLehr1 of this.getKursG(eKurs1.id).lehrer)
										for (const gLehr2 of this.getKursG(eKurs2.id).lehrer)
											if (gLehr1.id === gLehr2.id)
												regelVerletzungen.add(r.id);
					break;
				}
				default: {
					throw new IllegalStateException("Der Regel-Typ ist unbekannt: " + typ)
				}
			}
		}
		this._parent.updateErgebnisBewertung(this._ergebnis);
	}

	/**
	 * Fügt den Schüler dem Kurs hinzu.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 *
	 * @return             FALSE, falls der Schüler bereits zugeordnet war, sonst TRUE.
	 */
	private stateSchuelerKursHinzufuegen(pSchuelerID : number, pKursID : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const fachID : number = kurs.fachID;
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) !== null)
			return false;
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
		return true;
	}

	/**
	 * Entfernt den Schüler aus dem Kurs.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @param  pKursID     Die Datenbank-ID des Kurses.
	 *
	 * @return             FALSE, falls der Schüler bereits nicht zugeordnet war, sonst TRUE.
	 */
	private stateSchuelerKursEntfernen(pSchuelerID : number, pKursID : number) : boolean {
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const fachID : number = kurs.fachID;
		if (this.getOfSchuelerOfFachZugeordneterKurs(pSchuelerID, fachID) as unknown !== kurs as unknown)
			return false;
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
		return true;
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
		if (schieneSchuelerzahl === 0)
			throw new DeveloperNotificationException("schieneSchuelerzahl == 0 --> Entfernen unmöglich!")
		this._map_schienenID_schuelerAnzahl.put(pSchienenID, schieneSchuelerzahl - 1);
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> = this.getOfSchuelerOfSchieneKursmenge(pSchuelerID, pSchienenID);
		kursmenge.remove(pKurs);
		if (!kursmenge.isEmpty()) {
			const schieneKollisionen : number = this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID);
			if (schieneKollisionen === 0)
				throw new DeveloperNotificationException("schieneKollisionen == 0 --> Entfernen unmöglich!")
			this._map_schienenID_kollisionen.put(pSchienenID, schieneKollisionen - 1);
			const schuelerKollisionen : number = this.getOfSchuelerAnzahlKollisionen(pSchuelerID);
			if (schuelerKollisionen === 0)
				throw new DeveloperNotificationException("schuelerKollisionen == 0 --> Entfernen unmöglich!")
			this._map_schuelerID_kollisionen.put(pSchuelerID, schuelerKollisionen - 1);
			if (this._ergebnis.bewertung.anzahlSchuelerKollisionen === 0)
				throw new DeveloperNotificationException("Gesamtkollisionen == 0 --> Entfernen unmöglich!")
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
		const kurseOfFach : List<GostBlockungsergebnisKurs> | null = this._map_fachID_kurse.get(pFachID);
		if (kurseOfFach === null)
			throw new DeveloperNotificationException("Fach-ID " + pFachID + " unbekannt!")
		return kurseOfFach;
	}

	/**
	 * Liefert die Kursmenge, die zur Fachart gehört. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return                      Die Kursmenge, die zur Fachart gehört.
	 *
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursmenge(pFachartID : number) : List<GostBlockungsergebnisKurs> {
		const fachartGruppe : List<GostBlockungsergebnisKurs> | null = this._map_fachartID_kurse.get(pFachartID);
		if (fachartGruppe === null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " unbekannt!")
		return fachartGruppe;
	}

	/**
	 * Liefert die Kursdifferenz der Fachart. <br>
	 * Die Fachart-ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @param  pFachartID           Die ID wird berechnet über: {@link GostKursart#getFachartID(long, int)}.
	 *
	 * @return                      Die Kursdifferenz der Fachart.
	 *
	 * @throws DeveloperNotificationException Falls die Fachart-ID unbekannt ist.
	 */
	public getOfFachartKursdifferenz(pFachartID : number) : number {
		const kursdifferenz : number | null = this._map_fachartID_kursdifferenz.get(pFachartID);
		if (kursdifferenz === null)
			throw new DeveloperNotificationException("Fachart-ID " + pFachartID + " unbekannt!")
		return kursdifferenz!;
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. Delegiert den Aufruf an das Eltern-Objekt {@link GostBlockungsdatenManager}.
	 * Erzeugt eine DeveloperNotificationException im Fehlerfall, dass die ID nicht bekannt ist.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
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
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse, die dem Schüler zugeordnet sind.
	 */
	public getOfSchuelerKursmenge(pSchuelerID : number) : JavaSet<GostBlockungsergebnisKurs> {
		const kursIDs : JavaSet<GostBlockungsergebnisKurs> | null = this._map_schuelerID_kurse.get(pSchuelerID);
		if (kursIDs === null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return kursIDs;
	}

	/**
	 * Liefert die Menge aller Kurse des Schülers mit Kollisionen.
	 *
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Menge aller Kurse des Schülers mit Kollisionen.
	 */
	public getOfSchuelerKursmengeMitKollisionen(pSchuelerID : number) : JavaSet<GostBlockungsergebnisKurs> {
		const mapSchieneKurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> = this.getOfSchuelerSchienenKursmengeMap(pSchuelerID);
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
	 * @param pSchuelerID   Die Datenbank.ID des Schülers.
	 * @param pFach         Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param pKursart      Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 * @return              TRUE, falls der übergebene Schüler die entsprechende Fachwahl=Fach+Kursart hat.
	 */
	public getOfSchuelerHatFachwahl(pSchuelerID : number, pFach : number, pKursart : number) : boolean {
		return this._parent.getOfSchuelerHatFachart(pSchuelerID, pFach, pKursart);
	}

	/**
	 * Liefert die Anzahl an Kollisionen des Schülers. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Anzahl an Kollisionen des Schülers.
	 */
	public getOfSchuelerAnzahlKollisionen(pSchuelerID : number) : number {
		const anzahl : number | null = this._map_schuelerID_kollisionen.get(pSchuelerID);
		if (anzahl === null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return anzahl!;
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
	 * @param  pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map die jedem Fach eines Schülers seinen Kurs zuordnet (oder null).
	 */
	public getOfSchuelerFachIDKursMap(pSchuelerID : number) : JavaMap<number, GostBlockungsergebnisKurs | null> {
		const mapFachKurs : JavaMap<number, GostBlockungsergebnisKurs | null> | null = this._map_schuelerID_fachID_kurs.get(pSchuelerID);
		if (mapFachKurs === null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return mapFachKurs;
	}

	/**
	 * Liefert die Map des Schülers, die einer Schiene die Kurse des Schülers zuordnet.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @return Die Map des Schülers, die der Schiene die Kurse des Schülers zuordnet.
	 */
	public getOfSchuelerSchienenKursmengeMap(pSchuelerID : number) : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> {
		const mapSchuelerKurse : JavaMap<number, JavaSet<GostBlockungsergebnisKurs>> | null = this._map_schuelerID_schienenID_kurse.get(pSchuelerID);
		if (mapSchuelerKurse === null)
			throw new DeveloperNotificationException("Schüler-ID " + pSchuelerID + " unbekannt!")
		return mapSchuelerKurse;
	}

	/**
	 * Liefert die Kursmenge des Schülers in der Schiene.
	 *
	 * @param pSchuelerID Die Datenbank-ID des Schülers.
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Kursmenge des Schülers in der Schiene.
	 */
	public getOfSchuelerOfSchieneKursmenge(pSchuelerID : number, pSchienenID : number) : JavaSet<GostBlockungsergebnisKurs> {
		const kursmenge : JavaSet<GostBlockungsergebnisKurs> | null = this.getOfSchuelerSchienenKursmengeMap(pSchuelerID).get(pSchienenID);
		if (kursmenge === null)
			throw new DeveloperNotificationException("Schüler-ID=" + pSchuelerID + ", Schienen-ID=" + pSchienenID + " unbekannt!")
		return kursmenge;
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
	 * Liefert die Schiene  zur übergebenen ID. <br>
	 * Wirft eine Exception, wenn der ID keine Schiene zugeordnet ist.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Schiene (des Blockungsergebnisses) zur übergebenen ID.
	 */
	public getSchieneE(pSchienenID : number) : GostBlockungsergebnisSchiene {
		const schiene : GostBlockungsergebnisSchiene | null = this._map_schienenID_schiene.get(pSchienenID);
		if (schiene === null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!")
		return schiene;
	}

	/**
	 * Liefert die Schiene mit der übergebenen Schienen-NR. <br>
	 * Wirft eine Exception, wenn eine Schiene mit NR nicht existiert.
	 *
	 * @param pSchienenNr Die Nummer der Schiene.
	 * @return Die Schiene mit der übergebenen Schienen-NR.
	 */
	public getSchieneEmitNr(pSchienenNr : number) : GostBlockungsergebnisSchiene {
		const schiene : GostBlockungsergebnisSchiene | null = this._map_schienenNr_schiene.get(pSchienenNr);
		if (schiene === null)
			throw new DeveloperNotificationException("Schienen-NR " + pSchienenNr + " unbekannt!")
		return schiene;
	}

	/**
	 * Liefert die Anzahl an Schülern in der Schiene mit der übergebenen ID zurück. <br>
	 * Falls ein Schüler mehrfach in der Schiene ist, wird er mehrfach gezählt!
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schülern in der Schiene.
	 */
	public getOfSchieneAnzahlSchueler(pSchienenID : number) : number {
		const anzahl : number | null = this._map_schienenID_schuelerAnzahl.get(pSchienenID);
		if (anzahl === null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!")
		return anzahl!;
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
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return TRUE, falls die Schiene mindestens eine Schüler-Kollision hat.
	 */
	public getOfSchieneHatKollision(pSchienenID : number) : boolean {
		return this.getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID) > 0;
	}

	/**
	 * Liefert die Anzahl an Schüler-Kollisionen der Schiene zurück. <br>
	 * Ein Schüler, der N>1 Mal in einer Schiene ist, erzeugt N-1 Kollisionen.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Anzahl an Schüler-Kollisionen in der Schiene.
	 */
	public getOfSchieneAnzahlSchuelerMitKollisionen(pSchienenID : number) : number {
		const anzahl : number | null = this._map_schienenID_kollisionen.get(pSchienenID);
		if (anzahl === null)
			throw new DeveloperNotificationException("Schienen-ID " + pSchienenID + " unbekannt!")
		return anzahl!;
	}

	/**
	 * Liefert die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 *
	 * @param pSchienenID Die Datenbank-ID der Schiene.
	 * @return Die Menge an Schüler-IDs, die in der Schiene eine Kollision haben.
	 */
	public getOfSchieneSchuelermengeMitKollisionen(pSchienenID : number) : JavaSet<number> {
		const set : HashSet<number> = new HashSet();
		for (const schuelerID of this._map_schuelerID_kollisionen.keySet())
			if (this.getOfSchuelerOfSchieneKursmenge(schuelerID!, pSchienenID).size() > 1)
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
		const map : JavaMap<number, List<GostBlockungsergebnisKurs>> | null = this._map_schienenID_fachartID_kurse.get(pSchienenID);
		if (map === null)
			throw new DeveloperNotificationException("Die Schienen-ID " + pSchienenID + " ist unbekannt!")
		return map;
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
	 * @param  pKursID      Falls > 0, werden Schüler des Kurses herausgefiltert.
	 * @param  pFachID      Falls > 0 und
	 * @param  pKursartID   falls > 0, werden Schüler mit dieser Fachwahl herausgefiltert.
	 * @param  pKonfliktTyp Falls 1 = mit Kollisionen, 2 = mit Nichtwahlen, 3 = mit Kollisionen und Nichtwahlen, sonst alle Schüler.
	 * @param  pSubString   Falls |pSubString| > 0 werden Schüler deren Vor- oder Nachname diesen String enthält herausgefiltert.
	 *
	 * @return eine gefilterte Menge aller Schüler.
	 */
	public getMengeDerSchuelerGefiltert(pKursID : number, pFachID : number, pKursartID : number, pKonfliktTyp : number, pSubString : string) : List<Schueler> {
		const menge : List<Schueler> = new ArrayList();
		for (const schueler of this._parent.getMengeOfSchueler()) {
			const pSchuelerID : number = schueler.id;
			if ((pKonfliktTyp === 1) && (!this.getOfSchuelerHatKollision(pSchuelerID)))
				continue;
			if ((pKonfliktTyp === 2) && (!this.getOfSchuelerHatNichtwahl(pSchuelerID)))
				continue;
			if ((pKonfliktTyp === 3) && ((!this.getOfSchuelerHatKollision(pSchuelerID)) && (!this.getOfSchuelerHatNichtwahl(pSchuelerID))))
				continue;
			if ((pSubString.length > 0) && (!this.getOfSchuelerHatImNamenSubstring(pSchuelerID, pSubString)))
				continue;
			if ((pKursID > 0) && (!this.getOfSchuelerOfKursIstZugeordnet(pSchuelerID, pKursID)))
				continue;
			if (((pFachID > 0) && (pKursartID > 0)) && (!this.getOfSchuelerHatFachwahl(pSchuelerID, pFachID, pKursartID)))
				continue;
			menge.add(schueler);
		}
		return menge;
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
		const set : HashSet<GostBlockungsergebnisSchiene> = new HashSet();
		for (const schiene of this._map_schienenID_schiene.values())
			if (this.getOfSchieneHatKollision(schiene.id))
				set.add(schiene);
		return set;
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
	 * Verknüpft einen Kurs mit einer Schiene. Die Schiene wird anhand ihrer Nummer (nicht die Datenbank-ID)
	 * identifiziert.
	 *
	 * @param  pKursID              Die Datenbank-ID des Kurses.
	 * @param  pSchienenNr          Die Nummer der Schiene (nicht die Datenbank-ID).
	 *
	 * @throws DeveloperNotificationException Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchienenNr(pKursID : number, pSchienenNr : number) : void {
		const eSchiene : GostBlockungsergebnisSchiene | null = this._map_schienenNr_schiene.get(pSchienenNr);
		if (eSchiene === null)
			throw new DeveloperNotificationException("Schienen-Nr. " + pSchienenNr + " unbekannt!")
		this.stateKursSchieneHinzufuegen(pKursID, eSchiene.id);
	}

	/**
	 * Verknüpft einen Kurs mit einer Schiene oder hebt die Verknüpfung auf.
	 *
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pSchienenID               Die Datenbank-ID der Schiene.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 *
	 * @throws DeveloperNotificationException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setKursSchiene(pKursID : number, pSchienenID : number, pHinzufuegenOderEntfernen : boolean) : boolean {
		if (pHinzufuegenOderEntfernen)
			return this.stateKursSchieneHinzufuegen(pKursID, pSchienenID);
		return this.stateKursSchieneEntfernen(pKursID, pSchienenID);
	}

	/**
	 * Verknüpft einen Schüler mit einem Kurs oder hebt die Verknüpfung auf.
	 *
	 * @param  pSchuelerID               Die Datenbank-ID des Schülers.
	 * @param  pKursID                   Die Datenbank-ID des Kurses.
	 * @param  pHinzufuegenOderEntfernen TRUE=Hinzufügen, FALSE=Entfernen
	 *
	 * @return                           TRUE, falls die jeweilige Operation erfolgreich war.
	 *
	 * @throws DeveloperNotificationException      Falls ein Fehler passiert, z. B. wenn es die Zuordnung bereits gab.
	 */
	public setSchuelerKurs(pSchuelerID : number, pKursID : number, pHinzufuegenOderEntfernen : boolean) : boolean {
		if (pHinzufuegenOderEntfernen)
			return this.stateSchuelerKursHinzufuegen(pSchuelerID, pKursID);
		return this.stateSchuelerKursEntfernen(pSchuelerID, pKursID);
	}

	/**
	 * Geht die übergebenen Zuordnungen (Fach --> Kurs) durch und setzt
	 * bei Veränderung Kurse des übergebenen Schülers neu.
	 *
	 * @param schuelerID  Die Datenbank-ID des Schülers.
	 * @param pZuordnung  Die gewünschte Zuordnung.
	 */
	public setSchuelerNeuzuordnung(schuelerID : number, pZuordnung : SchuelerblockungOutput) : void {
		for (const z of pZuordnung.fachwahlenZuKurs) {
			const kursV : GostBlockungsergebnisKurs | null = this.getOfSchuelerOfFachZugeordneterKurs(schuelerID, z.fachID);
			const kursN : GostBlockungsergebnisKurs | null = z.kursID < 0 ? null : this.getKursE(z.kursID);
			if (kursV as unknown !== kursN as unknown) {
				if (kursV !== null)
					this.setSchuelerKurs(schuelerID, kursV.id, false);
				if (kursN !== null)
					this.setSchuelerKurs(schuelerID, kursN.id, true);
			}
		}
	}

	/**
	 * Fügt die übergebene Schiene hinzu.
	 *
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @throws DeveloperNotificationException  Falls die Schiene nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddSchieneByID(pSchienenID : number) : void {
		if (!this._parent.getSchieneExistiert(pSchienenID))
			throw new DeveloperNotificationException("Die Schiene " + pSchienenID + " muss erst beim Datenmanager hinzugefügt werden!")
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Schiene.
	 *
	 * @param  pSchienenID           Die Datenbank-ID der Schiene.
	 * @throws DeveloperNotificationException  Falls die Schiene nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                               falls die Schiene noch Kurszuordnungen hat.
	 */
	public setRemoveSchieneByID(pSchienenID : number) : void {
		if (this._parent.getSchieneExistiert(pSchienenID))
			throw new DeveloperNotificationException("Die Schiene " + pSchienenID + " muss erst beim Datenmanager entfernt werden!")
		const nKurse : number = this.getSchieneE(pSchienenID).kurse.size();
		if (nKurse > 0)
			throw new DeveloperNotificationException("Entfernen unmöglich: Schiene " + pSchienenID + " hat noch " + nKurse + " Kurse!")
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt die übergebene Regel hinzu.
	 *
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @throws DeveloperNotificationException  Falls die Regel nicht zuerst im Datenmanager hinzugefügt wurde.
	 */
	public setAddRegelByID(pRegelID : number) : void {
		if (!this._parent.getRegelExistiert(pRegelID))
			throw new DeveloperNotificationException("Die Regel " + pRegelID + " muss erst beim Datenmanager hinzugefügt werden!")
		this.stateRevalidateEverything();
	}

	/**
	 * Löscht die übergebene Regel.
	 *
	 * @param  pRegelID              Die Datenbank-ID der Regel.
	 * @throws DeveloperNotificationException  Falls die Regel nicht zuerst beim Datenmanager entfernt wurde.
	 */
	public setRemoveRegelByID(pRegelID : number) : void {
		if (this._parent.getRegelExistiert(pRegelID))
			throw new DeveloperNotificationException("Die Regel " + pRegelID + " muss erst beim Datenmanager entfernt werden!")
		this.stateRevalidateEverything();
	}

	/**
	 * Fügt den übergebenen Kurs hinzu.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager hinzugefügt wurde.
	 */
	public setAddKursByID(pKursID : number) : void {
		if (!this._parent.getKursExistiert(pKursID))
			throw new DeveloperNotificationException("Der Kurs " + pKursID + " muss erst beim Datenmanager hinzugefügt werden!")
		const kurs : GostBlockungKurs = this._parent.getKurs(pKursID);
		const nSchienen : number = this._parent.getSchienenAnzahl();
		if (nSchienen < kurs.anzahlSchienen)
			throw new DeveloperNotificationException("Es gibt " + nSchienen + " Schienen, da passt ein Kurs mit " + kurs.anzahlSchienen + " nicht hinein!")
		this.stateRevalidateEverything();
		for (let nr : number = 1; nr <= kurs.anzahlSchienen; nr++)
			this.setKursSchienenNr(pKursID, nr);
	}

	/**
	 * Löscht den übergebenen Kurs.
	 *
	 * @param  pKursID               Die Datenbank-ID des Kurses.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht zuerst beim Datenmanager entfernt wurde, oder
	 *                               falls der Kurs noch Schülerzuordnungen hat.
	 */
	public setRemoveKursByID(pKursID : number) : void {
		if (this._parent.getKursExistiert(pKursID))
			throw new DeveloperNotificationException("Der Kurs " + pKursID + " muss erst beim Datenmanager entfernt werden!")
		const nSchueler : number = this.getKursE(pKursID).schueler.size();
		if (nSchueler > 0)
			throw new DeveloperNotificationException("Entfernen unmöglich: Kurs " + pKursID + " hat noch " + nSchueler + " Schüler!")
		const kurs : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		for (const schienenID of kurs.schienen)
			this.getSchieneE(schienenID!).kurse.remove(kurs);
		kurs.schienen.clear();
		this.stateRevalidateEverything();
	}

	/**
	 * Verschiebt alles SuS von pKursID2delete nach pKursID1keep und löscht dann den
	 * Kurs mit der ID beim {@link GostBlockungsdatenManager}, anschließend in diesem
	 * Manager.
	 *
	 * @param  pKursID1keep    Die Datenbank-ID des Kurses, der erhalten bleibt.
	 * @param  pKursID2delete  Die Datenbank-ID des Kurses, der gelöscht wird.
	 */
	public setMergeKurseByID(pKursID1keep : number, pKursID2delete : number) : void {
		const kurs2 : GostBlockungsergebnisKurs = this.getKursE(pKursID2delete);
		for (const schuelerID of new ArrayList(kurs2.schueler)) {
			this.stateSchuelerKursEntfernen(schuelerID!, pKursID2delete);
			this.stateSchuelerKursHinzufuegen(schuelerID!, pKursID1keep);
		}
		this._parent.removeKursByID(pKursID2delete);
		this.setRemoveKursByID(pKursID2delete);
	}

	/**
	 * Erzeugt einen neuen Kurs2 beim {@link GostBlockungsdatenManager},
	 * dann bei diesem Manager und
	 * verschiebt alle SuS des übergebenen Arrays von Kurs1 nach Kurs2.
	 *
	 * @param  pKurs1alt     Der Kurs, der gesplittet wird.
	 * @param  pKurs2neu     Der Kurs, der neu erzeugt wird.
	 * @param  pSusVon1nach2 Die Datenbank-IDs der Schüler, die verschoben werden sollen.
	 */
	public setSplitKurs(pKurs1alt : GostBlockungKurs, pKurs2neu : GostBlockungKurs, pSusVon1nach2 : Array<number>) : void {
		this._parent.addKurs(pKurs2neu);
		this.setAddKursByID(pKurs2neu.id);
		for (const schuelerID of pSusVon1nach2) {
			this.stateSchuelerKursEntfernen(schuelerID, pKurs1alt.id);
			this.stateSchuelerKursHinzufuegen(schuelerID, pKurs2neu.id);
		}
	}

	/**
	 * Verändert die Schienenanzahl eines Kurses. Dies ist nur bei einer Blockungsvorlage erlaubt.
	 *
	 * @param  pKursID Die Datenbank-ID des Kurses.
	 * @param  pAnzahlSchienenNeu Die neue Schienenanzahl des Kurses.
	 * @throws DeveloperNotificationException Falls ein unerwarteter Fehler passiert.
	 */
	public patchOfKursSchienenAnzahl(pKursID : number, pAnzahlSchienenNeu : number) : void {
		const kursG : GostBlockungKurs = this.getKursG(pKursID);
		const kursE : GostBlockungsergebnisKurs = this.getKursE(pKursID);
		const nSchienen : number = this._parent.getSchienenAnzahl();
		if (!this._parent.getIstBlockungsVorlage())
			throw new DeveloperNotificationException("Die Schienenanzahl eines Kurses darf nur bei der Blockungsvorlage verändert werden!")
		if (kursE.anzahlSchienen !== kursG.anzahlSchienen)
			throw new DeveloperNotificationException("Der GostBlockungKurs hat " + kursG.anzahlSchienen + " Schienen, der GostBlockungsergebnisKurs hat hingegen " + kursE.anzahlSchienen + " Schienen!")
		if (nSchienen === 0)
			throw new DeveloperNotificationException("Die Blockung hat 0 Schienen. Das darf nicht passieren!")
		if (pAnzahlSchienenNeu <= 0)
			throw new DeveloperNotificationException("Ein Kurs muss mindestens einer Schiene zugeordnet sein, statt " + pAnzahlSchienenNeu + " Schienen!")
		if (pAnzahlSchienenNeu > nSchienen)
			throw new DeveloperNotificationException("Es gibt nur " + nSchienen + " Schienen, der Kurs kann nicht " + pAnzahlSchienenNeu + " Schienen zugeordnet werden!")
		while (pAnzahlSchienenNeu > kursG.anzahlSchienen) {
			let hinzugefuegt : boolean = false;
			for (let nr : number = 1; (nr <= this._map_schienenNr_schiene.size()) && (!hinzugefuegt); nr++) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (!kursE.schienen.contains(schiene.id)) {
					hinzugefuegt = true;
					kursG.anzahlSchienen++;
					kursE.anzahlSchienen++;
					this.setKursSchiene(pKursID, schiene.id, true);
				}
			}
			if (!hinzugefuegt)
				throw new DeveloperNotificationException("Es wurde keine freie Schiene für den Kurs " + pKursID + " gefunden!")
		}
		while (pAnzahlSchienenNeu < kursG.anzahlSchienen) {
			let entfernt : boolean = false;
			for (let nr : number = this._map_schienenNr_schiene.size(); (nr >= 1) && (!entfernt); nr--) {
				const schiene : GostBlockungsergebnisSchiene = this.getSchieneEmitNr(nr);
				if (kursE.schienen.contains(schiene.id)) {
					entfernt = true;
					kursG.anzahlSchienen--;
					kursE.anzahlSchienen--;
					this.setKursSchiene(pKursID, schiene.id, false);
				}
			}
			if (!entfernt)
				throw new DeveloperNotificationException("Es wurde keine belegte Schiene von Kurs " + pKursID + " gefunden!")
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
	 * @param pLogger Ein Logger für Debug-Zwecke.
	 */
	public debug(pLogger : Logger) : void {
		pLogger.modifyIndent(+4);
		pLogger.logLn("----- Kurse sortiert nach Fachart -----");
		for (const fachartID of this._map_fachartID_kurse.keySet()) {
			pLogger.logLn("FachartID = " + fachartID! + " (KD = " + this.getOfFachartKursdifferenz(fachartID!) + ")");
			for (const kurs of this.getOfFachartKursmenge(fachartID!)) {
				pLogger.logLn("    " + this.getOfKursName(kurs.id)! + " : " + kurs.schueler.size() + " SuS");
			}
		}
		pLogger.logLn("KursdifferenzMax = " + this._ergebnis.bewertung.kursdifferenzMax);
		pLogger.logLn("KursdifferenzHistogramm = " + Arrays.toString(this._ergebnis.bewertung.kursdifferenzHistogramm)!);
		pLogger.modifyIndent(-4);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsergebnisManager(obj : unknown) : GostBlockungsergebnisManager {
	return obj as GostBlockungsergebnisManager;
}

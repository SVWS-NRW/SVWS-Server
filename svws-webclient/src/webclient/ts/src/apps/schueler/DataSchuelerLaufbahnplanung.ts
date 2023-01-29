import { App } from "../BaseApp";
import { List, Vector, GostHalbjahr, GostKursart, Jahrgaenge, Fachgruppe, Schulgliederung,
	Abiturdaten, AbiturdatenManager, AbiturFachbelegung, AbiturFachbelegungHalbjahr,
	GostSchuelerFachwahl, GostAbiturjahrUtils, GostBelegpruefungErgebnis, GostBelegpruefungsArt,
	ZulaessigesFach, GostFach, GostFachbereich,
	SchuelerListeEintrag,
	Sprachbelegung, SprachendatenUtils } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { reactive } from "vue";
import { mainApp } from "../Main";
import { DataGostFaecher } from "../gost/DataGostFaecher";
import { DataGostJahrgang } from "../gost/DataGostJahrgang";
import { DataSchuleStammdaten } from "../schule/DataSchuleStammdaten";

/** Signatur für die Sprachbelegungen */
interface GostSprachbelegungen {
	[fachID: string]: Sprachbelegung;
}

class DataSchuelerLaufbahnplanungReactiveState {
	/**
	 * Eine Liste der Fächer der gymnasialen Oberstufe, welche aus dem SVWS-Server
	 * ausgelesen wurden, aber ohne Vertiefungs- und Projektkursfächer
	 */
	gostFaecherOhnePJKundVTF: Array<GostFach> = [];

	/**
	 * Die Fachbelegungen der derzeitigen Laufbahndaten der gymnasialen Oberstufe
	 * als assoziatives Array
	 */
	gostFachbelegungen: Array<AbiturFachbelegung> = [];

	/**
	 * Die Sprachbelegungen der derzeitigen Laufbahndaten der gymnasialen
	 * Oberstufe als assoziatives Array
	 */
	gostSprachbelegungen: GostSprachbelegungen = {};

	/** Das Belegprüfungsergbnis der aktuellen Belegprüfungsart für die gymnasiale Oberstufe */
	gostBelegpruefungsErgebnis: GostBelegpruefungErgebnis =
		new GostBelegpruefungErgebnis();

	/** Die Art der Belegprüfung: Nur EF1 oder Gesamt */
	gostBelegpruefungsart: GostBelegpruefungsArt = GostBelegpruefungsArt.GESAMT;

	_kurszahlen = [0, 0, 0, 0, 0, 0];
	_wochenstunden = [0, 0, 0, 0, 0, 0];
	_anrechenbare_kurse = [0, 0, 0, 0, 0, 0];
}

export class DataSchuelerLaufbahnplanung extends BaseData<Abiturdaten, SchuelerListeEintrag, AbiturdatenManager> {

	protected _data = reactive(new DataSchuelerLaufbahnplanungReactiveState());

	protected _dataGostFaecher: DataGostFaecher | undefined;
	protected _dataGostJahrgang: DataGostJahrgang | undefined;
	protected _dataSchule: DataSchuleStammdaten | undefined;

	public constructor() {
		super();
	}

	protected on_update(daten: Partial<GostSchuelerFachwahl>): void {
		// TODO
		return void daten;
	}

	get dataGostFaecher(): DataGostFaecher | undefined {
		return this._dataGostFaecher;
	}

	set dataGostFaecher(value: DataGostFaecher | undefined) {
		this._dataGostFaecher = value;
	}

	get gostFaecher(): List<GostFach> {
		return this._dataGostFaecher?.daten || new Vector()
	}

	get dataGostJahrgang(): DataGostJahrgang | undefined {
		return this._dataGostJahrgang;
	}

	set dataGostJahrgang(value: DataGostJahrgang | undefined) {
		this._dataGostJahrgang = value;
	}

	get dataSchule(): DataSchuleStammdaten | undefined {
		return this._dataSchule;
	}

	set dataSchule(value: DataSchuleStammdaten | undefined) {
		this._dataSchule = value;
	}

	get gostFachbelegungen(): Array<AbiturFachbelegung> {
		return this._data.gostFachbelegungen;
	}

	/**
	 * Getter für die Eigenschaft abiturjahr.
	 *
	 * @returns {number | undefined} Liefert das Abiturjahr für den Schüler der
	 *   gymnasialen Oberstufe
	 */
	get abiturjahr(): number | undefined {
		if (!this.selected_list_item || !mainApp.config.hasGost)
			return undefined;
		const gliederung: Schulgliederung | null = Schulgliederung.getByKuerzel(
			this.selected_list_item.schulgliederung
		);
		const schulform = this._dataSchule?.schulform.value || null;
		if (gliederung === null || schulform === null)
			return undefined;
		const schuljahr = App.akt_abschnitt;
		const result = GostAbiturjahrUtils.getGostAbiturjahr(
			schulform, gliederung, schuljahr?.schuljahr || 0, this.selected_list_item.jahrgang
		);
		return result ?? undefined;
	}

	/**
	 * Prüft, ob der aktuell ausgewählte Schüler eine Laufbahn der gymnasialen
	 * Oberstufe hat oder nicht.
	 *
	 * @returns {boolean} True, falls gymnasiale Laufbahndaten zur Verfügung stehen.
	 */
	private hasGostlaufbahn(): boolean {
		const schulform = this._dataSchule?.schulform.value || null;
		return (!!schulform && mainApp.config.hasGost && this.abiturjahr !== undefined && this.abiturjahr !== -1);
	}

	/**
	 * Getter für die aktuelle gostBelegpruefungsart
	 *
	 * @returns {GostBelegpruefungsArt} Die aktuelle Belegprüfungsart
	 */
	get gostAktuelleBelegpruefungsart(): GostBelegpruefungsArt {
		return this._data.gostBelegpruefungsart;
	}

	/**
	 * Setter für die Belegprüfungsart.
	 *
	 * @param {GostBelegpruefungsArt} value Die neue aktuelle Belegprüfungsart
	 */
	set gostAktuelleBelegpruefungsart(value: GostBelegpruefungsArt) {
		this._data.gostBelegpruefungsart = value;
		this.set_manager();
		this.gostBelegpruefung();
	}

	/**
	 * Getter für das Belegprüfungsergebnis
	 */
	get gostBelegpruefungsErgebnis() {
		return this._data.gostBelegpruefungsErgebnis;
	}

	/**
	 * Führt eine Belegprüfung für die gymnasiale Oberstufe mit den aktuellen
	 * Einstellungen durch.
	 *
	 * @returns {void}
	 */
	private gostBelegpruefung(): void {
		if (this._daten === undefined || this.manager === undefined) return;
		this._data.gostBelegpruefungsErgebnis = this.manager.getBelegpruefungErgebnis();
		this._data._wochenstunden = this.manager.getWochenstunden();
		this._data._anrechenbare_kurse = this.manager.getAnrechenbareKurse();
	}

	/** aktualisiert den Abiturdatenmanager, z.B. wenn sich die Belegprüfungsart ändert */
	private set_manager() {
		if (this._daten === undefined)
			return;
		const art = (this._data.gostBelegpruefungsart.kuerzel === GostBelegpruefungsArt.GESAMT.kuerzel)
			? GostBelegpruefungsArt.GESAMT
			: GostBelegpruefungsArt.EF1;
		this.manager = new AbiturdatenManager(this._daten, this.gostFaecher, art);
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuelerStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<Abiturdaten | undefined> {
		try {
			await super._select((eintrag: SchuelerListeEintrag) => App.api.getGostSchuelerLaufbahnplanung(App.schema, eintrag.id));
		} catch(e) {
			return undefined;
		}
		if (!this._daten)
			return undefined;
		if (!this.gostFaecher.size() && this.hasGostlaufbahn() && this.abiturjahr !== undefined && this.abiturjahr !== -1) {
			this._data.gostFaecherOhnePJKundVTF = []
			for (const fach of this.gostFaecher)
				if (!this.istVertiefungsOderProjektkursfach(fach))
					this._data.gostFaecherOhnePJKundVTF.push(fach)
		}
		this._data._kurszahlen = [0, 0, 0, 0, 0, 0];
		this._data._wochenstunden = [0, 0, 0, 0, 0, 0];
		if (this._daten.abiturjahr > 0) {
			this._data.gostFachbelegungen = [];
			for (const belegung of this._daten.fachbelegungen) {
				this._data.gostFachbelegungen[belegung.fachID] = belegung;
			}
			this._data.gostSprachbelegungen = {};
			for (const belegung of this._daten.sprachendaten.belegungen) {
				if (belegung.sprache)
					this._data.gostSprachbelegungen[belegung.sprache] = belegung;
			}
			this.set_manager();
			this.gostBelegpruefung();
		} else {
			this._data.gostFachbelegungen = [];
			this._data.gostSprachbelegungen = {};
			this._data.gostBelegpruefungsErgebnis = new GostBelegpruefungErgebnis();
		}
		return this._daten;
	}

	public ist_VTF(fach: GostFach): boolean {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe() === Fachgruppe.FG_VX;
	}

	public ist_PJK(fach: GostFach): boolean {
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe() === Fachgruppe.FG_PX;
	}

	public istVertiefungsOderProjektkursfach(fach: GostFach): boolean {
		return this.ist_VTF(fach) || this.ist_PJK(fach);
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuelerStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<Abiturdaten>): Promise<boolean> {
		data;
		return false;
	}

	get wochenstunden(): Array<number> {
		return this._data._wochenstunden;
	}
	get anrechenbare_kurszahlen(): Array<number> {
		return this._data._anrechenbare_kurse;
	}
	get kurszahlen(): Array<number> {
		return this._data._kurszahlen;
	}

	public getWahlen(row: GostFach): Array<string> {
		if (!row.id) return [];
		const fachbelegung = this._data.gostFachbelegungen[row.id] || new AbiturFachbelegung();
		return fachbelegung.belegungen.map(
			(b: AbiturFachbelegungHalbjahr | null) => {
				b = b ? b : new AbiturFachbelegungHalbjahr();
				if (!b.halbjahrKuerzel)
					return "";
				const kursart = GostKursart.fromKuerzel(b.kursartKuerzel);
				if (!kursart)
					return b.kursartKuerzel.toString() || "";
				switch (kursart) {
				case GostKursart.ZK:
				case GostKursart.LK:
					return kursart.kuerzel;
				}
				return b.schriftlich ? "S" : "M";
			}
		);
	}

	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostFach} row
	 * @returns {string}
	 */
	public getBgColor(row: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzel).getHMTLFarbeRGB();
	}

	public getBgColorIfLanguage(row: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzel).daten.istFremdsprache ? this.getBgColor(row) : "gray";
	}

	private sprachbelegung(row: GostFach): Sprachbelegung | undefined {
		return this._data.gostSprachbelegungen[ZulaessigesFach.getByKuerzelASD(row.kuerzel).daten.kuerzel];
	}

	public sprachenfolgeNr(row: GostFach): number {
		if (this.getFallsSpracheMoeglich(row))
			return this.sprachbelegung(row)?.reihenfolge ?? 0;
		else
			return 0;
	}

	public sprachenfolgeJahrgang(row: GostFach): string {
		if (this.getFallsSpracheMoeglich(row))
			return (this.sprachbelegung(row)?.belegungVonJahrgang ?? "");
		else
			return "";
	}

	/**
	 * Prüft, ob eine Sprache bisher schon unterrichtet wurde oder neu einsetzend ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getFallsSpracheMoeglich(row: GostFach): boolean {
		if (!this._daten) return false;
		const ist_fortfuehrbar = SprachendatenUtils.istFortfuehrbareSpracheInGOSt(
			this._daten.sprachendaten, ZulaessigesFach.getByKuerzelASD(row.kuerzel).daten.kuerzel
		);
		//TODO, warum muss diese Zeile hier rein? Sonst Fehler mit Sprachenfolge in Laufbahnplanung
		this.sprachbelegung(row);
		return ((ist_fortfuehrbar && !row.istFremdSpracheNeuEinsetzend) || (!ist_fortfuehrbar && row.istFremdSpracheNeuEinsetzend));
	}

	/**
	 * Prüft, ob ein Fach bereits belegt ist durch ein gleichnamiges Fach, z.B. bei Bili-Fächern
	 * @param {GostFach} row Das GostFach
	 * @param {GostHalbjahr} hj Das GostHalbjahr
	 * @returns {boolean} ob doppel belegt wurde, z.B. bei Bili-Fächern
	 */
	private checkDoppelbelegung(row: GostFach, hj: GostHalbjahr): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_VX)
			return false;
		const fachbelegungen = this.manager?.getFachbelegungByFachkuerzel(row.kuerzel);
		if (fachbelegungen !== undefined) {
			for (const fachbelegung of fachbelegungen) {
				if (this.manager?.pruefeBelegung(fachbelegung, hj)) {
					if (fachbelegung.fachID !== row.id)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gibt an, ob die Fachwahl in der EF1 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getEF1Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_ME || fach.getFachgruppe() === Fachgruppe.FG_PX
				|| (row.istFremdsprache && !this.getFallsSpracheMoeglich(row)))
			return false;
		return this.checkDoppelbelegung(row, GostHalbjahr.EF1)
			? false
			: row.istMoeglichEF1;
	}

	/**
	 * Gibt an, ob die Fachwahl in der EF2 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getEF2Moeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_ME || fach.getFachgruppe() === Fachgruppe.FG_PX
				|| (row.istFremdsprache && !this.getFallsSpracheMoeglich(row)))
			return false;
		return this.checkDoppelbelegung(row, GostHalbjahr.EF2)
			? false
			: row.istMoeglichEF2;
	}

	/**
	 * Gibt an, ob die Fachwahl in der Q11 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ11Moeglich(row: GostFach): boolean {
		if (this.checkDoppelbelegung(row, GostHalbjahr.Q11))
			return false;
		if (row.istMoeglichQ11) {
			return row.istFremdsprache ? this.getFallsSpracheMoeglich(row) : true;
		} else
			return row.istMoeglichQ11;
	}

	/**
	 * Gibt an, ob die Fachwahl in der Q12 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ12Moeglich(row: GostFach): boolean {
		if (this.checkDoppelbelegung(row, GostHalbjahr.Q12))
			return false;
		if (row.istMoeglichQ12) {
			return row.istFremdsprache ? this.getFallsSpracheMoeglich(row) : true;
		} else
			return row.istMoeglichQ12;
	}

	/**
	 * Gibt an, ob die Fachwahl in der Q21 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ21Moeglich(row: GostFach): boolean {
		if (this.checkDoppelbelegung(row, GostHalbjahr.Q21))
			return false;
		if (row.istMoeglichQ21) {
			return row.istFremdsprache ? this.getFallsSpracheMoeglich(row) : true;
		} else
			return false;
	}

	/**
	 * Gibt an, ob die Fachwahl in der Q22 möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getQ22Moeglich(row: GostFach): boolean {
		if (this.checkDoppelbelegung(row, GostHalbjahr.Q22))
			return false;
		if (row.istMoeglichQ22) {
			return row.istFremdsprache ? this.getFallsSpracheMoeglich(row) : true;
		} else
			return false;
	}


	/**
	 * Schickt die Fachwahl an den Server
	 *
	 * @param {GostFach} row
	 * @param {GostSchuelerFachwahl} wahl
	 * @returns {void}
	 */
	public setWahl(row: GostFach, wahl: GostSchuelerFachwahl): void {
		const eintrag = this.selected_list_item;
		if (!eintrag)
			return;
		void this._patch(wahl, () => App.api.patchGostSchuelerFachwahl(wahl, App.schema, eintrag.id, row.id).then(async () => { await this.on_select() }));
	}

}

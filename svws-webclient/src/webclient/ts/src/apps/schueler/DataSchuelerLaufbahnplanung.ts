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

	/** Legt fest, ob die Eingabe frei gewählt werden darf oder Einschränkungen eingestellt sind */
	public manuelle_eingabe = false;

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
		return result === null ? undefined : result.valueOf();
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
		this._data._wochenstunden = this.manager.getWochenstunden() || [];
		this._data._anrechenbare_kurse = this.manager.getAnrechenbareKurse() || [];
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
		await super._select((eintrag: SchuelerListeEintrag) => App.api.getGostSchuelerLaufbahnplanung(App.schema, eintrag.id));
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
					this._data.gostSprachbelegungen[belegung.sprache.valueOf()] = belegung;
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

	//TODO -> helfer
	public ist_VTF(fach: GostFach): boolean {
		if (!fach.kuerzel) return false;
		const fg = ZulaessigesFach.getByKuerzelASD(
			fach.kuerzel
		).getFachgruppe();
		return fg === Fachgruppe.FG_VX;
	}

	public ist_PJK(fach: GostFach): boolean {
		if (!fach.kuerzel) return false;
		const fg = ZulaessigesFach.getByKuerzelASD(
			fach.kuerzel
		).getFachgruppe();
		return fg === Fachgruppe.FG_PX;
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
					return kursart.kuerzel.valueOf();
				}
				return b.schriftlich ? "S" : "M";
			}
		);
	}


	/**
	 * Gibt die Belegungsdaten für die aktuelle Sprache zurück
	 *
	 * @param {GostFach} row
	 * @param {GostHalbjahr} halbjahr
	 * @returns {string}
	 */
	public getWahl(row: GostFach, halbjahr: GostHalbjahr): string | null {
		if (!row.id)
			return null;
		const fachbelegung = this._data.gostFachbelegungen[row.id] || new AbiturFachbelegung();
		let halbjahresbelegung = fachbelegung.belegungen[halbjahr.id];
		if (!halbjahresbelegung) {
			halbjahresbelegung = new AbiturFachbelegungHalbjahr();
			halbjahresbelegung.halbjahrKuerzel = halbjahr.kuerzel;
			fachbelegung.belegungen[halbjahr.id] = halbjahresbelegung;
		}
		const kursart = GostKursart.fromKuerzel(halbjahresbelegung.kursartKuerzel);
		if (kursart === null)
			return halbjahresbelegung.kursartKuerzel.toString() || null;
		switch (kursart) {
		case GostKursart.ZK:
		case GostKursart.LK:
			return kursart.kuerzel.valueOf();
		}
		return halbjahresbelegung.schriftlich ? "S" : "M";
	}


	/**
	 * Gibt den Farbcode für das Fach zurück
	 *
	 * @param {GostFach} row
	 * @returns {string}
	 */
	public getBgColor(row: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzel).getHMTLFarbeRGB().valueOf();
	}

	public getBgColorIfLanguage(row: GostFach): string {
		return ZulaessigesFach.getByKuerzelASD(row.kuerzel).daten.istFremdsprache ? this.getBgColor(row) : "gray";
	}

	private sprachbelegung(row: GostFach): Sprachbelegung | undefined {
		return this._data.gostSprachbelegungen[ZulaessigesFach.getByKuerzelASD(row.kuerzel).daten.kuerzel.valueOf()];
	}

	public sprachenfolgeNr(row: GostFach): number {
		if (this.getFallsSpracheMoeglich(row))
			return this.sprachbelegung(row)?.reihenfolge?.valueOf() || 0;
		else
			return 0;
	}

	public sprachenfolgeJahrgang(row: GostFach): string {
		if (this.getFallsSpracheMoeglich(row))
			return (this.sprachbelegung(row)?.belegungVonJahrgang?.valueOf() || "");
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
	 * Gibt an, ob die Fachwahl als Grundkurs möglich ist
	 *
	 * @param {GostFach} row
	 * @returns {boolean}
	 */
	public getAbiGKMoeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_ME
				|| fach.getFachgruppe() === Fachgruppe.FG_VX
				|| fach.getFachgruppe() === Fachgruppe.FG_PX)
			return false;
		return row.istMoeglichAbiGK;
	}

	/**
	 * Gibt an, ob die Fachwahl als LK möglich ist
	 *
	 * @param {GostFach} row
	 *
	 * @returns {boolean}
	 */
	public getAbiLKMoeglich(row: GostFach): boolean {
		const fach = ZulaessigesFach.getByKuerzelASD(row.kuerzel);
		if (fach.getFachgruppe() === Fachgruppe.FG_ME
				|| fach.getFachgruppe() === Fachgruppe.FG_VX
				|| fach.getFachgruppe() === Fachgruppe.FG_PX || fach.getJahrgangAb() === Jahrgaenge.JG_EF
				|| (row.biliSprache === null && row.biliSprache === "D"))
			return false;
		return row.istMoeglichAbiLK;
	}

	/**
	 * Gibt an, ob die Fachwahl als Abiturfach möglich ist
	 *
	 * @param {GostFach} row
	 *
	 * @returns {boolean}
	 */
	public getAbiMoeglich(row: GostFach): boolean {
		if (!row?.id)
			return false;
		const fachbelegung = this._data.gostFachbelegungen[row.id];
		if (!fachbelegung?.letzteKursart)
			return false;
		switch (GostKursart.fromKuerzel(fachbelegung.letzteKursart)) {
		case GostKursart.LK:
			return this.getAbiLKMoeglich(row);
		case GostKursart.GK: {
			const hjQ11 = fachbelegung.belegungen?.[GostHalbjahr.Q11.id];
			const hjQ12 = fachbelegung.belegungen?.[GostHalbjahr.Q12.id];
			const hjQ21 = fachbelegung.belegungen?.[GostHalbjahr.Q21.id];
			const hjQ22 = fachbelegung.belegungen?.[GostHalbjahr.Q22.id];
			if (!hjQ11?.kursartKuerzel
						|| !hjQ12?.kursartKuerzel
						|| !hjQ21?.kursartKuerzel
						|| !hjQ22?.kursartKuerzel)
				return false;
			if (GostKursart.fromKuerzel(hjQ11.kursartKuerzel) !== GostKursart.GK
						|| GostKursart.fromKuerzel(hjQ12.kursartKuerzel) !== GostKursart.GK
						|| GostKursart.fromKuerzel(hjQ21.kursartKuerzel) !== GostKursart.GK
						|| GostKursart.fromKuerzel(hjQ22.kursartKuerzel) !== GostKursart.GK)
				return false;
			if (!hjQ11.schriftlich || !hjQ12.schriftlich || !hjQ21.schriftlich)
				return false;
			return this.getAbiGKMoeglich(row);
		}
		}
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
		this._patch(wahl, () => App.api.patchGostSchuelerFachwahl(wahl, App.schema, eintrag.id, row.id).then(() => { this.on_select(); }));
	}

	/**
	 * TODO besser machen Diese Methode holt die Fachwahlen und gibt ein
	 * ordentliches Objekt zurück Ist zum Patchen praktisch
	 *
	 * @param {GostFach} row
	 * @returns {GostSchuelerFachwahl}
	 */
	public extract_fachwahl(row: GostFach): GostSchuelerFachwahl {
		const wahl = new GostSchuelerFachwahl();
		wahl.EF1 = this.getWahl(row, GostHalbjahr.EF1);
		wahl.EF2 = this.getWahl(row, GostHalbjahr.EF2);
		wahl.Q11 = this.getWahl(row, GostHalbjahr.Q11);
		wahl.Q12 = this.getWahl(row, GostHalbjahr.Q12);
		wahl.Q21 = this.getWahl(row, GostHalbjahr.Q21);
		wahl.Q22 = this.getWahl(row, GostHalbjahr.Q22);
		wahl.abiturFach = this._data.gostFachbelegungen[row.id]?.abiturFach || null;
		return wahl;
	}

	public stepper_manuell(row: GostFach, hj: 'EF1'|'EF2'|'Q11'|'Q12'|'Q21'|'Q22'|'abiturFach'): void {
		const wahl = this.extract_fachwahl(row);
		if (hj === 'abiturFach') {
			if (!wahl.Q22)
				return
			switch (wahl.abiturFach) {
			case null: wahl.abiturFach = wahl.Q22 === "LK" ? 1 : 3; break;
			case 1:    wahl.abiturFach = wahl.Q22 === "LK" ? 2 : 3; break;
			case 2:    wahl.abiturFach = wahl.Q22 === "LK" ? null : 3; break;
			case 3:    wahl.abiturFach = wahl.Q22 === "LK" ? null : 4; break;
			case 4:    wahl.abiturFach = null; break;
			default:   wahl.abiturFach = null; break;
			}
		} else {
			switch (wahl[hj]) {
			case "AT": wahl[hj] = null; break;
			case "ZK": wahl[hj] = null; break;
			case null: wahl[hj] = "M"; break;
			case "M":  wahl[hj] = "S"; break
			case "S":  wahl[hj] = "LK"; break;
			case "LK": {
				wahl[hj] = null
				if (GostFachbereich.SPORT.hat(row))
					wahl[hj] = "AT"
				if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row) || GostFachbereich.GESCHICHTE.hat(row))
					wahl[hj] = "ZK"
				break;
			}
			default:  wahl[hj] = null; break;
			}
		}
		this.setWahl(row, wahl)
	}

	/**
	 * Setzt die Werte für die Fachwahl EF1
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setEF1Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'EF1');
			return;
		}
		if (!row.id || !this.getEF1Moeglich(row) || this.daten?.bewertetesHalbjahr[0])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		switch (wahl.EF1) {
		case null: wahl.EF1 = ist_PJK_VTF ? "M" : "S"; break;
		case "S":  wahl.EF1 = "M"; break;
		case "M":  wahl.EF1 = null; break;
		}
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Fachwahl EF2
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setEF2Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'EF2');
			return;
		}
		if (!row.id || !this.getEF2Moeglich(row) || this.daten?.bewertetesHalbjahr[1])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		switch (wahl.EF2) {
		case null:
			wahl.EF2 = ist_PJK_VTF ? "M" : "S";
			break;
		case "S":
			wahl.EF2 = "M";
			break;
		case "M":
			wahl.EF2 = null;
			if (GostFachbereich.SPORT.hat(row)) wahl.EF2 = "AT";
			break;
		case "AT":
			wahl.EF2 = null;
		}
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Fachwahl Q11
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setQ11Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'Q11');
			return;
		}
		if (!row.id || !this.getQ11Moeglich(row) || this.daten?.bewertetesHalbjahr[2])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		const lk1_belegt = this._data.gostFachbelegungen.find(f => f && f.abiturFach === 1);
		const lk2_belegt = this._data.gostFachbelegungen.find(f => f && f.abiturFach === 2);
		switch (wahl.Q11) {
		case null: wahl.Q11 = (GostFachbereich.DEUTSCH.hat(row) || GostFachbereich.MATHEMATIK.hat(row)) ? "S" : "M"; break;
		case "M":  wahl.Q11 = ist_PJK_VTF ? null : "S"; break;
			//TODO S->S ist bestimmt falsch
		case "S":  wahl.Q11 = (row.istMoeglichAbiLK) ? "LK" : (GostFachbereich.DEUTSCH.hat(row) || GostFachbereich.MATHEMATIK.hat(row)) ? "S" : null; break;
		case "LK":
			wahl.Q11 = (GostFachbereich.DEUTSCH.hat(row) || GostFachbereich.MATHEMATIK.hat(row)) ? "S" : null;
			wahl.abiturFach = null;
			break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q11 && GostFachbereich.SPORT.hat(row))
			wahl.Q11 = "AT";
		else if (wahl.Q11 === "AT" && GostFachbereich.SPORT.hat(row))
			wahl.Q11 = null;
		// Q11 wählt bis Q22
		switch (wahl.Q11) {
		case null:
			if (!this.ist_VTF(row)) {
				wahl.Q12 = null;
				wahl.Q21 = null;
				wahl.Q22 = null;
			}
			if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row) && (this.dataGostJahrgang?.daten?.hatZusatzkursSW)) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursSW);
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) {
					wahl.Q11 = "ZK";
					wahl.Q12 = "ZK";
				}
			}
			if (GostFachbereich.GESCHICHTE.hat(row) && this.dataGostJahrgang?.daten?.hatZusatzkursGE) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursGE);
				if ((beginn !== null) && (beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) {
					wahl.Q11 = "ZK";
					wahl.Q12 = "ZK";
				}
			}
			break;
		case "M":
			if (row.istMoeglichQ12 && !this.ist_VTF(row))
				wahl.Q12 = wahl.Q11;
			if (!ist_PJK_VTF && !GostFachbereich.KUNST_MUSIK.hat(row) && !GostFachbereich.RELIGION.hat(row)) {
				if (row.istMoeglichQ21) wahl.Q21 = wahl.Q11;
				if (row.istMoeglichQ22) wahl.Q22 = wahl.Q11;
			}
			break;
		case "S":
			if (row.istMoeglichQ12) wahl.Q12 = wahl.Q11;
			if (!ist_PJK_VTF) {
				if (row.istMoeglichQ21)
					wahl.Q21 = wahl.Q11;
					// "S" kann nur für drittes Abifach gewählt werden, Vorauswahl daher "M"
				if (row.istMoeglichQ22)
					wahl.Q22 = "M";
			}
			break;
		case "ZK":
			const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row))
				? GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursSW || "")
				: GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursGE || "");
			wahl.Q11 = null;
			wahl.Q12 = null;
			break;
		case "LK":
			wahl.Q12 = row.istMoeglichQ12 ? wahl.Q11 : null;
			wahl.Q21 = row.istMoeglichQ21 ? wahl.Q11 : null;
			wahl.Q22 = row.istMoeglichQ22 ? wahl.Q11 : null;
			// Bedingungen für LK1
			if (GostFachbereich.DEUTSCH.hat(row) || GostFachbereich.MATHEMATIK.hat(row)
						|| GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(row)
						|| (GostFachbereich.FREMDSPRACHE.hat(row) && !row.istFremdSpracheNeuEinsetzend))
				wahl.abiturFach = !lk1_belegt ? 1 : lk2_belegt ? null : 2;
			else
				wahl.abiturFach = lk2_belegt ? null : 2;
			break;
		}
		if (wahl.Q11 === null || wahl.Q11 === "M")
			wahl.abiturFach = null;
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Fachwahl Q12
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setQ12Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'Q12');
			return;
		}
		if (!row.id || !this.getQ12Moeglich(row) || this.daten?.bewertetesHalbjahr[3])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		switch (wahl.Q12) {
		case null:
			wahl.Q12 = "M";
			if (this.ist_PJK(row) && wahl.Q11 === null && row.istMoeglichQ21) {
				wahl.Q21 = "M";
				wahl.Q22 = null;
			}
			if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row) && (this.dataGostJahrgang?.daten?.hatZusatzkursSW)) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursSW);
				if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)))) {
					if (beginn === GostHalbjahr.Q11)
						wahl.Q11 = "ZK";
					wahl.Q12 = "ZK";
					if (beginn === GostHalbjahr.Q12)
						wahl.Q21 = "ZK";
				}
			}
			if (GostFachbereich.GESCHICHTE.hat(row) && this.dataGostJahrgang?.daten?.hatZusatzkursGE) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursGE);
				if ((beginn !== null) && (((beginn === GostHalbjahr.Q11) && (wahl.EF2 === null)) || ((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)))) {
					if (beginn === GostHalbjahr.Q11)
						wahl.Q11 = "ZK";
					wahl.Q12 = "ZK";
					if (beginn === GostHalbjahr.Q12)
						wahl.Q21 = "ZK";
				}
			}
			break;
		case "M":
			wahl.Q12 = ist_PJK_VTF ? null : "S";
			break;
		case "S":
			wahl.Q12 = wahl.Q11 === "LK" ? "LK" : null;
			break;
		case "ZK":
			const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row))
				? GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursSW || "")
				: GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursGE || "");
			if ((beginn !== null) && (beginn === GostHalbjahr.Q11))
				wahl.Q11 = null;
			wahl.Q12 = null;
			if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
				wahl.Q21 = null;
			break;
			// TODO: Warum ist das so? Bis Q22. Was ist erlaubt: M, S, null?
		case "LK":
			wahl.Q12 = null;
			wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q12 && GostFachbereich.SPORT.hat(row))
			wahl.Q12 = "AT";
		else if (wahl.Q12 === "AT" && GostFachbereich.SPORT.hat(row))
			wahl.Q12 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q12 === null && !this.ist_VTF(row)) {
			wahl.Q21 = null;
			wahl.Q22 = null;
		}
		if (wahl.Q12 === null || wahl.Q12 === "M")
			wahl.abiturFach = null;
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Fachwahl Q21
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setQ21Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'Q21')
			return
		}
		if (!row.id || !this.getQ21Moeglich(row) || this.daten?.bewertetesHalbjahr[4])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		switch (wahl.Q21) {
		case null:
			wahl.Q21 = "M";
			if (this.ist_PJK(row) && wahl.Q12 === null && row.istMoeglichQ22) {
				wahl.Q22 = "M";
			}
			if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row) && (this.dataGostJahrgang?.daten?.hatZusatzkursSW)) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursSW);
				if ((beginn !== null) && (((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)) || ((beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)))) {
					if (beginn === GostHalbjahr.Q12)
						wahl.Q12 = "ZK";
					wahl.Q21 = "ZK";
					if (beginn === GostHalbjahr.Q21)
						wahl.Q22 = "ZK";
				}
			}
			if (GostFachbereich.GESCHICHTE.hat(row) && this.dataGostJahrgang?.daten?.hatZusatzkursGE) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursGE);
				if ((beginn !== null) && (((beginn === GostHalbjahr.Q12) && (wahl.Q11 === null)) || ((beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)))) {
					if (beginn === GostHalbjahr.Q12)
						wahl.Q12 = "ZK";
					wahl.Q21 = "ZK";
					if (beginn === GostHalbjahr.Q21)
						wahl.Q22 = "ZK";
				}
			}
			break;
		case "M":
			wahl.Q21 = ist_PJK_VTF ? null : "S";
			break;
		case "S":
			wahl.Q21 = wahl.Q12 === "LK" ? "LK" : null;
			break;
		case "ZK":
			const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row))
				? GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursSW || "")
				: GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursGE || "");
			if ((beginn !== null) && (beginn === GostHalbjahr.Q12))
				wahl.Q12 = null;
			wahl.Q21 = null;
			if ((beginn !== null) && (beginn === GostHalbjahr.Q21))
				wahl.Q22 = null;
			break;
		case "LK":
			wahl.Q21 = null;
			wahl.abiturFach = null;
			break;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q21 && GostFachbereich.SPORT.hat(row))
			wahl.Q21 = "AT";
		else if (wahl.Q21 === "AT" && GostFachbereich.SPORT.hat(row))
			wahl.Q21 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q21 === null && !this.ist_VTF(row))
			wahl.Q22 = null;
		if (wahl.Q21 === null || wahl.Q21 === "ZK")
			wahl.abiturFach = null;
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Fachwahl Q22
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setQ22Wahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'Q22');
			return;
		}
		if (!row.id || !this.getQ22Moeglich(row) || this.daten?.bewertetesHalbjahr[5])
			return;
		const wahl = this.extract_fachwahl(row);
		const ist_PJK_VTF = this.istVertiefungsOderProjektkursfach(row);
		switch (wahl.Q22) {
		case null:
			wahl.Q22 = "M";
			if (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row) && (this.dataGostJahrgang?.daten?.hatZusatzkursSW)) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursSW);
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)) {
					wahl.Q21 = "ZK";
					wahl.Q22 = "ZK";
				}
			}
			if (GostFachbereich.GESCHICHTE.hat(row) && this.dataGostJahrgang?.daten?.hatZusatzkursGE) {
				const beginn : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten.beginnZusatzkursGE);
				if ((beginn !== null) && (beginn === GostHalbjahr.Q21) && (wahl.Q12 === null)) {
					wahl.Q21 = "ZK";
					wahl.Q22 = "ZK";
				}
			}
			break;
		case "M":
			wahl.Q22 = ist_PJK_VTF ? null : "S";
			break;
		case "S":
			wahl.Q22 = wahl.Q21 === "LK" ? "LK" : null;
			break;
		case "ZK":
			const beginn : GostHalbjahr | null = (GostFachbereich.SOZIALWISSENSCHAFTEN.hat(row))
				? GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursSW || "")
				: GostHalbjahr.fromKuerzel(this.dataGostJahrgang?.daten?.beginnZusatzkursGE || "");
			if ((beginn !== null) && (beginn === GostHalbjahr.Q21)) {
				wahl.Q21 = null;
			}
			wahl.Q22 = null;
			break;
		case "LK":
			wahl.Q22 = null;
			wahl.abiturFach = null;
		}
		// Sonderfall Sport - darf AT haben
		if (!wahl.Q22 && GostFachbereich.SPORT.hat(row))
			wahl.Q22 = "AT";
		else if (wahl.Q22 === "AT" && GostFachbereich.SPORT.hat(row))
			wahl.Q22 = null;
		// Nachfolgende HJ ebenfalls setzen
		if (wahl.Q22 === null || wahl.Q22 === "ZK")
			wahl.abiturFach = null;
		if (wahl.abiturFach === 3 && wahl.Q22 === "M")
			wahl.abiturFach = this._data.gostFachbelegungen.find(f => f && f.abiturFach === 4) ? null : 4;
		if (wahl.abiturFach === 4 && wahl.Q22 === "S")
			wahl.abiturFach = this._data.gostFachbelegungen.find(f => f && f.abiturFach === 3) ? null : 3;
		this.setWahl(row, wahl);
	}

	/**
	 * Setzt die Werte für die Abiwahl
	 *
	 * @param {GostFach} row
	 * @returns {void}
	 */
	public setAbiturWahl(row: GostFach): void {
		if (this.manuelle_eingabe) {
			this.stepper_manuell(row, 'abiturFach');
			return;
		}
		if (!row.id || !this.getAbiMoeglich(row) || this.daten?.bewertetesHalbjahr[5])
			return;
		const wahl = this.extract_fachwahl(row);
		if (wahl.Q11 === "LK" && wahl.Q12 === "LK" && wahl.Q21 === "LK" && wahl.Q22 === "LK") {
			if (!this.getAbiLKMoeglich(row)) {
				wahl.abiturFach = null;
			} else {
				switch (wahl.abiturFach) {
				case 1:
					wahl.abiturFach = 2;
					break;
				case 2:
					// TODO Prüfe zuvor, ob das Fach überhaupt als erster LK wählbar ist
					wahl.abiturFach = 1;
					break;
				default:
					wahl.abiturFach = 2;
					break;
				}
			}
		} else if ((wahl.Q11 === "S" || wahl.Q11 === "LK") && (wahl.Q12 === "S" || wahl.Q12 === "LK")
				&& (wahl.Q21 === "S" || wahl.Q21 === "LK") && (wahl.Q22 === "S" || wahl.Q22 === "LK" || wahl.Q22 === "M")) {
			if (!this.getAbiGKMoeglich(row)) {
				wahl.abiturFach = null;
			} else {
				switch (wahl.abiturFach) {
				case null:
					wahl.abiturFach = wahl.Q22 === "M" ? 4 : 3;
					break;
				case 4:
					wahl.abiturFach = wahl.Q22 === "S" ? 3 : null;
					break;
				case 3:
					wahl.abiturFach = wahl.Q22 === "M" ? 4 : null;
					break;
				default:
					wahl.abiturFach = null;
					break;
				}
			}
		} else {
			wahl.abiturFach = null;
		}
		this.setWahl(row, wahl);
	}

	public reset_fachwahlen() {
		if (!this._daten || !this.manager)
			return;
		for (const fachbelegung of this._daten.fachbelegungen) {
			const fach = this.manager.getFach(fachbelegung);
			if (fach) {
				const fachwahl = this.extract_fachwahl(fach);
				let index = 0;
				for (const hj of this._daten.bewertetesHalbjahr) {
					if (!hj) {
						const gost_hj = GostHalbjahr.fromID(index)
						const name = gost_hj?.toString() as 'EF1'|'EF2'|'Q11'|'Q12'|'Q21'|'Q22'|undefined
						if (name)
							fachwahl[name] = null;
					}
					++index;
				}
				fachwahl.abiturFach = null;
				this.setWahl(fach, fachwahl);
			}
		}
	}
}

import { App } from "../BaseApp";
import { List, Vector, GostKursart, Fachgruppe, Schulgliederung,
	Abiturdaten, AbiturdatenManager, AbiturFachbelegung, AbiturFachbelegungHalbjahr,
	GostSchuelerFachwahl, GostAbiturjahrUtils, GostBelegpruefungErgebnis, GostBelegpruefungsArt,
	ZulaessigesFach, GostFach,
	SchuelerListeEintrag,
	Sprachbelegung, SprachendatenUtils } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { reactive, ShallowRef, shallowRef } from "vue";
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

	_kurszahlen = [0, 0, 0, 0, 0, 0];
	_wochenstunden = [0, 0, 0, 0, 0, 0];
	_anrechenbare_kurse = [0, 0, 0, 0, 0, 0];
}

export class DataSchuelerLaufbahnplanung extends BaseData<Abiturdaten, SchuelerListeEintrag, unknown> {

	protected _data = reactive(new DataSchuelerLaufbahnplanungReactiveState());

	protected _dataGostFaecher: DataGostFaecher | undefined;
	protected _dataGostJahrgang: DataGostJahrgang | undefined;
	protected _dataSchule: DataSchuleStammdaten | undefined;

	public abimanager: ShallowRef<AbiturdatenManager | undefined> = shallowRef(undefined);

	/** Die Art der Belegprüfung: Nur EF1 oder Gesamt */
	protected _gostBelegpruefungsart: GostBelegpruefungsArt = GostBelegpruefungsArt.GESAMT;


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
		if (!this.selected_list_item)
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
		return (!!schulform && this.abiturjahr !== undefined && this.abiturjahr !== -1);
	}

	/**
	 * Getter für die aktuelle gostBelegpruefungsart
	 *
	 * @returns {GostBelegpruefungsArt} Die aktuelle Belegprüfungsart
	 */
	get gostAktuelleBelegpruefungsart(): GostBelegpruefungsArt {
		return this._gostBelegpruefungsart;
	}

	/**
	 * Setter für die Belegprüfungsart.
	 *
	 * @param {GostBelegpruefungsArt} value Die neue aktuelle Belegprüfungsart
	 */
	set gostAktuelleBelegpruefungsart(value: GostBelegpruefungsArt) {
		this._gostBelegpruefungsart = value;
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
		if (this._daten === undefined || this.abimanager.value === undefined)
			return;
		this._data.gostBelegpruefungsErgebnis = this.abimanager.value.getBelegpruefungErgebnis();
		this._data._wochenstunden = this.abimanager.value.getWochenstunden();
		this._data._anrechenbare_kurse = this.abimanager.value.getAnrechenbareKurse();
	}

	/** aktualisiert den Abiturdatenmanager, z.B. wenn sich die Belegprüfungsart ändert */
	private set_manager() {
		if (this._daten === undefined)
			return;
		const art = (this._gostBelegpruefungsart.kuerzel === GostBelegpruefungsArt.GESAMT.kuerzel)
			? GostBelegpruefungsArt.GESAMT
			: GostBelegpruefungsArt.EF1;
		this.abimanager.value = new AbiturdatenManager(this._daten, this.gostFaecher, art);
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
					return b.kursartKuerzel;
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
	 * Schickt die Fachwahl an den Server
	 *
	 * @param {GostFach} row
	 * @param {GostSchuelerFachwahl} wahl
	 * @returns {void}
	 */
	public async setWahl(fachID: number, wahl: GostSchuelerFachwahl): Promise<void> {
		const eintrag = this.selected_list_item;
		if (!eintrag)
			return;
		await this._patch(wahl, () => App.api.patchGostSchuelerFachwahl(wahl, App.schema, eintrag.id, fachID).then(async () => { await this.on_select() }));
	}

}

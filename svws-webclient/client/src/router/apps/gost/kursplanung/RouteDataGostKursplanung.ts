
import { type Ref, ref, computed } from "vue";
import type { DownloadPDFTypen } from "~/components/gost/kursplanung/DownloadPDFTypen";
import type { KurseLeerenTypen } from "~/components/gost/kursplanung/KurseLeerenTypen";
import type { ApiPendingData } from "~/components/ApiStatus";
import type { ApiFile, GostBlockungKurs, GostBlockungKursLehrer, GostBlockungListeneintrag, GostBlockungSchiene, GostBlockungsergebnisKurs, GostJahrgangsdaten, GostStatistikFachwahl, JavaSet, LehrerListeEintrag, List, SchuelerListeEintrag, Schuljahresabschnitt, GostBlockungRegelUpdate} from "@core";
import { GostBlockungsdaten, GostBlockungsergebnis, ArrayList, DeveloperNotificationException, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFaecherManager, GostHalbjahr, SchuelerStatus, GostBlockungsergebnisKursSchuelerZuordnung, GostBlockungsergebnisKursSchuelerZuordnungUpdate, HashSet } from "@core";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeGostKursplanung } from "~/router/apps/gost/kursplanung/RouteGostKursplanung";
import { routeGostKursplanungSchueler } from "~/router/apps/gost/kursplanung/RouteGostKursplanungSchueler";
import { GostKursplanungSchuelerFilter } from "~/components/gost/kursplanung/GostKursplanungSchuelerFilter";

interface RouteStateGostKursplanung extends RouteStateInterface {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
	mapFachwahlStatistik: Map<number, GostStatistikFachwahl>;
	// ... die mit dem Abiturjahrgang aktualisiert/mitgeladen werden
	mapLehrer: Map<number, LehrerListeEintrag>;
	// ... auch abhängig vom ausgewählten Halbjahr der gymnasialen Oberstufe
	halbjahr: GostHalbjahr;
	halbjahrInitialisiert: boolean;
	mapBlockungen: Map<number, GostBlockungListeneintrag>;
	existiertSchuljahresabschnitt: boolean;
	// ...auch abhängig von der ausgewählten Blockung
	auswahlBlockung: GostBlockungListeneintrag | undefined;
	datenmanager: GostBlockungsdatenManager | undefined;
	// ...auch abhängig von dem ausgewählten Blockungsergebnis
	auswahlErgebnis: GostBlockungsergebnis | undefined;
	ergebnismanager: GostBlockungsergebnisManager | undefined;
	schuelerFilter: GostKursplanungSchuelerFilter;
	// ... auch abhängig von dem ausgewählten Schüler
	auswahlSchueler: SchuelerListeEintrag | undefined;
}

const emptySchuelerFilter = () => new GostKursplanungSchuelerFilter(undefined, undefined, new ArrayList(), new Map());

const defaultState: RouteStateGostKursplanung = {
	abiturjahr: undefined,
	jahrgangsdaten: undefined,
	mapSchueler: new Map(),
	faecherManager: new GostFaecherManager(),
	mapFachwahlStatistik: new Map(),
	mapLehrer: new Map(),
	halbjahr: GostHalbjahr.EF1,
	halbjahrInitialisiert: false,
	mapBlockungen: new Map(),
	existiertSchuljahresabschnitt: false,
	auswahlBlockung: undefined,
	datenmanager: undefined,
	auswahlErgebnis: undefined,
	ergebnismanager: undefined,
	schuelerFilter: emptySchuelerFilter(),
	auswahlSchueler: undefined,
};

export class RouteDataGostKursplanung extends RouteData<RouteStateGostKursplanung> {

	public constructor() {
		super(defaultState);
	}

	private _kursauswahl = ref<JavaSet<number>>(new HashSet<number>());

	public get hatAbiturjahr(): boolean {
		return this._state.value.abiturjahr !== undefined;
	}

	public get abiturjahrIstVorlage() : boolean {
		return (this._state.value.abiturjahr !== undefined) && (this._state.value.abiturjahr === -1);
	}

	public get abiturjahr() : number {
		if (this._state.value.abiturjahr === undefined)
			throw new DeveloperNotificationException("Es wurde noch kein Abiturjahrgang geladen.");
		return this._state.value.abiturjahr;
	}

	public setAbiturjahr = async (abiturjahr: number | undefined) => {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		if (abiturjahr === undefined) {
			this.setDefaultState();
			return;
		}
		api.status.start();
		// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
		const jahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, abiturjahr)
		const listSchueler = await api.server.getGostAbiturjahrgangSchueler(api.schema, abiturjahr);
		const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, abiturjahr);
		const faecherManager = new GostFaecherManager(listFaecher);
		// Lade die Schülerliste des Abiturjahrgangs
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listSchueler) {
			const status = SchuelerStatus.fromID(s.status);
			if ((status !== null) && ([SchuelerStatus.AKTIV, SchuelerStatus.EXTERN, SchuelerStatus.ABSCHLUSS, SchuelerStatus.BEURLAUBT, SchuelerStatus.NEUAUFNAHME, SchuelerStatus.WARTELISTE].includes(status)))
				mapSchueler.set(s.id, s);
		}
		// Lade die Lehrerliste
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer: Map<number, LehrerListeEintrag> = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		// Lade die Fachwahlstatistik
		const listFachwahlStatistik = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr);
		const mapFachwahlStatistik: Map<number, GostStatistikFachwahl> = new Map();
		for (const fw of listFachwahlStatistik)
			mapFachwahlStatistik.set(fw.id, fw);
		api.status.stop();
		// Setze den State neu
		this.setPatchedDefaultState({
			abiturjahr,
			jahrgangsdaten,
			mapSchueler,
			faecherManager,
			mapLehrer,
			halbjahr: this._state.value.halbjahr,
			halbjahrInitialisiert: false,
			mapFachwahlStatistik,
		});
	}

	public get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._state.value.jahrgangsdaten === undefined)
			throw new DeveloperNotificationException("Es wurde noch kein Abiturjahrgang geladen, so dass keine Jahrgangsdaten zur Verfügung stehen.");
		return this._state.value.jahrgangsdaten;
	}

	public get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this._state.value.mapSchueler;
	}

	public get faecherManager() : GostFaecherManager {
		return this._state.value.faecherManager;
	}

	public get mapFachwahlStatistik() : Map<number, GostStatistikFachwahl> {
		return this._state.value.mapFachwahlStatistik;
	}

	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	public get halbjahr() : GostHalbjahr {
		return this._state.value.halbjahr;
	}

	get blockungstabelleHidden(): boolean {
		return api.config.getValue("gost.kursplanung.kursansicht.ausgeblendet") === 'true';
	}

	setBlockungstabelleHidden = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.kursansicht.ausgeblendet', value ? "true" : "false");
	}

	get zeigeSchienenbezeichnungen(): boolean {
		return api.config.getValue("gost.kursplanung.kursansicht.zeigeSchienenbezeichnung") === 'true';
	}

	setZeigeSchienenbezeichnungen = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.kursansicht.zeigeSchienenbezeichnung', value ? "true" : "false");
	}

	get isSchuelerFilterOpen(): boolean {
		return api.config.getValue("gost.kursplanung.schueler.auswahl.filterOpen") === 'true';
	}

	setIsSchuelerFilterOpen = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.schueler.auswahl.filterOpen', value ? "true" : "false");
	}

	get showGeschlecht(): boolean {
		return api.config.getValue("gost.kursplanung.schueler.auswahl.geschlecht") === 'true';
	}

	setShowGeschlecht = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.schueler.auswahl.geschlecht', value ? "true" : "false");
	}

	get fixierteVerschieben(): boolean {
		return api.config.getValue("gost.kursplanung.umkursen.fixierteVerschieben") === 'true';
	}

	setFixierteVerschieben = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.umkursen.fixierteVerschieben', value ? "true" : "false");
	}

	get inZielkursFixieren(): boolean {
		return api.config.getValue("gost.kursplanung.umkursen.inZielkursFixieren") === 'true';
	}

	setInZielkursFixieren = async (value: boolean) => {
		await api.config.setValue('gost.kursplanung.umkursen.inZielkursFixieren', value ? "true" : "false");
	}

	public setHalbjahr = async (halbjahr: GostHalbjahr): Promise<boolean> => {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		const result : boolean = (halbjahr !== this._state.value.halbjahr);
		if ((!result) && (this._state.value.halbjahrInitialisiert === true))
			return result;
		// Lade die Liste der Blockungen
		api.status.start();
		const listBlockungen = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, this.abiturjahr, halbjahr.id);
		const mapBlockungen: Map<number, GostBlockungListeneintrag> = new Map();
		for (const bl of listBlockungen)
			mapBlockungen.set(bl.id, bl);
		let auswahlBlockung: GostBlockungListeneintrag | undefined = undefined;
		if (listBlockungen.size() > 0) {
			for (const bl of listBlockungen) {
				if (bl.istAktiv === true) {
					auswahlBlockung = bl;
					break;
				}
			}
			if (auswahlBlockung === undefined)
				auswahlBlockung = listBlockungen.get(0);
		}
		const schuljahr = halbjahr.getSchuljahrFromAbiturjahr(this._state.value.abiturjahr);
		const abschnitt : Schuljahresabschnitt | undefined = api.getAbschnittBySchuljahrUndHalbjahr(schuljahr, halbjahr.halbjahr);
		const existiertSchuljahresabschnitt = (abschnitt !== undefined);
		api.status.stop();
		this._kursauswahl.value.clear();
		this.setPatchedState({
			halbjahr,
			halbjahrInitialisiert: true,
			mapBlockungen,
			existiertSchuljahresabschnitt,
			auswahlBlockung,
			datenmanager: undefined,
			auswahlErgebnis: undefined,
			ergebnismanager: undefined,
			schuelerFilter: emptySchuelerFilter(),
		});
		return result;
	}

	public get existiertSchuljahresabschnitt() : boolean {
		return this._state.value.existiertSchuljahresabschnitt;
	}

	/**
	 * Gibt zurück, ob eine Blockung, also ein Datenmanager existiert. Es wird
	 * direkt auf dem State geprüft, da der Getter für den Datenmanager bei
	 * undefined einen Fehler erzeugen würde.
	 */
	public get hatBlockung(): boolean {
		return this._state.value.datenmanager !== undefined;
	}

	public get mapBlockungen(): Map<number, GostBlockungListeneintrag> {
		return this._state.value.mapBlockungen;
	}

	public get auswahlBlockung() : GostBlockungListeneintrag {
		if (this._state.value.auswahlBlockung === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine gültige Blockung ausgewählt.");
		return this._state.value.auswahlBlockung;
	}

	public setAuswahlBlockung = async (value: GostBlockungListeneintrag | undefined, force?: boolean) => {
		if (this._state.value.abiturjahr === undefined)
			throw new DeveloperNotificationException("Es kann keine Blockung ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (!force && (this._state.value.auswahlBlockung?.id === value?.id) && (this._state.value.datenmanager !== undefined))
			return;
		if (value === undefined) {
			this._kursauswahl.value.clear();
			this.setPatchedState({
				auswahlBlockung: undefined,
				datenmanager: undefined,
				auswahlErgebnis: undefined,
				ergebnismanager: undefined,
				schuelerFilter: emptySchuelerFilter(),
				auswahlSchueler: undefined,
			});
			return;
		}
		api.status.start();
		const blockungsdatenGzip = await api.server.getGostBlockungGZip(api.schema, value.id);
		const blockungsdatenBlob = await new Response(blockungsdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const blockungsdaten = GostBlockungsdaten.transpilerFromJSON(await blockungsdatenBlob.text());
		const datenmanager = new GostBlockungsdatenManager(blockungsdaten, this.faecherManager);
		const ergebnisse = datenmanager.ergebnisGetListeSortiertNachBewertung();
		api.status.stop();
		this._kursauswahl.value.clear();
		this.setPatchedState({
			auswahlBlockung: value,
			datenmanager,
			auswahlErgebnis: undefined,
			ergebnismanager: undefined,
			schuelerFilter: emptySchuelerFilter(),
		});
		let ergebnis : GostBlockungsergebnis | undefined = undefined;
		if (ergebnisse.size() > 0) {
			for (const e of ergebnisse) {
				if (e.istAktiv) {
					ergebnis = e;
					break;
				}
			}
			if (ergebnis === undefined)
				ergebnis = ergebnisse.get(0);
		}
		await this.setAuswahlErgebnis(ergebnis);
	}

	public get datenmanager(): GostBlockungsdatenManager {
		// Wurde bereits eine Blockung geladen, so dass kein Daten-Manager zur Verfügung steht?
		if (this._state.value.datenmanager === undefined) {
			// Wenn nicht, dann übergebe einen leeren Daten-Manager.
			return new GostBlockungsdatenManager();
		}
		return this._state.value.datenmanager;
	}

	public get kursAuswahl(): Ref<JavaSet<number>> {
		return this._kursauswahl;
	}

	public get ergebnismanager(): GostBlockungsergebnisManager {
		// Prüfe, ob bereits ein Ergebnis ausgewählt wurde
		if (this._state.value.ergebnismanager === undefined) {
			// Wenn nicht, dann erstelle einen leeren Ergebnis-Manager
			return new GostBlockungsergebnisManager(this.datenmanager, -1);
		}
		return this._state.value.ergebnismanager;
	}

	/**
	 * Gibt zurück, ob ein Blockungsergebnis, also ein Ergebnismanager existiert. Es wird
	 * direkt auf dem State geprüft, da der Getter für den Ergebnismanager bei
	 * undefined einen Fehler erzeugen würde.
	 */
	public get hatErgebnis(): boolean {
		return this._state.value.ergebnismanager !== undefined;
	}

	public get ergebnisse(): List<GostBlockungsergebnis> {
		return this.datenmanager.ergebnisGetListeSortiertNachBewertung();
	}

	public get auswahlErgebnis() : GostBlockungsergebnis {
		if (this._state.value.auswahlErgebnis === undefined)
			throw new DeveloperNotificationException("Es wurde noch kein gültiges Ergebnis ausgewählt.");
		return this._state.value.auswahlErgebnis;
	}

	public setAuswahlErgebnis = async (value: GostBlockungsergebnis | undefined) => {
		if (this._state.value.abiturjahr === undefined)
			throw new DeveloperNotificationException("Es kann kein Ergebnis ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if ((this._state.value.auswahlErgebnis?.id === value?.id) && (this._state.value.ergebnismanager !== undefined))
			return;
		if (value === undefined) {
			this.setPatchedState({
				auswahlErgebnis: undefined,
				ergebnismanager: undefined,
				schuelerFilter: emptySchuelerFilter(),
				auswahlSchueler: undefined,
			});
			return;
		}
		if (this._state.value.datenmanager === undefined)
			throw new DeveloperNotificationException("Es kann keine Ergebnis ausgewählt werden, wenn zuvor keine Blockung ausgewählt wurde.");
		api.status.start();
		const ergebnismanager = this.datenmanager.ergebnisManagerGet(value.id);
		const schuelerFilter = new GostKursplanungSchuelerFilter(this.datenmanager, () => this.ergebnismanager, this.faecherManager.faecher(), this.mapSchueler)
		api.status.stop();
		this.setPatchedState({
			auswahlErgebnis: value,
			ergebnismanager: ergebnismanager,
			schuelerFilter: schuelerFilter,
		});
	}

	public get schuelerFilter(): GostKursplanungSchuelerFilter {
		return this._state.value.schuelerFilter;
	}

	public get hatSchueler(): boolean {
		return this._state.value.auswahlSchueler !== undefined;
	}

	public get auswahlSchueler() : SchuelerListeEintrag {
		if (this._state.value.auswahlSchueler === undefined)
			throw new DeveloperNotificationException("Es wurde noch kein Schüler ausgewählt.");
		return this._state.value.auswahlSchueler;
	}

	public async setAuswahlSchueler(value: SchuelerListeEintrag | undefined) {
		if (this._state.value.abiturjahr === undefined)
			throw new DeveloperNotificationException("Es kann keine Ergebnis ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (value?.id === this._state.value.auswahlSchueler?.id)
			return;
		// Setze die neue Schülerauswahl im geklonten State
		this.setPatchedState({ auswahlSchueler: value });
	}

	addBlockung = api.call(async () => {
		if ((this._state.value.abiturjahr === undefined) || (this._state.value.abiturjahr === -1))
			return;
		const result = await api.server.createGostAbiturjahrgangBlockung(api.schema, this.jahrgangsdaten.abiturjahr, this.halbjahr.id);
		this.mapBlockungen.set(result.id, result);
		this.setPatchedState({mapBlockungen: this.mapBlockungen})
		await this.gotoBlockung(result);
	});

	restoreBlockung = api.call(async () => {
		const result = await api.server.restauriereGostBlockung(api.schema, this.jahrgangsdaten.abiturjahr, this.halbjahr.id)
		this.mapBlockungen.set(result.id, result);
		// Lade die Fächer des Abiturjahrgangs auch neu, da diese eventuell beim Wiederherstellen der Blockung angepasst wurden.
		const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, this.jahrgangsdaten.abiturjahr);
		const faecherManager = new GostFaecherManager(listFaecher);
		// Aktualisiere den State
		this.setPatchedState({
			faecherManager,
			mapBlockungen: this.mapBlockungen
		})
		await this.gotoBlockung(result);
	});

	removeBlockung = api.call(async () => {
		if (!this.hatBlockung)
			return;
		await api.server.deleteGostBlockung(api.schema, this.auswahlBlockung.id);
		this._state.value.mapBlockungen.delete(this.auswahlBlockung.id);
		await this.setAuswahlBlockung(undefined);
		await this.gotoHalbjahr(this.halbjahr);
	});

	patchBlockung = async (data: Partial<GostBlockungsdaten>, idBlockung: number): Promise<boolean> => {
		if (this._state.value.datenmanager === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine Blockung geladen, so dass die Blockung nicht angepasst werden kann.");
		api.status.start();
		await api.server.patchGostBlockung(data, api.schema, idBlockung);
		if (data.name)
			this.datenmanager.setName(data.name)
		if (data.istAktiv !== undefined) {
			if (data.istAktiv === true) {
				for (const blockung of this.mapBlockungen.values())
					blockung.istAktiv = (blockung.id === idBlockung);
			} else if (data.istAktiv === false) {
				const blockung = this.mapBlockungen.get(idBlockung);
				if (blockung !== undefined)
					blockung.istAktiv = false;
			}
			this.datenmanager.daten().istAktiv = (this.datenmanager.daten().id === idBlockung);
		}
		api.status.stop();
		this.commit();
		return true;
	}


	patchErgebnis = async (data: Partial<GostBlockungsergebnis>, idErgebnis: number): Promise<boolean> => {
		if (this._state.value.datenmanager === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine Blockung geladen, so dass die Ergebnisliste nicht angepasst werden kann.");
		api.status.start();
		await api.server.patchGostBlockungsergebnis(data, api.schema, idErgebnis);
		if (data.istAktiv === true) {
			for (const ergebnis of this.datenmanager.ergebnisGetListeSortiertNachBewertung())
				ergebnis.istAktiv = (ergebnis.id === idErgebnis);
		} else if (data.istAktiv === false) {
			this.datenmanager.ergebnisGet(idErgebnis).istAktiv = false;
		}
		api.status.stop();
		this.commit();
		return true;
	}

	patchKurs = api.call(async (data: Partial<GostBlockungKurs>, kurs_id: number) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		await api.server.patchGostBlockungKurs(data, api.schema, kurs_id);
		if (data.suffix !== undefined)
			this.datenmanager.kursSetSuffix(kurs_id, data.suffix);
		if (data.istKoopKurs !== undefined)
			this.datenmanager.kursGet(kurs_id).istKoopKurs = data.istKoopKurs;
		this.commit();
	});

	addKurs = api.call(async (fach_id : number, kursart_id : number): Promise<GostBlockungKurs | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		const kurs = await api.server.addGostBlockungKurs(api.schema, this.auswahlBlockung.id, fach_id, kursart_id);
		if (kurs === undefined)
			return;
		this.datenmanager.kursAdd(kurs);
		this.ergebnismanager.setAddKursByID(kurs.id);
		this.commit();
		return kurs;
	});

	removeKurse = api.call(async (ids: Iterable<number>) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		const list = new ArrayList<number>();
		for (const id of ids)
			list.add(id);
		await api.server.deleteGostBlockungKurse(list, api.schema);
		this.datenmanager.kurseRemoveByID(list);
		this.ergebnismanager.setRemoveKurseByID(list);
		this.commit();
	});

	combineKurs = api.call(async (kurs1: GostBlockungKurs, kurs2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => {
		if (kurs2 === undefined || kurs2 === null)
			return;
		await api.server.combineGostBlockungKurs(api.schema, kurs1.id, kurs2.id);
		this.ergebnismanager.setMergeKurseByID(kurs1.id, kurs2.id);
		this.commit();
	});

	splitKurs = api.call(async (kurs: GostBlockungKurs) => {
		const { kurs1, kurs2, schueler2 } = await api.server.splitGostBlockungKurs(api.schema, kurs.id);
		this.ergebnismanager.setSplitKurs(kurs1, kurs2, <number[]>schueler2.toArray())
		this.commit();
	});

	addSchieneKurs = api.call(async (kurs: GostBlockungKurs) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		this.ergebnismanager.patchOfKursSchienenAnzahl(kurs.id, kurs.anzahlSchienen + 1);
		const k = this.ergebnismanager.getKursE(kurs.id);
		await this.patchKurs(k, k.id);
		this.commit();
	});

	removeSchieneKurs = api.call(async (kurs: GostBlockungKurs) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis) || (kurs.anzahlSchienen <= 1))
			return;
		this.ergebnismanager.patchOfKursSchienenAnzahl(kurs.id, kurs.anzahlSchienen - 1);
		const k = this.ergebnismanager.getKursE(kurs.id);
		await this.patchKurs(k, k.id);
		this.commit();
	});

	addKursLehrer = api.call(async (kurs_id: number, lehrer_id: number): Promise<GostBlockungKursLehrer | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		const lehrer = await api.server.addGostBlockungKurslehrer(api.schema, kurs_id, lehrer_id);
		if (lehrer === undefined)
			return
		this.datenmanager.kursAddLehrkraft(kurs_id, lehrer);
		this.ergebnismanager.patchOfKursLehrkaefteChanged();
		this.commit();
		return lehrer;
	});

	removeKursLehrer = api.call(async (kurs_id: number, lehrer_id: number): Promise<void> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		await api.server.deleteGostBlockungKurslehrer(api.schema, kurs_id, lehrer_id);
		this.datenmanager.kursRemoveLehrkraft(kurs_id, lehrer_id);
		this.ergebnismanager.patchOfKursLehrkaefteChanged();
		this.commit();
	});

	patchSchiene = api.call(async (data: Partial<GostBlockungSchiene>, id : number) => {
		await api.server.patchGostBlockungSchiene(data, api.schema, id);
		const bezeichnung = data.bezeichnung;
		if (bezeichnung === undefined)
			return;
		const datenmanager = this.datenmanager;
		datenmanager.schienePatchBezeichnung(id, bezeichnung);
		this.setPatchedState({datenmanager});
	});

	addSchiene = api.call(async (): Promise<GostBlockungSchiene | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		const schiene = await api.server.addGostBlockungSchiene(api.schema, this.auswahlBlockung.id);
		if (schiene === undefined)
			return
		this.datenmanager.schieneAdd(schiene);
		this.ergebnismanager.setAddSchieneByID(schiene.id)
		this.commit();
		return schiene;
	});

	removeSchiene = api.call(async (schiene: GostBlockungSchiene) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		const result = await api.server.deleteGostBlockungSchieneByID(api.schema, schiene.id);
		if (!result)
			return;
		this.datenmanager.schieneRemoveByID(result.id);
		this.ergebnismanager.setRemoveSchieneByID(result.id);
		this.commit();
		return result;
	});

	updateKursSchienenZuordnung = api.call(async (idKurs: number, idSchieneAlt: number, idSchieneNeu: number): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		// TODO wie in updateKursSchuelerZuordnungen sollten die regelupdates von der Api zurückkommen
		await api.server.updateGostBlockungsergebnisKursSchieneZuordnung(api.schema, this._state.value.auswahlErgebnis.id, idSchieneAlt, idKurs, idSchieneNeu);
		this.ergebnismanager.setKursSchiene(idKurs, idSchieneAlt, false);
		this.ergebnismanager.setKursSchiene(idKurs, idSchieneNeu, true);
		const ergebnis = this.ergebnismanager.getErgebnis();
		this.datenmanager.ergebnisUpdateBewertung(ergebnis);
		this.commit();
		return true;
	});

	updateKursSchuelerZuordnungen = api.call(async (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate): Promise<boolean> => {
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return true;
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		// Aktualisiere die Zuordnungen ...
		const listRegelnAdded = await api.server.updateGostBlockungsergebnisKursSchuelerZuordnungen(update, api.schema, ergebnisid);
		for (const zuordnung of update.listEntfernen)
			this.ergebnismanager.setSchuelerKurs(zuordnung.idSchueler, zuordnung.idKurs, false);
		for (const zuordnung of update.listHinzuzufuegen)
			this.ergebnismanager.setSchuelerKurs(zuordnung.idSchueler, zuordnung.idKurs, true);
		const ergebnis = this.ergebnismanager.getErgebnis();
		this.datenmanager.ergebnisUpdateBewertung(ergebnis);
		if (!update.regelUpdates.listEntfernen.isEmpty() || !listRegelnAdded.isEmpty()) {
			update.regelUpdates.listHinzuzufuegen = listRegelnAdded;
			this.regelnUpdate(update.regelUpdates);
		}
		this.commit();
		return true;
	});

	updateKursSchuelerZuordnung = async (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined): Promise<boolean> => {
		// Erstelle das Update
		const update = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		if (idKursAlt !== undefined) {
			const zuordnungAlt = new GostBlockungsergebnisKursSchuelerZuordnung();
			zuordnungAlt.idSchueler = idSchueler;
			zuordnungAlt.idKurs = idKursAlt;
			update.listEntfernen.add(zuordnungAlt);
		}
		const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
		zuordnung.idSchueler = idSchueler;
		zuordnung.idKurs = idKursNeu;
		update.listHinzuzufuegen.add(zuordnung);
		return await this.updateKursSchuelerZuordnungen(update);
	};

	removeKursSchuelerZuordnung = api.call(async (zuordnungen: Iterable<GostBlockungsergebnisKursSchuelerZuordnung>): Promise<boolean> => {
		const liste: List<GostBlockungsergebnisKursSchuelerZuordnung> = new ArrayList();
		for (const zuordnung of zuordnungen) {
			if (this.ergebnismanager.getOfSchuelerOfKursIstZugeordnet(zuordnung.idSchueler, zuordnung.idKurs)) {
				liste.add(zuordnung);
				this.ergebnismanager.setSchuelerKurs(zuordnung.idSchueler, zuordnung.idKurs, false);
			} else {
				const map = this.ergebnismanager.getOfSchuelerMapIDzuUngueltigeKurse();
				const set = map.get(zuordnung.idSchueler);
				if (set !== null) {
					for (const kurs of set) {
						if (kurs.id === zuordnung.idKurs) {
							liste.add(zuordnung);
							this.ergebnismanager.setSchuelerKurs(zuordnung.idSchueler, zuordnung.idKurs, false);
							break;
						}
					}
				}
			}
		}
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined) || liste.size() === 0)
			return false;
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		await api.server.deleteGostBlockungsergebnisKursSchuelerZuordnungen(liste, api.schema, ergebnisid);
		const ergebnis = this.ergebnismanager.getErgebnis();
		this.datenmanager.ergebnisUpdateBewertung(ergebnis);
		this.commit();
		return true;
	});

	autoKursSchuelerZuordnung = api.call(async (idSchueler : number) => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		const zuordnungen = this.ergebnismanager.getOfSchuelerNeuzuordnungMitFixierung(idSchueler, false);
		for (const z of zuordnungen.fachwahlenZuKurs) {
			const kursV = this.ergebnismanager.getOfSchuelerOfFachZugeordneterKurs(idSchueler, z.fachID);
			const kursN = (z.kursID < 0) ? null : this.ergebnismanager.getKursE(z.kursID);
			if (kursV !== kursN) {
				if (kursV !== null) {
					await api.server.deleteGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, kursV.id);
					this.ergebnismanager.setSchuelerKurs(idSchueler, kursV.id, false);
				}
				if (kursN !== null) {
					await api.server.createGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, kursN.id);
					this.ergebnismanager.setSchuelerKurs(idSchueler, kursN.id, true);
				}
			}
		}
		const ergebnis = this.ergebnismanager.getErgebnis();
		this.datenmanager.ergebnisUpdateBewertung(ergebnis);
		this.commit();
	});

	addErgebnisse = api.call(async (ergebnisse: List<GostBlockungsergebnis>): Promise<void> => {
		if ((ergebnisse.isEmpty()) || (!this.hatBlockung))
			return;
		const idBlockung = this.datenmanager.daten().id;
		const ergebnisseMitIDs = await api.server.addGostBlockungErgebnisse(ergebnisse, api.schema, idBlockung);
		this.datenmanager.ergebnisAddListe(ergebnisseMitIDs);
		this.commit();
	});

	removeErgebnisse = api.call(async (ergebnisse: GostBlockungsergebnis[]): Promise<void> => {
		if ((ergebnisse.length <= 0) || (!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		const reselect = ergebnisse.some(e => e.id === ergebnisid);
		const liste = new ArrayList<GostBlockungsergebnis>();
		for (const ergebnis of ergebnisse) {
			await api.server.deleteGostBlockungsergebnis(api.schema, ergebnis.id);
			liste.add(ergebnis);
		}
		this.datenmanager.ergebnisRemoveListe(liste);
		this.commit();
		if (reselect) {
			for (const e of this.ergebnisse)
				if (!ergebnisse.includes(e)) {
					await this.gotoErgebnis(e);
					break;
				}
		}
	});

	rechneGostBlockung = async (): Promise<List<number>> => {
		const id = this.auswahlBlockung.id;
		let liste;
		try {
			api.status.start(<ApiPendingData>{ name: "gost.kursblockung.berechnen", id });
			liste = await api.server.rechneGostBlockung(api.schema, id, 5000);
			await this.setAuswahlBlockung(this.auswahlBlockung, true);
			await this.gotoErgebnis(this._state.value.auswahlErgebnis)
			api.status.stop();
		} catch (e) {
			api.status.stop(e instanceof Error ? e : undefined);
			throw e;
		}
		return liste;
	}

	ergebnisAbleiten = api.call(async () => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		const result = await api.server.dupliziereGostBlockungMitErgebnis(api.schema, this.auswahlErgebnis.id);
		this.mapBlockungen.set(result.id, result);
		this.setPatchedState({mapBlockungen: this.mapBlockungen})
		await this.gotoBlockung(result);
	});

	ergebnisHochschreiben = api.call(async () => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		const abiturjahr = this.abiturjahr;
		const halbjahr = this.halbjahr.next()?.id || this.halbjahr.id;
		const result = await api.server.schreibeGostBlockungsErgebnisHoch(api.schema, this.auswahlErgebnis.id);
		await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr, result.id));
	});

	ergebnisAktivieren = api.call(async (): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		await api.server.activateGostBlockungsergebnis(api.schema, this.auswahlErgebnis.id);
		this.jahrgangsdaten.istBlockungFestgelegt[this.halbjahr.id] = true;
		this.auswahlBlockung.istAktiv = true;
		this.datenmanager.daten().istAktiv = true;
		this.ergebnismanager.getErgebnis().istAktiv = true;
		this.auswahlErgebnis.istAktiv = true;
		this.commit();
		return true;
	});

	ergebnisSynchronisieren = api.call(async (): Promise<void> => {
		if ((!this.hatBlockung && !this.jahrgangsdaten.istBlockungFestgelegt[this.halbjahr.id]) || (this._state.value.auswahlErgebnis === undefined))
			return;
		await api.server.syncGostBlockungsergebnis(api.schema, this.auswahlErgebnis.id);
	});

	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKursplanung.getRouteHalbjahr(this.abiturjahr, value.id));
	}

	gotoBlockung = async (value: GostBlockungListeneintrag | undefined) => {
		if (value !== this._state.value.auswahlBlockung) {
			if (value === undefined || value === null)
				await RouteManager.doRoute(routeGostKursplanung.getRouteHalbjahr(this.abiturjahr, this.halbjahr.id));
			else
				await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(this.abiturjahr, this.halbjahr.id, value.id));
		}
	}

	getPDF = api.call(async (title: DownloadPDFTypen): Promise<ApiFile> => {
		if (!this.hatErgebnis)
			throw new DeveloperNotificationException("Die Kurs-Schienen-Zuordnung kann nur gedruckt werden, wenn ein Ergebnis ausgewählt ist.");
		const list = new ArrayList<number>();
		switch (title) {
			case "Schülerliste markierte Kurse":
				for (const kurs of this.kursAuswahl.value)
					list.add(kurs);
				return await api.server.pdfGostKursplanungKurseMitKursschuelern(list, api.schema, this.ergebnismanager.getErgebnis().id);
			case "Kurse-Schienen-Zuordnung":
				return await api.server.pdfGostKursplanungKurseSchienenZuordnung(list, api.schema, this.ergebnismanager.getErgebnis().id);
			case "Kurse-Schienen-Zuordnung markierter Schüler":
				list.add(this.auswahlSchueler.id);
				return await api.server.pdfGostKursplanungKurseSchienenZuordnung(list, api.schema, this.ergebnismanager.getErgebnis().id);
			case "Kurse-Schienen-Zuordnung gefilterte Schüler":
				for (const schueler of this.schuelerFilter.filtered.value)
					list.add(schueler.id);
				return await api.server.pdfGostKursplanungKurseSchienenZuordnung(list, api.schema, this.ergebnismanager.getErgebnis().id);
			case "Kursbelegung markierter Schüler":
				list.add(this.auswahlSchueler.id);
				return await api.server.pdfGostKursplanungSchuelerMitKursen(list, api.schema, this.ergebnismanager.getErgebnis().id);
			case "Kursbelegung gefilterte Schüler":
				for (const schueler of this.schuelerFilter.filtered.value)
					list.add(schueler.id);
				return await api.server.pdfGostKursplanungSchuelerMitKursen(list, api.schema, this.ergebnismanager.getErgebnis().id);
			default:
				throw new DeveloperNotificationException(`"${title}" ist kein gültiger PDF Download-Typ`);
		}
	})

	gotoErgebnis = async (value: GostBlockungsergebnis | number | undefined) => {
		let id;
		if (value instanceof GostBlockungsergebnis)
			id = value.id;
		else
			id = value;
		if ((id !== this.auswahlErgebnis?.id) && (!RouteManager.isActive()))
			if (this.hatErgebnis && this.hatSchueler && (id !== undefined))
				await RouteManager.doRoute(routeGostKursplanung.getRouteSchueler(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, id, this.auswahlSchueler.id));
			else if (id !== undefined)
				await RouteManager.doRoute(routeGostKursplanung.getRouteErgebnis(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, id));
			else
				await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id));
	}

	gotoSchueler = async (schueler: SchuelerListeEintrag) => {
		// TODO alle möglichen Fälle von fehlenden Informationen (Abiturjahr, Blockung und Ergebnis) berücksichtigen
		if ((!this.hatSchueler) || (schueler.id !== this.auswahlSchueler.id))
			await RouteManager.doRoute(routeGostKursplanungSchueler.getRoute(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, this.auswahlErgebnis.id, schueler.id));
	}

	public kurssortierung = computed<'fach' | 'kursart'>({
		get: () => {
			const value = api.config.getValue('gost.kursplanung.kursansicht.sortierung');
			if ((value === undefined) || ((value !== 'kursart') && (value !== 'fach')))
				return 'kursart';
			return value;
		},
		set: (value) => {
			void api.config.setValue('gost.kursplanung.kursansicht.sortierung', value);
			if (this._state.value.ergebnismanager !== undefined) {
				if (value === 'kursart')
					this.ergebnismanager.kursSetSortierungKursartFachNummer();
				else
					this.ergebnismanager.kursSetSortierungFachKursartNummer();
			}
			this.commit();
		}
	});

	protected getListeKursauswahl(): List<number> {
		const result = new ArrayList<number>();
		for (const idKurs of this.kursAuswahl.value)
			result.add(idKurs);
		return result;
	}

	regelnUpdate = api.call(async (update: GostBlockungRegelUpdate) => {
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return;
		const listAdd = await api.server.updateGostBlockungRegeln(update, api.schema, this.auswahlBlockung.id);
		update.listHinzuzufuegen = listAdd;
		this.ergebnismanager.regelupdateExecute(update);
		this.commit();
	}, {name: 'gost.regelnUpdate'})

	updateKurseLeeren = api.call(async (typ: KurseLeerenTypen, ids?: List<number>) => {
		const listKursIDs = ids || this.getListeKursauswahl();
		switch (typ) {
			case "leereKurseAlle":
			case "leereKurseKursauswahl":
			case "leereKurseFilterFach":
			case "leereKursFilterKurs": {
				if (!listKursIDs.isEmpty()) {
					const list = new ArrayList<GostBlockungsergebnisKursSchuelerZuordnung>();
					for (const kurs of listKursIDs) {
						const schueler = this.ergebnismanager.getOfKursSchuelermenge(kurs);
						for (const s of schueler) {
							const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
							zuordnung.idKurs = kurs;
							zuordnung.idSchueler = s.id;
							list.add(zuordnung);
						}
					}
					await this.removeKursSchuelerZuordnung(list);
				}
				break;
			}
			default:
				throw new DeveloperNotificationException(`Der Typ "${typ}" für die Leerung von Kursen wird noch nicht unterstützt.`);
		}
	})

}

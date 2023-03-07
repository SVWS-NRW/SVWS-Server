import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungListeneintrag, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdaten, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, GostBlockungsergebnisManager, GostFaecherManager, GostHalbjahr, GostJahrgangsdaten, GostStatistikFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { shallowRef } from "vue";
import { ApiPendingData } from "~/components/ApiStatus";
import { GostKursplanungSchuelerFilter } from "~/components/gost/kursplanung/GostKursplanungSchuelerFilter";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeGostKursplanung } from "../RouteGostKursplanung";
import { routeGostKursplanungSchueler } from "./RouteGostKursplanungSchueler";

interface RouteStateGostKursplanung {
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
	mapBlockungen: Map<number, GostBlockungListeneintrag>;
	// ...auch abhängig von der ausgewählten Blockung
	auswahlBlockung: GostBlockungListeneintrag | undefined;
	datenmanager: GostBlockungsdatenManager | undefined;
	// ...auch abhängig von dem ausgewählten Blockungsergebnis
	auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
	ergebnismanager: GostBlockungsergebnisManager | undefined;
	schuelerFilter: GostKursplanungSchuelerFilter | undefined;
	// ... auch abhängig von dem ausgewählten Schüler
	auswahlSchueler: SchuelerListeEintrag | undefined;
}

export class RouteDataGostKursplanung {

	private static _defaultState : RouteStateGostKursplanung = {
		abiturjahr: undefined,
		jahrgangsdaten: undefined,
		mapSchueler: new Map(),
		faecherManager: new GostFaecherManager(),
		mapFachwahlStatistik: new Map(),
		mapLehrer: new Map(),
		halbjahr: GostHalbjahr.EF1,
		mapBlockungen: new Map(),
		auswahlBlockung: undefined,
		datenmanager: undefined,
		auswahlErgebnis: undefined,
		ergebnismanager: undefined,
		schuelerFilter: undefined,
		auswahlSchueler: undefined,
	}

	private _state = shallowRef<RouteStateGostKursplanung>(RouteDataGostKursplanung._defaultState);


	private commit(): void {
		this._state.value = { ... this._state.value };
	}


	public get hatAbiturjahr(): boolean {
		return this._state.value.abiturjahr !== undefined;
	}

	public get abiturjahrIstVorlage() : boolean {
		return (this._state.value.abiturjahr !== undefined) && (this._state.value.abiturjahr === -1);
	}

	public get abiturjahr() : number {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es wurde noch kein Abiturjahrgang geladen.");
		return this._state.value.abiturjahr;
	}

	public setAbiturjahr = async (abiturjahr: number | undefined) => {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		if (abiturjahr === undefined) {
			this._state.value = RouteDataGostKursplanung._defaultState;
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
		for (const s of listSchueler)
			mapSchueler.set(s.id, s);
		api.status.stop();
		// Lade die Fachwahlstatistik des Abiturjahrgangs
		const listFachwahlStatistik = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, abiturjahr);
		const mapFachwahlStatistik: Map<number, GostStatistikFachwahl> = new Map();
		for (const fw of listFachwahlStatistik)
			mapFachwahlStatistik.set(fw.id, fw);
		// Lade die Lehrerliste
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer: Map<number, LehrerListeEintrag> = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		// Setze den State neu
		this._state.value = {
			abiturjahr: abiturjahr,
			jahrgangsdaten: jahrgangsdaten,
			mapSchueler: mapSchueler,
			faecherManager: faecherManager,
			mapFachwahlStatistik: mapFachwahlStatistik,
			mapLehrer: mapLehrer,
			halbjahr: this._state.value.halbjahr,
			mapBlockungen: new Map(),
			auswahlBlockung: undefined,
			datenmanager: undefined,
			auswahlErgebnis: undefined,
			ergebnismanager: undefined,
			schuelerFilter: undefined,
			auswahlSchueler: undefined,
		};
	}

	public get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._state.value.jahrgangsdaten === undefined)
			throw new Error("Es wurde noch kein Abiturjahrgang geladen, so dass keine Jahrgangsdaten zur Verfügung stehen.");
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

	public setHalbjahr = async (halbjahr: GostHalbjahr): Promise<boolean> => {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (halbjahr === this._state.value.halbjahr)
			return false;
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
		api.status.stop();
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			faecherManager: this._state.value.faecherManager,
			mapFachwahlStatistik: this._state.value.mapFachwahlStatistik,
			mapLehrer: this._state.value.mapLehrer,
			halbjahr: halbjahr,
			mapBlockungen: mapBlockungen,
			auswahlBlockung: auswahlBlockung,
			datenmanager: undefined,
			auswahlErgebnis: undefined,
			ergebnismanager: undefined,
			schuelerFilter: undefined,
			auswahlSchueler: this._state.value.auswahlSchueler,
		};
		return true;
	}


	public get hatBlockung(): boolean {
		return this._state.value.datenmanager !== undefined;
	}

	public get mapBlockungen(): Map<number, GostBlockungListeneintrag> {
		return this._state.value.mapBlockungen;
	}

	public get auswahlBlockung() : GostBlockungListeneintrag {
		if (this._state.value.auswahlBlockung === undefined)
			throw new Error("Es wurde noch keine gültige Blockung ausgewählt.");
		return this._state.value.auswahlBlockung;
	}

	public setAuswahlBlockung = async (value: GostBlockungListeneintrag | undefined) => {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann keine Blockung ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if ((this._state.value.auswahlBlockung?.id === value?.id) && (this._state.value.datenmanager !== undefined))
			return;
		if (value === undefined) {
			this._state.value = {
				abiturjahr: this._state.value.abiturjahr,
				jahrgangsdaten: this._state.value.jahrgangsdaten,
				mapSchueler: this._state.value.mapSchueler,
				faecherManager: this._state.value.faecherManager,
				mapFachwahlStatistik: this._state.value.mapFachwahlStatistik,
				mapLehrer: this._state.value.mapLehrer,
				halbjahr: this._state.value.halbjahr,
				mapBlockungen: this._state.value.mapBlockungen,
				auswahlBlockung: undefined,
				datenmanager: undefined,
				auswahlErgebnis: undefined,
				ergebnismanager: undefined,
				schuelerFilter: undefined,
				auswahlSchueler: this._state.value.auswahlSchueler,
			};
			return;
		}
		api.status.start();
		const blockungsdaten = await api.server.getGostBlockung(api.schema, value.id);
		const datenmanager = new GostBlockungsdatenManager(blockungsdaten, this.faecherManager);
		const ergebnisse = datenmanager.getErgebnisseSortiertNachBewertung();
		api.status.stop();
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			faecherManager: this._state.value.faecherManager,
			mapFachwahlStatistik: this._state.value.mapFachwahlStatistik,
			mapLehrer: this._state.value.mapLehrer,
			halbjahr: this._state.value.halbjahr,
			mapBlockungen: this._state.value.mapBlockungen,
			auswahlBlockung: value,
			datenmanager: datenmanager,
			auswahlErgebnis: undefined,
			ergebnismanager: undefined,
			schuelerFilter: undefined,
			auswahlSchueler: this._state.value.auswahlSchueler,
		};
		await this.setAuswahlErgebnis(ergebnisse.size() <= 0 ? undefined : ergebnisse.get(0));
	}

	public get datenmanager(): GostBlockungsdatenManager {
		if (this._state.value.datenmanager === undefined)
			throw new Error("Es wurde noch keine Blockung geladen, so dass kein Daten-Manager zur Verfügung steht.");
		return this._state.value.datenmanager;
	}


	public get hatErgebnis(): boolean {
		return this._state.value.ergebnismanager !== undefined;
	}

	public get ergebnisse(): List<GostBlockungsergebnisListeneintrag> {
		return this.datenmanager.getErgebnisseSortiertNachBewertung();
	}

	public get auswahlErgebnis() : GostBlockungsergebnisListeneintrag {
		if (this._state.value.auswahlErgebnis === undefined)
			throw new Error("Es wurde noch kein gültiges Ergebnis ausgewählt.");
		return this._state.value.auswahlErgebnis;
	}

	public setAuswahlErgebnis = async (value: GostBlockungsergebnisListeneintrag | undefined) => {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann keine Ergebnis ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if ((this._state.value.auswahlBlockung?.id === value?.id) && (this._state.value.ergebnismanager !== undefined))
			return;
		if (value === undefined) {
			this._state.value = {
				abiturjahr: this._state.value.abiturjahr,
				jahrgangsdaten: this._state.value.jahrgangsdaten,
				mapSchueler: this._state.value.mapSchueler,
				faecherManager: this._state.value.faecherManager,
				mapFachwahlStatistik: this._state.value.mapFachwahlStatistik,
				mapLehrer: this._state.value.mapLehrer,
				halbjahr: this._state.value.halbjahr,
				mapBlockungen: this._state.value.mapBlockungen,
				auswahlBlockung: this._state.value.auswahlBlockung,
				datenmanager: this._state.value.datenmanager,
				auswahlErgebnis: undefined,
				ergebnismanager: undefined,
				schuelerFilter: undefined,
				auswahlSchueler: this._state.value.auswahlSchueler,
			};
			return;
		}
		if (this._state.value.datenmanager === undefined)
			throw new Error("Es kann keine Ergebnis ausgewählt werden, wenn zuvor keine Blockung ausgewählt wurde.");
		api.status.start();
		const ergebnis = await api.server.getGostBlockungsergebnis(api.schema, value.id);
		const ergebnismanager = new GostBlockungsergebnisManager(this.datenmanager, ergebnis);
		const schuelerFilter = new GostKursplanungSchuelerFilter(this.datenmanager, ergebnismanager, this.faecherManager.toVector(), this.mapSchueler)
		api.status.stop();
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			faecherManager: this._state.value.faecherManager,
			mapFachwahlStatistik: this._state.value.mapFachwahlStatistik,
			mapLehrer: this._state.value.mapLehrer,
			halbjahr: this._state.value.halbjahr,
			mapBlockungen: this._state.value.mapBlockungen,
			auswahlBlockung: this._state.value.auswahlBlockung,
			datenmanager: this._state.value.datenmanager,
			auswahlErgebnis: value,
			ergebnismanager: ergebnismanager,
			schuelerFilter: schuelerFilter,
			auswahlSchueler: this._state.value.auswahlSchueler,
		};
	}

	public get ergebnismanager(): GostBlockungsergebnisManager {
		if (this._state.value.ergebnismanager === undefined)
			throw new Error("Es wurde noch kein Blockungsergebnis geladen, so dass kein Ergebnis-Manager zur Verfügung steht.");
		return this._state.value.ergebnismanager;
	}

	public get schuelerFilter(): GostKursplanungSchuelerFilter {
		if (this._state.value.schuelerFilter === undefined)
			throw new Error("Es wurde noch keine Ergebnis geladen, so dass kein Schüler-Filter zur Verfügung steht.");
		return this._state.value.schuelerFilter;
	}


	public get hatSchueler(): boolean {
		return this._state.value.auswahlSchueler !== undefined;
	}

	public get auswahlSchueler() : SchuelerListeEintrag {
		if (this._state.value.auswahlSchueler === undefined)
			throw new Error("Es wurde noch kein Schüler ausgewählt.");
		return this._state.value.auswahlSchueler;
	}

	public async setAuswahlSchueler(value: SchuelerListeEintrag | undefined) {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann keine Ergebnis ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (value?.id === this._state.value.auswahlSchueler?.id)
			return;
		// Setze die neue Schülerauswahl im geklonten State
		this._state.value = Object.assign({ ... this._state.value }, { auswahlSchueler: value });
	}

	addBlockung = async () => {
		api.status.start();
		const result = await api.server.createGostAbiturjahrgangBlockung(api.schema, this.jahrgangsdaten.abiturjahr, this.halbjahr.id);
		api.status.stop();
		await this.gotoBlockung(result);
	}

	removeBlockung = async () => {
		if (!this.hatBlockung)
			return;
		api.status.start();
		await api.server.deleteGostBlockung(api.schema, this.auswahlBlockung.id);
		await this.setAuswahlBlockung(undefined);
		api.status.stop();
		await this.gotoHalbjahr(this.halbjahr);
	}

	patchBlockung = async (data: Partial<GostBlockungsdaten>, idBlockung: number): Promise<boolean> => {
		if (this._state.value.datenmanager === undefined)
			throw new Error("Es wurde noch keine Blockung geladen, so dass die Blockung nicht angepasst werden kann.");
		api.status.start();
		await api.server.patchGostBlockung(data, api.schema, idBlockung);
		// TODO Anpassungen an den Managern und Commit
		api.status.stop();
		return true;
	}

	addRegel = async (regel: GostBlockungRegel): Promise<GostBlockungRegel | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const result = await api.server.addGostBlockungRegel(regel.parameter, api.schema, this.auswahlBlockung.id, regel.typ);
		if (!result) {
			api.status.stop();
			return;
		}
		this.datenmanager.addRegel(result);
		this.ergebnismanager.setAddRegelByID(result.id);
		this.commit();
		api.status.stop();
		return result;
	}

	removeRegel = async (id: number) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const result = await api.server.deleteGostBlockungRegelByID(api.schema, id);
		if (!result) {
			api.status.stop();
			return
		}
		this.datenmanager.removeRegel(result);
		this.ergebnismanager.setRemoveRegelByID(result.id);
		this.commit();
		api.status.stop();
		return result;
	}

	patchRegel = async (data: GostBlockungRegel, idRegel: number): Promise<void> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		await api.server.patchGostBlockungRegel(data, api.schema, idRegel);
		this.datenmanager.removeRegelByID(idRegel);
		this.ergebnismanager.setRemoveRegelByID(idRegel);
		this.datenmanager.addRegel(data);
		this.ergebnismanager.setAddRegelByID(data.id);
		this.commit();
		api.status.stop();
	}

	patchKurs = async(data: Partial<GostBlockungKurs>, kurs_id: number): Promise<void> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		await api.server.patchGostBlockungKurs(data, api.schema, kurs_id);
		if (data.suffix !== undefined)
			this.datenmanager.setSuffixOfKurs(kurs_id, data.suffix);
		if (data.istKoopKurs !== undefined)
			this.datenmanager.getKurs(kurs_id).istKoopKurs = data.istKoopKurs;
		this.commit();
		api.status.stop();
	}

	addKurs = async (fach_id : number, kursart_id : number): Promise<GostBlockungKurs | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const kurs = await api.server.addGostBlockungKurs(api.schema, this.auswahlBlockung.id, fach_id, kursart_id);
		if (kurs === undefined) {
			api.status.stop();
			return;
		}
		this.datenmanager.addKurs(kurs);
		this.ergebnismanager.setAddKursByID(kurs.id);
		this.commit();
		api.status.stop();
		return kurs;
	}

	removeKurs = async (fach_id : number, kursart_id : number): Promise<GostBlockungKurs | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const kurs = await api.server.deleteGostBlockungKurs(api.schema, this.auswahlBlockung.id, fach_id, kursart_id);
		if (kurs === undefined) {
			api.status.stop();
			return;
		}
		this.datenmanager.removeKurs(kurs)
		this.ergebnismanager.setRemoveKursByID(kurs.id)
		this.commit()
		api.status.stop();
		return kurs;
	}

	addKursLehrer = async(kurs_id: number, lehrer_id: number): Promise<GostBlockungKursLehrer | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const lehrer = await api.server.addGostBlockungKurslehrer(api.schema, kurs_id, lehrer_id);
		if (lehrer === undefined) {
			api.status.stop();
			return
		}
		this.datenmanager.patchOfKursAddLehrkraft(kurs_id, lehrer);
		this.commit();
		api.status.stop();
		return lehrer;
	}

	removeKursLehrer = async(kurs_id: number, lehrer_id: number): Promise<void> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		await api.server.deleteGostBlockungKurslehrer(api.schema, kurs_id, lehrer_id);
		this.datenmanager.patchOfKursRemoveLehrkraft(kurs_id, lehrer_id);
		this.commit();
		api.status.stop();
	}

	patchSchiene = async (data: Partial<GostBlockungSchiene>, id : number) => {
		api.status.start();
		await api.server.patchGostBlockungSchiene(data, api.schema, id);
		const schiene = this.datenmanager.getSchiene(id);
		Object.assign(schiene, data);
		api.status.stop();
	}

	addSchiene = async (): Promise<GostBlockungSchiene | undefined> => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const schiene = await api.server.addGostBlockungSchiene(api.schema, this.auswahlBlockung.id);
		if (schiene === undefined) {
			api.status.stop();
			return
		}
		this.datenmanager.addSchiene(schiene);
		this.ergebnismanager.setAddSchieneByID(schiene.id)
		this.commit();
		api.status.stop();
		return schiene;
	}

	removeSchiene = async (schiene: GostBlockungSchiene) => {
		if ((!this.hatBlockung) || (!this.hatErgebnis))
			return;
		api.status.start();
		const result = await api.server.deleteGostBlockungSchieneByID(api.schema, schiene.id);
		if (!result) {
			api.status.stop();
			return;
		}
		this.datenmanager.removeSchieneByID(result.id);
		this.ergebnismanager.setRemoveSchieneByID(result.id);
		this.commit();
		api.status.stop();
		return result;
	}


	updateKursSchienenZuordnung = async (idKurs: number, idSchieneAlt: number, idSchieneNeu: number): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		api.status.start();
		await api.server.updateGostBlockungsergebnisKursSchieneZuordnung(api.schema, this._state.value.auswahlErgebnis.id, idSchieneAlt, idKurs, idSchieneNeu);
		this.ergebnismanager.setKursSchiene(idKurs, idSchieneAlt, false);
		this.ergebnismanager.setKursSchiene(idKurs, idSchieneNeu, true);
		this.commit();
		api.status.stop();
		return true;
	}

	updateKursSchuelerZuordnung = async (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		api.status.start();
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		if (idKursAlt !== undefined) {
			await api.server.deleteGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, idKursAlt);
			await api.server.createGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, idKursNeu);
			this.ergebnismanager.setSchuelerKurs(idSchueler, idKursAlt, false);
		} else {
			await api.server.createGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, idKursNeu);
		}
		this.ergebnismanager.setSchuelerKurs(idSchueler, idKursNeu, true);
		this.commit();
		api.status.stop();
		return true;
	}

	removeKursSchuelerZuordnung = async (idSchueler: number, idKurs: number): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		api.status.start();
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		await api.server.deleteGostBlockungsergebnisKursSchuelerZuordnung(api.schema, ergebnisid, idSchueler, idKurs);
		this.ergebnismanager.setSchuelerKurs(idSchueler, idKurs, false);
		this.commit();
		api.status.stop();
		return true;
	}

	autoKursSchuelerZuordnung = async (idSchueler : number): Promise<void> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		api.status.start();
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		const zuordnungen = this.ergebnismanager.getOfSchuelerNeuzuordnung(idSchueler);
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
		this.commit();
		api.status.stop();
		return;
	}

	removeErgebnisse = async (ergebnisse: GostBlockungsergebnisListeneintrag[]): Promise<void> => {
		if ((ergebnisse.length <= 0) || (!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		api.status.start();
		const ergebnisid = this._state.value.auswahlErgebnis.id;
		const reselect = ergebnisse.find(e => e.id === ergebnisid);
		if (reselect) {
			for await (const e of this.ergebnisse)
				if (!ergebnisse.includes(e)) {
					await this.gotoErgebnis(e);
					break;
				}
		}
		for await (const ergebnis of ergebnisse) {
			await api.server.deleteGostBlockungsergebnis(api.schema, ergebnis.id);
			this.datenmanager.removeErgebnis(ergebnis);
		}
		this.commit();
		api.status.stop();
	}

	rechneGostBlockung = async () => {
		const id = this.auswahlBlockung.id;
		let liste: List<number> = new Vector();
		try {
			api.status.start(<ApiPendingData>{ name: "gost.kursblockung.berechnen", id: id });
			liste = await api.server.rechneGostBlockung(api.schema, id, 5000);
			await this.setAuswahlBlockung(this.auswahlBlockung);
			await this.gotoErgebnis(this._state.value.auswahlErgebnis)
			api.status.stop();
		} catch (e) {
			api.status.stop(e instanceof Error ? e : undefined);
			throw e;
		}
		return liste;
	}

	ergebnisZuNeueBlockung = async (idErgebnis: number) => {
		api.status.start();
		const result = await api.server.dupliziereGostBlockungMitErgebnis(api.schema, idErgebnis);
		api.status.stop();
		await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(result.abijahrgang, result.gostHalbjahr, result.id));
	}

	ergebnisHochschreiben = async () => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return;
		api.status.start();
		const abiturjahr = this.abiturjahr;
		const halbjahr = this.halbjahr.next()?.id || this.halbjahr.id;
		const result = await api.server.schreibeGostBlockungsErgebnisHoch(api.schema, this.auswahlErgebnis.id);
		api.status.stop();
		await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(abiturjahr, halbjahr, result.id));
	}

	ergebnisAktivieren = async (): Promise<boolean> => {
		if ((!this.hatBlockung) || (this._state.value.auswahlErgebnis === undefined))
			return false;
		await api.server.activateGostBlockungsergebnis(api.schema, this.auswahlErgebnis.id);
		this.jahrgangsdaten.istBlockungFestgelegt[this.halbjahr.id] = true;
		this.auswahlBlockung.istAktiv = true;
		this.datenmanager.daten().istAktiv = true;
		this.ergebnismanager.getErgebnis().istVorlage = true;
		this.auswahlErgebnis.istVorlage = true;
		return true;
	}

	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKursplanung.getRouteHalbjahr(this.abiturjahr, value.id));
	}

	gotoBlockung = async (value: GostBlockungListeneintrag | undefined) => {
		if (value?.id !== this._state.value.auswahlBlockung?.id) {
			if (value === undefined || value === null)
				await RouteManager.doRoute(routeGostKursplanung.getRouteHalbjahr(this.abiturjahr, this.halbjahr.id));
			else
				await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(this.abiturjahr, this.halbjahr.id, value.id));
		}
	}

	gotoErgebnis = async (value: GostBlockungsergebnisListeneintrag | number | undefined) => {
		let id;
		if (value instanceof GostBlockungsergebnisListeneintrag)
			id = value.id;
		else
			id = value;
		if ((id !== this.auswahlErgebnis?.id) && (!RouteManager.isActive())) {
			if (this.hatErgebnis && this.hatSchueler && (id !== undefined)) {
				await RouteManager.doRoute(routeGostKursplanung.getRouteSchueler(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, id, this.auswahlSchueler.id));
			} else if (id !== undefined) {
				await RouteManager.doRoute(routeGostKursplanung.getRouteErgebnis(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, id));
			} else {
				await RouteManager.doRoute(routeGostKursplanung.getRouteBlockung(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id));
			}
		}
	}

	gotoSchueler = async (schueler: SchuelerListeEintrag) => {
		// TODO alle möglichen Fälle von fehlenden Informationen (Abiturjahr, Blockung und Ergebnis) berücksichtigen
		if ((!this.hatSchueler) || (schueler.id !== this.auswahlSchueler.id))
			await RouteManager.doRoute(routeGostKursplanungSchueler.getRoute(this.abiturjahr, this.halbjahr.id, this.auswahlBlockung.id, this.auswahlErgebnis.id, schueler.id));
	}

}
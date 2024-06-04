
import type { GostJahrgangsdaten, LehrerListeEintrag, SchuelerListeEintrag, GostKlausurvorgabe, GostKlausurraum, Schuljahresabschnitt, GostSchuelerklausur, GostKlausurterminblockungDaten, GostNachschreibterminblockungKonfiguration, GostKlausurenUpdate, List, GostKlausurraumRich} from "@core";
import { StundenplanKalenderwochenzuordnung} from "@core";
import { GostSchuelerklausurTermin, HashMap } from "@core";
import { GostKlausurenCollectionSkrsKrs, GostKursklausur } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import { GostKlausurraumManager, StundenplanManager, KursManager, GostFaecherManager, GostHalbjahr, GostKursklausurManager, GostKlausurvorgabenManager, StundenplanListUtils, DeveloperNotificationException, GostKlausurenMetaDataCollection } from "@core";
import { GostKlausurtermin, ArrayList} from "@core";
import { computed } from "vue";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungVorgaben";
import { routeApp } from "../../RouteApp";
import { routeGostKlausurplanungRaumzeit } from "./RouteGostKlausurplanungRaumzeit";

interface RouteStateGostKlausurplanung extends RouteStateInterface {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	abschnitt : Schuljahresabschnitt | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	kursklausurmanager: GostKursklausurManager | undefined;
	klausurvorgabenmanager: GostKlausurvorgabenManager | undefined;
	stundenplanmanager: StundenplanManager | undefined;
	kalenderwoche: StundenplanKalenderwochenzuordnung;
	termin: GostKlausurtermin | undefined;
	raummanager: GostKlausurraumManager | undefined;
}

const defaultState = <RouteStateGostKlausurplanung> {
	abiturjahr: undefined,
	abschnitt: undefined,
	jahrgangsdaten: undefined,
	halbjahr: GostHalbjahr.EF1,
	kursklausurmanager: undefined,
	klausurvorgabenmanager: undefined,
	stundenplanmanager: undefined,
	view: routeGostKlausurplanungVorgaben,
	raummanager: undefined,
	kalenderwoche: new StundenplanKalenderwochenzuordnung(),
	termin: undefined,
};


export class RouteDataGostKlausurplanung extends RouteData<RouteStateGostKlausurplanung> {

	public constructor() {
		super(defaultState);
	}

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

	public async setAbiturjahr(abiturjahr: number | undefined) {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		if (abiturjahr === undefined) {
			this._state.value = this._defaultState;
			return;
		}
		try {
			api.status.start();
			// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
			const jahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, abiturjahr)
			let view: RouteNode<any, any> = this.view;

			if (abiturjahr === -1) {
				if ((view !== routeGostKlausurplanungKalender) && (view !== routeGostKlausurplanungVorgaben))
					view = routeGostKlausurplanungVorgaben;
			}

			const result: Partial<RouteStateGostKlausurplanung> = {
				abiturjahr: abiturjahr,
				jahrgangsdaten: jahrgangsdaten,
				halbjahr: this._state.value.halbjahr,
				view: view,
			}
			if (this._state.value.klausurvorgabenmanager)
				Object.assign(result, {klausurvorgabenmanager: this._state.value.klausurvorgabenmanager});
			if (this._state.value.kursklausurmanager)
				Object.assign(result, {kursklausurmanager: this._state.value.kursklausurmanager});
			if (this._state.value.stundenplanmanager)
				Object.assign(result, {stundenplanmanager: this._state.value.stundenplanmanager});
			if (this._state.value.kalenderwoche !== undefined)
				Object.assign(result, {kalenderwoche: this._state.value.kalenderwoche});
			// Setze den State neu
			this.setPatchedDefaultState(result);
		} finally {
			api.status.stop();
		}
	}

	public get hatJahrgangsdaten(): boolean {
		return this._state.value.jahrgangsdaten !== undefined;
	}

	public get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._state.value.jahrgangsdaten === undefined)
			throw new DeveloperNotificationException("Es wurde noch kein Abiturjahrgang geladen, so dass keine Jahrgangsdaten zur Verfügung stehen.");
		return this._state.value.jahrgangsdaten;
	}

	public get halbjahr() : GostHalbjahr {
		return this._state.value.halbjahr;
	}

	public async setHalbjahr(halbjahr: GostHalbjahr, hjChanged: boolean): Promise<boolean> {
		if (this._state.value.abiturjahr === undefined)
			throw new DeveloperNotificationException("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (!hjChanged && halbjahr === this._state.value.halbjahr)
			return false;
		try {
			api.status.start();
			const result: Partial<RouteStateGostKlausurplanung> = {
				abschnitt: undefined,
				halbjahr: halbjahr,
			}
			if (this._state.value.abiturjahr === -1) {
				if (!this.hatKlausurvorgabenManager) {
					const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgang(api.schema, -1);
					const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, -1);
					const faecherManager = new GostFaecherManager(listFaecher);
					const klausurvorgabenmanager = new GostKlausurvorgabenManager(faecherManager, listKlausurvorgaben);
					Object.assign(result, {klausurvorgabenmanager});
				}
				this.setPatchedState(result);
				return true;
			}
			const schuljahr = halbjahr.getSchuljahrFromAbiturjahr(this._state.value.abiturjahr);
			const abschnitt : Schuljahresabschnitt | undefined = api.getAbschnittBySchuljahrUndHalbjahr(schuljahr, halbjahr.halbjahr);
			if (abschnitt === undefined) {
				this.setPatchedState(result);
				return true;
			}
			Object.assign(result, {abschnitt});
			if (!this.hatKursklausurManager) {
				const klausurdatenGzip = await api.server.getGostKlausurenMetaCollectionOberstufeGZip(api.schema, this.abiturjahr, halbjahr.id);
				const klausurdatenBlob = await new Response(klausurdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
				const klausurdaten = GostKlausurenMetaDataCollection.transpilerFromJSON(await klausurdatenBlob.text());
				const faecherManager = new GostFaecherManager(klausurdaten.faecher);
				const klausurvorgabenmanager = new GostKlausurvorgabenManager(faecherManager, klausurdaten.klausurdata.vorgaben);
				const kursklausurmanager = new GostKursklausurManager(klausurvorgabenmanager, klausurdaten.klausurdata.kursklausuren, klausurdaten.klausurdata.termine, klausurdaten.klausurdata.schuelerklausuren, klausurdaten.klausurdata.schuelerklausurtermine);
				kursklausurmanager.setKursManager(new KursManager(klausurdaten.kurse));
				const mapLehrer = new HashMap<number, LehrerListeEintrag>();
				for (const l of klausurdaten.lehrer)
					mapLehrer.put(l.id, l);
				const mapSchueler = new HashMap<number, SchuelerListeEintrag>();
				for (const l of klausurdaten.schueler)
					mapSchueler.put(l.id, l);
				kursklausurmanager.setLehrerMap(mapLehrer);
				kursklausurmanager.setSchuelerMap(mapSchueler);
				Object.assign(result, {kursklausurmanager, klausurvorgabenmanager});
			}
			if (!this.hatStundenplanManager) {
				const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, abschnitt.id);
				if (listStundenplaene.isEmpty()) {
					this.setPatchedState(result);
					return true;
				}
				const stundenplan = StundenplanListUtils.get(listStundenplaene, new Date().toISOString().substring(0, 10));
				if (stundenplan === null)
					throw new DeveloperNotificationException("Es konnte kein aktiver Stundenplan gefunden werden.");
				const stundenplandaten = await api.server.getStundenplan(api.schema, stundenplan.id);
				const unterrichte = await api.server.getStundenplanUnterrichte(api.schema, stundenplan.id);
				const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, stundenplan.id);
				const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, stundenplan.id);
				const stundenplanmanager = new StundenplanManager(stundenplandaten, unterrichte, pausenaufsichten, unterrichtsverteilung);
				if (this.kalenderwoche.value.jahr === -1)
					this.kalenderwoche.value = stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString());
				Object.assign(result, {	stundenplanmanager });
			}
			this.setPatchedState(result);
			return true;
		} finally {
			api.status.stop();
		}
	}

	public get hatStundenplanManager(): boolean {
		return this._state.value.stundenplanmanager !== undefined;
	}

	public get stundenplanmanager(): StundenplanManager {
		if (this._state.value.stundenplanmanager === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine Daten geladen, so dass kein Stundenplan-Manager zur Verfügung steht.");
		return this._state.value.stundenplanmanager;
	}


	public get hatKursklausurManager(): boolean {
		return this._state.value.kursklausurmanager !== undefined;
	}

	public get kursklausurmanager(): GostKursklausurManager {
		if (this._state.value.kursklausurmanager === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine Daten geladen, so dass kein Kurs-Klausur-Manager zur Verfügung steht.");
		return this._state.value.kursklausurmanager;
	}

	public get hatKlausurvorgabenManager(): boolean {
		return this._state.value.klausurvorgabenmanager !== undefined;
	}

	public get klausurvorgabenmanager(): GostKlausurvorgabenManager {
		if (this._state.value.klausurvorgabenmanager === undefined)
			throw new DeveloperNotificationException("Es wurde noch keine Daten geladen, so dass kein Klausur-Vorgaben-Manager zur Verfügung steht.");
		return this._state.value.klausurvorgabenmanager;
	}

	public get raummanager(): GostKlausurraumManager | undefined {
		return this._state.value.raummanager;
	}

	public getConfigValue(key: string): string {
		return api.config.getValue("gost.klausurplan." + key);
	}

	public setConfigValue(key: string, value: string) {
		api.config.setValue('gost.klausurplan.' + key, value);
	}

	setRaumTermin = async (termin: GostKlausurtermin | null) => {
		if (this._state.value.raummanager === undefined || termin !== this._state.value.raummanager.getHauptTermin()) {
			this._state.value.raummanager = termin !== null ? await this.erzeugeKlausurraummanager(termin) : undefined;
			this.commit();
		}
	}

	quartalsauswahl = computed<0 | 1 | 2>({
		get: () => {
			const value = parseInt(this.getConfigValue("quartal"));
			return (value === 1 || value === 2) ? value : 0;
		},
		set: (value) => {
			if (this.quartalsauswahl.value !== value) {
				this.setConfigValue("quartal", value.toString());
				this.commit();
			}
		}
	});

	kalenderwoche = computed<StundenplanKalenderwochenzuordnung>({
		get: () => this._state.value.kalenderwoche,
		set: (value) => {
			if (this._state.value.kalenderwoche !== value) {
				this._state.value.kalenderwoche = value;
				this.commit();
			}
		}
	});

	terminSelected = computed<GostKlausurtermin | undefined>({
		get: () => this._state.value.termin,
		set: (value) => {
			if (this._state.value.termin !== value) {
				this._state.value.termin = value;
				this.commit();
			}
		}
	});

	gotoKalenderwoche = async (kw: StundenplanKalenderwochenzuordnung) => {
		await RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute(this.abiturjahr, this.halbjahr.id, parseInt(kw.jahr + "" + kw.kw), this.terminSelected.value !== undefined ? this.terminSelected.value.id : undefined ));
	}

	gotoTermin = async (idtermin: number | undefined) => {
		await RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute(this.abiturjahr, this.halbjahr.id, idtermin ));
	}

	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(this.view.getRoute(this.abiturjahr, value.id));
	}

	get zeigeAlleJahrgaenge(): boolean {
		return this.getConfigValue("zeigeAlleJahrgaenge") === 'true';
	}

	setZeigeAlleJahrgaenge = (value: boolean) => {
		this.setConfigValue('zeigeAlleJahrgaenge', value ? "true" : "false");
	}

	erzeugeKlausurtermin = async (quartal: number, ht: boolean): Promise<GostKlausurtermin> => {
		api.status.start();
		const terminNeu : Partial<GostKlausurtermin> = new GostKlausurtermin();
		terminNeu.abijahr = this.abiturjahr;
		terminNeu.halbjahr = this.halbjahr.id;
		terminNeu.quartal = quartal;
		terminNeu.istHaupttermin = ht;
		delete terminNeu.id;
		const termin = await api.server.createGostKlausurenKlausurtermin(terminNeu, api.schema);
		this.kursklausurmanager.terminAdd(termin);
		this.commit();
		api.status.stop();
		return termin;
	}

	loescheKlausurtermine = async (termine: List<GostKlausurtermin>) => {
		api.status.start();
		const terminIds = new ArrayList<number>();
		for (const termin of termine)
			terminIds.add(termin.id);
		await api.server.deleteGostKlausurenKlausurtermine(terminIds, api.schema);
		this.kursklausurmanager.terminRemoveAll(termine);
		this.commit();
		api.status.stop();
	}

	patchKlausur = async (klausur: GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin>): Promise<GostKlausurenCollectionSkrsKrs> => {
		try {
			api.status.start();
			delete patch.id;
			if (klausur instanceof GostKursklausur) {
				if (this._state.value.abschnitt === undefined)
					throw new DeveloperNotificationException('Es wurde kein gültiger Abschnitt für diese Planung gesetzt')
				const result = await api.server.patchGostKlausurenKursklausur(patch, api.schema, klausur.id, this._state.value.abschnitt.id);
				if (result.kursKlausurPatched !== null)
					this.kursklausurmanager.kursklausurPatchAttributes(result.kursKlausurPatched);
				return result;
			} else if (klausur instanceof GostSchuelerklausurTermin) {
				const _schuelerklausurtermin = this.kursklausurmanager.schuelerklausurterminGetByIdOrException(klausur.id);
				await api.server.patchGostKlausurenSchuelerklausurtermin(patch, api.schema, klausur.id);
			 	this.kursklausurmanager.schuelerklausurterminPatchAttributes(Object.assign(_schuelerklausurtermin, patch));
			}
			return new GostKlausurenCollectionSkrsKrs();
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	erzeugeDefaultKlausurvorgaben = async (quartal: number) => {
		api.status.start();
		const neueVorgaben = await api.server.createDefaultGostKlausurenVorgaben(api.schema, this.halbjahr.id, quartal);
		this.klausurvorgabenmanager.vorgabeAddAll(neueVorgaben);
		this.commit();
		api.status.stop();
	}

	erzeugeKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>) => {
		api.status.start();
		delete vorgabe.idVorgabe;
		vorgabe.abiJahrgang = this.abiturjahr;
		vorgabe.halbjahr = this.halbjahr.id;
		try {
			const neueVorgabe = await api.server.createGostKlausurenVorgabe(vorgabe, api.schema);
			this.klausurvorgabenmanager.vorgabeAdd(neueVorgabe);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	patchKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>, id: number) => {
		api.status.start();
		await api.server.patchGostKlausurenVorgabe(vorgabe, api.schema, id);
		this.klausurvorgabenmanager.vorgabePatchAttributes(Object.assign(this.klausurvorgabenmanager.vorgabeGetByIdOrException(id), vorgabe));
		this.commit();
		api.status.stop();
	}

	loescheKlausurvorgabe = async (idVorgabe: number) => {
		api.status.start();
		await api.server.deleteGostKlausurenVorgabe(api.schema, idVorgabe);
		this.klausurvorgabenmanager.vorgabeRemoveById(idVorgabe);
		this.commit();
		api.status.stop();
	}

	erzeugeKursklausurenAusVorgaben = async (quartal: number) => {
		api.status.start();
		try {
			const result = await api.server.createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
			this.kursklausurmanager.kursklausurAddAll(result.kursklausuren);
			this.kursklausurmanager.schuelerklausurAddAll(result.schuelerklausuren);
			this.kursklausurmanager.schuelerklausurterminAddAll(result.schuelerklausurtermine);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	patchKlausurtermin = async (id: number, termin: Partial<GostKlausurtermin>) => {
		api.status.start();
		try {
			const oldTtermin = this.kursklausurmanager.terminGetByIdOrException(id);
			await api.server.patchGostKlausurenKlausurtermin(termin, api.schema, id);
			Object.assign(oldTtermin, termin);
			this.kursklausurmanager.terminPatchAttributes(oldTtermin);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	erzeugeVorgabenAusVorlage = async (quartal: number) => {
		api.status.start();
		const listKlausurvorgaben = await api.server.copyGostKlausurenVorgaben(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
		this.klausurvorgabenmanager.vorgabeAddAll(listKlausurvorgaben);
		this.commit();
		api.status.stop();
	}

	createKlausurraum = async (raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => {
		api.status.start();
		const neuerRaum = await api.server.createGostKlausurenRaum(raum, api.schema);
		manager.raumAdd(neuerRaum);
		this.commit();
		api.status.stop();
	}

	loescheKlausurraum = async (id: number, manager: GostKlausurraumManager): Promise<boolean> => {
		api.status.start();
		await api.server.deleteGostKlausurenRaum(api.schema, id);
		manager.raumRemoveById(id);
		this.commit();
		api.status.stop();
		return true;
	}

	patchKlausurraum = async (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager): Promise<boolean> => {
		api.status.start();
		const oldRaum: GostKlausurraum = manager.raumGetByIdOrException(id);
		await api.server.patchGostKlausurenRaum(raum, api.schema, id);
		manager.raumPatchAttributes(Object.assign(oldRaum, raum));
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeKlausurraummanager = async (termin: GostKlausurtermin | List<number>): Promise<GostKlausurraumManager> => {
		api.status.start();
		let manager;
		if (termin instanceof GostKlausurtermin) {
			const krsCollection = await api.server.getGostKlausurenSchuelerraumstundenTermin(api.schema, termin.id);
			manager = new GostKlausurraumManager(krsCollection.raeume, krsCollection.raumstunden, krsCollection.sktRaumstunden, krsCollection.idsSchuelerklausurtermine, this.kursklausurmanager, termin);
		} else {
			const krsCollection = await api.server.getGostKlausurenSchuelerraumstundenSktids(termin, api.schema);
			manager = new GostKlausurraumManager(krsCollection.raeume, krsCollection.raumstunden, krsCollection.sktRaumstunden, krsCollection.idsSchuelerklausurtermine, this.kursklausurmanager, new GostKlausurtermin());
		}
		manager.setStundenplanManager(this.stundenplanmanager);
		api.status.stop();
		return manager;
	}

	setzeRaumZuSchuelerklausuren = async (rRaeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean): Promise<GostKlausurenCollectionSkrsKrs> => {
		if (!this.raummanager)
			throw new DeveloperNotificationException('Kein Raummanager gesetzt');
		if (this._state.value.abschnitt === undefined)
			throw new DeveloperNotificationException('Es wurde kein gültiger Abschnitt für diese Planung gesetzt');
		if (rRaeume.isEmpty())
			return new GostKlausurenCollectionSkrsKrs();
		api.status.start();
		const skids = new ArrayList<number>();
		let collectionSkrsKrs;
		if (!deleteFromRaeume) {
			collectionSkrsKrs = await api.server.setzeGostSchuelerklausurenZuRaum(rRaeume, api.schema, this._state.value.abschnitt.id);
		} else {
			collectionSkrsKrs = await api.server.loescheGostSchuelerklausurenAusRaum(rRaeume, api.schema);
		}
		this.raummanager.setzeRaumZuSchuelerklausuren(collectionSkrsKrs);
		this.commit();
		api.status.stop();
		return collectionSkrsKrs;
	}

	blockenKursklausuren = async (blockungDaten: GostKlausurterminblockungDaten) => {
		api.status.start();
		const blockung = await api.server.blockenGostKursklausuren(blockungDaten, api.schema);
		this.kursklausurmanager.terminAddAll(blockung.termine);
		this.kursklausurmanager.kursklausurMengePatchAttributes(blockung.kursklausuren);
		this.commit();
		api.status.stop();
	}

	blockenNachschreiber = async (config: GostNachschreibterminblockungKonfiguration) => {
		api.status.start();
		const blockungDaten = await api.server.blockenGostSchuelerklausurtermine(config, api.schema);
		this.kursklausurmanager.terminAddAll(blockungDaten.termine);
		for (const skt of blockungDaten.schuelerklausurtermine)
			this.kursklausurmanager.schuelerklausurterminPatchAttributes(skt);
		this.commit();
		api.status.stop();
	}

	updateKlausurblockung = async (update: GostKlausurenUpdate) => {
		api.status.start();
		await api.server.updateGostKlausuren(update, api.schema);
		this.kursklausurmanager.updateExecute(update);
		this.commit();
		api.status.stop();
	}

	gotoVorgaben = async () => {
		await RouteManager.doRoute({ name: routeGostKlausurplanungVorgaben.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.abiturjahr, halbjahr: this.halbjahr.id } });
	}


}
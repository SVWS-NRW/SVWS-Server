
import type { GostJahrgangsdaten, GostKlausurvorgabe, GostKlausurraum, Schuljahresabschnitt, GostKlausurterminblockungDaten, GostNachschreibterminblockungKonfiguration, GostKlausurenUpdate, List, GostKlausurraumRich, ApiFile, GostSchuelerklausur} from "@core";
import { GostKlausurenCollectionAllData, ReportingParameter, ReportingReportvorlage, StundenplanKalenderwochenzuordnung} from "@core";
import { GostSchuelerklausurTermin } from "@core";
import { GostKlausurenCollectionSkrsKrsData, GostKursklausur } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import { StundenplanManager, GostFaecherManager, GostHalbjahr, GostKlausurplanManager, StundenplanListUtils, DeveloperNotificationException } from "@core";
import { GostKlausurtermin, ArrayList} from "@core";
import { computed } from "vue";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungVorgaben";
import { routeApp } from "../../RouteApp";
import { routeGostKlausurplanungRaumzeit } from "./RouteGostKlausurplanungRaumzeit";
import type { DownloadPDFTypen } from "~/components/gost/klausurplanung/DownloadPDFTypen";

interface RouteStateGostKlausurplanung extends RouteStateInterface {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	abschnitt : Schuljahresabschnitt | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	halbjahr: GostHalbjahr;
	manager: GostKlausurplanManager;
	kalenderwoche: StundenplanKalenderwochenzuordnung;
	termin: GostKlausurtermin | undefined;
}

const defaultState = <RouteStateGostKlausurplanung> {
	abiturjahr: undefined,
	abschnitt: undefined,
	jahrgangsdaten: undefined,
	halbjahr: GostHalbjahr.EF1,
	manager: new GostKlausurplanManager(-1),
	view: routeGostKlausurplanungVorgaben,
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
			Object.assign(result, {manager: this._state.value.manager, kalenderwoche: this._state.value.kalenderwoche});
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
				if (!this.manager.isVorgabenInitialized()) {
					const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgang(api.schema, -1);
					this.manager.vorgabeAddAll(listKlausurvorgaben);
					const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, -1);
					const faecherManager = new GostFaecherManager(api.abschnitt.schuljahr, listFaecher);
					this.manager.setFaecherManager(faecherManager);
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
			if (!this.manager.isKlausurenInitialized()) {
				const klausurdatenGzip = await api.server.getGostKlausurenMetaCollectionOberstufeGZip(api.schema, this.abiturjahr, halbjahr.id);
				const klausurdatenBlob = await new Response(klausurdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
				const klausurdaten = GostKlausurenCollectionAllData.transpilerFromJSON(await klausurdatenBlob.text());
				const manager = new GostKlausurplanManager(this._state.value.abiturjahr - 1, klausurdaten);
				Object.assign(result, {	manager });
				this.setPatchedState(result);
			}
			if (!this.manager.getStundenplanManagerOrNull()) {
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
				this.manager.setStundenplanManager(stundenplanmanager);
				if (this.kalenderwoche.value.jahr === -1)
					this.kalenderwoche.value = stundenplanmanager.kalenderwochenzuordnungGetByDatum(new Date().toISOString());
			}
			this.setPatchedState(result);
			return true;
		} finally {
			api.status.stop();
		}
	}

	public get manager(): GostKlausurplanManager {
		return this._state.value.manager;
	}

	public getConfigValue(key: string): string {
		return api.config.getValue("gost.klausurplan." + key);
	}

	public async setConfigValue(key: string, value: string) {
		await api.config.setValue('gost.klausurplan.' + key, value);
	}

	setRaumTermin = async (termin: GostKlausurtermin | null) => {
		if (termin !== null && (this.terminSelected.value === undefined || !this.terminSelected.value.equals(termin))) {
			this.terminSelected.value = termin;
			// await this.erzeugeKlausurraummanager(termin);
		}
		this.commit();
	}

	quartalsauswahl = computed<0 | 1 | 2>({
		get: () => {
			const value = parseInt(this.getConfigValue("quartal"));
			return (value === 1 || value === 2) ? value : 0;
		},
		set: (value) => {
			if (this.quartalsauswahl.value !== value) {
				void this.setConfigValue("quartal", value.toString());
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
		await RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute(this.abiturjahr, this.halbjahr.id, parseInt(kw.jahr.toString() + (kw.kw <= 9 ? "0" : "") + kw.kw.toString()), this.terminSelected.value !== undefined ? this.terminSelected.value.id : undefined ));
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
		void this.setConfigValue('zeigeAlleJahrgaenge', value ? "true" : "false");
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
		this.manager.terminAdd(termin);
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
		this.manager.terminRemoveAll(termine);
		this.commit();
		api.status.stop();
	}

	erzeugeSchuelerklausuren = async (klausuren: List<Partial<GostSchuelerklausur>>) => {
		api.status.start();
		for (const klausur of klausuren)
			delete klausur.id;
		const dtos = await api.server.createGostKlausurenSchuelerklausuren(klausuren, api.schema);
		this.manager.addKlausurData(dtos);
		this.commit();
		api.status.stop();
	}

	loescheSchuelerklausuren = async (klausuren: List<GostSchuelerklausur>) => {
		api.status.start();
		const ids = new ArrayList<number>();
		for (const klausur of klausuren)
			ids.add(klausur.id);
		await api.server.deleteGostKlausurenSchuelerklausuren(ids, api.schema);
		this.manager.schuelerklausurRemoveAll(klausuren);
		this.commit();
		api.status.stop();
	}

	patchKlausur = async (klausur: GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin>): Promise<GostKlausurenCollectionSkrsKrsData> => {
		try {
			api.status.start();
			delete patch.id;
			if (klausur instanceof GostKursklausur) {
				if (this._state.value.abschnitt === undefined)
					throw new DeveloperNotificationException('Es wurde kein gültiger Abschnitt für diese Planung gesetzt')
				const result = await api.server.patchGostKlausurenKursklausur(patch, api.schema, klausur.id, this._state.value.abschnitt.id);
				if (result.kursKlausurPatched !== null) {
					this.manager.kursklausurPatchAttributes(result.kursKlausurPatched);
					this.manager.setzeRaumZuSchuelerklausuren(result);
				}
				return result;
			} else if (klausur instanceof GostSchuelerklausurTermin) {
				const _schuelerklausurtermin = this.manager.schuelerklausurterminGetByIdOrException(klausur.id);
				await api.server.patchGostKlausurenSchuelerklausurtermin(patch, api.schema, klausur.id);
				this.manager.schuelerklausurterminPatchAttributes(Object.assign(_schuelerklausurtermin, patch));
			}
			return new GostKlausurenCollectionSkrsKrsData();
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	erzeugeDefaultKlausurvorgaben = async (quartal: number) => {
		api.status.start();
		const neueVorgaben = await api.server.createDefaultGostKlausurenVorgaben(api.schema, this.halbjahr.id, quartal);
		this.manager.vorgabeAddAll(neueVorgaben);
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
			this.manager.vorgabeAdd(neueVorgabe);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	patchKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>, id: number) => {
		api.status.start();
		await api.server.patchGostKlausurenVorgabe(vorgabe, api.schema, id);
		this.manager.vorgabePatchAttributes(Object.assign(this.manager.vorgabeGetByIdOrException(id), vorgabe));
		this.commit();
		api.status.stop();
	}

	loescheKlausurvorgabe = async (idVorgabe: number) => {
		api.status.start();
		await api.server.deleteGostKlausurenVorgabe(api.schema, idVorgabe);
		this.manager.vorgabeRemoveById(idVorgabe);
		this.commit();
		api.status.stop();
	}

	erzeugeKursklausurenAusVorgaben = async (quartal: number) => {
		api.status.start();
		try {
			const result = await api.server.createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
			this.manager.addKlausurData(result);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	patchKlausurtermin = async (id: number, termin: Partial<GostKlausurtermin>) => {
		api.status.start();
		try {
			const oldTtermin = this.manager.terminGetByIdOrException(id);
			await api.server.patchGostKlausurenKlausurtermin(termin, api.schema, id);
			Object.assign(oldTtermin, termin);
			this.manager.terminPatchAttributes(oldTtermin);
		} finally {
			this.commit();
			api.status.stop();
		}
	}

	erzeugeVorgabenAusVorlage = async (quartal: number) => {
		api.status.start();
		const listKlausurvorgaben = await api.server.copyGostKlausurenVorgaben(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
		this.manager.vorgabeAddAll(listKlausurvorgaben);
		this.commit();
		api.status.stop();
	}

	createKlausurraum = async (raum: Partial<GostKlausurraum>) => {
		api.status.start();
		const neuerRaum = await api.server.createGostKlausurenRaum(raum, api.schema);
		this.manager.raumAdd(neuerRaum);
		this.commit();
		api.status.stop();
	}

	loescheKlausurraum = async (id: number): Promise<boolean> => {
		api.status.start();
		await api.server.deleteGostKlausurenRaum(api.schema, id);
		this.manager.raumRemoveById(id);
		this.commit();
		api.status.stop();
		return true;
	}

	patchKlausurraum = async (id: number, raum: Partial<GostKlausurraum>): Promise<boolean> => {
		api.status.start();
		const oldRaum: GostKlausurraum = this.manager.raumGetByIdOrException(id);
		await api.server.patchGostKlausurenRaum(raum, api.schema, id);
		this.manager.raumPatchAttributes(Object.assign(oldRaum, raum));
		this.commit();
		api.status.stop();
		return true;
	}

	setzeRaumZuSchuelerklausuren = async (rRaeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean): Promise<GostKlausurenCollectionSkrsKrsData> => {
		if (this._state.value.abschnitt === undefined)
			throw new DeveloperNotificationException('Es wurde kein gültiger Abschnitt für diese Planung gesetzt');
		if (rRaeume.isEmpty())
			return new GostKlausurenCollectionSkrsKrsData();
		api.status.start();
		const skids = new ArrayList<number>();
		let collectionSkrsKrs;
		if (!deleteFromRaeume) {
			collectionSkrsKrs = await api.server.setzeGostSchuelerklausurenZuRaum(rRaeume, api.schema, this._state.value.abschnitt.id);
		} else {
			collectionSkrsKrs = await api.server.loescheGostSchuelerklausurenAusRaum(rRaeume, api.schema);
		}
		this.manager.setzeRaumZuSchuelerklausuren(collectionSkrsKrs);
		this.commit();
		api.status.stop();
		return collectionSkrsKrs;
	}

	blockenKursklausuren = async (blockungDaten: GostKlausurterminblockungDaten) => {
		api.status.start();
		const blockung = await api.server.blockenGostKursklausuren(blockungDaten, api.schema);
		this.manager.terminAddAll(blockung.termine);
		this.manager.kursklausurMengePatchAttributes(blockung.kursklausuren);
		this.commit();
		api.status.stop();
	}

	blockenNachschreiber = async (config: GostNachschreibterminblockungKonfiguration) => {
		api.status.start();
		const blockungDaten = await api.server.blockenGostSchuelerklausurtermine(config, api.schema);
		this.manager.terminAddAll(blockungDaten.termine);
		for (const skt of blockungDaten.schuelerklausurtermine)
			this.manager.schuelerklausurterminPatchAttributes(skt);
		this.commit();
		api.status.stop();
	}

	updateKlausurblockung = async (update: GostKlausurenUpdate) => {
		api.status.start();
		await api.server.updateGostKlausuren(update, api.schema);
		this.manager.updateExecute(update);
		this.commit();
		api.status.stop();
	}

	createSchuelerklausurTermin = async (id: number) => {
		api.status.start();
		const skNeu = await api.server.createGostKlausurenSchuelerklausurtermin(api.schema, id);
		this.manager.schuelerklausurterminAdd(skNeu);
		this.commit();
		api.status.stop();
	}

	gotoVorgaben = async () => {
		await RouteManager.doRoute({ name: routeGostKlausurplanungVorgaben.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.abiturjahr, halbjahr: this.halbjahr.id } });
	}

	getPDF = api.call(async (title: DownloadPDFTypen): Promise<ApiFile> => {
		const reportingParameter = new ReportingParameter();
		reportingParameter.idSchuljahresabschnitt = routeApp.data.aktAbschnitt.value.id;
		reportingParameter.reportvorlage = ReportingReportvorlage.GOST_KLAUSURPLANUNG_v_KLAUSURTERMINE_MIT_KURSEN.getBezeichnung()!;
		if (title.startsWith("Klausurplan (", 0)) {
			reportingParameter.idsHauptdaten.add(this.abiturjahr);
			reportingParameter.idsHauptdaten.add(this.halbjahr.id);
		}
		if (title.indexOf("Nachschreiber") > 0) {
			reportingParameter.detailLevel = 1;
		}
		if (title.indexOf("detailliert") > 0) {
			reportingParameter.detailLevel = 2;
		}
		return await api.server.pdfReport(reportingParameter, api.schema);
	})


}

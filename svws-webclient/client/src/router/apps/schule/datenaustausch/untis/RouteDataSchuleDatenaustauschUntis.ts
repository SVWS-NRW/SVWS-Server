import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuleDatenaustauschUntisStundenplan } from "./RouteSchuleDatenaustauschUntisStundenplan";
import type { ApiFile, GostBlockungListeneintrag, GostBlockungsergebnis, GostJahrgang} from "@core";
import { DeveloperNotificationException, OpenApiError, SimpleOperationResponse, GostBlockungsdatenManager, GostBlockungsdaten, GostFaecherManager, GostHalbjahr } from "@core";


interface RouteStateDatenaustauschUntis extends RouteStateInterface {
	abiturjahrgang: GostJahrgang | undefined;
	halbjahr: GostHalbjahr | undefined;
	blockung: GostBlockungListeneintrag | undefined;
	ergebnis: GostBlockungsergebnis | undefined;
	mapAbiturjahrgaenge: Map<number, GostJahrgang>;
	mapBlockungen: Map<number, GostBlockungListeneintrag>;
	getDatenmanager: GostBlockungsdatenManager;
}

const defaultState = <RouteStateDatenaustauschUntis> {
	abiturjahrgang: undefined,
	halbjahr: undefined,
	blockung: undefined,
	ergebnis: undefined,
	mapAbiturjahrgaenge: new Map<number, GostJahrgang>(),
	mapBlockungen: new Map<number, GostBlockungListeneintrag>(),
	getDatenmanager: new GostBlockungsdatenManager(),
	view: routeSchuleDatenaustauschUntisStundenplan,
};


export class RouteDataSchuleDatenaustauschUntis extends RouteData<RouteStateDatenaustauschUntis> {

	public constructor() {
		super(defaultState);
	}

	public get mapAbiturjahrgaenge(): Map<number, GostJahrgang> {
		return this._state.value.mapAbiturjahrgaenge;
	}

	public get abiturjahrgang(): GostJahrgang | undefined {
		return this._state.value.abiturjahrgang;
	}

	public get halbjahr(): GostHalbjahr | undefined {
		return this._state.value.halbjahr;
	}

	public get blockung(): GostBlockungListeneintrag | undefined {
		return this._state.value.blockung;
	}

	public get ergebnis(): GostBlockungsergebnis | undefined {
		return this._state.value.ergebnis;
	}

	public get mapBlockungen(): Map<number, GostBlockungListeneintrag> {
		return this._state.value.mapBlockungen;
	}

	public get getDatenmanager(): GostBlockungsdatenManager {
		return this._state.value.getDatenmanager;
	}

	public async ladeAbiturjahrgaenge() {
		const listAbiturjahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema);
		const mapAbiturjahrgaenge = new Map<number, GostJahrgang>();
		for (const l of listAbiturjahrgaenge)
			mapAbiturjahrgaenge.set(l.abiturjahr, l);
		this.setPatchedState({ mapAbiturjahrgaenge });
	}

	public setAbiturjahrgang = async (abiturjahrgang: GostJahrgang) => {
		this.setPatchedState({abiturjahrgang, halbjahr: undefined, mapBlockungen: new Map(), getDatenmanager: new GostBlockungsdatenManager()})
	}

	public setHalbjahr = async (halbjahr: GostHalbjahr) => {
		if (this._state.value.abiturjahrgang === undefined)
			throw new DeveloperNotificationException("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		api.status.start();
		const listBlockungen = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, this._state.value.abiturjahrgang.abiturjahr, halbjahr.id);
		const mapBlockungen: Map<number, GostBlockungListeneintrag> = new Map();
		let blockung = undefined;
		if (listBlockungen.size()) {
			blockung = listBlockungen.getFirst();
			for (const b of listBlockungen) {
				mapBlockungen.set(b.id, b);
				if (b.istAktiv)
					blockung = b;
			}
		}
		api.status.stop();
		this.setPatchedState({ halbjahr, mapBlockungen, getDatenmanager: new GostBlockungsdatenManager() });
	}

	setBlockung = async (blockung: GostBlockungListeneintrag) => {
		if (this._state.value.abiturjahrgang === undefined)
			throw new DeveloperNotificationException("Es kann keine Blockung ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		api.status.start();
		const blockungsdatenGzip = await api.server.getGostBlockungGZip(api.schema, blockung.id);
		const blockungsdatenBlob = await new Response(blockungsdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const blockungsdaten = GostBlockungsdaten.transpilerFromJSON(await blockungsdatenBlob.text());
		const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, this._state.value.abiturjahrgang.abiturjahr);
		const faecherManager = new GostFaecherManager(listFaecher);
		const getDatenmanager = new GostBlockungsdatenManager(blockungsdaten, faecherManager);
		const ergebnisse = getDatenmanager.ergebnisGetListeSortiertNachBewertung();
		let ergebnis = undefined;
		if (ergebnisse.size()) {
			ergebnis = ergebnisse.getFirst();
			for (const e of getDatenmanager.ergebnisGetListeSortiertNachBewertung())
				if (e.istAktiv)
					ergebnis = e;
		}
		api.status.stop();
		this.setPatchedState({ blockung, getDatenmanager, ergebnis });
	}

	public setErgebnis = async (ergebnis: GostBlockungsergebnis) => {
		this.setPatchedState({ergebnis})
	}

	public async gotoErgebnis(abiturjahr?: number, idHalbjahr?: number, idBlockung?: number, idErgebnis?: number) {
		if (abiturjahr === undefined || idHalbjahr === undefined || idBlockung === undefined || idErgebnis === undefined)
			return;
		const abiturjahrgang = this.mapAbiturjahrgaenge.get(abiturjahr);
		const halbjahr = GostHalbjahr.fromID(idHalbjahr);
		const listBlockungen = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, abiturjahr, idHalbjahr);
		const mapBlockungen: Map<number, GostBlockungListeneintrag> = new Map();
		for (const b of listBlockungen)
			mapBlockungen.set(b.id, b);
		const blockung = mapBlockungen.get(idBlockung);
		if (abiturjahrgang === undefined || blockung === undefined || halbjahr === null)
			return;
		const blockungsdatenGzip = await api.server.getGostBlockungGZip(api.schema, blockung.id);
		const blockungsdatenBlob = await new Response(blockungsdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const blockungsdaten = GostBlockungsdaten.transpilerFromJSON(await blockungsdatenBlob.text());
		const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, abiturjahrgang.abiturjahr);
		const faecherManager = new GostFaecherManager(listFaecher);
		const getDatenmanager = new GostBlockungsdatenManager(blockungsdaten, faecherManager);
		const ergebnis = getDatenmanager.ergebnisGet(idErgebnis);
		this.setPatchedState({abiturjahrgang, halbjahr, blockung, getDatenmanager, ergebnis, mapBlockungen});
	}

	public async importUntisGPU(apimethod : () => Promise<SimpleOperationResponse>) : Promise<SimpleOperationResponse> {
		try {
			return await apimethod();
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 404) || (e.response.status === 409) || (e.response.status === 500))) {
				const json : string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error)
				result.log.add("  " + e.message);
			return result;
		}
	}

	importUntisStundenplanGPU001 = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importStundenplanUntisGPU001(formData, api.schema));
	}

	importUntisRaeumeGPU005 = async (formData: FormData) : Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importUntisRaeumeGPU005(formData, api.schema));
	}

	exportUntisBlockungenZIP = async (formData: FormData): Promise<ApiFile> => {
		const ergebnisID = formData.get('ergebnisID');
		const unterrichtID = formData.get('unterrichtID');
		if (typeof ergebnisID === 'string' && typeof unterrichtID === 'string')
			return await api.server.exportUntisKursblockungAsZip(api.schema, parseInt(ergebnisID), parseInt(unterrichtID));
		throw new DeveloperNotificationException(`Es konnte keine Exportdatei für die ergebnisID ${ergebnisID} und unterrichtID ${unterrichtID} erstellt werden`);
	}

}


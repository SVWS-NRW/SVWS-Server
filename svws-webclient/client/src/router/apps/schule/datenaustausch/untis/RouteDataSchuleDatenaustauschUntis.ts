import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuleDatenaustauschUntisStundenplan } from "./RouteSchuleDatenaustauschUntisStundenplan";
import type { ApiFile, GostBlockungListeneintrag, GostBlockungsergebnis, GostJahrgang, List, GostHalbjahr } from "@core";
import { DeveloperNotificationException, OpenApiError, SimpleOperationResponse, GostBlockungsdatenManager, GostBlockungsdaten, GostFaecherManager, ArrayList } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuleDatenaustauschUntisBlockungen } from "./RouteSchuleDatenaustauschUntisBlockungen";


interface RouteStateDatenaustauschUntis extends RouteStateInterface {
	abiturjahrgang: GostJahrgang | undefined;
	halbjahr: GostHalbjahr | undefined;
	blockung: GostBlockungListeneintrag | undefined;
	ergebnis: GostBlockungsergebnis | undefined;
	mapAbiturjahrgaenge: Map<number, GostJahrgang>;
	mapBlockungen: Map<number, Map<number, Map<number, GostBlockungListeneintrag>>>;
	mapBlockung: Map<number, GostBlockungsdaten>;
	mapErgebnisse: Map<number, List<GostBlockungsergebnis>>;
}

const defaultState = <RouteStateDatenaustauschUntis> {
	abiturjahrgang: undefined,
	halbjahr: undefined,
	blockung: undefined,
	ergebnis: undefined,
	mapAbiturjahrgaenge: new Map(),
	mapBlockungen: new Map(),
	mapBlockung: new Map(),
	mapErgebnisse: new Map(),
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

	public get mapBlockung(): Map<number, GostBlockungsdaten> {
		return this._state.value.mapBlockung;
	}

	public get ergebnis(): GostBlockungsergebnis | undefined {
		return this._state.value.ergebnis;
	}

	public get listErgebnisse(): List<GostBlockungsergebnis> {
		return (this.blockung && this.mapErgebnisse.get(this.blockung.id)) || new ArrayList();
	}

	public get mapBlockungen(): Map<number, Map<number, Map<number, GostBlockungListeneintrag>>> {
		return this._state.value.mapBlockungen;
	}

	public get listBlockungen(): List<GostBlockungListeneintrag> {
		const abiturjahr = this.abiturjahrgang?.abiturjahr;
		const halbjahr = this.halbjahr?.id;
		const list = new ArrayList<GostBlockungListeneintrag>();
		if (abiturjahr === undefined || halbjahr === undefined)
			return list;
		const mapJahrgang = this.mapBlockungen.get(abiturjahr);
		if (mapJahrgang === undefined)
			return list;
		const mapHalbjahr = mapJahrgang.get(halbjahr);
		if (mapHalbjahr === undefined)
			return list;
		for (const b of mapHalbjahr.values())
			list.add(b);
		return list;
	}

	public get mapErgebnisse(): Map<number, List<GostBlockungsergebnis>> {
		return this._state.value.mapErgebnisse;
	}

	public async ladeAbiturjahrgaenge() {
		const listAbiturjahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema);
		const mapAbiturjahrgaenge = new Map<number, GostJahrgang>();
		for (const l of listAbiturjahrgaenge)
			mapAbiturjahrgaenge.set(l.abiturjahr, l);
		this.setPatchedDefaultState({ mapAbiturjahrgaenge });
	}

	public gotoAbiturjahrgang = async (abiturjahr: number) => RouteManager.doRoute(routeSchuleDatenaustauschUntisBlockungen.getRoute({abiturjahr: abiturjahr.toString()}));

	public gotoHalbjahr = async (idHalbjahr: number) => {
		const abiturjahrgang = this.abiturjahrgang;
		if (abiturjahrgang === undefined)
			return;
		return RouteManager.doRoute(routeSchuleDatenaustauschUntisBlockungen.getRoute({abiturjahr: abiturjahrgang.abiturjahr.toString(), halbjahr: idHalbjahr.toString()}));
	}

	public gotoBlockung = async (idBlockung: number) => {
		const abiturjahrgang = this.abiturjahrgang;
		const halbjahr = this.halbjahr;
		if (abiturjahrgang === undefined || halbjahr === undefined)
			return;
		return RouteManager.doRoute(routeSchuleDatenaustauschUntisBlockungen.getRoute({abiturjahr: abiturjahrgang.abiturjahr.toString(), halbjahr: halbjahr.id.toString(), idblockung: idBlockung.toString()}));
	}

	public gotoErgebnis = async (idErgebnis: number) => {
		const abiturjahrgang = this.abiturjahrgang;
		const halbjahr = this.halbjahr;
		const blockung = this.blockung;
		if (abiturjahrgang === undefined || halbjahr === undefined || blockung === undefined)
			return;
		return RouteManager.doRoute(routeSchuleDatenaustauschUntisBlockungen.getRoute({abiturjahr: abiturjahrgang.abiturjahr.toString(), halbjahr: halbjahr.id.toString(), idblockung: blockung.id.toString(), idergebnis: idErgebnis.toString()}));
	}

	public setAbiturjahrgang = async (abiturjahrgang: GostJahrgang) => {
		const mapAbiturjahr = this.mapBlockungen.get(abiturjahrgang.abiturjahr);
		if (mapAbiturjahr === undefined)
			this.mapBlockungen.set(abiturjahrgang.abiturjahr, new Map());
		this.setPatchedState({abiturjahrgang, halbjahr: undefined, mapBlockungen: this.mapBlockungen, blockung: undefined, ergebnis: undefined})
	}

	public setHalbjahr = async (halbjahr: GostHalbjahr) => {
		const abiturjahr = this.abiturjahrgang?.abiturjahr;
		if (abiturjahr === undefined)
			throw new DeveloperNotificationException("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		api.status.start();
		const mapAbiturjahr = this.mapBlockungen.get(abiturjahr);
		let blockung = undefined;
		if (mapAbiturjahr) {
			const map = mapAbiturjahr.get(halbjahr.id)
			if (!map) {
				const listBlockungen = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, abiturjahr, halbjahr.id);
				const mapBlockungen: Map<number, GostBlockungListeneintrag> = new Map();
				if (listBlockungen.size()) {
					blockung = listBlockungen.getFirst();
					for (const b of listBlockungen) {
						mapBlockungen.set(b.id, b);
						if (b.istAktiv)
							blockung = b;
					}
				}
				mapAbiturjahr.set(halbjahr.id, mapBlockungen);
			} else {
				if (map.size) {
					blockung = map.values().next().value;
					for (const b of map.values())
						if (b.istAktiv)
							blockung = b;
				}
			}
		}
		api.status.stop();
		this.setPatchedState({ halbjahr, mapBlockungen: this.mapBlockungen, blockung, ergebnis: undefined });
		if (blockung !== undefined)
			await this.setBlockung(blockung);
	}

	setBlockung = async (blockung: GostBlockungListeneintrag) => {
		if (this.abiturjahrgang === undefined)
			throw new DeveloperNotificationException("Es kann keine Blockung ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		api.status.start();
		let ergebnis = undefined;
		if (!this.mapBlockung.get(blockung.id)) {
			const blockungsdatenGzip = await api.server.getGostBlockungGZip(api.schema, blockung.id);
			const blockungsdatenBlob = await new Response(blockungsdatenGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
			const blockungsdaten = GostBlockungsdaten.transpilerFromJSON(await blockungsdatenBlob.text());
			const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, this.abiturjahrgang.abiturjahr);
			const faecherManager = new GostFaecherManager(listFaecher);
			const getDatenmanager = new GostBlockungsdatenManager(blockungsdaten, faecherManager);
			const ergebnisse = getDatenmanager.ergebnisGetListeSortiertNachBewertung();
			this.mapErgebnisse.set(blockung.id, ergebnisse);
			this.mapBlockung.set(blockung.id, blockungsdaten);
		}
		const ergebnisse = this.mapErgebnisse.get(blockung.id);
		if (ergebnisse && ergebnisse.size()) {
			ergebnis = ergebnisse.getFirst();
			for (const e of ergebnisse) {
				if (e.istAktiv)
					ergebnis = e;
			}
		}
		api.status.stop();
		this.setPatchedState({ blockung, ergebnis, mapErgebnisse: this.mapErgebnisse, mapBlockung: this.mapBlockung });
	}

	public setErgebnis = async (ergebnis: GostBlockungsergebnis) => {
		this.setPatchedState({ergebnis})
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


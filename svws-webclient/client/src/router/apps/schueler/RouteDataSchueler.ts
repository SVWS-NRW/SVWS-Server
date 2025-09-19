import type { ApiFile, List, ReportingParameter, SchuelerListeEintrag, SchuelerStammdaten, SimpleOperationResponse, StundenplanListeEintrag, SchuelerTelefon, SchuelerSchulbesuchsdaten, ErzieherStammdaten, Merkmal, KatalogEntlassgrund, SchulEintrag } from "@core";
import { BenutzerKompetenz, ArrayList, SchuelerListe, SchuelerStatus, DeveloperNotificationException, ServerMode, UserNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { ViewType, SchuelerListeManager, type PendingStateManager } from "@ui";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerNeuSchnelleingabe } from "~/router/apps/schueler/RouteSchuelerNeuSchnelleingabe";
import { SchuelerSchulbesuchManager } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchManager";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";


interface RouteStateSchueler extends RouteStateAuswahlInterface<SchuelerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	schuelerSchulbesuchManager: SchuelerSchulbesuchManager | undefined;
	listSchuelerErziehereintraege: List<ErzieherStammdaten>;
	listSchuelerTelefoneintraege: List<SchuelerTelefon>;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchuelerIndividualdaten,
	gruppenprozesseView: routeSchuelerIndividualdatenGruppenprozesse,
	activeViewType: ViewType.DEFAULT,
	mapStundenplaene: new Map(),
	schuelerSchulbesuchManager: undefined,
	listSchuelerErziehereintraege: new ArrayList(),
	listSchuelerTelefoneintraege: new ArrayList(),
	pendingStateRegistry: undefined,
};

export class RouteDataSchueler extends RouteDataAuswahl<SchuelerListeManager, RouteStateSchueler> {

	public constructor() {
		super(defaultState, { hinzufuegen: routeSchuelerNeu, schnelleingabe: routeSchuelerNeuSchnelleingabe });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateSchueler>> {
		// Lade die Daten von der API
		const auswahllisteGzip = await api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const auswahllisteBlob = await new Response(auswahllisteGzip.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
		const schuelerListe: SchuelerListe = SchuelerListe.transpilerFromJSON(await auswahllisteBlob.text());
		const lehrer = await api.server.getLehrer(api.schema);

		// Erstelle den Schüler-Liste-Manager
		const manager = new SchuelerListeManager(api.schulform, schuelerListe, lehrer, api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);

		// Übernehme den Filter von dem vorigen Manager oder initialisiere ihn neu, falls kein voriger Manager vorhanden ist
		if (this._state.value.manager === undefined) {
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		} else {
			manager.useFilter(this._state.value.manager);
		}

		// Hinweis: Dieses Nachträgliche Verändern des DefaultStates wurde gemacht, weil zum Zeitpunkt der Klassen initialisierung der ServerMode noch nicht
		// abgerufen wurde und somit die Bedingung, welche Route als Default für Gruppenprozesse genutzt werden soll, nicht geprüft werden kann
		// Diese Stelle eignet sich als Alternative, da sie noch vor dem ersten Betreten der Route aber bereits nach dem Abruf der ServerModes liegt
		// TODO: Ausbauen sobald die Route routeSchuelerIndividualdatenGruppenprozesse im "Stable" Mode bereitsteht
		if (api.mode !== ServerMode.DEV)
			this._defaultState = { ...defaultState, gruppenprozesseView: routeSchuelerAllgemeinesGruppenprozesse };

		return { manager };
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null, state: Partial<RouteStateSchueler>) : Promise<SchuelerStammdaten | null> {
		if (auswahl === null)
			return null;
		const res = await api.server.getSchuelerStammdaten(api.schema, auswahl.id);
		const listSchuelerTelefoneintraege = await api.server.getSchuelerTelefone(api.schema, auswahl.id);
		const listSchuelerErziehereintraege = await api.server.getSchuelerErzieher(api.schema, auswahl.id);

		const schuelerSchulbesuchsdaten: SchuelerSchulbesuchsdaten = await api.server.getSchuelerSchulbesuch(api.schema, auswahl.id);
		const kindergaerten = await api.server.getKindergaerten(api.schema)
		const schulen = new ArrayList<SchulEintrag>();
		const merkmale = new ArrayList<Merkmal>();
		const entlassgruende = new ArrayList<KatalogEntlassgrund>();
		const schuelerSchulbesuchManager = new SchuelerSchulbesuchManager(
			schuelerSchulbesuchsdaten, auswahl, api.schuleStammdaten.abschnitte, schulen, merkmale, entlassgruende, kindergaerten, routeSchuelerSchulbesuch.data.patch);

		this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(res.status));
		this.setPatchedState({ listSchuelerErziehereintraege, listSchuelerTelefoneintraege, schuelerSchulbesuchManager });
		return res;
	}

	public async ladeDatenMultiple(auswahlList: List<SchuelerListeEintrag>, state: Partial<RouteStateSchueler>): Promise<List<SchuelerStammdaten> | null> {
		if (auswahlList.isEmpty())
			return null;

		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList){
			ids.add(eintrag.id);
		}
		const response = await api.server.getSchuelerStammdatenMultiple(ids, api.schema);
		// TODO: derzeit müsste bei einem Bulk selekt zu jedem Schüler einzeln ein API Call für Telefone gemacht werden, muss umgebaut werden
		// const schuelerTelefone = await api.server.getSchuelerTelefone(api.schema, auswahl.id);
		// this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(response.status));
		// state.listSchuelerTelefoneintraege = schuelerTelefone;

		return response;
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedState({ mapStundenplaene });
	}

	// /**
	//  * Gebe einen Dummy-Manager zurück, wenn noch keine Initialisierung des Managers stattgefunden hat.
	//  *
	//  * @returns der Dummy-Manager
	//  */
	// protected getDummyManager() : SchuelerListeManager | undefined {
	// 	return new SchuelerListeManager(api.schulform, new SchuelerListe(), new ArrayList(), api.schuleStammdaten.abschnitte, api.schuleStammdaten.idSchuljahresabschnitt);
	// }

	addSchueler = async (data: Partial<SchuelerStammdaten>): Promise<SchuelerStammdaten> => {
		const schuelerStammdaten = await api.server.addSchuelerStammdaten(data, api.schema, this._state.value.idSchuljahresabschnitt);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		return schuelerStammdaten;
	}

	get schuelerSchulbesuchManager(): SchuelerSchulbesuchManager {
		if (this._state.value.schuelerSchulbesuchManager === undefined)
			throw new DeveloperNotificationException("SchülerSchulbesuchManager nicht initialisiert.")
		return this._state.value.schuelerSchulbesuchManager;
	}

	patchSchuelerSchulbesuchdaten = async (data: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number) : Promise<void> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, idEintrag);
	}

	get getListSchuelerTelefoneintraege(): List<SchuelerTelefon> {
		const list = new ArrayList<SchuelerTelefon>();
		list.addAll(this._state.value.listSchuelerTelefoneintraege);
		return list;
	}

	get getListSchuelerErziehereintraege(): List<ErzieherStammdaten> {
		const list = new ArrayList<ErzieherStammdaten>();
		list.addAll(this._state.value.listSchuelerErziehereintraege);
		return list;
	}

	addSchuelerErziehereintrag = async (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number): Promise<ErzieherStammdaten> => {
		const erzieher = await api.server.addSchuelerErzieher(data, api.schema, idEintrag, pos);
		const listSchuelerErziehereintraege = this.getListSchuelerErziehereintraege;
		listSchuelerErziehereintraege.add(erzieher);
		this.setPatchedState({ listSchuelerErziehereintraege });
		return erzieher;
	}

	patchSchuelerErziehereintrag = async (data : Partial<ErzieherStammdaten>, idEintrag: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, idEintrag);
		const listSchuelerErziehereintraege = this.getListSchuelerErziehereintraege;
		for (const e of listSchuelerErziehereintraege)
			if (e.id === idEintrag) {
				Object.assign(e, data);
				break;
			}
		this.setPatchedState({ listSchuelerErziehereintraege });
	}

	patchSchuelerErzieherAnPosition = async (data : Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => {
		await api.server.patchErzieherStammdatenZweitePosition(data, api.schema, idEintrag, pos);
		const listSchuelerErziehereintraege = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({ listSchuelerErziehereintraege });
	}

	deleteSchuelerErziehereintrage = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteErzieherStammdaten(idsEintraege, api.schema);
		const listSchuelerErziehereintraege = this.getListSchuelerErziehereintraege;
		for (const id of idsEintraege) {
			for (let i = 0; i < listSchuelerErziehereintraege.size(); i++) {
				const eintrag = listSchuelerErziehereintraege.get(i);
				if (eintrag.id === id) {
					listSchuelerErziehereintraege.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listSchuelerErziehereintraege });
	}

	addSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> => {
		const telefon = await api.server.addSchuelerTelefon(data, api.schema, idSchueler);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		listSchuelerTelefoneintraege.add(telefon);
		this.setPatchedState({ listSchuelerTelefoneintraege });
	}

	patchSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		for (const l of listSchuelerTelefoneintraege)
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		this.setPatchedState({ listSchuelerTelefoneintraege });
	}

	deleteSchuelerTelefoneintrage = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		for (const id of idsEintraege) {
			for (let i = 0; i < listSchuelerTelefoneintraege .size(); i++) {
				const eintrag = listSchuelerTelefoneintraege .get(i);
				if (eintrag.id === id) {
					listSchuelerTelefoneintraege.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listSchuelerTelefoneintraege });
	}

	protected async doPatch(data : Partial<SchuelerStammdaten>, id: number) : Promise<void> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchueler(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null) : string {
		return `Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteSchuelerCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');

		if (!this.manager.liste.auswahlExists())
			errorLog.add('Es ist kein Schüler ausgewählt.');

		return [errorLog.isEmpty(), errorLog];
	}

	getPDF = api.call(async (reportingParameter: ReportingParameter): Promise<ApiFile> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
			return await api.server.pdfReport(reportingParameter, api.schema);
		}
		throw new UserNotificationException("Dieser Report kann nur gedruckt werden, wenn mindestens ein Schüler ausgewählt ist.");
	})

	patchMultiple = async (pendingStateManager: PendingStateManager<any>): Promise<void> => {
		api.status.start();

		const partialsToPatch = pendingStateManager.partials;
		await api.server.patchSchuelerStammdatenMultiple(partialsToPatch, api.schema);

		// Übernehme nur geänderte SchuelerStammdaten Objekte in den AuswahlManager, damit nicht alle Stammdaten neugeladen werden müssen
		for (const partialToPatch of partialsToPatch) {
			if (partialToPatch.id !== undefined) {
				const patchId = (partialToPatch as Record<string, any>)[pendingStateManager.idFieldName];
				const currentStammdaten = this._state.value.manager?.getListeDaten().get(patchId);
				this._state.value.manager?.getListeDaten().put(patchId, Object.assign(Object.assign({}, currentStammdaten), partialToPatch));
			}
		}

		pendingStateManager.resetPendingState();
		this.commit();
		api.status.stop();
	}

}

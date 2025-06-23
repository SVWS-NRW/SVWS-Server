import type { ApiFile, List, ReportingParameter, SchuelerListeEintrag, SchuelerStammdaten, SimpleOperationResponse, StundenplanListeEintrag, SchuelerTelefon, SchuelerSchulbesuchsdaten, } from "@core";

import { BenutzerKompetenz, ArrayList, SchuelerListe, SchuelerListeManager, SchuelerStatus, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { ViewType } from "@ui";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import type { PendingStateManager } from "@ui";


interface RouteStateSchueler extends RouteStateAuswahlInterface<SchuelerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	listSchuelerTelefoneintraege: List<SchuelerTelefon>;
};

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchuelerIndividualdaten,
	gruppenprozesseView: routeSchuelerIndividualdatenGruppenprozesse,
	activeViewType: ViewType.DEFAULT,
	mapStundenplaene: new Map(),
	listSchuelerTelefoneintraege: new ArrayList(),
	pendingStateRegistry: undefined,
};

export class RouteDataSchueler extends RouteDataAuswahl<SchuelerListeManager, RouteStateSchueler> {

	public constructor() {
		super(defaultState, { hinzufuegen: routeSchuelerNeu });
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

		return { manager };
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null, state: Partial<RouteStateSchueler>) : Promise<SchuelerStammdaten | null> {
		if (auswahl === null)
			return null;
		const res = await api.server.getSchuelerStammdaten(api.schema, auswahl.id);
		const listSchuelerTelefoneintraege = await api.server.getSchuelerTelefone(api.schema, auswahl.id);
		this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(res.status));
		this.setPatchedState({ listSchuelerTelefoneintraege });
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
		// await this.gotoDefaultView(schuelerStammdaten.id);
		return schuelerStammdaten;
	}

	patchSchuelerNeu = async (data : Partial<SchuelerStammdaten>, id: number) : Promise<void> => {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
	}

	get listSchuelerTelefoneintraege(): List<SchuelerTelefon> {
		return this._state.value.listSchuelerTelefoneintraege;
	}

	addSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> => {
		api.status.start();
		const telefon = await api.server.addSchuelerTelefon(data, api.schema, idSchueler);
		this.listSchuelerTelefoneintraege.add(telefon);
		this.setPatchedState({ listSchuelerTelefoneintraege: <List<SchuelerTelefon>>this.listSchuelerTelefoneintraege.clone() });
		api.status.stop();
	}

	patchSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void> => {
		api.status.start();
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		for (const l of this.listSchuelerTelefoneintraege)
			if (l.id === idEintrag)
				Object.assign(l, data);
		this.setPatchedState({ listSchuelerTelefoneintraege: <List<SchuelerTelefon>>this.listSchuelerTelefoneintraege.clone() });
		api.status.stop();
	}

	deleteSchuelerTelefoneintrage = async (idsEintraege: List<number>): Promise<void> => {
		api.status.start();
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		const liste = this.listSchuelerTelefoneintraege;
		for (const id of idsEintraege) {
			for (let i = 0; i < liste.size(); i++) {
				const eintrag = liste.get(i);
				if (eintrag.id === id) {
					liste.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listSchuelerTelefoneintraege: <List<SchuelerTelefon>>this.listSchuelerTelefoneintraege.clone() });
		api.status.stop();
	}

	patchSchuelerKindergarten = async (data : Partial<SchuelerSchulbesuchsdaten>, id: number) : Promise<void> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, id);
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

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens eine Klasse ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl())
			reportingParameter.idsDetaildaten.add(l.id);
		return await api.server.pdfReport(reportingParameter, api.schema);
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

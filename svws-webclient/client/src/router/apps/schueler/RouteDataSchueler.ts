import type { ApiFile, List, ReportingParameter, SchuelerListeEintrag, SchuelerStammdaten, SimpleOperationResponse, StundenplanListeEintrag, SchuelerTelefon, SchuelerSchulbesuchsdaten, ErzieherStammdaten, SchuelerStammdatenNeu, SchuelerLernabschnittsdaten, KlassenDaten, SchuelerVermerke } from "@core";
import { BenutzerKompetenz, ArrayList, SchuelerStatus, ServerMode, UserNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { ViewType, SchuelerListeManager, type PendingStateManager } from "@ui";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerNeuSchnelleingabe } from "~/router/apps/schueler/RouteSchuelerNeuSchnelleingabe";


interface RouteStateSchueler extends RouteStateAuswahlInterface<SchuelerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	listSchuelerErziehereintraege: List<ErzieherStammdaten>;
	listSchuelerTelefoneintraege: List<SchuelerTelefon>;
	listSchuelerVermerkeintraege: List<SchuelerVermerke>;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchuelerIndividualdaten,
	gruppenprozesseView: routeSchuelerIndividualdatenGruppenprozesse,
	activeViewType: ViewType.DEFAULT,
	mapStundenplaene: new Map(),
	listSchuelerErziehereintraege: new ArrayList(),
	listSchuelerTelefoneintraege: new ArrayList(),
	listSchuelerVermerkeintraege: new ArrayList(),
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

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateSchueler>> {
		// Lade die Daten von der API
		const [schuelerListe, lehrer] = await Promise.all([
			api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrer(api.schema),
		]);

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
		if (api.mode !== ServerMode.DEV) {
			this._defaultState = { ...defaultState, gruppenprozesseView: routeSchuelerAllgemeinesGruppenprozesse };
		}

		return { manager };
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null, state: Partial<RouteStateSchueler>): Promise<SchuelerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}
		const [res, listSchuelerTelefoneintraege, listSchuelerErziehereintraege, listSchuelerVermerkeintraege] = await Promise.all([
			api.server.getSchuelerStammdaten(api.schema, auswahl.id),
			api.server.getSchuelerTelefone(api.schema, auswahl.id),
			api.server.getSchuelerErzieher(api.schema, auswahl.id),
			api.server.getVermerkdaten(api.schema, auswahl.id),
		]);
		this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(res.status));
		state.listSchuelerErziehereintraege = listSchuelerErziehereintraege;
		state.listSchuelerTelefoneintraege = listSchuelerTelefoneintraege;
		state.listSchuelerVermerkeintraege = listSchuelerVermerkeintraege;
		return res;
	}

	public async ladeDatenMultiple(auswahlList: List<SchuelerListeEintrag>, state: Partial<RouteStateSchueler>): Promise<List<SchuelerStammdaten> | null> {
		if (auswahlList.isEmpty()) {
			return null;
		}

		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList) {
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
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		if (api.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
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

	addSchueler = async (data: Partial<SchuelerStammdatenNeu>, idSchulJahresabschnitt: number): Promise<SchuelerStammdaten> => {
		const schuelerStammdaten = await api.server.addSchuelerStammdaten(data, api.schema, idSchulJahresabschnitt);
		await this.setSchuljahresabschnitt(idSchulJahresabschnitt, true);
		this.manager.setDaten(schuelerStammdaten);
		return schuelerStammdaten;
	};

	getSchuelerKlassenFuerAbschnitt = async (idAbschnitt: number): Promise<List<KlassenDaten>> => {
		return await api.server.getKlassenFuerAbschnitt(api.schema, idAbschnitt);
	};

	patchSchuelerLernabschnitt = async (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerLernabschnittsdaten(data, api.schema, idEintrag);
		Object.assign(idEintrag, data);
		this.commit();
	};

	patchSchuelerSchulbesuchdaten = async (data: Partial<SchuelerSchulbesuchsdaten>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerSchulbesuch(data, api.schema, idEintrag);
	};

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

	get getListSchuelerVermerkeintraege(): List<SchuelerVermerke> {
		const list = new ArrayList<SchuelerVermerke>();
		list.addAll(this._state.value.listSchuelerVermerkeintraege);
		return list;
	}

	addSchuelerErziehereintrag = async (data: Partial<ErzieherStammdaten>, idEintrag: number, pos: number): Promise<ErzieherStammdaten> => {
		const erzieher = await api.server.addSchuelerErzieher(data, api.schema, idEintrag, pos);
		const listSchuelerErziehereintraege = this.getListSchuelerErziehereintraege;
		listSchuelerErziehereintraege.add(erzieher);
		this.setPatchedState({ listSchuelerErziehereintraege });
		return erzieher;
	};

	patchSchuelerErziehereintrag = async (data: Partial<ErzieherStammdaten>, idEintrag: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, idEintrag);
		const listSchuelerErziehereintraege = this.getListSchuelerErziehereintraege;
		for (const e of listSchuelerErziehereintraege) {
			if (e.id === idEintrag) {
				Object.assign(e, data);
				break;
			}
		}
		this.setPatchedState({ listSchuelerErziehereintraege });
	};

	patchSchuelerErzieherAnPosition = async (data: Partial<ErzieherStammdaten>, idEintrag: number, idSchueler: number, pos: number) => {
		await api.server.patchErzieherStammdatenZweitePosition(data, api.schema, idEintrag, pos);
		const listSchuelerErziehereintraege = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({ listSchuelerErziehereintraege });
	};

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
	};

	addSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> => {
		const telefon = await api.server.addSchuelerTelefon(data, api.schema, idSchueler);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		listSchuelerTelefoneintraege.add(telefon);
		this.setPatchedState({ listSchuelerTelefoneintraege });
	};

	patchSchuelerTelefoneintrag = async (data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		for (const l of listSchuelerTelefoneintraege) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ listSchuelerTelefoneintraege });
	};

	deleteSchuelerTelefoneintrage = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		const listSchuelerTelefoneintraege = this.getListSchuelerTelefoneintraege;
		for (const id of idsEintraege) {
			for (let i = 0; i < listSchuelerTelefoneintraege.size(); i++) {
				const eintrag = listSchuelerTelefoneintraege.get(i);
				if (eintrag.id === id) {
					listSchuelerTelefoneintraege.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listSchuelerTelefoneintraege });
	};

	addSchuelerVermerkeintrag = async (data: Partial<SchuelerVermerke>): Promise<void> => {
		const vermerk = await api.server.addVermerk(data, api.schema);
		const listSchuelerVermerkeintraege = this.getListSchuelerVermerkeintraege;
		listSchuelerVermerkeintraege.add(vermerk);
		this.setPatchedState({ listSchuelerVermerkeintraege });
	};

	patchSchuelerVermerkeintrag = async (data: Partial<SchuelerVermerke>, idEintrag: number): Promise<void> => {
		await api.server.patchSchuelerVermerke(data, api.schema, idEintrag);
		const listSchuelerVermerkeintraege = this.getListSchuelerVermerkeintraege;
		for (const l of listSchuelerVermerkeintraege) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ listSchuelerVermerkeintraege });
	};

	deleteSchuelerVermerkeintrage = async (idsEintraege: List<number>): Promise<void> => {
		await api.server.deleteSchuelerVermerke(idsEintraege, api.schema);
		const listSchuelerVermerkeintraege = this.getListSchuelerVermerkeintraege;
		for (const id of idsEintraege) {
			for (let i = 0; i < listSchuelerVermerkeintraege.size(); i++) {
				const eintrag = listSchuelerVermerkeintraege.get(i);
				if (eintrag.id === id) {
					listSchuelerVermerkeintraege.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listSchuelerVermerkeintraege });
	};

	protected async doPatch(data: Partial<SchuelerStammdaten>, id: number): Promise<void> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchueler(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null): string {
		return `Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteSchuelerCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es ist kein Schüler ausgewählt.');
		}

		return [errorLog.isEmpty(), errorLog];
	};

	getPDF = api.call(async (reportingParameter: ReportingParameter): Promise<ApiFile> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
			return await api.server.pdfReport(reportingParameter, api.schema);
		}
		throw new UserNotificationException("Dieser Report kann nur gedruckt werden, wenn mindestens ein Schüler ausgewählt ist.");
	});

	sendEMail = api.call(async (reportingParameter: ReportingParameter): Promise<SimpleOperationResponse> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
			return await api.server.emailReport(reportingParameter, api.schema);
		}
		throw new UserNotificationException("Dieser Report kann nur versendet werden, wenn mindestens ein Schüler ausgewählt ist.");
	});

	fetchEMailJobStatus = api.call(async (jobId: number): Promise<SimpleOperationResponse> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			return await api.server.getEmailJobStatus(api.schema, jobId);
		}
		throw new UserNotificationException("Dieser Report kann nur versendet werden, wenn mindestens ein Schüler ausgewählt ist.");
	});

	fetchEMailJobLog = api.call(async (jobId: number): Promise<SimpleOperationResponse> => {
		if (this.manager.liste.auswahlExists() || this.manager.hasDaten()) {
			return await api.server.getEmailJobLog(api.schema, jobId);
		}
		throw new UserNotificationException("Dieser Report kann nur versendet werden, wenn mindestens ein Schüler ausgewählt ist.");
	});

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
	};

}

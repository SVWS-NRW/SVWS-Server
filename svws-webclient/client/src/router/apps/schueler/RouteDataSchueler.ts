import { api } from "~/router/Api";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchuelerNeu } from "~/router/apps/schueler/neu/RouteSchuelerNeu";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { serverStateImpl } from "~/states/ServerStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { ViewType } from "@ui/ui/nav/ViewType";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";


interface RouteStateSchueler extends RouteStateAuswahlInterface<SchuelerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	listSchuelerTelefoneintraege: List<SchuelerTelefon>;
}

const defaultState = <RouteStateSchueler> {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchuelerIndividualdaten,
	gruppenprozesseView: routeSchuelerIndividualdatenGruppenprozesse,
	activeViewType: ViewType.DEFAULT,
	mapStundenplaene: new Map(),
	pendingStateRegistry: undefined,
	listSchuelerTelefoneintraege: new ArrayList(),
};

export class RouteDataSchueler extends RouteDataAuswahl<SchuelerListeManager, RouteStateSchueler> {

	public constructor() {
		super(defaultState, { hinzufuegen: routeSchuelerNeu, schnelleingabe: routeSchuelerSchnelleingabe });
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
		const manager = new SchuelerListeManager(schuleStateImpl.schulform, schuelerListe, lehrer, abschnittStateImpl.alle, schuleStateImpl.abschnitt.id);

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
		if (!serverStateImpl.hasDev) {
			this._defaultState = { ...defaultState, gruppenprozesseView: routeSchuelerAllgemeinesGruppenprozesse };
		}

		return { manager };
	}

	public async ladeDaten(auswahl: SchuelerListeEintrag | null, state: Partial<RouteStateSchueler>): Promise<SchuelerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}

		const resolveSchuelerTelefoneOrEmpty = benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)
			? api.server.getSchuelerTelefone(api.schema, auswahl.id)
			: Promise.resolve(new ArrayList<SchuelerTelefon>());
		const [stammdaten, schuelerTelefone] = await Promise.all([
			api.server.getSchuelerStammdaten(api.schema, auswahl.id),
			resolveSchuelerTelefoneOrEmpty,
		]);

		this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(stammdaten.status));
		state.listSchuelerTelefoneintraege = schuelerTelefone;

		return stammdaten;
	}

	public async ladeDatenMultiple(auswahlList: List<SchuelerListeEintrag>, state: Partial<RouteStateSchueler>): Promise<List<SchuelerStammdaten> | null> {
		if (auswahlList.isEmpty()) {
			return null;
		}

		const ids: List<number> = new ArrayList();
		for (const eintrag of auswahlList) {
			ids.add(eintrag.id);
		}
		return await api.server.getSchuelerStammdatenMultiple(ids, api.schema);
		// TODO: derzeit müsste bei einem Bulk selekt zu jedem Schüler einzeln ein API Call für Telefone gemacht werden, muss umgebaut werden
		// const schuelerTelefone = await api.server.getSchuelerTelefone(api.schema, auswahl.id);
		// this.manager.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(response.status));
		// state.listSchuelerTelefoneintraege = schuelerTelefone;
	}
	public async updateMapStundenplaene() {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		if (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

	add = async (data: Partial<SchuelerNeu>): Promise<SchuelerStammdaten> => {
		const result = await api.server.addSchueler(data, api.schema);
		await this.setSchuljahresabschnitt(data.idSchuljahresabschnitt ?? -1, true);
		this.manager.setDaten(result);
		this.commit();
		return result;
	};

	get getListSchuelerTelefoneintraege(): List<SchuelerTelefon> {
		const list = new ArrayList<SchuelerTelefon>();
		list.addAll(this._state.value.listSchuelerTelefoneintraege);
		return list;
	}

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

	protected async doPatch(data: Partial<SchuelerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchueler(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null): string {
		return `Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteSchuelerCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');
		}

		return [errorLog.isEmpty(), errorLog];
	};

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

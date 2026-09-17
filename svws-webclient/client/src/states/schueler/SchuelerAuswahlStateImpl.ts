import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { useSchuleState } from "@ui/states/SchuleState";
import { ViewType } from "@ui/ui/nav/ViewType";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import { abschnittStateImpl } from "../AbschnittStateImpl";
import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { api } from "~/router/Api";

import type { SchuelerAuswahlState } from "./SchuelerAuswahlState";
import { schuelerAuswahlStateRoutingAdapter } from "./SchuelerAuswahlStateRoutingAdapter";
import { SchuelerListeManager } from "./SchuelerListeManager";

interface SchuelerAuswahlReactiveState extends GenericAuswahlReactiveState<SchuelerListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	listSchuelerTelefoneintraege: List<SchuelerTelefon>;
}

/**
 * Der State für die Auswahlliste der Schüler
 */
export class SchuelerAuswahlStateImpl extends GenericAuswahlStateImpl<SchuelerListeManager, SchuelerAuswahlReactiveState> implements SchuelerAuswahlState {

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			manager: undefined,
			activeViewType: ViewType.DEFAULT,
			mapStundenplaene: new Map(),
			listSchuelerTelefoneintraege: new ArrayList(),
		}, schuelerAuswahlStateRoutingAdapter);
	}

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	public async init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		return await super.setSchuljahresabschnitt(idSchuljahresabschnitt, force);
	}

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	/**
	 * Gibt eine Map mit den Stundenplänen des Schuljahresabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Stundenplänen
	 */
	public get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	/**
	 * Erstellt einen neunen Auswahl-Manager für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für den Manager und dessen Daten im KlassenState
	 */
	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<SchuelerAuswahlState>> {
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Daten von der API
		const [schuelerListe, lehrer] = await Promise.all([
			api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrer(api.schema),
		]);

		// Erstelle den Schüler-Liste-Manager
		const schuleState = useSchuleState();
		const manager = new SchuelerListeManager(
			schuleState.schulform,
			schuelerListe,
			lehrer,
			abschnittStateImpl.alle,
			schuleState.abschnitt.id);

		// Übernehme den Filter von dem vorigen Manager oder initialisiere ihn neu, falls kein voriger Manager vorhanden ist
		if (this._state.value.manager === undefined) {
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
			manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		} else {
			manager.useFilter(this._state.value.manager);
		}

		return { manager };
	}

	/**
	 * Lädt die Daten für den übergebenen Listeneintrag eines Schülers
	 *
	 * @param auswahl   der ausgewählte Listeneintrag eines Schülers
	 *
	 * @returns eine Promise mit den Daten zu dem Schüler
	 */
	public async ladeDaten(auswahl: SchuelerListeEintrag | null, state: Partial<SchuelerAuswahlReactiveState>): Promise<SchuelerStammdaten | null> {
		if (auswahl === null) {
			return null;
		}

		const benutzerState = useBenutzerState();
		const resolveSchuelerTelefoneOrEmpty = benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)
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

	public async ladeDatenMultiple(auswahlList: List<SchuelerListeEintrag>, state: Partial<SchuelerAuswahlReactiveState>): Promise<List<SchuelerStammdaten> | null> {
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

	/**
	 * Führt einen Patch auf den Schülerstammdaten über die API aus.
	 *
	 * @param data   die Daten des Patches
	 * @param id     die ID des zu patchenden Schülers
	 *
	 * @returns eine Promise, die bei Erfolg true zurückgibt
	 */
	protected async doPatch(data: Partial<SchuelerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
		return true;
	}

	public async patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void> {
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

	/**
	 * Führt eine LöscheOperation auf dem Server für die Klassen mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchueler(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null): string {
		return `Schüler ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteSchuelerCheck(): [boolean, List<string>] {
		const errorLog = new ArrayList<string>();
		const benutzerState = useBenutzerState();
		if (!benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');
		}
		return [errorLog.isEmpty(), errorLog];
	};



	public async updateMapStundenplaene(): Promise<void> {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const benutzerState = useBenutzerState();
		if (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

	public async add(data: Partial<SchuelerNeu>): Promise<SchuelerStammdaten> {
		const result = await api.server.addSchueler(data, api.schema);
		await this.setSchuljahresabschnitt(data.idSchuljahresabschnitt ?? -1, true);
		this.manager.setDaten(result);
		this.commit();
		return result;
	};


	get listTelefoneintraege(): List<SchuelerTelefon> {
		const list = new ArrayList<SchuelerTelefon>();
		list.addAll(this._state.value.listSchuelerTelefoneintraege);
		return list;
	}

	public async addTelefoneintrag(data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> {
		const telefon = await api.server.addSchuelerTelefon(data, api.schema, idSchueler);
		const listSchuelerTelefoneintraege = this.listTelefoneintraege;
		listSchuelerTelefoneintraege.add(telefon);
		this.setPatchedState({ listSchuelerTelefoneintraege });
	};

	public async patchTelefoneintrag(data: Partial<SchuelerTelefon>, idEintrag: number): Promise<void> {
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		const listSchuelerTelefoneintraege = this.listTelefoneintraege;
		for (const l of listSchuelerTelefoneintraege) {
			if (l.id === idEintrag) {
				Object.assign(l, data);
				break;
			}
		}
		this.setPatchedState({ listSchuelerTelefoneintraege });
	};

	public async deleteTelefoneintrage(idsEintraege: List<number>): Promise<void> {
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		const listSchuelerTelefoneintraege = this.listTelefoneintraege;
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

}

export const schuelerAuswahlStateImpl = new SchuelerAuswahlStateImpl();

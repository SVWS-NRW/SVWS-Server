import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { useConfigState } from "@ui/states/ConfigState";
import { useSchuleState } from "@ui/states/SchuleState";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager } from "~/router/RouteManager";

import type { KurseAuswahlState } from "./KurseAuswahlState";
import { kurseAuswahlStateRoutingAdapter } from "./KurseAuswahlStateRoutingAdapter";
import { KursListeManager } from "./KursListeManager";

interface KurseAuswahlReactiveState extends GenericAuswahlReactiveState<KursListeManager> {
}

/**
 * Der State für die Auswahlliste der Kurse
 */
export class KurseAuswahlStateImpl extends GenericAuswahlStateImpl<KursListeManager, KurseAuswahlReactiveState> implements KurseAuswahlState {

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			manager: undefined,
			activeViewType: ViewType.DEFAULT,
		}, kurseAuswahlStateRoutingAdapter);
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

	public get filterNurSichtbar(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("kurse.auswahl.filterNurSichtbar") === 'true';
	}

	public async setFilterNurSichtbar(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('kurse.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	/**
	 * Erstellt einen neunen Auswahl-Manager für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für den Manager und dessen Daten im KurseAuswahlState
	 */
	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<KurseAuswahlReactiveState>> {
		const abschnittState = useAbschnittState();
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Kataloge und erstelle den Manager
		const [listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher] = await Promise.all([
			api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getJahrgaenge(api.schema),
			api.server.getLehrerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getFaecher(api.schema),
		]);

		const schuleState = useSchuleState();
		const manager = new KursListeManager(
			idSchuljahresabschnitt,
			schuleState.abschnitt.id,
			abschnittState.alle,
			schuleState.schulform,
			listKurse,
			listSchueler,
			listJahrgaenge,
			listLehrer,
			listFaecher);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(this.filterNurSichtbar);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager };
	}

	/**
	 * Lädt die Daten für den übergebenen Listeneintrag eines Kurses
	 *
	 * @param auswahl   der ausgewählte Listeneintrag eines Kurses
	 *
	 * @returns eine Promise mit den Daten zu der Kurses
	 */
	public async ladeDaten(auswahl: KursDaten | null): Promise<KursDaten | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return auswahl;
	}

	public async ladeDatenMultiple(auswahlList: List<KursDaten>, state: Partial<KurseAuswahlReactiveState>): Promise<List<KursDaten> | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return auswahlList;
	}

	/**
	 * Führt einen Patch auf den Kursdaten über die API aus.
	 *
	 * @param data   die Daten des Patches
	 * @param id     die ID des zu patchenden Kurses
	 *
	 * @returns eine Promise, die bei Erfolg true zurückgibt
	 */
	protected async doPatch(data: Partial<KursDaten>, id: number): Promise<boolean> {
		await api.server.patchKurs(data, api.schema, id);
		return true;
	}

	/**
	 * Führt eine Lösch-Operation auf dem Server für die Kurse mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Kurse
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKurse(ids, api.schema);
	}

	protected deleteMessage(id: number, kurs: KursDaten | null): string {
		return `Kurs ${kurs?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public async add(partialKurs: Partial<KursDaten>): Promise<void> {
		const abschnittState = useAbschnittState();
		const neuerKurs = await api.server.addKurs({ ...partialKurs, idSchuljahresabschnitt: abschnittState.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neuerKurs.id);
	};

	public async gotoSchueler(eintrag: Schueler): Promise<void> {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	public async addKursLehrer(data: Partial<KursLehrer>, idKurs: number): Promise<void> {
		api.status.start();
		const result = await api.server.addKursLehrer(data, api.schema, idKurs);
		this.manager.daten().weitereLehrer.add(result);
		this.commit();
		api.status.stop();
	};

	public async patchKursLehrer(data: Partial<KursLehrer>, idKurs: number, idLehrer: number): Promise<void> {
		api.status.start();
		await api.server.patchKursLehrer(data, api.schema, idKurs, idLehrer);
		for (const k of this.manager.daten().weitereLehrer) {
			if (k.idLehrer === idLehrer) {
				Object.assign(k, data);
			}
		}
		this.commit();
		api.status.stop();
	};

	public async deleteKursLehrer(lehrerIds: List<number>, idKurs: number): Promise<void> {
		await api.server.deleteKursLehrer(lehrerIds, api.schema, idKurs);
		const weitereLehrer = this.manager.daten().weitereLehrer;
		for (let i = weitereLehrer.size() - 1; i >= 0; i--) {
			if (lehrerIds.contains(weitereLehrer.get(i).idLehrer)) {
				weitereLehrer.removeElementAt(i);
			}
		}
		this.commit();
	};

}

export const kurseAuswahlStateImpl = new KurseAuswahlStateImpl();

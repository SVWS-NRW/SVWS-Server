import type { RouteParamsRawGeneric } from "vue-router";

import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

import { routeUvPlanungsabschnitt } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnitte";
import { routeUvPlanungsabschnittGruppenprozesse } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnittGruppenprozesse";
import { routeUvPlanungsabschnittNeu } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnittNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { abschnittStateImpl as abschnittState } from "~/states/AbschnittStateImpl";
import { schuleStateImpl as schuleState } from "~/states/SchuleStateImpl";
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUnterrichtsverteilung extends RouteStateAuswahlInterface<UvPlanungsabschnitteListeManager> {
}

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeUvPlanungsabschnitt,
	activeViewType: ViewType.DEFAULT,
} as RouteStateUnterrichtsverteilung;

export class RouteDataUv extends RouteDataAuswahl<UvPlanungsabschnitteListeManager, RouteStateUnterrichtsverteilung> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeUvPlanungsabschnittGruppenprozesse, hinzufuegen: routeUvPlanungsabschnittNeu });
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, isEntering: boolean, forceReload: boolean = false): Promise<number | null> {
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		if (!forceReload && (this._state.value.idSchuljahresabschnitt === idSchuljahresabschnitt)) {
			return null;
		}
		return await super.setSchuljahresabschnitt(idSchuljahresabschnitt, isEntering || forceReload);
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateUnterrichtsverteilung>> {
		const planungsabschnitte = await uvStateImpl.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		if (planungsabschnitte === null) {
			throw new DeveloperNotificationException('Die UV-Planungsabschnitte konnten nicht geladen werden.');
		}
		return {
			manager: new UvPlanungsabschnitteListeManager(idSchuljahresabschnitt, schuleState.stammdaten.idSchuljahresabschnitt,
				schuleState.stammdaten.abschnitte, schuleState.schulform, planungsabschnitte, true),
		};
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public async ladeDaten(auswahl: UvPlanungsabschnitt | null): Promise<UvPlanungsabschnitt | null> {
		return await uvStateImpl.ladeDaten(auswahl, this.manager.liste.list());
	}

	protected deleteMessage(id: number, abschnitt: UvPlanungsabschnitt): string {
		return `Planungsabschnitt ${abschnitt.beschreibung} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await uvStateImpl.deletePlanungsabschnitte(ids);
	}

	public async doPatch(data: Partial<UvPlanungsabschnitt>, id: number): Promise<boolean> {
		return await uvStateImpl.patch(id, data);
	}

	public addAsCopy = async (partial: Partial<UvPlanungsabschnitt>, idFromUvPlanungsabschnitt?: number): Promise<UvPlanungsabschnitt> => {
		const neu = await uvStateImpl.addAsCopy(partial, idFromUvPlanungsabschnitt);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		return neu;
	};

}

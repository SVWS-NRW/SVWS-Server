import type { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvLerngruppen extends RouteStateInterface {
	auswahl: UvLerngruppe | undefined;
}

const defaultState = <RouteStateUvLerngruppen> {
	auswahl: undefined,
};

export class RouteDataUvLerngruppen extends RouteData<RouteStateUvLerngruppen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvLerngruppe | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.lerngruppeGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (lerngruppe: UvLerngruppe | undefined): void => {
		this.setPatchedState({ auswahl: lerngruppe });
	};

	public gotoLerngruppe = async (lerngruppe: UvLerngruppe | undefined): Promise<void> => {
		await this.gotoUv('uv.lerngruppen', 'idLerngruppe', lerngruppe?.id);
	};

	public gotoKurs = async (id: number): Promise<void> => await this.gotoUv('uv.kurse', 'idKurs', id);
	public gotoKlasse = async (id: number): Promise<void> => await this.gotoUv('uv.klassen', 'idKlasse', id);
	public gotoLehrer = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.lehrer', 'idLehrer', id, -1);
	public gotoSchiene = async (id: number): Promise<void> => await this.gotoUv('uv.schienen', 'idSchiene', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined, idPlanungsabschnitt = uvStateImpl.planungsabschnitt?.id ?? -1): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: idPlanungsabschnitt, [parameter]: id ?? '' }));
	}

}

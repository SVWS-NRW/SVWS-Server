import type { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenRaumgruppen extends RouteStateInterface {
	auswahl: UvRaumgruppe | undefined;
}

const defaultState = <RouteStateUvGrunddatenRaumgruppen> {
	auswahl: undefined,
};

export class RouteDataUvGrunddatenRaumgruppen extends RouteData<RouteStateUvGrunddatenRaumgruppen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvRaumgruppe | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.raumgruppeGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (raumgruppe: UvRaumgruppe | undefined): void => {
		this.setPatchedState({ auswahl: raumgruppe });
	};

	public gotoRaumgruppe = async (raumgruppe: UvRaumgruppe | undefined): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.raumgruppen');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.raumgruppen ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idRaumgruppe: raumgruppe?.id ?? '' }));
	};

}

import type { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenRaumgruppen extends RouteStateInterface {
	idAuswahl: number | undefined;
}

const defaultState = <RouteStateUvGrunddatenRaumgruppen> {
	idAuswahl: undefined,
};

export class RouteDataUvGrunddatenRaumgruppen extends RouteData<RouteStateUvGrunddatenRaumgruppen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvRaumgruppe | undefined {
		const idAuswahl = this._state.value.idAuswahl;
		if (idAuswahl === undefined) {
			return undefined;
		}
		return uvStateImpl.uvManager.raumgruppeGetByIdOrNull(idAuswahl) ?? undefined;
	}

	public setAuswahlFromRoute = (idAuswahl: number | undefined): void => {
		this.setPatchedState({ idAuswahl });
	};

	public gotoRaumgruppe = async (raumgruppe: UvRaumgruppe | undefined): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.raumgruppen');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.raumgruppen ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idRaumgruppe: raumgruppe?.id ?? '' }));
	};

}

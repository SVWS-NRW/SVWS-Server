import type { UvRaum } from "@core/core/data/uv/UvRaum";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenRaeume extends RouteStateInterface {
	idAuswahl: number | undefined;
}

const defaultState = <RouteStateUvGrunddatenRaeume> {
	idAuswahl: undefined,
};

export class RouteDataUvGrunddatenRaeume extends RouteData<RouteStateUvGrunddatenRaeume> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvRaum | undefined {
		const idAuswahl = this._state.value.idAuswahl;
		if (idAuswahl === undefined) {
			return undefined;
		}
		return uvStateImpl.uvManager.raumGetByIdOrNull(idAuswahl) ?? undefined;
	}

	public setAuswahlFromRoute = (idAuswahl: number | undefined): void => {
		this.setPatchedState({ idAuswahl });
	};

	public gotoRaum = async (raum: UvRaum | undefined): Promise<void> => {
		await this.gotoUv('uv.grunddaten.raeume', 'idRaum', raum?.id);
	};

	public gotoRaumgruppe = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.raumgruppen', 'idRaumgruppe', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, [parameter]: id ?? '' }));
	}

}

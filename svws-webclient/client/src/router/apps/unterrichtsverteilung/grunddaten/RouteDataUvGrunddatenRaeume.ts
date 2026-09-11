import type { UvRaum } from "@core/core/data/uv/UvRaum";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenRaeume extends RouteStateInterface {
	auswahl: UvRaum | undefined;
}

const defaultState = <RouteStateUvGrunddatenRaeume> {
	auswahl: undefined,
};

export class RouteDataUvGrunddatenRaeume extends RouteData<RouteStateUvGrunddatenRaeume> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvRaum | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.raumGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (raum: UvRaum | undefined): void => {
		this.setPatchedState({ auswahl: raum });
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

import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenStundentafeln extends RouteStateInterface {
	auswahl: UvStundentafel | undefined;
}

const defaultState = <RouteStateUvGrunddatenStundentafeln> {
	auswahl: undefined,
};

export class RouteDataUvGrunddatenStundentafeln extends RouteData<RouteStateUvGrunddatenStundentafeln> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvStundentafel | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.stundentafelGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (stundentafel: UvStundentafel | undefined): void => {
		this.setPatchedState({ auswahl: stundentafel });
	};

	public gotoStundentafel = async (stundentafel: UvStundentafel | undefined): Promise<void> => {
		await this.gotoUv('uv.grunddaten.stundentafeln', 'idStundentafel', stundentafel?.id);
	};

	public gotoFach = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.faecher', 'idFach', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, [parameter]: id ?? '' }));
	}

}

import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenStundentafeln extends RouteStateInterface {
	idAuswahl: number | undefined;
}

const defaultState = <RouteStateUvGrunddatenStundentafeln> {
	idAuswahl: undefined,
};

export class RouteDataUvGrunddatenStundentafeln extends RouteData<RouteStateUvGrunddatenStundentafeln> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvStundentafel | undefined {
		const idAuswahl = this._state.value.idAuswahl;
		if (idAuswahl === undefined) {
			return undefined;
		}
		return uvStateImpl.uvManager.stundentafelGetByIdOrNull(idAuswahl) ?? undefined;
	}

	public setAuswahlFromRoute = (idAuswahl: number | undefined): void => {
		this.setPatchedState({ idAuswahl });
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

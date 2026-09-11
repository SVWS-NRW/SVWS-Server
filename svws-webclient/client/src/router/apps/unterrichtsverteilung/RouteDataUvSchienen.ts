import type { UvSchiene } from "@core/core/data/uv/UvSchiene";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvSchienen extends RouteStateInterface {
	auswahl: UvSchiene | undefined;
}

const defaultState = <RouteStateUvSchienen> {
	auswahl: undefined,
};

export class RouteDataUvSchienen extends RouteData<RouteStateUvSchienen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvSchiene | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.schieneGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (schiene: UvSchiene | undefined): void => {
		this.setPatchedState({ auswahl: schiene });
	};

	public gotoSchiene = async (schiene: UvSchiene | undefined): Promise<void> => {
		await this.gotoUv('uv.schienen', 'idSchiene', schiene?.id);
	};

	public gotoLerngruppe = async (id: number): Promise<void> => await this.gotoUv('uv.lerngruppen', 'idLerngruppe', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: uvStateImpl.planungsabschnitt?.id ?? -1, [parameter]: id ?? '' }));
	}

}

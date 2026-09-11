import type { UvUnterricht } from "@core/core/data/uv/UvUnterricht";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvUnterrichte extends RouteStateInterface {
	auswahl: UvUnterricht | undefined;
}

const defaultState = <RouteStateUvUnterrichte> {
	auswahl: undefined,
};

export class RouteDataUvUnterrichte extends RouteData<RouteStateUvUnterrichte> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvUnterricht | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.unterrichtGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (unterricht: UvUnterricht | undefined): void => {
		this.setPatchedState({ auswahl: unterricht });
	};

	public gotoUnterricht = async (unterricht: UvUnterricht | undefined): Promise<void> => {
		await this.gotoUv('uv.unterrichte', 'idUnterricht', unterricht?.id);
	};

	public gotoLerngruppe = async (id: number): Promise<void> => await this.gotoUv('uv.lerngruppen', 'idLerngruppe', id);
	public gotoLehrer = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.lehrer', 'idLehrer', id, -1);
	public gotoRaum = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.raeume', 'idRaum', id, -1);

	private async gotoUv(name: string, parameter: string, id: number | undefined, idPlanungsabschnitt = uvStateImpl.planungsabschnitt?.id ?? -1): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: idPlanungsabschnitt, [parameter]: id ?? '' }));
	}

}

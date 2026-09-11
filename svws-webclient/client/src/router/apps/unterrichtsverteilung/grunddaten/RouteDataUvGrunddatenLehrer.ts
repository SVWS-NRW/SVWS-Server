import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenLehrer extends RouteStateInterface {
	auswahl: UvLehrer | undefined;
}

const defaultState = <RouteStateUvGrunddatenLehrer> {
	auswahl: undefined,
};

export class RouteDataUvGrunddatenLehrer extends RouteData<RouteStateUvGrunddatenLehrer> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvLehrer | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.lehrerGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (lehrer: UvLehrer | undefined): void => {
		this.setPatchedState({ auswahl: lehrer });
	};

	public gotoUvLehrer = async (lehrer: UvLehrer | undefined): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.lehrer');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.lehrer ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idLehrer: lehrer?.id ?? '' }));
	};

	public gotoLehrer = async (id: number) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id }));
	};

}

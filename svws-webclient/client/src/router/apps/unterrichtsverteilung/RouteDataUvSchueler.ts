import type { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvSchueler extends RouteStateInterface {
	auswahl: UvPlanungsabschnittSchueler | undefined;
}

const defaultState = <RouteStateUvSchueler> {
	auswahl: undefined,
};

export class RouteDataUvSchueler extends RouteData<RouteStateUvSchueler> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvPlanungsabschnittSchueler | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.planungsabschnittSchuelerGetByIdOrNull(auswahl.idPlanungsabschnitt, auswahl.idSchueler) ?? undefined;
	}

	public setAuswahlFromRoute = (schueler: UvPlanungsabschnittSchueler | undefined): void => {
		this.setPatchedState({ auswahl: schueler });
	};

	public gotoUvSchueler = async (schueler: UvPlanungsabschnittSchueler | undefined): Promise<void> => {
		await this.gotoUv('uv.schueler', 'idSchueler', schueler?.idSchueler);
	};

	public gotoKlasse = async (idKlasse: number): Promise<void> => {
		await this.gotoUv('uv.klassen', 'idKlasse', idKlasse);
	};

	public gotoSchueler = async (id: number) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id }));
	};

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: uvStateImpl.planungsabschnitt?.id ?? -1, [parameter]: id ?? '' }));
	}

}

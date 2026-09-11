import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvSchuelergruppen extends RouteStateInterface {
	auswahl: UvSchuelergruppe | undefined;
}

const defaultState = <RouteStateUvSchuelergruppen> {
	auswahl: undefined,
};

export class RouteDataUvSchuelergruppen extends RouteData<RouteStateUvSchuelergruppen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvSchuelergruppe | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.schuelergruppeGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (schuelergruppe: UvSchuelergruppe | undefined): void => {
		this.setPatchedState({ auswahl: schuelergruppe });
	};

	public gotoSchuelergruppe = async (schuelergruppe: UvSchuelergruppe | undefined): Promise<void> => {
		await this.gotoUv('uv.schuelergruppen', 'idSchuelergruppe', schuelergruppe?.id);
	};

	public gotoSchueler = async (id: number): Promise<void> => await this.gotoUv('uv.schueler', 'idSchueler', id);
	public gotoKlasse = async (id: number): Promise<void> => await this.gotoUv('uv.klassen', 'idKlasse', id);
	public gotoLerngruppe = async (id: number): Promise<void> => await this.gotoUv('uv.lerngruppen', 'idLerngruppe', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: uvStateImpl.planungsabschnitt?.id ?? -1, [parameter]: id ?? '' }));
	}

}

import type { UvKlasse } from "@core/core/data/uv/UvKlasse";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvKlassen extends RouteStateInterface {
	auswahl: UvKlasse | undefined;
}

const defaultState = <RouteStateUvKlassen> {
	auswahl: undefined,
};

export class RouteDataUvKlassen extends RouteData<RouteStateUvKlassen> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvKlasse | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.klasseGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (klasse: UvKlasse | undefined): void => {
		this.setPatchedState({ auswahl: klasse });
	};

	public gotoKlasse = async (klasse: UvKlasse | number | undefined): Promise<void> => {
		const id = typeof klasse === 'number' ? klasse : klasse?.id;
		await this.gotoUv('uv.klassen', 'idKlasse', id);
	};

	public gotoSchuelergruppe = async (id: number): Promise<void> => this.gotoUv('uv.schuelergruppen', 'idSchuelergruppe', id);
	public gotoSchueler = async (id: number): Promise<void> => this.gotoUv('uv.schueler', 'idSchueler', id);
	public gotoLehrer = async (id: number): Promise<void> => this.gotoUv('uv.grunddaten.lehrer', 'idLehrer', id, -1);
	public gotoStundentafel = async (id: number): Promise<void> => this.gotoUv('uv.grunddaten.stundentafeln', 'idStundentafel', id, -1);

	private async gotoUv(name: string, parameter: string, id: number | undefined, idPlanungsabschnitt = uvStateImpl.planungsabschnitt?.id ?? -1): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: idPlanungsabschnitt, [parameter]: id ?? '' }));
	}

}

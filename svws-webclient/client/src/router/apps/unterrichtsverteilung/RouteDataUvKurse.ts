import type { UvKurs } from "@core/core/data/uv/UvKurs";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvKurse extends RouteStateInterface {
	auswahl: UvKurs | undefined;
}

const defaultState = <RouteStateUvKurse> {
	auswahl: undefined,
};

export class RouteDataUvKurse extends RouteData<RouteStateUvKurse> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvKurs | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.kursGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (kurs: UvKurs | undefined): void => {
		this.setPatchedState({ auswahl: kurs });
	};

	public gotoKurs = async (kurs: UvKurs | undefined): Promise<void> => {
		await this.goto('uv.kurse', 'idKurs', kurs?.id);
	};

	public gotoSchuelergruppe = async (idSchuelergruppe: number): Promise<void> => {
		await this.goto('uv.schuelergruppen', 'idSchuelergruppe', idSchuelergruppe);
	};

	public gotoSchueler = async (idSchueler: number): Promise<void> => {
		await this.goto('uv.schueler', 'idSchueler', idSchueler);
	};

	public gotoKlasse = async (idKlasse: number): Promise<void> => {
		await this.goto('uv.klassen', 'idKlasse', idKlasse);
	};

	private async goto(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: uvStateImpl.planungsabschnitt?.id ?? -1, [parameter]: id ?? '' }));
	}

}

import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
import { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import type { Wochentag } from "@core/core/types/Wochentag";

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

type UvZeitrasterSelection = Wochentag | number | UvZeitrasterEintrag | undefined;

interface RouteStateUvGrunddatenZeitraster extends RouteStateInterface {
	auswahl: UvZeitraster | undefined;
	selected: UvZeitrasterSelection;
}

const defaultState = <RouteStateUvGrunddatenZeitraster> {
	auswahl: undefined,
	selected: undefined,
};

export class RouteDataUvGrunddatenZeitraster extends RouteData<RouteStateUvGrunddatenZeitraster> {

	public constructor() {
		super(defaultState);
	}

	public async ladeZeitraster(): Promise<void> {
		// Daten sind bereits im UvManager geladen, nichts zu tun
	}

	public get auswahl(): UvZeitraster | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.zeitrasterGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public get selected(): UvZeitrasterSelection {
		const selected = this._state.value.selected;
		if (selected instanceof UvZeitrasterEintrag) {
			return uvStateImpl.uvManager.zeitrasterEintragGetByIdOrNull(selected.id) ?? undefined;
		}
		return selected;
	}

	public setSelection = (value: UvZeitrasterSelection): void => {
		this.setPatchedState({ selected: value });
	};

	public setAuswahlFromRoute = (zeitraster: UvZeitraster | undefined): void => {
		this.setPatchedState({ auswahl: zeitraster, selected: undefined });
	};

	public gotoZeitraster = async (zeitraster: UvZeitraster | undefined): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.zeitraster');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.zeitraster ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idZeitraster: zeitraster?.id ?? '' }));
	};

}

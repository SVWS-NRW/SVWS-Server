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
	idAuswahl: number | undefined;
	selected: UvZeitrasterSelection;
}

const defaultState = <RouteStateUvGrunddatenZeitraster> {
	idAuswahl: undefined,
	selected: undefined,
};

export class RouteDataUvGrunddatenZeitraster extends RouteData<RouteStateUvGrunddatenZeitraster> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvZeitraster | undefined {
		const idAuswahl = this._state.value.idAuswahl;
		if (idAuswahl === undefined) {
			return undefined;
		}
		return uvStateImpl.uvManager.zeitrasterGetByIdOrNull(idAuswahl) ?? undefined;
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

	public setAuswahlFromRoute = (idAuswahl: number | undefined): void => {
		this.setPatchedState({ idAuswahl, selected: undefined });
	};

	public gotoZeitraster = async (zeitraster: UvZeitraster | undefined): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.zeitraster');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.zeitraster ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idZeitraster: zeitraster?.id ?? '' }));
	};

}

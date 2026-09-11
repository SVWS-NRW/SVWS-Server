import type { WritableComputedRef } from "vue";
import { computed } from "vue";

import type { UvFach } from "@core/core/data/uv/UvFach";
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';

import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from '~/router/RouteManager';
import { RouteNode } from '~/router/RouteNode';
import { uvStateImpl } from "~/states/UvStateImpl";

interface RouteStateUvGrunddatenFaecher extends RouteStateInterface {
	auswahl: UvFach | undefined;
	konfliktMitFach: UvFach | null;
}

const defaultState = <RouteStateUvGrunddatenFaecher> {
	auswahl: undefined,
	konfliktMitFach: null,
};

export class RouteDataUvGrunddatenFaecher extends RouteData<RouteStateUvGrunddatenFaecher> {

	public constructor() {
		super(defaultState);
	}

	public get auswahl(): UvFach | undefined {
		const auswahl = this._state.value.auswahl;
		if (auswahl === undefined) {
			return undefined;
		}
		// Nach DTO-Patches immer die aktuelle Managerinstanz verwenden.
		return uvStateImpl.uvManager.fachGetByIdOrNull(auswahl.id) ?? undefined;
	}

	public setAuswahlFromRoute = (fach: UvFach | undefined): void => {
		if (this._state.value.auswahl?.id !== fach?.id) {
			this.setPatchedState({ auswahl: fach, konfliktMitFach: null });
		}
	};

	public gotoFach = async (fach: UvFach | undefined): Promise<void> => {
		await this.gotoUv('uv.grunddaten.faecher', 'idFach', fach?.id);
	};

	public gotoLehrer = async (id: number): Promise<void> => await this.gotoUv('uv.grunddaten.lehrer', 'idLehrer', id);

	private async gotoUv(name: string, parameter: string, id: number | undefined): Promise<void> {
		const node = RouteNode.getNodeByName(name);
		if (node === undefined) {
			throw new DeveloperNotificationException(`UV-Zielroute ${name} ist nicht registriert.`);
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, [parameter]: id ?? '' }));
	}

	public konfliktMitFachRef: WritableComputedRef<UvFach | null> = computed({
		get: () => this._state.value.konfliktMitFach,
		set: (v) => this.setPatchedState({ konfliktMitFach: v }),
	});

}

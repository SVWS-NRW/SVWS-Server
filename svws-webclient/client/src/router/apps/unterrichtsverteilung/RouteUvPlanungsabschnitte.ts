import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import SUvPlanungsabschnitte from "~/components/unterrichtsverteilung/SUvPlanungsabschnitte.vue";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

export class RouteUvPlanungsabschnitte extends RouteNode<any, RouteUv> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN,
		], "uv.planungsabschnitt", "planungsabschnitt", SUvPlanungsabschnitte);
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Planungsabschnitt";
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(false, true, params);
	}

	public getProps() {
		const parent = this.parent;
		if (parent === undefined) {
			throw new DeveloperNotificationException('Die UV-Planungsabschnittsroute hat keine Elternroute.');
		}
		return {
			benutzerKompetenzen: benutzerStateImpl.kompetenzen,
			manager: () => parent.data.manager,
			patch: parent.data.patch,
			gotoZeitraster: this.gotoZeitraster,
		};
	}

	private gotoZeitraster = async (idZeitraster: number): Promise<void> => {
		const node = RouteNode.getNodeByName('uv.grunddaten.zeitraster');
		if (node === undefined) {
			throw new DeveloperNotificationException('UV-Zielroute uv.grunddaten.zeitraster ist nicht registriert.');
		}
		await RouteManager.doRoute(node.getRoute({ id: -1, idZeitraster }));
	};
}

export const routeUvPlanungsabschnitt = new RouteUvPlanungsabschnitte();

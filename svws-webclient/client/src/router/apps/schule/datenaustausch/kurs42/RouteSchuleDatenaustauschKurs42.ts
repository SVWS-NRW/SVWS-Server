import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteApp} from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { SchuleDatenaustauschKurs42Props } from "~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42Props";
import type { TabData } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteDataSchuleDatenaustauschKurs42 } from "./RouteDataSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschKurs42Blockung } from "./RouteSchuleDatenaustauschKurs42Blockung";
import { routeSchuleDatenaustauschKurs42Raeume } from "./RouteSchuleDatenaustauschKurs42Raeume";
import { routeSchule } from "../../RouteSchule";

const SSchuleDatenaustauschKurs42 = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42.vue");
const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")

export class RouteSchuleDatenaustauschKurs42 extends RouteNode<RouteDataSchuleDatenaustauschKurs42, RouteApp> {

	public constructor() {
		super(schulformenGymOb, [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.kurs42", "kurs42", SSchuleDatenaustauschKurs42, new RouteDataSchuleDatenaustauschKurs42());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs42";
		super.setView("liste", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.children = [
			routeSchuleDatenaustauschKurs42Blockung,
			routeSchuleDatenaustauschKurs42Raeume
		];
		super.defaultChild = routeSchuleDatenaustauschKurs42Blockung;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to.name === this.name)
			return this.defaultChild!.getRoute();
		if (!to.name.startsWith(this.data.view.name)) {
			for (const child of this.children) {
				if (to.name.startsWith(child.name)) {
					this.data.setView(child, this.children);
					return child.getRoute();
				}
			}
		}
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschKurs42Props {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.children);
	}

}

export const routeSchuleDatenaustauschKurs42 = new RouteSchuleDatenaustauschKurs42();


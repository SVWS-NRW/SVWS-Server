import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import type { SchuleDatenaustauschUntisProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisProps";
import type { TabData } from "@ui";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp} from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { RouteDataSchuleDatenaustauschUntis } from "./RouteDataSchuleDatenaustauschUntis";
import { routeSchuleDatenaustauschUntisStundenplan } from "./RouteSchuleDatenaustauschUntisStundenplan";
import { routeSchuleDatenaustauschUntisRaeume } from "./RouteSchuleDatenaustauschUntisRaeume";
import { routeSchuleDatenaustauschUntisBlockungen } from "./RouteSchuleDatenaustauschUntisBlockungen";
import { routeSchule } from "../../RouteSchule";

const SSchuleDatenaustauschUntis = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntis.vue");
const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")

export class RouteSchuleDatenaustauschUntis extends RouteNode<RouteDataSchuleDatenaustauschUntis, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis", "untis", SSchuleDatenaustauschUntis, new RouteDataSchuleDatenaustauschUntis());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Untis";
		super.setView("liste", SSchuleAuswahl, (route) => routeSchule.getAuswahlProps(route));
		super.children = [
			routeSchuleDatenaustauschUntisStundenplan,
			routeSchuleDatenaustauschUntisRaeume,
			routeSchuleDatenaustauschUntisBlockungen,
		];
		super.defaultChild = routeSchuleDatenaustauschUntisStundenplan;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to.name === this.name)
			return this.defaultChild!.getRoute();
		if (!to.name.startsWith(this.data.view.name)) {
			for (const child of this.children) {
				if (to.name.startsWith(child.name)) {
					this.data.setView(child, this.children);
					return child.getRoute(to_params);
				}
			}
		}
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisProps {
		return {
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): TabData[] {
		const result: TabData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
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

export const routeSchuleDatenaustauschUntis = new RouteSchuleDatenaustauschUntis();


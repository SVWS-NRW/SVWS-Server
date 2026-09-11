import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { TabData } from "@ui/ui/nav/TabData";
import { ViewType } from "@ui/ui/nav/ViewType";

import SUvGrunddatenRaeumeGruppen from "~/components/unterrichtsverteilung/grunddaten/raeume/SUvGrunddatenRaeumeGruppen.vue";
import { RouteDataUvGrunddatenRaeumeGruppen } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteDataUvGrunddatenRaeumeGruppen";
import { routeUvGrunddatenRaeume } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaeume";
import { routeUvGrunddatenRaumgruppen } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaumgruppen";
import { RouteUv } from "~/router/apps/unterrichtsverteilung/RouteUv";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

export class RouteUvGrunddatenRaeumeGruppen extends RouteNode<any, RouteUv> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "uv.grunddaten.raeumegruppen", "raeumegruppen", SUvGrunddatenRaeumeGruppen, new RouteDataUvGrunddatenRaeumeGruppen());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Räume";
		super.children = [
			routeUvGrunddatenRaeume,
			routeUvGrunddatenRaumgruppen,
		];
		super.defaultChild = routeUvGrunddatenRaeume;
		this.isHidden = (params?: RouteParams) => RouteUv.tabsCheckHidden(true, false, params);
		// super.setView("eintraege", SUvGrunddatenRaeumeAuswahl, () => this.getAuswahlProps());
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (to.name === this.name) {
			return routeUvGrunddatenRaeume.getRoute();
		}
		if (!to.name.startsWith(this.data.view.name)) {
			this.data.setView(to, this.children);
		}
	}

	public getProps() {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.getType()) };
	}

	private getType(): ViewType {
		// if (this.data.gruppenprozesseEnabled)
		// 	return ViewType.GRUPPENPROZESSE;
		// if (this.data.creationModeEnabled)
		// 	return ViewType.HINZUFUEGEN;
		return ViewType.DEFAULT;
	}

	private readonly setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) {
			return;
		}
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, this.children);
	};

}

export const routeUvGrunddatenRaeumeGruppen = new RouteUvGrunddatenRaeumeGruppen();

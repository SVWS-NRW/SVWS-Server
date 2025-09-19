import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";

import type { SchuelerSonstigesProps } from "~/components/schueler/sonstiges/SchuelerSonstigesProps";
import type { TabData } from "@ui";
import { routeSchuelerEinwilligungen } from "../einwilligungen/RouteSchuelerEinwilligungen";
import { routeSchuelerVermerke } from "../vermerke/RouteSchuelerVermerke";
import { routeSchuelerLernplattformen } from "../lernplattformen/RouteSchuelerLernplattformen";
import { RouteDataSchuelerLernabschnitte } from "./RouteDataSchuelerSonstiges";

const SchuelerSonstiges = () => import("~/components/schueler/sonstiges/SchuelerSonstiges.vue");


export class RouteSchuelerSonstiges extends RouteNode<RouteDataSchuelerLernabschnitte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN ], "schueler.sonstiges", "sonstiges", SchuelerSonstiges, new RouteDataSchuelerLernabschnitte());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Sonstiges";
		super.children = [
			routeSchuelerVermerke,
			routeSchuelerEinwilligungen,
			routeSchuelerLernplattformen,
		];
		super.defaultChild = routeSchuelerVermerke;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined)
				throw new DeveloperNotificationException("Fehler: Keine Schüler-ID in der URL angegeben.");
			if (to === this)
				return this.getRouteView(this.data.view);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerSonstigesProps {
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
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
	}

}

export const routeSchuelerSonstiges = new RouteSchuelerSonstiges();


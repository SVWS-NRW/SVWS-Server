import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeError } from "~/router/error/RouteError";

import { routeLadeDaten } from "./RouteLadeDaten";

import LaufbahnplanungOberstufe from "~/components/LaufbahnplanungOberstufe.vue";
import type { LaufbahnplanungOberstufeProps } from "~/components/LaufbahnplanungOberstufeProps";
import { GostLaufbahnplanungDaten } from "@core/core/data/gost/GostLaufbahnplanungDaten";


export class RouteLaufbahnplanung extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("laufbahnplanung", "laufbahnplanung", LaufbahnplanungOberstufe, null);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (this.parent === undefined)
			return routeError.getRoute(new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		if (!routeApp.data.hatAuswahl)
			return routeLadeDaten.getRoute();
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		routeApp.data.reset();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): LaufbahnplanungOberstufeProps {
		return {
			serverMode: routeApp.data.serverMode,
			config: () => routeApp.data.config,
			setWahl: routeApp.data.setWahl,
			setGostBelegpruefungsArt: routeApp.data.setGostBelegpruefungsArt,
			exportLaufbahnplanung: routeApp.data.exportLaufbahnplanung,
			importLaufbahnplanung: routeApp.data.importLaufbahnplanung,
			schueler: routeApp.data.auswahl,
			gostJahrgangsdaten: routeApp.data.gostJahrgangsdaten,
			gostBelegpruefungsArt: () => routeApp.data.gostBelegpruefungsArt,
			gostBelegpruefungErgebnis: () => routeApp.data.gostBelegpruefungErgebnis,
			abiturdatenManager: () => routeApp.data.abiturdatenManager,
			id: undefined,
			zwischenspeicher: routeApp.data.zwischenspeicher === undefined ? undefined : new GostLaufbahnplanungDaten(),
			saveLaufbahnplanung: routeApp.data.saveLaufbahnplanung,
			restoreLaufbahnplanung: routeApp.data.restoreLaufbahnplanung,
			resetFachwahlen: routeApp.data.resetFachwahlen,
			exitLaufbahnplanung: routeApp.data.exitLaufbahnplanung,
			dirty: () => routeApp.data.dirty,
		};
	}

}

export const routeLaufbahnplanung = new RouteLaufbahnplanung();


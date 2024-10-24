import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleFaecher, type RouteSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";

import type { FachDatenProps } from "~/components/schule/faecher/daten/SFachDatenProps";
import { routeApp } from "../../RouteApp";

const SFachDaten = () => import("~/components/schule/faecher/daten/SFachDaten.vue");

export class RouteSchuleFachDaten extends RouteNode<any, RouteSchuleFaecher> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.faecher.daten", "daten", SFachDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchuleFaecher.data.fachListeManager.auswahlID() === null)
			return routeSchuleFaecher.getRoute()
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): FachDatenProps {
		return {
			patch: routeSchuleFaecher.data.patch,
			fachListeManager: () => routeSchuleFaecher.data.fachListeManager,
		};
	}

}

export const routeSchuleFachDaten = new RouteSchuleFachDaten();


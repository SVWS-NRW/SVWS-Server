import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { LehrerIndividualdatenProps } from "~/components/lehrer/individualdaten/LehrerIndividualdatenProps";
import { type RouteLehrer, routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { wiedervorlageStateImpl } from "~/states/wiedervorlage/WiedervorlageStateImpl";

const LehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/LehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer.daten", "daten", LehrerIndividualdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		// initialize states, load data etc
	}

	public getProps(to: RouteLocationNormalized): LehrerIndividualdatenProps {
		return {
			patch: routeLehrer.data.patch,
			lehrerListeManager: () => routeLehrer.data.manager,
			getListLeitungsfunktionen: () => routeLehrer.data.getListLeitungsfunktionen,
			addLeitungsfunktion: routeLehrer.data.addLeitungsfunktion,
			patchLeitungsfunktion: routeLehrer.data.patchLeitungsfunktion,
			deleteLeitungsfunktionen: routeLehrer.data.deleteLeitungsfunktionen,
			mapLeitungsfunktionen: routeApp.cache.kataloge.leitungsfunktionenById,
			zeigeAlles: true,
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();


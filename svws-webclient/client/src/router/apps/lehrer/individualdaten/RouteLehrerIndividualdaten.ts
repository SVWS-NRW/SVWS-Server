import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";

import type { LehrerIndividualdatenProps } from "~/components/lehrer/individualdaten/LehrerIndividualdatenProps";
import { api } from "~/router/Api";
import { wiedervorlageStateImpl } from "~/states/WiedervorlageStateImpl";

const LehrerIndividualdaten = () => import("~/components/lehrer/individualdaten/LehrerIndividualdaten.vue");

export class RouteLehrerIndividualdaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer.daten", "daten", LehrerIndividualdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		// initialize used states
		await wiedervorlageStateImpl.init();
	}

	public getProps(to: RouteLocationNormalized): LehrerIndividualdatenProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			patch: routeLehrer.data.patch,
			lehrerListeManager: () => routeLehrer.data.manager,
			getListLeitungsfunktionen: () => routeLehrer.data.getListLeitungsfunktionen,
			addLeitungsfunktion: routeLehrer.data.addLeitungsfunktion,
			patchLeitungsfunktion: routeLehrer.data.patchLeitungsfunktion,
			deleteLeitungsfunktionen: routeLehrer.data.deleteLeitungsfunktionen,
			orteById: routeApp.cache.kataloge.orteById,
			ortsteileById: routeApp.cache.kataloge.ortsteileById,
			mapLeitungsfunktionen: routeApp.cache.kataloge.leitungsfunktionenById,
		};
	}

}

export const routeLehrerIndividualdaten = new RouteLehrerIndividualdaten();


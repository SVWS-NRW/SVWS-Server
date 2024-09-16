import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode, Einwilligungsart } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeKatalogEinwilligungsarten, type RouteKatalogEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteKatalogEinwilligungsarten";

import type { EinwilligungsartDatenProps } from "~/components/schule/kataloge/einwilligungsarten/daten/SEinwilligungsartDatenProps";
import { routeApp } from "../../../RouteApp";

const SEinwilligungsartDaten = () => import("~/components/schule/kataloge/einwilligungsarten/daten/SEinwilligungsartDaten.vue");

export class RouteKatalogEinwilligungsartenDaten extends RouteNode<any, RouteKatalogEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schule.kataloge.einwilligungsarten.daten", "daten", SEinwilligungsartDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungsart";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (routeKatalogEinwilligungsarten.data.auswahl === undefined)
			return routeKatalogEinwilligungsarten.getRoute(undefined)
	}

	public getRoute(id: number): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id } };
	}

	public getProps(to: RouteLocationNormalized): EinwilligungsartDatenProps {
		return {
			patch: routeKatalogEinwilligungsarten.data.patch,
			auswahl: () => routeKatalogEinwilligungsarten.data.auswahl ?? new Einwilligungsart(),
		};
	}

}

export const routeKatalogEinwilligungsartenDaten = new RouteKatalogEinwilligungsartenDaten();

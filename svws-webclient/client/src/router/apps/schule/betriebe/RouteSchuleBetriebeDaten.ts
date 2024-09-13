import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";

import type { BetriebeDatenProps } from "~/components/schule/betriebe/daten/SBetriebeDatenProps";
import type { RouteSchuleBetriebe} from "./RouteSchuleBetriebe";
import { routeSchuleBetriebe } from "./RouteSchuleBetriebe";

const SBetriebeDaten = () => import("~/components/schule/betriebe/daten/SBetriebeDaten.vue")

export class RouteSchuleBetriebeDaten extends RouteNode<any, RouteSchuleBetriebe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.betriebe.daten", "daten", SBetriebeDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betrieb";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeSchuleBetriebe.data.auswahl === undefined)
			return routeSchuleBetriebe.getRoute(undefined)
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): BetriebeDatenProps {
		return {
			patch: routeSchuleBetriebe.data.patch,
			patchBetriebAnpsrechpartner: routeSchuleBetriebe.data.patchBetriebAnsprechpartner,
			addBetriebAnsprechpartner: routeSchuleBetriebe.data.addBetriebAnsprechpartner,
			removeBetriebAnsprechpartner: routeSchuleBetriebe.data.removeBetriebAnsprechpartner,
			daten: routeSchuleBetriebe.data.daten,
			mapBeschaeftigungsarten: routeSchuleBetriebe.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapAnsprechpartner: routeSchuleBetriebe.data.mapAnsprechpartner,

		};
	}

}

export const routeSchuleBetriebeDaten = new RouteSchuleBetriebeDaten();


import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteDataKlassen } from "~/router/apps/klassen/RouteDataKlassen";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { routeKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";

import type { KlassenAuswahlProps } from "~/components/klassen/SKlassenAuswahlProps";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import { routeKlassenNeu } from "./RouteKlassenNeu";
import { AppMenuGroup, KlassenListeManager } from "@ui";


const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteAuswahlNode<KlassenListeManager, RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN ], "klassen", "klassen/:id(-?\\d+)?", SKlassenApp, SKlassenAuswahl, new RouteDataKlassen());
		super.mode = ServerMode.STABLE;
		super.text = "Klassen";
		super.children = [
			routeKlassenDaten,
			routeKlassenNeu,
			routeKlassenStundenplan,
			routeKlasseGruppenprozesse,
		];
		super.defaultChild = routeKlassenDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-team-line";
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<KlassenAuswahlProps>{
			...props,
			setzeDefaultSortierung: this.data.setzeDefaultSortierung,
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
			return this.getRouteView(routeKlassenStundenplan);
		return this.getRouteSelectedChild();
	};

}

export const routeKlassen = new RouteKlassen();

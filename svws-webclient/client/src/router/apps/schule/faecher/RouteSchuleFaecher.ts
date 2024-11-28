import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, type FachListeManager, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeSchuleFachDaten } from "~/router/apps/schule/faecher/RouteSchuleFachDaten";

import type { FaecherAuswahlProps } from "~/components/schule/faecher/SFaecherAuswahlProps";
import { RouteDataSchuleFaecher } from "./RouteDataSchuleFaecher";
import { routeFachStundenplan } from "./stundenplan/RouteFachStundenplan";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { routeSchuleFachGruppenprozesse } from "./RouteSchuleFachGruppenprozesse";
import { routeSchuleFachNeu } from "./RouteSchuleFachNeu";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";

const SFaecherAuswahl = () => import("~/components/schule/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/schule/faecher/SFaecherApp.vue")

export class RouteSchuleFaecher extends RouteAuswahlNode<FachListeManager, RouteDataSchuleFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN ], "schule.faecher", "schule/faecher/:id(\\d+)?", SFaecherApp, SFaecherAuswahl, new RouteDataSchuleFaecher());
		super.mode = ServerMode.STABLE;
		super.text = "Fächer";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeSchuleFachDaten,
			routeFachStundenplan,
			routeSchuleFachGruppenprozesse,
			routeSchuleFachNeu,
		];
		super.defaultChild = routeSchuleFachDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<FaecherAuswahlProps>{
			...props,
			setzeDefaultSortierungSekII: this.data.setzeDefaultSortierungSekII,
		})
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeSchuleFaecher = new RouteSchuleFaecher();

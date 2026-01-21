import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { FaecherListeManager } from "@ui";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeFaecherDaten } from "~/router/apps/schule/kataloge/faecher/RouteFaecherDaten";

import type { FaecherAuswahlProps } from "~/components/schule/kataloge/faecher/FaecherAuswahlProps";
import { RouteDataFaecher } from "./RouteDataFaecher";
import { routeFaecherStundenplan } from "./stundenplan/RouteFaecherStundenplan";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeFaecherGruppenprozesse } from "./RouteFaecherGruppenprozesse";
import { routeFaecherNeu } from "./RouteFaecherNeu";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";

const FaecherAuswahl = () => import("~/components/schule/kataloge/faecher/FaecherAuswahl.vue");
const FaecherApp = () => import("~/components/schule/kataloge/faecher/FaecherApp.vue");

export class RouteFaecher extends RouteAuswahlNode<FaecherListeManager, RouteDataFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.faecher", "schule/faecher/:id(\\d+)?", FaecherApp, FaecherAuswahl, new RouteDataFaecher());
		super.mode = ServerMode.STABLE;
		super.text = "Fächer";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeFaecherDaten,
			routeFaecherStundenplan,
			routeFaecherGruppenprozesse,
			routeFaecherNeu,
		];
		super.defaultChild = routeFaecherDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<FaecherAuswahlProps>{
			...props,
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeFaecher = new RouteFaecher();

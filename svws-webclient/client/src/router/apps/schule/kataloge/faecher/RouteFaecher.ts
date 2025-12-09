import type { RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { FachListeManager } from "@ui";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeFaecherDaten } from "~/router/apps/schule/kataloge/faecher/RouteFaecherDaten";

import type { FaecherAuswahlProps } from "~/components/schule/kataloge/faecher/SFaecherAuswahlProps";
import { RouteDataFaecher } from "./RouteDataFaecher";
import { routeFaecherStundenplan } from "./stundenplan/RouteFaecherStundenplan";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeFaecherGruppenprozesse } from "./RouteFaecherGruppenprozesse";
import { routeFaecherNeu } from "./RouteFaecherNeu";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";

const SFaecherAuswahl = () => import("~/components/schule/kataloge/faecher/SFaecherAuswahl.vue");
const SFaecherApp = () => import("~/components/schule/kataloge/faecher/SFaecherApp.vue");

export class RouteFaecher extends RouteAuswahlNode<FachListeManager, RouteDataFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.faecher", "schule/faecher/:id(\\d+)?", SFaecherApp, SFaecherAuswahl, new RouteDataFaecher());
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
			setzeDefaultSortierungSekII: this.data.setzeDefaultSortierungSekII,
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeFaecher = new RouteFaecher();

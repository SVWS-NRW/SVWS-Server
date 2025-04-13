import type { RouteParams } from "vue-router";

import type { LehrerListeManager } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteDataLehrer } from "~/router/apps/lehrer/RouteDataLehrer";
import { routeLehrerGruppenprozesse } from "~/router/apps/lehrer/RouteLehrerGruppenprozesse";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerStundenplan } from "./stundenplan/RouteLehrerStundenplan";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { api } from "~/router/Api";
import { ConfigElement } from "../../../../../ui/src/utils/Config";
import type { LehrerAuswahlProps } from "~/components/lehrer/SLehrerAuswahlProps";
import { AppMenuGroup } from "@ui";
import {routeLehrerEinwilligungen} from "~/router/apps/lehrer/einwilligungen/RouteLehrerEinwilligungen";
import {routeLehrerLernplattformen} from "~/router/apps/lehrer/lernplattformen/RouteLehrerLernplattformen";

const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue");
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue");

export class RouteLehrer extends RouteAuswahlNode<LehrerListeManager, RouteDataLehrer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_ANSEHEN ], "lehrer", "lehrkraefte/:id(\\d+)?", SLehrerApp, SLehrerAuswahl, new RouteDataLehrer());
		super.mode = ServerMode.STABLE;
		super.text = "Lehrkräfte";
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerStundenplan,
			routeLehrerUnterrichtsdaten,
			routeLehrerEinwilligungen,
			routeLehrerLernplattformen,
			routeLehrerGruppenprozesse,
			routeLehrerNeu,
		];
		super.defaultChild = routeLehrerIndividualdaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-briefcase-line";
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<LehrerAuswahlProps>{
			...props,
			setFilterNurSichtbar: this.data.setFilterNurSichtbar,
			setFilterNurStatistikrelevant: this.data.setFilterNurStatistikrelevant,
		});
		api.config.addElements([
			new ConfigElement("lehrer.auswahl.filterNurSichtbar", "user", "true"),
			new ConfigElement("lehrer.auswahl.filterNurStatistikrelevant", "user", "true"),
		]);
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
			return this.getRouteView(routeLehrerStundenplan);
		return this.getRouteSelectedChild();
	};

}

export const routeLehrer = new RouteLehrer();

import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { LehrerListeManager } from "@ui";
import { AppMenuGroup, ConfigElement } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteDataLehrer } from "~/router/apps/lehrer/RouteDataLehrer";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerStundenplan } from "./stundenplan/RouteLehrerStundenplan";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import type { LehrerAuswahlProps } from "~/components/lehrer/LehrerAuswahlProps";
import { routeLehrerEinwilligungen } from "~/router/apps/lehrer/einwilligungen/RouteLehrerEinwilligungen";
import { routeLehrerLernplattformen } from "~/router/apps/lehrer/lernplattformen/RouteLehrerLernplattformen";
import { routeLehrerAllgemeinesGruppenprozesse } from "~/router/apps/lehrer/allgemeines/RouteLehrerAllgemeinesGruppenprozesse";
import { routeLehrerIndividualdatenGruppenprozesse } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdatenGruppenprozesse";
import type { LehrerAppProps } from "~/components/lehrer/LehrerAppProps";
import type { RouteNode } from "~/router/RouteNode";
import { Katalog } from "~/cache/Katalog";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

const LehrerAuswahl = () => import("~/components/lehrer/LehrerAuswahl.vue");
const LehrerApp = () => import("~/components/lehrer/LehrerApp.vue");

export class RouteLehrer extends RouteAuswahlNode<LehrerListeManager, RouteDataLehrer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer", String.raw`lehrkraefte/:id(\d+)?`, LehrerApp, LehrerAuswahl,
			new RouteDataLehrer());
		super.mode = ServerMode.STABLE;
		super.text = "Lehrkräfte";
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerStundenplan,
			routeLehrerUnterrichtsdaten,
			routeLehrerEinwilligungen,
			routeLehrerLernplattformen,
			routeLehrerAllgemeinesGruppenprozesse,
			routeLehrerIndividualdatenGruppenprozesse,
			routeLehrerNeu,
		];
		super.defaultChild = routeLehrerIndividualdaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-briefcase-line";
		super.getAuswahlListProps = (props) => (<LehrerAuswahlProps>{
			...props,
			setFilterNurSichtbar: this.data.setFilterNurSichtbar,
			setFilterNurStatistikrelevant: this.data.setFilterNurStatistikrelevant,
		});
		super.getAuswahlProps = props => (<LehrerAppProps>{
			...props,
			gotoDefaultView: this.data.gotoDefaultView,
		});
		configStateImpl.config.addElements([
			new ConfigElement("lehrer.auswahl.filterNurSichtbar", "user", "true"),
			new ConfigElement("lehrer.auswahl.filterNurStatistikrelevant", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await Promise.all([orteStateImpl.init(), routeApp.cache.refreshKataloge(Katalog.LEITUNGSFUNKTIONEN)]);
		}
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}
}

export const routeLehrer = new RouteLehrer();

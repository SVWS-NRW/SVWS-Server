import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { FaecherNeuProps } from "~/components/schule/kataloge/faecher/FaecherNeuProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeFaecher, type RouteFaecher } from "./RouteFaecher";
import { routeApp } from "../../../RouteApp";
import { api } from "~/router/Api";

const FaecherNeu = () => import("~/components/schule/kataloge/faecher/FaecherNeu.vue");

export class RouteFaecherNeu extends RouteNode<any, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.faecher.neu", "neu", FaecherNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FaecherNeuProps {
		return {
			manager: () => routeFaecher.data.manager,
			add: routeFaecher.data.add,
			gotoDefaultView: routeFaecher.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
		};
	}
}

export const routeFaecherNeu = new RouteFaecherNeu();

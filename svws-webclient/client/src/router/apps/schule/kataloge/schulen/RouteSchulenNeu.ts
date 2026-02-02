import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SchulenNeuProps } from "~/components/schule/kataloge/schulen/SchulenNeuProps";
import type { RouteSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "../../../RouteApp";
import { routeSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { api } from "~/router/Api";

const SchulenNeu = () => import("~/components/schule/kataloge/schulen/SchulenNeu.vue");

export class RouteSchulenNeu extends RouteNode<any, RouteSchulen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen.neu", "neu", SchulenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchulenNeuProps {
		return {
			manager: () => routeSchulen.data.manager,
			add: routeSchulen.data.add,
			gotoDefaultView: routeSchulen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
			schuljahr: routeApp.data.aktAbschnitt.value.schuljahr,
			schulform: api.schulform,
		};
	}
}

export const routeSchulenNeu = new RouteSchulenNeu();

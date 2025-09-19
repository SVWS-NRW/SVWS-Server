import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { ReligionenNeuProps } from "~/components/schule/allgemein/religionen/SReligionenNeuProps";
import type { RouteReligionen } from "~/router/apps/schule/allgemein/religionen/RouteReligionen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "../../../RouteApp";
import { routeReligionen } from "~/router/apps/schule/allgemein/religionen/RouteReligionen";
import { api } from "~/router/Api";

const SReligionenNeu = () => import("~/components/schule/allgemein/religionen/SReligionenNeu.vue");

export class RouteReligionenNeu extends RouteNode<any, RouteReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen.neu", "neu", SReligionenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religionen Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): ReligionenNeuProps {
		return {
			manager: () => routeReligionen.data.manager,
			add: routeReligionen.data.add,
			gotoDefaultView: routeReligionen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeReligionenNeu = new RouteReligionenNeu();

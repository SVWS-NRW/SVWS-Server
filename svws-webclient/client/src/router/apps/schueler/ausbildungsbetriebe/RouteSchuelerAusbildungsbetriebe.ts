import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerAusbildungsbetriebe } from "~/router/apps/schueler/ausbildungsbetriebe/RouteDataSchuelerAusbildungsbetriebe";

import type { SchuelerAdressenProps } from "~/components/schueler/adressen/SSChuelerAdressenProps";

const SSchuelerAdressen = () => import("~/components/schueler/adressen/SSchuelerAdressen.vue");

export class RouteSchuelerAusbildungsbetriebe extends RouteNode<RouteDataSchuelerAusbildungsbetriebe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.ausbildungsbetriebe", "ausbildungsbetriebe", SSchuelerAdressen, new RouteDataSchuelerAusbildungsbetriebe());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ausbildungsbetriebe";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		const id = to_params.id === undefined ? undefined : parseInt(to_params.id);
		await this.data.setSchueler(id);
		await this.data.setSchuelerBetrieb();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAdressenProps {
		return {
			patchBetrieb: this.data.patchBetrieb,
			patchSchuelerBetriebsdaten: this.data.patchSchuelerBetriebsdaten,
			patchAnsprechpartner: this.data.patchAnsprechpartner,
			setSchuelerBetrieb: this.data.setSchuelerBetrieb,
			createAnsprechpartner: this.data.createAnsprechpartner,
			createSchuelerBetriebsdaten: this.data.createSchuelerBetriebsdaten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			idSchueler: routeSchueler.data.schuelerListeManager.daten().id,
			listSchuelerbetriebe: () => this.data.listSchuelerbetriebe,
			betrieb: this.data.betrieb,
			betriebsStammdaten: this.data.daten,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapLehrer: this.data.mapLehrer,
			mapBetriebe: this.data.mapBetriebe,
			mapAnsprechpartner: this.data.mapAnsprechpartner,
		};
	}

}

export const routeSchuelerAusbildungsbetriebe = new RouteSchuelerAusbildungsbetriebe();


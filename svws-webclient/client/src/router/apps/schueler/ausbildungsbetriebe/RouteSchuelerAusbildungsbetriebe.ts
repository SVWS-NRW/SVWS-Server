import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException } from "@core";
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
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN ], "schueler.ausbildungsbetriebe", "ausbildungsbetriebe", SSchuelerAdressen, new RouteDataSchuelerAusbildungsbetriebe());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ausbildungsbetriebe";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			await this.data.setSchueler(id);
			await this.data.setSchuelerBetrieb();
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
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
			idSchueler: routeSchueler.data.manager.daten().id,
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


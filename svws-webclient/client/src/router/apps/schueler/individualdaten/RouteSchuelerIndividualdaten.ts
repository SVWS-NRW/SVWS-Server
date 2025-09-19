import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteDataSchuelerIndividualdaten";

import type { SchuelerIndividualdatenProps } from "~/components/schueler/individualdaten/SSchuelerIndividualdatenProps";
import { api } from "~/router/Api";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");


export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
	}

	public getProps(to: RouteLocationNormalized): SchuelerIndividualdatenProps {
		return {
			patch: routeSchueler.data.patch,
			schuelerListeManager: () => routeSchueler.data.manager,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			mapFahrschuelerarten: this.data.mapFahrschuelerarten,
			mapFoerderschwerpunkte: this.data.mapFoerderschwerpunkte,
			mapHaltestellen: this.data.mapHaltestellen,
			mapReligionen: this.data.mapReligionen,
			mapTelefonArten: this.data.mapTelefonArten,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.getListSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			mapSchulen: this.data.mapSchulen,
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			autofocus: routeSchueler.data.autofocus,
			getPDF: routeSchueler.data.getPDF,
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();


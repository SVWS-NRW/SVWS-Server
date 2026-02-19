import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import type { SchuelerSchnelleingabeProps } from "~/components/schueler/neuanlage/SchuelerSchnelleingabeProps.js";
import { ViewType } from "@ui";
import { RouteDataSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteDataSchuelerSchnelleingabe";

const SchuelerSchnelleingabe = () => import("~/components/schueler/neuanlage/SchuelerSchnelleingabe.vue");

export class RouteSchuelerSchnelleingabe extends RouteNode<RouteDataSchuelerSchnelleingabe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.schnelleingabe", "schnelleingabe", SchuelerSchnelleingabe, new RouteDataSchuelerSchnelleingabe());
		super.types = new Set([ViewType.NEU]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schnelleingabe";
		this.setCheckpoint = true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchnelleingabeProps {
		return {
			manager: () => this.data.manager,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			gotoSchuelerNeuView: routeSchueler.data.gotoHinzufuegenView,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,

			patch: routeSchueler.data.patch,
			getListSchuelerErziehereintraege: () => routeSchueler.data.getListSchuelerErziehereintraege,
			addSchuelerErziehereintrag: routeSchueler.data.addSchuelerErziehereintrag,
			patchSchuelerErziehereintrag: routeSchueler.data.patchSchuelerErziehereintrag,
			patchSchuelerErzieherAnPosition: routeSchueler.data.patchSchuelerErzieherAnPosition,
			deleteSchuelerErziehereintrage: routeSchueler.data.deleteSchuelerErziehereintrage,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.getListSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			getListSchuelerVermerkeintraege: () => routeSchueler.data.getListSchuelerVermerkeintraege,
			addSchuelerVermerkeintrag: routeSchueler.data.addSchuelerVermerkeintrag,
			patchSchuelerVermerkeintrag: routeSchueler.data.patchSchuelerVermerkeintrag,
			deleteSchuelerVermerkeintraege: routeSchueler.data.deleteSchuelerVermerkeintrage,
			patchSchuelerSchulbesuchsdaten: routeSchueler.data.patchSchuelerSchulbesuchdaten,
			patchSchuelerLernabschnittsdaten: routeSchueler.data.patchSchuelerLernabschnitt,
			getSchuelerKlassenFuerAbschnitt: routeSchueler.data.getSchuelerKlassenFuerAbschnitt,
		};
	}

}

export const routeSchuelerSchnelleingabe = new RouteSchuelerSchnelleingabe();

import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, type DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import type { RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeApp } from "~/router/apps/RouteApp";
import { api } from "~/router/Api";
import type { SchuelerNeuSchnelleingabeProps } from "~/components/schueler/SSchuelerNeuSchnelleingabeProps.js";
import { ViewType } from "@ui";
import { RouteDataSchuelerNeuSchnelleingabe } from "~/router/apps/schueler/RouteDataSchuelerNeuSchnelleingabe";
import { routeError } from "~/router/error/RouteError";

const SSchuelerNeuSchnelleingabe = () => import("~/components/schueler/SSchuelerNeuSchnelleingabe.vue");

export class RouteSchuelerNeuSchnelleingabe extends RouteNode<RouteDataSchuelerNeuSchnelleingabe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN ], "schueler.schnelleingabe", "schnelleingabe", SSchuelerNeuSchnelleingabe, new RouteDataSchuelerNeuSchnelleingabe());
		super.types = new Set([ ViewType.NEU ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schnelleingabe";
		this.setCheckpoint = true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeKataloge();
			if (id !== undefined)
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerNeuSchnelleingabeProps {
		return {
			schuelerListeManager: () => routeSchueler.data.manager,
			schuelerSchulbesuchsManager: () => routeSchueler.data.schuelerSchulbesuchManager,
			patch: routeSchueler.data.patch,
			mapSchulen: this.data.mapSchulen,
			mapOrte: this.data.mapOrte,
			mapOrtsteile: this.data.mapOrtsteile,
			mapReligionen: this.data.mapReligionen,
			mapFahrschuelerarten: this.data.mapFahrschuelerarten,
			mapHaltestellen: this.data.mapHaltestellen,
			mapKindergaerten: this.data.mapKindergaerten,
			mapEinschulungsarten: this.data.mapEinschulungsarten,
			mapErzieherarten: this.data.mapErzieherarten,
			mapTelefonArten: this.data.mapTelefonArten,
			getListSchuelerErziehereintraege: () => routeSchueler.data.getListSchuelerErziehereintraege,
			addSchuelerErziehereintrag: routeSchueler.data.addSchuelerErziehereintrag,
			patchSchuelerErziehereintrag: routeSchueler.data.patchSchuelerErziehereintrag,
			patchSchuelerErzieherAnPosition: routeSchueler.data.patchSchuelerErzieherAnPosition,
			deleteSchuelerErziehereintrage: routeSchueler.data.deleteSchuelerErziehereintrage,
			getListSchuelerTelefoneintraege: () => routeSchueler.data.getListSchuelerTelefoneintraege,
			addSchuelerTelefoneintrag: routeSchueler.data.addSchuelerTelefoneintrag,
			patchSchuelerTelefoneintrag: routeSchueler.data.patchSchuelerTelefoneintrag,
			deleteSchuelerTelefoneintrage: routeSchueler.data.deleteSchuelerTelefoneintrage,
			patchSchuelerSchulbesuchsdaten: routeSchueler.data.patchSchuelerSchulbesuchdaten,
			gotoDefaultView: routeSchueler.data.gotoDefaultView,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

}

export const routeSchuelerNeuSchnelleingabe = new RouteSchuelerNeuSchnelleingabe();

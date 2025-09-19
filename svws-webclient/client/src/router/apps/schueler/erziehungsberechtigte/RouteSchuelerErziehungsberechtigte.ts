import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeApp } from "~/router/apps/RouteApp";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteDataSchuelerErziehungsberechtigte";

import type { SchuelerErziehungsberechtigteProps } from "~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigteProps";
import { api } from "~/router/Api";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN ], "schueler.erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering)
				await this.data.ladeListe();
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			await this.data.setEintrag(id);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerErziehungsberechtigteProps {
		return {
			patchErzieher: this.data.patchErzieher,
			patchErzieherAnPosition: this.data.patchErzieherAnPosition,
			addErzieher: this.data.addErzieher,
			deleteErzieher: this.data.deleteErzieher,
			data: () => this.data.daten,
			mapErzieherarten: this.data.mapErzieherarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			benutzerKompetenzen: api.benutzerKompetenzen,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
		};
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();


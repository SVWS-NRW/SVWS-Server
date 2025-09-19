import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteDataSchuelerSchulbesuch";

import type { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SSchuelerSchulbesuchProps";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeKatalogSchulen } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchulen";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN ], "schueler.schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id !== undefined)
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	goToSchule = async (idSchule : number) => {
		await RouteManager.doRoute(routeKatalogSchulen.getRoute({id : idSchule}));
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchulbesuchProps {
		return {
			manager: () => this.data.schuelerSchulbesuchManager,
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			autofocus: routeSchueler.data.autofocus,
			goToSchule: this.goToSchule,
			addSchuelerSchulbesuchSchule: this.data.addSchuelerSchulbesuchSchule,
			patchSchuelerSchulbesuchSchule: this.data.patchSchuelerSchulbesuchSchule,
			deleteSchuelerSchulbesuchSchulen: this.data.deleteSchuelerSchulbesuchSchulen,
			addSchuelerSchulbesuchMerkmal: this.data.addSchuelerSchulbesuchMerkmal,
			patchSchuelerSchulbesuchMerkmal: this.data.patchSchuelerSchulbesuchMerkmal,
			deleteSchuelerSchulbesuchMerkmale: this.data.deleteSchuelerSchulbesuchMerkmale,
		};
	}
}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();


import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerEinwilligungen } from "~/router/apps/schueler/einwilligungen/RouteDataSchuelerEinwilligungen";

import type { SchuelerEinwilligungenProps } from "~/components/schueler/einwilligungen/SchuelerEinwilligungenProps";
import { api } from "~/router/Api";

const SSchuelerEinwilligungen = () => import("~/components/schueler/einwilligungen/SSchuelerEinwilligungen.vue");

export class RouteSchuelerEinwilligungen extends RouteNode<RouteDataSchuelerEinwilligungen, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.einwilligungen", "einwilligungen", SSchuelerEinwilligungen, new RouteDataSchuelerEinwilligungen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einwilligungen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined)
				await this.data.ladeDaten(null);
			else
				await this.data.ladeDaten(routeSchueler.data.schuelerListeManager.liste.get(id));
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerEinwilligungenProps {
		return {
			einwilligungen: () => this.data.einwilligungen,
			mapEinwilligungsarten: this.data.mapEinwilligungsarten,
			patch: this.data.patch,
			apiStatus: api.status,
		};
	}
}

export const routeSchuelerEinwilligungen = new RouteSchuelerEinwilligungen();


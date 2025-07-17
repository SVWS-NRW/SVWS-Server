import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";

import { api } from "~/router/Api";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteDataLehrerEinwilligungen } from "~/router/apps/lehrer/einwilligungen/RouteDataLehrerEinwilligungen";
import type { LehrerEinwilligungenProps } from "~/components/lehrer/einwilligungen/LehrerEinwilligungenProps";

const SLehrerEinwilligungen = () => import("~/components/lehrer/einwilligungen/SLehrerEinwilligungen.vue");

export class RouteLehrerEinwilligungen extends RouteNode<RouteDataLehrerEinwilligungen, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN ], "lehrer.einwilligungen", "einwilligungen", SLehrerEinwilligungen, new RouteDataLehrerEinwilligungen());
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
				await this.data.ladeDaten(routeLehrer.data.manager.liste.get(id));
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): LehrerEinwilligungenProps {
		return {
			einwilligungen: () => this.data.einwilligungen,
			mapEinwilligungsarten: this.data.mapEinwilligungsarten,
			patch: this.data.patch,
			apiStatus: api.status,
		};
	}
}

export const routeLehrerEinwilligungen = new RouteLehrerEinwilligungen();


import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";

import { api } from "~/router/Api";
import { RouteDataLehrerLernplattformen } from "~/router/apps/lehrer/lernplattformen/RouteDataLehrerLernplattformen";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { LehrerLernplattformenProps } from "~/components/lehrer/lernplattformen/LehrerLernplattformenProps";

const SLehrerLernplattformen = () => import("~/components/lehrer/lernplattformen/SLehrerLernplattformen.vue");

export class RouteLehrerLernplattformen extends RouteNode<RouteDataLehrerLernplattformen, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN ], "lehrer.lernplattformen", "lernplattformen", SLehrerLernplattformen, new RouteDataLehrerLernplattformen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen";
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
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): LehrerLernplattformenProps {
		return {
			lehrerLernplattformen: () => this.data.lehrerLernplattformen,
			mapLernplattformen: this.data.mapLernplattformen,
			patch: this.data.patch,
			apiStatus: api.status,
		};
	}
}

export const routeLehrerLernplattformen = new RouteLehrerLernplattformen();


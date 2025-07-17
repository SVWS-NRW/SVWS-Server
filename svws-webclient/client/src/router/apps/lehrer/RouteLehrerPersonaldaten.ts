import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";

import type { LehrerPersonaldatenProps } from "~/components/lehrer/personaldaten/SLehrerPersonaldatenProps";
import { routeApp } from "../RouteApp";
import { routeError } from "~/router/error/RouteError";
import { api } from "~/router/Api";

const SLehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN ], "lehrer.personaldaten", "personaldaten", SLehrerPersonaldaten);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (!routeLehrer.data.manager.hasDaten())
				return routeLehrer.getRoute();
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if ((!routeLehrer.data.manager.hasPersonalDaten()) || (id !== routeLehrer.data.manager.personalDaten().id))
				await routeLehrer.data.loadPersonaldaten();
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeLehrer.data.unloadPersonaldaten();
	}

	public getProps(to: RouteLocationNormalized): LehrerPersonaldatenProps {
		return {
			validatorKontext: () => api.validatorKontext,
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			lehrerListeManager: () => routeLehrer.data.manager,
			patch: routeLehrer.data.patchPersonaldaten,
			patchAbschnittsdaten: routeLehrer.data.patchPersonalAbschnittsdaten,
			patchLehramt: routeLehrer.data.patchLehramt,
			addLehramt: routeLehrer.data.addLehramt,
			removeLehraemter: routeLehrer.data.removeLehraemter,
			patchLehrbefaehigung: routeLehrer.data.patchLehrbefaehigung,
			addLehrbefaehigung: routeLehrer.data.addLehrbefaehigung,
			removeLehrbefaehigungen: routeLehrer.data.removeLehrbefaehigungen,
			patchFachrichtung: routeLehrer.data.patchFachrichtung,
			addFachrichtung: routeLehrer.data.addFachrichtung,
			removeFachrichtungen: routeLehrer.data.removeFachrichtungen,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();

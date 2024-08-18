import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";

import type { LehrerPersonaldatenProps } from "~/components/lehrer/personaldaten/SLehrerPersonaldatenProps";
import { routeApp } from "../RouteApp";
import { routeError } from "~/router/error/RouteError";

const SLehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/SLehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_DETAILDATEN_ANSEHEN ], "lehrer.personaldaten", "personaldaten", SLehrerPersonaldaten);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (!routeLehrer.data.lehrerListeManager.hasDaten())
			return routeLehrer.getRoute();
		const id = RouteNode.getIntParam(to_params, "id");
		if (id instanceof Error)
			return routeError.getRoute(id);
		if ((!routeLehrer.data.lehrerListeManager.hasPersonalDaten()) || (id !== routeLehrer.data.lehrerListeManager.personalDaten().id))
			await routeLehrer.data.loadPersonaldaten();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeLehrer.data.unloadPersonaldaten();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: id }};
	}

	public getProps(to: RouteLocationNormalized): LehrerPersonaldatenProps {
		return {
			lehrerListeManager: () => routeLehrer.data.lehrerListeManager,
			patch: routeLehrer.data.patchPersonaldaten,
			patchAbschnittsdaten: routeLehrer.data.patchPersonalAbschnittsdaten,
			patchLehramtAnerkennung: routeLehrer.data.patchLehramtAnerkennung,
			addLehramt: routeLehrer.data.addLehramt,
			removeLehraemter: routeLehrer.data.removeLehraemter,
			patchLehrbefaehigungAnerkennung: routeLehrer.data.patchLehrbefaehigungAnerkennung,
			addLehrbefaehigung: routeLehrer.data.addLehrbefaehigung,
			removeLehrbefaehigungen: routeLehrer.data.removeLehrbefaehigungen,
			patchFachrichtungAnerkennung: routeLehrer.data.patchFachrichtungAnerkennung,
			addFachrichtung: routeLehrer.data.addFachrichtung,
			removeFachrichtungen: routeLehrer.data.removeFachrichtungen,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();

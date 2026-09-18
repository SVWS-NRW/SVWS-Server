import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { type RouteLehrer, routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

const LehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/LehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN], "lehrer.personaldaten", "personaldaten", LehrerPersonaldaten);
		super.mode = ServerMode.ALPHA;
		super.text = "Personaldaten";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		const lehrerAuswahlState = useLehrerAuswahlState();
		try {
			if (!lehrerAuswahlState.manager.hasDaten()) {
				return routeLehrer.getRoute();
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if ((!lehrerAuswahlState.manager.hasPersonalDaten()) || (id !== lehrerAuswahlState.manager.personalDaten().id)) {
				await lehrerAuswahlState.loadPersonaldaten();
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		const lehrerAuswahlState = useLehrerAuswahlState();
		await lehrerAuswahlState.unloadPersonaldaten();
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();

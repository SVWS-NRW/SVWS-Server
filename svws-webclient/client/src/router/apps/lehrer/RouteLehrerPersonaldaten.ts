import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { LehrerPersonaldatenProps } from "~/components/lehrer/personaldaten/LehrerPersonaldatenProps";
import { routeError } from "~/router/error/RouteError";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const LehrerPersonaldaten = () => import("~/components/lehrer/personaldaten/LehrerPersonaldaten.vue");

export class RouteLehrerPersonaldaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN], "lehrer.personaldaten", "personaldaten", LehrerPersonaldaten);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Personaldaten";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			if (!routeLehrer.data.manager.hasDaten()) {
				return routeLehrer.getRoute();
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if ((!routeLehrer.data.manager.hasPersonalDaten()) || (id !== routeLehrer.data.manager.personalDaten().id)) {
				await routeLehrer.data.loadPersonaldaten();
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await routeLehrer.data.unloadPersonaldaten();
	}

	public getProps(_: RouteLocationNormalized): LehrerPersonaldatenProps {
		return {
			lehrerListeManager: () => routeLehrer.data.manager,
			mapSchulen: () => routeLehrer.data.mapSchulen,
			patchPersonaldaten: routeLehrer.data.patchPersonaldaten,
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
			addMehrleistung: routeLehrer.data.addMehrleistung,
			patchMehrleistung: routeLehrer.data.patchMehrleistung,
			removeMehrleistung: routeLehrer.data.removeMehrleistung,
			addMinderleistung: routeLehrer.data.addMinderleistung,
			patchMinderleistung: routeLehrer.data.patchMinderleistung,
			removeMinderleistung: routeLehrer.data.removeMinderleistung,
			addAnrechnung: routeLehrer.data.addAnrechnung,
			patchAnrechnungen: routeLehrer.data.patchAnrechnungen,
			removeAnrechnung: routeLehrer.data.removeAnrechnung,
			mapFaecher: () => routeLehrer.data.mapFaecher,
			lehrerUnterrichtsfaecher: () => routeLehrer.data.lehrerUnterrichtsfaecher,
			addLehrerUnterrichtsfach: routeLehrer.data.addLehrerUnterrichtsfach,
			patchLehrerUnterrichtsfach: routeLehrer.data.patchLehrerUnterrichtsfach,
			removeLehrerUnterrichtsfach: routeLehrer.data.removeLehrerUnterrichtsfach,
		};
	}

}

export const routeLehrerPersonaldaten = new RouteLehrerPersonaldaten();

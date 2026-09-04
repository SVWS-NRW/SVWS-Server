import type { RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { routeError } from "~/router/error/RouteError";
import { schulformenGymOb } from "~/router/RouteHelper";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { ServerMode } from "@core/core/types/ServerMode";

const SGostKlausurplanungNachschreiber = () => import("~/components/gost/klausuren/SGostKlausurplanungNachschreiber.vue");

export class RouteGostKlausurplanungNachschreiber extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.nachschreiber", "nachschreiber", SGostKlausurplanungNachschreiber);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Nachschreiber";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
	}

	public getProps() {
		return {
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			gotoNachschreiber: routeGostKlausurplanung.data.gotoNachschreiber,
		};
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			if ((abiturjahr === undefined) || (abiturjahr === -1)) {
				return { name: routeGostKlausurplanung.defaultChild!.name, params };
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			if ((abiturjahr === undefined) || (halbjahr === null)) {
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
			}
		} catch (e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

}

export const routeGostKlausurplanungNachschreiber = new RouteGostKlausurplanungNachschreiber();

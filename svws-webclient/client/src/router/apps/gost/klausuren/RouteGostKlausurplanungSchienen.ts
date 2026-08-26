import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { gostKlausurplanungStateImpl } from "~/states/GostKlausurplanungStateImpl";
import { BenutzerKompetenz, GostHalbjahr, ServerMode, DeveloperNotificationException } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { routeError } from "~/router/error/RouteError";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausuren/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.schienen", String.raw`schienen/:idtermin(\d+)?`, SGostKlausurplanungSchienen);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Schienen";
	}

	public getProps() {
		return {
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoNachschreiber: routeGostKlausurplanung.data.gotoNachschreiber,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			gotoSchienen: routeGostKlausurplanung.data.gotoSchienen,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
		};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			const termin = gostKlausurplanungStateImpl.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			gostKlausurplanungStateImpl.setSelectedTermin(termin ?? undefined);
			if ((abiturjahr === undefined) || (halbjahr === null)) {
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
			}
		} catch (e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { idtermin: gostKlausurplanungStateImpl.selectedTermin?.id ?? undefined };
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();

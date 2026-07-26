import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { BenutzerKompetenz, GostHalbjahr, ServerMode, DeveloperNotificationException } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import type { GostKlausurplanungSchienenProps } from "~/components/gost/klausuren/SGostKlausurplanungSchienenProps";
import { routeError } from "~/router/error/RouteError";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostKlausurplanungSchienen = () => import("~/components/gost/klausuren/SGostKlausurplanungSchienen.vue");

export class RouteGostKlausurplanungSchienen extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN,
		], "gost.klausurplanung.schienen", "schienen/:idtermin(d+)?", SGostKlausurplanungSchienen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schienen";
	}
	protected async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr: halbjahrId, idtermin } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idtermin"]);
			const halbjahr = GostHalbjahr.fromID(halbjahrId ?? null);
			const termin = routeGostKlausurplanung.data.manager.terminGetByIdOrNull(idtermin ?? -1) ?? undefined;
			routeGostKlausurplanung.data.terminSelected.value = termin ?? undefined;
			if ((abiturjahr === undefined) || (halbjahr === null)) {
				throw new DeveloperNotificationException("Fehler: Abiturjahr und Halbjahr müssen als Parameter der Route an dieser Stelle vorhanden sein.");
			}
		} catch (e) {
			return await routeError.getErrorRoute(e instanceof Error ? e : new DeveloperNotificationException("Unbekannter Fehler beim Laden der Klausurplanungsdaten."));
		}
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { idtermin: routeGostKlausurplanung.data.terminSelected.value?.id ?? undefined };
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungSchienenProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			abschnitt: routeGostKlausurplanung.data.abschnitt,
			kMan: () => routeGostKlausurplanung.data.manager,
			patchKlausur: routeGostKlausurplanung.data.patchKlausur,
			createSchuelerklausurtermin: routeGostKlausurplanung.data.createSchuelerklausurtermin,
			patchKlausurtermin: routeGostKlausurplanung.data.patchKlausurtermin,
			erzeugeKlausurtermin: routeGostKlausurplanung.data.erzeugeKlausurtermin,
			loescheKlausurtermine: routeGostKlausurplanung.data.loescheKlausurtermine,
			loescheKursklausuren: routeGostKlausurplanung.data.loescheKursklausuren,
			erzeugeKursklausurenAusVorgaben: routeGostKlausurplanung.data.erzeugeKursklausurenAusVorgaben,
			blockenKursklausuren: routeGostKlausurplanung.data.blockenKursklausuren,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
			terminSelected: routeGostKlausurplanung.data.terminSelected,
			gotoVorgaben: routeGostKlausurplanung.data.gotoVorgaben,
			gotoKalenderdatum: routeGostKlausurplanung.data.gotoKalenderdatum,
			gotoRaumzeitTermin: routeGostKlausurplanung.data.gotoRaumzeitTermin,
			gotoSchienen: routeGostKlausurplanung.data.gotoSchienen,
		};
	}

}

export const routeGostKlausurplanungSchienen = new RouteGostKlausurplanungSchienen();


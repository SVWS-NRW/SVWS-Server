import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { DeveloperNotificationException, GostSchuelerklausurTermin, List} from "@core";
import { ArrayList, BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanung, type RouteGostKlausurplanung } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanung";
import type { GostKlausurplanungNachschreibAnsichtProps } from "~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsichtProps";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostKlausurplanungNachschreibAnsicht = () => import("~/components/gost/klausurplanung/SGostKlausurplanungNachschreibAnsicht.vue");

export class RouteGostKlausurplanungNachschreibAnsicht extends RouteNode<any, RouteGostKlausurplanung> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "gost.klausurplanung.nachschreibansicht", "nachschreibansicht", SGostKlausurplanungNachschreibAnsicht);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachschreibplan";
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			return ((abiturjahr === undefined) || (abiturjahr === -1))
		} catch (e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		// if (isEntering) {
		// 	await routeGostKlausurplanung.data.erzeugeKlausurraummanager(this.mapIDs(routeGostKlausurplanung.data.manager.schuelerklausurterminNtAktuellMitTerminUndDatumGetMengeByHalbjahrAndQuartal(routeGostKlausurplanung.data.jahrgangsdaten.abiturjahr, routeGostKlausurplanung.data.halbjahr, routeGostKlausurplanung.data.quartalsauswahl.value)));
		// }
	}

	public getProps(to: RouteLocationNormalized): GostKlausurplanungNachschreibAnsichtProps {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten,
			halbjahr: routeGostKlausurplanung.data.halbjahr,
			kMan: () => routeGostKlausurplanung.data.manager,
			quartalsauswahl: routeGostKlausurplanung.data.quartalsauswahl,
		}
	}

	private mapIDs(skts: List<GostSchuelerklausurTermin>) {
		const numList = new ArrayList<number>();
		for (const skt of skts)
			numList.add(skt.id);
		return numList;
	}

}



export const routeGostKlausurplanungNachschreibAnsicht = new RouteGostKlausurplanungNachschreibAnsicht();


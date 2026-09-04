import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeGostAbitur, type RouteGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";
import type { GostAbiturNoteneingabeProps } from "~/components/gost/abitur/GostAbiturNoteneingabeProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeGost } from "../RouteGost";
import { routeError } from "~/router/error/RouteError";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const GostAbiturNoteneingabe = () => import("~/components/gost/abitur/GostAbiturNoteneingabe.vue");

export class RouteGostAbiturNoteneingabe extends RouteNode<any, RouteGostAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "gost.abitur.noteneingabe", "noteneingabe", GostAbiturNoteneingabe);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Noteneingabe";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
		super.children = [
		];
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = (params !== undefined) ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: undefined };
			if (abiturjahr === undefined) {
				return false;
			}
			const eintrag = routeGost.data.mapAbiturjahrgaenge.get(abiturjahr);
			if (eintrag === undefined) {
				return false;
			}
			if ((eintrag.abiturjahr !== -1)
				&& (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN)
					|| (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN) && benutzerStateImpl.kompetenzenAbiturjahrgaenge.has(eintrag.abiturjahr)))
				&& (eintrag.jahrgang === 'Q2') && (eintrag.halbjahr === 2)) {
				return false;
			}
			return routeGost.getRouteDefaultChild({ abiturjahr });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): GostAbiturNoteneingabeProps {
		return {
			schuelerListe: routeGostAbitur.data.schuelerListe,
			mapLehrer: routeGostAbitur.data.mapLehrer,
			mapKurse: routeGostAbitur.data.mapKurse,
			managerMap: () => routeGostAbitur.data.managerAbiturMap,
			updateAbiturpruefungsdaten: routeGostAbitur.data.updateAbiturpruefungsdaten,
		};
	}

}

export const routeGostAbiturNoteneingabe = new RouteGostAbiturNoteneingabe();


import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschKurs42, type RouteSchuleDatenaustauschKurs42 } from "~/router/apps/schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import type { SchuleDatenaustauschKurs42RaeumeProps } from "~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42RaeumeProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SSchuleDatenaustauschKurs42Raeume = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42Raeume.vue");

export class RouteSchuleDatenaustauschKurs42Raeume extends RouteNode<any, RouteSchuleDatenaustauschKurs42> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.kurs42.raeume", "raeume", SSchuleDatenaustauschKurs42Raeume);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschKurs42RaeumeProps {
		return {
			setGostKurs42RaeumeTxt: routeSchuleDatenaustauschKurs42.data.setGostKurs42RaeumeTxt,
		};
	}

}

export const routeSchuleDatenaustauschKurs42Raeume = new RouteSchuleDatenaustauschKurs42Raeume();


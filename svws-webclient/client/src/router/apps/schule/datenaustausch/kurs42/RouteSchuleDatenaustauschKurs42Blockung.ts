import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { type RouteSchuleDatenaustauschKurs42 } from "~/router/apps/schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import type { SchuleDatenaustauschKurs42BlockungProps } from "~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42BlockungProps";
import { routeSchuleDatenaustauschKurs42 } from "./RouteSchuleDatenaustauschKurs42";
import { schulformenGymOb } from "~/router/RouteHelper";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SSchuleDatenaustauschKurs42Blockung = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42Blockung.vue");

export class RouteSchuleDatenaustauschKurs42Blockung extends RouteNode<any, RouteSchuleDatenaustauschKurs42> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
		], "schule.datenaustausch.kurs42.blockung", "blockung", SSchuleDatenaustauschKurs42Blockung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Blockung";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschKurs42BlockungProps {
		return {
			setGostKurs42ImportZip: routeSchuleDatenaustauschKurs42.data.setGostKurs42ImportZip,
		};
	}
}

export const routeSchuleDatenaustauschKurs42Blockung = new RouteSchuleDatenaustauschKurs42Blockung();


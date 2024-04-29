import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisBlockungenProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungenProps";

const SSchuleDatenaustauschUntisBlockungen = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungen.vue");

export class RouteSchuleDatenaustauschUntisBlockungen extends RouteNode<unknown, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis.blockungen", "blockungen", SSchuleDatenaustauschUntisBlockungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Blockungen";
	}

	public async enter() {
		return routeSchuleDatenaustauschUntis.data.ladeAbiturjahrgaenge();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisBlockungenProps {
		return {
			mapAbiturjahrgaenge: () => routeSchuleDatenaustauschUntis.data.mapAbiturjahrgaenge,
			abiturjahrgang: () => routeSchuleDatenaustauschUntis.data.abiturjahrgang,
			setAbiturjahrgang: routeSchuleDatenaustauschUntis.data.setAbiturjahrgang,
			halbjahr: () => routeSchuleDatenaustauschUntis.data.halbjahr,
			setHalbjahr: routeSchuleDatenaustauschUntis.data.setHalbjahr,
			mapBlockungen: () => routeSchuleDatenaustauschUntis.data.mapBlockungen,
			setBlockung: routeSchuleDatenaustauschUntis.data.setBlockung,
			getDatenmanager: () => routeSchuleDatenaustauschUntis.data.getDatenmanager,
			exportUntisBlockungenZIP: routeSchuleDatenaustauschUntis.data.exportUntisBlockungenZIP,
		};
	}

}

export const routeSchuleDatenaustauschUntisBlockungen = new RouteSchuleDatenaustauschUntisBlockungen();


import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisBlockungenProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungenProps";

const SSchuleDatenaustauschUntisBlockungen = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungen.vue");

export class RouteSchuleDatenaustauschUntisBlockungen extends RouteNode<unknown, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis.blockungen", "blockungen/:abiturjahr(\\d+)?/:halbjahr([0-5])?/:idblockung(\\d+)?/:idergebnis(\\d+)?", SSchuleDatenaustauschUntisBlockungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Blockungen";
	}

	public async enter() {
		return routeSchuleDatenaustauschUntis.data.ladeAbiturjahrgaenge();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : parseInt(to_params.halbjahr);
		const idBlockung = !to_params.idblockung ? undefined : parseInt(to_params.idblockung);
		const idErgebnis = !to_params.idergebnis ? undefined : parseInt(to_params.idergebnis);
		if (abiturjahr && halbjahr && idBlockung && idErgebnis)
			routeSchuleDatenaustauschUntis.data.gotoErgebnis(abiturjahr, halbjahr, idBlockung, idErgebnis);
	}

	public getRoute(to_params?: RouteParams) : RouteLocationRaw {
		const { abiturjahr, halbjahr, idblockung, idergebnis } = to_params || {};
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, idblockung, idergebnis}};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisBlockungenProps {
		return {
			mapAbiturjahrgaenge: () => routeSchuleDatenaustauschUntis.data.mapAbiturjahrgaenge,
			abiturjahrgang: () => routeSchuleDatenaustauschUntis.data.abiturjahrgang,
			setAbiturjahrgang: routeSchuleDatenaustauschUntis.data.setAbiturjahrgang,
			halbjahr: () => routeSchuleDatenaustauschUntis.data.halbjahr,
			setHalbjahr: routeSchuleDatenaustauschUntis.data.setHalbjahr,
			mapBlockungen: () => routeSchuleDatenaustauschUntis.data.mapBlockungen,
			blockung: () => routeSchuleDatenaustauschUntis.data.blockung,
			setBlockung: routeSchuleDatenaustauschUntis.data.setBlockung,
			ergebnis: () => routeSchuleDatenaustauschUntis.data.ergebnis,
			setErgebnis: routeSchuleDatenaustauschUntis.data.setErgebnis,
			getDatenmanager: () => routeSchuleDatenaustauschUntis.data.getDatenmanager,
			exportUntisBlockungenZIP: routeSchuleDatenaustauschUntis.data.exportUntisBlockungenZIP,
		};
	}

}

export const routeSchuleDatenaustauschUntisBlockungen = new RouteSchuleDatenaustauschUntisBlockungen();


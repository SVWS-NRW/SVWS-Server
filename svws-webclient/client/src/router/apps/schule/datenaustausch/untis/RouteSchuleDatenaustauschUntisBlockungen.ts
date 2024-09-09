import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, DeveloperNotificationException, GostHalbjahr, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisBlockungenProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungenProps";
import { schulformenGymOb } from "~/router/RouteHelper";

const SSchuleDatenaustauschUntisBlockungen = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungen.vue");

export class RouteSchuleDatenaustauschUntisBlockungen extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(schulformenGymOb, [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis.blockungen", "blockungen/:abiturjahr(\\d+)?/:halbjahr([0-5])?/:idblockung(\\d+)?/:idergebnis(\\d+)?", SSchuleDatenaustauschUntisBlockungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Blockungen";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await routeSchuleDatenaustauschUntis.data.ladeAbiturjahrgaenge();
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array || to_params.idblockung instanceof Array || to_params.idergebnis instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr !== undefined) {
			const abiturjahrgang = routeSchuleDatenaustauschUntis.data.mapAbiturjahrgaenge.get(abiturjahr);
			if (abiturjahrgang !== undefined) {
				await routeSchuleDatenaustauschUntis.data.setAbiturjahrgang(abiturjahrgang);
				const halbjahr = !to_params.halbjahr ? undefined : parseInt(to_params.halbjahr);
				if (halbjahr !== undefined) {
					const hj = GostHalbjahr.fromID(halbjahr);
					if (hj !== null)
						await routeSchuleDatenaustauschUntis.data.setHalbjahr(hj);
					const idBlockung = !to_params.idblockung ? undefined : parseInt(to_params.idblockung);
					if (idBlockung !== undefined) {
						const blockung = routeSchuleDatenaustauschUntis.data.mapBlockungen.get(abiturjahr)?.get(halbjahr)?.get(idBlockung);
						if (blockung) {
							await routeSchuleDatenaustauschUntis.data.setBlockung(blockung);
							const idErgebnis = !to_params.idergebnis ? undefined : parseInt(to_params.idergebnis);
							if (idErgebnis) {
								const ergebnisse = routeSchuleDatenaustauschUntis.data.mapErgebnisse.get(idBlockung);
								if (ergebnisse !== undefined)
									for (const e of ergebnisse) {
										if (e.id === idErgebnis) {
											await routeSchuleDatenaustauschUntis.data.setErgebnis(e);
											break;
										}
									}
							} else if (routeSchuleDatenaustauschUntis.data.ergebnis)
								return this.getRoute({abiturjahr: to_params.abiturjahr, halbjahr: to_params.halbjahr, idblockung: to_params.idblockung, idergebnis: routeSchuleDatenaustauschUntis.data.ergebnis.id.toString()});
						}
					} else {
						if (routeSchuleDatenaustauschUntis.data.ergebnis && routeSchuleDatenaustauschUntis.data.blockung)
							return this.getRoute({abiturjahr: to_params.abiturjahr, halbjahr: to_params.halbjahr, idblockung: routeSchuleDatenaustauschUntis.data.blockung.id.toString(), idergebnis: routeSchuleDatenaustauschUntis.data.ergebnis.id.toString()});
					}
				}
			}
		}
	}

	public leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		return routeSchuleDatenaustauschUntis.data.cleanup();
	}

	public getRoute(to_params?: RouteParams) : RouteLocationRaw {
		const { abiturjahr, halbjahr, idblockung, idergebnis } = to_params || {};
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr, halbjahr, idblockung, idergebnis}};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisBlockungenProps {
		return {
			mapAbiturjahrgaenge: () => routeSchuleDatenaustauschUntis.data.mapAbiturjahrgaenge,
			abiturjahrgang: () => routeSchuleDatenaustauschUntis.data.abiturjahrgang,
			gotoAbiturjahrgang: routeSchuleDatenaustauschUntis.data.gotoAbiturjahrgang,
			halbjahr: () => routeSchuleDatenaustauschUntis.data.halbjahr,
			gotoHalbjahr: routeSchuleDatenaustauschUntis.data.gotoHalbjahr,
			listBlockungen: () => routeSchuleDatenaustauschUntis.data.listBlockungen,
			blockung: () => routeSchuleDatenaustauschUntis.data.blockung,
			gotoBlockung: routeSchuleDatenaustauschUntis.data.gotoBlockung,
			listErgebnisse: () => routeSchuleDatenaustauschUntis.data.listErgebnisse,
			ergebnis: () => routeSchuleDatenaustauschUntis.data.ergebnis,
			gotoErgebnis: routeSchuleDatenaustauschUntis.data.gotoErgebnis,
			exportUntisBlockungenZIP: routeSchuleDatenaustauschUntis.data.exportUntisBlockungenZIP,
		};
	}

}

export const routeSchuleDatenaustauschUntisBlockungen = new RouteSchuleDatenaustauschUntisBlockungen();


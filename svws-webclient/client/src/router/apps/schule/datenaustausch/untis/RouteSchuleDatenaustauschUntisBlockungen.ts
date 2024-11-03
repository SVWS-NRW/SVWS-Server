import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { DeveloperNotificationException} from "@core";
import { BenutzerKompetenz, GostHalbjahr, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisBlockungenProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungenProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SSchuleDatenaustauschUntisBlockungen = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisBlockungen.vue");

export class RouteSchuleDatenaustauschUntisBlockungen extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.blockungen", "blockungen/:abiturjahr(\\d+)?/:halbjahr([0-5])?/:idblockung(\\d+)?/:idergebnis(\\d+)?", SSchuleDatenaustauschUntisBlockungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Blockungen";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr, halbjahr, idblockung: idBlockung, idergebnis: idErgebnis } = RouteNode.getIntParams(to_params, ["abiturjahr", "halbjahr", "idblockung", "idergebnis"]);
			if (isEntering)
				await routeSchuleDatenaustauschUntis.data.ladeAbiturjahrgaenge();
			if (abiturjahr !== undefined) {
				const abiturjahrgang = routeSchuleDatenaustauschUntis.data.mapAbiturjahrgaenge.get(abiturjahr);
				if (abiturjahrgang !== undefined) {
					await routeSchuleDatenaustauschUntis.data.setAbiturjahrgang(abiturjahrgang);
					if (halbjahr !== undefined) {
						const hj = GostHalbjahr.fromID(halbjahr);
						if (hj !== null)
							await routeSchuleDatenaustauschUntis.data.setHalbjahr(hj);
						if (idBlockung !== undefined) {
							const blockung = routeSchuleDatenaustauschUntis.data.mapBlockungen.get(abiturjahr)?.get(halbjahr)?.get(idBlockung);
							if (blockung) {
								await routeSchuleDatenaustauschUntis.data.setBlockung(blockung);
								if (idErgebnis !== undefined) {
									const ergebnisse = routeSchuleDatenaustauschUntis.data.mapErgebnisse.get(idBlockung);
									if (ergebnisse !== undefined)
										for (const e of ergebnisse)
											if (e.id === idErgebnis) {
												await routeSchuleDatenaustauschUntis.data.setErgebnis(e);
												break;
											}
								} else if (routeSchuleDatenaustauschUntis.data.ergebnis)
									return this.getRoute({ abiturjahr: to_params.abiturjahr, halbjahr: to_params.halbjahr, idblockung: to_params.idblockung, idergebnis: routeSchuleDatenaustauschUntis.data.ergebnis.id.toString() });
							}
						} else {
							if (routeSchuleDatenaustauschUntis.data.ergebnis && routeSchuleDatenaustauschUntis.data.blockung)
								return this.getRoute({ abiturjahr: to_params.abiturjahr, halbjahr: to_params.halbjahr, idblockung: routeSchuleDatenaustauschUntis.data.blockung.id.toString(), idergebnis: routeSchuleDatenaustauschUntis.data.ergebnis.id.toString() });
						}
					}
				}
			}
		} catch(e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		return routeSchuleDatenaustauschUntis.data.cleanup();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		const abiturjahr = routeSchuleDatenaustauschUntis.data.abiturjahrgang?.abiturjahr ?? -1;
		const halbjahr = routeSchuleDatenaustauschUntis.data.halbjahr?.id ?? undefined;
		const idblockung = routeSchuleDatenaustauschUntis.data.blockung?.id ?? undefined;
		const idergebnis = routeSchuleDatenaustauschUntis.data.ergebnis?.id ?? undefined;
		return { abiturjahr, halbjahr, idblockung, idergebnis };
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


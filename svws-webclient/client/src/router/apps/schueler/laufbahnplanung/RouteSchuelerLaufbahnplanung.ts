import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { schulformenGymOb } from "~/router/RouteHelper";
import { gostLaufbahnplanungStateImpl } from "~/states/GostLaufbahnplanungStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { SchuelerLaufbahnplanungProps } from "@ui/components/gost/laufbahnplanung/SSchuelerLaufbahnplanungProps";
import { ConfigElement } from "@ui/utils/Config";

const SSchuelerLaufbahnplanung = () => import("@ui/components/gost/laufbahnplanung/SSchuelerLaufbahnplanung.vue");

export class RouteSchuelerLaufbahnplanung extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "schueler.laufbahnplanung", "laufbahnplanung", SSchuelerLaufbahnplanung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		};
		configStateImpl.config.addElements([
			new ConfigElement("app.gost.belegpruefungsart", "user", "gesamt"),
			new ConfigElement("app.schueler.laufbahnplanung.modus", "user", "normal"),
			new ConfigElement("app.schueler.laufbahnplanung.faecher.anzeigen", "user", "alle"),
		]);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { id } = (params === undefined) ? { id: undefined } : RouteNode.getIntParams(params, ["id"]);
			if (!routeSchueler.data.manager.hasDaten()) {
				return false;
			}
			const abiturjahr = routeSchueler.data.manager.auswahl().abiturjahrgang;
			if (((abiturjahr !== null) && routeSchueler.data.manager.abiturjahrgaenge.get(abiturjahr))
				&& (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
					|| (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
						&& benutzerStateImpl.kompetenzenAbiturjahrgaenge.has(abiturjahr)))) {
				return false;
			}
			return routeSchueler.getRouteDefaultChild({ id });
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering) {
				// Wenn man in die Laufbahnplanung wechselt und von einer Gost-Route per Schülerlink kommt, dann im Filter direkt den Jahrgang wählen
				if ((from !== undefined) && from.checkSuccessorOf('gost') !== false) {
					for (const e of routeSchueler.data.manager.jahrgaenge.list()) {
						if (e.id === routeSchueler.data.manager.auswahl().idJahrgang) {
							routeSchueler.data.manager.jahrgaenge.auswahlAdd(e);
							await routeSchueler.data.setFilter();
							break;
						}
					}
				}
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id === undefined) {
				await gostLaufbahnplanungStateImpl.ladeSchuelerDaten(null);
				return;
			}
			try {
				await gostLaufbahnplanungStateImpl.ladeSchuelerDaten(routeSchueler.data.manager.liste.get(id));
			} catch {
				return routeSchueler.getRoute({ id });
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leaveBefore(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await gostLaufbahnplanungStateImpl.clear();
	}

	public getProps(to: RouteLocationNormalized): SchuelerLaufbahnplanungProps {
		return { };
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

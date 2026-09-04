import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";
import { routeGostFaecher } from "~/router/apps/gost/faecher/RouteGostFaecher";
import { routeGostBeratung } from "~/router/apps/gost/beratung/RouteGostBeratung";
import { routeGostKlausurplanung } from "~/router/apps/gost/klausuren/RouteGostKlausurplanung";
import { routeGostKursplanung } from "~/router/apps/gost/kursplanung/RouteGostKursplanung";
import { routeGostLaufbahnfehler } from "~/router/apps/gost/laufbahnfehler/RouteGostLaufbahnfehler";
import { routeGostAbitur } from "~/router/apps/gost/abitur/RouteGostAbitur";
import { RouteDataGost } from "~/router/apps/gost/RouteDataGost";
import type { GostAppProps } from "~/components/gost/SGostAppProps";
import type { GostAuswahlProps } from "~/components/gost/SGostAuswahlProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";
import { routeGostAbiturjahrNeu } from "./RouteGostAbiturjahrNeu";
import { routeGostGruppenprozesse } from "./RouteGostGruppenprozesse";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import type { TabData } from "@ui/ui/nav/TabData";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue");
const SGostApp = () => import("~/components/gost/SGostApp.vue");

export class RouteGost extends RouteNode<RouteDataGost, RouteApp> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
		], "gost", String.raw`gost/:abiturjahr(-?\d+)?`, SGostApp, new RouteDataGost());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Oberstufe";
		super.setView("liste", SGostAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeGostFaecher,
			routeGostBeratung,
			routeGostLaufbahnfehler,
			routeGostFachwahlen,
			routeGostKursplanung,
			routeGostKlausurplanung,
			routeGostAbitur,
			routeGostAbiturjahrNeu,
			routeGostGruppenprozesse,
		];
		super.defaultChild = routeGostFaecher;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-graduation-cap-line";
		configStateImpl.config.addElements([
			new ConfigElement("gost.auswahl.filterNurAktuelle", "user", "true"),
			new ConfigElement("gost.auswahl.abiturjahr", "user", "-1"),
			new ConfigElement("gost.tab.selected", "user", ""),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering || (this.data.idSchuljahresabschnitt !== abschnittStateImpl.auswahl.id)) {
				if (isEntering && (to === this) && (this.data.oldView !== undefined)) {
					return this.data.oldView.getRoute(to_params);
				}
				await this.data.setSchuljahresabschnitt(abschnittStateImpl.auswahl.id);
			}
			if (to === this) {
				return this.getRouteDefaultChild();
			}
			let cur: RouteNode<any, any> = to;
			while (cur.parent !== this) {
				cur = cur.parent;
			}
			if (cur !== this.data.view) {
				this.data.setView(cur, this.children);
			}
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (abiturjahr === undefined) {
				if ((this.selectedChild === undefined) || (this.selectedChild.hidden({ abiturjahr: "-1" }) !== false)) {
					return this.getRouteDefaultChild();
				}
				return this.getRouteSelectedChild();
			}
			let eintrag = this.data.mapAbiturjahrgaenge.get(abiturjahr);
			if ((eintrag === undefined) && (this.data.mapAbiturjahrgaenge.size > 0)) {
				eintrag = this.data.mapAbiturjahrgaenge.get(-1);
			}
			await this.data.setAbiturjahrgang(eintrag, isEntering);
			if (this.name !== to.name) {
				return;
			}
			const redirect: RouteNode<any, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
			if (redirect.hidden({ abiturjahr: abiturjahr.toString() }) !== false) {
				return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, abiturjahr } };
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
		routeGost.data.oldView = from;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { abiturjahr: this.data.abiturjahrFromConfig };
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			jahrgangsdaten: () => this.data.auswahl === undefined ? undefined : this.data.jahrgangsdaten,
			mapAbiturjahrgaenge: () => this.data.mapAbiturjahrgaenge,
			mapJahrgaengeOhneAbiJahrgang: () => this.data.mapJahrgaengeOhneAbiJahrgang,
			apiStatus: api.status,
			addAbiturjahrgang: this.data.addAbiturjahrgang,
			gotoAbiturjahrgang: this.data.gotoAbiturjahrgang,
			getAbiturjahrFuerJahrgang: this.data.getAbiturjahrFuerJahrgang,
			filterNurAktuelle: () => this.data.filterNurAktuelle,
			setFilterNurAktuelle: this.data.setFilterNurAktuelle,
			gotoCreationMode: this.data.gotoCreationMode,
			gotoGruppenprozess: this.data.gotoGruppenprozess,
			selected: () => this.data.selected,
			setSelected: this.data.setSelected,
		};
	}

	public getProps(to: RouteLocationNormalized): GostAppProps {
		return {
			auswahl: this.data.auswahl,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab, this.getType()),
			creationModeEnabled: this.data.creationModeEnabled,
			gruppenprozesseEnabled: this.data.gruppenprozesseEnabled,
			selected: () => this.data.selected,
		};
	}

	private getType(): ViewType {
		if (this.data.gruppenprozesseEnabled) {
			return ViewType.GRUPPENPROZESSE;
		}
		if (this.data.creationModeEnabled) {
			return ViewType.HINZUFUEGEN;
		}
		return ViewType.DEFAULT;
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) {
			return;
		}
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		const previousTab = this.data.selectedTabFromConfig;
		if (value.name !== previousTab.name) {
			await this.data.setSelectedTabToConfig(value);
		}
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, this.children);
	};

}

export const routeGost = new RouteGost();

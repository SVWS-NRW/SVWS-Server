import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteDataGostKlausurplanung } from "./klausurplanung/RouteDataGostKlausurplanung";
import { routeGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { BenutzerKompetenz, GostHalbjahr, Schulform } from "@svws-nrw/svws-core-ts";
import { routeApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { GostKlausurplanungAuswahlChildData, GostKlausurplanungAuswahlProps } from "~/components/gost/klausurplanung/SGostKursplanungAuswahlProps";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");
const SGostKlausurplanungAuswahl = () => import("~/components/gost/klausurplanung/SGostKlausurplanungAuswahl.vue");

export class RouteGostKlausurplanung extends RouteNode<RouteDataGostKlausurplanung, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung", "klausurplanung/:halbjahr([0-5])?", SGostKlausurplanung, new RouteDataGostKlausurplanung());
		super.propHandler = (route) => this.getNoProps(route);
		super.setView("gost_child_auswahl", SGostKlausurplanungAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Klausurplanung";
		super.children = [
			routeGostKlausurplanungKlausurdaten,
			routeGostKlausurplanungSchienen,
			routeGostKlausurplanungKalender,
			routeGostKlausurplanungPlanung,
			routeGostKlausurplanungKonflikte
		];
		super.defaultChild = routeGostKlausurplanungKlausurdaten;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr as string);
		if ((abiturjahr === undefined) || (routeGost.data.auswahl.value !== undefined) && (abiturjahr !== routeGost.data.auswahl.value.abiturjahr))
			return routeGost.defaultChild!.getRoute(-1);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe die Parameter zunächst allgemein
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		const halbjahr = !to_params.halbjahr ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		// Prüfe das Abiturjahr
		if (abiturjahr === undefined)
			throw new Error("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
		await this.data.setAbiturjahr(abiturjahr);
		// Aktualisiere das Halbjahr
		if (halbjahr === undefined) {
			let hj = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt);
			if (hj === null) // In zwei Fällen existiert Halbjahr, z.B. weil der Abiturjahrgang abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
			return this.getRoute(abiturjahr, hj.id);
		}
		await this.data.setHalbjahr(halbjahr);
		const changedHalbjahr: boolean = await this.data.setHalbjahr(halbjahr);
		if ((changedHalbjahr) || (to.name === this.name))
			return this.data.view.getRoute(abiturjahr, halbjahr.id);
	}

	public getRoute(abiturjahr: number, halbjahr?: number) : RouteLocationRaw {
		if (halbjahr === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostKlausurplanungAuswahlProps {
		return {
			gotoHalbjahr: this.data.gotoHalbjahr,
			halbjahr: this.data.halbjahr,
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
		}
	}

	private getChild(): GostKlausurplanungAuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): GostKlausurplanungAuswahlChildData[] {
		const result: GostKlausurplanungAuswahlChildData[] = [];
		for (const c of this.children)
			result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: GostKlausurplanungAuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { abiturjahr: this.data.abiturjahr, halbjahr: this.data.halbjahr.id } });
		await this.data.setView(node);
	}

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();

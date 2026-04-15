import type { EnmAnkreuzkompetenzenAuswahlProps } from "@ui/components/enm/EnmAnkreuzkompetenzenAuswahlProps";
import type { EnmAnkreuzkompetenzenProps } from "@ui/components/enm/EnmAnkreuzkompetenzenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import EnmAnkreuzkompetenzen from "@ui/components/enm/EnmAnkreuzkompetenzen.vue";
import { RouteNode } from "../RouteNode";
import { routeApp, type RouteApp } from "./RouteApp";
import { ServerMode } from "@core/core/types/ServerMode";
import EnmAnkreuzkompetenzenAuswahl from "@ui/components/enm/EnmAnkreuzkompetenzenAuswahl.vue";


export class RouteAnkreuzkompetenzen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), "ankreuzkompetenzen", "ankreuzkompetenzen", EnmAnkreuzkompetenzen);
		super.mode = ServerMode.STABLE;
		super.propHandler = () => this.getProps();
		super.text = "Ankreuzkompetenzen";
		super.setView("liste", EnmAnkreuzkompetenzenAuswahl, () => this.getAuswahlProps());
		this.isHidden = () => routeApp.data.manager.setKlassenMitAnkreuzkompetenzen.isEmpty() ? routeApp.getRouteDefaultChild() : false;
	}

	public getProps(): EnmAnkreuzkompetenzenProps {
		return {
			enmManager: () => routeApp.data.manager,
			auswahl: () => routeApp.data.auswahlKlassen,
			patchLeistung: routeApp.data.patchLeistung,
			patchAnkreuzkompetenz: routeApp.data.patchAnkreuzkompetenz,
		};
	}

	public getAuswahlProps(): EnmAnkreuzkompetenzenAuswahlProps {
		return {
			enmManager: () => routeApp.data.manager,
			setAuswahlMehrfach: routeApp.data.setAuswahlKlassen,
			auswahlMehrfach: () => routeApp.data.auswahlKlassenNurMehrfachauswahl,
			setAuswahlEinzel: routeApp.data.setAuswahlKlasse,
			auswahlEinzel: () => routeApp.data.auswahlKlasse,
		};
	}

}

export const routeAnkreuzkompetenzen = new RouteAnkreuzkompetenzen();

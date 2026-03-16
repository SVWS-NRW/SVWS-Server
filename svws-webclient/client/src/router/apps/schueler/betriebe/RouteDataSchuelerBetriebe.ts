import type { Betrieb } from "@core";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

interface RouteStateDataSchuelerAusbildungsbetriebe extends RouteStateInterface {
	daten: Betrieb | undefined;
}

const defaultState = <RouteStateDataSchuelerAusbildungsbetriebe> {
	daten: undefined,
};

export class RouteDataSchuelerBetriebe extends RouteData<RouteStateDataSchuelerAusbildungsbetriebe> {

	public constructor() {
		super(defaultState);
	}

}


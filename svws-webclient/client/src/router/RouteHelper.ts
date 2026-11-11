import type { RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import type { GenericAuswahlNavigator } from "~/states/GenericAuswahlNavigator";

export const schulformenGymOb = [Schulform.FW, Schulform.GE, Schulform.GY, Schulform.SG, Schulform.WF] as const;

/**
 * Die Parameter für die Aktualisierung einer Route mit einer Auswahlliste
 */
export interface GenericAuswahlRouteUpdateParams {

	/** Die Route mit der Auswahlliste */
	route: RouteNode<any, any>;

	/** Der Auswahl-State der Route */
	state: { init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null>, get navigator(): GenericAuswahlNavigator };

	/** Die Ziel-Route */
	to: RouteNode<any, any>;

	/** Die ID des Schuljahresabschnittes und die ID der Auswahl aus den Parametern der Ziel-Route */
	ids: { idSchuljahresabschnitt: number | undefined, id: number | undefined };

	/** Gibt an, ob die Route betreten wird */
	isEntering: boolean;

	/** Gibt an, ob bei einer anderen ID als der gewählten nur dann zur Default-Ansicht umgeleitet werden soll, wenn keine ID in der URL steht */
	redirectOnlyWithoutId?: boolean;

}

/**
 * Bestimmt die Auswahl für eine Route mit Auswahlliste: Der Auswahl-State wird für den Schuljahresabschnitt aus den
 * Routen-Parametern initialisiert, es werden ggf. Umleitungen ermittelt und danach wird über den Navigator des States die
 * Ansicht zu der Ziel-Route (Default, Gruppenprozesse, Hinzufügen oder Schnelleingabe) gewechselt.
 *
 * @param params   die Parameter für die Aktualisierung
 *
 * @returns die Route, zu der umgeleitet werden soll, oder undefined, wenn keine Umleitung nötig ist
 */
export async function updateAuswahlRoute(params: GenericAuswahlRouteUpdateParams): Promise<RouteLocationRaw | undefined> {
	const { route, state, to, isEntering } = params;
	const { idSchuljahresabschnitt, id: paramId } = params.ids;
	if (idSchuljahresabschnitt === undefined) {
		throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
	}
	let id = paramId;
	if ((paramId === undefined) && isEntering) {
		const lastId = Number.parseInt(configStateImpl.nonPersistentConfig.getValue(`${route.name}.auswahl.id`));
		if (!Number.isNaN(lastId)) {
			id = lastId;
		}
	}
	if (isEntering && to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN, ViewType.NEU])) {
		return route.getRouteView(route.data.view, { id: id ?? '' });
	}

	// Daten zum ausgewählten Schuljahresabschnitt laden
	const idNeu = await state.init(idSchuljahresabschnitt, isEntering);
	if ((idNeu !== null) && (idNeu !== id) && ((params.redirectOnlyWithoutId !== true) || (id === undefined))) {
		return route.data.defaultView.getRoute({ id: idNeu });
	}

	// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
	if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
		return route.getRouteView(to, { id: '' });
	}

	if (to.hasType(ViewType.GRUPPENPROZESSE)) {
		await state.navigator.gotoGruppenprozesse(false);
	} else if (to.hasType(ViewType.HINZUFUEGEN)) {
		await state.navigator.gotoHinzufuegen(false);
	} else if (to.hasType(ViewType.NEU)) {
		await state.navigator.gotoSchnelleingabe(false);
	} else {
		await state.navigator.select(id);
	}
	return undefined;
}

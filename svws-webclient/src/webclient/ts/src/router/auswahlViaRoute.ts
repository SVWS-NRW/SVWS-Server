import { computed, watch, WritableComputedRef } from "vue";
import { useRouterHelpers } from "./routeHelpers";
import { injectMainApp } from "../apps/Main";

import type { RouteNames, RouteItemTypesMap } from "./types";
import { GostJahrgang } from "@svws-nrw/svws-core-ts";

// FIXME: Bug in ui table component
/**
 *	NOTES:
 *	die table component des ui packages hat aktuell noch einen Bug: auch wenn wir über v-model:selected initial ein eitem übergeben, wird trotzdem
 *  das erste Item im Table selektiert, und diese selektion über ein event wieder emitiert. Daher springt die URL aktuelle bei einem reload der page immer
 *  wieder auf die id des ersten items im table.
 *
 *  Das wird nur dadurch behoben dass der Bug in der Table component behoben wird.
 */

export function useAuswahlViaRoute<
	T extends RouteNames,
	Item extends RouteItemTypesMap[T]
>(routeName: T): WritableComputedRef<Item | undefined> {
	const { route, navigateTo } = useRouterHelpers();
	const app = injectMainApp().apps[routeName];

	/**
	 * Sucht das zur ID passende item aus der jeweiligen app und setzt es als "ausgewählt"
	 */
	const updateAuswahl = (id: string) => {
		let item
		for (const i of app.auswahl.liste) {
			console.log(id, item)
			if ((i instanceof GostJahrgang) 
				? i.abiturjahr?.valueOf() === +id
				: i.id === +id) {
					item = i;
					break
				}
		};
		// console.log(`route id update for ${routeName}-${id}:`, item);
		app.auswahl.ausgewaehlt = item;
	};

	watch(
		() => route.params.id,
		id => {
			if (route.name === routeName && id) {
				updateAuswahl(id as string);
			}
		}
	);

	// da wir gerade mounten, können wir die auswahl hier direkt setzen
	route.params.id && updateAuswahl(route.params.id as string);

	// Diese computed property erlaubt es, per z.B. v-model das gerade ausgewählte item der app zu lesen und ändern.
	// beim setzen der neuen Auswahl gehen wir aber über den router, denn die URL ist unsere "source of truth".
	// die neue Auswahl wird dann vom code weiter oben aus der route gelesen und in der app gesetzt.
	const selected = computed({
		get() {
			return app.auswahl.ausgewaehlt as Item;
		},
		set(item: Item | undefined) {
			item && navigateTo(routeName, item);
		}
	});

	return selected;
}

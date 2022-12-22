import { computed, ComputedRef, Ref, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteMeta, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";

/**
 * Dieses Interface gibt an, welche Informationen bei Routen-Einträgen
 * im SVWS-Client als Meta-Informationen abgelegt werden.
 */
export interface RouteAppMeta<ItemAuswahl, RouteData> extends RouteMeta {

	/** Eine Funktion, welche eine Computed-Property zurückliefert, die für die Auswahl eines Elements aus einer Liste von Elementen verantwirtlich ist. */
	auswahl: (routename? : string) => WritableComputedRef<ItemAuswahl>;

	/** Eine Funktion, welche die Sichtbarkeit der Route für die Anwendung prüft */
	hidden: () => boolean;

	/** Die Daten, die einer Route zugeordnet sind */
	data: RouteData;

	/** Ein Pfad, welcher als Suffix der Route angehangen wird, falls ein automatischer redirect erfolgt (z.B. bei Tabs nach einer Auswahl) */
	redirect: Ref<RouteRecordRaw>;

	/** Ein Text, welcher zur Darstellung in der GUI genutzt wird (z.B. der Text auf Tabs) */
	text: string;
}


/**
 * Eine interne Hilfsmethode, um Typ-sicher auf die Meta-Informationen von Routen-Einträgen zuzugreifen.
 * 
 * @param route   der Routen-Eintrag, bei der auf die Meta-Informationen zugegriffen werden soll
 * 
 * @returns Die Meta-Information des Routen-Eintrags
 */
export function routeAppMeta<T extends RouteRecordRaw, ItemAuswahl, RouteData>(route : T) : RouteAppMeta<ItemAuswahl, RouteData> {
	if (route.meta === undefined)
		throw new Error("Meta-Informationen für die Route '" + route.name?.toString() + "' nicht definiert.");
	return route.meta as RouteAppMeta<ItemAuswahl, RouteData>;
}

/**
 * Methode zum Erzeugen der Computed-Property für die Auswahl eines Routen-Eintrags.
 * 
 * @param route       der Routen-Eintrag, für den die Computed-Property erzeugt werden soll
 * @param routename   der Name der Route, die zuvor aktiviert war
 * 
 * @returns die Computed-Property
 */
export function routeAppAuswahl<T extends RouteRecordRaw, ItemAuswahl, RouteData>(route : T, routename? : string) : WritableComputedRef<ItemAuswahl> {
	return routeAppMeta<T, ItemAuswahl, RouteData>(route).auswahl(routename);
}

/**
 * Eine interne Hilfsmethode, um Typ-sicher auf die Daten von Routen-Einträgen zuzugreifen.
 * 
 * @param route   der Routen-Eintrag, bei der auf die Meta-Informationen zugegriffen werden soll
 * 
 * @returns Die Daten des Routen-Eintrags
 */
export function routeAppData<T extends RouteRecordRaw, ItemAuswahl, RouteData>(route : T) : RouteData {
	if (route.meta === undefined)
		throw new Error("Meta-Informationen für die Route '" + route.name?.toString() + "' nicht definiert.");
	return route.meta.data as RouteData;
}


/**
 * Prüft für die übergeben Route, ob sie versteckt oder sichtbar ist.
 * Dafür wird die hidden-Methode bei den Meta-Informationen der Router aufgerufen,
 * falls sie definiert ist. Ist keine Methode definiert, so ist die Router sichtbar.
 * 
 * @param route   die Route
 * 
 * @returns true, falls die Route versteckt sein soll
 */
export function routeAppIsHidden(route : RouteRecordRaw): boolean {
	if (route.meta === undefined)
		return false;
	if ((route.meta.hidden === undefined) || (typeof route.meta.hidden !== "function"))
		return false;
	return route.meta.hidden();
}


/**
 * Prüft für das übergebene Array von Routen, ob sie versteckt oder sichtbar sind.
 * Dafür wird jeweils die hidden-Methode bei den Meta-Informationen der Router
 * aufgerufen, falls sie definiert ist. Ist diese nicht definiert, so ist
 * die jeweilige Route sichtbar.
 * 
 * @param route   die Route
 * 
 * @returns ein Array mit der Länge der Eingabe und jeweils true, falls die entspechende
 *          Route des übergebenen Arrays versteckt sein soll
 */
export function routeAppAreHidden(routes : RouteRecordRaw[]): boolean[] {
	return routes.map(r => routeAppIsHidden(r));
}


/**
 * Eine Default-Methode zum Erzeugen der Properties "id" und "item" zu einer Route bei 
 * "normalen" Auswahl-Listen, welche ein numerisches ID-Attribut haben.
 * 
 * Dies ist eine Hilfsmethode für die Nutzung beim Definieren von Routen-Einträgen.
 * 
 * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
 * @param name      der Name des Routen-Eintrages, für welche die Properties erzeugt werden sollen.
 *                  Dieser wird genutzt, um zu prüfen, ob die übergebene Route zu dem Routen-Eintrag passt
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns das Objekt mit den Werten für die Properties
 */
export function routePropsAuswahlID<TAuswahl extends BaseList<{ id: number }, unknown>>(route: RouteLocationNormalized, auswahl: TAuswahl) {
	if ((auswahl === undefined) || (route.params.id === undefined))
		return { id: undefined, item: undefined, routename: route.name?.toString() };
	const id = parseInt(route.params.id as string);
	const item = auswahl.liste.find(s => s.id === id);
	return { id: id, item: item, routename: route.name?.toString() };
}


/**
 * Eine Default-Methode für Routen-Einträge zum Erzeugen der Computed-Property bei der
 * Auswahl einer Route eines Routen-Eintrags.
 * 
 * Dies ist eine Hilfsmethode für die Nutzung beim Definieren von Routen-Einträgen.
 * 
 * @param name      der Name des Routen-Eintrags, so dass dieser beim Setzen einer Route aktualisiert werden kann
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns die Computed-Property
 */
export function routeAuswahlID<TItem extends { id: number }, TAuswahl extends BaseList<TItem, unknown>>(name: string, auswahl: TAuswahl) : WritableComputedRef<TItem | undefined> {
	const router = useRouter();
	const route = useRoute();

	const selected = computed({
		get(): TItem | undefined {
			if (route.params.id === undefined)
				return undefined;
			let tmp = auswahl.ausgewaehlt;
			if ((tmp === undefined) || (tmp.id.toString() !== route.params.id))
				tmp = auswahl.liste.find(s => s.id.toString() === route.params.id);
			return tmp;
		},
		set(value: TItem | undefined) {
			auswahl.ausgewaehlt = value;
			router.push({ name: name, params: { id: value?.id } });
		}
	});
	return selected;
}



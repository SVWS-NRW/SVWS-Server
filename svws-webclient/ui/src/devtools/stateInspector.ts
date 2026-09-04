import type { JavaIterator } from '@core/java/util/JavaIterator';
import { setupDevtoolsPlugin } from '@vue/devtools-api';
import type { App, ShallowRef } from 'vue';
import { watch } from 'vue';

const INSPECTOR_ID = 'svws-state-inspector';

// Der Typ der API-Instanz wird aus den Parametern des Callbacks extrahiert...
type DevToolsApiInstance = Parameters<Parameters<typeof setupDevtoolsPlugin>[1]>[0];

/** Struktur eines SVWS-State-Objekts mit internem reaktiven Zustand */
interface SVWSState {
	_state: ShallowRef<any>;
	[key: string]: any;
}

/** Zentrale Registry für alle ermittelten States im vue-Kontext */
const stateRegistry = new Map<string, Record<string, SVWSState>>();

/** Map zur Verwaltung aktiver Watcher für die States */
const activeStateWatchers = new Map<string, () => void>();

/** Globale Referenz auf die DevTools-API */
let stateDevtoolsApi: DevToolsApiInstance | null = null;


interface VueAppInternal {
	_context?: {
		provides?: Record<string | symbol, unknown>;
	};
}

/**
 * Ermittelt alle States der Applikation aus den provides. Dabei werden Framework-Interne Keys herausgefiltert.
 *
 * @param app   die Applikation an welches das Modul gebunden wird und von welcher die States ermittelt werden sollen
 *
 * @returns die gefilterten provide-Objekte zugeordnet zu ihren Namen
 */
function getFilteredStates(app: App): Record<string, unknown> {
	const internalApp = app as unknown as VueAppInternal;
	const provides = internalApp._context?.provides;
	const filtered: Record<string, unknown> = {};
	if (provides === undefined) {
		return filtered;
	}
	for (const key of Reflect.ownKeys(provides)) {
		const value = provides[key];
		const name = (typeof key === 'symbol') ? (key.description ?? key.toString()) : key;

		// Prüfe, ob es sich um eine Key handelt, der mit State endet
		const isStateKey = name.endsWith('State');

		// Prüfe, ob der Konstruktorname mit StateImpl endet
		const hasStateImplConstructor = (value !== undefined) && (value !== null)
			&& (typeof value === 'object')
			&& (typeof value.constructor.name === 'string')
			&& value.constructor.name.endsWith('StateImpl');

		// Lasse die provided States durch den Filter
		if (isStateKey || hasStateImplConstructor) {
			filtered[name] = value;
		}
	}
	return filtered;
}

/**
 * Wandelt eine Java-Liste rekursiv in ein JavaScript-Array um.
 *
 * @param javaList   die Java-Liste
 * @param visited    ein Set um festzustellen, wenn ein Knoten bereits besucht wurde - nötig zur Schleifenvermeidung
 *
 * @returns das JavaScript-Array mit den verarbeiteten Unterelementen
 */
function convertJavaListToArray(javaList: any, visited: WeakSet<object>): any[] {
	const listAsArray: any[] = [];
	try {
		const iterator = javaList.iterator() as JavaIterator<any> | null | undefined;
		if ((iterator !== null) && (iterator !== undefined) && (typeof iterator.hasNext === 'function')
			&& (typeof iterator.next === 'function')) {
			while (iterator.hasNext() === true) {
				listAsArray.push(prepareData(iterator.next(), visited));
			}
		}
	} catch {
		// Fallback bei Fehlern während der Iteration
	}
	return listAsArray;
}

/**
 * Bereitet die Datenstrukturen der States rekursiv für die Darstellung vor.
 * Es werden Funktionen aus der Objekten ausgelassen und die Anzeige von
 * Java-Datenstrukturen vorbereitet. Außerdem werden Objekttypen für die typisierte Anzeige
 * ermittelt.
 *
 * @param value     der Wert, welcher für die Anzeige vorbereitet wird
 * @param visited   ein Set um festzustellen, wenn ein Knoten bereits besucht wurde - nötig zur Schleifenvermeidung
 *
 * @returns die vorbereiteten Daten für die Anzeige
 */
function prepareData(value: unknown, visited = new WeakSet<object>()): unknown {
	if ((value === null) || (value === undefined)) {
		return value;
	}

	// Vermeide Schleifen und beende die Traveriserung an diesem Zweig
	if (typeof value === 'object') {
		if (visited.has(value)) {
			// Gebe in den DevTools eine Rückmeldung, dass ein Zyklus vorliegt und daher die Darstellung abgebrochen wird
			return {
				_custom: {
					type: 'object',
					display: '↻ [Zirkuläre Referenz]',
					value: 'Bereits verarbeitet',
					readOnly: true,
				},
			};
		}
		visited.add(value);
	}

	// Falls es sich um eine reaktive Vue-Ref handelt, packen wir den inneren Wert aus
	if ((typeof value === 'object') && ('value' in value) && (((value as any).__v_isRef === true) || (typeof (value as any).effect === 'object'))) {
		return prepareData(value.value, visited);
	}

	// Prüfe, ob es sich um eine Java-Liste mit iterator handelt
	if (typeof (value as any).iterator === 'function') {
		return convertJavaListToArray(value, visited);
	}

	// Bei einem Array werden die einzelnen Elemente vorbereitet...
	if (Array.isArray(value)) {
		return value.map(item => prepareData(item, visited));
	}

	// Bei Objekten werden die einzelnen Attribute vorbereitet...
	if (typeof value === 'object') {
		if (value instanceof Date) {
			return value;
		}

		const obj = value as Record<string, unknown>;
		if ((obj.elementData !== undefined) && Array.isArray(obj.elementData)) {
			let itemType = 'Unknown';
			if (obj.elementData.length === 0) {
				itemType = 'Empty';
			} else {
				const firstItem = obj.elementData[0];
				if ((firstItem !== undefined) && (firstItem !== null)) {
					if (typeof firstItem === 'object') {
						const cName = firstItem.constructor?.name;
						itemType = ((typeof cName === 'string') && (cName !== '') && (cName !== 'Object')) ? cName : 'Object';
					} else {
						const tName = typeof firstItem;
						itemType = tName.charAt(0).toUpperCase() + tName.slice(1);
					}
				}
			}

			const cNameParent = value.constructor.name;
			const collectionName = ((typeof cNameParent === 'string') && (cNameParent !== '') && (cNameParent !== 'Object')) ? cNameParent : 'List';
			return {
				_custom: {
					type: 'object',
					display: `${collectionName}⟨${itemType}⟩[${obj.elementData.length}]`,
					value: obj.elementData.map(item => prepareData(item, visited)),
					readOnly: true,
				},
			};
		}

		const cleanedObj: Record<string, unknown> = {};
		for (const key of Object.keys(value)) {
			const propValue = (value as Record<string, unknown>)[key];
			if ((typeof propValue === 'function') || key.startsWith('_')) {
				continue;
			}
			cleanedObj[key] = prepareData(propValue, visited);
		}

		const constructorName = value.constructor.name;
		if ((typeof constructorName === 'string') && (constructorName !== '') && (constructorName !== 'Object')) {
			return {
				_custom: {
					type: 'object',
					display: constructorName,
					value: cleanedObj,
					readOnly: true,
				},
			};
		}

		return cleanedObj;
	}

	return value;
}


/** Timer-Referenz für das Debouncing der DevTools-Updates */
let stateUpdateTimer: ReturnType<typeof setTimeout> | null = null;

/**
 * Fordert eine Aktualisierung des State-Inspector-Baums an. Nutze Debouncing, um bei massenhaften
 * Lifecycle-Events (z.B. Routing) die Performance zu schonen.
 */
function requestStateUpdate(): void {
	if (stateDevtoolsApi === null) {
		return;
	}

	// Breche einen ggf. noch laufenden Timer ab
	if (stateUpdateTimer !== null) {
		clearTimeout(stateUpdateTimer);
	}

	// Warte eine kurze Zeit bis der State aktualisiert wird. Falls in der Zwischenzeit weitere Anpassungen erfolgen
	// wird die Ansicht nicht unnötig oft aktualisiert.
	stateUpdateTimer = setTimeout(() => {
		stateDevtoolsApi?.sendInspectorTree(INSPECTOR_ID);
		stateDevtoolsApi?.sendInspectorState(INSPECTOR_ID);
		stateUpdateTimer = null;
	}, 150);
}


/**
 * Ermittelt die Attribute des States und gibt diese für die Anzeige in den DevTools
 * zurück.
 *
 * @param das State-Objekt
 *
 * @returns die Informationen für die Anzeige
 */
function retrieveStateAttributes(stateObj: Record<string, unknown>): Record<string, unknown>[] {
	const stateProps: Record<string, unknown>[] = [];

	const visited = new WeakSet<object>();
	visited.add(stateObj);

	// Prüfe, auf die reaktiven Daten und füge diese zu den Properties hinzu
	if (stateObj.data !== undefined) {
		const dataProp = stateObj.data as Record<string, unknown>;
		const isVueRef = dataProp.__v_isRef === true || ('value' in dataProp);
		const rawData = isVueRef ? dataProp.value : stateObj.data;

		stateProps.push({
			key: 'Daten (shallowRef.value)',
			value: prepareData(rawData, visited),
			editable: false,
			revive: true,
		});
	}

	const props = new Set<string>();
	Object.keys(stateObj).forEach(k => props.add(k));
	const proto = Object.getPrototypeOf(stateObj);
	if ((proto !== undefined) && (proto !== null) && (proto !== Object.prototype)) {
		Object.getOwnPropertyNames(proto).forEach(k => props.add(k));
	}

	for (const prop of Array.from(props)) {
		if ((prop === 'data') || (prop.startsWith('_')) || (prop === 'constructor')) {
			continue;
		}
		try {
			const val = stateObj[prop];
			if (typeof val === 'function') {
				continue;
			}

			let isGetter = false;
			const desc = Object.getOwnPropertyDescriptor(proto, prop);
			if ((desc !== undefined) && (typeof desc.get === 'function') && (typeof desc.set !== 'function')) {
				isGetter = true;
			}

			stateProps.push({
				key: prop,
				value: prepareData(val, visited),
				editable: !isGetter,
				revive: true,
			});
		} catch (e) {
			const errorMsg = (e instanceof Error) ? e.message : String(e);
			stateProps.push({
				key: prop,
				value: `Error: ${errorMsg}`,
				editable: false,
			});
		}
	}
	return stateProps;
}


/**
 * Ersetzt einen Wert tief innerhalb einer Objektstruktur anhand eines Pfad-Arrays.
 * Erzwingt durch eine Neu-Zuweisung auf der Root-Ebene das Feuern des Proxy-Handlers.
 *
 * @param rootObj   das reaktive Proxy-Objekt der obersten Ebene
 * @param path      der vollständige Mutationspfad aus den DevTools (z.B. ['adresse', 'strasse'])
 * @param value     der neu zu setzende Wert
 */
function setDeepValue(rootObj: any, path: string[], value: any): void {
	if (path.length === 0) {
		return;
	}

	const topKey = path[0];

	if (path.length === 1) {
		if ((rootObj[topKey] !== undefined) && (rootObj[topKey] !== null) && ((rootObj[topKey].__v_isRef === true) || ('value' in rootObj[topKey]))) {
			rootObj[topKey].value = value;
		} else {
			rootObj[topKey] = value;
		}
		return;
	}

	let current = rootObj[topKey];
	if ((current !== undefined) && (current !== null) && ((current.__v_isRef === true) || ('value' in current))) {
		current = current.value;
	}

	for (let i = 1; i < path.length - 1; i++) {
		current = current[path[i]];
		if ((current !== undefined) && (current !== null) && ((current.__v_isRef === true) || ('value' in current))) {
			current = current.value;
		}
		if ((current === null) || (current === undefined) || (typeof current !== 'object')) {
			return;
		}
	}

	const lastKey = path.at(-1);
	if (lastKey !== undefined) {
		if ((current[lastKey] !== undefined) && (current[lastKey] !== null) && ((current[lastKey].__v_isRef === true) || ('value' in current[lastKey]))) {
			current[lastKey].value = value;
		} else {
			current[lastKey] = value;
		}
	}

	const topValue: unknown = rootObj[topKey];
	if (Array.isArray(topValue)) {
		rootObj[topKey] = [...topValue];
	} else if ((topValue !== null) && (topValue !== undefined) && (typeof topValue === 'object') && (topValue.constructor.name === 'Object')) {
		rootObj[topKey] = { ...topValue };
	}
}


/**
 * Diese Methode wird von den vue-Dev-Tools getriggert, wenn der Baum des Inspektors aktualisiert werden soll.
 *
 * @param payload   die Daten für das Ereignis
 */
function onGetInspectorTree(payload: any): void {
	if (payload.inspectorId !== INSPECTOR_ID) {
		return;
	}
	payload.rootNodes = Array.from(stateRegistry.keys()).map(key => ({ id: key, label: key }));
}

/**
 * Diese Methode wird von den vue-Dev-Tools getriggert, wenn der allgemeine Zustand des Inspektors abgefragt wird.
 *
 * @param payload   die Daten für das Ereignis
 */
function onGetInspectorState(payload: any): void {
	if (payload.inspectorId !== INSPECTOR_ID) {
		return;
	}

	const stateObj = stateRegistry.get(payload.nodeId);
	if (!stateObj) {
		payload.state = {
			'Fehler': [{ key: 'Status', value: 'State-Instanz nicht mehr im Kontext verfügbar.', editable: false }],
		};
		return;
	}

	const className = ((typeof stateObj.constructor.name === 'string') && (stateObj.constructor.name !== ''))
		? stateObj.constructor.name : 'Unbekannt';
	payload.state = {
		'State': retrieveStateAttributes(stateObj) as any,
		'Metadaten': [{ key: 'Klasse', value: className, editable: false }],
	};

	if (!activeStateWatchers.has(payload.nodeId)) {
		const stopWatch = watch(
			() => stateObj._state.value,
			() => {
				requestStateUpdate();
			},
			{ deep: true, flush: 'post' }
		);
		activeStateWatchers.set(payload.nodeId, stopWatch);
	}
}


/**
 * Diese Methode wird von den vue-Dev-Tools getriggert, wenn dort Daten verändert werden.
 *
 * @param payload   die Daten für das Ereignis
 */
function onEditInspectorState(payload: any): void {
	if (payload.inspectorId !== INSPECTOR_ID) {
		return;
	}
	const entry = stateRegistry.get(payload.nodeId);
	if (!entry) {
		return;
	}
	setDeepValue(entry, payload.path, payload.state.value);
	stateDevtoolsApi?.sendInspectorState(INSPECTOR_ID);
}


/**
 * Registriert das SVWS-Modul für due Anzeige der States in den Vue-Dev-Tools
 *
 * @param app   die Applikation für welche die States angezeigt werden sollen
 */
export function registerSVWSDevTools(app: App): void {
	if (process.env.NODE_ENV !== 'development') {
		return;
	}

	setupDevtoolsPlugin({
		id: 'de.svws-nrw.webclient.inspector',
		label: 'SVWS State Inspector',
		packageName: 'svws-webclient',
		homepage: 'https://github.com/SVWS-NRW/SVWS-Server',
		app,
	}, (api) => {
		console.info("SVWS State Inspector wird geladen...");
		stateDevtoolsApi = api;
		api.addInspector({
			id: INSPECTOR_ID,
			label: 'SVWS States',
			icon: 'storage',
		});

		// Synchronisiere initial die States in die Registry
		const initialStates = getFilteredStates(app);
		for (const [key, value] of Object.entries(initialStates)) {
			if ((value !== undefined) && (value !== null) && (typeof value === 'object') && ('_state' in value)) {
				stateRegistry.set(key, value as Record<string, SVWSState>);
			}
		}

		api.on.getInspectorTree(onGetInspectorTree);
		api.on.getInspectorState(onGetInspectorState);
		api.on.editInspectorState(onEditInspectorState);
		requestStateUpdate();
	});
}

import { setupDevtoolsPlugin } from '@vue/devtools-api';
import { type App, watch, unref, nextTick } from 'vue';
import type { JavaIterator } from '../../../core/src/java/util/JavaIterator';

/** ID für den eindeutigen Zugriff auf diesen Inspector in den Vue DevTools */
const INSPECTOR_ID = 'svws-model-proxy-inspector';

/** Gruppenbezeichnungen für die strukturierten Bereiche im State-Panel */
const GROUP_PROXY_VIEW = '1. UI Sicht (Proxy)';
const GROUP_PENDING = '2. Pending State';
const GROUP_ORIGINAL = '3. Original Daten';
const GROUP_VALIDATION = '4. Validierung';

// Der Typ der API-Instanz wird aus den Parametern des Callbacks extrahiert...
type DevToolsApiInstance = Parameters<Parameters<typeof setupDevtoolsPlugin>[1]>[0];

/** Struktur für die interne Verwaltung registrierter Proxies */
interface RegisteredProxy {
	instance: any;
	componentName: string;
	variableName: string;
}

/** Struktur für die Custom-State-Array  */
interface CustomState {
	key: string;
	value: unknown;
	editable: boolean;
}

/** Zentrale Registry für alle aktuell im DOM aktiven Model-Proxies */
const proxyRegistry = new Map<string, RegisteredProxy>();

/** Globale Referenz auf die DevTools-API, um den Baum von außen aktualisieren zu können */
let devtoolsApi: DevToolsApiInstance | null = null;

/** Map zur Verwaltung aktiver Watcher, um Speicherlecks und redundante Registrierungen zu verhindern */
const activeWatchers = new Map<string, () => void>();


/**
 * Prüft, ob es sich bei einem Objekt strukturell um eine Instanz von ModelProxy handelt.
 * Dabei wird geprüft, ob die charakteristischen Methoden eines Model-Proxies vorhanden sind.
 *
 * @param obj   das zu prüfende Objekt
 *
 * @returns true, wenn das Objekt die Signatur eines Model-Proxies hat
 */
function isModelProxy(obj: unknown): boolean {
	if ((obj === null) || (typeof obj !== 'object')) {
		return false;
	}
	const candidate = obj as Record<string, unknown>;
	return (typeof candidate.applyToPending === 'function') &&
		(typeof candidate.getAlleFehler === 'function') &&
		(typeof candidate.getFehler === 'function') &&
		('proxy' in candidate) && ('pending' in candidate);
}

/**
 * Wandelt eine Java-Liste rekursiv in ein JavaScript-Array um.
 *
 * @param javaList  die Java-Liste
 *
 * @returns das JavaScript-Array mit den verarbeiteten Unterelementen
 */
function convertJavaListToArray(javaList: any): any[] {
	const listAsArray: any[] = [];
	try {
		const iterator = javaList.iterator() as JavaIterator<any> | null | undefined;
		if ((iterator !== null) && (iterator !== undefined) && (typeof iterator.hasNext === 'function')
			&& (typeof iterator.next === 'function') && (typeof iterator.remove === 'function')) {
			while (iterator.hasNext() === true) {
				listAsArray.push(prepareData(iterator.next()));
			}
		}
	} catch {
		// Fallback bei Fehlern während der Iteration
	}
	return listAsArray;
}

/**
 * Bereitet einen beliebigen Wert für die Darstellung in der Detailansicht der vue-dev-tools vor.
 * Dabei werden auch verschachtelte Datenstrukturen berücksichtigt
 *
 * @param value die aufzubereitenden Daten
 *
 * @returns der vorbereitete Wert als nativer JavaScript-Wert (primitive, array oder plain object)
 */
function prepareData(value: unknown): any {
	if ((value === null) || (typeof value !== 'object')) {
		return value;
	}

	// Falls es sich um eine reaktive Vue-Ref handelt, packen wir den inneren Wert aus
	if (('value' in value) && (((value as any).__v_isRef === true) || (typeof (value as any).effect === 'object'))) {
		return prepareData(value.value);
	}

	// Prüfe, ob es sich um eine Java-Liste mit iterator handelt
	if (typeof (value as any).iterator === 'function') {
		return convertJavaListToArray(value);
	}

	// Bearbeite die Inhalte von Arrays rekursiv
	if (Array.isArray(value)) {
		return value.map(item => prepareData(item));
	}

	// Bearbeite sonstige Objekte rekursiv
	const cleanObj: Record<string, any> = {};
	for (const key of Object.keys(value)) {
		cleanObj[key] = prepareData((value as Record<string, any>)[key]);
	}
	return cleanObj;
}

/**
 * Bereitet ein Objekt für die Darstellung auf der obersten Ebene des State-Panels vor.
 * Die Inhalte werden rekursiv mit der Methode prepareData vorbereitet.
 *
 * @param source das vorzubereitende Objekt der obersten Ebene
 * @param editable gibt an, ob die Felder im Inspector bearbeitet werden dürfen
 *
 * @returns ein Array mit den DevTools-kompatiblenn State-Objekten
 */
function mapObjectToInspectorState(source: any, editable: boolean): CustomState[] {
	if ((source === undefined) || (source === null) || (typeof source !== 'object')) {
		return [];
	}
	const keys = Object.keys(source);
	if (keys.length === 0) {
		return [{ key: '---', value: '(Leer)', editable: false }];
	}
	return keys.map(key => ({
		key,
		value: prepareData(source[key]),
		editable,
	}));
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
		rootObj[topKey] = value;
		return;
	}

	let current = rootObj[topKey];
	for (let i = 1; i < path.length - 1; i++) {
		current = current[path[i]];
		if ((current === null) || (current === undefined) || (typeof current !== 'object')) {
			return;
		}
	}

	const lastKey = path.at(-1);
	if (lastKey !== undefined) {
		current[lastKey] = value;
	}

	const topValue: unknown = rootObj[topKey];
	if (Array.isArray(topValue)) {
		rootObj[topKey] = [...topValue];
	} else if ((topValue !== null) && (topValue !== undefined) && (typeof topValue === 'object')) {
		rootObj[topKey] = { ...topValue };
	}
}


/**
 * Fordert eine Aktualisierung des Inspector-Baums an. Nutze Debouncing, um bei massenhaften
 * Lifecycle-Events (z.B. Routing) die Performance zu schonen.
 */
function requestUpdate(): void {
	if (devtoolsApi === null) {
		console.warn('DevTools Inspektor für Model-Proxies: In requestTreeUpdate: devtoolsApi ist noch null, Update wird übersprungen.');
		return;
	}
	void nextTick(() => {
		devtoolsApi?.sendInspectorTree(INSPECTOR_ID);
		devtoolsApi?.sendInspectorState(INSPECTOR_ID);
	});
}

/**
 * Synchronisiert den Setup-Zustand einer Vue-Komponente mit der DevTools-Registry.
 * Erkennt asynchron erstellte Proxies, entpackt Refs und behandelt Reassignments.
 *
 * @param instance   die interne Vue-Komponenteninstanz
 */
function syncModelProxiesFromComponent(instance: any): void {
	if (instance?.setupState === undefined || instance.setupState === null) {
		return;
	}

	const componentName = instance.type.__name ?? instance.type.name ?? 'UnknownComponent';
	const setupState = instance.setupState;
	let hasRegistryChanged = false;
	for (const key of Object.keys(setupState)) {
		const value = unref(setupState[key]);
		if (!isModelProxy(value)) {
			continue;
		}

		// Reagiere bei einer neuen Model-Proxy-Instanz oder beim Wechsel der Instanz
		const uniqueId = `${instance.uid}::${key}`;
		const existingEntry = proxyRegistry.get(uniqueId);
		if ((existingEntry === undefined) || (existingEntry.instance !== value)) {
			// Alten Watcher aufräumen, falls es ein Reassignment war
			if (existingEntry) {
				const stopWatch = activeWatchers.get(uniqueId);
				if (stopWatch) {
					stopWatch();
					activeWatchers.delete(uniqueId);
				}
			}

			proxyRegistry.set(uniqueId, {
				instance: value,
				componentName,
				variableName: key,
			});
			hasRegistryChanged = true;

			setupProxyWatcher(uniqueId, value);
		}
	}

	// Aktualisiere den Baum mit den Model-Proxies
	if (hasRegistryChanged) {
		requestUpdate();
	}
}

/**
 * Entfernt die Proxies einer ungemounteten Komponente aus der Registry und stoppt die Watcher.
 *
 * @param instance   die interne Vue-Komponenteninstanz
 */
function unregisterModelProxiesFromComponent(instance: any): void {
	if (instance?.setupState === undefined || instance.setupState === null) {
		return;
	}

	const setupState = instance.setupState;
	let hasRegistryChanged = false;

	for (const key of Object.keys(setupState)) {
		const uniqueId = `${instance.uid}::${key}`;

		if (proxyRegistry.delete(uniqueId)) {
			hasRegistryChanged = true;
		}

		const stopWatch = activeWatchers.get(uniqueId);
		if (stopWatch) {
			stopWatch();
			activeWatchers.delete(uniqueId);
		}
	}

	// Aktualisiere den Baum mit den Model-Proxies
	if (hasRegistryChanged) {
		requestUpdate();
	}

	// ggf. ein "Leer"-Update senden, um den Zustand der DevTools zu bereinigen
	if (proxyRegistry.size === 0) {
		void nextTick(() => {
			devtoolsApi?.sendInspectorTree(INSPECTOR_ID);
		});
	}
}

/**
 * Konvertiert die Fehlerliste des Model-Proxies in ein Format für die DevTools.
 *
 * @param modelProxy   der Model-Proxy
 *
 * @returns ein Array mit den Fehlerdaten für die Anzeige
 */
function formatValidationErrors(modelProxy: any): CustomState[] {
	const errors = modelProxy.getAlleFehler();
	if ((errors === undefined) || (errors === null) || (typeof errors.isEmpty !== 'function') || (errors.isEmpty() === true)) {
		return [{ key: 'Status', value: 'Keine Fehler', editable: false }];
	}

	const formattedErrors: CustomState[] = [];
	try {
		const iterator = errors.iterator() as JavaIterator<any> | null | undefined;
		let index = 0;
		if ((iterator !== null) && (iterator !== undefined) && (typeof iterator.hasNext === 'function') && (typeof iterator.next === 'function')) {
			while (iterator.hasNext() === true) {
				const errorItem = iterator.next();
				formattedErrors.push({
					key: `Fehler [${index++}]`,
					value: errorItem.text ?? errorItem.toString(),
					editable: false,
				});
			}
		}
	} catch {
		formattedErrors.push({ key: 'Fehler', value: 'Fehlerliste konnte nicht gelesen werden', editable: false });
	}

	return formattedErrors;
}

/**
 * Konfiguriert die reaktive Überwachung für eine ModelProxy-Instanz für die ausstehenden Daten,
 * die Original-Daten und die Validierungsfehler.
 *
 * @param nodeId       die eindeutige ID des Knotens im Baum
 * @param modelProxy   der ModelProxy
 */
function setupProxyWatcher(nodeId: string, modelProxy: any): void {
	if (activeWatchers.has(nodeId)) {
		return;
	}

	const stopWatch = watch(
		() => {
			return {
				pending: modelProxy.pending,
				data: modelProxy.dataComputed,
				fehler: modelProxy.getAlleFehler(),
			};
		},
		() => {
			requestUpdate();
		},
		{ deep: true, flush: 'post' }
	);

	activeWatchers.set(nodeId, stopWatch);
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

	if (proxyRegistry.size === 0) {
		payload.rootNodes = [{
			id: 'empty-registry-placeholder',
			label: 'Keine aktiven Model-Proxies',
		}];
		return;
	}

	payload.rootNodes = Array.from(proxyRegistry.entries()).map(([id, reg]) => {
		const proxyInstance = reg.instance;
		const data = proxyInstance.data;

		const className = ((data?.constructor?.name !== undefined) && data.constructor.name !== 'Object')
			? data.constructor.name
			: 'ModelProxy';

		let identifier = '';
		if ((data !== undefined) && (data !== null) && (typeof data === 'object')) {
			if (('id' in data) && (data.id !== undefined) && (data.id !== null)) {
				identifier = ` #${data.id}`;
			} else if (('kuerzel' in data) && (data.kuerzel !== null) && (typeof data.kuerzel === 'string') && (data.kuerzel.trim().length !== 0)) {
				identifier = ` (${data.kuerzel})`;
			} else if (('bezeichnung' in data) && (data.bezeichnung !== null) && (typeof data.bezeichnung === 'string') && (data.bezeichnung.trim().length !== 0)) {
				identifier = ` (${data.bezeichnung})`;
			}
		}

		return {
			id: id,
			// Das Hauptlabel zeigt nun z.B.: "LehrerStammdaten #5"
			label: `${className}${identifier}`,
			// Die Beschreibung zeigt den technischen Ort: "<SLehrerStammdaten> ➔ modelProxy"
			description: `<${reg.componentName}> ➔ ${reg.variableName}`,
		};
	});
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

	if (payload.nodeId === 'empty-registry-placeholder') {
		payload.state = {
			'Hinweis': [{ key: 'Info', value: 'Aktuell sind keine Komponenten mit einem ModelProxy im DOM aktiv.', editable: false }],
		};
		return;
	}

	const entry = proxyRegistry.get(payload.nodeId);
	if (!entry) {
		payload.state = { 'Fehler': [{ key: 'Status', value: 'Instanz nicht mehr aktiv', editable: false }] };
		return;
	}

	const proxyInstance = entry.instance;
	setupProxyWatcher(payload.nodeId, proxyInstance);

	payload.state = {
		[GROUP_PROXY_VIEW]: mapObjectToInspectorState(proxyInstance.proxy, true),
		[GROUP_PENDING]: mapObjectToInspectorState(proxyInstance.pending, false),
		[GROUP_ORIGINAL]: mapObjectToInspectorState(unref(proxyInstance.dataComputed), false),
		[GROUP_VALIDATION]: formatValidationErrors(proxyInstance),
	};
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
	const entry = proxyRegistry.get(payload.nodeId);
	if (!entry) {
		return;
	}
	setDeepValue(entry.instance.proxy, payload.path, payload.state.value);
}


/**
 * Registriert das SVWS-Modul zur automatischen Erkennung und Anzeige von ModelProxies.
 *
 * @param app   die Vue-Applikation, auf welche die Dev-Tools angewendet werden sollen
 */
export function registerSVWSModelProxyDevTools(app: App): void {
	if (process.env.NODE_ENV !== 'development') {
		return;
	}

	setupDevtoolsPlugin({
		id: 'de.svws-nrw.webclient.modelproxy.inspector',
		label: 'SVWS ModelProxy Inspector',
		packageName: 'svws-webclient',
		homepage: 'https://github.com/SVWS-NRW/SVWS-Server',
		app,
	}, (api) => {
		console.info("SVWS ModelProxy Inspector wird geladen...");
		devtoolsApi = api;
		api.addInspector({
			id: INSPECTOR_ID,
			label: 'SVWS Model Proxies',
			icon: 'layers',
		});
		api.on.getInspectorTree(onGetInspectorTree);
		api.on.getInspectorState(onGetInspectorState);
		api.on.editInspectorState(onEditInspectorState);

		requestUpdate();
	});

	app.mixin({
		mounted() {
			syncModelProxiesFromComponent(this.$);
		},
		unmounted() {
			unregisterModelProxiesFromComponent(this.$);
		},
	});

}

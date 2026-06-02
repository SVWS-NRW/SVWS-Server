import { setupDevtoolsPlugin } from '@vue/devtools-api';
import { type App, watch } from 'vue';

const INSPECTOR_ID = 'svws-state-inspector';

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
 * Bereitet die Datenstrukturen der States rekursiv für die Darstellung vor.
 * Es werden Funktionen aus der Objekten ausgelassen und die Anzeige von
 * Java-Datenstrukturen vorbereitet. Außerdem werden Objekttypen für die typisierte Anzeige
 * ermittelt.
 *
 * @param value   der Wert, welcher für die Anzeige vorbereitet wird
 *
 * @returns die vorbereiteten Daten für die Anzeige
 */
function prepareData(value: unknown): unknown {
	if ((value === null) || (value === undefined)) {
		return value;
	}

	// Bei einem Array werden die einzelnen Elemente vorbereitet...
	if (Array.isArray(value)) {
		return value.map(item => prepareData(item));
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
						if ((typeof cName === 'string') && (cName !== '') && (cName !== 'Object')) {
							itemType = cName;
						} else {
							itemType = 'Object';
						}
					} else {
						const tName = typeof firstItem;
						itemType = tName.charAt(0).toUpperCase() + tName.slice(1);
					}
				}
			}

			const cNameParent = value.constructor.name;
			const collectionName = ((typeof cNameParent === 'string') && (cNameParent !== '') && (cNameParent !== 'Object'))
				? cNameParent
				: 'List';
			return {
				_custom: {
					type: 'object',
					display: `${collectionName}⟨${itemType}⟩[${obj.elementData.length}]`,
					value: obj.elementData.map(item => prepareData(item)),
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
			cleanedObj[key] = prepareData(propValue);
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

	// Prüfe, auf die reaktiven Daten und füge diese zu den Properties hinzu
	if (stateObj.data !== undefined) {
		const dataProp = stateObj.data as Record<string, unknown>;
		const isVueRef = dataProp.__v_isRef === true || ('value' in dataProp);
		const rawData = isVueRef ? dataProp.value : stateObj.data;

		stateProps.push({
			key: 'Daten (shallowRef.value)',
			value: prepareData(rawData),
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
			stateProps.push({
				key: prop,
				value: prepareData(val),
				editable: false,
				revive: true,
			});
		} catch {
			stateProps.push({
				key: prop,
				value: 'Error: Wert konnte nicht gelesen werden',
				editable: false,
			});
		}
	}
	return stateProps;
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

		// Erstellen des Inspectors
		api.addInspector({
			id: INSPECTOR_ID,
			label: 'SVWS States',
			icon: 'storage',
		});

		// Erzeuge die Baumstruktur mit einem Knoten im linken Panel für jeden gefundenen State
		api.on.getInspectorTree((payload) => {
			if (payload.inspectorId === INSPECTOR_ID) {
				const states = getFilteredStates(app);
				payload.rootNodes = Object.keys(states).map(key => ({ id: key, label: key }));
			}
		});

		// Ermittle die Werte für den ausgewählten State-Knoten und verwende Watcher für die Aktualisierung der Ansicht
		const watchers = new Map<string, () => void>();
		api.on.getInspectorState((payload) => {
			if (payload.inspectorId === INSPECTOR_ID) {
				const states = getFilteredStates(app);
				const selectedState = states[payload.nodeId];
				if ((selectedState === undefined) || (selectedState === null) || (typeof selectedState !== 'object')) {
					payload.state = {
						'Fehler': [{ key: 'Status', value: 'State-Instanz nicht mehr im Kontext verfügbar.', editable: false }],
					};
					return;
				}

				const stateObj = selectedState as Record<string, any>;
				const className = ((typeof stateObj.constructor.name === 'string') && (stateObj.constructor.name !== ''))
					? stateObj.constructor.name : 'Unbekannt';
				payload.state = {
					'State': retrieveStateAttributes(stateObj) as any,
					'Metadaten': [{ key: 'Klasse', value: className, editable: false }],
				};

				if (!watchers.has(payload.nodeId) && (stateObj._state !== undefined)) {
					const stopWatch = watch(
						() => stateObj._state.value,
						() => {
							api.sendInspectorState(INSPECTOR_ID);
						},
						{ deep: true, flush: 'post' }
					);
					watchers.set(payload.nodeId, stopWatch);
				}
			}
		});
	});
}

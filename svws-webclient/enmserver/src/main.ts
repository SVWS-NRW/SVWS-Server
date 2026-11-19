import SWrapper from "@wenom/components/SWrapper.vue";
import { createApp } from "vue";

import { CoreTypeException } from "@core/asd/data/CoreTypeException";
import { NoteKatalogEintrag } from "@core/asd/data/NoteKatalogEintrag";
import { SchulformKatalogEintrag } from "@core/asd/data/schule/SchulformKatalogEintrag";
import { Note } from "@core/asd/types/Note";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { CoreTypeDataManager } from "@core/asd/utils/CoreTypeDataManager";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { List } from "@core/java/util/List";
import { AppContext } from "@ui/AppContext";

import NoteJson from "../../../svws-asd/src/main/resources/de/svws_nrw/asd/types/Note.json";
import SchulformJson from "../../../svws-asd/src/main/resources/de/svws_nrw/asd/types/schule/Schulform.json";

import { RouteManager } from "./router/RouteManager";
import { auskunftStateImpl } from "./states/AuskunftStateImpl";
import { registerStates } from "./states/registerStates";

import "@ui/assets/styles/index.css";
import "./main.css";

// Lese die Daten für die Core-Types ein
const configSchulform = { key: "Schulform", entry: SchulformKatalogEintrag, type: Schulform, json: SchulformJson };
const configNote = { key: "Note", entry: NoteKatalogEintrag, type: Note, json: NoteJson };

// Wird im Code ein weiterer Core-Type verwendet, so bricht der Build über das Plugin coreTypeGuard ab, bis er hier ergänzt wurde.
initCoreTypes([configSchulform, configNote]);

function initCoreType(config: any) {
	const data = config.json;
	const mapData = new HashMap<string, List<any>>();
	const mapStatistikIDs = new HashMap<string, string>();
	for (const eintrag of data.daten) {
		mapStatistikIDs.put(eintrag.bezeichner, eintrag.idStatistik);
		const list = new ArrayList<any>();
		for (const h of eintrag.historie) {
			list.add(config.entry.transpilerFromJSON(JSON.stringify(h)));
		}
		mapData.put(eintrag.bezeichner, list);
	}
	const manager = new CoreTypeDataManager(data.version, config.type.class, config.type.values(), mapData, mapStatistikIDs);
	config.type.init(manager);
}

function initCoreTypes(configs: any[]) {
	// Core-Types, die bei der Initialisierung auf andere zugreifen (z.B. ZulaessigeKursart auf Schulform), werden zurückgestellt, bis diese initialisiert sind
	let offen = configs;
	while (offen.length > 0) {
		const zurueckgestellt: any[] = [];
		let letzterFehler: unknown = null;
		for (const config of offen) {
			try {
				initCoreType(config);
			} catch (e) {
				if (!(e instanceof CoreTypeException)) {
					throw e;
				}
				zurueckgestellt.push(config);
				letzterFehler = e;
			}
		}
		// Wurde in einem Durchlauf kein Core-Type initialisiert, so fehlt eine Voraussetzung, die auch durch Wiederholung nicht erfüllt wird
		if (zurueckgestellt.length === offen.length) {
			throw letzterFehler;
		}
		offen = zurueckgestellt;
	}
}

await auskunftStateImpl.init();

const context = AppContext.init(createApp(SWrapper));
RouteManager.create(AppContext.instance.router);

registerStates();

await context.mount();

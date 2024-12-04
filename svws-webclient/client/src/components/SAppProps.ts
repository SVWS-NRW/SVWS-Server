import type { SchuleStammdaten, Schulform } from "@core";
import type { TabData, TabManager } from "@ui";
import type { ApiStatus } from "./ApiStatus";
import type { AbschnittAuswahlDaten } from "@comp";

export interface AppProps {
	schulform: Schulform;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	schemaname: string; // Der Name des DB-Schemas
	logout: () => Promise<void>;
	setApp: (value: TabData) => Promise<void>;
	benutzerprofilApp: TabData;
	app: TabData;
	selectedChild: TabData;
	apps: TabData[];
	appsHidden: boolean[];
	apiStatus: ApiStatus;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	tabManagerSchule: () => TabManager;
	tabManagerEinstellungen: () => TabManager;
}

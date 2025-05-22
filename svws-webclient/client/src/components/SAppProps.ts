import type { SchuleStammdaten, Schulform, ServerMode } from "@core";
import type { TabData, TabManager, AbschnittAuswahlDaten, AppMenuManager } from "@ui";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	schulform: Schulform;
	servermode: ServerMode;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	schemaname: string; // Der Name des DB-Schemas
	logout: () => Promise<void>;
	menu: AppMenuManager;
	benutzerprofilApp: TabData;
	apiStatus: ApiStatus;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	tabManagerSchule: () => TabManager;
	tabManagerEinstellungen: () => TabManager;
}

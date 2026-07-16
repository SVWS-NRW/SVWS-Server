import type { TabData, TabManager, AppMenuManager } from "@ui";
import type { ApiStatus } from "./ApiStatus";

export interface AppProps {
	schemaname: string; // Der Name des DB-Schemas
	logout: () => Promise<void>;
	menu: AppMenuManager;
	benutzerprofilApp: TabData;
	apiStatus: ApiStatus;
	tabManagerSchule: () => TabManager;
	tabManagerBenutzerprofil: () => TabManager;
	tabManagerNotenmodul: () => TabManager;
	tabManagerEinstellungen: () => TabManager;
}

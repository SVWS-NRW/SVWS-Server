import type { AppMenuManager } from "@ui/ui/nav/AppMenuManager";
import type { TabData } from "@ui/ui/nav/TabData";
import type { TabManager } from "@ui/ui/nav/TabManager";
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

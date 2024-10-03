import type { JahrgangsDaten } from "@core";
import type { TabManager } from "@ui";

export interface JahrgaengeAppProps {
	auswahl: () => JahrgangsDaten | undefined;
	tabManager: () => TabManager;
}
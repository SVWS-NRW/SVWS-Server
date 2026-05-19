import type { GostJahrgang } from "@core";
import type { TabManager } from "@ui";

export interface GostAppProps {
	auswahl: GostJahrgang | undefined;
	tabManager: () => TabManager;
	creationModeEnabled: boolean;
	gruppenprozesseEnabled: boolean;
	selected: () => GostJahrgang[];
}

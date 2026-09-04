import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface GostAppProps {
	auswahl: GostJahrgang | undefined;
	tabManager: () => TabManager;
	creationModeEnabled: boolean;
	gruppenprozesseEnabled: boolean;
	selected: () => GostJahrgang[];
}

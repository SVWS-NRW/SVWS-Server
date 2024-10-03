import type { KlassenListeManager } from "@core";
import type { TabManager } from "@ui";

export interface KlassenAppProps {
	klassenListeManager: () => KlassenListeManager;
	tabManager: () => TabManager;
	gruppenprozesseEnabled: boolean;
	creationModeEnabled: boolean;
}

import type { GostJahrgang, Schuljahresabschnitt } from "@core";
import type { TabManager } from "@ui";

export interface GostAppProps {
	schuljahresabschnitt: () => Schuljahresabschnitt;
	auswahl: GostJahrgang | undefined;
	tabManager: () => TabManager;
	creationModeEnabled: boolean;
	gruppenprozesseEnabled: boolean;
	selected: () => GostJahrgang[];
}

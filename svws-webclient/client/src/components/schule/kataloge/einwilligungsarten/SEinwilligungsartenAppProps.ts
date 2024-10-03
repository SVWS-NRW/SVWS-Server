import type { TabManager } from "@ui";
import type { Einwilligungsart } from "@core";

export interface SEinwilligungsartenAppProps {
	auswahl: () => Einwilligungsart | undefined;
	tabManager: () => TabManager;
}

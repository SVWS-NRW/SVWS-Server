import type { FachListeManager } from "@core";
import type { TabManager } from "@ui";

export interface FaecherAppProps {
	fachListeManager: () => FachListeManager;
	tabManager: () => TabManager;
}
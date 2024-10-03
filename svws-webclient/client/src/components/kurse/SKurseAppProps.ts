import type { KursListeManager } from "@core";
import type { TabManager } from "@ui";

export interface KurseAppProps {
	kursListeManager: () => KursListeManager;
	tabManager: () => TabManager;
}
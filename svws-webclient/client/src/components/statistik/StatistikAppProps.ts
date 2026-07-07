import type { SchuleStammdaten } from "@core";
import type { TabManager } from "@ui";

export interface StatistikAppProps {
	tabManager: () => TabManager;
	schuleStammdaten: SchuleStammdaten;
}

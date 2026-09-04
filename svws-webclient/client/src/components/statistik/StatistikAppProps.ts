import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface StatistikAppProps {
	tabManager: () => TabManager;
	schuleStammdaten: SchuleStammdaten;
}

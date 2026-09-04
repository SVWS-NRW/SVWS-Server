import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface BenutzerAppProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	tabManager: () => TabManager;
}
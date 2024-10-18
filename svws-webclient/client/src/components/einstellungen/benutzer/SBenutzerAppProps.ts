import type { BenutzerListeEintrag } from "@core";
import type { TabManager } from "@ui";

export interface BenutzerAppProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	tabManager: () => TabManager;
}
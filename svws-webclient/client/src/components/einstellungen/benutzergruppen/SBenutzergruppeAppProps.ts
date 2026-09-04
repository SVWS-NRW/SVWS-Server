import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { TabManager } from "@ui/ui/nav/TabManager";

export interface BenutzergruppeAppProps {
	auswahl: () => BenutzergruppeListeEintrag | undefined;
	tabManager: () => TabManager;
}
import type { BenutzergruppeListeEintrag} from "@core";
import type { TabManager } from "@ui";

export interface BenutzergruppeAppProps {
	auswahl:() => BenutzergruppeListeEintrag | undefined;
	tabManager: () => TabManager;
}
import type { BenutzerDaten, List, ServerMode, WiedervorlageEintrag } from "@core";

export interface WiedervorlagenAppProps {
	mode: ServerMode;
	benutzer: () => BenutzerDaten;
	getListWiedervorlagen: () => List<WiedervorlageEintrag>;
	goToPerson: (eintrag: WiedervorlageEintrag) => Promise<void>,
}

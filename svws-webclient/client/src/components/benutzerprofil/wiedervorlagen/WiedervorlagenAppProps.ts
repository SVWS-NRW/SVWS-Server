import type { BenutzerDaten, ServerMode, WiedervorlageEintrag } from "@core";

export interface WiedervorlagenAppProps {
	benutzer: () => BenutzerDaten;
	goToPerson: (eintrag: WiedervorlageEintrag) => Promise<void>;
}

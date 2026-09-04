import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { WiedervorlageEintrag } from "@core/core/data/schule/WiedervorlageEintrag";

// Wiedervorlage Typ (mit Pick für explizite Properties und Erweiterungen für "typPerson")
export type Wiedervorlage = Pick<WiedervorlageEintrag,
		"idPerson" | "bemerkung" | "tsWiedervorlage" | "automatischErledigt"> & {
			typPerson: 1 | 2 | 3 | null;
			idBenutzergruppe: BenutzergruppeListeEintrag | null;
			id: number | null
		};

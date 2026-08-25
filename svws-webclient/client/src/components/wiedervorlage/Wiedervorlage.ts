import type { BenutzergruppeListeEintrag, WiedervorlageEintrag } from "@core";

// Wiedervorlage Typ (mit Pick für explizite Properties und Erweiterungen für "typPerson")
export type Wiedervorlage = Pick<WiedervorlageEintrag,
		"idPerson" | "bemerkung" | "tsWiedervorlage" | "automatischErledigt"> & {
			typPerson: 1 | 2 | 3 | null;
			idBenutzergruppe: BenutzergruppeListeEintrag | null;
			id: number | null
		};

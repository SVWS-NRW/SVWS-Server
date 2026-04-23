import type { BenutzergruppeListeEintrag, WiedervorlageEintrag } from "@core";

// Pick explicit properties to have a clean type and extend with explicit type for 'typPerson'
// (Note: Exports need to be in separate script block in SFC)
export type Wiedervorlage = Pick<WiedervorlageEintrag,
		"idPerson" | "bemerkung" | "tsWiedervorlage" | "automatischErledigt"> & {
			typPerson: null | 1 | 2 | 3;
			idBenutzergruppe: BenutzergruppeListeEintrag | null
		};

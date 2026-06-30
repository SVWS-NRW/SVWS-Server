import type { Config } from "../../../utils/Config";
import type { BenutzerKompetenz } from "../../../../../core/src/core/types/benutzer/BenutzerKompetenz";

export interface SchuelerLaufbahnplanungProps {
	benutzerKompetenzen?: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge?: Set<number>;
	config: () => Config;
}
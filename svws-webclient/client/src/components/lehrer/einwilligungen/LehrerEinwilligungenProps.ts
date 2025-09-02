import type { LehrerEinwilligung, Einwilligungsart, BenutzerKompetenz, List } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface LehrerEinwilligungenProps {
	einwilligungen: () => List<LehrerEinwilligung>;
	mapEinwilligungsarten: Map<number, Einwilligungsart>;
	patch: (data : Partial<LehrerEinwilligung>, idEinwilligungsart: number) => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	apiStatus: ApiStatus;
}

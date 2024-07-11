import type {Einwilligungsart} from "@core";

export interface EinwilligungsartDatenProps {
	patch: (data: Partial<Einwilligungsart>) => Promise<void>;
	auswahl: () => Einwilligungsart;
}

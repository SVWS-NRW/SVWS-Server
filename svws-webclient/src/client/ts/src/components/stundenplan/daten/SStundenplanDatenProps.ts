import type { Stundenplan } from "@svws-nrw/svws-core";

export interface StundenplanDatenProps {
	data: () => Stundenplan;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
}
import type { FoerderschwerpunktEintrag } from "@core";

export interface FoerderschwerpunktDatenProps {
	schuljahr: number;
	patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	data: FoerderschwerpunktEintrag;
}
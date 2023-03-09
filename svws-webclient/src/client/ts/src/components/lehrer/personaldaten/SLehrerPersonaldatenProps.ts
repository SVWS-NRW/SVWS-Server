import { LehrerPersonaldaten } from "@svws-nrw/svws-core";

export interface LehrerPersonaldatenProps {
	patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	personaldaten: LehrerPersonaldaten;
}
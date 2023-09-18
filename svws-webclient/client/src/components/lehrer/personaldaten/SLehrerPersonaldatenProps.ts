import type { LehrerPersonaldaten } from "@core";

export interface LehrerPersonaldatenProps {
	patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	personaldaten: LehrerPersonaldaten;
}
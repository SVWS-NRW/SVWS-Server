import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";

export interface SchuleMerkmaleProps {
	patch: (data: Partial<SchuleStammdaten>) => Promise<void>;
}
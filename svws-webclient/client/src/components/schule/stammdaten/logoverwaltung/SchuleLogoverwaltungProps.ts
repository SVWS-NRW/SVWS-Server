import type { ApiFile, List, Logo } from "@core";

export interface SchuleLogoverwaltungProps {
	logos: () => List<Logo>;
	patchLogo: (logo: Partial<Logo>, id: number) => Promise<boolean>;
	addLogo: (logo: Partial<Logo>) => Promise<Logo>;
	deleteLogo: (logos: Logo[]) => Promise<void>;
	zipLogos: (logos: List<number>) => Promise<ApiFile>;
}

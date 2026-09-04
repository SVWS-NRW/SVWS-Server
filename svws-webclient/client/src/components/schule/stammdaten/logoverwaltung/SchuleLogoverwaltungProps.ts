import type { ApiFile } from "@core/api/BaseApi";
import type { Logo } from "@core/core/data/schule/Logo";
import type { List } from "@core/java/util/List";

export interface SchuleLogoverwaltungProps {
	logos: () => List<Logo>;
	patchLogo: (logo: Partial<Logo>, id: number) => Promise<boolean>;
	addLogo: (logo: Partial<Logo>) => Promise<Logo>;
	deleteLogo: (logos: Logo[]) => Promise<void>;
	zipLogos: (logos: List<number>) => Promise<ApiFile>;
}

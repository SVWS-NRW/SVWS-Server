import type { RouteTabProps } from "~/router/RouteTabNode";
import type { SchuleStammdaten } from "@core";

export interface SchuleStammdatenProps extends RouteTabProps {
	schule: () => SchuleStammdaten;
}

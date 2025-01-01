import type { api } from "../../router/Api";

export interface ErrorProps {
	code: number | undefined;
	error: Error | undefined;
	api: typeof api;
}

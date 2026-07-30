import { ModelProxy } from "@ui";
import type { TableLogo } from "../LogoUtils";

export class LogoModelProxy extends ModelProxy<TableLogo> {

	constructor(
		data: () => TableLogo,
		patch?: (data: Partial<TableLogo>) => Promise<boolean>
	) {
		super({ data, patch });
		this.validate();
	}
}

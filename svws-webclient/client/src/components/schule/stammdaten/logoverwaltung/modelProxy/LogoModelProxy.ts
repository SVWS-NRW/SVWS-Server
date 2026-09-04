import { ModelProxy } from "@ui/model/ModelProxy";
import type { ImageInfo, TableLogo } from "../LogoUtils";
import { getImageRestrictions } from "../LogoUtils";
import { ValidatorLogoImage } from "./ValidatorLogoImage";

export class LogoModelProxy extends ModelProxy<TableLogo> {

	/** Gecachte Bilddimensionen – werden async von außen gesetzt */
	public imageInfo: ImageInfo = { width: 0, height: 0, fileSize: null, fileType: null };

	constructor(
		data: () => TableLogo,
		patch?: (data: Partial<TableLogo>) => Promise<boolean>
	) {
		super({ data, patch });
		this.addValidator(new ValidatorLogoImage(() => this.imageInfo, getImageRestrictions(this.data.kennung)), 'base64');
		this.validate();

	}

}

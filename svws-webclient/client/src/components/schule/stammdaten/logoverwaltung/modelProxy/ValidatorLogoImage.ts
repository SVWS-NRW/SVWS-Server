import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

import type { ImageInfo, ImageRestrictions } from "../LogoUtils";

import { ValidatorLogoImageAspectRatio } from "./ValidatorLogoImageAspectRatio";
import { ValidatorLogoImageFileSize } from "./ValidatorLogoImageFileSize";
import { ValidatorLogoImageFileType } from "./ValidatorLogoImageFileType";
import { ValidatorLogoImageResolution } from "./ValidatorLogoImageResolution";


/**
 * Ein Validator, welcher prüft, ob das Logo-Bild gültig ist.
 */
export class ValidatorLogoImage extends BasicValidator {
	/**
	 * Erzeugt einen neuen Validator
	 *
	 * @param imageInfo           die Werte des Bildes
	 * @param imageRestrictions   die Vorgaben, die das Bild erfüllen muss
	 */
	constructor(imageInfo: () => ImageInfo, imageRestrictions: ImageRestrictions) {
		super(ValidatorFehlerart.KANN);
		this._validatoren.add(new ValidatorLogoImageFileType(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageFileSize(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageResolution(imageInfo, imageRestrictions));
		this._validatoren.add(new ValidatorLogoImageAspectRatio(imageInfo, imageRestrictions));
	}

	protected pruefe(): boolean {
		return true;
	}

}

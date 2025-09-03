import { JavaString } from "@core";

export function emailIsValid(value: string | null, maxLength: number) {
	if ((value === null) || JavaString.isBlank(value))
		return true;

	if (value.length > maxLength)
		return false;

	return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
}

export function mandatoryInputIsValid(value: string | null, maxLength: number) {
	if (value === null)
		return false;

	return (!JavaString.isBlank(value)) && (value.length <= maxLength);
}

export function optionalInputIsValid(input : string | null, maxLength: number) {
	if ((input === null) || (JavaString.isBlank(input)))
		return true;

	return input.length <= maxLength;
}

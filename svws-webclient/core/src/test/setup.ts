import { beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "./../asd/utils/JsonCoreTypeReaderStatic";

const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	reader.readAll();
});

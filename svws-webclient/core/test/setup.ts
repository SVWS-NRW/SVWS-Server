import { beforeAll } from "vitest";
import { JsonCoreTypeReaderStatic } from "../src/asd/utils/JsonCoreTypeReaderStatic";

const reader = new JsonCoreTypeReaderStatic();

beforeAll(async () => {
	reader.readAll();
});

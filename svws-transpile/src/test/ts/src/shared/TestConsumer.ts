import type { Consumer } from "../../../../main/resources/typescript/java/util/function/Consumer";
import type { TestPerson } from "./TestPerson";

type TypeAlias = string | number | TestPerson;
export class TestConsumer implements Consumer<TypeAlias> {
	public value: (TypeAlias)[] = [];
	public accept(e: TypeAlias): void {
		this.value.push(e);
	}
}

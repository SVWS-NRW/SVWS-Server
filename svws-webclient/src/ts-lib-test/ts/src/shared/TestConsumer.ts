/* eslint-disable @typescript-eslint/ban-types */
import { LehrerListeEintrag, Consumer } from "~/index";

export class TestConsumer implements Consumer<string | Number | LehrerListeEintrag> {
    public value: (string | Number | LehrerListeEintrag)[] = [];
    public accept(e: string | Number | LehrerListeEintrag): void {
        this.value.push(e);
    }
}

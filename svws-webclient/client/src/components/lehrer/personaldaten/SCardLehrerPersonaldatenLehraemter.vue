<template>
	<svws-ui-content-card title="Lehramtsangaben">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Lehramt" v-model="lehramt" :items="LehrerLehramt.values()"
				:item-text="(i: LehrerLehramt) => i.daten.text" required />
			<svws-ui-select title="Anerkennung Lehramt" v-model="lehramt_anerkennung" :items="LehrerLehramtAnerkennung.values()"
				:item-text="(i: LehrerLehramtAnerkennung) => i.daten.text" required />
			<svws-ui-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()"
				:item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required />
			<svws-ui-select title="Anerkennung Lehrbefähigung" v-model="lehrbefaehigung_anerkennung" :items="LehrerLehrbefaehigungAnerkennung.values()"
				:item-text="(i: LehrerLehrbefaehigungAnerkennung) => i.daten.text" required />
			<svws-ui-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()"
				:item-text="(i: LehrerFachrichtung) => i.daten.text" required />
			<svws-ui-select title="Anerkennung Fachrichtung" v-model="fachrichtung_anerkennung" :items="LehrerFachrichtungAnerkennung.values()"
				:item-text="(i: LehrerFachrichtungAnerkennung) => i.daten.text" required />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type LehrerListeManager, type LehrerPersonalabschnittsdaten, type LehrerPersonaldaten,
		LehrerFachrichtung, LehrerFachrichtungAnerkennung, LehrerLehramt, LehrerLehramtAnerkennung,
		LehrerLehrbefaehigung, LehrerLehrbefaehigungAnerkennung } from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
		patchAbschnittsdaten: (data : Partial<LehrerPersonalabschnittsdaten>, id : number) => Promise<void>;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const lehramt = computed<LehrerLehramt | undefined>({
		get(): LehrerLehramt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehramt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehramt | undefined) {
			// TODO props.personaldaten.patch({	lehramt: val?.kuerzel });
		}
	});

	const lehramt_anerkennung = computed<LehrerLehramtAnerkennung | undefined>({
		get(): LehrerLehramtAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehramtAnerkennung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehramtAnerkennung | undefined) {
			// TODO props.personaldaten.patch({ lehramtAnerkennung: val?.kuerzel });
		}
	});

	const lehrbefaehigung = computed<LehrerLehrbefaehigung | undefined>({
		get(): LehrerLehrbefaehigung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehrbefaehigung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehrbefaehigung | undefined) {
			// TODO props.personaldaten.patch({ lehrbefaehigung: val?.kuerzel });
		}
	});

	const lehrbefaehigung_anerkennung = computed<LehrerLehrbefaehigungAnerkennung | undefined>({
		get(): LehrerLehrbefaehigungAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehrbefaehigungAnerkennung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehrbefaehigungAnerkennung | undefined) {
			// TODO props.personaldaten.patch({ lehrbefaehigungAnerkennung: val?.kuerzel });
		}
	});

	const fachrichtung = computed<LehrerFachrichtung | undefined>({
		get(): LehrerFachrichtung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerFachrichtung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerFachrichtung | undefined) {
			// TODO props.personaldaten.patch({ fachrichtung: val?.kuerzel });
		}
	});

	const fachrichtung_anerkennung = computed<LehrerFachrichtungAnerkennung | undefined>({
		get(): LehrerFachrichtungAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerFachrichtungAnerkennung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerFachrichtungAnerkennung | undefined) {
			// TODO props.personaldaten.patch({ fachrichtungAnerkennung: val?.kuerzel });
		}
	});

</script>

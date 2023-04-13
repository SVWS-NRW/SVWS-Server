<template>
	<svws-ui-content-card title="Lehramtsangaben">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Lehramt" v-model="lehramt" :items="LehrerLehramt.values()"
				:item-text="(i: LehrerLehramt) => i.daten.text" required />
			<svws-ui-multi-select title="Anerkennung Lehramt" v-model="lehramt_anerkennung" :items="LehrerLehramtAnerkennung.values()"
				:item-text="(i: LehrerLehramtAnerkennung) => i.daten.text" required />
			<svws-ui-multi-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()"
				:item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required />
			<svws-ui-multi-select title="Anerkennung Lehrbefähigung" v-model="lehrbefaehigung_anerkennung" :items="LehrerLehrbefaehigungAnerkennung.values()"
				:item-text="(i: LehrerLehrbefaehigungAnerkennung) => i.daten.text" required />
			<svws-ui-multi-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()"
				:item-text="(i: LehrerFachrichtung) => i.daten.text" required />
			<svws-ui-multi-select title="Anerkennung Fachrichtung" v-model="fachrichtung_anerkennung" :items="LehrerFachrichtungAnerkennung.values()"
				:item-text="(i: LehrerFachrichtungAnerkennung) => i.daten.text" required />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { LehrerPersonaldaten } from "@svws-nrw/svws-core";
	import { LehrerFachrichtung, LehrerFachrichtungAnerkennung, LehrerLehramt, LehrerLehramtAnerkennung, LehrerLehrbefaehigung,
		LehrerLehrbefaehigungAnerkennung } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		personaldaten: LehrerPersonaldaten
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerPersonaldaten>): void;
	}>()

	function doPatch(data: Partial<LehrerPersonaldaten>) {
		emit('patch', data);
	}

	const lehramt: WritableComputedRef<LehrerLehramt | undefined> = computed({
		get(): LehrerLehramt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehramt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehramt | undefined) {
			// TODO props.personaldaten.patch({	lehramt: val?.kuerzel });
		}
	});

	const lehramt_anerkennung: WritableComputedRef<LehrerLehramtAnerkennung | undefined> = computed({
		get(): LehrerLehramtAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehramtAnerkennung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehramtAnerkennung | undefined) {
			// TODO props.personaldaten.patch({ lehramtAnerkennung: val?.kuerzel });
		}
	});

	const lehrbefaehigung: WritableComputedRef<LehrerLehrbefaehigung | undefined> = computed({
		get(): LehrerLehrbefaehigung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehrbefaehigung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehrbefaehigung | undefined) {
			// TODO props.personaldaten.patch({ lehrbefaehigung: val?.kuerzel });
		}
	});

	const lehrbefaehigung_anerkennung: WritableComputedRef<LehrerLehrbefaehigungAnerkennung | undefined> = computed({
		get(): LehrerLehrbefaehigungAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehrbefaehigungAnerkennung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehrbefaehigungAnerkennung | undefined) {
			// TODO props.personaldaten.patch({ lehrbefaehigungAnerkennung: val?.kuerzel });
		}
	});

	const fachrichtung: WritableComputedRef<LehrerFachrichtung | undefined> = computed({
		get(): LehrerFachrichtung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerFachrichtung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerFachrichtung | undefined) {
			// TODO props.personaldaten.patch({ fachrichtung: val?.kuerzel });
		}
	});

	const fachrichtung_anerkennung: WritableComputedRef<LehrerFachrichtungAnerkennung | undefined> = computed({
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

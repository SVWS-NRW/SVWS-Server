<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-multi-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text" />
			<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text" />
			<svws-ui-text-input placeholder="Pflichtstundensoll" :model-value="personaldaten.pflichtstundensoll" @change="pflichtstundensoll=>doPatch({pflichtstundensoll: Number(pflichtstundensoll)})" type="text" />
			<svws-ui-multi-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text" />
			<svws-ui-text-input placeholder="Stammschule" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer=>doPatch({stammschulnummer})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerPersonaldaten} from "@core";
	import type { WritableComputedRef } from "vue";
	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";
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

	const rechtsverhaeltnis: WritableComputedRef<LehrerRechtsverhaeltnis | undefined> = computed({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten.kuerzel === props.personaldaten.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			doPatch({ rechtsverhaeltnis: val?.daten.kuerzel });
		}
	});

	const beschaeftigungsart: WritableComputedRef<LehrerBeschaeftigungsart | undefined> = computed({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten.kuerzel === props.personaldaten.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			doPatch({ beschaeftigungsart: val?.daten.kuerzel });
		}
	});

	const einsatzstatus: WritableComputedRef<LehrerEinsatzstatus | undefined> = computed({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten.kuerzel === props.personaldaten.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			doPatch({ einsatzstatus: val?.daten.kuerzel });
		}
	});

</script>

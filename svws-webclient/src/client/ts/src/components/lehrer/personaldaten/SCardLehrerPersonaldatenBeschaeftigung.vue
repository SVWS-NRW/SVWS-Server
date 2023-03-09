<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text" />
			<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text" />
			<svws-ui-text-input placeholder="Pflichtstundensoll" v-model="pflichtstundensoll" type="text" />
			<svws-ui-multi-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text" />
			<svws-ui-text-input placeholder="Stammschule" v-model="stammschulnummer" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerPersonaldaten, LehrerRechtsverhaeltnis } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

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

	const pflichtstundensoll: WritableComputedRef<number | undefined> =
		computed({
			get(): number | undefined {
				return props.personaldaten.pflichtstundensoll ?? undefined;
			},
			set(val: number | undefined) {
				doPatch({ pflichtstundensoll: val });
			}
		});

	const stammschulnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.stammschulnummer ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ stammschulnummer: val });
		}
	});

</script>

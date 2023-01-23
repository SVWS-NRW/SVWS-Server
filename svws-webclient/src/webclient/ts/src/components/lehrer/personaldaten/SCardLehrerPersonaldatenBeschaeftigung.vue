<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text.toString()" />
			<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text.toString()" />
			<svws-ui-text-input placeholder="Pflichtstundensoll" v-model="pflichtstundensoll" type="text" />
			<svws-ui-multi-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text.toString()" />
			<svws-ui-text-input placeholder="Stammschule" v-model="stammschulnummer" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerPersonaldaten, LehrerRechtsverhaeltnis } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";

	const props = defineProps<{ personaldaten: DataLehrerPersonaldaten }>();

	const daten: ComputedRef<LehrerPersonaldaten> = computed(() => props.personaldaten.daten || new LehrerPersonaldaten());

	const rechtsverhaeltnis: WritableComputedRef<LehrerRechtsverhaeltnis | undefined> = computed({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten.kuerzel === daten.value.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			void props.personaldaten.patch({ rechtsverhaeltnis: val?.daten.kuerzel });
		}
	});

	const beschaeftigungsart: WritableComputedRef<LehrerBeschaeftigungsart | undefined> = computed({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten.kuerzel === daten.value.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			void props.personaldaten.patch({ beschaeftigungsart: val?.daten.kuerzel });
		}
	});

	const einsatzstatus: WritableComputedRef<LehrerEinsatzstatus | undefined> = computed({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten.kuerzel === daten.value.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			void props.personaldaten.patch({ einsatzstatus: val?.daten.kuerzel });
		}
	});

	const pflichtstundensoll: WritableComputedRef<number | undefined> =
		computed({
			get(): number | undefined {
				return daten.value.pflichtstundensoll?.valueOf();
			},
			set(val: number | undefined) {
				void props.personaldaten.patch({ pflichtstundensoll: val });
			}
		});

	const stammschulnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.stammschulnummer?.toString();
		},
		set(val: string | undefined) {
			void props.personaldaten.patch({ stammschulnummer: val });
		}
	});

</script>

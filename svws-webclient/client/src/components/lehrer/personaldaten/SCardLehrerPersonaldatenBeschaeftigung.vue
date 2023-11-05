<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Rechtsverhältnis" v-model="rechtsverhaeltnis" :items="LehrerRechtsverhaeltnis.values()"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text" />
			<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="LehrerBeschaeftigungsart.values()"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text" />
			<svws-ui-text-input placeholder="Pflichtstundensoll" :model-value="personaldaten.pflichtstundensoll" @change="pflichtstundensoll => patch({pflichtstundensoll: Number(pflichtstundensoll)})" type="text" />
			<svws-ui-select title="Einsatzstatus" v-model="einsatzstatus" :items="LehrerEinsatzstatus.values()"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text" />
			<svws-ui-text-input placeholder="Stammschule" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer => patch({stammschulnummer})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type LehrerPersonaldaten, LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerRechtsverhaeltnis } from "@core";

	const props = defineProps<{
		personaldaten: LehrerPersonaldaten;
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	}>();

	const rechtsverhaeltnis = computed<LehrerRechtsverhaeltnis | undefined>({
		get(): LehrerRechtsverhaeltnis | undefined {
			return LehrerRechtsverhaeltnis.values().find(r => r.daten.kuerzel === props.personaldaten.rechtsverhaeltnis);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			void props.patch({ rechtsverhaeltnis: val?.daten.kuerzel });
		}
	});

	const beschaeftigungsart = computed<LehrerBeschaeftigungsart | undefined>({
		get(): LehrerBeschaeftigungsart | undefined {
			return LehrerBeschaeftigungsart.values().find(r => r.daten.kuerzel === props.personaldaten.beschaeftigungsart);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			void props.patch({ beschaeftigungsart: val?.daten.kuerzel });
		}
	});

	const einsatzstatus = computed<LehrerEinsatzstatus | undefined>({
		get(): LehrerEinsatzstatus | undefined {
			return LehrerEinsatzstatus.values().find(r => r.daten.kuerzel === props.personaldaten.einsatzstatus);
		},
		set(val: LehrerEinsatzstatus | undefined) {
			void props.patch({ einsatzstatus: val?.daten.kuerzel });
		}
	});

</script>

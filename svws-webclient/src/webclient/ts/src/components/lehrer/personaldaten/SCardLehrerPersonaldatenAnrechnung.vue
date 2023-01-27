<template>
	<svws-ui-content-card title="Mehr- / Minderleistung /Anrechnungsstunden">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Mehrleistung" v-model="mehrleistungsgrund" :items="LehrerMehrleistungArt.values()"
				:item-text="(i: LehrerMehrleistungArt) =>i.daten.text.toString()" />
			<svws-ui-multi-select title="Minderleistung" v-model="minderleistungsgrund" :items="LehrerMinderleistungArt.values()"
				:item-text="(i: LehrerMinderleistungArt) =>i.daten.text.toString()" />
			<svws-ui-text-input placeholder="Stundensumme" v-model="pflichtstundensoll" type="text" />
			<svws-ui-multi-select title="Nicht unterichtliche Tätigkeiten" v-model="anrechnungsgrund" :items="LehrerAnrechnungsgrund.values()"
				:item-text="(i: LehrerAnrechnungsgrund) =>i.daten.text.toString()" />
			<svws-ui-text-input placeholder="Stammschulnummer" v-model="stammschulnummer" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { LehrerAnrechnungsgrund, LehrerMehrleistungArt, LehrerMinderleistungArt, LehrerPersonaldaten } from "@svws-nrw/svws-core-ts";
	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";

	const props = defineProps<{ personaldaten: DataLehrerPersonaldaten }>();

	const daten: ComputedRef<LehrerPersonaldaten> = computed(() => props.personaldaten.daten || new LehrerPersonaldaten());

	const mehrleistungsgrund: WritableComputedRef<LehrerMehrleistungArt | undefined> = computed({
		get(): LehrerMehrleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMehrleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMehrleistungArt | undefined) {
			// TODO props.personaldaten.patch({ mehrleistungsgrund: val?.kuerzel });
		}
	});

	const minderleistungsgrund: WritableComputedRef<LehrerMinderleistungArt | undefined> = computed({
		get(): LehrerMinderleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerMinderleistungArt.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerMinderleistungArt | undefined) {
			// TODO props.personaldaten.patch({ minderleistungsgrund: val?.kuerzel });
		}
	});

	const anrechnungsgrund: WritableComputedRef<LehrerAnrechnungsgrund | undefined> = computed({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerAnrechnungsgrund.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO props.personaldaten.patch({ anrechnungsgrund: val?.kuerzel });
		}
	});

	const pflichtstundensoll: WritableComputedRef<number | undefined> = computed({
		get(): number | undefined {
			return daten.value.pflichtstundensoll ?? undefined;
		},
		set(val: number | undefined) {
			void props.personaldaten.patch({ pflichtstundensoll: val });
		}
	});

	const stammschulnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.stammschulnummer ?? undefined;
		},
		set(val: string | undefined) {
			void props.personaldaten.patch({ stammschulnummer: val });
		}
	});

</script>

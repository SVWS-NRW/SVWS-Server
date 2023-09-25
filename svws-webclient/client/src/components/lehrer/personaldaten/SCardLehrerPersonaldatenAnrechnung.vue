<template>
	<svws-ui-content-card title="Mehr- und Minderleistung, Anrechnungsstunden">
		<svws-ui-input-wrapper>
			<svws-ui-select title="Mehrleistung" v-model="mehrleistungsgrund" :items="LehrerMehrleistungArt.values()"
				:item-text="(i: LehrerMehrleistungArt) =>i.daten.text" />
			<svws-ui-select title="Minderleistung" v-model="minderleistungsgrund" :items="LehrerMinderleistungArt.values()"
				:item-text="(i: LehrerMinderleistungArt) =>i.daten.text" />
			<svws-ui-text-input placeholder="Stundensumme" :model-value="personaldaten.pflichtstundensoll" @change="pflichtstundensoll=>doPatch({pflichtstundensoll: Number(pflichtstundensoll)})" type="text" />
			<svws-ui-select title="Nicht unterichtliche Tätigkeiten" v-model="anrechnungsgrund" :items="LehrerAnrechnungsgrund.values()"
				:item-text="(i: LehrerAnrechnungsgrund) =>i.daten.text" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Stammschulnummer" :model-value="personaldaten.stammschulnummer" @change="stammschulnummer=>doPatch({stammschulnummer})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerPersonaldaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { LehrerAnrechnungsgrund, LehrerMehrleistungArt, LehrerMinderleistungArt } from "@core";
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
</script>

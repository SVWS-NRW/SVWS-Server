<template>
	<td>
		<svws-ui-multi-select title="Betrieb" v-model="inputBetrieb" :items="mapBetriebe" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Ausbilder" v-model="ausbilder" type="text" />
	</td>
	<td>
		<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Vertragsbeginn" v-model="vertragsbeginn" type="date" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Vertragsende" v-model="vertragsende" type="date" />
	</td>
	<td>
		<svws-ui-checkbox v-model="praktikum" />
	</td>
	<td>
		<svws-ui-multi-select title="Betreuungslehrer" v-model="inputBetreuungslehrer" :items="mapLehrer" :item-text="(i: LehrerListeEintrag) => i.nachname" />
	</td>
	<td>
		<svws-ui-multi-select title="Ansprechpartner" v-model="ansprechpartner" :items="mapAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name || ''" />
	</td>
	<td>
		<svws-ui-checkbox v-model="anschreiben" />
	</td>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, SchuelerBetriebsdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		betrieb: SchuelerBetriebsdaten;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapBetriebe: Map<number, BetriebListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
	}>();

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.betrieb.betreuungslehrer_id === null ? undefined : props.mapLehrer.get(props.betrieb.betreuungslehrer_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ betreuungslehrer_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const ausbilder: WritableComputedRef<string | undefined> = computed({
		get: () => props.betrieb.ausbilder === null ? undefined : props.betrieb.ausbilder,
		set: (value) => void props.patchSchuelerBetriebsdaten({ ausbilder: value === undefined ? null : value }, props.betrieb.id)
	});

	const inputBetrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get: () => props.mapBetriebe.get(props.betrieb.betrieb_id),
		set: (value) => {
			if (value !== undefined)
				void props.patchSchuelerBetriebsdaten({ betrieb_id: value.id }, props.betrieb.id);
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => (props.betrieb.beschaeftigungsart_id === null) ? undefined : props.mapBeschaeftigungsarten.get(props.betrieb.beschaeftigungsart_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ beschaeftigungsart_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const praktikum: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.praktikum === null ? false : props.betrieb.praktikum,
		set: (value) => void props.patchSchuelerBetriebsdaten({ praktikum: value }, props.betrieb.id)
	});

	const vertragsbeginn: WritableComputedRef<string | undefined> = computed({
		get: () => props.betrieb.vertragsbeginn === null ? undefined : props.betrieb.vertragsbeginn,
		set: (value) => void props.patchSchuelerBetriebsdaten({ vertragsbeginn: value === undefined ? null : value }, props.betrieb.id)
	});

	const vertragsende: WritableComputedRef<string | undefined> = computed({
		get: () => props.betrieb.vertragsende === null ? undefined : props.betrieb.vertragsende,
		set: (value) => void props.patchSchuelerBetriebsdaten({ vertragsende: value === undefined ? null : value }, props.betrieb.id)
	});

	const anschreiben: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.allgadranschreiben === null ? false : props.betrieb.allgadranschreiben,
		set: (value) => void props.patchSchuelerBetriebsdaten({ allgadranschreiben: value }, props.betrieb.id)
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => props.betrieb.ansprechpartner_id === null ? undefined : props.mapAnsprechpartner.get(props.betrieb.ansprechpartner_id),
		set: (value) => {
			void props.patchSchuelerBetriebsdaten({ ansprechpartner_id: value === undefined ? null : value.id }, props.betrieb.id);
		}
	});

//text-gray-600
</script>

<style lang="postcss" scoped>

	td {
		@apply whitespace-nowrap px-1 py-2 text-sm font-medium
	}

</style>

<template>
	<svws-ui-content-card title="Vor der Aufnahme besucht">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.vorigeSchulnummer" @change="vorigeSchulnummer=>doPatch({ vorigeSchulnummer })" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="data.vorigeAllgHerkunft" @change="vorigeAllgHerkunft=>doPatch({ vorigeAllgHerkunft })" type="text" />
			<svws-ui-text-input placeholder="Entlassen am" :model-value="data.vorigeEntlassdatum" @change="vorigeEntlassdatum=>doPatch({ vorigeEntlassdatum })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.vorigeEntlassjahrgang" @change="vorigeEntlassjahrgang=>doPatch({ vorigeEntlassjahrgang })" type="number" />
			<svws-ui-text-input placeholder="Bemerkung" :model-value="data.vorigeBemerkung" @change="vorigeBemerkung=>doPatch({ vorigeBemerkung })" type="text" span="full" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Entlassgrund" :model-value="data.vorigeEntlassgrundID" @change="doPatch({ vorigeEntlassgrundID: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" :model-value="data.vorigeAbschlussartID" @change="vorigeAbschlussartID=>doPatch({ vorigeAbschlussartID })" type="text" />
			<svws-ui-multi-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Herkunftsarten, Schulform, Schulgliederung } from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerSchulbesuchsdaten } from "@core";

	const props = defineProps<{
		data: SchuelerSchulbesuchsdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerSchulbesuchsdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerSchulbesuchsdaten>) {
		emit('patch', data);
	}

	const vorigeArtLetzteVersetzung: WritableComputedRef<Herkunftsarten | undefined> = computed({
		get: () => {
			if (props.data.vorigeArtLetzteVersetzung === null)
				return undefined;
			const artID = +props.data.vorigeArtLetzteVersetzung;
			return Herkunftsarten.getByID(artID) || undefined;
		},
		set: (value) => doPatch({ vorigeArtLetzteVersetzung:  value === undefined || value === null ? null : "" + value.daten.id })
	});

	const herkunftsarten:ComputedRef<Herkunftsarten[]> = computed(() => {
		return Herkunftsarten.values().filter(h => getBezeichnung(h) !== null);
	});

	const vorigeSchulform: ComputedRef<Schulform | undefined> = computed(() => {
		const vorigeAllgHerkunft = props.data.vorigeAllgHerkunft;
		if (vorigeAllgHerkunft === undefined)
			return undefined;
		const sgl = Schulgliederung.getByKuerzel(vorigeAllgHerkunft);
		if (sgl !== null)
			return Schulform.BK;
		return Schulform.getByKuerzel(vorigeAllgHerkunft) || undefined;
	});

	function getBezeichnung(h: Herkunftsarten) {
		return h.getBezeichnung(vorigeSchulform.value || Schulform.G);
	}

	const showstatistic: ComputedRef<boolean> = computed(() => true);

</script>

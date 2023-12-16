<template>
	<svws-ui-content-card title="Vor der Aufnahme besucht">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.vorigeSchulnummer" @change="vorigeSchulnummer=>patch({ vorigeSchulnummer })" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="data.vorigeAllgHerkunft" @change="vorigeAllgHerkunft=>patch({ vorigeAllgHerkunft })" type="text" />
			<svws-ui-text-input placeholder="Entlassen am" :model-value="data.vorigeEntlassdatum" @change="vorigeEntlassdatum=>patch({ vorigeEntlassdatum })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.vorigeEntlassjahrgang" @change="vorigeEntlassjahrgang=>patch({ vorigeEntlassjahrgang })" type="text" />
			<svws-ui-text-input placeholder="Bemerkung" :model-value="data.vorigeBemerkung" @change="vorigeBemerkung=>patch({ vorigeBemerkung })" type="text" span="full" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Entlassgrund" :model-value="data.vorigeEntlassgrundID" @change="patch({ vorigeEntlassgrundID: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" :model-value="data.vorigeAbschlussartID" @change="vorigeAbschlussartID=>patch({ vorigeAbschlussartID })" type="text" />
			<svws-ui-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" class="col-span-full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerSchulbesuchsdaten } from "@core";
	import { Herkunftsarten, Schulform, Schulgliederung } from "@core";

	const props = defineProps<{
		data: SchuelerSchulbesuchsdaten;
		patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	}>();


	const vorigeArtLetzteVersetzung = computed<Herkunftsarten | undefined>({
		get: () => {
			if (props.data.vorigeArtLetzteVersetzung === null)
				return undefined;
			const artID = +props.data.vorigeArtLetzteVersetzung;
			return Herkunftsarten.getByID(artID) || undefined;
		},
		set: (value) => void props.patch({ vorigeArtLetzteVersetzung:  value === undefined || value === null ? null : "" + value.daten.id })
	});

	const herkunftsarten = computed<Herkunftsarten[]>(() => {
		return Herkunftsarten.values().filter(h => getBezeichnung(h) !== null);
	});

	const vorigeSchulform = computed<Schulform | undefined>(() => {
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

	const showstatistic = computed(() => true);

</script>

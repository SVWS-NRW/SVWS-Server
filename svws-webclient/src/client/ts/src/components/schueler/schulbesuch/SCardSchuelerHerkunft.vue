<template>
	<svws-ui-content-card title="Vor der Aufnahme besuchte Schule/Einrichtung/Herkunftsarten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" :model-value="data.vorigeSchulnummer"
				@update:model-value="doPatch({ vorigeSchulnummer: String($event) })" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="data.vorigeAllgHerkunft"
				@update:model-value="doPatch({ vorigeAllgHerkunft: String($event) })" type="text" />
			<svws-ui-text-input placeholder="Entlassen am" :model-value="data.vorigeEntlassdatum"
				@update:model-value="doPatch({ vorigeEntlassdatum: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.vorigeEntlassjahrgang"
				@update:model-value="doPatch({ vorigeEntlassjahrgang: String($event) })" type="number" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Bemerkung" :model-value="data.vorigeBemerkung"
					@update:model-value="doPatch({ vorigeBemerkung: String($event) })" type="text" />
			</div>
			<svws-ui-text-input placeholder="Entlassgrund" :model-value="data.vorigeEntlassgrundID"
				@update:model-value="doPatch({ vorigeEntlassgrundID: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" :model-value="data.vorigeAbschlussartID"
				@update:model-value="doPatch({ vorigeAbschlussartID: String($event) })" type="text" />
			<div class="col-span-2">
				<svws-ui-multi-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Herkunftsarten, Schulform, Schulgliederung } from "@svws-nrw/svws-core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core";

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

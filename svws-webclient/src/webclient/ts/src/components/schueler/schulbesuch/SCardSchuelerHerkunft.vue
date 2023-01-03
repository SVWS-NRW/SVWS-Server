<template>
	<svws-ui-content-card title="Vor der Aufnahme besuchte Schule/Einrichtung/Herkunftsarten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" v-model="vorigeSchulnummer" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" v-model="vorigeAllgHerkunft" type="text" />
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Entlassen am" v-model="vorigeEntlassDatum" type="date" />
				<svws-ui-text-input placeholder="Entlassjahrgang" v-model="vorigeEntlassjahrgang" type="year" />
				<svws-ui-multi-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" statistics />
			</div>
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Bemerkung" v-model="vorigeBemerkung" type="text" />
				<svws-ui-text-input placeholder="Entlassgrund" v-model="vorigeEntlassgrundID" type="text" />
				<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" v-model="vorigeAbschlussartID" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Herkunftsarten, HerkunftsartKatalogEintrag, Schulform, Schulgliederung } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { mainApp } from "~/apps/Main";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();
	const schulform: ComputedRef<Schulform> = computed(() => mainApp.apps.schule.schulform || Schulform.G);

	const vorigeSchulnummer: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeSchulnummer?.toString(),
		set: (value) => props.data.patch({ vorigeSchulnummer:  value })
	});

	const vorigeSchulform: ComputedRef<Schulform | undefined> = computed(() => {
		const vorigeAllgHerkunft = props.data.daten?.vorigeAllgHerkunft;
		if (vorigeAllgHerkunft === undefined)
			return undefined;
		const sgl = Schulgliederung.getByKuerzel(vorigeAllgHerkunft);
		if (sgl !== null)
			return Schulform.BK;
		return Schulform.getByKuerzel(vorigeAllgHerkunft) || undefined;
	});
	const vorigeAllgHerkunft: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeAllgHerkunft?.toString(),
		set: (value) => props.data.patch({ vorigeAllgHerkunft:  value })
	});

	const vorigeEntlassDatum: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeEntlassdatum?.toString(),
		set: (value) => props.data.patch({ vorigeEntlassdatum:  value })
	});

	const vorigeEntlassjahrgang: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeEntlassjahrgang?.toString(),
		set: (value) => props.data.patch({ vorigeEntlassjahrgang:  value })
	});

	const herkunftsarten:ComputedRef<Herkunftsarten[]> = computed(() => {
		return Herkunftsarten.values().filter(h => getBezeichnung(h) !== null);
	});
	const vorigeArtLetzteVersetzung: WritableComputedRef<Herkunftsarten | undefined> = computed({
		get: () => {
			if ((props.data.daten === undefined) || (props.data.daten.vorigeArtLetzteVersetzung === null))
				return undefined;
			const artID = parseInt(props.data.daten.vorigeArtLetzteVersetzung.valueOf());
			return  Herkunftsarten.getByID(artID) || undefined;
		},
		set: (value) => props.data.patch({ vorigeArtLetzteVersetzung:  value === undefined || value === null ? null : "" + value.daten.id.valueOf() })
	});
	function getBezeichnung(h: Herkunftsarten) {
		return h.getBezeichnung(vorigeSchulform.value || Schulform.G);
	}

	const vorigeBemerkung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeBemerkung?.toString(),
		set: (value) => props.data.patch({ vorigeBemerkung:  value })
	});

	const vorigeEntlassgrundID: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.vorigeEntlassgrundID?.valueOf(),
		set: (value) => props.data.patch({ vorigeEntlassgrundID: value })
	});

	const vorigeAbschlussartID: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.vorigeAbschlussartID?.toString(),
		set: (value) => props.data.patch({ vorigeAbschlussartID:  value })
	});

</script>

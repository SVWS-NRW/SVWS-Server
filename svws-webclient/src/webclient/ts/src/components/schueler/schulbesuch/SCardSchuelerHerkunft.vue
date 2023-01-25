<template>
	<svws-ui-content-card title="Vor der Aufnahme besuchte Schule/Einrichtung/Herkunftsarten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" v-model="vorigeSchulnummer" type="text" />
			<svws-ui-text-input placeholder="allgemeine Herkunft" v-model="vorigeAllgHerkunft" type="text" />
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Entlassen am" v-model="vorigeEntlassDatum" type="date" />
				<svws-ui-text-input placeholder="Entlassjahrgang" v-model="vorigeEntlassjahrgang" type="number" />
				<svws-ui-multi-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" />
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

	import { Herkunftsarten, Schulform } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { mainApp } from "~/apps/Main";
	import { PropDataSchulbesuch } from "./PropDataSchulbesuch";

	const props = defineProps<{
		data: PropDataSchulbesuch;
	}>();

	const vorigeSchulnummer = props.data.vorigeSchulnummer();
	const vorigeSchulform = props.data.vorigeSchulform();
	const vorigeAllgHerkunft = props.data.vorigeAllgHerkunft();
	const vorigeEntlassDatum = props.data.vorigeEntlassDatum();
	const vorigeEntlassjahrgang = props.data.vorigeEntlassjahrgang();
	const vorigeArtLetzteVersetzung = props.data.vorigeArtLetzteVersetzung();
	const vorigeBemerkung = props.data.vorigeBemerkung();
	const vorigeEntlassgrundID = props.data.vorigeEntlassgrundID();
	const vorigeAbschlussartID = props.data.vorigeAbschlussartID();

	const schulform: ComputedRef<Schulform> = computed(() => mainApp.apps.schule.schuleStammdaten.schulform.value || Schulform.G);

	const herkunftsarten:ComputedRef<Herkunftsarten[]> = computed(() => {
		return Herkunftsarten.values().filter(h => getBezeichnung(h) !== null);
	});

	function getBezeichnung(h: Herkunftsarten) {
		return h.getBezeichnung(vorigeSchulform.value || Schulform.G);
	}

	const showstatistic: ComputedRef<boolean> = computed(() => true);

</script>

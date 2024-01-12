<template>
	<div class="page--content">
		<svws-ui-content-card title="Schulangaben" v-if="auswahl !== undefined">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kuerzel" :model-value="auswahl.kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel || null })" />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="auswahl.kurzbezeichnung" @change="kurzbezeichnung => patch({ kurzbezeichnung: kurzbezeichnung || null })" />
				<svws-ui-text-input placeholder="Alle Vornamen" :model-value="auswahl.schulnummer" @change="schulnummer => patch({ schulnummer })" />
				<svws-ui-text-input placeholder="Schulnummer" :model-value="auswahl.name" @change="name => name && patch({name})" />
				<svws-ui-select title="Schulform" :model-value="Schulform.getByNummer(String(auswahl.schulformID || 1))" :items="Schulform.values()" :item-text="i=>i.daten.bezeichnung" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenProps } from "./SSchuleDatenProps";
	import { Schulform } from "@core"

	const props = defineProps<SchuleDatenProps>();

</script>

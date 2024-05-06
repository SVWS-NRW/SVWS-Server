<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-input-number placeholder="ID" :model-value="data.id" disabled />
				<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({kuerzel})" type="text" />
				<svws-ui-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="Foerderschwerpunkt.values()"
					:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' : ' + i.daten.beschreibung" required statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunktDatenProps } from "./SFoerderschwerpunktDatenProps";
	import { Foerderschwerpunkt } from "@core";
	import { computed } from "vue";

	const props = defineProps<FoerderschwerpunktDatenProps>();

	const statistikEintrag = computed<Foerderschwerpunkt | undefined>({
		get: () => Foerderschwerpunkt.getByKuerzel(props.data.kuerzelStatistik) ?? undefined,
		set: (value) => void props.patch({ kuerzelStatistik: value?.daten.kuerzel })
	});

</script>

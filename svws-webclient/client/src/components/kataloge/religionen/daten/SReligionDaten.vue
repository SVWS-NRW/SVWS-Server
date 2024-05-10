<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel" :model-value="auswahl.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" span="full" />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl.text" @change="text=>patch({text})" type="text" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="auswahl.textZeugnis" @change="textZeugnis=>patch({textZeugnis})" type="text" />
				<svws-ui-input-wrapper>
					<svws-ui-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="Religion.values()" :item-text="getStatistikText" required statistics />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-input-number placeholder="Sortierung" :model-value="auswahl.sortierung" @change="value => patch({ sortierung: value === null ? 32000 : value })" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="auswahl.istSichtbar" @update:model-value="value => patch({ istSichtbar: value === true ? true : false })">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ReligionDatenProps } from "./SReligionDatenProps";
	import { Religion } from "@core";
	import { computed } from "vue";

	const props = defineProps<ReligionDatenProps>();

	const inputStatistikKuerzel = computed<Religion | undefined>({
		get: () => Religion.getByKuerzel(props.auswahl.kuerzel || null) || undefined,
		set: (value) => void props.patch({ kuerzel: value?.daten.kuerzel || null })
	});

	function getStatistikText(r : Religion) : string {
		return r.daten.kuerzel + ' : ' + r.daten.bezeichnung;
	}

</script>

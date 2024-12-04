<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-select focus-id="contentFocusField" title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="Religion.values()" :item-text="getStatistikText" required statistics />
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl.text" @change="text=>patch({text})" type="text" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="auswahl.textZeugnis" @change="textZeugnis=>patch({textZeugnis})" type="text" />
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

	import { computed } from "vue";
	import type { ReligionDatenProps } from "./SReligionDatenProps";
	import { Religion } from "@core";

	const props = defineProps<ReligionDatenProps>();

	const schuljahr = computed<number>(() => props.religionListeManager().getSchuljahr());

	const auswahl = computed(() => props.religionListeManager().auswahl());

	const inputStatistikKuerzel = computed<Religion | null>({
		get: () => auswahl.value.kuerzel !== null ? Religion.data().getWertByKuerzel(auswahl.value.kuerzel) : null,
		set: (value) => void props.patch({ kuerzel: value?.daten(schuljahr.value)?.kuerzel ?? null })
	});

	function getStatistikText(r: Religion): string {
		return (r.daten(schuljahr.value)?.kuerzel ?? '—') + ' : ' + (r.daten(schuljahr.value)?.text ?? '—');
	}

</script>

<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="Religion.values()" :item-text="getStatistikText"
						required statistics focus-class-content :readonly />
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl.bezeichnung" :readonly
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? '' })" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="auswahl.bezeichnungZeugnis" :readonly
					@change="bezeichnungZeugnis => patch({ bezeichnungZeugnis })" />
				<svws-ui-spacing />
				<svws-ui-input-number placeholder="Sortierung" :model-value="auswahl.sortierung" :readonly
					@change="sortierung => patch({ sortierung: sortierung === null ? 32000 : sortierung })" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="auswahl.istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { ReligionDatenProps } from "./SReligionDatenProps";
	import {BenutzerKompetenz, Religion} from "@core";

	const props = defineProps<ReligionDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const schuljahr = computed<number>(() => props.religionListeManager().getSchuljahr());

	const auswahl = computed(() => props.religionListeManager().auswahl());

	const inputStatistikKuerzel = computed<Religion | null>({
		get: () => auswahl.value.kuerzel !== null ? Religion.data().getWertByKuerzel(auswahl.value.kuerzel) : null,
		set: (value) => void props.patch({ kuerzel: value?.daten(schuljahr.value)?.kuerzel ?? null }),
	});

	function getStatistikText(r: Religion): string {
		return (r.daten(schuljahr.value)?.kuerzel ?? '—') + ' : ' + (r.daten(schuljahr.value)?.text ?? '—');
	}

</script>

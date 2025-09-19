<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-select title="ASD-Statistik-Religion" v-model="selectedReligion" :items="manager().getAvailableReligionenForPatch()" :item-text="religionText"
					required statistics focus-class-content :readonly />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl.bezeichnung" :readonly required :min-len="1" :max-len="30"
					@change="v => patch({ bezeichnung: v?.trim() ?? '' })" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="auswahl.bezeichnungZeugnis" :readonly :max-len="50"
					@change="v => patch({ bezeichnungZeugnis: v?.trim() ?? '' })" />
				<svws-ui-spacing />
				<svws-ui-input-number placeholder="Sortierung" :model-value="auswahl.sortierung" :readonly
					@change="v => patch({ sortierung: v === null ? 32000 : v })" />
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
	import type { ReligionenDatenProps } from "./SReligionenDatenProps";
	import { BenutzerKompetenz, Religion } from "@core";

	const props = defineProps<ReligionenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const auswahl = computed(() => props.manager().auswahl());

	const selectedReligion = computed<Religion | null>({
		get: () => auswahl.value.kuerzel !== null ? Religion.data().getWertByKuerzel(auswahl.value.kuerzel) : null,
		set: (value) => void props.patch({ kuerzel: value?.daten(schuljahr.value)?.kuerzel ?? null }),
	});

	function religionText(r: Religion): string {
		return (r.daten(schuljahr.value)?.kuerzel ?? '—') + ' : ' + (r.daten(schuljahr.value)?.text ?? '—');
	}

</script>

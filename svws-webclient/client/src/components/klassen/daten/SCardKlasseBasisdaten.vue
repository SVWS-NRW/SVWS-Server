<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-checkbox :model-value="data.istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({ kuerzel })" type="text" />
			<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @change="beschreibung => patch({ beschreibung })" type="text" />
			<svws-ui-select title="Jahrgang" v-model="jahrgang" :items="klassenListeManager().jahrgaenge.list()" :item-text="item => item.kuerzel ?? ''" />
			<svws-ui-text-input placeholder="Parallelität" :model-value="data.parallelitaet" @change="parallelitaet => patch({ parallelitaet })" type="text" />
			<svws-ui-input-number placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung => (sortierung !== null) && patch({ sortierung })" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { JahrgangsListeEintrag, KlassenDaten, KlassenListeManager } from "@core";

	const props = defineProps<{
		patch: (data : Partial<KlassenDaten>) => Promise<void>;
		klassenListeManager: () => KlassenListeManager;
	}>();

	const data = computed<KlassenDaten>(() => props.klassenListeManager().daten());

	const jahrgang = computed<JahrgangsListeEintrag | null>({
		get: () => ((data.value === undefined) || (data.value.idJahrgang === null)) ? null : props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang),
		set: (value) => void props.patch({ idJahrgang: value?.id ?? null })
	});

</script>

<template>
	<svws-ui-content-card title="Beratung" class="mt-9">
		<svws-ui-input-wrapper>
			<svws-ui-text-input v-model="beratungsdatum" type="date" placeholder="Beratungsdatum" @update:model-value="dirty = true" />
			<svws-ui-textarea-input placeholder="Kommentar" v-model="kommentar" resizeable="vertical" :autoresize="true" @update:model-value="dirty = true" />
			<svws-ui-spacing />
			<svws-ui-multi-select :items="mapLehrer.values()" v-model="beratungslehrer" :item-text="(i: LehrerListeEintrag)=>i.kuerzel" @update:model-value="dirty = true" :item-filter="filter" removable autocomplete title="Letzte Beratung durchgeführt von" />
			<svws-ui-spacing />
			<svws-ui-button :disabled="!dirty" @click="speichern()">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@svws-nrw/svws-core";
	import { GostLaufbahnplanungBeratungsdaten } from "@svws-nrw/svws-core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		id?: number;
	}>();

	const beratungsdaten = ref(new GostLaufbahnplanungBeratungsdaten());
	const beratungsdatum = computed({
		get: () => props.gostLaufbahnBeratungsdaten().beratungsdatum || new Date().toISOString().slice(0, -14),
		set: value => beratungsdaten.value.beratungsdatum = value
	})
	const kommentar = computed({
		get: ()=>props.gostLaufbahnBeratungsdaten().kommentar || "",
		set: value => beratungsdaten.value.kommentar = value
	})
	const beratungslehrer = computed({
		get: ()=>props.mapLehrer.get(props.gostLaufbahnBeratungsdaten().beratungslehrerID || props.id || -1),
		set: value => beratungsdaten.value.beratungslehrerID = value?.id || null
	});
	const dirty = ref(false);

	async function speichern() {
		await props.patchBeratungsdaten(beratungsdaten.value);
		dirty.value = false;
	}

	const filter = (items: LehrerListeEintrag[], search: string) =>
		items.filter(i =>
			i.kuerzel.includes(search.toLocaleLowerCase()) ||
			i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase()));
</script>

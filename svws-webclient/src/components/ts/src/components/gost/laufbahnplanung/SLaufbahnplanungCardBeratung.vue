<template>
	<svws-ui-content-card title="Beratung" class="mt-9">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-multi-select :items="mapLehrer.values()" :model-value="mapLehrer.get(gostLaufbahnBeratungsdaten().beratungslehrerID || props.id || -1)" :item-text="(i: LehrerListeEintrag)=>`${i.kuerzel} (${i.vorname} ${i.nachname})`" @update:model-value="beratungsdaten.beratungslehrerID = $event.id || null" :item-filter="filter" removable autocomplete title="Letzte Beratung durchgeführt von" />
			<svws-ui-text-input :model-value="gostLaufbahnBeratungsdaten().beratungsdatum || new Date().toISOString().slice(0, -14)" type="date" placeholder="Beratungsdatum" @update:model-value="beratungsdaten.beratungsdatum = $event" />
			<svws-ui-textarea-input placeholder="Kommentar" :model-value="gostLaufbahnBeratungsdaten().kommentar" :autoresize="true"
				@change="(kommentar: string | null) => beratungsdaten.kommentar = kommentar" span="full" />
			<svws-ui-button @click="speichern()">Beratungsdaten speichern</svws-ui-button>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@core";
	import { GostLaufbahnplanungBeratungsdaten } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
		patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
		mapLehrer: Map<number, LehrerListeEintrag>;
		id?: number;
	}>();

	const beratungsdaten = ref(new GostLaufbahnplanungBeratungsdaten());

	async function speichern() {
		await props.patchBeratungsdaten(beratungsdaten.value);
	}

	const filter = (items: LehrerListeEintrag[], search: string) =>
		items.filter(i => i.kuerzel.includes(search.toLocaleLowerCase()) || i.nachname?.toLocaleLowerCase().includes(search.toLocaleLowerCase()));


</script>

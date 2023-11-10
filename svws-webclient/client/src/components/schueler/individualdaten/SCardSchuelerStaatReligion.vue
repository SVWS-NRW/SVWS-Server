<template>
	<svws-ui-content-card title="Staatsangehörigkeit und Konfession">
		<template #actions>
			<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse">Konfession aufs Zeugnis</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select v-model="staatsangehoerigkeit" title="1. Staatsangehörigkeit" autocomplete
				:items="Nationalitaeten.values()" :item-text="i => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" required statistics />
			<svws-ui-select v-model="staatsangehoerigkeit2" title="2. Staatsangehörigkeit" autocomplete
				:items="Nationalitaeten.values()" :item-text="i => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
			<svws-ui-select v-model="religion" title="Konfession" :items="mapReligionen" :item-text="i=>i.text ?? ''" required class="col-span-full" statistics />
			<svws-ui-text-input :model-value="data.religionabmeldung" @change="religionabmeldung => patch({religionabmeldung})" type="date" placeholder="Abmeldung vom Religionsunterricht" />
			<svws-ui-text-input :model-value="data.religionanmeldung" @change="religionanmeldung => patch({religionanmeldung})" type="date" placeholder="Wiederanmeldung" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ReligionEintrag, SchuelerListeManager, SchuelerStammdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "../../../utils/helfer";
	import { computed } from "vue";
	import { Nationalitaeten } from "@core";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
		mapReligionen: Map<number, ReligionEintrag>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const staatsangehoerigkeit2: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeit2ID: value.daten.iso3 })
	});

	const religion: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get: () => {
			const id = data.value.religionID;
			return id === null ? undefined : props.mapReligionen.get(id)
		},
		set: (value) => void props.patch({ religionID: value === undefined ? null : value.id })
	});

	const druckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get: () => data.value.druckeKonfessionAufZeugnisse,
		set: (value) => void props.patch({ druckeKonfessionAufZeugnisse: value })
	});

</script>

<template>
	<svws-ui-content-card title="Staatsangehörigkeit und Konfession">
		<template #actions>
			<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse">Konfession aufs Zeugnis</svws-ui-checkbox>
		</template>
		<div class="input-wrapper">
			<svws-ui-multi-select v-model="staatsangehoerigkeit" title="1. Staatsangehörigkeit" autocomplete
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
				required />
			<svws-ui-multi-select v-model="staatsangehoerigkeit2" title="2. Staatsangehörigkeit" autocomplete
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
			<div class="col-span-2">
				<svws-ui-multi-select v-model="religion" title="Konfession" :items="mapReligionen" :item-text="i=>i.text ?? ''" required />
			</div>
			<svws-ui-text-input v-model="religionAbmeldung" type="date"
				placeholder="Abmeldung vom Religionsunterricht" />
			<svws-ui-text-input v-model="religionAnmeldung" type="date" placeholder="Wiederanmeldung" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { ReligionEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core";
	import { Nationalitaeten } from "@svws-nrw/svws-core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "../../../helfer";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
		mapReligionen: Map<number, ReligionEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data().staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => doPatch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const staatsangehoerigkeit2: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data().staatsangehoerigkeit2ID) || Nationalitaeten.DEU,
		set: (value) => doPatch({ staatsangehoerigkeit2ID: value.daten.iso3 })
	});

	const religion: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get: () => {
			const id = props.data().religionID;
			return id === null ? undefined : props.mapReligionen.get(id)
		},
		set: (value) => doPatch({ religionID: value === undefined ? null : value.id })
	});

	const religionAbmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data().religionabmeldung ?? undefined,
		set: (value) => doPatch({ religionabmeldung: value })
	});

	const religionAnmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data().religionanmeldung ?? undefined,
		set: (value) => doPatch({ religionanmeldung: value })
	});

	const druckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get: () => props.data().druckeKonfessionAufZeugnisse,
		set: (value) => doPatch({ druckeKonfessionAufZeugnisse: value })
	});

</script>

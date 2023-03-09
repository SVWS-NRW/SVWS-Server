<template>
	<svws-ui-content-card title="Staatsangehörigkeit und Konfession">
		<div class="input-wrapper">
			<svws-ui-multi-select v-model="staatsangehoerigkeit" title="1. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
				required />
			<svws-ui-multi-select v-model="staatsangehoerigkeit2" title="2. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
			<svws-ui-multi-select v-model="religion" title="Konfession" :items="mapReligionen" required />
			<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse">Konfession aufs Zeugnis</svws-ui-checkbox>
			<svws-ui-text-input v-model="religionAbmeldung" type="date"
				placeholder="Abmeldung vom Religionsunterricht" />
			<svws-ui-text-input v-model="religionAnmeldung" type="date" placeholder="Wiederanmeldung" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { Nationalitaeten, ReligionEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "../../../helfer";

	const props = defineProps<{
		data: SchuelerStammdaten;
		mapReligionen: Map<number, ReligionEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => doPatch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const staatsangehoerigkeit2: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data.staatsangehoerigkeit2ID) || Nationalitaeten.DEU,
		set: (value) => doPatch({ staatsangehoerigkeit2ID: value.daten.iso3 })
	});

	const religion: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get: () => props.data.religionID === null ? undefined : props.mapReligionen.get(props.data.religionID),
		set: (value) => doPatch({ religionID: value === undefined ? null : value.id })
	});

	const religionAbmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.religionabmeldung ?? undefined,
		set: (value) => doPatch({ religionabmeldung: value })
	});

	const religionAnmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.religionanmeldung ?? undefined,
		set: (value) => doPatch({ religionanmeldung: value })
	});

	const druckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get: () => props.data.druckeKonfessionAufZeugnisse,
		set: (value) => doPatch({ druckeKonfessionAufZeugnisse: value })
	});

</script>

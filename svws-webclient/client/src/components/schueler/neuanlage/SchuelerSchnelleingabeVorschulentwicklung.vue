<template>
	<svws-ui-content-card title="Vorschulentwicklung" class="col-span-full">
		<svws-ui-input-wrapper :grid="2">
			<ui-select label="Name des Kindergartens"
				v-model="selectedKindergarten"
				:manager="kindergartenManager"
				searchable />
			<ui-select label="Dauer des Kindergartenbesuchs"
				v-model="selectedDauer"
				:manager="dauerManager"
				searchable />
			<svws-ui-spacing />
			<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurss"
				v-model="verpflichtungSprachfoerderkurs">
				Verpflichtung f. Sprachförderkurs
			</svws-ui-checkbox>
			<svws-ui-checkbox title="Teilnahme an Sprachförderkurs"
				v-model="teilnahmeSprachfoerderkurs">
				Teilnahme an Sprachförderkurs
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { CoreTypeSelectManager, SelectManager, useAbschnittState } from "@ui";
	import type { SchuelerSchnelleingabeManager } from "@ui";
	import { computed, ref } from "vue";
	import type { Kindergarten, KindergartenbesuchKatalogEintrag, SchuelerSchulbesuchsdaten } from "@core";
	import { Kindergartenbesuch } from "@core";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		patchSchulbesuchsdaten: (data: Partial<SchuelerSchulbesuchsdaten>, idSchueler: number) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();

	const manager = () => props.manager();
	const schulbesuch = ref(manager().schulbesuchsdaten);

	const selectedKindergarten = computed<Kindergarten | null>({
		get: () => props.manager().kindergaertenById.get(schulbesuch.value.idKindergarten ?? -1) ?? null,
		set: (value: Kindergarten | null) => {
			schulbesuch.value.idKindergarten = value?.id ?? null;
			void props.patchSchulbesuchsdaten({ idKindergarten: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const selectedDauer = computed<KindergartenbesuchKatalogEintrag | null>({
		get: () => Kindergartenbesuch.data().getEintragByID(schulbesuch.value.idDauerKindergartenbesuch ?? -1) ?? null,
		set: (value: KindergartenbesuchKatalogEintrag | null) => {
			schulbesuch.value.idDauerKindergartenbesuch = value?.id ?? null;
			void props.patchSchulbesuchsdaten({ idDauerKindergartenbesuch: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const verpflichtungSprachfoerderkurs = computed<boolean>({
		get: () => manager().schulbesuchsdaten.verpflichtungSprachfoerderkurs,
		set: (verpflichtungSprachfoerderkurs: boolean) => void props.patchSchulbesuchsdaten({ verpflichtungSprachfoerderkurs }, manager().stammdaten.id),
	});

	const teilnahmeSprachfoerderkurs = computed<boolean>({
		get: () => manager().schulbesuchsdaten.teilnahmeSprachfoerderkurs,
		set: (teilnahmeSprachfoerderkurs: boolean) => void props.patchSchulbesuchsdaten({ teilnahmeSprachfoerderkurs }, manager().stammdaten.id),
	});

	const dauerManager = new CoreTypeSelectManager({
		clazz: Kindergartenbesuch.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const kindergartenManager = new SelectManager({
		options: manager().kindergaertenById.values(),
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

</script>

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
			<svws-ui-checkbox v-model="verpflichtungSprachfoerderkurs">
				Verpflichtung für Sprachförderkurs
			</svws-ui-checkbox>
			<svws-ui-checkbox v-model="teilnahmeSprachfoerderkurs">
				Teilnahme an Sprachförderkurs
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
	import type { KindergartenbesuchKatalogEintrag } from "@core/asd/data/schule/KindergartenbesuchKatalogEintrag";
	import { Kindergartenbesuch } from "@core/asd/types/schule/Kindergartenbesuch";
	import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";
	import { computed, ref } from "vue";

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

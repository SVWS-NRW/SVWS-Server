<template>
	<svws-ui-content-card title="Kindergartenbesuch">
		<svws-ui-input-wrapper :grid="2">
			<ui-select label="Name des Kindergartens"
				:manager="kindergartenManager"
				v-model="model.idKindergarten.value"
				:readonly />
			<ui-select label="Dauer des Kindergartenbesuchs"
				:manager="dauerKindergartenbesuchManager"
				v-model="model.idDauerKindergartenbesuch.value"
				:readonly />
			<svws-ui-checkbox v-model="model.proxy.verpflichtungSprachfoerderkurs" :readonly>
				Verpflichtung für Sprachförderkurs
			</svws-ui-checkbox>
			<svws-ui-checkbox v-model="model.proxy.teilnahmeSprachfoerderkurs" :readonly>
				Teilnahme an Sprachförderkurs
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { Kindergartenbesuch } from "@core/asd/types/schule/Kindergartenbesuch";
	import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useKindergaertenState } from "@ui/states/kataloge/KindergaertenState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import type { SchuelerSchulbesuchModelProxy } from "./modelProxy/SchuelerSchulbesuchModelProxy";

	defineProps<{
		model: SchuelerSchulbesuchModelProxy;
		readonly: boolean;
	}>();

	const abschnittState = useAbschnittState();
	const kindergaertenState = useKindergaertenState();

	const kindergartenManager = new SelectManager<Kindergarten>({
		options: computed(() => kindergaertenState.kindergaerten.list),
		optionDisplayText: s => s.bezeichnung,
		selectionDisplayText: s => s.bezeichnung,
	});

	const dauerKindergartenbesuchManager = new CoreTypeSelectManager({
		clazz: Kindergartenbesuch.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>

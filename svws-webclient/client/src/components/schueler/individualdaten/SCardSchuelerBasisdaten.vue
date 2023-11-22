<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Nachname" :model-value="data.nachname" @change="nachname => patch({ nachname })" type="text" />
			<svws-ui-text-input placeholder="Rufname" :model-value="data.vorname" @change="vorname => patch({ vorname })" type="text" />
			<svws-ui-text-input placeholder="Alle Vornamen" :model-value="data.alleVornamen" @change="alleVornamen => patch({ alleVornamen })" type="text" />
			<svws-ui-spacing />
			<svws-ui-select title="Geschlecht" v-model="geschlecht" :items="Geschlecht.values()" statistics :item-text="(i: Geschlecht)=>i.text" />
			<svws-ui-text-input placeholder="Geburtsdatum" :model-value="data.geburtsdatum" @change="geburtsdatum => patch({geburtsdatum})" type="date" :valid="istGeburtsdatumGueltig" required statistics />
			<svws-ui-text-input placeholder="Geburtsort" :model-value="data.geburtsort" @change="geburtsort => patch({ geburtsort })" type="text" />
			<svws-ui-text-input placeholder="Geburtsname" :model-value="data.geburtsname" @change="geburtsname => patch({ geburtsname })" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerListeManager, SchuelerStammdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import type { InputDataType } from "@ui";
	import { computed } from "vue";
	import { DateUtils, Geschlecht } from "@core";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const istGeburtsdatumGueltig = (strDate: InputDataType) => {
		if (strDate === null || typeof strDate === 'number')
			return true;
		try {
			const date = DateUtils.extractFromDateISO8601(strDate);
			const curDate = new Date();
			const diffYear = curDate.getFullYear() - date[0];
			return (diffYear > 3) && (diffYear < 51);
		} catch (e) {
			return false;
		}
	};

	const geschlecht: WritableComputedRef<Geschlecht> = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id })
	});

</script>

<template>
	<svws-ui-table :columns :items="personaldaten.lehraemter" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(lehramt)="{ rowData }">
			{{ getLehramt(rowData).daten(schuljahr)?.text ?? '—' }}
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Lehramt" v-if="hatUpdateKompetenz" :model-value="getLehramtAnerkennung(rowData)"
				@update:model-value="anerkennung => patchLehramtAnerkennung(rowData, anerkennung ?? null)"
				:items="LehrerLehramtAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			<div v-else> {{ getLehramtAnerkennung(rowData)?.daten(schuljahr)?.text ?? '—' }} </div>
		</template>
		<template #actions v-if="hatUpdateKompetenz">
			<svws-ui-button @click="removeLehraemter(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
			<svws-ui-button @click="show = true" type="icon" title="Lehramt hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-lehraemter-modal-add v-if="hatUpdateKompetenz" v-model:show="show" :id-lehrer="personaldaten.id" :add-lehramt :schuljahr />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, LehrerLehramtEintrag, LehrerListeManager, LehrerPersonaldaten} from "@core";
	import { LehrerLehramt, LehrerLehramtAnerkennung, Arrays } from "@core";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		lehrerListeManager: () => LehrerListeManager;
		patchLehramtAnerkennung: (eintrag: LehrerLehramtEintrag, anerkennung : LehrerLehramtAnerkennung | null) => Promise<void>;
		addLehramt: (eintrag: LehrerLehramtEintrag) => Promise<void>;
		removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
		schuljahr: number;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const show = ref<boolean>(false);

	const selected = ref<LehrerLehramtEintrag[]>([]);

	const columns = [
		{key: 'lehramt', label: 'Lehramt', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 }
	]

	function getLehramt(eintrag: LehrerLehramtEintrag) : LehrerLehramt {
		const lehramt = LehrerLehramt.data().getWertByID(eintrag.idLehramt);
		return lehramt;
	}

	function getLehramtAnerkennung(eintrag: LehrerLehramtEintrag) : LehrerLehramtAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerLehramtAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund));
	}

</script>

<template>
	<svws-ui-table :columns="cols" :items="personaldaten.lehraemter" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(lehramt)="{ rowData }">
			{{ getLehramt(rowData).daten.text }}
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Lehramt" v-if="hatUpdateKompetenz" :model-value="getLehramtAnerkennung(rowData)"
				@update:model-value="anerkennung => patchLehramtAnerkennung(rowData, anerkennung ?? null)"
				:items="LehrerLehramtAnerkennung.values()" :item-text="(i: LehrerLehramtAnerkennung) => i.daten.text" headless />
			<div v-else> {{ getLehramtAnerkennung(rowData)?.daten.text ?? '—' }} </div>
		</template>
		<template #actions v-if="hatUpdateKompetenz">
			<svws-ui-button @click="removeLehraemter(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
			<svws-ui-button @click="showModal().value = true" type="icon" title="Lehramt hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-lehraemter-modal-add v-if="hatUpdateKompetenz" :show="showModal" :id-lehrer="personaldaten.id" :add-lehramt="addLehramt" />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { DeveloperNotificationException, LehrerLehramt, LehrerLehramtAnerkennung,
		type List, type LehrerLehramtEintrag, type LehrerListeManager, type LehrerPersonaldaten, Arrays
	} from "@core";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		lehrerListeManager: () => LehrerListeManager;
		patchLehramtAnerkennung: (eintrag: LehrerLehramtEintrag, anerkennung : LehrerLehramtAnerkennung | null) => Promise<void>;
		addLehramt: (eintrag: LehrerLehramtEintrag) => Promise<void>;
		removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const _modal = ref<boolean>(false);
	const showModal = () => _modal;

	const selected = ref<LehrerLehramtEintrag[]>([]);

	const cols = [
		{key: 'lehramt', label: 'Lehramt', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 }
	]

	function getLehramt(eintrag: LehrerLehramtEintrag) : LehrerLehramt {
		const lehramt = LehrerLehramt.getByID(eintrag.idLehramt);
		if (lehramt === null)
			throw new DeveloperNotificationException("Die ID des Lehramtes ist unzulässig. Die vorhandenen Daten sind inkonsistent.");
		return lehramt;
	}

	function getLehramtAnerkennung(eintrag: LehrerLehramtEintrag) : LehrerLehramtAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerLehramtAnerkennung.getByID(eintrag.idAnerkennungsgrund));
	}

</script>

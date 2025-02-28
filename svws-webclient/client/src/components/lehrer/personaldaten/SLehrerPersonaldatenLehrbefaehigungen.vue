<template>
	<svws-ui-table :columns :items="personaldaten.lehrbefaehigungen" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(lehrbefaehigung)="{ rowData }">
			{{ getLehrbefaehigung(rowData).daten(schuljahr)?.text ?? '—' }}
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Lehrbefähigung" v-if="hatUpdateKompetenz" :model-value="getLehrbefaehigungAnerkennung(rowData)"
				@update:model-value="anerkennung => patchLehrbefaehigungAnerkennung(rowData, anerkennung ?? null)"
				:items="LehrerLehrbefaehigungAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			<div v-else> {{ getLehrbefaehigungAnerkennung(rowData)?.daten(schuljahr)?.text ?? '—' }} </div>
		</template>
		<template #actions v-if="hatUpdateKompetenz">
			<div class="inline-flex gap-4">
				<svws-ui-button @click="removeLehrbefaehigungen(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
				<svws-ui-button @click="show = true" type="icon" title="Lehrbefähigung hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
			</div>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-lehrbefaehigungen-modal-add v-if="hatUpdateKompetenz" v-model:show="show" :id-lehrer="personaldaten.id" :add-lehrbefaehigung :schuljahr />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, LehrerLehrbefaehigungEintrag, LehrerListeManager, LehrerPersonaldaten} from "@core";
	import { LehrerLehrbefaehigung, LehrerLehrbefaehigungAnerkennung, Arrays } from "@core";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		lehrerListeManager: () => LehrerListeManager;
		patchLehrbefaehigungAnerkennung: (eintrag: LehrerLehrbefaehigungEintrag, anerkennung : LehrerLehrbefaehigungAnerkennung | null) => Promise<void>;
		addLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag) => Promise<void>;
		removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		schuljahr: number;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const show = ref<boolean>(false);

	const selected = ref<LehrerLehrbefaehigungEintrag[]>([]);

	const columns = [
		{key: 'lehrbefaehigung', label: 'Lehrbefähigung', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 },
	]

	function getLehrbefaehigung(eintrag: LehrerLehrbefaehigungEintrag) : LehrerLehrbefaehigung {
		const lehrbefaehigung = LehrerLehrbefaehigung.data().getWertByID(eintrag.idLehrbefaehigung);
		return lehrbefaehigung;
	}

	function getLehrbefaehigungAnerkennung(eintrag: LehrerLehrbefaehigungEintrag) : LehrerLehrbefaehigungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerLehrbefaehigungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund));
	}

</script>

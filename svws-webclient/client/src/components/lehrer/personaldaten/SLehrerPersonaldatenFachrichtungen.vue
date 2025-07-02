<template>
	<svws-ui-table :columns :items="personaldaten.fachrichtungen" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(fachrichtung)="{ rowData }">
			{{ getFachrichtung(rowData).daten(schuljahr)?.text ?? '—' }}
		</template>
		<template #cell(lehramt)="{ rowData }">
			<svws-ui-select title="Fachrichtung durch Lehramt" v-if="hatUpdateKompetenz" :model-value="getLehramt(rowData)"
				@update:model-value="lehramt => patchFachrichtung(rowData, { idLehramt: lehramt?.daten(schuljahr)?.id ?? -1 })"
				:items="getLehraemter()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			<div v-else> {{ getLehramt(rowData)?.daten(schuljahr)?.text ?? '—' }} </div>
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Fachrichtung" v-if="hatUpdateKompetenz" :model-value="getFachrichtungAnerkennung(rowData)"
				@update:model-value="anerkennung => patchFachrichtung(rowData, { idAnerkennungsgrund: anerkennung?.daten(schuljahr)?.id ?? null })"
				:items="LehrerFachrichtungAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
			<div v-else> {{ getFachrichtungAnerkennung(rowData)?.daten(schuljahr)?.text ?? '—' }} </div>
		</template>
		<template #actions v-if="hatUpdateKompetenz">
			<div class="inline-flex gap-4">
				<svws-ui-button @click="removeFachrichtungen(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
				<svws-ui-button @click="show = true" type="icon" title="Fachrichtung hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
			</div>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-fachrichtungen-modal-add v-if="hatUpdateKompetenz" v-model:show="show" :id-lehrer="personaldaten.id" :add-fachrichtung :schuljahr />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, LehrerFachrichtungEintrag, LehrerListeManager, LehrerPersonaldaten } from "@core";
	import { LehrerFachrichtung, LehrerFachrichtungAnerkennung, Arrays, LehrerLehramt, ArrayList } from "@core";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		lehrerListeManager: () => LehrerListeManager;
		patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch : Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		addFachrichtung: (eintrag: LehrerFachrichtungEintrag) => Promise<void>;
		removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
		schuljahr: number;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	function getLehraemter() : List<LehrerLehramt> {
		const result = new ArrayList<LehrerLehramt>();
		for (const l of personaldaten.value.lehraemter)
			if (l.id >= 0)
				result.add(LehrerLehramt.data().getWertByID(l.id));
		return result;
	}

	const show = ref<boolean>(false);

	const selected = ref<LehrerFachrichtungEintrag[]>([]);

	const columns = [
		{key: 'fachrichtung', label: 'Fachrichtung', span: 1, statistic: true },
		{key: 'lehramt', label: 'Lehramt', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 },
	]

	function getFachrichtung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtung {
		const fachrichtung = LehrerFachrichtung.data().getWertByID(eintrag.idFachrichtung);
		return fachrichtung;
	}

	function getLehramt(eintrag: LehrerFachrichtungEintrag) : LehrerLehramt | null {
		return (eintrag.idLehramt < 0) ? null : LehrerLehramt.data().getWertByID(eintrag.idLehramt);
	}

	function getFachrichtungAnerkennung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerFachrichtungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund));
	}

</script>

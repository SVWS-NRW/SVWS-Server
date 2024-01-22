<template>
	<svws-ui-table :columns="cols" :items="personaldaten.fachrichtungen" selectable :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(fachrichtung)="{ rowData }">
			{{ getFachrichtung(rowData).daten.text }}
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Fachrichtung" :model-value="getFachrichtungAnerkennung(rowData)" @update:model-value="anerkennung => patchFachrichtungAnerkennung(rowData, anerkennung ?? null)"
				:items="LehrerFachrichtungAnerkennung.values()" :item-text="(i: LehrerFachrichtungAnerkennung) => i.daten.text" headless />
		</template>
		<template #actions>
			<svws-ui-button @click="removeFachrichtungen(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
			<svws-ui-button @click="showModal().value = true" type="icon" title="Fachrichtung hinzufügen"> <i-ri-add-line /> </svws-ui-button>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-fachrichtungen-modal-add :show="showModal" :id-lehrer="personaldaten.id" :add-fachrichtung="addFachrichtung" />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { DeveloperNotificationException, LehrerFachrichtung, LehrerFachrichtungAnerkennung,
		type List, type LehrerFachrichtungEintrag, type LehrerListeManager, type LehrerPersonaldaten, Arrays
	} from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patchFachrichtungAnerkennung: (eintrag: LehrerFachrichtungEintrag, anerkennung : LehrerFachrichtungAnerkennung | null) => Promise<void>;
		addFachrichtung: (eintrag: LehrerFachrichtungEintrag) => Promise<void>;
		removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const _modal = ref<boolean>(false);
	const showModal = () => _modal;

	const selected = ref<LehrerFachrichtungEintrag[]>([]);

	const cols = [
		{key: 'fachrichtung', label: 'Fachrichtung', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 }
	]

	function getFachrichtung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtung {
		const fachrichtung = LehrerFachrichtung.getByID(eintrag.idFachrichtung);
		if (fachrichtung === null)
			throw new DeveloperNotificationException("Die ID der Fachrichtung ist unzulässig. Die vorhandenen Daten sind inkonsistent.");
		return fachrichtung;
	}

	function getFachrichtungAnerkennung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerFachrichtungAnerkennung.getByID(eintrag.idAnerkennungsgrund));
	}

</script>

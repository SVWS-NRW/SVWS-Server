<template>
	<svws-ui-table :columns="cols" :items="personaldaten.lehrbefaehigungen" selectable :model-value="selected" @update:model-value="selected=$event" count>
		<template #cell(lehrbefaehigung)="{ rowData }">
			{{ getLehrbefaehigung(rowData).daten.text }}
		</template>
		<template #cell(anerkennung)="{ rowData }">
			<svws-ui-select title="Anerkennungsgrund Lehrbefähigung" :model-value="getLehrbefaehigungAnerkennung(rowData)" @update:model-value="anerkennung => patchLehrbefaehigungAnerkennung(rowData, anerkennung ?? null)"
				:items="LehrerLehrbefaehigung.values()" :item-text="(i: LehrerLehrbefaehigungAnerkennung) => i.daten.text" headless />
		</template>
		<template #actions>
			<svws-ui-button @click="removeLehrbefaehigungen(Arrays.asList(selected))" type="trash" :disabled="selected.length <= 0" />
			<svws-ui-button @click="showModal().value = true" type="icon" title="Lehrbefähigung hinzufügen"> <i-ri-add-line /> </svws-ui-button>
		</template>
	</svws-ui-table>
	<s-lehrer-personaldaten-lehrbefaehigungen-modal-add :show="showModal" :id-lehrer="personaldaten.id" :add-lehrbefaehigung="addLehrbefaehigung" />
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { DeveloperNotificationException, LehrerLehrbefaehigung, LehrerLehrbefaehigungAnerkennung,
		type List, type LehrerLehrbefaehigungEintrag, type LehrerListeManager, type LehrerPersonaldaten, Arrays
	} from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patchLehrbefaehigungAnerkennung: (eintrag: LehrerLehrbefaehigungEintrag, anerkennung : LehrerLehrbefaehigungAnerkennung | null) => Promise<void>;
		addLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag) => Promise<void>;
		removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
	}>();

	const personaldaten = computed<LehrerPersonaldaten>(() => props.lehrerListeManager().personalDaten());

	const _modal = ref<boolean>(false);
	const showModal = () => _modal;

	const selected = ref<LehrerLehrbefaehigungEintrag[]>([]);

	const cols = [
		{key: 'lehrbefaehigung', label: 'Lehrbefähigung', span: 1 },
		{key: 'anerkennung', label: 'Anerkennungsgrund', span: 1 }
	]

	function getLehrbefaehigung(eintrag: LehrerLehrbefaehigungEintrag) : LehrerLehrbefaehigung {
		const lehrbefaehigung = LehrerLehrbefaehigung.getByID(eintrag.idLehrbefaehigung);
		if (lehrbefaehigung == null)
			throw new DeveloperNotificationException("Die ID der Lehrbefähigung ist unzulässig. Die vorhandenen Daten sind inkonsistent.");
		return lehrbefaehigung;
	}

	function getLehrbefaehigungAnerkennung(eintrag: LehrerLehrbefaehigungEintrag) : LehrerLehrbefaehigungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null ? null : LehrerLehrbefaehigungAnerkennung.getByID(eintrag.idAnerkennungsgrund));
	}

</script>

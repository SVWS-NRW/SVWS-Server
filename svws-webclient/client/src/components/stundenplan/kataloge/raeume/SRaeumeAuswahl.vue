<template>
	<svws-ui-table :clicked clickable @update:clicked="gotoEintrag" :items="raumListeManager().liste.list()" :columns selectable v-model="selected" class="max-w-128 min-w-96">
		<template #actions>
			<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
			<svws-ui-button type="transparent" title="Räume exportieren" @click="export_raeume" :disabled="selected.length === 0"><span class="icon-sm i-ri-upload-2-line" /></svws-ui-button>
			<s-raum-import-modal v-slot="{ openModal }" :set-katalog-raeume-import-j-s-o-n>
				<svws-ui-button type="icon" @click="openModal()">
					<span class="icon-sm i-ri-download-2-line" />
				</svws-ui-button>
			</s-raum-import-modal>
			<s-raum-neu-modal v-slot="{ openModal }" :add-raum="addEintrag">
				<svws-ui-button type="icon" @click="openModal()">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</s-raum-neu-modal>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { RaeumeAuswahlProps } from "./SRaeumeAuswahlProps";
	import { Raum } from "@core";

	const props = defineProps<RaeumeAuswahlProps>();
	const selected = ref<Raum[]>([]);

	const columns = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
		{ key: "groesse", label: "Größe", sortable: true },
	];

	const clicked = computed<Raum | undefined>(() => {
		return props.raumListeManager().hasDaten() ? props.raumListeManager().auswahl() : undefined;
	});

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	function export_raeume() {
		const arr = selected.value.map(r => Raum.transpilerToJSON(r));
		const blob = new Blob(['['+arr.toString()+']'], {
			type: "application/json",
		});
		const link = document.createElement("a");
		link.href = URL.createObjectURL(blob);
		link.download = "ExportRaeume.json";
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>

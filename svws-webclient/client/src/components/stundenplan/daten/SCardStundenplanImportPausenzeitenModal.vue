<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" :type="listPausenzeiten.size() < 1 ? 'danger' : 'default'" size="medium">
		<template #modalTitle>Pausenzeiten aus Vorlage importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listPausenzeiten.size()" :items="listPausenzeiten" :columns clickable :clicked="pausenzeit" selectable v-model="selected">
					<template #cell(wochentag)="{value}">
						{{ Wochentag.fromIDorException(value).kuerzel }}
					</template>
					<template #cell(beginn)="{value}">
						{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}
					</template>
					<template #cell(ende)="{value}">
						{{ DateUtils.getStringOfUhrzeitFromMinuten(value ?? 0) }}
					</template>
				</svws-ui-table>
				<div v-else>Importieren nicht möglich, keine (zusätzlichen) Einträge in der Pausenzeiten-Vorlage hinterlegt.</div>
				<div>Neue Einträge in der Pausenzeiten-Vorlage können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { List, StundenplanPausenzeit } from "@core";
	import { Wochentag, DateUtils } from "@core";

	const props = defineProps<{
		importPausenzeiten: (Pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: List<StundenplanPausenzeit>;
	}>();

	const columns: DataTableColumn[] = [
		{ key: "wochentag", label: "Tag", span: 1 },
		{ key: "beginn", label: "Beginn", span: 2 },
		{ key: "ende", label: "Ende", span: 2 },
		{ key: "bezeichnung", label: "Bezeichnung", span: 3 },
	];

	const show = ref<boolean>(false);

	const selected = ref<StundenplanPausenzeit[]>([]);
	const pausenzeit = ref<StundenplanPausenzeit>()

	function openModal() {
		selected.value = [...props.listPausenzeiten];
		show.value = true;
	}

	async function importer() {
		await props.importPausenzeiten(selected.value);
		show.value = false;
		selected.value = [];
	}
</script>

<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" :type="listPausenzeiten.size() < 1 ? 'danger' : 'default'" size="medium">
		<template #modalTitle>Pausenzeiten aus Katalog importieren</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-table v-if="listPausenzeiten.size()" :items="listPausenzeiten" :columns="cols" clickable :clicked="pausenzeit" selectable v-model="selected">
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
				<div v-else>Importieren nicht möglich, keine (zusätzlichen) Einträge im Pausenzeiten-Katalog hinterlegt.</div>
				<div>Neue Einträge im Pausenzeiten-Katalog können unter Schule angelegt werden</div>
				<!-- TODO Link einfügen und Beschreibung anpassen -->
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="selected.length === 0"> Ausgewählte importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { List, StundenplanPausenzeit } from "@core";
	import { Wochentag, DateUtils } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		importPausenzeiten: (Pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: List<StundenplanPausenzeit>;
	}>();

	const cols: DataTableColumn[] = [
		{ key: "wochentag", label: "Tag", span: 1 },
		{ key: "beginn", label: "Beginn", span: 2 },
		{ key: "ende", label: "Ende", span: 2 },
		{ key: "bezeichnung", label: "Bezeichnung", span: 3 },
	];

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	// eslint-disable-next-line vue/no-setup-props-destructure
	const selected = ref<StundenplanPausenzeit[]>([...props.listPausenzeiten]);
	const pausenzeit = ref<StundenplanPausenzeit>()

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.importPausenzeiten(selected.value);
		showModal().value = false;
		selected.value = [];
	}
</script>

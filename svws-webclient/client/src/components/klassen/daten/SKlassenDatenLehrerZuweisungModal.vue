<template>
	<slot :open-modal="showModal" />
	<svws-ui-modal :show="showModal" size="medium">
		<template #modalTitle>Lehrkraft als Klassenleitung hinzufügen</template>
		<template #modalContent>
			<div style="height:250pt">
				<svws-ui-table clickable v-model:clicked="clickedRow" :items="rowsFiltered.values()" :columns="cols" count scroll-into-view scroll>
					<template #search>
						<svws-ui-text-input type="search" placeholder="Lehrkraft suchen" v-model="search" />
					</template>
				</svws-ui-table>
			</div>
		</template>
		<template #modalActions>
			<div style="display: flex; justify-content: flex-end; padding-top: 4pt">
				<SvwsUiButton type="secondary" @click="closeModal()"> Abbrechen </SvwsUiButton>
				<div style="padding-left:4pt">
					<SvwsUiButton type="primary" @click="add()" :disabled="auswaehlenDeaktiviert"> Auswählen </SvwsUiButton>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { List, LehrerListeEintrag } from "@core";
	import type { SKlassenDatenLehrerZuweisungModalProps } from "~/components/klassen/daten/SKlassenDatenLehrerZuweisungModalProps";

	const props = defineProps<SKlassenDatenLehrerZuweisungModalProps>();

	const clickedRow = ref<LehrerListeEintrag | undefined>(undefined);

	const search = ref<string>("");

	const klassenleitungen = computed<List<number>>( () =>
		props.klassenListeManager().daten().klassenLeitungen
	);

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.klassenListeManager().lehrer.list())
			if ((e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				&& !klassenleitungen.value.contains(e.id))
				arr.push(e);
		return arr;
	});

	const auswaehlenDeaktiviert = computed<boolean>(() => clickedRow.value === undefined)

	const _showModal = ref<boolean>(false);

	function showModal() {
		// Benötigt damit der X Button oben das Modal zurücksetzt
		if (_showModal.value === false) {
			search.value = "";
			clickedRow.value = undefined;
		}
		return _showModal;
	}

	function closeModal() {
		search.value = "";
		clickedRow.value = undefined;
		_showModal.value = false;
	}

	async function add() {
		const klassenId = props.klassenListeManager().auswahl().id;
		await props.addKlassenleitung(clickedRow.value!.id, klassenId)
		closeModal();
	}

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 }
	];

</script>

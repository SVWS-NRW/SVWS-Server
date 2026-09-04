<template>
	<slot :open-modal />
	<svws-ui-modal :show @update:show="closeModal" size="medium">
		<template #modalTitle>Lehrkraft als Klassenleitung hinzufügen</template>
		<template #modalContent>
			<div style="height:250pt">
				<svws-ui-table clickable v-model:clicked="clickedRow" :items="rowsFiltered.values()" :columns="cols" count scroll>
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

	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { List } from "@core/java/util/List";
	import { computed, ref } from "vue";
	import type { KlassenDatenLehrerZuweisungModalProps } from "~/components/klassen/daten/KlassenDatenLehrerZuweisungModalProps";

	const props = defineProps<KlassenDatenLehrerZuweisungModalProps>();

	const clickedRow = ref<LehrerListeEintrag | undefined>(undefined);

	const search = ref<string>("");

	const klassenleitungen = computed<List<number>>(() => props.manager().daten().klassenLeitungen);

	const rowsFiltered = computed<LehrerListeEintrag[]>(() => {
		const arr = [];
		for (const e of props.manager().lehrer.list()) {
			if (!e.istAktiv) {
				continue;
			}
			if ((e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| e.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				&& !klassenleitungen.value.contains(e.id)) {
				arr.push(e);
			}
		}
		return arr;
	});

	const auswaehlenDeaktiviert = computed<boolean>(() => clickedRow.value === undefined);

	const show = ref<boolean>(false);

	function closeModal() {
		search.value = "";
		clickedRow.value = undefined;
		show.value = false;
	}

	function openModal() {
		show.value = true;
	}

	async function add() {
		const klassenId = props.manager().auswahl().id;
		await props.addKlassenleitung(clickedRow.value!.id, klassenId);
		closeModal();
	}

	const cols = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", statistic: true },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
	];

</script>

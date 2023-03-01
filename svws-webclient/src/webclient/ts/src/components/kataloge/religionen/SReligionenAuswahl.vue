<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Religionen</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="setReligion" :items="listReligionen"
					:columns="cols" clickable :footer="true">
					<template #footer>
						<button @click="modalAdd.openModal()" class="flex h-10 w-10 items-center justify-center">
							<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
						</button>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
	<svws-ui-modal ref="modalAdd" size="medium">
		<template #modalTitle>Religion Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select v-model="reli_neu.kuerzel" title="Statistikkürzel" :items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel" required />
				<svws-ui-text-input v-model="reli_neu.kuerzel" type="text" placeholder="Kürzel" />
				<svws-ui-text-input v-model="reli_neu.text" type="text" placeholder="Bezeichnung" />
				<svws-ui-text-input v-model="reli_neu.textZeugnis" type="text" placeholder="Zeugnisbezeichnung" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button v-if="reli_neu.kuerzel || reli_neu.textZeugnis || reli_neu.text" type="secondary" @click="deleteEntries()"> Felder Leeren </svws-ui-button>
			<svws-ui-button type="secondary" @click="modalAdd.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Religion, ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef, reactive, ref } from "vue";
	import { router } from "~/router/RouteManager";
	import { ReligionenAuswahlProps } from "./SReligionenAuswahlPops";

	const props = defineProps<ReligionenAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "text", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const modalAdd = ref();

	/** Modalfenster: Neue Religion */
	const reli_neu: ReligionEintrag = reactive(new ReligionEintrag());

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> = computed(() => Religion.values());

	async function saveEntries() {
		if (reli_neu.kuerzel) {
			modalAdd.value.closeModal();
			await props.addReligion(reli_neu);
		} else {
			alert("Kürzel darf nicht leer sein");
		}
		deleteEntries();
	}

	function deleteEntries() {
		reli_neu.kuerzel = null;
		reli_neu.text = null;
		reli_neu.textZeugnis = null;
	}

</script>

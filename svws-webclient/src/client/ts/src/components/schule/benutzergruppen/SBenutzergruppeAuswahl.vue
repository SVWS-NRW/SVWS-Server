<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzergruppen">Benutzergruppen</span>
			</nav>
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl()" @update:clicked="gotoBenutzergruppe" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :unique-key="String(auswahl()?.id)" filter>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel" />
				</template>
				<!-- Footer mit Button zum Hinzufügen einer Zeile -->
				<template #footerActions>
					<s-modal-benutzergruppe-neu :show-delete-icon="selectedItems.length > 0" :create-benutzergruppe="createBenutzergruppe"
						:delete-benutzergruppe_n="deleteMultipleGroup" />
				</template>
			</svws-ui-data-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, Ref, ref, ShallowRef, WritableComputedRef } from "vue";
	import { router } from "~/router/RouteManager";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";
	import { BenutzergruppeAuswahlProps } from "./SBenutzergruppeAuswahlProps";

	const props = defineProps<BenutzergruppeAuswahlProps>();

	const selectedItems: Ref<BenutzergruppeListeEintrag[]> = ref([]);

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
		{ key: "id", label: "ID", sortable: true, span: 0.5 }
	];

	const search: Ref<string> = ref("");

	const rowsFiltered: ComputedRef<Map<number, BenutzergruppeListeEintrag>> = computed(() => {
		console.log("rowsFiltered--");
		if (!search.value)
			return props.mapBenutzergruppe;
		const result = new Map<number, BenutzergruppeListeEintrag>();
		for (const l of props.mapBenutzergruppe.values()) {
			if (l.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				result.set(l.id, l);
		}
		return result;
	});

	async function deleteMultipleGroup() {
		const items = selectedItems.value;
		selectedItems.value = [];
		await props.deleteBenutzergruppe_n(items);
	}

</script>

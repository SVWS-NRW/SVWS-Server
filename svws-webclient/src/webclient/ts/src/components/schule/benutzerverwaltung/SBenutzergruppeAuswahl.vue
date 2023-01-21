<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzergruppen">Benutzergruppen</span>
			</nav>
		</template>
		<template #header>
			<div class="mb-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
		</template>
		<template #content>
			<svws-ui-table v-model="selected" v-model:selection="selectedItems" :columns="cols" :data="rowsFiltered" is-multi-select :footer="true">
				<template #footer>
					<s-modal-benutzergruppe-neu :show_delete_icon="selectedItems.length" />
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzergruppeListeEintrag, routename: string }>();

	const selected = routeSchuleBenutzergruppe.auswahl;
	const selection = ref([]);

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzergruppe.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		const rowsValue: BenutzergruppeListeEintrag[] = rows.value;
		return (search.value)
			? rowsValue.filter((e: BenutzergruppeListeEintrag) => e.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const selectedItems: WritableComputedRef<BenutzergruppeListeEintrag[]> = computed({
		get: () => app.auswahl.ausgewaehlt_gruppe,
		set: (items: BenutzergruppeListeEintrag[]) => {
			app.auswahl.ausgewaehlt_gruppe = items;
		}
	});
</script>

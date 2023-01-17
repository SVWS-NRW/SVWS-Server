<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzer">Benutzer</span>
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
			<!-- Auswahlliste für die Benutzer -->
			<svws-ui-table v-model="selected" :columns="cols" :data="rowsFiltered" v-model:selection="selectedItems" is-multi-select :footer="true">
				<template #footer>
					<s-modal-benutzer-neu :show_delete_icon="selectedItems.length > 0"/>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag, routename: string }>();

	const selected = routeSchuleBenutzer.auswahl;

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
		{ key: "name", label: "Name", sortable: true, span: 2}
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = rows.value;
		return (search.value)
			? rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const selectedItems: WritableComputedRef<BenutzerListeEintrag[]> = computed({
		get: () => app.auswahl.ausgewaehlt_gruppe,
		set: (items: BenutzerListeEintrag[]) => {
			app.auswahl.ausgewaehlt_gruppe = items;
		}
	});

</script>

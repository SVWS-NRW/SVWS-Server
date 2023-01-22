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
					<s-modal-benutzer-neu :show-delete-icon="selectedItems.length > 0" />
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, ShallowRef, WritableComputedRef } from "vue";
	import { router } from "~/router";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";

	const props = defineProps<{
		item: ShallowRef<BenutzerListeEintrag | undefined>;
	}>();

	const selected = routeSchuleBenutzer.auswahl;

	const cols = [
		{ key: "id", label: "ID", sortable: true },
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
		{ key: "name", label: "Name", sortable: true, span: 2}
	];

	const search: Ref<string> = ref("");

	const rows: ComputedRef<BenutzerListeEintrag[]> = computed(() => routeSchuleBenutzer.liste.liste || []);

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(() => {
		if (rows.value === undefined)
			return undefined;
		const rowsValue: BenutzerListeEintrag[] = rows.value;
		return (search.value)
			? rowsValue.filter((e: BenutzerListeEintrag) => e.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
			: rowsValue;
	});

	const selectedItems: WritableComputedRef<BenutzerListeEintrag[]> = computed({
		get: () => routeSchuleBenutzer.liste.ausgewaehlt_gruppe,
		set: (items) => routeSchuleBenutzer.liste.ausgewaehlt_gruppe = items
	});

</script>

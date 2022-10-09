<template>
	<svws-ui-secondary-menu>
		<template #headline> Schulverwaltung </template>
		<template #header> </template>
		<template #content>
			<div class="container mt-2">
				<svws-ui-sidebar-menu-item
					v-for="item in menu_items"
					:key="item.value"
					@click="router.push({ name: item.value })"
				>
					<template #label>
						<span>{{ item.title }}</span>
					</template>
				</svws-ui-sidebar-menu-item>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router";
	import { useAuswahlViaRoute } from "~/router/auswahlViaRoute";

	const menu_items = [
		{ title: "Schule bearbeiten", value: "schule_bearbeiten" },
		{ title: "Einstellungen", value: "einstellungen" },
		{ title: "Datenaustausch", value: "datenaustausch" },
		{ title: "Datensicherung", value: "datensicherung" },
		{ title: "Schuljahreswechsel", value: "schuljahreswechsel" },
		{ title: "Statistik IT.NRW", value: "statistikitnrw" },
		{ title: "Werkzeuge", value: "werkzeuge" },
		{ title: "Benutzerverwaltung", value: "benutzerverwaltung" },
		{ title: "Hilfe", value: "hilfe" }
	];
</script>

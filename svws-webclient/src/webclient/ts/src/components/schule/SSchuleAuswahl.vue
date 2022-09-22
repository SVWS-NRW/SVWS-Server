<template>
	<svws-ui-secondary-menu>
		<template #headline> Schulverwaltung </template>
		<template #header> </template>
		<template #content>
			<div class="container mt-2">
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Schule bearbeiten</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Einstellungen</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					@click="menubar_selected = 'schule-datenaustausch'"
					><template #label
						><span>Datenaustausch</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Datensicherung</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Schuljahreswechsel</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label><span></span></template
				></svws-ui-sidebar-menu-item>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Statistik IT.NRW</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Werkzeuge</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Benutzerverwaltung</span></template
					></svws-ui-sidebar-menu-item
				>
				<svws-ui-sidebar-menu-item
					><template #label
						><span>Hilfe</span></template
					></svws-ui-sidebar-menu-item
				>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schule;

	const menubar_selected: WritableComputedRef<string> = computed({
		get(): string {
			if (main.config.selected_app) {
				return main.config.selected_app;
			}
			return "";
		},
		set(val: string) {
			if (val !== menubar_selected.value && main.config.selected_app) {
				main.config.selected_app = val;
			}
		}
	});
</script>

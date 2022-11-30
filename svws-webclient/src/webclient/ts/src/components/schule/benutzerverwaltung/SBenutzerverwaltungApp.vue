<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal">
				<span>{{ inputBezeichnung }}</span>
				<svws-ui-badge variant="highlight" size="normal">
					{{ anzeigename }}
				</svws-ui-badge>
			</svws-ui-header>
			<svws-ui-tab-bar>
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-benutzerverwaltung-daten />
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();

	const anzeigename: ComputedRef<string | false> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return main.apps.benutzer.auswahl.ausgewaehlt?.anzeigename.toString();
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return main.apps.benutzergruppe.auswahl.ausgewaehlt.bezeichnung.toString();
		return false;
	});

	const inputId: ComputedRef<string | false> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return "ID: " + main.apps.benutzer.auswahl.ausgewaehlt.id;
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return "ID: " + main.apps.benutzergruppe.auswahl.ausgewaehlt.id;
		return false;
	});

	const inputBezeichnung: ComputedRef<string | undefined> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return main.apps.benutzer.dataBenutzer.daten?.anzeigename?.toString();
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return main.apps.benutzergruppe.dataBenutzergruppe.manager?.getBezeichnung().valueOf();
		return "";
	});

</script>

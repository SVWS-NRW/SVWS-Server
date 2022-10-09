<template>
	<div class="flex h-full flex-row">
		<div
			v-if="app.dataBenutzergruppe.daten"
			class="flex w-full flex-col px-4"
		>
			<svws-ui-header
				:badge="inputId"
				badge-variant="light"
				badge-size="normal"
				><span>{{ inputBezeichnung }}</span>
				<svws-ui-badge variant="highlight" size="normal">{{
					anzeigename
				}}</svws-ui-badge>
			</svws-ui-header>
			<svws-ui-tab-bar>
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-benutzergruppe-daten />
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

	const app = main.apps.benutzergruppe;

	const anzeigename: ComputedRef<string | false> = computed(() => {
		if (app.auswahl.ausgewaehlt?.bezeichnung) {
			return app.auswahl.ausgewaehlt.bezeichnung.toString();
		}
		return false;
	});

	const inputId: ComputedRef<string | false> = computed(() => {
		if (app.auswahl.ausgewaehlt) {
			return "ID: " + app.auswahl.ausgewaehlt.id;
		}
		return false;
	});

	const inputBezeichnung: ComputedRef<string | undefined> = computed(() => {
		if (app.auswahl.ausgewaehlt) {
			return app.dataBenutzergruppe.daten?.bezeichnung?.toString();
		}
		return "";
	});
</script>

<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="flex flex-row gap-4">
			<div class="-my-2 flex-none sm:-mx-6 lg:-mx-8">
				<div class="inline-block py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm">
                            <s-kompetenzgruppe v-for="kompetenzgruppe in BenutzerKompetenzGruppe.values()" :key="kompetenzgruppe.daten.id" :kompetenzgruppe="kompetenzgruppe" :istAdmin="istAdmin" :benutzertyp=0>
                            </s-kompetenzgruppe>
                        </table>
                    </div>
                </div>
            </div>
        </div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerManager, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	});

	const istAdmin: ComputedRef<Boolean | undefined> = computed(() => {
		if(app.dataBenutzer.manager?.istAdmin())
			return true;
		else 
			return false;
	});
</script>

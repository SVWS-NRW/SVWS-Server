<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-y-scroll h-screen  shadow-md sm:rounded-lg">
			<table class=" ">
				<tr class="bg-green-100">
					<td colspan="2">Kompetenzen</td>
					<td>durch Gruppe(n)</td>
					<td>
						<svws-ui-icon>
							<i-ri-information-fill />
						</svws-ui-icon>
					</td>
				</tr>
				<s-kompetenzgruppe v-for="kompetenzgruppe in BenutzerKompetenzGruppe.values()" :key="kompetenzgruppe.daten.id" :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" :benutzertyp="0" />
			</table>
		</div>
	</svws-ui-content-card>
	<!-- <svws-ui-table :columns="kompetenzen_cols">
							<template #body>

							</template>
						</svws-ui-table>
	</svws-ui-content-card> -->
</template>

<script setup lang="ts">

	import { BenutzerManager, BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const kompetenzen_cols = [
		{ key: "coll", label: "", sortable: false },
		{ key: "bezeichnung", label: "KOMPETENZEN", sortable: false },
		{ key: "gruppen", label: "durch Gruppe(n)", sortable: false },
		{ key: "direkt", label: "i", sortable: false },

	];

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	});

	const istAdmin: ComputedRef<boolean> = computed(() => {
		if (app.dataBenutzer.manager === undefined)
			return false;
		return app.dataBenutzer.manager.istAdmin();
	});

</script>

<style scoped>
.tooltip {
  position: relative;
  display: inline-block;
  border-bottom: 1px dotted black;
}

.tooltip .tooltiptext {
  visibility: hidden;
  width: 120px;
  background-color: rgb(175, 215, 231);
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;

  /* Position the tooltip */
  position: absolute;
  z-index: 1;
}
</style>
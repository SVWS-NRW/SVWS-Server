<template>
	<svws-ui-content-card title="Kompetenzen">
		<div class="overflow-y-scroll h-screen shadow-md sm:rounded-lg">
			<table class=" ">
				<tr class="bg-green-100">
					<td />
					<td> <svws-ui-icon> <i-ri-information-fill /> </svws-ui-icon> </td>
					<td> Kompetenz / Kompetenzgruppe </td>
					<td>durch Gruppe(n)</td>
				</tr>
				<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :ist-admin="istAdmin" :data="data" />
				</template>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerKompetenzGruppe } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";

	const props = defineProps<{
		data: DataBenutzer;
	}>();

	const istAdmin: ComputedRef<boolean> = computed(() => (props.data.manager === undefined) ? false: props.data.manager.istAdmin());

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

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
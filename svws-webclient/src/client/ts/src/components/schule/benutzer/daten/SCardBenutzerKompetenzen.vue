<template>
	<svws-ui-content-card title="Kompetenzen" class="col-span-full">
		<svws-ui-data-table :items="kompetenzgruppen" :disable-footer="true">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="columnheader"
						 class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								<span class="inline-flex items-center gap-1">
									<i-ri-information-fill />
									<span>Kompetenz/-gruppe durch Gruppe(n)</span>
								</span>
							</span>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<template
					 v-for="(kompetenzgruppe, index) in kompetenzgruppen"
					 :key="index">
					<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :get-benutzer-manager="getBenutzerManager"
												:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
												:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
												:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
				</template>
			</template>
		</svws-ui-data-table>
<!--		<table class=" ">
			<tr class="bg-green-100">
				<td />
				<td> <svws-ui-icon> <i-ri-information-fill /> </svws-ui-icon> </td>
				<td> Kompetenz / Kompetenzgruppe </td>
				<td>durch Gruppe(n)</td>
			</tr>
			&lt;!&ndash; <tr :class="{vorhanden : selected && !aktiviert, nichtvorhanden : !selected && !aktiviert, deaktiviert:aktiviert }"> &ndash;&gt;
			<tr>
				<td />
				<td colspan="2"> <svws-ui-checkbox class="mb-4 " v-model="inputIstAdmin" :disabled="getBenutzerManager().istInAdminGruppe()"> Admin ? </svws-ui-checkbox></td>
				&lt;!&ndash; <td> Admin ?  </td>  &ndash;&gt;
				<td> Kompetenz von </td>
			</tr>
			<template v-for="kompetenzgruppe in kompetenzgruppen" :key="kompetenzgruppe.daten.id">
				<s-benutzer-kompetenzgruppe :kompetenzgruppe="kompetenzgruppe" :get-benutzer-manager="getBenutzerManager"
											:add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
											:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe"
											:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
			</template>
		</table>-->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerManager } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<boolean>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<boolean>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

	const kompetenzgruppen: ComputedRef<BenutzerKompetenzGruppe[]> = computed(() => BenutzerKompetenzGruppe.values().filter(gr => gr.daten.id >= 0));

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === props.getBenutzerManager().istAdmin()))
				return;
			void props.setIstAdmin(value);
		}
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
